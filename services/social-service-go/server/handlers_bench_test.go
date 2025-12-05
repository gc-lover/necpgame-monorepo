// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"net/http/httptest"
	"testing"

	"github.com/sirupsen/logrus"
)

// BenchmarkHealthCheck benchmarks HealthCheck handler
// Target: <100μs per operation, minimal allocs
func BenchmarkHealthCheck(b *testing.B) {
	handlers := NewServiceHandlers(logrus.New())

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/health", nil)
		w := httptest.NewRecorder()
		handlers.HealthCheck(w, req)
	}
}
