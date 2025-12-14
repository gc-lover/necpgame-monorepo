package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/jackc/pgx/v4/pgxpool"
)

// Repository handles data persistence for AI enemies
type Repository struct {
	db  *pgxpool.Pool
	rdb *redis.Client
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool, rdb *redis.Client) *Repository {
	return &Repository{
		db:  db,
		rdb: rdb,
	}
}

// Enemy represents an AI enemy in the database
type Enemy struct {
	ID          string    `json:"id" db:"id"`
	EnemyType   string    `json:"enemy_type" db:"enemy_type"`
	Position    Position  `json:"position" db:"position"`
	Health      Health    `json:"health" db:"health"`
	SquadID     *string   `json:"squad_id,omitempty" db:"squad_id"`
	ZoneID      string    `json:"zone_id" db:"zone_id"`
	Status      string    `json:"status" db:"status"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	LastUpdated time.Time `json:"last_updated" db:"last_updated"`
}

// Position represents 3D coordinates
type Position struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

// Health represents enemy health status
type Health struct {
	Current    int     `json:"current"`
	Maximum    int     `json:"maximum"`
	Percentage float64 `json:"percentage"`
}

// SaveEnemy saves an enemy to the database
func (r *Repository) SaveEnemy(ctx context.Context, enemy *Enemy) error {
	query := `
		INSERT INTO ai_enemies (
			id, enemy_type, position, health, squad_id, zone_id, status, created_at, last_updated
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
		ON CONFLICT (id) DO UPDATE SET
			position = EXCLUDED.position,
			health = EXCLUDED.health,
			squad_id = EXCLUDED.squad_id,
			status = EXCLUDED.status,
			last_updated = EXCLUDED.last_updated
	`

	positionJSON, err := json.Marshal(enemy.Position)
	if err != nil {
		return fmt.Errorf("failed to marshal position: %w", err)
	}

	healthJSON, err := json.Marshal(enemy.Health)
	if err != nil {
		return fmt.Errorf("failed to marshal health: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		enemy.ID,
		enemy.EnemyType,
		positionJSON,
		healthJSON,
		enemy.SquadID,
		enemy.ZoneID,
		enemy.Status,
		enemy.CreatedAt,
		enemy.LastUpdated,
	)

	if err != nil {
		return fmt.Errorf("failed to save enemy: %w", err)
	}

	// Cache in Redis for real-time access
	return r.cacheEnemy(ctx, enemy)
}

// GetEnemy retrieves an enemy from the database or cache
func (r *Repository) GetEnemy(ctx context.Context, enemyID string) (*Enemy, error) {
	// Try cache first
	if enemy, err := r.getCachedEnemy(ctx, enemyID); err == nil && enemy != nil {
		return enemy, nil
	}

	// Fallback to database
	query := `
		SELECT id, enemy_type, position, health, squad_id, zone_id, status, created_at, last_updated
		FROM ai_enemies
		WHERE id = $1
	`

	var enemy Enemy
	var positionJSON, healthJSON []byte

	err := r.db.QueryRow(ctx, query, enemyID).Scan(
		&enemy.ID,
		&enemy.EnemyType,
		&positionJSON,
		&healthJSON,
		&enemy.SquadID,
		&enemy.ZoneID,
		&enemy.Status,
		&enemy.CreatedAt,
		&enemy.LastUpdated,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get enemy: %w", err)
	}

	if err := json.Unmarshal(positionJSON, &enemy.Position); err != nil {
		return nil, fmt.Errorf("failed to unmarshal position: %w", err)
	}

	if err := json.Unmarshal(healthJSON, &enemy.Health); err != nil {
		return nil, fmt.Errorf("failed to unmarshal health: %w", err)
	}

	// Cache the result
	r.cacheEnemy(ctx, &enemy)

	return &enemy, nil
}

// GetActiveEnemies retrieves all active enemies in a zone
func (r *Repository) GetActiveEnemies(ctx context.Context, zoneID string) ([]*Enemy, error) {
	query := `
		SELECT id, enemy_type, position, health, squad_id, zone_id, status, created_at, last_updated
		FROM ai_enemies
		WHERE zone_id = $1 AND status = 'active'
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, zoneID)
	if err != nil {
		return nil, fmt.Errorf("failed to query active enemies: %w", err)
	}
	defer rows.Close()

	var enemies []*Enemy
	for rows.Next() {
		var enemy Enemy
		var positionJSON, healthJSON []byte

		err := rows.Scan(
			&enemy.ID,
			&enemy.EnemyType,
			&positionJSON,
			&healthJSON,
			&enemy.SquadID,
			&enemy.ZoneID,
			&enemy.Status,
			&enemy.CreatedAt,
			&enemy.LastUpdated,
		)

		if err != nil {
			return nil, fmt.Errorf("failed to scan enemy: %w", err)
		}

		if err := json.Unmarshal(positionJSON, &enemy.Position); err != nil {
			return nil, fmt.Errorf("failed to unmarshal position: %w", err)
		}

		if err := json.Unmarshal(healthJSON, &enemy.Health); err != nil {
			return nil, fmt.Errorf("failed to unmarshal health: %w", err)
		}

		enemies = append(enemies, &enemy)
	}

	return enemies, rows.Err()
}

// DeleteEnemy removes an enemy from the database and cache
func (r *Repository) DeleteEnemy(ctx context.Context, enemyID string) error {
	query := `DELETE FROM ai_enemies WHERE id = $1`

	_, err := r.db.Exec(ctx, query, enemyID)
	if err != nil {
		return fmt.Errorf("failed to delete enemy: %w", err)
	}

	// Remove from cache
	return r.rdb.Del(ctx, fmt.Sprintf("enemy:%s", enemyID)).Err()
}

// cacheEnemy stores enemy in Redis cache
func (r *Repository) cacheEnemy(ctx context.Context, enemy *Enemy) error {
	data, err := json.Marshal(enemy)
	if err != nil {
		return fmt.Errorf("failed to marshal enemy for cache: %w", err)
	}

	key := fmt.Sprintf("enemy:%s", enemy.ID)
	return r.rdb.Set(ctx, key, data, 30*time.Minute).Err()
}

// getCachedEnemy retrieves enemy from Redis cache
func (r *Repository) getCachedEnemy(ctx context.Context, enemyID string) (*Enemy, error) {
	key := fmt.Sprintf("enemy:%s", enemyID)
	data, err := r.rdb.Get(ctx, key).Result()
	if err != nil {
		return nil, err
	}

	var enemy Enemy
	if err := json.Unmarshal([]byte(data), &enemy); err != nil {
		return nil, err
	}

	return &enemy, nil
}

// Issue: #1861
