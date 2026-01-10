package handlers

import (
	"context"
	"fmt"
	"log"
	"time"

	"necpgame/services/matchmaking-service-go/internal/database"
	"necpgame/services/matchmaking-service-go/internal/service"
	api "necpgame/services/matchmaking-service-go"
)

// MatchmakingHandlers implements the generated Handler interface
type MatchmakingHandlers struct {
	matchmakingSvc *service.MatchmakingService
	dbManager      *database.Manager
}

// NewMatchmakingHandlers creates a new instance of MatchmakingHandlers
func NewMatchmakingHandlers(svc *service.MatchmakingService, dbManager *database.Manager) *MatchmakingHandlers {
	return &MatchmakingHandlers{
		matchmakingSvc: svc,
		dbManager:      dbManager,
	}
}

// HealthCheck implements health check endpoint
func (h *MatchmakingHandlers) HealthCheck(ctx context.Context) (*api.HealthCheckOK, error) {
	log.Println("Health check requested")

	// Check database health with context timeout
	if err := h.dbManager.HealthCheck(ctx); err != nil {
		log.Printf("Database health check failed: %v", err)
		response := &api.HealthCheckOK{}
		response.Status.SetTo("unhealthy")
		return response, nil
	}

	response := &api.HealthCheckOK{}
	response.Status.SetTo("healthy")

	return response, nil
}

// JoinQueue implements queue join endpoint
func (h *MatchmakingHandlers) JoinQueue(ctx context.Context, req *api.JoinQueueReq) (api.JoinQueueRes, error) {
	// BACKEND NOTE: Context timeout for queue join (prevents hanging in high-load matchmaking)
	joinCtx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	playerID := req.PlayerID.String()
	gameMode := string(req.GameMode)
	log.Printf("Player %s joining queue for mode %s", playerID, gameMode)

	// Call service to join queue
	result, err := h.matchmakingSvc.JoinQueue(joinCtx, playerID, gameMode)
	if err != nil {
		log.Printf("Failed to join queue for player %s: %v", req.PlayerID, err)
		return &api.JoinQueueAccepted{}, nil // Still return accepted for async processing
	}

	response := &api.JoinQueueAccepted{}
	response.QueuePosition.SetTo(result.QueuePosition)
	response.EstimatedWaitSeconds.SetTo(result.EstimatedWaitSeconds)

	return response, nil
}

// LeaveQueue implements queue leave endpoint
func (h *MatchmakingHandlers) LeaveQueue(ctx context.Context, params api.LeaveQueueParams) (api.LeaveQueueRes, error) {
	playerID := params.PlayerID.String()
	log.Printf("Player %s leaving queue", playerID)

	// Call service to leave queue
	err := h.matchmakingSvc.LeaveQueue(ctx, playerID)
	if err != nil {
		log.Printf("Failed to leave queue for player %s: %v", playerID, err)
		return &api.LeaveQueueNotFound{}, nil
	}

	return &api.LeaveQueueNoContent{}, nil
}

// FindMatch implements match finding endpoint
func (h *MatchmakingHandlers) FindMatch(ctx context.Context, req *api.FindMatchReq) (api.FindMatchRes, error) {
	playerID := req.PlayerID.String()
	log.Printf("Finding match for player %s", playerID)

	// Call service to find match
	match, err := h.matchmakingSvc.FindMatch(ctx, playerID)
	if err != nil {
		log.Printf("Failed to find match for player %s: %v", playerID, err)
		return &api.FindMatchAccepted{}, nil // Search in progress
	}

	// Convert service match to API response
	players := make([]api.FindMatchOKPlayersItem, len(match.Players))
	for i, player := range match.Players {
		players[i] = api.FindMatchOKPlayersItem{}
		players[i].PlayerID.SetTo(player.PlayerID)
		players[i].Team.SetTo(player.Team)
	}

	response := &api.FindMatchOK{}
	response.MatchID.SetTo(match.MatchID)
	response.Players = players

	return response, nil
}

// MountRoutes mounts the handlers on the provided mux
// This is a placeholder - actual mounting depends on ogen router implementation
func (h *MatchmakingHandlers) MountRoutes(mux interface{}) error {
	// This would be implemented based on how ogen mounts routes
	// For now, this is a placeholder
	return fmt.Errorf("MountRoutes not implemented - depends on ogen router struc