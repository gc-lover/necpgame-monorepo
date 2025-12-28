// Anti-Cheat Detection Engine
// Issue: #2212
// Real-time detection of cheating patterns and automated response

package detection

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/analytics"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/repository"
)

// DetectionEngine handles real-time cheating detection
type DetectionEngine struct {
	config         DetectionConfig
	analytics      *analytics.AnalyticsEngine
	repo           *repository.Repository
	logger         *zap.SugaredLogger
	rules          map[string]*DetectionRule
	activeChecks   sync.Map // map[string]*ActiveCheck
	mu             sync.RWMutex
}

// DetectionConfig holds detection configuration
type DetectionConfig struct {
	EnabledRules        []string
	UpdateInterval      time.Duration
	FalsePositiveRate   float64
	MinConfidence       float64
	MaxConcurrentChecks int
	CacheTTL            time.Duration
}

// DetectionRule represents a detection rule
type DetectionRule struct {
	ID          string
	Name        string
	Type        string
	Description string
	Config      RuleConfig
	Enabled     bool
}

// RuleConfig holds rule-specific configuration
type RuleConfig struct {
	Thresholds   map[string]float64 `json:"thresholds"`
	TimeWindows  map[string]int     `json:"time_windows"` // in seconds
	Weights      map[string]float64 `json:"weights"`
	Dependencies []string           `json:"dependencies"`
}

// ActiveCheck represents an ongoing detection check
type ActiveCheck struct {
	PlayerID    string
	RuleID      string
	StartTime   time.Time
	LastUpdate  time.Time
	DataPoints  []DataPoint
	Status      string
}

// DataPoint represents a single data measurement
type DataPoint struct {
	Timestamp time.Time
	Metric    string
	Value     float64
	Metadata  map[string]interface{}
}

// DetectionResult represents the result of a detection check
type DetectionResult struct {
	PlayerID    string
	RuleID      string
	Confidence  float64
	Severity    string
	Description string
	Data        map[string]interface{}
	Timestamp   time.Time
}

// NewDetectionEngine creates a new detection engine
func NewDetectionEngine(config DetectionConfig, analytics *analytics.AnalyticsEngine, logger *zap.SugaredLogger) *DetectionEngine {
	engine := &DetectionEngine{
		config:    config,
		analytics: analytics,
		logger:    logger,
		rules:     make(map[string]*DetectionRule),
	}

	engine.initializeDefaultRules()

	// Start background rule updates
	go engine.startRuleUpdates()

	return engine
}

// SetRepository sets the repository for the detection engine
func (d *DetectionEngine) SetRepository(repo *repository.Repository) {
	d.repo = repo
}

// ProcessEvent processes a game event for cheating detection
func (d *DetectionEngine) ProcessEvent(ctx context.Context, event map[string]interface{}) error {
	playerID, ok := event["player_id"].(string)
	if !ok {
		return fmt.Errorf("missing player_id in event")
	}

	eventType, ok := event["type"].(string)
	if !ok {
		return fmt.Errorf("missing event type")
	}

	// Start or update active checks for this player
	d.updateActiveChecks(playerID, event)

	// Run detection rules
	results := d.runDetectionRules(ctx, playerID, eventType, event)

	// Process detection results
	for _, result := range results {
		if result.Confidence >= d.config.MinConfidence {
			d.processDetectionResult(ctx, result)
		}
	}

	return nil
}

// updateActiveChecks updates active detection checks for a player
func (d *DetectionEngine) updateActiveChecks(playerID string, event map[string]interface{}) {
	eventType := event["type"].(string)
	timestamp := time.Now()

	// Update existing checks
	d.activeChecks.Range(func(key, value interface{}) bool {
		checkKey := key.(string)
		if checkKey[:len(playerID)] == playerID { // Player-specific checks
			check := value.(*ActiveCheck)

			// Add data point
			dataPoint := DataPoint{
				Timestamp: timestamp,
				Metric:    eventType,
				Value:     extractMetricValue(event),
				Metadata:  event,
			}

			check.DataPoints = append(check.DataPoints, dataPoint)
			check.LastUpdate = timestamp

			// Keep only recent data points (last 5 minutes)
			cutoff := timestamp.Add(-5 * time.Minute)
			var recentPoints []DataPoint
			for _, point := range check.DataPoints {
				if point.Timestamp.After(cutoff) {
					recentPoints = append(recentPoints, point)
				}
			}
			check.DataPoints = recentPoints

			d.activeChecks.Store(checkKey, check)
		}
		return true
	})
}

// extractMetricValue extracts a numeric metric value from event data
func extractMetricValue(event map[string]interface{}) float64 {
	// Try common metric fields
	if val, ok := event["value"].(float64); ok {
		return val
	}
	if val, ok := event["amount"].(float64); ok {
		return val
	}
	if val, ok := event["count"].(float64); ok {
		return val
	}
	if val, ok := event["damage"].(float64); ok {
		return val
	}
	if val, ok := event["accuracy"].(float64); ok {
		return val
	}

	// Default to 1.0 for counting events
	return 1.0
}

