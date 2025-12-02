package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

func TestHTTPServer_CreateReport(t *testing.T) {
	reporterID := uuid.New()
	reportedID := uuid.New()
	mockService := &mockSocialService{
		reports: make(map[uuid.UUID]models.ChatReport),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateReportRequest{
		ReportedID: reportedID,
		Reason:     "Spam messages",
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/chat/report", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	req = req.WithContext(context.WithValue(req.Context(), "user_id", reporterID.String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.ChatReport
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.ReporterID != reporterID {
		t.Errorf("Expected reporter ID %s, got %s", reporterID, response.ReporterID)
	}
	if response.ReportedID != reportedID {
		t.Errorf("Expected reported ID %s, got %s", reportedID, response.ReportedID)
	}
	if response.Status != "pending" {
		t.Errorf("Expected status 'pending', got %s", response.Status)
	}
}

func TestHTTPServer_CreateReportInvalidRequest(t *testing.T) {
	reporterID := uuid.New()
	mockService := &mockSocialService{}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := models.CreateReportRequest{
		ReportedID: uuid.New(),
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/chat/report", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	req = req.WithContext(context.WithValue(req.Context(), "user_id", reporterID.String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusBadRequest {
		t.Errorf("Expected status %d, got %d", http.StatusBadRequest, w.Code)
	}
}

func TestHTTPServer_CreateBan(t *testing.T) {
	adminID := uuid.New()
	characterID := uuid.New()
	mockService := &mockSocialService{
		bans: make(map[uuid.UUID]models.ChatBan),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	duration := 24
	reqBody := models.CreateBanRequest{
		CharacterID: characterID,
		Reason:      "Spam",
		Duration:    &duration,
	}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/chat/ban", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	req = req.WithContext(context.WithValue(req.Context(), "user_id", adminID.String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusCreated {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusCreated, w.Code, w.Body.String())
	}

	var response models.ChatBan
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.CharacterID != characterID {
		t.Errorf("Expected character ID %s, got %s", characterID, response.CharacterID)
	}
	if response.Reason != "Spam" {
		t.Errorf("Expected reason 'Spam', got %s", response.Reason)
	}
	if !response.IsActive {
		t.Errorf("Expected ban to be active")
	}
}

func TestHTTPServer_GetBans(t *testing.T) {
	characterID := uuid.New()
	mockService := &mockSocialService{
		bans: make(map[uuid.UUID]models.ChatBan),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/bans?character_id="+characterID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response models.BanListResponse
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response.Bans == nil {
		t.Error("Expected bans array, got nil")
	}
}

func TestHTTPServer_RemoveBan(t *testing.T) {
	banID := uuid.New()
	mockService := &mockSocialService{
		bans: make(map[uuid.UUID]models.ChatBan),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("DELETE", "/api/v1/social/chat/bans/"+banID.String(), nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

func TestHTTPServer_GetReports(t *testing.T) {
	mockService := &mockSocialService{
		reports: make(map[uuid.UUID]models.ChatReport),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	req := httptest.NewRequest("GET", "/api/v1/social/chat/reports?status=pending", nil)
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}

	var response map[string]interface{}
	if err := json.Unmarshal(w.Body.Bytes(), &response); err != nil {
		t.Fatalf("Failed to unmarshal response: %v", err)
	}

	if response["reports"] == nil {
		t.Error("Expected reports array, got nil")
	}
}

func TestHTTPServer_ResolveReport(t *testing.T) {
	reportID := uuid.New()
	adminID := uuid.New()
	mockService := &mockSocialService{
		reports: make(map[uuid.UUID]models.ChatReport),
	}
	server := NewHTTPServer(":8080", mockService, nil, false)

	reqBody := map[string]string{"status": "resolved"}
	body, _ := json.Marshal(reqBody)
	req := httptest.NewRequest("POST", "/api/v1/social/chat/reports/"+reportID.String()+"/resolve", bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	req = req.WithContext(context.WithValue(req.Context(), "user_id", adminID.String()))
	w := httptest.NewRecorder()

	server.router.ServeHTTP(w, req)

	if w.Code != http.StatusOK {
		t.Errorf("Expected status %d, got %d. Body: %s", http.StatusOK, w.Code, w.Body.String())
	}
}

