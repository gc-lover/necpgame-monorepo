// Issue: #1597
package server

import (
	"context"
	"database/sql"
	"fmt"

	_ "github.com/lib/pq"
)

// Repository handles data access
type Repository struct {
	db *sql.DB
}

// NewRepository creates new repository with database connection
func NewRepository() *Repository {
	dbURL := getEnv("DATABASE_URL", "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable")

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		// For now, just log and continue without DB
		fmt.Printf("Warning: Failed to connect to database: %v\n", err)
		return &Repository{db: nil}
	}

	if err := db.Ping(); err != nil {
		fmt.Printf("Warning: Failed to ping database: %v\n", err)
		return &Repository{db: nil}
	}

	return &Repository{db: db}
}


// GetQuestDefinitionByID retrieves quest definition by quest_id
func (r *Repository) GetQuestDefinitionByID(ctx context.Context, questID string) (*QuestDefinition, error) {
	if r.db == nil {
		return nil, fmt.Errorf("database not connected")
	}

	query := `
		SELECT quest_id, title, quest_type, level_min, level_max, requirements, objectives, rewards, content_data, is_active
		FROM gameplay.quest_definitions
		WHERE quest_id = $1
	`

	var questDef QuestDefinition
	err := r.db.QueryRowContext(ctx, query, questID).Scan(
		&questDef.QuestID,
		&questDef.Title,
		&questDef.QuestType,
		&questDef.LevelMin,
		&questDef.LevelMax,
		&questDef.Requirements,
		&questDef.Objectives,
		&questDef.Rewards,
		&questDef.ContentData,
		&questDef.IsActive,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("not found")
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get quest definition: %w", err)
	}

	return &questDef, nil
}

// CreateQuestDefinition creates new quest definition
func (r *Repository) CreateQuestDefinition(ctx context.Context, questDef *QuestDefinition) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO gameplay.quest_definitions (
			quest_id, title, quest_type, level_min, level_max,
			requirements, objectives, rewards, content_data, is_active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	_, err := r.db.ExecContext(ctx, query,
		questDef.QuestID,
		questDef.Title,
		questDef.QuestType,
		questDef.LevelMin,
		questDef.LevelMax,
		questDef.Requirements,
		questDef.Objectives,
		questDef.Rewards,
		questDef.ContentData,
		questDef.IsActive,
	)

	if err != nil {
		return fmt.Errorf("failed to create quest definition: %w", err)
	}

	return nil
}

// UpdateQuestDefinition updates existing quest definition
func (r *Repository) UpdateQuestDefinition(ctx context.Context, questDef *QuestDefinition) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		UPDATE gameplay.quest_definitions SET
			title = $2, quest_type = $3, level_min = $4, level_max = $5,
			requirements = $6, objectives = $7, rewards = $8, content_data = $9,
			is_active = $10, updated_at = CURRENT_TIMESTAMP
		WHERE quest_id = $1
	`

	result, err := r.db.ExecContext(ctx, query,
		questDef.QuestID,
		questDef.Title,
		questDef.QuestType,
		questDef.LevelMin,
		questDef.LevelMax,
		questDef.Requirements,
		questDef.Objectives,
		questDef.Rewards,
		questDef.ContentData,
		questDef.IsActive,
	)

	if err != nil {
		return fmt.Errorf("failed to update quest definition: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("quest definition not found")
	}

	return nil
}

