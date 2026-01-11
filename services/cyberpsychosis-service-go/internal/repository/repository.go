package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necp-game/services/cyberpsychosis-service-go/internal/models"
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

// CreateCyberpsychosisState создает новое состояние киберпсихоза
func (r *PostgresRepository) CreateCyberpsychosisState(ctx context.Context, state *models.CyberpsychosisState) error {
	query := `
		INSERT INTO cyberpsychosis.states (
			state_id, player_id, state_type, severity_level, is_active,
			damage_multiplier, speed_multiplier, accuracy_multiplier,
			health_drain_rate, neural_overload_level, system_instability,
			is_controllable, can_be_cured
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)`

	_, err := r.db.Exec(ctx, query,
		state.StateID, state.PlayerID, state.StateType, state.SeverityLevel, state.IsActive,
		state.DamageMultiplier, state.SpeedMultiplier, state.AccuracyMultiplier,
		state.HealthDrainRate, state.NeuralOverloadLevel, state.SystemInstability,
		state.IsControllable, state.CanBeCured)

	if err != nil {
		r.logger.Error("Failed to create cyberpsychosis state", zap.Error(err))
		return fmt.Errorf("failed to create cyberpsychosis state: %w", err)
	}

	r.logger.Info("Cyberpsychosis state created", zap.String("state_id", state.StateID))
	return nil
}

// GetCyberpsychosisState получает состояние киберпсихоза по ID
func (r *PostgresRepository) GetCyberpsychosisState(ctx context.Context, stateID string) (*models.CyberpsychosisState, error) {
	query := `
		SELECT state_id, player_id, state_type, severity_level, is_active,
			   damage_multiplier, speed_multiplier, accuracy_multiplier,
			   health_drain_rate, neural_overload_level, system_instability,
			   is_controllable, can_be_cured
		FROM cyberpsychosis.states
		WHERE state_id = $1`

	var state models.CyberpsychosisState
	err := r.db.QueryRow(ctx, query, stateID).Scan(
		&state.StateID, &state.PlayerID, &state.StateType, &state.SeverityLevel, &state.IsActive,
		&state.DamageMultiplier, &state.SpeedMultiplier, &state.AccuracyMultiplier,
		&state.HealthDrainRate, &state.NeuralOverloadLevel, &state.SystemInstability,
		&state.IsControllable, &state.CanBeCured)

	if err != nil {
		r.logger.Error("Failed to get cyberpsychosis state", zap.String("state_id", stateID), zap.Error(err))
		return nil, fmt.Errorf("failed to get cyberpsychosis state: %w", err)
	}

	return &state, nil
}

// GetPlayerCyberpsychosisState получает активное состояние киберпсихоза игрока
func (r *PostgresRepository) GetPlayerCyberpsychosisState(ctx context.Context, playerID string) (*models.CyberpsychosisState, error) {
	query := `
		SELECT state_id, player_id, state_type, severity_level, is_active,
			   damage_multiplier, speed_multiplier, accuracy_multiplier,
			   health_drain_rate, neural_overload_level, system_instability,
			   is_controllable, can_be_cured
		FROM cyberpsychosis.states
		WHERE player_id = $1 AND is_active = true
		ORDER BY severity_level DESC
		LIMIT 1`

	var state models.CyberpsychosisState
	err := r.db.QueryRow(ctx, query, playerID).Scan(
		&state.StateID, &state.PlayerID, &state.StateType, &state.SeverityLevel, &state.IsActive,
		&state.DamageMultiplier, &state.SpeedMultiplier, &state.AccuracyMultiplier,
		&state.HealthDrainRate, &state.NeuralOverloadLevel, &state.SystemInstability,
		&state.IsControllable, &state.CanBeCured)

	if err != nil {
		r.logger.Error("Failed to get player cyberpsychosis state", zap.String("player_id", playerID), zap.Error(err))
		return nil, fmt.Errorf("failed to get player cyberpsychosis state: %w", err)
	}

	return &state, nil
}

