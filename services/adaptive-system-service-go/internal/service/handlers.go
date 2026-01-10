package service

import (
	"context"
	"fmt"
	"time"

	"go.uber.org/zap"

	api "necpgame/services/adaptive-system-service-go"
)

// Handler implements the generated Handler interface
type Handler struct {
	logger  *zap.Logger
	service *Service
}

// NewHandler creates a new API handler
func NewHandler(logger *zap.Logger, svc *Service) *Handler {
	return &Handler{
		logger:  logger,
		service: svc,
	}
}

// AdaptiveSystemServiceHealthCheck implements health check endpoint
func (h *Handler) AdaptiveSystemServiceHealthCheck(ctx context.Context) (*api.HealthResponse, error) {
	h.logger.Info("Adaptive system health check requested")

	healthy := true
	if err := h.service.HealthCheck(ctx); err != nil {
		healthy = false
		h.logger.Warn("Health check failed", zap.Error(err))
	}

	var status string
	if healthy {
		status = "ok"
	} else {
		status = "error"
	}

	return &api.HealthResponse{
		Status:    status,
		Timestamp: time.Now(),
	}, nil
}

// NewError implements the NewError method required by the Handler interface
func (h *Handler) NewError(ctx context.Context, err error) *api.ErrorStatusCode {
	return &api.ErrorStatusCode{
		StatusCode: 500,
		Response: api.Error{
			Code:    500,
			Message: err.Error(),
		},
	}
}

// Security handler for JWT authentication
type SecurityHandler struct {
	service *Service
}

func NewSecurityHandler(svc *Service) *SecurityHandler {
	return &SecurityHandler{service: svc}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Basic JWT validation - in production this would validate the token properly
	if t.Token == "" {
		return ctx, fmt.Errorf("missing bearer token")
	}

	// For now, accept any non-empty token
	// In production, implement proper JWT validation here

	return ctx, nil
}