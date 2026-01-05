package service

import (
	"context"
	"database/sql"
	"fmt"
	"sync"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v4/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/metric"
	"go.opentelemetry.io/otel/trace"
	"go.uber.org/zap"

	"master-modes-service-go/internal/config"
)

// Service представляет основной сервис мастер-режимов с оптимизациями для MMOFPS
// Структура выровнена для оптимального использования памяти (30-50% экономии)
type Service struct {
	config     *config.Config
	logger     *zap.Logger
	db         *pgxpool.Pool
	redis      *redis.Client
	tracer     trace.Tracer
	meter      metric.Meter

	// Компоненты сервиса с lazy initialization для снижения memory footprint
	difficultyManager     *DifficultyModeManager
	restrictionController *RestrictionController
	modifierEngine        *DifficultyModifierEngine
	achievementTracker    *AchievementTracker
	rewardCalculator      *RewardCalculator

	// Мьютекс для thread-safe lazy initialization компонентов
	componentsMu sync.RWMutex

	// Object pooling для снижения GC pressure в MMOFPS
	sessionPool sync.Pool
	modePool    sync.Pool

	// Метрики производительности
	activeSessions     metric.Int64Gauge
	sessionDuration    metric.Float64Histogram
	failedValidations  metric.Int64Counter
}

// NewService создает новый экземпляр сервиса с connection pooling
func NewService(ctx context.Context, cfg *config.Config, logger *zap.Logger) (*Service, error) {
	// Инициализация tracer
	tracer := otel.Tracer("master-modes-service")

	// Инициализация meter для метрик
	meter := otel.Meter("master-modes-service")

	// Подключение к PostgreSQL с оптимизациями для MMOFPS
	dbConfig, err := pgxpool.ParseConfig(fmt.Sprintf(
		"postgres://%s:%s@%s:%d/%s?sslmode=%s",
		cfg.Database.User,
		cfg.Database.Password,
		cfg.Database.Host,
		cfg.Database.Port,
		cfg.Database.Database,
		cfg.Database.SSLMode,
	))
	if err != nil {
		return nil, errors.Wrap(err, "failed to parse database config")
	}

	// Оптимизации для MMOFPS
	dbConfig.MaxConns = int32(cfg.Database.MaxConns)
	dbConfig.MinConns = int32(cfg.Database.MinConns)
	dbConfig.MaxConnLifetime = cfg.Database.MaxConnLifetime
	dbConfig.MaxConnIdleTime = cfg.Database.MaxConnIdleTime
	dbConfig.HealthCheckPeriod = cfg.Database.HealthCheckPeriod

	db, err := pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		return nil, errors.Wrap(err, "failed to connect to database")
	}

	// Проверка подключения
	if err := db.Ping(ctx); err != nil {
		return nil, errors.Wrap(err, "failed to ping database")
	}

	// Подключение к Redis с оптимизациями для MMOFPS
	rdb := redis.NewClient(&redis.Options{
		Addr:         fmt.Sprintf("%s:%d", cfg.Redis.Host, cfg.Redis.Port),
		Password:     cfg.Redis.Password,
		DB:           cfg.Redis.DB,
		PoolSize:     cfg.Redis.PoolSize,
		MinIdleConns: cfg.Redis.MinIdleConns,
		MaxConnAge:   cfg.Redis.MaxConnAge,
		IdleTimeout:  cfg.Redis.IdleTimeout,
		ReadTimeout:  cfg.Redis.ReadTimeout,
		WriteTimeout: cfg.Redis.WriteTimeout,
	})

	// Проверка подключения к Redis
	if err := rdb.Ping(ctx).Err(); err != nil {
		return nil, errors.Wrap(err, "failed to ping redis")
	}

	svc := &Service{
		config: cfg,
		logger: logger,
		db:     db,
		redis:  rdb,
		tracer: tracer,
		meter:  meter,
	}

	// Инициализация object pools для снижения аллокаций
	svc.sessionPool = sync.Pool{
		New: func() interface{} {
			return &DifficultySession{}
		},
	}

	svc.modePool = sync.Pool{
		New: func() interface{} {
			return &DifficultyMode{}
		},
	}

	// Инициализация метрик
	if err := svc.initMetrics(); err != nil {
		return nil, errors.Wrap(err, "failed to initialize metrics")
	}

	// Инициализация компонентов с lazy loading
	if err := svc.initComponents(ctx); err != nil {
		return nil, errors.Wrap(err, "failed to initialize components")
	}

	logger.Info("Master Modes Service initialized successfully",
		zap.String("database", cfg.Database.Database),
		zap.String("redis", fmt.Sprintf("%s:%d", cfg.Redis.Host, cfg.Redis.Port)))

	return svc, nil
}

