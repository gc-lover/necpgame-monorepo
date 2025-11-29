package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/stock-margin-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type MarginHandlers struct {
	logger *logrus.Logger
}

func NewMarginHandlers() *MarginHandlers {
	return &MarginHandlers{
		logger: GetLogger(),
	}
}

func (h *MarginHandlers) GetMarginAccount(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetMarginAccount request")

	accountId := openapi_types.UUID{}
	playerId := openapi_types.UUID{}
	balance := float32(0.0)
	borrowedAmount := float32(0.0)
	equity := float32(0.0)
	leverage := float32(2.0)
	availableCredit := float32(0.0)
	maintenanceMargin := float32(0.0)
	marginHealth := float32(100.0)

	response := api.MarginAccount{
		AccountId:         &accountId,
		PlayerId:          &playerId,
		Balance:           &balance,
		BorrowedAmount:    &borrowedAmount,
		Equity:            &equity,
		Leverage:          &leverage,
		AvailableCredit:   &availableCredit,
		MaintenanceMargin: &maintenanceMargin,
		MarginHealth:      &marginHealth,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) OpenMarginAccount(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.OpenMarginAccountJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode OpenMarginAccount request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithField("initial_deposit", req.InitialDeposit).Info("OpenMarginAccount request")

	accountId := openapi_types.UUID{}
	playerId := openapi_types.UUID{}
	balance := req.InitialDeposit
	borrowedAmount := float32(0.0)
	equity := balance
	leverage := float32(2.0)
	availableCredit := balance * leverage
	maintenanceMargin := balance * 0.3
	marginHealth := float32(100.0)

	response := api.MarginAccount{
		AccountId:         &accountId,
		PlayerId:          &playerId,
		Balance:           &balance,
		BorrowedAmount:    &borrowedAmount,
		Equity:            &equity,
		Leverage:          &leverage,
		AvailableCredit:   &availableCredit,
		MaintenanceMargin: &maintenanceMargin,
		MarginHealth:      &marginHealth,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *MarginHandlers) BorrowMargin(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.BorrowMarginJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode BorrowMargin request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithField("amount", req.Amount).Info("BorrowMargin request")

	borrowedAmount := req.Amount
	interestRate := float32(5.0)
	leverage := float32(2.0)
	collateralRequired := req.Amount * 1.5

	response := api.BorrowMarginResponse{
		BorrowedAmount:     &borrowedAmount,
		InterestRate:       &interestRate,
		Leverage:           &leverage,
		CollateralRequired: &collateralRequired,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) RepayMargin(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.RepayMarginJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode RepayMargin request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithField("amount", req.Amount).Info("RepayMargin request")

	response := map[string]interface{}{
		"success": true,
		"amount":  req.Amount,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) GetMarginCallHistory(w http.ResponseWriter, r *http.Request, params api.GetMarginCallHistoryParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"limit":  params.Limit,
		"offset": params.Offset,
	}).Info("GetMarginCallHistory request")

	calls := []api.MarginCall{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type GetMarginCallHistoryResponse struct {
		Data       []api.MarginCall         `json:"data"`
		Pagination *api.PaginationResponse `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := GetMarginCallHistoryResponse{
		Data:       calls,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) OpenShortPosition(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.OpenShortPositionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.WithError(err).Error("Failed to decode OpenShortPosition request")
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"ticker":   req.Ticker,
		"quantity": req.Quantity,
	}).Info("OpenShortPosition request")

	positionId := openapi_types.UUID{}
	playerId := openapi_types.UUID{}
	now := time.Now()
	entryPrice := float32(0.0)
	currentPrice := float32(0.0)
	pnl := float32(0.0)
	collateral := entryPrice * float32(req.Quantity) * 1.5

	response := api.ShortPosition{
		PositionId:  &positionId,
		PlayerId:    &playerId,
		Ticker:      &req.Ticker,
		Quantity:    &req.Quantity,
		EntryPrice:  &entryPrice,
		CurrentPrice: &currentPrice,
		Pnl:         &pnl,
		Collateral:  &collateral,
		OpenedAt:    &now,
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *MarginHandlers) ListShortPositions(w http.ResponseWriter, r *http.Request, params api.ListShortPositionsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"limit":  params.Limit,
		"offset": params.Offset,
	}).Info("ListShortPositions request")

	positions := []api.ShortPosition{}
	total := 0
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	type ListShortPositionsResponse struct {
		Data       []api.ShortPosition      `json:"data"`
		Pagination *api.PaginationResponse `json:"pagination,omitempty"`
	}

	pagination := api.PaginationResponse{
		Total:  total,
		Limit:  &limit,
		Offset: &offset,
		Items:  []interface{}{},
	}

	response := ListShortPositionsResponse{
		Data:       positions,
		Pagination: &pagination,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) GetShortPosition(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("position_id", positionId).Info("GetShortPosition request")

	ticker := ""
	quantity := 0
	entryPrice := float32(0.0)
	currentPrice := float32(0.0)
	pnl := float32(0.0)
	collateral := float32(0.0)
	playerId := openapi_types.UUID{}
	now := time.Now()

	response := api.ShortPosition{
		PositionId:  &positionId,
		PlayerId:    &playerId,
		Ticker:      &ticker,
		Quantity:    &quantity,
		EntryPrice:  &entryPrice,
		CurrentPrice: &currentPrice,
		Pnl:         &pnl,
		Collateral:  &collateral,
		OpenedAt:    &now,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) CloseShortPosition(w http.ResponseWriter, r *http.Request, positionId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("position_id", positionId).Info("CloseShortPosition request")

	now := time.Now()
	realizedPnl := float32(0.0)

	response := api.ClosePositionResponse{
		PositionId:  &positionId,
		RealizedPnl: &realizedPnl,
		ClosedAt:    &now,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) GetRiskHealth(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetRiskHealth request")

	marginHealth := float32(100.0)
	maintenanceMargin := float32(0.0)
	liquidationPrice := float32(0.0)
	atRisk := false
	marginCallWarning := false
	openPositionsRisk := map[string]interface{}{}

	response := api.RiskHealth{
		MarginHealth:       &marginHealth,
		MaintenanceMargin:  &maintenanceMargin,
		LiquidationPrice:   &liquidationPrice,
		AtRisk:             &atRisk,
		MarginCallWarning:  &marginCallWarning,
		OpenPositionsRisk:  &openPositionsRisk,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *MarginHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *MarginHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}



