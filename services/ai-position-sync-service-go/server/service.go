package server

import (
	"context"
	"fmt"
	"log/slog"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"

	"necpgame/services/ai-position-sync-service-go/pkg/api"
)

type Service interface {
	UpdatePosition(ctx context.Context, req api.UpdatePositionRequest) (*api.PositionUpdateResponse, error)
	GetPosition(ctx context.Context, req api.GetPositionRequest) (*api.PositionResponse, error)
	BatchUpdatePositions(ctx context.Context, req api.BatchUpdatePositionsRequest) (*api.BatchUpdateResponse, error)
	GetZonePositions(ctx context.Context, req api.GetZonePositionsRequest) (*api.ZonePositionsResponse, error)
	PredictMovement(ctx context.Context, req api.PredictMovementRequest) (*api.MovementPredictionResponse, error)
}

type AiPositionSyncService struct {
	repo        Repository
	redis       *redis.Client
	metrics     *ServiceMetrics
	mu          sync.RWMutex
	positionCache map[string]*PositionData // Performance: Memory pooling for position cache
}

type PositionData struct {
	EntityID    uuid.UUID
	Position    api.Position
	Velocity    api.Velocity
	ZoneID      uuid.UUID
	LastUpdate  time.Time
	TTL         time.Duration
}

func NewAiPositionSyncService(repo Repository, redisClient *redis.Client) *AiPositionSyncService {
	return &AiPositionSyncService{
		repo:          repo,
		redis:         redisClient,
		metrics:       NewServiceMetrics(),
		positionCache: make(map[string]*PositionData),
	}
}

func (s *AiPositionSyncService) UpdatePosition(ctx context.Context, req api.UpdatePositionRequest) (*api.PositionUpdateResponse, error) {
	start := time.Now()
	defer func() {
		s.metrics.RecordLatency("update_position", time.Since(start))
	}()

	entityID := req.EntityId.String()
	zoneID := req.ZoneId.String()

	// Performance: Update Redis for real-time sync (<25ms P99 target)
	redisKey := fmt.Sprintf("position:%s", entityID)
	positionData := map[string]interface{}{
		"x":          req.Position.X,
		"y":          req.Position.Y,
		"z":          req.Position.Z,
		"zone_id":    zoneID,
		"timestamp":  req.Timestamp.Unix(),
		"velocity_x": req.Velocity.X,
		"velocity_y": req.Velocity.Y,
		"velocity_z": req.Velocity.Z,
	}

	if err := s.redis.HSet(ctx, redisKey, positionData).Err(); err != nil {
		s.metrics.IncrementErrors("redis_update")
		slog.Error("Failed to update position in Redis", "entity_id", entityID, "error", err)
		return nil, fmt.Errorf("failed to update position: %w", err)
	}

	// Set TTL for cleanup (5 minutes)
	if err := s.redis.Expire(ctx, redisKey, 5*time.Minute).Err(); err != nil {
		slog.Warn("Failed to set TTL on position key", "entity_id", entityID, "error", err)
	}

	// Performance: Async persistence to PostgreSQL
	go func() {
		ctx := context.Background()
		if err := s.repo.SavePositionUpdate(ctx, req); err != nil {
			s.metrics.IncrementErrors("db_persist")
			slog.Error("Failed to persist position update", "entity_id", entityID, "error", err)
		}
	}()

	// Update memory cache for faster subsequent queries
	s.mu.Lock()
	s.positionCache[entityID] = &PositionData{
		EntityID:   req.EntityId,
		Position:   req.Position,
		Velocity:   req.Velocity,
		ZoneID:     req.ZoneId,
		LastUpdate: req.Timestamp,
		TTL:        5 * time.Minute,
	}
	s.mu.Unlock()

	s.metrics.IncrementRequests("update_position")
	return &api.PositionUpdateResponse{
		Success:   true,
		Timestamp: req.Timestamp,
	}, nil
}

