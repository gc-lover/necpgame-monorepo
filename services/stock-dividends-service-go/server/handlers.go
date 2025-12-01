package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-dividends-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type DividendsHandlers struct {
	logger *logrus.Logger
}

func NewDividendsHandlers() *DividendsHandlers {
	return &DividendsHandlers{
		logger: GetLogger(),
	}
}

func (h *DividendsHandlers) GetDividendSchedule(w http.ResponseWriter, r *http.Request, stockId openapi_types.UUID, params api.GetDividendScheduleParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"stock_id": stockId,
		"limit":    params.Limit,
		"offset":   params.Offset,
	}).Info("GetDividendSchedule request")

	data := []api.DividendSchedule{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetDividendScheduleResponse struct {
		Data       []api.DividendSchedule      `json:"data"`
		Pagination *api.PaginationResponse    `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetDividendScheduleResponse{
		Data:       data,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DividendsHandlers) GetPlayerDividendPayments(w http.ResponseWriter, r *http.Request, playerId api.PlayerId, params api.GetPlayerDividendPaymentsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"player_id": playerId,
		"stock_id":  params.StockId,
		"status":    params.Status,
		"from_date": params.FromDate,
		"to_date":   params.ToDate,
		"limit":     params.Limit,
		"offset":    params.Offset,
	}).Info("GetPlayerDividendPayments request")

	data := []api.DividendPayment{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetPlayerDividendPaymentsResponse struct {
		Data       []api.DividendPayment       `json:"data"`
		Pagination *api.PaginationResponse     `json:"pagination,omitempty"`
		Summary    *api.PaymentSummary         `json:"summary,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	totalGross := 0.0
	totalNet := 0.0
	totalTax := 0.0
	summary := api.PaymentSummary{
		TotalGross:     &totalGross,
		TotalNet:       &totalNet,
		TotalTax:       &totalTax,
		TotalDripShares: nil,
	}

	response := GetPlayerDividendPaymentsResponse{
		Data:       data,
		Pagination: &pagination,
		Summary:    &summary,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DividendsHandlers) GetPlayerDRIPSettings(w http.ResponseWriter, r *http.Request, playerId api.PlayerId) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", playerId).Info("GetPlayerDRIPSettings request")

	stocks := []api.DRIPStockSettings{}
	response := api.DRIPSettings{
		PlayerId:     playerId,
		GlobalEnabled: false,
		Stocks:        stocks,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DividendsHandlers) UpdatePlayerDRIPSettings(w http.ResponseWriter, r *http.Request, playerId api.PlayerId) {
	ctx := r.Context()
	_ = ctx

	var req api.UpdatePlayerDRIPSettingsJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode UpdatePlayerDRIPSettings request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"player_id":     playerId,
		"global_enabled": req.GlobalEnabled,
		"stocks_count":   len(req.Stocks),
	}).Info("UpdatePlayerDRIPSettings request")

	stocks := []api.DRIPStockSettings{}
	for _, stock := range req.Stocks {
		stocks = append(stocks, api.DRIPStockSettings{
			StockId:     stock.StockId,
			Enabled:     stock.Enabled,
			Threshold:   stock.Threshold,
			StockSymbol: "",
		})
	}

	now := time.Now()
	response := api.DRIPSettings{
		PlayerId:     playerId,
		GlobalEnabled: req.GlobalEnabled,
		Stocks:        stocks,
		CreatedAt:     nil,
		UpdatedAt:     &now,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DividendsHandlers) CreateDividendSchedule(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.CreateDividendScheduleJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode CreateDividendSchedule request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"stock_id":        req.StockId,
		"amount_per_share": req.AmountPerShare,
		"frequency":       req.Frequency,
		"payment_date":    req.PaymentDate,
	}).Info("CreateDividendSchedule request")

	scheduleId := openapi_types.UUID{}
	now := time.Now()
	response := api.DividendSchedule{
		Id:              scheduleId,
		StockId:         req.StockId,
		AmountPerShare:  req.AmountPerShare,
		Frequency:       api.DividendScheduleFrequency(req.Frequency),
		DeclarationDate: req.DeclarationDate,
		ExDividendDate:  req.ExDividendDate,
		RecordDate:      req.RecordDate,
		PaymentDate:     req.PaymentDate,
		Status:          api.DividendScheduleStatusScheduled,
		StockSymbol:     "",
		CreatedAt:       &now,
		UpdatedAt:       nil,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *DividendsHandlers) ProcessDividendPayment(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.ProcessDividendPaymentJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ProcessDividendPayment request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithField("schedule_id", req.ScheduleId).Info("ProcessDividendPayment request")

	type ProcessDividendPaymentResponse struct {
		Processed int `json:"processed"`
		Failed    int `json:"failed"`
	}

	response := ProcessDividendPaymentResponse{
		Processed: 0,
		Failed:    0,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *DividendsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *DividendsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}










