package server

import (
	"context"
	"fmt"
	"log/slog"
	"sync"
	"time"

	"github.com/google/uuid"
	"necpgame/services/quest-engine-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

// Service defines the business logic interface
type Service interface {
	ListQuests(ctx context.Context, params *api.ListQuestsParams) (*api.ListQuestsResponse, error)
	CreateQuest(ctx context.Context, req *api.CreateQuestJSONRequestBody) (*api.QuestResponse, error)
	GetQuest(ctx context.Context, questID openapi_types.UUID) (*api.QuestResponse, error)
	UpdateQuest(ctx context.Context, questID openapi_types.UUID, req *api.UpdateQuestJSONRequestBody) (*api.QuestResponse, error)
	DeleteQuest(ctx context.Context, questID openapi_types.UUID) error
}

// QuestEngineService implements the business logic
type QuestEngineService struct {
	repo     Repository
	metrics  *ServiceMetrics
	mu       sync.RWMutex
	questCache map[uuid.UUID]*api.QuestResponse // Performance: Memory pooling for quest data
}

// QuestData holds quest-specific information
type QuestData struct {
	ActivePlayers   int
	LastActivity    time.Time
	PerformanceData PerformanceMetrics
}

// PerformanceMetrics tracks service performance
type PerformanceMetrics struct {
	CPUUsagePercent    float64
	MemoryUsageMB      int
	QuestDecisionLatency  time.Duration
	ObjectiveSyncLatency time.Duration
}

// ServiceMetrics collects service-wide metrics
type ServiceMetrics struct {
	TotalActiveQuests int64
	AverageLatency    time.Duration
	ErrorRate         float64
}

// NewQuestEngineService creates a new service instance
func NewQuestEngineService(repo Repository) *QuestEngineService {
	return &QuestEngineService{
		repo:        repo,
		metrics:     &ServiceMetrics{},
		questCache:  make(map[uuid.UUID]*api.QuestResponse),
	}
}

// ListQuests implements quest listing business logic
func (s *QuestEngineService) ListQuests(ctx context.Context, params *api.ListQuestsParams) (*api.ListQuestsResponse, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("ListQuests completed", "duration_ms", duration.Milliseconds())
	}()

	// Get quests from repository
	quests, err := s.repo.ListQuests(ctx, params)
	if err != nil {
		return nil, fmt.Errorf("failed to list quests: %w", err)
	}

	// Convert to API response format
	var questSummaries []api.QuestSummary
	for _, quest := range quests {
		summary := api.QuestSummary{
			Id:          quest.Id,
			Title:       quest.Title,
			Description: quest.Description,
			Status:      quest.Status,
			Difficulty:  quest.Difficulty,
			Rewards:     quest.Rewards,
			CreatedAt:   quest.CreatedAt,
			UpdatedAt:   quest.UpdatedAt,
		}
		questSummaries = append(questSummaries, summary)
	}

	total := len(questSummaries)
	resp := &api.ListQuestsResponse{
		JSON200: &struct {
			Quests *[]api.QuestSummary `json:"quests,omitempty"`
			Total  *int                `json:"total,omitempty"`
		}{
			Quests: &questSummaries,
			Total:  &total,
		},
	}

	slog.Info("Quests listed successfully", "count", len(questSummaries))
	return resp, nil
}

// CreateQuest implements quest creation business logic
func (s *QuestEngineService) CreateQuest(ctx context.Context, req *api.CreateQuestJSONRequestBody) (*api.QuestResponse, error) {
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

// GetQuest implements quest retrieval business logic
func (s *QuestEngineService) GetQuest(ctx context.Context, questID openapi_types.UUID) (*api.QuestResponse, error) {
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

// UpdateQuest implements quest update business logic
func (s *QuestEngineService) UpdateQuest(ctx context.Context, questID openapi_types.UUID, req *api.UpdateQuestJSONRequestBody) (*api.QuestResponse, error) {
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

// DeleteQuest implements quest deletion business logic
func (s *QuestEngineService) DeleteQuest(ctx context.Context, questID openapi_types.UUID) error {
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

