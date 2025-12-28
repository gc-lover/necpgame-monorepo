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

	"economy-service-go/pkg/api"
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

func TestEconomyHandler_GetEconomyOverview_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock expectations for economy overview query
	rows := sqlmock.NewRows([]string{"total_active_trades", "total_market_volume", "average_trade_price", "active_sellers", "active_buyers"}).
		AddRow(150, 75000.50, 500.33, 45, 62)

	mock.ExpectQuery("SELECT (.+) FROM active_trades WHERE status = 'active' AND created_at >= NOW\\(\\) - INTERVAL '24 hours'").
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/economy/overview", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetEconomyOverview(w, req, api.GetEconomyOverviewParams{})

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, int32(150), result.TotalActiveTrades)
	assert.Equal(t, 75000.50, result.TotalMarketVolume)
	assert.Equal(t, 500.33, result.AverageTradePrice)
	assert.Equal(t, int32(45), result.ActiveSellers)
	assert.Equal(t, int32(62), result.ActiveBuyers)
	assert.True(t, result.LastUpdated.IsSet())
}

func TestEconomyHandler_GetEconomyOverview_DatabaseError(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Setup mock to return error
	mock.ExpectQuery("SELECT (.+) FROM active_trades WHERE status = 'active' AND created_at >= NOW\\(\\) - INTERVAL '24 hours'").
		WillReturnError(sql.ErrNoRows)

	// Create test request
	req := httptest.NewRequest("GET", "/economy/overview", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetEconomyOverview(w, req, api.GetEconomyOverviewParams{})

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_GetCharacterInventory_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	characterID := uuid.New()
	itemID1 := uuid.New()
	itemID2 := uuid.New()
	createdAt := time.Now()
	updatedAt := time.Now()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"item_id", "name", "quantity", "item_type", "rarity", "value", "created_at", "updated_at"}).
		AddRow(itemID1, "Steel Sword", 1, "weapon", "common", 150.0, createdAt, updatedAt).
		AddRow(itemID2, "Health Potion", 5, "consumable", "common", 25.0, createdAt, updatedAt)

	mock.ExpectQuery("SELECT (.+) FROM character_inventory (.+) WHERE (.+) character_id = \\$1").
		WithArgs(characterID).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/characters/"+characterID.String()+"/inventory", nil)
	w := httptest.NewRecorder()

	params := api.GetCharacterInventoryParams{
		CharacterID: characterID,
	}

	// Execute
	result, err := handler.GetCharacterInventory(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Len(t, result.Items, 2)
	assert.Equal(t, 2, result.TotalItems)
	assert.True(t, result.LastUpdated.IsSet())

	// Check first item
	assert.Equal(t, itemID1, result.Items[0].ItemID)
	assert.Equal(t, "Steel Sword", result.Items[0].Name)
	assert.Equal(t, int32(1), result.Items[0].Quantity)
	assert.Equal(t, "weapon", result.Items[0].ItemType)
	assert.Equal(t, "common", result.Items[0].Rarity)
	assert.Equal(t, 150.0, result.Items[0].Value)
}

