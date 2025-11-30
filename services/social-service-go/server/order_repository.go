// Issue: #141888033
package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type OrderRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewOrderRepository(db *pgxpool.Pool) *OrderRepository {
	return &OrderRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *OrderRepository) Create(ctx context.Context, order *models.PlayerOrder) error {
	rewardJSON, _ := json.Marshal(order.Reward)
	requirementsJSON, _ := json.Marshal(order.Requirements)

	query := `
		INSERT INTO social.player_orders (
			id, customer_id, executor_id, order_type, title, description,
			status, reward, requirements, deadline, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12
		)`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.CustomerID, order.ExecutorID, order.OrderType,
		order.Title, order.Description, order.Status,
		rewardJSON, requirementsJSON, order.Deadline,
		order.CreatedAt, order.UpdatedAt,
	)

	return err
}

func (r *OrderRepository) GetByID(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrder, error) {
	var order models.PlayerOrder
	var rewardJSON []byte
	var requirementsJSON []byte
	var executorID *uuid.UUID

	query := `
		SELECT id, customer_id, executor_id, order_type, title, description,
			status, reward, requirements, deadline, created_at, updated_at, completed_at
		FROM social.player_orders
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&order.ID, &order.CustomerID, &executorID, &order.OrderType,
		&order.Title, &order.Description, &order.Status,
		&rewardJSON, &requirementsJSON, &order.Deadline,
		&order.CreatedAt, &order.UpdatedAt, &order.CompletedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	order.ExecutorID = executorID
	if len(rewardJSON) > 0 {
		json.Unmarshal(rewardJSON, &order.Reward)
	}
	if len(requirementsJSON) > 0 {
		json.Unmarshal(requirementsJSON, &order.Requirements)
	}

	return &order, nil
}

func (r *OrderRepository) List(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus, limit, offset int) ([]models.PlayerOrder, error) {
	query := `
		SELECT id, customer_id, executor_id, order_type, title, description,
			status, reward, requirements, deadline, created_at, updated_at, completed_at
		FROM social.player_orders
		WHERE 1=1`
	args := []interface{}{}
	argPos := 1

	if orderType != nil {
		query += ` AND order_type = $` + strconv.Itoa(argPos)
		args = append(args, *orderType)
		argPos++
	}

	if status != nil {
		query += ` AND status = $` + strconv.Itoa(argPos)
		args = append(args, *status)
		argPos++
	}

	query += ` ORDER BY created_at DESC LIMIT $` + strconv.Itoa(argPos) + ` OFFSET $` + strconv.Itoa(argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var orders []models.PlayerOrder
	for rows.Next() {
		var order models.PlayerOrder
		var rewardJSON []byte
		var requirementsJSON []byte
		var executorID *uuid.UUID

		err := rows.Scan(
			&order.ID, &order.CustomerID, &executorID, &order.OrderType,
			&order.Title, &order.Description, &order.Status,
			&rewardJSON, &requirementsJSON, &order.Deadline,
			&order.CreatedAt, &order.UpdatedAt, &order.CompletedAt,
		)
		if err != nil {
			return nil, err
		}

		order.ExecutorID = executorID
		if len(rewardJSON) > 0 {
			if err := json.Unmarshal(rewardJSON, &order.Reward); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal reward JSON")
				order.Reward = make(map[string]interface{})
			}
		}
		if len(requirementsJSON) > 0 {
			if err := json.Unmarshal(requirementsJSON, &order.Requirements); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal requirements JSON")
				order.Requirements = make(map[string]interface{})
			}
		}

		orders = append(orders, order)
	}

	return orders, nil
}

func (r *OrderRepository) Count(ctx context.Context, orderType *models.OrderType, status *models.OrderStatus) (int, error) {
	query := `SELECT COUNT(*) FROM social.player_orders WHERE 1=1`
	args := []interface{}{}
	argPos := 1

	if orderType != nil {
		query += ` AND order_type = $` + strconv.Itoa(argPos)
		args = append(args, *orderType)
		argPos++
	}

	if status != nil {
		query += ` AND status = $` + strconv.Itoa(argPos)
		args = append(args, *status)
		argPos++
	}

	var count int
	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

func (r *OrderRepository) UpdateStatus(ctx context.Context, orderID uuid.UUID, status models.OrderStatus) error {
	query := `
		UPDATE social.player_orders
		SET status = $1, updated_at = $2
		WHERE id = $3`

	_, err := r.db.Exec(ctx, query, status, time.Now(), orderID)
	return err
}

func (r *OrderRepository) AcceptOrder(ctx context.Context, orderID, executorID uuid.UUID) error {
	query := `
		UPDATE social.player_orders
		SET executor_id = $1, status = $2, updated_at = $3
		WHERE id = $4 AND status = $5`

	_, err := r.db.Exec(ctx, query, executorID, models.OrderStatusAccepted, time.Now(), orderID, models.OrderStatusOpen)
	return err
}

func (r *OrderRepository) StartOrder(ctx context.Context, orderID uuid.UUID) error {
	query := `
		UPDATE social.player_orders
		SET status = $1, updated_at = $2
		WHERE id = $3 AND status = $4`

	_, err := r.db.Exec(ctx, query, models.OrderStatusInProgress, time.Now(), orderID, models.OrderStatusAccepted)
	return err
}

func (r *OrderRepository) CompleteOrder(ctx context.Context, orderID uuid.UUID) error {
	query := `
		UPDATE social.player_orders
		SET status = $1, completed_at = $2, updated_at = $3
		WHERE id = $4 AND status = $5`

	_, err := r.db.Exec(ctx, query, models.OrderStatusCompleted, time.Now(), time.Now(), orderID, models.OrderStatusInProgress)
	return err
}

func (r *OrderRepository) CancelOrder(ctx context.Context, orderID uuid.UUID) error {
	query := `
		UPDATE social.player_orders
		SET status = $1, updated_at = $2
		WHERE id = $3 AND status NOT IN ($4, $5)`

	_, err := r.db.Exec(ctx, query, models.OrderStatusCancelled, time.Now(), orderID, models.OrderStatusCompleted, models.OrderStatusCancelled)
	return err
}

func (r *OrderRepository) CreateReview(ctx context.Context, review *models.PlayerOrderReview) error {
	query := `
		INSERT INTO social.player_order_reviews (
			id, order_id, reviewer_id, executor_id, rating, comment, created_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7
		)`

	_, err := r.db.Exec(ctx, query,
		review.ID, review.OrderID, review.ReviewerID, review.ExecutorID,
		review.Rating, review.Comment, review.CreatedAt,
	)

	return err
}

func (r *OrderRepository) GetReviewByOrderID(ctx context.Context, orderID uuid.UUID) (*models.PlayerOrderReview, error) {
	var review models.PlayerOrderReview

	query := `
		SELECT id, order_id, reviewer_id, executor_id, rating, comment, created_at
		FROM social.player_order_reviews
		WHERE order_id = $1`

	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&review.ID, &review.OrderID, &review.ReviewerID, &review.ExecutorID,
		&review.Rating, &review.Comment, &review.CreatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &review, nil
}

