// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/progression-experience-service-go/pkg/api"
)

// BenchmarkAddExperience benchmarks AddExperience handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAddExperience(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.AddExperienceRequest{
		PlayerID:         uuid.New(),
		ExperienceAmount: 100,
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.AddExperience(ctx, req)
	}
}

// BenchmarkGetPlayerLevel benchmarks GetPlayerLevel handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerLevel(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	params := api.GetPlayerLevelParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerLevel(ctx, params)
	}
}

// BenchmarkCalculateExperience benchmarks CalculateExperience handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateExperience(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	req := &api.CalculateExperienceRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateExperience(ctx, req)
	}
}

