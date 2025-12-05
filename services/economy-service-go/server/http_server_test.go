package server

import (
	"bytes"
	"context"
	"encoding/json"
	"errors"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
)

type mockTradeService struct {
	trades         map[uuid.UUID]*models.TradeSession
	activeTrades   map[uuid.UUID][]models.TradeSession
	tradeHistory   map[uuid.UUID]*models.TradeHistoryListResponse
	createErr      error
	getErr         error
	updateErr      error
	confirmErr     error
	completeErr    error
	cancelErr      error
}

func (m *mockTradeService) CreateTrade(ctx context.Context, initiatorID uuid.UUID, req *models.CreateTradeRequest) (*models.TradeSession, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}

	session := &models.TradeSession{
		ID:              uuid.New(),
		InitiatorID:     initiatorID,
		RecipientID:     req.RecipientID,
		Status:          models.TradeStatusPending,
		InitiatorConfirmed: false,
		RecipientConfirmed:  false,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	m.trades[session.ID] = session
	return session, nil
}

func (m *mockTradeService) GetTrade(ctx context.Context, id uuid.UUID) (*models.TradeSession, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	session, ok := m.trades[id]
	if !ok {
		return nil, errors.New("trade not found")
	}

	return session, nil
}

func (m *mockTradeService) GetActiveTrades(ctx context.Context, characterID uuid.UUID) ([]models.TradeSession, error) {
	trades, ok := m.activeTrades[characterID]
	if !ok {
		return []models.TradeSession{}, nil
	}
	return trades, nil
}

func (m *mockTradeService) UpdateOffer(ctx context.Context, sessionID, characterID uuid.UUID, req *models.UpdateTradeOfferRequest) (*models.TradeSession, error) {
	if m.updateErr != nil {
		return nil, m.updateErr
	}

	session, ok := m.trades[sessionID]
	if !ok {
		return nil, errors.New("trade not found")
	}

	if session.InitiatorID == characterID {
		session.InitiatorOffer = models.TradeOffer{
			Items:    req.Items,
			Currency: req.Currency,
		}
		session.InitiatorConfirmed = false
	} else if session.RecipientID == characterID {
		session.RecipientOffer = models.TradeOffer{
			Items:    req.Items,
			Currency: req.Currency,
		}
		session.RecipientConfirmed = false
	}

	session.UpdatedAt = time.Now()
	return session, nil
}

func (m *mockTradeService) ConfirmTrade(ctx context.Context, sessionID, characterID uuid.UUID) (*models.TradeSession, error) {
	if m.confirmErr != nil {
		return nil, m.confirmErr
	}

	session, ok := m.trades[sessionID]
	if !ok {
		return nil, errors.New("trade not found")
	}

	if session.InitiatorID == characterID {
		session.InitiatorConfirmed = true
	} else if session.RecipientID == characterID {
		session.RecipientConfirmed = true
	}

	if session.InitiatorConfirmed && session.RecipientConfirmed {
		session.Status = models.TradeStatusConfirmed
	}

	session.UpdatedAt = time.Now()
	return session, nil
}

func (m *mockTradeService) CompleteTrade(ctx context.Context, sessionID uuid.UUID) error {
	if m.completeErr != nil {
		return m.completeErr
	}

	session, ok := m.trades[sessionID]
	if !ok {
		return errors.New("trade not found")
	}

	if session.Status != models.TradeStatusConfirmed {
		return errors.New("trade is not confirmed")
	}

	session.Status = models.TradeStatusCompleted
	now := time.Now()
	session.CompletedAt = &now
	session.UpdatedAt = now
	return nil
}

func (m *mockTradeService) CancelTrade(ctx context.Context, sessionID, characterID uuid.UUID) error {
	if m.cancelErr != nil {
		return m.cancelErr
	}

	session, ok := m.trades[sessionID]
	if !ok {
		return errors.New("trade not found")
	}

	if session.InitiatorID != characterID && session.RecipientID != characterID {
		return errors.New("unauthorized")
	}

	session.Status = models.TradeStatusCancelled
	session.UpdatedAt = time.Now()
	return nil
}

func (m *mockTradeService) GetTradeHistory(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.TradeHistoryListResponse, error) {
	history, ok := m.tradeHistory[characterID]
	if !ok {
		return &models.TradeHistoryListResponse{
			History: []models.TradeHistory{},
			Total:   0,
		}, nil
	}
	return history, nil
}

