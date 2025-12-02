// Issue: #44
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Repository - интерфейс для работы с БД
type Repository interface {
	// Events CRUD
	CreateEvent(ctx context.Context, event *WorldEvent) error
	GetEventByID(ctx context.Context, id uuid.UUID) (*WorldEvent, error)
	UpdateEvent(ctx context.Context, event *WorldEvent) error
	DeleteEvent(ctx context.Context, id uuid.UUID) error
	ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error)
	
	// Event state
	GetActiveEvents(ctx context.Context) ([]*WorldEvent, error)
	GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error)
	
	// Event activation
	RecordActivation(ctx context.Context, activation *EventActivation) error
	RecordAnnouncement(ctx context.Context, announcement *EventAnnouncement) error
}

type repository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewRepository создает новый репозиторий
func NewRepository(db *sql.DB, logger *zap.Logger) Repository {
	return &repository{
		db:     db,
		logger: logger,
	}
}

func (r *repository) CreateEvent(ctx context.Context, event *WorldEvent) error {
	query := `
		INSERT INTO world_events (
			id, name, description, type, scale, frequency, status,
			start_time, end_time, effects, triggers, constraints, metadata,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)
	`

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.Name, event.Description, event.Type, event.Scale,
		event.Frequency, event.Status, event.StartTime, event.EndTime,
		event.Effects, event.Triggers, event.Constraints, event.Metadata,
		event.CreatedAt, event.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create event", zap.Error(err))
		return err
	}

	return nil
}

func (r *repository) GetEventByID(ctx context.Context, id uuid.UUID) (*WorldEvent, error) {
	query := `
		SELECT id, name, description, type, scale, frequency, status,
			   start_time, end_time, effects, triggers, constraints, metadata,
			   created_at, updated_at
		FROM world_events
		WHERE id = $1
	`

	event := &WorldEvent{}
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale,
		&event.Frequency, &event.Status, &event.StartTime, &event.EndTime,
		&event.Effects, &event.Triggers, &event.Constraints, &event.Metadata,
		&event.CreatedAt, &event.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.Error("Failed to get event", zap.Error(err))
		return nil, err
	}

	return event, nil
}

func (r *repository) UpdateEvent(ctx context.Context, event *WorldEvent) error {
	query := `
		UPDATE world_events
		SET name = $2, description = $3, type = $4, scale = $5, frequency = $6,
			status = $7, start_time = $8, end_time = $9, effects = $10,
			triggers = $11, constraints = $12, metadata = $13, updated_at = $14
		WHERE id = $1
	`

	event.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.Name, event.Description, event.Type, event.Scale,
		event.Frequency, event.Status, event.StartTime, event.EndTime,
		event.Effects, event.Triggers, event.Constraints, event.Metadata,
		event.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to update event", zap.Error(err))
		return err
	}

	return nil
}

func (r *repository) DeleteEvent(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM world_events WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query, id)
	if err != nil {
		r.logger.Error("Failed to delete event", zap.Error(err))
		return err
	}

	return nil
}

