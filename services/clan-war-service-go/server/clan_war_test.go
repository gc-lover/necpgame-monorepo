package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
)

type MockClanWarService struct{}

func (m *MockClanWarService) DeclareWar(ctx context.Context, req *models.DeclareWarRequest) (*models.ClanWar, error) {
	return &models.ClanWar{
		ID:              uuid.New(),
		AttackerGuildID: req.AttackerGuildID,
		DefenderGuildID: req.DefenderGuildID,
		Status:          models.WarStatusDeclared,
	}, nil
}

func (m *MockClanWarService) GetWar(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error) {
	return &models.ClanWar{
		ID:     warID,
		Status: models.WarStatusOngoing,
	}, nil
}

func (m *MockClanWarService) ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error) {
	return []models.ClanWar{}, 0, nil
}

func (m *MockClanWarService) StartWar(ctx context.Context, warID uuid.UUID) error {
	return nil
}

func (m *MockClanWarService) CompleteWar(ctx context.Context, warID uuid.UUID) error {
	return nil
}

func (m *MockClanWarService) CreateBattle(ctx context.Context, req *models.CreateBattleRequest) (*models.WarBattle, error) {
	return &models.WarBattle{
		ID:    uuid.New(),
		WarID: req.WarID,
	}, nil
}

func (m *MockClanWarService) GetBattle(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error) {
	return &models.WarBattle{
		ID: battleID,
	}, nil
}

func (m *MockClanWarService) ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error) {
	return []models.WarBattle{}, 0, nil
}

func (m *MockClanWarService) StartBattle(ctx context.Context, battleID uuid.UUID) error {
	return nil
}

func (m *MockClanWarService) UpdateBattleScore(ctx context.Context, req *models.UpdateBattleScoreRequest) error {
	return nil
}

func (m *MockClanWarService) CompleteBattle(ctx context.Context, battleID uuid.UUID) error {
	return nil
}

func (m *MockClanWarService) GetTerritory(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error) {
	return &models.Territory{
		ID: territoryID,
	}, nil
}

func (m *MockClanWarService) ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error) {
	return []models.Territory{}, 0, nil
}

func TestNewHTTPServer(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	if server == nil {
		t.Fatal("Expected server to be created")
	}
	
	if server.router == nil {
		t.Error("Expected router to be initialized")
	}
}

func TestHealthCheck(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestDeclareWar(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	reqBody := models.DeclareWarRequest{
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
	}
	
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/clan-war/wars", bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusCreated {
		t.Errorf("Expected status 201, got %d", w.Code)
	}
	
	var war models.ClanWar
	if err := json.NewDecoder(w.Body).Decode(&war); err != nil {
		t.Fatalf("Failed to decode response: %v", err)
	}
	
	if war.Status != models.WarStatusDeclared {
		t.Errorf("Expected status Declared, got %v", war.Status)
	}
}

func TestGetWar(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	warID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/clan-war/wars/"+warID.String(), nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
	
	var war models.ClanWar
	if err := json.NewDecoder(w.Body).Decode(&war); err != nil {
		t.Fatalf("Failed to decode response: %v", err)
	}
}

func TestListWars(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/api/v1/clan-war/wars", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestStartWar(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	warID := uuid.New()
	req := httptest.NewRequest("POST", "/api/v1/clan-war/wars/"+warID.String()+"/start", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCompleteWar(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	warID := uuid.New()
	req := httptest.NewRequest("POST", "/api/v1/clan-war/wars/"+warID.String()+"/complete", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCreateBattle(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	reqBody := models.CreateBattleRequest{
		WarID: uuid.New(),
	}
	
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/clan-war/battles", bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusCreated {
		t.Errorf("Expected status 201, got %d", w.Code)
	}
}

func TestGetBattle(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	battleID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/clan-war/battles/"+battleID.String(), nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestListBattles(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/api/v1/clan-war/battles", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestUpdateBattleScore(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	battleID := uuid.New()
	reqBody := models.UpdateBattleScoreRequest{
		BattleID:      battleID,
		AttackerScore: 100,
		DefenderScore: 90,
	}
	
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/clan-war/battles/"+battleID.String()+"/score", bytes.NewReader(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestGetTerritory(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	territoryID := uuid.New()
	req := httptest.NewRequest("GET", "/api/v1/clan-war/territories/"+territoryID.String(), nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestListTerritories(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/api/v1/clan-war/territories", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Code != http.StatusOK {
		t.Errorf("Expected status OK, got %d", w.Code)
	}
}

func TestCORSHeaders(t *testing.T) {
	service := &MockClanWarService{}
	server := NewHTTPServer(":8080", service, nil, false)
	
	req := httptest.NewRequest("GET", "/health", nil)
	w := httptest.NewRecorder()
	
	server.router.ServeHTTP(w, req)
	
	if w.Header().Get("Access-Control-Allow-Origin") != "*" {
		t.Error("Expected CORS headers to be set")
	}
}

