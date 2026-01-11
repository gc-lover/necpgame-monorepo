// Advanced Threat Detection System
// Issue: #2163
// PERFORMANCE: DDoS mitigation, anomaly detection, behavioral analysis

package threatdetection

import (
	"context"
	"fmt"
	"sync"
	"sync/atomic"
	"time"

	"github.com/go-faster/errors"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// ThreatLevel represents the severity of a detected threat
type ThreatLevel int

const (
	ThreatLevelLow ThreatLevel = iota
	ThreatLevelMedium
	ThreatLevelHigh
	ThreatLevelCritical
)

// ThreatType represents the type of threat detected
type ThreatType string

const (
	ThreatTypeDDoS        ThreatType = "ddos"
	ThreatTypeAnomaly     ThreatType = "anomaly"
	ThreatTypeBehavioral  ThreatType = "behavioral"
	ThreatTypeBruteForce  ThreatType = "brute_force"
	ThreatTypeSuspicious  ThreatType = "suspicious"
)

// Threat represents a detected security threat
type Threat struct {
	ID          string
	Type        ThreatType
	Level       ThreatLevel
	Source      string // IP address or user ID
	Description string
	Timestamp   time.Time
	Metadata    map[string]interface{}
	Score       float64 // 0.0 - 1.0 threat score
}

// DetectorConfig holds configuration for threat detection
type DetectorConfig struct {
	Redis        *redis.Client
	Logger       *zap.Logger
	// DDoS detection
	DDosThreshold      int           // Requests per window
	DDosWindow         time.Duration // Time window for DDoS detection
	DDosBlockDuration  time.Duration // Block duration after detection
	// Anomaly detection
	AnomalyThreshold   float64       // Statistical threshold (z-score)
	AnomalyWindow      time.Duration // Time window for anomaly detection
	// Behavioral analysis
	BehaviorWindow     time.Duration // Time window for behavioral patterns
	BehaviorThreshold  float64       // Threshold for behavioral anomalies
}

// Detector provides advanced threat detection capabilities
type Detector struct {
	config DetectorConfig

	// Rate tracking (IP-based)
	ipRequestCounts map[string]*RequestCounter
	ipMutex         sync.RWMutex

	// Behavioral patterns (user-based)
	userPatterns map[string]*BehaviorPattern
	userMutex    sync.RWMutex

	// Statistics
	totalThreats     atomic.Int64
	ddosDetections   atomic.Int64
	anomalyDetections atomic.Int64
	behavioralDetections atomic.Int64
}

// RequestCounter tracks request counts for DDoS detection
type RequestCounter struct {
	Count     int64
	WindowStart time.Time
	BlockedUntil *time.Time
}

// BehaviorPattern tracks user behavior patterns
type BehaviorPattern struct {
	UserID        string
	RequestRate   float64
	ErrorRate     float64
	LatencyAvg    time.Duration
	LastSeen      time.Time
	SuspiciousScore float64
}

// NewDetector creates a new threat detection system
func NewDetector(config DetectorConfig) (*Detector, error) {
	if config.Redis == nil {
		return nil, errors.New("redis client is required")
	}
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}

	d := &Detector{
		config:         config,
		ipRequestCounts: make(map[string]*RequestCounter),
		userPatterns:   make(map[string]*BehaviorPattern),
	}

	// Start background cleanup
	go d.cleanupExpiredCounters()

	return d, nil
}

// AnalyzeRequest analyzes a request for potential threats
func (d *Detector) AnalyzeRequest(ctx context.Context, sourceIP, userID string, requestTime time.Duration, isError bool) (*Threat, error) {
	// Check DDoS
	if threat := d.detectDDoS(ctx, sourceIP); threat != nil {
		return threat, nil
	}

	// Check anomalies
	if threat := d.detectAnomaly(ctx, sourceIP, userID, requestTime, isError); threat != nil {
		return threat, nil
	}

	// Check behavioral patterns
	if threat := d.detectBehavioral(ctx, userID, requestTime, isError); threat != nil {
		return threat, nil
	}

	return nil, nil
}

