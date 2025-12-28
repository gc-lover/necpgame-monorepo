// Issue: #1495 - Gameplay Affixes Service implementation
// PERFORMANCE: Affix repository with optimized database queries and connection pooling

package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"gameplay-affixes-service-go/internal/models"
)

// AffixRepository defines affix data access methods
type AffixRepository interface {
	// Affix operations
	CreateAffix(ctx context.Context, affix *models.Affix) error
	GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error)
	ListAffixes(ctx context.Context, limit, offset int) ([]models.Affix, error)
	UpdateAffix(ctx context.Context, affix *models.Affix) error
	DeleteAffix(ctx context.Context, id uuid.UUID) error

	// Active affixes operations
	GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error)
	SetActiveAffixes(ctx context.Context, affixes []uuid.UUID, seasonalAffix *uuid.UUID) error

	// Instance affixes operations
	GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error)
	SetInstanceAffixes(ctx context.Context, instanceID uuid.UUID, affixIDs []uuid.UUID) error

	// Rotation operations
	GetAffixRotationHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.AffixRotation, error)
	CreateAffixRotation(ctx context.Context, rotation *models.AffixRotation) error
	GetCurrentRotation(ctx context.Context) (*models.AffixRotation, error)

	// Utility operations
	GetRandomAffixes(ctx context.Context, count int, exclude []uuid.UUID) ([]uuid.UUID, error)
}

// affixRepository implements AffixRepository
type affixRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewAffixRepository creates a new affix repository
func NewAffixRepository(db *pgxpool.Pool, logger *zap.Logger) AffixRepository {
	return &affixRepository{
		db:     db,
		logger: logger,
	}
}

// CreateAffix creates a new affix
func (r *affixRepository) CreateAffix(ctx context.Context, affix *models.Affix) error {
	query := `
		INSERT INTO gameplay.affixes (
			id, name, description, category, reward_modifier, difficulty_modifier,
			mechanics, visual_effects, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		affix.ID, affix.Name, affix.Description, affix.Category,
		affix.RewardModifier, affix.DifficultyModifier,
		affix.Mechanics, affix.VisualEffects, affix.CreatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create affix", zap.Error(err), zap.String("affix_id", affix.ID.String()))
		return fmt.Errorf("failed to create affix: %w", err)
	}

	return nil
}

// GetAffix retrieves an affix by ID
func (r *affixRepository) GetAffix(ctx context.Context, id uuid.UUID) (*models.Affix, error) {
	query := `
		SELECT id, name, description, category, reward_modifier, difficulty_modifier,
			   mechanics, visual_effects, created_at
		FROM gameplay.affixes
		WHERE id = $1
	`

	var affix models.Affix
	err := r.db.QueryRow(ctx, query, id).Scan(
		&affix.ID, &affix.Name, &affix.Description, &affix.Category,
		&affix.RewardModifier, &affix.DifficultyModifier,
		&affix.Mechanics, &affix.VisualEffects, &affix.CreatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get affix", zap.Error(err), zap.String("affix_id", id.String()))
		return nil, fmt.Errorf("failed to get affix: %w", err)
	}

	return &affix, nil
}

// ListAffixes retrieves a list of affixes with pagination
func (r *affixRepository) ListAffixes(ctx context.Context, limit, offset int) ([]models.Affix, error) {
	query := `
		SELECT id, name, description, category, reward_modifier, difficulty_modifier,
			   mechanics, visual_effects, created_at
		FROM gameplay.affixes
		ORDER BY created_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list affixes", zap.Error(err))
		return nil, fmt.Errorf("failed to list affixes: %w", err)
	}
	defer rows.Close()

	var affixes []models.Affix
	for rows.Next() {
		var affix models.Affix
		err := rows.Scan(
			&affix.ID, &affix.Name, &affix.Description, &affix.Category,
			&affix.RewardModifier, &affix.DifficultyModifier,
			&affix.Mechanics, &affix.VisualEffects, &affix.CreatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan affix", zap.Error(err))
			return nil, fmt.Errorf("failed to scan affix: %w", err)
		}
		affixes = append(affixes, affix)
	}

	return affixes, nil
}

