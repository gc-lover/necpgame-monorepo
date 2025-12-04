// Issue: Performance benchmarks
// Template for service benchmarks
package server

import (
	"context"
	"testing"
	// Import API package - adjust path
	// api "github.com/gc-lover/necpgame-monorepo/services/{service-name}/pkg/api"
)

// BenchmarkGetHandler benchmarks GET handler performance
// Target: <100μs per operation, minimal allocs
func BenchmarkGetHandler(b *testing.B) {
	// Setup
	service := NewService(nil) // Adjust based on service structure
	handlers := NewHandlers(service)

	ctx := context.Background()
	// Adjust params based on actual handler signature
	// params := api.GetParams{ID: uuid.New()}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// Adjust based on actual handler
		// _, _ = handlers.Get(ctx, params)
		_ = handlers
		_ = ctx
	}
}

// BenchmarkPostHandler benchmarks POST handler performance
// Target: <200μs per operation
func BenchmarkPostHandler(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	// req := &api.CreateRequest{...}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// _, _ = handlers.Create(ctx, req)
		_ = handlers
		_ = ctx
	}
}

// BenchmarkListHandler benchmarks LIST handler performance
// Target: <500μs per operation
func BenchmarkListHandler(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	// params := api.ListParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// _, _ = handlers.List(ctx, params)
		_ = handlers
		_ = ctx
	}
}
