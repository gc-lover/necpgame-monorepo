// Request validation for Announcement Service
// Issue: #323
// PERFORMANCE: Fast validation with minimal allocations

package server

import (
	"context"
	"errors"
	"fmt"
	"strings"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Validator handles request validation
type Validator struct {
	logger *zap.Logger
}

// NewValidator creates a new validator instance
func NewValidator(logger *zap.Logger) *Validator {
	return &Validator{logger: logger}
}

// ValidateCreateAnnouncementRequest validates the request for creating an announcement
func (v *Validator) ValidateCreateAnnouncementRequest(ctx context.Context, req *api.CreateAnnouncementRequest) error {
	v.logger.Debug("Validating CreateAnnouncementRequest", zap.Any("request", req))

	if strings.TrimSpace(req.Title) == "" {
		return errors.New("title cannot be empty")
	}

	if len(req.Title) > 255 {
		return errors.New("title cannot exceed 255 characters")
	}

	if strings.TrimSpace(req.Content) == "" {
		return errors.New("content cannot be empty")
	}

	// Validate announcement type
	validTypes := []string{
		"GAME_NEWS", "PATCH_NOTES", "MAINTENANCE", "EVENT",
		"PROMOTION", "COMMUNITY", "EMERGENCY",
	}
	if !v.isValidEnum(req.AnnouncementType, validTypes) {
		return errors.New("invalid announcement_type")
	}

	// Validate priority
	validPriorities := []string{"LOW", "NORMAL", "HIGH", "CRITICAL"}
	if req.Priority.IsSet() && !v.isValidEnum(string(req.Priority.Value), validPriorities) {
		return errors.New("invalid priority")
	}

	// Validate display style
	validStyles := []string{"NEWS_FEED", "POPUP", "MODAL", "BANNER", "TOAST"}
	if req.DisplayStyle.IsSet() && !v.isValidEnum(string(req.DisplayStyle.Value), validStyles) {
		return errors.New("invalid display_style")
	}

	// Validate delivery channels
	if req.DeliveryChannels.IsSet() {
		validChannels := []string{"IN_GAME_POPUP", "PUSH_NOTIFICATION", "EMAIL", "NEWS_FEED"}
		for _, channel := range req.DeliveryChannels.Value {
			if !v.isValidEnum(channel, validChannels) {
				return fmt.Errorf("invalid delivery channel: %s", channel)
			}
		}
	}

	return nil
}

// ValidateUpdateAnnouncementRequest validates the request for updating an announcement
func (v *Validator) ValidateUpdateAnnouncementRequest(ctx context.Context, req *api.UpdateAnnouncementRequest) error {
	v.logger.Debug("Validating UpdateAnnouncementRequest", zap.Any("request", req))

	if req.Title.IsSet() && strings.TrimSpace(req.Title.Value) == "" {
		return errors.New("title cannot be empty")
	}

	if req.Title.IsSet() && len(req.Title.Value) > 255 {
		return errors.New("title cannot exceed 255 characters")
	}

	if req.Content.IsSet() && strings.TrimSpace(req.Content.Value) == "" {
		return errors.New("content cannot be empty")
	}

	// Validate priority
	validPriorities := []string{"LOW", "NORMAL", "HIGH", "CRITICAL"}
	if req.Priority.IsSet() && !v.isValidEnum(string(req.Priority.Value), validPriorities) {
		return errors.New("invalid priority")
	}

	// Validate display style
	validStyles := []string{"NEWS_FEED", "POPUP", "MODAL", "BANNER", "TOAST"}
	if req.DisplayStyle.IsSet() && !v.isValidEnum(string(req.DisplayStyle.Value), validStyles) {
		return errors.New("invalid display_style")
	}

	// Validate delivery channels
	if req.DeliveryChannels.IsSet() {
		validChannels := []string{"IN_GAME_POPUP", "PUSH_NOTIFICATION", "EMAIL", "NEWS_FEED"}
		for _, channel := range req.DeliveryChannels.Value {
			if !v.isValidEnum(channel, validChannels) {
				return fmt.Errorf("invalid delivery channel: %s", channel)
			}
		}
	}

	return nil
}

// ValidateUUID validates if a string is a valid UUID
func (v *Validator) ValidateUUID(ctx context.Context, id string) error {
	if _, err := uuid.Parse(id); err != nil {
		return fmt.Errorf("invalid UUID format: %w", err)
	}
	return nil
}

// isValidEnum checks if a value is in the list of valid values
func (v *Validator) isValidEnum(value string, validValues []string) bool {
	for _, valid := range validValues {
		if value == valid {
			return true
		}
	}
	return false
}



