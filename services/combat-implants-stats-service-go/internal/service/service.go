package service

import (
	"context"
	"fmt"
	"math"
	"sort"
	"sync"
	"sync/atomic"
	"time"

	"combat-implants-stats-service-go/internal/repository"
)

// PERFORMANCE: Memory pooling for hot path objects (Level 2 optimization)
// Reduces GC pressure and allocations in high-throughput combat operations
var (
	implantStatsPool = sync.Pool{
		New: func() interface{} {
			return &repository.ImplantStats{}
		},
	}

	usageRequestPool = sync.Pool{
		New: func() interface{} {
			return &ImplantUsageRequest{}
		},
	}

	efficiencyResultPool = sync.Pool{
		New: func() interface{} {
			return make(map[string]interface{})
		},
	}
)

// Service handles business logic for combat implants stats
// OPTIMIZATION: Enterprise-grade service with MMOFPS optimizations
type Service struct {
	repo      *repository.Repository
	metrics   *ServiceMetrics
}

// ServiceMetrics provides atomic performance counters
//go:align 64
type ServiceMetrics struct {
	totalRequests   int64 // Atomic counter for total requests
	successfulOps   int64 // Atomic counter for successful operations
	failedOps       int64 // Atomic counter for failed operations
	avgResponseTime int64 // Atomic nanoseconds for average response time
	cacheHits       int64 // Atomic counter for cache hits
	cacheMisses     int64 // Atomic counter for cache misses
}

// NewService creates a new service instance with MMOFPS optimizations
func NewService(repo *repository.Repository) *Service {
	return &Service{
		repo: repo,
		metrics: &ServiceMetrics{},
	}
}

// ImplantUsageRequest represents a request to record implant usage
type ImplantUsageRequest struct {
	ImplantID   string  `json:"implant_id"`
	PlayerID    string  `json:"player_id"`
	Success     bool    `json:"success"`
	Duration    float64 `json:"duration"` // in seconds
}

// RecordImplantUsage records implant usage and updates statistics
func (s *Service) RecordImplantUsage(ctx context.Context, req *ImplantUsageRequest) error {
	startTime := time.Now()

	// PERFORMANCE: Increment total requests counter
	atomic.AddInt64(&s.metrics.totalRequests, 1)

	// PERFORMANCE: Context timeout for MMOFPS real-time requirements (<50ms P99)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// PERFORMANCE: Get object from pool to reduce allocations
	stats := implantStatsPool.Get().(*repository.ImplantStats)
	defer implantStatsPool.Put(stats)

	// Get current stats
	stats, err := s.repo.GetImplantStats(ctx, req.ImplantID)
	if err != nil {
		// If no stats exist, create new ones
		stats = &repository.ImplantStats{
			ImplantID:   req.ImplantID,
			PlayerID:    req.PlayerID,
			UsageCount:  0,
			SuccessRate: 0,
			AvgDuration: 0,
			LastUsed:    time.Now(),
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		}
	}

	// Update statistics
	stats.UsageCount++
	if req.Success {
		// Calculate new success rate using rolling average
		stats.SuccessRate = (stats.SuccessRate*float64(stats.UsageCount-1) + 1) / float64(stats.UsageCount)
	} else {
		// Failed usage
		stats.SuccessRate = (stats.SuccessRate * float64(stats.UsageCount-1)) / float64(stats.UsageCount)
	}

	// Update average duration
	if stats.UsageCount == 1 {
		stats.AvgDuration = req.Duration
	} else {
		stats.AvgDuration = (stats.AvgDuration*float64(stats.UsageCount-1) + req.Duration) / float64(stats.UsageCount)
	}

	stats.LastUsed = time.Now()
	stats.UpdatedAt = time.Now()

	err = s.repo.UpdateImplantStats(ctx, stats)
	if err != nil {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		return err
	}

	// PERFORMANCE: Record success and update response time
	atomic.AddInt64(&s.metrics.successfulOps, 1)
	responseTime := time.Since(startTime).Nanoseconds()
	s.updateAverageResponseTime(responseTime)

	return nil
}

