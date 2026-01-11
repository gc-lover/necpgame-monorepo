package handlers

import (
	"context"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/api"
	"github.com/gc-lover/necpgame/services/stock-dividends-service-go/internal/service"
)

// DividendsHandlers implements the generated Handler interface
type DividendsHandlers struct {
	dividendsSvc *service.DividendsService
	logger       *zap.Logger
}

// NewDividendsHandlers creates a new instance of DividendsHandlers
func NewDividendsHandlers(svc *service.DividendsService, logger *zap.Logger) *DividendsHandlers {
	return &DividendsHandlers{
		dividendsSvc: svc,
		logger:       logger,
	}
}

// StocksStockIDDividendsScheduleGet implements GET /stocks/{stock_id}/dividends/schedule operation
func (h *DividendsHandlers) StocksStockIDDividendsScheduleGet(ctx context.Context, params api.StocksStockIDDividendsScheduleGetParams) (api.StocksStockIDDividendsScheduleGetRes, error) {
	h.logger.Info("Getting dividend schedule for stock",
		zap.String("stock_id", params.StockID.String()))

	schedule, err := h.dividendsSvc.GetDividendSchedule(ctx, params.StockID)
	if err != nil {
		h.logger.Error("Failed to get dividend schedule", zap.Error(err))
		return &api.StocksStockIDDividendsScheduleGetNotFound{}, nil
	}

	return &api.DividendScheduleResponse{
		Schedule: api.NewOptDividendSchedule(*schedule),
	}, nil
}

// PlayersPlayerIDDividendsPaymentsGet implements GET /players/{player_id}/dividends/payments operation
func (h *DividendsHandlers) PlayersPlayerIDDividendsPaymentsGet(ctx context.Context, params api.PlayersPlayerIDDividendsPaymentsGetParams) (api.PlayersPlayerIDDividendsPaymentsGetRes, error) {
	limit := 20 // default
	if params.Limit.Set {
		limit = params.Limit.Value
	}

	offset := 0 // default
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	h.logger.Info("Getting dividend payments for player",
		zap.String("player_id", params.PlayerID.String()),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	payments, total, err := h.dividendsSvc.GetDividendPayments(ctx, params.PlayerID, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get dividend payments", zap.Error(err))
		return &api.PlayersPlayerIDDividendsPaymentsGetNotFound{}, nil
	}

	summary := h.calculatePaymentSummary(payments)

	return &api.DividendPaymentsResponse{
		Payments: payments,
		Pagination: api.NewOptPaginationInfo(api.PaginationInfo{
			Limit:  limit,
			Offset: offset,
			Total:  int(total),
		}),
		Summary: api.NewOptPaymentSummary(summary),
	}, nil
}

// PlayersPlayerIDDividendsDripGet implements GET /players/{player_id}/dividends/drip operation
func (h *DividendsHandlers) PlayersPlayerIDDividendsDripGet(ctx context.Context, params api.PlayersPlayerIDDividendsDripGetParams) (api.PlayersPlayerIDDividendsDripGetRes, error) {
	h.logger.Info("Getting DRIP settings for player",
		zap.String("player_id", params.PlayerID.String()))

	settings, err := h.dividendsSvc.GetDRIPSettings(ctx, params.PlayerID)
	if err != nil {
		h.logger.Error("Failed to get DRIP settings", zap.Error(err))
		return &api.PlayersPlayerIDDividendsDripGetNotFound{}, nil
	}

	return settings, nil
}

// PlayersPlayerIDDividendsDripPut implements PUT /players/{player_id}/dividends/drip operation
func (h *DividendsHandlers) PlayersPlayerIDDividendsDripPut(ctx context.Context, req *api.DRIPSettingsUpdate, params api.PlayersPlayerIDDividendsDripPutParams) (api.PlayersPlayerIDDividendsDripPutRes, error) {
	h.logger.Info("Updating DRIP settings for player",
		zap.String("player_id", params.PlayerID.String()),
		zap.Bool("enabled", req.Enabled))

	updatedSettings, err := h.dividendsSvc.UpdateDRIPSettings(ctx, params.PlayerID, req)
	if err != nil {
		h.logger.Error("Failed to update DRIP settings", zap.Error(err))
		return &api.PlayersPlayerIDDividendsDripPutBadRequest{}, nil
	}

	return updatedSettings, nil
}

// AdminDividendsSchedulesPost implements POST /admin/dividends/schedules operation
func (h *DividendsHandlers) AdminDividendsSchedulesPost(ctx context.Context, req *api.CreateDividendScheduleRequest) (api.AdminDividendsSchedulesPostRes, error) {
	h.logger.Info("Creating dividend schedule",
		zap.String("stock_id", req.StockID.String()),
		zap.Float64("amount_per_share", float64(req.AmountPerShare)))

	schedule, err := h.dividendsSvc.CreateDividendSchedule(ctx, req)
	if err != nil {
		h.logger.Error("Failed to create dividend schedule", zap.Error(err))
		return &api.AdminDividendsSchedulesPostBadRequest{}, nil
	}

	return &api.DividendSchedule{
		ID:               schedule.ID,
		StockID:          schedule.StockID,
		Frequency:        api.DividendScheduleFrequency(schedule.Frequency),
		AmountPerShare:   float32(schedule.AmountPerShare),
		DeclarationDate:  schedule.DeclarationDate,
		ExDividendDate:   schedule.ExDividendDate,
		RecordDate:       schedule.RecordDate,
		PaymentDate:      schedule.PaymentDate,
		Status:           api.DividendScheduleStatus(schedule.Status),
		CreatedAt:        api.NewOptDateTime(schedule.CreatedAt),
		UpdatedAt:        api.NewOptDateTime(schedule.UpdatedAt),
	}, nil
}

// AdminDividendsProcessPost implements POST /admin/dividends/process operation
func (h *DividendsHandlers) AdminDividendsProcessPost(ctx context.Context, req *api.ProcessDividendsRequest) (api.AdminDividendsProcessPostRes, error) {
	dryRun := false
	if req.DryRun.Set {
		dryRun = req.DryRun.Value
	}

	h.logger.Info("Processing dividend payments",
		zap.Bool("dry_run", dryRun))

	result, err := h.dividendsSvc.ProcessDividendPayments(ctx, req)
	if err != nil {
		h.logger.Error("Failed to process dividend payments", zap.Error(err))
		return &api.AdminDividendsProcessPostBadRequest{}, nil
	}

	return result, nil
}

// calculatePaymentSummary calculates summary statistics for payments
func (h *DividendsHandlers) calculatePaymentSummary(payments []api.DividendPayment) api.PaymentSummary {
	var totalPayments int
	var totalGrossAmount, totalTaxAmount, totalNetAmount float32
	var totalDRIPShares int

	for _, payment := range payments {
		totalPayments++
		totalGrossAmount += payment.GrossAmount
		totalTaxAmount += payment.TaxAmount
		totalNetAmount += payment.NetAmount
		if payment.DripSharesPurchased.Set {
			totalDRIPShares += payment.DripSharesPurchased.Value
		}
	}

	return api.PaymentSummary{
		TotalPayments:     totalPayments,
		TotalGrossAmount:  totalGrossAmount,
		TotalTaxAmount:    totalTaxAmount,
		TotalNetAmount:    totalNetAmount,
		TotalDripShares:   totalDRIPShares,
	}
}