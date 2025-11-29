package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type ParagonRepositoryInterface interface {
	GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*models.ParagonLevels, error)
	CreateParagonLevels(ctx context.Context, paragon *models.ParagonLevels) error
	UpdateParagonLevels(ctx context.Context, paragon *models.ParagonLevels) error
	GetParagonStats(ctx context.Context, characterID uuid.UUID) (*models.ParagonStats, error)
}

type ParagonRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewParagonRepository(db *pgxpool.Pool) *ParagonRepository {
	return &ParagonRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *ParagonRepository) GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*models.ParagonLevels, error) {
	query := `
		SELECT 
			character_id, paragon_level, paragon_points_total, paragon_points_spent,
			paragon_points_available, experience_current, experience_required,
			allocations, updated_at
		FROM progression.paragon_levels
		WHERE character_id = $1`

	var paragon models.ParagonLevels
	var allocationsJSON []byte

	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&paragon.CharacterID,
		&paragon.ParagonLevel,
		&paragon.ParagonPointsTotal,
		&paragon.ParagonPointsSpent,
		&paragon.ParagonPointsAvailable,
		&paragon.ExperienceCurrent,
		&paragon.ExperienceRequired,
		&allocationsJSON,
		&paragon.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get paragon levels")
		return nil, fmt.Errorf("failed to get paragon levels: %w", err)
	}

	if len(allocationsJSON) > 0 {
		if err := json.Unmarshal(allocationsJSON, &paragon.Allocations); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal allocations JSON")
			return nil, fmt.Errorf("failed to unmarshal allocations JSON: %w", err)
		}
	}

	return &paragon, nil
}

func (r *ParagonRepository) CreateParagonLevels(ctx context.Context, paragon *models.ParagonLevels) error {
	allocationsJSON, err := json.Marshal(paragon.Allocations)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal allocations JSON")
		return fmt.Errorf("failed to marshal allocations JSON: %w", err)
	}

	query := `
		INSERT INTO progression.paragon_levels (
			character_id, paragon_level, paragon_points_total, paragon_points_spent,
			paragon_points_available, experience_current, experience_required,
			allocations, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (character_id) DO NOTHING`

	_, err = r.db.Exec(ctx, query,
		paragon.CharacterID,
		paragon.ParagonLevel,
		paragon.ParagonPointsTotal,
		paragon.ParagonPointsSpent,
		paragon.ParagonPointsAvailable,
		paragon.ExperienceCurrent,
		paragon.ExperienceRequired,
		allocationsJSON,
		paragon.UpdatedAt,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create paragon levels")
		return fmt.Errorf("failed to create paragon levels: %w", err)
	}

	return nil
}

func (r *ParagonRepository) UpdateParagonLevels(ctx context.Context, paragon *models.ParagonLevels) error {
	allocationsJSON, err := json.Marshal(paragon.Allocations)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal allocations JSON")
		return fmt.Errorf("failed to marshal allocations JSON: %w", err)
	}

	query := `
		UPDATE progression.paragon_levels
		SET 
			paragon_level = $1,
			paragon_points_total = $2,
			paragon_points_spent = $3,
			paragon_points_available = $4,
			experience_current = $5,
			experience_required = $6,
			allocations = $7,
			updated_at = $8
		WHERE character_id = $9`

	_, err = r.db.Exec(ctx, query,
		paragon.ParagonLevel,
		paragon.ParagonPointsTotal,
		paragon.ParagonPointsSpent,
		paragon.ParagonPointsAvailable,
		paragon.ExperienceCurrent,
		paragon.ExperienceRequired,
		allocationsJSON,
		paragon.UpdatedAt,
		paragon.CharacterID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update paragon levels")
		return fmt.Errorf("failed to update paragon levels: %w", err)
	}

	return nil
}

func (r *ParagonRepository) GetParagonStats(ctx context.Context, characterID uuid.UUID) (*models.ParagonStats, error) {
	paragon, err := r.GetParagonLevels(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if paragon == nil {
		return nil, fmt.Errorf("paragon levels not found for character %s", characterID)
	}

	pointsByStat := make(map[string]int)
	for _, alloc := range paragon.Allocations {
		pointsByStat[alloc.StatType] = alloc.PointsAllocated
	}

	rankQuery := `
		SELECT COUNT(*) + 1
		FROM progression.paragon_levels
		WHERE paragon_level > $1 OR (paragon_level = $1 AND paragon_points_total > $2)`

	var globalRank int
	err = r.db.QueryRow(ctx, rankQuery, paragon.ParagonLevel, paragon.ParagonPointsTotal).Scan(&globalRank)
	if err != nil {
		r.logger.WithError(err).Warn("Failed to get global rank, using default")
		globalRank = 0
	}

	totalQuery := `SELECT COUNT(*) FROM progression.paragon_levels`
	var totalPlayers int
	err = r.db.QueryRow(ctx, totalQuery).Scan(&totalPlayers)
	if err != nil {
		r.logger.WithError(err).Warn("Failed to get total players, using default")
		totalPlayers = 1
	}

	percentile := 0.0
	if totalPlayers > 0 {
		percentile = float64(totalPlayers-globalRank) / float64(totalPlayers) * 100.0
	}

	stats := &models.ParagonStats{
		CharacterID:        characterID,
		TotalParagonLevels: paragon.ParagonLevel,
		TotalPointsEarned:  paragon.ParagonPointsTotal,
		TotalPointsSpent:   paragon.ParagonPointsSpent,
		PointsByStat:       pointsByStat,
		GlobalRank:         globalRank,
		Percentile:         percentile,
	}

	return stats, nil
}