// updateAverageResponseTime atomically updates the average response time
func (s *Service) updateAverageResponseTime(responseTime int64) {
	currentAvg := atomic.LoadInt64(&s.metrics.avgResponseTime)
	if currentAvg == 0 {
		atomic.StoreInt64(&s.metrics.avgResponseTime, responseTime)
	} else {
		// Exponential moving average: 0.1 * new + 0.9 * old
		newAvg := (responseTime + 9*currentAvg) / 10
		atomic.StoreInt64(&s.metrics.avgResponseTime, newAvg)
	}
}

// GetMetrics returns current service metrics
func (s *Service) GetMetrics() ServiceMetrics {
	return ServiceMetrics{
		totalRequests:   atomic.LoadInt64(&s.metrics.totalRequests),
		successfulOps:   atomic.LoadInt64(&s.metrics.successfulOps),
		failedOps:       atomic.LoadInt64(&s.metrics.failedOps),
		avgResponseTime: atomic.LoadInt64(&s.metrics.avgResponseTime),
		cacheHits:       atomic.LoadInt64(&s.metrics.cacheHits),
		cacheMisses:     atomic.LoadInt64(&s.metrics.cacheMisses),
	}
}

// GetImplantPerformance retrieves performance statistics for an implant
func (s *Service) GetImplantPerformance(ctx context.Context, implantID string) (*repository.ImplantStats, error) {
	startTime := time.Now()
	atomic.AddInt64(&s.metrics.totalRequests, 1)

	// PERFORMANCE: Context timeout for analytics queries
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	stats, err := s.repo.GetImplantStats(ctx, implantID)
	if err != nil {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		return nil, err
	}

	atomic.AddInt64(&s.metrics.successfulOps, 1)
	responseTime := time.Since(startTime).Nanoseconds()
	s.updateAverageResponseTime(responseTime)

	return stats, nil
}

// GetPlayerImplantAnalytics retrieves analytics for player's implant usage
func (s *Service) GetPlayerImplantAnalytics(ctx context.Context, playerID string) ([]*repository.ImplantStats, error) {
	return s.repo.GetPlayerImplantAnalytics(ctx, playerID)
}

// CalculateImplantEfficiency calculates efficiency metrics for an implant
func (s *Service) CalculateImplantEfficiency(ctx context.Context, implantID string) (map[string]interface{}, error) {
	startTime := time.Now()
	atomic.AddInt64(&s.metrics.totalRequests, 1)

	// PERFORMANCE: Context timeout for analytics calculations
	ctx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	stats, err := s.repo.GetImplantStats(ctx, implantID)
	if err != nil {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		return nil, fmt.Errorf("failed to get implant stats: %w", err)
	}

	// PERFORMANCE: Get map from pool to reduce allocations
	efficiency := efficiencyResultPool.Get().(map[string]interface{})
	defer func() {
		// Clear map before returning to pool
		for k := range efficiency {
			delete(efficiency, k)
		}
		efficiencyResultPool.Put(efficiency)
	}()

	// Fill efficiency map
	efficiency["implant_id"] = stats.ImplantID
	efficiency["total_usage"] = stats.UsageCount
	efficiency["success_rate"] = stats.SuccessRate
	efficiency["average_duration"] = stats.AvgDuration
	efficiency["efficiency_score"] = stats.SuccessRate * (1.0 / (stats.AvgDuration + 1)) // Higher score = better efficiency
	efficiency["last_used"] = stats.LastUsed

	atomic.AddInt64(&s.metrics.successfulOps, 1)
	responseTime := time.Since(startTime).Nanoseconds()
	s.updateAverageResponseTime(responseTime)

	// Create a copy for return (don't return pooled object)
	result := make(map[string]interface{})
	for k, v := range efficiency {
		result[k] = v
	}

	return result, nil
}

// AggregatedStats and TypeStats are defined in repository package

