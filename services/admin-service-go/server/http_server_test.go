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
	"github.com/necpgame/admin-service-go/models"
)

func createRequestWithAdminID(method, url string, body []byte, adminID uuid.UUID) *http.Request {
	req := httptest.NewRequest(method, url, bytes.NewBuffer(body))
	req.Header.Set("Content-Type", "application/json")
	claims := &Claims{
		Subject: adminID.String(),
	}
	ctx := context.WithValue(req.Context(), "claims", claims)
	return req.WithContext(ctx)
}

type mockAdminService struct {
	banResponse      *models.AdminActionResponse
	kickResponse     *models.AdminActionResponse
	muteResponse     *models.AdminActionResponse
	searchResponse   *models.PlayerSearchResponse
	analyticsResponse *models.AnalyticsResponse
	auditLogsResponse *models.AuditLogListResponse
	auditLog         *models.AdminAuditLog
	banErr           error
	logActionErr     error
}

func (m *mockAdminService) LogAction(ctx context.Context, adminID uuid.UUID, actionType models.AdminActionType, targetID *uuid.UUID, targetType string, details map[string]interface{}, ipAddress, userAgent string) error {
	return m.logActionErr
}

func (m *mockAdminService) BanPlayer(ctx context.Context, adminID uuid.UUID, req *models.BanPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	if m.banErr != nil {
		return nil, m.banErr
	}
	return m.banResponse, nil
}

func (m *mockAdminService) KickPlayer(ctx context.Context, adminID uuid.UUID, req *models.KickPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return m.kickResponse, nil
}

func (m *mockAdminService) MutePlayer(ctx context.Context, adminID uuid.UUID, req *models.MutePlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return m.muteResponse, nil
}

func (m *mockAdminService) GiveItem(ctx context.Context, adminID uuid.UUID, req *models.GiveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Item given",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (m *mockAdminService) RemoveItem(ctx context.Context, adminID uuid.UUID, req *models.RemoveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Item removed",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (m *mockAdminService) SetCurrency(ctx context.Context, adminID uuid.UUID, req *models.SetCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Currency set",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (m *mockAdminService) AddCurrency(ctx context.Context, adminID uuid.UUID, req *models.AddCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Currency added",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (m *mockAdminService) SetWorldFlag(ctx context.Context, adminID uuid.UUID, req *models.SetWorldFlagRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return &models.AdminActionResponse{
		Success:   true,
		Message:   "World flag set",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (m *mockAdminService) CreateEvent(ctx context.Context, adminID uuid.UUID, req *models.CreateEventRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return &models.AdminActionResponse{
		Success:   true,
		Message:   "Event created",
		ActionID:  uuid.New(),
		Timestamp: time.Now(),
	}, nil
}

func (m *mockAdminService) SearchPlayers(ctx context.Context, req *models.SearchPlayersRequest) (*models.PlayerSearchResponse, error) {
	return m.searchResponse, nil
}

func (m *mockAdminService) GetAnalytics(ctx context.Context) (*models.AnalyticsResponse, error) {
	return m.analyticsResponse, nil
}

func (m *mockAdminService) GetAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) (*models.AuditLogListResponse, error) {
	return m.auditLogsResponse, nil
}

func (m *mockAdminService) GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error) {
	if m.auditLog == nil {
		return nil, errors.New("audit log not found")
	}
	return m.auditLog, nil
}

func TestHTTPServer_BanPlayer(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_KickPlayer(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_MutePlayer(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_UnbanPlayer(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_UnmutePlayer(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_SearchPlayers(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_GiveItem(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_GetAnalytics(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_GetAuditLogs(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_GetAuditLog(t *testing.T) {
	t.Skip("Legacy endpoint removed - functionality available through ogen API at /api/v1/admin")
}

func TestHTTPServer_HealthCheck(t *testing.T) {
	mockService := &mockAdminService{}

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

func int64Ptr(i int64) *int64 {
	return &i
}

