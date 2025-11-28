package server

import (
	"context"
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/stock-margin-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type MarginService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewMarginService(repository Repository, logger *logrus.Logger) *MarginService {
	return &MarginService{
		repository: repository,
		logger:     logger,
	}
}

func (s *MarginService) GetMarginAccount(ctx context.Context, playerID uuid.UUID) (*api.MarginAccount, error) {
	s.logger.WithField("player_id", playerID).Info("Getting margin account")

	account, err := s.repository.GetMarginAccount(ctx, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get margin account")
		return nil, fmt.Errorf("failed to get margin account: %w", err)
	}

	return account, nil
}

func (s *MarginService) OpenMarginAccount(ctx context.Context, playerID uuid.UUID, initialDeposit float32) (*api.MarginAccount, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id":       playerID,
		"initial_deposit": initialDeposit,
	}).Info("Opening margin account")

	account, err := s.repository.OpenMarginAccount(ctx, playerID, initialDeposit)
	if err != nil {
		s.logger.WithError(err).Error("Failed to open margin account")
		return nil, fmt.Errorf("failed to open margin account: %w", err)
	}

	return account, nil
}

func (s *MarginService) BorrowMargin(ctx context.Context, playerID uuid.UUID, amount float32) (*api.BorrowMarginResponse, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"amount":    amount,
	}).Info("Borrowing margin")

	response, err := s.repository.BorrowMargin(ctx, playerID, amount)
	if err != nil {
		s.logger.WithError(err).Error("Failed to borrow margin")
		return nil, fmt.Errorf("failed to borrow margin: %w", err)
	}

	return response, nil
}

func (s *MarginService) RepayMargin(ctx context.Context, playerID uuid.UUID, amount float32) error {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"amount":    amount,
	}).Info("Repaying margin")

	if err := s.repository.RepayMargin(ctx, playerID, amount); err != nil {
		s.logger.WithError(err).Error("Failed to repay margin")
		return fmt.Errorf("failed to repay margin: %w", err)
	}

	return nil
}

func (s *MarginService) GetMarginCallHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.MarginCall, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"limit":     limit,
		"offset":    offset,
	}).Info("Getting margin call history")

	calls, total, err := s.repository.GetMarginCallHistory(ctx, playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get margin call history")
		return nil, 0, fmt.Errorf("failed to get margin call history: %w", err)
	}

	return calls, total, nil
}

func (s *MarginService) GetRiskHealth(ctx context.Context, playerID uuid.UUID) (*api.RiskHealth, error) {
	s.logger.WithField("player_id", playerID).Info("Getting risk health")

	health, err := s.repository.GetRiskHealth(ctx, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get risk health")
		return nil, fmt.Errorf("failed to get risk health: %w", err)
	}

	return health, nil
}

func (s *MarginService) ListShortPositions(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.ShortPosition, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"limit":     limit,
		"offset":    offset,
	}).Info("Listing short positions")

	positions, total, err := s.repository.ListShortPositions(ctx, playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list short positions")
		return nil, 0, fmt.Errorf("failed to list short positions: %w", err)
	}

	return positions, total, nil
}

func (s *MarginService) OpenShortPosition(ctx context.Context, playerID uuid.UUID, request *api.ShortPositionRequest) (*api.ShortPosition, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"ticker":    request.Ticker,
		"quantity":  request.Quantity,
	}).Info("Opening short position")

	position, err := s.repository.OpenShortPosition(ctx, playerID, request)
	if err != nil {
		s.logger.WithError(err).Error("Failed to open short position")
		return nil, fmt.Errorf("failed to open short position: %w", err)
	}

	return position, nil
}

func (s *MarginService) GetShortPosition(ctx context.Context, positionID uuid.UUID) (*api.ShortPosition, error) {
	s.logger.WithField("position_id", positionID).Info("Getting short position")

	position, err := s.repository.GetShortPosition(ctx, positionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get short position")
		return nil, fmt.Errorf("failed to get short position: %w", err)
	}

	return position, nil
}

func (s *MarginService) CloseShortPosition(ctx context.Context, positionID uuid.UUID) (*api.ClosePositionResponse, error) {
	s.logger.WithField("position_id", positionID).Info("Closing short position")

	response, err := s.repository.CloseShortPosition(ctx, positionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to close short position")
		return nil, fmt.Errorf("failed to close short position: %w", err)
	}

	return response, nil
}