// GetAggregatedStats retrieves aggregated statistics across all implants
func (s *Service) GetAggregatedStats(ctx context.Context) (*repository.AggregatedStats, error) {
	// Use repository method to get real aggregated data from database
	aggregatedData, err := s.repo.GetAggregatedStats(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get aggregated stats from repository: %w", err)
	}

	return aggregatedData, nil
}

// UsageTrendsData and TrendPoint are defined in repository package

// GetUsageTrends retrieves usage trends data
func (s *Service) GetUsageTrends(ctx context.Context, trendType, period string) (*repository.UsageTrendsData, error) {
	// Use repository method to get real trends data from database
	trendsData, err := s.repo.GetUsageTrends(ctx, trendType, period)
	if err != nil {
		return nil, fmt.Errorf("failed to get usage trends from repository: %w", err)
	}

	return trendsData, nil
}

// AnomaliesData and AnomalyData are defined in repository package

// DetectAnomalies performs anomaly detection on implant usage
func (s *Service) DetectAnomalies(ctx context.Context, playerID [16]byte, implantType string, sensitivity float64) (*repository.AnomaliesData, error) {
	// Get comprehensive stats for statistical analysis
	allStats, err := s.repo.GetAllImplantStats(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get implant stats for anomaly detection: %w", err)
	}

	// Calculate statistical baselines
	statsBaseline := s.calculateStatisticalBaseline(allStats)

	// Apply multiple anomaly detection algorithms
	anomalies := s.detectAnomaliesWithAlgorithms(allStats, statsBaseline, sensitivity)

	// Calculate overall anomaly score
	anomalyScore := s.calculateAnomalyScore(anomalies, len(allStats))

	return &repository.AnomaliesData{
		DetectedAnomalies: anomalies,
		AnomalyScore:      anomalyScore,
		TotalScanned:      len(allStats),
	}, nil
}

// StatisticalBaseline represents statistical properties of the implant population
type StatisticalBaseline struct {
	MeanSuccessRate     float64
	StdDevSuccessRate   float64
	MeanUsageCount      float64
	StdDevUsageCount    float64
	MeanDuration        float64
	StdDevDuration      float64
	SuccessRateQuartiles [4]float64 // Q1, median, Q3, max
}

// calculateStatisticalBaseline calculates statistical properties for anomaly detection
func (s *Service) calculateStatisticalBaseline(stats []*repository.ImplantStats) StatisticalBaseline {
	if len(stats) == 0 {
		return StatisticalBaseline{}
	}

	// Extract values for statistical calculations
	var successRates []float64
	var usageCounts []float64
	var durations []float64

	for _, stat := range stats {
		successRates = append(successRates, stat.SuccessRate)
		usageCounts = append(usageCounts, float64(stat.UsageCount))
		durations = append(durations, stat.AvgDuration)
	}

	baseline := StatisticalBaseline{
		MeanSuccessRate:   s.calculateMean(successRates),
		StdDevSuccessRate: s.calculateStdDev(successRates),
		MeanUsageCount:    s.calculateMean(usageCounts),
		StdDevUsageCount:  s.calculateStdDev(usageCounts),
		MeanDuration:      s.calculateMean(durations),
		StdDevDuration:    s.calculateStdDev(durations),
	}

	// Calculate quartiles for success rate
	baseline.SuccessRateQuartiles = s.calculateQuartiles(successRates)

	return baseline
}

// detectAnomaliesWithAlgorithms applies multiple anomaly detection algorithms
func (s *Service) detectAnomaliesWithAlgorithms(stats []*repository.ImplantStats, baseline StatisticalBaseline, sensitivity float64) []repository.AnomalyData {
	var anomalies []repository.AnomalyData

	for _, stat := range stats {
		// Algorithm 1: Z-Score based outlier detection
		if anomaly := s.detectZScoreAnomaly(stat, baseline, sensitivity); anomaly != nil {
			anomalies = append(anomalies, *anomaly)
		}

		// Algorithm 2: Quartile-based outlier detection
		if anomaly := s.detectQuartileAnomaly(stat, baseline, sensitivity); anomaly != nil {
			anomalies = append(anomalies, *anomaly)
		}

		// Algorithm 3: Pattern-based anomaly detection
		if anomaly := s.detectPatternAnomaly(stat, baseline, sensitivity); anomaly != nil {
			anomalies = append(anomalies, *anomaly)
		}

		// Algorithm 4: Threshold-based detection for extreme values
		if anomaly := s.detectThresholdAnomaly(stat, baseline, sensitivity); anomaly != nil {
			anomalies = append(anomalies, *anomaly)
		}
	}

	return anomalies
}

