package conflict

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"reflect"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
)

// Config holds conflict resolver configuration
type Config struct {
	DB     *pgxpool.Pool
	Redis  *redis.Client
	Logger *zap.Logger
	Meter  metric.Meter
}

// Resolver handles conflict detection and resolution
type Resolver struct {
	config Config
	logger *zap.Logger
	db     *pgxpool.Pool
	redis  *redis.Client
	meter  metric.Meter
}

// NewResolver creates a new conflict resolver
func NewResolver(config Config) *Resolver {
	return &Resolver{
		config: config,
		logger: config.Logger,
		db:     config.DB,
		redis:  config.Redis,
		meter:  config.Meter,
	}
}

// DetectConflict detects conflicts in synchronization state
func (r *Resolver) DetectConflict(ctx context.Context, key, category string, oldState, newState interface{}) (*Conflict, error) {
	// Check if states are different (basic conflict detection)
	if r.statesEqual(oldState, newState) {
		return nil, nil // No conflict
	}

	// Determine conflict type
	conflictType := r.determineConflictType(key, category, oldState, newState)

	// Check if this is a critical conflict requiring resolution
	if r.isCriticalConflict(conflictType, category) {
		conflict := &Conflict{
			ID:           r.generateConflictID(),
			Key:          key,
			Category:     category,
			OldState:     oldState,
			NewState:     newState,
			ConflictType: conflictType.String(),
			DetectedAt:   time.Now().Unix(),
			Resolution:   "",
			Metadata:     r.generateConflictMetadata(key, category, oldState, newState),
			Priority:     r.calculateConflictPriority(conflictType, category),
		}

		// Persist conflict for later resolution
		if err := r.persistConflict(ctx, conflict); err != nil {
			r.logger.Error("failed to persist conflict", zap.Error(err), zap.String("conflict_id", conflict.ID))
			return nil, err
		}

		r.logger.Info("conflict detected",
			zap.String("conflict_id", conflict.ID),
			zap.String("key", key),
			zap.String("category", category),
			zap.String("type", conflictType.String()),
			zap.Int("priority", conflict.Priority))

		// Publish conflict detection event
		r.publishConflictEvent(ctx, "detected", conflict)

		return conflict, nil
	}

	// For non-critical conflicts, auto-resolve using last-write-wins
	r.logger.Info("auto-resolving non-critical conflict",
		zap.String("key", key),
		zap.String("category", category),
		zap.String("strategy", "last_write_wins"))

	return nil, nil
}

// ResolveConflict resolves a detected conflict
func (r *Resolver) ResolveConflict(ctx context.Context, conflict *Conflict) error {
	r.logger.Info("resolving conflict",
		zap.String("conflict_id", conflict.ID),
		zap.String("strategy", conflict.Resolution))

	var resolvedState interface{}
	var err error

	// Apply resolution strategy
	switch conflict.Resolution {
	case "last_write_wins":
		resolvedState = conflict.NewState
	case "merge":
		resolvedState, err = r.mergeStates(conflict.OldState, conflict.NewState, MergeStrategyUnion)
		if err != nil {
			return errors.Wrap(err, "merge failed")
		}
	case "keep_old":
		resolvedState = conflict.OldState
	case "manual":
		return errors.New("manual resolution required")
	default:
		return errors.New("unknown resolution strategy")
	}

	// Update conflict record
	now := time.Now().Unix()
	conflict.ResolvedAt = &now

	if err := r.updateConflictResolution(ctx, conflict); err != nil {
		return errors.Wrap(err, "failed to update conflict resolution")
	}

	// Publish resolution event
	r.publishConflictEvent(ctx, "resolved", conflict)

	// Apply resolved state to synchronization
	if err := r.applyResolvedState(ctx, conflict.Key, conflict.Category, resolvedState); err != nil {
		r.logger.Error("failed to apply resolved state",
			zap.Error(err),
			zap.String("conflict_id", conflict.ID))
		return errors.Wrap(err, "failed to apply resolved state")
	}

	r.logger.Info("conflict resolved successfully",
		zap.String("conflict_id", conflict.ID),
		zap.String("strategy", conflict.Resolution))

	return nil
}

