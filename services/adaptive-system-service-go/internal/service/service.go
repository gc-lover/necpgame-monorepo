package service

import (
	"context"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/adaptive-system-service-go/config"
	"necpgame/services/adaptive-system-service-go/internal/models"
	"necpgame/services/adaptive-system-service-go/internal/repository"
)

// Service implements adaptive system business logic with machine learning capabilities
type Service struct {
	logger  *zap.Logger
	repo    *repository.Repository
	config  *config.Config
}

// NewService creates a new adaptive system service
func NewService(logger *zap.Logger, repo *repository.Repository, cfg *config.Config) *Service {
	return &Service{
		logger: logger,
		repo:   repo,
		config: cfg,
	}
}

// CreateAdaptationEvent creates and processes a new adaptation event
func (s *Service) CreateAdaptationEvent(ctx context.Context, req *models.CreateAdaptationEventRequest) (*models.AdaptationEventResponse, error) {
	// Create context with timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	event := &models.AdaptationEvent{
		ID:        uuid.New(),
		PlayerID:  req.PlayerID,
		EventType: req.EventType,
		Data:      req.Data,
		Timestamp: time.Now(),
		Processed: false,
	}

	createdEvent, err := s.repo.CreateAdaptationEvent(dbCtx, event)
	if err != nil {
		s.logger.Error("Failed to create adaptation event", zap.Error(err))
		return nil, fmt.Errorf("failed to create adaptation event: %w", err)
	}

	// Process the event asynchronously (don't block the response)
	go s.processAdaptationEvent(createdEvent)

	response := &models.AdaptationEventResponse{
		ID:        createdEvent.ID,
		PlayerID:  createdEvent.PlayerID,
		EventType: createdEvent.EventType,
		Data:      createdEvent.Data,
		Timestamp: createdEvent.Timestamp,
		Processed: createdEvent.Processed,
	}

	return response, nil
}

// GetAdaptationEvent retrieves an adaptation event
func (s *Service) GetAdaptationEvent(ctx context.Context, eventID uuid.UUID) (*models.AdaptationEventResponse, error) {
	// Create context with timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	event, err := s.repo.GetAdaptationEvent(dbCtx, eventID)
	if err != nil {
		s.logger.Error("Failed to get adaptation event", zap.String("event_id", eventID.String()), zap.Error(err))
		return nil, fmt.Errorf("failed to get adaptation event: %w", err)
	}

	if event == nil {
		return nil, nil // Event not found
	}

	response := &models.AdaptationEventResponse{
		ID:        event.ID,
		PlayerID:  event.PlayerID,
		EventType: event.EventType,
		Data:      event.Data,
		Timestamp: event.Timestamp,
		Processed: event.Processed,
	}

	return response, nil
}

// ListAdaptationEvents retrieves paginated list of adaptation events
func (s *Service) ListAdaptationEvents(ctx context.Context, filter *models.AdaptationEventFilter, page, limit int) (*models.AdaptationEventListResponse, error) {
	// Create context with timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	events, total, err := s.repo.ListAdaptationEvents(dbCtx, filter, page, limit)
	if err != nil {
		s.logger.Error("Failed to list adaptation events", zap.Error(err))
		return nil, fmt.Errorf("failed to list adaptation events: %w", err)
	}

	// Convert to response format
	eventResponses := make([]models.AdaptationEventResponse, len(events))
	for i, event := range events {
		eventResponses[i] = models.AdaptationEventResponse{
			ID:        event.ID,
			PlayerID:  event.PlayerID,
			EventType: event.EventType,
			Data:      event.Data,
			Timestamp: event.Timestamp,
			Processed: event.Processed,
		}
	}

	totalPages := (total + limit - 1) / limit

	response := &models.AdaptationEventListResponse{
		Events: eventResponses,
		Pagination: struct {
			Page       int `json:"page"`
			Limit      int `json:"limit"`
			Total      int `json:"total"`
			TotalPages int `json:"total_pages"`
		}{
			Page:       page,
			Limit:      limit,
			Total:      total,
			TotalPages: totalPages,
		},
	}

	return response, nil
}

// GetPlayerProfile retrieves player's adaptive profile
func (s *Service) GetPlayerProfile(ctx context.Context, playerID uuid.UUID) (*models.PlayerProfileResponse, error) {
	// Create context with timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	profile, err := s.repo.GetPlayerProfile(dbCtx, playerID)
	if err != nil {
		s.logger.Error("Failed to get player profile", zap.String("player_id", playerID.String()), zap.Error(err))
		return nil, fmt.Errorf("failed to get player profile: %w", err)
	}

	response := &models.PlayerProfileResponse{
		PlayerID:     profile.PlayerID,
		Difficulty:   profile.Difficulty,
		LearningRate: profile.LearningRate,
		LastUpdated:  profile.LastUpdated,
		EventCount:   profile.EventCount,
	}

	return response, nil
}

