// Package server Issue: #140875766
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"go.uber.org/zap"

	"necpgame/services/sandevistan-service-go/pkg/api"
)

// SandevistanRepository предоставляет доступ к данным Sandevistan
type SandevistanRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewSandevistanRepository создает новый репозиторий
func NewSandevistanRepository(db *sql.DB, logger *zap.Logger) *SandevistanRepository {
	return &SandevistanRepository{
		db:     db,
		logger: logger,
	}
}

// GetSandevistanState получает состояние Sandevistan для пользователя
func (r *SandevistanRepository) GetSandevistanState(ctx context.Context, userID string) (*api.SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT is_active, activation_time, remaining_time, time_dilation,
		       cooldown_remaining, cyberpsychosis_level, heat_level
		FROM sandevistan_states
		WHERE user_id = $1
	`

	var state api.SandevistanState
	var activationTime sql.NullTime

	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&state.IsActive, &activationTime, &state.RemainingTime,
		&state.TimeDilation, &state.CooldownRemaining, &state.CyberpsychosisLevel,
		&state.HeatLevel,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Создаем дефолтное состояние для нового пользователя
			return &api.SandevistanState{
				IsActive:            false,
				TimeDilation:        1.0,
				CooldownRemaining:   0,
				CyberpsychosisLevel: 0,
				HeatLevel:           0,
			}, nil
		}
		return nil, fmt.Errorf("failed to get Sandevistan state: %w", err)
	}

	// Конвертируем sql.NullTime в OptNilDateTime
	if activationTime.Valid {
		state.ActivationTime = api.NewOptNilDateTime(activationTime.Time)
	}

	return &state, nil
}

// GetSandevistanStats получает статистику Sandevistan
func (r *SandevistanRepository) GetSandevistanStats(ctx context.Context, userID string) (*api.SandevistanStats, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT level, max_duration, cooldown_time, time_dilation_base,
		       cyberpsychosis_resistance, heat_dissipation_rate, total_activations,
		       total_duration, best_streak, average_duration
		FROM sandevistan_stats
		WHERE user_id = $1
	`

	var stats api.SandevistanStats
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&stats.Level, &stats.MaxDuration, &stats.CooldownTime,
		&stats.TimeDilationBase, &stats.CyberpsychosisResistance, &stats.HeatDissipationRate,
		&stats.TotalActivations, &stats.TotalDuration, &stats.BestStreak, &stats.AverageDuration,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Создаем дефолтную статистику для нового пользователя
			return &api.SandevistanStats{
				Level:                    0,
				MaxDuration:              8.0,
				CooldownTime:             30.0,
				TimeDilationBase:         0.1,
				CyberpsychosisResistance: 0.0,
				HeatDissipationRate:      0.02,
				TotalActivations:         0,
				TotalDuration:            0,
				BestStreak:               0,
				AverageDuration:          0,
			}, nil
		}
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	return &stats, nil
}

// GetSandevistanStatsInline получает статистику и заполняет существующий объект (zero allocations)
func (r *SandevistanRepository) GetSandevistanStatsInline(ctx context.Context, userID string, stats *api.SandevistanStats) error {
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	query := `
		SELECT level, max_duration, cooldown_time, time_dilation_base,
		       cyberpsychosis_resistance, heat_dissipation_rate, total_activations,
		       total_duration, best_streak, average_duration
		FROM combat.sandevistan_stats
		WHERE user_id = $1
	`

	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&stats.Level, &stats.MaxDuration, &stats.CooldownTime,
		&stats.TimeDilationBase, &stats.CyberpsychosisResistance, &stats.HeatDissipationRate,
		&stats.TotalActivations, &stats.TotalDuration, &stats.BestStreak, &stats.AverageDuration,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Заполняем дефолтными значениями
			stats.Level = 0
			stats.MaxDuration = 8.0
			stats.CooldownTime = 30.0
			stats.TimeDilationBase = 0.1
			stats.CyberpsychosisResistance = 0.0
			stats.HeatDissipationRate = 0.02
			stats.TotalActivations = 0
			stats.TotalDuration = 0
			stats.BestStreak = 0
			stats.AverageDuration = 0
			return nil
		}
		return fmt.Errorf("failed to get stats inline: %w", err)
	}

	return nil
}

