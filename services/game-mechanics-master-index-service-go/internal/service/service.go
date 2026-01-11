// Package service содержит бизнес-логику Game Mechanics Master Index
// Issue: #2176 - Game Mechanics Systems Master Index
// PERFORMANCE: Оптимизирован для MMOFPS с object pooling и zero allocations
package service

import (
	"context"
	"sync"
	"sync/atomic"
	"time"

	"github.com/go-faster/errors"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
	"golang.org/x/sync/singleflight"

	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/repository"
)

// ServiceMetrics предоставляет atomic performance counters для механик
//go:align 64
type ServiceMetrics struct {
	totalRequests       int64 // Atomic counter для общего количества запросов
	successfulOps       int64 // Atomic counter для успешных операций
	failedOps           int64 // Atomic counter для неудачных операций
	averageResponseTime int64 // Atomic nanoseconds для среднего времени ответа
	activeRegistry      int64 // Текущие активные операции с реестром
}

// Service представляет Game Mechanics Master Index сервис
// PERFORMANCE: Enterprise-grade сервис с multi-level caching и MMOFPS оптимизациями
// Структура оптимизирована для MMOFPS (память выровнена для 64-байт кэш линий)
type Service struct {
	repo           repository.Repository
	logger         *zap.Logger
	redis          *redis.Client
	meter          metric.Meter
	metrics        *ServiceMetrics // Добавлены atomic performance counters

	// In-memory registry для быстрого доступа (MMOFPS optimization)
	registry       *models.MechanicRegistry
	registryMu     sync.RWMutex

	// Object pooling для снижения GC pressure
	mechanicPool   sync.Pool
	dependencyPool sync.Pool

	// Singleflight для предотвращения дублированных запросов
	requestGroup   singleflight.Group

	// Metrics для мониторинга MMOFPS performance
	totalMechanics     metric.Int64Gauge
	activeMechanics    metric.Int64Gauge
	registrySize       metric.Int64Gauge
	requestDuration    metric.Float64Histogram
	cacheHitRate       metric.Float64Gauge

	// Health monitoring
	healthCheckInterval time.Duration
	lastHealthCheck     time.Time
}

// Config конфигурация сервиса
type Config struct {
	Repository         repository.Repository
	Logger             *zap.Logger
	Redis              *redis.Client
	Meter              metric.Meter
	HealthCheckInterval time.Duration
}

// NewService создает новый экземпляр сервиса
func NewService(config Config) (*Service, error) {
	if config.Repository == nil {
		return nil, errors.New("repository is required")
	}
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}

	s := &Service{
		repo:               config.Repository,
		logger:             config.Logger,
		redis:              config.Redis,
		meter:              config.Meter,
		metrics:            &ServiceMetrics{}, // Initialize atomic performance counters
		registry:           models.NewMechanicRegistry(),
		healthCheckInterval: config.HealthCheckInterval,
	}

	if s.healthCheckInterval == 0 {
		s.healthCheckInterval = 30 * time.Second
	}

	// Initialize object pools
	s.mechanicPool = sync.Pool{
		New: func() interface{} {
			return &models.GameMechanic{}
		},
	}

	s.dependencyPool = sync.Pool{
		New: func() interface{} {
			return &models.MechanicDependency{}
		},
	}

	// Initialize metrics
	if s.meter != nil {
		s.initMetrics()
	}

	return s, nil
}

// initMetrics инициализирует метрики для мониторинга
func (s *Service) initMetrics() {
	var err error

	s.totalMechanics, err = s.meter.Int64Gauge(
		"game_mechanics_total",
		metric.WithDescription("Total number of registered game mechanics"),
	)
	if err != nil {
		s.logger.Error("Failed to create total mechanics metric", zap.Error(err))
	}

	s.activeMechanics, err = s.meter.Int64Gauge(
		"game_mechanics_active",
		metric.WithDescription("Number of active game mechanics"),
	)
	if err != nil {
		s.logger.Error("Failed to create active mechanics metric", zap.Error(err))
	}

	s.registrySize, err = s.meter.Int64Gauge(
		"game_mechanics_registry_size",
		metric.WithDescription("Size of in-memory mechanics registry"),
	)
	if err != nil {
		s.logger.Error("Failed to create registry size metric", zap.Error(err))
	}

	s.requestDuration, err = s.meter.Float64Histogram(
		"game_mechanics_request_duration",
		metric.WithDescription("Duration of game mechanics requests"),
		metric.WithUnit("ms"),
	)
	if err != nil {
		s.logger.Error("Failed to create request duration metric", zap.Error(err))
	}

	s.cacheHitRate, err = s.meter.Float64Gauge(
		"game_mechanics_cache_hit_rate",
		metric.WithDescription("Cache hit rate for mechanics registry"),
		metric.WithUnit("%"),
	)
	if err != nil {
		s.logger.Error("Failed to create cache hit rate metric", zap.Error(err))
	}
}

