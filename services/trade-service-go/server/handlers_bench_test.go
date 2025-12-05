// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/trade-service-go/pkg/api"
)

// mockService implements Service for benchmarks
type mockService struct{}

func (m *mockService) CreateTradeSession(ctx context.Context, req *api.CreateTradeRequest) (*api.TradeSessionResponse, error) {
	return nil, nil
}

func (m *mockService) GetTradeSession(ctx context.Context, sessionID string) (*api.TradeSessionResponse, error) {
	return nil, nil
}

func (m *mockService) CancelTradeSession(ctx context.Context, sessionID string) error {
	return nil
}

func (m *mockService) AddTradeItems(ctx context.Context, sessionID string, req *api.AddItemsRequest) (*api.TradeSessionResponse, error) {
	return nil, nil
}

func (m *mockService) AddTradeCurrency(ctx context.Context, sessionID string, req *api.AddCurrencyRequest) (*api.TradeSessionResponse, error) {
	return nil, nil
}

func (m *mockService) SetTradeReady(ctx context.Context, sessionID string, req *api.ReadyRequest) (*api.TradeSessionResponse, error) {
	return nil, nil
}

func (m *mockService) CompleteTrade(ctx context.Context, sessionID string) (*api.TradeCompleteResponse, error) {
	return nil, nil
}

func (m *mockService) GetTradeHistory(ctx context.Context, playerID string, params api.GetTradeHistoryParams) (*api.TradeHistoryResponse, error) {
	return nil, nil
}

// BenchmarkCreateTradeSession benchmarks CreateTradeSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateTradeSession(b *testing.B) {
	service := &mockService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CreateTradeRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateTradeSession(ctx, req)
	}
}

// BenchmarkGetTradeSession benchmarks GetTradeSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetTradeSession(b *testing.B) {
	service := &mockService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetTradeSessionParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetTradeSession(ctx, params)
	}
}

// BenchmarkCancelTradeSession benchmarks CancelTradeSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCancelTradeSession(b *testing.B) {
	service := &mockService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.CancelTradeSessionParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CancelTradeSession(ctx, params)
	}
}

