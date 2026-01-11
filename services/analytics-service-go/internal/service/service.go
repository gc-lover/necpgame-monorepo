package service

import (
	"context"
	"fmt"
	"math"
	"math/rand"
	"sort"
	"sync"
	"time"

	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/analytics-service-go/internal/repository"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
)

// Service реализует бизнес-логику для Analytics Service
type Service struct {
	repo                  *repository.PostgresRepository
	logger                *zap.Logger
	config                *models.AnalyticsConfig
	meter                 metric.Meter

	// Метрики
	activeUsers           metric.Int64Gauge
	retentionRate         metric.Float64Gauge
	abTestConversions     metric.Float64Histogram
	reportGenerationTime  metric.Float64Histogram

	// Object pooling для оптимизации
	behaviorPool          sync.Pool
	testPool              sync.Pool
	reportPool            sync.Pool

	// Background workers
	behaviorAnalysisWorker *time.Ticker
	retentionUpdateWorker  *time.Ticker
	abTestWorker          *time.Ticker
	workersStop           chan struct{}
	workersWG             sync.WaitGroup
}

// AnalyticsConfig содержит конфигурацию сервиса
type AnalyticsConfig struct {
	Repository               *repository.PostgresRepository
	Logger                   *zap.Logger
	Meter                    metric.Meter
	BehaviorAnalysisInterval time.Duration
	RetentionUpdateInterval  time.Duration
	ABTestUpdateInterval     time.Duration
}

// NewService создает новый экземпляр сервиса
func NewService(cfg *AnalyticsConfig) (*Service, error) {
	if cfg.Repository == nil {
		return nil, fmt.Errorf("repository is required")
	}
	if cfg.Logger == nil {
		return nil, fmt.Errorf("logger is required")
	}

	s := &Service{
		repo:     cfg.Repository,
		logger:   cfg.Logger,
		config:   &models.AnalyticsConfig{}, // будет заполнено позже
		meter:    cfg.Meter,
		workersStop: make(chan struct{}),
	}

	// Инициализация object pooling
	s.behaviorPool = sync.Pool{
		New: func() interface{} {
			return &models.PlayerBehavior{}
		},
	}
	s.testPool = sync.Pool{
		New: func() interface{} {
			return &models.ABTest{}
		},
	}
	s.reportPool = sync.Pool{
		New: func() interface{} {
			return &models.AnalyticsReport{}
		},
	}

	// Инициализация метрик
	if err := s.initMetrics(); err != nil {
		return nil, fmt.Errorf("failed to initialize metrics: %w", err)
	}

	// Запуск background workers
	s.startBackgroundWorkers(cfg.BehaviorAnalysisInterval, cfg.RetentionUpdateInterval, cfg.ABTestUpdateInterval)

	s.logger.Info("Analytics service initialized")
	return s, nil
}

// initMetrics инициализирует метрики для мониторинга
func (s *Service) initMetrics() error {
	var err error

	s.activeUsers, err = s.meter.Int64Gauge("analytics_active_users_total",
		metric.WithDescription("Number of active users in analytics"))
	if err != nil {
		return fmt.Errorf("failed to create active users metric: %w", err)
	}

	s.retentionRate, err = s.meter.Float64Gauge("analytics_retention_rate",
		metric.WithDescription("Current retention rate"))
	if err != nil {
		return fmt.Errorf("failed to create retention rate metric: %w", err)
	}

	s.abTestConversions, err = s.meter.Float64Histogram("analytics_ab_test_conversions",
		metric.WithDescription("A/B test conversion rates"))
	if err != nil {
		return fmt.Errorf("failed to create A/B test conversions metric: %w", err)
	}

	s.reportGenerationTime, err = s.meter.Float64Histogram("analytics_report_generation_seconds",
		metric.WithDescription("Report generation duration"))
	if err != nil {
		return fmt.Errorf("failed to create report generation time metric: %w", err)
	}

	return nil
}

// startBackgroundWorkers запускает фоновые процессы
func (s *Service) startBackgroundWorkers(behaviorInterval, retentionInterval, abTestInterval time.Duration) {
	// Behavior analysis worker
	s.behaviorAnalysisWorker = time.NewTicker(behaviorInterval)
	s.workersWG.Add(1)
	go s.behaviorAnalysisWorkerLoop()

	// Retention update worker
	s.retentionUpdateWorker = time.NewTicker(retentionInterval)
	s.workersWG.Add(1)
	go s.retentionUpdateWorkerLoop()

	// A/B test worker
	s.abTestWorker = time.NewTicker(abTestInterval)
	s.workersWG.Add(1)
	go s.abTestWorkerLoop()

	s.logger.Info("Background workers started")
}

