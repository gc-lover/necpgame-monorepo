package server

import (
	"context"
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"
	"necpgame/services/ai-enemy-coordinator-service-go/pkg/api"
)

// AiEnemyEntity represents an AI enemy in the database
type AiEnemyEntity struct {
	ID            uuid.UUID `json:"id"`
	EnemyType     string    `json:"enemy_type"`
	Level         int       `json:"level"`
	ZoneID        uuid.UUID `json:"zone_id"`
	PositionX     float64   `json:"position_x"`
	PositionY     float64   `json:"position_y"`
	PositionZ     float64   `json:"position_z"`
	BehaviorState string    `json:"behavior_state"`
	Health        int       `json:"health"`
	MaxHealth     int       `json:"max_health"`
	Faction       string    `json:"faction"`
	CreatedAt     time.Time `json:"created_at"`
	UpdatedAt     time.Time `json:"updated_at"`
}

// Repository defines the data access interface
type Repository interface {
	// AI Enemy operations
	CreateAiEnemy(ctx context.Context, enemy *api.AiEnemyEntity) error
	GetAiEnemy(ctx context.Context, enemyID string) (*api.AiEnemyEntity, error)
	UpdateAiEnemy(ctx context.Context, enemyID string, updates map[string]interface{}) error
	DeleteAiEnemy(ctx context.Context, enemyID string) error

	// Zone operations
	GetZoneActiveEnemies(ctx context.Context, zoneID string) ([]*api.AiEnemyEntity, error)
	GetZoneMetrics(ctx context.Context, zoneID string) (*ZoneData, error)

	// Health check
	Ping(ctx context.Context) error
}

// PostgresRepository implements Repository for PostgreSQL
type PostgresRepository struct {
	db *sql.DB
}

// NewPostgresRepository creates a new PostgreSQL repository
func NewPostgresRepository(db *sql.DB) *PostgresRepository {
	return &PostgresRepository{
		db: db,
	}
}

// CreateAiEnemy creates a new AI enemy in the database
func (r *PostgresRepository) CreateAiEnemy(ctx context.Context, enemy *AiEnemyEntity) error {
	query := `
		INSERT INTO gameplay.ai_enemies (
			id, enemy_type, level, zone_id, position_x, position_y, position_z,
			behavior_state, health, max_health, faction, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)
	`

	now := time.Now().UTC()
	position := enemy.Position

	_, err := r.db.ExecContext(ctx, query,
		enemy.Id, enemy.EnemyType, enemy.Level, enemy.ZoneID,
		position.X, position.Y, position.Z,
		enemy.BehaviorState, enemy.Health, enemy.MaxHealth, enemy.Faction,
		now, now,
	)

	if err != nil {
		slog.Error("Failed to create AI enemy", "error", err, "enemy_id", enemy.Id)
		return fmt.Errorf("failed to create AI enemy: %w", err)
	}

	slog.Info("AI enemy created", "enemy_id", enemy.Id, "zone_id", enemy.ZoneID)
	return nil
}

// GetAiEnemy retrieves an AI enemy by ID
func (r *PostgresRepository) GetAiEnemy(ctx context.Context, enemyID string) (*api.AiEnemyEntity, error) {
	query := `
		SELECT id, enemy_type, level, zone_id, position_x, position_y, position_z,
			   behavior_state, health, max_health, faction, created_at, updated_at
		FROM gameplay.ai_enemies
		WHERE id = $1
	`

	var enemy api.AiEnemyEntity
	var posX, posY, posZ float64

	err := r.db.QueryRowContext(ctx, query, enemyID).Scan(
		&enemy.Id, &enemy.EnemyType, &enemy.Level, &enemy.ZoneID,
		&posX, &posY, &posZ, &enemy.BehaviorState,
		&enemy.Health, &enemy.MaxHealth, &enemy.Faction,
		&enemy.CreatedAt, &enemy.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("AI enemy not found: %s", enemyID)
		}
		slog.Error("Failed to get AI enemy", "error", err, "enemy_id", enemyID)
		return nil, fmt.Errorf("failed to get AI enemy: %w", err)
	}

	enemy.Position = &api.Vector3{
		X: &posX,
		Y: &posY,
		Z: &posZ,
	}

	return &enemy, nil
}

