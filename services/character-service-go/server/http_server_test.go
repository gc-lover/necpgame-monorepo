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
	"github.com/necpgame/character-service-go/models"
)

type mockCharacterService struct {
	accounts     map[uuid.UUID]*models.PlayerAccount
	characters   map[uuid.UUID]*models.Character
	accountChars map[uuid.UUID][]models.Character
	createErr    error
	getErr       error
	updateErr    error
	deleteErr    error
	switchErr    error
}

func (m *mockCharacterService) GetAccount(_ context.Context, accountID uuid.UUID) (*models.PlayerAccount, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.accounts[accountID], nil
}

func (m *mockCharacterService) CreateAccount(_ context.Context, req *models.CreateAccountRequest) (*models.PlayerAccount, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	account := &models.PlayerAccount{
		ID:         uuid.New(),
		Nickname:   req.Nickname,
		OriginCode: req.OriginCode,
		CreatedAt:  time.Now(),
		UpdatedAt:  time.Now(),
	}
	m.accounts[account.ID] = account
	return account, nil
}

func (m *mockCharacterService) GetCharactersByAccountID(_ context.Context, accountID uuid.UUID) ([]models.Character, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.accountChars[accountID], nil
}

func (m *mockCharacterService) GetCharacter(_ context.Context, characterID uuid.UUID) (*models.Character, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.characters[characterID], nil
}

