// Redis Snapshot Store for Event Sourcing performance optimization
// Issue: #2217
// Agent: Backend Agent
package snapshots

import (
	"context"
	"encoding/json"
	"fmt"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

// Snapshot represents a snapshot of aggregate state
type Snapshot struct {
	AggregateID   uuid.UUID              `json:"aggregate_id"`
	AggregateType string                 `json:"aggregate_type"`
	Version       int                    `json:"version"`
	State         interface{}            `json:"state"`
	CreatedAt     time.Time              `json:"created_at"`
	Size          int                    `json:"size"` // Size in bytes
	EventCount    int                    `json:"event_count"`
}

// SnapshotStore defines the interface for snapshot storage
type SnapshotStore interface {
	// SaveSnapshot saves an aggregate snapshot
	SaveSnapshot(ctx context.Context, snapshot *Snapshot) error

	// GetLatestSnapshot retrieves the latest snapshot for an aggregate
	GetLatestSnapshot(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (*Snapshot, error)

	// GetSnapshot retrieves snapshot for specific version
	GetSnapshot(ctx context.Context, aggregateID uuid.UUID, aggregateType string, version int) (*Snapshot, error)

	// DeleteSnapshots removes old snapshots for cleanup
	DeleteSnapshots(ctx context.Context, aggregateID uuid.UUID, aggregateType string, keepLast int) error

	// GetSnapshotStats returns statistics about snapshots
	GetSnapshotStats(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (map[string]interface{}, error)

	// Close closes the snapshot store
	Close() error
}

// RedisSnapshotStore implements SnapshotStore using Redis
type RedisSnapshotStore struct {
	client *redis.Client
}

// NewRedisSnapshotStore creates a new Redis snapshot store
func NewRedisSnapshotStore(redisURL string) (*RedisSnapshotStore, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	// Configure for high performance
	opt.PoolSize = 10
	opt.MinIdleConns = 2
	opt.ConnMaxLifetime = time.Hour

	client := redis.NewClient(opt)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	return &RedisSnapshotStore{
		client: client,
	}, nil
}

// SaveSnapshot saves an aggregate snapshot with TTL
func (s *RedisSnapshotStore) SaveSnapshot(ctx context.Context, snapshot *Snapshot) error {
	// Serialize snapshot
	data, err := json.Marshal(snapshot)
	if err != nil {
		return fmt.Errorf("failed to marshal snapshot: %w", err)
	}

	// Create keys for different access patterns
	baseKey := fmt.Sprintf("snapshot:%s:%s", snapshot.AggregateType, snapshot.AggregateID)
	versionKey := fmt.Sprintf("%s:%d", baseKey, snapshot.Version)
	latestKey := fmt.Sprintf("%s:latest", baseKey)

	// Store snapshot data
	pipe := s.client.Pipeline()
	pipe.Set(ctx, versionKey, data, 24*time.Hour) // TTL 24 hours
	pipe.Set(ctx, latestKey, data, 24*time.Hour)

	// Update metadata
	sizeKey := fmt.Sprintf("%s:size", baseKey)
	pipe.Set(ctx, sizeKey, snapshot.Size, 24*time.Hour)

	// Maintain version list for cleanup
	versionsKey := fmt.Sprintf("%s:versions", baseKey)
	pipe.SAdd(ctx, versionsKey, snapshot.Version)
	pipe.Expire(ctx, versionsKey, 24*time.Hour)

	_, err = pipe.Exec(ctx)
	if err != nil {
		return fmt.Errorf("failed to save snapshot: %w", err)
	}

	return nil
}

// GetLatestSnapshot retrieves the latest snapshot for an aggregate
func (s *RedisSnapshotStore) GetLatestSnapshot(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (*Snapshot, error) {
	key := fmt.Sprintf("snapshot:%s:%s:latest", aggregateType, aggregateID)

	data, err := s.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, nil // No snapshot found
		}
		return nil, fmt.Errorf("failed to get snapshot: %w", err)
	}

	var snapshot Snapshot
	if err := json.Unmarshal([]byte(data), &snapshot); err != nil {
		return nil, fmt.Errorf("failed to unmarshal snapshot: %w", err)
	}

	return &snapshot, nil
}

// GetSnapshot retrieves snapshot for specific version
func (s *RedisSnapshotStore) GetSnapshot(ctx context.Context, aggregateID uuid.UUID, aggregateType string, version int) (*Snapshot, error) {
	key := fmt.Sprintf("snapshot:%s:%s:%d", aggregateType, aggregateID, version)

	data, err := s.client.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, nil // No snapshot found
		}
		return nil, fmt.Errorf("failed to get snapshot: %w", err)
	}

	var snapshot Snapshot
	if err := json.Unmarshal([]byte(data), &snapshot); err != nil {
		return nil, fmt.Errorf("failed to unmarshal snapshot: %w", err)
	}

	return &snapshot, nil
}

