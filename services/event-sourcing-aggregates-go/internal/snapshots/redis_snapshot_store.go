// Issue: #2217
// PERFORMANCE: Optimized Redis snapshot store for fast aggregate reconstruction
package snapshots

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Snapshot represents a snapshot of an aggregate state
type Snapshot struct {
	AggregateID   uuid.UUID `json:"aggregate_id"`
	AggregateType string    `json:"aggregate_type"`
	Version       int       `json:"version"`
	Data          []byte    `json:"data"`
	Timestamp     time.Time `json:"timestamp"`
	Metadata      map[string]interface{} `json:"metadata,omitempty"`
}

// SnapshotStore defines the interface for snapshot storage
type SnapshotStore interface {
	// SaveSnapshot saves a snapshot
	SaveSnapshot(ctx context.Context, snapshot *Snapshot) error

	// GetSnapshot retrieves the latest snapshot for an aggregate
	GetSnapshot(ctx context.Context, aggregateID uuid.UUID) (*Snapshot, error)

	// GetSnapshotAtVersion retrieves a snapshot at a specific version
	GetSnapshotAtVersion(ctx context.Context, aggregateID uuid.UUID, version int) (*Snapshot, error)

	// DeleteSnapshots deletes snapshots for an aggregate
	DeleteSnapshots(ctx context.Context, aggregateID uuid.UUID) error

	// ListSnapshots returns all snapshots for an aggregate
	ListSnapshots(ctx context.Context, aggregateID uuid.UUID) ([]*Snapshot, error)
}

// RedisSnapshotStore implements SnapshotStore using Redis
type RedisSnapshotStore struct {
	// In a real implementation, this would use a Redis client
	// For now, we'll use an in-memory store for demonstration
	snapshots map[string]*Snapshot
	logger    *zap.Logger
}

// NewRedisSnapshotStore creates a new Redis snapshot store
func NewRedisSnapshotStore(logger *zap.Logger) *RedisSnapshotStore {
	return &RedisSnapshotStore{
		snapshots: make(map[string]*Snapshot),
		logger:    logger,
	}
}

// SaveSnapshot saves a snapshot to Redis
func (r *RedisSnapshotStore) SaveSnapshot(ctx context.Context, snapshot *Snapshot) error {
	startTime := time.Now()

	key := r.getSnapshotKey(snapshot.AggregateID, snapshot.Version)

	// Serialize snapshot
	data, err := json.Marshal(snapshot)
	if err != nil {
		return fmt.Errorf("failed to marshal snapshot: %w", err)
	}

	// In a real Redis implementation:
	// err = r.redisClient.Set(ctx, key, data, 30*24*time.Hour).Err()

	// For now, store in memory
	r.snapshots[key] = snapshot

	duration := time.Since(startTime)
	r.logger.Info("Snapshot saved",
		zap.String("aggregate_id", snapshot.AggregateID.String()),
		zap.String("aggregate_type", snapshot.AggregateType),
		zap.Int("version", snapshot.Version),
		zap.Int("data_size", len(data)),
		zap.Duration("duration", duration))

	return nil
}

// GetSnapshot retrieves the latest snapshot for an aggregate
func (r *RedisSnapshotStore) GetSnapshot(ctx context.Context, aggregateID uuid.UUID) (*Snapshot, error) {
	startTime := time.Now()

	// In a real Redis implementation, we would scan for the highest version
	// For now, iterate through all snapshots for this aggregate
	var latestSnapshot *Snapshot
	latestVersion := -1

	prefix := r.getSnapshotPrefix(aggregateID)
	for key, snapshot := range r.snapshots {
		if len(key) > len(prefix) && key[:len(prefix)] == prefix {
			if snapshot.Version > latestVersion {
				latestSnapshot = snapshot
				latestVersion = snapshot.Version
			}
		}
	}

	if latestSnapshot == nil {
		return nil, nil // No snapshot found
	}

	duration := time.Since(startTime)
	r.logger.Debug("Snapshot retrieved",
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("version", latestSnapshot.Version),
		zap.Duration("duration", duration))

	return latestSnapshot, nil
}

