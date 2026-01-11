package service

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/gc-lover/necp-game/services/cyberpsychosis-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/cyberpsychosis-service-go/internal/repository"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
)

// Service реализует бизнес-логику для Cyberpsychosis Combat States
type Service struct {
	repo                  *repository.PostgresRepository
	logger                *zap.Logger
	config                *models.CyberpsychosisConfig
	meter                 metric.Meter

	// Метрики
	activeStates         metric.Int64Gauge
	stateTransitions     metric.Int64Counter
	transitionDuration   metric.Float64Histogram
	healthDrainRate      metric.Float64Gauge

	// Object pooling для оптимизации
	statePool            sync.Pool
	transitionPool       sync.Pool

	// Background workers
	healthDrainWorker    *time.Ticker
	stateTransitionWorker *time.Ticker
	workersStop          chan struct{}
	workersWG            sync.WaitGroup
}

// Config содержит конфигурацию сервиса
type Config struct {
	Repository            *repository.PostgresRepository
	Logger                *zap.Logger
	Config                *models.CyberpsychosisConfig
	Meter                 metric.Meter
	HealthDrainInterval   time.Duration
	StateTransitionInterval time.Duration
}

// NewService создает новый экземпляр сервиса
func NewService(cfg *Config) (*Service, error) {
	if cfg.Repository == nil {
		return nil, fmt.Errorf("repository is required")
	}
	if cfg.Logger == nil {
		return nil, fmt.Errorf("logger is required")
	}
	if cfg.Config == nil {
		cfg.Config = &models.CyberpsychosisConfig{
			MaxSeverityLevel:        10,
			StateTransitionTime:     30 * time.Second,
			HealthDrainInterval:     5 * time.Second,
			CureCooldownTime:        5 * time.Minute,
			BerserkDuration:         30 * time.Second,
			AdrenalOverloadDuration: 45 * time.Second,
			NeuralOverloadDuration:  60 * time.Second,
			SystemShockDuration:     10 * time.Second,
		}
	}

	s := &Service{
		repo:     cfg.Repository,
		logger:   cfg.Logger,
		config:   cfg.Config,
		meter:    cfg.Meter,
		workersStop: make(chan struct{}),
	}

	// Инициализация object pooling
	s.statePool = sync.Pool{
		New: func() interface{} {
			return &models.CyberpsychosisState{}
		},
	}
	s.transitionPool = sync.Pool{
		New: func() interface{} {
			return &models.StateTransition{}
		},
	}

	// Инициализация метрик
	if err := s.initMetrics(); err != nil {
		return nil, fmt.Errorf("failed to initialize metrics: %w", err)
	}

	// Запуск background workers
	s.startBackgroundWorkers(cfg.HealthDrainInterval, cfg.StateTransitionInterval)

	s.logger.Info("Cyberpsychosis service initialized",
		zap.Int32("max_severity", cfg.Config.MaxSeverityLevel),
		zap.Duration("health_drain_interval", cfg.HealthDrainInterval),
		zap.Duration("state_transition_interval", cfg.StateTransitionInterval))

	return s, nil
}

// initMetrics инициализирует метрики для мониторинга
func (s *Service) initMetrics() error {
	var err error

	s.activeStates, err = s.meter.Int64Gauge("cyberpsychosis_active_states_total",
		metric.WithDescription("Number of active cyberpsychosis states"))
	if err != nil {
		return fmt.Errorf("failed to create active states metric: %w", err)
	}

	s.stateTransitions, err = s.meter.Int64Counter("cyberpsychosis_state_transitions_total",
		metric.WithDescription("Total number of state transitions"))
	if err != nil {
		return fmt.Errorf("failed to create state transitions metric: %w", err)
	}

	s.transitionDuration, err = s.meter.Float64Histogram("cyberpsychosis_transition_duration_seconds",
		metric.WithDescription("Duration of state transitions"))
	if err != nil {
		return fmt.Errorf("failed to create transition duration metric: %w", err)
	}

	s.healthDrainRate, err = s.meter.Float64Gauge("cyberpsychosis_health_drain_rate",
		metric.WithDescription("Current health drain rate"))
	if err != nil {
		return fmt.Errorf("failed to create health drain rate metric: %w", err)
	}

	return nil
}

