package repository

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"necpgame/services/adaptive-system-service-go/config"
	"necpgame/services/adaptive-system-service-go/internal/models"
)

// Repository handles database operations for adaptive system
type Repository struct {
	pool   *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository creates a new repository with enterprise-grade database optimization
func NewRepository(ctx context.Context, logger *zap.Logger, dsn string, dbConfig config.DatabaseConfig) (*Repository, error) {
	// PERFORMANCE: Configure database connection pool for MMOFPS scale
	config, err := pgxpool.ParseConfig(dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// Apply enterprise-grade pool optimizations for adaptive system
	config.MaxConns = int32(dbConfig.MaxConns)
	config.MinConns = int32(dbConfig.MinConns)
	config.MaxConnLifetime = dbConfig.MaxConnLifetime
	config.MaxConnIdleTime = dbConfig.MaxConnIdleTime
	config.HealthCheckPeriod = 1 * time.Minute // Health check frequency

	pool, err := pgxpool.NewWithConfig(ctx, config)
	if err != nil {
		return nil, fmt.Errorf("failed to create optimized connection pool: %w", err)
	}

	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Connected to adaptive database with enterprise-grade pool optimization",
		zap.Int("max_conns", dbConfig.MaxConns),
		zap.Int("min_conns", dbConfig.MinConns),
		zap.Duration("max_conn_lifetime", dbConfig.MaxConnLifetime),
		zap.Duration("max_conn_idle_time", dbConfig.MaxConnIdleTime))

	return &Repository{
		pool:   pool,
		logger: logger,
	}, nil
}

// Close closes the database connection pool
func (r *Repository) Close() {
	if r.pool != nil {
		r.pool.Close()
		r.logger.Info("Adaptive database connection pool closed")
	}
}

// HealthCheck performs database health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	return r.pool.Ping(ctx)
}

// CreateAdaptationEvent creates a new adaptation event
func (r *Repository) CreateAdaptationEvent(ctx context.Context, event *models.AdaptationEvent) (*models.AdaptationEvent, error) {
	query := `
		INSERT INTO adaptive.adaptation_events (id, player_id, event_type, data, timestamp, processed)
		VALUES ($1, $2, $3, $4, $5, $6)
		RETURNING id`

	err := r.pool.QueryRow(ctx, query,
		event.ID, event.PlayerID, event.EventType, event.Data, event.Timestamp, event.Processed).
		Scan(&event.ID)

	if err != nil {
		r.logger.Error("Failed to create adaptation event", zap.Error(err))
		return nil, err
	}

	r.logger.Info("Adaptation event created",
		zap.String("id", event.ID.String()),
		zap.String("player_id", event.PlayerID.String()),
		zap.String("event_type", event.EventType))

	return event, nil
}

// GetAdaptationEvent retrieves an adaptation event by ID
func (r *Repository) GetAdaptationEvent(ctx context.Context, id uuid.UUID) (*models.AdaptationEvent, error) {
	query := `
		SELECT id, player_id, event_type, data, timestamp, processed
		FROM adaptive.adaptation_events
		WHERE id = $1`

	event := &models.AdaptationEvent{}
	err := r.pool.QueryRow(ctx, query, id).Scan(
		&event.ID, &event.PlayerID, &event.EventType, &event.Data, &event.Timestamp, &event.Processed)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil // Event not found
		}
		r.logger.Error("Failed to get adaptation event", zap.String("id", id.String()), zap.Error(err))
		return nil, err
	}

	return event, nil
}