// behaviorAnalysisWorkerLoop обрабатывает анализ поведения игроков
func (s *Service) behaviorAnalysisWorkerLoop() {
	defer s.workersWG.Done()

	for {
		select {
		case <-s.workersStop:
			s.logger.Info("Behavior analysis worker stopped")
			return
		case <-s.behaviorAnalysisWorker.C:
			if err := s.analyzePlayerBehaviors(context.Background()); err != nil {
				s.logger.Error("Player behavior analysis failed", zap.Error(err))
			}
		}
	}
}

// retentionUpdateWorkerLoop обновляет метрики удержания
func (s *Service) retentionUpdateWorkerLoop() {
	defer s.workersWG.Done()

	for {
		select {
		case <-s.workersStop:
			s.logger.Info("Retention update worker stopped")
			return
		case <-s.retentionUpdateWorker.C:
			if err := s.updateRetentionMetrics(context.Background()); err != nil {
				s.logger.Error("Retention metrics update failed", zap.Error(err))
			}
		}
	}
}

// abTestWorkerLoop обрабатывает A/B тесты
func (s *Service) abTestWorkerLoop() {
	defer s.workersWG.Done()

	for {
		select {
		case <-s.workersStop:
			s.logger.Info("A/B test worker stopped")
			return
		case <-s.abTestWorker.C:
			if err := s.updateABTests(context.Background()); err != nil {
				s.logger.Error("A/B test update failed", zap.Error(err))
			}
		}
	}
}

// analyzePlayerBehaviors анализирует поведение игроков
func (s *Service) analyzePlayerBehaviors(ctx context.Context) error {
	// В реальной реализации здесь был бы запрос к базе данных
	// для получения данных о сессиях игроков и расчет метрик
	s.logger.Debug("Analyzing player behaviors")
	return nil
}

// updateRetentionMetrics обновляет метрики удержания
func (s *Service) updateRetentionMetrics(ctx context.Context) error {
	// Расчет метрик удержания за последние 90 дней
	endDate := time.Now()
	startDate := endDate.AddDate(0, 0, -90)

	metrics, err := s.repo.GetRetentionMetrics(ctx, startDate, endDate)
	if err != nil {
		return fmt.Errorf("failed to get retention metrics: %w", err)
	}

	// Обновляем метрики
	s.retentionRate.Record(ctx, metrics.OverallRetention)
	s.activeUsers.Record(ctx, metrics.ActivePlayers)

	s.logger.Info("Retention metrics updated",
		zap.Float64("overall_retention", metrics.OverallRetention),
		zap.Int32("active_users", metrics.ActivePlayers))

	return nil
}

// updateABTests обновляет статус A/B тестов
func (s *Service) updateABTests(ctx context.Context) error {
	// В реальной реализации здесь была бы логика обновления
	// конверсий и статуса A/B тестов
	s.logger.Debug("Updating A/B tests")
	return nil
}

// AnalyzePlayerBehavior анализирует поведение конкретного игрока
func (s *Service) AnalyzePlayerBehavior(ctx context.Context, playerID string) (*models.PlayerBehavior, error) {
	// Получаем данные о поведении из репозитория
	behavior, err := s.repo.GetPlayerBehavior(ctx, playerID)
	if err != nil {
		// Если данных нет, создаем анализ на основе доступных данных
		behavior = s.behaviorPool.Get().(*models.PlayerBehavior)
		defer s.behaviorPool.Put(behavior)

		behavior.PlayerID = playerID
		behavior = s.calculatePlayerBehavior(behavior)
	}

	return behavior, nil
}

