// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetFeedback benchmarks GetFeedback handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetFeedback(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetFeedbackParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetFeedback(ctx, params)
	}
}

// BenchmarkGetPlayerFeedback benchmarks GetPlayerFeedback handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetPlayerFeedback(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetPlayerFeedbackParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerFeedback(ctx, params)
	}
}

// BenchmarkSubmitFeedback benchmarks SubmitFeedback handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkSubmitFeedback(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SubmitFeedback(ctx)
	}
}

