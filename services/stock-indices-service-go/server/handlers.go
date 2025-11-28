package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-indices-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *IndicesService
	logger  *logrus.Logger
}

func NewHandlers(service *IndicesService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) GetAllIndices(w http.ResponseWriter, r *http.Request, params api.GetAllIndicesParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordIndexRequestDuration(r.Method, "/stocks/indices", duration)
	}()

	var indexType *string
	if params.Type != nil {
		s := string(*params.Type)
		indexType = &s
	}

	indices, err := h.service.GetAllIndices(r.Context(), indexType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get all indices")
		RecordError("get_all_indices_error")
		RecordIndexRequest(r.Method, "/stocks/indices", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get all indices", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": indices,
	}

	RecordIndexRequest(r.Method, "/stocks/indices", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetIndex(w http.ResponseWriter, r *http.Request, code string) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordIndexRequestDuration(r.Method, "/stocks/indices/{code}", duration)
	}()

	index, err := h.service.GetIndex(r.Context(), code)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get index")
		RecordError("get_index_error")
		RecordIndexRequest(r.Method, "/stocks/indices/{code}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get index", err.Error())
		return
	}

	if index == nil {
		RecordIndexRequest(r.Method, "/stocks/indices/{code}", "404")
		writeErrorResponse(w, http.StatusNotFound, "Index not found", "")
		return
	}

	RecordIndexRequest(r.Method, "/stocks/indices/{code}", "200")
	writeJSONResponse(w, http.StatusOK, index)
}

func (h *Handlers) GetIndexConstituents(w http.ResponseWriter, r *http.Request, code string, params api.GetIndexConstituentsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordIndexRequestDuration(r.Method, "/stocks/indices/{code}/constituents", duration)
	}()

	var sortBy *string
	if params.SortBy != nil {
		s := string(*params.SortBy)
		sortBy = &s
	}

	var order *string
	if params.Order != nil {
		s := string(*params.Order)
		order = &s
	}

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	constituents, total, err := h.service.GetIndexConstituents(r.Context(), code, sortBy, order, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get index constituents")
		RecordError("get_index_constituents_error")
		RecordConstituentRequest(r.Method, "/stocks/indices/{code}/constituents", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get index constituents", err.Error())
		return
	}

	response := map[string]interface{}{
		"data": constituents,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordConstituentRequest(r.Method, "/stocks/indices/{code}/constituents", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetIndexHistory(w http.ResponseWriter, r *http.Request, code string, params api.GetIndexHistoryParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordIndexRequestDuration(r.Method, "/stocks/indices/{code}/history", duration)
	}()

	var fromDate, toDate *time.Time
	if params.FromDate != nil {
		fromDate = params.FromDate
	}
	if params.ToDate != nil {
		toDate = params.ToDate
	}

	var interval *string
	if params.Interval != nil {
		s := string(*params.Interval)
		interval = &s
	}

	limit := 100
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	history, total, err := h.service.GetIndexHistory(r.Context(), code, fromDate, toDate, interval, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get index history")
		RecordError("get_index_history_error")
		RecordIndexRequest(r.Method, "/stocks/indices/{code}/history", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get index history", err.Error())
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

	RecordIndexRequest(r.Method, "/stocks/indices/{code}/history", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) RebalanceIndex(w http.ResponseWriter, r *http.Request, code string) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordIndexRequestDuration(r.Method, "/admin/indices/{code}/rebalance", duration)
	}()

	if err := h.service.RebalanceIndex(r.Context(), code); err != nil {
		h.logger.WithError(err).Error("Failed to rebalance index")
		RecordError("rebalance_index_error")
		RecordIndexRequest(r.Method, "/admin/indices/{code}/rebalance", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to rebalance index", err.Error())
		return
	}

	result := map[string]interface{}{
		"index_code": code,
		"status":     "success",
	}

	RecordIndexRequest(r.Method, "/admin/indices/{code}/rebalance", "200")
	writeJSONResponse(w, http.StatusOK, result)
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

