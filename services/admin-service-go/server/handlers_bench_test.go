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

func (m *benchmarkAdminService) GetAnalytics(_ context.Context) (*models.AnalyticsResponse, error) {
	return &models.AnalyticsResponse{
		OnlinePlayers: 100,
	}, nil
}

func (m *benchmarkAdminService) GetAuditLogs(_ context.Context, _ *uuid.UUID, _ *models.AdminActionType, _, _ int) (*models.AuditLogListResponse, error) {
	return &models.AuditLogListResponse{
		Logs: []models.AdminAuditLog{},
	}, nil
}

// Stub methods for AdminServiceInterface
func (m *benchmarkAdminService) LogAction(_ context.Context, _ uuid.UUID, _ models.AdminActionType, _ *uuid.UUID, _ string, _ map[string]interface{}, _, _ string) error {
	return nil
}

func (m *benchmarkAdminService) BanPlayer(_ context.Context, _ uuid.UUID, _ *models.BanPlayerRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) KickPlayer(_ context.Context, _ uuid.UUID, _ *models.KickPlayerRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) MutePlayer(_ context.Context, _ uuid.UUID, _ *models.MutePlayerRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) GiveItem(_ context.Context, _ uuid.UUID, _ *models.GiveItemRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) RemoveItem(_ context.Context, _ uuid.UUID, _ *models.RemoveItemRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) SetCurrency(_ context.Context, _ uuid.UUID, _ *models.SetCurrencyRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) AddCurrency(_ context.Context, _ uuid.UUID, _ *models.AddCurrencyRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) SetWorldFlag(_ context.Context, _ uuid.UUID, _ *models.SetWorldFlagRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) CreateEvent(_ context.Context, _ uuid.UUID, _ *models.CreateEventRequest, _, _ string) (*models.AdminActionResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) SearchPlayers(_ context.Context, _ *models.SearchPlayersRequest) (*models.PlayerSearchResponse, error) {
	return nil, nil
}

func (m *benchmarkAdminService) GetAuditLog(_ context.Context, _ uuid.UUID) (*models.AdminAuditLog, error) {
	return nil, nil
}
