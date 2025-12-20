// Issue: #44
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"fmt"
	"strings"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

type Repository interface {
	CreateEvent(ctx context.Context, event *WorldEvent) error
	GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error)
	GetEventByID(ctx context.Context, id uuid.UUID) (*WorldEvent, error)
	UpdateEvent(ctx context.Context, event *WorldEvent) error
	DeleteEvent(ctx context.Context, id uuid.UUID) error
	ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error)
	GetActiveEvents(ctx context.Context) ([]*WorldEvent, error)
	GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error)
	RecordActivation(ctx context.Context, activation *EventActivation) error
	RecordAnnouncement(ctx context.Context, announcement *EventAnnouncement) error
}

type repository struct {
	db     *sql.DB
	logger *zap.Logger
}

func NewRepository(db *sql.DB, logger *zap.Logger) Repository {
	return &repository{
		db:     db,
		logger: logger,
	}
}

func (r *repository) CreateEvent(ctx context.Context, event *WorldEvent) error {
	query := `INSERT INTO world_events.world_events 
		(id, title, description, type, scale, frequency, status, start_time, end_time, created_at, updated_at)
		VALUES ($1, $2, $3, $4::world_event_type, $5::world_event_scale, $6::world_event_frequency, $7::world_event_status, $8, $9, $10, $11)`

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.Name, event.Description, event.Type, event.Scale, event.Frequency,
		event.Status, event.StartTime, event.EndTime, event.CreatedAt, event.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to create event", zap.Error(err), zap.String("event_id", event.ID.String()))
		return err
	}

	// Store effects, triggers, constraints as JSONB in event_history or separate table
	// For now, we'll store them in event_history as metadata
	if len(event.Effects) > 0 || len(event.Triggers) > 0 || len(event.Constraints) > 0 {
		metadata := map[string]interface{}{
			"effects":     json.RawMessage(event.Effects),
			"triggers":    json.RawMessage(event.Triggers),
			"constraints": json.RawMessage(event.Constraints),
		}
		metadataJSON, _ := json.Marshal(metadata)

		historyQuery := `INSERT INTO world_events.event_history (event_id, action, changes, timestamp)
			VALUES ($1, 'CREATED', $2::jsonb, $3)`
		_, err = r.db.ExecContext(ctx, historyQuery, event.ID, metadataJSON, time.Now())
		if err != nil {
			r.logger.Warn("Failed to store event metadata", zap.Error(err))
		}
	}

	return nil
}

func (r *repository) GetEvent(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	query := `SELECT id, title, description, type::text, scale::text, frequency::text, status::text, 
		start_time, end_time, created_at, updated_at
		FROM world_events.world_events WHERE id = $1`

	event := &WorldEvent{}
	var startTime, endTime sql.NullTime

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale, &event.Frequency,
		&event.Status, &startTime, &endTime, &event.CreatedAt, &event.UpdatedAt)

	if err == sql.ErrNoRows {
		return nil, errors.New("event not found")
	}
	if err != nil {
		r.logger.Error("Failed to get event", zap.Error(err), zap.String("event_id", id.String()))
		return nil, err
	}

	if startTime.Valid {
		event.StartTime = &startTime.Time
	}
	if endTime.Valid {
		event.EndTime = &endTime.Time
	}

	// Load effects, triggers, constraints from event_history
	metadataQuery := `SELECT changes FROM world_events.event_history 
		WHERE event_id = $1 AND action = 'CREATED' ORDER BY timestamp DESC LIMIT 1`
	var metadataJSON []byte
	err = r.db.QueryRowContext(ctx, metadataQuery, id).Scan(&metadataJSON)
	if err == nil && len(metadataJSON) > 0 {
		var metadata map[string]json.RawMessage
		if err := json.Unmarshal(metadataJSON, &metadata); err == nil {
			if effects, ok := metadata["effects"]; ok {
				event.Effects = []byte(effects)
			}
			if triggers, ok := metadata["triggers"]; ok {
				event.Triggers = []byte(triggers)
			}
			if constraints, ok := metadata["constraints"]; ok {
				event.Constraints = []byte(constraints)
			}
		}
	}

	return event, nil
}

