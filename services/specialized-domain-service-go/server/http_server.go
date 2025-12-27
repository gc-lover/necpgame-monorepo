// Issue: #backend-specialized_domain
// SECURITY: JWT authentication, rate limiting, CORS

package server

import (
	"context"
	"net/http"
	"strings"
	"time"

	"github.com/golang-jwt/jwt/v4"
	"specialized-domain-service-go/pkg/api"
)

// JWTClaims represents JWT token claims
type JWTClaims struct {
	UserID   string `json:"user_id"`
	Username string `json:"username"`
	Role     string `json:"role"`
	jwt.RegisteredClaims
}

// SecurityMiddleware provides authentication and authorization
type SecurityMiddleware struct {
	jwtSecret []byte
}

// NewSecurityMiddleware creates a new security middleware
func NewSecurityMiddleware(jwtSecret string) *SecurityMiddleware {
	return &SecurityMiddleware{
		jwtSecret: []byte(jwtSecret),
	}
}

// Authenticate validates JWT token and sets user context
func (sm *SecurityMiddleware) Authenticate(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Extract token from Authorization header
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			http.Error(w, "Missing authorization header", http.StatusUnauthorized)
			return
		}

		// Check Bearer token format
		if !strings.HasPrefix(authHeader, "Bearer ") {
			http.Error(w, "Invalid authorization format", http.StatusUnauthorized)
			return
		}

		tokenString := strings.TrimPrefix(authHeader, "Bearer ")

		// Parse and validate token
		token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
			return sm.jwtSecret, nil
		})

		if err != nil || !token.Valid {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		claims, ok := token.Claims.(*JWTClaims)
		if !ok {
			http.Error(w, "Invalid token claims", http.StatusUnauthorized)
			return
		}

		// Check token expiration
		if claims.ExpiresAt != nil && claims.ExpiresAt.Before(time.Now()) {
			http.Error(w, "Token expired", http.StatusUnauthorized)
			return
		}

		// Add user info to request context
		ctx := context.WithValue(r.Context(), "user_id", claims.UserID)
		ctx = context.WithValue(ctx, "username", claims.Username)
		ctx = context.WithValue(ctx, "role", claims.Role)

		// Call next handler with updated context
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}


type SpecializeddomainService struct {
	api *api.Server
}

// NewSpecializeddomainService creates a new service with security middleware
func NewSpecializeddomainService() *SpecializeddomainService {
	handler := &Handler{}

	// SECURITY: Apply middleware chain
	security := NewSecurityMiddleware("your-jwt-secret-key-change-in-production")
	mux := CORSMiddleware(RateLimitMiddleware(security.Authenticate(handler)))

	server, _ := api.NewServer(mux, nil)

	return &SpecializeddomainService{
		api: server,
	}
}

func (s *SpecializeddomainService) Handler() http.Handler {
	return s.api
}
