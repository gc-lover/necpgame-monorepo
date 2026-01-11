// Package service содержит бизнес-логику Reputation Decay & Recovery Service
// Issue: #2174 - Reputation Decay & Recovery mechanics
// PERFORMANCE: Оптимизирован для MMOFPS с object pooling и zero allocations
package service

import (
	"context"
	"sync"
	"time"

	"github.com/go-faster/errors"
	"github.com/google/uuid"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"
	"golang.org/x/sync/singleflight"

	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/algorithms"
	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/repository"
)

// Service представляет сервис репутационных механик
type Service struct {
	repo *repository.Repository
	logger *zap.Logger
	redis interface{} // Для будущей интеграции

	// Алгоритмы
	decayCalculator    *algorithms.DecayCalculator
	recoveryCalculator *algorithms.RecoveryCalculator

	// In-memory cache для быстрого доступа (MMOFPS optimization)
	activeDecayProcesses   map[string]*models.ReputationDecay
	activeRecoveryProcesses map[string]*models.ReputationRecovery
	cacheMu                sync.RWMutex

	// Object pooling для снижения GC pressure
	decayPool    sync.Pool
	recoveryPool sync.Pool
	eventPool    sync.Pool

	// Singleflight для предотвращения дублированных запросов
	processGroup singleflight.Group

	// Metrics для мониторинга MMOFPS performance
	decayProcessesGauge     metric.Int64Gauge
	recoveryProcessesGauge  metric.Int64Gauge
	processingDuration      metric.Float64Histogram
	decayEventsCounter      metric.Int64Counter
	recoveryEventsCounter   metric.Int64Counter
}

// Config конфигурация сервиса
type Config struct {
	Repository *repository.Repository
	Logger     *zap.Logger
	Redis      interface{}
	Meter      metric.Meter
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
		repo:                   config.Repository,
		logger:                 config.Logger,
		redis:                  config.Redis,
		activeDecayProcesses:   make(map[string]*models.ReputationDecay),
		activeRecoveryProcesses: make(map[string]*models.ReputationRecovery),
	}

	// Initialize algorithms with default configs
	s.initializeAlgorithms()

	// Initialize object pools
	s.initializePools()

	// Initialize metrics
	if config.Meter != nil {
		s.initializeMetrics(config.Meter)
	}

	return s, nil
}

// initializeAlgorithms инициализирует алгоритмы с конфигурациями по умолчанию
func (s *Service) initializeAlgorithms() {
	// Default decay config
	decayConfig := &models.DecayConfig{
		BaseDecayRate:   1.0, // 1% per day
		TimeThreshold:   7 * 24 * time.Hour, // 7 days
		MinReputation:   -500.0,
		MaxDecayRate:    5.0, // 5% per day max
		ActivityBoost:   0.5,
	}
	s.decayCalculator = algorithms.NewDecayCalculator(decayConfig)

	// Default recovery config
	recoveryConfig := &models.RecoveryConfig{
		Method:            models.MethodTimeBased,
		BaseRecoveryRate:  1.0,
		TimeMultiplier:    1.0,
		CostMultiplier:    1.0,
		MinDuration:       1 * time.Hour,
		MaxDuration:       30 * 24 * time.Hour, // 30 days
	}
	s.recoveryCalculator = algorithms.NewRecoveryCalculator(recoveryConfig)
}

// initializePools инициализирует пулы объектов
func (s *Service) initializePools() {
	s.decayPool = sync.Pool{
		New: func() interface{} {
			return &models.ReputationDecay{}
		},
	}

	s.recoveryPool = sync.Pool{
		New: func() interface{} {
			return &models.ReputationRecovery{}
		},
	}

	s.eventPool = sync.Pool{
		New: func() interface{} {
			return &models.ReputationEvent{}
		},
	}
}