// detectZScoreAnomaly detects anomalies using Z-Score method
func (s *Service) detectZScoreAnomaly(stat *repository.ImplantStats, baseline StatisticalBaseline, sensitivity float64) *repository.AnomalyData {
	zScoreThreshold := 3.0 * sensitivity // More sensitive = lower threshold

	// Z-Score for success rate
	if baseline.StdDevSuccessRate > 0 {
		zScore := (stat.SuccessRate - baseline.MeanSuccessRate) / baseline.StdDevSuccessRate
		if zScore > zScoreThreshold {
			return &repository.AnomalyData{
				ImplantID:         stat.ImplantID,
				AnomalyType:       "statistical_outlier_high_success",
				Description:       fmt.Sprintf("Success rate %.2f is %.1f standard deviations above mean", stat.SuccessRate, zScore),
				DetectedAt:        time.Now(),
				RecommendedAction: "investigate_performance",
				SeverityScore:     s.calculateSeverityScore(zScore, zScoreThreshold),
			}
		} else if zScore < -zScoreThreshold {
			return &repository.AnomalyData{
				ImplantID:         stat.ImplantID,
				AnomalyType:       "statistical_outlier_low_success",
				Description:       fmt.Sprintf("Success rate %.2f is %.1f standard deviations below mean", stat.SuccessRate, -zScore),
				DetectedAt:        time.Now(),
				RecommendedAction: "maintenance_required",
				SeverityScore:     s.calculateSeverityScore(-zScore, zScoreThreshold),
			}
		}
	}

	return nil
}

// detectQuartileAnomaly detects anomalies using quartile method (IQR)
func (s *Service) detectQuartileAnomaly(stat *repository.ImplantStats, baseline StatisticalBaseline, sensitivity float64) *repository.AnomalyData {
	iqr := baseline.SuccessRateQuartiles[2] - baseline.SuccessRateQuartiles[0] // Q3 - Q1
	upperFence := baseline.SuccessRateQuartiles[2] + (iqr * 1.5 * sensitivity)
	lowerFence := baseline.SuccessRateQuartiles[0] - (iqr * 1.5 * sensitivity)

	if stat.SuccessRate > upperFence {
		return &repository.AnomalyData{
			ImplantID:         stat.ImplantID,
			AnomalyType:       "quartile_outlier_high_success",
			Description:       fmt.Sprintf("Success rate %.2f exceeds upper quartile fence (%.2f)", stat.SuccessRate, upperFence),
			DetectedAt:        time.Now(),
			RecommendedAction: "performance_review",
			SeverityScore:     0.7,
		}
	} else if stat.SuccessRate < lowerFence {
		return &repository.AnomalyData{
			ImplantID:         stat.ImplantID,
			AnomalyType:       "quartile_outlier_low_success",
			Description:       fmt.Sprintf("Success rate %.2f below lower quartile fence (%.2f)", stat.SuccessRate, lowerFence),
			DetectedAt:        time.Now(),
			RecommendedAction: "failure_analysis",
			SeverityScore:     0.6,
		}
	}

	return nil
}

