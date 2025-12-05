// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/sirupsen/logrus"
)

// BenchmarkHealthCheck benchmarks HealthCheck handler
// Target: <100μs per operation, minimal allocs
func BenchmarkHealthCheck(b *testing.B) {
	handlers := NewServiceHandlers(logrus.New())

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.HealthCheck(ctx)
	}
}
