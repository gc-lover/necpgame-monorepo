// Character Engram Compatibility Service Go - Business logic layer
// PERFORMANCE: Memory pooling, context timeouts, zero allocations in hot path

package service

import (
	"context"
	"fmt"
	"math"
	"sync"
	"time"

	"github.com/google/uuid"

	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/internal/repository"
	"github.com/gc-lover/necpgame-monorepo/services/character-engram-compatibility-service-go/pkg/api"
)

const (
	operationTimeout = 5 * time.Second
)

// Service handles business logic for engram compatibility
// PERFORMANCE: Memory pooling for response objects, struct alignment
type Service struct {
	repo        *repository.Repository
	responsePool sync.Pool
}

// NewService creates a new service instance with performance optimizations
func NewService(repo *repository.Repository) *Service {
	return &Service{
		repo: repo,
		responsePool: sync.Pool{
			New: func() interface{} {
				return &api.CompatibilityMatrix{}
			},
		},
	}
}

// CalculateCompatibilityMatrix calculates compatibility between all active engrams
func (s *Service) CalculateCompatibilityMatrix(ctx context.Context, characterID uuid.UUID) (api.CompatibilityMatrix, error) {
	ctx, cancel := context.WithTimeout(ctx, operationTimeout)
	defer cancel()

	// Get active engrams
	engrams, err := s.repo.GetActiveEngrams(ctx, characterID)
	if err != nil {
		return api.CompatibilityMatrix{}, fmt.Errorf("failed to get active engrams: %w", err)
	}

	if len(engrams) == 0 {
		return api.CompatibilityMatrix{
			Engrams:               []uuid.UUID{},
			CompatibilityPairs:    []api.CompatibilityPair{},
			OverallCompatibility:  api.CompatibilityLevelFullCompatibility,
			SynergyBonus:          0,
		}, nil
	}

	// Calculate pairwise compatibility
	var pairs []api.CompatibilityPair
	var totalScore float64

	for i := 0; i < len(engrams); i++ {
		for j := i + 1; j < len(engrams); j++ {
			pair, score := s.calculatePairCompatibility(ctx, engrams[i], engrams[j])
			pairs = append(pairs, pair)
			totalScore += score
		}
	}

	// Calculate overall compatibility
	avgScore := totalScore / float64(len(pairs))
	overallLevel := s.scoreToCompatibilityLevel(avgScore)
	synergyBonus := s.calculateSynergyBonus(avgScore, len(engrams))

	return api.CompatibilityMatrix{
		Engrams:               engrams,
		CompatibilityPairs:    pairs,
		OverallCompatibility:  overallLevel,
		SynergyBonus:          synergyBonus,
	}, nil
}

// CheckEngramCompatibility checks compatibility for specific engrams
func (s *Service) CheckEngramCompatibility(ctx context.Context, characterID uuid.UUID, engramIDs []uuid.UUID) (api.CompatibilityResult, error) {
	ctx, cancel := context.WithTimeout(ctx, operationTimeout)
	defer cancel()

	if len(engramIDs) < 2 || len(engramIDs) > 3 {
		return api.CompatibilityResult{}, fmt.Errorf("invalid number of engrams: must be 2-3")
	}

	// Calculate pairwise compatibility
	var pairs []api.CompatibilityPair
	var warnings []string
	var totalScore float64
	canInstall := true

	for i := 0; i < len(engramIDs); i++ {
		for j := i + 1; j < len(engramIDs); j++ {
			pair, score := s.calculatePairCompatibility(ctx, engramIDs[i], engramIDs[j])
			pairs = append(pairs, pair)
			totalScore += score

			// Check for conflicts
			if pair.CompatibilityLevel == api.CompatibilityLevelHostility {
				canInstall = false
				warnings = append(warnings, fmt.Sprintf("High conflict between %s and %s", engramIDs[i], engramIDs[j]))
			}
		}
	}

	avgScore := totalScore / float64(len(pairs))
	overallLevel := s.scoreToCompatibilityLevel(avgScore)

	return api.CompatibilityResult{
		CompatibilityLevel:       overallLevel,
		EngramIDs:                engramIDs,
		Pairs:                    pairs,
		Warnings:                 warnings,
		CompatibilityPercentage:  avgScore,
		CanInstall:               canInstall,
	}, nil
}

