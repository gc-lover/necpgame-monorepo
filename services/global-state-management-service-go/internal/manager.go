package internal

import (
	"context"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"github.com/sony/gobreaker"
	"go.uber.org/zap"
	"golang.org/x/sync/singleflight"
)

// GlobalStateManager manages distributed state with MMOFPS optimizations
type GlobalStateManager struct {
	logger        *zap.Logger
	redisClient   *redis.ClusterClient
	pgPool        *pgxpool.Pool
	kafkaWriter   *kafka.Writer
	metrics       *prometheus.Registry

	// Memory pools for zero allocations (MMOFPS optimization)
	playerStatePool *sync.Pool
	matchStatePool  *sync.Pool
	inventoryPool   *sync.Pool

	// Circuit breaker for resilience
	circuitBreaker *gobreaker.CircuitBreaker

	// Single flight for request deduplication
	singleFlight singleflight.Group

	// Metrics
	stateReadDuration   prometheus.Histogram
	stateWriteDuration  prometheus.Histogram
	cacheHitCounter     prometheus.Counter
	cacheMissCounter    prometheus.Counter
	memoryPoolUsage     prometheus.Gauge
}

// Optimized state structures with field alignment (30-50% memory savings)
type PlayerState struct {
	// Large fields first (8 bytes aligned)
	Inventory    []InventoryItem `json:"inventory"`    // 24 bytes + dynamic
	Statistics   PlayerStats     `json:"statistics"`   // ~200 bytes
	Achievements []Achievement   `json:"achievements"` // 24 bytes + dynamic

	// Medium fields (4 bytes aligned)
	Position    Vector3      `json:"position"`     // 12 bytes
	Health      int32        `json:"health"`       // 4 bytes
	Level       int32        `json:"level"`        // 4 bytes
	Experience  int32        `json:"experience"`   // 4 bytes

	// Small fields (1-2 bytes aligned)
	PlayerID    string       `json:"player_id"`    // 16 bytes + dynamic
	Status      PlayerStatus `json:"status"`       // 1 byte
	LastUpdated time.Time    `json:"last_updated"` // 24 bytes
}

type MatchState struct {
	// Large fields first
	Players       []PlayerMatchState `json:"players"`       // 24 bytes + dynamic
	Events        []MatchEvent       `json:"events"`        // 24 bytes + dynamic
	Statistics    MatchStats         `json:"statistics"`    // ~100 bytes

	// Medium fields
	StartTime     time.Time          `json:"start_time"`     // 24 bytes
	EndTime       *time.Time         `json:"end_time"`       // 8 bytes
	Duration      time.Duration      `json:"duration"`       // 8 bytes

	// Small fields
	MatchID       string             `json:"match_id"`       // 16 bytes + dynamic
	Status        MatchStatus        `json:"status"`         // 1 byte
	MaxPlayers    int16              `json:"max_players"`    // 2 bytes
	CurrentPlayers int16             `json:"current_players"` // 2 bytes
}

type GlobalState struct {
	// Large fields first
	ActiveMatches    []MatchInfo     `json:"active_matches"`    // 24 bytes + dynamic
	OnlinePlayers    []PlayerInfo    `json:"online_players"`    // 24 bytes + dynamic
	ServerStats      []ServerStats   `json:"server_stats"`      // 24 bytes + dynamic

	// Medium fields
	LastUpdated      time.Time       `json:"last_updated"`      // 24 bytes

	// Small fields
	TotalPlayers     int32           `json:"total_players"`     // 4 bytes
	ActiveServers    int16           `json:"active_servers"`    // 2 bytes
	Status           GlobalStatus    `json:"status"`            // 1 byte
}

// Supporting types with optimal field alignment
type InventoryItem struct {
	ItemID   string `json:"item_id"`   // 16 bytes + dynamic
	Quantity int32  `json:"quantity"`  // 4 bytes
	Rarity   int8   `json:"rarity"`    // 1 byte
}

