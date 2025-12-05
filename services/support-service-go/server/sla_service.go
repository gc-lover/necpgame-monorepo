// Issue: #1489
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
)

type SLAServiceInterface interface {
	GetTicketSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAStatus, error)
	GetSLAViolations(ctx context.Context, priority *string, violationType *string, limit, offset int) (*models.SLAViolationsResponse, error)
}

type SLAService struct {
	repo SLARepositoryInterface
}

type SLARepositoryInterface interface {
	GetTicketSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAStatus, error)
	GetSLAViolations(ctx context.Context, priority *string, violationType *string, limit, offset int) ([]models.SLAViolation, int, error)
}

func NewSLAService(repo SLARepositoryInterface) *SLAService {
	return &SLAService{
		repo: repo,
	}
}

func (s *SLAService) GetTicketSLAStatus(ctx context.Context, ticketID uuid.UUID) (*models.TicketSLAStatus, error) {
	status, err := s.repo.GetTicketSLAStatus(ctx, ticketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get ticket SLA status: %w", err)
	}
	if status == nil {
		return nil, fmt.Errorf("ticket not found")
	}
	return status, nil
}

func (s *SLAService) GetSLAViolations(ctx context.Context, priority *string, violationType *string, limit, offset int) (*models.SLAViolationsResponse, error) {
	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	if violationType != nil {
		validTypes := []string{"FIRST_RESPONSE", "RESOLUTION"}
		valid := false
		for _, vt := range validTypes {
			if *violationType == vt {
				valid = true
				break
			}
		}
		if !valid {
			return nil, fmt.Errorf("invalid violation_type: %s", *violationType)
		}
	}

	violations, total, err := s.repo.GetSLAViolations(ctx, priority, violationType, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get SLA violations: %w", err)
	}

	return &models.SLAViolationsResponse{
		Items:  violations,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}, nil
}

