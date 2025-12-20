// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/necpgame/stock-options-service-go/pkg/api"
)

// BenchmarkListOptionsContracts benchmarks ListOptionsContracts handler
// Target: <100μs per operation, minimal allocs
func BenchmarkListOptionsContracts(b *testing.B) {
	handlers := NewOptionsHandlers()

	ctx := context.Background()
	params := api.ListOptionsContractsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListOptionsContracts(ctx, params)
	}
}
