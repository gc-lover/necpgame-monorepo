// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/client-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkTriggerVisualEffect benchmarks TriggerVisualEffect handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkTriggerVisualEffect(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerVisualEffect(ctx)
	}
}

// BenchmarkTriggerAudioEffect benchmarks TriggerAudioEffect handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkTriggerAudioEffect(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerAudioEffect(ctx)
	}
}

// BenchmarkGetEffect benchmarks GetEffect handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkGetEffect(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetEffectParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEffect(ctx, params)
	}
}