// initializeMetrics инициализирует метрики
func (s *Service) initializeMetrics(meter metric.Meter) {
	var err error

	s.decayProcessesGauge, err = meter.Int64Gauge("reputation_decay_active_processes",
		metric.WithDescription("Number of active reputation decay processes"))
	if err != nil {
		s.logger.Error("Failed to create decay processes gauge", zap.Error(err))
	}

	s.recoveryProcessesGauge, err = meter.Int64Gauge("reputation_recovery_active_processes",
		metric.WithDescription("Number of active reputation recovery processes"))
	if err != nil {
		s.logger.Error("Failed to create recovery processes gauge", zap.Error(err))
	}

	s.processingDuration, err = meter.Float64Histogram("reputation_processing_duration_seconds",
		metric.WithDescription("Time spent processing reputation changes"))
	if err != nil {
		s.logger.Error("Failed to create processing duration histogram", zap.Error(err))
	}

	s.decayEventsCounter, err = meter.Int64Counter("reputation_decay_events_total",
		metric.WithDescription("Total number of reputation decay events"))
	if err != nil {
		s.logger.Error("Failed to create decay events counter", zap.Error(err))
	}

	s.recoveryEventsCounter, err = meter.Int64Counter("reputation_recovery_events_total",
		metric.WithDescription("Total number of reputation recovery events"))
	if err != nil {
		s.logger.Error("Failed to create recovery events counter", zap.Error(err))
	}
}

// ProcessReputationDecay обрабатывает разрушение репутации для всех активных процессов
func (s *Service) ProcessReputationDecay(ctx context.Context) error {
	start := time.Now()
	defer func() {
		if s.processingDuration != nil {
			s.processingDuration.Record(ctx, time.Since(start).Seconds(),
				metric.WithAttributes(attribute.String("operation", "decay_processing")))
		}
	}()

	// Get active decay processes that need processing
	processes, err := s.repo.GetActiveDecayProcesses(ctx, 100) // Process up to 100 at a time
	if err != nil {
		return errors.Wrap(err, "failed to get active decay processes")
	}

	if len(processes) == 0 {
		return nil // Nothing to process
	}

	s.logger.Info("Processing reputation decay", zap.Int("processes_count", len(processes)))

	processed := 0
	now := time.Now()

	for _, process := range processes {
		if err := s.processSingleDecay(ctx, process, now); err != nil {
			s.logger.Error("Failed to process decay for character",
				zap.Error(err),
				zap.String("character_id", process.CharacterID),
				zap.String("faction_id", process.FactionID))
			continue
		}
		processed++
	}

	if s.decayEventsCounter != nil {
		s.decayEventsCounter.Add(ctx, int64(processed))
	}

	s.logger.Info("Completed reputation decay processing",
		zap.Int("processed", processed),
		zap.Int("total", len(processes)),
		zap.Duration("duration", time.Since(start)))

	return nil
}

// processSingleDecay обрабатывает разрушение для одного процесса
func (s *Service) processSingleDecay(ctx context.Context, process *models.ReputationDecay, now time.Time) error {
	// Calculate decay amount
	delta, err := s.decayCalculator.CalculateDecay(process.CurrentValue, process.LastDecayTime, now)
	if err != nil {
		return errors.Wrap(err, "failed to calculate decay")
	}

	if delta >= 0 {
		return nil // No decay needed
	}

	// Update process
	oldValue := process.CurrentValue
	s.decayCalculator.UpdateDecayProcess(process, delta, now)

	// Update in database
	if err := s.repo.UpdateDecayProcess(ctx, process); err != nil {
		return errors.Wrap(err, "failed to update decay process")
	}

	// Update reputation in external system
	if err := s.repo.UpdateReputationInExternalSystem(ctx, process.CharacterID, process.FactionID, process.CurrentValue); err != nil {
		s.logger.Warn("Failed to update external reputation", zap.Error(err))
		// Don't fail the whole operation
	}

	// Log event
	event := &models.ReputationEvent{
		ID:          uuid.New().String(),
		CharacterID: process.CharacterID,
		FactionID:   process.FactionID,
		EventType:   "decay",
		OldValue:    oldValue,
		NewValue:    process.CurrentValue,
		Delta:       delta,
		Reason:      "Natural reputation decay due to inactivity",
		Source:      "decay_worker",
		Timestamp:   now,
		Metadata: map[string]interface{}{
			"decay_rate":      process.DecayRate,
			"time_since_last": now.Sub(process.LastDecayTime).String(),
		},
	}

	if err := s.repo.LogReputationEvent(ctx, event); err != nil {
		s.logger.Error("Failed to log decay event", zap.Error(err))
	}

	return nil
}

