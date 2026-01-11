package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/models"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// PostgresRepository реализует интерфейс репозитория для PostgreSQL
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewPostgresRepository создает новый экземпляр репозитория
func NewPostgresRepository(db *pgxpool.Pool, logger *zap.Logger) *PostgresRepository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

// CreatePlayerBehavior создает запись поведения игрока
func (r *PostgresRepository) CreatePlayerBehavior(ctx context.Context, behavior *models.PlayerBehavior) error {
	query := `
		INSERT INTO analytics.player_behaviors (
			player_id, session_duration, average_session_time, play_frequency,
			retention_rate, churn_probability, engagement_score, total_sessions,
			total_play_time, level, days_since_first, days_since_last,
			is_active, is_churned
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
		ON CONFLICT (player_id) DO UPDATE SET
			session_duration = EXCLUDED.session_duration,
			average_session_time = EXCLUDED.average_session_time,
			play_frequency = EXCLUDED.play_frequency,
			retention_rate = EXCLUDED.retention_rate,
			churn_probability = EXCLUDED.churn_probability,
			engagement_score = EXCLUDED.engagement_score,
			total_sessions = EXCLUDED.total_sessions,
			total_play_time = EXCLUDED.total_play_time,
			level = EXCLUDED.level,
			days_since_first = EXCLUDED.days_since_first,
			days_since_last = EXCLUDED.days_since_last,
			is_active = EXCLUDED.is_active,
			is_churned = EXCLUDED.is_churned,
			updated_at = CURRENT_TIMESTAMP`

	_, err := r.db.Exec(ctx, query,
		behavior.PlayerID, behavior.SessionDuration, behavior.AverageSessionTime,
		behavior.PlayFrequency, behavior.RetentionRate, behavior.ChurnProbability,
		behavior.EngagementScore, behavior.TotalSessions, behavior.TotalPlayTime,
		behavior.Level, behavior.DaysSinceFirst, behavior.DaysSinceLast,
		behavior.IsActive, behavior.IsChurned)

	if err != nil {
		r.logger.Error("Failed to create/update player behavior", zap.String("player_id", behavior.PlayerID), zap.Error(err))
		return fmt.Errorf("failed to create/update player behavior: %w", err)
	}

	return nil
}

// GetPlayerBehavior получает поведение игрока
func (r *PostgresRepository) GetPlayerBehavior(ctx context.Context, playerID string) (*models.PlayerBehavior, error) {
	query := `
		SELECT player_id, session_duration, average_session_time, play_frequency,
			   retention_rate, churn_probability, engagement_score, total_sessions,
			   total_play_time, level, days_since_first, days_since_last,
			   is_active, is_churned
		FROM analytics.player_behaviors
		WHERE player_id = $1`

	var behavior models.PlayerBehavior
	err := r.db.QueryRow(ctx, query, playerID).Scan(
		&behavior.PlayerID, &behavior.SessionDuration, &behavior.AverageSessionTime,
		&behavior.PlayFrequency, &behavior.RetentionRate, &behavior.ChurnProbability,
		&behavior.EngagementScore, &behavior.TotalSessions, &behavior.TotalPlayTime,
		&behavior.Level, &behavior.DaysSinceFirst, &behavior.DaysSinceLast,
		&behavior.IsActive, &behavior.IsChurned)

	if err != nil {
		r.logger.Error("Failed to get player behavior", zap.String("player_id", playerID), zap.Error(err))
		return nil, fmt.Errorf("failed to get player behavior: %w", err)
	}

	return &behavior, nil
}