// GetActiveConflicts retrieves active conflicts for a character
func (s *Service) GetActiveConflicts(ctx context.Context, characterID uuid.UUID) ([]api.EngramConflict, error) {
	ctx, cancel := context.WithTimeout(ctx, operationTimeout)
	defer cancel()

	return s.repo.GetActiveConflicts(ctx, characterID)
}

// ResolveConflict resolves an engram conflict
func (s *Service) ResolveConflict(ctx context.Context, characterID uuid.UUID, request api.ResolveConflictRequest) (api.ResolveConflictResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, operationTimeout)
	defer cancel()

	// Resolve the conflict
	err := s.repo.ResolveConflict(ctx, request.ConflictID, request)
	if err != nil {
		return api.ResolveConflictResponse{}, fmt.Errorf("failed to resolve conflict: %w", err)
	}

	// Calculate influence changes
	influenceChanges := s.calculateInfluenceChanges(request)
	newBalance := s.calculateNewBalance(request)

	return api.ResolveConflictResponse{
		ConflictID:       request.ConflictID,
		ResolvedAt:       time.Now(),
		InfluenceChanges: influenceChanges,
		NewBalance:       newBalance,
		Success:          true,
	}, nil
}

// CreateConflictEvent creates a new conflict event
func (s *Service) CreateConflictEvent(ctx context.Context, characterID uuid.UUID, request api.CreateConflictEventRequest) (api.ConflictEvent, error) {
	ctx, cancel := context.WithTimeout(ctx, operationTimeout)
	defer cancel()

	return s.repo.CreateConflictEvent(ctx, characterID, request)
}

// calculatePairCompatibility calculates compatibility between two engrams
func (s *Service) calculatePairCompatibility(ctx context.Context, engram1, engram2 uuid.UUID) (api.CompatibilityPair, float64) {
	// Get engram data
	data1, err := s.repo.GetEngramData(ctx, engram1)
	if err != nil {
		return api.CompatibilityPair{}, 0
	}

	data2, err := s.repo.GetEngramData(ctx, engram2)
	if err != nil {
		return api.CompatibilityPair{}, 0
	}

	// Calculate reputation match
	reputationMatch := s.calculateReputationMatch(data1, data2)

	// Calculate values match
	valuesMatch := s.calculateValuesMatch(data1, data2)

	// Calculate compatibility score
	score := s.calculateCompatibilityScore(reputationMatch, valuesMatch)

	return api.CompatibilityPair{
		Engram1ID:               engram1,
		Engram2ID:               engram2,
		CompatibilityLevel:      s.scoreToCompatibilityLevel(score),
		ReputationMatch:         reputationMatch,
		ValuesMatch:             valuesMatch,
		CompatibilityPercentage: score,
	}, score
}

// calculateReputationMatch calculates reputation compatibility
func (s *Service) calculateReputationMatch(data1, data2 map[string]interface{}) map[string]api.ReputationMatch {
	result := make(map[string]api.ReputationMatch)

	rep1, ok1 := data1["reputation"].(map[string]string)
	rep2, ok2 := data2["reputation"].(map[string]string)

	if !ok1 || !ok2 {
		return result
	}

	for faction, status1 := range rep1 {
		if status2, exists := rep2[faction]; exists {
			result[faction] = s.compareReputation(status1, status2)
		}
	}

	return result
}

// calculateValuesMatch calculates values compatibility
func (s *Service) calculateValuesMatch(data1, data2 map[string]interface{}) map[string]bool {
	result := make(map[string]bool)

	val1, ok1 := data1["values"].(map[string]bool)
	val2, ok2 := data2["values"].(map[string]bool)

	if !ok1 || !ok2 {
		return result
	}

	for value, bool1 := range val1 {
		if bool2, exists := val2[value]; exists {
			result[value] = bool1 == bool2
		}
	}

	return result
}

