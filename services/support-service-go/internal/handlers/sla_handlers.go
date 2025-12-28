// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: SLA handlers with optimized request processing and error handling

package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"support-service-go/internal/models"
	"support-service-go/internal/service"
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

// GetTicketSLA handles GET /support/tickets/{ticket_id}/sla
// Returns SLA status for a specific ticket
func (h *SLAHandlers) GetTicketSLA(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		h.logger.Info("GetTicketSLA request completed",
			zap.Duration("duration", time.Since(start)))
	}()

	// Extract ticket ID from URL
	vars := mux.Vars(r)
	ticketIDStr := vars["ticket_id"]

	// Parse UUID
	ticketID, err := uuid.Parse(ticketIDStr)
	if err != nil {
		h.logger.Error("Invalid ticket ID format",
			zap.String("ticket_id", ticketIDStr),
			zap.Error(err))
		h.writeError(w, http.StatusBadRequest, "INVALID_TICKET_ID", "Invalid ticket ID format")
		return
	}

	// Get SLA status from service
	ctx := r.Context()
	slaStatus, err := h.slaService.GetSLAStatus(ctx, ticketID)
	if err != nil {
		h.logger.Error("Failed to get SLA status",
			zap.String("ticket_id", ticketID.String()),
			zap.Error(err))
		h.writeError(w, http.StatusNotFound, "SLA_STATUS_NOT_FOUND", "SLA status not found for ticket")
		return
	}

	// Convert to API response format
	response := h.convertToAPITicketSLAStatus(slaStatus)

	// Return response
	h.writeJSON(w, http.StatusOK, response)
}

// GetSLAViolations handles GET /support/sla/violations
// Returns paginated list of SLA violations with optional filtering
func (h *SLAHandlers) GetSLAViolations(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		h.logger.Info("GetSLAViolations request completed",
			zap.Duration("duration", time.Since(start)))
	}()

	// Parse query parameters
	limit := 20 // default
	offset := 0 // default

	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	// Optional filters
	var priority, violationType *string

	if priorityStr := r.URL.Query().Get("priority"); priorityStr != "" {
		priority = &priorityStr
	}

	if violationTypeStr := r.URL.Query().Get("violation_type"); violationTypeStr != "" {
		violationType = &violationTypeStr
	}

	// Get violations from service
	ctx := r.Context()
	violations, total, err := h.slaService.GetSLAViolations(ctx, limit, offset, priority, violationType)
	if err != nil {
		h.logger.Error("Failed to get SLA violations", zap.Error(err))
		h.writeError(w, http.StatusInternalServerError, "INTERNAL_ERROR", "Failed to retrieve SLA violations")
		return
	}

	// Convert to API response format
	apiViolations := make([]SLAViolation, len(violations))
	for i, v := range violations {
		apiViolations[i] = h.convertToAPISLAViolation(v)
	}

	response := SLAViolationsResponse{
		Items:  apiViolations,
		Total:  total,
		Limit:  limit,
		Offset: offset,
	}

	// Return response
	h.writeJSON(w, http.StatusOK, response)
}

// Helper methods

// convertToAPITicketSLAStatus converts internal SLA status to API format
func (h *SLAHandlers) convertToAPITicketSLAStatus(status *models.SLAStatus) TicketSLAStatus {
	return TicketSLAStatus{
		TicketID:               status.TicketID,
		Priority:               status.Priority,
		FirstResponseTarget:    status.FirstResponseTarget,
		FirstResponseActual:    status.FirstResponseActual,
		ResolutionTarget:       status.ResolutionTarget,
		ResolutionActual:       status.ResolutionActual,
		FirstResponseSLAMet:    status.FirstResponseSLAMet,
		ResolutionSLAMet:       status.ResolutionSLAMet,
		TimeUntilFirstResponse: status.TimeUntilFirstResponse,
		TimeUntilResolution:    status.TimeUntilResolution,
	}
}

// convertToAPISLAViolation converts internal SLA violation to API format
func (h *SLAHandlers) convertToAPISLAViolation(violation models.SLAViolation) SLAViolation {
	return SLAViolation{
		TicketID:          violation.TicketID,
		TicketNumber:      violation.TicketNumber,
		Priority:          violation.Priority,
		ViolationType:     violation.ViolationType,
		TargetTime:        violation.TargetTime,
		ActualTime:        violation.ActualTime,
		ViolationDuration: violation.ViolationDuration,
	}
}

// writeJSON writes JSON response
func (h *SLAHandlers) writeJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

// writeError writes error response
func (h *SLAHandlers) writeError(w http.ResponseWriter, status int, code, message string) {
	errorResponse := map[string]interface{}{
		"error": map[string]string{
			"code":    code,
			"message": message,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(errorResponse); err != nil {
		h.logger.Error("Failed to encode error response", zap.Error(err))
	}
}

// Issue: #1489 - Support SLA Service: ogen handlers implementation