// statesEqual compares two states for equality
func (r *Resolver) statesEqual(state1, state2 interface{}) bool {
	if state1 == nil && state2 == nil {
		return true
	}
	if state1 == nil || state2 == nil {
		return false
	}

	// Use reflection for deep comparison
	return reflect.DeepEqual(state1, state2)
}

// determineConflictType analyzes states to determine conflict type
func (r *Resolver) determineConflictType(key, category string, oldState, newState interface{}) ConflictType {
	// Analyze based on category and state differences
	switch category {
	case "user_inventory":
		return r.analyzeInventoryConflict(oldState, newState)
	case "game_state":
		return r.analyzeGameStateConflict(oldState, newState)
	case "user_profile":
		return r.analyzeProfileConflict(oldState, newState)
	default:
		return TypeConcurrentModification
	}
}

// analyzeInventoryConflict analyzes inventory-specific conflicts
func (r *Resolver) analyzeInventoryConflict(oldState, newState interface{}) ConflictType {
	// Check for quantity inconsistencies, item conflicts, etc.
	oldMap, ok1 := oldState.(map[string]interface{})
	newMap, ok2 := newState.(map[string]interface{})

	if !ok1 || !ok2 {
		return TypeDataInconsistency
	}

	// Check for currency conflicts
	if oldMap["currency"] != newMap["currency"] {
		return TypeConcurrentModification
	}

	// Check for item conflicts
	oldItems := oldMap["items"]
	newItems := newMap["items"]
	if !reflect.DeepEqual(oldItems, newItems) {
		return TypeConcurrentModification
	}

	return TypeDataInconsistency
}

// analyzeGameStateConflict analyzes game state conflicts
func (r *Resolver) analyzeGameStateConflict(oldState, newState interface{}) ConflictType {
	// Check for position conflicts, stat inconsistencies, etc.
	oldMap, ok1 := oldState.(map[string]interface{})
	newMap, ok2 := newState.(map[string]interface{})

	if !ok1 || !ok2 {
		return TypeDataInconsistency
	}

	// Check for critical state conflicts
	if oldMap["level"] != newMap["level"] {
		return TypeVersionConflict
	}

	return TypeConcurrentModification
}

// analyzeProfileConflict analyzes user profile conflicts
func (r *Resolver) analyzeProfileConflict(oldState, newState interface{}) ConflictType {
	oldMap, ok1 := oldState.(map[string]interface{})
	newMap, ok2 := newState.(map[string]interface{})

	if !ok1 || !ok2 {
		return TypeDataInconsistency
	}

	// Check for username conflicts (should not happen)
	if oldMap["username"] != newMap["username"] {
		return TypeBusinessRuleViolation
	}

	return TypeConcurrentModification
}

// isCriticalConflict determines if conflict requires resolution
func (r *Resolver) isCriticalConflict(conflictType ConflictType, category string) bool {
	// Define which conflicts are critical based on type and category
	switch conflictType {
	case TypeBusinessRuleViolation:
		return true
	case TypeSchemaMismatch:
		return true
	case TypeVersionConflict:
		return category == "user_profile" || category == "game_state"
	case TypeDataInconsistency:
		return true
	case TypeConcurrentModification:
		// Only critical for certain categories
		return category == "user_inventory" || category == "game_achievements"
	default:
		return false
	}
}

// generateConflictID generates a unique conflict ID
func (r *Resolver) generateConflictID() string {
	bytes := make([]byte, 16)
	rand.Read(bytes)
	return "conflict-" + hex.EncodeToString(bytes)[:16]
}

// generateConflictMetadata creates metadata for conflict analysis
func (r *Resolver) generateConflictMetadata(key, category string, oldState, newState interface{}) map[string]interface{} {
	return map[string]interface{}{
		"key":       key,
		"category":  category,
		"timestamp": time.Now().Unix(),
		"old_type":  fmt.Sprintf("%T", oldState),
		"new_type":  fmt.Sprintf("%T", newState),
		"severity":  "medium", // Could be calculated based on impact
	}
}

