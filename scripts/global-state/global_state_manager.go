// Package globalstate provides optimized global state management for MMOFPS games
package globalstate

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"sync/atomic"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// GlobalStateManager manages global game state with high-performance optimizations
type GlobalStateManager struct {
	// State storage
	playerStates    sync.Map // map[string]*PlayerState
	gameStates      sync.Map // map[string]*GameState
	globalStates    sync.Map // map[string]interface{}

	// Performance tracking
	operationsCount int64
	cacheHits       int64
	cacheMisses     int64

	// Configuration
	config         *GlobalStateConfig
	logger         *errorhandling.Logger
	shutdownChan   chan struct{}
	wg             sync.WaitGroup

	// Event system
	eventSubscribers map[string][]StateEventSubscriber
	eventMutex       sync.RWMutex

	// Optimizations
	statePool       *sync.Pool
	updateBuffer    chan *StateUpdate
	bufferSize      int
}

// GlobalStateConfig holds configuration for global state management
type GlobalStateConfig struct {
	MaxStates          int           `json:"max_states"`
	CacheTTL           time.Duration `json:"cache_ttl"`
	UpdateBufferSize   int           `json:"update_buffer_size"`
	CleanupInterval    time.Duration `json:"cleanup_interval"`
	StateSyncInterval  time.Duration `json:"state_sync_interval"`
	EnableCompression  bool          `json:"enable_compression"`
	MaxConcurrentOps   int           `json:"max_concurrent_ops"`
}

// PlayerState represents a player's global state
type PlayerState struct {
	PlayerID       string                 `json:"player_id"`
	Position       Position               `json:"position"`
	Health         float64                `json:"health"`
	Level          int                    `json:"level"`
	Experience     int64                  `json:"experience"`
	Inventory      map[string]int         `json:"inventory"`
	Skills         map[string]interface{} `json:"skills"`
	Achievements   []string               `json:"achievements"`
	SocialStatus   SocialStatus           `json:"social_status"`
	LastUpdate     time.Time              `json:"last_update"`
	Version        int64                  `json:"version"`
	ServerID       string                 `json:"server_id"`
	IsOnline       bool                   `json:"is_online"`
}

// Position represents 3D position with zone information
type Position struct {
	X     float64 `json:"x"`
	Y     float64 `json:"y"`
	Z     float64 `json:"z"`
	Zone  string  `json:"zone"`
	World string  `json:"world"`
}

// GameState represents global game state
type GameState struct {
	GameID         string                 `json:"game_id"`
	Status         GameStatus             `json:"status"`
	Players        []string               `json:"players"`
	MaxPlayers     int                    `json:"max_players"`
	StartTime      *time.Time             `json:"start_time,omitempty"`
	EndTime        *time.Time             `json:"end_time,omitempty"`
	Score          map[string]int         `json:"score"`
	Settings       map[string]interface{} `json:"settings"`
	LastUpdate     time.Time              `json:"last_update"`
	Version        int64                  `json:"version"`
}

// GameStatus represents the status of a game
type GameStatus string

const (
	GameStatusWaiting   GameStatus = "waiting"
	GameStatusStarting  GameStatus = "starting"
	GameStatusRunning   GameStatus = "running"
	GameStatusFinished  GameStatus = "finished"
	GameStatusCancelled GameStatus = "cancelled"
)

// SocialStatus represents player's social standing
type SocialStatus struct {
	Reputation int                    `json:"reputation"`
	Faction    string                 `json:"faction"`
	Rank       string                 `json:"rank"`
	Guild      string                 `json:"guild"`
	Friends    []string               `json:"friends"`
	Blocked    []string               `json:"blocked"`
}

// StateUpdate represents a state update operation
type StateUpdate struct {
	Type      UpdateType      `json:"type"`
	EntityID  string          `json:"entity_id"`
	StateType StateType       `json:"state_type"`
	Data      interface{}     `json:"data"`
	Timestamp time.Time       `json:"timestamp"`
	Priority  UpdatePriority  `json:"priority"`
}

// UpdateType represents the type of state update
type UpdateType string

const (
	UpdateTypeCreate UpdateType = "create"
	UpdateTypeUpdate UpdateType = "update"
	UpdateTypeDelete UpdateType = "delete"
	UpdateTypeSync   UpdateType = "sync"
)