// IncrementActivationCount увеличивает счетчик активаций и обновляет статистику
func (r *SandevistanRepository) IncrementActivationCount(ctx context.Context, userID string, duration float64) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		UPDATE combat.sandevistan_stats
		SET total_activations = total_activations + 1,
		    total_duration = total_duration + $2,
		    average_duration = (total_duration + $2) / (total_activations + 1)
		WHERE user_id = $1
	`

	_, err := r.db.ExecContext(ctx, query, userID, duration)
	if err != nil {
		r.logger.Error("Failed to increment activation count",
			zap.String("user_id", userID),
			zap.Float64("duration", duration),
			zap.Error(err))
		return fmt.Errorf("failed to increment activation count: %w", err)
	}

	return nil
}

// Методы для Temporal Marks

// AddTemporalMark добавляет метку на цель
func (r *SandevistanRepository) AddTemporalMark(ctx context.Context, activationID, targetID, targetType string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_temporal_marks (
			activation_id, target_id, target_type, marked_at, created_at
		) VALUES ($1, $2, $3, $4, $5)
		ON CONFLICT (activation_id, target_id) DO NOTHING
	`

	_, err := r.db.ExecContext(ctx, query, activationID, targetID, targetType, time.Now(), time.Now())
	if err != nil {
		r.logger.Error("Failed to add temporal mark",
			zap.String("activation_id", activationID),
			zap.String("target_id", targetID),
			zap.Error(err))
		return fmt.Errorf("failed to add temporal mark: %w", err)
	}

	return nil
}

// GetTemporalMarks получает метки для активации
func (r *SandevistanRepository) GetTemporalMarks(ctx context.Context, activationID string) ([]*TemporalMark, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT target_id, target_type, marked_at
		FROM combat.sandevistan_temporal_marks
		WHERE activation_id = $1 AND applied_at IS NULL
		ORDER BY marked_at ASC
		LIMIT 3
	`

	rows, err := r.db.QueryContext(ctx, query, activationID)
	if err != nil {
		return nil, fmt.Errorf("failed to query temporal marks: %w", err)
	}
	defer rows.Close()

	var marks []*TemporalMark
	for rows.Next() {
		var mark TemporalMark
		if err := rows.Scan(&mark.TargetID, &mark.TargetType, &mark.MarkedAt); err != nil {
			return nil, fmt.Errorf("failed to scan temporal mark: %w", err)
		}
		marks = append(marks, &mark)
	}

	return marks, nil
}

// MarkTemporalMarkApplied помечает метку как примененную
func (r *SandevistanRepository) MarkTemporalMarkApplied(ctx context.Context, activationID, targetID string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		UPDATE combat.sandevistan_temporal_marks
		SET applied_at = $3, updated_at = $3
		WHERE activation_id = $1 AND target_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, activationID, targetID, time.Now())
	if err != nil {
		r.logger.Error("Failed to mark temporal mark as applied",
			zap.String("activation_id", activationID),
			zap.String("target_id", targetID),
			zap.Error(err))
		return fmt.Errorf("failed to mark temporal mark: %w", err)
	}

	return nil
}

// Методы для Action Priority Budget

// GetActionBudget получает бюджет действий пользователя
func (r *SandevistanRepository) GetActionBudget(ctx context.Context, userID string) (*ActionBudget, error) {
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	query := `
		SELECT user_id, remaining_actions, max_actions, last_reset_time
		FROM combat.sandevistan_action_budgets
		WHERE user_id = $1
	`

	var budget ActionBudget
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&budget.UserID, &budget.RemainingSlots,
		&budget.MaxSlots, &budget.ResetTime,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			// Создаем дефолтный бюджет
			budget = ActionBudget{
				UserID:         userID,
				RemainingSlots: 3,
				MaxSlots:       3,
				ResetTime:      time.Now(),
			}
			return &budget, nil
		}
		return nil, fmt.Errorf("failed to get action budget: %w", err)
	}

	return &budget, nil
}

// UpdateActionBudget обновляет бюджет действий
func (r *SandevistanRepository) UpdateActionBudget(ctx context.Context, budget *ActionBudget) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_action_budgets (
			user_id, remaining_actions, max_actions, last_reset_time, created_at
		) VALUES ($1, $2, $3, $4, $5)
		ON CONFLICT (user_id) DO UPDATE SET
			remaining_actions = EXCLUDED.remaining_actions,
			max_actions = EXCLUDED.max_actions,
			last_reset_time = EXCLUDED.last_reset_time,
			updated_at = EXCLUDED.created_at
	`

	_, err := r.db.ExecContext(ctx, query,
		budget.UserID, budget.RemainingSlots, budget.MaxSlots,
		budget.ResetTime, time.Now(),
	)

	if err != nil {
		r.logger.Error("Failed to update action budget",
			zap.String("user_id", budget.UserID),
			zap.Error(err))
		return fmt.Errorf("failed to update action budget: %w", err)
	}

	return nil
}

