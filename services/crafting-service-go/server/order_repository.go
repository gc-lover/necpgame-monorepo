// Package server Issue: #2203 - Order repository implementation
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// OrderRepository handles order database operations
type OrderRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

// NewOrderRepository creates new order repository
func NewOrderRepository(db *pgxpool.Pool) *OrderRepository {
	return &OrderRepository{
		db:     db,
		logger: GetLogger(),
	}
}

// GetByID retrieves order by ID
func (r *OrderRepository) GetByID(ctx context.Context, id uuid.UUID) (*Order, error) {
	query := `
		SELECT id, player_id, recipe_id, station_id, status, quality_modifier,
			   station_bonus, progress, started_at, completed_at, created_at, updated_at
		FROM crafting_orders
		WHERE id = $1
	`

	var order Order
	err := r.db.QueryRow(ctx, query, id).Scan(
		&order.ID, &order.PlayerID, &order.RecipeID, &order.StationID,
		&order.Status, &order.QualityModifier, &order.StationBonus,
		&order.Progress, &order.StartedAt, &order.CompletedAt,
		&order.CreatedAt, &order.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get order: %w", err)
	}

	return &order, nil
}

// List retrieves orders with pagination and filtering
func (r *OrderRepository) List(ctx context.Context, playerID *uuid.UUID, status *string, limit, offset int) ([]Order, int, error) {
	baseQuery := `
		SELECT id, player_id, recipe_id, station_id, status, quality_modifier,
			   station_bonus, progress, started_at, completed_at, created_at, updated_at
		FROM crafting_orders
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

	query := baseQuery + fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list orders: %w", err)
	}
	defer rows.Close()

	var orders []Order
	for rows.Next() {
		var order Order
		err := rows.Scan(
			&order.ID, &order.PlayerID, &order.RecipeID, &order.StationID,
			&order.Status, &order.QualityModifier, &order.StationBonus,
			&order.Progress, &order.StartedAt, &order.CompletedAt,
			&order.CreatedAt, &order.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan order: %w", err)
		}
		orders = append(orders, order)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM crafting_orders WHERE 1=1"
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

	return orders, total, nil
}

// Create inserts new order
func (r *OrderRepository) Create(ctx context.Context, order *Order) error {
	query := `
		INSERT INTO crafting_orders (
			id, player_id, recipe_id, station_id, status, quality_modifier,
			station_bonus, progress, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.PlayerID, order.RecipeID, order.StationID,
		order.Status, order.QualityModifier, order.StationBonus,
		order.Progress, order.CreatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to create order: %w", err)
	}

	return nil
}

// Update modifies existing order
func (r *OrderRepository) Update(ctx context.Context, order *Order) error {
	query := `
		UPDATE crafting_orders SET
			station_id = $2, status = $3, quality_modifier = $4,
			station_bonus = $5, progress = $6, started_at = $7,
			completed_at = $8, updated_at = $9
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.StationID, order.Status, order.QualityModifier,
		order.StationBonus, order.Progress, order.StartedAt,
		order.CompletedAt, order.UpdatedAt,
	)
	if err != nil {
		return fmt.Errorf("failed to update order: %w", err)
	}

	return nil
}

// Delete removes order
func (r *OrderRepository) Delete(ctx context.Context, id uuid.UUID) error {
	query := "DELETE FROM crafting_orders WHERE id = $1"

	_, err := r.db.Exec(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete order: %w", err)
	}

	return nil
}

// GetActiveByStation retrieves active order for station
func (r *OrderRepository) GetActiveByStation(ctx context.Context, stationID uuid.UUID) (*Order, error) {
	query := `
		SELECT id, player_id, recipe_id, station_id, status, quality_modifier,
			   station_bonus, progress, started_at, completed_at, created_at, updated_at
		FROM crafting_orders
		WHERE station_id = $1 AND status IN ('active', 'pending')
		ORDER BY created_at ASC
		LIMIT 1
	`

	var order Order
	err := r.db.QueryRow(ctx, query, stationID).Scan(
		&order.ID, &order.PlayerID, &order.RecipeID, &order.StationID,
		&order.Status, &order.QualityModifier, &order.StationBonus,
		&order.Progress, &order.StartedAt, &order.CompletedAt,
		&order.CreatedAt, &order.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get active order for station: %w", err)
	}

	return &order, nil
}
