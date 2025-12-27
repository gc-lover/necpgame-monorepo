package server

import (
	"bytes"
	"context"
	"database/sql"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"go.uber.org/zap/zaptest"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/pkg/api"
)

func setupTestHandler() (*EconomyHandler, sqlmock.Sqlmock) {
	// Create mock database
	db, mock, err := sqlmock.New()
	if err != nil {
		panic("Failed to create mock database")
	}

	// Create mock pool (we'll use the db directly for simplicity)
	logger := zaptest.NewLogger(nil)

	handler := &EconomyHandler{
		dbPool: db,
		logger: logger,
	}

	return handler, mock
}

func TestEconomyHandler_HealthCheck_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock expectations
	mock.ExpectPing().WillReturnError(nil)

	// Create test request
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.HealthCheck(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, api.HealthResponseStatusHealthy, result.Status)
	assert.Equal(t, "economy-service-go", result.Service)
	assert.WithinDuration(t, time.Now(), result.Timestamp, time.Second)
}

func TestEconomyHandler_HealthCheck_DatabaseError(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock to return error
	mock.ExpectPing().WillReturnError(sql.ErrConnDone)

	// Create test request
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.HealthCheck(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, api.HealthResponseStatusUnhealthy, result.Status)
	assert.Equal(t, "economy-service-go", result.Service)
}

func TestEconomyHandler_ReadinessCheck_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock expectations
	mock.ExpectPing().WillReturnError(nil)

	// Create test request
	req := httptest.NewRequest("GET", "/ready", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.ReadinessCheck(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, "healthy", result.Status)
	assert.Equal(t, "economy-service-go", result.Service)
}

func TestEconomyHandler_ReadinessCheck_Timeout(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock to delay (simulate timeout)
	mock.ExpectPing().WillDelayFor(100 * time.Millisecond)

	// Create test request with short timeout
	req := httptest.NewRequest("GET", "/ready", nil)
	ctx, cancel := context.WithTimeout(req.Context(), 10*time.Millisecond)
	defer cancel()
	req = req.WithContext(ctx)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.ReadinessCheck(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, "unhealthy", result.Status)
}

func TestEconomyHandler_Metrics_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock expectations
	mock.ExpectPing().WillReturnError(nil)

	// Create test request
	req := httptest.NewRequest("GET", "/metrics", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.Metrics(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, "healthy", result.Status)
	assert.Equal(t, "economy-service-go", result.Service)
}

func TestEconomyHandler_GetMarketOverview_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock expectations for market data query
	rows := sqlmock.NewRows([]string{"item_id", "price", "volume", "last_updated"}).
		AddRow(uuid.New(), 100.50, 1000, time.Now()).
		AddRow(uuid.New(), 200.75, 500, time.Now())

	mock.ExpectQuery("SELECT (.+) FROM market_overview").WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/market/overview", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetMarketOverview(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotEmpty(t, result.Items)
}

func TestEconomyHandler_GetMarketOverview_DatabaseError(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock to return error
	mock.ExpectQuery("SELECT (.+) FROM market_overview").WillReturnError(sql.ErrNoRows)

	// Create test request
	req := httptest.NewRequest("GET", "/market/overview", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetMarketOverview(w, req)

	// Verify - should handle error gracefully
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_CreateTradeListing_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()
	playerID := uuid.New()

	// Setup mock expectations
	mock.ExpectBegin()
	mock.ExpectExec("INSERT INTO trades").
		WithArgs(tradeID, playerID, sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
		WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	// Create test request
	req := httptest.NewRequest("POST", "/trades", nil)
	w := httptest.NewRecorder()

	request := &api.CreateTradeRequest{
		ItemID:   uuid.New(),
		Price:    150.0,
		Quantity: 5,
	}

	// Execute
	result, err := handler.CreateTradeListing(w, req, request)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotEqual(t, uuid.Nil, result.ID)
}

func TestEconomyHandler_CreateTradeListing_InvalidRequest(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Create test request
	req := httptest.NewRequest("POST", "/trades", nil)
	w := httptest.NewRecorder()

	request := &api.CreateTradeRequest{
		ItemID:   uuid.Nil, // Invalid
		Price:    -10.0,    // Invalid
		Quantity: 0,        // Invalid
	}

	// Execute
	result, err := handler.CreateTradeListing(w, req, request)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_GetTradeDetails_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"id", "player_id", "item_id", "price", "quantity", "status", "created_at"}).
		AddRow(tradeID, uuid.New(), uuid.New(), 100.0, 10, "active", time.Now())

	mock.ExpectQuery("SELECT (.+) FROM trades WHERE id = \\$1").
		WithArgs(tradeID).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/trades/"+tradeID.String(), nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetTradeDetails(w, req, tradeID)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, tradeID, result.ID)
}

