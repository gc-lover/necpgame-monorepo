package auth

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"errors"
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"github.com/rs/zerolog"
	"golang.org/x/crypto/bcrypt"

	"security-service-go/internal/config"
	"security-service-go/internal/database"
)

// Service provides authentication and authorization operations
type Service struct {
	db     *database.Service
	config config.JWTConfig
	logger zerolog.Logger
}

// NewService creates a new authentication service
func NewService(db *database.Service, jwtConfig config.JWTConfig, logger zerolog.Logger) *Service {
	return &Service{
		db:     db,
		config: jwtConfig,
		logger: logger,
	}
}

// LoginRequest represents a login request
type LoginRequest struct {
	Username string `json:"username" validate:"required,min=3,max=50"`
	Password string `json:"password" validate:"required,min=8"`
	IPAddress string `json:"ip_address,omitempty"`
	UserAgent string `json:"user_agent,omitempty"`
}

// LoginResponse represents a login response
type LoginResponse struct {
	AccessToken  string    `json:"access_token"`
	RefreshToken string    `json:"refresh_token"`
	TokenType    string    `json:"token_type"`
	ExpiresIn    int       `json:"expires_in"`
	User         *UserInfo `json:"user"`
}

// UserInfo represents user information in responses
type UserInfo struct {
	ID            string   `json:"id"`
	Username      string   `json:"username"`
	Email         string   `json:"email"`
	Roles         []string `json:"roles"`
	Permissions   []string `json:"permissions"`
	LastLogin     *time.Time `json:"last_login,omitempty"`
	SecurityFlags []string `json:"security_flags,omitempty"`
}

// Authenticate authenticates a user and returns tokens
func (s *Service) Authenticate(ctx context.Context, req *LoginRequest) (*LoginResponse, error) {
	// Get user by username
	user, err := s.db.GetUserByUsername(ctx, req.Username)
	if err != nil {
		s.logger.Warn().Str("username", req.Username).Msg("Authentication failed: user not found")
		return nil, errors.New("invalid credentials")
	}

	// Check account status
	if user.AccountStatus != "active" {
		s.logger.Warn().Str("username", req.Username).Str("status", user.AccountStatus).Msg("Authentication failed: account not active")
		return nil, errors.New("account is not active")
	}

	// Verify password
	if err := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(req.Password)); err != nil {
		s.logger.Warn().Str("username", req.Username).Msg("Authentication failed: invalid password")
		return nil, errors.New("invalid credentials")
	}

	// Update last login
	if err := s.db.UpdateUserLastLogin(ctx, user.ID); err != nil {
		s.logger.Error().Err(err).Str("user_id", user.ID).Msg("Failed to update last login")
		// Don't fail authentication for this
	}

	// Generate tokens
	accessToken, err := s.generateAccessToken(user)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", user.ID).Msg("Failed to generate access token")
		return nil, errors.New("authentication failed")
	}

	refreshToken, err := s.generateRefreshToken(user.ID)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", user.ID).Msg("Failed to generate refresh token")
		return nil, errors.New("authentication failed")
	}

	// Store refresh token session
	sessionID := uuid.New().String()
	if err := s.db.StoreSession(ctx, sessionID, user.ID, s.config.RefreshTokenTTL); err != nil {
		s.logger.Error().Err(err).Str("user_id", user.ID).Msg("Failed to store session")
		return nil, errors.New("authentication failed")
	}

	// Determine security flags
	securityFlags := s.determineSecurityFlags(user, req)

	response := &LoginResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    int(s.config.AccessTokenTTL.Seconds()),
		User: &UserInfo{
			ID:            user.ID,
			Username:      user.Username,
			Email:         user.Email,
			Roles:         user.Roles,
			Permissions:   user.Permissions,
			LastLogin:     user.LastLogin,
			SecurityFlags: securityFlags,
		},
	}

	s.logger.Info().
		Str("user_id", user.ID).
		Str("username", user.Username).
		Str("ip", req.IPAddress).
		Msg("User authenticated successfully")

	return response, nil
}

// RefreshTokenRequest represents a token refresh request
type RefreshTokenRequest struct {
	RefreshToken string `json:"refresh_token" validate:"required"`
}

// RefreshToken refreshes an access token using a refresh token
func (s *Service) RefreshToken(ctx context.Context, req *RefreshTokenRequest) (*LoginResponse, error) {
	// Parse and validate refresh token
	token, err := jwt.Parse(req.RefreshToken, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(s.config.Secret), nil
	})

	if err != nil || !token.Valid {
		s.logger.Warn().Msg("Invalid refresh token")
		return nil, errors.New("invalid refresh token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		s.logger.Warn().Msg("Invalid token claims")
		return nil, errors.New("invalid token claims")
	}

	userID, ok := claims["sub"].(string)
	if !ok {
		s.logger.Warn().Msg("Missing user ID in token")
		return nil, errors.New("invalid token")
	}

	sessionID, ok := claims["session_id"].(string)
	if !ok {
		s.logger.Warn().Msg("Missing session ID in token")
		return nil, errors.New("invalid token")
	}

	// Verify session exists
	storedUserID, err := s.db.GetSession(ctx, sessionID)
	if err != nil || storedUserID != userID {
		s.logger.Warn().Str("user_id", userID).Str("session_id", sessionID).Msg("Session not found or invalid")
		return nil, errors.New("invalid session")
	}

	// Get user information
	user, err := s.db.GetUserByID(ctx, userID)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get user for token refresh")
		return nil, errors.New("user not found")
	}

	// Generate new access token
	accessToken, err := s.generateAccessToken(user)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to generate access token during refresh")
		return nil, errors.New("token refresh failed")
	}

	response := &LoginResponse{
		AccessToken: accessToken,
		RefreshToken: req.RefreshToken, // Keep the same refresh token
		TokenType:    "Bearer",
		ExpiresIn:    int(s.config.AccessTokenTTL.Seconds()),
		User: &UserInfo{
			ID:          user.ID,
			Username:    user.Username,
			Email:       user.Email,
			Roles:       user.Roles,
			Permissions: user.Permissions,
			LastLogin:   user.LastLogin,
		},
	}

	s.logger.Info().Str("user_id", userID).Msg("Token refreshed successfully")

	return response, nil
}