// ListAdaptationEvents retrieves paginated list of adaptation events with filters
func (r *Repository) ListAdaptationEvents(ctx context.Context, filter *models.AdaptationEventFilter, page, limit int) ([]*models.AdaptationEvent, int, error) {
	offset := (page - 1) * limit

	baseQuery := `
		SELECT id, player_id, event_type, data, timestamp, processed
		FROM adaptive.adaptation_events
		WHERE 1=1`

	countQuery := `SELECT COUNT(*) FROM adaptive.adaptation_events WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	// Apply filters
	if filter.PlayerID != nil {
		baseQuery += fmt.Sprintf(" AND player_id = $%d", argCount+1)
		countQuery += fmt.Sprintf(" AND player_id = $%d", argCount+1)
		args = append(args, *filter.PlayerID)
		argCount++
	}

	if filter.EventType != nil {
		baseQuery += fmt.Sprintf(" AND event_type = $%d", argCount+1)
		countQuery += fmt.Sprintf(" AND event_type = $%d", argCount+1)
		args = append(args, *filter.EventType)
		argCount++
	}

	if filter.Processed != nil {
		baseQuery += fmt.Sprintf(" AND processed = $%d", argCount+1)
		countQuery += fmt.Sprintf(" AND processed = $%d", argCount+1)
		args = append(args, *filter.Processed)
		argCount++
	}

	baseQuery += fmt.Sprintf(" ORDER BY timestamp DESC LIMIT $%d OFFSET $%d", argCount+1, argCount+2)
	args = append(args, limit, offset)

	// Get total count
	var total int
	err := r.pool.QueryRow(ctx, countQuery, args[:argCount]...).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to get total count", zap.Error(err))
		return nil, 0, err
	}

	// Get events
	rows, err := r.pool.Query(ctx, baseQuery, args...)
	if err != nil {
		r.logger.Error("Failed to list adaptation events", zap.Error(err))
		return nil, 0, err
	}
	defer rows.Close()

	var events []*models.AdaptationEvent
	for rows.Next() {
		event := &models.AdaptationEvent{}
		err := rows.Scan(&event.ID, &event.PlayerID, &event.EventType, &event.Data, &event.Timestamp, &event.Processed)
		if err != nil {
			r.logger.Error("Failed to scan adaptation event", zap.Error(err))
			return nil, 0, err
		}
		events = append(events, event)
	}

	return events, total, nil
}

// UpdateAdaptationEvent updates an adaptation event
func (r *Repository) UpdateAdaptationEvent(ctx context.Context, event *models.AdaptationEvent) error {
	query := `
		UPDATE adaptive.adaptation_events
		SET processed = $2
		WHERE id = $1`

	_, err := r.pool.Exec(ctx, query, event.ID, event.Processed)
	if err != nil {
		r.logger.Error("Failed to update adaptation event", zap.String("id", event.ID.String()), zap.Error(err))
		return err
	}

	r.logger.Info("Adaptation event updated", zap.String("id", event.ID.String()))
	return nil
}

// GetPlayerProfile retrieves player's adaptive profile
func (r *Repository) GetPlayerProfile(ctx context.Context, playerID uuid.UUID) (*models.PlayerProfile, error) {
	query := `
		SELECT player_id, difficulty, learning_rate, last_updated, event_count
		FROM adaptive.player_profiles
		WHERE player_id = $1`

	profile := &models.PlayerProfile{}
	err := r.pool.QueryRow(ctx, query, playerID).Scan(
		&profile.PlayerID, &profile.Difficulty, &profile.LearningRate,
		&profile.LastUpdated, &profile.EventCount)

	if err != nil {
		if err == sql.ErrNoRows {
			// Return default profile
			return &models.PlayerProfile{
				PlayerID:     playerID,
				Difficulty:   1.0,
				LearningRate: 0.01,
				LastUpdated:  time.Now(),
				EventCount:   0,
			}, nil
		}
		r.logger.Error("Failed to get player profile", zap.String("player_id", playerID.String()), zap.Error(err))
		return nil, err
	}

	return profile, nil
}

// UpdatePlayerProfile updates player's adaptive profile
func (r *Repository) UpdatePlayerProfile(ctx context.Context, profile *models.PlayerProfile) error {
	query := `
		INSERT INTO adaptive.player_profiles (player_id, difficulty, learning_rate, last_updated, event_count)
		VALUES ($1, $2, $3, $4, $5)
		ON CONFLICT (player_id)
		DO UPDATE SET
			difficulty = EXCLUDED.difficulty,
			learning_rate = EXCLUDED.learning_rate,
			last_updated = EXCLUDED.last_updated,
			event_count = EXCLUDED.event_count`

	_, err := r.pool.Exec(ctx, query,
		profile.PlayerID, profile.Difficulty, profile.LearningRate,
		profile.LastUpdated, profile.EventCount)

	if err != nil {
		r.logger.Error("Failed to update player profile", zap.String("player_id", profile.PlayerID.String()), zap.Error(err))
		return err
	}

	r.logger.Info("Player profile updated", zap.String("player_id", profile.PlayerID.String()))
	return nil
}

// GetAdaptationMetrics retrieves system performance metrics
func (r *Repository) GetAdaptationMetrics(ctx context.Context, periodStart, periodEnd time.Time) (*models.AdaptationMetrics, error) {
	query := `
		SELECT
			COUNT(*) as total_events,
			COUNT(CASE WHEN processed THEN 1 END) as processed_events,
			AVG(EXTRACT(EPOCH FROM (NOW() - timestamp))) as avg_processing_time
		FROM adaptive.adaptation_events
		WHERE timestamp BETWEEN $1 AND $2`

	metrics := &models.AdaptationMetrics{
		PeriodStart: periodStart,
		PeriodEnd:   periodEnd,
	}

	err := r.pool.QueryRow(ctx, query, periodStart, periodEnd).Scan(
		&metrics.TotalEvents, &metrics.ProcessedEvents, &metrics.AvgProcessingTime)

	if err != nil {
		r.logger.Error("Failed to get adaptation metrics", zap.Error(err))
		return nil, err
	}

	// Calculate success rate
	if metrics.TotalEvents > 0 {
		metrics.SuccessRate = float64(metrics.ProcessedEvents) / float64(metrics.TotalEvents)
	}

	return metrics, nil
}

// GetUnprocessedEventsCount returns count of unprocessed adaptation events
func (r *Repository) GetUnprocessedEventsCount(ctx context.Context) (int64, error) {
	query := `SELECT COUNT(*) FROM adaptive.adaptation_events WHERE processed = false`

	var count int64
	err := r.pool.QueryRow(ctx, query).Scan(&count)
	if err != nil {
		r.logger.Error("Failed to get unprocessed events count", zap.Error(err))
		return 0, err
	}

	return count, nil
}

// MarkEventProcessed marks an adaptation event as processed
func (r *Repository) MarkEventProcessed(ctx context.Context, eventID uuid.UUID) error {
	query := `UPDATE adaptive.adaptation_events SET processed = true WHERE id = $1`

	_, err := r.pool.Exec(ctx, query, eventID)
	if err != nil {
		r.logger.Error("Failed to mark event as processed", zap.String("event_id", eventID.String()), zap.Error(err))
		return err
	}

	return nil
}