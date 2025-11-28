package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-futures-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *FuturesService
	logger  *logrus.Logger
}

func NewHandlers(service *FuturesService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) ListFuturesContracts(w http.ResponseWriter, r *http.Request, params api.ListFuturesContractsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordFuturesRequestDuration(r.Method, "/api/v1/futures/contracts", duration)
	}()

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	contracts, total, err := h.service.ListFuturesContracts(r.Context(), params.Underlying, params.ExpirationFrom, params.ExpirationTo, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list futures contracts")
		RecordError("list_futures_contracts_error")
		RecordFuturesRequest(r.Method, "/api/v1/futures/contracts", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to list futures contracts", err.Error())
		return
	}

	response := map[string]interface{}{
		"contracts": contracts,
		"pagination": map[string]interface{}{
			"total":  total,
			"limit":  limit,
			"offset": offset,
		},
	}

	RecordFuturesRequest(r.Method, "/api/v1/futures/contracts", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) OpenFuturesPosition(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordFuturesRequestDuration(r.Method, "/api/v1/futures/open", duration)
	}()

	var request api.OpenFuturesRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordPositionRequest(r.Method, "/api/v1/futures/open", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	contractUUID := uuid.UUID(request.ContractId)
	position, err := h.service.OpenFuturesPosition(r.Context(), playerID, contractUUID, request.Quantity)
	if err != nil {
		h.logger.WithError(err).Error("Failed to open futures position")
		RecordError("open_futures_position_error")
		RecordPositionRequest(r.Method, "/api/v1/futures/open", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to open futures position", err.Error())
		return
	}

	RecordPositionRequest(r.Method, "/api/v1/futures/open", "201")
	writeJSONResponse(w, http.StatusCreated, position)
}

func (h *Handlers) ListFuturesPositions(w http.ResponseWriter, r *http.Request, params api.ListFuturesPositionsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordFuturesRequestDuration(r.Method, "/api/v1/futures/positions", duration)
	}()

	activeOnly := true
	if params.ActiveOnly != nil {
		activeOnly = *params.ActiveOnly
	}

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	playerID := uuid.New()
	positions, total, err := h.service.ListFuturesPositions(r.Context(), playerID, activeOnly, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list futures positions")
		RecordError("list_futures_positions_error")
		RecordPositionRequest(r.Method, "/api/v1/futures/positions", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to list futures positions", err.Error())
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

	RecordPositionRequest(r.Method, "/api/v1/futures/positions", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) CloseFuturesPosition(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordFuturesRequestDuration(r.Method, "/api/v1/futures/{position_id}/close", duration)
	}()

	positionUUID := uuid.UUID(positionId)
	response, err := h.service.CloseFuturesPosition(r.Context(), positionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to close futures position")
		RecordError("close_futures_position_error")
		RecordPositionRequest(r.Method, "/api/v1/futures/{position_id}/close", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to close futures position", err.Error())
		return
	}

	RecordPositionRequest(r.Method, "/api/v1/futures/{position_id}/close", "200")
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
