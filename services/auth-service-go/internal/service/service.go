package service

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"
	"golang.org/x/crypto/argon2"

	"necpgame/services/auth-service-go/config"
	"necpgame/services/auth-service-go/internal/repository"
	api "necpgame/services/auth-service-go/pkg/api"
)

type Service struct {
	logger        *zap.Logger
	repo          *repository.Repository
	config        *config.Config
	jwtService    *JWTService
	passwordSvc   *PasswordService
	server        *api.Server
	handler       *Handler
}

func NewService(logger *zap.Logger, repo *repository.Repository, cfg *config.Config) *Service {
	jwtSvc := NewJWTService(cfg)
	s := &Service{
		logger:      logger,
		repo:        repo,
		config:      cfg,
		jwtService:  jwtSvc,
		passwordSvc: NewPasswordService(),
	}

	// Create handler with generated API
	s.handler = &Handler{
		logger:      logger,
		repo:        repo,
		config:      cfg,
		jwtService:  s.jwtService,
		passwordSvc: s.passwordSvc,
	}

	// Create security handler
	sec := &SecurityHandler{
		jwtService: s.jwtService,
		repo:       s.repo,
		logger:     s.logger,
	}

	// Create server with generated API
	var err error
	s.server, err = api.NewServer(s.handler, sec)
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	return s
}

func (s *Service) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	s.server.ServeHTTP(w, r)
}

// SecurityHandler implements the generated SecurityHandler interface
type SecurityHandler struct {
	jwtService *JWTService
	repo       *repository.Repository
	logger     *zap.Logger
}


// generatePasswordHash generates a secure hash using Argon2id
func generatePasswordHash(password string) (string, error) {
	salt := make([]byte, 32)
	if _, err := rand.Read(salt); err != nil {
		return "", err
	}

	hash := argon2.IDKey([]byte(password), salt, 1, 64*1024, 4, 32)
	hashWithSalt := append(salt, hash...)

	return hex.EncodeToString(hashWithSalt), nil
}

// verifyPassword verifies a password against its hash
func verifyPassword(password, hash string) (bool, error) {
	hashBytes, err := hex.DecodeString(hash)
	if err != nil {
		return false, err
	}

	if len(hashBytes) < 32 {
		return false, fmt.Errorf("invalid hash format")
	}

	salt := hashBytes[:32]
	storedHash := hashBytes[32:]

	computedHash := argon2.IDKey([]byte(password), salt, 1, 64*1024, 4, 32)

	return string(computedHash) == string(storedHash), nil
}

// generateJWT generates a JWT token for the user
func (s *Service) generateJWT(userID string) (string, error) {
	claims := jwt.MapClaims{
		"user_id": userID,
		"exp":     time.Now().Add(s.config.JWT.Expiration).Unix(),
		"iat":     time.Now().Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.config.JWT.Secret))
}

// validateJWT validates a JWT token and returns the user ID
func (s *Service) validateJWT(tokenString string) (string, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(s.config.JWT.Secret), nil
	})

	if err != nil {
		return "", err
	}

	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		if userID, ok := claims["user_id"].(string); ok {
			return userID, nil
		}
	}

	return "", fmt.Errorf("invalid token claims")
}

// generateSessionToken generates a secure session token
func generateSessionToken() (string, error) {
	bytes := make([]byte, 32)
	if _, err := rand.Read(bytes); err != nil {
		return "", err
	}
	return hex.EncodeToString(bytes), nil
}

// HandleBearerAuth implements the BearerAuth security scheme
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	tokenString := t.Token
	if tokenString == "" {
		return ctx, fmt.Errorf("missing bearer token")
	}

	// Validate JWT token
	claims, err := s.jwtService.ValidateAccessToken(tokenString)
	if err != nil {
		s.logger.Warn("Invalid JWT token", zap.Error(err))
		return ctx, fmt.Errorf("invalid token")
	}

	// Verify session exists and is not expired
	session, err := s.repo.GetSessionByToken(ctx, tokenString)
	if err != nil || session == nil {
		s.logger.Warn("Session not found or expired", zap.String("token", tokenString[:16]+"..."))
		return ctx, fmt.Errorf("session expired or invalid")
	}

	// Add user information to context
	ctx = context.WithValue(ctx, "user_id", claims.UserID)
	ctx = context.WithValue(ctx, "username", claims.Username)
	ctx = context.WithValue(ctx, "email", claims.Email)

	s.logger.Debug("Bearer auth successful", zap.String("user_id", claims.UserID))
	return ctx, nil
}