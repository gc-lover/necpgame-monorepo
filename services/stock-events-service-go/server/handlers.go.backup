package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-events-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type EventsHandlers struct {
	logger *logrus.Logger
}

func NewEventsHandlers() *EventsHandlers {
	return &EventsHandlers{
		logger: GetLogger(),
	}
}

func (h *EventsHandlers) GetStockEventImpacts(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID, params api.GetStockEventImpactsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"stock_id": stockId,
		"status":   params.Status,
		"limit":    params.Limit,
		"offset":   params.Offset,
	}).Info("GetStockEventImpacts request")

	impacts := []api.EventImpact{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetStockEventImpactsResponse struct {
		Data       []api.EventImpact        `json:"data"`
		Summary    *api.ImpactSummary       `json:"summary,omitempty"`
		Pagination *api.PaginationResponse  `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	combinedEffect := 0.0
	strongestPositive := 0.0
	strongestNegative := 0.0
	totalActive := 0
	summary := api.ImpactSummary{
		CombinedEffectPercent: &combinedEffect,
		StrongestPositive:      &strongestPositive,
		StrongestNegative:      &strongestNegative,
		TotalActiveImpacts:     &totalActive,
	}

	response := GetStockEventImpactsResponse{
		Data:       impacts,
		Summary:    &summary,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EventsHandlers) GetStockEventHistory(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID, params api.GetStockEventHistoryParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"stock_id":  stockId,
		"event_type": params.EventType,
		"from_date":  params.FromDate,
		"to_date":    params.ToDate,
		"limit":      params.Limit,
		"offset":     params.Offset,
	}).Info("GetStockEventHistory request")

	history := []api.StockEventHistoryEntry{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetStockEventHistoryResponse struct {
		Data       []api.StockEventHistoryEntry `json:"data"`
		Pagination *api.PaginationResponse      `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetStockEventHistoryResponse{
		Data:       history,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EventsHandlers) GetAllActiveImpacts(w http.ResponseWriter, r *http.Request, params api.GetAllActiveImpactsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"event_type": params.EventType,
		"min_impact": params.MinImpact,
		"limit":      params.Limit,
		"offset":     params.Offset,
	}).Info("GetAllActiveImpacts request")

	impacts := []api.EventImpact{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetAllActiveImpactsResponse struct {
		Data       []api.EventImpact        `json:"data"`
		Pagination *api.PaginationResponse  `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetAllActiveImpactsResponse{
		Data:       impacts,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EventsHandlers) SimulateEventImpact(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.SimulateEventImpactJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode SimulateEventImpact request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"stock_id":           req.StockId,
		"event_type":         req.EventType,
		"base_impact_percent": req.BaseImpactPercent,
		"duration_type":      req.DurationType,
	}).Info("SimulateEventImpact request")

	currentPrice := 0.0
	projectedPrice := 0.0
	priceChange := 0.0
	totalImpactPercent := req.BaseImpactPercent
	durationHours := 168
	decayCurve := api.Linear
	calculatedModifiers := []api.EventModifier{}

	response := api.EventSimulationResult{
		StockId:            &req.StockId,
		BaseImpactPercent:  &req.BaseImpactPercent,
		TotalImpactPercent: &totalImpactPercent,
		CurrentPrice:       &currentPrice,
		ProjectedPrice:     &projectedPrice,
		PriceChange:        &priceChange,
		DurationHours:      &durationHours,
		DecayCurve:         &decayCurve,
		CalculatedModifiers: &calculatedModifiers,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EventsHandlers) ApplyEventImpact(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.ApplyEventImpactJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ApplyEventImpact request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"stock_id":           req.StockId,
		"event_id":           req.EventId,
		"event_type":         req.EventType,
		"base_impact_percent": req.BaseImpactPercent,
		"duration_type":      req.DurationType,
		"decay_curve":        req.DecayCurve,
	}).Info("ApplyEventImpact request")

	now := time.Now()
	impactId := openapi_types.UUID{}
	durationHours := 168
	currentEffectPercent := req.BaseImpactPercent
	totalImpactPercent := req.BaseImpactPercent

	response := api.EventImpact{
		Id:                  impactId,
		StockId:             req.StockId,
		EventId:             req.EventId,
		EventName:           req.EventName,
		EventType:           req.EventType,
		BaseImpactPercent:   req.BaseImpactPercent,
		TotalImpactPercent:  totalImpactPercent,
		CurrentEffectPercent: &currentEffectPercent,
		DurationType:        req.DurationType,
		DurationHours:       durationHours,
		DecayCurve:          req.DecayCurve,
		Status:              api.EventImpactStatusActive,
		AppliedAt:           now,
		StockSymbol:         "",
		Modifiers:           nil,
		ExpiresAt:           nil,
		CreatedAt:           &now,
		UpdatedAt:           nil,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *EventsHandlers) ReverseEventImpact(w http.ResponseWriter, r *http.Request, impactId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.ReverseEventImpactJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ReverseEventImpact request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"impact_id": impactId,
		"reason":    req.Reason,
	}).Info("ReverseEventImpact request")

	now := time.Now()
	response := api.EventImpact{
		Id:        impactId,
		Status:    api.EventImpactStatusReversed,
		UpdatedAt: &now,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *EventsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *EventsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