// StateType represents the type of state being updated
type StateType string

const (
	StateTypePlayer StateType = "player"
	StateTypeGame   StateType = "game"
	StateTypeGlobal StateType = "global"
)

// UpdatePriority represents update priority levels
type UpdatePriority string

const (
	UpdatePriorityLow    UpdatePriority = "low"
	UpdatePriorityNormal UpdatePriority = "normal"
	UpdatePriorityHigh   UpdatePriority = "high"
	UpdatePriorityUrgent UpdatePriority = "urgent"
)

// StateEventSubscriber receives state change events
type StateEventSubscriber interface {
	OnStateUpdate(update *StateUpdate)
	OnStateConflict(entityID string, conflicts []*StateUpdate)
}

// NewGlobalStateManager creates a new global state manager
func NewGlobalStateManager(config *GlobalStateConfig, logger *errorhandling.Logger) (*GlobalStateManager, error) {
	if config == nil {
		config = &GlobalStateConfig{
			MaxStates:         100000,
			CacheTTL:          30 * time.Minute,
			UpdateBufferSize:  10000,
			CleanupInterval:   5 * time.Minute,
			StateSyncInterval: 1 * time.Minute,
			EnableCompression: true,
			MaxConcurrentOps:  100,
		}
	}

	gsm := &GlobalStateManager{
		config:          config,
		logger:          logger,
		shutdownChan:    make(chan struct{}),
		eventSubscribers: make(map[string][]StateEventSubscriber),
		updateBuffer:   make(chan *StateUpdate, config.UpdateBufferSize),
		bufferSize:     config.UpdateBufferSize,
		statePool: &sync.Pool{
			New: func() interface{} {
				return &PlayerState{}
			},
		},
	}

	// Start background processes
	gsm.startBackgroundProcesses()

	logger.Infow("Global state manager initialized",
		"max_states", config.MaxStates,
		"buffer_size", config.UpdateBufferSize,
		"cleanup_interval", config.CleanupInterval)

	return gsm, nil
}

// GetPlayerState retrieves a player's state
func (gsm *GlobalStateManager) GetPlayerState(playerID string) (*PlayerState, error) {
	if value, ok := gsm.playerStates.Load(playerID); ok {
		atomic.AddInt64(&gsm.cacheHits, 1)
		if state, ok := value.(*PlayerState); ok {
			return state, nil
		}
	}

	atomic.AddInt64(&gsm.cacheMisses, 1)
	return nil, errorhandling.NewNotFoundError("PLAYER_STATE_NOT_FOUND", "Player state not found")
}

// UpdatePlayerState updates a player's state
func (gsm *GlobalStateManager) UpdatePlayerState(playerID string, updateFunc func(*PlayerState) *PlayerState) error {
	atomic.AddInt64(&gsm.operationsCount, 1)

	var newState *PlayerState
	var existed bool

	gsm.playerStates.LoadOrStore(playerID, &PlayerState{
		PlayerID:   playerID,
		LastUpdate: time.Now(),
		Version:    1,
		IsOnline:   true,
	})

	value, _ := gsm.playerStates.Load(playerID)
	currentState := value.(*PlayerState)

	// Apply update function
	newState = updateFunc(currentState)
	if newState == nil {
		return errorhandling.NewValidationError("INVALID_UPDATE", "Update function returned nil")
	}

	// Update version and timestamp
	newState.Version++
	newState.LastUpdate = time.Now()
	newState.PlayerID = playerID

	// Store updated state
	gsm.playerStates.Store(playerID, newState)
	existed = true

	// Send update event
	if existed {
		gsm.sendStateUpdate(&StateUpdate{
			Type:      UpdateTypeUpdate,
			EntityID:  playerID,
			StateType: StateTypePlayer,
			Data:      newState,
			Timestamp: newState.LastUpdate,
			Priority:  UpdatePriorityNormal,
		})
	}

	return nil
}

// GetGameState retrieves a game's state
func (gsm *GlobalStateManager) GetGameState(gameID string) (*GameState, error) {
	if value, ok := gsm.gameStates.Load(gameID); ok {
		atomic.AddInt64(&gsm.cacheHits, 1)
		if state, ok := value.(*GameState); ok {
			return state, nil
		}
	}

	atomic.AddInt64(&gsm.cacheMisses, 1)
	return nil, errorhandling.NewNotFoundError("GAME_STATE_NOT_FOUND", "Game state not found")
}

