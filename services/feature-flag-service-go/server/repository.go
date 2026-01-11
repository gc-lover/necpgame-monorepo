package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"
)

// Repository handles database operations for feature flags and experiments
type Repository interface {
	// Feature flag operations
	StoreFeatureFlag(ctx context.Context, flag *FeatureFlag) error
	GetFeatureFlag(ctx context.Context, flagName string) (*FeatureFlag, error)
	UpdateFeatureFlag(ctx context.Context, flag *FeatureFlag) error
	DeleteFeatureFlag(ctx context.Context, flagName string) error
	ListFeatureFlags(ctx context.Context) ([]*FeatureFlag, error)

	// Experiment operations
	StoreExperiment(ctx context.Context, experiment *Experiment) error
	GetExperiment(ctx context.Context, experimentID string) (*Experiment, error)
	UpdateExperiment(ctx context.Context, experiment *Experiment) error
	GetActiveExperimentsForFlag(ctx context.Context, flagName string) ([]Experiment, error)

	// Analytics and results
	StoreEvaluationEvent(ctx context.Context, event *EvaluationResult) error
	GetExperimentResults(ctx context.Context, experimentID string) (*ExperimentResultData, error)

	// Health check
	Ping(ctx context.Context) error
}

// PostgresRepository implements Repository for PostgreSQL
type PostgresRepository struct {
	db *sql.DB
}

// NewPostgresRepository creates a new PostgreSQL repository
func NewPostgresRepository(db *sql.DB) Repository {
	return &PostgresRepository{db: db}
}

// StoreFeatureFlag stores a feature flag in database
func (r *PostgresRepository) StoreFeatureFlag(ctx context.Context, flag *FeatureFlag) error {
	rolloutJSON, err := json.Marshal(flag.Rollout)
	if err != nil {
		return fmt.Errorf("failed to marshal rollout: %w", err)
	}

	conditionsJSON, err := json.Marshal(flag.Conditions)
	if err != nil {
		return fmt.Errorf("failed to marshal conditions: %w", err)
	}

	tagsJSON, err := json.Marshal(flag.Tags)
	if err != nil {
		return fmt.Errorf("failed to marshal tags: %w", err)
	}

	query := `
		INSERT INTO feature_flags.feature_flags (
			id, name, description, enabled, rollout, conditions, tags, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (name) DO UPDATE SET
			description = EXCLUDED.description,
			enabled = EXCLUDED.enabled,
			rollout = EXCLUDED.rollout,
			conditions = EXCLUDED.conditions,
			tags = EXCLUDED.tags,
			updated_at = EXCLUDED.updated_at
	`

	_, err = r.db.ExecContext(ctx, query,
		flag.ID, flag.Name, flag.Description, flag.Enabled,
		string(rolloutJSON), string(conditionsJSON), string(tagsJSON),
		flag.CreatedAt, flag.UpdatedAt,
	)

	if err != nil {
		slog.Error("Failed to store feature flag", "flag_name", flag.Name, "error", err)
		return fmt.Errorf("failed to store feature flag: %w", err)
	}

	slog.Debug("Feature flag stored", "flag_name", flag.Name, "flag_id", flag.ID)
	return nil
}

// GetFeatureFlag retrieves a feature flag from database
func (r *PostgresRepository) GetFeatureFlag(ctx context.Context, flagName string) (*FeatureFlag, error) {
	query := `
		SELECT id, name, description, enabled, rollout, conditions, tags, created_at, updated_at
		FROM feature_flags.feature_flags
		WHERE name = $1
	`

	var flag FeatureFlag
	var rolloutStr, conditionsStr, tagsStr string

	err := r.db.QueryRowContext(ctx, query, flagName).Scan(
		&flag.ID, &flag.Name, &flag.Description, &flag.Enabled,
		&rolloutStr, &conditionsStr, &tagsStr,
		&flag.CreatedAt, &flag.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("feature flag not found: %s", flagName)
		}
		slog.Error("Failed to get feature flag", "flag_name", flagName, "error", err)
		return nil, fmt.Errorf("failed to get feature flag: %w", err)
	}

	// Unmarshal JSON fields
	if err := json.Unmarshal([]byte(rolloutStr), &flag.Rollout); err != nil {
		return nil, fmt.Errorf("failed to unmarshal rollout: %w", err)
	}
	if err := json.Unmarshal([]byte(conditionsStr), &flag.Conditions); err != nil {
		return nil, fmt.Errorf("failed to unmarshal conditions: %w", err)
	}
	if err := json.Unmarshal([]byte(tagsStr), &flag.Tags); err != nil {
		return nil, fmt.Errorf("failed to unmarshal tags: %w", err)
	}

	return &flag, nil
}