// runDetectionRules runs all enabled detection rules
func (d *DetectionEngine) runDetectionRules(ctx context.Context, playerID, eventType string, event map[string]interface{}) []*DetectionResult {
	var results []*DetectionResult

	d.mu.RLock()
	defer d.mu.RUnlock()

	for ruleID, rule := range d.rules {
		if !rule.Enabled {
			continue
		}

		result := d.evaluateRule(ctx, playerID, rule, eventType, event)
		if result != nil {
			results = append(results, result)
		}
	}

	return results
}

// evaluateRule evaluates a single detection rule
func (d *DetectionEngine) evaluateRule(ctx context.Context, playerID string, rule *DetectionRule, eventType string, event map[string]interface{}) *DetectionResult {
	switch rule.Type {
	case "aimbot_detection":
		return d.detectAimbot(playerID, event)
	case "speed_hack_detection":
		return d.detectSpeedHack(playerID, event)
	case "wallhack_detection":
		return d.detectWallhack(playerID, event)
	case "macro_detection":
		return d.detectMacro(playerID, event)
	case "stat_anomaly_detection":
		return d.detectStatAnomaly(playerID, event)
	default:
		d.logger.Warnf("Unknown detection rule type: %s", rule.Type)
		return nil
	}
}

// detectAimbot detects aimbot usage patterns
func (d *DetectionEngine) detectAimbot(playerID string, event map[string]interface{}) *DetectionResult {
	if event["type"] != "aim_assist" {
		return nil
	}

	// Analyze aim patterns
	accuracy, ok := event["accuracy"].(float64)
	if !ok {
		return nil
	}

	headshots, ok := event["headshots"].(float64)
	if !ok {
		return nil
	}

	totalShots, ok := event["total_shots"].(float64)
	if !ok {
		return nil
	}

	// Suspicious patterns
	headshotRate := headshots / totalShots
	suspicionScore := 0.0

	if accuracy > 0.95 && totalShots > 50 {
		suspicionScore += 0.6
	}

	if headshotRate > 0.8 {
		suspicionScore += 0.4
	}

	if suspicionScore > 0.5 {
		return &DetectionResult{
			PlayerID:    playerID,
			RuleID:      "aimbot_detection",
			Confidence:  suspicionScore,
			Severity:    "high",
			Description: fmt.Sprintf("Aimbot suspicion: %.1f%% accuracy, %.1f%% headshot rate", accuracy*100, headshotRate*100),
			Data:        event,
			Timestamp:   time.Now(),
		}
	}

	return nil
}

// detectSpeedHack detects speed hack usage
func (d *DetectionEngine) detectSpeedHack(playerID string, event map[string]interface{}) *DetectionResult {
	if event["type"] != "movement" {
		return nil
	}

	speed, ok := event["speed"].(float64)
	if !ok {
		return nil
	}

	normalSpeed := 10.0 // Normal max speed

	if speed > normalSpeed*2 {
		return &DetectionResult{
			PlayerID:    playerID,
			RuleID:      "speed_hack_detection",
			Confidence:  0.8,
			Severity:    "high",
			Description: fmt.Sprintf("Speed hack detected: %.1f units/sec (normal max: %.1f)", speed, normalSpeed),
			Data:        event,
			Timestamp:   time.Now(),
		}
	}

	return nil
}

// detectWallhack detects wallhack usage
func (d *DetectionEngine) detectWallhack(playerID string, event map[string]interface{}) *DetectionResult {
	// Wallhack detection is complex and requires game-specific data
	// This is a placeholder implementation
	return nil
}

// detectMacro detects macro/script usage
func (d *DetectionEngine) detectMacro(playerID string, event map[string]interface{}) *DetectionResult {
	if event["type"] != "input_pattern" {
		return nil
	}

	// Analyze input patterns for automation
	// This would require more complex pattern analysis
	return nil
}

// detectStatAnomaly detects statistical anomalies
func (d *DetectionEngine) detectStatAnomaly(playerID string, event map[string]interface{}) *DetectionResult {
	// Get player statistics from analytics engine
	stats, exists := d.analytics.GetPlayerStats(playerID)
	if !exists {
		return nil
	}

	// Check for statistical anomalies
	if stats.RiskScore > 0.8 {
		return &DetectionResult{
			PlayerID:    playerID,
			RuleID:      "stat_anomaly_detection",
			Confidence:  stats.RiskScore,
			Severity:    "medium",
			Description: fmt.Sprintf("Statistical anomaly detected: risk score %.2f", stats.RiskScore),
			Data:        map[string]interface{}{"risk_score": stats.RiskScore},
			Timestamp:   time.Now(),
		}
	}

	return nil
}

