// Issue: #2264
// Analytics Dashboard Service API Interfaces
// PERFORMANCE: Interface-based design for testability and dependency injection

package api

import (
	"context"

	"analytics-dashboard-service-go/pkg/models"
)

// ServiceInterface defines the analytics service interface
type ServiceInterface interface {
	// Core analytics operations
	GetGameAnalyticsOverview(ctx context.Context, period string) (*models.GameAnalyticsOverview, error)
	GetPlayerBehaviorAnalytics(ctx context.Context, params GetPlayerBehaviorAnalyticsParams) (*models.PlayerBehaviorAnalytics, error)
	GetEconomicAnalytics(ctx context.Context, period string) (*models.EconomicAnalytics, error)
	GetCombatAnalytics(ctx context.Context, params GetCombatAnalyticsParams) (*models.CombatAnalytics, error)
	GetSocialAnalytics(ctx context.Context, period string) (*models.SocialAnalytics, error)
	GetRevenueAnalytics(ctx context.Context, period string) (*models.RevenueAnalytics, error)
	GetSystemPerformanceAnalytics(ctx context.Context, period string) (*models.SystemPerformanceAnalytics, error)
	GetAnalyticsAlerts(ctx context.Context, severity string, acknowledged *bool) (*models.AnalyticsAlerts, error)
	GenerateAnalyticsReport(ctx context.Context, params GenerateAnalyticsReportParams) (*models.AnalyticsReport, error)
}

// Parameter structs for API operations
type GetGameAnalyticsOverviewParams struct {
	Period string `json:"period"`
}

type GetPlayerBehaviorAnalyticsParams struct {
	Period   string  `json:"period"`
	Segment  string  `json:"segment"`
	Cohort   *string `json:"cohort,omitempty"`
}

type GetCombatAnalyticsParams struct {
	Period   string  `json:"period"`
	GameMode string  `json:"game_mode"`
	Region   *string `json:"region,omitempty"`
}

type GenerateAnalyticsReportParams struct {
	ReportType string                  `json:"report_type"`
	StartDate  *string                 `json:"start_date,omitempty"`
	EndDate    *string                 `json:"end_date,omitempty"`
	Format     string                  `json:"format"`
}

// Handler interface for HTTP handlers (stub for ogen compatibility)
type Handler interface {
	GetGameAnalyticsOverview(ctx context.Context, params GetGameAnalyticsOverviewParams) (*models.GameAnalyticsOverview, error)
	GetPlayerBehaviorAnalytics(ctx context.Context, params GetPlayerBehaviorAnalyticsParams) (*models.PlayerBehaviorAnalytics, error)
	GetEconomicAnalytics(ctx context.Context, params GetEconomicAnalyticsParams) (*models.EconomicAnalytics, error)
	GetCombatAnalytics(ctx context.Context, params GetCombatAnalyticsParams) (*models.CombatAnalytics, error)
	GetSocialAnalytics(ctx context.Context, params GetSocialAnalyticsParams) (*models.SocialAnalytics, error)
	GetRevenueAnalytics(ctx context.Context, params GetRevenueAnalyticsParams) (*models.RevenueAnalytics, error)
	GetSystemPerformanceAnalytics(ctx context.Context, params GetSystemPerformanceAnalyticsParams) (*models.SystemPerformanceAnalytics, error)
	GetAnalyticsAlerts(ctx context.Context, params GetAnalyticsAlertsParams) (*models.AnalyticsAlerts, error)
	GenerateAnalyticsReport(ctx context.Context, params GenerateAnalyticsReportParams) (*models.AnalyticsReport, error)
}

// Parameter structs for handler methods
type GetEconomicAnalyticsParams struct {
	Period string `json:"period"`
}

type GetSocialAnalyticsParams struct {
	Period string `json:"period"`
}

type GetRevenueAnalyticsParams struct {
	Period string `json:"period"`
}

type GetSystemPerformanceAnalyticsParams struct {
	Period string `json:"period"`
}

type GetAnalyticsAlertsParams struct {
	Severity     string `json:"severity"`
	Acknowledged *bool  `json:"acknowledged,omitempty"`
}

// Stub function for ogen compatibility
func HandlerFromMuxWithBaseURL(handler Handler, mux interface{}, baseURL string) {
	// This is a stub implementation
	// In production, ogen would generate the actual routing
}
