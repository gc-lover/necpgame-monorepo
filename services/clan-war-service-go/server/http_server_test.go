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
	"github.com/necpgame/clan-war-service-go/models"
)

type mockClanWarService struct {
	wars       map[uuid.UUID]*models.ClanWar
	battles    map[uuid.UUID]*models.WarBattle
	territories map[uuid.UUID]*models.Territory
	declareErr error
	getErr     error
	startErr   error
}

func (m *mockClanWarService) DeclareWar(ctx context.Context, req *models.DeclareWarRequest) (*models.ClanWar, error) {
	if m.declareErr != nil {
		return nil, m.declareErr
	}

	if req.AttackerGuildID == req.DefenderGuildID {
		return nil, errors.New("attacker and defender cannot be the same guild")
	}

	war := &models.ClanWar{
		ID:              uuid.New(),
		AttackerGuildID: req.AttackerGuildID,
		DefenderGuildID: req.DefenderGuildID,
		Allies:          req.Allies,
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
		TerritoryID:     req.TerritoryID,
		AttackerScore:   0,
		DefenderScore:   0,
		StartTime:       time.Now().Add(24 * time.Hour),
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	m.wars[war.ID] = war
	return war, nil
}

func (m *mockClanWarService) GetWar(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	war, ok := m.wars[warID]
	if !ok {
		return nil, errors.New("war not found")
	}

	return war, nil
}

func (m *mockClanWarService) ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error) {
	var wars []models.ClanWar
	for _, war := range m.wars {
		if guildID != nil && war.AttackerGuildID != *guildID && war.DefenderGuildID != *guildID {
			continue
		}
		if status != nil && war.Status != *status {
			continue
		}
		wars = append(wars, *war)
	}

	total := len(wars)
	if offset >= total {
		return []models.ClanWar{}, total, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return wars[offset:end], total, nil
}

func (m *mockClanWarService) StartWar(ctx context.Context, warID uuid.UUID) error {
	if m.startErr != nil {
		return m.startErr
	}

	war, ok := m.wars[warID]
	if !ok {
		return errors.New("war not found")
	}

	if war.Status != models.WarStatusDeclared {
		return errors.New("war is not in declared status")
	}

	war.Status = models.WarStatusOngoing
	war.Phase = models.WarPhaseActive
	war.UpdatedAt = time.Now()
	return nil
}

func (m *mockClanWarService) CompleteWar(ctx context.Context, warID uuid.UUID) error {
	war, ok := m.wars[warID]
	if !ok {
		return errors.New("war not found")
	}

	if war.Status != models.WarStatusOngoing {
		return errors.New("war is not in ongoing status")
	}

	var winnerGuildID *uuid.UUID
	if war.AttackerScore > war.DefenderScore {
		winnerGuildID = &war.AttackerGuildID
	} else if war.DefenderScore > war.AttackerScore {
		winnerGuildID = &war.DefenderGuildID
	}

	war.Status = models.WarStatusCompleted
	war.Phase = models.WarPhaseCompleted
	war.WinnerGuildID = winnerGuildID
	now := time.Now()
	war.EndTime = &now
	war.UpdatedAt = now
	return nil
}

func (m *mockClanWarService) CreateBattle(ctx context.Context, req *models.CreateBattleRequest) (*models.WarBattle, error) {
	battle := &models.WarBattle{
		ID:            uuid.New(),
		WarID:         req.WarID,
		Type:          req.Type,
		TerritoryID:   req.TerritoryID,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     req.StartTime,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	m.battles[battle.ID] = battle
	return battle, nil
}

func (m *mockClanWarService) GetBattle(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error) {
	battle, ok := m.battles[battleID]
	if !ok {
		return nil, errors.New("battle not found")
	}

	return battle, nil
}

func (m *mockClanWarService) ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error) {
	var battles []models.WarBattle
	for _, battle := range m.battles {
		if warID != nil && battle.WarID != *warID {
			continue
		}
		if status != nil && battle.Status != *status {
			continue
		}
		battles = append(battles, *battle)
	}

	total := len(battles)
	if offset >= total {
		return []models.WarBattle{}, total, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return battles[offset:end], total, nil
}

func (m *mockClanWarService) StartBattle(ctx context.Context, battleID uuid.UUID) error {
	battle, ok := m.battles[battleID]
	if !ok {
		return errors.New("battle not found")
	}

	if battle.Status != models.BattleStatusScheduled {
		return errors.New("battle is not in scheduled status")
	}

	battle.Status = models.BattleStatusActive
	battle.StartTime = time.Now()
	battle.UpdatedAt = time.Now()
	return nil
}

func (m *mockClanWarService) UpdateBattleScore(ctx context.Context, req *models.UpdateBattleScoreRequest) error {
	battle, ok := m.battles[req.BattleID]
	if !ok {
		return errors.New("battle not found")
	}

	battle.AttackerScore = req.AttackerScore
	battle.DefenderScore = req.DefenderScore
	battle.UpdatedAt = time.Now()
	return nil
}

func (m *mockClanWarService) CompleteBattle(ctx context.Context, battleID uuid.UUID) error {
	battle, ok := m.battles[battleID]
	if !ok {
		return errors.New("battle not found")
	}

	if battle.Status != models.BattleStatusActive {
		return errors.New("battle is not in active status")
	}

	battle.Status = models.BattleStatusCompleted
	now := time.Now()
	battle.EndTime = &now
	battle.UpdatedAt = now
	return nil
}

func (m *mockClanWarService) GetTerritory(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error) {
	territory, ok := m.territories[territoryID]
	if !ok {
		return nil, errors.New("territory not found")
	}

	return territory, nil
}

func (m *mockClanWarService) ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error) {
	var territories []models.Territory
	for _, territory := range m.territories {
		if ownerGuildID != nil {
			if territory.OwnerGuildID == nil {
				continue
			}
			if *territory.OwnerGuildID != *ownerGuildID {
				continue
			}
		}
		territories = append(territories, *territory)
	}

	total := len(territories)
	if offset >= total {
		return []models.Territory{}, total, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return territories[offset:end], total, nil
}

func TestHTTPServer_DeclareWar(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	server := NewHTTPServer(":8080", mockService, nil, false)

	attackerGuildID := uuid.New()
	defenderGuildID := uuid.New()
	reqBody := models.DeclareWarRequest{
		AttackerGuildID: attackerGuildID,
		DefenderGuildID: defenderGuildID,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/clan-war/wars", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var war models.ClanWar
	if err := json.Unmarshal(w.Body.Bytes(), &war); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if war.AttackerGuildID != attackerGuildID {
		t.Errorf("Expected attacker_guild_id %s, got %s", attackerGuildID, war.AttackerGuildID)
	}

	if war.Status != models.WarStatusDeclared {
		t.Errorf("Expected status %s, got %s", models.WarStatusDeclared, war.Status)
	}
}

func TestHTTPServer_GetWar(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.wars[warID] = war

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/clan-war/wars/"+warID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ClanWar
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != warID {
		t.Errorf("Expected war_id %s, got %s", warID, response.ID)
	}
}

func TestHTTPServer_ListWars(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	guildID := uuid.New()
	war1 := &models.ClanWar{
		ID:              uuid.New(),
		AttackerGuildID: guildID,
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	war2 := &models.ClanWar{
		ID:              uuid.New(),
		AttackerGuildID: uuid.New(),
		DefenderGuildID: guildID,
		Status:          models.WarStatusOngoing,
		Phase:           models.WarPhaseActive,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.wars[war1.ID] = war1
	mockService.wars[war2.ID] = war2

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/clan-war/wars?guild_id="+guildID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.WarListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_StartWar(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusDeclared,
		Phase:           models.WarPhasePreparation,
		StartTime:       time.Now().Add(-1 * time.Hour),
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.wars[warID] = war

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/clan-war/wars/"+warID.String()+"/start", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_CompleteWar(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	warID := uuid.New()
	war := &models.ClanWar{
		ID:              warID,
		AttackerGuildID: uuid.New(),
		DefenderGuildID: uuid.New(),
		Status:          models.WarStatusOngoing,
		Phase:           models.WarPhaseActive,
		AttackerScore:   100,
		DefenderScore:   50,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.wars[warID] = war

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/clan-war/wars/"+warID.String()+"/complete", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_CreateBattle(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	warID := uuid.New()
	reqBody := models.CreateBattleRequest{
		WarID:     warID,
		Type:      models.BattleTypeTerritory,
		StartTime: time.Now().Add(1 * time.Hour),
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/clan-war/battles", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server := NewHTTPServer(":8080", mockService, nil, false)
	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var battle models.WarBattle
	if err := json.Unmarshal(w.Body.Bytes(), &battle); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if battle.WarID != warID {
		t.Errorf("Expected war_id %s, got %s", warID, battle.WarID)
	}

	if battle.Status != models.BattleStatusScheduled {
		t.Errorf("Expected status %s, got %s", models.BattleStatusScheduled, battle.Status)
	}
}

func TestHTTPServer_GetBattle(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		AttackerScore: 0,
		DefenderScore: 0,
		StartTime:     time.Now(),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.battles[battleID] = battle

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/clan-war/battles/"+battleID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.WarBattle
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != battleID {
		t.Errorf("Expected battle_id %s, got %s", battleID, response.ID)
	}
}

func TestHTTPServer_ListBattles(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	warID := uuid.New()
	battle1 := &models.WarBattle{
		ID:            uuid.New(),
		WarID:         warID,
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	battle2 := &models.WarBattle{
		ID:            uuid.New(),
		WarID:         warID,
		Type:          models.BattleTypeSiege,
		Status:        models.BattleStatusActive,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.battles[battle1.ID] = battle1
	mockService.battles[battle2.ID] = battle2

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/clan-war/battles?war_id="+warID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.BattleListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_StartBattle(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusScheduled,
		StartTime:     time.Now(),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.battles[battleID] = battle

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/clan-war/battles/"+battleID.String()+"/start", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_UpdateBattleScore(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusActive,
		AttackerScore: 0,
		DefenderScore: 0,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.battles[battleID] = battle

	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.UpdateBattleScoreRequest{
		BattleID:      battleID,
		AttackerScore: 50,
		DefenderScore: 30,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/clan-war/battles/"+battleID.String()+"/score", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_CompleteBattle(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	battleID := uuid.New()
	battle := &models.WarBattle{
		ID:            battleID,
		WarID:         uuid.New(),
		Type:          models.BattleTypeTerritory,
		Status:        models.BattleStatusActive,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.battles[battleID] = battle

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("POST", "/api/v1/clan-war/battles/"+battleID.String()+"/complete", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetTerritory(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	territoryID := uuid.New()
	territory := &models.Territory{
		ID:            territoryID,
		Name:          "Test Territory",
		Region:        "Test Region",
		DefenseLevel:  5,
		SiegeDifficulty: 3,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.territories[territoryID] = territory

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/clan-war/territories/"+territoryID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Territory
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != territoryID {
		t.Errorf("Expected territory_id %s, got %s", territoryID, response.ID)
	}
}

func TestHTTPServer_ListTerritories(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	guildID := uuid.New()
	territory1 := &models.Territory{
		ID:            uuid.New(),
		Name:          "Territory 1",
		Region:        "Region 1",
		OwnerGuildID:  &guildID,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	territory2 := &models.Territory{
		ID:            uuid.New(),
		Name:          "Territory 2",
		Region:        "Region 2",
		OwnerGuildID:  nil,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockService.territories[territory1.ID] = territory1
	mockService.territories[territory2.ID] = territory2

	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/clan-war/territories?owner_guild_id="+guildID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.TerritoryListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockClanWarService{
		wars:        make(map[uuid.UUID]*models.ClanWar),
		battles:     make(map[uuid.UUID]*models.WarBattle),
		territories: make(map[uuid.UUID]*models.Territory),
	}

	server := NewHTTPServer(":8080", mockService, nil, false)

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