func (r *repository) ListEvents(ctx context.Context, filter EventFilter) ([]*WorldEvent, int, error) {
	// Build WHERE clause
	where := "WHERE 1=1"
	args := []interface{}{}
	argNum := 1

	if filter.Status != nil {
		where += fmt.Sprintf(" AND status = $%d", argNum)
		args = append(args, *filter.Status)
		argNum++
	}

	if filter.Type != nil {
		where += fmt.Sprintf(" AND type = $%d", argNum)
		args = append(args, *filter.Type)
		argNum++
	}

	if filter.Scale != nil {
		where += fmt.Sprintf(" AND scale = $%d", argNum)
		args = append(args, *filter.Scale)
		argNum++
	}

	if filter.Frequency != nil {
		where += fmt.Sprintf(" AND frequency = $%d", argNum)
		args = append(args, *filter.Frequency)
		argNum++
	}

	// Count total
	countQuery := "SELECT COUNT(*) FROM world_events " + where
	var total int
	err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to count events", zap.Error(err))
		return nil, 0, err
	}

	// Get events
	query := fmt.Sprintf(`
		SELECT id, name, description, type, scale, frequency, status,
			   start_time, end_time, effects, triggers, constraints, metadata,
			   created_at, updated_at
		FROM world_events
		%s
		ORDER BY created_at DESC
		LIMIT $%d OFFSET $%d
	`, where, argNum, argNum+1)

	args = append(args, filter.Limit, filter.Offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to list events", zap.Error(err))
		return nil, 0, err
	}
	defer rows.Close()

	events := []*WorldEvent{}
	for rows.Next() {
		event := &WorldEvent{}
		err := rows.Scan(
			&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale,
			&event.Frequency, &event.Status, &event.StartTime, &event.EndTime,
			&event.Effects, &event.Triggers, &event.Constraints, &event.Metadata,
			&event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan event", zap.Error(err))
			continue
		}
		events = append(events, event)
	}

	return events, total, nil
}

func (r *repository) GetActiveEvents(ctx context.Context) ([]*WorldEvent, error) {
	query := `
		SELECT id, name, description, type, scale, frequency, status,
			   start_time, end_time, effects, triggers, constraints, metadata,
			   created_at, updated_at
		FROM world_events
		WHERE status = 'active'
		ORDER BY start_time DESC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get active events", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	events := []*WorldEvent{}
	for rows.Next() {
		event := &WorldEvent{}
		err := rows.Scan(
			&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale,
			&event.Frequency, &event.Status, &event.StartTime, &event.EndTime,
			&event.Effects, &event.Triggers, &event.Constraints, &event.Metadata,
			&event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan event", zap.Error(err))
			continue
		}
		events = append(events, event)
	}

	return events, nil
}

func (r *repository) GetPlannedEvents(ctx context.Context) ([]*WorldEvent, error) {
	query := `
		SELECT id, name, description, type, scale, frequency, status,
			   start_time, end_time, effects, triggers, constraints, metadata,
			   created_at, updated_at
		FROM world_events
		WHERE status = 'planned'
		ORDER BY start_time ASC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		r.logger.Error("Failed to get planned events", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	events := []*WorldEvent{}
	for rows.Next() {
		event := &WorldEvent{}
		err := rows.Scan(
			&event.ID, &event.Name, &event.Description, &event.Type, &event.Scale,
			&event.Frequency, &event.Status, &event.StartTime, &event.EndTime,
			&event.Effects, &event.Triggers, &event.Constraints, &event.Metadata,
			&event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan event", zap.Error(err))
			continue
		}
		events = append(events, event)
	}

	return events, nil
}

func (r *repository) RecordActivation(ctx context.Context, activation *EventActivation) error {
	query := `
		INSERT INTO world_event_activations (event_id, activated_at, activated_by, reason)
		VALUES ($1, $2, $3, $4)
	`

	_, err := r.db.ExecContext(ctx, query,
		activation.EventID, activation.ActivatedAt, activation.ActivatedBy, activation.Reason,
	)

	if err != nil {
		r.logger.Error("Failed to record activation", zap.Error(err))
		return err
	}

	return nil
}

func (r *repository) RecordAnnouncement(ctx context.Context, announcement *EventAnnouncement) error {
	query := `
		INSERT INTO world_event_announcements (event_id, announced_at, announced_by, message, channels)
		VALUES ($1, $2, $3, $4, $5)
	`

	channelsJSON, err := json.Marshal(announcement.Channels)
	if err != nil {
		return err
	}

	_, err = r.db.ExecContext(ctx, query,
		announcement.EventID, announcement.AnnouncedAt, announcement.AnnouncedBy,
		announcement.Message, channelsJSON,
	)

	if err != nil {
		r.logger.Error("Failed to record announcement", zap.Error(err))
		return err
	}

	return nil
}

