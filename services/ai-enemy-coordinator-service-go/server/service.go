package server

import (
	"context"
	"fmt"
	"log/slog"
	"sync"
	"time"

	"github.com/google/uuid"
	"necpgame/services/ai-enemy-coordinator-service-go/pkg/api"
)

// Service defines the business logic interface
type Service interface {
	SpawnAiEnemy(ctx context.Context, req api.AiSpawnRequest) (*api.AiCoordinationResponse, error)
	// TODO: Add other business methods
}

// AiEnemyCoordinatorService implements the business logic
type AiEnemyCoordinatorService struct {
	repo     Repository
	metrics  *ServiceMetrics
	mu       sync.RWMutex
	zoneData map[string]*ZoneData // Performance: Memory pooling for zone data
}

// ZoneData holds zone-specific information
type ZoneData struct {
	ActiveEnemies   int
	LastActivity    time.Time
	PerformanceData PerformanceMetrics
}

// PerformanceMetrics tracks service performance
type PerformanceMetrics struct {
	CPUUsagePercent    float64
	MemoryUsageMB      int
	AIDecisionLatency  time.Duration
	NetworkSyncLatency time.Duration
}

// ServiceMetrics collects service-wide metrics
type ServiceMetrics struct {
	TotalActiveEnemies int64
	AverageLatency     time.Duration
	ErrorRate          float64
}

// NewAiEnemyCoordinatorService creates a new service instance
func NewAiEnemyCoordinatorService(repo Repository) *AiEnemyCoordinatorService {
	return &AiEnemyCoordinatorService{
		repo:     repo,
		metrics:  &ServiceMetrics{},
		zoneData: make(map[string]*ZoneData),
	}
}

// SpawnAiEnemy implements AI enemy spawning business logic
func (s *AiEnemyCoordinatorService) SpawnAiEnemy(ctx context.Context, req api.AiSpawnRequest) (*api.AiCoordinationResponse, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("SpawnAiEnemy completed", "duration_ms", duration.Milliseconds())
	}()

	// Validate request
	if err := s.validateSpawnRequest(req); err != nil {
		return nil, fmt.Errorf("invalid spawn request: %w", err)
	}

	// Check zone capacity (Performance: Prevent overloading)
	zoneIDStr := req.ZoneID.String()
	s.mu.RLock()
	zoneData, exists := s.zoneData[zoneIDStr]
	s.mu.RUnlock()

	if exists && zoneData.ActiveEnemies >= 500 { // MMOFPS: 500 AI entities per zone max
		return nil, fmt.Errorf("zone at maximum AI capacity")
	}

	// Generate enemy ID (in production, use UUID v7 for better performance)
	enemyID := uuid.New()

	// Prepare spawn response
	now := time.Now().UTC()

	resp := &api.AiCoordinationResponse{
		EnemyID:        enemyID,
		Status:         api.AiCoordinationResponseStatusSpawned,
		SpawnTimestamp: now,
		ZoneMetrics: &api.AiCoordinationResponseZoneMetrics{
			ActiveEnemies: func() *int {
				count := zoneData.ActiveEnemies + 1
				return &count
			}(),
			PerformanceMetrics: &api.ZonePerformanceMetrics{
				CpuUsagePercent: func() *float64 {
					if exists {
						return &zoneData.PerformanceData.CPUUsagePercent
					}
					defaultCPU := 25.0
					return &defaultCPU
				}(),
				MemoryUsageMb: func() *int {
					if exists {
						return &zoneData.PerformanceData.MemoryUsageMB
					}
					defaultMem := 128
					return &defaultMem
				}(),
				AiDecisionLatencyMs: func() *float64 {
					if exists {
						ms := float64(zoneData.PerformanceData.AIDecisionLatency.Milliseconds())
						return &ms
					}
					defaultLatency := 15.0
					return &defaultLatency
				}(),
			},
		},
	}

	// Update zone data (Performance: Thread-safe updates)
	s.mu.Lock()
	if !exists {
		s.zoneData[*req.ZoneID] = &ZoneData{
			ActiveEnemies: 1,
			LastActivity:  now,
			PerformanceData: PerformanceMetrics{
				CPUUsagePercent:    25.0,
				MemoryUsageMB:      128,
				AIDecisionLatency:  15 * time.Millisecond,
				NetworkSyncLatency: 25 * time.Millisecond,
			},
		}
	} else {
		zoneData.ActiveEnemies++
		zoneData.LastActivity = now
	}
	s.mu.Unlock()

	// TODO: Persist to database via repository
	// TODO: Publish spawn event to Kafka/Redis
	// TODO: Update zone coordination state

	slog.Info("AI enemy spawned successfully",
		"enemy_id", enemyID,
		"enemy_type", req.EnemyType,
		"zone_id", req.ZoneID,
		"zone_active_enemies", func() int {
			if exists {
				return zoneData.ActiveEnemies
			}
			return 0
		}(),
	)

	return resp, nil
}

// validateSpawnRequest validates the spawn request
func (s *AiEnemyCoordinatorService) validateSpawnRequest(req api.AiSpawnRequest) error {
	// ZoneID is required UUID
	if req.ZoneID == uuid.Nil {
		return fmt.Errorf("zone_id is required")
	}

	// EnemyType is validated by enum
	// SpawnPosition is required by schema

	// TODO: Add more validation (position bounds, zone existence, etc.)

	return nil
}

// GetZoneMetrics returns zone performance metrics
func (s *AiEnemyCoordinatorService) GetZoneMetrics(ctx context.Context, zoneID string) (*ZoneData, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	zoneData, exists := s.zoneData[zoneID]
	if !exists {
		return nil, fmt.Errorf("zone not found: %s", zoneID)
	}

	return zoneData, nil
}

// UpdateZoneMetrics updates zone performance data
func (s *AiEnemyCoordinatorService) UpdateZoneMetrics(zoneID string, metrics PerformanceMetrics) {
	s.mu.Lock()
	defer s.mu.Unlock()

	if zoneData, exists := s.zoneData[zoneID]; exists {
		zoneData.PerformanceData = metrics
		zoneData.LastActivity = time.Now().UTC()
	}
}