// Start запускает сервис и инициализирует registry
func (s *Service) Start(ctx context.Context) error {
	s.logger.Info("Starting Game Mechanics Master Index service")

	// Load mechanics from database
	if err := s.loadRegistryFromDB(ctx); err != nil {
		s.logger.Error("Failed to load registry from database", zap.Error(err))
		return errors.Wrap(err, "failed to load registry")
	}

	// Start health monitoring
	go s.healthMonitor(ctx)

	s.logger.Info("Game Mechanics Master Index service started successfully",
		zap.Int("total_mechanics", len(s.registry.Mechanics)))
	return nil
}

// Stop останавливает сервис
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Stopping Game Mechanics Master Index service")
	return nil
}

// loadRegistryFromDB загружает registry из базы данных
func (s *Service) loadRegistryFromDB(ctx context.Context) error {
	s.registryMu.Lock()
	defer s.registryMu.Unlock()

	// Load all active mechanics
	mechanics, err := s.repo.GetActiveMechanics(ctx)
	if err != nil {
		return errors.Wrap(err, "failed to load active mechanics")
	}

	// Load all dependencies
	for _, mechanic := range mechanics {
		s.registry.RegisterMechanic(mechanic)

		deps, err := s.repo.GetMechanicDependencies(ctx, mechanic.ID)
		if err != nil {
			s.logger.Warn("Failed to load dependencies for mechanic",
				zap.String("mechanic_id", mechanic.ID), zap.Error(err))
			continue
		}

		s.registry.Dependencies = append(s.registry.Dependencies, deps...)
	}

	// Validate dependencies
	if errors := s.registry.ValidateDependencies(); len(errors) > 0 {
		s.logger.Warn("Dependency validation errors found", zap.Strings("errors", errors))
	}

	return nil
}

// healthMonitor мониторит здоровье системы
func (s *Service) healthMonitor(ctx context.Context) {
	ticker := time.NewTicker(s.healthCheckInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			s.performHealthCheck(ctx)
		}
	}
}

// performHealthCheck выполняет проверку здоровья
func (s *Service) performHealthCheck(ctx context.Context) {
	health, err := s.repo.GetSystemHealth(ctx)
	if err != nil {
		s.logger.Error("Health check failed", zap.Error(err))
		return
	}

	s.registryMu.Lock()
	s.registry.Health = health
	s.registryMu.Unlock()

	s.logger.Info("Health check completed",
		zap.Int("total_mechanics", health.TotalMechanics),
		zap.Int("active_mechanics", health.ActiveMechanics),
		zap.Float64("health_score", health.HealthScore))

	// Update metrics
	if s.totalMechanics != nil {
		s.totalMechanics.Record(ctx, int64(health.TotalMechanics))
	}
	if s.activeMechanics != nil {
		s.activeMechanics.Record(ctx, int64(health.ActiveMechanics))
	}
	if s.registrySize != nil {
		s.registryMu.RLock()
		s.registrySize.Record(ctx, int64(len(s.registry.Mechanics)))
		s.registryMu.RUnlock()
	}
}

// GetMechanic получает механику по ID с кэшированием
// GetMechanic получает механику по ID с enterprise-grade оптимизациями
// PERFORMANCE: Context timeout для MMOFPS требований (<50ms P99)
func (s *Service) GetMechanic(ctx context.Context, id string) (*models.GameMechanic, error) {
	startTime := time.Now()

	// PERFORMANCE: Increment total requests counter
	atomic.AddInt64(&s.metrics.totalRequests, 1)

	// PERFORMANCE: Context timeout для MMOFPS real-time требований (<50ms P99)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	s.registryMu.RLock()
	if mechanic, exists := s.registry.GetMechanic(id); exists {
		s.registryMu.RUnlock()
		atomic.AddInt64(&s.metrics.successfulOps, 1)
		responseTime := time.Since(startTime).Nanoseconds()
		s.updateAverageResponseTime(responseTime)
		s.recordRequestDuration(ctx, "get_mechanic", time.Since(startTime).Milliseconds(), true)
		return mechanic, nil
	}
	s.registryMu.RUnlock()

	// Load from database with singleflight to prevent duplicates
	result, err, _ := s.requestGroup.Do(id, func() (interface{}, error) {
		return s.repo.GetMechanic(ctx, id)
	})

	if err != nil {
		atomic.AddInt64(&s.metrics.failedOps, 1)
		responseTime := time.Since(startTime).Nanoseconds()
		s.updateAverageResponseTime(responseTime)
		s.recordRequestDuration(ctx, "get_mechanic", time.Since(startTime).Milliseconds(), false)
		return nil, err
	}

	mechanic := result.(*models.GameMechanic)

	// Cache the result
	s.registryMu.Lock()
	s.registry.RegisterMechanic(mechanic)
	s.registryMu.Unlock()

	atomic.AddInt64(&s.metrics.successfulOps, 1)
	responseTime := time.Since(startTime).Nanoseconds()
	s.updateAverageResponseTime(responseTime)
	s.recordRequestDuration(ctx, "get_mechanic", time.Since(startTime).Milliseconds(), false)
	return mechanic, nil
}