func TestEconomyHandler_GetTradeDetails_NotFound(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()

	// Setup mock to return no rows
	mock.ExpectQuery("SELECT (.+) FROM trades WHERE id = \\$1").
		WithArgs(tradeID).
		WillReturnError(sql.ErrNoRows)

	// Create test request
	req := httptest.NewRequest("GET", "/trades/"+tradeID.String(), nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetTradeDetails(w, req, tradeID)

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_CancelTrade_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()

	// Setup mock expectations
	mock.ExpectBegin()
	mock.ExpectExec("UPDATE trades SET status = \\$1 WHERE id = \\$2 AND status = \\$3").
		WithArgs("cancelled", tradeID, "active").
		WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	// Create test request
	req := httptest.NewRequest("DELETE", "/trades/"+tradeID.String(), nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.CancelTrade(w, req, tradeID)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
}

func TestEconomyHandler_CancelTrade_AlreadyCancelled(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()

	// Setup mock - no rows affected (trade already cancelled)
	mock.ExpectBegin()
	mock.ExpectExec("UPDATE trades SET status = \\$1 WHERE id = \\$2 AND status = \\$3").
		WithArgs("cancelled", tradeID, "active").
		WillReturnResult(sqlmock.NewResult(0, 0))
	mock.ExpectRollback()

	// Create test request
	req := httptest.NewRequest("DELETE", "/trades/"+tradeID.String(), nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.CancelTrade(w, req, tradeID)

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_GetPlayerTransactionHistory_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"id", "player_id", "type", "amount", "currency", "timestamp"}).
		AddRow(uuid.New(), playerID, "purchase", 50.0, "eddies", time.Now()).
		AddRow(uuid.New(), playerID, "sale", 25.0, "eddies", time.Now())

	mock.ExpectQuery("SELECT (.+) FROM transactions WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/transactions", nil)
	w := httptest.NewRecorder()

	params := api.GetPlayerTransactionHistoryParams{
		Limit:  &[]int{50}[0],
		Offset: &[]int{0}[0],
	}

	// Execute
	result, err := handler.GetPlayerTransactionHistory(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Transactions, 2)
}

func TestEconomyHandler_GetPlayerTransactionHistory_NoTransactions(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock to return empty result
	mock.ExpectQuery("SELECT (.+) FROM transactions WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnRows(sqlmock.NewRows([]string{}))

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/transactions", nil)
	w := httptest.NewRecorder()

	params := api.GetPlayerTransactionHistoryParams{
		Limit:  &[]int{50}[0],
		Offset: &[]int{0}[0],
	}

	// Execute
	result, err := handler.GetPlayerTransactionHistory(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Empty(t, result.Transactions)
}

func TestEconomyHandler_GetPlayerWallet_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()
	createdAt := time.Now()
	updatedAt := time.Now()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"eurodollars", "cryptocurrency", "reputation_points", "created_at", "updated_at"}).
		AddRow(1000.50, 500.25, 150, createdAt, updatedAt)

	mock.ExpectQuery("SELECT (.+) FROM player_wallets WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/wallet", nil)
	w := httptest.NewRecorder()

	params := api.GetPlayerWalletParams{
		PlayerID: playerID,
	}

	// Execute
	result, err := handler.GetPlayerWallet(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
	assert.Equal(t, 1000.50, result.Eurodollars)
	assert.Equal(t, 500.25, result.Cryptocurrency)
	assert.Equal(t, int32(150), result.ReputationPoints)
}

func TestEconomyHandler_GetPlayerWallet_NotFound(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock to return no rows
	mock.ExpectQuery("SELECT (.+) FROM player_wallets WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnError(sql.ErrNoRows)

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/wallet", nil)
	w := httptest.NewRecorder()

	params := api.GetPlayerWalletParams{
		PlayerID: playerID,
	}

	// Execute
	result, err := handler.GetPlayerWallet(w, req, params)

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_UpdatePlayerWallet_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()
	createdAt := time.Now()
	updatedAt := time.Now()

	// Setup mock expectations
	mock.ExpectBegin()
	mock.ExpectExec("UPDATE player_wallets SET (.+) WHERE player_id = \\$1").
		WithArgs(sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), playerID).
		WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	// Setup mock for SELECT after update
	rows := sqlmock.NewRows([]string{"eurodollars", "cryptocurrency", "reputation_points", "created_at", "updated_at"}).
		AddRow(1500.50, 600.25, 200, createdAt, updatedAt)

	mock.ExpectQuery("SELECT (.+) FROM player_wallets WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("PUT", "/players/"+playerID.String()+"/wallet", nil)
	w := httptest.NewRecorder()

	request := &api.UpdateWalletRequest{
		Eurodollars:       &[]float64{500.0}[0],
		Cryptocurrency:    &[]float64{100.0}[0],
		ReputationPoints:  &[]int32{50}[0],
	}

	// Execute
	result, err := handler.UpdatePlayerWallet(w, req, playerID, request)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerID)
}

func TestEconomyHandler_UpdatePlayerWallet_InvalidRequest(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Create test request with invalid data
	req := httptest.NewRequest("PUT", "/players/"+playerID.String()+"/wallet", nil)
	w := httptest.NewRecorder()

	request := &api.UpdateWalletRequest{
		Eurodollars:      &[]float64{-100.0}[0], // Invalid negative value
		ReputationPoints: &[]int32{-50}[0],     // Invalid negative value
	}

	// Execute
	result, err := handler.UpdatePlayerWallet(w, req, playerID, request)

	// Verify - should handle validation
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_GetActiveTrades_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"id", "player_id", "item_id", "price", "quantity", "status", "created_at"}).
		AddRow(uuid.New(), playerID, uuid.New(), 100.0, 5, "active", time.Now()).
		AddRow(uuid.New(), playerID, uuid.New(), 200.0, 3, "active", time.Now())

	mock.ExpectQuery("SELECT (.+) FROM trades WHERE player_id = \\$1 AND status = \\$2").
		WithArgs(playerID, "active").
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/trades", nil)
	w := httptest.NewRecorder()

	params := api.GetActiveTradesParams{
		PlayerID: playerID,
		Limit:    &[]int{50}[0],
		Offset:   &[]int{0}[0],
	}

	// Execute
	result, err := handler.GetActiveTrades(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Trades, 2)
}

