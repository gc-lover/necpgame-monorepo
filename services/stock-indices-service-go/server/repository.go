package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-indices-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	GetAllIndices(ctx context.Context, indexType *string) ([]api.StockIndex, error)
	GetIndex(ctx context.Context, code string) (*api.StockIndexDetailed, error)
	GetIndexConstituents(ctx context.Context, code string, sortBy *string, order *string, limit, offset int) ([]api.IndexConstituent, int, error)
	GetIndexHistory(ctx context.Context, code string, fromDate, toDate *time.Time, interval *string, limit, offset int) ([]api.IndexHistoryEntry, int, error)
	RebalanceIndex(ctx context.Context, code string) error
}

type InMemoryRepository struct {
	logger      *logrus.Logger
	indices     map[string]*api.StockIndexDetailed
	constituents map[string][]api.IndexConstituent
	history     map[string][]api.IndexHistoryEntry
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:       logger,
		indices:      make(map[string]*api.StockIndexDetailed),
		constituents: make(map[string][]api.IndexConstituent),
		history:      make(map[string][]api.IndexHistoryEntry),
	}
}

func (r *InMemoryRepository) GetAllIndices(ctx context.Context, indexType *string) ([]api.StockIndex, error) {
	var result []api.StockIndex
	for code, detailed := range r.indices {
		if indexType != nil && string(detailed.Type) != *indexType {
			continue
		}
		indexTypeValue := api.StockIndexType(detailed.Type)
		index := api.StockIndex{
			Code:              code,
			Name:              detailed.Name,
			Type:              indexTypeValue,
			Description:       detailed.Description,
			CurrentValue:      detailed.CurrentValue,
			Change24h:         detailed.Change24h,
			Change24hPercent:  detailed.Change24hPercent,
			ConstituentsCount: detailed.ConstituentsCount,
			MarketCapTotal:    detailed.MarketCapTotal,
			LastRebalance:     detailed.LastRebalance,
			UpdatedAt:         detailed.UpdatedAt,
		}
		result = append(result, index)
	}
	return result, nil
}

func (r *InMemoryRepository) GetIndex(ctx context.Context, code string) (*api.StockIndexDetailed, error) {
	index, exists := r.indices[code]
	if !exists {
		return nil, nil
	}
	return index, nil
}

func (r *InMemoryRepository) GetIndexConstituents(ctx context.Context, code string, sortBy *string, order *string, limit, offset int) ([]api.IndexConstituent, int, error) {
	constituents, exists := r.constituents[code]
	if !exists {
		return []api.IndexConstituent{}, 0, nil
	}

	total := len(constituents)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.IndexConstituent{}, total, nil
	}

	return constituents[start:end], total, nil
}

func (r *InMemoryRepository) GetIndexHistory(ctx context.Context, code string, fromDate, toDate *time.Time, interval *string, limit, offset int) ([]api.IndexHistoryEntry, int, error) {
	history, exists := r.history[code]
	if !exists {
		return []api.IndexHistoryEntry{}, 0, nil
	}

	var filtered []api.IndexHistoryEntry
	for _, entry := range history {
		if fromDate != nil && entry.Timestamp.Before(*fromDate) {
			continue
		}
		if toDate != nil && entry.Timestamp.After(*toDate) {
			continue
		}
		filtered = append(filtered, entry)
	}

	total := len(filtered)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.IndexHistoryEntry{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) RebalanceIndex(ctx context.Context, code string) error {
	return nil
}

