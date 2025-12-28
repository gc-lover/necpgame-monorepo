// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: SLA service with optimized business logic and caching

package service

import (
	"context"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"support-service-go/internal/models"
	"support-service-go/internal/repository"
)

// SLAService defines SLA business logic operations
type SLAService interface {
	// SLA Status operations
	CalculateSLAStatus(ctx context.Context, ticketID uuid.UUID, priority string, createdAt time.Time) (*models.SLAStatus, error)
	UpdateSLAStatus(ctx context.Context, ticketID uuid.UUID, firstResponseAt, resolutionAt *time.Time) error
	GetSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.SLAStatus, error)

	// SLA Violations operations
	CheckForSLAViolations(ctx context.Context, ticketID uuid.UUID) ([]models.SLAViolation, error)
	GetSLAViolations(ctx context.Context, limit, offset int, priority, violationType *string) ([]models.SLAViolation, int, error)

	// SLA Analytics
	GetSLAViolationStats(ctx context.Context, days int) (map[string]int, error)
}

// SLAServiceImpl implements SLAService
type SLAServiceImpl struct {
	repo   repository.SLARepository
	logger *zap.Logger
}

// NewSLAService creates a new SLA service instance
func NewSLAService(repo repository.SLARepository, logger *zap.Logger) *SLAServiceImpl {
	return &SLAServiceImpl{
		repo:   repo,
		logger: logger,
	}
}

