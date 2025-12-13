// Issue: #140875766
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"go.uber.org/zap"
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
func (r *SandevistanRepository) GetSandevistanState(ctx context.Context, userID string) (*SandevistanState, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT user_id, is_active, activation_time, remaining_time, time_dilation,
		       cooldown_remaining, cyberpsychosis_level, heat_level, last_activation
		FROM combat.sandevistan_states
		WHERE user_id = $1
	`

	var state SandevistanState
	var activationTime, lastActivation sql.NullTime

	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&state.UserID, &state.IsActive, &activationTime, &state.RemainingTime,
		&state.TimeDilation, &state.CooldownRemaining, &state.CyberpsychosisLevel,
		&state.HeatLevel, &lastActivation,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Создаем дефолтное состояние для нового пользователя
			return &SandevistanState{
				UserID:             userID,
				IsActive:           false,
				TimeDilation:       1.0,
				CooldownRemaining:  0,
				CyberpsychosisLevel: 0,
				HeatLevel:          0,
			}, nil
		}
		return nil, fmt.Errorf("failed to get state: %w", err)
	}

	if activationTime.Valid {
		state.ActivationTime = &activationTime.Time
	}
	if lastActivation.Valid {
		state.LastActivation = &lastActivation.Time
	}

	return &state, nil
}

// UpdateSandevistanState обновляет состояние Sandevistan
func (r *SandevistanRepository) UpdateSandevistanState(ctx context.Context, state *SandevistanState) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_states (
			user_id, is_active, activation_time, remaining_time, time_dilation,
			cooldown_remaining, cyberpsychosis_level, heat_level, last_activation
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (user_id) DO UPDATE SET
			is_active = EXCLUDED.is_active,
			activation_time = EXCLUDED.activation_time,
			remaining_time = EXCLUDED.remaining_time,
			time_dilation = EXCLUDED.time_dilation,
			cooldown_remaining = EXCLUDED.cooldown_remaining,
			cyberpsychosis_level = EXCLUDED.cyberpsychosis_level,
			heat_level = EXCLUDED.heat_level,
			last_activation = EXCLUDED.last_activation
	`

	_, err := r.db.ExecContext(ctx, query,
		state.UserID, state.IsActive, state.ActivationTime, state.RemainingTime,
		state.TimeDilation, state.CooldownRemaining, state.CyberpsychosisLevel,
		state.HeatLevel, state.LastActivation,
	)

	if err != nil {
		r.logger.Error("Failed to update Sandevistan state",
			zap.String("user_id", state.UserID),
			zap.Error(err))
		return fmt.Errorf("failed to update state: %w", err)
	}

	return nil
}

// GetSandevistanStats получает статистику Sandevistan
func (r *SandevistanRepository) GetSandevistanStats(ctx context.Context, userID string) (*SandevistanStats, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT user_id, level, max_duration, cooldown_time, time_dilation_base,
		       cyberpsychosis_resistance, heat_dissipation_rate, total_activations,
		       total_duration, best_streak, average_duration
		FROM combat.sandevistan_stats
		WHERE user_id = $1
	`

	var stats SandevistanStats
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&stats.UserID, &stats.Level, &stats.MaxDuration, &stats.CooldownTime,
		&stats.TimeDilationBase, &stats.CyberpsychosisResistance, &stats.HeatDissipationRate,
		&stats.TotalActivations, &stats.TotalDuration, &stats.BestStreak, &stats.AverageDuration,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Создаем дефолтную статистику для нового пользователя
			return &SandevistanStats{
				UserID:                 userID,
				Level:                  0,
				MaxDuration:            8.0,
				CooldownTime:           30.0,
				TimeDilationBase:       0.1,
				CyberpsychosisResistance: 0.0,
				HeatDissipationRate:    0.02,
				TotalActivations:       0,
				TotalDuration:          0,
				BestStreak:             0,
				AverageDuration:        0,
			}, nil
		}
		return nil, fmt.Errorf("failed to get stats: %w", err)
	}

	return &stats, nil
}

// UpdateSandevistanStats обновляет статистику Sandevistan
func (r *SandevistanRepository) UpdateSandevistanStats(ctx context.Context, stats *SandevistanStats) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO combat.sandevistan_stats (
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
		stats.UserID, stats.Level, stats.MaxDuration, stats.CooldownTime,
		stats.TimeDilationBase, stats.CyberpsychosisResistance, stats.HeatDissipationRate,
		stats.TotalActivations, stats.TotalDuration, stats.BestStreak, stats.AverageDuration,
	)

	if err != nil {
		r.logger.Error("Failed to update Sandevistan stats",
			zap.String("user_id", stats.UserID),
			zap.Error(err))
		return fmt.Errorf("failed to update stats: %w", err)
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