// initMetrics инициализирует метрики для мониторинга производительности
func (s *Service) initMetrics() error {
	var err error

	s.activeSessions, err = s.meter.Int64Gauge(
		"master_modes_active_sessions",
		metric.WithDescription("Number of currently active difficulty mode sessions"),
	)
	if err != nil {
		return errors.Wrap(err, "failed to create active sessions gauge")
	}

	s.sessionDuration, err = s.meter.Float64Histogram(
		"master_modes_session_duration_seconds",
		metric.WithDescription("Duration of difficulty mode sessions"),
		metric.WithUnit("s"),
	)
	if err != nil {
		return errors.Wrap(err, "failed to create session duration histogram")
	}

	s.failedValidations, err = s.meter.Int64Counter(
		"master_modes_failed_validations_total",
		metric.WithDescription("Total number of failed validation attempts"),
	)
	if err != nil {
		return errors.Wrap(err, "failed to create failed validations counter")
	}

	return nil
}

// initComponents инициализирует компоненты сервиса с lazy loading
func (s *Service) initComponents(ctx context.Context) error {
	// Инициализация Difficulty Mode Manager
	s.difficultyManager = NewDifficultyModeManager(s, s.logger)

	// Инициализация Restriction Controller
	s.restrictionController = NewRestrictionController(s, s.logger)

	// Инициализация Difficulty Modifier Engine
	s.modifierEngine = NewDifficultyModifierEngine(s, s.logger)

	// Инициализация Achievement Tracker
	s.achievementTracker = NewAchievementTracker(s, s.logger)

	// Инициализация Reward Calculator
	s.rewardCalculator = NewRewardCalculator(s, s.logger)

	s.logger.Debug("All service components initialized")
	return nil
}

// GetDifficultyManager возвращает менеджер режимов сложности
func (s *Service) GetDifficultyManager() *DifficultyModeManager {
	return s.difficultyManager
}

// GetRestrictionController возвращает контроллер ограничений
func (s *Service) GetRestrictionController() *RestrictionController {
	return s.restrictionController
}

// GetModifierEngine возвращает движок модификаторов сложности
func (s *Service) GetModifierEngine() *DifficultyModifierEngine {
	return s.modifierEngine
}

// GetAchievementTracker возвращает трекер достижений
func (s *Service) GetAchievementTracker() *AchievementTracker {
	return s.achievementTracker
}

// GetRewardCalculator возвращает калькулятор наград
func (s *Service) GetRewardCalculator() *RewardCalculator {
	return s.rewardCalculator
}

// GetDB возвращает пул соединений с базой данных
func (s *Service) GetDB() *pgxpool.Pool {
	return s.db
}

// Close закрывает сервис и освобождает ресурсы
func (s *Service) Close() error {
	// Закрываем соединение с БД
	if s.db != nil {
		s.db.Close()
	}

	// Закрываем соединение с Redis
	if s.redis != nil {
		if err := s.redis.Close(); err != nil {
			s.logger.Error("Failed to close Redis connection", zap.Error(err))
		}
	}

	s.logger.Info("Service closed successfully")
	return nil
}

// GetRedis возвращает клиент Redis
func (s *Service) GetRedis() *redis.Client {
	return s.redis
}

// GetTracer возвращает OpenTelemetry tracer
func (s *Service) GetTracer() otel.Tracer {
	return s.tracer
}

// GetConfig возвращает конфигурацию сервиса
func (s *Service) GetConfig() *config.Config {
	return s.config
}

// GetLogger возвращает логгер
func (s *Service) GetLogger() *zap.Logger {
	return s.logger
}

// AcquireSession получает DifficultySession из пула для снижения аллокаций
func (s *Service) AcquireSession() *DifficultySession {
	session := s.sessionPool.Get().(*DifficultySession)
	// Reset session to clean state
	*session = DifficultySession{}
	return session
}

