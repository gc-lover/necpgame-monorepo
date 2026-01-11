package server

import (
	"context"
	"log/slog"
	"time"

	"necpgame/services/ai-enemy-coordinator-service-go/pkg/api"
)

// Handler implements the API handlers
type Handler struct {
	service Service
}

// NewHandler creates a new handler instance
func NewHandler(service Service) *Handler {
	return &Handler{
		service: service,
	}
}

// AiEnemyCoordinatorHealthCheck handles health check requests
func (h *Handler) AiEnemyCoordinatorHealthCheck(ctx context.Context) (*api.AiEnemyCoordinatorHealthCheckOK, error) {
	status := api.OptString{}
	status.SetTo("healthy")
	timestamp := api.OptDateTime{}
	timestamp.SetTo(time.Now().UTC())

	resp := &api.AiEnemyCoordinatorHealthCheckOK{
		Status:    status,
		Timestamp: timestamp,
	}

	return resp, nil
}

// SpawnAiEnemy handles AI enemy spawning requests
func (h *Handler) SpawnAiEnemy(ctx context.Context, req *api.AiSpawnRequest) (api.SpawnAiEnemyRes, error) {
	// Call service
	resp, err := h.service.SpawnAiEnemy(ctx, *req)
	if err != nil {
		slog.Error("Failed to spawn AI enemy", "error", err)
		return &api.SpawnAiEnemyBadRequest{}, nil
	}

	return resp, nil
}

// NewAiEnemyCoordinatorServer creates the API server with all handlers
func NewAiEnemyCoordinatorServer(service Service) api.Handler {
	return NewHandler(service)
}