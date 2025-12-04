// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-implants-maintenance-service-go/pkg/api"
)

// BenchmarkModifyImplant benchmarks ModifyImplant handler
// Target: <100μs per operation, minimal allocs
func BenchmarkModifyImplant(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.ModifyRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ModifyImplant(ctx, req)
	}
}

// BenchmarkRepairImplant benchmarks RepairImplant handler
// Target: <100μs per operation, minimal allocs
func BenchmarkRepairImplant(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.RepairRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.RepairImplant(ctx, req)
	}
}

// BenchmarkUpgradeImplant benchmarks UpgradeImplant handler
// Target: <100μs per operation, minimal allocs
func BenchmarkUpgradeImplant(b *testing.B) {
	handlers := NewHandlers()

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.UpgradeRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.UpgradeImplant(ctx, req)
	}
}

