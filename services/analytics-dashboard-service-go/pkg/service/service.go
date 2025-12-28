// Issue: #2264
// Analytics Service Layer
// PERFORMANCE: Worker pools, batch operations, memory pooling

package service

import (
	"context"
	"sync"
	"time"

	"go.uber.org/zap"

	"analytics-dashboard-service-go/pkg/api"
	"analytics-dashboard-service-go/pkg/models"
	"analytics-dashboard-service-go/pkg/repository"
)

// Service contains business logic for analytics operations
// PERFORMANCE: Struct aligned (pointers first, then values)
type Service struct {
	repo      *repository.Repository // 8 bytes (pointer)
	logger    *zap.Logger           // 8 bytes (pointer)
	workers   chan struct{}         // 8 bytes (pointer)
	pool      *sync.Pool            // 8 bytes (pointer)
	// Padding for alignment
	_pad [0]byte
}

// ServiceInterface defines the service interface
type ServiceInterface interface {
	GetGameAnalyticsOverview(ctx context.Context, period string) (*models.GameAnalyticsOverview, error)
	GetPlayerBehaviorAnalytics(ctx context.Context, params api.GetPlayerBehaviorAnalyticsParams) (*models.PlayerBehaviorAnalytics, error)
	GetEconomicAnalytics(ctx context.Context, period string) (*models.EconomicAnalytics, error)
	GetCombatAnalytics(ctx context.Context, params api.GetCombatAnalyticsParams) (*models.CombatAnalytics, error)
	GetSocialAnalytics(ctx context.Context, period string) (*models.SocialAnalytics, error)
	GetRevenueAnalytics(ctx context.Context, period string) (*models.RevenueAnalytics, error)
	GetSystemPerformanceAnalytics(ctx context.Context, period string) (*models.SystemPerformanceAnalytics, error)
	GetAnalyticsAlerts(ctx context.Context, severity string, acknowledged *bool) (*models.AnalyticsAlerts, error)
	GenerateAnalyticsReport(ctx context.Context, params api.GenerateAnalyticsReportParams) (*models.AnalyticsReport, error)
}

// NewService creates a new service instance with PERFORMANCE optimizations
func NewService(repo *repository.Repository, logger *zap.Logger) ServiceInterface {
	return &Service{
		repo:    repo,
		logger:  logger,
		workers: make(chan struct{}, 10), // Worker pool for concurrent operations
		pool: &sync.Pool{
			New: func() interface{} {
				return &models.GameAnalyticsOverview{}
			},
		},
	}
}

// GetGameAnalyticsOverview retrieves comprehensive game analytics data
func (s *Service) GetGameAnalyticsOverview(ctx context.Context, period string) (*models.GameAnalyticsOverview, error) {
	s.logger.Info("Processing game analytics overview request",
		zap.String("period", period))

	// PERFORMANCE: Acquire worker from pool (limit concurrency)
	select {
	case s.workers <- struct{}{}:
		defer func() { <-s.workers }() // Release worker
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-time.After(5 * time.Second): // Timeout
		return nil, context.DeadlineExceeded
	}

	// PERFORMANCE: Add timeout for analytics queries
	queryCtx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	overview, err := s.repo.GetGameAnalyticsOverview(queryCtx, period)
	if err != nil {
		s.logger.Error("Failed to get game analytics overview",
			zap.String("period", period),
			zap.Error(err))
		return nil, err
	}

	s.logger.Info("Successfully retrieved game analytics overview",
		zap.String("period", period),
		zap.Int("active_users", overview.Summary.ActiveUsers))

	return overview, nil
}

// GetPlayerBehaviorAnalytics retrieves detailed player behavior analytics
func (s *Service) GetPlayerBehaviorAnalytics(ctx context.Context, params api.GetPlayerBehaviorAnalyticsParams) (*models.PlayerBehaviorAnalytics, error) {
	period := "24h"
	if params.Period.IsSet() {
		period = string(params.Period.Value)
	}
	segment := "all"
	if params.Segment.IsSet() {
		segment = string(params.Segment.Value)
	}

	s.logger.Info("Processing player behavior analytics request",
		zap.String("period", period),
		zap.String("segment", segment))

	queryCtx, cancel := context.WithTimeout(ctx, 15*time.Second)
	defer cancel()

	analytics, err := s.repo.GetPlayerBehaviorAnalytics(queryCtx, map[string]string{
		"period":  period,
		"segment": segment,
	})
	if err != nil {
		s.logger.Error("Failed to get player behavior analytics", zap.Error(err))
		return nil, err
	}

	return analytics, nil
}

