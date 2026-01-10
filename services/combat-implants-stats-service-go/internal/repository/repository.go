package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for combat implants stats
type Repository struct {
	db *pgxpool.Pool
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool) *Repository {
	return &Repository{db: db}
}

// ImplantStats represents implant usage statistics
type ImplantStats struct {
	ImplantID    string    `json:"implant_id"`
	PlayerID     string    `json:"player_id"`
	UsageCount   int64     `json:"usage_count"`
	SuccessRate  float64   `json:"success_rate"`
	AvgDuration  float64   `json:"avg_duration"`
	LastUsed     time.Time `json:"last_used"`
	CreatedAt    time.Time `json:"created_at"`
	UpdatedAt    time.Time `json:"updated_at"`
}

// GetImplantStats retrieves statistics for a specific implant
func (r *Repository) GetImplantStats(ctx context.Context, implantID string) (*ImplantStats, error) {
	query := `
		SELECT implant_id, player_id, usage_count, success_rate, avg_duration, last_used, created_at, updated_at
		FROM combat.implant_stats
		WHERE implant_id = $1
	`

	var stats ImplantStats
	err := r.db.QueryRow(ctx, query, implantID).Scan(
		&stats.ImplantID, &stats.PlayerID, &stats.UsageCount,
		&stats.SuccessRate, &stats.AvgDuration, &stats.LastUsed,
		&stats.CreatedAt, &stats.UpdatedAt,
	)
	if err != nil {
		return nil, err
	}

	return &stats, nil
}

// UpdateImplantStats updates implant usage statistics
func (r *Repository) UpdateImplantStats(ctx context.Context, stats *ImplantStats) error {
	query := `
		INSERT INTO combat.implant_stats (
			implant_id, player_id, usage_count, success_rate, avg_duration, last_used, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (implant_id, player_id)
		DO UPDATE SET
			usage_count = EXCLUDED.usage_count,
			success_rate = EXCLUDED.success_rate,
			avg_duration = EXCLUDED.avg_duration,
			last_used = EXCLUDED.last_used,
			updated_at = EXCLUDED.updated_at
	`

	_, err := r.db.Exec(ctx, query,
		stats.ImplantID, stats.PlayerID, stats.UsageCount,
		stats.SuccessRate, stats.AvgDuration, stats.LastUsed, time.Now(),
	)
	return err
}

