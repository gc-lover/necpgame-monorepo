// Package server Issue: #2224
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"strings"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-service-go/models"
	"github.com/google/uuid"
	"github.com/lib/pq"
	"go.uber.org/zap"
)

// WorldEventsRepository handles database operations for world events
type WorldEventsRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewWorldEventsRepository creates a new world events repository
func NewWorldEventsRepository(db *sql.DB, logger *zap.Logger) *WorldEventsRepository {
	return &WorldEventsRepository{
		db:     db,
		logger: logger,
	}
}

// ListWorldEvents retrieves world events with filtering and pagination
func (r *WorldEventsRepository) ListWorldEvents(ctx context.Context, status *models.WorldEventStatus, eventType *models.WorldEventType, scale *models.WorldEventScale, frequency *models.WorldEventFrequency, limit, offset int) ([]*models.WorldEvent, int, error) {
	// Build query with proper parameterization
	var whereConditions []string
	var args []interface{}
	paramCount := 0

	if status != nil {
		paramCount++
		whereConditions = append(whereConditions, fmt.Sprintf("status = $%d", paramCount))
		args = append(args, *status)
	}

	if eventType != nil {
		paramCount++
		whereConditions = append(whereConditions, fmt.Sprintf("type = $%d", paramCount))
		args = append(args, *eventType)
	}

	if scale != nil {
		paramCount++
		whereConditions = append(whereConditions, fmt.Sprintf("scale = $%d", paramCount))
		args = append(args, *scale)
	}

	if frequency != nil {
		paramCount++
		whereConditions = append(whereConditions, fmt.Sprintf("frequency = $%d", paramCount))
		args = append(args, *frequency)
	}

	whereClause := ""
	if len(whereConditions) > 0 {
		whereClause = " WHERE " + strings.Join(whereConditions, " AND ")
	}

	baseQuery := fmt.Sprintf(`
		SELECT id, title, description, type, scale, frequency, status, start_time, end_time, duration,
		       target_regions, target_factions, prerequisites, cooldown_duration, max_concurrent,
		       created_at, updated_at, version
		FROM world.world_events%s ORDER BY created_at DESC`, whereClause)

	// Get total count
	countQuery := fmt.Sprintf("SELECT COUNT(*) FROM (%s) AS count_query", baseQuery)
	var total int
	countArgs := make([]interface{}, len(args))
	copy(countArgs, args)

	err := r.db.QueryRowContext(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	// Add pagination
	paramCount++
	query := baseQuery + fmt.Sprintf(" LIMIT $%d", paramCount)
	args = append(args, limit)

	paramCount++
	query += fmt.Sprintf(" OFFSET $%d", paramCount)
	args = append(args, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query world events: %w", err)
	}
	defer rows.Close()

	var events []*models.WorldEvent
	for rows.Next() {
		event := &models.WorldEvent{}
		var targetRegions []string
		var targetFactions []uuid.UUID
		var prerequisites []uuid.UUID

		err := rows.Scan(
			&event.ID, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
			&event.Status, &event.StartTime, &event.EndTime, &event.Duration,
			pq.Array(&targetRegions), pq.Array(&targetFactions), pq.Array(&prerequisites),
			&event.CooldownDuration, &event.MaxConcurrent,
			&event.CreatedAt, &event.UpdatedAt, &event.Version,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan world event row: %w", err)
		}

		event.TargetRegions = targetRegions
		event.TargetFactions = targetFactions
		event.Prerequisites = prerequisites

		events = append(events, event)
	}

	if err = rows.Err(); err != nil {
		return nil, 0, fmt.Errorf("error iterating world events rows: %w", err)
	}

	return events, total, nil
}

// GetWorldEvent retrieves a single world event by ID
func (r *WorldEventsRepository) GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*models.WorldEvent, error) {
	query := `
		SELECT id, title, description, type, scale, frequency, status, start_time, end_time, duration,
		       target_regions, target_factions, prerequisites, cooldown_duration, max_concurrent,
		       created_at, updated_at, version
		FROM world.world_events
		WHERE id = $1`

	event := &models.WorldEvent{}
	var targetRegions []string
	var targetFactions []uuid.UUID
	var prerequisites []uuid.UUID

	err := r.db.QueryRowContext(ctx, query, eventID).Scan(
		&event.ID, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
		&event.Status, &event.StartTime, &event.EndTime, &event.Duration,
		pq.Array(&targetRegions), pq.Array(&targetFactions), pq.Array(&prerequisites),
		&event.CooldownDuration, &event.MaxConcurrent,
		&event.CreatedAt, &event.UpdatedAt, &event.Version,
	)

	if err != nil {
		return nil, err
	}

	event.TargetRegions = targetRegions
	event.TargetFactions = targetFactions
	event.Prerequisites = prerequisites

	// Load effects
	effects, err := r.GetWorldEventEffects(ctx, eventID)
	if err != nil {
		r.logger.Warn("Failed to load effects for event", zap.String("event_id", eventID.String()), zap.Error(err))
	} else {
		event.Effects = effects
	}

	return event, nil
}