// LogActionConsumption логирует использование действия
func (r *SandevistanRepository) LogActionConsumption(ctx context.Context, userID, actionType, targetID string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_action_logs (
			user_id, action_type, target_id, consumed_at, created_at
		) VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.ExecContext(ctx, query, userID, actionType, targetID, time.Now(), time.Now())
	if err != nil {
		return fmt.Errorf("failed to log action consumption: %w", err)
	}

	return nil
}

// MarkWindowProcessed помечает MicroTick Window как обработанное
func (r *SandevistanRepository) MarkWindowProcessed(ctx context.Context, window *MicroTickWindow) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_microtick_windows (
			user_id, actions, timestamp, processed, created_at
		) VALUES ($1, $2, $3, $4, $5)
	`

	actionsJSON, _ := json.Marshal(window.Actions)

	_, err := r.db.ExecContext(ctx, query,
		window.UserID, actionsJSON, window.StartTime, window.Processed, time.Now())

	if err != nil {
		return fmt.Errorf("failed to mark window processed: %w", err)
	}

	return nil
}

// Методы для Counterplay

// LogCounterplay логирует применение контрплея
func (r *SandevistanRepository) LogCounterplay(ctx context.Context, userID, counterplayType, appliedBy string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_counterplay_logs (
			user_id, counterplay_type, applied_by, applied_at, created_at
		) VALUES ($1, $2, $3, $4, $5)
	`

	_, err := r.db.ExecContext(ctx, query, userID, counterplayType, appliedBy, time.Now(), time.Now())
	if err != nil {
		r.logger.Error("Failed to log counterplay",
			zap.String("user_id", userID),
			zap.String("counterplay_type", counterplayType),
			zap.Error(err))
		return fmt.Errorf("failed to log counterplay: %w", err)
	}

	return nil
}

