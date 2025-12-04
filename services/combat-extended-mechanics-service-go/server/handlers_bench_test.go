// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/combat-extended-mechanics-service-go/pkg/api"
)

// BenchmarkActivateCombatImplant benchmarks ActivateCombatImplant handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateCombatImplant(b *testing.B) {
	repo, _ := NewRepository("")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CombatImplantActivationRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateCombatImplant(ctx, req)
	}
}

// BenchmarkAdvancedAim benchmarks AdvancedAim handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAdvancedAim(b *testing.B) {
	repo, _ := NewRepository("")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.AdvancedAimRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.AdvancedAim(ctx, req)
	}
}

// BenchmarkControlRecoil benchmarks ControlRecoil handler
// Target: <100μs per operation, minimal allocs
func BenchmarkControlRecoil(b *testing.B) {
	repo, _ := NewRepository("")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.RecoilControlRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ControlRecoil(ctx, req)
	}
}