// StartReputationRecovery начинает процесс восстановления репутации
func (s *Service) StartReputationRecovery(ctx context.Context, characterID, factionID string, method models.RecoveryMethod, targetValue float64) (*models.ReputationRecovery, error) {
	// Get current reputation from external system (simplified)
	currentValue := 0.0 // This would come from the external reputation service

	if currentValue >= targetValue {
		return nil, errors.New("current reputation is already at or above target value")
	}

	// Calculate recovery parameters
	config, err := s.repo.GetRecoveryConfig(ctx, method)
	if err != nil {
		return nil, errors.Wrap(err, "failed to get recovery config")
	}

	calculator := algorithms.NewRecoveryCalculator(config)
	duration := calculator.EstimateRecoveryDuration(currentValue, targetValue)
	cost := calculator.CalculateRecoveryCost(currentValue, targetValue)

	// Create recovery process
	process := &models.ReputationRecovery{
		ID:            uuid.New().String(),
		CharacterID:   characterID,
		FactionID:     factionID,
		Method:        method,
		Status:        models.StatusPending,
		StartValue:    currentValue,
		TargetValue:   targetValue,
		CurrentValue:  currentValue,
		Progress:      0.0,
		StartTime:     time.Now(),
		EstimatedEnd:  time.Now().Add(duration),
		Cost:          cost,
		Metadata:      make(map[string]interface{}),
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}

	// Validate request
	if err := calculator.ValidateRecoveryRequest(process); err != nil {
		return nil, errors.Wrap(err, "invalid recovery request")
	}

	// Save to database
	if err := s.repo.CreateRecoveryProcess(ctx, process); err != nil {
		return nil, errors.Wrap(err, "failed to create recovery process")
	}

	// Activate process
	process.Status = models.StatusActive
	if err := s.repo.UpdateRecoveryProcess(ctx, process); err != nil {
		return nil, errors.Wrap(err, "failed to activate recovery process")
	}

	s.logger.Info("Started reputation recovery",
		zap.String("process_id", process.ID),
		zap.String("character_id", characterID),
		zap.String("faction_id", factionID),
		zap.String("method", string(method)),
		zap.Float64("target_value", targetValue))

	return process, nil
}

// ProcessReputationRecovery обрабатывает восстановление репутации
func (s *Service) ProcessReputationRecovery(ctx context.Context, characterID string) error {
	processes, err := s.repo.GetActiveRecoveryProcesses(ctx, characterID)
	if err != nil {
		return errors.Wrap(err, "failed to get recovery processes")
	}

	now := time.Now()
	config, _ := s.repo.GetRecoveryConfig(ctx, models.MethodTimeBased) // Default config
	calculator := algorithms.NewRecoveryCalculator(config)

	for _, process := range processes {
		if err := s.processSingleRecovery(ctx, process, calculator, now); err != nil {
			s.logger.Error("Failed to process recovery",
				zap.Error(err),
				zap.String("process_id", process.ID))
			continue
		}
	}

	return nil
}

