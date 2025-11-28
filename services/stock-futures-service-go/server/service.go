package server

import (
	"context"
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/stock-futures-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type FuturesService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewFuturesService(repository Repository, logger *logrus.Logger) *FuturesService {
	return &FuturesService{
		repository: repository,
		logger:     logger,
	}
}

func (s *FuturesService) ListFuturesContracts(ctx context.Context, underlying *string, expirationFrom, expirationTo *openapi_types.Date, limit, offset int) ([]api.FuturesContract, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"underlying":      underlying,
		"expiration_from": expirationFrom,
		"expiration_to":   expirationTo,
		"limit":          limit,
		"offset":         offset,
	}).Info("Listing futures contracts")

	contracts, total, err := s.repository.ListFuturesContracts(ctx, underlying, expirationFrom, expirationTo, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list futures contracts")
		return nil, 0, fmt.Errorf("failed to list futures contracts: %w", err)
	}

	return contracts, total, nil
}

func (s *FuturesService) OpenFuturesPosition(ctx context.Context, playerID uuid.UUID, contractID uuid.UUID, quantity int) (*api.FuturesPosition, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id":  playerID,
		"contract_id": contractID,
		"quantity":   quantity,
	}).Info("Opening futures position")

	position, err := s.repository.OpenFuturesPosition(ctx, playerID, contractID, quantity)
	if err != nil {
		s.logger.WithError(err).Error("Failed to open futures position")
		return nil, fmt.Errorf("failed to open futures position: %w", err)
	}

	return position, nil
}

func (s *FuturesService) ListFuturesPositions(ctx context.Context, playerID uuid.UUID, activeOnly bool, limit, offset int) ([]api.FuturesPosition, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id":  playerID,
		"active_only": activeOnly,
		"limit":      limit,
		"offset":     offset,
	}).Info("Listing futures positions")

	positions, total, err := s.repository.ListFuturesPositions(ctx, playerID, activeOnly, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list futures positions")
		return nil, 0, fmt.Errorf("failed to list futures positions: %w", err)
	}

	return positions, total, nil
}

func (s *FuturesService) CloseFuturesPosition(ctx context.Context, positionID uuid.UUID) (*api.ClosePositionResponse, error) {
	s.logger.WithField("position_id", positionID).Info("Closing futures position")

	response, err := s.repository.CloseFuturesPosition(ctx, positionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to close futures position")
		return nil, fmt.Errorf("failed to close futures position: %w", err)
	}

	return response, nil
}

