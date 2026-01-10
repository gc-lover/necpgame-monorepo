package clients

import (
	"fmt"

	"go.uber.org/zap"
)

// EconomyClient handles communication with the Economy Service
type EconomyClient struct {
	*HTTPClient
}

// NewEconomyClient creates a new Economy Service client
func NewEconomyClient(baseURL string, logger *zap.Logger) *EconomyClient {
	return &EconomyClient{
		HTTPClient: NewHTTPClient(baseURL, logger),
	}
}

// CurrencyBalance represents a player's currency balance
type CurrencyBalance struct {
	PlayerID string `json:"playerId"`
	Currency string `json:"currency"`
	Amount   int    `json:"amount"`
}

// TransactionRequest represents a currency transaction request
type TransactionRequest struct {
	PlayerID string `json:"playerId"`
	Currency string `json:"currency"`
	Amount   int    `json:"amount"`
	Reason   string `json:"reason"`
	Metadata map[string]interface{} `json:"metadata,omitempty"`
}

// TransactionResult represents the result of a currency transaction
type TransactionResult struct {
	Success      bool   `json:"success"`
	TransactionID string `json:"transactionId,omitempty"`
	NewBalance   int    `json:"newBalance,omitempty"`
	ErrorMessage string `json:"errorMessage,omitempty"`
}

// ProcessPayment processes a payment transaction
func (c *EconomyClient) ProcessPayment(request TransactionRequest) (*TransactionResult, error) {
	path := "/economy/transactions"

	resp, err := c.Post(path, request, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to process payment: %w", err)
	}

	var result TransactionResult
	if err := c.ReadJSONResponse(resp, &result); err != nil {
		return nil, fmt.Errorf("failed to read payment response: %w", err)
	}

	return &result, nil
}

// GrantCurrency grants currency to a player
func (c *EconomyClient) GrantCurrency(request TransactionRequest) (*TransactionResult, error) {
	path := "/economy/grants"

	resp, err := c.Post(path, request, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to grant currency: %w", err)
	}

	var result TransactionResult
	if err := c.ReadJSONResponse(resp, &result); err != nil {
		return nil, fmt.Errorf("failed to read grant response: %w", err)
	}

	return &result, nil
}

// GetPlayerBalance retrieves a player's currency balance
func (c *EconomyClient) GetPlayerBalance(playerID, currency string) (*CurrencyBalance, error) {
	path := fmt.Sprintf("/economy/balances/%s", playerID)
	if currency != "" {
		path += fmt.Sprintf("?currency=%s", currency)
	}

	resp, err := c.Get(path, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to get player balance: %w", err)
	}

	var balance CurrencyBalance
	if err := c.ReadJSONResponse(resp, &balance); err != nil {
		return nil, fmt.Errorf("failed to read balance response: %w", err)
	}

	return &balance, nil
}

// ValidatePayment validates if a player can afford a purchase
func (c *EconomyClient) ValidatePayment(playerID, currency string, amount int) error {
	balance, err := c.GetPlayerBalance(playerID, currency)
	if err != nil {
		return fmt.Errorf("failed to get balance for validation: %w", err)
	}

	if balance.Amount < amount {
		return fmt.Errorf("insufficient funds: has %d, needs %d", balance.Amount, amount)
	}

	return nil
}