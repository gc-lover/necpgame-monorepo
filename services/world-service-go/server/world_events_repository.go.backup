package server

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/necpgame/world-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type WorldEventsRepositoryInterface interface {
	ListWorldEvents(ctx context.Context, status *api.WorldEventStatus, eventType *api.WorldEventType, scale *api.WorldEventScale, frequency *api.WorldEventFrequency, limit, offset int) ([]api.WorldEvent, error)
	CreateWorldEvent(ctx context.Context, req *api.CreateWorldEventRequest) (*api.WorldEvent, error)
	DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error
	GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
	UpdateWorldEvent(ctx context.Context, eventID uuid.UUID, req *api.UpdateWorldEventRequest) (*api.WorldEvent, error)
	UpdateWorldEventStatus(ctx context.Context, eventID uuid.UUID, status api.WorldEventStatus) (*api.WorldEvent, error)
	GetWorldEventAlerts(ctx context.Context) (*api.WorldEventAlertsResponse, error)
	GetWorldEventEngagement(ctx context.Context, eventID uuid.UUID) (*api.WorldEventEngagement, error)
	GetWorldEventImpact(ctx context.Context, eventID uuid.UUID) (*api.WorldEventImpact, error)
	GetWorldEventMetrics(ctx context.Context, eventID uuid.UUID) (*api.WorldEventMetrics, error)
	GetWorldEventsCalendar(ctx context.Context, params *api.GetWorldEventsCalendarParams) (*api.WorldEventsListResponse, error)
	ScheduleWorldEvent(ctx context.Context, req *api.ScheduleWorldEventRequest) (*api.EventSchedule, error)
	GetScheduledWorldEvents(ctx context.Context) ([]api.WorldEvent, error)
	TriggerScheduledWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
}

type worldEventsRepository struct {
	db     *sqlx.DB
	logger *logrus.Logger
}

func NewWorldEventsRepository(db *sqlx.DB) WorldEventsRepositoryInterface {
	return &worldEventsRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *worldEventsRepository) ListWorldEvents(ctx context.Context, status *api.WorldEventStatus, eventType *api.WorldEventType, scale *api.WorldEventScale, frequency *api.WorldEventFrequency, limit, offset int) ([]api.WorldEvent, error) {
	query := `SELECT id, title, description, type, scale, frequency, status, start_time, end_time, duration, cooldown_duration, max_concurrent, target_regions, target_factions, prerequisites, effects, created_at, updated_at FROM world.world_events WHERE 1=1`
	args := []interface{}{}
	argPos := 1

	if status != nil {
		query += fmt.Sprintf(" AND status = $%d", argPos)
		args = append(args, string(*status))
		argPos++
	}
	if eventType != nil {
		query += fmt.Sprintf(" AND type = $%d", argPos)
		args = append(args, string(*eventType))
		argPos++
	}
	if scale != nil {
		query += fmt.Sprintf(" AND scale = $%d", argPos)
		args = append(args, string(*scale))
		argPos++
	}
	if frequency != nil {
		query += fmt.Sprintf(" AND frequency = $%d", argPos)
		args = append(args, string(*frequency))
		argPos++
	}

	query += fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", argPos, argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		r.logger.WithError(err).Error("Failed to list world events")
		return nil, fmt.Errorf("failed to list world events: %w", err)
	}
	defer rows.Close()

	var events []api.WorldEvent
	for rows.Next() {
		var event api.WorldEvent
		err := rows.Scan(
			&event.Id, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
			&event.Status, &event.StartTime, &event.EndTime, &event.Duration, &event.CooldownDuration,
			&event.MaxConcurrent, &event.TargetRegions, &event.TargetFactions, &event.Prerequisites,
			&event.Effects, &event.CreatedAt, &event.UpdatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan world event")
			return nil, fmt.Errorf("failed to scan world event: %w", err)
		}
		events = append(events, event)
	}

	return events, nil
}