// UpdateCyberpsychosisState обновляет состояние киберпсихоза
func (r *PostgresRepository) UpdateCyberpsychosisState(ctx context.Context, state *models.CyberpsychosisState) error {
	query := `
		UPDATE cyberpsychosis.states SET
			state_type = $3, severity_level = $4, is_active = $5,
			damage_multiplier = $6, speed_multiplier = $7, accuracy_multiplier = $8,
			health_drain_rate = $9, neural_overload_level = $10, system_instability = $11,
			is_controllable = $12, can_be_cured = $13
		WHERE state_id = $1 AND player_id = $2`

	result, err := r.db.Exec(ctx, query,
		state.StateID, state.PlayerID, state.StateType, state.SeverityLevel, state.IsActive,
		state.DamageMultiplier, state.SpeedMultiplier, state.AccuracyMultiplier,
		state.HealthDrainRate, state.NeuralOverloadLevel, state.SystemInstability,
		state.IsControllable, state.CanBeCured)

	if err != nil {
		r.logger.Error("Failed to update cyberpsychosis state", zap.String("state_id", state.StateID), zap.Error(err))
		return fmt.Errorf("failed to update cyberpsychosis state: %w", err)
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("Cyberpsychosis state not found for update", zap.String("state_id", state.StateID))
		return fmt.Errorf("cyberpsychosis state not found")
	}

	return nil
}

// DeactivateCyberpsychosisState деактивирует состояние киберпсихоза
func (r *PostgresRepository) DeactivateCyberpsychosisState(ctx context.Context, stateID string) error {
	query := `UPDATE cyberpsychosis.states SET is_active = false WHERE state_id = $1`

	result, err := r.db.Exec(ctx, query, stateID)
	if err != nil {
		r.logger.Error("Failed to deactivate cyberpsychosis state", zap.String("state_id", stateID), zap.Error(err))
		return fmt.Errorf("failed to deactivate cyberpsychosis state: %w", err)
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("Cyberpsychosis state not found for deactivation", zap.String("state_id", stateID))
		return fmt.Errorf("cyberpsychosis state not found")
	}

	r.logger.Info("Cyberpsychosis state deactivated", zap.String("state_id", stateID))
	return nil
}

// CreateStateTransition создает запись о переходе состояния
func (r *PostgresRepository) CreateStateTransition(ctx context.Context, transition *models.StateTransition) error {
	query := `
		INSERT INTO cyberpsychosis.state_transitions (
			transition_id, state_id, from_state, to_state, transition_time,
			trigger_reason, severity_change
		) VALUES ($1, $2, $3, $4, $5, $6, $7)`

	_, err := r.db.Exec(ctx, query,
		transition.TransitionID, transition.TransitionID, transition.FromState,
		transition.ToState, transition.TransitionTime, transition.TriggerReason, transition.SeverityChange)

	if err != nil {
		r.logger.Error("Failed to create state transition", zap.String("transition_id", transition.TransitionID), zap.Error(err))
		return fmt.Errorf("failed to create state transition: %w", err)
	}

	return nil
}

// GetStateTransitions получает историю переходов для состояния
func (r *PostgresRepository) GetStateTransitions(ctx context.Context, stateID string, limit int) ([]*models.StateTransition, error) {
	query := `
		SELECT transition_id, state_id, from_state, to_state, transition_time,
			   trigger_reason, severity_change
		FROM cyberpsychosis.state_transitions
		WHERE state_id = $1
		ORDER BY transition_time DESC
		LIMIT $2`

	rows, err := r.db.Query(ctx, query, stateID, limit)
	if err != nil {
		r.logger.Error("Failed to get state transitions", zap.String("state_id", stateID), zap.Error(err))
		return nil, fmt.Errorf("failed to get state transitions: %w", err)
	}
	defer rows.Close()

	var transitions []*models.StateTransition
	for rows.Next() {
		var transition models.StateTransition
		err := rows.Scan(
			&transition.TransitionID, &transition.TransitionID, &transition.FromState,
			&transition.ToState, &transition.TransitionTime, &transition.TriggerReason, &transition.SeverityChange)
		if err != nil {
			r.logger.Error("Failed to scan state transition", zap.Error(err))
			return nil, fmt.Errorf("failed to scan state transition: %w", err)
		}
		transitions = append(transitions, &transition)
	}

	return transitions, nil
}

// GetActiveStatesCount получает количество активных состояний
func (r *PostgresRepository) GetActiveStatesCount(ctx context.Context) (int64, error) {
	query := `SELECT COUNT(*) FROM cyberpsychosis.states WHERE is_active = true`

	var count int64
	err := r.db.QueryRow(ctx, query).Scan(&count)
	if err != nil {
		r.logger.Error("Failed to get active states count", zap.Error(err))
		return 0, fmt.Errorf("failed to get active states count: %w", err)
	}

	return count, nil
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