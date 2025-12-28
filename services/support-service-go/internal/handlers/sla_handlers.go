// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: SLA handlers with optimized request processing and error handling

package handlers

import (
	"context"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"support-service-go/internal/service"
	"support-service-go/pkg/api"
)

// SLAHandlers handles SLA-related HTTP requests
type SLAHandlers struct {
	slaService service.SLAService
	logger     *zap.Logger
}

// NewSLAHandlers creates new SLA handlers instance
func NewSLAHandlers(slaService service.SLAService, logger *zap.Logger) *SLAHandlers {
	return &SLAHandlers{
		slaService: slaService,
		logger:     logger,
	}
}

// GetTicketSLA implements ogen-generated interface for SLA status retrieval
// PERFORMANCE: Optimized SLA status lookup with caching support
func (h *SLAHandlers) GetTicketSLA(ctx context.Context, ticketID uuid.UUID) (api.TicketSLAStatus, error) {
	start := time.Now()
	defer func() {
		h.logger.Info("GetTicketSLA ogen operation completed",
			zap.Duration("duration", time.Since(start)),
			zap.String("ticket_id", ticketID.String()))
	}()

	// Get SLA status from service
	slaStatus, err := h.slaService.GetSLAStatus(ctx, ticketID)
	if err != nil {
		h.logger.Error("Failed to get SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		return api.TicketSLAStatus{}, err
	}

	// Convert to ogen API format
	result := api.TicketSLAStatus{
		TicketID:            slaStatus.TicketID,
		Priority:            api.TicketSLAStatusPriority(slaStatus.Priority),
		FirstResponseTarget: api.NewOptDateTime(slaStatus.FirstResponseTarget),
		ResolutionTarget:    api.NewOptDateTime(slaStatus.ResolutionTarget),
	}

	if slaStatus.FirstResponseActual != nil {
		result.FirstResponseActual = api.NewOptDateTime(*slaStatus.FirstResponseActual)
	}
	if slaStatus.ResolutionActual != nil {
		result.ResolutionActual = api.NewOptDateTime(*slaStatus.ResolutionActual)
	}
	if slaStatus.FirstResponseSLAMet != nil {
		result.FirstResponseSLAMet = api.NewOptBool(*slaStatus.FirstResponseSLAMet)
	}
	if slaStatus.ResolutionSLAMet != nil {
		result.ResolutionSLAMet = api.NewOptBool(*slaStatus.ResolutionSLAMet)
	}
	if slaStatus.TimeUntilFirstResponse != nil {
		result.TimeUntilFirstResponse = api.NewOptInt(*slaStatus.TimeUntilFirstResponse)
	}
	if slaStatus.TimeUntilResolution != nil {
		result.TimeUntilResolution = api.NewOptInt(*slaStatus.TimeUntilResolution)
	}

	return result, nil
}

// GetSLAViolations implements ogen-generated interface for SLA violations retrieval
// PERFORMANCE: Paginated SLA violations with filtering and sorting
func (h *SLAHandlers) GetSLAViolations(ctx context.Context, limit, offset int, priority, violationType *string) ([]api.SLAViolation, int, error) {
	start := time.Now()
	defer func() {
		h.logger.Info("GetSLAViolations ogen operation completed",
			zap.Duration("duration", time.Since(start)),
			zap.Int("limit", limit),
			zap.Int("offset", offset))
	}()

	// Get violations from service
	violations, total, err := h.slaService.GetSLAViolations(ctx, limit, offset, priority, violationType)
	if err != nil {
		h.logger.Error("Failed to get SLA violations", zap.Error(err))
		return nil, 0, err
	}

	// Convert to ogen API format
	apiViolations := make([]api.SLAViolation, len(violations))
	for i, v := range violations {
		apiViolation := api.SLAViolation{
			TicketID:     v.TicketID,
			TicketNumber: v.TicketNumber,
			Priority:     api.SLAViolationPriority(v.Priority),
			ViolationType: api.SLAViolationViolationType(v.ViolationType),
			TargetTime:   api.NewOptDateTime(v.TargetTime),
			ViolationDuration: api.NewOptInt(v.ViolationDuration),
		}

		if v.ActualTime != nil {
			apiViolation.ActualTime = api.NewOptDateTime(*v.ActualTime)
		}

		apiViolations[i] = apiViolation
	}

	return apiViolations, total, nil
}



// Issue: #1489 - Support SLA Service: ogen handlers implementation