// CreateWorldEvent creates a new world event
func (r *WorldEventsRepository) CreateWorldEvent(ctx context.Context, event *models.WorldEvent) error {
	query := `
		INSERT INTO world.world_events (
			id, title, description, type, scale, frequency, status, start_time, end_time, duration,
			target_regions, target_factions, prerequisites, cooldown_duration, max_concurrent,
			created_at, updated_at, version
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18
		)`

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.Title, event.Description, event.Type, event.Scale, event.Frequency,
		event.Status, event.StartTime, event.EndTime, event.Duration,
		pq.Array(event.TargetRegions), pq.Array(event.TargetFactions), pq.Array(event.Prerequisites),
		event.CooldownDuration, event.MaxConcurrent,
		event.CreatedAt, event.UpdatedAt, event.Version,
	)

	if err != nil {
		return fmt.Errorf("failed to create world event: %w", err)
	}

	return nil
}

// UpdateWorldEvent updates an existing world event
func (r *WorldEventsRepository) UpdateWorldEvent(ctx context.Context, event *models.WorldEvent) error {
	query := `
		UPDATE world.world_events SET
			title = $2, description = $3, start_time = $4, end_time = $5, duration = $6,
			target_regions = $7, target_factions = $8, cooldown_duration = $9, max_concurrent = $10,
			updated_at = $11, version = $12, status = $13
		WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.Title, event.Description, event.StartTime, event.EndTime, event.Duration,
		pq.Array(event.TargetRegions), pq.Array(event.TargetFactions),
		event.CooldownDuration, event.MaxConcurrent,
		event.UpdatedAt, event.Version, event.Status,
	)

	if err != nil {
		return fmt.Errorf("failed to update world event: %w", err)
	}

	return nil
}

// DeleteWorldEvent deletes a world event
func (r *WorldEventsRepository) DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error {
	query := `DELETE FROM world.world_events WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, eventID)
	if err != nil {
		return fmt.Errorf("failed to delete world event: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return sql.ErrNoRows
	}

	return nil
}

