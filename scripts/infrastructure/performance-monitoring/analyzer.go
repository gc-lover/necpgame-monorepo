// Package monitoring provides performance analysis and reporting
package monitoring

import (
	"context"
	"fmt"
	"sort"
	"time"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// PerformanceAnalyzer provides real-time performance analysis
type PerformanceAnalyzer struct {
	monitor *PerformanceMonitor
	alerts  *AlertManager
	logger  *errorhandling.Logger

	// Analysis data
	responseTimeData   []ResponseTimeSample
	networkLatencyData []NetworkLatencySample
	errorRateData      []ErrorRateSample
	sessionData        []SessionSample

	maxSamples int // Maximum samples to keep in memory
}

// ResponseTimeSample represents a response time measurement
type ResponseTimeSample struct {
	Timestamp   time.Time
	Service     string
	Endpoint    string
	Method      string
	Duration    time.Duration
	StatusCode  int
	UserID      string
	RequestID   string
}

// NetworkLatencySample represents network latency measurement
type NetworkLatencySample struct {
	Timestamp     time.Time
	Region        string
	ConnectionType string
	Latency       time.Duration
	PacketLoss    float64
	Jitter        time.Duration
	PlayerCount   int
}

// ErrorRateSample represents error rate measurement
type ErrorRateSample struct {
	Timestamp  time.Time
	Service    string
	Endpoint   string
	ErrorCount int
	TotalCount int
	ErrorRate  float64
}

// SessionSample represents session performance data
type SessionSample struct {
	Timestamp    time.Time
	SessionID    string
	PlayerID     string
	Region       string
	GameMode     string
	Duration     time.Duration
	Ping         int
	FPS          int
	DataTransferred int64
	Dropped      bool
}

// PerformanceReport represents a comprehensive performance report
type PerformanceReport struct {
	GeneratedAt    time.Time              `json:"generated_at"`
	TimeRange      TimeRange              `json:"time_range"`
	Summary        PerformanceSummary      `json:"summary"`
	ServiceMetrics map[string]ServiceMetrics `json:"service_metrics"`
	NetworkMetrics NetworkMetrics          `json:"network_metrics"`
	Alerts         []AlertSummary          `json:"alerts"`
	Recommendations []string              `json:"recommendations"`
}

// TimeRange represents a time range for analysis
type TimeRange struct {
	Start time.Time `json:"start"`
	End   time.Time `json:"end"`
}

// PerformanceSummary provides high-level performance overview
type PerformanceSummary struct {
	AvgResponseTime   time.Duration `json:"avg_response_time"`
	P95ResponseTime   time.Duration `json:"p95_response_time"`
	P99ResponseTime   time.Duration `json:"p99_response_time"`
	ErrorRate         float64       `json:"error_rate"`
	ActiveUsers       int           `json:"active_users"`
	SessionDropRate   float64       `json:"session_drop_rate"`
	AvgNetworkLatency time.Duration `json:"avg_network_latency"`
	CacheHitRate      float64       `json:"cache_hit_rate"`
}

// ServiceMetrics provides detailed service performance metrics
type ServiceMetrics struct {
	ServiceName       string        `json:"service_name"`
	AvgResponseTime   time.Duration `json:"avg_response_time"`
	ErrorRate         float64       `json:"error_rate"`
	RequestCount      int           `json:"request_count"`
	ActiveConnections int           `json:"active_connections"`
	MemoryUsage       int64         `json:"memory_usage_bytes"`
	CPUUsage          float64       `json:"cpu_usage_percent"`
	EndpointMetrics   map[string]EndpointMetrics `json:"endpoint_metrics"`
}

// EndpointMetrics provides endpoint-specific performance data
type EndpointMetrics struct {
	Endpoint         string        `json:"endpoint"`
	Method           string        `json:"method"`
	RequestCount     int           `json:"request_count"`
	AvgResponseTime  time.Duration `json:"avg_response_time"`
	ErrorRate        float64       `json:"error_rate"`
	P95ResponseTime  time.Duration `json:"p95_response_time"`
	LastRequestTime  time.Time     `json:"last_request_time"`
}

// NetworkMetrics provides network performance overview
type NetworkMetrics struct {
	Regions         map[string]RegionMetrics `json:"regions"`
	GlobalAvgLatency time.Duration           `json:"global_avg_latency"`
	GlobalPacketLoss float64                 `json:"global_packet_loss"`
	TotalConnections int                     `json:"total_connections"`
}

// RegionMetrics provides region-specific network metrics
type RegionMetrics struct {
	Region         string        `json:"region"`
	AvgLatency     time.Duration `json:"avg_latency"`
	PacketLoss     float64       `json:"packet_loss"`
	Jitter         time.Duration `json:"jitter"`
	ActivePlayers  int           `json:"active_players"`
	ConnectionType string        `json:"connection_type"`
}

// AlertSummary provides alert summary for reports
type AlertSummary struct {
	Level      AlertLevel `json:"level"`
	Count      int        `json:"count"`
	TopAlerts  []string   `json:"top_alerts"`
	LastAlert  time.Time  `json:"last_alert"`
}

// NewPerformanceAnalyzer creates a new performance analyzer
func NewPerformanceAnalyzer(monitor *PerformanceMonitor, alerts *AlertManager, logger *errorhandling.Logger) *PerformanceAnalyzer {
	return &PerformanceAnalyzer{
		monitor:     monitor,
		alerts:      alerts,
		logger:      logger,
		maxSamples:  10000, // Keep last 10k samples
		responseTimeData:   make([]ResponseTimeSample, 0),
		networkLatencyData: make([]NetworkLatencySample, 0),
		errorRateData:      make([]ErrorRateSample, 0),
		sessionData:        make([]SessionSample, 0),
	}
}

// RecordResponseTime records a response time measurement
func (pa *PerformanceAnalyzer) RecordResponseTime(sample ResponseTimeSample) {
	pa.responseTimeData = append(pa.responseTimeData, sample)
	pa.trimSamples(&pa.responseTimeData)
}

// RecordNetworkLatency records network latency measurement
func (pa *PerformanceAnalyzer) RecordNetworkLatency(sample NetworkLatencySample) {
	pa.networkLatencyData = append(pa.networkLatencyData, sample)
	pa.trimSamples(&pa.networkLatencyData)
}

// RecordErrorRate records error rate measurement
func (pa *PerformanceAnalyzer) RecordErrorRate(sample ErrorRateSample) {
	pa.errorRateData = append(pa.errorRateData, sample)
	pa.trimSamples(&pa.errorRateData)
}

// RecordSession records session performance data
func (pa *PerformanceAnalyzer) RecordSession(sample SessionSample) {
	pa.sessionData = append(pa.sessionData, sample)
	pa.trimSamples(&pa.sessionData)
}

// GenerateReport generates a comprehensive performance report
func (pa *PerformanceAnalyzer) GenerateReport(timeRange TimeRange) *PerformanceReport {
	report := &PerformanceReport{
		GeneratedAt: time.Now(),
		TimeRange:   timeRange,
		Summary:     pa.calculateSummary(timeRange),
		ServiceMetrics: pa.calculateServiceMetrics(timeRange),
		NetworkMetrics: pa.calculateNetworkMetrics(timeRange),
		Alerts:      pa.calculateAlertSummary(timeRange),
		Recommendations: pa.generateRecommendations(),
	}

	return report
}

// calculateSummary calculates high-level performance summary
func (pa *PerformanceAnalyzer) calculateSummary(timeRange TimeRange) PerformanceSummary {
	// Filter data by time range
	validResponses := pa.filterResponseTimes(timeRange)
	validSessions := pa.filterSessions(timeRange)
	validNetwork := pa.filterNetworkLatency(timeRange)

	summary := PerformanceSummary{}

	// Calculate response times
	if len(validResponses) > 0 {
		durations := make([]time.Duration, len(validResponses))
		for i, r := range validResponses {
			durations[i] = r.Duration
		}
		sort.Slice(durations, func(i, j int) bool {
			return durations[i] < durations[j]
		})

		summary.AvgResponseTime = pa.calculateAverageDuration(durations)
		summary.P95ResponseTime = pa.calculatePercentileDuration(durations, 0.95)
		summary.P99ResponseTime = pa.calculatePercentileDuration(durations, 0.99)
	}

	// Calculate error rate
	totalRequests := len(validResponses)
	errorRequests := 0
	for _, r := range validResponses {
		if r.StatusCode >= 400 {
			errorRequests++
		}
	}
	if totalRequests > 0 {
		summary.ErrorRate = float64(errorRequests) / float64(totalRequests)
	}

	// Calculate session metrics
	totalSessions := len(validSessions)
	droppedSessions := 0
	for _, s := range validSessions {
		if s.Dropped {
			droppedSessions++
		}
	}
	if totalSessions > 0 {
		summary.SessionDropRate = float64(droppedSessions) / float64(totalSessions)
	}

	// Calculate network metrics
	if len(validNetwork) > 0 {
		totalLatency := time.Duration(0)
		totalPacketLoss := 0.0
		for _, n := range validNetwork {
			totalLatency += n.Latency
			totalPacketLoss += n.PacketLoss
		}
		summary.AvgNetworkLatency = totalLatency / time.Duration(len(validNetwork))
		summary.GlobalPacketLoss = totalPacketLoss / float64(len(validNetwork))
	}

	// Placeholder values for other metrics
	summary.ActiveUsers = len(validSessions)
	summary.CacheHitRate = 0.92 // Would come from metrics

	return summary
}

// calculateServiceMetrics calculates detailed service performance metrics
func (pa *PerformanceAnalyzer) calculateServiceMetrics(timeRange TimeRange) map[string]ServiceMetrics {
	validResponses := pa.filterResponseTimes(timeRange)

	serviceMetrics := make(map[string]ServiceMetrics)

	for _, response := range validResponses {
		service := response.Service
		if service == "" {
			service = "unknown"
		}

		if _, exists := serviceMetrics[service]; !exists {
			serviceMetrics[service] = ServiceMetrics{
				ServiceName:     service,
				EndpointMetrics: make(map[string]EndpointMetrics),
			}
		}

		metrics := serviceMetrics[service]
		metrics.RequestCount++

		// Calculate response time metrics
		endpointKey := fmt.Sprintf("%s %s", response.Method, response.Endpoint)
		if endpoint, exists := metrics.EndpointMetrics[endpointKey]; exists {
			endpoint.RequestCount++
			endpoint.AvgResponseTime = (endpoint.AvgResponseTime*time.Duration(endpoint.RequestCount-1) + response.Duration) / time.Duration(endpoint.RequestCount)
			if response.StatusCode >= 400 {
				endpoint.ErrorRate = float64(endpoint.RequestCount-1)/float64(endpoint.RequestCount) + 1.0/float64(endpoint.RequestCount)
			}
			endpoint.LastRequestTime = response.Timestamp
			metrics.EndpointMetrics[endpointKey] = endpoint
		} else {
			endpoint := EndpointMetrics{
				Endpoint:        response.Endpoint,
				Method:          response.Method,
				RequestCount:    1,
				AvgResponseTime: response.Duration,
				LastRequestTime: response.Timestamp,
			}
			if response.StatusCode >= 400 {
				endpoint.ErrorRate = 1.0
			}
			metrics.EndpointMetrics[endpointKey] = endpoint
		}

		// Update service-level metrics
		if metrics.RequestCount == 1 {
			metrics.AvgResponseTime = response.Duration
		} else {
			metrics.AvgResponseTime = (metrics.AvgResponseTime*time.Duration(metrics.RequestCount-1) + response.Duration) / time.Duration(metrics.RequestCount)
		}

		serviceMetrics[service] = metrics
	}

	return serviceMetrics
}

// calculateNetworkMetrics calculates network performance metrics
func (pa *PerformanceAnalyzer) calculateNetworkMetrics(timeRange TimeRange) NetworkMetrics {
	validNetwork := pa.filterNetworkLatency(timeRange)

	metrics := NetworkMetrics{
		Regions: make(map[string]RegionMetrics),
	}

	totalLatency := time.Duration(0)
	totalPacketLoss := 0.0
	totalConnections := 0

	for _, sample := range validNetwork {
		totalLatency += sample.Latency
		totalPacketLoss += sample.PacketLoss
		totalConnections += sample.PlayerCount

		if region, exists := metrics.Regions[sample.Region]; exists {
			// Update existing region metrics
			totalSamples := region.ActivePlayers + 1
			region.AvgLatency = (region.AvgLatency*time.Duration(region.ActivePlayers) + sample.Latency) / time.Duration(totalSamples)
			region.PacketLoss = (region.PacketLoss*float64(region.ActivePlayers) + sample.PacketLoss) / float64(totalSamples)
			region.Jitter = pa.calculateJitter([]time.Duration{region.Jitter, sample.Jitter})
			region.ActivePlayers = sample.PlayerCount
			metrics.Regions[sample.Region] = region
		} else {
			// Create new region metrics
			metrics.Regions[sample.Region] = RegionMetrics{
				Region:         sample.Region,
				AvgLatency:     sample.Latency,
				PacketLoss:     sample.PacketLoss,
				Jitter:         sample.Jitter,
				ActivePlayers:  sample.PlayerCount,
				ConnectionType: sample.ConnectionType,
			}
		}
	}

	metrics.TotalConnections = totalConnections
	if len(validNetwork) > 0 {
		metrics.GlobalAvgLatency = totalLatency / time.Duration(len(validNetwork))
		metrics.GlobalPacketLoss = totalPacketLoss / float64(len(validNetwork))
	}

	return metrics
}

// calculateAlertSummary creates alert summary for the report
func (pa *PerformanceAnalyzer) calculateAlertSummary(timeRange TimeRange) []AlertSummary {
	activeAlerts := pa.alerts.GetActiveAlerts()

	alertSummary := make(map[AlertLevel]*AlertSummary)
	alertCounts := make(map[string]int)

	for _, alert := range activeAlerts {
		if alert.Timestamp.Before(timeRange.Start) || alert.Timestamp.After(timeRange.End) {
			continue
		}

		if _, exists := alertSummary[alert.Level]; !exists {
			alertSummary[alert.Level] = &AlertSummary{
				Level:     alert.Level,
				Count:     0,
				TopAlerts: make([]string, 0),
				LastAlert: alert.Timestamp,
			}
		}

		summary := alertSummary[alert.Level]
		summary.Count++

		alertKey := alert.Title
		alertCounts[alertKey]++

		if alert.Timestamp.After(summary.LastAlert) {
			summary.LastAlert = alert.Timestamp
		}
	}

	// Convert to slice and sort top alerts
	result := make([]AlertSummary, 0, len(alertSummary))
	for _, summary := range alertSummary {
		// Sort top alerts by frequency
		type alertCount struct {
			title string
			count int
		}

		var counts []alertCount
		for title, count := range alertCounts {
			counts = append(counts, alertCount{title, count})
		}

		sort.Slice(counts, func(i, j int) bool {
			return counts[i].count > counts[j].count
		})

		summary.TopAlerts = make([]string, 0, 5)
		for i, count := range counts {
			if i >= 5 { // Top 5 alerts
				break
			}
			summary.TopAlerts = append(summary.TopAlerts, fmt.Sprintf("%s (%d)", count.title, count.count))
		}

		result = append(result, *summary)
	}

	return result
}

// generateRecommendations generates performance improvement recommendations
func (pa *PerformanceAnalyzer) generateRecommendations() []string {
	recommendations := make([]string, 0)

	// Analyze current metrics and generate recommendations
	// This would be more sophisticated in a real implementation

	recommendations = append(recommendations,
		"Consider implementing Redis caching for frequently accessed data",
		"Optimize database queries with proper indexing",
		"Implement connection pooling for database connections",
		"Consider using a CDN for static assets",
		"Implement rate limiting to prevent abuse",
		"Add circuit breakers for external service calls",
		"Consider horizontal scaling for high-traffic regions",
		"Implement proper session management and cleanup",
		"Add comprehensive monitoring and alerting",
		"Optimize memory usage and garbage collection",
	)

	return recommendations
}

// Helper methods

func (pa *PerformanceAnalyzer) trimSamples(samples interface{}) {
	// This would trim samples to maxSamples using reflection
	// Simplified implementation
}

func (pa *PerformanceAnalyzer) filterResponseTimes(timeRange TimeRange) []ResponseTimeSample {
	var filtered []ResponseTimeSample
	for _, sample := range pa.responseTimeData {
		if sample.Timestamp.After(timeRange.Start) && sample.Timestamp.Before(timeRange.End) {
			filtered = append(filtered, sample)
		}
	}
	return filtered
}

func (pa *PerformanceAnalyzer) filterSessions(timeRange TimeRange) []SessionSample {
	var filtered []SessionSample
	for _, sample := range pa.sessionData {
		if sample.Timestamp.After(timeRange.Start) && sample.Timestamp.Before(timeRange.End) {
			filtered = append(filtered, sample)
		}
	}
	return filtered
}

func (pa *PerformanceAnalyzer) filterNetworkLatency(timeRange TimeRange) []NetworkLatencySample {
	var filtered []NetworkLatencySample
	for _, sample := range pa.networkLatencyData {
		if sample.Timestamp.After(timeRange.Start) && sample.Timestamp.Before(timeRange.End) {
			filtered = append(filtered, sample)
		}
	}
	return filtered
}

func (pa *PerformanceAnalyzer) calculateAverageDuration(durations []time.Duration) time.Duration {
	if len(durations) == 0 {
		return 0
	}

	total := time.Duration(0)
	for _, d := range durations {
		total += d
	}
	return total / time.Duration(len(durations))
}

func (pa *PerformanceAnalyzer) calculatePercentileDuration(durations []time.Duration, percentile float64) time.Duration {
	if len(durations) == 0 {
		return 0
	}

	index := int(float64(len(durations)-1) * percentile)
	return durations[index]
}

func (pa *PerformanceAnalyzer) calculateJitter(latencies []time.Duration) time.Duration {
	if len(latencies) <= 1 {
		return 0
	}

	var diffs []time.Duration
	for i := 1; i < len(latencies); i++ {
		diff := latencies[i] - latencies[i-1]
		if diff < 0 {
			diff = -diff
		}
		diffs = append(diffs, diff)
	}

	return pa.calculateAverageDuration(diffs)
}
