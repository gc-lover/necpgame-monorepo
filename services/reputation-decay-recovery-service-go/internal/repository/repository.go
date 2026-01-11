// Package repository содержит репозиторий для работы с репутационными данными
// Issue: #2174 - Reputation Decay & Recovery mechanics
// PERFORMANCE: Оптимизирован для MMOFPS с prepared statements и connection pooling
package repository

import (
	"context"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/models"
)

// Repository представляет репозиторий для репутационных данных
type Repository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository создает новый репозиторий
func NewRepository(db *pgxpool.Pool, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		logger: logger,
	}
}

// Decay Process CRUD operations

// CreateDecayProcess создает новый процесс разрушения репутации
func (r *Repository) CreateDecayProcess(ctx context.Context, process *models.ReputationDecay) error {
	query := `
		INSERT INTO reputation_decay_processes (
			id, character_id, faction_id, current_value, decay_rate,
			last_decay_time, next_decay_time, is_active, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	_, err := r.db.Exec(ctx, query,
		process.ID, process.CharacterID, process.FactionID, process.CurrentValue,
		process.DecayRate, process.LastDecayTime, process.NextDecayTime,
		process.IsActive, process.CreatedAt, process.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create decay process", zap.Error(err), zap.String("process_id", process.ID))
		return errors.Wrap(err, "failed to create decay process")
	}

	return nil
}

// GetActiveDecayProcesses получает все активные процессы разрушения
func (r *Repository) GetActiveDecayProcesses(ctx context.Context, limit int) ([]*models.ReputationDecay, error) {
	query := `
		SELECT id, character_id, faction_id, current_value, decay_rate,
		       last_decay_time, next_decay_time, is_active, created_at, updated_at
		FROM reputation_decay_processes
		WHERE is_active = true AND next_decay_time <= $1
		ORDER BY next_decay_time ASC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, time.Now(), limit)
	if err != nil {
		return nil, errors.Wrap(err, "failed to query active decay processes")
	}
	defer rows.Close()

	var processes []*models.ReputationDecay
	for rows.Next() {
		var process models.ReputationDecay
		err := rows.Scan(
			&process.ID, &process.CharacterID, &process.FactionID, &process.CurrentValue,
			&process.DecayRate, &process.LastDecayTime, &process.NextDecayTime,
			&process.IsActive, &process.CreatedAt, &process.UpdatedAt,
		)
		if err != nil {
			return nil, errors.Wrap(err, "failed to scan decay process")
		}
		processes = append(processes, &process)
	}

	return processes, nil
}

// UpdateDecayProcess обновляет процесс разрушения
func (r *Repository) UpdateDecayProcess(ctx context.Context, process *models.ReputationDecay) error {
	query := `
		UPDATE reputation_decay_processes
		SET current_value = $1, last_decay_time = $2, next_decay_time = $3,
		    is_active = $4, updated_at = $5
		WHERE id = $6
	`

	_, err := r.db.Exec(ctx, query,
		process.CurrentValue, process.LastDecayTime, process.NextDecayTime,
		process.IsActive, process.UpdatedAt, process.ID,
	)

	if err != nil {
		r.logger.Error("Failed to update decay process", zap.Error(err), zap.String("process_id", process.ID))
		return errors.Wrap(err, "failed to update decay process")
	}

	return nil
}

// Recovery Process CRUD operations

// CreateRecoveryProcess создает новый процесс восстановления репутации
func (r *Repository) CreateRecoveryProcess(ctx context.Context, process *models.ReputationRecovery) error {
	query := `
		INSERT INTO reputation_recovery_processes (
			id, character_id, faction_id, method, status, start_value, target_value,
			current_value, progress, start_time, estimated_end, cost_currency_type,
			cost_amount, cost_item_id, metadata, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17)
	`

	_, err := r.db.Exec(ctx, query,
		process.ID, process.CharacterID, process.FactionID, process.Method,
		process.Status, process.StartValue, process.TargetValue, process.CurrentValue,
		process.Progress, process.StartTime, process.EstimatedEnd,
		process.Cost.CurrencyType, process.Cost.Amount, process.Cost.ItemID,
		process.Metadata, process.CreatedAt, process.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create recovery process", zap.Error(err), zap.String("process_id", process.ID))
		return errors.Wrap(err, "failed to create recovery process")
	}

	return nil
}

