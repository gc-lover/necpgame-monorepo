// Issue: #39, #1607 - Sandevistan service interface and constructor
// Implementation split across multiple files for better maintainability:
// - service_activation.go: Activation/deactivation operations
// - service_status.go: Status and bonus operations
// - service_action_budget.go: Action budget operations
// - service_temporal_marks.go: Temporal marks operations
// - service_heat_cooling.go: Heat and cooling operations
// - service_counterplay.go: Counterplay and perception drag operations
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

const (
	PhasePreparation = "preparation"
	PhaseActive      = "active"
	PhaseRecovery    = "recovery"
	PhaseIdle        = "idle"

	PreparationDuration = 300 * time.Millisecond
	ActiveDuration      = 4 * time.Second
	RecoveryDuration    = 6 * time.Second

	MaxActionBudget     = 100
	MaxActionsPerTick   = 3
	MaxTemporalMarks    = 3
	MaxHeatStacks       = 4
	OverstressThreshold = 4
)

type SandevistanService interface {
	Activate(ctx context.Context, playerID uuid.UUID) (*api.SandevistanActivation, error)
	Deactivate(ctx context.Context, playerID uuid.UUID) error
	GetStatus(ctx context.Context, playerID uuid.UUID) (*api.SandevistanStatus, error)
	UseActionBudget(ctx context.Context, playerID uuid.UUID, actions []api.Action) (*api.ActionBudgetResult, error)
	SetTemporalMarks(ctx context.Context, playerID uuid.UUID, targetIDs []uuid.UUID) error
	GetTemporalMarks(ctx context.Context, playerID uuid.UUID) ([]api.TemporalMark, error)
	ApplyCooling(ctx context.Context, playerID uuid.UUID, cartridgeID uuid.UUID) (*api.CoolingResult, error)
	GetHeatStatus(ctx context.Context, playerID uuid.UUID) (*api.HeatStatus, error)
	ApplyCounterplay(ctx context.Context, playerID uuid.UUID, effectType string, sourcePlayerID uuid.UUID) (*api.CounterplayResult, error)
	ApplyTemporalMarks(ctx context.Context, playerID uuid.UUID) (*api.TemporalMarksApplied, error)
	GetBonuses(ctx context.Context, playerID uuid.UUID) (*api.SandevistanBonuses, error)
	PublishPerceptionDragEvent(ctx context.Context, playerID uuid.UUID, event *api.PerceptionDragEvent) error
}

type sandevistanService struct {
	repo   Repository
	logger *logrus.Logger

	// Add mutex for thread-safe operations
	mu sync.RWMutex
}

// NewSandevistanService creates a new sandevistan service
func NewSandevistanService(repo Repository, logger *logrus.Logger) SandevistanService {
	return &sandevistanService{
		repo:   repo,
		logger: logger,
	}
}