// SaveSandevistanState сохраняет состояние Sandevistan
func (r *SandevistanRepository) SaveSandevistanState(ctx context.Context, userID string, state *api.SandevistanState) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO sandevistan_states (
			user_id, is_active, activation_time, remaining_time, time_dilation,
			cooldown_remaining, cyberpsychosis_level, heat_level
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		ON CONFLICT (user_id) DO UPDATE SET
			is_active = EXCLUDED.is_active,
			activation_time = EXCLUDED.activation_time,
			remaining_time = EXCLUDED.remaining_time,
			time_dilation = EXCLUDED.time_dilation,
			cooldown_remaining = EXCLUDED.cooldown_remaining,
			cyberpsychosis_level = EXCLUDED.cyberpsychosis_level,
			heat_level = EXCLUDED.heat_level
	`

	_, err := r.db.ExecContext(ctx, query,
		userID, state.IsActive, state.ActivationTime, state.RemainingTime,
		state.TimeDilation, state.CooldownRemaining, state.CyberpsychosisLevel,
		state.HeatLevel,
	)

	if err != nil {
		return fmt.Errorf("failed to save Sandevistan state: %w", err)
	}

	return nil
}

// SaveSandevistanStats сохраняет статистику Sandevistan
func (r *SandevistanRepository) SaveSandevistanStats(ctx context.Context, userID string, stats *api.SandevistanStats) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO sandevistan_stats (
			user_id, level, max_duration, cooldown_time, time_dilation_base,
			cyberpsychosis_resistance, heat_dissipation_rate, total_activations,
			total_duration, best_streak, average_duration
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
		ON CONFLICT (user_id) DO UPDATE SET
			level = EXCLUDED.level,
			max_duration = EXCLUDED.max_duration,
			cooldown_time = EXCLUDED.cooldown_time,
			time_dilation_base = EXCLUDED.time_dilation_base,
			cyberpsychosis_resistance = EXCLUDED.cyberpsychosis_resistance,
			heat_dissipation_rate = EXCLUDED.heat_dissipation_rate,
			total_activations = EXCLUDED.total_activations,
			total_duration = EXCLUDED.total_duration,
			best_streak = EXCLUDED.best_streak,
			average_duration = EXCLUDED.average_duration
	`

	_, err := r.db.ExecContext(ctx, query,
		userID, stats.Level, stats.MaxDuration, stats.CooldownTime, stats.TimeDilationBase,
		stats.CyberpsychosisResistance, stats.HeatDissipationRate, stats.TotalActivations,
		stats.TotalDuration, stats.BestStreak, stats.AverageDuration,
	)

	if err != nil {
		return fmt.Errorf("failed to save Sandevistan stats: %w", err)
	}

	return nil
}

// SaveCounterplayRecord сохраняет запись о контроле
func (r *SandevistanRepository) SaveCounterplayRecord() error {
	// TODO: Implement counterplay record save logic
	return fmt.Errorf("SaveCounterplayRecord not implemented")
}

// GetActiveCounterplays получает активные контрмеры
func (r *SandevistanRepository) GetActiveCounterplays() ([]interface{}, error) {
	// TODO: Implement active counterplays retrieval
	return nil, fmt.Errorf("GetActiveCounterplays not implemented")
}

// RemoveCounterplayEffect удаляет эффект контрмеры
func (r *SandevistanRepository) RemoveCounterplayEffect() error {
	// TODO: Implement counterplay effect removal
	return fmt.Errorf("RemoveCounterplayEffect not implemented")
}

// SaveTemporalMark сохраняет временную метку
func (r *SandevistanRepository) SaveTemporalMark() error {
	// TODO: Implement temporal mark save logic
	return fmt.Errorf("SaveTemporalMark not implemented")
}

// DeleteTemporalMarks удаляет временные метки
func (r *SandevistanRepository) DeleteTemporalMarks() error {
	// TODO: Implement temporal marks deletion
	return fmt.Errorf("DeleteTemporalMarks not implemented")
}

// MarkMicroTickWindowProcessed отмечает окно микротиков как обработанное
func (r *SandevistanRepository) MarkMicroTickWindowProcessed() error {
	// TODO: Implement micro tick window processing mark
	return fmt.Errorf("MarkMicroTickWindowProcessed not implemented")
}

// SaveActionBudget сохраняет бюджет действий
func (r *SandevistanRepository) SaveActionBudget(ctx context.Context, budget *ActionBudget) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO sandevistan_action_budgets (
			user_id, remaining_slots, max_slots, reset_time, last_updated
		) VALUES ($1, $2, $3, $4, $5)
		ON CONFLICT (user_id) DO UPDATE SET
			remaining_slots = EXCLUDED.remaining_slots,
			max_slots = EXCLUDED.max_slots,
			reset_time = EXCLUDED.reset_time,
			last_updated = EXCLUDED.last_updated
	`

	_, err := r.db.ExecContext(ctx, query,
		budget.UserID, budget.RemainingSlots, budget.MaxSlots,
		budget.ResetTime, budget.LastUpdated,
	)

	if err != nil {
		return fmt.Errorf("failed to save action budget: %w", err)
	}

	return nil
}
