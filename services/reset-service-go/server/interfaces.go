package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/models"
)

type ResetServiceInterface interface {
	TriggerReset(ctx context.Context, resetType models.ResetType) error
	GetResetStats(ctx context.Context) (*models.ResetStats, error)
	GetResetHistory(ctx context.Context, resetType *models.ResetType, limit, offset int) (*models.ResetListResponse, error)
}
