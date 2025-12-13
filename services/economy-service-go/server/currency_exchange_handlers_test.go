// Issue: #1443
// Currency Exchange Handlers Tests
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/pkg/api"
	"github.com/stretchr/testify/assert"
)

func TestEconomyHandlers_GetCurrencyExchangeRates(t *testing.T) {
	// Create mock repository
	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRatesFunc: func(ctx context.Context) ([]models.CurrencyExchangeRate, error) {
			return []models.CurrencyExchangeRate{
				{Pair: "USD/EUR", Bid: 0.85, Ask: 0.87, Spread: 0.02, IsActive: true},
			}, nil
		},
	}

	// Create service and handlers
	service := NewCurrencyExchangeService(mockRepo)
	handlers := NewEconomyHandlers(nil, service)

	// Test the handler
	resp, err := handlers.GetCurrencyExchangeRates(context.Background(), api.GetCurrencyExchangeRatesParams{})

	assert.NoError(t, err)
	assert.IsType(t, &api.GetCurrencyExchangeRatesOK{}, resp)

	okResp := resp.(*api.GetCurrencyExchangeRatesOK)
	assert.Len(t, okResp.Data, 1)
	assert.Equal(t, "USD/EUR", okResp.Data[0].Pair)
	assert.Equal(t, 0.85, okResp.Data[0].Bid)
	assert.Equal(t, 0.87, okResp.Data[0].Ask)
	assert.Equal(t, 0.02, okResp.Data[0].Spread)
}

func TestEconomyHandlers_PostCurrencyExchangeQuote(t *testing.T) {
	// Create mock repository
	mockRepo := &MockCurrencyExchangeRepository{
		GetExchangeRateFunc: func(ctx context.Context, pair string) (*models.CurrencyExchangeRate, error) {
			if pair == "USD/EUR" {
				return &models.CurrencyExchangeRate{
					Pair: "USD/EUR", Bid: 0.85, Ask: 0.87, Spread: 0.02, IsActive: true,
				}, nil
			}
			return nil, nil
		},
	}

	// Create service and handlers
	service := NewCurrencyExchangeService(mockRepo)
	handlers := NewEconomyHandlers(nil, service)

	// Test the handler
	req := &api.CurrencyExchangeQuoteRequest{
		FromCurrency: "USD",
		ToCurrency:   "EUR",
		FromAmount:   100.0,
	}

	resp, err := handlers.PostCurrencyExchangeQuote(context.Background(), req)

	assert.NoError(t, err)
	assert.IsType(t, &api.PostCurrencyExchangeQuoteOK{}, resp)

	okResp := resp.(*api.PostCurrencyExchangeQuoteOK)
	assert.Equal(t, "USD", okResp.Data.FromCurrency)
	assert.Equal(t, "EUR", okResp.Data.ToCurrency)
	assert.Equal(t, 100.0, okResp.Data.FromAmount)
	assert.Equal(t, 84.15, okResp.Data.ToAmount) // 100 * 0.85 - 0.85 fee
	assert.Equal(t, 0.85, okResp.Data.ExchangeRate)
	assert.Equal(t, 0.85, okResp.Data.Fee)
	assert.True(t, okResp.Data.ValidUntil.After(okResp.Data.ValidUntil.Add(-1)))
}