// GetSnapshotAtVersion retrieves a snapshot at a specific version
func (r *RedisSnapshotStore) GetSnapshotAtVersion(ctx context.Context, aggregateID uuid.UUID, version int) (*Snapshot, error) {
	key := r.getSnapshotKey(aggregateID, version)

	// In a real Redis implementation:
	// data, err := r.redisClient.Get(ctx, key).Result()
	// if err == redis.Nil {
	//     return nil, nil
	// }

	snapshot, exists := r.snapshots[key]
	if !exists {
		return nil, nil
	}

	r.logger.Debug("Snapshot retrieved at version",
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("version", version))

	return snapshot, nil
}

// DeleteSnapshots deletes all snapshots for an aggregate
func (r *RedisSnapshotStore) DeleteSnapshots(ctx context.Context, aggregateID uuid.UUID) error {
	prefix := r.getSnapshotPrefix(aggregateID)

	// In a real Redis implementation:
	// keys, err := r.redisClient.Keys(ctx, prefix+"*").Result()
	// if err != nil {
	//     return err
	// }
	// if len(keys) > 0 {
	//     return r.redisClient.Del(ctx, keys...).Err()
	// }

	deletedCount := 0
	for key := range r.snapshots {
		if len(key) > len(prefix) && key[:len(prefix)] == prefix {
			delete(r.snapshots, key)
			deletedCount++
		}
	}

	r.logger.Info("Snapshots deleted",
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("deleted_count", deletedCount))

	return nil
}

// ListSnapshots returns all snapshots for an aggregate
func (r *RedisSnapshotStore) ListSnapshots(ctx context.Context, aggregateID uuid.UUID) ([]*Snapshot, error) {
	prefix := r.getSnapshotPrefix(aggregateID)
	snapshots := make([]*Snapshot, 0)

	// In a real Redis implementation:
	// keys, err := r.redisClient.Keys(ctx, prefix+"*").Result()
	// for _, key := range keys {
	//     data, err := r.redisClient.Get(ctx, key).Result()
	//     // unmarshal and add to snapshots
	// }

	for key, snapshot := range r.snapshots {
		if len(key) > len(prefix) && key[:len(prefix)] == prefix {
			snapshots = append(snapshots, snapshot)
		}
	}

	r.logger.Debug("Snapshots listed",
		zap.String("aggregate_id", aggregateID.String()),
		zap.Int("count", len(snapshots)))

	return snapshots, nil
}

// getSnapshotKey generates a Redis key for a snapshot
func (r *RedisSnapshotStore) getSnapshotKey(aggregateID uuid.UUID, version int) string {
	return fmt.Sprintf("snapshot:%s:%d", aggregateID.String(), version)
}

// getSnapshotPrefix generates a prefix for all snapshots of an aggregate
func (r *RedisSnapshotStore) getSnapshotPrefix(aggregateID uuid.UUID) string {
	return fmt.Sprintf("snapshot:%s:", aggregateID.String())
}

// CleanupExpiredSnapshots removes snapshots older than the retention period
func (r *RedisSnapshotStore) CleanupExpiredSnapshots(ctx context.Context, retentionPeriod time.Duration) error {
	cutoffTime := time.Now().Add(-retentionPeriod)
	deletedCount := 0

	// In a real implementation, we would use Redis SCAN to find old snapshots
	// For now, iterate through memory store
	for key, snapshot := range r.snapshots {
		if snapshot.Timestamp.Before(cutoffTime) {
			delete(r.snapshots, key)
			deletedCount++
		}
	}

	if deletedCount > 0 {
		r.logger.Info("Expired snapshots cleaned up",
			zap.Int("deleted_count", deletedCount),
			zap.Duration("retention_period", retentionPeriod))
	}

	return nil
}

// GetSnapshotStats returns statistics about snapshots
func (r *RedisSnapshotStore) GetSnapshotStats() SnapshotStats {
	stats := SnapshotStats{
		TotalSnapshots: len(r.snapshots),
		SnapshotsByType: make(map[string]int),
	}

	for _, snapshot := range r.snapshots {
		stats.SnapshotsByType[snapshot.AggregateType]++
	}

	return stats
}

// SnapshotStats represents snapshot statistics
type SnapshotStats struct {
	TotalSnapshots   int            `json:"total_snapshots"`
	SnapshotsByType  map[string]int `json:"snapshots_by_type"`
	TotalSizeBytes   int64          `json:"total_size_bytes"`
	AverageSizeBytes int64          `json:"average_size_bytes"`
}