func TestEconomyHandler_GetCharacterInventory_Empty(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	characterID := uuid.New()

	// Setup mock to return empty result
	mock.ExpectQuery("SELECT (.+) FROM character_inventory (.+) WHERE (.+) character_id = \\$1").
		WithArgs(characterID).
		WillReturnRows(sqlmock.NewRows([]string{}))

	// Create test request
	req := httptest.NewRequest("GET", "/characters/"+characterID.String()+"/inventory", nil)
	w := httptest.NewRecorder()

	params := api.GetCharacterInventoryParams{
		CharacterID: characterID,
	}

	// Execute
	result, err := handler.GetCharacterInventory(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Empty(t, result.Items)
	assert.Equal(t, 0, result.TotalItems)
	assert.True(t, result.LastUpdated.IsSet())
}

func TestEconomyHandler_CreateTrade_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	sellerID := uuid.New()
	itemID := uuid.New()

	// Setup mock expectations
	mock.ExpectBegin()

	// Check inventory availability
	mock.ExpectQuery("SELECT quantity FROM character_inventory WHERE character_id = \\$1 AND item_id = \\$2").
		WithArgs(sellerID, itemID).
		WillReturnRows(sqlmock.NewRows([]string{"quantity"}).AddRow(10))

	// Insert trade
	mock.ExpectExec("INSERT INTO active_trades").
		WithArgs(sqlmock.AnyArg(), sellerID, itemID, sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg(), sqlmock.AnyArg()).
		WillReturnResult(sqlmock.NewResult(1, 1))

	// Update inventory
	mock.ExpectExec("UPDATE character_inventory SET quantity = quantity - \\$1 WHERE character_id = \\$2 AND item_id = \\$3").
		WithArgs(3, sellerID, itemID).
		WillReturnResult(sqlmock.NewResult(1, 1))

	mock.ExpectCommit()

	// Create test request
	req := httptest.NewRequest("POST", "/trades", nil)
	w := httptest.NewRecorder()

	request := &api.CreateTradeRequest{
		SellerID:     sellerID,
		ItemID:       itemID,
		Quantity:     3,
		PricePerUnit: 100.0,
		TradeType:    "direct",
	}

	// Execute
	result, err := handler.CreateTrade(w, req, request)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.NotEqual(t, uuid.Nil, result.TradeID)
	assert.Equal(t, sellerID, result.SellerID)
	assert.Equal(t, itemID, result.ItemID)
	assert.Equal(t, 3, result.Quantity)
	assert.Equal(t, 100.0, result.PricePerUnit)
	assert.Equal(t, 300.0, result.TotalPrice)
	assert.Equal(t, "active", result.Status)
}

func TestEconomyHandler_CreateTrade_InsufficientInventory(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	sellerID := uuid.New()
	itemID := uuid.New()

	// Setup mock expectations
	mock.ExpectBegin()

	// Check inventory - insufficient quantity
	mock.ExpectQuery("SELECT quantity FROM character_inventory WHERE character_id = \\$1 AND item_id = \\$2").
		WithArgs(sellerID, itemID).
		WillReturnRows(sqlmock.NewRows([]string{"quantity"}).AddRow(2)) // Only 2 items, but want to trade 5

	mock.ExpectRollback()

	// Create test request
	req := httptest.NewRequest("POST", "/trades", nil)
	w := httptest.NewRecorder()

	request := &api.CreateTradeRequest{
		SellerID:     sellerID,
		ItemID:       itemID,
		Quantity:     5, // Want 5, but only have 2
		PricePerUnit: 100.0,
		TradeType:    "direct",
	}

	// Execute
	result, err := handler.CreateTrade(w, req, request)

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_GetCraftingRecipes_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	recipeID1 := uuid.New()
	recipeID2 := uuid.New()
	resultItemID := uuid.New()
	createdAt := time.Now()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"recipe_id", "name", "description", "result_item_id", "result_quantity", "crafting_time", "skill_required", "difficulty", "created_at"}).
		AddRow(recipeID1, "Iron Sword", "A sturdy iron sword", resultItemID, 1, 300, "blacksmith", "intermediate", createdAt).
		AddRow(recipeID2, "Steel Armor", "Durable steel armor", resultItemID, 1, 600, "armorsmith", "advanced", createdAt)

	mock.ExpectQuery("SELECT (.+) FROM crafting_recipes (.+) WHERE 1=1 (.+) ORDER BY (.+) LIMIT (.+)").
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/crafting/recipes", nil)
	w := httptest.NewRecorder()

	params := api.GetCraftingRecipesParams{
		Limit: api.NewOptInt(50),
	}

	// Execute
	result, err := handler.GetCraftingRecipes(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Recipes, 2)
	assert.Equal(t, 2, result.TotalCount)
	assert.True(t, result.Limit.IsSet())
	assert.Equal(t, 50, result.Limit.Value)

	// Check first recipe
	assert.Equal(t, recipeID1, result.Recipes[0].RecipeID)
	assert.Equal(t, "Iron Sword", result.Recipes[0].Name)
	assert.Equal(t, "A sturdy iron sword", result.Recipes[0].Description)
	assert.Equal(t, resultItemID, result.Recipes[0].ResultItemID)
	assert.Equal(t, int32(1), result.Recipes[0].ResultQuantity)
	assert.Equal(t, int32(300), result.Recipes[0].CraftingTime)
	assert.Equal(t, "blacksmith", result.Recipes[0].SkillRequired)
	assert.Equal(t, "intermediate", result.Recipes[0].Difficulty)
}

