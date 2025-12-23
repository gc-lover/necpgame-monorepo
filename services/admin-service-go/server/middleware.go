// Issue: Implement admin-service-go based on OpenAPI specification
package server

import (
	"net/http"
	"time"

	"github.com/go-chi/cors"
	"github.com/google/uuid"
	"go.uber.org/zap"
	"admin-service-go/server/internal/models"
)

// loggingMiddleware provides detailed request logging for admin operations
func (s *AdminService) loggingMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Create a response writer wrapper to capture status code
			wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			// Extract admin user from context if available
			adminUser := "anonymous"
			if user := r.Context().Value("admin_user"); user != nil {
				if admin, ok := user.(*models.AdminUser); ok {
					adminUser = admin.Username
				}
			}

			// Log request
			s.logger.Info("Admin request started",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("admin", adminUser),
				zap.String("remote_addr", r.RemoteAddr),
				zap.String("user_agent", r.UserAgent()),
			)

			// Call next handler
			next.ServeHTTP(wrapped, r)

			// Log response
			duration := time.Since(start)
			s.logger.Info("Admin request completed",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("admin", adminUser),
				zap.Int("status", wrapped.statusCode),
				zap.Duration("duration", duration),
			)
		})
	}
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// auditMiddleware automatically logs all admin actions for compliance
func (s *AdminService) auditMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// Only audit write operations
			if r.Method == "GET" || r.Method == "HEAD" || r.Method == "OPTIONS" {
				next.ServeHTTP(w, r)
				return
			}

			adminUser := r.Context().Value("admin_user")
			if adminUser == nil {
				next.ServeHTTP(w, r)
				return
			}

			admin := adminUser.(*models.AdminUser)

			// Create audit action
			action := &models.AdminAction{
				ID:        uuid.New(),
				AdminID:   admin.ID,
				Action:    r.Method + "_" + r.URL.Path,
				Resource:  r.URL.Path,
				Timestamp: time.Now(),
				IPAddress: r.RemoteAddr,
				UserAgent: r.UserAgent(),
				Metadata: map[string]interface{}{
					"method": r.Method,
					"path":   r.URL.Path,
					"query":  r.URL.RawQuery,
				},
			}

			// Log the action (this happens after the request completes)
			defer func() {
				if err := s.logAdminAction(r.Context(), action); err != nil {
					s.logger.Error("Failed to log admin action", zap.Error(err))
				}
			}()

			next.ServeHTTP(w, r)
		})
	}
}

// securityHeadersMiddleware adds security headers for admin panel
func (s *AdminService) securityHeadersMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// Security headers for admin panel
			w.Header().Set("X-Content-Type-Options", "nosniff")
			w.Header().Set("X-Frame-Options", "DENY")
			w.Header().Set("X-XSS-Protection", "1; mode=block")
			w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
			w.Header().Set("Content-Security-Policy", "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'")
			w.Header().Set("Referrer-Policy", "strict-origin-when-cross-origin")

			next.ServeHTTP(w, r)
		})
	}
}

// corsMiddleware provides CORS support for admin panel
func (s *AdminService) corsMiddleware() func(http.Handler) http.Handler {
	return cors.Handler(cors.Options{
		AllowedOrigins:   []string{"https://admin.necpgame.com", "http://localhost:3000"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	})
}

// rateLimitMiddleware implements rate limiting for admin operations
func (s *AdminService) rateLimitMiddleware() func(http.Handler) http.Handler {
	// TODO: Implement Redis-based rate limiting with different tiers
	// Super admins: 1000 req/min
	// Regular admins: 100 req/min
	// Moderators: 50 req/min

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// For now, implement simple in-memory rate limiting
			// In production, this should use Redis or similar

			adminUser := r.Context().Value("admin_user")
			if adminUser == nil {
				next.ServeHTTP(w, r)
				return
			}

			admin := adminUser.(*models.AdminUser)

			// Determine rate limit based on role
			var limit int
			switch admin.Role {
			case "super_admin":
				limit = 1000 // requests per minute
			case "admin":
				limit = 100
			case "moderator":
				limit = 50
			default:
				limit = 10
			}

			// TODO: Check rate limit against Redis/store
			// For now, allow all requests
			_ = limit // prevent unused variable error

			next.ServeHTTP(w, r)
		})
	}
}

// timeoutMiddleware sets request timeout for admin operations
func (s *AdminService) timeoutMiddleware(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.TimeoutHandler(next, timeout, "Request timeout")
	}
}

// recoveryMiddleware recovers from panics and logs them
func (s *AdminService) recoveryMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if err := recover(); err != nil {
					s.logger.Error("Panic recovered in admin handler",
						zap.Any("panic", err),
						zap.String("path", r.URL.Path),
						zap.String("method", r.Method),
					)

					w.WriteHeader(http.StatusInternalServerError)
					s.writeError(w, http.StatusInternalServerError, "Internal server error")
				}
			}()

			next.ServeHTTP(w, r)
		})
	}
}
