// Anti-Cheat Behavior Analytics Service
// Issue: #2212
// Main service layer coordinating analytics and detection

package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"

	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/analytics"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/detection"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/repository"
)

// Service coordinates anti-cheat operations
type Service struct {
	repo       *repository.Repository
	analytics  *analytics.AnalyticsEngine
	detection  *detection.DetectionEngine
	logger     *zap.SugaredLogger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, analytics *analytics.AnalyticsEngine, detection *detection.DetectionEngine, logger *zap.SugaredLogger) *Service {
	// Set repository for detection engine
	detection.SetRepository(repo)

	return &Service{
		repo:      repo,
		analytics: analytics,
		detection: detection,
		logger:    logger,
	}
}

// ProcessEvent processes a game event
func (s *Service) ProcessEvent(message kafka.Message) error {
	ctx := context.Background()

	// Parse event data
	var event map[string]interface{}
	if err := json.Unmarshal(message.Value, &event); err != nil {
		s.logger.Errorf("Failed to parse event: %v", err)
		return err
	}

	playerID, ok := event["player_id"].(string)
	if !ok {
		s.logger.Warn("Event missing player_id, skipping")
		return nil
	}

	s.logger.Debugf("Processing event for player %s: %s", playerID, event["type"])

	// Process with analytics engine
	behaviorPattern, err := s.analytics.AnalyzePlayerBehavior(ctx, playerID, event)
	if err != nil {
		s.logger.Errorf("Analytics processing failed for player %s: %v", playerID, err)
		return err
	}

	// Save behavior data if confidence is significant
	if behaviorPattern.Confidence > 0.3 {
		behavior := &repository.PlayerBehavior{
			PlayerID:      playerID,
			SessionID:     getSessionID(event),
			BehaviorType:  behaviorPattern.Type,
			Data:          behaviorPattern.Data,
			Timestamp:     behaviorPattern.Timestamp,
			Confidence:    behaviorPattern.Confidence,
			RiskScore:     behaviorPattern.Confidence, // Simplified for now
		}

		if err := s.repo.SavePlayerBehavior(ctx, behavior); err != nil {
			s.logger.Errorf("Failed to save behavior data for player %s: %v", playerID, err)
			return err
		}
	}

	// Process with detection engine
	if err := s.detection.ProcessEvent(ctx, event); err != nil {
		s.logger.Errorf("Detection processing failed for player %s: %v", playerID, err)
		return err
	}

	s.logger.Debugf("Successfully processed event for player %s", playerID)
	return nil
}

// getSessionID extracts or generates session ID from event
func getSessionID(event map[string]interface{}) string {
	if sessionID, ok := event["session_id"].(string); ok {
		return sessionID
	}

	// Generate session ID from player and timestamp
	playerID := event["player_id"].(string)
	timestamp := time.Now().Unix()
	return fmt.Sprintf("%s_%d", playerID, timestamp)
}

// GetPlayerBehavior retrieves player behavior data
func (s *Service) GetPlayerBehavior(ctx context.Context, playerID string, limit int) ([]*repository.PlayerBehavior, error) {
	return s.repo.GetPlayerBehavior(ctx, playerID, limit)
}

// GetPlayerRiskScore retrieves player risk score
func (s *Service) GetPlayerRiskScore(ctx context.Context, playerID string) (float64, error) {
	return s.repo.GetPlayerRiskScore(ctx, playerID)
}

// FlagPlayer flags a player for manual review
func (s *Service) FlagPlayer(ctx context.Context, playerID, reason string, severity string) error {
	alert := &repository.Alert{
		ID:          fmt.Sprintf("manual_flag_%s_%d", playerID, time.Now().Unix()),
		PlayerID:    playerID,
		RuleID:      "manual_flag",
		Type:        "manual_review",
		Severity:    severity,
		Message:     fmt.Sprintf("Manually flagged: %s", reason),
		Data:        fmt.Sprintf("reason: %s", reason),
		Status:      "active",
		CreatedAt:   time.Now(),
	}

	return s.repo.SaveAlert(ctx, alert)
}

// GetMatchAnalysis performs analysis on a match
func (s *Service) GetMatchAnalysis(ctx context.Context, matchID string) (map[string]interface{}, error) {
	// This would analyze match-level statistics
	// Placeholder implementation
	return map[string]interface{}{
		"match_id":        matchID,
		"analysis_status": "completed",
		"anomalies_found": 0,
		"risk_score":      0.0,
		"timestamp":       time.Now(),
	}, nil
}

// GetMatchAnomalies retrieves anomalies for a match
func (s *Service) GetMatchAnomalies(ctx context.Context, matchID string) ([]map[string]interface{}, error) {
	// This would return detected anomalies for a match
	// Placeholder implementation
	return []map[string]interface{}{}, nil
}

