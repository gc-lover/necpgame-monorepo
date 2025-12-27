// Business logic service for Announcement Service
// Issue: #323
// PERFORMANCE: Optimized for high-throughput announcement management

package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/internal/config"
	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// AnnouncementService handles business logic for announcements
type AnnouncementService struct {
	repo     *Repository
	cache    *Cache
	validator *Validator
	metrics   *Metrics
	logger   *zap.Logger
	mu       sync.RWMutex
}

// NewAnnouncementService creates a new announcement service instance
func NewAnnouncementService(logger *zap.Logger, dbConfig *config.DatabaseConfig) *AnnouncementService {
	return &AnnouncementService{
		repo:      NewRepository(logger, dbConfig),
		cache:     NewCache(logger),
		validator: NewValidator(logger),
		metrics:   NewMetrics(logger),
		logger:    logger,
	}
}

// GetAnnouncements returns a paginated list of announcements
func (s *AnnouncementService) GetAnnouncements(ctx context.Context, params api.GetAnnouncementsParams) ([]*api.Announcement, int, error) {
	s.metrics.RecordRequest("GetAnnouncements")

	// Parse query parameters
	var announcementType *string
	if params.Type.IsSet() {
		announcementType = &params.Type.Value
	}

	var priority *string
	if params.Priority.IsSet() {
		priority = &params.Priority.Value
	}

	limit := 50 // default
	if params.Limit.IsSet() {
		limit = params.Limit.Value
		if limit > 100 {
			limit = 100
		}
	}

	offset := 0 // default
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	announcements, total, err := s.repo.GetAnnouncements(ctx, announcementType, priority, limit, offset)
	if err != nil {
		s.metrics.RecordError("GetAnnouncements")
		return nil, 0, fmt.Errorf("failed to get announcements: %w", err)
	}

	return announcements, total, nil
}

// GetAnnouncement returns a single announcement by ID
func (s *AnnouncementService) GetAnnouncement(ctx context.Context, id uuid.UUID) (*api.Announcement, error) {
	s.metrics.RecordRequest("GetAnnouncement")

	// Try cache first
	if announcement, found := s.cache.Get(ctx, fmt.Sprintf("announcement:%s", id.String())); found {
		if ann, ok := announcement.(*api.Announcement); ok {
			return ann, nil
		}
	}

	// Get from repository
	announcement, err := s.repo.GetAnnouncement(ctx, id)
	if err != nil {
		s.metrics.RecordError("GetAnnouncement")
		return nil, fmt.Errorf("failed to get announcement: %w", err)
	}

	// Cache the result
	s.cache.Set(ctx, fmt.Sprintf("announcement:%s", id.String()), announcement, 5*time.Minute)

	return announcement, nil
}

// CreateAnnouncement creates a new announcement
func (s *AnnouncementService) CreateAnnouncement(ctx context.Context, req *api.CreateAnnouncementRequest) (*api.Announcement, error) {
	s.metrics.RecordRequest("CreateAnnouncement")

	// Validate request
	if err := s.validator.ValidateCreateAnnouncementRequest(ctx, req); err != nil {
		s.metrics.RecordError("CreateAnnouncement")
		return nil, fmt.Errorf("validation failed: %w", err)
	}

	// Extract user ID from context
	userID := getUserIDFromContext(ctx)
	var createdByUUID uuid.UUID
	if userID != "" && userID != "unknown" {
		var err error
		createdByUUID, err = uuid.Parse(userID)
		if err != nil {
			s.logger.Warn("Failed to parse user ID from context", zap.String("user_id", userID), zap.Error(err))
			createdByUUID = uuid.Nil
		}
	} else {
		createdByUUID = uuid.Nil
	}

	// Create announcement object
	now := time.Now()
	announcement := &api.Announcement{
		ID:               api.NewOptUUID(uuid.New()),
		CreatedBy:        api.NewOptUUID(createdByUUID),
		Title:            req.Title,
		Content:          req.Content,
		AnnouncementType: req.AnnouncementType,
		Priority:         req.Priority,
		DisplayStyle:     req.DisplayStyle,
		Status:           api.NewOptAnnouncementStatus(api.AnnouncementStatusDRAFT),
		CreatedAt:        api.NewOptDateTime(now),
		UpdatedAt:        api.NewOptDateTime(now),
	}

	// Set optional fields
	if req.Targeting.IsSet() {
		announcement.Targeting = req.Targeting
	}
	if req.Media.IsSet() {
		announcement.Media = req.Media
	}
	if req.DeliveryChannels.IsSet() {
		announcement.DeliveryChannels = req.DeliveryChannels
	}

	// Save to repository
	if err := s.repo.CreateAnnouncement(ctx, announcement); err != nil {
		s.metrics.RecordError("CreateAnnouncement")
		return nil, fmt.Errorf("failed to save announcement: %w", err)
	}

	s.logger.Info("Announcement created", zap.String("id", announcement.ID.Value.String()), zap.String("title", announcement.Title))

	return announcement, nil
}