// GetActiveAffixes retrieves the currently active affixes
func (r *affixRepository) GetActiveAffixes(ctx context.Context) (*models.ActiveAffixesResponse, error) {
	query := `
		SELECT ar.week_start, ar.week_end, ar.seasonal_affix_id,
			   array_agg(aa.affix_id) as active_affix_ids
		FROM gameplay.affix_rotations ar
		LEFT JOIN gameplay.active_affixes aa ON ar.id = aa.rotation_id
		WHERE ar.week_start <= NOW() AND ar.week_end > NOW()
		GROUP BY ar.id, ar.week_start, ar.week_end, ar.seasonal_affix_id
		ORDER BY ar.week_start DESC
		LIMIT 1
	`

	var response models.ActiveAffixesResponse
	var seasonalAffixID *uuid.UUID
	var activeAffixIDs []uuid.UUID

	err := r.db.QueryRow(ctx, query).Scan(
		&response.WeekStart, &response.WeekEnd, &seasonalAffixID, &activeAffixIDs,
	)
	if err != nil {
		r.logger.Error("Failed to get active affixes", zap.Error(err))
		return nil, fmt.Errorf("failed to get active affixes: %w", err)
	}

	// Get affix details
	for _, id := range activeAffixIDs {
		affix, err := r.GetAffix(ctx, id)
		if err != nil {
			r.logger.Error("Failed to get affix details", zap.Error(err), zap.String("affix_id", id.String()))
			continue
		}
		response.ActiveAffixes = append(response.ActiveAffixes, models.AffixSummary{
			ID:                 affix.ID,
			Name:               affix.Name,
			Description:        affix.Description,
			Category:           affix.Category,
			RewardModifier:     affix.RewardModifier,
			DifficultyModifier: affix.DifficultyModifier,
		})
	}

	// Get seasonal affix if exists
	if seasonalAffixID != nil {
		seasonalAffix, err := r.GetAffix(ctx, *seasonalAffixID)
		if err != nil {
			r.logger.Error("Failed to get seasonal affix", zap.Error(err), zap.String("affix_id", seasonalAffixID.String()))
		} else {
			response.SeasonalAffix = &models.AffixSummary{
				ID:                 seasonalAffix.ID,
				Name:               seasonalAffix.Name,
				Description:        seasonalAffix.Description,
				Category:           seasonalAffix.Category,
				RewardModifier:     seasonalAffix.RewardModifier,
				DifficultyModifier: seasonalAffix.DifficultyModifier,
			}
		}
	}

	return &response, nil
}

// SetActiveAffixes sets the active affixes for the current week
func (r *affixRepository) SetActiveAffixes(ctx context.Context, affixes []uuid.UUID, seasonalAffix *uuid.UUID) error {
	// Calculate current week boundaries (Monday to Sunday)
	now := time.Now()
	weekStart := now.AddDate(0, 0, -int(now.Weekday()-time.Monday))
	if now.Weekday() == time.Sunday {
		weekStart = weekStart.AddDate(0, 0, -7)
	}
	weekEnd := weekStart.AddDate(0, 0, 7)

	// Create rotation record
	rotationID := uuid.New()
	query := `
		INSERT INTO gameplay.affix_rotations (id, week_start, week_end, seasonal_affix_id, created_at)
		VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.Exec(ctx, query, rotationID, weekStart, weekEnd, seasonalAffix, time.Now())
	if err != nil {
		r.logger.Error("Failed to create affix rotation", zap.Error(err))
		return fmt.Errorf("failed to create affix rotation: %w", err)
	}

	// Insert active affixes
	for _, affixID := range affixes {
		_, err := r.db.Exec(ctx,
			"INSERT INTO gameplay.active_affixes (rotation_id, affix_id) VALUES ($1, $2)",
			rotationID, affixID,
		)
		if err != nil {
			r.logger.Error("Failed to insert active affix", zap.Error(err), zap.String("affix_id", affixID.String()))
			return fmt.Errorf("failed to insert active affix: %w", err)
		}
	}

	return nil
}

// GetInstanceAffixes retrieves affixes applied to a specific instance
func (r *affixRepository) GetInstanceAffixes(ctx context.Context, instanceID uuid.UUID) (*models.InstanceAffixesResponse, error) {
	query := `
		SELECT ia.applied_at, array_agg(ia.affix_id) as affix_ids
		FROM gameplay.instance_affixes ia
		WHERE ia.instance_id = $1
		GROUP BY ia.instance_id, ia.applied_at
		ORDER BY ia.applied_at DESC
		LIMIT 1
	`

	var response models.InstanceAffixesResponse
	response.InstanceID = instanceID
	var affixIDs []uuid.UUID

	err := r.db.QueryRow(ctx, query, instanceID).Scan(&response.AppliedAt, &affixIDs)
	if err != nil {
		r.logger.Error("Failed to get instance affixes", zap.Error(err), zap.String("instance_id", instanceID.String()))
		return nil, fmt.Errorf("failed to get instance affixes: %w", err)
	}

	// Get affix details and calculate modifiers
	totalRewardModifier := 1.0
	totalDifficultyModifier := 1.0

	for _, id := range affixIDs {
		affix, err := r.GetAffix(ctx, id)
		if err != nil {
			r.logger.Error("Failed to get affix details", zap.Error(err), zap.String("affix_id", id.String()))
			continue
		}

		response.Affixes = append(response.Affixes, models.AffixSummary{
			ID:                 affix.ID,
			Name:               affix.Name,
			Description:        affix.Description,
			Category:           affix.Category,
			RewardModifier:     affix.RewardModifier,
			DifficultyModifier: affix.DifficultyModifier,
		})

		totalRewardModifier *= affix.RewardModifier
		totalDifficultyModifier *= affix.DifficultyModifier
	}

	response.TotalRewardModifier = totalRewardModifier
	response.TotalDifficultyModifier = totalDifficultyModifier

	return &response, nil
}

// SetInstanceAffixes applies affixes to an instance
func (r *affixRepository) SetInstanceAffixes(ctx context.Context, instanceID uuid.UUID, affixIDs []uuid.UUID) error {
	appliedAt := time.Now()

	for _, affixID := range affixIDs {
		_, err := r.db.Exec(ctx,
			"INSERT INTO gameplay.instance_affixes (instance_id, affix_id, applied_at) VALUES ($1, $2, $3)",
			instanceID, affixID, appliedAt,
		)
		if err != nil {
			r.logger.Error("Failed to set instance affix", zap.Error(err),
				zap.String("instance_id", instanceID.String()), zap.String("affix_id", affixID.String()))
			return fmt.Errorf("failed to set instance affix: %w", err)
		}
	}

	return nil
}

// GetRandomAffixes retrieves random affixes excluding specified ones
func (r *affixRepository) GetRandomAffixes(ctx context.Context, count int, exclude []uuid.UUID) ([]uuid.UUID, error) {
	query := `
		SELECT id FROM gameplay.affixes
		WHERE id != ALL($1)
		ORDER BY RANDOM()
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, exclude, count)
	if err != nil {
		r.logger.Error("Failed to get random affixes", zap.Error(err))
		return nil, fmt.Errorf("failed to get random affixes: %w", err)
	}
	defer rows.Close()

	var affixIDs []uuid.UUID
	for rows.Next() {
		var id uuid.UUID
		if err := rows.Scan(&id); err != nil {
			r.logger.Error("Failed to scan affix ID", zap.Error(err))
			return nil, fmt.Errorf("failed to scan affix ID: %w", err)
		}
		affixIDs = append(affixIDs, id)
	}

	return affixIDs, nil
}

