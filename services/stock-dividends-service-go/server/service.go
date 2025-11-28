package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-dividends-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type DividendsService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewDividendsService(repository Repository, logger *logrus.Logger) *DividendsService {
	return &DividendsService{
		repository: repository,
		logger:     logger,
	}
}

func (s *DividendsService) GetDividendSchedule(ctx context.Context, stockID uuid.UUID, limit, offset int) ([]api.DividendSchedule, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"stock_id": stockID,
		"limit":    limit,
		"offset":   offset,
	}).Info("Getting dividend schedule")

	schedules, total, err := s.repository.GetDividendSchedule(ctx, stockID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get dividend schedule")
		return nil, 0, fmt.Errorf("failed to get dividend schedule: %w", err)
	}

	return schedules, total, nil
}

func (s *DividendsService) GetPlayerDividendPayments(ctx context.Context, playerID uuid.UUID, stockID *uuid.UUID, status *string, fromDate, toDate *time.Time, limit, offset int) ([]api.DividendPayment, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"stock_id":  stockID,
		"status":    status,
		"limit":     limit,
		"offset":    offset,
	}).Info("Getting player dividend payments")

	payments, total, err := s.repository.GetPlayerDividendPayments(ctx, playerID, stockID, status, fromDate, toDate, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player dividend payments")
		return nil, 0, fmt.Errorf("failed to get player dividend payments: %w", err)
	}

	return payments, total, nil
}

func (s *DividendsService) GetPlayerDRIPSettings(ctx context.Context, playerID uuid.UUID) (*api.DRIPSettings, error) {
	s.logger.WithField("player_id", playerID).Info("Getting player DRIP settings")

	settings, err := s.repository.GetPlayerDRIPSettings(ctx, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player DRIP settings")
		return nil, fmt.Errorf("failed to get player DRIP settings: %w", err)
	}

	return settings, nil
}

func (s *DividendsService) UpdatePlayerDRIPSettings(ctx context.Context, playerID uuid.UUID, update *api.DRIPSettingsUpdate) (*api.DRIPSettings, error) {
	s.logger.WithField("player_id", playerID).Info("Updating player DRIP settings")

	settings, err := s.repository.UpdatePlayerDRIPSettings(ctx, playerID, update)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update player DRIP settings")
		return nil, fmt.Errorf("failed to update player DRIP settings: %w", err)
	}

	return settings, nil
}

func (s *DividendsService) CreateDividendSchedule(ctx context.Context, create *api.DividendScheduleCreate) (*api.DividendSchedule, error) {
	s.logger.WithFields(map[string]interface{}{
		"stock_id": create.StockId,
		"frequency": create.Frequency,
	}).Info("Creating dividend schedule")

	schedule, err := s.repository.CreateDividendSchedule(ctx, create)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create dividend schedule")
		return nil, fmt.Errorf("failed to create dividend schedule: %w", err)
	}

	return schedule, nil
}

func (s *DividendsService) ProcessDividendPayment(ctx context.Context, scheduleID uuid.UUID) error {
	s.logger.WithField("schedule_id", scheduleID).Info("Processing dividend payment")

	if err := s.repository.ProcessDividendPayment(ctx, scheduleID); err != nil {
		s.logger.WithError(err).Error("Failed to process dividend payment")
		return fmt.Errorf("failed to process dividend payment: %w", err)
	}

	return nil
}

