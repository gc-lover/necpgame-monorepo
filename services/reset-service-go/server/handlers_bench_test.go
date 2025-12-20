// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/pkg/api"
)

type benchResetService struct{}

func (b *benchResetService) GetResetStats(_ context.Context) (*models.ResetStats, error) {
	return &models.ResetStats{}, nil
}
func (b *benchResetService) GetResetHistory(_ context.Context, _ *models.ResetType, _, _ int) (*models.ResetListResponse, error) {
	return &models.ResetListResponse{}, nil
}
func (b *benchResetService) TriggerReset(_ context.Context, _ models.ResetType) error {
	return nil
}

// BenchmarkGetResetStats benchmarks GetResetStats handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetResetStats(b *testing.B) {
	service := &benchResetService{}
	handlers := NewHandlers(service)

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetResetStats(ctx)
	}
}

// BenchmarkGetResetHistory benchmarks GetResetHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetResetHistory(b *testing.B) {
	service := &benchResetService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetResetHistoryParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetResetHistory(ctx, params)
	}
}

// BenchmarkTriggerReset benchmarks TriggerReset handler
// Target: <100μs per operation, minimal allocs
func BenchmarkTriggerReset(b *testing.B) {
	service := &benchResetService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.TriggerResetRequest{Type: api.ResetTypeDaily}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerReset(ctx, req)
	}
}
