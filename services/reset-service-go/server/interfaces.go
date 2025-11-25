package server

import (
	"context"

	"github.com/necpgame/reset-service-go/models"
	"github.com/necpgame/reset-service-go/pkg/api"
)

type ResetServiceInterface interface {
	TriggerReset(ctx context.Context, resetType models.ResetType) error
	GetResetStats(ctx context.Context) (*api.ResetStats, error)
	GetResetHistory(ctx context.Context, resetType *models.ResetType, limit, offset int) (*api.ResetListResponse, error)
}