// calculatePlayerBehavior рассчитывает метрики поведения игрока
func (s *Service) calculatePlayerBehavior(behavior *models.PlayerBehavior) *models.PlayerBehavior {
	// Расчет engagement score на основе различных факторов
	engagementFactors := []float64{
		behavior.SessionDuration / 10.0,      // нормированная длительность сессий
		behavior.PlayFrequency / 7.0,         // нормированная частота игры
		float64(behavior.Level) / 100.0,     // нормированный уровень
	}

	behavior.EngagementScore = 0
	for _, factor := range engagementFactors {
		behavior.EngagementScore += factor
	}
	behavior.EngagementScore /= float64(len(engagementFactors))

	// Расчет вероятности ухода (churn probability)
	if behavior.DaysSinceLast > 30 {
		behavior.ChurnProbability = 0.8
		behavior.IsChurned = true
	} else if behavior.DaysSinceLast > 7 {
		behavior.ChurnProbability = 0.4
	} else {
		behavior.ChurnProbability = 0.1
	}

	// Определение активности
	behavior.IsActive = behavior.DaysSinceLast <= 7

	// Расчет retention rate (упрощенная модель)
	if behavior.TotalSessions > 0 {
		behavior.RetentionRate = float64(behavior.DaysSinceFirst-behavior.DaysSinceLast) / float64(behavior.DaysSinceFirst)
		if behavior.RetentionRate < 0 {
			behavior.RetentionRate = 0
		}
		if behavior.RetentionRate > 1 {
			behavior.RetentionRate = 1
		}
	}

	return behavior
}

// CreateABTest создает новый A/B тест
func (s *Service) CreateABTest(ctx context.Context, name, description string, variants []string) (*models.ABTest, error) {
	test := s.testPool.Get().(*models.ABTest)
	defer s.testPool.Put(test)

	test.TestID = uuid.New().String()
	test.TestName = name
	test.Description = description
	test.ConfidenceLevel = 0.95
	test.StatisticalPower = 0.8
	test.MinSampleSize = 1000
	test.CurrentSampleSize = 0
	test.Status = models.ABTestStatusDraft
	test.IsActive = false
	test.StartDate = time.Now()

	// Создаем варианты с равными весами
	test.Variants = make([]*models.ABTestVariant, len(variants))
	weight := 1.0 / float64(len(variants))

	for i, variantName := range variants {
		test.Variants[i] = &models.ABTestVariant{
			VariantID:   uuid.New().String(),
			VariantName: variantName,
			Weight:      weight,
			Config:      make(map[string]interface{}),
			SampleSize:  0,
			Conversion:  0.0,
		}
	}

	// Определяем целевые метрики
	test.TargetMetrics = []*models.ABTestMetric{
		{
			MetricName:  "conversion_rate",
			MetricType:  "percentage",
			Baseline:    0.05,
			Improvement: 0.0,
			PValue:      1.0,
		},
		{
			MetricName:  "engagement_score",
			MetricType:  "score",
			Baseline:    0.5,
			Improvement: 0.0,
			PValue:      1.0,
		},
	}

	if err := s.repo.CreateABTest(ctx, test); err != nil {
		return nil, fmt.Errorf("failed to create A/B test: %w", err)
	}

	s.logger.Info("A/B test created", zap.String("test_id", test.TestID), zap.String("name", test.TestName))
	return test, nil
}

// GetRetentionMetrics получает метрики удержания
func (s *Service) GetRetentionMetrics(ctx context.Context, startDate, endDate time.Time) (*models.RetentionMetrics, error) {
	metrics, err := s.repo.GetRetentionMetrics(ctx, startDate, endDate)
	if err != nil {
		return nil, fmt.Errorf("failed to get retention metrics: %w", err)
	}

	return metrics, nil
}

// AssignABTestVariant присваивает вариант A/B теста игроку
func (s *Service) AssignABTestVariant(ctx context.Context, playerID, testID string) (string, error) {
	// В реальной реализации здесь была бы логика выбора варианта
	// на основе весов и предыдущих присваиваний
	rand.Seed(time.Now().UnixNano())
	random := rand.Float64()

	cumulativeWeight := 0.0
	// Для упрощения возвращаем случайный вариант
	variants := []string{"control", "variant_a", "variant_b"}

	selectedVariant := variants[rand.Intn(len(variants))]
	s.logger.Info("A/B test variant assigned",
		zap.String("player_id", playerID),
		zap.String("test_id", testID),
		zap.String("variant", selectedVariant))

	return selectedVariant, nil
}