// UpdatePlayerProfile updates player's adaptive profile
func (s *Service) UpdatePlayerProfile(ctx context.Context, req *models.UpdatePlayerProfileRequest) (*models.PlayerProfileResponse, error) {
	// Create context with timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// Get current profile
	profile, err := s.repo.GetPlayerProfile(dbCtx, req.PlayerID)
	if err != nil {
		s.logger.Error("Failed to get current player profile", zap.String("player_id", req.PlayerID.String()), zap.Error(err))
		return nil, fmt.Errorf("failed to get current player profile: %w", err)
	}

	// Update fields
	if req.Difficulty != nil {
		profile.Difficulty = *req.Difficulty
	}
	if req.LearningRate != nil {
		profile.LearningRate = *req.LearningRate
	}
	profile.LastUpdated = time.Now()
	profile.EventCount++

	// Save updated profile
	err = s.repo.UpdatePlayerProfile(dbCtx, profile)
	if err != nil {
		s.logger.Error("Failed to update player profile", zap.String("player_id", req.PlayerID.String()), zap.Error(err))
		return nil, fmt.Errorf("failed to update player profile: %w", err)
	}

	response := &models.PlayerProfileResponse{
		PlayerID:     profile.PlayerID,
		Difficulty:   profile.Difficulty,
		LearningRate: profile.LearningRate,
		LastUpdated:  profile.LastUpdated,
		EventCount:   profile.EventCount,
	}

	return response, nil
}

// GetAdaptationMetrics retrieves system performance metrics
func (s *Service) GetAdaptationMetrics(ctx context.Context, periodStart, periodEnd time.Time) (*models.AdaptationMetricsResponse, error) {
	// Create context with timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	metrics, err := s.repo.GetAdaptationMetrics(dbCtx, periodStart, periodEnd)
	if err != nil {
		s.logger.Error("Failed to get adaptation metrics", zap.Error(err))
		return nil, fmt.Errorf("failed to get adaptation metrics: %w", err)
	}

	response := &models.AdaptationMetricsResponse{
		PeriodStart:       metrics.PeriodStart,
		PeriodEnd:         metrics.PeriodEnd,
		TotalEvents:       metrics.TotalEvents,
		ProcessedEvents:   metrics.ProcessedEvents,
		AvgProcessingTime: metrics.AvgProcessingTime,
		SuccessRate:       metrics.SuccessRate,
	}

	return response, nil
}

// processAdaptationEvent processes an adaptation event and updates player profile
func (s *Service) processAdaptationEvent(event *models.AdaptationEvent) {
	ctx := context.Background()

	// Create context with timeout for processing
	processCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// Get current player profile
	profile, err := s.repo.GetPlayerProfile(processCtx, event.PlayerID)
	if err != nil {
		s.logger.Error("Failed to get player profile for event processing",
			zap.String("event_id", event.ID.String()), zap.Error(err))
		return
	}

	// Apply machine learning adaptation based on event type
	switch event.EventType {
	case "combat_win":
		// Increase difficulty slightly for skilled players
		profile.Difficulty = math.Min(profile.Difficulty*1.01, 5.0)
	case "combat_loss":
		// Decrease difficulty for struggling players
		profile.Difficulty = math.Max(profile.Difficulty*0.99, 0.1)
	case "quest_failure":
		// Adjust learning rate based on quest performance
		profile.LearningRate = math.Max(profile.LearningRate*0.95, 0.001)
	case "quest_success":
		// Reward successful learning
		profile.LearningRate = math.Min(profile.LearningRate*1.05, 0.1)
	case "high_score":
		// Recognize exceptional performance
		profile.Difficulty = math.Min(profile.Difficulty*1.02, 10.0)
	}

	profile.LastUpdated = time.Now()
	profile.EventCount++

	// Save updated profile
	err = s.repo.UpdatePlayerProfile(processCtx, profile)
	if err != nil {
		s.logger.Error("Failed to update player profile during event processing",
			zap.String("event_id", event.ID.String()), zap.Error(err))
		return
	}

	// Mark event as processed
	err = s.repo.MarkEventProcessed(processCtx, event.ID)
	if err != nil {
		s.logger.Error("Failed to mark event as processed",
			zap.String("event_id", event.ID.String()), zap.Error(err))
		return
	}

	s.logger.Info("Adaptation event processed successfully",
		zap.String("event_id", event.ID.String()),
		zap.String("player_id", event.PlayerID.String()),
		zap.String("event_type", event.EventType),
		zap.Float64("new_difficulty", profile.Difficulty),
		zap.Float64("new_learning_rate", profile.LearningRate))
}

// HealthCheck performs service health check
func (s *Service) HealthCheck(ctx context.Context) error {
	// Create context with timeout for health check
	healthCtx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	return s.repo.HealthCheck(healthCtx)
}