// GetEconomicAnalytics retrieves economic analytics data
func (s *Service) GetEconomicAnalytics(ctx context.Context, period string) (*models.EconomicAnalytics, error) {
	s.logger.Info("Processing economic analytics request",
		zap.String("period", period))

	queryCtx, cancel := context.WithTimeout(ctx, 8*time.Second)
	defer cancel()

	analytics, err := s.repo.GetEconomicAnalytics(queryCtx, period)
	if err != nil {
		s.logger.Error("Failed to get economic analytics", zap.Error(err))
		return nil, err
	}

	return analytics, nil
}

// GetCombatAnalytics retrieves combat performance analytics
func (s *Service) GetCombatAnalytics(ctx context.Context, params api.GetCombatAnalyticsParams) (*models.CombatAnalytics, error) {
	period := "24h"
	if params.Period.IsSet() {
		period = string(params.Period.Value)
	}
	gameMode := "all"
	if params.GameMode.IsSet() {
		gameMode = string(params.GameMode.Value)
	}

	s.logger.Info("Processing combat analytics request",
		zap.String("period", period),
		zap.String("game_mode", gameMode))

	queryCtx, cancel := context.WithTimeout(ctx, 12*time.Second)
	defer cancel()

	analytics, err := s.repo.GetCombatAnalytics(queryCtx, params)
	if err != nil {
		s.logger.Error("Failed to get combat analytics", zap.Error(err))
		return nil, err
	}

	return analytics, nil
}

// GetSocialAnalytics retrieves social network analytics
func (s *Service) GetSocialAnalytics(ctx context.Context, period string) (*models.SocialAnalytics, error) {
	s.logger.Info("Processing social analytics request",
		zap.String("period", period))

	queryCtx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	analytics, err := s.repo.GetSocialAnalytics(queryCtx, period)
	if err != nil {
		s.logger.Error("Failed to get social analytics", zap.Error(err))
		return nil, err
	}

	return analytics, nil
}

// GetRevenueAnalytics retrieves revenue and monetization analytics
func (s *Service) GetRevenueAnalytics(ctx context.Context, period string) (*models.RevenueAnalytics, error) {
	s.logger.Info("Processing revenue analytics request",
		zap.String("period", period))

	queryCtx, cancel := context.WithTimeout(ctx, 8*time.Second)
	defer cancel()

	analytics, err := s.repo.GetRevenueAnalytics(queryCtx, period)
	if err != nil {
		s.logger.Error("Failed to get revenue analytics", zap.Error(err))
		return nil, err
	}

	return analytics, nil
}

// GetSystemPerformanceAnalytics retrieves system performance data
func (s *Service) GetSystemPerformanceAnalytics(ctx context.Context, period string) (*models.SystemPerformanceAnalytics, error) {
	s.logger.Info("Processing system performance analytics request",
		zap.String("period", period))

	queryCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	analytics, err := s.repo.GetSystemPerformanceAnalytics(queryCtx, period)
	if err != nil {
		s.logger.Error("Failed to get system performance analytics", zap.Error(err))
		return nil, err
	}

	return analytics, nil
}

// GetAnalyticsAlerts retrieves analytics alerts and notifications
func (s *Service) GetAnalyticsAlerts(ctx context.Context, severity string, acknowledged *bool) (*models.AnalyticsAlerts, error) {
	s.logger.Info("Processing analytics alerts request",
		zap.String("severity", severity))

	queryCtx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	alerts, err := s.repo.GetAnalyticsAlerts(queryCtx, severity, acknowledged)
	if err != nil {
		s.logger.Error("Failed to get analytics alerts", zap.Error(err))
		return nil, err
	}

	return alerts, nil
}

// GenerateAnalyticsReport generates custom analytics reports
func (s *Service) GenerateAnalyticsReport(ctx context.Context, params api.GenerateAnalyticsReportParams) (*models.AnalyticsReport, error) {
	s.logger.Info("Processing analytics report generation request",
		zap.String("report_type", string(params.ReportType)))

	// Longer timeout for report generation
	queryCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	report, err := s.repo.GenerateAnalyticsReport(queryCtx, params)
	if err != nil {
		s.logger.Error("Failed to generate analytics report", zap.Error(err))
		return nil, err
	}

	return report, nil
}
