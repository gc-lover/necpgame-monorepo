// Request validation for Mentorship Service
// Issue: #140890865
// PERFORMANCE: Fast validation with minimal allocations

package server

import (
	"context"
	"errors"
	"fmt"

	"github.com/gc-lover/necpgame-monorepo/services/mentorship-service-go/pkg/api"
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

// ValidateCreateMentorshipContractRequest validates contract creation request
func (v *Validator) ValidateCreateMentorshipContractRequest(ctx context.Context, req *api.CreateMentorshipContractRequest) error {
	v.logger.Debug("Validating CreateMentorshipContractRequest")

	if req.MentorID == "" {
		return errors.New("mentor_id cannot be empty")
	}
	if req.MenteeID == "" {
		return errors.New("mentee_id cannot be empty")
	}
	if req.SkillTrack == "" {
		return errors.New("skill_track cannot be empty")
	}
	if req.MentorshipType == "" {
		return errors.New("mentorship_type cannot be empty")
	}

	// Validate UUIDs
	if _, err := uuid.Parse(req.MentorID); err != nil {
		return fmt.Errorf("invalid mentor_id format: %w", err)
	}
	if _, err := uuid.Parse(req.MenteeID); err != nil {
		return fmt.Errorf("invalid mentee_id format: %w", err)
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

