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
	"github.com/necpgame/gameplay-service-go/models"
)

type mockProgressionService struct {
	progression      map[uuid.UUID]*models.CharacterProgression
	skillProgression map[uuid.UUID][]models.SkillExperience
	addExpErr        error
	addSkillExpErr   error
	allocateAttrErr  error
	allocateSkillErr error
}

func (m *mockProgressionService) GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error) {
	prog, ok := m.progression[characterID]
	if !ok {
		return nil, errors.New("progression not found")
	}
	return prog, nil
}

func (m *mockProgressionService) AddExperience(ctx context.Context, characterID uuid.UUID, amount int64, source string) error {
	return m.addExpErr
}

func (m *mockProgressionService) AddSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string, amount int64) error {
	return m.addSkillExpErr
}

func (m *mockProgressionService) AllocateAttributePoint(ctx context.Context, characterID uuid.UUID, attribute string) error {
	return m.allocateAttrErr
}

func (m *mockProgressionService) AllocateSkillPoint(ctx context.Context, characterID uuid.UUID, skillID string) error {
	return m.allocateSkillErr
}

func (m *mockProgressionService) GetSkillProgression(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.SkillProgressionResponse, error) {
	skills, ok := m.skillProgression[characterID]
	if !ok {
		skills = []models.SkillExperience{}
	}

	total := len(skills)
	if offset >= total {
		return &models.SkillProgressionResponse{
			Skills: []models.SkillExperience{},
			Total:  total,
		}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.SkillProgressionResponse{
		Skills: skills[offset:end],
		Total:  total,
	}, nil
}

type mockQuestService struct {
	questInstances map[uuid.UUID]*models.QuestInstance
	questLists     map[uuid.UUID][]models.QuestInstance
	startQuestErr  error
	updateDialErr  error
	skillCheckPass bool
	skillCheckErr  error
	completeObjErr error
	completeErr    error
	failErr        error
}

func (m *mockQuestService) StartQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error) {
	if m.startQuestErr != nil {
		return nil, m.startQuestErr
	}

	instance := &models.QuestInstance{
		ID:            uuid.New(),
		CharacterID:   characterID,
		QuestID:       questID,
		Status:       models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:    make(map[string]interface{}),
		StartedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	m.questInstances[instance.ID] = instance
	return instance, nil
}

func (m *mockQuestService) GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error) {
	instance, ok := m.questInstances[instanceID]
	if !ok {
		return nil, errors.New("quest instance not found")
	}
	return instance, nil
}

func (m *mockQuestService) UpdateDialogue(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, nodeID string, choiceID *string) error {
	return m.updateDialErr
}

func (m *mockQuestService) PerformSkillCheck(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, skillID string, requiredLevel int) (bool, error) {
	if m.skillCheckErr != nil {
		return false, m.skillCheckErr
	}
	return m.skillCheckPass, nil
}

func (m *mockQuestService) CompleteObjective(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, objectiveID string) error {
	return m.completeObjErr
}

func (m *mockQuestService) CompleteQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error {
	return m.completeErr
}

func (m *mockQuestService) FailQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error {
	return m.failErr
}

func (m *mockQuestService) ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) (*models.QuestListResponse, error) {
	quests, ok := m.questLists[characterID]
	if !ok {
		quests = []models.QuestInstance{}
	}

	var filtered []models.QuestInstance
	for _, q := range quests {
		if status != nil && q.Status != *status {
			continue
		}
		filtered = append(filtered, q)
	}

	total := len(filtered)
	if offset >= total {
		return &models.QuestListResponse{
			Quests: []models.QuestInstance{},
			Total:  total,
		}, nil
	}

	end := offset + limit
	if end > total {
		end = total
	}

	return &models.QuestListResponse{
		Quests: filtered[offset:end],
		Total:  total,
	}, nil
}

func TestHTTPServer_GetProgression(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:           10,
		Experience:      5000,
		ExperienceToNext: 1000,
		AttributePoints: 5,
		SkillPoints:     3,
		Attributes:      map[string]int{"strength": 10, "agility": 8},
		UpdatedAt:       time.Now(),
	}

	mockProgService.progression[characterID] = progression

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	req := httptest.NewRequest("GET", "/api/v1/gameplay/progression/characters/"+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ProgressionResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Progression.CharacterID != characterID {
		t.Errorf("Expected character_id %s, got %s", characterID, response.Progression.CharacterID)
	}
}

