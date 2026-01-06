package state

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
)

// Config holds state manager configuration
type Config struct {
	DB     *pgxpool.Pool
	Redis  *redis.Client
	Logger *zap.Logger
	Meter  metric.Meter
}

// Manager manages synchronization state
type Manager struct {
	config Config
	logger *zap.Logger
	db     *pgxpool.Pool
	redis  *redis.Client
	meter  metric.Meter
}

// NewManager creates a new state manager
func NewManager(config Config) *Manager {
	return &Manager{
		config: config,
		logger: config.Logger,
		db:     config.DB,
		redis:  config.Redis,
		meter:  config.Meter,
	}
}

// GetState retrieves synchronization state
func (m *Manager) GetState(ctx context.Context, key, category string) (*State, error) {
	m.logger.Info("Getting state",
		zap.String("key", key),
		zap.String("category", category))

	// PERFORMANCE: Try Redis cache first
	cacheKey := m.buildCacheKey(key, category)
	cachedData, err := m.redis.Get(ctx, cacheKey).Result()

	if err == nil && cachedData != "" {
		// Cache hit - deserialize and return
		var state State
		if err := json.Unmarshal([]byte(cachedData), &state); err == nil {
			m.logger.Debug("State retrieved from cache",
				zap.String("key", key),
				zap.String("category", category),
				zap.Int("version", state.Version))
			return &state, nil
		}
		m.logger.Warn("Failed to unmarshal cached state, falling back to database",
			zap.String("key", key),
			zap.String("category", category),
			zap.Error(err))
	}

	// Cache miss or error - retrieve from database
	query := `
		SELECT key, category, value, version, updated_at
		FROM sync.states
		WHERE key = $1 AND category = $2
	`

	var (
		dbKey, dbCategory string
		dbValue           []byte
		dbVersion         int
		dbUpdatedAt       time.Time
	)

	err = m.db.QueryRow(ctx, query, key, category).Scan(
		&dbKey, &dbCategory, &dbValue, &dbVersion, &dbUpdatedAt,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			m.logger.Info("State not found in database",
				zap.String("key", key),
				zap.String("category", category))
			return nil, errors.New("state not found")
		}
		m.logger.Error("Failed to retrieve state from database",
			zap.String("key", key),
			zap.String("category", category),
			zap.Error(err))
		return nil, errors.Wrap(err, "failed to retrieve state from database")
	}

	// Deserialize value from JSONB
	var value interface{}
	if len(dbValue) > 0 {
		if err := json.Unmarshal(dbValue, &value); err != nil {
			m.logger.Warn("Failed to unmarshal state value from database",
				zap.String("key", key),
				zap.String("category", category),
				zap.Error(err))
			// Continue with nil value
		}
	}

	state := &State{
		Key:      dbKey,
		Category: dbCategory,
		Value:    value,
		Version:  dbVersion,
	}

	// PERFORMANCE: Cache the result for 5 minutes
	stateJSON, _ := json.Marshal(state)
	m.redis.Set(ctx, cacheKey, stateJSON, 5*time.Minute)

	m.logger.Info("State retrieved successfully",
		zap.String("key", key),
		zap.String("category", category),
		zap.Int("version", state.Version))

	return state, nil
}

// UpdateState updates synchronization state with conflict detection
func (m *Manager) UpdateState(ctx context.Context, state *State) error {
	m.logger.Info("Updating state",
		zap.String("key", state.Key),
		zap.String("category", state.Category),
		zap.Int("version", state.Version))

	// Serialize value to JSONB
	valueJSON, err := json.Marshal(state.Value)
	if err != nil {
		m.logger.Error("Failed to marshal state value",
			zap.String("key", state.Key),
			zap.String("category", state.Category),
			zap.Error(err))
		return errors.Wrap(err, "failed to marshal state value")
	}

	// Use optimistic locking with version check for conflict detection
	query := `
		UPDATE sync.states
		SET value = $1, version = version + 1, updated_at = $2
		WHERE key = $3 AND category = $4 AND version = $5
		RETURNING version
	`

	var newVersion int
	err = m.db.QueryRow(ctx, query, valueJSON, time.Now(), state.Key, state.Category, state.Version).Scan(&newVersion)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Version mismatch - conflict detected
			m.logger.Warn("State update conflict detected",
				zap.String("key", state.Key),
				zap.String("category", state.Category),
				zap.Int("expected_version", state.Version))

			// Get current state to return conflict information
			currentState, getErr := m.GetState(ctx, state.Key, state.Category)
			if getErr != nil {
				return errors.Wrap(getErr, "failed to get current state after conflict")
			}

			return &StateConflictError{
				Key:            state.Key,
				Category:       state.Category,
				ExpectedVersion: state.Version,
				CurrentVersion: currentState.Version,
				Message:        "state version conflict detected",
			}
		}
		m.logger.Error("Failed to update state in database",
			zap.String("key", state.Key),
			zap.String("category", state.Category),
			zap.Error(err))
		return errors.Wrap(err, "failed to update state in database")
	}

	// Update successful - invalidate cache and update with new version
	state.Version = newVersion

	cacheKey := m.buildCacheKey(state.Key, state.Category)
	stateJSON, _ := json.Marshal(state)
	m.redis.Set(ctx, cacheKey, stateJSON, 5*time.Minute)

	m.logger.Info("State updated successfully",
		zap.String("key", state.Key),
		zap.String("category", state.Category),
		zap.Int("new_version", newVersion))

	return nil
}

// State represents synchronization state
type State struct {
	Key      string      `json:"key"`
	Category string      `json:"category"`
	Value    interface{} `json:"value"`
	Version  int         `json:"version"`
}

// StateConflictError represents a state update conflict
type StateConflictError struct {
	Key             string
	Category        string
	ExpectedVersion int
	CurrentVersion  int
	Message         string
}

func (e *StateConflictError) Error() string {
	return fmt.Sprintf("state conflict: %s (key=%s, category=%s, expected_version=%d, current_version=%d)",
		e.Message, e.Key, e.Category, e.ExpectedVersion, e.CurrentVersion)
}

// buildCacheKey builds a Redis cache key for state
func (m *Manager) buildCacheKey(key, category string) string {
	return fmt.Sprintf("sync:state:%s:%s", category, key)
}
