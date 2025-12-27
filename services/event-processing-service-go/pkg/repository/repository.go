// Issue: #event-processing-service - High-performance Event Repository
// Repository layer for Event Processing Service - Optimized for high-throughput event handling

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/lib/pq"

	"event-processing-service-go/pkg/models"
)

// RepositoryInterface defines the repository interface for dependency injection
type RepositoryInterface interface {
	// Event CRUD operations
	CreateEvent(ctx context.Context, event *models.Event) error
	GetEvent(ctx context.Context, id string) (*models.Event, error)
	GetEventsByFilter(ctx context.Context, filter *models.EventFilter, limit, offset int) ([]*models.Event, error)
	UpdateEventStatus(ctx context.Context, id string, status models.ProcessingStatus, errorMsg string, processingTime int) error
	MarkEventProcessed(ctx context.Context, id string, processingTime int) error
	DeleteOldEvents(ctx context.Context, olderThan time.Time) error

	// Bulk operations
	CreateEventsBulk(ctx context.Context, events []*models.Event) error
	GetPendingEvents(ctx context.Context, limit int) ([]*models.Event, error)
	GetFailedEventsForRetry(ctx context.Context, limit int) ([]*models.Event, error)

	// Handler operations
	GetEventHandler(ctx context.Context, eventType string) (*models.EventHandler, error)
	GetActiveEventHandlers(ctx context.Context) ([]*models.EventHandler, error)
	CreateEventHandler(ctx context.Context, handler *models.EventHandler) error
	UpdateEventHandler(ctx context.Context, handler *models.EventHandler) error

	// Statistics operations
	GetEventProcessingStats(ctx context.Context, eventType string) (*models.EventProcessingStats, error)
	UpdateEventProcessingStats(ctx context.Context, stats *models.EventProcessingStats) error
	GetAllEventStats(ctx context.Context) ([]*models.EventProcessingStats, error)

	// Dead letter operations
	CreateDeadLetterEvent(ctx context.Context, deadLetter *models.DeadLetterEvent) error
	GetDeadLetterEvents(ctx context.Context, limit, offset int) ([]*models.DeadLetterEvent, error)
	MarkDeadLetterReviewed(ctx context.Context, id string, reviewedBy, action string) error

	// Batch operations
	CreateEventBatch(ctx context.Context, batch *models.EventBatch) error
	GetEventBatch(ctx context.Context, id string) (*models.EventBatch, error)
	UpdateEventBatchStatus(ctx context.Context, id string, status string) error

	// Health check
	HealthCheck(ctx context.Context) error
}

// Repository implements RepositoryInterface with PostgreSQL
type Repository struct {
	db *sql.DB
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB) RepositoryInterface {
	return &Repository{db: db}
}

// CreateEvent creates a new event
func (r *Repository) CreateEvent(ctx context.Context, event *models.Event) error {
	eventData, _ := json.Marshal(event.EventData)

	query := `
		INSERT INTO events (
			id, event_type, player_id, session_id, game_id, event_data,
			timestamp, processed, status, priority, retries, max_retries,
			created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14
		)
	`

	event.ID = fmt.Sprintf("%d", time.Now().UnixNano()) // Simple ID generation
	event.CreatedAt = time.Now()
	event.UpdatedAt = time.Now()
	event.Status = string(models.ProcessingStatusPending)

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.EventType, event.PlayerID, event.SessionID, event.GameID,
		eventData, event.Timestamp, event.Processed, event.Status, event.Priority,
		event.Retries, event.MaxRetries, event.CreatedAt, event.UpdatedAt,
	)

	return err
}

// GetEvent retrieves an event by ID
func (r *Repository) GetEvent(ctx context.Context, id string) (*models.Event, error) {
	query := `
		SELECT id, event_type, player_id, session_id, game_id, event_data,
			   timestamp, processed, processed_at, processing_time, status,
			   error, priority, retries, max_retries, created_at, updated_at
		FROM events WHERE id = $1
	`

	var event models.Event
	var eventData []byte
	var processedAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&event.ID, &event.EventType, &event.PlayerID, &event.SessionID, &event.GameID,
		&eventData, &event.Timestamp, &event.Processed, &processedAt,
		&event.ProcessingTime, &event.Status, &event.Error, &event.Priority,
		&event.Retries, &event.MaxRetries, &event.CreatedAt, &event.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("event not found: %s", id)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get event: %w", err)
	}

	if processedAt.Valid {
		event.ProcessedAt = &processedAt.Time
	}

	if err := json.Unmarshal(eventData, &event.EventData); err != nil {
		return nil, fmt.Errorf("failed to unmarshal event data: %w", err)
	}

	return &event, nil
}

