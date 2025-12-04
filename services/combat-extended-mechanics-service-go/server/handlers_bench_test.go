// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
)

// BenchmarkActivateCombatImplant benchmarks ActivateCombatImplant handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateCombatImplant(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateCombatImplant(ctx)
	}
}

// BenchmarkAdvancedAim benchmarks AdvancedAim handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAdvancedAim(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.AdvancedAim(ctx)
	}
}

// BenchmarkControlRecoil benchmarks ControlRecoil handler
// Target: <100μs per operation, minimal allocs
func BenchmarkControlRecoil(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ControlRecoil(ctx)
	}
}

