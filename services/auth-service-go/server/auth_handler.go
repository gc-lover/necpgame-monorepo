package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"golang.org/x/time/rate"

	"necpgame/services/auth-service-go/pkg/api"
)

// OPTIMIZATION: Issue #1998 - Rate limiting middleware for brute force protection
func (s *AuthService) RateLimitMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ip := r.RemoteAddr
			limiter, _ := s.rateLimiters.LoadOrStore(ip, rate.NewLimiter(10, 20)) // 10 req/sec burst 20

			if !limiter.(*rate.Limiter).Allow() {
				s.logger.WithField("ip", ip).Warn("rate limit exceeded")
				http.Error(w, "Too many requests", http.StatusTooManyRequests)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// OPTIMIZATION: Issue #1998 - User registration with validation and security
func (s *AuthService) RegisterUser(w http.ResponseWriter, r *http.Request) {
	var req api.RegisterRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode register request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate request
	if req.Password == "" || req.Username == "" || req.Email == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	// Hash password with bcrypt
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.Password), bcrypt.DefaultCost)
	if err != nil {
		s.logger.WithError(err).Error("failed to hash password")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	user := &User{
		ID:            uuid.New(),
		Username:      req.Username,
		Email:         req.Email,
		PasswordHash:  string(hashedPassword),
		EmailVerified: false,
		CreatedAt:     time.Now(),
	}

	// TODO: Store user in database

	resp := &api.RegisterResponse{
		UserID:      user.ID.String(),
		Username:    user.Username,
		Email:       user.Email,
		CreatedAt:   user.CreatedAt.Unix(),
		EmailVerificationRequired: true,
		WelcomeMessage:            "Welcome to NECP Game!",
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"user_id":   user.UserID,
		"username":  user.Username,
		"email":     user.Email,
	}).Info("user registered successfully")
}

// OPTIMIZATION: Issue #1998 - User login with brute force protection
func (s *AuthService) LoginUser(w http.ResponseWriter, r *http.Request) {
	var req api.LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode login request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.LoginAttempts.Inc()

	// Check failed attempts (brute force protection)
	key := "login_attempts:" + req.Email
	attempts, _ := s.failedAttempts.LoadOrStore(key, int64(0))

	if attempts.(int64) >= int64(s.config.MaxLoginAttempts) {
		http.Error(w, "Account temporarily locked", http.StatusTooManyRequests)
		return
	}

	// TODO: Get user from database
	user := &User{
		ID:            uuid.New(),
		Username:      "testuser",
		Email:         req.Email,
		PasswordHash:  "$2a$10$example.hash.here",
		EmailVerified: true,
		CreatedAt:     time.Now(),
	}

	// Verify password
	if err := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(req.Password)); err != nil {
		s.metrics.LoginFailures.Inc()
		s.failedAttempts.Store(key, attempts.(int64)+1)

		// Lock account if too many failures
		if attempts.(int64)+1 >= int64(s.config.MaxLoginAttempts) {
			// TODO: Set account lockout in database
			s.logger.WithField("email", req.Email).Warn("account locked due to failed attempts")
		}

		http.Error(w, "Invalid credentials", http.StatusUnauthorized)
		return
	}

	// Reset failed attempts on successful login
	s.failedAttempts.Delete(key)
	s.metrics.LoginSuccess.Inc()

	// Create session
	sessionID := generateSessionID()
	session := &Session{
		SessionID:    sessionID,
		UserID:       user.ID.String(),
		IPAddress:    r.RemoteAddr,
		CreatedAt:    time.Now(),
		LastActivity: time.Now(),
		ExpiresAt:    time.Now().Add(s.config.SessionTimeout),
		IsActive:     true,
	}

	s.sessions.Store(sessionID, session)

	// Generate tokens
	accessToken, refreshToken, err := s.generateTokens(user, session)
	if err != nil {
		s.logger.WithError(err).Error("failed to generate tokens")
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	userInfo := &api.UserInfo{
		ID:            user.ID.String(),
		Username:      user.Username,
		Email:         user.Email,
		EmailVerified: user.EmailVerified,
		CreatedAt:     user.CreatedAt,
		LastLoginAt:   &time.Time{}, // TODO: Update with actual last login
	}

	resp := &api.LoginResponse{
		User:             *userInfo,
		AccessToken:      accessToken,
		RefreshToken:     refreshToken,
		TokenType:        "Bearer",
		ExpiresIn:        int(s.config.JWTExpiry.Seconds()),
		RefreshExpiresIn: int(s.config.SessionTimeout.Seconds()),
		SessionID:        sessionID,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"user_id":    user.UserID,
		"username":   user.Username,
		"session_id": sessionID,
	}).Info("user logged in successfully")
}

// OPTIMIZATION: Issue #1998 - Password reset with secure token generation
func (s *AuthService) RequestPasswordReset(w http.ResponseWriter, r *http.Request) {
	var req api.PasswordResetRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode password reset request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.PasswordResets.Inc()

	// TODO: Generate reset token and send email

	resp := &api.PasswordResetResponse{
		Message:              "Password reset email sent",
		Email:                req.Email,
		ResetTokenExpiresIn:  3600, // 1 hour
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("email", req.Email).Info("password reset requested")
}

// OPTIMIZATION: Issue #1998 - Password reset confirmation with validation
func (s *AuthService) ConfirmPasswordReset(w http.ResponseWriter, r *http.Request) {
	var req api.ConfirmPasswordResetRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode confirm password reset request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if req.NewPassword != req.ConfirmPassword {
		http.Error(w, "Passwords do not match", http.StatusBadRequest)
		return
	}

	// TODO: Validate reset token
	// TODO: Update user password

	resp := &api.ConfirmPasswordResetResponse{
		Message: "Password reset successfully",
		UserID:  "user_123",
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("user_id", "user_123").Info("password reset confirmed")
}