// calculateConflictPriority calculates conflict priority (1-10)
func (r *Resolver) calculateConflictPriority(conflictType ConflictType, category string) int {
	basePriority := 5

	// Adjust based on conflict type
	switch conflictType {
	case TypeBusinessRuleViolation:
		basePriority += 3
	case TypeSchemaMismatch:
		basePriority += 4
	case TypeVersionConflict:
		basePriority += 2
	}

	// Adjust based on category
	switch category {
	case "user_profile":
		basePriority += 2
	case "user_inventory":
		basePriority += 1
	case "game_achievements":
		basePriority += 1
	}

	if basePriority > 10 {
		basePriority = 10
	}

	return basePriority
}

// persistConflict saves conflict to database
func (r *Resolver) persistConflict(ctx context.Context, conflict *Conflict) error {
	oldStateJSON, _ := json.Marshal(conflict.OldState)
	newStateJSON, _ := json.Marshal(conflict.NewState)
	metadataJSON, _ := json.Marshal(conflict.Metadata)

	query := `
		INSERT INTO conflicts.conflicts (
			id, key, category, old_state, new_state, conflict_type,
			detected_at, priority, metadata
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		conflict.ID, conflict.Key, conflict.Category,
		string(oldStateJSON), string(newStateJSON), conflict.ConflictType,
		time.Unix(conflict.DetectedAt, 0), conflict.Priority, string(metadataJSON))

	return errors.Wrap(err, "failed to persist conflict")
}

// updateConflictResolution updates conflict with resolution information
func (r *Resolver) updateConflictResolution(ctx context.Context, conflict *Conflict) error {
	query := `
		UPDATE conflicts.conflicts
		SET resolution = $1, resolved_at = $2
		WHERE id = $3
	`

	_, err := r.db.Exec(ctx, query,
		conflict.Resolution, time.Unix(*conflict.ResolvedAt, 0), conflict.ID)

	return errors.Wrap(err, "failed to update conflict resolution")
}

// mergeStates merges two conflicting states
func (r *Resolver) mergeStates(oldState, newState interface{}, strategy MergeStrategy) (interface{}, error) {
	switch strategy {
	case MergeStrategyUnion:
		return r.mergeUnion(oldState, newState)
	case MergeStrategyIntersection:
		return r.mergeIntersection(oldState, newState)
	case MergeStrategyOverride:
		return newState, nil
	default:
		return nil, errors.New("unsupported merge strategy")
	}
}

// mergeUnion performs union merge of states
func (r *Resolver) mergeUnion(oldState, newState interface{}) (interface{}, error) {
	oldMap, ok1 := oldState.(map[string]interface{})
	newMap, ok2 := newState.(map[string]interface{})

	if !ok1 || !ok2 {
		return newState, nil // Fallback to new state
	}

	merged := make(map[string]interface{})

	// Add all old keys
	for k, v := range oldMap {
		merged[k] = v
	}

	// Add/override with new keys
	for k, v := range newMap {
		merged[k] = v
	}

	return merged, nil
}

// mergeIntersection performs intersection merge of states
func (r *Resolver) mergeIntersection(oldState, newState interface{}) (interface{}, error) {
	oldMap, ok1 := oldState.(map[string]interface{})
	newMap, ok2 := newState.(map[string]interface{})

	if !ok1 || !ok2 {
		return newState, nil // Fallback to new state
	}

	merged := make(map[string]interface{})

	// Only include keys that exist in both states
	for k, oldVal := range oldMap {
		if newVal, exists := newMap[k]; exists {
			// For conflicting values, prefer new state
			merged[k] = newVal
		} else {
			merged[k] = oldVal
		}
	}

	return merged, nil
}

// applyResolvedState applies the resolved state to synchronization
func (r *Resolver) applyResolvedState(ctx context.Context, key, category string, resolvedState interface{}) error {
	// Store resolved state in Redis for synchronization
	stateKey := fmt.Sprintf("sync:resolved:%s:%s", category, key)
	stateJSON, _ := json.Marshal(resolvedState)

	err := r.redis.Set(ctx, stateKey, string(stateJSON), 24*time.Hour).Err()
	if err != nil {
		return errors.Wrap(err, "failed to store resolved state")
	}

	r.logger.Info("applied resolved state",
		zap.String("key", key),
		zap.String("category", category))

	return nil
}

// publishConflictEvent publishes conflict events to Redis
func (r *Resolver) publishConflictEvent(ctx context.Context, eventType string, conflict *Conflict) {
	event := map[string]interface{}{
		"type":        fmt.Sprintf("conflict_%s", eventType),
		"conflict_id": conflict.ID,
		"key":         conflict.Key,
		"category":    conflict.Category,
		"priority":    conflict.Priority,
		"timestamp":   time.Now().Unix(),
	}

	if conflict.ResolvedAt != nil {
		event["resolution"] = conflict.Resolution
		event["resolved_at"] = *conflict.ResolvedAt
	}

	eventJSON, _ := json.Marshal(event)
	r.redis.Publish(ctx, "conflict-events", eventJSON)
}

// GetPendingConflicts returns unresolved conflicts
func (r *Resolver) GetPendingConflicts(ctx context.Context, limit int) ([]*Conflict, error) {
	query := `
		SELECT id, key, category, old_state, new_state, conflict_type,
		       detected_at, priority, metadata
		FROM conflicts.conflicts
		WHERE resolved_at IS NULL
		ORDER BY priority DESC, detected_at ASC
		LIMIT $1
	`

	rows, err := r.db.Query(ctx, query, limit)
	if err != nil {
		return nil, errors.Wrap(err, "failed to query pending conflicts")
	}
	defer rows.Close()

	var conflicts []*Conflict
	for rows.Next() {
		var conflict Conflict
		var oldStateJSON, newStateJSON, metadataJSON string

		err := rows.Scan(
			&conflict.ID, &conflict.Key, &conflict.Category,
			&oldStateJSON, &newStateJSON, &conflict.ConflictType,
			&conflict.DetectedAt, &conflict.Priority, &metadataJSON,
		)
		if err != nil {
			return nil, errors.Wrap(err, "failed to scan conflict")
		}

		// Unmarshal JSON fields
		json.Unmarshal([]byte(oldStateJSON), &conflict.OldState)
		json.Unmarshal([]byte(newStateJSON), &conflict.NewState)
		json.Unmarshal([]byte(metadataJSON), &conflict.Metadata)

		conflicts = append(conflicts, &conflict)
	}

	return conflicts, nil
}

// AutoResolveConflict attempts automatic conflict resolution
func (r *Resolver) AutoResolveConflict(ctx context.Context, conflict *Conflict) error {
	// Determine resolution strategy based on conflict type and priority
	var resolution string

	switch conflict.ConflictType {
	case "concurrent_modification":
		if conflict.Priority >= 8 {
			resolution = "manual" // High priority conflicts need manual review
		} else {
			resolution = "last_write_wins"
		}
	case "version_conflict":
		resolution = "merge"
	case "data_inconsistency":
		resolution = "keep_old"
	default:
		resolution = "manual"
	}

	conflict.Resolution = resolution

	if resolution == "manual" {
		r.logger.Info("conflict requires manual resolution",
			zap.String("conflict_id", conflict.ID),
			zap.Int("priority", conflict.Priority))
		return errors.New("manual resolution required")
	}

	return r.ResolveConflict(ctx, conflict)
}

// Conflict represents a synchronization conflict
type Conflict struct {
	ID            string                 `json:"id"`
	Key           string                 `json:"key"`
	Category      string                 `json:"category"`
	OldState      interface{}            `json:"old_state"`
	NewState      interface{}            `json:"new_state"`
	ConflictType  string                 `json:"conflict_type"`
	DetectedAt    int64                  `json:"detected_at"`
	ResolvedAt    *int64                 `json:"resolved_at,omitempty"`
	Resolution    string                 `json:"resolution"`
	Metadata      map[string]interface{} `json:"metadata"`
	Priority      int                    `json:"priority"` // 1-10, higher = more critical
}

// ConflictResolution represents resolution strategies
type ConflictResolution int

const (
	ResolutionManual ConflictResolution = iota
	ResolutionLastWriteWins
	ResolutionMerge
	ResolutionCustom
)

// ConflictType represents different types of conflicts
type ConflictType int

const (
	TypeConcurrentModification ConflictType = iota
	TypeDataInconsistency
	TypeVersionConflict
	TypeSchemaMismatch
	TypeBusinessRuleViolation
)

// MergeStrategy represents data merge strategies
type MergeStrategy int

const (
	MergeStrategyUnion MergeStrategy = iota
	MergeStrategyIntersection
	MergeStrategyOverride
	MergeStrategyCustom
)
