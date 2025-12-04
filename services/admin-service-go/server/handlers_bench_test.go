// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
	"github.com/sirupsen/logrus"
)

// BenchmarkGetDashboard benchmarks GetDashboard handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetDashboard(b *testing.B) {
	mockService := &benchmarkAdminService{}
	logger := logrus.New()
	handlers := NewAdminHandlers(mockService, logger)

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetDashboard(ctx)
	}
}

// benchmarkAdminService implements AdminServiceInterface for benchmarks
type benchmarkAdminService struct{}

func (m *benchmarkAdminService) GetAnalytics(ctx context.Context) (*models.AnalyticsResponse, error) {
	return &models.AnalyticsResponse{
		OnlinePlayers: 100,
	}, nil
}

func (m *benchmarkAdminService) GetAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) (*models.AuditLogListResponse, error) {
	return &models.AuditLogListResponse{
		Logs: []models.AdminAuditLog{},
	}, nil
}

// Stub methods for AdminServiceInterface
func (m *benchmarkAdminService) LogAction(ctx context.Context, adminID uuid.UUID, actionType models.AdminActionType, targetID *uuid.UUID, targetType string, details map[string]interface{}, ipAddress, userAgent string) error {
	return nil
}

func (m *benchmarkAdminService) BanPlayer(ctx context.Context, adminID uuid.UUID, req *models.BanPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) KickPlayer(ctx context.Context, adminID uuid.UUID, req *models.KickPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) MutePlayer(ctx context.Context, adminID uuid.UUID, req *models.MutePlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) GiveItem(ctx context.Context, adminID uuid.UUID, req *models.GiveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) RemoveItem(ctx context.Context, adminID uuid.UUID, req *models.RemoveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) SetCurrency(ctx context.Context, adminID uuid.UUID, req *models.SetCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) AddCurrency(ctx context.Context, adminID uuid.UUID, req *models.AddCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) SetWorldFlag(ctx context.Context, adminID uuid.UUID, req *models.SetWorldFlagRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) CreateEvent(ctx context.Context, adminID uuid.UUID, req *models.CreateEventRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) SearchPlayers(ctx context.Context, req *models.SearchPlayersRequest) (*models.PlayerSearchResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error) {
	return nil, nil
}
