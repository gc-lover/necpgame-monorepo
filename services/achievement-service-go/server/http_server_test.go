package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/achievement-service-go/models"
)

type mockAchievementService struct {
	achievements      map[uuid.UUID]*models.Achievement
	playerAchievements map[uuid.UUID][]models.PlayerAchievement
	leaderboard       []models.LeaderboardEntry
	stats             *models.AchievementStatsResponse
	createErr         error
	getErr            error
	listErr           error
	trackErr          error
	unlockErr         error
}

func (m *mockAchievementService) CreateAchievement(ctx context.Context, achievement *models.Achievement) error {
	if m.createErr != nil {
		return m.createErr
	}
	m.achievements[achievement.ID] = achievement
	return nil
}

func (m *mockAchievementService) GetAchievement(ctx context.Context, id uuid.UUID) (*models.Achievement, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.achievements[id], nil
}

func (m *mockAchievementService) GetAchievementByCode(ctx context.Context, code string) (*models.Achievement, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	for _, a := range m.achievements {
		if a.Code == code {
			return a, nil
		}
	}
	return nil, nil
}

func (m *mockAchievementService) ListAchievements(ctx context.Context, category *models.AchievementCategory, limit, offset int) (*models.AchievementListResponse, error) {
	if m.listErr != nil {
		return nil, m.listErr
	}
	achievements := make([]models.Achievement, 0)
	for _, a := range m.achievements {
		if category == nil || *category == a.Category {
			achievements = append(achievements, *a)
		}
	}
	return &models.AchievementListResponse{
		Achievements: achievements,
		Total:        len(achievements),
		Categories:   make(map[string]int),
	}, nil
}

func (m *mockAchievementService) TrackProgress(ctx context.Context, playerID, achievementID uuid.UUID, progress int, progressData map[string]interface{}) error {
	return m.trackErr
}

func (m *mockAchievementService) UnlockAchievement(ctx context.Context, playerID, achievementID uuid.UUID) error {
	return m.unlockErr
}

func (m *mockAchievementService) GetPlayerAchievements(ctx context.Context, playerID uuid.UUID, category *models.AchievementCategory, limit, offset int) (*models.PlayerAchievementResponse, error) {
	if m.listErr != nil {
		return nil, m.listErr
	}
	return &models.PlayerAchievementResponse{
		Achievements:   m.playerAchievements[playerID],
		Total:          len(m.playerAchievements[playerID]),
		Unlocked:       0,
		NearCompletion: []models.PlayerAchievement{},
		RecentUnlocks:  []models.PlayerAchievement{},
	}, nil
}

func (m *mockAchievementService) GetLeaderboard(ctx context.Context, period string, limit int) (*models.LeaderboardResponse, error) {
	if m.listErr != nil {
		return nil, m.listErr
	}
	return &models.LeaderboardResponse{
		Entries: m.leaderboard,
		Total:   len(m.leaderboard),
		Period:  period,
	}, nil
}

func (m *mockAchievementService) GetAchievementStats(ctx context.Context, achievementID uuid.UUID) (*models.AchievementStatsResponse, error) {
	if m.getErr != nil {
		return nil, m.getErr
	}
	return m.stats, nil
}

func TestHTTPServer_CreateAchievement(t *testing.T) {
	mockService := &mockAchievementService{
		achievements: make(map[uuid.UUID]*models.Achievement),
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

	achievement := models.Achievement{
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test Description",
		Points:      10,
		Conditions:  map[string]interface{}{"target": 1},
		Rewards:     map[string]interface{}{"currency": 100},
	}

	body, _ := json.Marshal(achievement)
	req := httptest.NewRequest("POST", "/api/v1/achievements", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d", http.StatusCreated, w.Code)
	}

	var response models.Achievement
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Code != achievement.Code {
		t.Errorf("Expected code %s, got %s", achievement.Code, response.Code)
	}
}

func TestHTTPServer_GetAchievement(t *testing.T) {
	achievementID := uuid.New()
	achievement := &models.Achievement{
		ID:          achievementID,
		Code:        "test_achievement",
		Type:        models.AchievementTypeOneTime,
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test Achievement",
		Description: "Test Description",
		Points:      10,
	}

	mockService := &mockAchievementService{
		achievements: map[uuid.UUID]*models.Achievement{
			achievementID: achievement,
		},
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/achievements/"+achievementID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.Achievement
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ID != achievementID {
		t.Errorf("Expected ID %s, got %s", achievementID, response.ID)
	}
}

func TestHTTPServer_GetAchievementNotFound(t *testing.T) {
	mockService := &mockAchievementService{
		achievements: make(map[uuid.UUID]*models.Achievement),
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/achievements/"+uuid.New().String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusNotFound {
		t.Errorf("Expected status %d, got %d", http.StatusNotFound, w.Code)
	}
}

func TestHTTPServer_ListAchievements(t *testing.T) {
	achievement1 := &models.Achievement{
		ID:          uuid.New(),
		Code:        "test_1",
		Category:    models.CategoryCombat,
		Rarity:      models.RarityCommon,
		Title:       "Test 1",
		Points:      10,
	}
	achievement2 := &models.Achievement{
		ID:          uuid.New(),
		Code:        "test_2",
		Category:    models.CategoryQuest,
		Rarity:      models.RarityRare,
		Title:       "Test 2",
		Points:      50,
	}

	mockService := &mockAchievementService{
		achievements: map[uuid.UUID]*models.Achievement{
			achievement1.ID: achievement1,
			achievement2.ID: achievement2,
		},
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/achievements", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.AchievementListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_TrackProgress(t *testing.T) {
	playerID := uuid.New()
	achievementID := uuid.New()

	mockService := &mockAchievementService{
		achievements: make(map[uuid.UUID]*models.Achievement),
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

	reqBody := map[string]interface{}{
		"achievement_id": achievementID.String(),
		"progress":       5,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/achievements/players/"+playerID.String()+"/progress", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}
}

func TestHTTPServer_GetLeaderboard(t *testing.T) {
	playerID := uuid.New()
	mockService := &mockAchievementService{
		leaderboard: []models.LeaderboardEntry{
			{
				Rank:     1,
				PlayerID: playerID,
				Points:   100,
				Unlocked: 5,
			},
		},
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/achievements/leaderboard?period=all&limit=10", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response models.LeaderboardResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockAchievementService{
		achievements: make(map[uuid.UUID]*models.Achievement),
	}
	server := NewHTTPServer(":8085", mockService, nil, false)

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

