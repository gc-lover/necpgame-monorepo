package server

import (
	"context"
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	"necpgame/services/interactive-object-manager-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

// Repository defines the data access interface
type Repository interface {
	// Interactive object operations
	ListInteractiveObjects(ctx context.Context, params *api.ListInteractiveObjectsParams) ([]*api.InteractiveObjectSummary, error)
	CreateInteractiveObject(ctx context.Context, object *api.InteractiveObjectResponse) error
	GetInteractiveObject(ctx context.Context, objectID openapi_types.UUID) (*api.InteractiveObjectResponse, error)
	UpdateInteractiveObject(ctx context.Context, object *api.InteractiveObjectResponse) error
	DeleteInteractiveObject(ctx context.Context, objectID openapi_types.UUID) error

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

// ListInteractiveObjects retrieves objects with pagination and filtering
func (r *PostgresRepository) ListInteractiveObjects(ctx context.Context, params *api.ListInteractiveObjectsParams) ([]*api.InteractiveObjectSummary, error) {
	query := `
		SELECT id, object_type, zone_id, status, created_at, updated_at
		FROM gameplay.interactive_objects
		WHERE ($1::text IS NULL OR zone_id::text = $1)
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	limit := int64(20) // default
	offset := int64(0) // default

	if params != nil {
		if params.Limit != nil {
			limit = int64(*params.Limit)
		}
		if params.Offset != nil {
			offset = int64(*params.Offset)
		}
	}

	rows, err := r.db.QueryContext(ctx, query,
		params.ZoneId, limit, offset)
	if err != nil {
		slog.Error("Failed to list objects", "error", err)
		return nil, fmt.Errorf("failed to list objects: %w", err)
	}
	defer rows.Close()

	var objects []*api.InteractiveObjectSummary
	for rows.Next() {
		var obj api.InteractiveObjectSummary
		err := rows.Scan(
			&obj.Id, &obj.ObjectType, &obj.ZoneId, &obj.Status, &obj.CreatedAt, &obj.UpdatedAt,
		)
		if err != nil {
			slog.Error("Failed to scan object", "error", err)
			return nil, fmt.Errorf("failed to scan object: %w", err)
		}
		objects = append(objects, &obj)
	}

	slog.Info("Objects listed", "count", len(objects))
	return objects, nil
}

// CreateInteractiveObject creates a new object in the database
func (r *PostgresRepository) CreateInteractiveObject(ctx context.Context, object *api.InteractiveObjectResponse) error {
	query := `
		INSERT INTO gameplay.quests (
			id, title, description, status, difficulty, objectives, rewards, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	now := time.Now().UTC()

	_, err := r.db.ExecContext(ctx, query,
		quest.Id, quest.Title, quest.Description, quest.Status,
		quest.Difficulty, quest.Objectives, quest.Rewards, now, now,
	)

	if err != nil {
		slog.Error("Failed to create quest", "error", err, "quest_id", quest.Id)
		return fmt.Errorf("failed to create quest: %w", err)
	}

	slog.Info("Quest created", "quest_id", quest.Id, "title", *quest.Title)
	return nil
}

// GetInteractiveObject retrieves an object by ID
func (r *PostgresRepository) GetInteractiveObject(ctx context.Context, objectID openapi_types.UUID) (*api.InteractiveObjectResponse, error) {
	query := `
		SELECT id, title, description, status, difficulty, objectives, rewards, created_at, updated_at
		FROM gameplay.quests
		WHERE id = $1
	`

	var quest api.QuestResponse
	var objectives, rewards []map[string]interface{}

	err := r.db.QueryRowContext(ctx, query, questID).Scan(
		&quest.Id, &quest.Title, &quest.Description, &quest.Status,
		&quest.Difficulty, &objectives, &rewards, &quest.CreatedAt, &quest.UpdatedAt,
	)
	if err != nil {
		slog.Error("Failed to get quest", "quest_id", questID, "error", err)
		return nil, fmt.Errorf("failed to get quest: %w", err)
	}

	quest.Objectives = &objectives
	quest.Rewards = &rewards

	slog.Info("Quest retrieved", "quest_id", questID)
	return &quest, nil
}

// UpdateInteractiveObject updates an object in the database
func (r *PostgresRepository) UpdateInteractiveObject(ctx context.Context, object *api.InteractiveObjectResponse) error {
	query := `
		UPDATE gameplay.quests
		SET title = $2, description = $3, status = $4, difficulty = $5,
		    objectives = $6, rewards = $7, updated_at = $8
		WHERE id = $1
	`

	now := time.Now().UTC()

	_, err := r.db.ExecContext(ctx, query,
		quest.Id, quest.Title, quest.Description, quest.Status,
		quest.Difficulty, quest.Objectives, quest.Rewards, now,
	)

	if err != nil {
		slog.Error("Failed to update quest", "error", err, "quest_id", quest.Id)
		return fmt.Errorf("failed to update quest: %w", err)
	}

	slog.Info("Quest updated", "quest_id", quest.Id)
	return nil
}

// DeleteInteractiveObject deletes an object from the database
func (r *PostgresRepository) DeleteInteractiveObject(ctx context.Context, objectID openapi_types.UUID) error {
	query := `DELETE FROM gameplay.quests WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query, questID)
	if err != nil {
		slog.Error("Failed to delete quest", "error", err, "quest_id", questID)
		return fmt.Errorf("failed to delete quest: %w", err)
	}

	slog.Info("Quest deleted", "quest_id", questID)
	return nil
}

// Ping checks database connectivity
func (r *PostgresRepository) Ping(ctx context.Context) error {
	return r.db.PingContext(ctx)
}
