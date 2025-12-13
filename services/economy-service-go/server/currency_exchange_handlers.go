// Issue: #1443
// Currency Exchange Handlers - HTTP handlers for currency exchange endpoints
package server

import (
	"context"
	"net/http"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/pkg/api"
	"github.com/google/uuid"
)

// Currency Exchange Rates Handlers

// GetCurrencyExchangeRates implements getCurrencyExchangeRates operation
func (h *EconomyHandlers) GetCurrencyExchangeRates(ctx context.Context, params api.GetCurrencyExchangeRatesParams) (api.GetCurrencyExchangeRatesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	rates, err := h.currencyExchangeService.GetExchangeRates(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get exchange rates")
		return &api.GetCurrencyExchangeRatesInternalServerError{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to get exchange rates",
		}, nil
	}

	// Convert to API format
	apiRates := make([]api.CurrencyExchangeRate, len(rates))
	for i, rate := range rates {
		apiRates[i] = api.CurrencyExchangeRate{
			Pair:   rate.Pair,
			Bid:    rate.Bid,
			Ask:    rate.Ask,
			Spread: rate.Spread,
		}
	}

	return &api.GetCurrencyExchangeRatesOK{
		Data: apiRates,
	}, nil
}

// GetCurrencyExchangeRatesPair implements getCurrencyExchangeRatesPair operation
func (h *EconomyHandlers) GetCurrencyExchangeRatesPair(ctx context.Context, params api.GetCurrencyExchangeRatesPairParams) (api.GetCurrencyExchangeRatesPairRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	rate, err := h.currencyExchangeService.GetExchangeRate(ctx, params.Pair)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get exchange rate")
		return &api.GetCurrencyExchangeRatesPairInternalServerError{
			Error:   http.StatusText(http.StatusInternalServerError),
			Message: "failed to get exchange rate",
		}, nil
	}

	if rate == nil {
		return &api.GetCurrencyExchangeRatesPairNotFound{
			Error:   http.StatusText(http.StatusNotFound),
			Message: "currency pair not found",
		}, nil
	}

	apiRate := api.CurrencyExchangeRate{
		Pair:   rate.Pair,
		Bid:    rate.Bid,
		Ask:    rate.Ask,
		Spread: rate.Spread,
	}

	return &api.GetCurrencyExchangeRatesPairOK{
		Data: apiRate,
	}, nil
}

// PostCurrencyExchangeQuote implements postCurrencyExchangeQuote operation
func (h *EconomyHandlers) PostCurrencyExchangeQuote(ctx context.Context, req *api.CurrencyExchangeQuoteRequest) (api.PostCurrencyExchangeQuoteRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	quote, err := h.currencyExchangeService.CreateQuote(ctx, req.FromCurrency, req.ToCurrency, req.FromAmount)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create quote")
		return &api.PostCurrencyExchangeQuoteBadRequest{
			Error:   http.StatusText(http.StatusBadRequest),
			Message: err.Error(),
		}, nil
	}

	apiQuote := api.CurrencyExchangeQuote{
		FromCurrency: quote.FromCurrency,
		ToCurrency:   quote.ToCurrency,
		FromAmount:   quote.FromAmount,
		ToAmount:     quote.ToAmount,
		ExchangeRate: quote.ExchangeRate,
		Fee:          quote.Fee,
		ValidUntil:   quote.ValidUntil,
	}

	return &api.PostCurrencyExchangeQuoteOK{
		Data: apiQuote,
	}, nil
}

// Currency Exchange Orders Handlers

// PostCurrencyExchangeOrdersInstant implements postCurrencyExchangeOrdersInstant operation
func (h *EconomyHandlers) PostCurrencyExchangeOrdersInstant(ctx context.Context, req *api.CurrencyExchangeInstantOrderRequest) (api.PostCurrencyExchangeOrdersInstantRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get player ID from context (security handler)
	playerID := uuid.New() // Placeholder

	createReq := models.CreateInstantExchangeRequest{
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.FromAmount,
	}

	order, err := h.currencyExchangeService.CreateInstantExchange(ctx, playerID, createReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create instant exchange")
		return &api.PostCurrencyExchangeOrdersInstantBadRequest{
			Error:   http.StatusText(http.StatusBadRequest),
			Message: err.Error(),
		}, nil
	}

	apiOrder := api.CurrencyExchangeOrder{
		Id:           order.ID,
		PlayerId:     order.PlayerID,
		OrderType:    order.OrderType,
		FromCurrency: order.FromCurrency,
		ToCurrency:   order.ToCurrency,
		FromAmount:   order.FromAmount,
		ToAmount:     order.ToAmount,
		ExchangeRate: order.ExchangeRate,
		Fee:          order.Fee,
		Status:       order.Status,
		CreatedAt:    order.CreatedAt,
		UpdatedAt:    order.UpdatedAt,
	}

	return &api.PostCurrencyExchangeOrdersInstantOK{
		Data: apiOrder,
	}, nil
}

