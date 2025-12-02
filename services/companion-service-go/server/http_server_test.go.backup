package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/companion-service-go/models"
)

type mockCompanionService struct {
	types      map[string]*models.CompanionType
	companions map[uuid.UUID]*models.PlayerCompanion
	abilities  map[uuid.UUID][]models.CompanionAbility
	createErr  error
	getErr     error
}

func (m *mockCompanionService) GetCompanionType(ctx context.Context, companionTypeID string) (*models.CompanionType, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.types[companionTypeID], nil
}

func (m *mockCompanionService) ListCompanionTypes(ctx context.Context, category *models.CompanionCategory, limit, offset int) (*models.CompanionTypeListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	types := []models.CompanionType{}
	for _, t := range m.types {
		if category == nil || t.Category == *category {
			types = append(types, *t)
		}
	}

	total := len(types)
	if offset >= total {
		return &models.CompanionTypeListResponse{Types: []models.CompanionType{}, Total: total}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.CompanionTypeListResponse{
		Types: types[offset:end],
		Total: total,
	}, nil
}

func (m *mockCompanionService) PurchaseCompanion(ctx context.Context, characterID uuid.UUID, companionTypeID string) (*models.PlayerCompanion, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}

	companionType := m.types[companionTypeID]
	if companionType == nil {
		return nil, &companionTypeNotFoundError{}
	}

	for _, c := range m.companions {
		if c.CharacterID == characterID && c.CompanionTypeID == companionTypeID {
			return nil, &companionAlreadyOwnedError{}
		}
	}

	companion := &models.PlayerCompanion{
		ID:              uuid.New(),
		CharacterID:     characterID,
		CompanionTypeID: companionTypeID,
		Level:           1,
		Experience:      0,
		Status:          models.CompanionStatusOwned,
		Equipment:       make(map[string]interface{}),
		Stats:           make(map[string]interface{}),
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	m.companions[companion.ID] = companion
	return companion, nil
}

func (m *mockCompanionService) ListPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus, limit, offset int) (*models.PlayerCompanionListResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	companions := []models.PlayerCompanion{}
	for _, c := range m.companions {
		if c.CharacterID == characterID {
			if status == nil || c.Status == *status {
				companions = append(companions, *c)
			}
		}
	}

	total := len(companions)
	if offset >= total {
		return &models.PlayerCompanionListResponse{Companions: []models.PlayerCompanion{}, Total: total}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.PlayerCompanionListResponse{
		Companions: companions[offset:end],
		Total:      total,
	}, nil
}

func (m *mockCompanionService) GetCompanionDetail(ctx context.Context, companionID uuid.UUID) (*models.CompanionDetailResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}

	companion := m.companions[companionID]
	if companion == nil {
		return nil, &companionNotFoundError{}
	}

	companionType := m.types[companion.CompanionTypeID]
	abilities := m.abilities[companionID]

	return &models.CompanionDetailResponse{
		Companion: companion,
		Type:      companionType,
		Abilities: abilities,
	}, nil
}

func (m *mockCompanionService) SummonCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID) error {
	companion := m.companions[companionID]
	if companion == nil {
		return &companionNotFoundError{}
	}

	if companion.CharacterID != characterID {
		return &companionNotOwnedError{}
	}

	if companion.Status == models.CompanionStatusSummoned {
		return &companionAlreadySummonedError{}
	}

	now := time.Now()
	companion.Status = models.CompanionStatusSummoned
	companion.SummonedAt = &now
	companion.UpdatedAt = now
	return nil
}

func (m *mockCompanionService) DismissCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID) error {
	companion := m.companions[companionID]
	if companion == nil {
		return &companionNotFoundError{}
	}

	if companion.CharacterID != characterID {
		return &companionNotOwnedError{}
	}

	if companion.Status != models.CompanionStatusSummoned {
		return &companionNotSummonedError{}
	}

	companion.Status = models.CompanionStatusDismissed
	companion.SummonedAt = nil
	companion.UpdatedAt = time.Now()
	return nil
}

