package server

import (
	"context"
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/stock-options-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type OptionsService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewOptionsService(repository Repository, logger *logrus.Logger) *OptionsService {
	return &OptionsService{
		repository: repository,
		logger:     logger,
	}
}

func (s *OptionsService) ListOptionsContracts(ctx context.Context, ticker string, contractType *string, expirationFrom, expirationTo *openapi_types.Date, limit, offset int) ([]api.OptionsContract, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"ticker":        ticker,
		"contract_type": contractType,
		"limit":         limit,
		"offset":        offset,
	}).Info("Listing options contracts")

	contracts, total, err := s.repository.ListOptionsContracts(ctx, ticker, contractType, expirationFrom, expirationTo, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list options contracts")
		return nil, 0, fmt.Errorf("failed to list options contracts: %w", err)
	}

	return contracts, total, nil
}

func (s *OptionsService) BuyOptionsContract(ctx context.Context, playerID uuid.UUID, contractID uuid.UUID, quantity int) (*api.OptionsPosition, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id":  playerID,
		"contract_id": contractID,
		"quantity":   quantity,
	}).Info("Buying options contract")

	position, err := s.repository.BuyOptionsContract(ctx, playerID, contractID, quantity)
	if err != nil {
		s.logger.WithError(err).Error("Failed to buy options contract")
		return nil, fmt.Errorf("failed to buy options contract: %w", err)
	}

	return position, nil
}

func (s *OptionsService) ListOptionsPositions(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.OptionsPosition, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id": playerID,
		"limit":     limit,
		"offset":    offset,
	}).Info("Listing options positions")

	positions, total, err := s.repository.ListOptionsPositions(ctx, playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list options positions")
		return nil, 0, fmt.Errorf("failed to list options positions: %w", err)
	}

	return positions, total, nil
}

func (s *OptionsService) ExerciseOption(ctx context.Context, positionID uuid.UUID) (*api.ExerciseOptionResponse, error) {
	s.logger.WithField("position_id", positionID).Info("Exercising option")

	response, err := s.repository.ExerciseOption(ctx, positionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to exercise option")
		return nil, fmt.Errorf("failed to exercise option: %w", err)
	}

	return response, nil
}

func (s *OptionsService) GetOptionsGreeks(ctx context.Context, positionID uuid.UUID) (*api.Greeks, error) {
	s.logger.WithField("position_id", positionID).Info("Getting options Greeks")

	greeks, err := s.repository.GetOptionsGreeks(ctx, positionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get options Greeks")
		return nil, fmt.Errorf("failed to get options Greeks: %w", err)
	}

	return greeks, nil
}