func TestEconomyHandler_GetCraftingRecipes_WithFilters(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	recipeID := uuid.New()
	resultItemID := uuid.New()
	createdAt := time.Now()

	// Setup mock expectations with skill filter
	rows := sqlmock.NewRows([]string{"recipe_id", "name", "description", "result_item_id", "result_quantity", "crafting_time", "skill_required", "difficulty", "created_at"}).
		AddRow(recipeID, "Iron Sword", "A sturdy iron sword", resultItemID, 1, 300, "blacksmith", "intermediate", createdAt)

	mock.ExpectQuery("SELECT (.+) FROM crafting_recipes (.+) WHERE 1=1 AND r.skill_required = \\$1 (.+) ORDER BY (.+) LIMIT (.+)").
		WithArgs("blacksmith").
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/crafting/recipes?skill_required=blacksmith", nil)
	w := httptest.NewRecorder()

	params := api.GetCraftingRecipesParams{
		SkillRequired: api.NewOptString("blacksmith"),
		Limit:         api.NewOptInt(50),
	}

	// Execute
	result, err := handler.GetCraftingRecipes(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Recipes, 1)
	assert.Equal(t, "Iron Sword", result.Recipes[0].Name)
	assert.Equal(t, "blacksmith", result.Recipes[0].SkillRequired)
}

func TestEconomyHandler_GetCurrencies_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	// Create test request
	req := httptest.NewRequest("GET", "/currencies", nil)
	w := httptest.NewRecorder()

	// Execute
	result, err := handler.GetCurrencies(w, req)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Currencies, 3)
	assert.Equal(t, 3, result.TotalCount)
	assert.True(t, result.LastUpdate.IsSet())

	// Check currencies
	assert.Equal(t, "ED", result.Currencies[0].Code)
	assert.Equal(t, "Eurodollar", result.Currencies[0].Name)
	assert.Equal(t, "€", result.Currencies[0].Symbol)
	assert.Equal(t, "fiat", result.Currencies[0].Type)
	assert.Equal(t, 1.0, result.Currencies[0].ExchangeRate)
	assert.Equal(t, "Primary currency for player transactions", result.Currencies[0].Description)

	assert.Equal(t, "CRYPTO", result.Currencies[1].Code)
	assert.Equal(t, "Cryptocurrency", result.Currencies[1].Name)
	assert.Equal(t, "₿", result.Currencies[1].Symbol)
	assert.Equal(t, "crypto", result.Currencies[1].Type)
	assert.Equal(t, 0.85, result.Currencies[1].ExchangeRate)

	assert.Equal(t, "REP", result.Currencies[2].Code)
	assert.Equal(t, "Reputation Points", result.Currencies[2].Name)
	assert.Equal(t, "★", result.Currencies[2].Symbol)
	assert.Equal(t, "reputation", result.Currencies[2].Type)
	assert.Equal(t, 0.0, result.Currencies[2].ExchangeRate)
}

