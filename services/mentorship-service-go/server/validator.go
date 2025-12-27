// Request validation for Mentorship Service
// Issue: #140890865
// PERFORMANCE: Fast validation with minimal allocations

package server

import (
	"context"
	"errors"
	"fmt"
	"time"

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
// PERFORMANCE: Fast validation with minimal allocations for MMO load
func (v *Validator) ValidateCreateMentorshipContractRequest(ctx context.Context, req *api.CreateMentorshipContractRequest) error {
	v.logger.Debug("Validating CreateMentorshipContractRequest")

	// Required field validation
	if req == nil {
		return errors.New("request cannot be nil")
	}
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
	if req.ContractType == "" {
		return errors.New("contract_type cannot be empty")
	}

	// UUID format validation
	if _, err := uuid.Parse(req.MentorID); err != nil {
		return fmt.Errorf("invalid mentor_id format: %w", err)
	}
	if _, err := uuid.Parse(req.MenteeID); err != nil {
		return fmt.Errorf("invalid mentee_id format: %w", err)
	}

	// Business rule validation
	if req.MentorID == req.MenteeID {
		return errors.New("mentor_id and mentee_id cannot be the same")
	}

	// Enum validation for mentorship types
	validMentorshipTypes := map[string]bool{
		"technical":     true,
		"career":        true,
		"leadership":    true,
		"specialized":   true,
		"general":       true,
	}
	if !validMentorshipTypes[req.MentorshipType] {
		return fmt.Errorf("invalid mentorship_type: %s (must be one of: technical, career, leadership, specialized, general)", req.MentorshipType)
	}

	// Enum validation for contract types
	validContractTypes := map[string]bool{
		"trial":     true,
		"standard":  true,
		"premium":   true,
		"corporate": true,
	}
	if !validContractTypes[req.ContractType] {
		return fmt.Errorf("invalid contract_type: %s (must be one of: trial, standard, premium, corporate)", req.ContractType)
	}

	// Payment validation if provided
	if req.PaymentModel != "" {
		validPaymentModels := map[string]bool{
			"free":       true,
			"hourly":     true,
			"fixed":      true,
			"commission": true,
		}
		if !validPaymentModels[req.PaymentModel] {
			return fmt.Errorf("invalid payment_model: %s (must be one of: free, hourly, fixed, commission)", req.PaymentModel)
		}
	}

	// Date validation if provided
	if req.StartDate != "" {
		if _, err := time.Parse(time.RFC3339, req.StartDate); err != nil {
			return fmt.Errorf("invalid start_date format (must be RFC3339): %w", err)
		}
	}
	if req.EndDate != "" {
		if _, err := time.Parse(time.RFC3339, req.EndDate); err != nil {
			return fmt.Errorf("invalid end_date format (must be RFC3339): %w", err)
		}
	}

	v.logger.Debug("CreateMentorshipContractRequest validation passed")
	return nil
}

// ValidateUpdateMentorshipContractRequest validates contract update request
// PERFORMANCE: Fast validation for update operations
func (v *Validator) ValidateUpdateMentorshipContractRequest(ctx context.Context, req *api.UpdateMentorshipContractRequest) error {
	v.logger.Debug("Validating UpdateMentorshipContractRequest")

	if req == nil {
		return errors.New("request cannot be nil")
	}

	// At least one field must be provided for update
	hasUpdates := req.Status.IsSet() || req.EndDate.IsSet() || req.Terms != nil
	if !hasUpdates {
		return errors.New("at least one field must be provided for update")
	}

	// Status validation
	if req.Status.IsSet() {
		validStatuses := map[string]bool{
			"active":     true,
			"completed":  true,
			"cancelled":  true,
			"paused":     true,
		}
		if !validStatuses[req.Status.Value] {
			return fmt.Errorf("invalid status: %s (must be one of: active, completed, cancelled, paused)", req.Status.Value)
		}
	}

	// End date validation
	if req.EndDate.IsSet() && req.EndDate.Value != "" {
		if _, err := time.Parse(time.RFC3339, req.EndDate.Value); err != nil {
			return fmt.Errorf("invalid end_date format (must be RFC3339): %w", err)
		}
	}

	// Terms validation (JSON)
	if req.Terms != nil {
		// Basic JSON structure validation
		if terms, ok := req.Terms.(map[string]interface{}); ok {
			// Validate common terms fields
			for key, value := range terms {
				if key == "" {
					return errors.New("terms keys cannot be empty")
				}
				if value == nil {
					return fmt.Errorf("terms value for key '%s' cannot be null", key)
				}
			}
		}
	}

	v.logger.Debug("UpdateMentorshipContractRequest validation passed")
	return nil
}

// ValidateCreateLessonScheduleRequest validates lesson schedule creation request
func (v *Validator) ValidateCreateLessonScheduleRequest(ctx context.Context, req *api.CreateLessonScheduleRequest) error {
	v.logger.Debug("Validating CreateLessonScheduleRequest")

	if req == nil {
		return errors.New("request cannot be nil")
	}
	if req.ContractID == "" {
		return errors.New("contract_id cannot be empty")
	}
	if req.LessonDate == "" {
		return errors.New("lesson_date cannot be empty")
	}
	if req.LessonTime == "" {
		return errors.New("lesson_time cannot be empty")
	}
	if req.Location == "" {
		return errors.New("location cannot be empty")
	}
	if req.Format == "" {
		return errors.New("format cannot be empty")
	}

	// UUID validation
	if _, err := uuid.Parse(req.ContractID); err != nil {
		return fmt.Errorf("invalid contract_id format: %w", err)
	}

	// Date validation
	if _, err := time.Parse(time.RFC3339, req.LessonDate); err != nil {
		return fmt.Errorf("invalid lesson_date format (must be RFC3339): %w", err)
	}

	// Format validation
	validFormats := map[string]bool{
		"online":  true,
		"offline": true,
		"hybrid":  true,
	}
	if !validFormats[req.Format] {
		return fmt.Errorf("invalid format: %s (must be one of: online, offline, hybrid)", req.Format)
	}

	v.logger.Debug("CreateLessonScheduleRequest validation passed")
	return nil
}

// ValidateCompleteLessonRequest validates lesson completion request
func (v *Validator) ValidateCompleteLessonRequest(ctx context.Context, req *api.CompleteLessonRequest) error {
	v.logger.Debug("Validating CompleteLessonRequest")

	if req == nil {
		return errors.New("request cannot be nil")
	}

	// Duration validation if provided
	if req.Duration.IsSet() {
		if req.Duration.Value <= 0 {
			return errors.New("duration must be positive")
		}
		if req.Duration.Value > 480 { // 8 hours max
			return errors.New("duration cannot exceed 480 minutes (8 hours)")
		}
	}

	// Skill progress validation
	if req.SkillProgress != nil {
		if err := v.validateSkillProgress(req.SkillProgress); err != nil {
			return fmt.Errorf("invalid skill_progress: %w", err)
		}
	}

	// Evaluation validation
	if req.Evaluation != nil {
		if err := v.validateEvaluation(req.Evaluation); err != nil {
			return fmt.Errorf("invalid evaluation: %w", err)
		}
	}

	v.logger.Debug("CompleteLessonRequest validation passed")
	return nil
}

// validateSkillProgress validates skill progress JSON structure
func (v *Validator) validateSkillProgress(skillProgress map[string]interface{}) error {
	// Basic structure validation - ensure it's a valid JSON object
	if len(skillProgress) == 0 {
		return errors.New("skill_progress cannot be empty")
	}

	// Validate that values are numeric (skill levels)
	for skill, value := range skillProgress {
		if skill == "" {
			return errors.New("skill names cannot be empty")
		}
		if value == nil {
			return fmt.Errorf("skill '%s' value cannot be null", skill)
		}
		// Allow both numbers and strings that can be parsed as numbers
		switch v := value.(type) {
		case float64:
			if v < 0 || v > 100 {
				return fmt.Errorf("skill '%s' value must be between 0 and 100", skill)
			}
		case int:
			if v < 0 || v > 100 {
				return fmt.Errorf("skill '%s' value must be between 0 and 100", skill)
			}
		default:
			return fmt.Errorf("skill '%s' value must be a number", skill)
		}
	}

	return nil
}

// validateEvaluation validates evaluation JSON structure
func (v *Validator) validateEvaluation(evaluation map[string]interface{}) error {
	// Basic structure validation
	if len(evaluation) == 0 {
		return errors.New("evaluation cannot be empty")
	}

	// Validate required evaluation fields
	requiredFields := []string{"overall_rating", "effectiveness"}
	for _, field := range requiredFields {
		if _, exists := evaluation[field]; !exists {
			return fmt.Errorf("evaluation must contain '%s' field", field)
		}
	}

	// Validate rating fields are numeric
	ratingFields := []string{"overall_rating", "content_quality", "teaching_quality", "communication"}
	for _, field := range ratingFields {
		if value, exists := evaluation[field]; exists && value != nil {
			switch v := value.(type) {
			case float64:
				if v < 1.0 || v > 5.0 {
					return fmt.Errorf("rating field '%s' must be between 1.0 and 5.0", field)
				}
			case int:
				if v < 1 || v > 5 {
					return fmt.Errorf("rating field '%s' must be between 1 and 5", field)
				}
			default:
				return fmt.Errorf("rating field '%s' must be a number", field)
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




