package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-margin-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *MarginService
	logger  *logrus.Logger
}

func NewHandlers(service *MarginService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) GetMarginAccount(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/margin/account", duration)
	}()

	playerID := uuid.New()
	account, err := h.service.GetMarginAccount(r.Context(), playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get margin account")
		RecordError("get_margin_account_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/account", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get margin account", err.Error())
		return
	}

	if account == nil {
		RecordMarginRequest(r.Method, "/api/v1/margin/account", "404")
		writeErrorResponse(w, http.StatusNotFound, "Margin account not found", "")
		return
	}

	RecordMarginRequest(r.Method, "/api/v1/margin/account", "200")
	writeJSONResponse(w, http.StatusOK, account)
}

func (h *Handlers) OpenMarginAccount(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/margin/account", duration)
	}()

	var request api.OpenMarginAccountRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/account", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	account, err := h.service.OpenMarginAccount(r.Context(), playerID, request.InitialDeposit)
	if err != nil {
		h.logger.WithError(err).Error("Failed to open margin account")
		RecordError("open_margin_account_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/account", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to open margin account", err.Error())
		return
	}

	RecordMarginRequest(r.Method, "/api/v1/margin/account", "201")
	writeJSONResponse(w, http.StatusCreated, account)
}

func (h *Handlers) BorrowMargin(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/margin/borrow", duration)
	}()

	var request api.BorrowMarginRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/borrow", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	response, err := h.service.BorrowMargin(r.Context(), playerID, request.Amount)
	if err != nil {
		h.logger.WithError(err).Error("Failed to borrow margin")
		RecordError("borrow_margin_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/borrow", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to borrow margin", err.Error())
		return
	}

	RecordMarginRequest(r.Method, "/api/v1/margin/borrow", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) RepayMargin(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/margin/repay", duration)
	}()

	var request api.RepayMarginRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/repay", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	if err := h.service.RepayMargin(r.Context(), playerID, request.Amount); err != nil {
		h.logger.WithError(err).Error("Failed to repay margin")
		RecordError("repay_margin_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/repay", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to repay margin", err.Error())
		return
	}

	RecordMarginRequest(r.Method, "/api/v1/margin/repay", "200")
	w.WriteHeader(http.StatusOK)
}

func (h *Handlers) GetMarginCallHistory(w http.ResponseWriter, r *http.Request, params api.GetMarginCallHistoryParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/margin/history", duration)
	}()

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	playerID := uuid.New()
	calls, total, err := h.service.GetMarginCallHistory(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get margin call history")
		RecordError("get_margin_call_history_error")
		RecordMarginRequest(r.Method, "/api/v1/margin/history", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get margin call history", err.Error())
		return
	}

	response := map[string]interface{}{
		"margin_calls": calls,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordMarginRequest(r.Method, "/api/v1/margin/history", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetRiskHealth(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/risk/health", duration)
	}()

	playerID := uuid.New()
	health, err := h.service.GetRiskHealth(r.Context(), playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get risk health")
		RecordError("get_risk_health_error")
		RecordMarginRequest(r.Method, "/api/v1/risk/health", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get risk health", err.Error())
		return
	}

	RecordMarginRequest(r.Method, "/api/v1/risk/health", "200")
	writeJSONResponse(w, http.StatusOK, health)
}

func (h *Handlers) ListShortPositions(w http.ResponseWriter, r *http.Request, params api.ListShortPositionsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/short", duration)
	}()

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	playerID := uuid.New()
	positions, total, err := h.service.ListShortPositions(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list short positions")
		RecordError("list_short_positions_error")
		RecordShortRequest(r.Method, "/api/v1/short", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to list short positions", err.Error())
		return
	}

	response := map[string]interface{}{
		"positions": positions,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordShortRequest(r.Method, "/api/v1/short", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) OpenShortPosition(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/short", duration)
	}()

	var request api.ShortPositionRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordShortRequest(r.Method, "/api/v1/short", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	position, err := h.service.OpenShortPosition(r.Context(), playerID, &request)
	if err != nil {
		h.logger.WithError(err).Error("Failed to open short position")
		RecordError("open_short_position_error")
		RecordShortRequest(r.Method, "/api/v1/short", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to open short position", err.Error())
		return
	}

	RecordShortRequest(r.Method, "/api/v1/short", "201")
	writeJSONResponse(w, http.StatusCreated, position)
}

func (h *Handlers) GetShortPosition(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/short/{position_id}", duration)
	}()

	positionUUID := uuid.UUID(positionId)
	position, err := h.service.GetShortPosition(r.Context(), positionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get short position")
		RecordError("get_short_position_error")
		RecordShortRequest(r.Method, "/api/v1/short/{position_id}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get short position", err.Error())
		return
	}

	if position == nil {
		RecordShortRequest(r.Method, "/api/v1/short/{position_id}", "404")
		writeErrorResponse(w, http.StatusNotFound, "Short position not found", "")
		return
	}

	RecordShortRequest(r.Method, "/api/v1/short/{position_id}", "200")
	writeJSONResponse(w, http.StatusOK, position)
}

func (h *Handlers) CloseShortPosition(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordMarginRequestDuration(r.Method, "/api/v1/short/{position_id}/close", duration)
	}()

	positionUUID := uuid.UUID(positionId)
	response, err := h.service.CloseShortPosition(r.Context(), positionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to close short position")
		RecordError("close_short_position_error")
		RecordShortRequest(r.Method, "/api/v1/short/{position_id}/close", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to close short position", err.Error())
		return
	}

	RecordShortRequest(r.Method, "/api/v1/short/{position_id}/close", "200")
	writeJSONResponse(w, http.StatusOK, response)
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