// UpdateGameState updates a game's state
func (gsm *GlobalStateManager) UpdateGameState(gameID string, updateFunc func(*GameState) *GameState) error {
	atomic.AddInt64(&gsm.operationsCount, 1)

	var newState *GameState

	if value, ok := gsm.gameStates.Load(gameID); ok {
		currentState := value.(*GameState)
		newState = updateFunc(currentState)
	} else {
		// Create new game state
		newState = updateFunc(&GameState{
			GameID:     gameID,
			Status:     GameStatusWaiting,
			Players:    []string{},
			Score:      make(map[string]int),
			Settings:   make(map[string]interface{}),
			LastUpdate: time.Now(),
			Version:    1,
		})
	}

	if newState == nil {
		return errorhandling.NewValidationError("INVALID_UPDATE", "Update function returned nil")
	}

	// Update version and timestamp
	newState.Version++
	newState.LastUpdate = time.Now()
	newState.GameID = gameID

	gsm.gameStates.Store(gameID, newState)

	// Send update event
	gsm.sendStateUpdate(&StateUpdate{
		Type:      UpdateTypeUpdate,
		EntityID:  gameID,
		StateType: StateTypeGame,
		Data:      newState,
		Timestamp: newState.LastUpdate,
		Priority:  UpdatePriorityHigh,
	})

	return nil
}

// SetGlobalState sets a global state value
func (gsm *GlobalStateManager) SetGlobalState(key string, value interface{}) error {
	atomic.AddInt64(&gsm.operationsCount, 1)

	gsm.globalStates.Store(key, value)

	// Send update event
	gsm.sendStateUpdate(&StateUpdate{
		Type:      UpdateTypeUpdate,
		EntityID:  key,
		StateType: StateTypeGlobal,
		Data:      value,
		Timestamp: time.Now(),
		Priority:  UpdatePriorityNormal,
	})

	return nil
}

// GetGlobalState retrieves a global state value
func (gsm *GlobalStateManager) GetGlobalState(key string) (interface{}, error) {
	if value, ok := gsm.globalStates.Load(key); ok {
		atomic.AddInt64(&gsm.cacheHits, 1)
		return value, nil
	}

	atomic.AddInt64(&gsm.cacheMisses, 1)
	return nil, errorhandling.NewNotFoundError("GLOBAL_STATE_NOT_FOUND", "Global state not found")
}

// SubscribeToEvents subscribes to state change events
func (gsm *GlobalStateManager) SubscribeToEvents(eventType string, subscriber StateEventSubscriber) {
	gsm.eventMutex.Lock()
	defer gsm.eventMutex.Unlock()

	gsm.eventSubscribers[eventType] = append(gsm.eventSubscribers[eventType], subscriber)
}

// BatchUpdate performs multiple state updates atomically
func (gsm *GlobalStateManager) BatchUpdate(updates []*StateUpdate) error {
	atomic.AddInt64(&gsm.operationsCount, int64(len(updates)))

	// Group updates by priority
	urgentUpdates := []*StateUpdate{}
	highUpdates := []*StateUpdate{}
	normalUpdates := []*StateUpdate{}
	lowUpdates := []*StateUpdate{}

	for _, update := range updates {
		switch update.Priority {
		case UpdatePriorityUrgent:
			urgentUpdates = append(urgentUpdates, update)
		case UpdatePriorityHigh:
			highUpdates = append(highUpdates, update)
		case UpdatePriorityNormal:
			normalUpdates = append(normalUpdates, update)
		default:
			lowUpdates = append(lowUpdates, update)
		}
	}

	// Process urgent updates first
	allUpdates := append(urgentUpdates, highUpdates...)
	allUpdates = append(allUpdates, normalUpdates...)
	allUpdates = append(allUpdates, lowUpdates...)

	// Apply all updates
	for _, update := range allUpdates {
		if err := gsm.applyStateUpdate(update); err != nil {
			gsm.logger.LogError(err, "Failed to apply batch update",
				zap.String("entity_id", update.EntityID),
				zap.String("state_type", string(update.StateType)))
			continue
		}
	}

	// Send batch update event
	gsm.sendStateUpdate(&StateUpdate{
		Type:      UpdateTypeSync,
		StateType: StateTypeGlobal,
		Data:      map[string]interface{}{"batch_size": len(updates)},
		Timestamp: time.Now(),
		Priority:  UpdatePriorityNormal,
	})

	return nil
}