// DeleteSnapshots removes old snapshots for cleanup
func (s *RedisSnapshotStore) DeleteSnapshots(ctx context.Context, aggregateID uuid.UUID, aggregateType string, keepLast int) error {
	baseKey := fmt.Sprintf("snapshot:%s:%s", aggregateType, aggregateID)
	versionsKey := fmt.Sprintf("%s:versions", baseKey)

	// Get all versions
	versions, err := s.client.SMembers(ctx, versionsKey).Result()
	if err != nil {
		return fmt.Errorf("failed to get versions: %w", err)
	}

	if len(versions) <= keepLast {
		return nil // Nothing to delete
	}

	// Convert to int slice and sort
	versionInts := make([]int, 0, len(versions))
	for _, v := range versions {
		if vi, err := strconv.Atoi(v); err == nil {
			versionInts = append(versionInts, vi)
		}
	}

	// Sort in descending order (newest first)
	for i := 0; i < len(versionInts)-1; i++ {
		for j := i + 1; j < len(versionInts); j++ {
			if versionInts[i] < versionInts[j] {
				versionInts[i], versionInts[j] = versionInts[j], versionInts[i]
			}
		}
	}

	// Keep only the latest N versions
	versionsToDelete := versionInts[keepLast:]

	pipe := s.client.Pipeline()
	for _, version := range versionsToDelete {
		versionKey := fmt.Sprintf("%s:%d", baseKey, version)
		pipe.Del(ctx, versionKey)
		pipe.SRem(ctx, versionsKey, version)
	}

	_, err = pipe.Exec(ctx)
	if err != nil {
		return fmt.Errorf("failed to delete snapshots: %w", err)
	}

	return nil
}

// GetSnapshotStats returns statistics about snapshots
func (s *RedisSnapshotStore) GetSnapshotStats(ctx context.Context, aggregateID uuid.UUID, aggregateType string) (map[string]interface{}, error) {
	baseKey := fmt.Sprintf("snapshot:%s:%s", aggregateType, aggregateID)
	versionsKey := fmt.Sprintf("%s:versions", baseKey)
	sizeKey := fmt.Sprintf("%s:size", baseKey)

	stats := make(map[string]interface{})

	// Get version count
	versionCount, err := s.client.SCard(ctx, versionsKey).Result()
	if err != nil {
		return nil, fmt.Errorf("failed to get version count: %w", err)
	}
	stats["version_count"] = versionCount

	// Get latest size
	size, err := s.client.Get(ctx, sizeKey).Result()
	if err != nil && err != redis.Nil {
		return nil, fmt.Errorf("failed to get size: %w", err)
	}
	if err == nil {
		if sizeInt, err := strconv.Atoi(size); err == nil {
			stats["latest_size_bytes"] = sizeInt
		}
	}

	// Check if latest snapshot exists
	latestKey := fmt.Sprintf("%s:latest", baseKey)
	exists, err := s.client.Exists(ctx, latestKey).Result()
	if err != nil {
		return nil, fmt.Errorf("failed to check latest snapshot: %w", err)
	}
	stats["has_latest_snapshot"] = exists > 0

	return stats, nil
}

// Close closes the Redis connection
func (s *RedisSnapshotStore) Close() error {
	return s.client.Close()
}