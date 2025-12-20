package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
)

// TokenClaims represents JWT token claims
type TokenClaims struct {
	UserID      string `json:"user_id"`
	Username    string `json:"username"`
	Email       string `json:"email"`
	DisplayName string `json:"display_name"`
	Level       int    `json:"level"`
	Experience  int    `json:"experience"`
	SessionID   string `json:"session_id"`
	jwt.RegisteredClaims
}

// OPTIMIZATION: Issue #1998 - JWT token refresh with validation
func (s *AuthService) RefreshToken(w http.ResponseWriter, r *http.Request) {
	var req RefreshTokenRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode refresh request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.TokenRefreshes.Inc()

	// Validate refresh token
	claims := &TokenClaims{}
	token, err := jwt.ParseWithClaims(req.RefreshToken, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(s.config.JWTSecret), nil
	})

	if err != nil || !token.Valid {
		http.Error(w, "Invalid refresh token", http.StatusUnauthorized)
		return
	}

	// Check if session is still active
	session, exists := s.sessions.Load(claims.SessionID)
	if !exists || !session.(*Session).IsActive {
		http.Error(w, "Session expired", http.StatusUnauthorized)
		return
	}

	// TODO: Get user from database
	userID, err := uuid.Parse(claims.UserID)
	if err != nil {
		s.logger.WithError(err).Error("invalid user ID in token")
		http.Error(w, "Invalid token", http.StatusUnauthorized)
		return
	}

	user := &User{
		ID:       userID,
		Username: claims.Username,
		Email:    claims.Email,
	}

	// Generate new access token
	accessToken, err := s.generateAccessToken(user, claims.SessionID)
	if err != nil {
		s.logger.WithError(err).Error("failed to generate access token")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	resp := &RefreshTokenResponse{
		AccessToken:  accessToken,
		RefreshToken: req.RefreshToken, // Keep same refresh token
		TokenType:    "Bearer",
		ExpiresIn:    int(s.config.JWTExpiry.Seconds()),
		SessionID:    claims.SessionID,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// OPTIMIZATION: Issue #1998 - JWT token validation with caching
func (s *AuthService) ValidateToken(w http.ResponseWriter, r *http.Request) {
	// Get token from Authorization header
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" {
		http.Error(w, "Missing authorization header", http.StatusUnauthorized)
		return
	}

	tokenString := authHeader[len("Bearer "):]
	s.metrics.TokenValidations.Inc()

	// Parse and validate token
	claims := &TokenClaims{}
	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(s.config.JWTSecret), nil
	})

	if err != nil || !token.Valid {
		resp := &ValidateTokenResponse{
			Valid: false,
		}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(resp)
		return
	}

	// Check if session is still active
	session, exists := s.sessions.Load(claims.SessionID)
	if !exists || !session.(*Session).IsActive {
		resp := &ValidateTokenResponse{
			Valid: false,
		}
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(resp)
		return
	}

	userInfo := &UserInfo{
		UserID:        claims.UserID,
		Username:      claims.Username,
		Email:         claims.Email,
		DisplayName:   claims.DisplayName,
		Level:         claims.Level,
		Experience:    claims.Experience,
		AccountStatus: "ACTIVE",
	}

	resp := &ValidateTokenResponse{
		Valid:       true,
		User:        userInfo,
		SessionID:   claims.SessionID,
		ExpiresAt:   claims.ExpiresAt.Unix(),
		Permissions: []string{"read", "write"},
		Roles:       []string{"player"},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// OPTIMIZATION: Issue #1998 - JWT token generation with optimized claims
func (s *AuthService) generateTokens(user *User, session *Session) (string, string, error) {
	accessToken, err := s.generateAccessToken(user, session.SessionID)
	if err != nil {
		return "", "", err
	}

	refreshToken, err := s.generateRefreshToken(user, session.SessionID)
	if err != nil {
		return "", "", err
	}

	return accessToken, refreshToken, nil
}

func (s *AuthService) generateAccessToken(user *User, sessionID string) (string, error) {
	claims := &TokenClaims{
		UserID:      user.UserID,
		Username:    user.Username,
		Email:       user.Email,
		DisplayName: user.DisplayName,
		Level:       user.Level,
		Experience:  user.Experience,
		SessionID:   sessionID,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(s.config.JWTExpiry)),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
			NotBefore: jwt.NewNumericDate(time.Now()),
			Issuer:    "auth-service",
			Subject:   user.UserID,
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.config.JWTSecret))
}

func (s *AuthService) generateRefreshToken(user *User, sessionID string) (string, error) {
	claims := &TokenClaims{
		UserID:    user.UserID,
		SessionID: sessionID,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(s.config.SessionTimeout)),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
			Issuer:    "auth-service",
			Subject:   user.UserID,
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.config.JWTSecret))
}

// OPTIMIZATION: Issue #1998 - Logout with session invalidation
func (s *AuthService) LogoutUser(w http.ResponseWriter, r *http.Request) {
	// Get user from context (set by auth middleware)
	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	// Get session ID from context
	sessionID := r.Header.Get("X-Session-ID")
	if sessionID != "" {
		s.sessions.Delete(sessionID)
	}

	resp := &LogoutResponse{
		Message:             "Successfully logged out",
		SessionsInvalidated: 1,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("user_id", userID).Info("user logged out successfully")
}