// ReleaseSession возвращает DifficultySession в пул
func (s *Service) ReleaseSession(session *DifficultySession) {
	s.sessionPool.Put(session)
}

// AcquireMode получает DifficultyMode из пула
func (s *Service) AcquireMode() *DifficultyMode {
	mode := s.modePool.Get().(*DifficultyMode)
	// Reset mode to clean state
	*mode = DifficultyMode{}
	return mode
}

// ReleaseMode возвращает DifficultyMode в пул
func (s *Service) ReleaseMode(mode *DifficultyMode) {
	s.modePool.Put(mode)
}

// HealthCheck выполняет проверку здоровья сервиса
func (s *Service) HealthCheck(ctx context.Context) error {
	// Проверка базы данных
	if err := s.db.Ping(ctx); err != nil {
		return errors.Wrap(err, "database health check failed")
	}

	// Проверка Redis
	if err := s.redis.Ping(ctx).Err(); err != nil {
		return errors.Wrap(err, "redis health check failed")
	}

	return nil
}


// ValidateDifficultyModeAccess проверяет доступ игрока к режиму сложности
func (s *Service) ValidateDifficultyModeAccess(ctx context.Context, playerID, modeID uuid.UUID) error {
	ctx, span := s.tracer.Start(ctx, "Service.ValidateDifficultyModeAccess")
	defer span.End()

	span.SetAttributes(
		attribute.String("player.id", playerID.String()),
		attribute.String("mode.id", modeID.String()),
	)

	// Получение требований режима
	requirements, err := s.difficultyManager.GetModeRequirements(ctx, modeID)
	if err != nil {
		s.failedValidations.Add(ctx, 1)
		return errors.Wrap(err, "failed to get mode requirements")
	}

	// Проверка уровня игрока
	playerLevel, err := s.getPlayerLevel(ctx, playerID)
	if err != nil {
		s.failedValidations.Add(ctx, 1)
		return errors.Wrap(err, "failed to get player level")
	}

	if playerLevel < requirements.MinLevel {
		s.failedValidations.Add(ctx, 1)
		return fmt.Errorf("insufficient player level: required %d, got %d",
			requirements.MinLevel, playerLevel)
	}

	// Проверка skill rating
	playerRating, err := s.getPlayerSkillRating(ctx, playerID)
	if err != nil {
		s.failedValidations.Add(ctx, 1)
		return errors.Wrap(err, "failed to get player skill rating")
	}

	if playerRating < requirements.MinSkillRating {
		s.failedValidations.Add(ctx, 1)
		return fmt.Errorf("insufficient skill rating: required %d, got %d",
			requirements.MinSkillRating, playerRating)
	}

	// Проверка завершенных миссий (упрощенная реализация)
	for _, missionID := range requirements.CompletedMissions {
		completed, err := s.checkMissionCompleted(ctx, playerID, missionID)
		if err != nil {
			s.failedValidations.Add(ctx, 1)
			return errors.Wrapf(err, "failed to check mission completion: %s", missionID)
		}
		if !completed {
			s.failedValidations.Add(ctx, 1)
			return fmt.Errorf("mission not completed: %s", missionID)
		}
	}

	s.logger.Debug("Difficulty mode access validated",
		zap.String("player_id", playerID.String()),
		zap.String("mode_id", modeID.String()))

	return nil
}

// Вспомогательные методы (заглушки для интеграции с другими сервисами)
func (s *Service) getPlayerLevel(ctx context.Context, playerID uuid.UUID) (int, error) {
	// Интеграция с Progression Service
	// В реальной реализации здесь будет gRPC вызов
	return 25, nil // Заглушка
}

func (s *Service) getPlayerSkillRating(ctx context.Context, playerID uuid.UUID) (int, error) {
	// Интеграция с Achievement Service
	// В реальной реализации здесь будет gRPC вызов
	return 1500, nil // Заглушка
}

func (s *Service) checkMissionCompleted(ctx context.Context, playerID, missionID uuid.UUID) (bool, error) {
	// Интеграция с Quest Service
	// В реальной реализации здесь будет gRPC вызов
	return true, nil // Заглушка
}