func (r *worldEventsRepository) CreateWorldEvent(ctx context.Context, req *api.CreateWorldEventRequest) (*api.WorldEvent, error) {
	eventID := uuid.New()
	query := `INSERT INTO world.world_events (id, title, description, type, scale, frequency, status, start_time, end_time, duration, cooldown_duration, max_concurrent, target_regions, target_factions, prerequisites, effects, created_at, updated_at) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, NOW(), NOW()) RETURNING id, title, description, type, scale, frequency, status, start_time, end_time, duration, cooldown_duration, max_concurrent, target_regions, target_factions, prerequisites, effects, created_at, updated_at`

	status := api.PLANNED
	event := &api.WorldEvent{
		Id:              openapi_types.UUID(eventID),
		Title:           req.Title,
		Description:     req.Description,
		Type:            req.Type,
		Scale:           req.Scale,
		Frequency:       req.Frequency,
		Status:          status,
		StartTime:       req.StartTime,
		EndTime:         req.EndTime,
		Duration:        req.Duration,
		CooldownDuration: req.CooldownDuration,
		MaxConcurrent:   req.MaxConcurrent,
		TargetRegions:   req.TargetRegions,
		TargetFactions:  req.TargetFactions,
		Prerequisites:   req.Prerequisites,
		Effects:         convertEffectsFromRequest(req.Effects),
	}

	err := r.db.QueryRowContext(ctx, query,
		eventID, req.Title, req.Description, string(req.Type), string(req.Scale), string(req.Frequency),
		string(status), req.StartTime, req.EndTime, req.Duration, req.CooldownDuration,
		req.MaxConcurrent, req.TargetRegions, req.TargetFactions, req.Prerequisites, req.Effects,
	).Scan(
		&event.Id, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
		&event.Status, &event.StartTime, &event.EndTime, &event.Duration, &event.CooldownDuration,
		&event.MaxConcurrent, &event.TargetRegions, &event.TargetFactions, &event.Prerequisites,
		&event.Effects, &event.CreatedAt, &event.UpdatedAt,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to create world event")
		return nil, fmt.Errorf("failed to create world event: %w", err)
	}

	return event, nil
}

func (r *worldEventsRepository) DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error {
	query := `DELETE FROM world.world_events WHERE id = $1`
	result, err := r.db.ExecContext(ctx, query, eventID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to delete world event")
		return fmt.Errorf("failed to delete world event: %w", err)
	}
		rowsAffected, _ := result.RowsAffected()
		if rowsAffected == 0 {
		return errors.New("world event not found")
	}
	return nil
}

func (r *worldEventsRepository) GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	query := `SELECT id, title, description, type, scale, frequency, status, start_time, end_time, duration, cooldown_duration, max_concurrent, target_regions, target_factions, prerequisites, effects, created_at, updated_at FROM world.world_events WHERE id = $1`
	event := &api.WorldEvent{}
	err := r.db.QueryRowContext(ctx, query, eventID).Scan(
		&event.Id, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
		&event.Status, &event.StartTime, &event.EndTime, &event.Duration, &event.CooldownDuration,
		&event.MaxConcurrent, &event.TargetRegions, &event.TargetFactions, &event.Prerequisites,
		&event.Effects, &event.CreatedAt, &event.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).Error("Failed to get world event")
		return nil, fmt.Errorf("failed to get world event: %w", err)
	}
	return event, nil
}

func (r *worldEventsRepository) UpdateWorldEvent(ctx context.Context, eventID uuid.UUID, req *api.UpdateWorldEventRequest) (*api.WorldEvent, error) {
	query := `UPDATE world.world_events SET title = COALESCE($2, title), description = COALESCE($3, description), type = COALESCE($4, type), scale = COALESCE($5, scale), frequency = COALESCE($6, frequency), start_time = COALESCE($7, start_time), end_time = COALESCE($8, end_time), duration = COALESCE($9, duration), cooldown_duration = COALESCE($10, cooldown_duration), max_concurrent = COALESCE($11, max_concurrent), target_regions = COALESCE($12, target_regions), target_factions = COALESCE($13, target_factions), prerequisites = COALESCE($14, prerequisites), effects = COALESCE($15, effects), updated_at = NOW() WHERE id = $1 RETURNING id, title, description, type, scale, frequency, status, start_time, end_time, duration, cooldown_duration, max_concurrent, target_regions, target_factions, prerequisites, effects, created_at, updated_at`

	var title, description *string
	var eventType *api.WorldEventType
	var scale *api.WorldEventScale
	var frequency *api.WorldEventFrequency

	if req.Title != nil {
		title = req.Title
	}
	if req.Description != nil {
		description = req.Description
	}
	if req.Type != nil {
		eventType = req.Type
	}
	if req.Scale != nil {
		scale = req.Scale
	}
	if req.Frequency != nil {
		frequency = req.Frequency
	}

	event := &api.WorldEvent{}
	err := r.db.QueryRowContext(ctx, query,
		eventID, title, description, eventType, scale, frequency,
		req.StartTime, req.EndTime, req.Duration, req.CooldownDuration,
		req.MaxConcurrent, req.TargetRegions, req.TargetFactions, req.Prerequisites, req.Effects,
	).Scan(
		&event.Id, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
		&event.Status, &event.StartTime, &event.EndTime, &event.Duration, &event.CooldownDuration,
		&event.MaxConcurrent, &event.TargetRegions, &event.TargetFactions, &event.Prerequisites,
		&event.Effects, &event.CreatedAt, &event.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, errors.New("world event not found")
		}
		r.logger.WithError(err).Error("Failed to update world event")
		return nil, fmt.Errorf("failed to update world event: %w", err)
	}
	return event, nil
}

