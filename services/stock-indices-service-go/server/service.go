package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-indices-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type IndicesService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewIndicesService(repository Repository, logger *logrus.Logger) *IndicesService {
	return &IndicesService{
		repository: repository,
		logger:     logger,
	}
}

func (s *IndicesService) GetAllIndices(ctx context.Context, indexType *string) ([]api.StockIndex, error) {
	s.logger.WithField("type", indexType).Info("Getting all indices")

	indices, err := s.repository.GetAllIndices(ctx, indexType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get all indices")
		return nil, fmt.Errorf("failed to get all indices: %w", err)
	}

	return indices, nil
}

func (s *IndicesService) GetIndex(ctx context.Context, code string) (*api.StockIndexDetailed, error) {
	s.logger.WithField("code", code).Info("Getting index")

	index, err := s.repository.GetIndex(ctx, code)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get index")
		return nil, fmt.Errorf("failed to get index: %w", err)
	}

	return index, nil
}

func (s *IndicesService) GetIndexConstituents(ctx context.Context, code string, sortBy *string, order *string, limit, offset int) ([]api.IndexConstituent, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"code":    code,
		"sort_by": sortBy,
		"order":   order,
		"limit":   limit,
		"offset":  offset,
	}).Info("Getting index constituents")

	constituents, total, err := s.repository.GetIndexConstituents(ctx, code, sortBy, order, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get index constituents")
		return nil, 0, fmt.Errorf("failed to get index constituents: %w", err)
	}

	return constituents, total, nil
}

func (s *IndicesService) GetIndexHistory(ctx context.Context, code string, fromDate, toDate *time.Time, interval *string, limit, offset int) ([]api.IndexHistoryEntry, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"code":     code,
		"from_date": fromDate,
		"to_date":   toDate,
		"interval":  interval,
		"limit":     limit,
		"offset":    offset,
	}).Info("Getting index history")

	history, total, err := s.repository.GetIndexHistory(ctx, code, fromDate, toDate, interval, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get index history")
		return nil, 0, fmt.Errorf("failed to get index history: %w", err)
	}

	return history, total, nil
}

func (s *IndicesService) RebalanceIndex(ctx context.Context, code string) error {
	s.logger.WithField("code", code).Info("Rebalancing index")

	if err := s.repository.RebalanceIndex(ctx, code); err != nil {
		s.logger.WithError(err).Error("Failed to rebalance index")
		return fmt.Errorf("failed to rebalance index: %w", err)
	}

	return nil
}

