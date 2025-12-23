// Request validation for Maintenance Windows Service
// Issue: #316
// PERFORMANCE: Fast validation with minimal allocations

package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/maintenance-windows-service-go/pkg/api"
)

// Validator handles request validation
type Validator struct{}

// NewValidator creates a new validator instance
func NewValidator() *Validator {
	return &Validator{}
}

// ValidateCreateRequest validates create maintenance window request
func (v *Validator) ValidateCreateRequest(ctx context.Context, req *api.CreateMaintenanceWindowRequest) error {
	if req.Title == "" {
		return errors.New("title is required")
	}

	if len(req.Title) > 255 {
		return errors.New("title must be less than 256 characters")
	}

	if req.ScheduledStart.After(req.ScheduledEnd) {
		return errors.New("scheduled_start must be before scheduled_end")
	}

	if req.ScheduledStart.Before(time.Now()) {
		return errors.New("scheduled_start cannot be in the past")
	}

	minDuration := 15 * time.Minute
	if req.ScheduledEnd.Sub(req.ScheduledStart) < minDuration {
		return errors.New("maintenance window must be at least 15 minutes long")
	}

	maxDuration := 24 * time.Hour
	if req.ScheduledEnd.Sub(req.ScheduledStart) > maxDuration {
		return errors.New("maintenance window cannot be longer than 24 hours")
	}

	// Validate affected services
	for _, service := range req.AffectedServices {
		if service == "" {
			return errors.New("affected_services cannot contain empty strings")
		}
		if len(service) > 100 {
			return errors.New("service name cannot be longer than 100 characters")
		}
	}

	return nil
}

// ValidateUpdateRequest validates update maintenance window request
func (v *Validator) ValidateUpdateRequest(ctx context.Context, req *api.UpdateMaintenanceWindowRequest) error {
	if req.Title != nil && *req.Title == "" {
		return errors.New("title cannot be empty")
	}

	if req.Title != nil && len(*req.Title) > 255 {
		return errors.New("title must be less than 256 characters")
	}

	if req.ScheduledStart != nil && req.ScheduledEnd != nil {
		if req.ScheduledStart.After(*req.ScheduledEnd) {
			return errors.New("scheduled_start must be before scheduled_end")
		}
	}

	if req.ScheduledStart != nil && req.ScheduledStart.Before(time.Now()) {
		return errors.New("scheduled_start cannot be in the past")
	}

	if req.ScheduledStart != nil && req.ScheduledEnd != nil {
		minDuration := 15 * time.Minute
		if req.ScheduledEnd.Sub(*req.ScheduledStart) < minDuration {
			return errors.New("maintenance window must be at least 15 minutes long")
		}

		maxDuration := 24 * time.Hour
		if req.ScheduledEnd.Sub(*req.ScheduledStart) > maxDuration {
			return errors.New("maintenance window cannot be longer than 24 hours")
		}
	}

	// Validate affected services if provided
	if req.AffectedServices != nil {
		for _, service := range *req.AffectedServices {
			if service == "" {
				return errors.New("affected_services cannot contain empty strings")
			}
			if len(service) > 100 {
				return errors.New("service name cannot be longer than 100 characters")
			}
		}
	}

	return nil
}

// ValidateWindowID validates that window ID is not empty
func (v *Validator) ValidateWindowID(ctx context.Context, windowID string) error {
	if windowID == "" {
		return errors.New("window_id is required")
	}

	// Could add UUID format validation here if needed
	return nil
}

// ValidatePagination validates pagination parameters
func (v *Validator) ValidatePagination(ctx context.Context, limit, offset *int) error {
	if limit != nil && (*limit < 1 || *limit > 100) {
		return errors.New("limit must be between 1 and 100")
	}

	if offset != nil && *offset < 0 {
		return errors.New("offset cannot be negative")
	}

	return nil
}
