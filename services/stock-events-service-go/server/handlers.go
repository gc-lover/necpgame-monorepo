package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-events-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *EventsService
	logger  *logrus.Logger
}

func NewHandlers(service *EventsService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) GetStockEventImpacts(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID, params api.GetStockEventImpactsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordEventRequestDuration(r.Method, "/stocks/{stock_id}/events/impacts", duration)
	}()

	var status *string
	if params.Status != nil {
		s := string(*params.Status)
		status = &s
	}

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	stockUUID := uuid.UUID(stockId)
	impacts, total, err := h.service.GetStockEventImpacts(r.Context(), stockUUID, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get stock event impacts")
		RecordError("get_stock_event_impacts_error")
		RecordImpactRequest(r.Method, "/stocks/{stock_id}/events/impacts", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get stock event impacts", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": impacts,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
		"summary": map[string]interface{}{
			"total_active_impacts":     len(impacts),
			"combined_effect_percent":  0.0,
			"strongest_positive":       0.0,
			"strongest_negative":       0.0,
		},
	}

	RecordImpactRequest(r.Method, "/stocks/{stock_id}/events/impacts", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetStockEventHistory(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID, params api.GetStockEventHistoryParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordEventRequestDuration(r.Method, "/stocks/{stock_id}/events/history", duration)
	}()

	var eventType *string
	if params.EventType != nil {
		s := string(*params.EventType)
		eventType = &s
	}

	var fromDate, toDate *time.Time
	if params.FromDate != nil {
		fromDate = params.FromDate
	}
	if params.ToDate != nil {
		toDate = params.ToDate
	}

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	stockUUID := uuid.UUID(stockId)
	history, total, err := h.service.GetStockEventHistory(r.Context(), stockUUID, eventType, fromDate, toDate, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get stock event history")
		RecordError("get_stock_event_history_error")
		RecordEventRequest(r.Method, "/stocks/{stock_id}/events/history", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get stock event history", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": history,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordEventRequest(r.Method, "/stocks/{stock_id}/events/history", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetAllActiveImpacts(w http.ResponseWriter, r *http.Request, params api.GetAllActiveImpactsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordEventRequestDuration(r.Method, "/events/impacts/active", duration)
	}()

	var eventType *string
	if params.EventType != nil {
		s := string(*params.EventType)
		eventType = &s
	}

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	impacts, total, err := h.service.GetAllActiveImpacts(r.Context(), eventType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get all active impacts")
		RecordError("get_all_active_impacts_error")
		RecordImpactRequest(r.Method, "/events/impacts/active", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get all active impacts", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": impacts,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordImpactRequest(r.Method, "/events/impacts/active", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) ApplyEventImpact(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordEventRequestDuration(r.Method, "/admin/stocks/events/apply", duration)
	}()

	var request api.EventApplicationRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordEventRequest(r.Method, "/admin/stocks/events/apply", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	impact, err := h.service.ApplyEventImpact(r.Context(), &request)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply event impact")
		RecordError("apply_event_impact_error")
		RecordEventRequest(r.Method, "/admin/stocks/events/apply", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to apply event impact", err.Error())
		return
	}

	RecordEventRequest(r.Method, "/admin/stocks/events/apply", "201")
	writeJSONResponse(w, http.StatusCreated, impact)
}

func (h *Handlers) SimulateEventImpact(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordEventRequestDuration(r.Method, "/admin/stocks/events/simulate", duration)
	}()

	var request api.EventSimulationRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordEventRequest(r.Method, "/admin/stocks/events/simulate", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	simulation, err := h.service.SimulateEventImpact(r.Context(), &request)
	if err != nil {
		h.logger.WithError(err).Error("Failed to simulate event impact")
		RecordError("simulate_event_impact_error")
		RecordEventRequest(r.Method, "/admin/stocks/events/simulate", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to simulate event impact", err.Error())
		return
	}

	RecordEventRequest(r.Method, "/admin/stocks/events/simulate", "200")
	writeJSONResponse(w, http.StatusOK, simulation)
}

func (h *Handlers) ReverseEventImpact(w http.ResponseWriter, r *http.Request, impactId openapi_types.UUID) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordEventRequestDuration(r.Method, "/admin/stocks/events/{impact_id}/reverse", duration)
	}()

	impactUUID := uuid.UUID(impactId)
	if err := h.service.ReverseEventImpact(r.Context(), impactUUID); err != nil {
		h.logger.WithError(err).Error("Failed to reverse event impact")
		RecordError("reverse_event_impact_error")
		RecordEventRequest(r.Method, "/admin/stocks/events/{impact_id}/reverse", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to reverse event impact", err.Error())
		return
	}

	RecordEventRequest(r.Method, "/admin/stocks/events/{impact_id}/reverse", "200")
	w.WriteHeader(http.StatusOK)
}

func writeJSONResponse(w http.ResponseWriter, statusCode int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		http.Error(w, "Failed to encode response", http.StatusInternalServerError)
	}
}

func writeErrorResponse(w http.ResponseWriter, statusCode int, message, details string) {
	errorResponse := api.Error{
		Error:   http.StatusText(statusCode),
		Message: message,
		Details: &map[string]interface{}{
			"details": details,
		},
	}
	writeJSONResponse(w, statusCode, errorResponse)
}

