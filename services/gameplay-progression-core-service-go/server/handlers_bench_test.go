// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-progression-core-service-go/pkg/api"
)

type mockProgressionService struct{}

func (m *mockProgressionService) ValidateProgression(_ context.Context, _ uuid.UUID) (*api.ProgressionValidationResponse, error) {
	return &api.ProgressionValidationResponse{
		Valid:  api.NewOptBool(true),
		Issues: []string{},
	}, nil
}

func (m *mockProgressionService) GetCharacterProgression(_ context.Context, _ uuid.UUID) (*api.CharacterProgression, error) {
	return &api.CharacterProgression{}, nil
}

func (m *mockProgressionService) DistributeAttributePoints(_ context.Context, _ uuid.UUID, _ map[string]int) (*api.CharacterProgression, error) {
	return &api.CharacterProgression{}, nil
}

func (m *mockProgressionService) AddExperience(_ context.Context, _ uuid.UUID, _ int, _ string) (*api.CharacterProgression, error) {
	return &api.CharacterProgression{}, nil
}

func (m *mockProgressionService) DistributeSkillPoints(_ context.Context, _ uuid.UUID, _ map[string]int) (*api.CharacterProgression, error) {
	return &api.CharacterProgression{}, nil
}

// BenchmarkValidateProgression benchmarks ValidateProgression handler
// Target: <100μs per operation, minimal allocs
func BenchmarkValidateProgression(b *testing.B) {
	handlers := NewHandlers(&mockProgressionService{})

	ctx := context.Background()
	req := &api.ValidateProgressionRequest{}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ValidateProgression(ctx, req)
	}
}

// BenchmarkGetCharacterProgression benchmarks GetCharacterProgression handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCharacterProgression(b *testing.B) {
	handlers := NewHandlers(&mockProgressionService{})

	ctx := context.Background()
	params := api.GetCharacterProgressionParams{
		CharacterId: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCharacterProgression(ctx, params)
	}
}

// BenchmarkDistributeAttributePoints benchmarks DistributeAttributePoints handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDistributeAttributePoints(b *testing.B) {
	handlers := NewHandlers(&mockProgressionService{})

	ctx := context.Background()
	req := &api.DistributeAttributePointsRequest{}
	params := api.DistributeAttributePointsParams{
		CharacterId: uuid.New(),
	}
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DistributeAttributePoints(ctx, req, params)
	}
}
