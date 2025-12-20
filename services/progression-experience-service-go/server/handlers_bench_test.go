// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/progression-experience-service-go/pkg/api"
)

type mockExperienceService struct{}

func (m *mockExperienceService) AddExperience(_ context.Context, _ uuid.UUID, _ int, _ string) (*api.CharacterProgression, error) {
	return &api.CharacterProgression{}, nil
}

func (m *mockExperienceService) CalculateExperience(_ context.Context, _ int, _ map[string]float32) (*api.ExperienceCalculationResponse, error) {
	return &api.ExperienceCalculationResponse{}, nil
}

func (m *mockExperienceService) CheckLevelUp(_ context.Context, _ uuid.UUID) (*api.LevelUpCheckResponse, error) {
	return &api.LevelUpCheckResponse{}, nil
}

func (m *mockExperienceService) GetLevelRequirements(_ context.Context, _ int) (*api.LevelRequirementsResponse, error) {
	return &api.LevelRequirementsResponse{}, nil
}

func (m *mockExperienceService) GetPlayerLevel(_ context.Context, _ uuid.UUID) (*api.PlayerLevelResponse, error) {
	return &api.PlayerLevelResponse{}, nil
}

// BenchmarkAddExperience benchmarks AddExperience handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAddExperience(b *testing.B) {
	handlers := NewHandlers(&mockExperienceService{})

	ctx := context.Background()
	req := &api.AddExperienceRequest{
		PlayerID:         uuid.New(),
		ExperienceAmount: 100,
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.AddExperience(ctx, req)
	}
}

// BenchmarkGetPlayerLevel benchmarks GetPlayerLevel handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetPlayerLevel(b *testing.B) {
	handlers := NewHandlers(&mockExperienceService{})

	ctx := context.Background()
	params := api.GetPlayerLevelParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetPlayerLevel(ctx, params)
	}
}

// BenchmarkCalculateExperience benchmarks CalculateExperience handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCalculateExperience(b *testing.B) {
	handlers := NewHandlers(&mockExperienceService{})

	ctx := context.Background()
	req := &api.CalculateExperienceRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CalculateExperience(ctx, req)
	}
}
