// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/stock-margin-service-go/pkg/api"
)

// BenchmarkGetMarginAccount benchmarks GetMarginAccount handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetMarginAccount(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetMarginAccount(ctx)
	}
}

// BenchmarkBorrowMargin benchmarks BorrowMargin handler
// Target: <100μs per operation, minimal allocs
func BenchmarkBorrowMargin(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.BorrowMarginRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.BorrowMargin(ctx, req)
	}
}

// BenchmarkRepayMargin benchmarks RepayMargin handler
// Target: <100μs per operation, minimal allocs
func BenchmarkRepayMargin(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.RepayMarginRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.RepayMargin(ctx, req)
	}
}