// Logout logs out a user by invalidating their session
func (s *Service) Logout(ctx context.Context, accessToken string) error {
	// Parse token to get session ID
	token, err := jwt.Parse(accessToken, func(token *jwt.Token) (interface{}, error) {
		return []byte(s.config.Secret), nil
	})

	if err == nil && token.Valid {
		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			if sessionID, ok := claims["session_id"].(string); ok {
				// Delete session
				if err := s.db.DeleteSession(ctx, sessionID); err != nil {
					s.logger.Error().Err(err).Str("session_id", sessionID).Msg("Failed to delete session")
				}

				// Blacklist access token
				if err := s.db.BlacklistToken(ctx, accessToken, s.config.AccessTokenTTL); err != nil {
					s.logger.Error().Err(err).Str("token", accessToken[:20]+"...").Msg("Failed to blacklist token")
				}
			}
		}
	}

	s.logger.Info().Msg("User logged out")
	return nil
}

// ValidateToken validates an access token and returns user information
func (s *Service) ValidateToken(ctx context.Context, tokenString string) (*UserInfo, error) {
	// Check if token is blacklisted
	if blacklisted, _ := s.db.IsTokenBlacklisted(ctx, tokenString); blacklisted {
		return nil, errors.New("token has been revoked")
	}

	// Parse and validate token
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(s.config.Secret), nil
	})

	if err != nil || !token.Valid {
		return nil, errors.New("invalid token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return nil, errors.New("invalid token claims")
	}

	userID, ok := claims["sub"].(string)
	if !ok {
		return nil, errors.New("invalid token: missing user ID")
	}

	// Get user information
	user, err := s.db.GetUserByID(ctx, userID)
	if err != nil {
		return nil, errors.New("user not found")
	}

	return &UserInfo{
		ID:          user.ID,
		Username:    user.Username,
		Email:       user.Email,
		Roles:       user.Roles,
		Permissions: user.Permissions,
		LastLogin:   user.LastLogin,
	}, nil
}

// GetUserPermissions returns user permissions and roles
func (s *Service) GetUserPermissions(ctx context.Context, userID string) (*UserInfo, error) {
	user, err := s.db.GetUserByID(ctx, userID)
	if err != nil {
		return nil, err
	}

	roles, err := s.db.GetUserRoles(ctx, userID)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get user roles")
		// Continue without roles
	}

	// Combine user permissions with role permissions
	allPermissions := make(map[string]bool)
	for _, perm := range user.Permissions {
		allPermissions[perm] = true
	}

	roleNames := make([]string, len(roles))
	for i, role := range roles {
		roleNames[i] = role.Name
		for _, perm := range role.Permissions {
			allPermissions[perm] = true
		}
	}

	permissions := make([]string, 0, len(allPermissions))
	for perm := range allPermissions {
		permissions = append(permissions, perm)
	}

	return &UserInfo{
		ID:          user.ID,
		Username:    user.Username,
		Email:       user.Email,
		Roles:       roleNames,
		Permissions: permissions,
		LastLogin:   user.LastLogin,
	}, nil
}

// Helper methods

func (s *Service) generateAccessToken(user *database.User) (string, error) {
	sessionID := generateSecureToken()

	claims := jwt.MapClaims{
		"sub":        user.ID,
		"username":   user.Username,
		"email":      user.Email,
		"roles":      user.Roles,
		"permissions": user.Permissions,
		"session_id": sessionID,
		"exp":        time.Now().Add(s.config.AccessTokenTTL).Unix(),
		"iat":        time.Now().Unix(),
		"iss":        s.config.Issuer,
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.config.Secret))
}

func (s *Service) generateRefreshToken(userID string) (string, error) {
	sessionID := generateSecureToken()

	claims := jwt.MapClaims{
		"sub":        userID,
		"session_id": sessionID,
		"exp":        time.Now().Add(s.config.RefreshTokenTTL).Unix(),
		"iat":        time.Now().Unix(),
		"iss":        s.config.Issuer,
		"type":       "refresh",
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.config.Secret))
}

func (s *Service) determineSecurityFlags(user *database.User, req *LoginRequest) []string {
	var flags []string

	// Check if MFA is required but not set up
	if !user.PhoneVerified && !user.EmailVerified {
		flags = append(flags, "mfa_required")
	}

	// Check if password change is required
	if user.LoginCount == 0 {
		flags = append(flags, "password_change_required")
	}

	// Check for suspicious activity (simplified logic)
	if user.LastLogin != nil && time.Since(*user.LastLogin) > 24*time.Hour {
		flags = append(flags, "suspicious_activity")
	}

	// Check for new device (simplified - would need device fingerprinting in real implementation)
	if req.UserAgent != "" {
		flags = append(flags, "new_device")
	}

	return flags
}

func generateSecureToken() string {
	bytes := make([]byte, 32)
	if _, err := rand.Read(bytes); err != nil {
		panic("failed to generate secure token")
	}
	return hex.EncodeToString(bytes)
}

// HashPassword hashes a password using bcrypt
func HashPassword(password string) (string, error) {
	bytes, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(bytes), err
}
