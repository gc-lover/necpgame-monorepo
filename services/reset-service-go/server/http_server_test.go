package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/reset-service-go/models"
	"github.com/google/uuid"
)

type mockResetService struct {
	stats         *models.ResetStats
	history       *models.ResetListResponse
	triggerErr    error
	getStatsErr   error
	getHistoryErr error
}

func (m *mockResetService) TriggerReset(_ context.Context, _ models.ResetType) error {
	return m.triggerErr
}

func (m *mockResetService) GetResetStats(_ context.Context) (*models.ResetStats, error) {
	if m.getStatsErr != nil {
		return nil, m.getStatsErr
	}
	return m.stats, nil
}

func (m *mockResetService) GetResetHistory(_ context.Context, _ *models.ResetType, _, _ int) (*models.ResetListResponse, error) {
	if m.getHistoryErr != nil {
		return nil, m.getHistoryErr
	}
	return m.history, nil
}

func TestHTTPServer_GetResetStats(t *testing.T) {
	mockService := &mockResetService{
		stats: &models.ResetStats{
			LastDailyReset:  timePtr(time.Now().Add(-24 * time.Hour)),
			LastWeeklyReset: timePtr(time.Now().Add(-7 * 24 * time.Hour)),
			NextDailyReset:  time.Now().Add(1 * time.Hour),
			NextWeeklyReset: time.Now().Add(24 * time.Hour),
		},
	}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/reset/stats", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ResetStats
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.NextDailyReset.IsZero() {
		t.Error("Expected NextDailyReset to be set")
	}
}

func TestHTTPServer_GetResetHistory(t *testing.T) {
	mockService := &mockResetService{
		history: &models.ResetListResponse{
			Resets: []models.ResetRecord{
				{
					ID:          uuid.New(),
					Type:        models.ResetTypeDaily,
					Status:      models.ResetStatusCompleted,
					StartedAt:   time.Now().Add(-24 * time.Hour),
					CompletedAt: timePtr(time.Now().Add(-23 * time.Hour)),
				},
				{
					ID:          uuid.New(),
					Type:        models.ResetTypeWeekly,
					Status:      models.ResetStatusCompleted,
					StartedAt:   time.Now().Add(-7 * 24 * time.Hour),
					CompletedAt: timePtr(time.Now().Add(-6 * 24 * time.Hour)),
				},
			},
			Total: 2,
		},
	}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/reset/history", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ResetListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 2 {
		t.Errorf("Expected total 2, got %d", response.Total)
	}
}

func TestHTTPServer_GetResetHistoryWithFilter(t *testing.T) {
	mockService := &mockResetService{
		history: &models.ResetListResponse{
			Resets: []models.ResetRecord{
				{
					ID:          uuid.New(),
					Type:        models.ResetTypeDaily,
					Status:      models.ResetStatusCompleted,
					StartedAt:   time.Now().Add(-24 * time.Hour),
					CompletedAt: timePtr(time.Now().Add(-23 * time.Hour)),
				},
			},
			Total: 1,
		},
	}

	server := NewHTTPServer(":8080", mockService)

	req := httptest.NewRequest("GET", "/api/v1/reset/history?type=daily", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d", http.StatusOK, w.Code)
	}

	var response models.ResetListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Total != 1 {
		t.Errorf("Expected total 1, got %d", response.Total)
	}
}

func TestHTTPServer_TriggerReset(t *testing.T) {
	mockService := &mockResetService{}

	server := NewHTTPServer(":8080", mockService)

	reqBody := models.TriggerResetRequest{
		Type: models.ResetTypeDaily,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/reset/trigger", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_TriggerResetWeekly(t *testing.T) {
	mockService := &mockResetService{}

	server := NewHTTPServer(":8080", mockService)

	reqBody := models.TriggerResetRequest{
		Type: models.ResetTypeWeekly,
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/reset/trigger", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_TriggerResetInvalidType(t *testing.T) {
	mockService := &mockResetService{}

	server := NewHTTPServer(":8080", mockService)

	reqBody := map[string]string{
		"type": "invalid",
	}

	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/reset/trigger", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockResetService{}

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

func timePtr(t time.Time) *time.Time {
	return &t
}