func (m *mockCompanionService) RenameCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, customName string) error {
	companion := m.companions[companionID]
	if companion == nil {
		return &companionNotFoundError{}
	}

	if companion.CharacterID != characterID {
		return &companionNotOwnedError{}
	}

	companion.CustomName = &customName
	companion.UpdatedAt = time.Now()
	return nil
}

func (m *mockCompanionService) AddExperience(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, amount int64, source string) error {
	companion := m.companions[companionID]
	if companion == nil {
		return &companionNotFoundError{}
	}

	if companion.CharacterID != characterID {
		return &companionNotOwnedError{}
	}

	companion.Experience += amount
	companion.UpdatedAt = time.Now()
	return nil
}

func (m *mockCompanionService) UseAbility(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, abilityID string) error {
	companion := m.companions[companionID]
	if companion == nil {
		return &companionNotFoundError{}
	}

	if companion.CharacterID != characterID {
		return &companionNotOwnedError{}
	}

	if companion.Status != models.CompanionStatusSummoned {
		return &companionNotSummonedError{}
	}

	abilities := m.abilities[companionID]
	for i, a := range abilities {
		if a.AbilityID == abilityID {
			if a.CooldownUntil != nil && time.Now().Before(*a.CooldownUntil) {
				return &abilityOnCooldownError{}
			}
			now := time.Now()
			abilities[i].LastUsedAt = &now
			abilities[i].UpdatedAt = now
			return nil
		}
	}

	return nil
}

type companionTypeNotFoundError struct{}

func (e *companionTypeNotFoundError) Error() string {
	return "companion type not found"
}

type companionAlreadyOwnedError struct{}

func (e *companionAlreadyOwnedError) Error() string {
	return "companion already owned"
}

type companionNotFoundError struct{}

func (e *companionNotFoundError) Error() string {
	return "companion not found"
}

type companionNotOwnedError struct{}

func (e *companionNotOwnedError) Error() string {
	return "companion does not belong to character"
}

type companionAlreadySummonedError struct{}

func (e *companionAlreadySummonedError) Error() string {
	return "companion already summoned"
}

type companionNotSummonedError struct{}

func (e *companionNotSummonedError) Error() string {
	return "companion is not summoned"
}

type abilityOnCooldownError struct{}

func (e *abilityOnCooldownError) Error() string {
	return "ability is on cooldown"
}

func TestHTTPServer_ListCompanionTypes(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	type1 := &models.CompanionType{
		ID:          "type1",
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Companion",
		Description: "A combat companion",
		Cost:        1000,
		CreatedAt:   time.Now(),
	}
	type2 := &models.CompanionType{
		ID:          "type2",
		Category:    models.CompanionCategoryUtility,
		Name:        "Utility Companion",
		Description: "A utility companion",
		Cost:        500,
		CreatedAt:   time.Now(),
	}

	mockService.types["type1"] = type1
	mockService.types["type2"] = type2

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/companions/types", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.CompanionTypeListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetCompanionType(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionType := &models.CompanionType{
		ID:          "type1",
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Companion",
		Description: "A combat companion",
		Cost:        1000,
		CreatedAt:   time.Now(),
	}

	mockService.types["type1"] = companionType
	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/companions/types/type1", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.CompanionType
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != "type1" {
		t.Errorf("Expected ID 'type1', got %s", response.ID)
	}
}

