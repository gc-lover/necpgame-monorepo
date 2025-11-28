package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/stock-options-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	ListOptionsContracts(ctx context.Context, ticker string, contractType *string, expirationFrom, expirationTo *openapi_types.Date, limit, offset int) ([]api.OptionsContract, int, error)
	BuyOptionsContract(ctx context.Context, playerID uuid.UUID, contractID uuid.UUID, quantity int) (*api.OptionsPosition, error)
	ListOptionsPositions(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.OptionsPosition, int, error)
	ExerciseOption(ctx context.Context, positionID uuid.UUID) (*api.ExerciseOptionResponse, error)
	GetOptionsGreeks(ctx context.Context, positionID uuid.UUID) (*api.Greeks, error)
}

type InMemoryRepository struct {
	logger     *logrus.Logger
	contracts  map[uuid.UUID]*api.OptionsContract
	positions  map[uuid.UUID]*api.OptionsPosition
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:    logger,
		contracts: make(map[uuid.UUID]*api.OptionsContract),
		positions: make(map[uuid.UUID]*api.OptionsPosition),
	}
}

func (r *InMemoryRepository) ListOptionsContracts(ctx context.Context, ticker string, contractType *string, expirationFrom, expirationTo *openapi_types.Date, limit, offset int) ([]api.OptionsContract, int, error) {
	var filtered []api.OptionsContract
	for _, contract := range r.contracts {
		if contract.Ticker != nil && *contract.Ticker != ticker {
			continue
		}
		if contractType != nil && contract.Type != nil && string(*contract.Type) != *contractType {
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
		return []api.OptionsContract{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) BuyOptionsContract(ctx context.Context, playerID uuid.UUID, contractID uuid.UUID, quantity int) (*api.OptionsPosition, error) {
	positionID := uuid.New()
	premiumPaid := float32(100.0)

	position := &api.OptionsPosition{
		PositionId:        func() *openapi_types.UUID { id := openapi_types.UUID(positionID); return &id }(),
		PlayerId:          func() *openapi_types.UUID { id := openapi_types.UUID(playerID); return &id }(),
		ContractId:        func() *openapi_types.UUID { id := openapi_types.UUID(contractID); return &id }(),
		Quantity:          &quantity,
		PremiumPaid:       &premiumPaid,
		CurrentValue:      &premiumPaid,
		Pnl:               func() *float32 { v := float32(0); return &v }(),
		DaysToExpiration: func() *int { v := 7; return &v }(),
	}

	r.positions[positionID] = position
	return position, nil
}

func (r *InMemoryRepository) ListOptionsPositions(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.OptionsPosition, int, error) {
	var filtered []api.OptionsPosition
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
		return []api.OptionsPosition{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) ExerciseOption(ctx context.Context, positionID uuid.UUID) (*api.ExerciseOptionResponse, error) {
	response := &api.ExerciseOptionResponse{
		PositionId:     func() *openapi_types.UUID { id := openapi_types.UUID(positionID); return &id }(),
		RealizedPnl:    func() *float32 { v := float32(0.0); return &v }(),
		SharesAcquired: func() *int { v := 100; return &v }(),
		TotalCost:      func() *float32 { v := float32(1000.0); return &v }(),
	}
	return response, nil
}

func (r *InMemoryRepository) GetOptionsGreeks(ctx context.Context, positionID uuid.UUID) (*api.Greeks, error) {
	greeks := &api.Greeks{
		Delta: func() *float32 { v := float32(0.5); return &v }(),
		Gamma: func() *float32 { v := float32(0.1); return &v }(),
		Theta: func() *float32 { v := float32(-0.05); return &v }(),
		Vega:  func() *float32 { v := float32(0.2); return &v }(),
	}
	return greeks, nil
}