// detectDDoS detects DDoS attacks based on request rate
func (d *Detector) detectDDoS(ctx context.Context, sourceIP string) *Threat {
	d.ipMutex.Lock()
	defer d.ipMutex.Unlock()

	counter, exists := d.ipRequestCounts[sourceIP]
	now := time.Now()

	if !exists {
		counter = &RequestCounter{
			Count:      1,
			WindowStart: now,
		}
		d.ipRequestCounts[sourceIP] = counter
		return nil
	}

	// Check if IP is blocked
	if counter.BlockedUntil != nil && now.Before(*counter.BlockedUntil) {
		return &Threat{
			ID:          fmt.Sprintf("ddos-blocked-%s", sourceIP),
			Type:        ThreatTypeDDoS,
			Level:       ThreatLevelHigh,
			Source:      sourceIP,
			Description: "IP is currently blocked due to DDoS detection",
			Timestamp:   now,
			Score:       1.0,
		}
	}

	// Reset window if expired
	if now.Sub(counter.WindowStart) > d.config.DDosWindow {
		counter.Count = 1
		counter.WindowStart = now
		counter.BlockedUntil = nil
		return nil
	}

	// Increment count
	counter.Count++

	// Check threshold
	if counter.Count >= int64(d.config.DDosThreshold) {
		blockUntil := now.Add(d.config.DDosBlockDuration)
		counter.BlockedUntil = &blockUntil

		d.ddosDetections.Add(1)
		d.totalThreats.Add(1)

		// Store in Redis for distributed blocking
		blockKey := fmt.Sprintf("threat:blocked:ip:%s", sourceIP)
		d.config.Redis.Set(ctx, blockKey, blockUntil.Unix(), d.config.DDosBlockDuration)

		return &Threat{
			ID:          fmt.Sprintf("ddos-%s-%d", sourceIP, now.Unix()),
			Type:        ThreatTypeDDoS,
			Level:       ThreatLevelCritical,
			Source:      sourceIP,
			Description: fmt.Sprintf("DDoS attack detected: %d requests in %v", counter.Count, d.config.DDosWindow),
			Timestamp:   now,
			Metadata: map[string]interface{}{
				"request_count": counter.Count,
				"window":        d.config.DDosWindow.String(),
				"blocked_until": blockUntil,
			},
			Score: 1.0,
		}
	}

	return nil
}

// detectAnomaly detects statistical anomalies
func (d *Detector) detectAnomaly(ctx context.Context, sourceIP, userID string, requestTime time.Duration, isError bool) *Threat {
	// Get historical data from Redis
	key := fmt.Sprintf("threat:stats:ip:%s", sourceIP)
	stats, err := d.getStats(ctx, key)
	if err != nil {
		return nil
	}

	// Calculate z-score for request time
	if stats.Count > 10 {
		zScore := (float64(requestTime) - stats.AvgLatency) / stats.StdDevLatency
		if zScore > d.config.AnomalyThreshold {
			d.anomalyDetections.Add(1)
			d.totalThreats.Add(1)

			return &Threat{
				ID:          fmt.Sprintf("anomaly-%s-%d", sourceIP, time.Now().Unix()),
				Type:        ThreatTypeAnomaly,
				Level:       ThreatLevelMedium,
				Source:      sourceIP,
				Description: fmt.Sprintf("Anomalous request time detected: z-score %.2f", zScore),
				Timestamp:   time.Now(),
				Metadata: map[string]interface{}{
					"z_score":      zScore,
					"request_time": requestTime.String(),
					"avg_latency":  stats.AvgLatency,
				},
				Score: min(zScore/d.config.AnomalyThreshold, 1.0),
			}
		}
	}

	// Update stats
	d.updateStats(ctx, key, requestTime, isError)

	return nil
}

// detectBehavioral detects behavioral anomalies
func (d *Detector) detectBehavioral(ctx context.Context, userID string, requestTime time.Duration, isError bool) *Threat {
	if userID == "" {
		return nil
	}

	d.userMutex.Lock()
	defer d.userMutex.Unlock()

	pattern, exists := d.userPatterns[userID]
	now := time.Now()

	if !exists {
		pattern = &BehaviorPattern{
			UserID:      userID,
			RequestRate: 1.0,
			ErrorRate:   0.0,
			LatencyAvg:  requestTime,
			LastSeen:    now,
		}
		d.userPatterns[userID] = pattern
		return nil
	}

	// Update pattern
	timeSinceLastSeen := now.Sub(pattern.LastSeen)
	if timeSinceLastSeen > 0 {
		pattern.RequestRate = 0.9*pattern.RequestRate + 0.1*(1.0/timeSinceLastSeen.Seconds())
	}
	if isError {
		pattern.ErrorRate = 0.9*pattern.ErrorRate + 0.1*1.0
	} else {
		pattern.ErrorRate = 0.9 * pattern.ErrorRate
	}
	pattern.LatencyAvg = time.Duration(0.9*float64(pattern.LatencyAvg) + 0.1*float64(requestTime))
	pattern.LastSeen = now

	// Calculate suspicious score
	pattern.SuspiciousScore = d.calculateSuspiciousScore(pattern)

	// Check threshold
	if pattern.SuspiciousScore > d.config.BehaviorThreshold {
		d.behavioralDetections.Add(1)
		d.totalThreats.Add(1)

		return &Threat{
			ID:          fmt.Sprintf("behavioral-%s-%d", userID, now.Unix()),
			Type:        ThreatTypeBehavioral,
			Level:       ThreatLevelHigh,
			Source:      userID,
			Description: fmt.Sprintf("Suspicious behavioral pattern detected: score %.2f", pattern.SuspiciousScore),
			Timestamp:   now,
			Metadata: map[string]interface{}{
				"request_rate":   pattern.RequestRate,
				"error_rate":     pattern.ErrorRate,
				"latency_avg":    pattern.LatencyAvg.String(),
				"suspicious_score": pattern.SuspiciousScore,
			},
			Score: pattern.SuspiciousScore,
		}
	}

	return nil
}