// detectPatternAnomaly detects pattern-based anomalies
func (s *Service) detectPatternAnomaly(stat *repository.ImplantStats, baseline StatisticalBaseline, sensitivity float64) *repository.AnomalyData {
	// High usage with low success rate (inefficient implant)
	if stat.UsageCount > int64(baseline.MeanUsageCount+baseline.StdDevUsageCount*sensitivity) &&
		stat.SuccessRate < baseline.MeanSuccessRate-baseline.StdDevSuccessRate*sensitivity {

		return &repository.AnomalyData{
			ImplantID:         stat.ImplantID,
			AnomalyType:       "inefficient_usage_pattern",
			Description:       "High usage count with abnormally low success rate indicates inefficiency",
			DetectedAt:        time.Now(),
			RecommendedAction: "optimize_usage",
			SeverityScore:     0.5,
		}
	}

	// Perfect success rate with minimal usage (possible data manipulation)
	if stat.SuccessRate > 0.99 && stat.UsageCount < 5 {
		return &repository.AnomalyData{
			ImplantID:         stat.ImplantID,
			AnomalyType:       "suspicious_perfection",
			Description:       "Perfect success rate with minimal usage may indicate data issues",
			DetectedAt:        time.Now(),
			RecommendedAction: "data_validation",
			SeverityScore:     0.8,
		}
	}

	return nil
}

// detectThresholdAnomaly detects extreme threshold violations
func (s *Service) detectThresholdAnomaly(stat *repository.ImplantStats, baseline StatisticalBaseline, sensitivity float64) *repository.AnomalyData {
	// Extremely high usage (potential abuse/health risk)
	abuseThreshold := int64(baseline.MeanUsageCount + baseline.StdDevUsageCount*3*sensitivity)
	if stat.UsageCount > abuseThreshold {
		return &repository.AnomalyData{
			ImplantID:         stat.ImplantID,
			AnomalyType:       "excessive_usage",
			Description:       fmt.Sprintf("Usage count %d significantly exceeds normal threshold", stat.UsageCount),
			DetectedAt:        time.Now(),
			RecommendedAction: "health_monitoring",
			SeverityScore:     0.9,
		}
	}

	// Complete failure rate
	if stat.SuccessRate < 0.01 && stat.UsageCount > 10 {
		return &repository.AnomalyData{
			ImplantID:         stat.ImplantID,
			AnomalyType:       "complete_failure",
			Description:       "Implant has 0% success rate with significant usage",
			DetectedAt:        time.Now(),
			RecommendedAction: "immediate_replacement",
			SeverityScore:     1.0,
		}
	}

	return nil
}

// calculateSeverityScore calculates anomaly severity score
func (s *Service) calculateSeverityScore(deviation, threshold float64) float64 {
	score := deviation / threshold
	if score > 1.0 {
		score = 1.0
	}
	return score
}

// calculateAnomalyScore calculates overall anomaly score
func (s *Service) calculateAnomalyScore(anomalies []repository.AnomalyData, totalScanned int) float64 {
	if totalScanned == 0 {
		return 0.0
	}

	totalSeverity := 0.0
	for _, anomaly := range anomalies {
		totalSeverity += anomaly.SeverityScore
	}

	return totalSeverity / float64(totalScanned)
}

// Helper statistical functions
func (s *Service) calculateMean(values []float64) float64 {
	if len(values) == 0 {
		return 0.0
	}
	sum := 0.0
	for _, v := range values {
		sum += v
	}
	return sum / float64(len(values))
}

func (s *Service) calculateStdDev(values []float64) float64 {
	if len(values) < 2 {
		return 0.0
	}

	mean := s.calculateMean(values)
	sumSquares := 0.0
	for _, v := range values {
		diff := v - mean
		sumSquares += diff * diff
	}

	return math.Sqrt(sumSquares / float64(len(values)-1))
}

func (s *Service) calculateQuartiles(values []float64) [4]float64 {
	if len(values) == 0 {
		return [4]float64{0, 0, 0, 0}
	}

	sorted := make([]float64, len(values))
	copy(sorted, values)
	sort.Float64s(sorted)

	var quartiles [4]float64
	n := len(sorted)

	quartiles[0] = sorted[n/4]           // Q1
	quartiles[1] = sorted[n/2]           // Median
	quartiles[2] = sorted[3*n/4]         // Q3
	quartiles[3] = sorted[n-1]           // Max

	return quartiles
}