package internal

import (
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
)

// GetPlayerState handles player state retrieval
func (gsm *GlobalStateManager) GetPlayerState(c *gin.Context) {
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

// UpdatePlayerState handles player state updates
func (gsm *GlobalStateManager) UpdatePlayerState(c *gin.Context) {
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

// SyncPlayerState handles player state synchronization
func (gsm *GlobalStateManager) SyncPlayerState(c *gin.Context) {
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

// GetMatchState handles match state retrieval
func (gsm *GlobalStateManager) GetMatchState(c *gin.Context) {
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

// UpdateMatchState handles match state updates
func (gsm *GlobalStateManager) UpdateMatchState(c *gin.Context) {
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

// GetGlobalState handles global state retrieval
func (gsm *GlobalStateManager) GetGlobalState(c *gin.Context) {
	// Get global state from manager
	state, err := gsm.GetGlobalState(c.Request.Context())
	if err != nil {
		gsm.logger.Error("Failed to get global state", zap.Error(err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to get global state"})
		return
	}

	c.JSON(http.StatusOK, state)
}

// SyncGlobalState handles global state synchronization
func (gsm *GlobalStateManager) SyncGlobalState(c *gin.Context) {
	err := gsm.SyncGlobalState(c.Request.Context())
	if err != nil {
		gsm.logger.Error("Failed to sync global state", zap.Error(err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to sync global state"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"status": "synced"})
}

// GetMatchState retrieves match state (placeholder implementation)
func (gsm *GlobalStateManager) GetMatchState(ctx context.Context, matchID string) (*MatchState, error) {
	// Placeholder implementation - would retrieve from cache/database
	state := &MatchState{
		MatchID:         matchID,
		Status:          1, // Active
		MaxPlayers:      10,
		CurrentPlayers:  8,
		StartTime:       time.Now().Add(-10 * time.Minute),
		LastUpdated:     time.Now(),
	}

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