func (s *AiPositionSyncService) GetPosition(ctx context.Context, req api.GetPositionRequest) (*api.PositionResponse, error) {
	start := time.Now()
	defer func() {
		s.metrics.RecordLatency("get_position", time.Since(start))
	}()

	entityID := req.EntityId.String()

	// Performance: Check memory cache first (<10ms target)
	s.mu.RLock()
	if cached, exists := s.positionCache[entityID]; exists && time.Since(cached.LastUpdate) < cached.TTL {
		s.mu.RUnlock()
		s.metrics.IncrementCacheHits("position")
		return &api.PositionResponse{
			EntityId:   cached.EntityID,
			Position:   cached.Position,
			Velocity:   cached.Velocity,
			ZoneId:     cached.ZoneID,
			Timestamp:  cached.LastUpdate,
			IsRealtime: true,
		}, nil
	}
	s.mu.RUnlock()

	// Performance: Check Redis for real-time data
	redisKey := fmt.Sprintf("position:%s", entityID)
	vals, err := s.redis.HGetAll(ctx, redisKey).Result()
	if err != nil {
		s.metrics.IncrementErrors("redis_get")
		slog.Error("Failed to get position from Redis", "entity_id", entityID, "error", err)
	} else if len(vals) > 0 {
		// Parse Redis data
		position := api.Position{
			X: parseFloat64(vals["x"]),
			Y: parseFloat64(vals["y"]),
			Z: parseFloat64(vals["z"]),
		}
		velocity := api.Velocity{
			X: parseFloat64(vals["velocity_x"]),
			Y: parseFloat64(vals["velocity_y"]),
			Z: parseFloat64(vals["velocity_z"]),
		}
		zoneID, _ := uuid.Parse(vals["zone_id"])
		timestamp := time.Unix(parseInt64(vals["timestamp"]), 0)

		// Update cache
		s.mu.Lock()
		s.positionCache[entityID] = &PositionData{
			EntityID:   req.EntityId,
			Position:   position,
			Velocity:   velocity,
			ZoneID:     zoneID,
			LastUpdate: timestamp,
			TTL:        5 * time.Minute,
		}
		s.mu.Unlock()

		s.metrics.IncrementRequests("get_position")
		return &api.PositionResponse{
			EntityId:   req.EntityId,
			Position:   position,
			Velocity:   velocity,
			ZoneId:     zoneID,
			Timestamp:  timestamp,
			IsRealtime: true,
		}, nil
	}

	// Fallback to database
	position, err := s.repo.GetLatestPosition(ctx, req.EntityId)
	if err != nil {
		s.metrics.IncrementErrors("db_get")
		return nil, fmt.Errorf("failed to get position: %w", err)
	}

	s.metrics.IncrementRequests("get_position")
	return &api.PositionResponse{
		EntityId:   req.EntityId,
		Position:   position.Position,
		Velocity:   position.Velocity,
		ZoneId:     position.ZoneID,
		Timestamp:  position.Timestamp,
		IsRealtime: false,
	}, nil
}

func (s *AiPositionSyncService) BatchUpdatePositions(ctx context.Context, req api.BatchUpdatePositionsRequest) (*api.BatchUpdateResponse, error) {
	start := time.Now()
	defer func() {
		s.metrics.RecordLatency("batch_update_positions", time.Since(start))
	}()

	// Performance: Use Redis pipeline for batch updates
	pipe := s.redis.Pipeline()
	results := make([]*api.PositionUpdateResponse, len(req.Updates))

	for i, update := range req.Updates {
		entityID := update.EntityId.String()
		redisKey := fmt.Sprintf("position:%s", entityID)

		positionData := map[string]interface{}{
			"x":          update.Position.X,
			"y":          update.Position.Y,
			"z":          update.Position.Z,
			"zone_id":    update.ZoneId.String(),
			"timestamp":  update.Timestamp.Unix(),
			"velocity_x": update.Velocity.X,
			"velocity_y": update.Velocity.Y,
			"velocity_z": update.Velocity.Z,
		}

		pipe.HSet(ctx, redisKey, positionData)
		pipe.Expire(ctx, redisKey, 5*time.Minute)

		results[i] = &api.PositionUpdateResponse{
			Success:   true,
			Timestamp: update.Timestamp,
		}
	}

	_, err := pipe.Exec(ctx)
	if err != nil {
		s.metrics.IncrementErrors("redis_batch_update")
		return nil, fmt.Errorf("failed to batch update positions: %w", err)
	}

	// Async persistence
	go func() {
		ctx := context.Background()
		if err := s.repo.BatchSavePositionUpdates(ctx, req.Updates); err != nil {
			s.metrics.IncrementErrors("db_batch_persist")
			slog.Error("Failed to batch persist position updates", "error", err)
		}
	}()

	s.metrics.IncrementRequests("batch_update_positions")
	return &api.BatchUpdateResponse{
		Results: results,
		SuccessCount: len(results),
	}, nil
}

