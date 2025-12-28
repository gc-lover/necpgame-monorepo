// Anti-Cheat Analytics Engine
// Issue: #2212
// ML-based behavioral analysis for anti-cheat detection

package analytics

import (
	"context"
	"fmt"
	"math"
	"sort"
	"sync"
	"time"

	"go.uber.org/zap"
)

// AnalyticsEngine handles behavioral analytics and pattern detection
type AnalyticsEngine struct {
	config      AnalyticsConfig
	logger      *zap.SugaredLogger
	playerStats sync.Map // map[string]*PlayerStats
	mu          sync.RWMutex
}

// AnalyticsConfig holds analytics configuration
type AnalyticsConfig struct {
	BatchSize          int
	ProcessingInterval time.Duration
	RetentionPeriod    time.Duration
	MaxConcurrentJobs  int
	EnableRealTime     bool
	AlertThreshold     float64
}

// PlayerStats holds aggregated player statistics
type PlayerStats struct {
	PlayerID           string
	SessionCount       int
	TotalPlayTime      time.Duration
	AvgSessionLength   time.Duration
	LastActivity       time.Time
	BehaviorPatterns   map[string]float64 // pattern -> confidence score
	RiskIndicators     []RiskIndicator
	RiskScore          float64
}

// RiskIndicator represents a risk indicator
type RiskIndicator struct {
	Type        string
	Value       float64
	Threshold   float64
	Confidence  float64
	Timestamp   time.Time
	Description string
}

// BehaviorPattern represents a detected behavior pattern
type BehaviorPattern struct {
	Type       string
	PlayerID   string
	Confidence float64
	Data       map[string]interface{}
	Timestamp  time.Time
}

// NewAnalyticsEngine creates a new analytics engine
func NewAnalyticsEngine(config AnalyticsConfig, logger *zap.SugaredLogger) *AnalyticsEngine {
	engine := &AnalyticsEngine{
		config: config,
		logger: logger,
	}

	// Start background processing
	if config.EnableRealTime {
		go engine.startBackgroundProcessing()
	}

	return engine
}

// AnalyzePlayerBehavior analyzes player behavior data
func (a *AnalyticsEngine) AnalyzePlayerBehavior(ctx context.Context, playerID string, behaviorData map[string]interface{}) (*BehaviorPattern, error) {
	// Extract relevant metrics from behavior data
	pattern := &BehaviorPattern{
		PlayerID:   playerID,
		Timestamp:  time.Now(),
		Data:       behaviorData,
	}

	// Analyze different behavior types
	if aimData, ok := behaviorData["aim_assist"].(map[string]interface{}); ok {
		if score := a.analyzeAimAssist(aimData); score > 0.5 {
			pattern.Type = "aimbot_suspicion"
			pattern.Confidence = score
			a.logger.Warnf("Aimbot suspicion detected for player %s (confidence: %.2f)", playerID, score)
		}
	}

	if speedData, ok := behaviorData["movement"].(map[string]interface{}); ok {
		if score := a.analyzeSpeedHack(speedData); score > 0.5 {
			pattern.Type = "speed_hack_suspicion"
			pattern.Confidence = score
			a.logger.Warnf("Speed hack suspicion detected for player %s (confidence: %.2f)", playerID, score)
		}
	}

	if damageData, ok := behaviorData["damage"].(map[string]interface{}); ok {
		if score := a.analyzeDamageAnomaly(damageData); score > 0.5 {
			pattern.Type = "damage_anomaly"
			pattern.Confidence = score
			a.logger.Warnf("Damage anomaly detected for player %s (confidence: %.2f)", playerID, score)
		}
	}

	// Update player statistics
	a.updatePlayerStats(playerID, pattern)

	return pattern, nil
}

// analyzeAimAssist analyzes aim assist patterns
func (a *AnalyticsEngine) analyzeAimAssist(data map[string]interface{}) float64 {
	// Extract aim metrics
	headshotRate, _ := data["headshot_rate"].(float64)
	accuracy, _ := data["accuracy"].(float64)
	reactionTime, _ := data["avg_reaction_time"].(float64)

	// Suspicious patterns:
	// - Very high headshot rate (>80%)
	// - Perfect accuracy with fast reactions
	// - Consistent performance across sessions

	suspicionScore := 0.0

	if headshotRate > 0.8 {
		suspicionScore += 0.4
	}

	if accuracy > 0.95 && reactionTime < 150 { // < 150ms reaction time
		suspicionScore += 0.4
	}

	if consistency, ok := data["performance_consistency"].(float64); ok && consistency > 0.9 {
		suspicionScore += 0.2
	}

	return math.Min(suspicionScore, 1.0)
}