func TestEconomyHandler_GetActiveTrades_NoTrades(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock to return empty result
	mock.ExpectQuery("SELECT (.+) FROM trades WHERE player_id = \\$1 AND status = \\$2").
		WithArgs(playerID, "active").
		WillReturnRows(sqlmock.NewRows([]string{}))

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/trades", nil)
	w := httptest.NewRecorder()

	params := api.GetActiveTradesParams{
		PlayerID: playerID,
		Limit:    &[]int{50}[0],
		Offset:   &[]int{0}[0],
	}

	// Execute
	result, err := handler.GetActiveTrades(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Empty(t, result.Trades)
}

func TestEconomyHandler_ExecuteTrade_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()
	buyerID := uuid.New()
	sellerID := uuid.New()

	// Setup mock expectations for trade execution
	mock.ExpectBegin()

	// Check trade exists and is active
	rows := sqlmock.NewRows([]string{"id", "player_id", "price", "quantity"}).
		AddRow(tradeID, sellerID, 100.0, 2)
	mock.ExpectQuery("SELECT (.+) FROM trades WHERE id = \\$1 AND status = \\$2").
		WithArgs(tradeID, "active").
		WillReturnRows(rows)

	// Update buyer wallet
	mock.ExpectExec("UPDATE player_wallets SET (.+) WHERE player_id = \\$1").
		WithArgs(sqlmock.AnyArg(), buyerID).
		WillReturnResult(sqlmock.NewResult(1, 1))

	// Update seller wallet
	mock.ExpectExec("UPDATE player_wallets SET (.+) WHERE player_id = \\$1").
		WithArgs(sqlmock.AnyArg(), sellerID).
		WillReturnResult(sqlmock.NewResult(1, 1))

	// Update trade status
	mock.ExpectExec("UPDATE trades SET status = \\$1 WHERE id = \\$2").
		WithArgs("completed", tradeID).
		WillReturnResult(sqlmock.NewResult(1, 1))

	mock.ExpectCommit()

	// Create test request
	req := httptest.NewRequest("POST", "/trades/"+tradeID.String()+"/execute", nil)
	w := httptest.NewRecorder()

	request := &api.ExecuteTradeRequest{
		BuyerID:      buyerID,
		Quantity:     1,
	}

	// Execute
	result, err := handler.ExecuteTrade(w, req, tradeID, request)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
}