func (r *repository) GetEventByID(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	return r.GetEvent(ctx, id)
}

func (r *repository) UpdateEvent(ctx context.Context, event *WorldEvent) error {
	query := `UPDATE world_events.world_events 
		SET title = $2, description = $3, type = $4::world_event_type, scale = $5::world_event_scale,
		frequency = $6::world_event_frequency, status = $7::world_event_status,
		start_time = $8, end_time = $9, updated_at = $10
		WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query,
		event.ID, event.Name, event.Description, event.Type, event.Scale, event.Frequency,
		event.Status, event.StartTime, event.EndTime, event.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to update event", zap.Error(err), zap.String("event_id", event.ID.String()))
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rowsAffected == 0 {
		return errors.New("event not found")
	}

	// Store updated effects, triggers, constraints
	if len(event.Effects) > 0 || len(event.Triggers) > 0 || len(event.Constraints) > 0 {
		metadata := map[string]interface{}{
			"effects":     json.RawMessage(event.Effects),
			"triggers":    json.RawMessage(event.Triggers),
			"constraints": json.RawMessage(event.Constraints),
		}
		metadataJSON, _ := json.Marshal(metadata)

		historyQuery := `INSERT INTO world_events.event_history (event_id, action, changes, timestamp)
			VALUES ($1, 'UPDATED', $2::jsonb, $3)`
		_, err = r.db.ExecContext(ctx, historyQuery, event.ID, metadataJSON, time.Now())
		if err != nil {
			r.logger.Warn("Failed to store event metadata update", zap.Error(err))
		}
	}

	return nil
}

func (r *repository) DeleteEvent(ctx context.Context, id uuid.UUID) error {
	// Soft delete by setting status to ARCHIVED
	query := `UPDATE world_events.world_events 
		SET status = 'ARCHIVED'::world_event_status, updated_at = $2
		WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, id, time.Now())
	if err != nil {
		r.logger.Error("Failed to delete event", zap.Error(err), zap.String("event_id", id.String()))
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rowsAffected == 0 {
		return errors.New("event not found")
	}

	// Record in history
	historyQuery := `INSERT INTO world_events.event_history (event_id, action, timestamp)
		VALUES ($1, 'ARCHIVED', $2)`
	_, err = r.db.ExecContext(ctx, historyQuery, id, time.Now())
	if err != nil {
		r.logger.Warn("Failed to record event deletion", zap.Error(err))
	}

	return nil
}

func (r *repository) ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error) {
	// Build WHERE clause safely - use parameter placeholders
	whereParts := []string{"1=1"}
	args := []interface{}{}
	argIndex := 1

	if filter.Status != nil {
		whereParts = append(whereParts, fmt.Sprintf("status = $%d::world_event_status", argIndex))
		args = append(args, *filter.Status)
		argIndex++
	}
	if filter.Type != nil {
		whereParts = append(whereParts, fmt.Sprintf("type = $%d::world_event_type", argIndex))
		args = append(args, *filter.Type)
		argIndex++
	}
	if filter.Scale != nil {
		whereParts = append(whereParts, fmt.Sprintf("scale = $%d::world_event_scale", argIndex))
		args = append(args, *filter.Scale)
		argIndex++
	}
	if filter.Frequency != nil {
		whereParts = append(whereParts, fmt.Sprintf("frequency = $%d::world_event_frequency", argIndex))
		args = append(args, *filter.Frequency)
		argIndex++
	}

	whereClause := strings.Join(whereParts, " AND ")

	// Count total - use safe parameterized query
	countQuery := "SELECT COUNT(*) FROM world_events.world_events WHERE " + whereClause
	var total int
	err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	// Get events with pagination - use safe parameterized query
	limit := filter.Limit
	if limit <= 0 {
		limit = 50
	}
	offset := filter.Offset
	if offset < 0 {
		offset = 0
	}

	query := fmt.Sprintf(`SELECT id, title, description, type::text, scale::text, frequency::text, status::text,
		start_time, end_time, created_at, updated_at
		FROM world_events.world_events WHERE %s ORDER BY created_at DESC LIMIT $%d OFFSET $%d`,
		whereClause, argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to list events", zap.Error(err))
		return nil, 0, err
	}
	defer rows.Close()

	var events []*WorldEvent
	for rows.Next() {
		event := &WorldEvent{}
		var startTime, endTime sql.NullTime

		err := rows.Scan(
			&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale, &event.Frequency,
			&event.Status, &startTime, &endTime, &event.CreatedAt, &event.UpdatedAt)
		if err != nil {
			r.logger.Error("Failed to scan event", zap.Error(err))
			continue
		}

		if startTime.Valid {
			event.StartTime = &startTime.Time
		}
		if endTime.Valid {
			event.EndTime = &endTime.Time
		}

		events = append(events, event)
	}

	return events, total, nil
}

