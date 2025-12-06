// Issue: #1516
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/progression-paragon-service-go/pkg/api"
)

func TestParagonHandlers_GetParagonLevels(t *testing.T) {
	// Mock service
	mockService := &MockParagonService{
		levels: &ParagonLevels{
			CharacterID:          uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
			ParagonLevel:         15,
			ParagonPointsTotal:   75,
			ParagonPointsSpent:   60,
			ParagonPointsAvailable: 15,
			ExperienceCurrent:    125000,
			ExperienceRequired:   150000,
			Allocations:          []ParagonAllocation{},
			UpdatedAt:            time.Now(),
		},
	}

	handlers := NewParagonHandlers(mockService)

	params := api.GetParagonLevelsParams{
		CharacterID: uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
	}

	res, err := handlers.GetParagonLevels(context.Background(), params)
	if err != nil {
		t.Fatalf("GetParagonLevels failed: %v", err)
	}

	levels, ok := res.(*api.ParagonLevels)
	if !ok {
		t.Fatalf("Expected *api.ParagonLevels, got %T", res)
	}

	if levels.ParagonLevel.Value != 15 {
		t.Errorf("Expected paragon level 15, got %d", levels.ParagonLevel.Value)
	}
}

func TestParagonHandlers_DistributeParagonPoints(t *testing.T) {
	mockService := &MockParagonService{
		levels: &ParagonLevels{
			CharacterID:          uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
			ParagonLevel:         15,
			ParagonPointsTotal:   75,
			ParagonPointsSpent:   60,
			ParagonPointsAvailable: 15,
			ExperienceCurrent:    125000,
			ExperienceRequired:   150000,
			Allocations:          []ParagonAllocation{},
			UpdatedAt:            time.Now(),
		},
	}

	handlers := NewParagonHandlers(mockService)

	req := &api.DistributeParagonPointsRequest{
		Allocations: []api.DistributeParagonPointsRequestAllocationsItem{
			{
				StatType: api.DistributeParagonPointsRequestAllocationsItemStatTypeStrength,
				Points:   10,
			},
		},
	}

	params := api.DistributeParagonPointsParams{
		CharacterID: uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
	}

	res, err := handlers.DistributeParagonPoints(context.Background(), req, params)
	if err != nil {
		t.Fatalf("DistributeParagonPoints failed: %v", err)
	}

	levels, ok := res.(*api.ParagonLevels)
	if !ok {
		t.Fatalf("Expected *api.ParagonLevels, got %T", res)
	}

	if levels.ParagonLevel.Value != 15 {
		t.Errorf("Expected paragon level 15, got %d", levels.ParagonLevel.Value)
	}
}

func TestParagonHandlers_DistributeParagonPoints_Validation(t *testing.T) {
	mockService := &MockParagonService{}
	handlers := NewParagonHandlers(mockService)

	// Test nil request
	params := api.DistributeParagonPointsParams{
		CharacterID: uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
	}

	res, err := handlers.DistributeParagonPoints(context.Background(), nil, params)
	if err != nil {
		t.Fatalf("DistributeParagonPoints failed: %v", err)
	}

	badReq, ok := res.(*api.DistributeParagonPointsBadRequest)
	if !ok {
		t.Fatalf("Expected BadRequest, got %T", res)
	}

	if badReq.Message != "allocations are required" {
		t.Errorf("Expected 'allocations are required', got '%s'", badReq.Message)
	}
}

func TestParagonHandlers_GetParagonStats(t *testing.T) {
	mockService := &MockParagonService{
		stats: &ParagonStats{
			CharacterID:        uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
			TotalParagonLevels: 15,
			TotalPointsEarned:  75,
			TotalPointsSpent:   60,
			PointsByStat:       map[string]int{"strength": 20},
			GlobalRank:         100,
			Percentile:         85.5,
		},
	}

	handlers := NewParagonHandlers(mockService)

	params := api.GetParagonStatsParams{
		CharacterID: uuid.MustParse("110e8400-e29b-41d4-a716-446655440000"),
	}

	res, err := handlers.GetParagonStats(context.Background(), params)
	if err != nil {
		t.Fatalf("GetParagonStats failed: %v", err)
	}

	stats, ok := res.(*api.ParagonStats)
	if !ok {
		t.Fatalf("Expected *api.ParagonStats, got %T", res)
	}

	if stats.TotalParagonLevels.Value != 15 {
		t.Errorf("Expected total paragon levels 15, got %d", stats.TotalParagonLevels.Value)
	}
}

// Mock service for testing
type MockParagonService struct {
	levels *ParagonLevels
	stats  *ParagonStats
	err    error
}

func (m *MockParagonService) GetParagonLevels(ctx context.Context, characterID uuid.UUID) (*ParagonLevels, error) {
	if m.err != nil {
		return nil, m.err
	}
	return m.levels, nil
}

func (m *MockParagonService) DistributeParagonPoints(ctx context.Context, characterID uuid.UUID, allocations []ParagonAllocation) (*ParagonLevels, error) {
	if m.err != nil {
		return nil, m.err
	}
	return m.levels, nil
}

func (m *MockParagonService) GetParagonStats(ctx context.Context, characterID uuid.UUID) (*ParagonStats, error) {
	if m.err != nil {
		return nil, m.err
	}
	return m.stats, nil
}

func (m *MockParagonService) AddParagonExperience(ctx context.Context, characterID uuid.UUID, amount int64) error {
	return m.err
}

