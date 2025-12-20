// Package server Issue: #156 - Abilities repository interface and constructor
// Implementation split across multiple files for better maintainability:
// - abilities_repository_catalog.go: Catalog operations
// - abilities_repository_loadout.go: Loadout operations
// - abilities_repository_cooldown.go: Cooldown operations
// - abilities_repository_synergy.go: Synergy operations
// - abilities_repository_cyberpsychosis.go: Cyberpsychosis operations
// - abilities_repository_metrics.go: Metrics operations
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

type AbilityRepositoryInterface interface {
	GetCatalog(ctx context.Context, abilityType *api.AbilityType, slot *api.AbilitySlot, source *api.AbilitySource, limit, offset int) ([]api.Ability, int, error)
	GetAbility(ctx context.Context, abilityID uuid.UUID) (*api.Ability, error)
	GetLoadout(ctx context.Context, characterID uuid.UUID) (*api.AbilityLoadout, error)
	SaveLoadout(ctx context.Context, loadout *api.AbilityLoadout) (*api.AbilityLoadout, error)
	GetCooldowns(ctx context.Context, characterID uuid.UUID) ([]api.CooldownStatus, error)
	StartCooldown(ctx context.Context, characterID, abilityID uuid.UUID, duration time.Duration) error
	RecordActivation(ctx context.Context, characterID, abilityID uuid.UUID, targetID *uuid.UUID) error
	GetAvailableSynergies(ctx context.Context, characterID uuid.UUID, abilityID *uuid.UUID) ([]api.Synergy, error)
	UpdateCyberpsychosis(ctx context.Context, characterID uuid.UUID, impact float32) error
	GetSynergy(ctx context.Context, synergyID uuid.UUID) (*api.Synergy, error)
	CheckSynergyRequirements(ctx context.Context, characterID uuid.UUID, synergy *api.Synergy) (bool, error)
	ApplySynergy(ctx context.Context, characterID, synergyID uuid.UUID, synergy *api.Synergy) error
	GetCyberpsychosisState(ctx context.Context, characterID uuid.UUID) (*api.CyberpsychosisState, error)
	GetAbilityMetrics(ctx context.Context, characterID uuid.UUID, abilityID api.OptUUID, periodStart api.OptDateTime, periodEnd api.OptDateTime) (*api.AbilityMetrics, error)
}

type AbilityRepository struct {
	db *pgxpool.Pool
}

func NewAbilityRepository(db *pgxpool.Pool) *AbilityRepository {
	return &AbilityRepository{db: db}
}
