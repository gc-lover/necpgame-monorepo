// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/pkg/api"
	"github.com/google/uuid"
)

const DBTimeout = 50 * time.Millisecond

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct{}

// NewHandlers creates new handlers
func NewHandlers() *Handlers {
	return &Handlers{}
}

// ListCombatSessions - TYPED response!
func (h *Handlers) ListCombatSessions(ctx context.Context, params api.ListCombatSessionsParams) ([]api.CombatSession, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	sessions := []api.CombatSession{}

	return sessions, nil
}

// CreateCombatSession - TYPED response!
func (h *Handlers) CreateCombatSession(ctx context.Context, req *api.CreateSessionRequest) (*api.CombatSession, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	sessionID := uuid.New()
	status := api.CombatSessionStatusActive

	result := &api.CombatSession{
		ID:          sessionID,
		PlayerID:    req.PlayerID,
		SessionType: string(req.SessionType),
		Status:      status,
		CreatedAt:   time.Now(),
	}

	return result, nil
}

// GetCombatSession - TYPED response!
func (h *Handlers) GetCombatSession(ctx context.Context, params api.GetCombatSessionParams) (api.GetCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	playerID := uuid.New()
	status := api.CombatSessionStatusActive

	result := &api.CombatSession{
		ID:          params.SessionID,
		PlayerID:    playerID,
		SessionType: "pvp",
		Status:      status,
		CreatedAt:   time.Now(),
		EndedAt:     api.OptDateTime{},
	}

	return result, nil
}

// EndCombatSession - TYPED response!
func (h *Handlers) EndCombatSession(ctx context.Context, params api.EndCombatSessionParams) (api.EndCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	result := &api.EndCombatSessionOK{}

	return result, nil
}
