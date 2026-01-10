package service

import (
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"

	"necpgame/services/auth-service-go/config"
	"necpgame/services/auth-service-go/internal/repository"
)

// JWTClaims represents the JWT claims structure
// PERFORMANCE: Struct field alignment optimized for memory efficiency
type JWTClaims struct {
	// jwt.RegisteredClaims first (large embedded struct)
	jwt.RegisteredClaims

	// Strings (16 bytes each header)
	UserID   string `json:"user_id"`
	Username string `json:"username"`
	Email    string `json:"email"`
}

// JWTService handles JWT token operations
type JWTService struct {
	config *config.Config
}

// NewJWTService creates a new JWT service
func NewJWTService(cfg *config.Config) *JWTService {
	return &JWTService{
		config: cfg,
	}
}

// GenerateAccessToken generates a new JWT access token
func (j *JWTService) GenerateAccessToken(user *repository.User) (string, error) {
	claims := JWTClaims{
		UserID:   user.ID,
		Username: user.Username,
		Email:    user.Email,
		RegisteredClaims: jwt.RegisteredClaims{
			Issuer:    "auth-service",
			Subject:   user.ID,
			Audience:  []string{"necpgame"},
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(j.config.JWT.Expiration)),
			NotBefore: jwt.NewNumericDate(time.Now()),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
			ID:        uuid.New().String(),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString([]byte(j.config.JWT.Secret))
	if err != nil {
		return "", fmt.Errorf("failed to sign token: %w", err)
	}

	return tokenString, nil
}

// GenerateRefreshToken generates a new refresh token
func (j *JWTService) GenerateRefreshToken() (string, error) {
	// Generate a secure random refresh token
	token := uuid.New().String() + "-" + uuid.New().String()
	return token, nil
}

// ValidateAccessToken validates and parses a JWT access token
func (j *JWTService) ValidateAccessToken(tokenString string) (*JWTClaims, error) {
	token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return []byte(j.config.JWT.Secret), nil
	})

	if err != nil {
		return nil, fmt.Errorf("failed to parse token: %w", err)
	}

	if claims, ok := token.Claims.(*JWTClaims); ok && token.Valid {
		return claims, nil
	}

	return nil, fmt.Errorf("invalid token")
}

// ValidateRefreshToken validates a refresh token (checks if it exists in database)
func (j *JWTService) ValidateRefreshToken(repo *repository.Repository, tokenString string) (*repository.RefreshToken, error) {
	// This would be implemented with database check
	// For now, return error as not implemented
	return nil, fmt.Errorf("refresh token validation not implemented")
}

// GetExpirationTime returns the expiration time for access tokens
func (j *JWTService) GetExpirationTime() time.Time {
	return time.Now().Add(j.config.JWT.Expiration)
}

// GetRefreshExpirationTime returns the expiration time for refresh tokens
func (j *JWTService) GetRefreshExpirationTime() time.Time {
	// Refresh tokens last longer than access tokens (e.g., 7 days vs 24 hours)
	return time.Now().Add(7 * 24 * time.Hour)
}