// analyzeSpeedHack analyzes movement speed patterns
func (a *AnalyticsEngine) analyzeSpeedHack(data map[string]interface{}) float64 {
	// Extract movement metrics
	maxSpeed, _ := data["max_speed"].(float64)
	avgSpeed, _ := data["avg_speed"].(float64)
	teleportEvents, _ := data["teleport_events"].(float64)

	// Normal speeds for different game modes
	normalMaxSpeed := 10.0 // units per second
	normalAvgSpeed := 5.0

	suspicionScore := 0.0

	if maxSpeed > normalMaxSpeed*2 {
		suspicionScore += 0.5
	}

	if avgSpeed > normalAvgSpeed*1.5 {
		suspicionScore += 0.3
	}

	if teleportEvents > 5 {
		suspicionScore += 0.2
	}

	return math.Min(suspicionScore, 1.0)
}

// analyzeDamageAnomaly analyzes damage patterns
func (a *AnalyticsEngine) analyzeDamageAnomaly(data map[string]interface{}) float64 {
	// Extract damage metrics
	damageDealt, _ := data["damage_dealt"].(float64)
	damageTaken, _ := data["damage_taken"].(float64)
	kills, _ := data["kills"].(float64)
	deaths, _ := data["deaths"].(float64)
	hits, _ := data["hits"].(float64)
	shots, _ := data["shots"].(float64)

	// Calculate ratios
	kdRatio := 0.0
	if deaths > 0 {
		kdRatio = kills / deaths
	}

	accuracy := 0.0
	if shots > 0 {
		accuracy = hits / shots
	}

	damageRatio := 0.0
	if damageTaken > 0 {
		damageRatio = damageDealt / damageTaken
	}

	suspicionScore := 0.0

	// Suspicious patterns:
	// - Extremely high K/D ratio (> 5.0)
	// - Perfect accuracy (> 95%)
	// - Unrealistic damage ratios
	// - No damage taken but high kills

	if kdRatio > 5.0 {
		suspicionScore += 0.3
	}

	if accuracy > 0.95 && shots > 100 {
		suspicionScore += 0.3
	}

	if damageRatio > 10.0 {
		suspicionScore += 0.2
	}

	if deaths == 0 && kills > 10 {
		suspicionScore += 0.2
	}

	return math.Min(suspicionScore, 1.0)
}

// updatePlayerStats updates player statistics with new behavior data
func (a *AnalyticsEngine) updatePlayerStats(playerID string, pattern *BehaviorPattern) {
	stats, _ := a.playerStats.LoadOrStore(playerID, &PlayerStats{
		PlayerID:         playerID,
		BehaviorPatterns: make(map[string]float64),
		RiskIndicators:   []RiskIndicator{},
	})

	playerStats := stats.(*PlayerStats)

	// Update behavior patterns
	if pattern.Confidence > 0.3 {
		playerStats.BehaviorPatterns[pattern.Type] = pattern.Confidence
	}

	// Add risk indicator if confidence is high
	if pattern.Confidence > a.config.AlertThreshold {
		indicator := RiskIndicator{
			Type:        pattern.Type,
			Value:       pattern.Confidence,
			Threshold:   a.config.AlertThreshold,
			Confidence:  pattern.Confidence,
			Timestamp:   pattern.Timestamp,
			Description: fmt.Sprintf("Detected %s with confidence %.2f", pattern.Type, pattern.Confidence),
		}

		playerStats.RiskIndicators = append(playerStats.RiskIndicators, indicator)

		// Keep only recent indicators (last 24 hours)
		cutoff := time.Now().Add(-24 * time.Hour)
		var recentIndicators []RiskIndicator
		for _, ind := range playerStats.RiskIndicators {
			if ind.Timestamp.After(cutoff) {
				recentIndicators = append(recentIndicators, ind)
			}
		}
		playerStats.RiskIndicators = recentIndicators
	}

	// Calculate overall risk score
	playerStats.RiskScore = a.calculateRiskScore(playerStats)
	playerStats.LastActivity = time.Now()

	a.playerStats.Store(playerID, playerStats)
}