// CalculateSLAStatus calculates SLA targets and current status for a ticket
func (s *SLAServiceImpl) CalculateSLAStatus(ctx context.Context, ticketID uuid.UUID, priority string, createdAt time.Time) (*models.SLAStatus, error) {
	// Get SLA priority configuration
	slaPriority, err := s.repo.GetSLAPriority(ctx, priority)
	if err != nil {
		s.logger.Error("Failed to get SLA priority",
			zap.String("ticket_id", ticketID.String()),
			zap.String("priority", priority),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get SLA priority: %w", err)
	}

	// Calculate target times
	firstResponseTarget := createdAt.Add(slaPriority.FirstResponseTarget)
	resolutionTarget := createdAt.Add(slaPriority.ResolutionTarget)

	now := time.Now()

	// Calculate remaining time in seconds
	var timeUntilFirstResponse, timeUntilResolution *int

	if now.Before(firstResponseTarget) {
		remaining := int(math.Ceil(firstResponseTarget.Sub(now).Seconds()))
		timeUntilFirstResponse = &remaining
	}

	if now.Before(resolutionTarget) {
		remaining := int(math.Ceil(resolutionTarget.Sub(now).Seconds()))
		timeUntilResolution = &remaining
	}

	status := &models.SLAStatus{
		TicketID:             ticketID,
		Priority:             priority,
		FirstResponseTarget:  firstResponseTarget,
		ResolutionTarget:     resolutionTarget,
		TimeUntilFirstResponse: timeUntilFirstResponse,
		TimeUntilResolution:    timeUntilResolution,
		CreatedAt:            now,
		UpdatedAt:            now,
	}

	// Save to database
	err = s.repo.CreateSLAStatus(ctx, status)
	if err != nil {
		s.logger.Error("Failed to create SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return nil, fmt.Errorf("failed to create SLA status: %w", err)
	}

	return status, nil
}

// UpdateSLAStatus updates SLA status when ticket events occur
func (s *SLAServiceImpl) UpdateSLAStatus(ctx context.Context, ticketID uuid.UUID, firstResponseAt, resolutionAt *time.Time) error {
	// Get current SLA status
	status, err := s.repo.GetSLAStatus(ctx, ticketID)
	if err != nil {
		s.logger.Error("Failed to get current SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return fmt.Errorf("failed to get SLA status: %w", err)
	}

	now := time.Now()
	status.UpdatedAt = now

	// Update first response if provided
	if firstResponseAt != nil && status.FirstResponseActual == nil {
		status.FirstResponseActual = firstResponseAt

		// Check if SLA was met
		firstResponseSLAMet := firstResponseAt.Before(status.FirstResponseTarget) ||
			firstResponseAt.Equal(status.FirstResponseTarget)
		status.FirstResponseSLAMet = &firstResponseSLAMet

		// Recalculate remaining time (should be 0 or negative)
		if now.Before(status.FirstResponseTarget) {
			remaining := int(math.Ceil(status.FirstResponseTarget.Sub(now).Seconds()))
			status.TimeUntilFirstResponse = &remaining
		} else {
			remaining := int(math.Ceil(now.Sub(status.FirstResponseTarget).Seconds())) * -1
			status.TimeUntilFirstResponse = &remaining
		}
	}

	// Update resolution if provided
	if resolutionAt != nil && status.ResolutionActual == nil {
		status.ResolutionActual = resolutionAt

		// Check if SLA was met
		resolutionSLAMet := resolutionAt.Before(status.ResolutionTarget) ||
			resolutionAt.Equal(status.ResolutionTarget)
		status.ResolutionSLAMet = &resolutionSLAMet

		// Recalculate remaining time (should be 0 or negative)
		if now.Before(status.ResolutionTarget) {
			remaining := int(math.Ceil(status.ResolutionTarget.Sub(now).Seconds()))
			status.TimeUntilResolution = &remaining
		} else {
			remaining := int(math.Ceil(now.Sub(status.ResolutionTarget).Seconds())) * -1
			status.TimeUntilResolution = &remaining
		}
	}

	// Save updated status
	err = s.repo.UpdateSLAStatus(ctx, status)
	if err != nil {
		s.logger.Error("Failed to update SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return fmt.Errorf("failed to update SLA status: %w", err)
	}

	// Check for violations and create them if needed
	_, err = s.CheckForSLAViolations(ctx, ticketID)
	if err != nil {
		s.logger.Error("Failed to check for SLA violations after update",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		// Don't return error here as the main operation succeeded
	}

	return nil
}

// GetSLAStatus retrieves current SLA status for a ticket
func (s *SLAServiceImpl) GetSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.SLAStatus, error) {
	status, err := s.repo.GetSLAStatus(ctx, ticketID)
	if err != nil {
		s.logger.Error("Failed to get SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get SLA status: %w", err)
	}

	// Recalculate remaining time based on current time
	now := time.Now()

	if status.FirstResponseActual == nil {
		if now.Before(status.FirstResponseTarget) {
			remaining := int(math.Ceil(status.FirstResponseTarget.Sub(now).Seconds()))
			status.TimeUntilFirstResponse = &remaining
		} else {
			remaining := int(math.Ceil(now.Sub(status.FirstResponseTarget).Seconds())) * -1
			status.TimeUntilFirstResponse = &remaining
		}
	}

	if status.ResolutionActual == nil {
		if now.Before(status.ResolutionTarget) {
			remaining := int(math.Ceil(status.ResolutionTarget.Sub(now).Seconds()))
			status.TimeUntilResolution = &remaining
		} else {
			remaining := int(math.Ceil(now.Sub(status.ResolutionTarget).Seconds())) * -1
			status.TimeUntilResolution = &remaining
		}
	}

	return status, nil
}

// CheckForSLAViolations checks if there are any SLA violations for a ticket and creates violation records
func (s *SLAServiceImpl) CheckForSLAViolations(ctx context.Context, ticketID uuid.UUID) ([]models.SLAViolation, error) {
	status, err := s.repo.GetSLAStatus(ctx, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get SLA status: %w", err)
	}

	var violations []models.SLAViolation
	now := time.Now()

	// Check first response violation
	if status.FirstResponseActual != nil && status.FirstResponseSLAMet != nil && !*status.FirstResponseSLAMet {
		violationDuration := int(math.Ceil(status.FirstResponseActual.Sub(status.FirstResponseTarget).Seconds()))

		violation := models.SLAViolation{
			TicketID:          ticketID,
			TicketNumber:      fmt.Sprintf("#%s", ticketID.String()[:8]), // Shortened ID for display
			Priority:          status.Priority,
			ViolationType:     "FIRST_RESPONSE",
			TargetTime:        status.FirstResponseTarget,
			ActualTime:        status.FirstResponseActual,
			ViolationDuration: violationDuration,
			CreatedAt:         now,
		}

		err = s.repo.CreateSLAViolation(ctx, &violation)
		if err != nil {
			s.logger.Error("Failed to create first response SLA violation",
				zap.String("ticket_id", ticketID.String()),
				zap.Error(err))
		} else {
			violations = append(violations, violation)
		}
	}

	// Check resolution violation
	if status.ResolutionActual != nil && status.ResolutionSLAMet != nil && !*status.ResolutionSLAMet {
		violationDuration := int(math.Ceil(status.ResolutionActual.Sub(status.ResolutionTarget).Seconds()))

		violation := models.SLAViolation{
			TicketID:          ticketID,
			TicketNumber:      fmt.Sprintf("#%s", ticketID.String()[:8]), // Shortened ID for display
			Priority:          status.Priority,
			ViolationType:     "RESOLUTION",
			TargetTime:        status.ResolutionTarget,
			ActualTime:        status.ResolutionActual,
			ViolationDuration: violationDuration,
			CreatedAt:         now,
		}

		err = s.repo.CreateSLAViolation(ctx, &violation)
		if err != nil {
			s.logger.Error("Failed to create resolution SLA violation",
				zap.String("ticket_id", ticketID.String()),
				zap.Error(err))
		} else {
			violations = append(violations, violation)
		}
	}

	return violations, nil
}

// GetSLAViolations retrieves SLA violations with pagination and filtering
func (s *SLAServiceImpl) GetSLAViolations(ctx context.Context, limit, offset int, priority, violationType *string) ([]models.SLAViolation, int, error) {
	return s.repo.GetSLAViolations(ctx, limit, offset, priority, violationType)
}

// GetSLAViolationStats gets violation statistics
func (s *SLAServiceImpl) GetSLAViolationStats(ctx context.Context, days int) (map[string]int, error) {
	return s.repo.GetSLAViolationStats(ctx, days)
}

// Issue: #1489 - Support SLA Service: ogen handlers implementation
