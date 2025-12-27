package service

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/internal/repository"
	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/pkg/api"
)

func TestService_CalculateCompatibilityMatrix(t *testing.T) {
	// Setup
	repo := repository.NewRepository()
	svc := NewService(repo)

	characterID := uuid.New()
	ctx := context.Background()

	// Test with mock data (empty character)
	matrix, err := svc.CalculateCompatibilityMatrix(ctx, characterID)
	require.NoError(t, err)

	// Verify response structure
	assert.NotNil(t, matrix)
	assert.Empty(t, matrix.Engrams)
	assert.Empty(t, matrix.CompatibilityPairs)
	assert.Equal(t, api.CompatibilityLevelFullCompatibility, matrix.OverallCompatibility)
	assert.Equal(t, 0.0, matrix.SynergyBonus)
}

func TestService_CheckEngramCompatibility(t *testing.T) {
	tests := []struct {
		name           string
		engramIDs      []uuid.UUID
		expectError    bool
		expectCanInstall bool
	}{
		{
			name:           "too few engrams",
			engramIDs:      []uuid.UUID{uuid.New()},
			expectError:    true,
			expectCanInstall: false,
		},
		{
			name:           "valid two engrams",
			engramIDs:      []uuid.UUID{uuid.New(), uuid.New()},
			expectError:    false,
			expectCanInstall: true,
		},
		{
			name:           "valid three engrams",
			engramIDs:      []uuid.UUID{uuid.New(), uuid.New(), uuid.New()},
			expectError:    false,
			expectCanInstall: true,
		},
		{
			name:           "too many engrams",
			engramIDs:      []uuid.UUID{uuid.New(), uuid.New(), uuid.New(), uuid.New()},
			expectError:    true,
			expectCanInstall: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			// Setup
			repo := repository.NewRepository()
			svc := NewService(repo)

			characterID := uuid.New()
			ctx := context.Background()

			// Execute
			result, err := svc.CheckEngramCompatibility(ctx, characterID, tt.engramIDs)

			// Verify
			if tt.expectError {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
				assert.NotNil(t, result)
				assert.Equal(t, tt.engramIDs, result.EngramIDs)
				assert.Equal(t, tt.expectCanInstall, result.CanInstall)
				assert.GreaterOrEqual(t, result.CompatibilityPercentage, -50.0)
				assert.LessOrEqual(t, result.CompatibilityPercentage, 50.0)
			}
		})
	}
}

func TestService_ResolveConflict(t *testing.T) {
	// Setup
	repo := repository.NewRepository()
	svc := NewService(repo)

	characterID := uuid.New()
	ctx := context.Background()

	request := api.ResolveConflictRequest{
		ConflictID:     uuid.New(),
		ResolutionType: api.ResolutionTypeBalance,
	}

	// Execute
	response, err := svc.ResolveConflict(ctx, characterID, request)
	require.NoError(t, err)

	// Verify
	assert.NotNil(t, response)
	assert.Equal(t, request.ConflictID, response.ConflictID)
	assert.True(t, response.Success)
	assert.WithinDuration(t, time.Now(), response.ResolvedAt, time.Second)
}

func TestService_CreateConflictEvent(t *testing.T) {
	// Setup
	repo := repository.NewRepository()
	svc := NewService(repo)

	characterID := uuid.New()
	ctx := context.Background()

	request := api.CreateConflictEventRequest{
		Engram1ID:   uuid.New(),
		Engram2ID:   uuid.New(),
		ConflictType: api.ConflictEventTypeDominanceStruggle,
		EventData:   map[string]interface{}{"intensity": "high"},
	}

	// Execute
	event, err := svc.CreateConflictEvent(ctx, characterID, request)
	require.NoError(t, err)

	// Verify
	assert.NotNil(t, event)
	assert.Equal(t, characterID, event.CharacterID)
	assert.Equal(t, request.ConflictType, event.ConflictType)
	assert.Equal(t, request.EventData, event.EventData)
	assert.Contains(t, event.EngramIDs, request.Engram1ID)
	assert.Contains(t, event.EngramIDs, request.Engram2ID)
	assert.Len(t, event.EngramIDs, 2)
	assert.WithinDuration(t, time.Now(), event.CreatedAt, time.Second)
}

func TestService_GetActiveConflicts(t *testing.T) {
	// Setup
	repo := repository.NewRepository()
	svc := NewService(repo)

	characterID := uuid.New()
	ctx := context.Background()

	// Execute
	conflicts, err := svc.GetActiveConflicts(ctx, characterID)
	require.NoError(t, err)

	// Verify (with mock data, should return empty slice)
	assert.NotNil(t, conflicts)
	assert.IsType(t, []api.EngramConflict{}, conflicts)
}

func TestService_calculateCompatibilityScore(t *testing.T) {
	svc := &Service{}

	tests := []struct {
		name             string
		reputationMatch  map[string]api.ReputationMatch
		valuesMatch      map[string]bool
		expectedMin      float64
		expectedMax      float64
	}{
		{
			name:            "no matches",
			reputationMatch: map[string]api.ReputationMatch{},
			valuesMatch:     map[string]bool{},
			expectedMin:     45.0, // Base 50 - some variance
			expectedMax:     55.0,
		},
		{
			name: "good reputation match",
			reputationMatch: map[string]api.ReputationMatch{
				"araskes": api.ReputationMatchSame,
			},
			valuesMatch: map[string]bool{},
			expectedMin: 55.0,
			expectedMax: 65.0,
		},
		{
			name: "conflicting reputation",
			reputationMatch: map[string]api.ReputationMatch{
				"araskes": api.ReputationMatchOpposite,
			},
			valuesMatch: map[string]bool{},
			expectedMin: 35.0,
			expectedMax: 45.0,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			score := svc.calculateCompatibilityScore(tt.reputationMatch, tt.valuesMatch)
			assert.GreaterOrEqual(t, score, -50.0)
			assert.LessOrEqual(t, score, 50.0)
			assert.GreaterOrEqual(t, score, tt.expectedMin)
			assert.LessOrEqual(t, score, tt.expectedMax)
		})
	}
}

func TestService_scoreToCompatibilityLevel(t *testing.T) {
	svc := &Service{}

	tests := []struct {
		score    float64
		expected api.CompatibilityLevel
	}{
		{40, api.CompatibilityLevelFullCompatibility},
		{20, api.CompatibilityLevelPartialCompatibility},
		{5, api.CompatibilityLevelNeutral},
		{-5, api.CompatibilityLevelNeutral},
		{-20, api.CompatibilityLevelConflict},
		{-40, api.CompatibilityLevelHostility},
	}

	for _, tt := range tests {
		t.Run("", func(t *testing.T) {
			level := svc.scoreToCompatibilityLevel(tt.score)
			assert.Equal(t, tt.expected, level)
		})
	}
}

func TestService_calculateSynergyBonus(t *testing.T) {
	svc := &Service{}

	tests := []struct {
		avgScore    float64
		engramCount int
		expected    float64
	}{
		{25.0, 2, 2.5},
		{25.0, 3, 3.75},
		{-25.0, 2, -2.5},
		{0.0, 3, 0.0},
	}

	for _, tt := range tests {
		t.Run("", func(t *testing.T) {
			bonus := svc.calculateSynergyBonus(tt.avgScore, tt.engramCount)
			assert.InDelta(t, tt.expected, bonus, 0.1)
			assert.GreaterOrEqual(t, bonus, -25.0)
			assert.LessOrEqual(t, bonus, 25.0)
		})
	}
}
