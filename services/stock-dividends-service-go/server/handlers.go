package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-dividends-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *DividendsService
	logger  *logrus.Logger
}

func NewHandlers(service *DividendsService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) GetDividendSchedule(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID, params api.GetDividendScheduleParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, "/stocks/{stock_id}/dividends/schedule", duration)
	}()

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	stockUUID := uuid.UUID(stockId)
	schedules, total, err := h.service.GetDividendSchedule(r.Context(), stockUUID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get dividend schedule")
		RecordError("get_dividend_schedule_error")
		RecordDividendRequest(r.Method, "/stocks/{stock_id}/dividends/schedule", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get dividend schedule", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": schedules,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordDividendRequest(r.Method, "/stocks/{stock_id}/dividends/schedule", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetPlayerDividendPayments(w http.ResponseWriter, r *http.Request, playerId api.PlayerId, params api.GetPlayerDividendPaymentsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, "/players/{player_id}/dividends/payments", duration)
	}()

	playerUUID := uuid.UUID(playerId)
	var stockID *uuid.UUID
	if params.StockId != nil {
		id := uuid.UUID(*params.StockId)
		stockID = &id
	}

	var status *string
	if params.Status != nil {
		s := string(*params.Status)
		status = &s
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

	payments, total, err := h.service.GetPlayerDividendPayments(r.Context(), playerUUID, stockID, status, fromDate, toDate, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player dividend payments")
		RecordError("get_player_dividend_payments_error")
		RecordDividendRequest(r.Method, "/players/{player_id}/dividends/payments", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get player dividend payments", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": payments,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
		"summary": map[string]interface{}{
			"total_gross": 0.0,
			"total_tax":   0.0,
			"total_net":   0.0,
		},
	}

	RecordDividendRequest(r.Method, "/players/{player_id}/dividends/payments", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetPlayerDRIPSettings(w http.ResponseWriter, r *http.Request, playerId api.PlayerId) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, "/players/{player_id}/dividends/drip", duration)
		RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "200")
	}()

	playerUUID := uuid.UUID(playerId)
	settings, err := h.service.GetPlayerDRIPSettings(r.Context(), playerUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player DRIP settings")
		RecordError("get_player_drip_settings_error")
		RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get player DRIP settings", err.Error())
		return
	}

	RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "200")
	writeJSONResponse(w, http.StatusOK, settings)
}

func (h *Handlers) UpdatePlayerDRIPSettings(w http.ResponseWriter, r *http.Request, playerId api.PlayerId) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, "/players/{player_id}/dividends/drip", duration)
		RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "200")
	}()

	var update api.DRIPSettingsUpdate
	if err := json.NewDecoder(r.Body).Decode(&update); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerUUID := uuid.UUID(playerId)
	settings, err := h.service.UpdatePlayerDRIPSettings(r.Context(), playerUUID, &update)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update player DRIP settings")
		RecordError("update_player_drip_settings_error")
		RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to update player DRIP settings", err.Error())
		return
	}

	RecordDRIPRequest(r.Method, "/players/{player_id}/dividends/drip", "200")
	writeJSONResponse(w, http.StatusOK, settings)
}

func (h *Handlers) CreateDividendSchedule(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, "/admin/dividends/schedules", duration)
	}()

	var create api.DividendScheduleCreate
	if err := json.NewDecoder(r.Body).Decode(&create); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordDividendRequest(r.Method, "/admin/dividends/schedules", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	schedule, err := h.service.CreateDividendSchedule(r.Context(), &create)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create dividend schedule")
		RecordError("create_dividend_schedule_error")
		RecordDividendRequest(r.Method, "/admin/dividends/schedules", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to create dividend schedule", err.Error())
		return
	}

	RecordDividendRequest(r.Method, "/admin/dividends/schedules", "201")
	writeJSONResponse(w, http.StatusCreated, schedule)
}

func (h *Handlers) ProcessDividendPayment(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordDividendRequestDuration(r.Method, "/admin/dividends/process", duration)
	}()

	var request api.ProcessDividendPaymentJSONBody
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordDividendRequest(r.Method, "/admin/dividends/process", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	scheduleUUID := uuid.UUID(request.ScheduleId)
	if err := h.service.ProcessDividendPayment(r.Context(), scheduleUUID); err != nil {
		h.logger.WithError(err).Error("Failed to process dividend payment")
		RecordError("process_dividend_payment_error")
		RecordDividendRequest(r.Method, "/admin/dividends/process", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to process dividend payment", err.Error())
		return
	}

	RecordDividendRequest(r.Method, "/admin/dividends/process", "200")
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

