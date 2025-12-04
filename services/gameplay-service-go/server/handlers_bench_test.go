// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// BenchmarkActivateAbility benchmarks ActivateAbility handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateAbility(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.AbilityActivationRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateAbility(ctx, req)
	}
}

// BenchmarkActivateCombo benchmarks ActivateCombo handler
// Target: <100μs per operation, minimal allocs
func BenchmarkActivateCombo(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	req := &api.ActivateComboRequest{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ActivateCombo(ctx, req)
	}
}

// BenchmarkCreateCombatSession benchmarks CreateCombatSession handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateCombatSession(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger)

	ctx := context.Background()
	req := &api.CreateSessionRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateCombatSession(ctx, req)
	}
}