func (r *worldEventsRepository) UpdateWorldEventStatus(ctx context.Context, eventID uuid.UUID, status api.WorldEventStatus) (*api.WorldEvent, error) {
	query := `UPDATE world.world_events SET status = $2, updated_at = NOW() WHERE id = $1 RETURNING id, title, description, type, scale, frequency, status, start_time, end_time, duration, cooldown_duration, max_concurrent, target_regions, target_factions, prerequisites, effects, created_at, updated_at`
	event := &api.WorldEvent{}
	err := r.db.QueryRowContext(ctx, query, eventID, string(status)).Scan(
		&event.Id, &event.Title, &event.Description, &event.Type, &event.Scale, &event.Frequency,
		&event.Status, &event.StartTime, &event.EndTime, &event.Duration, &event.CooldownDuration,
		&event.MaxConcurrent, &event.TargetRegions, &event.TargetFactions, &event.Prerequisites,
		&event.Effects, &event.CreatedAt, &event.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, errors.New("world event not found")
		}
		r.logger.WithError(err).Error("Failed to update world event status")
		return nil, fmt.Errorf("failed to update world event status: %w", err)
	}
	return event, nil
}

func (r *worldEventsRepository) GetWorldEventAlerts(ctx context.Context) (*api.WorldEventAlertsResponse, error) {
	alerts := []struct {
		AlertType *string                                 `json:"alert_type,omitempty"`
		CreatedAt *time.Time                              `json:"created_at,omitempty"`
		EventId   *openapi_types.UUID                     `json:"event_id,omitempty"`
		Id        *openapi_types.UUID                     `json:"id,omitempty"`
		Message   *string                                 `json:"message,omitempty"`
		Severity  *api.WorldEventAlertsResponseAlertsSeverity `json:"severity,omitempty"`
	}{}
	return &api.WorldEventAlertsResponse{
		Alerts: alerts,
	}, nil
}

func (r *worldEventsRepository) GetWorldEventEngagement(ctx context.Context, eventID uuid.UUID) (*api.WorldEventEngagement, error) {
	return &api.WorldEventEngagement{
		EventId:                openapi_types.UUID(eventID),
		TotalPlayers:           new(int),
		ActivePlayers:          new(int),
		AverageSessionDuration: new(string),
	}, nil
}

func (r *worldEventsRepository) GetWorldEventImpact(ctx context.Context, eventID uuid.UUID) (*api.WorldEventImpact, error) {
	return &api.WorldEventImpact{
		EventId: openapi_types.UUID(eventID),
	}, nil
}

func (r *worldEventsRepository) GetWorldEventMetrics(ctx context.Context, eventID uuid.UUID) (*api.WorldEventMetrics, error) {
	return &api.WorldEventMetrics{
		EventId: openapi_types.UUID(eventID),
		Uptime:  "0h",
	}, nil
}

func (r *worldEventsRepository) GetWorldEventsCalendar(ctx context.Context, params *api.GetWorldEventsCalendarParams) (*api.WorldEventsListResponse, error) {
	return &api.WorldEventsListResponse{
		Events: []api.WorldEvent{},
	}, nil
}

func (r *worldEventsRepository) ScheduleWorldEvent(ctx context.Context, req *api.ScheduleWorldEventRequest) (*api.EventSchedule, error) {
	scheduleID := uuid.New()
	query := `INSERT INTO world.event_schedules (id, event_id, scheduled_time, trigger_type, trigger_parameters, status, created_at) VALUES ($1, $2, $3, $4, $5, $6, NOW()) RETURNING id, event_id, scheduled_time, trigger_type, trigger_parameters, status, created_at`
	schedule := &api.EventSchedule{
		Id:            openapi_types.UUID(scheduleID),
		EventId:       req.EventId,
		ScheduledTime: req.ScheduledTime,
		TriggerType:   api.EventTriggerType("manual"),
		TriggerParameters: req.TriggerParameters,
		Status:        api.EventScheduleStatus("pending"),
	}
	err := r.db.QueryRowContext(ctx, query, scheduleID, req.EventId, req.ScheduledTime, "manual", req.TriggerParameters, "pending").Scan(
		&schedule.Id, &schedule.EventId, &schedule.ScheduledTime, &schedule.TriggerType, &schedule.TriggerParameters, &schedule.Status, &schedule.CreatedAt,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to schedule world event")
		return nil, fmt.Errorf("failed to schedule world event: %w", err)
	}
	return schedule, nil
}

func (r *worldEventsRepository) GetScheduledWorldEvents(ctx context.Context) ([]api.WorldEvent, error) {
	return []api.WorldEvent{}, nil
}

func (r *worldEventsRepository) TriggerScheduledWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	event, err := r.GetWorldEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("scheduled world event not found")
	}
	return r.UpdateWorldEventStatus(ctx, eventID, api.ACTIVE)
}