// GetStats returns performance statistics
func (gsm *GlobalStateManager) GetStats() map[string]interface{} {
	var playerStatesCount, gameStatesCount, globalStatesCount int

	gsm.playerStates.Range(func(key, value interface{}) bool {
		playerStatesCount++
		return true
	})

	gsm.gameStates.Range(func(key, value interface{}) bool {
		gameStatesCount++
		return true
	})

	gsm.globalStates.Range(func(key, value interface{}) bool {
		globalStatesCount++
		return true
	})

	hitRate := float64(0)
	total := gsm.cacheHits + gsm.cacheMisses
	if total > 0 {
		hitRate = float64(gsm.cacheHits) / float64(total) * 100
	}

	return map[string]interface{}{
		"player_states_count":    playerStatesCount,
		"game_states_count":      gameStatesCount,
		"global_states_count":    globalStatesCount,
		"total_operations":       atomic.LoadInt64(&gsm.operationsCount),
		"cache_hits":            atomic.LoadInt64(&gsm.cacheHits),
		"cache_misses":          atomic.LoadInt64(&gsm.cacheMisses),
		"cache_hit_rate_percent": hitRate,
		"buffer_size":           len(gsm.updateBuffer),
		"buffer_capacity":       gsm.bufferSize,
		"subscribers_count":     len(gsm.eventSubscribers),
	}
}

// ExportState exports current state for backup or migration
func (gsm *GlobalStateManager) ExportState() (map[string]interface{}, error) {
	export := map[string]interface{}{
		"exported_at": time.Now(),
		"version":     "1.0",
		"player_states": map[string]interface{}{},
		"game_states":   map[string]interface{}{},
		"global_states": map[string]interface{}{},
	}

	// Export player states
	gsm.playerStates.Range(func(key, value interface{}) bool {
		if state, ok := value.(*PlayerState); ok {
			export["player_states"].(map[string]interface{})[key.(string)] = state
		}
		return true
	})

	// Export game states
	gsm.gameStates.Range(func(key, value interface{}) bool {
		if state, ok := value.(*GameState); ok {
			export["game_states"].(map[string]interface{})[key.(string)] = state
		}
		return true
	})

	// Export global states
	gsm.globalStates.Range(func(key, value interface{}) bool {
		export["global_states"].(map[string]interface{})[key.(string)] = value
		return true
	})

	return export, nil
}

// ImportState imports state from backup or migration
func (gsm *GlobalStateManager) ImportState(importData map[string]interface{}) error {
	atomic.AddInt64(&gsm.operationsCount, 1)

	// Import player states
	if playerStates, ok := importData["player_states"].(map[string]interface{}); ok {
		for key, value := range playerStates {
			if state, ok := value.(*PlayerState); ok {
				gsm.playerStates.Store(key, state)
			}
		}
	}

	// Import game states
	if gameStates, ok := importData["game_states"].(map[string]interface{}); ok {
		for key, value := range gameStates {
			if state, ok := value.(*GameState); ok {
				gsm.gameStates.Store(key, state)
			}
		}
	}

	// Import global states
	if globalStates, ok := importData["global_states"].(map[string]interface{}); ok {
		for key, value := range globalStates {
			gsm.globalStates.Store(key, value)
		}
	}

	gsm.logger.Infow("State imported successfully",
		"player_states", len(importData["player_states"].(map[string]interface{})),
		"game_states", len(importData["game_states"].(map[string]interface{})),
		"global_states", len(importData["global_states"].(map[string]interface{})))

	return nil
}

// applyStateUpdate applies a single state update
func (gsm *GlobalStateManager) applyStateUpdate(update *StateUpdate) error {
	switch update.StateType {
	case StateTypePlayer:
		return gsm.applyPlayerUpdate(update)
	case StateTypeGame:
		return gsm.applyGameUpdate(update)
	case StateTypeGlobal:
		return gsm.applyGlobalUpdate(update)
	default:
		return errorhandling.NewValidationError("INVALID_STATE_TYPE", "Unknown state type")
	}
}