func TestEconomyHandler_ExecuteTrade_InsufficientFunds(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()
	buyerID := uuid.New()
	sellerID := uuid.New()

	// Setup mock expectations
	mock.ExpectBegin()

	// Check trade exists and is active
	rows := sqlmock.NewRows([]string{"id", "player_id", "price", "quantity"}).
		AddRow(tradeID, sellerID, 1000.0, 1) // Expensive trade
	mock.ExpectQuery("SELECT (.+) FROM trades WHERE id = \\$1 AND status = \\$2").
		WithArgs(tradeID, "active").
		WillReturnRows(rows)

	// Check buyer wallet - insufficient funds
	buyerRows := sqlmock.NewRows([]string{"eurodollars"}).
		AddRow(50.0) // Only 50, but trade costs 1000
	mock.ExpectQuery("SELECT eurodollars FROM player_wallets WHERE player_id = \\$1").
		WithArgs(buyerID).
		WillReturnRows(buyerRows)

	mock.ExpectRollback()

	// Create test request
	req := httptest.NewRequest("POST", "/trades/"+tradeID.String()+"/execute", nil)
	w := httptest.NewRecorder()

	request := &api.ExecuteTradeRequest{
		BuyerID:      buyerID,
		Quantity:     1,
	}

	// Execute
	result, err := handler.ExecuteTrade(w, req, tradeID, request)

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

// Integration test for full trade workflow
func TestEconomyHandler_Integration_TradeWorkflow(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()
	tradeID := uuid.New()

	// Step 1: Create trade listing
	mock.ExpectBegin()
	mock.ExpectExec("INSERT INTO trades").
		WithArgs(sqlmock.AnyArg(), playerID, sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
		WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	req1 := httptest.NewRequest("POST", "/trades", nil)
	w1 := httptest.NewRecorder()

	createRequest := &api.CreateTradeRequest{
		ItemID:   uuid.New(),
		Price:    100.0,
		Quantity: 1,
	}

	trade, err := handler.CreateTradeListing(w1, req1, createRequest)
	require.NoError(t, err)
	assert.NotNil(t, trade)

	// Step 2: Get trade details
	rows := sqlmock.NewRows([]string{"id", "player_id", "item_id", "price", "quantity", "status", "created_at"}).
		AddRow(trade.ID, playerID, createRequest.ItemID, createRequest.Price, createRequest.Quantity, "active", time.Now())

	mock.ExpectQuery("SELECT (.+) FROM trades WHERE id = \\$1").
		WithArgs(trade.ID).
		WillReturnRows(rows)

	req2 := httptest.NewRequest("GET", "/trades/"+trade.ID.String(), nil)
	w2 := httptest.NewRecorder()

	details, err := handler.GetTradeDetails(w2, req2, trade.ID)
	assert.NoError(t, err)
	assert.Equal(t, trade.ID, details.ID)

	// Step 3: Cancel trade
	mock.ExpectBegin()
	mock.ExpectExec("UPDATE trades SET status = \\$1 WHERE id = \\$2 AND status = \\$3").
		WithArgs("cancelled", trade.ID, "active").
		WillReturnResult(sqlmock.NewResult(1, 1))
	mock.ExpectCommit()

	req3 := httptest.NewRequest("DELETE", "/trades/"+trade.ID.String(), nil)
	w3 := httptest.NewRecorder()

	cancelResult, err := handler.CancelTrade(w3, req3, trade.ID)
	assert.NoError(t, err)
	assert.True(t, cancelResult.Success)
}
