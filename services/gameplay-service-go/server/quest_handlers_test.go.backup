package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
)

func TestHTTPServer_StartQuest(t *testing.T) {
	mockProgService := &mockProgressionService{
		progression:      make(map[uuid.UUID]*models.CharacterProgression),
		skillProgression: make(map[uuid.UUID][]models.SkillExperience),
	}

	mockQuestService := &mockQuestService{
		questInstances: make(map[uuid.UUID]*models.QuestInstance),
		questLists:     make(map[uuid.UUID][]models.QuestInstance),
	}

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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