// GetPlayerImplantAnalytics retrieves analytics for player's implant usage
func (r *Repository) GetPlayerImplantAnalytics(ctx context.Context, playerID string) ([]*ImplantStats, error) {
	query := `
		SELECT implant_id, player_id, usage_count, success_rate, avg_duration, last_used, created_at, updated_at
		FROM combat.implant_stats
		WHERE player_id = $1
		ORDER BY last_used DESC
	`

	rows, err := r.db.Query(ctx, query, playerID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var stats []*ImplantStats
	for rows.Next() {
		var stat ImplantStats
		err := rows.Scan(
			&stat.ImplantID, &stat.PlayerID, &stat.UsageCount,
			&stat.SuccessRate, &stat.AvgDuration, &stat.LastUsed,
			&stat.CreatedAt, &stat.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		stats = append(stats, &stat)
	}

	return stats, nil
}

// GetAllImplantStats retrieves all implant statistics for anomaly detection
func (r *Repository) GetAllImplantStats(ctx context.Context) ([]*ImplantStats, error) {
	query := `
		SELECT implant_id, player_id, usage_count, success_rate, avg_duration, last_used, created_at, updated_at
		FROM combat.implant_stats
		ORDER BY last_used DESC
		LIMIT 1000  -- Limit for performance, focus on recent activity
	`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var stats []*ImplantStats
	for rows.Next() {
		var stat ImplantStats
		err := rows.Scan(
			&stat.ImplantID, &stat.PlayerID, &stat.UsageCount,
			&stat.SuccessRate, &stat.AvgDuration, &stat.LastUsed,
			&stat.CreatedAt, &stat.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		stats = append(stats, &stat)
	}

	return stats, nil
}

// GetAggregatedStats retrieves aggregated statistics across all implants
func (r *Repository) GetAggregatedStats(ctx context.Context) (*AggregatedStats, error) {
	// First, get total counts
	totalStats := `
		SELECT
			COUNT(*) as total_implants,
			COALESCE(SUM(usage_count), 0) as total_requests
		FROM combat.implant_stats
	`

	var totalImplants int
	var totalRequests int64
	err := r.db.QueryRow(ctx, totalStats).Scan(&totalImplants, &totalRequests)
	if err != nil {
		return nil, fmt.Errorf("failed to get total stats: %w", err)
	}

	// Get stats by type (grouping by implant type extracted from implant_id)
	typeStats := `
		SELECT
			CASE
				WHEN implant_id LIKE 'combat%' THEN 'combat'
				WHEN implant_id LIKE 'stealth%' THEN 'stealth'
				WHEN implant_id LIKE 'hacking%' THEN 'hacking'
				WHEN implant_id LIKE 'medical%' THEN 'medical'
				WHEN implant_id LIKE 'social%' THEN 'social'
				ELSE 'unknown'
			END as implant_type,
			COUNT(*) as count,
			AVG(success_rate) as avg_success_rate,
			SUM(usage_count) as total_usage
		FROM combat.implant_stats
		GROUP BY
			CASE
				WHEN implant_id LIKE 'combat%' THEN 'combat'
				WHEN implant_id LIKE 'stealth%' THEN 'stealth'
				WHEN implant_id LIKE 'hacking%' THEN 'hacking'
				WHEN implant_id LIKE 'medical%' THEN 'medical'
				WHEN implant_id LIKE 'social%' THEN 'social'
				ELSE 'unknown'
			END
		ORDER BY count DESC
	`

	rows, err := r.db.Query(ctx, typeStats)
	if err != nil {
		return nil, fmt.Errorf("failed to get type stats: %w", err)
	}
	defer rows.Close()

	statsByType := make(map[string]*TypeStats)
	for rows.Next() {
		var implantType string
		var count int
		var avgSuccessRate float64
		var totalUsage int64

		err := rows.Scan(&implantType, &count, &avgSuccessRate, &totalUsage)
		if err != nil {
			return nil, fmt.Errorf("failed to scan type stats: %w", err)
		}

		statsByType[implantType] = &TypeStats{
			Count:         count,
			AvgSuccessRate: avgSuccessRate,
			TotalUsage:    totalUsage,
		}
	}

	// Get top performing implants (highest success rate with minimum usage)
	topPerforming := `
		SELECT implant_id, player_id, usage_count, success_rate, avg_duration, last_used, created_at, updated_at
		FROM combat.implant_stats
		WHERE usage_count >= 5  -- Minimum usage for meaningful statistics
		ORDER BY success_rate DESC, usage_count DESC
		LIMIT 10
	`

	topRows, err := r.db.Query(ctx, topPerforming)
	if err != nil {
		return nil, fmt.Errorf("failed to get top performing: %w", err)
	}
	defer topRows.Close()

	var topPerformingStats []*ImplantStats
	for topRows.Next() {
		var stat ImplantStats
		err := topRows.Scan(
			&stat.ImplantID, &stat.PlayerID, &stat.UsageCount,
			&stat.SuccessRate, &stat.AvgDuration, &stat.LastUsed,
			&stat.CreatedAt, &stat.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan top performing: %w", err)
		}
		topPerformingStats = append(topPerformingStats, &stat)
	}

	return &AggregatedStats{
		TotalImplants: totalImplants,
		TotalRequests: totalRequests,
		StatsByType:   statsByType,
		TopPerforming: topPerformingStats,
	}, nil
}

// AggregatedStats represents aggregated statistics across all implants
type AggregatedStats struct {
	TotalImplants int
	TotalRequests int64
	StatsByType   map[string]*TypeStats
	TopPerforming []*ImplantStats
}

// TypeStats represents statistics for a specific implant type
type TypeStats struct {
	Count         int
	AvgSuccessRate float64
	TotalUsage    int64
}

// GetUsageTrends retrieves usage trends data for analysis
func (r *Repository) GetUsageTrends(ctx context.Context, trendType, period string) (*UsageTrendsData, error) {
	// Calculate time range based on period
	now := time.Now()
	var startTime time.Time

	switch period {
	case "hour":
		startTime = now.Add(-time.Hour)
	case "day":
		startTime = now.Add(-24 * time.Hour)
	case "week":
		startTime = now.Add(-7 * 24 * time.Hour)
	case "month":
		startTime = now.Add(-30 * 24 * time.Hour)
	default:
		// Default to day
		startTime = now.Add(-24 * time.Hour)
	}

	// Build query based on trend type
	var query string
	switch trendType {
	case "success_rate":
		query = `
			SELECT
				DATE_TRUNC('hour', last_used) as time_bucket,
				AVG(success_rate) as avg_value,
				COUNT(*) as sample_count,
				STDDEV(success_rate) as std_dev
			FROM combat.implant_stats
			WHERE last_used >= $1
			GROUP BY DATE_TRUNC('hour', last_used)
			ORDER BY time_bucket
		`
	case "usage_count":
		query = `
			SELECT
				DATE_TRUNC('hour', last_used) as time_bucket,
				SUM(usage_count) as total_usage,
				COUNT(*) as active_implants,
				AVG(usage_count) as avg_usage_per_implant
			FROM combat.implant_stats
			WHERE last_used >= $1
			GROUP BY DATE_TRUNC('hour', last_used)
			ORDER BY time_bucket
		`
	case "performance":
		query = `
			SELECT
				DATE_TRUNC('hour', last_used) as time_bucket,
				AVG(success_rate * (1.0 / (avg_duration + 1))) as performance_score,
				COUNT(*) as sample_count,
				AVG(avg_duration) as avg_duration
			FROM combat.implant_stats
			WHERE last_used >= $1 AND usage_count >= 3
			GROUP BY DATE_TRUNC('hour', last_used)
			ORDER BY time_bucket
		`
	default:
		// Default to success rate
		query = `
			SELECT
				DATE_TRUNC('hour', last_used) as time_bucket,
				AVG(success_rate) as avg_value,
				COUNT(*) as sample_count,
				STDDEV(success_rate) as std_dev
			FROM combat.implant_stats
			WHERE last_used >= $1
			GROUP BY DATE_TRUNC('hour', last_used)
			ORDER BY time_bucket
		`
	}

	rows, err := r.db.Query(ctx, query, startTime)
	if err != nil {
		return nil, fmt.Errorf("failed to query usage trends: %w", err)
	}
	defer rows.Close()

	var dataPoints []TrendPoint
	var values []float64

	for rows.Next() {
		var timestamp time.Time
		var value float64
		var confidence float64

		switch trendType {
		case "usage_count":
			var totalUsage int64
			var activeImplants int
			var avgUsage float64
			err = rows.Scan(&timestamp, &totalUsage, &activeImplants, &avgUsage)
			value = float64(totalUsage)
			confidence = 0.9 // High confidence for usage counts
		case "performance":
			var performanceScore, avgDuration float64
			var sampleCount int
			err = rows.Scan(&timestamp, &performanceScore, &sampleCount, &avgDuration)
			value = performanceScore
			confidence = 0.85 // Good confidence for performance metrics
		default: // success_rate
			var sampleCount int
			var stdDev float64
			err = rows.Scan(&timestamp, &value, &sampleCount, &stdDev)
			// Calculate confidence based on sample size and standard deviation
			if sampleCount > 10 && stdDev < 0.1 {
				confidence = 0.95
			} else if sampleCount > 5 {
				confidence = 0.8
			} else {
				confidence = 0.6
			}
		}

		if err != nil {
			return nil, fmt.Errorf("failed to scan trend data: %w", err)
		}

		dataPoints = append(dataPoints, TrendPoint{
			Timestamp:  timestamp,
			Value:      value,
			Confidence: confidence,
		})
		values = append(values, value)
	}

	// Determine overall trend
	overallTrend := r.calculateOverallTrend(values)

	return &UsageTrendsData{
		TrendType:    trendType,
		Period:       period,
		OverallTrend: overallTrend,
		DataPoints:   dataPoints,
	}, nil
}

// UsageTrendsData represents usage trends data
type UsageTrendsData struct {
	TrendType    string
	Period       string
	OverallTrend string
	DataPoints   []TrendPoint
}

// TrendPoint represents a single data point in trends
type TrendPoint struct {
	Timestamp  time.Time
	Value      float64
	Confidence float64
}

// calculateOverallTrend determines the overall trend direction
func (r *Repository) calculateOverallTrend(values []float64) string {
	if len(values) < 2 {
		return "insufficient_data"
	}

	// Simple linear regression to determine trend
	n := float64(len(values))
	var sumX, sumY, sumXY, sumX2 float64

	for i, value := range values {
		x := float64(i)
		sumX += x
		sumY += value
		sumXY += x * value
		sumX2 += x * x
	}

	// Calculate slope
	slope := (n*sumXY - sumX*sumY) / (n*sumX2 - sumX*sumX)

	// Determine trend based on slope
	const threshold = 0.01
	if slope > threshold {
		return "increasing"
	} else if slope < -threshold {
		return "decreasing"
	} else {
		return "stable"
	}
}

// AnomaliesData represents detected anomalies data
type AnomaliesData struct {
	DetectedAnomalies []AnomalyData
	AnomalyScore      float64
	TotalScanned      int
}

// AnomalyData represents a single detected anomaly
type AnomalyData struct {
	ImplantID         string
	AnomalyType       string
	Description       string
	DetectedAt        time.Time
	RecommendedAction string
	SeverityScore     float64
}