func TestHTTPServer_AddExperience(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	characterID := uuid.New()
	reqBody := map[string]interface{}{
		"amount": 100,
		"source": "combat",
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/progression/characters/"+characterID.String()+"/experience", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_AddSkillExperience(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	characterID := uuid.New()
	reqBody := map[string]interface{}{
		"skill_id": "hacking",
		"amount":   50,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/progression/characters/"+characterID.String()+"/skills/experience", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_AllocateAttributePoint(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	characterID := uuid.New()
	reqBody := map[string]interface{}{
		"attribute": "strength",
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/progression/characters/"+characterID.String()+"/attributes/allocate", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_AllocateSkillPoint(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	characterID := uuid.New()
	reqBody := map[string]interface{}{
		"skill_id": "hacking",
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/progression/characters/"+characterID.String()+"/skills/allocate", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_GetSkillProgression(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	characterID := uuid.New()
	skill1 := models.SkillExperience{
		ID:          uuid.New(),
		CharacterID: characterID,
		SkillID:     "hacking",
		Level:       5,
		Experience:  500,
		UpdatedAt:   time.Now(),
	}

	skill2 := models.SkillExperience{
		ID:          uuid.New(),
		CharacterID: characterID,
		SkillID:     "combat",
		Level:       3,
		Experience:  200,
		UpdatedAt:   time.Now(),
	}

	mockProgService.skillProgression[characterID] = []models.SkillExperience{skill1, skill2}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	req := httptest.NewRequest("GET", "/api/v1/gameplay/progression/characters/"+characterID.String()+"/skills", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.SkillProgressionResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_StartQuest(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	characterID := uuid.New()
	reqBody := map[string]interface{}{
		"character_id": characterID.String(),
		"quest_id":     "quest_001",
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/quests/start", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response models.QuestInstanceResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.QuestInstance.CharacterID != characterID {
		t.Errorf("Expected character_id %s, got %s", characterID, response.QuestInstance.CharacterID)
	}
}

func TestHTTPServer_GetQuestInstance(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	instanceID := uuid.New()
	characterID := uuid.New()
	instance := &models.QuestInstance{
		ID:            instanceID,
		CharacterID:   characterID,
		QuestID:       "quest_001",
		Status:        models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:    make(map[string]interface{}),
		StartedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockQuestService.questInstances[instanceID] = instance

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	req := httptest.NewRequest("GET", "/api/v1/gameplay/quests/instances/"+instanceID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.QuestInstanceResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.QuestInstance.ID != instanceID {
		t.Errorf("Expected instance_id %s, got %s", instanceID, response.QuestInstance.ID)
	}
}

func TestHTTPServer_UpdateDialogue(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	instanceID := uuid.New()
	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	choiceID := "choice_1"
	reqBody := map[string]interface{}{
		"character_id": characterID.String(),
		"node_id":       "node_1",
		"choice_id":     choiceID,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/quests/instances/"+instanceID.String()+"/dialogue", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_PerformSkillCheck(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
		skillCheckPass: true,
	}

	instanceID := uuid.New()
	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	reqBody := map[string]interface{}{
		"character_id":   characterID.String(),
		"skill_id":       "hacking",
		"required_level": 5,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/quests/instances/"+instanceID.String()+"/skill-check", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response map[string]interface{}
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if passed, ok := response["passed"].(bool); !ok || !passed {
		t.Errorf("Expected passed=true, got %v", response["passed"])
	}
}

func TestHTTPServer_CompleteObjective(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	instanceID := uuid.New()
	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	reqBody := map[string]interface{}{
		"character_id": characterID.String(),
		"objective_id": "obj_1",
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/quests/instances/"+instanceID.String()+"/objectives/complete", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_CompleteQuest(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	instanceID := uuid.New()
	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	reqBody := map[string]interface{}{
		"character_id": characterID.String(),
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/quests/instances/"+instanceID.String()+"/complete", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_FailQuest(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	instanceID := uuid.New()
	characterID := uuid.New()

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	reqBody := map[string]interface{}{
		"character_id": characterID.String(),
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/gameplay/quests/instances/"+instanceID.String()+"/fail", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_ListQuestInstances(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	characterID := uuid.New()
	instance1 := models.QuestInstance{
		ID:            uuid.New(),
		CharacterID:   characterID,
		QuestID:       "quest_001",
		Status:        models.QuestStatusInProgress,
		StartedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	instance2 := models.QuestInstance{
		ID:            uuid.New(),
		CharacterID:   characterID,
		QuestID:       "quest_002",
		Status:        models.QuestStatusCompleted,
		StartedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	mockQuestService.questLists[characterID] = []models.QuestInstance{instance1, instance2}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

	req := httptest.NewRequest("GET", "/api/v1/gameplay/quests/characters/"+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.QuestListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	server := NewHTTPServer(":8080", mockProgService, mockQuestService)

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