func TestHTTPServer_PurchaseCompanion(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionType := &models.CompanionType{
		ID:          "type1",
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Companion",
		Cost:        1000,
		CreatedAt:   time.Now(),
	}

	mockService.types["type1"] = companionType
	server := NewHTTPServer(":8080", mockService)

	characterID := uuid.New()
	reqBody := models.PurchaseCompanionRequest{
		CharacterID:    characterID,
		CompanionTypeID: "type1",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/companions/purchase", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response models.PlayerCompanion
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.CompanionTypeID != "type1" {
		t.Errorf("Expected companion_type_id 'type1', got %s", response.CompanionTypeID)
	}
}

func TestHTTPServer_ListPlayerCompanions(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	characterID := uuid.New()
	companion1 := &models.PlayerCompanion{
		ID:              uuid.New(),
		CharacterID:     characterID,
		CompanionTypeID: "type1",
		Status:          models.CompanionStatusOwned,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}
	companion2 := &models.PlayerCompanion{
		ID:              uuid.New(),
		CharacterID:     characterID,
		CompanionTypeID: "type2",
		Status:          models.CompanionStatusSummoned,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.companions[companion1.ID] = companion1
	mockService.companions[companion2.ID] = companion2
	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/companions/characters/"+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.PlayerCompanionListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetCompanionDetail(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:              companionID,
		CharacterID:     uuid.New(),
		CompanionTypeID: "type1",
		Status:          models.CompanionStatusOwned,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	companionType := &models.CompanionType{
		ID:          "type1",
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Companion",
		CreatedAt:   time.Now(),
	}

	mockService.companions[companionID] = companion
	mockService.types["type1"] = companionType
	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/companions/"+companionID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.CompanionDetailResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Companion.ID != companionID {
		t.Errorf("Expected ID %s, got %s", companionID, response.Companion.ID)
	}
}

func TestHTTPServer_SummonCompanion(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionID := uuid.New()
	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:              companionID,
		CharacterID:     characterID,
		CompanionTypeID: "type1",
		Status:          models.CompanionStatusOwned,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.companions[companionID] = companion
	server := NewHTTPServer(":8080", mockService)

	reqBody := models.SummonCompanionRequest{
		CharacterID: characterID,
		CompanionID: companionID,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/companions/"+companionID.String()+"/summon", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_DismissCompanion(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionID := uuid.New()
	characterID := uuid.New()
	now := time.Now()
	companion := &models.PlayerCompanion{
		ID:              companionID,
		CharacterID:     characterID,
		CompanionTypeID: "type1",
		Status:          models.CompanionStatusSummoned,
		SummonedAt:      &now,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.companions[companionID] = companion
	server := NewHTTPServer(":8080", mockService)

	reqBody := models.DismissCompanionRequest{
		CharacterID: characterID,
		CompanionID: companionID,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/companions/"+companionID.String()+"/dismiss", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_RenameCompanion(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionID := uuid.New()
	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:              companionID,
		CharacterID:     characterID,
		CompanionTypeID: "type1",
		Status:          models.CompanionStatusOwned,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.companions[companionID] = companion
	server := NewHTTPServer(":8080", mockService)

	reqBody := models.RenameCompanionRequest{
		CharacterID: characterID,
		CompanionID: companionID,
		CustomName:  "My Companion",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/companions/"+companionID.String()+"/rename", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_AddExperience(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionID := uuid.New()
	characterID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:              companionID,
		CharacterID:     characterID,
		CompanionTypeID: "type1",
		Experience:      100,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	mockService.companions[companionID] = companion
	server := NewHTTPServer(":8080", mockService)

	reqBody := models.AddExperienceRequest{
		CharacterID: characterID,
		CompanionID: companionID,
		Amount:      50,
		Source:      "quest",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/companions/"+companionID.String()+"/experience", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_UseAbility(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
	}

	companionID := uuid.New()
	characterID := uuid.New()
	now := time.Now()
	companion := &models.PlayerCompanion{
		ID:              companionID,
		CharacterID:     characterID,
		CompanionTypeID: "type1",
		Status:          models.CompanionStatusSummoned,
		SummonedAt:      &now,
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	ability := models.CompanionAbility{
		ID:                uuid.New(),
		PlayerCompanionID: companionID,
		AbilityID:         "ability1",
		IsActive:          true,
		UpdatedAt:         time.Now(),
	}

	mockService.companions[companionID] = companion
	mockService.abilities[companionID] = []models.CompanionAbility{ability}
	server := NewHTTPServer(":8080", mockService)

	reqBody := models.UseAbilityRequest{
		CharacterID: characterID,
		CompanionID: companionID,
		AbilityID:   "ability1",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/companions/"+companionID.String()+"/abilities/ability1/use", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockCompanionService{
		types:      make(map[string]*models.CompanionType),
		companions: make(map[uuid.UUID]*models.PlayerCompanion),
		abilities:  make(map[uuid.UUID][]models.CompanionAbility),
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