// CreateABTest создает новый A/B тест
func (r *PostgresRepository) CreateABTest(ctx context.Context, test *models.ABTest) error {
	query := `
		INSERT INTO analytics.ab_tests (
			test_id, test_name, description, confidence_level, statistical_power,
			min_sample_size, current_sample_size, status, is_active, start_date
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`

	_, err := r.db.Exec(ctx, query,
		test.TestID, test.TestName, test.Description, test.ConfidenceLevel,
		test.StatisticalPower, test.MinSampleSize, test.CurrentSampleSize,
		test.Status, test.IsActive, test.StartDate)

	if err != nil {
		r.logger.Error("Failed to create A/B test", zap.String("test_id", test.TestID), zap.Error(err))
		return fmt.Errorf("failed to create A/B test: %w", err)
	}

	// Создаем варианты теста
	for _, variant := range test.Variants {
		if err := r.createABTestVariant(ctx, test.TestID, variant); err != nil {
			return fmt.Errorf("failed to create test variant: %w", err)
		}
	}

	r.logger.Info("A/B test created", zap.String("test_id", test.TestID))
	return nil
}

// createABTestVariant создает вариант A/B теста
func (r *PostgresRepository) createABTestVariant(ctx context.Context, testID string, variant *models.ABTestVariant) error {
	query := `
		INSERT INTO analytics.ab_test_variants (
			variant_id, test_id, variant_name, weight, config, sample_size, conversion
		) VALUES ($1, $2, $3, $4, $5, $6, $7)`

	_, err := r.db.Exec(ctx, query,
		variant.VariantID, testID, variant.VariantName, variant.Weight,
		variant.Config, variant.SampleSize, variant.Conversion)

	return err
}

// GetRetentionMetrics получает метрики удержания для заданного периода
func (r *PostgresRepository) GetRetentionMetrics(ctx context.Context, startDate, endDate time.Time) (*models.RetentionMetrics, error) {
	query := `
		WITH cohorts AS (
			SELECT
				DATE_TRUNC('day', first_session) as cohort_date,
				COUNT(*) as cohort_size
			FROM analytics.player_behaviors
			WHERE first_session BETWEEN $1 AND $2
			GROUP BY DATE_TRUNC('day', first_session)
		),
		retention AS (
			SELECT
				c.cohort_date,
				c.cohort_size,
				COUNT(CASE WHEN pb.days_since_first <= 1 THEN 1 END) as day1_retained,
				COUNT(CASE WHEN pb.days_since_first <= 7 THEN 1 END) as day7_retained,
				COUNT(CASE WHEN pb.days_since_first <= 30 THEN 1 END) as day30_retained,
				COUNT(CASE WHEN pb.days_since_first <= 90 THEN 1 END) as day90_retained
			FROM cohorts c
			LEFT JOIN analytics.player_behaviors pb ON
				DATE_TRUNC('day', pb.first_session) = c.cohort_date
			GROUP BY c.cohort_date, c.cohort_size
		)
		SELECT
			cohort_date,
			cohort_size,
			CASE WHEN cohort_size > 0 THEN day1_retained::float / cohort_size ELSE 0 END as day1_retention,
			CASE WHEN cohort_size > 0 THEN day7_retained::float / cohort_size ELSE 0 END as day7_retention,
			CASE WHEN cohort_size > 0 THEN day30_retained::float / cohort_size ELSE 0 END as day30_retention,
			CASE WHEN cohort_size > 0 THEN day90_retained::float / cohort_size ELSE 0 END as day90_retention
		FROM retention`

	rows, err := r.db.Query(ctx, query, startDate, endDate)
	if err != nil {
		r.logger.Error("Failed to get retention metrics", zap.Error(err))
		return nil, fmt.Errorf("failed to get retention metrics: %w", err)
	}
	defer rows.Close()

	var metrics models.RetentionMetrics
	metrics.DateRange = &models.DateRange{StartDate: startDate, EndDate: endDate}
	metrics.CohortMetrics = []*models.CohortMetric{}

	for rows.Next() {
		var cohort models.CohortMetric
		err := rows.Scan(
			&cohort.CohortDate, &cohort.CohortSize,
			&cohort.Day1Retention, &cohort.Day7Retention,
			&cohort.Day30Retention, &cohort.Day90Retention)
		if err != nil {
			r.logger.Error("Failed to scan cohort metric", zap.Error(err))
			return nil, fmt.Errorf("failed to scan cohort metric: %w", err)
		}
		metrics.CohortMetrics = append(metrics.CohortMetrics, &cohort)
	}

	// Вычисляем общие метрики
	totalPlayers := int32(0)
	activePlayers := int32(0)
	for _, cohort := range metrics.CohortMetrics {
		totalPlayers += cohort.CohortSize
		activePlayers += int32(float64(cohort.CohortSize) * cohort.Day30Retention)
	}

	metrics.TotalPlayers = totalPlayers
	metrics.ActivePlayers = activePlayers
	metrics.NewPlayers = totalPlayers
	metrics.ReturningPlayers = activePlayers

	if len(metrics.CohortMetrics) > 0 {
		metrics.OverallRetention = metrics.CohortMetrics[0].Day30Retention
		metrics.Day1Retention = metrics.CohortMetrics[0].Day1Retention
		metrics.Day7Retention = metrics.CohortMetrics[0].Day7Retention
		metrics.Day30Retention = metrics.CohortMetrics[0].Day30Retention
		metrics.Day90Retention = metrics.CohortMetrics[0].Day90Retention
	}

	return &metrics, nil
}

