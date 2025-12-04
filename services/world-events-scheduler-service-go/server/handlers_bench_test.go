// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkScheduleWorldEvent benchmarks ScheduleWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkScheduleWorldEvent(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ScheduleWorldEvent(ctx)
	}
}

// BenchmarkGetScheduledWorldEvents benchmarks GetScheduledWorldEvents handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetScheduledWorldEvents(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	params := api.GetScheduledWorldEventsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetScheduledWorldEvents(ctx, params)
	}
}

// BenchmarkTriggerScheduledWorldEvent benchmarks TriggerScheduledWorldEvent handler
// Target: <100μs per operation, minimal allocs
func BenchmarkTriggerScheduledWorldEvent(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerScheduledWorldEvent(ctx)
	}
}

