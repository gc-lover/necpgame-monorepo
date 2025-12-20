// Issue: #136 - Token management operations
package server

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/auth-service-go/pkg/api"
)

// RefreshToken обновляет access token с помощью refresh token
func (s *Service) RefreshToken(ctx context.Context, req *api.RefreshTokenRequest) (*RefreshTokenResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Validate refresh token
	userID, err := s.validateRefreshToken(req.RefreshToken)
	if err != nil {
		return nil, &AuthenticationError{Message: "invalid refresh token"}
	}

	// Generate new tokens
	accessToken, refreshToken, err := s.generateTokens(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to generate tokens", zap.Error(err))
		return nil, err
	}

	// Invalidate old refresh token
	if err := s.invalidateRefreshToken(ctx, req.RefreshToken); err != nil {
		s.logger.Error("Failed to invalidate old refresh token", zap.Error(err))
		// Don't fail the refresh for this
	}

	return &RefreshTokenResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    3600,
	}, nil
}

// ValidateAccessToken валидирует access token и возвращает user ID
func (s *Service) ValidateAccessToken(ctx context.Context, req *api.ValidateTokenRequest) (*api.ValidateTokenResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	userID, err := s.validateAccessToken(req.Token)
	if err != nil {
		return &api.ValidateTokenResponse{
			Valid:   false,
			Message: "invalid token",
		}, nil
	}

	return &api.ValidateTokenResponse{
		Valid:   true,
		UserId:  userID,
		Message: "token valid",
	}, nil
}

// generateTokens генерирует access и refresh токены
func (s *Service) generateTokens(ctx context.Context, userID uuid.UUID) (string, string, error) {
	// Generate access token
	accessClaims := jwt.MapClaims{
		"user_id": userID.String(),
		"type":    "access",
		"exp":     time.Now().Add(time.Hour).Unix(),
		"iat":     time.Now().Unix(),
	}

	accessToken := jwt.NewWithClaims(jwt.SigningMethodHS256, accessClaims)
	accessTokenString, err := accessToken.SignedString([]byte(s.jwtSecret))
	if err != nil {
		return "", "", err
	}

	// Generate refresh token
	refreshTokenBytes := make([]byte, 32)
	if _, err := rand.Read(refreshTokenBytes); err != nil {
		return "", "", err
	}
	refreshTokenString := hex.EncodeToString(refreshTokenBytes)

	// Store refresh token
	if err := s.storeRefreshToken(ctx, userID, refreshTokenString, time.Now().Add(24*time.Hour)); err != nil {
		return "", "", err
	}

	return accessTokenString, refreshTokenString, nil
}

// validateAccessToken валидирует access token
func (s *Service) validateAccessToken(tokenString string) (uuid.UUID, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, jwt.ErrSignatureInvalid
		}
		return []byte(s.jwtSecret), nil
	})

	if err != nil {
		return uuid.Nil, err
	}

	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		if userIDStr, ok := claims["user_id"].(string); ok {
			userID, err := uuid.Parse(userIDStr)
			if err != nil {
				return uuid.Nil, err
			}
			return userID, nil
		}
	}

	return uuid.Nil, jwt.ErrInvalidKey
}

// validateRefreshToken валидирует refresh token
func (s *Service) validateRefreshToken(tokenString string) (uuid.UUID, error) {
	return s.getUserIDFromRefreshToken(context.Background(), tokenString)
}

// invalidateRefreshToken инвалидирует refresh token
func (s *Service) invalidateRefreshToken(ctx context.Context, tokenString string) error {
	return s.deleteRefreshToken(ctx, tokenString)
}