// calculateSuspiciousScore calculates a suspicious behavior score
func (d *Detector) calculateSuspiciousScore(pattern *BehaviorPattern) float64 {
	score := 0.0

	// High request rate (potential bot)
	if pattern.RequestRate > 10.0 {
		score += 0.3
	}

	// High error rate (potential scanning)
	if pattern.ErrorRate > 0.5 {
		score += 0.3
	}

	// Unusual latency patterns
	if pattern.LatencyAvg < 1*time.Millisecond || pattern.LatencyAvg > 1*time.Second {
		score += 0.2
	}

	// Very recent activity (potential automated)
	if time.Since(pattern.LastSeen) < 100*time.Millisecond {
		score += 0.2
	}

	return min(score, 1.0)
}

// IsBlocked checks if an IP is currently blocked
func (d *Detector) IsBlocked(ctx context.Context, sourceIP string) (bool, error) {
	// Check Redis first (distributed blocking)
	blockKey := fmt.Sprintf("threat:blocked:ip:%s", sourceIP)
	blockedUntil, err := d.config.Redis.Get(ctx, blockKey).Int64()
	if err == nil {
		if time.Now().Unix() < blockedUntil {
			return true, nil
		}
		// Expired, remove key
		d.config.Redis.Del(ctx, blockKey)
	}

	// Check local cache
	d.ipMutex.RLock()
	defer d.ipMutex.RUnlock()

	counter, exists := d.ipRequestCounts[sourceIP]
	if !exists {
		return false, nil
	}

	if counter.BlockedUntil != nil && time.Now().Before(*counter.BlockedUntil) {
		return true, nil
	}

	return false, nil
}

// GetThreatStats returns threat detection statistics
func (d *Detector) GetThreatStats() map[string]interface{} {
	return map[string]interface{}{
		"total_threats":        d.totalThreats.Load(),
		"ddos_detections":      d.ddosDetections.Load(),
		"anomaly_detections":   d.anomalyDetections.Load(),
		"behavioral_detections": d.behavioralDetections.Load(),
	}
}

// Helper functions

type Stats struct {
	Count        int64
	AvgLatency   float64
	StdDevLatency float64
	ErrorCount   int64
}

func (d *Detector) getStats(ctx context.Context, key string) (*Stats, error) {
	// Get from Redis
	data, err := d.config.Redis.HGetAll(ctx, key).Result()
	if err != nil {
		return nil, err
	}

	if len(data) == 0 {
		return &Stats{}, nil
	}

	// Parse stats (simplified)
	stats := &Stats{}
	// In production, use proper deserialization
	return stats, nil
}

func (d *Detector) updateStats(ctx context.Context, key string, requestTime time.Duration, isError bool) {
	// Update in Redis (simplified)
	// In production, use proper statistical tracking
}

func (d *Detector) cleanupExpiredCounters() {
	ticker := time.NewTicker(1 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		d.ipMutex.Lock()
		now := time.Now()
		for ip, counter := range d.ipRequestCounts {
			if counter.BlockedUntil != nil && now.After(*counter.BlockedUntil) {
				counter.BlockedUntil = nil
			}
			if now.Sub(counter.WindowStart) > d.config.DDosWindow*2 {
				delete(d.ipRequestCounts, ip)
			}
		}
		d.ipMutex.Unlock()

		d.userMutex.Lock()
		for userID, pattern := range d.userPatterns {
			if now.Sub(pattern.LastSeen) > d.config.BehaviorWindow*2 {
				delete(d.userPatterns, userID)
			}
		}
		d.userMutex.Unlock()
	}
}

func min(a, b float64) float64 {
	if a < b {
		return a
	}
	return b
}
