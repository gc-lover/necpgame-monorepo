package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-events-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type EventsService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewEventsService(repository Repository, logger *logrus.Logger) *EventsService {
	return &EventsService{
		repository: repository,
		logger:     logger,
	}
}

func (s *EventsService) GetStockEventImpacts(ctx context.Context, stockID uuid.UUID, status *string, limit, offset int) ([]api.EventImpact, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"stock_id": stockID,
		"status":   status,
		"limit":    limit,
		"offset":   offset,
	}).Info("Getting stock event impacts")

	impacts, total, err := s.repository.GetStockEventImpacts(ctx, stockID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get stock event impacts")
		return nil, 0, fmt.Errorf("failed to get stock event impacts: %w", err)
	}

	return impacts, total, nil
}

func (s *EventsService) GetStockEventHistory(ctx context.Context, stockID uuid.UUID, eventType *string, fromDate, toDate *time.Time, limit, offset int) ([]api.StockEventHistoryEntry, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"stock_id":  stockID,
		"event_type": eventType,
		"limit":     limit,
		"offset":    offset,
	}).Info("Getting stock event history")

	history, total, err := s.repository.GetStockEventHistory(ctx, stockID, eventType, fromDate, toDate, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get stock event history")
		return nil, 0, fmt.Errorf("failed to get stock event history: %w", err)
	}

	return history, total, nil
}

func (s *EventsService) GetAllActiveImpacts(ctx context.Context, eventType *string, limit, offset int) ([]api.EventImpact, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"event_type": eventType,
		"limit":      limit,
		"offset":     offset,
	}).Info("Getting all active impacts")

	impacts, total, err := s.repository.GetAllActiveImpacts(ctx, eventType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get all active impacts")
		return nil, 0, fmt.Errorf("failed to get all active impacts: %w", err)
	}

	return impacts, total, nil
}

func (s *EventsService) ApplyEventImpact(ctx context.Context, request *api.EventApplicationRequest) (*api.EventImpact, error) {
	s.logger.WithFields(map[string]interface{}{
		"stock_id":  request.StockId,
		"event_type": request.EventType,
		"event_id":  request.EventId,
	}).Info("Applying event impact")

	impact, err := s.repository.ApplyEventImpact(ctx, request)
	if err != nil {
		s.logger.WithError(err).Error("Failed to apply event impact")
		return nil, fmt.Errorf("failed to apply event impact: %w", err)
	}

	return impact, nil
}

func (s *EventsService) SimulateEventImpact(ctx context.Context, request *api.EventSimulationRequest) (*api.EventSimulationResult, error) {
	s.logger.WithFields(map[string]interface{}{
		"stock_id":  request.StockId,
		"event_type": request.EventType,
	}).Info("Simulating event impact")

	simulation, err := s.repository.SimulateEventImpact(ctx, request)
	if err != nil {
		s.logger.WithError(err).Error("Failed to simulate event impact")
		return nil, fmt.Errorf("failed to simulate event impact: %w", err)
	}

	return simulation, nil
}

func (s *EventsService) ReverseEventImpact(ctx context.Context, impactID uuid.UUID) error {
	s.logger.WithField("impact_id", impactID).Info("Reversing event impact")

	if err := s.repository.ReverseEventImpact(ctx, impactID); err != nil {
		s.logger.WithError(err).Error("Failed to reverse event impact")
		return fmt.Errorf("failed to reverse event impact: %w", err)
	}

	return nil
}