// processDetectionResult processes a detection result
func (d *DetectionEngine) processDetectionResult(ctx context.Context, result *DetectionResult) {
	d.logger.Warnf("Detection alert: %s for player %s (confidence: %.2f)",
		result.Description, result.PlayerID, result.Confidence)

	// Save to repository if available
	if d.repo != nil {
		alert := &repository.Alert{
			ID:          fmt.Sprintf("alert_%s_%d", result.PlayerID, time.Now().Unix()),
			PlayerID:    result.PlayerID,
			RuleID:      result.RuleID,
			Type:        result.RuleID,
			Severity:    result.Severity,
			Message:     result.Description,
			Data:        fmt.Sprintf("%v", result.Data),
			Status:      "active",
			CreatedAt:   result.Timestamp,
		}

		if err := d.repo.SaveAlert(ctx, alert); err != nil {
			d.logger.Errorf("Failed to save alert: %v", err)
		}
	}

	// TODO: Send real-time notification to game servers
	// TODO: Trigger automated response (temporary ban, monitoring, etc.)
}

// initializeDefaultRules initializes default detection rules
func (d *DetectionEngine) initializeDefaultRules() {
	d.mu.Lock()
	defer d.mu.Unlock()

	rules := []*DetectionRule{
		{
			ID:          "aimbot_detection",
			Name:        "Aimbot Detection",
			Type:        "aimbot_detection",
			Description: "Detects aimbot usage through accuracy and headshot patterns",
			Config: RuleConfig{
				Thresholds: map[string]float64{
					"accuracy_threshold": 0.95,
					"headshot_threshold": 0.8,
				},
				TimeWindows: map[string]int{
					"analysis_window": 300, // 5 minutes
				},
			},
			Enabled: true,
		},
		{
			ID:          "speed_hack_detection",
			Name:        "Speed Hack Detection",
			Type:        "speed_hack_detection",
			Description: "Detects speed hack usage through movement patterns",
			Config: RuleConfig{
				Thresholds: map[string]float64{
					"speed_multiplier": 2.0,
				},
			},
			Enabled: true,
		},
		{
			ID:          "wallhack_detection",
			Name:        "Wallhack Detection",
			Type:        "wallhack_detection",
			Description: "Detects wallhack usage through visibility patterns",
			Config: RuleConfig{
				Thresholds: map[string]float64{
					"visibility_anomaly": 0.7,
				},
			},
			Enabled: false, // Requires game-specific implementation
		},
		{
			ID:          "macro_detection",
			Name:        "Macro Detection",
			Type:        "macro_detection",
			Description: "Detects macro/script usage through input patterns",
			Config: RuleConfig{
				Thresholds: map[string]float64{
					"pattern_consistency": 0.95,
				},
				TimeWindows: map[string]int{
					"pattern_window": 60, // 1 minute
				},
			},
			Enabled: false, // Requires advanced implementation
		},
		{
			ID:          "stat_anomaly_detection",
			Name:        "Statistical Anomaly Detection",
			Type:        "stat_anomaly_detection",
			Description: "Detects cheating through statistical analysis",
			Config: RuleConfig{
				Thresholds: map[string]float64{
					"risk_threshold": 0.8,
				},
			},
			Enabled: true,
		},
	}

	for _, rule := range rules {
		// Only enable rules that are in the enabled list
		rule.Enabled = contains(d.config.EnabledRules, rule.ID)
		d.rules[rule.ID] = rule
	}
}

// startRuleUpdates starts background rule updates
func (d *DetectionEngine) startRuleUpdates() {
	ticker := time.NewTicker(d.config.UpdateInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			d.updateRulesFromRepository()
		}
	}
}

// updateRulesFromRepository updates rules from repository
func (d *DetectionEngine) updateRulesFromRepository() {
	if d.repo == nil {
		return
	}

	ctx := context.Background()
	rules, err := d.repo.GetDetectionRules(ctx)
	if err != nil {
		d.logger.Errorf("Failed to update rules from repository: %v", err)
		return
	}

	d.mu.Lock()
	for _, repoRule := range rules {
		if localRule, exists := d.rules[repoRule.ID]; exists {
			localRule.Enabled = repoRule.Enabled
			localRule.Config = d.parseRuleConfig(repoRule.Config)
		}
	}
	d.mu.Unlock()

	d.logger.Info("Updated detection rules from repository")
}

// parseRuleConfig parses rule configuration
func (d *DetectionEngine) parseRuleConfig(config map[string]interface{}) RuleConfig {
	ruleConfig := RuleConfig{
		Thresholds:  make(map[string]float64),
		TimeWindows: make(map[string]int),
		Weights:     make(map[string]float64),
	}

	if thresholds, ok := config["thresholds"].(map[string]interface{}); ok {
		for k, v := range thresholds {
			if val, ok := v.(float64); ok {
				ruleConfig.Thresholds[k] = val
			}
		}
	}

	if timeWindows, ok := config["time_windows"].(map[string]interface{}); ok {
		for k, v := range timeWindows {
			if val, ok := v.(float64); ok {
				ruleConfig.TimeWindows[k] = int(val)
			}
		}
	}

	if weights, ok := config["weights"].(map[string]interface{}); ok {
		for k, v := range weights {
			if val, ok := v.(float64); ok {
				ruleConfig.Weights[k] = val
			}
		}
	}

	return ruleConfig
}

// contains checks if slice contains string
func contains(slice []string, item string) bool {
	for _, s := range slice {
		if s == item {
			return true
		}
	}
	return false
}