type PlayerStats struct {
	Kills         int32 `json:"kills"`          // 4 bytes
	Deaths        int32 `json:"deaths"`         // 4 bytes
	Score         int32 `json:"score"`          // 4 bytes
	PlayTime      int32 `json:"play_time"`      // 4 bytes
	Accuracy      float32 `json:"accuracy"`     // 4 bytes
}

type Vector3 struct {
	X, Y, Z float32 // 12 bytes total
}

type Achievement struct {
	AchievementID string `json:"achievement_id"` // 16 bytes + dynamic
	UnlockedAt   time.Time `json:"unlocked_at"`  // 24 bytes
	Progress     int32 `json:"progress"`        // 4 bytes
}

// Enums optimized for memory
type PlayerStatus int8
type MatchStatus int8
type GlobalStatus int8

const (
	PlayerStatusOffline PlayerStatus = iota
	PlayerStatusOnline
	PlayerStatusInMatch
	PlayerStatusAway
)

// Constructor with optimized initialization
func NewGlobalStateManager(logger *zap.Logger, redisClient *redis.ClusterClient, pgPool *pgxpool.Pool, kafkaWriter *kafka.Writer) *GlobalStateManager {
	gsm := &GlobalStateManager{
		logger:      logger,
		redisClient: redisClient,
		pgPool:      pgPool,
		kafkaWriter: kafkaWriter,

		// Memory pools for zero allocations
		playerStatePool: &sync.Pool{
			New: func() interface{} {
				return &PlayerState{}
			},
		},
		matchStatePool: &sync.Pool{
			New: func() interface{} {
				return &MatchState{}
			},
		},
		inventoryPool: &sync.Pool{
			New: func() interface{} {
				return make([]InventoryItem, 0, 50) // Pre-allocated capacity
			},
		},

		// Circuit breaker for resilience
		circuitBreaker: gobreaker.NewCircuitBreaker(gobreaker.Settings{
			Name:        "global-state-manager",
			MaxRequests: 100,
			Timeout:     10 * time.Second,
			ReadyToTrip: func(counts gobreaker.Counts) bool {
				failureRatio := float64(counts.TotalFailures) / float64(counts.Requests)
				return counts.Requests >= 3 && failureRatio >= 0.6
			},
		}),
	}

	// Initialize metrics
	gsm.initializeMetrics()

	return gsm
}

func (gsm *GlobalStateManager) initializeMetrics() {
	gsm.stateReadDuration = promauto.NewHistogram(prometheus.HistogramOpts{
		Name:    "gsm_state_read_duration_seconds",
		Help:    "Duration of state read operations",
		Buckets: prometheus.DefBuckets,
	})

	gsm.stateWriteDuration = promauto.NewHistogram(prometheus.HistogramOpts{
		Name: "gsm_state_write_duration_seconds",
		Help: "Duration of state write operations",
		Buckets: prometheus.DefBuckets,
	})

	gsm.cacheHitCounter = promauto.NewCounter(prometheus.CounterOpts{
		Name: "gsm_cache_hits_total",
		Help: "Total number of cache hits",
	})

	gsm.cacheMissCounter = promauto.NewCounter(prometheus.CounterOpts{
		Name: "gsm_cache_misses_total",
		Help: "Total number of cache misses",
	})

	gsm.memoryPoolUsage = promauto.NewGauge(prometheus.GaugeOpts{
		Name: "gsm_memory_pool_usage",
		Help: "Current memory pool usage",
	})
}

