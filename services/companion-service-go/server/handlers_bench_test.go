// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/necpgame/companion-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkHealthCheck benchmarks HealthCheck handler
// Target: <100Ојs per operation, minimal allocs
func BenchmarkHealthCheck(b *testing.B) {
	logger := GetLogger()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.HealthCheck(ctx)
	}
}

