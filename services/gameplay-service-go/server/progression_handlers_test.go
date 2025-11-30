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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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

	mockAffixService := &mockAffixService{}
	mockTimeTrialService := &mockTimeTrialService{}
	mockComboService := &mockComboService{}
	mockWeaponMechanicsService := &mockWeaponMechanicsService{}
	server := NewHTTPServer(":8080", mockProgService, mockQuestService, mockAffixService, mockTimeTrialService, mockComboService, mockWeaponMechanicsService)

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
