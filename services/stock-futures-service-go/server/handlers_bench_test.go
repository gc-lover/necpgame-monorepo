// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/necpgame/stock-futures-service-go/pkg/api"
)

// BenchmarkListFuturesContracts benchmarks ListFuturesContracts handler
// Target: <100μs per operation, minimal allocs
func BenchmarkListFuturesContracts(b *testing.B) {
	handlers := NewFuturesHandlers()

	ctx := context.Background()
	params := api.ListFuturesContractsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListFuturesContracts(ctx, params)
	}
}
