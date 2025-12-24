// Business logic service for Jackie Welles NPC interactions
// Issue: #1905
// PERFORMANCE: Optimized for real-time NPC behavior simulation

package server

import (
	"context"
	"sync"

	"github.com/gc-lover/necpgame-monorepo/services/jackie-welles-service-go/pkg/api"
	"github.com/google/uuid"
)

// JackieWellesService handles business logic for Jackie Welles NPC
type JackieWellesService struct {
	repo     *Repository
	cache    *Cache
	validator *Validator
	metrics   *Metrics
	mu       sync.RWMutex
}

// NewJackieWellesService creates a new Jackie Welles service
func NewJackieWellesService() *JackieWellesService {
	return &JackieWellesService{
		repo:      NewRepository(),
		cache:     NewCache(),
		validator: NewValidator(),
		metrics:   NewMetrics(),
	}
}

// GetJackieProfile returns Jackie Welles profile with current state
func (s *JackieWellesService) GetJackieProfile(ctx context.Context) (*api.JackieProfileResponse, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// PERFORMANCE: Try cache first
	if profile, found := s.cache.GetProfile(); found {
		return profile, nil
	}

	// Get from repository
	profile, err := s.repo.GetJackieProfile(ctx)
	if err != nil {
		return nil, err
	}

	// Cache result
	s.cache.SetProfile(profile)

	return profile, nil
}

// GetRelationshipStatus returns current relationship level with player
func (s *JackieWellesService) GetRelationshipStatus(ctx context.Context, playerID uuid.UUID) (*api.JackieRelationshipResponse, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// PERFORMANCE: Cache relationship data
	cacheKey := "relationship:" + playerID.String()
	if rel, found := s.cache.GetRelationship(cacheKey); found {
		return rel, nil
	}

	rel, err := s.repo.GetRelationshipStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	s.cache.SetRelationship(cacheKey, rel)
	return rel, nil
}

// GetCurrentStatus returns Jackie Welles current location and availability
func (s *JackieWellesService) GetCurrentStatus(ctx context.Context) (*api.JackieStatusResponse, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// PERFORMANCE: Status updates frequently, cache for short time
	if status, found := s.cache.GetStatus(); found {
		return status, nil
	}

	status, err := s.repo.GetCurrentStatus(ctx)
	if err != nil {
		return nil, err
	}

	s.cache.SetStatus(status)
	return status, nil
}

// AcceptQuest processes quest acceptance logic
func (s *JackieWellesService) AcceptQuest(ctx context.Context, questID uuid.UUID, playerID uuid.UUID) (*api.AcceptJackieQuestOK, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Validate quest availability
	if err := s.validator.ValidateQuestAcceptance(ctx, questID, playerID); err != nil {
		return nil, err
	}

	// Accept quest in repository
	result, err := s.repo.AcceptQuest(ctx, questID, playerID)
	if err != nil {
		return nil, err
	}

	// Update relationship (positive action)
	s.updateRelationshipForAction(ctx, playerID, "accept_quest", 5)

	// Clear relevant caches
	s.cache.InvalidateQuestCache(questID)
	s.cache.InvalidateRelationshipCache("relationship:" + playerID.String())

	s.metrics.RecordQuestAccepted()
	return result, nil
}

// GetAvailableQuests returns quests Jackie can offer based on relationship level
func (s *JackieWellesService) GetAvailableQuests(ctx context.Context, playerID uuid.UUID) (*api.GetJackieAvailableQuestsOK, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	// Get relationship level to filter available quests
	rel, err := s.GetRelationshipStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	quests, err := s.repo.GetAvailableQuests(ctx, rel.Level.GetOrZero())
	if err != nil {
		return nil, err
	}

	return quests, nil
}

// PerformTrade handles item trading with Jackie
func (s *JackieWellesService) PerformTrade(ctx context.Context, req *api.TradeRequest, playerID uuid.UUID) (*api.TradeWithJackieOK, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Validate trade request
	if err := s.validator.ValidateTradeRequest(ctx, req); err != nil {
		return nil, err
	}

	// Execute trade
	result, err := s.repo.PerformTrade(ctx, req, playerID)
	if err != nil {
		return nil, err
	}

	// Update relationship based on trade value
	tradeValue := req.TotalAmount.GetOrZero()
	relationshipChange := tradeValue / 100 // 1 loyalty point per 100 eddies
	s.updateRelationshipForAction(ctx, playerID, "trade", int(relationshipChange))

	s.metrics.RecordTradeCompleted()
	return result, nil
}

// StartDialogue initiates conversation with Jackie
func (s *JackieWellesService) StartDialogue(ctx context.Context, req *api.DialogueStartRequest, playerID uuid.UUID) (*api.StartJackieDialogueOK, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Get relationship context for dialogue options
	rel, err := s.GetRelationshipStatus(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result, err := s.repo.StartDialogue(ctx, req, rel)
	if err != nil {
		return nil, err
	}

	s.metrics.RecordDialogueStarted()
	return result, nil
}

// RespondToDialogue processes player response in active dialogue
func (s *JackieWellesService) RespondToDialogue(ctx context.Context, req *api.DialogueResponseRequest, dialogueID uuid.UUID, playerID uuid.UUID) (*api.RespondToJackieDialogueOK, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	// Validate dialogue is active
	if err := s.validator.ValidateDialogueResponse(ctx, req, dialogueID); err != nil {
		return nil, err
	}

	result, err := s.repo.RespondToDialogue(ctx, req, dialogueID)
	if err != nil {
		return nil, err
	}

	// Update relationship based on dialogue choice
	s.updateRelationshipForAction(ctx, playerID, "dialogue_response", s.calculateDialogueImpact(req))

	s.metrics.RecordDialogueResponse()
	return result, nil
}

// updateRelationshipForAction updates relationship level based on player actions
func (s *JackieWellesService) updateRelationshipForAction(ctx context.Context, playerID uuid.UUID, actionType string, change int) {
	// This would update relationship metrics in repository
	// For now, just invalidate cache to force refresh
	s.cache.InvalidateRelationshipCache("relationship:" + playerID.String())
}

// calculateDialogueImpact calculates relationship impact of dialogue choice
func (s *JackieWellesService) calculateDialogueImpact(req *api.DialogueResponseRequest) int {
	// Simple logic: positive responses increase loyalty, negative decrease
	response := req.Response.GetOrZero()
	if response == "positive" {
		return 2
	} else if response == "negative" {
		return -1
	}
	return 0
}