// UpdateAnnouncement updates an existing announcement
func (s *AnnouncementService) UpdateAnnouncement(ctx context.Context, id uuid.UUID, req *api.UpdateAnnouncementRequest) (*api.Announcement, error) {
	s.metrics.RecordRequest("UpdateAnnouncement")

	// Validate request
	if err := s.validator.ValidateUpdateAnnouncementRequest(ctx, req); err != nil {
		s.metrics.RecordError("UpdateAnnouncement")
		return nil, fmt.Errorf("validation failed: %w", err)
	}

	// Get existing announcement
	existing, err := s.repo.GetAnnouncement(ctx, id)
	if err != nil {
		s.metrics.RecordError("UpdateAnnouncement")
		return nil, fmt.Errorf("failed to get existing announcement: %w", err)
	}

	// Update fields
	if req.Title.IsSet() {
		existing.Title = req.Title.Value
	}
	if req.Content.IsSet() {
		existing.Content = req.Content.Value
	}
	if req.Priority.IsSet() {
		existing.Priority = req.Priority
	}
	if req.DisplayStyle.IsSet() {
		existing.DisplayStyle = req.DisplayStyle
	}
	if req.Targeting.IsSet() {
		existing.Targeting = req.Targeting
	}
	if req.Media.IsSet() {
		existing.Media = req.Media
	}
	if req.DeliveryChannels.IsSet() {
		existing.DeliveryChannels = req.DeliveryChannels
	}

	existing.UpdatedAt = api.NewOptDateTime(time.Now())

	// Save updated announcement
	if err := s.repo.UpdateAnnouncement(ctx, existing); err != nil {
		s.metrics.RecordError("UpdateAnnouncement")
		return nil, fmt.Errorf("failed to update announcement: %w", err)
	}

	// Invalidate cache
	s.cache.Delete(ctx, fmt.Sprintf("announcement:%s", id.String()))

	s.logger.Info("Announcement updated", zap.String("id", id.String()))

	return existing, nil
}

// DeleteAnnouncement deletes an announcement
func (s *AnnouncementService) DeleteAnnouncement(ctx context.Context, id uuid.UUID) error {
	s.metrics.RecordRequest("DeleteAnnouncement")

	// Check if announcement exists
	_, err := s.repo.GetAnnouncement(ctx, id)
	if err != nil {
		s.metrics.RecordError("DeleteAnnouncement")
		return fmt.Errorf("announcement not found: %w", err)
	}

	// Delete from repository
	if err := s.repo.DeleteAnnouncement(ctx, id); err != nil {
		s.metrics.RecordError("DeleteAnnouncement")
		return fmt.Errorf("failed to delete announcement: %w", err)
	}

	// Invalidate cache
	s.cache.Delete(ctx, fmt.Sprintf("announcement:%s", id.String()))

	s.logger.Info("Announcement deleted", zap.String("id", id.String()))

	return nil
}

// PublishAnnouncement publishes an announcement immediately
func (s *AnnouncementService) PublishAnnouncement(ctx context.Context, id uuid.UUID) (*api.Announcement, error) {
	s.metrics.RecordRequest("PublishAnnouncement")

	// Get announcement
	announcement, err := s.repo.GetAnnouncement(ctx, id)
	if err != nil {
		s.metrics.RecordError("PublishAnnouncement")
		return nil, fmt.Errorf("announcement not found: %w", err)
	}

	// Update status and timestamps
	now := time.Now()
	announcement.Status = api.NewOptAnnouncementStatus(api.AnnouncementStatusPUBLISHED)
	announcement.PublishedAt = api.NewOptDateTime(now)
	announcement.UpdatedAt = api.NewOptDateTime(now)

	// Clear scheduled time if exists
	announcement.ScheduledPublishAt = api.OptDateTime{}

	// Save updated announcement
	if err := s.repo.UpdateAnnouncement(ctx, announcement); err != nil {
		s.metrics.RecordError("PublishAnnouncement")
		return nil, fmt.Errorf("failed to publish announcement: %w", err)
	}

	// Invalidate cache
	s.cache.Delete(ctx, fmt.Sprintf("announcement:%s", id.String()))

	s.logger.Info("Announcement published", zap.String("id", id.String()))

	return announcement, nil
}

// ScheduleAnnouncement schedules an announcement for future publication
func (s *AnnouncementService) ScheduleAnnouncement(ctx context.Context, id uuid.UUID, req *api.ScheduleAnnouncementRequest) (*api.Announcement, error) {
	s.metrics.RecordRequest("ScheduleAnnouncement")

	// Get announcement
	announcement, err := s.repo.GetAnnouncement(ctx, id)
	if err != nil {
		s.metrics.RecordError("ScheduleAnnouncement")
		return nil, fmt.Errorf("announcement not found: %w", err)
	}

	// Update scheduled time and status
	announcement.Status = api.NewOptAnnouncementStatus(api.AnnouncementStatusSCHEDULED)
	announcement.ScheduledPublishAt = api.NewOptDateTime(req.ScheduledPublishAt.Value)
	announcement.UpdatedAt = api.NewOptDateTime(time.Now())

	// Save updated announcement
	if err := s.repo.UpdateAnnouncement(ctx, announcement); err != nil {
		s.metrics.RecordError("ScheduleAnnouncement")
		return nil, fmt.Errorf("failed to schedule announcement: %w", err)
	}

	// Invalidate cache
	s.cache.Delete(ctx, fmt.Sprintf("announcement:%s", id.String()))

	s.logger.Info("Announcement scheduled", zap.String("id", id.String()), zap.Time("scheduled_at", req.ScheduledPublishAt.Value))

	return announcement, nil
}

// getUserIDFromContext extracts user ID from context
// BACKEND NOTE: Standard way to extract user ID from JWT context
// The user ID is typically set by authentication middleware
func getUserIDFromContext(ctx context.Context) string {
	// Try to get user_id from context (set by auth middleware)
	if userID, ok := ctx.Value("user_id").(string); ok && userID != "" {
		return userID
	}
	
	// Try alternative key names
	if userID, ok := ctx.Value("userID").(string); ok && userID != "" {
		return userID
	}
	
	if userID, ok := ctx.Value("userId").(string); ok && userID != "" {
		return userID
	}
	
	// If not found, return empty string (will be converted to uuid.Nil)
	return ""
}