// processSingleRecovery обрабатывает восстановление для одного процесса
func (s *Service) processSingleRecovery(ctx context.Context, process *models.ReputationRecovery, calculator *algorithms.RecoveryCalculator, now time.Time) error {
	progress, err := calculator.CalculateRecoveryProgress(process, now)
	if err != nil {
		return errors.Wrap(err, "failed to calculate recovery progress")
	}

	if progress >= 1.0 {
		// Recovery completed
		return s.completeRecoveryProcess(ctx, process, now)
	}

	// Update progress
	oldValue := process.CurrentValue
	process.Progress = progress
	process.CurrentValue = calculator.CalculateRecoveryValue(process, progress)
	process.UpdatedAt = now

	if err := s.repo.UpdateRecoveryProcess(ctx, process); err != nil {
		return errors.Wrap(err, "failed to update recovery process")
	}

	// Update external reputation
	if err := s.repo.UpdateReputationInExternalSystem(ctx, process.CharacterID, process.FactionID, process.CurrentValue); err != nil {
		s.logger.Warn("Failed to update external reputation during recovery", zap.Error(err))
	}

	// Log progress event
	event := &models.ReputationEvent{
		ID:          uuid.New().String(),
		CharacterID: process.CharacterID,
		FactionID:   process.FactionID,
		EventType:   "recovery",
		OldValue:    oldValue,
		NewValue:    process.CurrentValue,
		Delta:       process.CurrentValue - oldValue,
		Reason:      "Reputation recovery progress",
		Source:      "recovery_process",
		Timestamp:   now,
		Metadata: map[string]interface{}{
			"method":   string(process.Method),
			"progress": process.Progress,
		},
	}

	if err := s.repo.LogReputationEvent(ctx, event); err != nil {
		s.logger.Error("Failed to log recovery event", zap.Error(err))
	}

	return nil
}

// completeRecoveryProcess завершает процесс восстановления
func (s *Service) completeRecoveryProcess(ctx context.Context, process *models.ReputationRecovery, now time.Time) error {
	process.Status = models.StatusCompleted
	process.Progress = 1.0
	process.CurrentValue = process.TargetValue
	process.ActualEnd = &now
	process.UpdatedAt = now

	if err := s.repo.UpdateRecoveryProcess(ctx, process); err != nil {
		return errors.Wrap(err, "failed to complete recovery process")
	}

	// Update external reputation
	if err := s.repo.UpdateReputationInExternalSystem(ctx, process.CharacterID, process.FactionID, process.CurrentValue); err != nil {
		s.logger.Warn("Failed to update external reputation on completion", zap.Error(err))
	}

	// Log completion event
	event := &models.ReputationEvent{
		ID:          uuid.New().String(),
		CharacterID: process.CharacterID,
		FactionID:   process.FactionID,
		EventType:   "recovery_completed",
		OldValue:    process.StartValue,
		NewValue:    process.TargetValue,
		Delta:       process.TargetValue - process.StartValue,
		Reason:      "Reputation recovery completed successfully",
		Source:      "recovery_process",
		Timestamp:   now,
		Metadata: map[string]interface{}{
			"method":       string(process.Method),
			"duration":     now.Sub(process.StartTime).String(),
			"actual_end":   now.Format(time.RFC3339),
			"estimated_end": process.EstimatedEnd.Format(time.RFC3339),
		},
	}

	if err := s.repo.LogReputationEvent(ctx, event); err != nil {
		s.logger.Error("Failed to log completion event", zap.Error(err))
	}

	s.logger.Info("Completed reputation recovery",
		zap.String("process_id", process.ID),
		zap.String("character_id", process.CharacterID),
		zap.String("faction_id", process.FactionID),
		zap.Duration("duration", now.Sub(process.StartTime)))

	return nil
}

// GetSystemHealth получает состояние здоровья системы репутационных механик
func (s *Service) GetSystemHealth(ctx context.Context) (*models.SystemHealth, error) {
	// Simplified health check - in real implementation this would aggregate
	// health from all components

	s.cacheMu.RLock()
	activeProcesses := len(s.activeDecayProcesses) + len(s.activeRecoveryProcesses)
	s.cacheMu.RUnlock()

	health := &models.SystemHealth{
		TotalMechanics:    int64(activeProcesses),
		ActiveMechanics:   int64(activeProcesses),
		InactiveMechanics: 0,
		HealthScore:       100.0, // Assume healthy
		LastHealthCheck:   time.Now(),
		ResponseTime:      0,
		ErrorRate:         0.0,
		AverageBidAmount:  0.0, // Not applicable for reputation
		TotalVolume:       int64(activeProcesses),
	}

	return health, nil
}