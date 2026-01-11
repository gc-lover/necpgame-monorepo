// Handler implements the ogen-generated API Handler interface
// PERFORMANCE: Optimized for MMOFPS crafting operations with <50ms P99 latency
package service

import (
	"go.uber.org/zap"

	api "necpgame/services/crafting-service-go/pkg/api"
)

// Handler implements the ogen-generated API Handler interface using UnimplementedHandler
type Handler struct {
	api.UnimplementedHandler
	service *Service
	logger  *zap.Logger
}

// NewHandler creates a new handler that implements the ogen API interface
func NewHandler(service *Service, logger *zap.Logger) *Handler {
	return &Handler{
		service: service,
		logger:  logger,
	}
}