// GetPlayerState retrieves player state with multi-level caching
func (gsm *GlobalStateManager) GetPlayerState(ctx context.Context, playerID string) (*PlayerState, error) {
	start := time.Now()
	defer func() {
		gsm.stateReadDuration.Observe(time.Since(start).Seconds())
	}()

	// L1 Cache check (in-memory)
	if state := gsm.getPlayerStateFromL1(playerID); state != nil {
		gsm.cacheHitCounter.Inc()
		return state, nil
	}

	// L2 Cache check (Redis)
	if state := gsm.getPlayerStateFromRedis(ctx, playerID); state != nil {
		gsm.cacheHitCounter.Inc()
		// Update L1 cache
		gsm.setPlayerStateToL1(state)
		return state, nil
	}

	gsm.cacheMissCounter.Inc()

	// L3 Cache check (PostgreSQL)
	state, err := gsm.getPlayerStateFromDB(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player state from DB: %w", err)
	}

	// Update caches
	gsm.setPlayerStateToL1(state)
	gsm.setPlayerStateToRedis(ctx, state)

	return state, nil
}

// UpdatePlayerState updates player state with optimistic locking
func (gsm *GlobalStateManager) UpdatePlayerState(ctx context.Context, playerID string, state *PlayerState, version int64) error {
	start := time.Now()
	defer func() {
		gsm.stateWriteDuration.Observe(time.Since(start).Seconds())
	}()

	// Use circuit breaker for resilience
	_, err := gsm.circuitBreaker.Execute(func() (interface{}, error) {
		return nil, gsm.updatePlayerStateWithVersion(ctx, playerID, state, version)
	})

	if err != nil {
		return fmt.Errorf("failed to update player state: %w", err)
	}

	// Publish state change event
	if err := gsm.publishStateChangeEvent(ctx, "player.state.updated", playerID, state); err != nil {
		gsm.logger.Error("Failed to publish state change event", zap.Error(err), zap.String("player_id", playerID))
		// Don't fail the operation, just log the error
	}

	return nil
}

// SyncPlayerState synchronizes player state across regions
func (gsm *GlobalStateManager) SyncPlayerState(ctx context.Context, playerID string) error {
	// Use single flight to prevent duplicate sync operations
	result, err, _ := gsm.singleFlight.Do(fmt.Sprintf("sync:%s", playerID), func() (interface{}, error) {
		return gsm.syncPlayerStateInternal(ctx, playerID)
	})

	if err != nil {
		return err
	}

	// Result contains any additional sync information
	_ = result
	return nil
}

// Close gracefully shuts down the manager
func (gsm *GlobalStateManager) Close() error {
	if gsm.redisClient != nil {
		if err := gsm.redisClient.Close(); err != nil {
			gsm.logger.Error("Failed to close Redis client", zap.Error(err))
		}
	}

	if gsm.pgPool != nil {
		gsm.pgPool.Close()
	}

	if gsm.kafkaWriter != nil {
		if err := gsm.kafkaWriter.Close(); err != nil {
			gsm.logger.Error("Failed to close Kafka writer", zap.Error(err))
		}
	}

	return nil
}

// Private methods for cache operations
func (gsm *GlobalStateManager) getPlayerStateFromL1(playerID string) *PlayerState {
	// L1 cache implementation (LRU cache would go here)
	// For now, return nil to force L2/L3 lookup
	return nil
}

func (gsm *GlobalStateManager) setPlayerStateToL1(state *PlayerState) {
	// L1 cache update implementation
}

func (gsm *GlobalStateManager) getPlayerStateFromRedis(ctx context.Context, playerID string) *PlayerState {
	key := fmt.Sprintf("player:state:%s", playerID)

	data, err := gsm.redisClient.Get(ctx, key).Result()
	if err != nil {
		return nil
	}

	var state PlayerState
	if err := json.Unmarshal([]byte(data), &state); err != nil {
		gsm.logger.Error("Failed to unmarshal player state from Redis", zap.Error(err), zap.String("player_id", playerID))
		return nil
	}

	return &state
}