// GetWorldEventEffects retrieves effects for a world event
func (r *WorldEventsRepository) GetWorldEventEffects(ctx context.Context, eventID uuid.UUID) ([]models.EventEffect, error) {
	query := `
		SELECT id, event_id, target_system, effect_type, parameters, start_time, end_time, is_active, created_at
		FROM world.world_event_effects
		WHERE event_id = $1
		ORDER BY created_at ASC`

	rows, err := r.db.QueryContext(ctx, query, eventID)
	if err != nil {
		return nil, fmt.Errorf("failed to query world event effects: %w", err)
	}
	defer rows.Close()

	var effects []models.EventEffect
	for rows.Next() {
		var effect models.EventEffect
		var parametersBytes []byte

		err := rows.Scan(
			&effect.ID, &effect.EventID, &effect.TargetSystem, &effect.EffectType,
			&parametersBytes, &effect.StartTime, &effect.EndTime, &effect.IsActive, &effect.CreatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan world event effect row: %w", err)
		}

		// Unmarshal parameters JSON
		if len(parametersBytes) > 0 {
			if err := json.Unmarshal(parametersBytes, &effect.Parameters); err != nil {
				r.logger.Warn("Failed to unmarshal effect parameters", zap.String("effect_id", effect.ID.String()), zap.Error(err))
				effect.Parameters = make(map[string]interface{})
			}
		} else {
			effect.Parameters = make(map[string]interface{})
		}

		effects = append(effects, effect)
	}

	if err = rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating world event effects rows: %w", err)
	}

	return effects, nil
}

// CreateWorldEventEffect creates a new event effect
func (r *WorldEventsRepository) CreateWorldEventEffect(ctx context.Context, effect *models.EventEffect) error {
	parametersJSON, err := json.Marshal(effect.Parameters)
	if err != nil {
		return fmt.Errorf("failed to marshal effect parameters: %w", err)
	}

	query := `
		INSERT INTO world.world_event_effects (
			id, event_id, target_system, effect_type, parameters, start_time, end_time, is_active, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`

	_, err = r.db.ExecContext(ctx, query,
		effect.ID, effect.EventID, effect.TargetSystem, effect.EffectType,
		parametersJSON, effect.StartTime, effect.EndTime, effect.IsActive, effect.CreatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create world event effect: %w", err)
	}

	return nil
}

// GetWorldEventEffect retrieves a single event effect by ID
func (r *WorldEventsRepository) GetWorldEventEffect(ctx context.Context, effectID uuid.UUID) (*models.EventEffect, error) {
	query := `
		SELECT id, event_id, target_system, effect_type, parameters, start_time, end_time, is_active, created_at
		FROM world.world_event_effects
		WHERE id = $1`

	var effect models.EventEffect
	var parametersBytes []byte

	err := r.db.QueryRowContext(ctx, query, effectID).Scan(
		&effect.ID, &effect.EventID, &effect.TargetSystem, &effect.EffectType,
		&parametersBytes, &effect.StartTime, &effect.EndTime, &effect.IsActive, &effect.CreatedAt,
	)

	if err != nil {
		return nil, err
	}

	// Unmarshal parameters JSON
	if len(parametersBytes) > 0 {
		if err := json.Unmarshal(parametersBytes, &effect.Parameters); err != nil {
			r.logger.Warn("Failed to unmarshal effect parameters", zap.String("effect_id", effect.ID.String()), zap.Error(err))
			effect.Parameters = make(map[string]interface{})
		}
	} else {
		effect.Parameters = make(map[string]interface{})
	}

	return &effect, nil
}

// UpdateWorldEventEffect updates an existing event effect
func (r *WorldEventsRepository) UpdateWorldEventEffect(ctx context.Context, effect *models.EventEffect) error {
	parametersJSON, err := json.Marshal(effect.Parameters)
	if err != nil {
		return fmt.Errorf("failed to marshal effect parameters: %w", err)
	}

	query := `
		UPDATE world.world_event_effects SET
			parameters = $2, start_time = $3, end_time = $4, is_active = $5
		WHERE id = $1`

	_, err = r.db.ExecContext(ctx, query,
		effect.ID, parametersJSON, effect.StartTime, effect.EndTime, effect.IsActive,
	)

	if err != nil {
		return fmt.Errorf("failed to update world event effect: %w", err)
	}

	return nil
}

