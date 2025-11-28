package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-analytics-tools-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

type ToolsService struct {
	repository Repository
	logger     *logrus.Logger
}

func NewToolsService(repository Repository, logger *logrus.Logger) *ToolsService {
	return &ToolsService{
		repository: repository,
		logger:     logger,
	}
}

func (s *ToolsService) GetHeatmap(ctx context.Context, period string) (*api.Heatmap, error) {
	s.logger.WithField("period", period).Info("Getting heatmap data")
	
	heatmap, err := s.repository.GetHeatmapData(ctx, period)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get heatmap data")
		return nil, fmt.Errorf("failed to get heatmap data: %w", err)
	}

	return heatmap, nil
}

func (s *ToolsService) GetOrderBook(ctx context.Context, ticker string, depth int) (*api.OrderBook, error) {
	s.logger.WithFields(map[string]interface{}{
		"ticker": ticker,
		"depth":  depth,
	}).Info("Getting order book data")

	orderBook, err := s.repository.GetOrderBookData(ctx, ticker, depth)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get order book data")
		return nil, fmt.Errorf("failed to get order book data: %w", err)
	}

	return orderBook, nil
}

func (s *ToolsService) CreateAlert(ctx context.Context, playerID uuid.UUID, req *api.CreateAlertRequest) (*api.Alert, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id":      playerID,
		"ticker":         req.Ticker,
		"condition_type": req.ConditionType,
	}).Info("Creating alert")

	alertID := uuid.New()
	now := time.Now()

	conditionTypeStr := string(req.ConditionType)
	alert := &api.Alert{
		Id:            &alertID,
		PlayerId:      &playerID,
		Ticker:        &req.Ticker,
		ConditionType: &conditionTypeStr,
		Threshold:     &req.Threshold,
		IsActive:      func() *bool { v := true; return &v }(),
		CreatedAt:     &now,
	}

	if err := s.repository.CreateAlert(ctx, alert); err != nil {
		s.logger.WithError(err).Error("Failed to create alert")
		return nil, fmt.Errorf("failed to create alert: %w", err)
	}

	return alert, nil
}

func (s *ToolsService) ListAlerts(ctx context.Context, playerID uuid.UUID, activeOnly bool, limit, offset int) ([]api.Alert, int, error) {
	s.logger.WithFields(map[string]interface{}{
		"player_id":  playerID,
		"active_only": activeOnly,
		"limit":      limit,
		"offset":     offset,
	}).Info("Listing alerts")

	alerts, total, err := s.repository.GetAlerts(ctx, playerID, activeOnly, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list alerts")
		return nil, 0, fmt.Errorf("failed to list alerts: %w", err)
	}

	return alerts, total, nil
}

func (s *ToolsService) DeleteAlert(ctx context.Context, alertID uuid.UUID) error {
	s.logger.WithField("alert_id", alertID).Info("Deleting alert")

	if err := s.repository.DeleteAlert(ctx, alertID); err != nil {
		s.logger.WithError(err).Error("Failed to delete alert")
		return fmt.Errorf("failed to delete alert: %w", err)
	}

	return nil
}

func (s *ToolsService) GetPortfolioDashboard(ctx context.Context, playerID uuid.UUID) (*api.PortfolioDashboard, error) {
	s.logger.WithField("player_id", playerID).Info("Getting portfolio dashboard")

	dashboard, err := s.repository.GetPortfolioDashboard(ctx, playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get portfolio dashboard")
		return nil, fmt.Errorf("failed to get portfolio dashboard: %w", err)
	}

	return dashboard, nil
}

func (s *ToolsService) GetMarketDashboard(ctx context.Context) (*api.MarketDashboard, error) {
	s.logger.Info("Getting market dashboard")

	dashboard, err := s.repository.GetMarketDashboard(ctx)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get market dashboard")
		return nil, fmt.Errorf("failed to get market dashboard: %w", err)
	}

	return dashboard, nil
}