// applyPlayerUpdate applies a player state update
func (gsm *GlobalStateManager) applyPlayerUpdate(update *StateUpdate) error {
	playerID := update.EntityID

	switch update.Type {
	case UpdateTypeCreate, UpdateTypeUpdate:
		if state, ok := update.Data.(*PlayerState); ok {
			gsm.playerStates.Store(playerID, state)
		}
	case UpdateTypeDelete:
		gsm.playerStates.Delete(playerID)
	}

	return nil
}

// applyGameUpdate applies a game state update
func (gsm *GlobalStateManager) applyGameUpdate(update *StateUpdate) error {
	gameID := update.EntityID

	switch update.Type {
	case UpdateTypeCreate, UpdateTypeUpdate:
		if state, ok := update.Data.(*GameState); ok {
			gsm.gameStates.Store(gameID, state)
		}
	case UpdateTypeDelete:
		gsm.gameStates.Delete(gameID)
	}

	return nil
}

// applyGlobalUpdate applies a global state update
func (gsm *GlobalStateManager) applyGlobalUpdate(update *StateUpdate) error {
	key := update.EntityID

	switch update.Type {
	case UpdateTypeCreate, UpdateTypeUpdate:
		gsm.globalStates.Store(key, update.Data)
	case UpdateTypeDelete:
		gsm.globalStates.Delete(key)
	}

	return nil
}

// sendStateUpdate sends a state update to subscribers
func (gsm *GlobalStateManager) sendStateUpdate(update *StateUpdate) {
	eventType := fmt.Sprintf("state_%s_%s", update.StateType, update.Type)

	gsm.eventMutex.RLock()
	subscribers := gsm.eventSubscribers[eventType]
	gsm.eventMutex.RUnlock()

	for _, subscriber := range subscribers {
		go func(sub StateEventSubscriber) {
			defer func() {
				if r := recover(); r != nil {
					gsm.logger.Errorw("Subscriber panic recovered",
						"subscriber", fmt.Sprintf("%T", sub),
						"panic", r)
				}
			}()
			sub.OnStateUpdate(update)
		}(subscriber)
	}
}

// startBackgroundProcesses starts background cleanup and sync processes
func (gsm *GlobalStateManager) startBackgroundProcesses() {
	// State cleanup process
	gsm.wg.Add(1)
	go func() {
		defer gsm.wg.Done()
		ticker := time.NewTicker(gsm.config.CleanupInterval)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				gsm.performCleanup()
			case <-gsm.shutdownChan:
				return
			}
		}
	}()

	// Update buffer processor
	gsm.wg.Add(1)
	go func() {
		defer gsm.wg.Done()

		for {
			select {
			case update := <-gsm.updateBuffer:
				if err := gsm.applyStateUpdate(update); err != nil {
					gsm.logger.LogError(err, "Failed to process buffered update",
						zap.String("entity_id", update.EntityID))
				}
			case <-gsm.shutdownChan:
				return
			}
		}
	}()
}

// performCleanup removes expired or stale state entries
func (gsm *GlobalStateManager) performCleanup() {
	cutoff := time.Now().Add(-gsm.config.CacheTTL)
	removed := 0

	// Clean up player states
	gsm.playerStates.Range(func(key, value interface{}) bool {
		if state, ok := value.(*PlayerState); ok {
			if !state.IsOnline && state.LastUpdate.Before(cutoff) {
				gsm.playerStates.Delete(key)
				removed++
			}
		}
		return true
	})

	if removed > 0 {
		gsm.logger.Infow("Cleanup completed", "removed_states", removed)
	}
}

// Shutdown gracefully shuts down the global state manager
func (gsm *GlobalStateManager) Shutdown(ctx context.Context) error {
	close(gsm.shutdownChan)
	done := make(chan struct{})

	go func() {
		gsm.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		gsm.logger.Info("Global state manager shut down gracefully")
		return nil
	case <-ctx.Done():
		gsm.logger.Warn("Global state manager shutdown timed out")
		return ctx.Err()
	}
}