// DeleteWorldEventEffect deletes an event effect
func (r *WorldEventsRepository) DeleteWorldEventEffect(ctx context.Context, effectID uuid.UUID) error {
	query := `DELETE FROM world.world_event_effects WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, effectID)
	if err != nil {
		return fmt.Errorf("failed to delete world event effect: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return sql.ErrNoRows
	}

	return nil
}

// CreateEventAnnouncement creates a new event announcement
func (r *WorldEventsRepository) CreateEventAnnouncement(ctx context.Context, announcement *models.EventAnnouncement) error {
	query := `
		INSERT INTO world.world_event_announcements (
			id, event_id, title, message, type, target_audience, priority, expires_at, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`

	_, err := r.db.ExecContext(ctx, query,
		announcement.ID, announcement.EventID, announcement.Title, announcement.Message,
		announcement.Type, announcement.TargetAudience, announcement.Priority,
		announcement.ExpiresAt, announcement.CreatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create event announcement: %w", err)
	}

	return nil
}

// GetActiveWorldEvents retrieves all currently active world events
func (r *WorldEventsRepository) GetActiveWorldEvents(ctx context.Context) ([]*models.WorldEvent, error) {
	status := models.EventStatusActive
	events, _, err := r.ListWorldEvents(ctx, &status, nil, nil, nil, 1000, 0)
	return events, err
}

// GetWorldEventsByRegion retrieves world events affecting specific regions
func (r *WorldEventsRepository) GetWorldEventsByRegion(ctx context.Context, regions []string) ([]*models.WorldEvent, error) {
	query := `
		SELECT DISTINCT we.id, we.title, we.description, we.type, we.scale, we.frequency, we.status,
		       we.start_time, we.end_time, we.duration, we.target_regions, we.target_factions,
		       we.prerequisites, we.cooldown_duration, we.max_concurrent,
		       we.created_at, we.updated_at, we.version
		FROM world.world_events we
		WHERE we.status = 'ACTIVE'
		  AND we.target_regions && $1
		ORDER BY we.created_at DESC`

	rows, err := r.db.QueryContext(ctx, query, pq.Array(regions))
	if err != nil {
		return nil, fmt.Errorf("failed to query world events by region: %w", err)
	}
	defer rows.Close()

	var events []*models.WorldEvent
	for rows.Next() {
		event := &models.WorldEvent{}
		var targetRegions []string
		var targetFactions []uuid.UUID
		var prerequisites []uuid.UUID

		err := rows.Scan(
			&event.ID, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
			&event.Status, &event.StartTime, &event.EndTime, &event.Duration,
			pq.Array(&targetRegions), pq.Array(&targetFactions), pq.Array(&prerequisites),
			&event.CooldownDuration, &event.MaxConcurrent,
			&event.CreatedAt, &event.UpdatedAt, &event.Version,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan world event by region row: %w", err)
		}

		event.TargetRegions = targetRegions
		event.TargetFactions = targetFactions
		event.Prerequisites = prerequisites

		events = append(events, event)
	}

	return events, rows.Err()
}

// UpdateWorldEventStatus updates only the status of a world event
func (r *WorldEventsRepository) UpdateWorldEventStatus(ctx context.Context, eventID uuid.UUID, status models.WorldEventStatus) error {
	query := `
		UPDATE world.world_events SET
			status = $2, updated_at = $3
		WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query, eventID, status, time.Now())
	if err != nil {
		return fmt.Errorf("failed to update world event status: %w", err)
	}

	return nil
}

// ArchiveCompletedEvents moves completed events to archive status
func (r *WorldEventsRepository) ArchiveCompletedEvents(ctx context.Context) error {
	query := `
		UPDATE world.world_events SET
			status = 'ARCHIVED',
			updated_at = $1
		WHERE status IN ('COOLDOWN', 'ACTIVE')
		  AND (end_time IS NOT NULL AND end_time < $1)`

	result, err := r.db.ExecContext(ctx, query, time.Now())
	if err != nil {
		return fmt.Errorf("failed to archive completed events: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	r.logger.Info("Archived completed world events", zap.Int64("count", rowsAffected))

	return nil
}
