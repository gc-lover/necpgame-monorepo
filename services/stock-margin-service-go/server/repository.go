package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/stock-margin-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	GetMarginAccount(ctx context.Context, playerID uuid.UUID) (*api.MarginAccount, error)
	OpenMarginAccount(ctx context.Context, playerID uuid.UUID, initialDeposit float32) (*api.MarginAccount, error)
	BorrowMargin(ctx context.Context, playerID uuid.UUID, amount float32) (*api.BorrowMarginResponse, error)
	RepayMargin(ctx context.Context, playerID uuid.UUID, amount float32) error
	GetMarginCallHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.MarginCall, int, error)
	GetRiskHealth(ctx context.Context, playerID uuid.UUID) (*api.RiskHealth, error)
	ListShortPositions(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.ShortPosition, int, error)
	OpenShortPosition(ctx context.Context, playerID uuid.UUID, request *api.ShortPositionRequest) (*api.ShortPosition, error)
	GetShortPosition(ctx context.Context, positionID uuid.UUID) (*api.ShortPosition, error)
	CloseShortPosition(ctx context.Context, positionID uuid.UUID) (*api.ClosePositionResponse, error)
}

type InMemoryRepository struct {
	logger         *logrus.Logger
	marginAccounts map[uuid.UUID]*api.MarginAccount
	shortPositions map[uuid.UUID]*api.ShortPosition
	marginCalls    map[uuid.UUID][]api.MarginCall
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:         logger,
		marginAccounts: make(map[uuid.UUID]*api.MarginAccount),
		shortPositions: make(map[uuid.UUID]*api.ShortPosition),
		marginCalls:    make(map[uuid.UUID][]api.MarginCall),
	}
}

func (r *InMemoryRepository) GetMarginAccount(ctx context.Context, playerID uuid.UUID) (*api.MarginAccount, error) {
	account, exists := r.marginAccounts[playerID]
	if !exists {
		return nil, nil
	}
	return account, nil
}

func (r *InMemoryRepository) OpenMarginAccount(ctx context.Context, playerID uuid.UUID, initialDeposit float32) (*api.MarginAccount, error) {
	accountID := uuid.New()
	account := &api.MarginAccount{
		AccountId:        func() *openapi_types.UUID { id := openapi_types.UUID(accountID); return &id }(),
		PlayerId:         func() *openapi_types.UUID { id := openapi_types.UUID(playerID); return &id }(),
		Balance:          &initialDeposit,
		Equity:           &initialDeposit,
		BorrowedAmount:   func() *float32 { v := float32(0); return &v }(),
		AvailableCredit: &initialDeposit,
		Leverage:         func() *float32 { v := float32(2.0); return &v }(),
		MaintenanceMargin: func() *float32 { v := float32(0.3); return &v }(),
		MarginHealth:     func() *float32 { v := float32(100.0); return &v }(),
	}

	r.marginAccounts[playerID] = account
	return account, nil
}

func (r *InMemoryRepository) BorrowMargin(ctx context.Context, playerID uuid.UUID, amount float32) (*api.BorrowMarginResponse, error) {
	response := &api.BorrowMarginResponse{
		BorrowedAmount:     &amount,
		CollateralRequired: &amount,
		InterestRate:       func() *float32 { v := float32(0.05); return &v }(),
		Leverage:           func() *float32 { v := float32(2.0); return &v }(),
	}
	return response, nil
}

func (r *InMemoryRepository) RepayMargin(ctx context.Context, playerID uuid.UUID, amount float32) error {
	return nil
}

func (r *InMemoryRepository) GetMarginCallHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.MarginCall, int, error) {
	calls, exists := r.marginCalls[playerID]
	if !exists {
		return []api.MarginCall{}, 0, nil
	}

	total := len(calls)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.MarginCall{}, total, nil
	}

	return calls[start:end], total, nil
}

func (r *InMemoryRepository) GetRiskHealth(ctx context.Context, playerID uuid.UUID) (*api.RiskHealth, error) {
	health := &api.RiskHealth{
		AtRisk:            func() *bool { v := false; return &v }(),
		MarginHealth:       func() *float32 { v := float32(100.0); return &v }(),
		MarginCallWarning: func() *bool { v := false; return &v }(),
		MaintenanceMargin: func() *float32 { v := float32(0.3); return &v }(),
	}
	return health, nil
}

func (r *InMemoryRepository) ListShortPositions(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]api.ShortPosition, int, error) {
	var filtered []api.ShortPosition
	for _, position := range r.shortPositions {
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
		return []api.ShortPosition{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) OpenShortPosition(ctx context.Context, playerID uuid.UUID, request *api.ShortPositionRequest) (*api.ShortPosition, error) {
	positionID := uuid.New()
	position := &api.ShortPosition{
		PositionId: func() *openapi_types.UUID { id := openapi_types.UUID(positionID); return &id }(),
		PlayerId:   func() *openapi_types.UUID { id := openapi_types.UUID(playerID); return &id }(),
		Ticker:     &request.Ticker,
		Quantity:   &request.Quantity,
	}
	r.shortPositions[positionID] = position
	return position, nil
}

func (r *InMemoryRepository) GetShortPosition(ctx context.Context, positionID uuid.UUID) (*api.ShortPosition, error) {
	position, exists := r.shortPositions[positionID]
	if !exists {
		return nil, nil
	}
	return position, nil
}

func (r *InMemoryRepository) CloseShortPosition(ctx context.Context, positionID uuid.UUID) (*api.ClosePositionResponse, error) {
	response := &api.ClosePositionResponse{
		PositionId:  func() *openapi_types.UUID { id := openapi_types.UUID(positionID); return &id }(),
		RealizedPnl: func() *float32 { v := float32(0.0); return &v }(),
	}
	return response, nil
}

