package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-protection-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type ProtectionHandlers struct {
	logger *logrus.Logger
}

func NewProtectionHandlers() *ProtectionHandlers {
	return &ProtectionHandlers{
		logger: GetLogger(),
	}
}

func (h *ProtectionHandlers) GetCircuitBreakerStatus(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("stock_id", stockId).Info("GetCircuitBreakerStatus request")

	status := api.Active
	response := api.CircuitBreakerStatus{
		StockId:     stockId,
		StockSymbol: "",
		Status:      status,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) GetSurveillanceAlerts(w http.ResponseWriter, r *http.Request, params api.GetSurveillanceAlertsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"alert_type": params.AlertType,
		"severity":   params.Severity,
		"status":     params.Status,
		"stock_id":   params.StockId,
		"limit":      params.Limit,
		"offset":     params.Offset,
	}).Info("GetSurveillanceAlerts request")

	alerts := []api.SurveillanceAlert{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetSurveillanceAlertsResponse struct {
		Data       []api.SurveillanceAlert   `json:"data"`
		Pagination *api.PaginationResponse  `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetSurveillanceAlertsResponse{
		Data:       alerts,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) GetAlertDetails(w http.ResponseWriter, r *http.Request, alertId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("alert_id", alertId).Info("GetAlertDetails request")

	response := api.SurveillanceAlertDetailed{
		Id:          alertId,
		TriggeredAt: time.Now(),
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) UpdateAlertStatus(w http.ResponseWriter, r *http.Request, alertId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	var req api.UpdateAlertStatusJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode UpdateAlertStatus request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"alert_id": alertId,
		"status":   req.Status,
		"notes":    req.Notes,
	}).Info("UpdateAlertStatus request")

	now := time.Now()
	response := api.SurveillanceAlertDetailed{
		Id:          alertId,
		Status:      api.SurveillanceAlertDetailedStatus(req.Status),
		Notes:       req.Notes,
		ReviewedAt:  &now,
		TriggeredAt: time.Now(),
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) GetEnforcementActions(w http.ResponseWriter, r *http.Request, params api.GetEnforcementActionsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"action_type": params.ActionType,
		"player_id":   params.PlayerId,
		"from_date":   params.FromDate,
		"to_date":     params.ToDate,
		"limit":       params.Limit,
		"offset":      params.Offset,
	}).Info("GetEnforcementActions request")

	actions := []api.EnforcementAction{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetEnforcementActionsResponse struct {
		Data       []api.EnforcementAction   `json:"data"`
		Pagination *api.PaginationResponse  `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetEnforcementActionsResponse{
		Data:       actions,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) CreateEnforcementAction(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.CreateEnforcementActionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode CreateEnforcementAction request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"player_id":    req.PlayerId,
		"action_type":  req.ActionType,
		"reason":       req.Reason,
		"fine_amount":  req.FineAmount,
		"duration_hours": req.DurationHours,
	}).Info("CreateEnforcementAction request")

	now := time.Now()
	actionId := openapi_types.UUID{}
	response := api.EnforcementAction{
		Id:          actionId,
		PlayerId:    req.PlayerId,
		ActionType:  api.EnforcementActionActionType(req.ActionType),
		Reason:      req.Reason,
		AppliedAt:   now,
		AlertId:     req.AlertId,
		FineAmount:  req.FineAmount,
		DurationHours: req.DurationHours,
		ConfiscatedAssets: nil,
		ExpiresAt:   nil,
		AppealStatus: nil,
		AppliedBy:   nil,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *ProtectionHandlers) TriggerCircuitBreaker(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.TriggerCircuitBreakerJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode TriggerCircuitBreaker request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"stock_id":         req.StockId,
		"duration_minutes": req.DurationMinutes,
		"reason":           req.Reason,
	}).Info("TriggerCircuitBreaker request")

	now := time.Now()
	resumeAt := now.Add(time.Duration(req.DurationMinutes) * time.Minute)
	status := api.Halted
	reason := api.Manual
	response := api.CircuitBreakerStatus{
		StockId:             req.StockId,
		StockSymbol:         "",
		Status:              status,
		Reason:              &reason,
		TriggeredAt:         &now,
		EstimatedResumeAt:   &resumeAt,
		HaltDurationMinutes: &req.DurationMinutes,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) ResumeTrading(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.ResumeTradingJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ResumeTrading request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"stock_id": req.StockId,
		"reason":   req.Reason,
	}).Info("ResumeTrading request")

	status := api.Active
	response := api.CircuitBreakerStatus{
		StockId:     req.StockId,
		StockSymbol: "",
		Status:      status,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ProtectionHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *ProtectionHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