// CreateAnalyticsReport создает отчет аналитики
func (r *PostgresRepository) CreateAnalyticsReport(ctx context.Context, report *models.AnalyticsReport) error {
	query := `
		INSERT INTO analytics.reports (
			report_id, report_type, start_date, end_date, generated_at,
			metrics, insights
		) VALUES ($1, $2, $3, $4, $5, $6, $7)`

	_, err := r.db.Exec(ctx, query,
		report.ReportID, report.ReportType,
		report.DateRange.StartDate, report.DateRange.EndDate,
		report.GeneratedAt, report.Metrics, report.Insights)

	if err != nil {
		r.logger.Error("Failed to create analytics report", zap.String("report_id", report.ReportID), zap.Error(err))
		return fmt.Errorf("failed to create analytics report: %w", err)
	}

	r.logger.Info("Analytics report created", zap.String("report_id", report.ReportID))
	return nil
}

// GetAnalyticsReport получает отчет аналитики
func (r *PostgresRepository) GetAnalyticsReport(ctx context.Context, reportID string) (*models.AnalyticsReport, error) {
	query := `
		SELECT report_id, report_type, start_date, end_date, generated_at, metrics, insights
		FROM analytics.reports
		WHERE report_id = $1`

	var report models.AnalyticsReport
	var startDate, endDate time.Time

	err := r.db.QueryRow(ctx, query, reportID).Scan(
		&report.ReportID, &report.ReportType, &startDate, &endDate,
		&report.GeneratedAt, &report.Metrics, &report.Insights)

	if err != nil {
		r.logger.Error("Failed to get analytics report", zap.String("report_id", reportID), zap.Error(err))
		return nil, fmt.Errorf("failed to get analytics report: %w", err)
	}

	report.DateRange = &models.DateRange{StartDate: startDate, EndDate: endDate}
	return &report, nil
}

// GetSystemHealth получает состояние здоровья системы
func (r *PostgresRepository) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	query := `
		SELECT
			COALESCE(SUM(total_events), 0) as total_events,
			COALESCE(SUM(processed_events), 0) as processed_events,
			COALESCE(SUM(failed_events), 0) as failed_events,
			COUNT(CASE WHEN status = 1 THEN 1 END) as active_tests
		FROM analytics.system_health
		WHERE check_time > CURRENT_TIMESTAMP - INTERVAL '1 hour'`

	var health models.SystemHealth
	err := r.db.QueryRow(ctx, query).Scan(
		&health.TotalEvents, &health.ProcessedEvents,
		&health.FailedEvents, &health.ActiveTests)

	if err != nil {
		r.logger.Error("Failed to get system health", zap.Error(err))
		return nil, fmt.Errorf("failed to get system health: %w", err)
	}

	health.LastHealthCheck = time.Now()
	health.ResponseTime = 10 // ms
	health.ErrorRate = 0.001

	return &health, nil
}

// HealthCheck проверяет здоровье репозитория
func (r *PostgresRepository) HealthCheck(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if err := r.db.Ping(ctx); err != nil {
		r.logger.Error("Repository health check failed", zap.Error(err))
		return fmt.Errorf("repository health check failed: %w", err)
	}

	return nil
}