// UpdateAiEnemy updates AI enemy fields
func (r *PostgresRepository) UpdateAiEnemy(ctx context.Context, enemyID string, updates map[string]interface{}) error {
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
		return fmt.Errorf("no fields to update")
	}

	setParts = append(setParts, "updated_at = $"+fmt.Sprintf("%d", argIndex))
	args = append(args, time.Now().UTC())
	argIndex++

	query := fmt.Sprintf(`
		UPDATE gameplay.ai_enemies
		SET %s
		WHERE id = $%d
	`, fmt.Sprintf("%s", setParts), argIndex)

	args = append(args, enemyID)

	result, err := r.db.ExecContext(ctx, query, args...)
	if err != nil {
		slog.Error("Failed to update AI enemy", "error", err, "enemy_id", enemyID)
		return fmt.Errorf("failed to update AI enemy: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("AI enemy not found: %s", enemyID)
	}

	slog.Info("AI enemy updated", "enemy_id", enemyID, "fields_updated", len(updates))
	return nil
}

// DeleteAiEnemy removes an AI enemy from the database
func (r *PostgresRepository) DeleteAiEnemy(ctx context.Context, enemyID string) error {
	query := `DELETE FROM gameplay.ai_enemies WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, enemyID)
	if err != nil {
		slog.Error("Failed to delete AI enemy", "error", err, "enemy_id", enemyID)
		return fmt.Errorf("failed to delete AI enemy: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("AI enemy not found: %s", enemyID)
	}

	slog.Info("AI enemy deleted", "enemy_id", enemyID)
	return nil
}

// GetZoneActiveEnemies gets all active enemies in a zone
func (r *PostgresRepository) GetZoneActiveEnemies(ctx context.Context, zoneID string) ([]*api.AiEnemyEntity, error) {
	query := `
		SELECT id, enemy_type, level, zone_id, position_x, position_y, position_z,
			   behavior_state, health, max_health, faction, created_at, updated_at
		FROM gameplay.ai_enemies
		WHERE zone_id = $1 AND behavior_state != 'dead'
		ORDER BY created_at DESC
	`

	rows, err := r.db.QueryContext(ctx, query, zoneID)
	if err != nil {
		slog.Error("Failed to get zone active enemies", "error", err, "zone_id", zoneID)
		return nil, fmt.Errorf("failed to get zone enemies: %w", err)
	}
	defer rows.Close()

	var enemies []*api.AiEnemyEntity

	for rows.Next() {
		var enemy api.AiEnemyEntity
		var posX, posY, posZ float64

		err := rows.Scan(
			&enemy.Id, &enemy.EnemyType, &enemy.Level, &enemy.ZoneID,
			&posX, &posY, &posZ, &enemy.BehaviorState,
			&enemy.Health, &enemy.MaxHealth, &enemy.Faction,
			&enemy.CreatedAt, &enemy.UpdatedAt,
		)

		if err != nil {
			slog.Error("Failed to scan enemy row", "error", err)
			continue
		}

		enemy.Position = &api.Vector3{
			X: &posX,
			Y: &posY,
			Z: &posZ,
		}

		enemies = append(enemies, &enemy)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating rows: %w", err)
	}

	slog.Info("Retrieved zone active enemies", "zone_id", zoneID, "count", len(enemies))
	return enemies, nil
}

// GetZoneMetrics gets zone performance metrics
func (r *PostgresRepository) GetZoneMetrics(ctx context.Context, zoneID string) (*ZoneData, error) {
	// This would typically aggregate from multiple tables/sources
	// For now, return mock data
	return &ZoneData{
		ActiveEnemies: 150,
		LastActivity:  time.Now().UTC(),
		PerformanceData: PerformanceMetrics{
			CPUUsagePercent:    45.5,
			MemoryUsageMB:      256,
			AIDecisionLatency:  15 * time.Millisecond,
			NetworkSyncLatency: 25 * time.Millisecond,
		},
	}, nil
}

// Ping checks database connectivity
func (r *PostgresRepository) Ping(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	return r.db.PingContext(ctx)
}