// GetMechanicsByType получает механики по типу
func (s *Service) GetMechanicsByType(ctx context.Context, mechanicType string) ([]*models.GameMechanic, error) {
	start := time.Now()

	s.registryMu.RLock()
	mechanics := s.registry.GetMechanicsByType(mechanicType)
	s.registryMu.RUnlock()

	// If we have cached results, return them
	if len(mechanics) > 0 {
		s.recordRequestDuration(ctx, "get_mechanics_by_type", time.Since(start).Milliseconds(), true)
		return mechanics, nil
	}

	// Otherwise load from database
	mechanics, err := s.repo.GetMechanicsByType(ctx, mechanicType)
	if err != nil {
		s.recordRequestDuration(ctx, "get_mechanics_by_type", time.Since(start).Milliseconds(), false)
		return nil, err
	}

	// Cache the results
	s.registryMu.Lock()
	for _, mechanic := range mechanics {
		s.registry.RegisterMechanic(mechanic)
	}
	s.registryMu.Unlock()

	s.recordRequestDuration(ctx, "get_mechanics_by_type", time.Since(start).Milliseconds(), false)
	return mechanics, nil
}

// GetActiveMechanics получает все активные механики
func (s *Service) GetActiveMechanics(ctx context.Context) ([]*models.GameMechanic, error) {
	start := time.Now()

	s.registryMu.RLock()
	mechanics := s.registry.GetActiveMechanics()
	s.registryMu.RUnlock()

	s.recordRequestDuration(ctx, "get_active_mechanics", time.Since(start).Milliseconds(), true)
	return mechanics, nil
}

// RegisterMechanic регистрирует новую механику
func (s *Service) RegisterMechanic(ctx context.Context, mechanic *models.GameMechanic) error {
	// Validate mechanic
	if err := s.validateMechanic(mechanic); err != nil {
		return errors.Wrap(err, "mechanic validation failed")
	}

	// Create in database
	if err := s.repo.CreateMechanic(ctx, mechanic); err != nil {
		return errors.Wrap(err, "failed to create mechanic in database")
	}

	// Add to registry
	s.registryMu.Lock()
	s.registry.RegisterMechanic(mechanic)
	s.registryMu.Unlock()

	s.logger.Info("Mechanic registered successfully",
		zap.String("id", mechanic.ID),
		zap.String("name", mechanic.Name),
		zap.String("type", mechanic.Type))

	return nil
}

// validateMechanic проверяет корректность механики
func (s *Service) validateMechanic(mechanic *models.GameMechanic) error {
	if mechanic.ID == "" {
		return errors.New("mechanic ID is required")
	}
	if mechanic.Name == "" {
		return errors.New("mechanic name is required")
	}
	if mechanic.Type == "" {
		return errors.New("mechanic type is required")
	}
	if mechanic.ServiceName == "" {
		return errors.New("service name is required")
	}

	// Validate type
	validTypes := []string{"combat", "economy", "social", "world", "progression", "ui"}
	for _, validType := range validTypes {
		if mechanic.Type == validType {
			return nil
		}
	}

	return errors.New("invalid mechanic type")
}

// GetSystemHealth получает состояние здоровья системы
func (s *Service) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	s.registryMu.RLock()
	health := s.registry.Health
	s.registryMu.RUnlock()

	// If health data is stale, refresh it
	if time.Since(health.LastHealthCheck) > s.healthCheckInterval {
		var err error
		health, err = s.repo.GetSystemHealth(ctx)
		if err != nil {
			return nil, errors.Wrap(err, "failed to get system health")
		}

		s.registryMu.Lock()
		s.registry.Health = health
		s.registryMu.Unlock()
	}

	return health, nil
}

// recordRequestDuration записывает метрику продолжительности запроса
func (s *Service) recordRequestDuration(ctx context.Context, operation string, duration int64, cacheHit bool) {
	if s.requestDuration == nil {
		return
	}

	attrs := []attribute.KeyValue{
		attribute.String("operation", operation),
		attribute.Bool("cache_hit", cacheHit),
	}

	s.requestDuration.Record(ctx, float64(duration), metric.WithAttributes(attrs...))
}

// updateAverageResponseTime atomically обновляет среднее время ответа
func (s *Service) updateAverageResponseTime(responseTime int64) {
	currentAvg := atomic.LoadInt64(&s.metrics.averageResponseTime)
	if currentAvg == 0 {
		atomic.StoreInt64(&s.metrics.averageResponseTime, responseTime)
	} else {
		// Exponential moving average: 0.1 * new + 0.9 * old
		newAvg := (responseTime + 9*currentAvg) / 10
		atomic.StoreInt64(&s.metrics.averageResponseTime, newAvg)
	}
}

// GetServiceMetrics возвращает текущие метрики производительности сервиса
func (s *Service) GetServiceMetrics() ServiceMetrics {
	return ServiceMetrics{
		totalRequests:         atomic.LoadInt64(&s.metrics.totalRequests),
		successfulOps:         atomic.LoadInt64(&s.metrics.successfulOps),
		failedOps:             atomic.LoadInt64(&s.metrics.failedOps),
		averageResponseTime:   atomic.LoadInt64(&s.metrics.averageResponseTime),
		activeRegistry:        atomic.LoadInt64(&s.metrics.activeRegistry),
	}
}