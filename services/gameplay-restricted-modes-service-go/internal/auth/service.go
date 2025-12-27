// Issue: #1499
package auth

import (
	"context"
	"errors"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/rs/zerolog"

	"gameplay-restricted-modes-service-go/internal/config"
)

// Service provides authentication and authorization operations
type Service struct {
	config config.JWTConfig
	logger zerolog.Logger
}

// NewService creates a new authentication service
func NewService(jwtConfig config.JWTConfig, logger zerolog.Logger) *Service {
	return &Service{
		config: jwtConfig,
		logger: logger,
	}
}

// ValidateToken validates a JWT token and extracts user information
func (s *Service) ValidateToken(ctx context.Context, tokenString string) (map[string]interface{}, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, errors.New("unexpected signing method")
		}
		return []byte(s.config.Secret), nil
	})

	if err != nil {
		s.logger.Warn().Err(err).Msg("Token parsing failed")
		return nil, errors.New("invalid token")
	}

	if !token.Valid {
		return nil, errors.New("token is not valid")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return nil, errors.New("invalid token claims")
	}

	// Check expiration
	if exp, ok := claims["exp"].(float64); ok {
		if time.Now().Unix() > int64(exp) {
			return nil, errors.New("token has expired")
		}
	}

	// Extract user information
	userInfo := map[string]interface{}{
		"user_id": claims["sub"],
		"roles":   claims["roles"],
	}

	if username, ok := claims["username"]; ok {
		userInfo["username"] = username
	}

	if permissions, ok := claims["permissions"]; ok {
		userInfo["permissions"] = permissions
	}

	return userInfo, nil
}

// ExtractUserID extracts user ID from token
func (s *Service) ExtractUserID(ctx context.Context, tokenString string) (string, error) {
	userInfo, err := s.ValidateToken(ctx, tokenString)
	if err != nil {
		return "", err
	}

	if userID, ok := userInfo["user_id"].(string); ok {
		return userID, nil
	}

	return "", errors.New("user ID not found in token")
}

// HasPermission checks if the user has a specific permission
func (s *Service) HasPermission(ctx context.Context, tokenString, requiredPermission string) (bool, error) {
	userInfo, err := s.ValidateToken(ctx, tokenString)
	if err != nil {
		return false, err
	}

	if permissions, ok := userInfo["permissions"].([]interface{}); ok {
		for _, perm := range permissions {
			if permStr, ok := perm.(string); ok && permStr == requiredPermission {
				return true, nil
			}
		}
	}

	return false, nil
}

// HasRole checks if the user has a specific role
func (s *Service) HasRole(ctx context.Context, tokenString, requiredRole string) (bool, error) {
	userInfo, err := s.ValidateToken(ctx, tokenString)
	if err != nil {
		return false, err
	}

	if roles, ok := userInfo["roles"].([]interface{}); ok {
		for _, role := range roles {
			if roleStr, ok := role.(string); ok && roleStr == requiredRole {
				return true, nil
			}
		}
	}

	return false, nil
}

// GetUserInfo returns complete user information from token
func (s *Service) GetUserInfo(ctx context.Context, tokenString string) (map[string]interface{}, error) {
	return s.ValidateToken(ctx, tokenString)
}