// UpdateFeatureFlag updates a feature flag in database
func (r *PostgresRepository) UpdateFeatureFlag(ctx context.Context, flag *FeatureFlag) error {
	rolloutJSON, err := json.Marshal(flag.Rollout)
	if err != nil {
		return fmt.Errorf("failed to marshal rollout: %w", err)
	}

	conditionsJSON, err := json.Marshal(flag.Conditions)
	if err != nil {
		return fmt.Errorf("failed to marshal conditions: %w", err)
	}

	tagsJSON, err := json.Marshal(flag.Tags)
	if err != nil {
		return fmt.Errorf("failed to marshal tags: %w", err)
	}

	query := `
		UPDATE feature_flags.feature_flags
		SET description = $2, enabled = $3, rollout = $4, conditions = $5,
		    tags = $6, updated_at = $7
		WHERE name = $1
	`

	result, err := r.db.ExecContext(ctx, query,
		flag.Name, flag.Description, flag.Enabled,
		string(rolloutJSON), string(conditionsJSON), string(tagsJSON), flag.UpdatedAt,
	)

	if err != nil {
		slog.Error("Failed to update feature flag", "flag_name", flag.Name, "error", err)
		return fmt.Errorf("failed to update feature flag: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("feature flag not found: %s", flag.Name)
	}

	return nil
}

// DeleteFeatureFlag removes a feature flag from database
func (r *PostgresRepository) DeleteFeatureFlag(ctx context.Context, flagName string) error {
	query := `DELETE FROM feature_flags.feature_flags WHERE name = $1`

	result, err := r.db.ExecContext(ctx, query, flagName)
	if err != nil {
		slog.Error("Failed to delete feature flag", "flag_name", flagName, "error", err)
		return fmt.Errorf("failed to delete feature flag: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("feature flag not found: %s", flagName)
	}

	slog.Info("Feature flag deleted", "flag_name", flagName)
	return nil
}

// ListFeatureFlags returns all feature flags
func (r *PostgresRepository) ListFeatureFlags(ctx context.Context) ([]*FeatureFlag, error) {
	query := `
		SELECT id, name, description, enabled, rollout, conditions, tags, created_at, updated_at
		FROM feature_flags.feature_flags
		ORDER BY name
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		slog.Error("Failed to list feature flags", "error", err)
		return nil, fmt.Errorf("failed to list feature flags: %w", err)
	}
	defer rows.Close()

	var flags []*FeatureFlag
	for rows.Next() {
		var flag FeatureFlag
		var rolloutStr, conditionsStr, tagsStr string

		err := rows.Scan(
			&flag.ID, &flag.Name, &flag.Description, &flag.Enabled,
			&rolloutStr, &conditionsStr, &tagsStr,
			&flag.CreatedAt, &flag.UpdatedAt,
		)
		if err != nil {
			slog.Error("Failed to scan feature flag", "error", err)
			continue
		}

		// Unmarshal JSON fields
		if err := json.Unmarshal([]byte(rolloutStr), &flag.Rollout); err != nil {
			slog.Error("Failed to unmarshal rollout", "flag_name", flag.Name, "error", err)
			continue
		}
		if err := json.Unmarshal([]byte(conditionsStr), &flag.Conditions); err != nil {
			slog.Error("Failed to unmarshal conditions", "flag_name", flag.Name, "error", err)
			continue
		}
		if err := json.Unmarshal([]byte(tagsStr), &flag.Tags); err != nil {
			slog.Error("Failed to unmarshal tags", "flag_name", flag.Name, "error", err)
			continue
		}

		flags = append(flags, &flag)
	}

	return flags, nil
}

// StoreExperiment stores an experiment in database
func (r *PostgresRepository) StoreExperiment(ctx context.Context, experiment *Experiment) error {
	variantsJSON, err := json.Marshal(experiment.Variants)
	if err != nil {
		return fmt.Errorf("failed to marshal variants: %w", err)
	}

	targetingJSON, err := json.Marshal(experiment.Targeting)
	if err != nil {
		return fmt.Errorf("failed to marshal targeting: %w", err)
	}

	metricsJSON, err := json.Marshal(experiment.Metrics)
	if err != nil {
		return fmt.Errorf("failed to marshal metrics: %w", err)
	}

	query := `
		INSERT INTO feature_flags.experiments (
			id, name, description, status, feature_flag_id, variants, targeting,
			metrics, start_date, end_date, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
		ON CONFLICT (id) DO UPDATE SET
			name = EXCLUDED.name,
			description = EXCLUDED.description,
			status = EXCLUDED.status,
			variants = EXCLUDED.variants,
			targeting = EXCLUDED.targeting,
			metrics = EXCLUDED.metrics,
			start_date = EXCLUDED.start_date,
			end_date = EXCLUDED.end_date,
			updated_at = EXCLUDED.updated_at
	`

	_, err = r.db.ExecContext(ctx, query,
		experiment.ID, experiment.Name, experiment.Description, experiment.Status,
		experiment.FeatureFlagID, string(variantsJSON), string(targetingJSON),
		string(metricsJSON), experiment.StartDate, experiment.EndDate,
		experiment.CreatedAt, experiment.UpdatedAt,
	)

	if err != nil {
		slog.Error("Failed to store experiment", "experiment_name", experiment.Name, "error", err)
		return fmt.Errorf("failed to store experiment: %w", err)
	}

	slog.Debug("Experiment stored", "experiment_name", experiment.Name, "experiment_id", experiment.ID)
	return nil
}

// GetExperiment retrieves an experiment from database
func (r *PostgresRepository) GetExperiment(ctx context.Context, experimentID string) (*Experiment, error) {
	query := `
		SELECT id, name, description, status, feature_flag_id, variants, targeting,
			   metrics, start_date, end_date, created_at, updated_at
		FROM feature_flags.experiments
		WHERE id = $1
	`

	var experiment Experiment
	var variantsStr, targetingStr, metricsStr string
	var startDate, endDate sql.NullTime

	err := r.db.QueryRowContext(ctx, query, experimentID).Scan(
		&experiment.ID, &experiment.Name, &experiment.Description, &experiment.Status,
		&experiment.FeatureFlagID, &variantsStr, &targetingStr, &metricsStr,
		&startDate, &endDate, &experiment.CreatedAt, &experiment.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("experiment not found: %s", experimentID)
		}
		slog.Error("Failed to get experiment", "experiment_id", experimentID, "error", err)
		return nil, fmt.Errorf("failed to get experiment: %w", err)
	}

	// Handle nullable dates
	if startDate.Valid {
		experiment.StartDate = &startDate.Time
	}
	if endDate.Valid {
		experiment.EndDate = &endDate.Time
	}

	// Unmarshal JSON fields
	if err := json.Unmarshal([]byte(variantsStr), &experiment.Variants); err != nil {
		return nil, fmt.Errorf("failed to unmarshal variants: %w", err)
	}
	if err := json.Unmarshal([]byte(targetingStr), &experiment.Targeting); err != nil {
		return nil, fmt.Errorf("failed to unmarshal targeting: %w", err)
	}
	if err := json.Unmarshal([]byte(metricsStr), &experiment.Metrics); err != nil {
		return nil, fmt.Errorf("failed to unmarshal metrics: %w", err)
	}

	return &experiment, nil
}

// UpdateExperiment updates an experiment in database
func (r *PostgresRepository) UpdateExperiment(ctx context.Context, experiment *Experiment) error {
	variantsJSON, err := json.Marshal(experiment.Variants)
	if err != nil {
		return fmt.Errorf("failed to marshal variants: %w", err)
	}

	targetingJSON, err := json.Marshal(experiment.Targeting)
	if err != nil {
		return fmt.Errorf("failed to marshal targeting: %w", err)
	}

	metricsJSON, err := json.Marshal(experiment.Metrics)
	if err != nil {
		return fmt.Errorf("failed to marshal metrics: %w", err)
	}

	query := `
		UPDATE feature_flags.experiments
		SET name = $2, description = $3, status = $4, variants = $5, targeting = $6,
		    metrics = $7, start_date = $8, end_date = $9, updated_at = $10
		WHERE id = $1
	`

	result, err := r.db.ExecContext(ctx, query,
		experiment.ID, experiment.Name, experiment.Description, experiment.Status,
		string(variantsJSON), string(targetingJSON), string(metricsJSON),
		experiment.StartDate, experiment.EndDate, experiment.UpdatedAt,
	)

	if err != nil {
		slog.Error("Failed to update experiment", "experiment_id", experiment.ID, "error", err)
		return fmt.Errorf("failed to update experiment: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("experiment not found: %s", experiment.ID)
	}

	return nil
}

// GetActiveExperimentsForFlag returns active experiments for a feature flag
func (r *PostgresRepository) GetActiveExperimentsForFlag(ctx context.Context, flagName string) ([]Experiment, error) {
	query := `
		SELECT e.id, e.name, e.description, e.status, e.feature_flag_id, e.variants, e.targeting,
			   e.metrics, e.start_date, e.end_date, e.created_at, e.updated_at
		FROM feature_flags.experiments e
		JOIN feature_flags.feature_flags f ON e.feature_flag_id = f.id
		WHERE f.name = $1 AND e.status = 'running'
	`

	rows, err := r.db.QueryContext(ctx, query, flagName)
	if err != nil {
		slog.Error("Failed to get active experiments", "flag_name", flagName, "error", err)
		return nil, fmt.Errorf("failed to get active experiments: %w", err)
	}
	defer rows.Close()

	var experiments []Experiment
	for rows.Next() {
		var experiment Experiment
		var variantsStr, targetingStr, metricsStr string
		var startDate, endDate sql.NullTime

		err := rows.Scan(
			&experiment.ID, &experiment.Name, &experiment.Description, &experiment.Status,
			&experiment.FeatureFlagID, &variantsStr, &targetingStr, &metricsStr,
			&startDate, &endDate, &experiment.CreatedAt, &experiment.UpdatedAt,
		)
		if err != nil {
			slog.Error("Failed to scan experiment", "error", err)
			continue
		}

		// Handle nullable dates
		if startDate.Valid {
			experiment.StartDate = &startDate.Time
		}
		if endDate.Valid {
			experiment.EndDate = &endDate.Time
		}

		// Unmarshal JSON fields
		if err := json.Unmarshal([]byte(variantsStr), &experiment.Variants); err != nil {
			slog.Error("Failed to unmarshal variants", "experiment_id", experiment.ID, "error", err)
			continue
		}
		if err := json.Unmarshal([]byte(targetingStr), &experiment.Targeting); err != nil {
			slog.Error("Failed to unmarshal targeting", "experiment_id", experiment.ID, "error", err)
			continue
		}
		if err := json.Unmarshal([]byte(metricsStr), &experiment.Metrics); err != nil {
			slog.Error("Failed to unmarshal metrics", "experiment_id", experiment.ID, "error", err)
			continue
		}

		experiments = append(experiments, experiment)
	}

	return experiments, nil
}

// StoreEvaluationEvent stores an evaluation event for analytics
func (r *PostgresRepository) StoreEvaluationEvent(ctx context.Context, event *EvaluationResult) error {
	query := `
		INSERT INTO feature_flags.evaluation_events (
			id, flag_name, user_id, value, rule_id, experiment_id, variant_id, timestamp
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	eventID := uuid.New().String()
	valueJSON, err := json.Marshal(event.Value)
	if err != nil {
		return fmt.Errorf("failed to marshal value: %w", err)
	}

	_, err = r.db.ExecContext(ctx, query,
		eventID, event.FlagName, "user_id_placeholder", string(valueJSON),
		event.RuleID, event.ExperimentID, event.VariantID, event.Timestamp,
	)

	if err != nil {
		slog.Error("Failed to store evaluation event", "flag_name", event.FlagName, "error", err)
		return fmt.Errorf("failed to store evaluation event: %w", err)
	}

	return nil
}

// GetExperimentResults calculates experiment results from evaluation events
func (r *PostgresRepository) GetExperimentResults(ctx context.Context, experimentID string) (*ExperimentResultData, error) {
	// This is a simplified implementation - in production, you'd have more sophisticated analytics
	query := `
		SELECT variant_id, COUNT(*) as sample_size
		FROM feature_flags.evaluation_events
		WHERE experiment_id = $1
		GROUP BY variant_id
	`

	rows, err := r.db.QueryContext(ctx, query, experimentID)
	if err != nil {
		slog.Error("Failed to get experiment results", "experiment_id", experimentID, "error", err)
		return nil, fmt.Errorf("failed to get experiment results: %w", err)
	}
	defer rows.Close()

	var results ExperimentResultData
	results.VariantResults = make([]VariantResult, 0)

	for rows.Next() {
		var variantResult VariantResult
		err := rows.Scan(&variantResult.VariantID, &variantResult.SampleSize)
		if err != nil {
			slog.Error("Failed to scan variant result", "error", err)
			continue
		}

		variantResult.MetricValues = make(map[string]float64)
		results.VariantResults = append(results.VariantResults, variantResult)
	}

	// Simple winner determination (largest sample size)
	if len(results.VariantResults) > 0 {
		maxSample := int64(0)
		for _, result := range results.VariantResults {
			if result.SampleSize > maxSample {
				maxSample = result.SampleSize
				results.Winner = result.VariantID
			}
		}
		results.Confidence = 0.95 // Placeholder confidence level
	}

	return &results, nil
}

// Ping tests database connectivity
func (r *PostgresRepository) Ping(ctx context.Context) error {
	return r.db.PingContext(ctx)
}