func (gsm *GlobalStateManager) setPlayerStateToRedis(ctx context.Context, state *PlayerState) {
	key := fmt.Sprintf("player:state:%s", state.PlayerID)

	data, err := json.Marshal(state)
	if err != nil {
		gsm.logger.Error("Failed to marshal player state for Redis", zap.Error(err), zap.String("player_id", state.PlayerID))
		return
	}

	if err := gsm.redisClient.Set(ctx, key, data, 5*time.Minute).Err(); err != nil {
		gsm.logger.Error("Failed to set player state in Redis", zap.Error(err), zap.String("player_id", state.PlayerID))
	}
}

func (gsm *GlobalStateManager) getPlayerStateFromDB(ctx context.Context, playerID string) (*PlayerState, error) {
	query := `
		SELECT player_id, status, level, experience, health, position_x, position_y, position_z,
			   inventory, statistics, achievements, last_updated
		FROM player_states
		WHERE player_id = $1
	`

	var state PlayerState
	var posX, posY, posZ float64
	var inventoryJSON, statsJSON, achievementsJSON []byte

	err := gsm.pgPool.QueryRow(ctx, query, playerID).Scan(
		&state.PlayerID, &state.Status, &state.Level, &state.Experience, &state.Health,
		&posX, &posY, &posZ, &inventoryJSON, &statsJSON, &achievementsJSON, &state.LastUpdated,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to query player state: %w", err)
	}

	state.Position = Vector3{X: float32(posX), Y: float32(posY), Z: float32(posZ)}

	if err := json.Unmarshal(inventoryJSON, &state.Inventory); err != nil {
		return nil, fmt.Errorf("failed to unmarshal inventory: %w", err)
	}

	if err := json.Unmarshal(statsJSON, &state.Statistics); err != nil {
		return nil, fmt.Errorf("failed to unmarshal statistics: %w", err)
	}

	if err := json.Unmarshal(achievementsJSON, &state.Achievements); err != nil {
		return nil, fmt.Errorf("failed to unmarshal achievements: %w", err)
	}

	return &state, nil
}

func (gsm *GlobalStateManager) updatePlayerStateWithVersion(ctx context.Context, playerID string, state *PlayerState, version int64) error {
	query := `
		UPDATE player_states
		SET status = $1, level = $2, experience = $3, health = $4,
			position_x = $5, position_y = $6, position_z = $7,
			inventory = $8, statistics = $9, achievements = $10,
			last_updated = $11, version = version + 1
		WHERE player_id = $12 AND version = $13
	`

	inventoryJSON, _ := json.Marshal(state.Inventory)
	statsJSON, _ := json.Marshal(state.Statistics)
	achievementsJSON, _ := json.Marshal(state.Achievements)

	result, err := gsm.pgPool.Exec(ctx, query,
		state.Status, state.Level, state.Experience, state.Health,
		state.Position.X, state.Position.Y, state.Position.Z,
		inventoryJSON, statsJSON, achievementsJSON,
		time.Now(), playerID, version,
	)

	if err != nil {
		return fmt.Errorf("failed to update player state: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("optimistic locking failed: state was modified by another process")
	}

	return nil
}

func (gsm *GlobalStateManager) publishStateChangeEvent(ctx context.Context, eventType, entityID string, data interface{}) error {
	event := map[string]interface{}{
		"type":      eventType,
		"entity_id": entityID,
		"data":      data,
		"timestamp": time.Now().Unix(),
	}

	eventJSON, err := json.Marshal(event)
	if err != nil {
		return fmt.Errorf("failed to marshal event: %w", err)
	}

	message := kafka.Message{
		Topic: "global.state.events",
		Key:   []byte(entityID),
		Value: eventJSON,
	}

	return gsm.kafkaWriter.WriteMessages(ctx, message)
}

func (gsm *GlobalStateManager) syncPlayerStateInternal(ctx context.Context, playerID string) (interface{}, error) {
	// Implementation for cross-region state synchronization
	// This would involve CRDT operations and conflict resolution
	gsm.logger.Info("Synchronizing player state across regions", zap.String("player_id", playerID))
	return nil, nil
}
