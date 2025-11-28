package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-options-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Handlers struct {
	service *OptionsService
	logger  *logrus.Logger
}

func NewHandlers(service *OptionsService, logger *logrus.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

func (h *Handlers) ListOptionsContracts(w http.ResponseWriter, r *http.Request, params api.ListOptionsContractsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordOptionsRequestDuration(r.Method, "/api/v1/options/contracts", duration)
	}()

	var contractType *string
	if params.Type != nil {
		s := string(*params.Type)
		contractType = &s
	}

	limit := 10
	if params.Limit != nil {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	contracts, total, err := h.service.ListOptionsContracts(r.Context(), params.Ticker, contractType, params.ExpirationFrom, params.ExpirationTo, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list options contracts")
		RecordError("list_options_contracts_error")
		RecordOptionsRequest(r.Method, "/api/v1/options/contracts", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to list options contracts", err.Error())
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

	RecordOptionsRequest(r.Method, "/api/v1/options/contracts", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) BuyOptionsContract(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordOptionsRequestDuration(r.Method, "/api/v1/options/buy", duration)
	}()

	var request api.BuyOptionsRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordPositionRequest(r.Method, "/api/v1/options/buy", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	playerID := uuid.New()
	contractUUID := uuid.UUID(request.ContractId)
	position, err := h.service.BuyOptionsContract(r.Context(), playerID, contractUUID, request.Quantity)
	if err != nil {
		h.logger.WithError(err).Error("Failed to buy options contract")
		RecordError("buy_options_contract_error")
		RecordPositionRequest(r.Method, "/api/v1/options/buy", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to buy options contract", err.Error())
		return
	}

	RecordPositionRequest(r.Method, "/api/v1/options/buy", "201")
	writeJSONResponse(w, http.StatusCreated, position)
}

func (h *Handlers) ListOptionsPositions(w http.ResponseWriter, r *http.Request, params api.ListOptionsPositionsParams) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordOptionsRequestDuration(r.Method, "/api/v1/options/positions", duration)
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
	positions, total, err := h.service.ListOptionsPositions(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list options positions")
		RecordError("list_options_positions_error")
		RecordPositionRequest(r.Method, "/api/v1/options/positions", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to list options positions", err.Error())
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

	RecordPositionRequest(r.Method, "/api/v1/options/positions", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) ExerciseOption(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordOptionsRequestDuration(r.Method, "/api/v1/options/exercise", duration)
	}()

	var request api.ExerciseOptionRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.logger.WithError(err).Error("Failed to decode request")
		RecordError("decode_error")
		RecordPositionRequest(r.Method, "/api/v1/options/exercise", "400")
		writeErrorResponse(w, http.StatusBadRequest, "Invalid request body", err.Error())
		return
	}

	positionUUID := uuid.UUID(request.PositionId)
	response, err := h.service.ExerciseOption(r.Context(), positionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to exercise option")
		RecordError("exercise_option_error")
		RecordPositionRequest(r.Method, "/api/v1/options/exercise", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to exercise option", err.Error())
		return
	}

	RecordPositionRequest(r.Method, "/api/v1/options/exercise", "200")
	writeJSONResponse(w, http.StatusOK, response)
}

func (h *Handlers) GetOptionsGreeks(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	start := time.Now()
	defer func() {
		duration := time.Since(start).Seconds()
		RecordOptionsRequestDuration(r.Method, "/api/v1/options/greeks/{position_id}", duration)
	}()

	positionUUID := uuid.UUID(positionId)
	greeks, err := h.service.GetOptionsGreeks(r.Context(), positionUUID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get options Greeks")
		RecordError("get_options_greeks_error")
		RecordOptionsRequest(r.Method, "/api/v1/options/greeks/{position_id}", "500")
		writeErrorResponse(w, http.StatusInternalServerError, "Failed to get options Greeks", err.Error())
		return
	}

	RecordOptionsRequest(r.Method, "/api/v1/options/greeks/{position_id}", "200")
	writeJSONResponse(w, http.StatusOK, greeks)
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

