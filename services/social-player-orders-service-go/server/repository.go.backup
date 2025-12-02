// Issue: #81
package server

import (
	"context"
	"fmt"

	"github.com/jackc/pgx/v5/pgxpool"
)

type OrderRepository struct {
	db *pgxpool.Pool
}

func NewOrderRepository(ctx context.Context, dbURL string) (*OrderRepository, error) {
	pool, err := pgxpool.New(ctx, dbURL)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	// Test connection
	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return &OrderRepository{
		db: pool,
	}, nil
}

func (r *OrderRepository) Close() {
	if r.db != nil {
		r.db.Close()
	}
}

// ListOrders retrieves orders from database
func (r *OrderRepository) ListOrders(ctx context.Context, orderType, status string) ([]*Order, error) {
	query := `
		SELECT id, title, description, order_type, status, creator_id, executor_id, 
		       reward_ed, created_at, updated_at
		FROM social.player_orders
		WHERE 1=1
	`
	args := []interface{}{}
	argNum := 1

	if orderType != "" {
		query += fmt.Sprintf(" AND order_type = $%d", argNum)
		args = append(args, orderType)
		argNum++
	}

	if status != "" {
		query += fmt.Sprintf(" AND status = $%d", argNum)
		args = append(args, status)
		argNum++
	}

	query += " ORDER BY created_at DESC LIMIT 50"

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, fmt.Errorf("query failed: %w", err)
	}
	defer rows.Close()

	var orders []*Order
	for rows.Next() {
		var order Order
		err := rows.Scan(
			&order.ID, &order.Title, &order.Description, &order.OrderType,
			&order.Status, &order.CreatorID, &order.ExecutorID,
			&order.RewardEd, &order.CreatedAt, &order.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("scan failed: %w", err)
		}
		orders = append(orders, &order)
	}

	return orders, nil
}

// CreateOrder inserts a new order
func (r *OrderRepository) CreateOrder(ctx context.Context, order *Order) error {
	query := `
		INSERT INTO social.player_orders (
			id, title, description, order_type, status, creator_id, 
			reward_ed, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.Title, order.Description, order.OrderType,
		order.Status, order.CreatorID, order.RewardEd,
		order.CreatedAt, order.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("insert failed: %w", err)
	}

	return nil
}

// GetOrder retrieves a single order by ID
func (r *OrderRepository) GetOrder(ctx context.Context, orderID string) (*Order, error) {
	query := `
		SELECT id, title, description, order_type, status, creator_id, executor_id,
		       reward_ed, created_at, updated_at
		FROM social.player_orders
		WHERE id = $1
	`

	var order Order
	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&order.ID, &order.Title, &order.Description, &order.OrderType,
		&order.Status, &order.CreatorID, &order.ExecutorID,
		&order.RewardEd, &order.CreatedAt, &order.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("query failed: %w", err)
	}

	return &order, nil
}

// UpdateOrder updates an existing order
func (r *OrderRepository) UpdateOrder(ctx context.Context, order *Order) error {
	query := `
		UPDATE social.player_orders
		SET status = $2, executor_id = $3, updated_at = $4
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.Status, order.ExecutorID, order.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("update failed: %w", err)
	}

	return nil
}