func (r *repository) GetActiveEvents(ctx context.Context) ([]*WorldEvent, error) {
	query := `SELECT id, title, description, type::text, scale::text, frequency::text, status::text,
		start_time, end_time, created_at, updated_at
		FROM world_events.world_events WHERE status = 'ACTIVE'::world_event_status ORDER BY start_time DESC`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get active events", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var events []*WorldEvent
	for rows.Next() {
		event := &WorldEvent{}
		var startTime, endTime sql.NullTime

		err := rows.Scan(
			&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale, &event.Frequency,
			&event.Status, &startTime, &endTime, &event.CreatedAt, &event.UpdatedAt)
		if err != nil {
			r.logger.Error("Failed to scan event", zap.Error(err))
			continue
		}

		if startTime.Valid {
			event.StartTime = &startTime.Time
		}
		if endTime.Valid {
			event.EndTime = &endTime.Time
		}

		events = append(events, event)
	}

	return events, nil
}

func (r *repository) GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error) {
	query := `SELECT id, title, description, type::text, scale::text, frequency::text, status::text,
		start_time, end_time, created_at, updated_at
		FROM world_events.world_events WHERE status = 'PLANNED'::world_event_status ORDER BY start_time ASC`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get planned events", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var events []*WorldEvent
	for rows.Next() {
		event := &WorldEvent{}
		var startTime, endTime sql.NullTime

		err := rows.Scan(
			&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale, &event.Frequency,
			&event.Status, &startTime, &endTime, &event.CreatedAt, &event.UpdatedAt)
		if err != nil {
			r.logger.Error("Failed to scan event", zap.Error(err))
			continue
		}

		if startTime.Valid {
			event.StartTime = &startTime.Time
		}
		if endTime.Valid {
			event.EndTime = &endTime.Time
		}

		events = append(events, event)
	}

	return events, nil
}

func (r *repository) RecordActivation(ctx context.Context, activation *EventActivation) error {
	// Record in event_history
	query := `INSERT INTO world_events.event_history (event_id, action, changes, timestamp)
		VALUES ($1, 'ACTIVATED', $2::jsonb, $3)`

	changes := map[string]interface{}{
		"activated_by": activation.ActivatedBy,
		"reason":       activation.Reason,
	}
	changesJSON, err := json.Marshal(changes)
	if err != nil {
		return err
	}

	_, err = r.db.ExecContext(ctx, query, activation.EventID, changesJSON, activation.ActivatedAt)
	if err != nil {
		r.logger.Error("Failed to record activation", zap.Error(err), zap.String("event_id", activation.EventID.String()))
		return err
	}

	return nil
}

func (r *repository) RecordAnnouncement(ctx context.Context, announcement *EventAnnouncement) error {
	// Record in event_history
	query := `INSERT INTO world_events.event_history (event_id, action, changes, timestamp)
		VALUES ($1, 'ANNOUNCED', $2::jsonb, $3)`

	changes := map[string]interface{}{
		"announced_by": announcement.AnnouncedBy,
		"message":      announcement.Message,
		"channels":     announcement.Channels,
	}
	changesJSON, err := json.Marshal(changes)
	if err != nil {
		return err
	}

	_, err = r.db.ExecContext(ctx, query, announcement.EventID, changesJSON, announcement.AnnouncedAt)
	if err != nil {
		r.logger.Error("Failed to record announcement", zap.Error(err), zap.String("event_id", announcement.EventID.String()))
		return err
	}

	return nil
}
