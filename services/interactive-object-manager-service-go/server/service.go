package server

import (
	"context"
	"fmt"
	"log/slog"
	"sync"
	"time"

	"github.com/google/uuid"
	"necpgame/services/interactive-object-manager-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

// Service defines the business logic interface
type Service interface {
	ListInteractiveObjects(ctx context.Context, params *api.ListInteractiveObjectsParams) (*api.ListInteractiveObjectsResponse, error)
	CreateInteractiveObject(ctx context.Context, req *api.CreateInteractiveObjectJSONRequestBody) (map[string]interface{}, error)
	GetInteractiveObject(ctx context.Context, objectID openapi_types.UUID) (map[string]interface{}, error)
	UpdateInteractiveObject(ctx context.Context, objectID openapi_types.UUID, req *api.UpdateInteractiveObjectJSONRequestBody) (map[string]interface{}, error)
	DeleteInteractiveObject(ctx context.Context, objectID openapi_types.UUID) error
}

// InteractiveObjectManagerService implements the business logic
type InteractiveObjectManagerService struct {
	repo     Repository
	metrics  *ServiceMetrics
	mu       sync.RWMutex
	objectCache map[uuid.UUID]*api.InteractiveObjectResponse // Performance: Memory pooling for object data
}

// ObjectData holds object-specific information
type ObjectData struct {
	ActiveInteractions int
	LastActivity       time.Time
	PerformanceData    PerformanceMetrics
}

// PerformanceMetrics tracks service performance
type PerformanceMetrics struct {
	CPUUsagePercent        float64
	MemoryUsageMB          int
	InteractionLatency     time.Duration
	StateSyncLatency       time.Duration
}

// ServiceMetrics collects service-wide metrics
type ServiceMetrics struct {
	TotalActiveObjects int64
	AverageLatency     time.Duration
	ErrorRate          float64
}

// NewInteractiveObjectManagerService creates a new service instance
func NewInteractiveObjectManagerService(repo Repository) *InteractiveObjectManagerService {
	return &InteractiveObjectManagerService{
		repo:        repo,
		metrics:     &ServiceMetrics{},
		objectCache: make(map[uuid.UUID]*api.InteractiveObjectResponse),
	}
}

// ListInteractiveObjects implements object listing business logic
func (s *InteractiveObjectManagerService) ListInteractiveObjects(ctx context.Context, params *api.ListInteractiveObjectsParams) (*api.ListInteractiveObjectsResponse, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("ListInteractiveObjects completed", "duration_ms", duration.Milliseconds())
	}()

	// Get objects from repository
	objects, err := s.repo.ListInteractiveObjects(ctx, params)
	if err != nil {
		return nil, fmt.Errorf("failed to list objects: %w", err)
	}

	// Convert to API response format
	var objectMaps []map[string]interface{}
	for _, obj := range objects {
		objMap := map[string]interface{}{
			"id":          obj.Id,
			"object_type": obj.ObjectType,
			"zone_id":     obj.ZoneId,
			"status":      obj.Status,
			"created_at":  obj.CreatedAt,
			"updated_at":  obj.UpdatedAt,
		}
		objectMaps = append(objectMaps, objMap)
	}

	total := len(objectMaps)
	resp := &api.ListInteractiveObjectsResponse{
		JSON200: &struct {
			Objects *[]map[string]interface{} `json:"objects,omitempty"`
			Total   *int                      `json:"total,omitempty"`
		}{
			Objects: &objectMaps,
			Total:   &total,
		},
	}

	slog.Info("Objects listed successfully", "count", len(objectSummaries))
	return resp, nil
}

// CreateInteractiveObject implements object creation business logic
func (s *InteractiveObjectManagerService) CreateInteractiveObject(ctx context.Context, req *api.CreateInteractiveObjectJSONRequestBody) (*api.InteractiveObjectResponse, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("CreateQuest completed", "duration_ms", duration.Milliseconds())
	}()

	// Generate quest ID
	questID := uuid.New()

	// Create quest entity
	now := time.Now().UTC()
	status := "active"
	quest := &api.QuestResponse{
		Id:          &questID,
		Title:       &req.Title,
		Description: &req.Description,
		Status:      &status,
		Objectives:  req.Objectives,
		Rewards:     req.Rewards,
		CreatedAt:   &now,
		UpdatedAt:   &now,
	}

	// Save to repository
	if err := s.repo.CreateQuest(ctx, quest); err != nil {
		return nil, fmt.Errorf("failed to create quest: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.questCache[questID] = quest
	s.mu.Unlock()

	slog.Info("Quest created successfully", "quest_id", questID, "title", req.Title)
	return quest, nil
}

// GetInteractiveObject implements object retrieval business logic
func (s *InteractiveObjectManagerService) GetInteractiveObject(ctx context.Context, objectID openapi_types.UUID) (*api.InteractiveObjectResponse, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("GetQuest completed", "duration_ms", duration.Milliseconds())
	}()

	uuid := uuid.UUID(questID)

	// Check cache first
	s.mu.RLock()
	if quest, exists := s.questCache[uuid]; exists {
		s.mu.RUnlock()
		return quest, nil
	}
	s.mu.RUnlock()

	// Get from repository
	quest, err := s.repo.GetQuest(ctx, uuid)
	if err != nil {
		return nil, fmt.Errorf("failed to get quest: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.questCache[uuid] = quest
	s.mu.Unlock()

	slog.Info("Quest retrieved successfully", "quest_id", questID)
	return quest, nil
}

// UpdateInteractiveObject implements object update business logic
func (s *InteractiveObjectManagerService) UpdateInteractiveObject(ctx context.Context, objectID openapi_types.UUID, req *api.UpdateInteractiveObjectJSONRequestBody) (*api.InteractiveObjectResponse, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("UpdateQuest completed", "duration_ms", duration.Milliseconds())
	}()

	// Get existing quest
	quest, err := s.GetQuest(ctx, questID)
	if err != nil {
		return nil, err
	}

	// Apply updates
	if req.Title != nil {
		quest.Title = req.Title
	}
	if req.Description != nil {
		quest.Description = req.Description
	}
	if req.Status != nil {
		quest.Status = req.Status
	}
	now := time.Now().UTC()
	quest.UpdatedAt = &now

	// Save to repository
	if err := s.repo.UpdateQuest(ctx, quest); err != nil {
		return nil, fmt.Errorf("failed to update quest: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.questCache[questID] = quest
	s.mu.Unlock()

	slog.Info("Quest updated successfully", "quest_id", questID)
	return quest, nil
}

// DeleteInteractiveObject implements object deletion business logic
func (s *InteractiveObjectManagerService) DeleteInteractiveObject(ctx context.Context, objectID openapi_types.UUID) error {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("DeleteQuest completed", "duration_ms", duration.Milliseconds())
	}()

	// Delete from repository
	if err := s.repo.DeleteQuest(ctx, questID); err != nil {
		return fmt.Errorf("failed to delete quest: %w", err)
	}

	// Remove from cache
	s.mu.Lock()
	delete(s.questCache, questID)
	s.mu.Unlock()

	slog.Info("Quest deleted successfully", "quest_id", questID)
	return nil
}