func (m *mockCharacterService) CreateCharacter(_ context.Context, req *models.CreateCharacterRequest) (*models.Character, error) {
	if m.createErr != nil {
		return nil, m.createErr
	}
	char := &models.Character{
		ID:          uuid.New(),
		AccountID:   req.AccountID,
		Name:        req.Name,
		ClassCode:   req.ClassCode,
		FactionCode: req.FactionCode,
		Level:       1,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	if req.Level != nil {
		char.Level = *req.Level
	}
	m.characters[char.ID] = char
	if m.accountChars[req.AccountID] == nil {
		m.accountChars[req.AccountID] = []models.Character{}
	}
	m.accountChars[req.AccountID] = append(m.accountChars[req.AccountID], *char)
	return char, nil
}

func (m *mockCharacterService) UpdateCharacter(_ context.Context, characterID uuid.UUID, req *models.UpdateCharacterRequest) (*models.Character, error) {
	if m.updateErr != nil {
		return nil, m.updateErr
	}
	char := m.characters[characterID]
	if char == nil {
		return nil, nil
	}
	if req.Name != nil {
		char.Name = *req.Name
	}
	if req.ClassCode != nil {
		char.ClassCode = req.ClassCode
	}
	if req.FactionCode != nil {
		char.FactionCode = req.FactionCode
	}
	if req.Level != nil {
		char.Level = *req.Level
	}
	char.UpdatedAt = time.Now()
	return char, nil
}

func (m *mockCharacterService) DeleteCharacter(_ context.Context, _ uuid.UUID) error {
	return m.deleteErr
}

func (m *mockCharacterService) ValidateCharacter(_ context.Context, characterID uuid.UUID) (bool, error) {
	if m.getErr != nil {
		return false, m.getErr
	}
	return m.characters[characterID] != nil, nil
}

func (m *mockCharacterService) SwitchCharacter(_ context.Context, accountID, characterID uuid.UUID) (*models.SwitchCharacterResponse, error) {
	if m.switchErr != nil {
		return nil, m.switchErr
	}
	char := m.characters[characterID]
	if char == nil || char.AccountID != accountID {
		return &models.SwitchCharacterResponse{
			Success: false,
		}, nil
	}
	return &models.SwitchCharacterResponse{
		CurrentCharacter: char,
		Success:          true,
	}, nil
}

func TestHTTPServer_CreateAccount(t *testing.T) {
	mockService := &mockCharacterService{
		accounts: make(map[uuid.UUID]*models.PlayerAccount),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateAccountRequest{
		Nickname:   "testuser",
		OriginCode: stringPtr("steam"),
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/accounts", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d", http.StatusCreated, w.Code)
	}

	var response models.PlayerAccount
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Nickname != "testuser" {
		t.Errorf("Expected nickname 'testuser', got %s", response.Nickname)
	}
}

func TestHTTPServer_GetAccount(t *testing.T) {
	accountID := uuid.New()
	account := &models.PlayerAccount{
		ID:        accountID,
		Nickname:  "testuser",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockCharacterService{
		accounts: map[uuid.UUID]*models.PlayerAccount{
			accountID: account,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/accounts/"+accountID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.PlayerAccount
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != accountID {
		t.Errorf("Expected ID %s, got %s", accountID, response.ID)
	}
}

func TestHTTPServer_CreateCharacter(t *testing.T) {
	accountID := uuid.New()
	mockService := &mockCharacterService{
		accounts:     make(map[uuid.UUID]*models.PlayerAccount),
		characters:   make(map[uuid.UUID]*models.Character),
		accountChars: make(map[uuid.UUID][]models.Character),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateCharacterRequest{
		AccountID:   accountID,
		Name:        "TestChar",
		ClassCode:   stringPtr("netrunner"),
		FactionCode: stringPtr("corpo"),
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/characters", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d", http.StatusCreated, w.Code)
	}

	var response models.Character
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Name != "TestChar" {
		t.Errorf("Expected name 'TestChar', got %s", response.Name)
	}
}

func TestHTTPServer_GetCharacter(t *testing.T) {
	characterID := uuid.New()
	character := &models.Character{
		ID:        characterID,
		AccountID: uuid.New(),
		Name:      "TestChar",
		Level:     10,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockCharacterService{
		characters: map[uuid.UUID]*models.Character{
			characterID: character,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/characters/"+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Character
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != characterID {
		t.Errorf("Expected ID %s, got %s", characterID, response.ID)
	}
}

func TestHTTPServer_GetCharacters(t *testing.T) {
	accountID := uuid.New()
	characters := []models.Character{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Char1",
			Level:     5,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Char2",
			Level:     10,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockService := &mockCharacterService{
		accountChars: map[uuid.UUID][]models.Character{
			accountID: characters,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/characters?account_id="+accountID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.CharacterListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_UpdateCharacter(t *testing.T) {
	characterID := uuid.New()
	character := &models.Character{
		ID:        characterID,
		AccountID: uuid.New(),
		Name:      "OldName",
		Level:     5,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockCharacterService{
		characters: map[uuid.UUID]*models.Character{
			characterID: character,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	newName := "NewName"
	reqBody := models.UpdateCharacterRequest{
		Name:  &newName,
		Level: intPtr(10),
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("PUT", "/api/v1/characters/"+characterID.String(), bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Character
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Name != "NewName" {
		t.Errorf("Expected name 'NewName', got %s", response.Name)
	}
}

func TestHTTPServer_DeleteCharacter(t *testing.T) {
	characterID := uuid.New()
	mockService := &mockCharacterService{
		characters: make(map[uuid.UUID]*models.Character),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("DELETE", "/api/v1/characters/"+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_SwitchCharacter(t *testing.T) {
	accountID := uuid.New()
	characterID := uuid.New()
	character := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      "TestChar",
		Level:     10,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockCharacterService{
		characters: map[uuid.UUID]*models.Character{
			characterID: character,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.SwitchCharacterRequest{
		AccountID:   accountID,
		CharacterID: characterID,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/characters/switch", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.SwitchCharacterResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if !response.Success {
		t.Errorf("Expected success true, got false")
	}
}

func TestHTTPServer_ValidateCharacter(t *testing.T) {
	characterID := uuid.New()
	character := &models.Character{
		ID:        characterID,
		AccountID: uuid.New(),
		Name:      "TestChar",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockService := &mockCharacterService{
		characters: map[uuid.UUID]*models.Character{
			characterID: character,
		},
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/characters/"+characterID.String()+"/validate", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response map[string]bool
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if !response["valid"] {
		t.Errorf("Expected valid true, got false")
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockCharacterService{
		accounts: make(map[uuid.UUID]*models.PlayerAccount),
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

func stringPtr(s string) *string {
	return &s
}

func intPtr(i int) *int {
	return &i
}
