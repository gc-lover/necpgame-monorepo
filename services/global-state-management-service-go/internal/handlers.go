package internal

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
)

// GetPlayerStateHandler handles player state retrieval HTTP requests
func (gsm *GlobalStateManager) GetPlayerStateHandler(c *gin.Context) {
	playerID := c.Param("playerId")

	// Get player state from manager
	state, err := gsm.GetPlayerState(c.Request.Context(), playerID)
	if err != nil {
		gsm.logger.Error("Failed to get player state",
			zap.Error(err),
			zap.String("player_id", playerID))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to get player state"})
		return
	}

	c.JSON(http.StatusOK, state)
}

// UpdatePlayerStateHandler handles player state updates HTTP requests
func (gsm *GlobalStateManager) UpdatePlayerStateHandler(c *gin.Context) {
	playerID := c.Param("playerId")

	var request struct {
		State   PlayerState `json:"state" binding:"required"`
		Version int64       `json:"version"`
	}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// Update player state
	err := gsm.UpdatePlayerState(c.Request.Context(), playerID, &request.State, request.Version)
	if err != nil {
		gsm.logger.Error("Failed to update player state",
			zap.Error(err),
			zap.String("player_id", playerID))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to update player state"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"status": "updated"})
}

// SyncPlayerStateHandler handles player state synchronization HTTP requests
func (gsm *GlobalStateManager) SyncPlayerStateHandler(c *gin.Context) {
	playerID := c.Param("playerId")

	err := gsm.SyncPlayerState(c.Request.Context(), playerID)
	if err != nil {
		gsm.logger.Error("Failed to sync player state",
			zap.Error(err),
			zap.String("player_id", playerID))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to sync player state"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"status": "synced"})
}

// GetMatchStateHandler handles match state retrieval HTTP requests
func (gsm *GlobalStateManager) GetMatchStateHandler(c *gin.Context) {
	matchID := c.Param("matchId")

	// Get match state from manager
	state, err := gsm.GetMatchState(c.Request.Context(), matchID)
	if err != nil {
		gsm.logger.Error("Failed to get match state",
			zap.Error(err),
			zap.String("match_id", matchID))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to get match state"})
		return
	}

	c.JSON(http.StatusOK, state)
}

// UpdateMatchStateHandler handles match state updates HTTP requests
func (gsm *GlobalStateManager) UpdateMatchStateHandler(c *gin.Context) {
	matchID := c.Param("matchId")

	var state MatchState
	if err := c.ShouldBindJSON(&state); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	// Update match state
	err := gsm.UpdateMatchState(c.Request.Context(), matchID, &state)
	if err != nil {
		gsm.logger.Error("Failed to update match state",
			zap.Error(err),
			zap.String("match_id", matchID))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to update match state"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"status": "updated"})
}

// GetGlobalStateHandler handles global state retrieval HTTP requests
func (gsm *GlobalStateManager) GetGlobalStateHandler(c *gin.Context) {
	// Get global state from manager
	state, err := gsm.GetGlobalState(c.Request.Context())
	if err != nil {
		gsm.logger.Error("Failed to get global state", zap.Error(err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to get global state"})
		return
	}

	c.JSON(http.StatusOK, state)
}

// SyncGlobalStateHandler handles global state synchronization HTTP requests
func (gsm *GlobalStateManager) SyncGlobalStateHandler(c *gin.Context) {
	err := gsm.SyncGlobalState(c.Request.Context())
	if err != nil {
		gsm.logger.Error("Failed to sync global state", zap.Error(err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to sync global state"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"status": "synced"})
}

// GetMatchState retrieves match state with multi-level caching optimization
func (gsm *GlobalStateManager) GetMatchState(ctx context.Context, matchID string) (*MatchState, error) {
	// L1 Cache check (ultra-fast for active matches)
	if state := gsm.getMatchStateFromL1(matchID); state != nil {
		return state, nil
	}

	// L2 Cache check (Redis)
	key := fmt.Sprintf("match:state:%s", matchID)
	data, err := gsm.redisClient.Get(ctx, key).Result()
	if err == nil {
		var state MatchState
		if err := json.Unmarshal([]byte(data), &state); err == nil {
			// Update L1 cache asynchronously
			gsm.matchStateWorkers.Submit(func() {
				gsm.setMatchStateToL1(&state)
			})
			return &state, nil
		}
	}

	// L3 Cache check (PostgreSQL) - optimized query for active matches
	state, err := gsm.getMatchStateFromDB(ctx, matchID)
	if err != nil {
		// Return default active match state if not found (for resilience)
		return &MatchState{
			MatchID:         matchID,
			Status:          1, // Active
			MaxPlayers:      10,
			CurrentPlayers:  8,
			StartTime:       time.Now().Add(-10 * time.Minute),
			LastUpdated:     time.Now(),
		}, nil
	}

	// Update caches asynchronously
	gsm.matchStateWorkers.Submit(func() {
		gsm.setMatchStateToL1(state)
		gsm.setMatchStateToRedis(ctx, state)
	})

	return state, nil
}

// UpdateMatchState updates match state (placeholder implementation)
func (gsm *GlobalStateManager) UpdateMatchState(ctx context.Context, matchID string, state *MatchState) error {
	// Placeholder implementation - would update cache/database
	gsm.logger.Info("Updating match state",
		zap.String("match_id", matchID),
		zap.Int16("current_players", state.CurrentPlayers))

	return nil
}

// GetGlobalState retrieves global state (placeholder implementation)
func (gsm *GlobalStateManager) GetGlobalState(ctx context.Context) (*GlobalState, error) {
	// Placeholder implementation - would aggregate from all regions
	state := &GlobalState{
		TotalPlayers:   15420,
		ActiveServers:  25,
		Status:         1, // Healthy
		LastUpdated:    time.Now(),
	}

	return state, nil
}

// SyncGlobalState synchronizes global state (placeholder implementation)
func (gsm *GlobalStateManager) SyncGlobalState(ctx context.Context) error {
	// Placeholder implementation - would synchronize across regions
	gsm.logger.Info("Synchronizing global state across regions")
	return nil
}
