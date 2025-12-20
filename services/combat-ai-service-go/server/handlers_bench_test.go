// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-ai-service-go/pkg/api"
)

// BenchmarkGetAIProfile benchmarks GetAIProfile handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetAIProfile(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetAIProfileParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetAIProfile(ctx, params)
	}
}

// BenchmarkGetAIProfileTelemetry benchmarks GetAIProfileTelemetry handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetAIProfileTelemetry(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetAIProfileTelemetryParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetAIProfileTelemetry(ctx, params)
	}
}

// BenchmarkListAIProfiles benchmarks ListAIProfiles handler
// Target: <100μs per operation, minimal allocs
func BenchmarkListAIProfiles(b *testing.B) {
	repo := &Repository{}
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.ListAIProfilesParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListAIProfiles(ctx, params)
	}
}
