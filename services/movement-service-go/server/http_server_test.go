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
	"github.com/necpgame/movement-service-go/models"
)

type mockMovementService struct {
	positions      map[uuid.UUID]*models.CharacterPosition
	positionHistory map[uuid.UUID][]models.PositionHistory
	getErr         error
	saveErr        error
	historyErr     error
}

func (m *mockMovementService) GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	position, ok := m.positions[characterID]
	if !ok {
		return nil, errors.New("position not found")
	}
	return position, nil
}

func (m *mockMovementService) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	if m.saveErr != nil {
		return nil, m.saveErr
	}

	position := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   req.PositionX,
		PositionY:   req.PositionY,
		PositionZ:   req.PositionZ,
		Yaw:         req.Yaw,
		VelocityX:   req.VelocityX,
		VelocityY:   req.VelocityY,
		VelocityZ:   req.VelocityZ,
		UpdatedAt:   time.Now(),
		CreatedAt:   time.Now(),
	}

	m.positions[characterID] = position
	return position, nil
}

func (m *mockMovementService) GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error) {
	if m.historyErr != nil {
		return nil, m.historyErr
	}

	history, ok := m.positionHistory[characterID]
	if !ok {
		history = []models.PositionHistory{}
	}

	if limit > len(history) {
		limit = len(history)
	}

	if limit <= 0 {
		return []models.PositionHistory{}, nil
	}

	return history[:limit], nil
}

func TestHTTPServer_GetPosition(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	characterID := uuid.New()
	position := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   100.0,
		PositionY:   200.0,
		PositionZ:   300.0,
		Yaw:         45.0,
		VelocityX:    1.0,
		VelocityY:    0.0,
		VelocityZ:    0.0,
		UpdatedAt:   time.Now(),
		CreatedAt:   time.Now(),
	}

	mockService.positions[characterID] = position

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/position", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.CharacterPosition
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.CharacterID != characterID {
		t.Errorf("Expected character_id %s, got %s", characterID, response.CharacterID)
	}

	if response.PositionX != 100.0 {
		t.Errorf("Expected position_x 100.0, got %f", response.PositionX)
	}
}

func TestHTTPServer_GetPositionNotFound(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/position", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusNotFound, w.Code, w.Body.String())
	}
}

func TestHTTPServer_SavePosition(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockService)

	reqBody := map[string]interface{}{
		"position_x": 150.0,
		"position_y": 250.0,
		"position_z": 350.0,
		"yaw":        90.0,
		"velocity_x": 2.0,
		"velocity_y": 0.0,
		"velocity_z": 0.0,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/movement/"+characterID.String()+"/position", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response models.CharacterPosition
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.CharacterID != characterID {
		t.Errorf("Expected character_id %s, got %s", characterID, response.CharacterID)
	}

	if response.PositionX != 150.0 {
		t.Errorf("Expected position_x 150.0, got %f", response.PositionX)
	}
}

func TestHTTPServer_SavePositionInvalidRequest(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("POST", "/api/v1/movement/"+characterID.String()+"/position", bytes.NewBufferString("invalid json"))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusBadRequest, w.Code, w.Body.String())
	}
}

func TestHTTPServer_GetPositionHistory(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	characterID := uuid.New()
	history1 := models.PositionHistory{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   100.0,
		PositionY:   200.0,
		PositionZ:   300.0,
		Yaw:         45.0,
		CreatedAt:   time.Now().Add(-2 * time.Hour),
	}

	history2 := models.PositionHistory{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   150.0,
		PositionY:   250.0,
		PositionZ:   350.0,
		Yaw:         90.0,
		CreatedAt:   time.Now().Add(-1 * time.Hour),
	}

	history3 := models.PositionHistory{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   200.0,
		PositionY:   300.0,
		PositionZ:   400.0,
		Yaw:         135.0,
		CreatedAt:   time.Now(),
	}

	mockService.positionHistory[characterID] = []models.PositionHistory{history1, history2, history3}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history?limit=2", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response []models.PositionHistory
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if len(response) != 2 {
		t.Errorf("Expected 2 history entries, got %d", len(response))
	}
}

func TestHTTPServer_GetPositionHistoryEmpty(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/movement/"+characterID.String()+"/history", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response []models.PositionHistory
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if len(response) != 0 {
		t.Errorf("Expected 0 history entries, got %d", len(response))
	}
}

func TestHTTPServer_GetPositionInvalidCharacterID(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/movement/invalid-id/position", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusBadRequest, w.Code, w.Body.String())
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockMovementService{
		positions:       make(map[uuid.UUID]*models.CharacterPosition),
		positionHistory: make(map[uuid.UUID][]models.PositionHistory),
	}

	server := NewHTTPServer(":8080", mockService)

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

