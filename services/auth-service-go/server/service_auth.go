// Issue: #136 - Authentication operations (login, register, logout)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
	"golang.org/x/crypto/bcrypt"

	"github.com/NECPGAME/auth-service-go/pkg/api"
)

// Register регистрирует нового пользователя
func (s *Service) Register(ctx context.Context, req *api.RegisterRequest) (*RegisterResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	// Validate input
	if req.Email == "" || req.Username == "" || req.Password == "" {
		return nil, &ValidationError{Field: "email/username/password", Message: "required fields missing"}
	}

	if len(req.Password) < 8 {
		return nil, &ValidationError{Field: "password", Message: "password must be at least 8 characters"}
	}

	// Check if user already exists
	exists, err := s.userExists(ctx, req.Email, req.Username)
	if err != nil {
		s.logger.Error("Failed to check user existence", zap.Error(err))
		return nil, err
	}

	if exists {
		return nil, &ConflictError{Message: "user with this email or username already exists"}
	}

	// Hash password
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.Password), bcrypt.DefaultCost)
	if err != nil {
		s.logger.Error("Failed to hash password", zap.Error(err))
		return nil, err
	}

	// Create user
	userID := uuid.New()
	now := time.Now()

	user := &User{
		ID:        userID,
		Email:     req.Email,
		Username:  req.Username,
		Password:  string(hashedPassword),
		Status:    "pending_verification",
		CreatedAt: now,
		UpdatedAt: now,
	}

	if err := s.createUser(ctx, user); err != nil {
		s.logger.Error("Failed to create user", zap.Error(err))
		return nil, err
	}

	// Assign default role
	if err := s.assignRole(ctx, userID, "user"); err != nil {
		s.logger.Error("Failed to assign default role", zap.Error(err))
		// Don't fail registration for role assignment failure
	}

	return &RegisterResponse{
		UserId:               userID,
		Email:                req.Email,
		Username:             req.Username,
		VerificationRequired: true,
		Message:              "User registered successfully. Please verify your email.",
	}, nil
}

// Login аутентифицирует пользователя
func (s *Service) Login(ctx context.Context, req *api.LoginRequest) (*LoginResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	// Find user
	user, err := s.getUserByEmail(ctx, req.Email)
	if err != nil {
		s.logger.Error("Failed to get user", zap.Error(err))
		return nil, &AuthenticationError{Message: "invalid credentials"}
	}

	if user == nil {
		return nil, &AuthenticationError{Message: "invalid credentials"}
	}

	// Check password
	if err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(req.Password)); err != nil {
		return nil, &AuthenticationError{Message: "invalid credentials"}
	}

	// Check user status
	if user.Status != "active" && user.Status != "verified" {
		return nil, &AuthenticationError{Message: "account not verified or suspended"}
	}

	// Generate tokens
	accessToken, refreshToken, err := s.generateTokens(ctx, user.ID)
	if err != nil {
		s.logger.Error("Failed to generate tokens", zap.Error(err))
		return nil, err
	}

	// Get user info
	userInfo, err := s.getUserInfo(ctx, user.ID)
	if err != nil {
		s.logger.Error("Failed to get user info", zap.Error(err))
		return nil, err
	}

	return &LoginResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    3600, // 1 hour
		User:         userInfo,
	}, nil
}

// Logout инвалидирует сессию пользователя
func (s *Service) Logout(ctx context.Context, req *api.LogoutRequest) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	userID, err := s.validateAccessToken(req.AccessToken)
	if err != nil {
		return &AuthenticationError{Message: "invalid token"}
	}

	// Add token to blacklist (optional)
	if err := s.blacklistToken(ctx, req.AccessToken, time.Hour); err != nil {
		s.logger.Error("Failed to blacklist token", zap.Error(err))
		// Don't fail logout for blacklist failure
	}

	s.logger.Info("User logged out", zap.String("user_id", userID.String()))
	return nil
}
