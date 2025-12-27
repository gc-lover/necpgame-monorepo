package server

import (
    "context"
    "time"

    "economy-domain-service-go/pkg/api"
    "go.uber.org/zap"
)

type Handler struct {
    service *Service
    logger  *zap.Logger
}

func NewHandler() *Handler {
    logger, _ := zap.NewProduction()
    return &Handler{
        service: NewService(),
        logger:  logger,
    }
}

// GetEconomyHealth implements the Handler interface for health check
func (h *Handler) GetEconomyHealth(ctx context.Context) (*api.HealthResponse, error) {
    return &api.HealthResponse{
        Status:    api.NewOptString("healthy"),
        Domain:    api.NewOptString("economy-domain"),
        Timestamp: api.NewOptDateTime(time.Now()),
    }, nil
}
