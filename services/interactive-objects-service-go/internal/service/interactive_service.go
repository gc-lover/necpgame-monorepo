package service

import (
	"context"
	"fmt"
	"sync"
	"sync/atomic"
	"time"

	"necpgame/services/interactive-objects-service-go/internal/repository"
)

// InteractiveService handles interactive objects business logic
type InteractiveService struct {
	repo        *repository.Repository
	memoryPool  *MemoryPool
	atomicStats *AtomicStatistics
}

// MemoryPool provides zero-allocation object reuse
type MemoryPool struct {
	objectPool *sync.Pool
}

// AtomicStatistics provides lock-free metrics collection
type AtomicStatistics struct {
	activeObjects         int64
	interactionsProcessed int64
	rewardsDistributed    int64
	objectsDestroyed      int64
}

// NewInteractiveService creates a new interactive service instance
func NewInteractiveService(repo *repository.Repository) *InteractiveService {
	return &InteractiveService{
		repo: repo,
		memoryPool: &MemoryPool{
			objectPool: &sync.Pool{
				New: func() interface{} {
					return &repository.InteractiveObject{}
				},
			},
		},
		atomicStats: &AtomicStatistics{},
	}
}

// SpawnObject creates a new interactive object
func (s *InteractiveService) SpawnObject(ctx context.Context, objectType, zoneType, zoneID string, position repository.Position) (*repository.InteractiveObject, error) {
	obj := s.memoryPool.objectPool.Get().(*repository.InteractiveObject)
	defer s.memoryPool.objectPool.Put(obj)

	obj.ID = generateObjectID()
	obj.ObjectType = objectType
	obj.Position = position
	obj.ZoneType = zoneType
	obj.ZoneID = zoneID
	obj.Status = "active"
	obj.CreatedAt = time.Now()

	// Initialize object-specific data
	obj.Data = s.initializeObjectData(objectType)

	// Validate object type
	if err := s.validateObjectType(objectType); err != nil {
		return nil, fmt.Errorf("invalid object type: %w", err)
	}

	// Save to database
	if err := s.repo.SaveObject(ctx, obj); err != nil {
		return nil, fmt.Errorf("failed to save object: %w", err)
	}

	atomic.AddInt64(&s.atomicStats.activeObjects, 1)

	return obj, nil
}

// GetObject retrieves object information
func (s *InteractiveService) GetObject(ctx context.Context, objectID string) (*repository.InteractiveObject, error) {
	obj, err := s.repo.GetObject(ctx, objectID)
	if err != nil {
		return nil, fmt.Errorf("failed to get object: %w", err)
	}

	return obj, nil
}

// GetActiveObjects retrieves all active objects in a zone
func (s *InteractiveService) GetActiveObjects(ctx context.Context, zoneID string) ([]*repository.InteractiveObject, error) {
	objects, err := s.repo.GetActiveObjects(ctx, zoneID)
	if err != nil {
		return nil, fmt.Errorf("failed to get active objects: %w", err)
	}

	return objects, nil
}

// InteractWithObject handles player interaction with an object
func (s *InteractiveService) InteractWithObject(ctx context.Context, objectID, interactionType string) (*InteractionResult, error) {
	obj, err := s.repo.GetObject(ctx, objectID)
	if err != nil {
		return nil, fmt.Errorf("failed to get object for interaction: %w", err)
	}

	if obj.Status != "active" {
		return &InteractionResult{
			InteractionType: interactionType,
			Success:         false,
			RewardType:      "",
			RewardAmount:    0,
			NewStatus:       obj.Status,
		}, nil
	}

	// Process interaction based on type and object
	result := s.processInteraction(obj, interactionType)

	// Update object status if needed
	if result.NewStatus != obj.Status {
		if err := s.repo.UpdateObjectStatus(ctx, objectID, result.NewStatus); err != nil {
			return nil, fmt.Errorf("failed to update object status: %w", err)
		}

		if result.NewStatus == "destroyed" {
			atomic.AddInt64(&s.atomicStats.activeObjects, -1)
			atomic.AddInt64(&s.atomicStats.objectsDestroyed, 1)
		}
	}

	atomic.AddInt64(&s.atomicStats.interactionsProcessed, 1)
	if result.Success && result.RewardAmount > 0 {
		atomic.AddInt64(&s.atomicStats.rewardsDistributed, 1)
	}

	return result, nil
}

// RemoveObject removes an object from the system
func (s *InteractiveService) RemoveObject(ctx context.Context, objectID string) error {
	obj, err := s.repo.GetObject(ctx, objectID)
	if err != nil {
		return fmt.Errorf("failed to get object for removal: %w", err)
	}

	if obj.Status == "active" {
		atomic.AddInt64(&s.atomicStats.activeObjects, -1)
	}

	return s.repo.UpdateObjectStatus(ctx, objectID, "removed")
}

