package service

import (
	"context"

	"go.uber.org/zap"

	api "necpgame/services/world-event-service-go/api"
	"necpgame/services/world-event-service-go/internal/repository"
)

// Handler implements api.Handler.
type Handler struct {
	api.UnimplementedHandler // Embed to get default implementations
	logger                   *zap.Logger
	repo                     *repository.Repository
}

// NewHandler creates a new handler.
func NewHandler(logger *zap.Logger, repo *repository.Repository) *Handler {
	return &Handler{
		logger: logger,
		repo:   repo,
	}
}

// NewError implements api.Handler.NewError.
func (h *Handler) NewError(ctx context.Context, err error) *api.ErrorStatusCode {
	return &api.ErrorStatusCode{
		StatusCode: 500,
		Response: api.Error{
			Code:    "500",
			Message: err.Error(),
		},
	}
}

// SecurityHandler implements api.SecurityHandler.
type SecurityHandler struct{}

// NewSecurityHandler creates a new security handler.
func NewSecurityHandler() *SecurityHandler {
	return &SecurityHandler{}
}

// HandleBearerAuth implements api.SecurityHandler.HandleBearerAuth.
func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Mock authentication
	return ctx, nil
}