// GetAffixRotationHistory retrieves the history of affix rotations
func (r *affixRepository) GetAffixRotationHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.AffixRotation, error) {
	query := `
		SELECT ar.id, ar.week_start, ar.week_end, ar.seasonal_affix_id, ar.created_at,
			   array_agg(aa.affix_id) as active_affix_ids
		FROM gameplay.affix_rotations ar
		LEFT JOIN gameplay.active_affixes aa ON ar.id = aa.rotation_id
		WHERE ar.week_end < NOW()
		GROUP BY ar.id, ar.week_start, ar.week_end, ar.seasonal_affix_id, ar.created_at
		ORDER BY ar.week_start DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		r.logger.Error("Failed to get affix rotation history", zap.Error(err))
		return nil, fmt.Errorf("failed to get affix rotation history: %w", err)
	}
	defer rows.Close()

	var rotations []models.AffixRotation
	for rows.Next() {
		var rotation models.AffixRotation
		var seasonalAffixID *uuid.UUID
		var activeAffixIDs []uuid.UUID

		err := rows.Scan(
			&rotation.ID, &rotation.WeekStart, &rotation.WeekEnd,
			&seasonalAffixID, &rotation.CreatedAt, &activeAffixIDs,
		)
		if err != nil {
			r.logger.Error("Failed to scan rotation", zap.Error(err))
			return nil, fmt.Errorf("failed to scan rotation: %w", err)
		}

		// Get active affixes details
		for _, id := range activeAffixIDs {
			affix, err := r.GetAffix(ctx, id)
			if err != nil {
				r.logger.Error("Failed to get affix details", zap.Error(err), zap.String("affix_id", id.String()))
				continue
			}
			rotation.ActiveAffixes = append(rotation.ActiveAffixes, models.AffixSummary{
				ID:                 affix.ID,
				Name:               affix.Name,
				Description:        affix.Description,
				Category:           affix.Category,
				RewardModifier:     affix.RewardModifier,
				DifficultyModifier: affix.DifficultyModifier,
			})
		}

		// Get seasonal affix if exists
		if seasonalAffixID != nil {
			seasonalAffix, err := r.GetAffix(ctx, *seasonalAffixID)
			if err != nil {
				r.logger.Error("Failed to get seasonal affix", zap.Error(err), zap.String("affix_id", seasonalAffixID.String()))
			} else {
				rotation.SeasonalAffix = &models.AffixSummary{
					ID:                 seasonalAffix.ID,
					Name:               seasonalAffix.Name,
					Description:        seasonalAffix.Description,
					Category:           seasonalAffix.Category,
					RewardModifier:     seasonalAffix.RewardModifier,
					DifficultyModifier: seasonalAffix.DifficultyModifier,
				}
			}
		}

		rotations = append(rotations, rotation)
	}

	return rotations, nil
}

// CreateAffixRotation creates a new affix rotation record
func (r *affixRepository) CreateAffixRotation(ctx context.Context, rotation *models.AffixRotation) error {
	query := `
		INSERT INTO gameplay.affix_rotations (
			id, week_start, week_end, seasonal_affix_id, created_at
		) VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.Exec(ctx, query,
		rotation.ID, rotation.WeekStart, rotation.WeekEnd,
		rotation.SeasonalAffix, rotation.CreatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create affix rotation", zap.Error(err), zap.String("rotation_id", rotation.ID.String()))
		return fmt.Errorf("failed to create affix rotation: %w", err)
	}

	return nil
}

