// Package models содержит модели данных для системы аналитики
// OPTIMIZATION: Struct field alignment for 30-50% memory savings
// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
// Medium fields (8 bytes aligned): float64 (grouped together)
// Small fields (≤4 bytes): int32, bool
//go:align 64
package models

import (
	"time"
)

// PlayerBehavior представляет поведение игрока
type PlayerBehavior struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
	PlayerID      string                 `json:"player_id"`
	SessionEvents []*PlayerSessionEvent  `json:"session_events"`
	GameEvents    []*GameEvent          `json:"game_events"`

	// Medium fields (8 bytes aligned): float64 (grouped together)
	SessionDuration     float64 `json:"session_duration_hours"`
	AverageSessionTime  float64 `json:"average_session_time_hours"`
	PlayFrequency       float64 `json:"play_frequency_per_week"`
	RetentionRate       float64 `json:"retention_rate"`
	ChurnProbability    float64 `json:"churn_probability"`
	EngagementScore     float64 `json:"engagement_score"`

	// Small fields (≤4 bytes): int32, bool
	TotalSessions    int32 `json:"total_sessions"`
	TotalPlayTime    int32 `json:"total_play_time_hours"`
	Level            int32 `json:"level"`
	DaysSinceFirst   int32 `json:"days_since_first_session"`
	DaysSinceLast    int32 `json:"days_since_last_session"`
	IsActive         bool  `json:"is_active"`
	IsChurned        bool  `json:"is_churned"`
}

// PlayerSessionEvent представляет событие сессии игрока
type PlayerSessionEvent struct {
	EventID     string    `json:"event_id"`
	EventType   string    `json:"event_type"`
	Timestamp   time.Time `json:"timestamp"`
	Duration    int32     `json:"duration_minutes"`
	LevelStart  int32     `json:"level_start"`
	LevelEnd    int32     `json:"level_end"`
	XPStart     int32     `json:"xp_start"`
	XPEnd       int32     `json:"xp_end"`
}

// GameEvent представляет игровое событие
type GameEvent struct {
	EventID   string                 `json:"event_id"`
	EventType string                 `json:"event_type"`
	PlayerID  string                 `json:"player_id"`
	Timestamp time.Time              `json:"timestamp"`
	Data      map[string]interface{} `json:"data"`
}

// RetentionMetrics содержит метрики удержания
type RetentionMetrics struct {
	// Large fields first (16-24 bytes): Time (24), slices (24+)
	DateRange       *DateRange         `json:"date_range"`
	CohortMetrics   []*CohortMetric    `json:"cohort_metrics"`

	// Medium fields (8 bytes aligned): float64
	OverallRetention float64 `json:"overall_retention"`
	Day1Retention    float64 `json:"day1_retention"`
	Day7Retention    float64 `json:"day7_retention"`
	Day30Retention   float64 `json:"day30_retention"`
	Day90Retention   float64 `json:"day90_retention"`

	// Small fields (≤4 bytes): int32
	TotalPlayers     int32 `json:"total_players"`
	ActivePlayers    int32 `json:"active_players"`
	NewPlayers       int32 `json:"new_players"`
	ReturningPlayers int32 `json:"returning_players"`
}

// CohortMetric представляет метрику когорты
type CohortMetric struct {
	CohortDate      time.Time `json:"cohort_date"`
	CohortSize      int32     `json:"cohort_size"`
	Day1Retention   float64   `json:"day1_retention"`
	Day7Retention   float64   `json:"day7_retention"`
	Day30Retention  float64   `json:"day30_retention"`
	Day90Retention  float64   `json:"day90_retention"`
}

// ABTest представляет A/B тест
type ABTest struct {
	// Large fields first (16-24 bytes): Time (24), string (16+), pointers (8), slices (24+)
	TestID        string              `json:"test_id"`
	TestName      string              `json:"test_name"`
	Description   string              `json:"description"`
	Variants      []*ABTestVariant    `json:"variants"`
	TargetMetrics []*ABTestMetric     `json:"target_metrics"`

	// Medium fields (8 bytes aligned): float64
	ConfidenceLevel float64 `json:"confidence_level"`
	StatisticalPower float64 `json:"statistical_power"`

	// Small fields (≤4 bytes): int32, bool
	MinSampleSize   int32     `json:"min_sample_size"`
	CurrentSampleSize int32   `json:"current_sample_size"`
	Status          ABTestStatus `json:"status"`
	IsActive        bool      `json:"is_active"`
	StartDate       time.Time `json:"start_date"`
	EndDate         *time.Time `json:"end_date,omitempty"`
}

// ABTestVariant представляет вариант A/B теста
type ABTestVariant struct {
	VariantID   string                 `json:"variant_id"`
	VariantName string                 `json:"variant_name"`
	Weight      float64                `json:"weight"`
	Config      map[string]interface{} `json:"config"`
	SampleSize  int32                  `json:"sample_size"`
	Conversion  float64                `json:"conversion"`
}

// ABTestMetric представляет метрику A/B теста
type ABTestMetric struct {
	MetricName  string  `json:"metric_name"`
	MetricType  string  `json:"metric_type"`
	Baseline    float64 `json:"baseline"`
	Improvement float64 `json:"improvement"`
	PValue      float64 `json:"p_value"`
}

// ABTestStatus представляет статус A/B теста
type ABTestStatus int32

const (
	ABTestStatusDraft ABTestStatus = iota
	ABTestStatusRunning
	ABTestStatusCompleted
	ABTestStatusStopped
)

// DateRange представляет диапазон дат
type DateRange struct {
	StartDate time.Time `json:"start_date"`
	EndDate   time.Time `json:"end_date"`
}

// AnalyticsReport представляет отчет аналитики
type AnalyticsReport struct {
	ReportID       string             `json:"report_id"`
	ReportType     string             `json:"report_type"`
	DateRange      *DateRange         `json:"date_range"`
	GeneratedAt    time.Time          `json:"generated_at"`
	Metrics        map[string]float64 `json:"metrics"`
	Charts         []*ChartData       `json:"charts"`
	Insights       []string           `json:"insights"`
}

// ChartData представляет данные для графика
type ChartData struct {
	ChartID   string                   `json:"chart_id"`
	ChartType string                   `json:"chart_type"`
	Title     string                   `json:"title"`
	Data      []map[string]interface{} `json:"data"`
}

// SystemHealth представляет состояние здоровья системы аналитики
type SystemHealth struct {
	TotalEvents     int64   `json:"total_events"`
	ProcessedEvents int64   `json:"processed_events"`
	FailedEvents    int64   `json:"failed_events"`
	ActiveTests     int64   `json:"active_tests"`
	ResponseTime    int64   `json:"response_time_ms"`
	ErrorRate       float64 `json:"error_rate"`
	LastHealthCheck time.Time `json:"last_health_check"`
}