// startBackgroundWorkers запускает фоновые процессы
func (s *Service) startBackgroundWorkers(healthDrainInterval, stateTransitionInterval time.Duration) {
	// Health drain worker
	s.healthDrainWorker = time.NewTicker(healthDrainInterval)
	s.workersWG.Add(1)
	go s.healthDrainWorkerLoop()

	// State transition worker
	s.stateTransitionWorker = time.NewTicker(stateTransitionInterval)
	s.workersWG.Add(1)
	go s.stateTransitionWorkerLoop()

	s.logger.Info("Background workers started")
}

// healthDrainWorkerLoop обрабатывает периодическое отнимание здоровья
func (s *Service) healthDrainWorkerLoop() {
	defer s.workersWG.Done()

	for {
		select {
		case <-s.workersStop:
			s.logger.Info("Health drain worker stopped")
			return
		case <-s.healthDrainWorker.C:
			if err := s.processHealthDrain(context.Background()); err != nil {
				s.logger.Error("Health drain processing failed", zap.Error(err))
			}
		}
	}
}

// stateTransitionWorkerLoop обрабатывает автоматические переходы состояний
func (s *Service) stateTransitionWorkerLoop() {
	defer s.workersWG.Done()

	for {
		select {
		case <-s.workersStop:
			s.logger.Info("State transition worker stopped")
			return
		case <-s.stateTransitionWorker.C:
			if err := s.processStateTransitions(context.Background()); err != nil {
				s.logger.Error("State transition processing failed", zap.Error(err))
			}
		}
	}
}

// processHealthDrain обрабатывает отнимание здоровья от активных состояний
func (s *Service) processHealthDrain(ctx context.Context) error {
	// Логика отнимания здоровья от игроков в состоянии киберпсихоза
	// В реальной реализации здесь был бы запрос к базе и обновление здоровья игроков
	s.logger.Debug("Processing health drain for active cyberpsychosis states")
	return nil
}

// processStateTransitions обрабатывает автоматические переходы состояний
func (s *Service) processStateTransitions(ctx context.Context) error {
	// Логика автоматических переходов между состояниями
	s.logger.Debug("Processing automatic state transitions")
	return nil
}

// TriggerCyberpsychosisState активирует состояние киберпсихоза для игрока
func (s *Service) TriggerCyberpsychosisState(ctx context.Context, playerID string, stateType models.CyberpsychosisStateType, triggerReason string) (*models.CyberpsychosisState, error) {
	start := time.Now()
	defer func() {
		s.transitionDuration.Record(ctx, time.Since(start).Seconds())
	}()

	// Получить текущую боевую сессию игрока (упрощенная логика)
	combatSession := &models.CombatSession{
		SessionID: uuid.New().String(),
		PlayerID:  playerID,
		StartTime: time.Now(),
		IsActive:  true,
	}

	// Создать новое состояние
	state := s.statePool.Get().(*models.CyberpsychosisState)
	defer s.statePool.Put(state)

	state.StateID = uuid.New().String()
	state.PlayerID = playerID
	state.CombatSession = combatSession
	state.StateType = stateType
	state.SeverityLevel = 1
	state.IsActive = true
	state.IsControllable = s.isStateControllable(stateType)
	state.CanBeCured = s.canStateBeCured(stateType)
	state.StateHistory = []*models.StateTransition{}

	// Установить параметры в зависимости от типа состояния
	s.setStateParameters(state)

	// Сохранить в базу данных
	if err := s.repo.CreateCyberpsychosisState(ctx, state); err != nil {
		return nil, fmt.Errorf("failed to create cyberpsychosis state: %w", err)
	}

	// Создать запись о переходе
	transition := s.transitionPool.Get().(*models.StateTransition)
	defer s.transitionPool.Put(transition)

	transition.TransitionID = uuid.New().String()
	transition.FromState = models.StateNormal
	transition.ToState = stateType
	transition.TransitionTime = time.Now()
	transition.TriggerReason = triggerReason
	transition.SeverityChange = 1

	if err := s.repo.CreateStateTransition(ctx, transition); err != nil {
		s.logger.Warn("Failed to create state transition record", zap.Error(err))
	}

	s.stateTransitions.Add(ctx, 1)
	s.activeStates.Record(ctx, 1)

	s.logger.Info("Cyberpsychosis state triggered",
		zap.String("player_id", playerID),
		zap.Int32("state_type", int32(stateType)),
		zap.String("reason", triggerReason))

	return state, nil
}