func TestEconomyHandler_ExecuteCrafting_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()
	recipeID := uuid.New()
	resultItemID := uuid.New()

	// Setup mock expectations
	mock.ExpectBegin()

	// Get recipe details
	rows := sqlmock.NewRows([]string{"recipe_id", "name", "result_item_id", "result_quantity", "crafting_time"}).
		AddRow(recipeID, "Iron Sword", resultItemID, 1, 300)
	mock.ExpectQuery("SELECT (.+) FROM crafting_recipes WHERE recipe_id = \\$1").
		WithArgs(recipeID).
		WillReturnRows(rows)

	// Update inventory
	mock.ExpectExec("INSERT INTO character_inventory (.+) VALUES (.+) ON CONFLICT (.+) DO UPDATE SET (.+)").
		WithArgs(playerID, resultItemID, 1).
		WillReturnResult(sqlmock.NewResult(1, 1))

	// Log transaction
	mock.ExpectExec("INSERT INTO transaction_history (.+) VALUES (.+)").
		WithArgs(sqlmock.AnyArg(), playerID, "crafting", 0, sqlmock.AnyArg()).
		WillReturnResult(sqlmock.NewResult(1, 1))

	mock.ExpectCommit()

	// Create test request
	req := httptest.NewRequest("POST", "/crafting/execute", nil)
	w := httptest.NewRecorder()

	request := &api.ExecuteCraftingRequest{
		PlayerId: playerID,
		RecipeId: recipeID,
	}

	// Execute
	result, err := handler.ExecuteCrafting(w, req, request)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, recipeID, result.RecipeId)
	assert.Equal(t, resultItemID, result.ResultItemId)
	assert.Equal(t, 1, result.Quantity)
	assert.Equal(t, int32(300), result.CraftingTime)
	assert.True(t, result.Success)
	assert.True(t, result.CompletedAt.IsSet())
}

func TestEconomyHandler_ExecuteCrafting_RecipeNotFound(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()
	recipeID := uuid.New()

	// Setup mock to return no rows
	mock.ExpectBegin()
	mock.ExpectQuery("SELECT (.+) FROM crafting_recipes WHERE recipe_id = \\$1").
		WithArgs(recipeID).
		WillReturnError(sql.ErrNoRows)
	mock.ExpectRollback()

	// Create test request
	req := httptest.NewRequest("POST", "/crafting/execute", nil)
	w := httptest.NewRecorder()

	request := &api.ExecuteCraftingRequest{
		PlayerId: playerID,
		RecipeId: recipeID,
	}

	// Execute
	result, err := handler.ExecuteCrafting(w, req, request)

	// Verify
	assert.Error(t, err)
	assert.Nil(t, result)
}

func TestEconomyHandler_GetMarketPrices_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	itemID1 := uuid.New()
	itemID2 := uuid.New()

	// Setup mock expectations
	rows := sqlmock.NewRows([]string{"item_id", "avg_price", "max_price", "min_price", "trade_count"}).
		AddRow(itemID1, 150.50, 200.00, 100.00, 25).
		AddRow(itemID2, 75.25, 90.00, 60.00, 15)

	mock.ExpectQuery("SELECT (.+) FROM active_trades (.+) WHERE (.+) created_at >= \\$1 (.+) GROUP BY item_id (.+) ORDER BY (.+)").
		WithArgs(sqlmock.AnyArg()).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/market/prices", nil)
	w := httptest.NewRecorder()

	params := api.GetMarketPricesParams{
		TimeWindow: api.NewOptGetMarketPricesParamsTimeWindow(api.GetMarketPricesParamsTimeWindow("24h")),
	}

	// Execute
	result, err := handler.GetMarketPrices(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Prices, 2)
	assert.Equal(t, "24h", result.TimeWindow)
	assert.True(t, result.LastUpdate.IsSet())

	// Check first price
	assert.Equal(t, itemID1, result.Prices[0].ItemId)
	assert.Equal(t, 150.50, result.Prices[0].AveragePrice)
	assert.Equal(t, 200.00, result.Prices[0].MaxPrice)
	assert.Equal(t, 100.00, result.Prices[0].MinPrice)
	assert.Equal(t, int32(25), result.Prices[0].TradeCount)
}

func TestEconomyHandler_GetMarketPrices_CustomTimeWindow(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	itemID := uuid.New()

	// Setup mock expectations for 7d window
	rows := sqlmock.NewRows([]string{"item_id", "avg_price", "max_price", "min_price", "trade_count"}).
		AddRow(itemID, 300.00, 350.00, 250.00, 50)

	mock.ExpectQuery("SELECT (.+) FROM active_trades (.+) WHERE (.+) created_at >= \\$1 (.+) GROUP BY item_id (.+) ORDER BY (.+)").
		WithArgs(sqlmock.AnyArg()).
		WillReturnRows(rows)

	// Create test request
	req := httptest.NewRequest("GET", "/market/prices?time_window=7d", nil)
	w := httptest.NewRecorder()

	params := api.GetMarketPricesParams{
		TimeWindow: api.NewOptGetMarketPricesParamsTimeWindow(api.GetMarketPricesParamsTimeWindow("7d")),
	}

	// Execute
	result, err := handler.GetMarketPrices(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Prices, 1)
	assert.Equal(t, "7d", result.TimeWindow)
	assert.Equal(t, 300.00, result.Prices[0].AveragePrice)
}

