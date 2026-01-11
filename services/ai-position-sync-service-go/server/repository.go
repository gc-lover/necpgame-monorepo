package server

import (
	"context"
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"

	"necpgame/services/ai-position-sync-service-go/pkg/api"
)

type Repository interface {
	SavePositionUpdate(ctx context.Context, update api.UpdatePositionRequest) error
	GetLatestPosition(ctx context.Context, entityID uuid.UUID) (*PositionEntity, error)
	BatchSavePositionUpdates(ctx context.Context, updates []api.UpdatePositionRequest) error
	GetPositionsInZone(ctx context.Context, zoneID uuid.UUID, since time.Time) ([]*PositionEntity, error)
	GetPositionHistory(ctx context.Context, entityID uuid.UUID, limit int, since time.Time) ([]*PositionEntity, error)
	Ping(ctx context.Context) error
}

type PositionEntity struct {
	ID        uuid.UUID
	EntityID  uuid.UUID
	Position  api.Position
	Velocity  api.Velocity
	ZoneID    uuid.UUID
	Timestamp time.Time
	CreatedAt time.Time
}

type PostgresRepository struct {
	db *sql.DB
}

func NewPostgresRepository(db *sql.DB) (*PostgresRepository, error) {
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("database ping failed: %w", err)
	}
	return &PostgresRepository{db: db}, nil
}

func (r *PostgresRepository) SavePositionUpdate(ctx context.Context, update api.UpdatePositionRequest) error {
	query := `
		INSERT INTO ai_position_updates (
			id, entity_id, position_x, position_y, position_z,
			velocity_x, velocity_y, velocity_z, zone_id, timestamp, created_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
		)`

	_, err := r.db.ExecContext(ctx, query,
		uuid.New(),
		update.EntityId,
		update.Position.X,
		update.Position.Y,
		update.Position.Z,
		update.Velocity.X,
		update.Velocity.Y,
		update.Velocity.Z,
		update.ZoneId,
		update.Timestamp,
		time.Now(),
	)

	if err != nil {
		return fmt.Errorf("failed to save position update: %w", err)
	}

	return nil
}

func (r *PostgresRepository) GetLatestPosition(ctx context.Context, entityID uuid.UUID) (*PositionEntity, error) {
	query := `
		SELECT id, entity_id, position_x, position_y, position_z,
			   velocity_x, velocity_y, velocity_z, zone_id, timestamp, created_at
		FROM ai_position_updates
		WHERE entity_id = $1
		ORDER BY timestamp DESC
		LIMIT 1`

	var entity PositionEntity
	err := r.db.QueryRowContext(ctx, query, entityID).Scan(
		&entity.ID,
		&entity.EntityID,
		&entity.Position.X,
		&entity.Position.Y,
		&entity.Position.Z,
		&entity.Velocity.X,
		&entity.Velocity.Y,
		&entity.Velocity.Z,
		&entity.ZoneID,
		&entity.Timestamp,
		&entity.CreatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("no position found for entity %s", entityID)
		}
		return nil, fmt.Errorf("failed to get latest position: %w", err)
	}

	return &entity, nil
}

func (r *PostgresRepository) BatchSavePositionUpdates(ctx context.Context, updates []api.UpdatePositionRequest) error {
	if len(updates) == 0 {
		return nil
	}

	// Performance: Use transaction for batch inserts
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	query := `
		INSERT INTO ai_position_updates (
			id, entity_id, position_x, position_y, position_z,
			velocity_x, velocity_y, velocity_z, zone_id, timestamp, created_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
		)`

	stmt, err := tx.PrepareContext(ctx, query)
	if err != nil {
		return fmt.Errorf("failed to prepare statement: %w", err)
	}
	defer stmt.Close()

	for _, update := range updates {
		_, err = stmt.ExecContext(ctx,
			uuid.New(),
			update.EntityId,
			update.Position.X,
			update.Position.Y,
			update.Position.Z,
			update.Velocity.X,
			update.Velocity.Y,
			update.Velocity.Z,
			update.ZoneId,
			update.Timestamp,
			time.Now(),
		)
		if err != nil {
			return fmt.Errorf("failed to execute batch insert: %w", err)
		}
	}

	if err := tx.Commit(); err != nil {
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	return nil
}

func (r *PostgresRepository) GetPositionsInZone(ctx context.Context, zoneID uuid.UUID, since time.Time) ([]*PositionEntity, error) {
	query := `
		SELECT DISTINCT ON (entity_id) id, entity_id, position_x, position_y, position_z,
			   velocity_x, velocity_y, velocity_z, zone_id, timestamp, created_at
		FROM ai_position_updates
		WHERE zone_id = $1 AND timestamp >= $2
		ORDER BY entity_id, timestamp DESC`

	rows, err := r.db.QueryContext(ctx, query, zoneID, since)
	if err != nil {
		return nil, fmt.Errorf("failed to query positions in zone: %w", err)
	}
	defer rows.Close()

	var positions []*PositionEntity
	for rows.Next() {
		var entity PositionEntity
		err := rows.Scan(
			&entity.ID,
			&entity.EntityID,
			&entity.Position.X,
			&entity.Position.Y,
			&entity.Position.Z,
			&entity.Velocity.X,
			&entity.Velocity.Y,
			&entity.Velocity.Z,
			&entity.ZoneID,
			&entity.Timestamp,
			&entity.CreatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan position row: %w", err)
		}
		positions = append(positions, &entity)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating position rows: %w", err)
	}

	return positions, nil
}

func (r *PostgresRepository) GetPositionHistory(ctx context.Context, entityID uuid.UUID, limit int, since time.Time) ([]*PositionEntity, error) {
	if limit <= 0 {
		limit = 100 // Default limit
	}
	if limit > 1000 {
		limit = 1000 // Max limit
	}

	query := `
		SELECT id, entity_id, position_x, position_y, position_z,
			   velocity_x, velocity_y, velocity_z, zone_id, timestamp, created_at
		FROM ai_position_updates
		WHERE entity_id = $1 AND timestamp >= $2
		ORDER BY timestamp DESC
		LIMIT $3`

	rows, err := r.db.QueryContext(ctx, query, entityID, since, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to query position history: %w", err)
	}
	defer rows.Close()

	var positions []*PositionEntity
	for rows.Next() {
		var entity PositionEntity
		err := rows.Scan(
			&entity.ID,
			&entity.EntityID,
			&entity.Position.X,
			&entity.Position.Y,
			&entity.Position.Z,
			&entity.Velocity.X,
			&entity.Velocity.Y,
			&entity.Velocity.Z,
			&entity.ZoneID,
			&entity.Timestamp,
			&entity.CreatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan position history row: %w", err)
		}
		positions = append(positions, &entity)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating position history rows: %w", err)
	}

	return positions, nil
}

func (r *PostgresRepository) Ping(ctx context.Context) error {
	return r.db.PingContext(ctx)
}

// Performance: Cleanup old position updates (should be called periodically)
func (r *PostgresRepository) CleanupOldUpdates(ctx context.Context, olderThan time.Duration) error {
	cutoff := time.Now().Add(-olderThan)
	query := `DELETE FROM ai_position_updates WHERE created_at < $1`

	result, err := r.db.ExecContext(ctx, query, cutoff)
	if err != nil {
		return fmt.Errorf("failed to cleanup old updates: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		slog.Warn("Could not get rows affected for cleanup", "error", err)
	} else {
		slog.Info("Cleaned up old position updates", "rows_deleted", rowsAffected)
	}

	return nil
}