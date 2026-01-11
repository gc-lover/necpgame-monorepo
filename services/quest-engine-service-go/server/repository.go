package server

import (
	"context"
	"database/sql"
	"fmt"
	"log/slog"
	"time"

	"necpgame/services/quest-engine-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

// Repository defines the data access interface
type Repository interface {
	// Quest operations
	ListQuests(ctx context.Context, params *api.ListQuestsParams) ([]*api.QuestSummary, error)
	CreateQuest(ctx context.Context, quest *api.QuestResponse) error
	GetQuest(ctx context.Context, questID openapi_types.UUID) (*api.QuestResponse, error)
	UpdateQuest(ctx context.Context, quest *api.QuestResponse) error
	DeleteQuest(ctx context.Context, questID openapi_types.UUID) error

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

// ListQuests retrieves quests with pagination and filtering
func (r *PostgresRepository) ListQuests(ctx context.Context, params *api.ListQuestsParams) ([]*api.QuestSummary, error) {
	query := `
		SELECT id, title, description, status, difficulty, rewards, created_at, updated_at
		FROM gameplay.quests
		WHERE ($1::text IS NULL OR status::text = $1)
		AND ($2::text IS NULL OR difficulty::text = $2)
		ORDER BY created_at DESC
		LIMIT $3 OFFSET $4
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
		nil, nil, limit, offset) // TODO: Add status and difficulty filtering
	if err != nil {
		slog.Error("Failed to list quests", "error", err)
		return nil, fmt.Errorf("failed to list quests: %w", err)
	}
	defer rows.Close()

	var quests []*api.QuestSummary
	for rows.Next() {
		var quest api.QuestSummary
		var rewards []map[string]interface{}
		err := rows.Scan(
			&quest.Id, &quest.Title, &quest.Description, &quest.Status,
			&quest.Difficulty, &rewards, &quest.CreatedAt, &quest.UpdatedAt,
		)
		if err != nil {
			slog.Error("Failed to scan quest", "error", err)
			return nil, fmt.Errorf("failed to scan quest: %w", err)
		}
		quest.Rewards = &rewards
		quests = append(quests, &quest)
	}

	slog.Info("Quests listed", "count", len(quests))
	return quests, nil
}

// CreateQuest creates a new quest in the database
func (r *PostgresRepository) CreateQuest(ctx context.Context, quest *api.QuestResponse) error {
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

// GetQuest retrieves a quest by ID
func (r *PostgresRepository) GetQuest(ctx context.Context, questID openapi_types.UUID) (*api.QuestResponse, error) {
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

// UpdateQuest updates a quest in the database
func (r *PostgresRepository) UpdateQuest(ctx context.Context, quest *api.QuestResponse) error {
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

// DeleteQuest deletes a quest from the database
func (r *PostgresRepository) DeleteQuest(ctx context.Context, questID openapi_types.UUID) error {
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
