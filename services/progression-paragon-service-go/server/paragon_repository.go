package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type ParagonRepositoryInterface interface {
	GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*ParagonLevels, error)
	DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, allocations []ParagonAllocation) (*ParagonLevels, error)
	GetParagonStats(ctx context.Context, characterID uuid.UUID) (*ParagonStats, error)
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

func (r *ParagonRepository) GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*ParagonLevels, error) {
	var levels ParagonLevels
	err := r.db.QueryRow(ctx,
		`SELECT character_id, paragon_level, paragon_points_total, paragon_points_spent,
		        paragon_points_available, experience_current, experience_required, updated_at
		 FROM progression.paragon_levels
		 WHERE character_id = $1`,
		characterID,
	).Scan(&levels.CharacterID, &levels.ParagonLevel, &levels.ParagonPointsTotal,
		&levels.ParagonPointsSpent, &levels.ParagonPointsAvailable,
		&levels.ExperienceCurrent, &levels.ExperienceRequired, &levels.UpdatedAt)

	if err == pgx.ErrNoRows {
		levels = ParagonLevels{
			CharacterID:            characterID,
			ParagonLevel:           0,
			ParagonPointsTotal:     0,
			ParagonPointsSpent:     0,
			ParagonPointsAvailable: 0,
			ExperienceCurrent:      0,
			ExperienceRequired:     150000,
			Allocations:            []ParagonAllocation{},
			UpdatedAt:              time.Now(),
		}
		return &levels, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get paragon levels")
		return nil, err
	}

	rows, err := r.db.Query(ctx,
		`SELECT stat_type, points_allocated
		 FROM progression.paragon_allocations
		 WHERE character_id = $1`,
		characterID,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get paragon allocations")
		return nil, err
	}
	defer rows.Close()

	var allocations []ParagonAllocation
	for rows.Next() {
		var alloc ParagonAllocation
		if err := rows.Scan(&alloc.StatType, &alloc.PointsAllocated); err != nil {
			r.logger.WithError(err).Error("Failed to scan allocation")
			continue
		}
		allocations = append(allocations, alloc)
	}
	levels.Allocations = allocations

	return &levels, nil
}

func (r *ParagonRepository) DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, allocations []ParagonAllocation) (*ParagonLevels, error) {
	validStats := map[string]bool{
		"strength":     true,
		"agility":      true,
		"intelligence": true,
		"vitality":     true,
		"willpower":    true,
		"perception":   true,
	}

	for _, alloc := range allocations {
		if !validStats[alloc.StatType] {
			return nil, fmt.Errorf("invalid stat_type: %s", alloc.StatType)
		}
		if alloc.PointsAllocated <= 0 {
			return nil, fmt.Errorf("points must be positive for stat_type: %s", alloc.StatType)
		}
	}

	tx, err := r.db.Begin(ctx)
	if err != nil {
		return nil, err
	}
	defer tx.Rollback(ctx)

	var pointsAvailable int
	err = tx.QueryRow(ctx,
		`SELECT paragon_points_available
		 FROM progression.paragon_levels
		 WHERE character_id = $1`,
		characterID,
	).Scan(&pointsAvailable)

	if err == pgx.ErrNoRows {
		return nil, fmt.Errorf("paragon levels not found for character")
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get available points")
		return nil, err
	}

	totalPoints := 0
	for _, alloc := range allocations {
		totalPoints += alloc.PointsAllocated
	}

	if totalPoints > pointsAvailable {
		return nil, fmt.Errorf("not enough paragon points available: need %d, have %d", totalPoints, pointsAvailable)
	}

	for _, alloc := range allocations {
		_, err = tx.Exec(ctx,
			`INSERT INTO progression.paragon_allocations (character_id, stat_type, points_allocated, updated_at)
			 VALUES ($1, $2, $3, $4)
			 ON CONFLICT (character_id, stat_type)
			 DO UPDATE SET points_allocated = progression.paragon_allocations.points_allocated + $3, updated_at = $4`,
			characterID, alloc.StatType, alloc.PointsAllocated, time.Now(),
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to update allocation")
			return nil, err
		}
	}

	_, err = tx.Exec(ctx,
		`UPDATE progression.paragon_levels
		 SET paragon_points_spent = paragon_points_spent + $1,
		     paragon_points_available = paragon_points_available - $1,
		     updated_at = $2
		 WHERE character_id = $3`,
		totalPoints, time.Now(), characterID,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update paragon levels")
		return nil, err
	}

	if err := tx.Commit(ctx); err != nil {
		return nil, err
	}

	return r.GetParagonLevels(ctx, characterID)
}

func (r *ParagonRepository) GetParagonStats(ctx context.Context, characterID uuid.UUID) (*ParagonStats, error) {
	levels, err := r.GetParagonLevels(ctx, characterID)
	if err != nil {
		return nil, err
	}

	pointsByStat := make(map[string]int)
	for _, alloc := range levels.Allocations {
		pointsByStat[alloc.StatType] = alloc.PointsAllocated
	}

	var globalRank int
	var totalPlayers int
	err = r.db.QueryRow(ctx,
		`WITH char_level AS (
			SELECT paragon_level FROM progression.paragon_levels WHERE character_id = $1
		)
		SELECT 
			COALESCE((SELECT COUNT(*) + 1 FROM progression.paragon_levels WHERE paragon_level > (SELECT paragon_level FROM char_level)), 1) as rank,
			(SELECT COUNT(*) FROM progression.paragon_levels) as total`,
		characterID,
	).Scan(&globalRank, &totalPlayers)

	percentile := 0.0
	if err == nil && totalPlayers > 0 {
		percentile = float64(totalPlayers-globalRank+1) / float64(totalPlayers) * 100.0
		if percentile > 100.0 {
			percentile = 100.0
		}
	} else if err != nil && err != pgx.ErrNoRows {
		r.logger.WithError(err).Error("Failed to get global rank")
		globalRank = 0
		percentile = 0.0
	}

	stats := &ParagonStats{
		CharacterID:        characterID,
		TotalParagonLevels: levels.ParagonLevel,
		TotalPointsEarned:  levels.ParagonPointsTotal,
		TotalPointsSpent:   levels.ParagonPointsSpent,
		PointsByStat:       pointsByStat,
		GlobalRank:         globalRank,
		Percentile:         percentile,
	}

	return stats, nil
}
