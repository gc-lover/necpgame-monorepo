package server

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
)

func TestHTTPServer_HealthCheck(t *testing.T) {
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