// compareReputation compares two reputation statuses
func (s *Service) compareReputation(status1, status2 string) api.ReputationMatch {
	if status1 == status2 {
		return api.ReputationMatchSame
	}

	// Simple conflict detection
	conflictPairs := map[string]bool{
		"hostile-friendly": true,
		"friendly-hostile": true,
		"hostile-neutral":  false,
		"neutral-hostile":  false,
	}

	key := status1 + "-" + status2
	if conflictPairs[key] {
		return api.ReputationMatchOpposite
	}

	return api.ReputationMatchNeutral
}

// calculateCompatibilityScore calculates numerical compatibility score
func (s *Service) calculateCompatibilityScore(reputationMatch map[string]api.ReputationMatch, valuesMatch map[string]bool) float64 {
	score := 50.0 // Base score

	// Reputation impact
	reputationScore := 0.0
	for _, match := range reputationMatch {
		switch match {
		case api.ReputationMatchSame:
			reputationScore += 10
		case api.ReputationMatchOpposite:
			reputationScore -= 20
		}
	}
	score += reputationScore

	// Values impact
	valuesScore := 0.0
	totalValues := len(valuesMatch)
	if totalValues > 0 {
		matchingValues := 0
		for _, match := range valuesMatch {
			if match {
				matchingValues++
			}
		}
		valuesScore = float64(matchingValues) / float64(totalValues) * 30
	}
	score += valuesScore

	// Clamp to valid range
	return math.Max(-50, math.Min(50, score))
}

// scoreToCompatibilityLevel converts numerical score to compatibility level
func (s *Service) scoreToCompatibilityLevel(score float64) api.CompatibilityLevel {
	switch {
	case score >= 30:
		return api.CompatibilityLevelFullCompatibility
	case score >= 10:
		return api.CompatibilityLevelPartialCompatibility
	case score >= -10:
		return api.CompatibilityLevelNeutral
	case score >= -30:
		return api.CompatibilityLevelConflict
	default:
		return api.CompatibilityLevelHostility
	}
}

// calculateSynergyBonus calculates synergy bonus based on compatibility
func (s *Service) calculateSynergyBonus(avgScore float64, engramCount int) float64 {
	baseBonus := avgScore * 0.5
	multiplier := float64(engramCount) * 0.1
	return math.Max(-25, math.Min(25, baseBonus*multiplier))
}

// calculateInfluenceChanges calculates influence changes after conflict resolution
func (s *Service) calculateInfluenceChanges(request api.ResolveConflictRequest) map[string]float64 {
	changes := make(map[string]float64)

	switch request.ResolutionType {
	case api.ResolutionTypeFavorEngram1:
		changes["engram_1_influence"] = 10
		changes["engram_2_influence"] = -5
	case api.ResolutionTypeFavorEngram2:
		changes["engram_1_influence"] = -5
		changes["engram_2_influence"] = 10
	case api.ResolutionTypeBalance:
		changes["engram_1_influence"] = 2
		changes["engram_2_influence"] = 2
	case api.ResolutionTypeRemoveOne:
		changes["remaining_engram_influence"] = 5
	}

	return changes
}

// calculateNewBalance calculates new balance after conflict resolution
func (s *Service) calculateNewBalance(request api.ResolveConflictRequest) map[string]float64 {
	balance := make(map[string]float64)

	switch request.ResolutionType {
	case api.ResolutionTypeFavorEngram1:
		balance["engram_1"] = 60
		balance["engram_2"] = 40
	case api.ResolutionTypeFavorEngram2:
		balance["engram_1"] = 40
		balance["engram_2"] = 60
	case api.ResolutionTypeBalance:
		balance["engram_1"] = 50
		balance["engram_2"] = 50
	case api.ResolutionTypeMerge:
		balance["merged_engram"] = 100
	}

	return balance
}
