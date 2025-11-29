package server

import (
	"context"
	"errors"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type WorldEventsServiceInterface interface {
	ListWorldEvents(ctx context.Context, status *api.WorldEventStatus, eventType *api.WorldEventType, scale *api.WorldEventScale, frequency *api.WorldEventFrequency, limit, offset int) ([]api.WorldEvent, error)
	CreateWorldEvent(ctx context.Context, req *api.CreateWorldEventRequest) (*api.WorldEvent, error)
	GetActiveWorldEvents(ctx context.Context) ([]api.WorldEvent, error)
	GetWorldEventsByFrequency(ctx context.Context, frequency api.WorldEventFrequency) ([]api.WorldEvent, error)
	GetWorldEventsByScale(ctx context.Context, scale api.WorldEventScale) ([]api.WorldEvent, error)
	GetWorldEventsByType(ctx context.Context, eventType api.WorldEventType) ([]api.WorldEvent, error)
	GetPlannedWorldEvents(ctx context.Context) ([]api.WorldEvent, error)
	DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error
	GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
	UpdateWorldEvent(ctx context.Context, eventID uuid.UUID, req *api.UpdateWorldEventRequest) (*api.WorldEvent, error)
	ActivateWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
	AnnounceWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
	DeactivateWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
	GetWorldEventAlerts(ctx context.Context) (*api.WorldEventAlertsResponse, error)
	GetWorldEventEngagement(ctx context.Context, eventID uuid.UUID) (*api.WorldEventEngagement, error)
	GetWorldEventImpact(ctx context.Context, eventID uuid.UUID) (*api.WorldEventImpact, error)
	GetWorldEventMetrics(ctx context.Context, eventID uuid.UUID) (*api.WorldEventMetrics, error)
	GetWorldEventsCalendar(ctx context.Context, params *api.GetWorldEventsCalendarParams) (*api.WorldEventsListResponse, error)
	ScheduleWorldEvent(ctx context.Context, req *api.ScheduleWorldEventRequest) (*api.EventSchedule, error)
	GetScheduledWorldEvents(ctx context.Context) ([]api.WorldEvent, error)
	TriggerScheduledWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error)
}

type worldEventsService struct {
	repo   WorldEventsRepositoryInterface
	logger *logrus.Logger
}

func NewWorldEventsService(repo WorldEventsRepositoryInterface) WorldEventsServiceInterface {
	return &worldEventsService{
		repo:   repo,
		logger: GetLogger(),
	}
}

func (s *worldEventsService) ListWorldEvents(ctx context.Context, status *api.WorldEventStatus, eventType *api.WorldEventType, scale *api.WorldEventScale, frequency *api.WorldEventFrequency, limit, offset int) ([]api.WorldEvent, error) {
	return s.repo.ListWorldEvents(ctx, status, eventType, scale, frequency, limit, offset)
}

func (s *worldEventsService) CreateWorldEvent(ctx context.Context, req *api.CreateWorldEventRequest) (*api.WorldEvent, error) {
	return s.repo.CreateWorldEvent(ctx, req)
}

func (s *worldEventsService) GetActiveWorldEvents(ctx context.Context) ([]api.WorldEvent, error) {
	status := api.ACTIVE
	return s.repo.ListWorldEvents(ctx, &status, nil, nil, nil, 100, 0)
}

func (s *worldEventsService) GetWorldEventsByFrequency(ctx context.Context, frequency api.WorldEventFrequency) ([]api.WorldEvent, error) {
	return s.repo.ListWorldEvents(ctx, nil, nil, nil, &frequency, 100, 0)
}

func (s *worldEventsService) GetWorldEventsByScale(ctx context.Context, scale api.WorldEventScale) ([]api.WorldEvent, error) {
	return s.repo.ListWorldEvents(ctx, nil, nil, &scale, nil, 100, 0)
}

func (s *worldEventsService) GetWorldEventsByType(ctx context.Context, eventType api.WorldEventType) ([]api.WorldEvent, error) {
	return s.repo.ListWorldEvents(ctx, nil, &eventType, nil, nil, 100, 0)
}

func (s *worldEventsService) GetPlannedWorldEvents(ctx context.Context) ([]api.WorldEvent, error) {
	status := api.PLANNED
	return s.repo.ListWorldEvents(ctx, &status, nil, nil, nil, 100, 0)
}

func (s *worldEventsService) DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error {
	return s.repo.DeleteWorldEvent(ctx, eventID)
}

func (s *worldEventsService) GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	event, err := s.repo.GetWorldEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}
	if event == nil {
		return nil, errors.New("world event not found")
	}
	return event, nil
}

func (s *worldEventsService) UpdateWorldEvent(ctx context.Context, eventID uuid.UUID, req *api.UpdateWorldEventRequest) (*api.WorldEvent, error) {
	return s.repo.UpdateWorldEvent(ctx, eventID, req)
}

func (s *worldEventsService) ActivateWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	return s.repo.UpdateWorldEventStatus(ctx, eventID, api.ACTIVE)
}

func (s *worldEventsService) AnnounceWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	return s.repo.UpdateWorldEventStatus(ctx, eventID, api.ANNOUNCED)
}

func (s *worldEventsService) DeactivateWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	return s.repo.UpdateWorldEventStatus(ctx, eventID, api.ARCHIVED)
}

func (s *worldEventsService) GetWorldEventAlerts(ctx context.Context) (*api.WorldEventAlertsResponse, error) {
	return s.repo.GetWorldEventAlerts(ctx)
}

func (s *worldEventsService) GetWorldEventEngagement(ctx context.Context, eventID uuid.UUID) (*api.WorldEventEngagement, error) {
	return s.repo.GetWorldEventEngagement(ctx, eventID)
}

func (s *worldEventsService) GetWorldEventImpact(ctx context.Context, eventID uuid.UUID) (*api.WorldEventImpact, error) {
	return s.repo.GetWorldEventImpact(ctx, eventID)
}

func (s *worldEventsService) GetWorldEventMetrics(ctx context.Context, eventID uuid.UUID) (*api.WorldEventMetrics, error) {
	return s.repo.GetWorldEventMetrics(ctx, eventID)
}

func (s *worldEventsService) GetWorldEventsCalendar(ctx context.Context, params *api.GetWorldEventsCalendarParams) (*api.WorldEventsListResponse, error) {
	return s.repo.GetWorldEventsCalendar(ctx, params)
}

func (s *worldEventsService) ScheduleWorldEvent(ctx context.Context, req *api.ScheduleWorldEventRequest) (*api.EventSchedule, error) {
	return s.repo.ScheduleWorldEvent(ctx, req)
}

func (s *worldEventsService) GetScheduledWorldEvents(ctx context.Context) ([]api.WorldEvent, error) {
	return s.repo.GetScheduledWorldEvents(ctx)
}

func (s *worldEventsService) TriggerScheduledWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEvent, error) {
	return s.repo.TriggerScheduledWorldEvent(ctx, eventID)
}