// PostCurrencyExchangeOrdersLimit implements postCurrencyExchangeOrdersLimit operation
func (h *EconomyHandlers) PostCurrencyExchangeOrdersLimit(ctx context.Context, req *api.CurrencyExchangeLimitOrderRequest) (api.PostCurrencyExchangeOrdersLimitRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get player ID from context (security handler)
	playerID := uuid.New() // Placeholder

	// TODO: Add currency exchange service to EconomyHandlers
	createReq := models.CreateLimitOrderRequest{
		FromCurrency: req.FromCurrency,
		ToCurrency:   req.ToCurrency,
		FromAmount:   req.FromAmount,
		ExchangeRate: req.ExchangeRate,
		// ExpiresAt: req.ExpiresAt, // TODO: convert from API format
	}

	// For now, return not implemented
	return &api.PostCurrencyExchangeOrdersLimitInternalServerError{
		Error:   http.StatusText(http.StatusInternalServerError),
		Message: "currency exchange service not implemented yet",
	}, nil
}

// GetCurrencyExchangeOrders implements getCurrencyExchangeOrders operation
func (h *EconomyHandlers) GetCurrencyExchangeOrders(ctx context.Context, params api.GetCurrencyExchangeOrdersParams) (api.GetCurrencyExchangeOrdersRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get player ID from context (security handler)
	playerID := uuid.New() // Placeholder

	// Parse pagination
	limit := 50 // default
	offset := 0 // default

	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	filter := models.OrderFilter{
		PlayerID: &playerID,
		Limit:    limit,
		Offset:   offset,
	}

	// TODO: Add currency exchange service to EconomyHandlers
	// For now, return empty list
	orders := []api.CurrencyExchangeOrder{}

	return &api.GetCurrencyExchangeOrdersOK{
		Data: orders,
	}, nil
}

// GetCurrencyExchangeOrdersOrderID implements getCurrencyExchangeOrdersOrderID operation
func (h *EconomyHandlers) GetCurrencyExchangeOrdersOrderID(ctx context.Context, params api.GetCurrencyExchangeOrdersOrderIDParams) (api.GetCurrencyExchangeOrdersOrderIDRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	orderID, err := uuid.Parse(params.OrderID)
	if err != nil {
		return &api.GetCurrencyExchangeOrdersOrderIDBadRequest{
			Error:   http.StatusText(http.StatusBadRequest),
			Message: "invalid order ID format",
		}, nil
	}

	// TODO: Add currency exchange service to EconomyHandlers
	// For now, return not found
	return &api.GetCurrencyExchangeOrdersOrderIDNotFound{
		Error:   http.StatusText(http.StatusNotFound),
		Message: "order not found",
	}, nil
}

// DeleteCurrencyExchangeOrdersOrderID implements deleteCurrencyExchangeOrdersOrderID operation
func (h *EconomyHandlers) DeleteCurrencyExchangeOrdersOrderID(ctx context.Context, params api.DeleteCurrencyExchangeOrdersOrderIDParams) (api.DeleteCurrencyExchangeOrdersOrderIDRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	orderID, err := uuid.Parse(params.OrderID)
	if err != nil {
		return &api.DeleteCurrencyExchangeOrdersOrderIDBadRequest{
			Error:   http.StatusText(http.StatusBadRequest),
			Message: "invalid order ID format",
		}, nil
	}

	// TODO: Add currency exchange service to EconomyHandlers
	// For now, return not found
	return &api.DeleteCurrencyExchangeOrdersOrderIDNotFound{
		Error:   http.StatusText(http.StatusNotFound),
		Message: "order not found",
	}, nil
}

// Currency Exchange Trades Handlers

// GetCurrencyExchangeTrades implements getCurrencyExchangeTrades operation
func (h *EconomyHandlers) GetCurrencyExchangeTrades(ctx context.Context, params api.GetCurrencyExchangeTradesParams) (api.GetCurrencyExchangeTradesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Get player ID from context (security handler)
	playerID := uuid.New() // Placeholder

	// Parse pagination
	limit := 50 // default
	offset := 0 // default

	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	filter := models.TradeFilter{
		PlayerID: &playerID,
		Limit:    limit,
		Offset:   offset,
	}

	// TODO: Add currency exchange service to EconomyHandlers
	// For now, return empty list
	trades := []api.CurrencyExchangeTrade{}

	return &api.GetCurrencyExchangeTradesOK{
		Data: trades,
	}, nil
}