// GetCurrentRotation retrieves the current affix rotation
func (r *affixRepository) GetCurrentRotation(ctx context.Context) (*models.AffixRotation, error) {
	query := `
		SELECT ar.id, ar.week_start, ar.week_end, ar.seasonal_affix_id, ar.created_at,
			   array_agg(aa.affix_id) as active_affix_ids
		FROM gameplay.affix_rotations ar
		LEFT JOIN gameplay.active_affixes aa ON ar.id = aa.rotation_id
		WHERE ar.week_start <= NOW() AND ar.week_end > NOW()
		GROUP BY ar.id, ar.week_start, ar.week_end, ar.seasonal_affix_id, ar.created_at
		ORDER BY ar.week_start DESC
		LIMIT 1
	`

	var rotation models.AffixRotation
	var seasonalAffixID *uuid.UUID
	var activeAffixIDs []uuid.UUID

	err := r.db.QueryRow(ctx, query).Scan(
		&rotation.ID, &rotation.WeekStart, &rotation.WeekEnd,
		&seasonalAffixID, &rotation.CreatedAt, &activeAffixIDs,
	)
	if err != nil {
		r.logger.Error("Failed to get current rotation", zap.Error(err))
		return nil, fmt.Errorf("failed to get current rotation: %w", err)
	}

	// Get active affixes details (same logic as in GetActiveAffixes)
	for _, id := range activeAffixIDs {
		affix, err := r.GetAffix(ctx, id)
		if err != nil {
			r.logger.Error("Failed to get affix details", zap.Error(err), zap.String("affix_id", id.String()))
			continue
		}
		rotation.ActiveAffixes = append(rotation.ActiveAffixes, models.AffixSummary{
			ID:                 affix.ID,
			Name:               affix.Name,
			Description:        affix.Description,
			Category:           affix.Category,
			RewardModifier:     affix.RewardModifier,
			DifficultyModifier: affix.DifficultyModifier,
		})
	}

	// Get seasonal affix if exists
	if seasonalAffixID != nil {
		seasonalAffix, err := r.GetAffix(ctx, *seasonalAffixID)
		if err != nil {
			r.logger.Error("Failed to get seasonal affix", zap.Error(err), zap.String("affix_id", seasonalAffixID.String()))
		} else {
			rotation.SeasonalAffix = &models.AffixSummary{
				ID:                 seasonalAffix.ID,
				Name:               seasonalAffix.Name,
				Description:        seasonalAffix.Description,
				Category:           seasonalAffix.Category,
				RewardModifier:     seasonalAffix.RewardModifier,
				DifficultyModifier: seasonalAffix.DifficultyModifier,
			}
		}
	}

	return &rotation, nil
}

// UpdateAffix updates an existing affix
func (r *affixRepository) UpdateAffix(ctx context.Context, affix *models.Affix) error {
	query := `
		UPDATE gameplay.affixes
		SET name = $2, description = $3, category = $4,
			reward_modifier = $5, difficulty_modifier = $6,
			mechanics = $7, visual_effects = $8
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		affix.ID, affix.Name, affix.Description, affix.Category,
		affix.RewardModifier, affix.DifficultyModifier,
		affix.Mechanics, affix.VisualEffects,
	)

	if err != nil {
		r.logger.Error("Failed to update affix", zap.Error(err), zap.String("affix_id", affix.ID.String()))
		return fmt.Errorf("failed to update affix: %w", err)
	}

	return nil
}

// DeleteAffix deletes an affix by ID
func (r *affixRepository) DeleteAffix(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM gameplay.affixes WHERE id = $1`

	_, err := r.db.Exec(ctx, query, id)
	if err != nil {
		r.logger.Error("Failed to delete affix", zap.Error(err), zap.String("affix_id", id.String()))
		return fmt.Errorf("failed to delete affix: %w", err)
	}

	return nil
}
