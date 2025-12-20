// Package server Issue: #44
package server

import (
	"context"
	"database/sql"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Repository - интерфейс для работы с БД
type Repository interface {
	CreateScheduledEvent(ctx context.Context, event *ScheduledEvent) error
	GetScheduledEvents(ctx context.Context) ([]*ScheduledEvent, error)
	GetScheduledEvent(ctx context.Context, id uuid.UUID) (*ScheduledEvent, error)
	UpdateScheduledEvent(ctx context.Context, event *ScheduledEvent) error
	DeleteScheduledEvent(ctx context.Context, id uuid.UUID) error
}

type repository struct {
	db     *sql.DB
	logger *zap.Logger
}

func NewRepository(db *sql.DB, logger *zap.Logger) Repository {
	return &repository{db: db, logger: logger}
}

func (r *repository) CreateScheduledEvent(ctx context.Context, event *ScheduledEvent) error {
	query := `
		INSERT INTO world_event_schedules 
		(id, event_id, scheduled_at, cron_pattern, trigger_type, enabled, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`
	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.EventID, event.ScheduledAt, event.CronPattern,
		event.TriggerType, event.Enabled, event.CreatedAt, event.UpdatedAt,
	)
	return err
}

func (r *repository) GetScheduledEvents(ctx context.Context) ([]*ScheduledEvent, error) {
	query := `
		SELECT id, event_id, scheduled_at, cron_pattern, trigger_type, enabled, created_at, updated_at
		FROM world_event_schedules
		WHERE enabled = true
		ORDER BY scheduled_at ASC
	`
	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var events []*ScheduledEvent
	for rows.Next() {
		event := &ScheduledEvent{}
		if err := rows.Scan(&event.ID, &event.EventID, &event.ScheduledAt, &event.CronPattern,
			&event.TriggerType, &event.Enabled, &event.CreatedAt, &event.UpdatedAt); err != nil {
			continue
		}
		events = append(events, event)
	}
	return events, nil
}

func (r *repository) GetScheduledEvent(ctx context.Context, id uuid.UUID) (*ScheduledEvent, error) {
	query := `
		SELECT id, event_id, scheduled_at, cron_pattern, trigger_type, enabled, created_at, updated_at
		FROM world_event_schedules
		WHERE id = $1
	`
	event := &ScheduledEvent{}
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&event.ID, &event.EventID, &event.ScheduledAt, &event.CronPattern,
		&event.TriggerType, &event.Enabled, &event.CreatedAt, &event.UpdatedAt,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return event, err
}

func (r *repository) UpdateScheduledEvent(ctx context.Context, event *ScheduledEvent) error {
	query := `
		UPDATE world_event_schedules
		SET event_id = $2, scheduled_at = $3, cron_pattern = $4, trigger_type = $5, 
		    enabled = $6, updated_at = $7
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.EventID, event.ScheduledAt, event.CronPattern,
		event.TriggerType, event.Enabled, event.UpdatedAt,
	)
	return err
}

func (r *repository) DeleteScheduledEvent(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM world_event_schedules WHERE id = $1`
	_, err := r.db.ExecContext(ctx, query, id)
	return err
}
