package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-options-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type OptionsHandlers struct {
	logger *logrus.Logger
}

func NewOptionsHandlers() *OptionsHandlers {
	return &OptionsHandlers{
		logger: GetLogger(),
	}
}

func (h *OptionsHandlers) ListOptionsContracts(w http.ResponseWriter, r *http.Request, params api.ListOptionsContractsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"ticker":          params.Ticker,
		"type":            params.Type,
		"expiration_from": params.ExpirationFrom,
		"expiration_to":   params.ExpirationTo,
		"limit":           params.Limit,
		"offset":          params.Offset,
	}).Info("ListOptionsContracts request")

	contracts := []api.OptionsContract{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type ListOptionsContractsResponse struct {
		Data       []api.OptionsContract    `json:"data"`
		Pagination *api.PaginationResponse `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := ListOptionsContractsResponse{
		Data:       contracts,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *OptionsHandlers) BuyOptionsContract(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.BuyOptionsContractJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode BuyOptionsContract request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"contract_id": req.ContractId,
		"quantity":    req.Quantity,
	}).Info("BuyOptionsContract request")

	positionId := openapi_types.UUID{}
	now := time.Now()
	currentValue := float32(0.0)
	premiumPaid := float32(0.0)
	pnl := float32(0.0)
	daysToExpiration := 0
	quantity := req.Quantity

	response := api.OptionsPosition{
		PositionId:       &positionId,
		ContractId:       &req.ContractId,
		PlayerId:         nil,
		Quantity:         &quantity,
		PremiumPaid:      &premiumPaid,
		CurrentValue:     &currentValue,
		Pnl:              &pnl,
		DaysToExpiration: &daysToExpiration,
		OpenedAt:         &now,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *OptionsHandlers) ListOptionsPositions(w http.ResponseWriter, r *http.Request, params api.ListOptionsPositionsParams) {
	ctx := r.Context()
	_ = ctx

	activeOnly := false
	if params.ActiveOnly != nil {
		activeOnly = *params.ActiveOnly
	}

	h.logger.WithFields(logrus.Fields{
		"active_only": activeOnly,
		"limit":       params.Limit,
		"offset":      params.Offset,
	}).Info("ListOptionsPositions request")

	positions := []api.OptionsPosition{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type ListOptionsPositionsResponse struct {
		Data       []api.OptionsPosition     `json:"data"`
		Pagination *api.PaginationResponse  `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := ListOptionsPositionsResponse{
		Data:       positions,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *OptionsHandlers) ExerciseOption(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.ExerciseOptionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode ExerciseOption request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithField("position_id", req.PositionId).Info("ExerciseOption request")

	now := time.Now()
	realizedPnl := float32(0.0)
	totalCost := float32(0.0)
	sharesAcquired := 0

	response := api.ExerciseOptionResponse{
		PositionId:     &req.PositionId,
		ExercisedAt:    &now,
		RealizedPnl:    &realizedPnl,
		TotalCost:      &totalCost,
		SharesAcquired: &sharesAcquired,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *OptionsHandlers) GetOptionsGreeks(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("position_id", positionId).Info("GetOptionsGreeks request")

	delta := float32(0.0)
	gamma := float32(0.0)
	theta := float32(0.0)
	vega := float32(0.0)

	response := api.Greeks{
		Delta: &delta,
		Gamma: &gamma,
		Theta: &theta,
		Vega:  &vega,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *OptionsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *OptionsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}










