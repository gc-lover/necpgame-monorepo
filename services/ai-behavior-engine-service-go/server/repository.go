package server

import (
	"context"
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"
)

// BehaviorEntity represents an AI behavior in the database
type BehaviorEntity struct {
	ID             uuid.UUID `json:"id"`
	EnemyID        uuid.UUID `json:"enemy_id"`
	BehaviorType   string    `json:"behavior_type"`
	State          string    `json:"state"`
	Priority       int       `json:"priority"`
	Parameters     string    `json:"parameters"` // JSON string
	LastExecuted   time.Time `json:"last_executed"`
	CreatedAt      time.Time `json:"created_at"`
	UpdatedAt      time.Time `json:"updated_at"`
}

// Repository defines the data access interface for AI Behavior Engine
type Repository interface {
	// Behavior operations
	CreateBehavior(ctx context.Context, behavior *BehaviorEntity) error
	GetBehavior(ctx context.Context, enemyID uuid.UUID) (*BehaviorEntity, error)
	UpdateBehavior(ctx context.Context, enemyID uuid.UUID, updates map[string]interface{}) error
	DeleteBehavior(ctx context.Context, enemyID uuid.UUID) error

	// Behavior tree operations
	SaveBehaviorTree(ctx context.Context, treeID string, treeData string) error
	GetBehaviorTree(ctx context.Context, treeID string) (string, error)

	// Analytics operations
	GetBehaviorStats(ctx context.Context, enemyID uuid.UUID) (map[string]interface{}, error)

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

// CreateBehavior creates a new behavior record
func (r *PostgresRepository) CreateBehavior(ctx context.Context, behavior *BehaviorEntity) error {
	query := `
		INSERT INTO ai_behaviors (id, enemy_id, behavior_type, state, priority, parameters, last_executed, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	now := time.Now().UTC()
	_, err := r.db.ExecContext(ctx, query,
		behavior.ID,
		behavior.EnemyID,
		behavior.BehaviorType,
		behavior.State,
		behavior.Priority,
		behavior.Parameters,
		behavior.LastExecuted,
		now,
		now,
	)

	if err != nil {
		slog.Error("Failed to create behavior", "error", err, "enemy_id", behavior.EnemyID)
		return fmt.Errorf("failed to create behavior: %w", err)
	}

	slog.Info("Behavior created", "enemy_id", behavior.EnemyID, "behavior_type", behavior.BehaviorType)
	return nil
}

// GetBehavior retrieves behavior for an enemy
func (r *PostgresRepository) GetBehavior(ctx context.Context, enemyID uuid.UUID) (*BehaviorEntity, error) {
	query := `
		SELECT id, enemy_id, behavior_type, state, priority, parameters, last_executed, created_at, updated_at
		FROM ai_behaviors
		WHERE enemy_id = $1
		ORDER BY updated_at DESC
		LIMIT 1
	`

	var behavior BehaviorEntity
	err := r.db.QueryRowContext(ctx, query, enemyID).Scan(
		&behavior.ID,
		&behavior.EnemyID,
		&behavior.BehaviorType,
		&behavior.State,
		&behavior.Priority,
		&behavior.Parameters,
		&behavior.LastExecuted,
		&behavior.CreatedAt,
		&behavior.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("behavior not found for enemy: %s", enemyID)
		}
		slog.Error("Failed to get behavior", "error", err, "enemy_id", enemyID)
		return nil, fmt.Errorf("failed to get behavior: %w", err)
	}

	return &behavior, nil
}

// UpdateBehavior updates behavior data
func (r *PostgresRepository) UpdateBehavior(ctx context.Context, enemyID uuid.UUID, updates map[string]interface{}) error {
	// Build dynamic update query
	setParts := []string{}
	args := []interface{}{}
	argIndex := 1

	for field, value := range updates {
		setParts = append(setParts, fmt.Sprintf("%s = $%d", field, argIndex))
		args = append(args, value)
		argIndex++
	}

	if len(setParts) == 0 {
		return fmt.Errorf("no updates provided")
	}

	setParts = append(setParts, "updated_at = $"+fmt.Sprintf("%d", argIndex))
	args = append(args, time.Now().UTC())
	argIndex++

	query := fmt.Sprintf(`
		UPDATE ai_behaviors
		SET %s
		WHERE enemy_id = $%d
	`, fmt.Sprintf("%s", setParts), argIndex)

	args = append(args, enemyID)

	result, err := r.db.ExecContext(ctx, query, args...)
	if err != nil {
		slog.Error("Failed to update behavior", "error", err, "enemy_id", enemyID)
		return fmt.Errorf("failed to update behavior: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("behavior not found for enemy: %s", enemyID)
	}

	slog.Info("Behavior updated", "enemy_id", enemyID, "updates", len(updates))
	return nil
}

// DeleteBehavior removes behavior data
func (r *PostgresRepository) DeleteBehavior(ctx context.Context, enemyID uuid.UUID) error {
	query := `DELETE FROM ai_behaviors WHERE enemy_id = $1`

	result, err := r.db.ExecContext(ctx, query, enemyID)
	if err != nil {
		slog.Error("Failed to delete behavior", "error", err, "enemy_id", enemyID)
		return fmt.Errorf("failed to delete behavior: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("behavior not found for enemy: %s", enemyID)
	}

	slog.Info("Behavior deleted", "enemy_id", enemyID)
	return nil
}

// SaveBehaviorTree saves behavior tree data
func (r *PostgresRepository) SaveBehaviorTree(ctx context.Context, treeID string, treeData string) error {
	query := `
		INSERT INTO behavior_trees (id, tree_data, updated_at)
		VALUES ($1, $2, $3)
		ON CONFLICT (id) DO UPDATE SET
			tree_data = EXCLUDED.tree_data,
			updated_at = EXCLUDED.updated_at
	`

	_, err := r.db.ExecContext(ctx, query, treeID, treeData, time.Now().UTC())
	if err != nil {
		slog.Error("Failed to save behavior tree", "error", err, "tree_id", treeID)
		return fmt.Errorf("failed to save behavior tree: %w", err)
	}

	slog.Info("Behavior tree saved", "tree_id", treeID)
	return nil
}

// GetBehaviorTree retrieves behavior tree data
func (r *PostgresRepository) GetBehaviorTree(ctx context.Context, treeID string) (string, error) {
	query := `SELECT tree_data FROM behavior_trees WHERE id = $1`

	var treeData string
	err := r.db.QueryRowContext(ctx, query, treeID).Scan(&treeData)

	if err != nil {
		if err == sql.ErrNoRows {
			return "", fmt.Errorf("behavior tree not found: %s", treeID)
		}
		slog.Error("Failed to get behavior tree", "error", err, "tree_id", treeID)
		return "", fmt.Errorf("failed to get behavior tree: %w", err)
	}

	return treeData, nil
}

// GetBehaviorStats retrieves behavior analytics
func (r *PostgresRepository) GetBehaviorStats(ctx context.Context, enemyID uuid.UUID) (map[string]interface{}, error) {
	query := `
		SELECT
			COUNT(*) as total_executions,
			AVG(EXTRACT(EPOCH FROM (updated_at - created_at))) as avg_duration,
			MAX(updated_at) as last_execution
		FROM ai_behaviors
		WHERE enemy_id = $1
	`

	var totalExecutions int64
	var avgDuration *float64
	var lastExecution *time.Time

	err := r.db.QueryRowContext(ctx, query, enemyID).Scan(
		&totalExecutions,
		&avgDuration,
		&lastExecution,
	)

	if err != nil {
		slog.Error("Failed to get behavior stats", "error", err, "enemy_id", enemyID)
		return nil, fmt.Errorf("failed to get behavior stats: %w", err)
	}

	stats := map[string]interface{}{
		"total_executions": totalExecutions,
		"avg_duration":     avgDuration,
		"last_execution":   lastExecution,
	}

	return stats, nil
}

// Ping tests database connectivity
func (r *PostgresRepository) Ping(ctx context.Context) error {
	// Performance: Use context timeout
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	return r.db.PingContext(ctx)
}