package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-futures-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type FuturesHandlers struct {
	logger *logrus.Logger
}

func NewFuturesHandlers() *FuturesHandlers {
	return &FuturesHandlers{
		logger: GetLogger(),
	}
}

func (h *FuturesHandlers) ListFuturesContracts(w http.ResponseWriter, r *http.Request, params api.ListFuturesContractsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"underlying":      params.Underlying,
		"expiration_from": params.ExpirationFrom,
		"expiration_to":   params.ExpirationTo,
		"limit":           params.Limit,
		"offset":          params.Offset,
	}).Info("ListFuturesContracts request")

	contracts := []api.FuturesContract{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type ListFuturesContractsResponse struct {
		Data       []api.FuturesContract    `json:"data"`
		Pagination *api.PaginationResponse `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := ListFuturesContractsResponse{
		Data:       contracts,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *FuturesHandlers) OpenFuturesPosition(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.OpenFuturesPositionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode OpenFuturesPosition request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"contract_id": req.ContractId,
		"quantity":    req.Quantity,
	}).Info("OpenFuturesPosition request")

	positionId := openapi_types.UUID{}
	now := time.Now()
	entryPrice := float32(0.0)
	currentPrice := float32(0.0)
	marginHeld := float32(0.0)
	pnl := float32(0.0)
	daysToSettlement := 0
	quantity := req.Quantity

	response := api.FuturesPosition{
		PositionId:       &positionId,
		ContractId:       &req.ContractId,
		PlayerId:         nil,
		Quantity:         &quantity,
		EntryPrice:       &entryPrice,
		CurrentPrice:     &currentPrice,
		MarginHeld:       &marginHeld,
		Pnl:              &pnl,
		DaysToSettlement: &daysToSettlement,
		OpenedAt:         &now,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *FuturesHandlers) ListFuturesPositions(w http.ResponseWriter, r *http.Request, params api.ListFuturesPositionsParams) {
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
	}).Info("ListFuturesPositions request")

	positions := []api.FuturesPosition{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type ListFuturesPositionsResponse struct {
		Data       []api.FuturesPosition     `json:"data"`
		Pagination *api.PaginationResponse  `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := ListFuturesPositionsResponse{
		Data:       positions,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *FuturesHandlers) CloseFuturesPosition(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("position_id", positionId).Info("CloseFuturesPosition request")

	now := time.Now()
	realizedPnl := float32(0.0)
	response := api.ClosePositionResponse{
		PositionId:  &positionId,
		RealizedPnl: &realizedPnl,
		ClosedAt:    &now,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *FuturesHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *FuturesHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}