// GetStatisticsSummary returns statistics summary
func (s *Service) GetStatisticsSummary(ctx context.Context) (*repository.Statistics, error) {
	return s.repo.GetStatistics(ctx)
}

// GetStatisticsTrends returns statistics trends
func (s *Service) GetStatisticsTrends(ctx context.Context, hours int) (map[string]interface{}, error) {
	// This would analyze trends over time
	// Placeholder implementation
	return map[string]interface{}{
		"period_hours":      hours,
		"risk_trend":        "stable",
		"alert_trend":       "decreasing",
		"player_trend":      "increasing",
		"last_updated":      time.Now(),
	}, nil
}

// GetTopRiskyPlayers returns top risky players
func (s *Service) GetTopRiskyPlayers(ctx context.Context, limit int) ([]map[string]interface{}, error) {
	highRiskPlayers := s.analytics.GetHighRiskPlayers(0.5)

	var results []map[string]interface{}
	for i, player := range highRiskPlayers {
		if i >= limit {
			break
		}

		results = append(results, map[string]interface{}{
			"player_id":         player.PlayerID,
			"risk_score":        player.RiskScore,
			"session_count":     player.SessionCount,
			"last_activity":     player.LastActivity,
			"behavior_patterns": player.BehaviorPatterns,
			"risk_indicators":   len(player.RiskIndicators),
		})
	}

	return results, nil
}

// GetDetectionRules retrieves all detection rules
func (s *Service) GetDetectionRules(ctx context.Context) ([]*repository.DetectionRule, error) {
	return s.repo.GetDetectionRules(ctx)
}

// CreateDetectionRule creates a new detection rule
func (s *Service) CreateDetectionRule(ctx context.Context, rule *repository.DetectionRule) error {
	rule.CreatedAt = time.Now()
	rule.UpdatedAt = time.Now()
	return s.repo.SaveDetectionRule(ctx, rule)
}

// UpdateDetectionRule updates an existing detection rule
func (s *Service) UpdateDetectionRule(ctx context.Context, ruleID string, config map[string]interface{}) error {
	// Get existing rule
	rules, err := s.repo.GetDetectionRules(ctx)
	if err != nil {
		return err
	}

	var existingRule *repository.DetectionRule
	for _, rule := range rules {
		if rule.ID == ruleID {
			existingRule = rule
			break
		}
	}

	if existingRule == nil {
		return fmt.Errorf("rule not found: %s", ruleID)
	}

	// Update rule
	existingRule.Config = config
	existingRule.UpdatedAt = time.Now()

	return s.repo.SaveDetectionRule(ctx, existingRule)
}

// DeleteDetectionRule deletes a detection rule
func (s *Service) DeleteDetectionRule(ctx context.Context, ruleID string) error {
	// Note: This is a simplified implementation
	// In a real system, you'd want soft deletes or proper rule management
	return fmt.Errorf("delete operation not implemented")
}

// GetAlerts retrieves alerts with filtering
func (s *Service) GetAlerts(ctx context.Context, status string, limit int) ([]*repository.Alert, error) {
	return s.repo.GetAlerts(ctx, status, limit)
}

// AcknowledgeAlert acknowledges an alert
func (s *Service) AcknowledgeAlert(ctx context.Context, alertID string) error {
	return s.repo.UpdateAlertStatus(ctx, alertID, "acknowledged")
}

// GetAlertDetails retrieves detailed alert information
func (s *Service) GetAlertDetails(ctx context.Context, alertID string) (*repository.Alert, error) {
	alerts, err := s.repo.GetAlerts(ctx, "", 1000) // Get all alerts (inefficient but works for now)
	if err != nil {
		return nil, err
	}

	for _, alert := range alerts {
		if alert.ID == alertID {
			return alert, nil
		}
	}

	return nil, fmt.Errorf("alert not found: %s", alertID)
}

// CleanupOldData performs maintenance cleanup
func (s *Service) CleanupOldData(ctx context.Context) error {
	// Cleanup old behavior data
	if err := s.repo.CleanupOldData(ctx, 90*24*time.Hour); err != nil {
		s.logger.Errorf("Failed to cleanup old data: %v", err)
		return err
	}

	s.logger.Info("Successfully cleaned up old analytics data")
	return nil
}

// GetAnalyticsSummary returns comprehensive analytics summary
func (s *Service) GetAnalyticsSummary(ctx context.Context) (map[string]interface{}, error) {
	// Get statistics from repository
	stats, err := s.repo.GetStatistics(ctx)
	if err != nil {
		return nil, err
	}

	// Get analytics engine summary
	analyticsSummary := s.analytics.GetAnalyticsSummary()

	// Combine summaries
	summary := map[string]interface{}{
		"database_stats":    stats,
		"analytics_stats":   analyticsSummary,
		"service_health":    "healthy",
		"last_updated":      time.Now(),
	}

	return summary, nil
}
