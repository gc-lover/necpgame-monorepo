// Issue: #131
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/trade-service-go/pkg/api"
)

type Service interface {
	CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest) (*api.TradeSessionResponse, error)
	GetTradeSession(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error)
	CancelTradeSession(ctx context.Context, sessionID string) error
	AddTradeItems(ctx context.Context, sessionID string, req *api.AddItemsRequest) (*api.TradeSessionResponse, error)
	AddTradeCurrency(ctx context.Context, sessionID string, req *api.AddCurrencyRequest) (*api.TradeSessionResponse, error)
	SetTradeReady(ctx context.Context, sessionID string, req *api.ReadyRequest) (*api.TradeSessionResponse, error)
	CompleteTrade(ctx context.Context, sessionID string) (*api.TradeCompleteResponse, error)
	GetTradeHistory(ctx context.Context, playerID string, params api.GetTradeHistoryParams) (*api.TradeHistoryResponse, error)
}

type TradeService struct {
	repository Repository
}

func NewTradeService(repository Repository) Service {
	return &TradeService{repository: repository}
}

func (s *TradeService) CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest) (*api.TradeSessionResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *TradeService) GetTradeSession(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *TradeService) CancelTradeSession(ctx context.Context, sessionID string) error {
	return nil
}

func (s *TradeService) AddTradeItems(ctx context.Context, sessionID string, req *api.AddItemsRequest) (*api.TradeSessionResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *TradeService) AddTradeCurrency(ctx context.Context, sessionID string, req *api.AddCurrencyRequest) (*api.TradeSessionResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *TradeService) SetTradeReady(ctx context.Context, sessionID string, req *api.ReadyRequest) (*api.TradeSessionResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *TradeService) CompleteTrade(ctx context.Context, sessionID string) (*api.TradeCompleteResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *TradeService) GetTradeHistory(ctx context.Context, playerID string, params api.GetTradeHistoryParams) (*api.TradeHistoryResponse, error) {
	return &api.TradeHistoryResponse{History: &[]api.TradeHistoryEntry{}}, nil
}