// calculateRiskScore calculates overall risk score for a player
func (a *AnalyticsEngine) calculateRiskScore(stats *PlayerStats) float64 {
	if len(stats.RiskIndicators) == 0 {
		return 0.0
	}

	// Weight recent indicators more heavily
	totalScore := 0.0
	totalWeight := 0.0

	now := time.Now()
	for _, indicator := range stats.RiskIndicators {
		// Time-based weighting (newer = higher weight)
		hoursOld := now.Sub(indicator.Timestamp).Hours()
		weight := math.Max(0.1, 1.0-(hoursOld/24.0)) // Linear decay over 24 hours

		totalScore += indicator.Confidence * weight
		totalWeight += weight
	}

	if totalWeight == 0 {
		return 0.0
	}

	riskScore := totalScore / totalWeight

	// Cap at 1.0 and apply sigmoid for smoother distribution
	riskScore = math.Min(riskScore, 1.0)
	riskScore = 1.0 / (1.0 + math.Exp(-5*(riskScore-0.5))) // Sigmoid centered at 0.5

	return riskScore
}

// GetPlayerStats retrieves player statistics
func (a *AnalyticsEngine) GetPlayerStats(playerID string) (*PlayerStats, bool) {
	stats, exists := a.playerStats.Load(playerID)
	if !exists {
		return nil, false
	}

	playerStats := stats.(*PlayerStats)
	return playerStats, true
}

// GetHighRiskPlayers returns players with high risk scores
func (a *AnalyticsEngine) GetHighRiskPlayers(threshold float64) []*PlayerStats {
	var highRiskPlayers []*PlayerStats

	a.playerStats.Range(func(key, value interface{}) bool {
		stats := value.(*PlayerStats)
		if stats.RiskScore >= threshold {
			highRiskPlayers = append(highRiskPlayers, stats)
		}
		return true
	})

	// Sort by risk score descending
	sort.Slice(highRiskPlayers, func(i, j int) bool {
		return highRiskPlayers[i].RiskScore > highRiskPlayers[j].RiskScore
	})

	return highRiskPlayers
}

// GetAnalyticsSummary returns analytics summary
func (a *AnalyticsEngine) GetAnalyticsSummary() map[string]interface{} {
	totalPlayers := 0
	highRiskCount := 0
	totalRiskIndicators := 0
	avgRiskScore := 0.0

	a.playerStats.Range(func(key, value interface{}) bool {
		totalPlayers++
		stats := value.(*PlayerStats)

		if stats.RiskScore > 0.7 {
			highRiskCount++
		}

		avgRiskScore += stats.RiskScore
		totalRiskIndicators += len(stats.RiskIndicators)

		return true
	})

	if totalPlayers > 0 {
		avgRiskScore /= float64(totalPlayers)
	}

	return map[string]interface{}{
		"total_players":         totalPlayers,
		"high_risk_players":     highRiskCount,
		"average_risk_score":    avgRiskScore,
		"total_risk_indicators": totalRiskIndicators,
		"last_updated":          time.Now(),
	}
}

// startBackgroundProcessing starts background analytics processing
func (a *AnalyticsEngine) startBackgroundProcessing() {
	ticker := time.NewTicker(a.config.ProcessingInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			a.processAnalyticsBatch()
		}
	}
}

// processAnalyticsBatch processes analytics in batches
func (a *AnalyticsEngine) processAnalyticsBatch() {
	// Clean up old player stats
	a.cleanupOldStats()

	// Log summary statistics
	summary := a.GetAnalyticsSummary()
	a.logger.Infof("Analytics summary: %d players, %d high-risk, avg risk: %.3f",
		summary["total_players"], summary["high_risk_players"], summary["average_risk_score"])
}

// cleanupOldStats removes old player statistics
func (a *AnalyticsEngine) cleanupOldStats() {
	cutoff := time.Now().Add(-a.config.RetentionPeriod)

	a.playerStats.Range(func(key, value interface{}) bool {
		stats := value.(*PlayerStats)
		if stats.LastActivity.Before(cutoff) {
			a.playerStats.Delete(key)
			a.logger.Debugf("Cleaned up old stats for player %s", key)
		}
		return true
	})
}
