// Package server Issue: #2203 - Production chain repository implementation
package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// ChainRepository handles production chain database operations
type ChainRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

// NewChainRepository creates new chain repository
func NewChainRepository(db *pgxpool.Pool) *ChainRepository {
	return &ChainRepository{
		db:     db,
		logger: GetLogger(),
	}
}

// GetByID retrieves production chain by ID
func (r *ChainRepository) GetByID(ctx context.Context, id uuid.UUID) (*ProductionChain, error) {
	query := `
		SELECT id, name, description, category, complexity, stages, status,
			   current_stage, player_id, total_progress, started_at, completed_at,
			   created_at, updated_at
		FROM production_chains
		WHERE id = $1
	`

	var chain ProductionChain
	var stagesJSON []byte

	err := r.db.QueryRow(ctx, query, id).Scan(
		&chain.ID, &chain.Name, &chain.Description, &chain.Category,
		&chain.Complexity, &stagesJSON, &chain.Status, &chain.CurrentStage,
		&chain.PlayerID, &chain.TotalProgress, &chain.StartedAt,
		&chain.CompletedAt, &chain.CreatedAt, &chain.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get production chain: %w", err)
	}

	// Unmarshal stages
	if err := json.Unmarshal(stagesJSON, &chain.Stages); err != nil {
		r.logger.WithError(err).Warn("Failed to unmarshal chain stages")
	}

	return &chain, nil
}

// List retrieves production chains with pagination and filtering
func (r *ChainRepository) List(ctx context.Context, playerID *uuid.UUID, status *string, limit, offset int) ([]ProductionChain, int, error) {
	baseQuery := `
		SELECT id, name, description, category, complexity, stages, status,
			   current_stage, player_id, total_progress, started_at, completed_at,
			   created_at, updated_at
		FROM production_chains
		WHERE 1=1
	`
	var args []interface{}

	if playerID != nil {
		baseQuery += fmt.Sprintf(" AND player_id = $%d", len(args)+1)
		args = append(args, *playerID)
	}

	if status != nil {
		baseQuery += fmt.Sprintf(" AND status = $%d", len(args)+1)
		args = append(args, *status)
	}

	// Safe parameterized query construction
	query := baseQuery + fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list production chains: %w", err)
	}
	defer rows.Close()

	var chains []ProductionChain
	for rows.Next() {
		var chain ProductionChain
		var stagesJSON []byte

		err := rows.Scan(
			&chain.ID, &chain.Name, &chain.Description, &chain.Category,
			&chain.Complexity, &stagesJSON, &chain.Status, &chain.CurrentStage,
			&chain.PlayerID, &chain.TotalProgress, &chain.StartedAt,
			&chain.CompletedAt, &chain.CreatedAt, &chain.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan production chain: %w", err)
		}

		// Unmarshal stages
		json.Unmarshal(stagesJSON, &chain.Stages)

		chains = append(chains, chain)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM production_chains WHERE 1=1"
	var countArgs []interface{}

	if playerID != nil {
		countQuery += " AND player_id = $1"
		countArgs = append(countArgs, *playerID)
	}

	if status != nil {
		countQuery += " AND status = $2"
		countArgs = append(countArgs, *status)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return chains, total, nil
}

// Create inserts new production chain
func (r *ChainRepository) Create(ctx context.Context, chain *ProductionChain) error {
	stagesJSON, _ := json.Marshal(chain.Stages)

	query := `
		INSERT INTO production_chains (
			id, name, description, category, complexity, stages, status,
			current_stage, player_id, total_progress, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.Exec(ctx, query,
		chain.ID, chain.Name, chain.Description, chain.Category,
		chain.Complexity, stagesJSON, chain.Status, chain.CurrentStage,
		chain.PlayerID, chain.TotalProgress, chain.CreatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to create production chain: %w", err)
	}

	return nil
}

// Update modifies existing production chain
func (r *ChainRepository) Update(ctx context.Context, chain *ProductionChain) error {
	stagesJSON, _ := json.Marshal(chain.Stages)

	query := `
		UPDATE production_chains SET
			name = $2, description = $3, category = $4, complexity = $5,
			stages = $6, status = $7, current_stage = $8, player_id = $9,
			total_progress = $10, started_at = $11, completed_at = $12,
			updated_at = $13
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		chain.ID, chain.Name, chain.Description, chain.Category,
		chain.Complexity, stagesJSON, chain.Status, chain.CurrentStage,
		chain.PlayerID, chain.TotalProgress, chain.StartedAt,
		chain.CompletedAt, chain.UpdatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to update production chain: %w", err)
	}

	return nil
}

// Delete removes production chain
func (r *ChainRepository) Delete(ctx context.Context, id uuid.UUID) error {
	query := "DELETE FROM production_chains WHERE id = $1"

	_, err := r.db.Exec(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete production chain: %w", err)
	}

	return nil
}
