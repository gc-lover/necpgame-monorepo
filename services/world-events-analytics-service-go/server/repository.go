// Issue: #44
package server

import (
	"context"
	"database/sql"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

type Repository interface {
	RecordEventMetrics(ctx context.Context, metrics *EventMetrics) error
	GetEventMetrics(ctx context.Context, eventID uuid.UUID) (*EventMetrics, error)
	GetEventAnalytics(ctx context.Context, eventID uuid.UUID) (*EventAnalytics, error)
}

type repository struct {
	db     *sql.DB
	logger *zap.Logger
}

func NewRepository(db *sql.DB, logger *zap.Logger) Repository {
	return &repository{db: db, logger: logger}
}

func (r *repository) RecordEventMetrics(ctx context.Context, metrics *EventMetrics) error {
	query := `
		INSERT INTO world_event_metrics 
		(event_id, participant_count, completion_rate, average_duration, total_rewards, player_engagement, recorded_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`
	_, err := r.db.ExecContext(ctx, query,
		metrics.EventID, metrics.ParticipantCount, metrics.CompletionRate,
		metrics.AverageDuration, metrics.TotalRewards, metrics.PlayerEngagement, metrics.RecordedAt,
	)
	return err
}

func (r *repository) GetEventMetrics(ctx context.Context, eventID uuid.UUID) (*EventMetrics, error) {
	query := `
		SELECT event_id, participant_count, completion_rate, average_duration, 
		       total_rewards, player_engagement, recorded_at
		FROM world_event_metrics
		WHERE event_id = $1
		ORDER BY recorded_at DESC
		LIMIT 1
	`
	metrics := &EventMetrics{}
	err := r.db.QueryRowContext(ctx, query, eventID).Scan(
		&metrics.EventID, &metrics.ParticipantCount, &metrics.CompletionRate,
		&metrics.AverageDuration, &metrics.TotalRewards, &metrics.PlayerEngagement, &metrics.RecordedAt,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return metrics, err
}

func (r *repository) GetEventAnalytics(ctx context.Context, eventID uuid.UUID) (*EventAnalytics, error) {
	query := `
		SELECT e.id, e.type, e.scale, e.start_time, e.end_time,
		       m.participant_count, m.completion_rate, m.average_duration,
		       m.total_rewards, m.player_engagement
		FROM world_events e
		LEFT JOIN world_event_metrics m ON e.id = m.event_id
		WHERE e.id = $1
	`
	
	analytics := &EventAnalytics{Metrics: &EventMetrics{}}
	err := r.db.QueryRowContext(ctx, query, eventID).Scan(
		&analytics.EventID, &analytics.Type, &analytics.Scale,
		&analytics.StartTime, &analytics.EndTime,
		&analytics.Metrics.ParticipantCount, &analytics.Metrics.CompletionRate,
		&analytics.Metrics.AverageDuration, &analytics.Metrics.TotalRewards,
		&analytics.Metrics.PlayerEngagement,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return analytics, err
}