// GetEventsByFilter retrieves events based on filter criteria
func (r *Repository) GetEventsByFilter(ctx context.Context, filter *models.EventFilter, limit, offset int) ([]*models.Event, error) {
	query := `
		SELECT id, event_type, player_id, session_id, game_id, event_data,
			   timestamp, processed, processed_at, processing_time, status,
			   error, priority, retries, max_retries, created_at, updated_at
		FROM events WHERE 1=1
	`
	args := []interface{}{}
	argCount := 0

	// Apply filters
	if len(filter.EventType) > 0 {
		argCount++
		query += fmt.Sprintf(" AND event_type = ANY($%d)", argCount)
		args = append(args, pq.Array(filter.EventType))
	}

	if filter.PlayerID != "" {
		argCount++
		query += fmt.Sprintf(" AND player_id = $%d", argCount)
		args = append(args, filter.PlayerID)
	}

	if len(filter.Status) > 0 {
		argCount++
		query += fmt.Sprintf(" AND status = ANY($%d)", argCount)
		args = append(args, pq.Array(filter.Status))
	}

	if filter.TimeRange != nil {
		argCount++
		query += fmt.Sprintf(" AND timestamp >= $%d", argCount)
		args = append(args, filter.TimeRange.Start)
		argCount++
		query += fmt.Sprintf(" AND timestamp <= $%d", argCount)
		args = append(args, filter.TimeRange.End)
	}

	if filter.Processed != nil {
		argCount++
		query += fmt.Sprintf(" AND processed = $%d", argCount)
		args = append(args, *filter.Processed)
	}

	query += fmt.Sprintf(" ORDER BY created_at DESC LIMIT %d OFFSET %d", limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("failed to query events: %w", err)
	}
	defer rows.Close()

	var events []*models.Event
	for rows.Next() {
		var event models.Event
		var eventData []byte
		var processedAt sql.NullTime

		err := rows.Scan(
			&event.ID, &event.EventType, &event.PlayerID, &event.SessionID, &event.GameID,
			&eventData, &event.Timestamp, &event.Processed, &processedAt,
			&event.ProcessingTime, &event.Status, &event.Error, &event.Priority,
			&event.Retries, &event.MaxRetries, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if processedAt.Valid {
			event.ProcessedAt = &processedAt.Time
		}

		if err := json.Unmarshal(eventData, &event.EventData); err != nil {
			continue
		}

		events = append(events, &event)
	}

	return events, nil
}

// UpdateEventStatus updates the status of an event
func (r *Repository) UpdateEventStatus(ctx context.Context, id string, status models.ProcessingStatus, errorMsg string, processingTime int) error {
	query := `
		UPDATE events
		SET status = $2, error = $3, processing_time = $4, updated_at = $5
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, id, string(status), errorMsg, processingTime, time.Now())
	return err
}

// MarkEventProcessed marks an event as processed
func (r *Repository) MarkEventProcessed(ctx context.Context, id string, processingTime int) error {
	query := `
		UPDATE events
		SET processed = true, processed_at = $2, processing_time = $3,
		    status = 'completed', updated_at = $2
		WHERE id = $1
	`

	now := time.Now()
	_, err := r.db.ExecContext(ctx, query, id, now, processingTime)
	return err
}

// DeleteOldEvents deletes events older than specified time
func (r *Repository) DeleteOldEvents(ctx context.Context, olderThan time.Time) error {
	query := `DELETE FROM events WHERE created_at < $1 AND processed = true`
	_, err := r.db.ExecContext(ctx, query, olderThan)
	return err
}

// CreateEventsBulk creates multiple events in a single transaction
func (r *Repository) CreateEventsBulk(ctx context.Context, events []*models.Event) error {
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	stmt, err := tx.PrepareContext(ctx, `
		INSERT INTO events (
			id, event_type, player_id, session_id, game_id, event_data,
			timestamp, processed, status, priority, retries, max_retries,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
	`)
	if err != nil {
		return fmt.Errorf("failed to prepare statement: %w", err)
	}
	defer stmt.Close()

	now := time.Now()
	for _, event := range events {
		event.ID = fmt.Sprintf("%d", time.Now().UnixNano())
		event.CreatedAt = now
		event.UpdatedAt = now
		event.Status = string(models.ProcessingStatusPending)

		eventData, _ := json.Marshal(event.EventData)

		_, err = stmt.ExecContext(ctx,
			event.ID, event.EventType, event.PlayerID, event.SessionID, event.GameID,
			eventData, event.Timestamp, event.Processed, event.Status, event.Priority,
			event.Retries, event.MaxRetries, event.CreatedAt, event.UpdatedAt,
		)
		if err != nil {
			return fmt.Errorf("failed to insert event %s: %w", event.ID, err)
		}
	}

	return tx.Commit()
}

// GetPendingEvents retrieves pending events for processing
func (r *Repository) GetPendingEvents(ctx context.Context, limit int) ([]*models.Event, error) {
	query := `
		SELECT id, event_type, player_id, session_id, game_id, event_data,
			   timestamp, processed, processed_at, processing_time, status,
			   error, priority, retries, max_retries, created_at, updated_at
		FROM events
		WHERE status = 'pending'
		ORDER BY priority DESC, created_at ASC
		LIMIT $1
		FOR UPDATE SKIP LOCKED
	`

	rows, err := r.db.QueryContext(ctx, query, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get pending events: %w", err)
	}
	defer rows.Close()

	var events []*models.Event
	for rows.Next() {
		var event models.Event
		var eventData []byte
		var processedAt sql.NullTime

		err := rows.Scan(
			&event.ID, &event.EventType, &event.PlayerID, &event.SessionID, &event.GameID,
			&eventData, &event.Timestamp, &event.Processed, &processedAt,
			&event.ProcessingTime, &event.Status, &event.Error, &event.Priority,
			&event.Retries, &event.MaxRetries, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if processedAt.Valid {
			event.ProcessedAt = &processedAt.Time
		}

		if err := json.Unmarshal(eventData, &event.EventData); err != nil {
			continue
		}

		events = append(events, &event)
	}

	return events, nil
}

// GetFailedEventsForRetry retrieves failed events that can be retried
func (r *Repository) GetFailedEventsForRetry(ctx context.Context, limit int) ([]*models.Event, error) {
	query := `
		SELECT id, event_type, player_id, session_id, game_id, event_data,
			   timestamp, processed, processed_at, processing_time, status,
			   error, priority, retries, max_retries, created_at, updated_at
		FROM events
		WHERE status = 'failed' AND retries < max_retries
		ORDER BY priority DESC, created_at ASC
		LIMIT $1
		FOR UPDATE SKIP LOCKED
	`

	rows, err := r.db.QueryContext(ctx, query, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get failed events for retry: %w", err)
	}
	defer rows.Close()

	var events []*models.Event
	for rows.Next() {
		var event models.Event
		var eventData []byte
		var processedAt sql.NullTime

		err := rows.Scan(
			&event.ID, &event.EventType, &event.PlayerID, &event.SessionID, &event.GameID,
			&eventData, &event.Timestamp, &event.Processed, &processedAt,
			&event.ProcessingTime, &event.Status, &event.Error, &event.Priority,
			&event.Retries, &event.MaxRetries, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if processedAt.Valid {
			event.ProcessedAt = &processedAt.Time
		}

		if err := json.Unmarshal(eventData, &event.EventData); err != nil {
			continue
		}

		events = append(events, &event)
	}

	return events, nil
}

// GetEventHandler retrieves event handler configuration
func (r *Repository) GetEventHandler(ctx context.Context, eventType string) (*models.EventHandler, error) {
	query := `
		SELECT id, event_type, handler_name, service_name, endpoint, method,
			   is_active, priority, timeout, created_at, updated_at
		FROM event_handlers WHERE event_type = $1 AND is_active = true
	`

	var handler models.EventHandler
	err := r.db.QueryRowContext(ctx, query, eventType).Scan(
		&handler.ID, &handler.EventType, &handler.HandlerName, &handler.ServiceName,
		&handler.Endpoint, &handler.Method, &handler.IsActive, &handler.Priority,
		&handler.Timeout, &handler.CreatedAt, &handler.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("event handler not found for type: %s", eventType)
	}

	return &handler, err
}

// GetActiveEventHandlers retrieves all active event handlers
func (r *Repository) GetActiveEventHandlers(ctx context.Context) ([]*models.EventHandler, error) {
	query := `
		SELECT id, event_type, handler_name, service_name, endpoint, method,
			   is_active, priority, timeout, created_at, updated_at
		FROM event_handlers WHERE is_active = true
		ORDER BY priority DESC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to get active event handlers: %w", err)
	}
	defer rows.Close()

	var handlers []*models.EventHandler
	for rows.Next() {
		var handler models.EventHandler
		err := rows.Scan(
			&handler.ID, &handler.EventType, &handler.HandlerName, &handler.ServiceName,
			&handler.Endpoint, &handler.Method, &handler.IsActive, &handler.Priority,
			&handler.Timeout, &handler.CreatedAt, &handler.UpdatedAt,
		)
		if err != nil {
			continue
		}
		handlers = append(handlers, &handler)
	}

	return handlers, nil
}

// CreateEventHandler creates a new event handler configuration
func (r *Repository) CreateEventHandler(ctx context.Context, handler *models.EventHandler) error {
	query := `
		INSERT INTO event_handlers (
			id, event_type, handler_name, service_name, endpoint, method,
			is_active, priority, timeout, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	handler.ID = fmt.Sprintf("handler_%d", time.Now().UnixNano())
	handler.CreatedAt = time.Now()
	handler.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		handler.ID, handler.EventType, handler.HandlerName, handler.ServiceName,
		handler.Endpoint, handler.Method, handler.IsActive, handler.Priority,
		handler.Timeout, handler.CreatedAt, handler.UpdatedAt,
	)

	return err
}

// UpdateEventHandler updates an existing event handler
func (r *Repository) UpdateEventHandler(ctx context.Context, handler *models.EventHandler) error {
	query := `
		UPDATE event_handlers
		SET handler_name = $2, service_name = $3, endpoint = $4, method = $5,
		    is_active = $6, priority = $7, timeout = $8, updated_at = $9
		WHERE id = $1
	`

	handler.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		handler.ID, handler.HandlerName, handler.ServiceName, handler.Endpoint,
		handler.Method, handler.IsActive, handler.Priority, handler.Timeout,
		handler.UpdatedAt,
	)

	return err
}

// GetEventProcessingStats retrieves processing statistics for an event type
func (r *Repository) GetEventProcessingStats(ctx context.Context, eventType string) (*models.EventProcessingStats, error) {
	query := `
		SELECT event_type, total_events, processed_events, failed_events,
			   avg_processing_time, success_rate, last_processed_at,
			   created_at, updated_at
		FROM event_processing_stats WHERE event_type = $1
	`

	var stats models.EventProcessingStats
	var lastProcessedAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, eventType).Scan(
		&stats.EventType, &stats.TotalEvents, &stats.ProcessedEvents,
		&stats.FailedEvents, &stats.AvgProcessingTime, &stats.SuccessRate,
		&lastProcessedAt, &stats.CreatedAt, &stats.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		// Return default stats
		return &models.EventProcessingStats{
			EventType:         eventType,
			TotalEvents:       0,
			ProcessedEvents:   0,
			FailedEvents:      0,
			AvgProcessingTime: 0,
			SuccessRate:       0,
			CreatedAt:         time.Now(),
			UpdatedAt:         time.Now(),
		}, nil
	}

	if lastProcessedAt.Valid {
		stats.LastProcessedAt = &lastProcessedAt.Time
	}

	return &stats, err
}

// UpdateEventProcessingStats updates processing statistics
func (r *Repository) UpdateEventProcessingStats(ctx context.Context, stats *models.EventProcessingStats) error {
	query := `
		INSERT INTO event_processing_stats (
			event_type, total_events, processed_events, failed_events,
			avg_processing_time, success_rate, last_processed_at,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (event_type)
		DO UPDATE SET
			total_events = EXCLUDED.total_events,
			processed_events = EXCLUDED.processed_events,
			failed_events = EXCLUDED.failed_events,
			avg_processing_time = EXCLUDED.avg_processing_time,
			success_rate = EXCLUDED.success_rate,
			last_processed_at = EXCLUDED.last_processed_at,
			updated_at = EXCLUDED.updated_at
	`

	stats.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		stats.EventType, stats.TotalEvents, stats.ProcessedEvents,
		stats.FailedEvents, stats.AvgProcessingTime, stats.SuccessRate,
		stats.LastProcessedAt, stats.CreatedAt, stats.UpdatedAt,
	)

	return err
}

// GetAllEventStats retrieves statistics for all event types
func (r *Repository) GetAllEventStats(ctx context.Context) ([]*models.EventProcessingStats, error) {
	query := `
		SELECT event_type, total_events, processed_events, failed_events,
			   avg_processing_time, success_rate, last_processed_at,
			   created_at, updated_at
		FROM event_processing_stats
		ORDER BY total_events DESC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to get all event stats: %w", err)
	}
	defer rows.Close()

	var stats []*models.EventProcessingStats
	for rows.Next() {
		var stat models.EventProcessingStats
		var lastProcessedAt sql.NullTime

		err := rows.Scan(
			&stat.EventType, &stat.TotalEvents, &stat.ProcessedEvents,
			&stat.FailedEvents, &stat.AvgProcessingTime, &stat.SuccessRate,
			&lastProcessedAt, &stat.CreatedAt, &stat.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if lastProcessedAt.Valid {
			stat.LastProcessedAt = &lastProcessedAt.Time
		}

		stats = append(stats, &stat)
	}

	return stats, nil
}

// CreateDeadLetterEvent creates a dead letter event for failed processing
func (r *Repository) CreateDeadLetterEvent(ctx context.Context, deadLetter *models.DeadLetterEvent) error {
	eventData, _ := json.Marshal(deadLetter.EventData)

	query := `
		INSERT INTO dead_letter_events (
			id, event_id, event_type, event_data, error, error_code,
			failed_at, retry_count, reviewed, reviewed_by, reviewed_at,
			action, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
	`

	deadLetter.ID = fmt.Sprintf("dl_%d", time.Now().UnixNano())
	deadLetter.CreatedAt = time.Now()
	deadLetter.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		deadLetter.ID, deadLetter.EventID, deadLetter.EventType, eventData,
		deadLetter.Error, deadLetter.ErrorCode, deadLetter.FailedAt,
		deadLetter.RetryCount, deadLetter.Reviewed, deadLetter.ReviewedBy,
		deadLetter.ReviewedAt, deadLetter.Action, deadLetter.CreatedAt,
		deadLetter.UpdatedAt,
	)

	return err
}

// GetDeadLetterEvents retrieves dead letter events for review
func (r *Repository) GetDeadLetterEvents(ctx context.Context, limit, offset int) ([]*models.DeadLetterEvent, error) {
	query := `
		SELECT id, event_id, event_type, event_data, error, error_code,
			   failed_at, retry_count, reviewed, reviewed_by, reviewed_at,
			   action, created_at, updated_at
		FROM dead_letter_events
		WHERE reviewed = false
		ORDER BY failed_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.QueryContext(ctx, query, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get dead letter events: %w", err)
	}
	defer rows.Close()

	var events []*models.DeadLetterEvent
	for rows.Next() {
		var event models.DeadLetterEvent
		var eventData []byte
		var reviewedAt sql.NullTime

		err := rows.Scan(
			&event.ID, &event.EventID, &event.EventType, &eventData,
			&event.Error, &event.ErrorCode, &event.FailedAt, &event.RetryCount,
			&event.Reviewed, &event.ReviewedBy, &event.ReviewedAt,
			&event.Action, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			continue
		}

		if reviewedAt.Valid {
			event.ReviewedAt = &reviewedAt.Time
		}

		if err := json.Unmarshal(eventData, &event.EventData); err != nil {
			continue
		}

		events = append(events, &event)
	}

	return events, nil
}

// MarkDeadLetterReviewed marks a dead letter event as reviewed
func (r *Repository) MarkDeadLetterReviewed(ctx context.Context, id string, reviewedBy, action string) error {
	query := `
		UPDATE dead_letter_events
		SET reviewed = true, reviewed_by = $2, reviewed_at = $3, action = $4, updated_at = $3
		WHERE id = $1
	`

	now := time.Now()
	_, err := r.db.ExecContext(ctx, query, id, reviewedBy, now, action)
	return err
}

// CreateEventBatch creates an event batch
func (r *Repository) CreateEventBatch(ctx context.Context, batch *models.EventBatch) error {
	eventIDsData, _ := json.Marshal(batch.EventIDs)

	query := `
		INSERT INTO event_batches (id, event_type, event_ids, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6)
	`

	batch.ID = fmt.Sprintf("batch_%d", time.Now().UnixNano())
	batch.CreatedAt = time.Now()
	batch.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		batch.ID, batch.EventType, eventIDsData, batch.Status,
		batch.CreatedAt, batch.UpdatedAt,
	)

	return err
}

// GetEventBatch retrieves an event batch
func (r *Repository) GetEventBatch(ctx context.Context, id string) (*models.EventBatch, error) {
	query := `SELECT id, event_type, event_ids, status, created_at, updated_at FROM event_batches WHERE id = $1`

	var batch models.EventBatch
	var eventIDsData []byte

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&batch.ID, &batch.EventType, &eventIDsData, &batch.Status,
		&batch.CreatedAt, &batch.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("event batch not found: %s", id)
	}

	if err := json.Unmarshal(eventIDsData, &batch.EventIDs); err != nil {
		return nil, fmt.Errorf("failed to unmarshal event IDs: %w", err)
	}

	return &batch, err
}

// UpdateEventBatchStatus updates the status of an event batch
func (r *Repository) UpdateEventBatchStatus(ctx context.Context, id string, status string) error {
	query := `UPDATE event_batches SET status = $2, updated_at = $3 WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query, id, status, time.Now())
	return err
}

// HealthCheck performs a simple database health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	return r.db.PingContext(ctx)
}