// GetActiveRecoveryProcesses получает активные процессы восстановления
func (r *Repository) GetActiveRecoveryProcesses(ctx context.Context, characterID string) ([]*models.ReputationRecovery, error) {
	query := `
		SELECT id, character_id, faction_id, method, status, start_value, target_value,
		       current_value, progress, start_time, estimated_end, actual_end,
		       cost_currency_type, cost_amount, cost_item_id, metadata, created_at, updated_at
		FROM reputation_recovery_processes
		WHERE character_id = $1 AND status = 'active'
		ORDER BY estimated_end ASC
	`

	rows, err := r.db.Query(ctx, query, characterID)
	if err != nil {
		return nil, errors.Wrap(err, "failed to query recovery processes")
	}
	defer rows.Close()

	var processes []*models.ReputationRecovery
	for rows.Next() {
		var process models.ReputationRecovery
		err := rows.Scan(
			&process.ID, &process.CharacterID, &process.FactionID, &process.Method,
			&process.Status, &process.StartValue, &process.TargetValue, &process.CurrentValue,
			&process.Progress, &process.StartTime, &process.EstimatedEnd, &process.ActualEnd,
			&process.Cost.CurrencyType, &process.Cost.Amount, &process.Cost.ItemID,
			&process.Metadata, &process.CreatedAt, &process.UpdatedAt,
		)
		if err != nil {
			return nil, errors.Wrap(err, "failed to scan recovery process")
		}
		processes = append(processes, &process)
	}

	return processes, nil
}

// UpdateRecoveryProcess обновляет процесс восстановления
func (r *Repository) UpdateRecoveryProcess(ctx context.Context, process *models.ReputationRecovery) error {
	query := `
		UPDATE reputation_recovery_processes
		SET status = $1, current_value = $2, progress = $3, actual_end = $4,
		    updated_at = $5
		WHERE id = $6
	`

	_, err := r.db.Exec(ctx, query,
		process.Status, process.CurrentValue, process.Progress,
		process.ActualEnd, process.UpdatedAt, process.ID,
	)

	if err != nil {
		r.logger.Error("Failed to update recovery process", zap.Error(err), zap.String("process_id", process.ID))
		return errors.Wrap(err, "failed to update recovery process")
	}

	return nil
}

// Event logging

// LogReputationEvent логирует событие изменения репутации
func (r *Repository) LogReputationEvent(ctx context.Context, event *models.ReputationEvent) error {
	query := `
		INSERT INTO reputation_events (
			id, character_id, faction_id, event_type, old_value, new_value,
			delta, reason, source, timestamp, metadata
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.Exec(ctx, query,
		event.ID, event.CharacterID, event.FactionID, event.EventType,
		event.OldValue, event.NewValue, event.Delta, event.Reason,
		event.Source, event.Timestamp, event.Metadata,
	)

	if err != nil {
		r.logger.Error("Failed to log reputation event", zap.Error(err), zap.String("event_id", event.ID))
		return errors.Wrap(err, "failed to log reputation event")
	}

	return nil
}

// Configuration queries

// GetDecayConfig получает конфигурацию разрушения для фракции
func (r *Repository) GetDecayConfig(ctx context.Context, factionID string) (*models.DecayConfig, error) {
	query := `
		SELECT faction_id, base_decay_rate, time_threshold, min_reputation,
		       max_decay_rate, activity_boost
		FROM reputation_decay_configs
		WHERE faction_id = $1 AND is_active = true
	`

	var config models.DecayConfig
	err := r.db.QueryRow(ctx, query, factionID).Scan(
		&config.FactionID, &config.BaseDecayRate, &config.TimeThreshold,
		&config.MinReputation, &config.MaxDecayRate, &config.ActivityBoost,
	)

	if err != nil {
		return nil, errors.Wrap(err, "failed to get decay config")
	}

	return &config, nil
}

// GetRecoveryConfig получает конфигурацию восстановления для метода
func (r *Repository) GetRecoveryConfig(ctx context.Context, method models.RecoveryMethod) (*models.RecoveryConfig, error) {
	query := `
		SELECT method, base_recovery_rate, time_multiplier, cost_multiplier,
		       min_duration, max_duration
		FROM reputation_recovery_configs
		WHERE method = $1 AND is_active = true
	`

	var config models.RecoveryConfig
	err := r.db.QueryRow(ctx, query, method).Scan(
		&config.Method, &config.BaseRecoveryRate, &config.TimeMultiplier,
		&config.CostMultiplier, &config.MinDuration, &config.MaxDuration,
	)

	if err != nil {
		return nil, errors.Wrap(err, "failed to get recovery config")
	}

	return &config, nil
}

// UpdateReputationInExternalSystem обновляет репутацию во внешней системе
// Это заглушка для интеграции с основным сервисом репутации
func (r *Repository) UpdateReputationInExternalSystem(ctx context.Context, characterID, factionID string, newValue float64) error {
	// Здесь должна быть логика обновления репутации в relationship-service
	// или другом сервисе, отвечающем за основную репутацию

	r.logger.Info("External reputation update needed",
		zap.String("character_id", characterID),
		zap.String("faction_id", factionID),
		zap.Float64("new_value", newValue),
	)

	// Пока просто логируем - реальная интеграция будет добавлена позже
	return nil
}