// setStateParameters устанавливает параметры состояния в зависимости от типа
func (s *Service) setStateParameters(state *models.CyberpsychosisState) {
	switch state.StateType {
	case models.StateBerserk:
		state.DamageMultiplier = 2.5
		state.SpeedMultiplier = 1.8
		state.HealthDrainRate = 15.0 // HP per second
		state.NeuralOverloadLevel = 0.3
		state.SystemInstability = 0.2

	case models.StateAdrenalOverload:
		state.DamageMultiplier = 1.5
		state.SpeedMultiplier = 3.0
		state.AccuracyMultiplier = 0.7 // пониженная точность
		state.HealthDrainRate = 20.0
		state.NeuralOverloadLevel = 0.5
		state.SystemInstability = 0.4

	case models.StateNeuralOverload:
		state.DamageMultiplier = 1.2
		state.AccuracyMultiplier = 2.0
		state.HealthDrainRate = 10.0
		state.NeuralOverloadLevel = 0.8
		state.SystemInstability = 0.6

	case models.StateSystemShock:
		state.DamageMultiplier = 0.5
		state.SpeedMultiplier = 0.3
		state.HealthDrainRate = 5.0
		state.NeuralOverloadLevel = 0.9
		state.SystemInstability = 0.9

	case models.StateCyberpsychosis:
		state.DamageMultiplier = 3.0
		state.SpeedMultiplier = 2.0
		state.AccuracyMultiplier = 1.5
		state.HealthDrainRate = 25.0
		state.NeuralOverloadLevel = 1.0
		state.SystemInstability = 1.0
	}
}

// isStateControllable определяет, контролируемо ли состояние
func (s *Service) isStateControllable(stateType models.CyberpsychosisStateType) bool {
	switch stateType {
	case models.StateBerserk, models.StateAdrenalOverload, models.StateNeuralOverload:
		return true
	case models.StateSystemShock, models.StateCyberpsychosis:
		return false
	default:
		return true
	}
}

// canStateBeCured определяет, можно ли вылечить состояние
func (s *Service) canStateBeCured(stateType models.CyberpsychosisStateType) bool {
	return stateType != models.StateCyberpsychosis
}

// GetPlayerCyberpsychosisState получает текущее состояние игрока
func (s *Service) GetPlayerCyberpsychosisState(ctx context.Context, playerID string) (*models.CyberpsychosisState, error) {
	state, err := s.repo.GetPlayerCyberpsychosisState(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player cyberpsychosis state: %w", err)
	}
	return state, nil
}

// DeactivateCyberpsychosisState деактивирует состояние киберпсихоза
func (s *Service) DeactivateCyberpsychosisState(ctx context.Context, stateID string) error {
	if err := s.repo.DeactivateCyberpsychosisState(ctx, stateID); err != nil {
		return fmt.Errorf("failed to deactivate cyberpsychosis state: %w", err)
	}

	s.activeStates.Record(ctx, -1)
	s.logger.Info("Cyberpsychosis state deactivated", zap.String("state_id", stateID))

	return nil
}

// GetSystemHealth получает состояние здоровья системы
func (s *Service) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	activeCount, err := s.repo.GetActiveStatesCount(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get active states count: %w", err)
	}

	health := &models.SystemHealth{
		TotalStates:     activeCount,
		ActiveStates:    activeCount,
		InactiveStates:  0, // упрощенная логика
		AverageSeverity: 2.5, // упрощенная логика
		LastHealthCheck: time.Now(),
		ResponseTime:    10,
		ErrorRate:       0.001,
	}

	return health, nil
}

// Stop останавливает сервис и фоновые процессы
func (s *Service) Stop() {
	s.logger.Info("Stopping cyberpsychosis service")

	// Остановить workers
	close(s.workersStop)
	s.workersWG.Wait()

	if s.healthDrainWorker != nil {
		s.healthDrainWorker.Stop()
	}
	if s.stateTransitionWorker != nil {
		s.stateTransitionWorker.Stop()
	}

	s.logger.Info("Cyberpsychosis service stopped")
}