func createRequestWithUserID(method, url string, body []byte, userID uuid.UUID) *http.Request {
	req := httptest.NewRequest(method, url, bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	ctx := context.WithValue(req.Context(), "user_id", userID.String())
	return req.WithContext(ctx)
}

func TestHTTPServer_CreateTrade(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	initiatorID := uuid.New()
	recipientID := uuid.New()
	reqBody := models.CreateTradeRequest{
		RecipientID: recipientID,
	}

	body, _ := json.Marshal(reqBody)
	req := createRequestWithUserID("POST", "/api/v1/economy/trade", body, initiatorID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var session models.TradeSession
	if err := json.Unmarshal(w.Body.Bytes(), &session); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if session.InitiatorID != initiatorID {
		t.Errorf("Expected initiator_id %s, got %s", initiatorID, session.InitiatorID)
	}

	if session.RecipientID != recipientID {
		t.Errorf("Expected recipient_id %s, got %s", recipientID, session.RecipientID)
	}
}

func TestHTTPServer_GetTrade(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	tradeID := uuid.New()
	session := &models.TradeSession{
		ID:              tradeID,
		InitiatorID:     uuid.New(),
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	mockService.trades[tradeID] = session

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := httptest.NewRequest("GET", "/api/v1/economy/trade/"+tradeID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.TradeSession
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != tradeID {
		t.Errorf("Expected trade_id %s, got %s", tradeID, response.ID)
	}
}

func TestHTTPServer_GetActiveTrades(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	characterID := uuid.New()
	trade1 := models.TradeSession{
		ID:              uuid.New(),
		InitiatorID:     characterID,
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	trade2 := models.TradeSession{
		ID:              uuid.New(),
		InitiatorID:     uuid.New(),
		RecipientID:     characterID,
		Status:          models.TradeStatusActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	mockService.activeTrades[characterID] = []models.TradeSession{trade1, trade2}

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := createRequestWithUserID("GET", "/api/v1/economy/trade", nil, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	// Response is JSON array, not ActiveTradesResponse
	var response []models.TradeSession
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if len(response) != 2 {
		t.Errorf("Expected 2 trades, got %d", len(response))
	}
}

func TestHTTPServer_UpdateOffer(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	tradeID := uuid.New()
	characterID := uuid.New()
	session := &models.TradeSession{
		ID:              tradeID,
		InitiatorID:     characterID,
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	mockService.trades[tradeID] = session

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	reqBody := models.UpdateTradeOfferRequest{
		Items: []map[string]interface{}{
			{"item_id": "item1", "quantity": 5},
		},
		Currency: map[string]int{
			"credits": 1000,
		},
	}

	body, _ := json.Marshal(reqBody)
	req := createRequestWithUserID("PUT", "/api/v1/economy/trade/"+tradeID.String()+"/offer", body, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_ConfirmTrade(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	tradeID := uuid.New()
	characterID := uuid.New()
	session := &models.TradeSession{
		ID:              tradeID,
		InitiatorID:     characterID,
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusActive,
		InitiatorConfirmed: false,
		RecipientConfirmed:  false,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	mockService.trades[tradeID] = session

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := createRequestWithUserID("POST", "/api/v1/economy/trade/"+tradeID.String()+"/confirm", nil, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_CompleteTrade(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	tradeID := uuid.New()
	session := &models.TradeSession{
		ID:              tradeID,
		InitiatorID:     uuid.New(),
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusConfirmed,
		InitiatorConfirmed: true,
		RecipientConfirmed:  true,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	mockService.trades[tradeID] = session

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := httptest.NewRequest("POST", "/api/v1/economy/trade/"+tradeID.String()+"/complete", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_CancelTrade(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	tradeID := uuid.New()
	characterID := uuid.New()
	session := &models.TradeSession{
		ID:              tradeID,
		InitiatorID:     characterID,
		RecipientID:     uuid.New(),
		Status:          models.TradeStatusActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
		ExpiresAt:       time.Now().Add(5 * time.Minute),
	}

	mockService.trades[tradeID] = session

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := createRequestWithUserID("POST", "/api/v1/economy/trade/"+tradeID.String()+"/cancel", nil, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetTradeHistory(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	characterID := uuid.New()
	history := &models.TradeHistoryListResponse{
		History: []models.TradeHistory{
			{
				ID:             uuid.New(),
				TradeSessionID: uuid.New(),
				InitiatorID:    characterID,
				RecipientID:    uuid.New(),
				Status:         models.TradeStatusCompleted,
				CreatedAt:      time.Now(),
				CompletedAt:    time.Now(),
			},
		},
		Total: 1,
	}

	mockService.tradeHistory[characterID] = history

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := createRequestWithUserID("GET", "/api/v1/economy/trade/history", nil, characterID)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
		return
	}

	var response models.TradeHistoryListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockTradeService{
		trades:       make(map[uuid.UUID]*models.TradeSession),
		activeTrades: make(map[uuid.UUID][]models.TradeSession),
		tradeHistory: make(map[uuid.UUID]*models.TradeHistoryListResponse),
	}

	server := NewHTTPServer(":8080", mockService, nil, false, nil, nil, nil)

	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response map[string]string
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response["status"] != "healthy" {
		t.Errorf("Expected status 'healthy', got %s", response["status"])
	}
}
