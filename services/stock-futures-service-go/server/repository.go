package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-futures-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	ListFuturesContracts(ctx context.Context, underlying *string, expirationFrom, expirationTo *openapi_types.Date, limit, offset int) ([]api.FuturesContract, int, error)
	OpenFuturesPosition(ctx context.Context, playerID uuid.UUID, contractID uuid.UUID, quantity int) (*api.FuturesPosition, error)
	ListFuturesPositions(ctx context.Context, playerID uuid.UUID, activeOnly bool, limit, offset int) ([]api.FuturesPosition, int, error)
	CloseFuturesPosition(ctx context.Context, positionID uuid.UUID) (*api.ClosePositionResponse, error)
}

type InMemoryRepository struct {
	logger     *logrus.Logger
	contracts  map[uuid.UUID]*api.FuturesContract
	positions  map[uuid.UUID]*api.FuturesPosition
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:    logger,
		contracts: make(map[uuid.UUID]*api.FuturesContract),
		positions: make(map[uuid.UUID]*api.FuturesPosition),
	}
}

func (r *InMemoryRepository) ListFuturesContracts(ctx context.Context, underlying *string, expirationFrom, expirationTo *openapi_types.Date, limit, offset int) ([]api.FuturesContract, int, error) {
	var filtered []api.FuturesContract
	for _, contract := range r.contracts {
		if underlying != nil && contract.Underlying != nil && *contract.Underlying != *underlying {
			continue
		}
		filtered = append(filtered, *contract)
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
		return []api.FuturesContract{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) OpenFuturesPosition(ctx context.Context, playerID uuid.UUID, contractID uuid.UUID, quantity int) (*api.FuturesPosition, error) {
	positionID := uuid.New()
	now := time.Now()
	entryPrice := float32(100.0)
	marginHeld := float32(1000.0)

	position := &api.FuturesPosition{
		PositionId:       func() *openapi_types.UUID { id := openapi_types.UUID(positionID); return &id }(),
		PlayerId:         func() *openapi_types.UUID { id := openapi_types.UUID(playerID); return &id }(),
		ContractId:       func() *openapi_types.UUID { id := openapi_types.UUID(contractID); return &id }(),
		Quantity:         &quantity,
		EntryPrice:       &entryPrice,
		CurrentPrice:     &entryPrice,
		MarginHeld:        &marginHeld,
		Pnl:              func() *float32 { v := float32(0); return &v }(),
		DaysToSettlement: func() *int { v := 30; return &v }(),
		OpenedAt:         &now,
	}

	r.positions[positionID] = position
	return position, nil
}

func (r *InMemoryRepository) ListFuturesPositions(ctx context.Context, playerID uuid.UUID, activeOnly bool, limit, offset int) ([]api.FuturesPosition, int, error) {
	var filtered []api.FuturesPosition
	for _, position := range r.positions {
		if position.PlayerId != nil && uuid.UUID(*position.PlayerId) == playerID {
			filtered = append(filtered, *position)
		}
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
		return []api.FuturesPosition{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) CloseFuturesPosition(ctx context.Context, positionID uuid.UUID) (*api.ClosePositionResponse, error) {
	position, exists := r.positions[positionID]
	if !exists {
		return nil, nil
	}

	now := time.Now()
	realizedPnl := float32(0.0)
	if position.Pnl != nil {
		realizedPnl = *position.Pnl
	}

	response := &api.ClosePositionResponse{
		PositionId:  func() *openapi_types.UUID { id := openapi_types.UUID(positionID); return &id }(),
		RealizedPnl: &realizedPnl,
		ClosedAt:    &now,
	}

	return response, nil
}

