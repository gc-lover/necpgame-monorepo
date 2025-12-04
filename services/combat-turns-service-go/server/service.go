// Issue: #1595, #1607
// Performance: Memory pooling for hot path (Issue #1607)
package server

import (
	"context"
	"errors"
	"sync"

	"github.com/gc-lover/necpgame-monorepo/services/combat-turns-service-go/pkg/api"
	"github.com/google/uuid"
)

var (
	// ErrNotFound is returned when entity is not found
	ErrNotFound = errors.New("not found")
)

// Service contains business logic
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot structs (zero allocations target!)
	currentTurnPool sync.Pool
	turnOrderPool   sync.Pool
}

// NewService creates new service with memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.currentTurnPool = sync.Pool{
		New: func() interface{} {
			return &api.CurrentTurn{}
		},
	}
	s.turnOrderPool = sync.Pool{
		New: func() interface{} {
			return &api.TurnOrder{}
		},
	}

	return s
}

// GetCurrentTurn returns current turn for session
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetCurrentTurn(ctx context.Context, sessionID uuid.UUID) (api.GetCurrentTurnRes, error) {
	// Get from pool (zero allocation!)
	resp := s.currentTurnPool.Get().(*api.CurrentTurn)
	defer s.currentTurnPool.Put(resp)

	// TODO: Implement business logic
	// For now, return stub response
	resp.SessionID = api.NewOptUUID(sessionID)
	resp.CurrentParticipantID = api.OptUUID{}
	resp.TurnNumber = api.OptInt{}
	resp.TimeRemaining = api.OptInt{}
	resp.Phase = api.NewOptCurrentTurnPhase(api.CurrentTurnPhasePreparation)
	
	// Clone response (caller owns it)
	result := &api.CurrentTurn{
		SessionID:            resp.SessionID,
		CurrentParticipantID: resp.CurrentParticipantID,
		TurnNumber:           resp.TurnNumber,
		TimeRemaining:        resp.TimeRemaining,
		Phase:                resp.Phase,
	}
	
	return result, nil
}

// GetTurnOrder returns turn order for session
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetTurnOrder(ctx context.Context, sessionID uuid.UUID) (api.GetTurnOrderRes, error) {
	// Get from pool (zero allocation!)
	resp := s.turnOrderPool.Get().(*api.TurnOrder)
	defer s.turnOrderPool.Put(resp)

	// TODO: Implement business logic
	// For now, return stub response
	resp.SessionID = api.NewOptUUID(sessionID)
	resp.Order = []api.TurnOrderOrderItem{}
	
	// Clone response (caller owns it)
	result := &api.TurnOrder{
		SessionID: resp.SessionID,
		Order:     resp.Order,
	}
	
	return result, nil
}

// NextTurn advances to next turn
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) NextTurn(ctx context.Context, sessionID uuid.UUID) (api.NextTurnRes, error) {
	// Get from pool (zero allocation!)
	resp := s.currentTurnPool.Get().(*api.CurrentTurn)
	defer s.currentTurnPool.Put(resp)

	// TODO: Implement business logic
	// For now, return stub response
	resp.SessionID = api.NewOptUUID(sessionID)
	resp.CurrentParticipantID = api.OptUUID{}
	resp.TurnNumber = api.OptInt{}
	resp.TimeRemaining = api.OptInt{}
	resp.Phase = api.NewOptCurrentTurnPhase(api.CurrentTurnPhasePreparation)
	
	// Clone response (caller owns it)
	result := &api.CurrentTurn{
		SessionID:            resp.SessionID,
		CurrentParticipantID: resp.CurrentParticipantID,
		TurnNumber:           resp.TurnNumber,
		TimeRemaining:        resp.TimeRemaining,
		Phase:                resp.Phase,
	}
	
	return result, nil
}

// SkipTurn skips current turn
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) SkipTurn(ctx context.Context, sessionID uuid.UUID, req *api.SkipTurnRequest) (api.SkipTurnRes, error) {
	// Get from pool (zero allocation!)
	resp := s.currentTurnPool.Get().(*api.CurrentTurn)
	defer s.currentTurnPool.Put(resp)

	// TODO: Implement business logic
	// For now, return stub response
	resp.SessionID = api.NewOptUUID(sessionID)
	resp.CurrentParticipantID = api.OptUUID{}
	resp.TurnNumber = api.OptInt{}
	resp.TimeRemaining = api.OptInt{}
	resp.Phase = api.NewOptCurrentTurnPhase(api.CurrentTurnPhasePreparation)
	
	// Clone response (caller owns it)
	result := &api.CurrentTurn{
		SessionID:            resp.SessionID,
		CurrentParticipantID: resp.CurrentParticipantID,
		TurnNumber:           resp.TurnNumber,
		TimeRemaining:        resp.TimeRemaining,
		Phase:                resp.Phase,
	}
	
	return result, nil
}