func TestEconomyHandler_GetPlayerEconomicStats_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock expectations for stats query
	statsRows := sqlmock.NewRows([]string{"purchases", "sales", "total_spent", "total_earned", "avg_sale_price", "highest_sale", "active_trading_days"}).
		AddRow(15, 8, 2500.50, 1800.75, 225.09, 350.00, 12)

	mock.ExpectQuery("SELECT (.+) FROM transaction_history WHERE player_id = \\$1 AND created_at >= \\$2").
		WithArgs(playerID, sqlmock.AnyArg()).
		WillReturnRows(statsRows)

	// Setup mock for wallet query
	walletRows := sqlmock.NewRows([]string{"eurodollars", "cryptocurrency", "reputation_points"}).
		AddRow(5000.25, 1200.50, 150)

	mock.ExpectQuery("SELECT eurodollars, cryptocurrency, reputation_points FROM player_wallets WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnRows(walletRows)

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/stats", nil)
	w := httptest.NewRecorder()

	params := api.GetPlayerEconomicStatsParams{
		PlayerId: playerID,
	}

	// Execute
	result, err := handler.GetPlayerEconomicStats(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerId)
	assert.Equal(t, "30d", result.TimeWindow)
	assert.Equal(t, int32(15), result.TotalPurchases)
	assert.Equal(t, int32(8), result.TotalSales)
	assert.Equal(t, 2500.50, result.TotalSpent)
	assert.Equal(t, 1800.75, result.TotalEarned)
	assert.Equal(t, 225.09, result.AverageSalePrice)
	assert.Equal(t, 350.00, result.HighestSale)
	assert.Equal(t, int32(12), result.ActiveTradingDays)

	// Check wallet
	assert.NotNil(t, result.CurrentBalance)
	assert.Equal(t, 5000.25, result.CurrentBalance.Eurodollars)
	assert.Equal(t, 1200.50, result.CurrentBalance.Cryptocurrency)
	assert.Equal(t, int32(150), result.CurrentBalance.ReputationPoints)
	assert.True(t, result.LastCalculated.IsSet())
}

func TestEconomyHandler_GetPlayerEconomicStats_NoWallet(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	playerID := uuid.New()

	// Setup mock expectations for stats query
	statsRows := sqlmock.NewRows([]string{"purchases", "sales", "total_spent", "total_earned", "avg_sale_price", "highest_sale", "active_trading_days"}).
		AddRow(5, 3, 800.00, 600.00, 200.00, 250.00, 8)

	mock.ExpectQuery("SELECT (.+) FROM transaction_history WHERE player_id = \\$1 AND created_at >= \\$2").
		WithArgs(playerID, sqlmock.AnyArg()).
		WillReturnRows(statsRows)

	// Setup mock for wallet query - no wallet found
	mock.ExpectQuery("SELECT eurodollars, cryptocurrency, reputation_points FROM player_wallets WHERE player_id = \\$1").
		WithArgs(playerID).
		WillReturnError(sql.ErrNoRows)

	// Create test request
	req := httptest.NewRequest("GET", "/players/"+playerID.String()+"/stats", nil)
	w := httptest.NewRecorder()

	params := api.GetPlayerEconomicStatsParams{
		PlayerId: playerID,
	}

	// Execute
	result, err := handler.GetPlayerEconomicStats(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, playerID, result.PlayerId)
	assert.Equal(t, int32(5), result.TotalPurchases)
	assert.Equal(t, int32(3), result.TotalSales)
	// Wallet should be nil when not found
	assert.Nil(t, result.CurrentBalance)
	assert.True(t, result.LastCalculated.IsSet())
}

