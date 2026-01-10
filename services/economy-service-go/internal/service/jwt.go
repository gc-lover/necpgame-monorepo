package service

import (
	"fmt"
	"time"

	"github.com/golang-jwt/jwt/v5"

	"necpgame/services/economy-service-go/config"
)

// JWTClaims represents the JWT claims structure for economy service
type JWTClaims struct {
	UserID   string `json:"user_id"`
	Username string `json:"username"`
	Email    string `json:"email"`
	jwt.RegisteredClaims
}

// JWTService handles JWT token operations for economy service
type JWTService struct {
	config *config.Config
}

// NewJWTService creates a new JWT service
func NewJWTService(cfg *config.Config) *JWTService {
	return &JWTService{
		config: cfg,
	}
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

// GetExpirationTime returns the expiration time for access tokens
func (j *JWTService) GetExpirationTime() time.Time {
	return time.Now().Add(j.config.JWT.Expiration)
}