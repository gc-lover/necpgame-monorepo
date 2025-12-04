// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkGetInbox benchmarks GetInbox handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetInbox(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetInboxParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetInbox(ctx, params)
	}
}

// BenchmarkGetMail benchmarks GetMail handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetMail(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetMailParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetMail(ctx, params)
	}
}

// BenchmarkDeleteMail benchmarks DeleteMail handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkDeleteMail(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeleteMail(ctx)
	}
}

