package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-indices-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type IndicesHandlers struct {
	logger *logrus.Logger
}

func NewIndicesHandlers() *IndicesHandlers {
	return &IndicesHandlers{
		logger: GetLogger(),
	}
}

func (h *IndicesHandlers) GetAllIndices(w http.ResponseWriter, r *http.Request, params api.GetAllIndicesParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"type": params.Type,
	}).Info("GetAllIndices request")

	indices := []api.StockIndex{}
	response := indices

	h.respondJSON(w, http.StatusOK, response)
}

func (h *IndicesHandlers) GetIndex(w http.ResponseWriter, r *http.Request, code string) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("code", code).Info("GetIndex request")

	response := api.StockIndexDetailed{
		Code: code,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *IndicesHandlers) GetIndexHistory(w http.ResponseWriter, r *http.Request, code string, params api.GetIndexHistoryParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"code":     code,
		"from_date": params.FromDate,
		"to_date":   params.ToDate,
		"interval":  params.Interval,
		"limit":     params.Limit,
	}).Info("GetIndexHistory request")

	history := []api.IndexHistoryEntry{}
	response := history

	h.respondJSON(w, http.StatusOK, response)
}

func (h *IndicesHandlers) GetIndexConstituents(w http.ResponseWriter, r *http.Request, code string, params api.GetIndexConstituentsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"code":    code,
		"sort_by": params.SortBy,
		"order":   params.Order,
		"limit":   params.Limit,
		"offset":  params.Offset,
	}).Info("GetIndexConstituents request")

	constituents := []api.IndexConstituent{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetIndexConstituentsResponse struct {
		Data       []api.IndexConstituent    `json:"data"`
		Pagination *api.PaginationResponse   `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetIndexConstituentsResponse{
		Data:       constituents,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *IndicesHandlers) RebalanceIndex(w http.ResponseWriter, r *http.Request, code string) {
	ctx := r.Context()
	_ = ctx

	var req api.RebalanceIndexJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode RebalanceIndex request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"code":          code,
		"effective_date": req.EffectiveDate,
	}).Info("RebalanceIndex request")

	now := time.Now()
	effectiveDate := now
	if req.EffectiveDate != nil {
		effectiveDate = *req.EffectiveDate
	}

	previousValue := 0.0
	newValue := 0.0
	divisorAdjustment := 0.0
	totalChanges := 0
	addedStocks := []struct {
		StockSymbol *string  `json:"stock_symbol,omitempty"`
		Weight      *float64 `json:"weight,omitempty"`
	}{}
	removedStocks := []struct {
		StockSymbol *string  `json:"stock_symbol,omitempty"`
		Weight      *float64 `json:"weight,omitempty"`
	}{}
	weightChanges := []struct {
		Change      *float64 `json:"change,omitempty"`
		NewWeight   *float64 `json:"new_weight,omitempty"`
		OldWeight   *float64 `json:"old_weight,omitempty"`
		StockSymbol *string  `json:"stock_symbol,omitempty"`
	}{}

	response := api.RebalanceResult{
		IndexCode:        &code,
		ExecutedAt:       &now,
		EffectiveDate:    &effectiveDate,
		PreviousValue:    &previousValue,
		NewValue:         &newValue,
		DivisorAdjustment: &divisorAdjustment,
		TotalChanges:     &totalChanges,
		AddedStocks:      &addedStocks,
		RemovedStocks:    &removedStocks,
		WeightChanges:    &weightChanges,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *IndicesHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *IndicesHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