// GetTelemetry returns current service metrics
func (s *InteractiveService) GetTelemetry() *ServiceTelemetry {
	return &ServiceTelemetry{
		ActiveObjects:         atomic.LoadInt64(&s.atomicStats.activeObjects),
		InteractionsProcessed: atomic.LoadInt64(&s.atomicStats.interactionsProcessed),
		RewardsDistributed:    atomic.LoadInt64(&s.atomicStats.rewardsDistributed),
		ObjectsDestroyed:      atomic.LoadInt64(&s.atomicStats.objectsDestroyed),
	}
}

// InteractionResult represents the outcome of an interaction
type InteractionResult struct {
	InteractionType string `json:"interaction_type"`
	Success         bool   `json:"success"`
	RewardType      string `json:"reward_type"`
	RewardAmount    int    `json:"reward_amount"`
	NewStatus       string `json:"new_status"`
}

// ServiceTelemetry contains service performance metrics
type ServiceTelemetry struct {
	ActiveObjects         int64 `json:"active_objects"`
	InteractionsProcessed int64 `json:"interactions_processed"`
	RewardsDistributed    int64 `json:"rewards_distributed"`
	ObjectsDestroyed      int64 `json:"objects_destroyed"`
}

// Private methods

func (s *InteractiveService) validateObjectType(objectType string) error {
	validTypes := []string{
		"terminal",
		"security_scanner",
		"ammo_depot",
		"medical_station",
		"data_node",
		"black_market",
		"security_door",
		"elevator",
		"cargo_container",
		"drone_station",
	}

	for _, validType := range validTypes {
		if objectType == validType {
			return nil
		}
	}

	return fmt.Errorf("unsupported object type: %s", objectType)
}

func (s *InteractiveService) initializeObjectData(objectType string) repository.ObjectData {
	data := repository.ObjectData{}

	switch objectType {
	case "terminal":
		charges := 10
		security := 2
		data.Charges = &charges
		data.SecurityLevel = &security
	case "security_scanner":
		security := 3
		data.SecurityLevel = &security
	case "ammo_depot":
		reward := 500
		data.RewardPool = &reward
	case "medical_station":
		charges := 5
		data.Charges = &charges
	case "data_node":
		reward := 100
		data.RewardPool = &reward
	case "black_market":
		reward := 1000
		data.RewardPool = &reward
	case "security_door":
		security := 4
		data.SecurityLevel = &security
	case "cargo_container":
		reward := 200
		data.RewardPool = &reward
	}

	return data
}

func (s *InteractiveService) processInteraction(obj *repository.InteractiveObject, interactionType string) *InteractionResult {
	result := &InteractionResult{
		InteractionType: interactionType,
		Success:         true,
		NewStatus:       obj.Status,
	}

	switch interactionType {
	case "hack":
		result = s.processHackInteraction(obj)
	case "loot":
		result = s.processLootInteraction(obj)
	case "use":
		result = s.processUseInteraction(obj)
	case "bypass":
		result = s.processBypassInteraction(obj)
	default:
		result.Success = false
	}

	return result
}

func (s *InteractiveService) processHackInteraction(obj *repository.InteractiveObject) *InteractionResult {
	result := &InteractionResult{
		InteractionType: "hack",
		Success:         true,
		RewardType:      "data",
		NewStatus:       obj.Status,
	}

	// Terminal hacking
	if obj.ObjectType == "terminal" && obj.Data.Charges != nil && *obj.Data.Charges > 0 {
		result.RewardAmount = 50
		*obj.Data.Charges--
		if *obj.Data.Charges <= 0 {
			result.NewStatus = "depleted"
		}
	} else if obj.ObjectType == "data_node" {
		result.RewardAmount = 100
		result.NewStatus = "destroyed"
	} else {
		result.Success = false
	}

	return result
}

func (s *InteractiveService) processLootInteraction(obj *repository.InteractiveObject) *InteractionResult {
	result := &InteractionResult{
		InteractionType: "loot",
		Success:         true,
		RewardType:      "credits",
		NewStatus:       "destroyed",
	}

	if obj.Data.RewardPool != nil {
		result.RewardAmount = *obj.Data.RewardPool
	} else {
		result.RewardAmount = 100 // Default loot
	}

	return result
}

func (s *InteractiveService) processUseInteraction(obj *repository.InteractiveObject) *InteractionResult {
	result := &InteractionResult{
		InteractionType: "use",
		Success:         true,
		RewardType:      "health",
		NewStatus:       obj.Status,
	}

	if obj.ObjectType == "medical_station" && obj.Data.Charges != nil && *obj.Data.Charges > 0 {
		result.RewardAmount = 50
		*obj.Data.Charges--
		if *obj.Data.Charges <= 0 {
			result.NewStatus = "depleted"
		}
	} else {
		result.Success = false
	}

	return result
}

func (s *InteractiveService) processBypassInteraction(obj *repository.InteractiveObject) *InteractionResult {
	result := &InteractionResult{
		InteractionType: "bypass",
		Success:         true,
		RewardType:      "access",
		RewardAmount:    1,
		NewStatus:       obj.Status,
	}

	if obj.ObjectType == "security_door" {
		// Bypass security door - stays active for reuse
	} else {
		result.Success = false
	}

	return result
}

func generateObjectID() string {
	return fmt.Sprintf("obj_%d", time.Now().UnixNano())
}

// Issue: #1861
