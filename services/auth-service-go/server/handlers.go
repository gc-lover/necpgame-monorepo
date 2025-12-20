// Package server Issue: #136
// TODO: Implement handlers when API types are properly generated
package server

import (
	"go.uber.org/zap"
)

// Handlers handles HTTP requests for auth service
type Handlers struct {
	service *AuthService
	logger  *zap.Logger
}

// NewHandlers creates new handlers