func (s *AiPositionSyncService) GetZonePositions(ctx context.Context, req api.GetZonePositionsRequest) (*api.ZonePositionsResponse, error) {
	start := time.Now()
	defer func() {
		s.metrics.RecordLatency("get_zone_positions", time.Since(start))
	}()

	// Get all entity IDs in zone from Redis
	pattern := fmt.Sprintf("position:*")
	keys, err := s.redis.Keys(ctx, pattern).Result()
	if err != nil {
		s.metrics.IncrementErrors("redis_keys")
		return nil, fmt.Errorf("failed to get zone positions: %w", err)
	}

	positions := make([]api.PositionResponse, 0, len(keys))
	for _, key := range keys {
		vals, err := s.redis.HGetAll(ctx, key).Result()
		if err != nil || len(vals) == 0 {
			continue
		}

		// Check if entity is in requested zone
		if vals["zone_id"] != req.ZoneId.String() {
			continue
		}

		entityID, _ := uuid.Parse(key[9:]) // Remove "position:" prefix
		position := api.Position{
			X: parseFloat64(vals["x"]),
			Y: parseFloat64(vals["y"]),
			Z: parseFloat64(vals["z"]),
		}
		velocity := api.Velocity{
			X: parseFloat64(vals["velocity_x"]),
			Y: parseFloat64(vals["velocity_y"]),
			Z: parseFloat64(vals["velocity_z"]),
		}
		timestamp := time.Unix(parseInt64(vals["timestamp"]), 0)

		positions = append(positions, api.PositionResponse{
			EntityId:   entityID,
			Position:   position,
			Velocity:   velocity,
			ZoneId:     req.ZoneId,
			Timestamp:  timestamp,
			IsRealtime: true,
		})
	}

	s.metrics.IncrementRequests("get_zone_positions")
	return &api.ZonePositionsResponse{
		ZoneId:    req.ZoneId,
		Positions: positions,
		Count:     int64(len(positions)),
	}, nil
}

func (s *AiPositionSyncService) PredictMovement(ctx context.Context, req api.PredictMovementRequest) (*api.MovementPredictionResponse, error) {
	start := time.Now()
	defer func() {
		s.metrics.RecordLatency("predict_movement", time.Since(start))
	}()

	// Get current position and velocity
	current, err := s.GetPosition(ctx, api.GetPositionRequest{EntityId: req.EntityId})
	if err != nil {
		return nil, fmt.Errorf("failed to get current position: %w", err)
	}

	// Simple linear prediction
	predictionTime := req.PredictionTimeSeconds
	predictedPosition := api.Position{
		X: current.Position.X + current.Velocity.X*predictionTime,
		Y: current.Position.Y + current.Velocity.Y*predictionTime,
		Z: current.Position.Z + current.Velocity.Z*predictionTime,
	}

	s.metrics.IncrementRequests("predict_movement")
	return &api.MovementPredictionResponse{
		EntityId:          req.EntityId,
		CurrentPosition:   current.Position,
		PredictedPosition: predictedPosition,
		PredictionTime:    predictionTime,
		Confidence:        0.8, // Basic prediction confidence
	}, nil
}

// Helper functions
func parseFloat64(s string) float64 {
	if s == "" {
		return 0
	}
	var result float64
	fmt.Sscanf(s, "%f", &result)
	return result
}

func parseInt64(s string) int64 {
	if s == "" {
		return 0
	}
	var result int64
	fmt.Sscanf(s, "%d", &result)
	return result
}