func TestEconomyHandler_GetBulkMarketData_Success(t *testing.T) {
	handler, mock := setupTestHandler()
	defer mock.ExpectClose()

	tradeID := uuid.New()
	sellerID := uuid.New()
	buyerID := uuid.New()
	itemID := uuid.New()
	transactionID := uuid.New()
	playerID := uuid.New()
	auctionID := uuid.New()
	auctionSellerID := uuid.New()

	// Setup mock for active trades
	tradeRows := sqlmock.NewRows([]string{"trade_id", "seller_id", "buyer_id", "item_id", "quantity", "price_per_unit", "total_price", "trade_type", "status", "created_at", "expires_at"}).
		AddRow(tradeID, sellerID, buyerID, itemID, 5, 100.0, 500.0, "direct", "active", time.Now(), nil)

	mock.ExpectQuery("SELECT (.+) FROM active_trades WHERE status = 'active' ORDER BY created_at DESC LIMIT 100").
		WillReturnRows(tradeRows)

	// Setup mock for recent transactions
	transactionRows := sqlmock.NewRows([]string{"transaction_id", "player_id", "transaction_type", "amount", "currency_type", "description", "created_at"}).
		AddRow(transactionID, playerID, "purchase", 250.0, "eddies", "Item purchase", time.Now())

	mock.ExpectQuery("SELECT (.+) FROM transaction_history (.+) WHERE (.+) created_at >= NOW\\(\\) - INTERVAL '24 hours' (.+) ORDER BY (.+) LIMIT 500").
		WillReturnRows(transactionRows)

	// Setup mock for market prices
	priceRows := sqlmock.NewRows([]string{"item_id", "avg_price", "trade_count"}).
		AddRow(itemID, 125.50, 15)

	mock.ExpectQuery("SELECT (.+) FROM active_trades (.+) WHERE (.+) status = 'completed' (.+) GROUP BY item_id ORDER BY trade_count DESC LIMIT 200").
		WillReturnRows(priceRows)

	// Setup mock for auction summaries
	auctionRows := sqlmock.NewRows([]string{"id", "item_id", "seller_id", "current_bidder_id", "expires_at", "status", "currency", "start_price", "current_bid", "buyout_price", "quantity", "bid_count"}).
		AddRow(auctionID, itemID, auctionSellerID, nil, time.Now().Add(time.Hour), "active", "eddies", 100, 150, nil, 1, 3)

	mock.ExpectQuery("SELECT (.+) FROM auctions WHERE status = 'active' ORDER BY expires_at ASC LIMIT 100").
		WillReturnRows(auctionRows)

	// Create test request
	req := httptest.NewRequest("GET", "/market/bulk", nil)
	w := httptest.NewRecorder()

	params := api.GetBulkMarketDataParams{}

	// Execute
	result, err := handler.GetBulkMarketData(w, req, params)

	// Verify
	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.ActiveTrades, 1)
	assert.Len(t, result.RecentTransactions, 1)
	assert.Len(t, result.MarketPrices, 1)
	assert.Len(t, result.AuctionSummaries, 1)
	assert.True(t, result.LastUpdate.IsSet())

	// Check trade
	assert.Equal(t, tradeID, result.ActiveTrades[0].TradeID)
	assert.Equal(t, sellerID, result.ActiveTrades[0].SellerID)
	assert.Equal(t, 500.0, result.ActiveTrades[0].TotalPrice)

	// Check transaction
	assert.Equal(t, transactionID, result.RecentTransactions[0].TransactionID)
	assert.Equal(t, playerID, result.RecentTransactions[0].PlayerID)
	assert.Equal(t, 250.0, result.RecentTransactions[0].Amount)

	// Check price
	assert.Equal(t, itemID, result.MarketPrices[0].ItemId)
	assert.Equal(t, 125.50, result.MarketPrices[0].AveragePrice)
	assert.Equal(t, int32(15), result.MarketPrices[0].TradeCount)

	// Check auction
	assert.Equal(t, auctionID, result.AuctionSummaries[0].Id)
	assert.Equal(t, auctionSellerID, result.AuctionSummaries[0].SellerId)
	assert.Equal(t, int64(150), result.AuctionSummaries[0].CurrentBid)
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
