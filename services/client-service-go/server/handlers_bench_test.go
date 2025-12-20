// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/client-service-go/pkg/api"
)

// mockWeaponEffectsService implements WeaponEffectsServiceInterface for benchmarks
type mockWeaponEffectsService struct{}

func (m *mockWeaponEffectsService) TriggerVisualEffect(_ context.Context, _ string, _ string, _ map[string]float64, _ *uuid.UUID, _ map[string]interface{}) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponEffectsService) TriggerAudioEffect(_ context.Context, _ string, _ string, _ string, _ map[string]float64, _ *float64, _ *float64) (uuid.UUID, error) {
	return uuid.New(), nil
}

func (m *mockWeaponEffectsService) GetEffect(_ context.Context, _ uuid.UUID) (map[string]interface{}, error) {
	return nil, nil
}

// BenchmarkTriggerVisualEffect benchmarks TriggerVisualEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkTriggerVisualEffect(b *testing.B) {
	mockService := &mockWeaponEffectsService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	req := &api.TriggerVisualEffectRequest{
		EffectType:   api.TriggerVisualEffectRequestEffectTypeEnvironmentDestruction,
		MechanicType: "weapon",
		Position: api.Position3D{
			X: 0.0,
			Y: 0.0,
			Z: 0.0,
		},
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerVisualEffect(ctx, req)
	}
}

// BenchmarkTriggerAudioEffect benchmarks TriggerAudioEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkTriggerAudioEffect(b *testing.B) {
	mockService := &mockWeaponEffectsService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	req := &api.TriggerAudioEffectRequest{
		EffectType:   api.TriggerAudioEffectRequestEffectTypeLaser,
		MechanicType: "weapon",
		SoundID:      "sound123",
		Position: api.Position3D{
			X: 0.0,
			Y: 0.0,
			Z: 0.0,
		},
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.TriggerAudioEffect(ctx, req)
	}
}

// BenchmarkGetEffect benchmarks GetEffect handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetEffect(b *testing.B) {
	mockService := &mockWeaponEffectsService{}
	handlers := NewHandlers(mockService)

	ctx := context.Background()
	params := api.GetEffectParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetEffect(ctx, params)
	}
}