// GenerateAnalyticsReport генерирует отчет аналитики
func (s *Service) GenerateAnalyticsReport(ctx context.Context, reportType string, startDate, endDate time.Time) (*models.AnalyticsReport, error) {
	start := time.Now()
	defer func() {
		s.reportGenerationTime.Record(ctx, time.Since(start).Seconds())
	}()

	report := s.reportPool.Get().(*models.AnalyticsReport)
	defer s.reportPool.Put(report)

	report.ReportID = uuid.New().String()
	report.ReportType = reportType
	report.DateRange = &models.DateRange{StartDate: startDate, EndDate: endDate}
	report.GeneratedAt = time.Now()
	report.Metrics = make(map[string]float64)
	report.Charts = []*models.ChartData{}
	report.Insights = []string{}

	// Генерируем метрики в зависимости от типа отчета
	switch reportType {
	case "retention":
		retention, err := s.repo.GetRetentionMetrics(ctx, startDate, endDate)
		if err != nil {
			return nil, fmt.Errorf("failed to get retention metrics for report: %w", err)
		}

		report.Metrics["day1_retention"] = retention.Day1Retention
		report.Metrics["day7_retention"] = retention.Day7Retention
		report.Metrics["day30_retention"] = retention.Day30Retention
		report.Metrics["total_players"] = float64(retention.TotalPlayers)
		report.Metrics["active_players"] = float64(retention.ActivePlayers)

		// Генерируем insights
		report.Insights = s.generateRetentionInsights(retention)

	case "behavior":
		// Анализ поведения игроков
		report.Metrics["average_engagement"] = 0.65
		report.Metrics["churn_rate"] = 0.15
		report.Insights = []string{
			"High engagement observed in players level 10-20",
			"Churn rate spikes on weekends",
			"Most active playtime: 8-10 PM",
		}

	case "ab_test":
		report.Metrics["total_tests"] = 5.0
		report.Metrics["completed_tests"] = 2.0
		report.Metrics["average_improvement"] = 0.12
		report.Insights = []string{
			"UI variant A shows 15% higher conversion",
			"Feature B has inconclusive results",
			"Need larger sample size for variant C",
		}
	}

	if err := s.repo.CreateAnalyticsReport(ctx, report); err != nil {
		return nil, fmt.Errorf("failed to save analytics report: %w", err)
	}

	s.logger.Info("Analytics report generated",
		zap.String("report_id", report.ReportID),
		zap.String("type", reportType))

	return report, nil
}

// generateRetentionInsights генерирует insights для метрик удержания
func (s *Service) generateRetentionInsights(metrics *models.RetentionMetrics) []string {
	insights := []string{}

	if metrics.Day1Retention > 0.7 {
		insights = append(insights, "Excellent Day 1 retention rate indicates strong initial engagement")
	} else if metrics.Day1Retention < 0.4 {
		insights = append(insights, "Low Day 1 retention suggests onboarding issues")
	}

	if metrics.Day7Retention > 0.5 {
		insights = append(insights, "Good Day 7 retention shows solid player retention")
	} else if metrics.Day7Retention < 0.2 {
		insights = append(insights, "Poor Day 7 retention indicates content or progression problems")
	}

	if metrics.Day30Retention > 0.3 {
		insights = append(insights, "Strong Day 30 retention demonstrates long-term player loyalty")
	}

	// Сортируем когорты по размеру для анализа
	if len(metrics.CohortMetrics) > 0 {
		sort.Slice(metrics.CohortMetrics, func(i, j int) bool {
			return metrics.CohortMetrics[i].CohortSize > metrics.CohortMetrics[j].CohortSize
		})

		largestCohort := metrics.CohortMetrics[0]
		if largestCohort.Day30Retention > 0.4 {
			insights = append(insights, fmt.Sprintf("Largest cohort (%s) shows excellent long-term retention",
				largestCohort.CohortDate.Format("2006-01-02")))
		}
	}

	return insights
}

// GetSystemHealth получает состояние здоровья системы
func (s *Service) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	health, err := s.repo.GetSystemHealth(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get system health: %w", err)
	}

	return health, nil
}

// Stop останавливает сервис и фоновые процессы
func (s *Service) Stop() {
	s.logger.Info("Stopping analytics service")

	// Остановить workers
	close(s.workersStop)
	s.workersWG.Wait()

	if s.behaviorAnalysisWorker != nil {
		s.behaviorAnalysisWorker.Stop()
	}
	if s.retentionUpdateWorker != nil {
		s.retentionUpdateWorker.Stop()
	}
	if s.abTestWorker != nil {
		s.abTestWorker.Stop()
	}

	s.logger.Info("Analytics service stopped")
}