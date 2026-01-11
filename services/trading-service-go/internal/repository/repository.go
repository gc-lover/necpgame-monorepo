// Trading Service Repository - PostgreSQL operations
// Issue: #2260 - Trading Service Implementation
// Agent: Backend Agent
package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"necpgame/services/trading-service-go/internal/models"
)

// Repository handles database operations for trading service
// PERFORMANCE: Optimized connection pooling for high-throughput trading operations
type Repository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(ctx context.Context, logger *zap.Logger, dsn string) (*Repository, error) {
	config, err := pgxpool.ParseConfig(dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// PERFORMANCE: Optimized for trading operations
	config.MaxConns = 20                    // Higher for concurrent trades
	config.MinConns = 5                     // Keep connections ready
	config.MaxConnLifetime = 30 * time.Minute // Longer for trade sessions
	config.MaxConnIdleTime = 5 * time.Minute  // Moderate cleanup

	pool, err := pgxpool.NewWithConfig(ctx, config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return &Repository{
		db:     pool,
		logger: logger.With(zap.String("component", "repository")),
	}, nil
}

// Close closes the database connection pool
func (r *Repository) Close() {
	r.db.Close()
}

// CreateTradeSession creates a new trade session
func (r *Repository) CreateTradeSession(ctx context.Context, session *models.TradeSession) error {
	query := `
		INSERT INTO trading.trade_sessions (
			id, initiator_id, participant_id, status, currency_type,
			total_value, created_at, updated_at, expires_at, is_active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	_, err := r.db.Exec(ctx, query,
		session.ID, session.InitiatorID, session.ParticipantID, session.Status,
		session.CurrencyType, session.TotalValue, session.CreatedAt, session.UpdatedAt,
		session.ExpiresAt, session.IsActive)

	if err != nil {
		r.logger.Error("Failed to create trade session", zap.Error(err))
		return fmt.Errorf("failed to create trade session: %w", err)
	}

	r.logger.Info("Trade session created",
		zap.String("session_id", session.ID.String()),
		zap.String("initiator_id", session.InitiatorID.String()),
		zap.String("participant_id", session.ParticipantID.String()))

	return nil
}

// GetTradeSession retrieves a trade session by ID
func (r *Repository) GetTradeSession(ctx context.Context, sessionID uuid.UUID) (*models.TradeSession, error) {
	query := `
		SELECT id, initiator_id, participant_id, status, currency_type,
			   total_value, created_at, updated_at, expires_at, is_active
		FROM trading.trade_sessions
		WHERE id = $1
	`

	var session models.TradeSession
	err := r.db.QueryRow(ctx, query, sessionID).Scan(
		&session.ID, &session.InitiatorID, &session.ParticipantID, &session.Status,
		&session.CurrencyType, &session.TotalValue, &session.CreatedAt, &session.UpdatedAt,
		&session.ExpiresAt, &session.IsActive)

	if err != nil {
		r.logger.Error("Failed to get trade session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return nil, fmt.Errorf("failed to get trade session: %w", err)
	}

	return &session, nil
}

// UpdateTradeSession updates a trade session
func (r *Repository) UpdateTradeSession(ctx context.Context, session *models.TradeSession) error {
	query := `
		UPDATE trading.trade_sessions
		SET status = $2, total_value = $3, updated_at = $4, is_active = $5
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		session.ID, session.Status, session.TotalValue, session.UpdatedAt, session.IsActive)

	if err != nil {
		r.logger.Error("Failed to update trade session", zap.Error(err), zap.String("session_id", session.ID.String()))
		return fmt.Errorf("failed to update trade session: %w", err)
	}

	return nil
}

// DeleteTradeSession deletes a trade session
func (r *Repository) DeleteTradeSession(ctx context.Context, sessionID uuid.UUID) error {
	query := `DELETE FROM trading.trade_sessions WHERE id = $1`

	_, err := r.db.Exec(ctx, query, sessionID)
	if err != nil {
		r.logger.Error("Failed to delete trade session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return fmt.Errorf("failed to delete trade session: %w", err)
	}

	r.logger.Info("Trade session deleted", zap.String("session_id", sessionID.String()))
	return nil
}

// ListActiveTradeSessions returns active trade sessions for a player
func (r *Repository) ListActiveTradeSessions(ctx context.Context, playerID uuid.UUID) ([]*models.TradeSession, error) {
	query := `
		SELECT id, initiator_id, participant_id, status, currency_type,
			   total_value, created_at, updated_at, expires_at, is_active
		FROM trading.trade_sessions
		WHERE (initiator_id = $1 OR participant_id = $1) AND is_active = true
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, playerID)
	if err != nil {
		r.logger.Error("Failed to list active trade sessions", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to list active trade sessions: %w", err)
	}
	defer rows.Close()

	var sessions []*models.TradeSession
	for rows.Next() {
		var session models.TradeSession
		err := rows.Scan(
			&session.ID, &session.InitiatorID, &session.ParticipantID, &session.Status,
			&session.CurrencyType, &session.TotalValue, &session.CreatedAt, &session.UpdatedAt,
			&session.ExpiresAt, &session.IsActive)
		if err != nil {
			r.logger.Error("Failed to scan trade session", zap.Error(err))
			return nil, fmt.Errorf("failed to scan trade session: %w", err)
		}
		sessions = append(sessions, &session)
	}

	return sessions, nil
}

// CreateTradeTransaction creates a new trade transaction
func (r *Repository) CreateTradeTransaction(ctx context.Context, tx *models.TradeTransaction) error {
	query := `
		INSERT INTO trading.trade_transactions (
			id, session_id, buyer_id, seller_id, item_id, quantity,
			total_price, currency_type, transaction_fee, status, executed_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.Exec(ctx, query,
		tx.ID, tx.SessionID, tx.BuyerID, tx.SellerID, tx.ItemID, tx.Quantity,
		tx.TotalPrice, tx.CurrencyType, tx.TransactionFee, tx.Status, tx.ExecutedAt)

	if err != nil {
		r.logger.Error("Failed to create trade transaction", zap.Error(err), zap.String("transaction_id", tx.ID.String()))
		return fmt.Errorf("failed to create trade transaction: %w", err)
	}

	return nil
}

// GetTradeHistory returns trade history for a player
func (r *Repository) GetTradeHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]*models.TradeTransaction, error) {
	query := `
		SELECT id, session_id, buyer_id, seller_id, item_id, quantity,
			   total_price, currency_type, transaction_fee, status, executed_at
		FROM trading.trade_transactions
		WHERE buyer_id = $1 OR seller_id = $1
		ORDER BY executed_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		r.logger.Error("Failed to get trade history", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to get trade history: %w", err)
	}
	defer rows.Close()

	var transactions []*models.TradeTransaction
	for rows.Next() {
		var tx models.TradeTransaction
		err := rows.Scan(
			&tx.ID, &tx.SessionID, &tx.BuyerID, &tx.SellerID, &tx.ItemID, &tx.Quantity,
			&tx.TotalPrice, &tx.CurrencyType, &tx.TransactionFee, &tx.Status, &tx.ExecutedAt)
		if err != nil {
			r.logger.Error("Failed to scan trade transaction", zap.Error(err))
			return nil, fmt.Errorf("failed to scan trade transaction: %w", err)
		}
		transactions = append(transactions, &tx)
	}

	return transactions, nil
}

// ============================================================================
// TRADE ORDERS METHODS
// ============================================================================

// CreateTradeOrder creates a new trade order
func (r *Repository) CreateTradeOrder(ctx context.Context, order *models.TradeOrder) error {
	query := `
		INSERT INTO trade_orders (
			id, player_id, item_id, order_type, order_mode, item_name,
			quantity, price, min_quantity, max_quantity, filled_quantity,
			currency_type, is_active, is_partial, created_at, updated_at, expires_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17)
	`

	_, err := r.db.Exec(ctx, query,
		order.ID, order.PlayerID, order.ItemID, order.OrderType, order.OrderMode, order.ItemName,
		order.Quantity, order.Price, order.MinQuantity, order.MaxQuantity, order.FilledQuantity,
		order.CurrencyType, order.IsActive, order.IsPartial, order.CreatedAt, order.UpdatedAt, order.ExpiresAt)

	if err != nil {
		r.logger.Error("Failed to create trade order", zap.Error(err))
		return fmt.Errorf("failed to create trade order: %w", err)
	}

	return nil
}

// GetTradeOrder retrieves a trade order by ID
func (r *Repository) GetTradeOrder(ctx context.Context, orderID uuid.UUID) (*models.TradeOrder, error) {
	query := `
		SELECT id, player_id, item_id, order_type, order_mode, item_name,
			   quantity, price, min_quantity, max_quantity, filled_quantity,
			   currency_type, is_active, is_partial, created_at, updated_at, expires_at
		FROM trade_orders WHERE id = $1
	`

	var order models.TradeOrder
	err := r.db.QueryRow(ctx, query, orderID).Scan(
		&order.ID, &order.PlayerID, &order.ItemID, &order.OrderType, &order.OrderMode, &order.ItemName,
		&order.Quantity, &order.Price, &order.MinQuantity, &order.MaxQuantity, &order.FilledQuantity,
		&order.CurrencyType, &order.IsActive, &order.IsPartial, &order.CreatedAt, &order.UpdatedAt, &order.ExpiresAt)

	if err != nil {
		r.logger.Error("Failed to get trade order", zap.Error(err))
		return nil, fmt.Errorf("failed to get trade order: %w", err)
	}

	return &order, nil
}

// UpdateTradeOrder updates a trade order
func (r *Repository) UpdateTradeOrder(ctx context.Context, order *models.TradeOrder) error {
	query := `
		UPDATE trade_orders
		SET filled_quantity = $2, is_active = $3, updated_at = $4
		WHERE id = $1
	`

	order.UpdatedAt = time.Now()
	_, err := r.db.Exec(ctx, query, order.ID, order.FilledQuantity, order.IsActive, order.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to update trade order", zap.Error(err))
		return fmt.Errorf("failed to update trade order: %w", err)
	}

	return nil
}

// CancelTradeOrder cancels an active trade order
func (r *Repository) CancelTradeOrder(ctx context.Context, orderID uuid.UUID) error {
	query := `
		UPDATE trade_orders
		SET is_active = false, updated_at = $2
		WHERE id = $1 AND is_active = true
	`

	_, err := r.db.Exec(ctx, query, orderID, time.Now())

	if err != nil {
		r.logger.Error("Failed to cancel trade order", zap.Error(err))
		return fmt.Errorf("failed to cancel trade order: %w", err)
	}

	return nil
}

// ListPlayerTradeOrders lists trade orders for a specific player
func (r *Repository) ListPlayerTradeOrders(ctx context.Context, playerID uuid.UUID, status, orderType string) ([]*models.TradeOrder, error) {
	query := `
		SELECT id, player_id, item_id, order_type, order_mode, item_name,
			   quantity, price, min_quantity, max_quantity, filled_quantity,
			   currency_type, is_active, is_partial, created_at, updated_at, expires_at
		FROM trade_orders
		WHERE player_id = $1
	`

	args := []interface{}{playerID}
	argCount := 1

	if status != "" {
		argCount++
		query += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, status)
	}

	if orderType != "" {
		argCount++
		query += fmt.Sprintf(" AND order_type = $%d", argCount)
		args = append(args, orderType)
	}

	query += " ORDER BY created_at DESC LIMIT 100"

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to list trade orders", zap.Error(err))
		return nil, fmt.Errorf("failed to list trade orders: %w", err)
	}
	defer rows.Close()

	var orders []*models.TradeOrder
	for rows.Next() {
		var order models.TradeOrder
		err := rows.Scan(
			&order.ID, &order.PlayerID, &order.ItemID, &order.OrderType, &order.OrderMode, &order.ItemName,
			&order.Quantity, &order.Price, &order.MinQuantity, &order.MaxQuantity, &order.FilledQuantity,
			&order.CurrencyType, &order.IsActive, &order.IsPartial, &order.CreatedAt, &order.UpdatedAt, &order.ExpiresAt)
		if err != nil {
			r.logger.Error("Failed to scan trade order", zap.Error(err))
			return nil, fmt.Errorf("failed to scan trade order: %w", err)
		}
		orders = append(orders, &order)
	}

	return orders, nil
}

// GetMatchingOrders retrieves orders that can be matched with the given order
func (r *Repository) GetMatchingOrders(ctx context.Context, itemID uuid.UUID, orderType, orderMode string) ([]*models.TradeOrder, error) {
	query := `
		SELECT id, player_id, item_id, order_type, order_mode, item_name,
			   quantity, price, min_quantity, max_quantity, filled_quantity,
			   currency_type, is_active, is_partial, created_at, updated_at, expires_at
		FROM trade_orders
		WHERE item_id = $1 AND order_type = $2 AND order_mode = $3 AND is_active = true
		AND expires_at > NOW()
		ORDER BY
			CASE WHEN order_type = 'buy' THEN -price ELSE price END,
			created_at ASC
		LIMIT 50
	`

	rows, err := r.db.Query(ctx, query, itemID, orderType, orderMode)
	if err != nil {
		r.logger.Error("Failed to get matching orders", zap.Error(err))
		return nil, fmt.Errorf("failed to get matching orders: %w", err)
	}
	defer rows.Close()

	var orders []*models.TradeOrder
	for rows.Next() {
		var order models.TradeOrder
		err := rows.Scan(
			&order.ID, &order.PlayerID, &order.ItemID, &order.OrderType, &order.OrderMode, &order.ItemName,
			&order.Quantity, &order.Price, &order.MinQuantity, &order.MaxQuantity, &order.FilledQuantity,
			&order.CurrencyType, &order.IsActive, &order.IsPartial, &order.CreatedAt, &order.UpdatedAt, &order.ExpiresAt)
		if err != nil {
			r.logger.Error("Failed to scan matching order", zap.Error(err))
			return nil, fmt.Errorf("failed to scan matching order: %w", err)
		}
		orders = append(orders, &order)
	}

	return orders, nil
}

// ============================================================================
// TRADE CONTRACTS METHODS
// ============================================================================

// CreateTradeContract creates a new trade contract
func (r *Repository) CreateTradeContract(ctx context.Context, contract *models.TradeContract) error {
	query := `
		INSERT INTO trade_contracts (
			id, seller_id, buyer_id, contract_type, status, item_name,
			total_quantity, delivered_quantity, unit_price, escrow_amount,
			delivery_deadline, expires_at, is_escrow_active, is_completed,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16)
	`

	_, err := r.db.Exec(ctx, query,
		contract.ID, contract.SellerID, contract.BuyerID, contract.ContractType, contract.Status, contract.ItemName,
		contract.TotalQuantity, contract.DeliveredQuantity, contract.UnitPrice, contract.EscrowAmount,
		contract.DeliveryDeadline, contract.ExpiresAt, contract.IsEscrowActive, contract.IsCompleted,
		contract.CreatedAt, contract.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to create trade contract", zap.Error(err))
		return fmt.Errorf("failed to create trade contract: %w", err)
	}

	return nil
}

// GetTradeContract retrieves a trade contract by ID
func (r *Repository) GetTradeContract(ctx context.Context, contractID uuid.UUID) (*models.TradeContract, error) {
	query := `
		SELECT id, seller_id, buyer_id, contract_type, status, item_name,
			   total_quantity, delivered_quantity, unit_price, escrow_amount,
			   delivery_deadline, expires_at, is_escrow_active, is_completed,
			   created_at, updated_at
		FROM trade_contracts WHERE id = $1
	`

	var contract models.TradeContract
	err := r.db.QueryRow(ctx, query, contractID).Scan(
		&contract.ID, &contract.SellerID, &contract.BuyerID, &contract.ContractType, &contract.Status, &contract.ItemName,
		&contract.TotalQuantity, &contract.DeliveredQuantity, &contract.UnitPrice, &contract.EscrowAmount,
		&contract.DeliveryDeadline, &contract.ExpiresAt, &contract.IsEscrowActive, &contract.IsCompleted,
		&contract.CreatedAt, &contract.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to get trade contract", zap.Error(err))
		return nil, fmt.Errorf("failed to get trade contract: %w", err)
	}

	// Load deliveries
	deliveries, err := r.getContractDeliveries(ctx, contractID)
	if err != nil {
		r.logger.Warn("Failed to load contract deliveries", zap.Error(err))
	}
	contract.Deliveries = deliveries

	return &contract, nil
}

// getContractDeliveries retrieves deliveries for a contract
func (r *Repository) getContractDeliveries(ctx context.Context, contractID uuid.UUID) ([]models.ContractDelivery, error) {
	query := `SELECT id, contract_id, quantity, delivered_at, status FROM contract_deliveries WHERE contract_id = $1 ORDER BY delivered_at`

	rows, err := r.db.Query(ctx, query, contractID)
	if err != nil {
		return nil, fmt.Errorf("failed to get contract deliveries: %w", err)
	}
	defer rows.Close()

	var deliveries []models.ContractDelivery
	for rows.Next() {
		var delivery models.ContractDelivery
		err := rows.Scan(&delivery.ID, &delivery.ContractID, &delivery.Quantity, &delivery.DeliveredAt, &delivery.Status)
		if err != nil {
			return nil, fmt.Errorf("failed to scan contract delivery: %w", err)
		}
		deliveries = append(deliveries, delivery)
	}

	return deliveries, nil
}

// ============================================================================
// AUCTION METHODS
// ============================================================================

// CreateAuction creates a new auction
func (r *Repository) CreateAuction(ctx context.Context, auction *models.Auction) error {
	query := `
		INSERT INTO auctions (
			id, seller_id, item_id, item_name, auction_type, status,
			starting_price, current_price, reserve_price, quantity,
			created_at, updated_at, ends_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)
	`

	_, err := r.db.Exec(ctx, query,
		auction.ID, auction.SellerID, auction.ItemID, auction.ItemName, auction.AuctionType, auction.Status,
		auction.StartingPrice, auction.CurrentPrice, auction.ReservePrice, auction.Quantity,
		auction.CreatedAt, auction.UpdatedAt, auction.EndsAt)

	if err != nil {
		r.logger.Error("Failed to create auction", zap.Error(err))
		return fmt.Errorf("failed to create auction: %w", err)
	}

	return nil
}

// GetAuction retrieves an auction by ID
func (r *Repository) GetAuction(ctx context.Context, auctionID uuid.UUID) (*models.Auction, error) {
	query := `
		SELECT id, seller_id, item_id, item_name, auction_type, status,
			   starting_price, current_price, reserve_price, quantity,
			   created_at, updated_at, ends_at, winner_id, final_price
		FROM auctions WHERE id = $1
	`

	var auction models.Auction
	err := r.db.QueryRow(ctx, query, auctionID).Scan(
		&auction.ID, &auction.SellerID, &auction.ItemID, &auction.ItemName, &auction.AuctionType, &auction.Status,
		&auction.StartingPrice, &auction.CurrentPrice, &auction.ReservePrice, &auction.Quantity,
		&auction.CreatedAt, &auction.UpdatedAt, &auction.EndsAt, &auction.WinnerID, &auction.FinalPrice)

	if err != nil {
		r.logger.Error("Failed to get auction", zap.Error(err))
		return nil, fmt.Errorf("failed to get auction: %w", err)
	}

	// Load bids
	bids, err := r.getAuctionBids(ctx, auctionID)
	if err != nil {
		r.logger.Warn("Failed to load auction bids", zap.Error(err))
	}
	auction.Bids = bids

	return &auction, nil
}

// PlaceAuctionBid places a bid on an auction
func (r *Repository) PlaceAuctionBid(ctx context.Context, bid *models.AuctionBid) error {
	// First, update the auction's current price
	_, err := r.db.Exec(ctx, "UPDATE auctions SET current_price = $2, updated_at = $3 WHERE id = $1 AND current_price < $2",
		bid.AuctionID, bid.Amount, time.Now())
	if err != nil {
		r.logger.Error("Failed to update auction price", zap.Error(err))
		return fmt.Errorf("failed to update auction price: %w", err)
	}

	// Then, insert the bid
	query := `INSERT INTO auction_bids (id, auction_id, bidder_id, amount, bid_time, is_winning) VALUES ($1, $2, $3, $4, $5, $6)`

	_, err = r.db.Exec(ctx, query, bid.ID, bid.AuctionID, bid.BidderID, bid.Amount, bid.BidTime, bid.IsWinning)
	if err != nil {
		r.logger.Error("Failed to place auction bid", zap.Error(err))
		return fmt.Errorf("failed to place auction bid: %w", err)
	}

	return nil
}

// getAuctionBids retrieves bids for an auction
func (r *Repository) getAuctionBids(ctx context.Context, auctionID uuid.UUID) ([]models.AuctionBid, error) {
	query := `SELECT id, auction_id, bidder_id, amount, bid_time, is_winning FROM auction_bids WHERE auction_id = $1 ORDER BY bid_time DESC`

	rows, err := r.db.Query(ctx, query, auctionID)
	if err != nil {
		return nil, fmt.Errorf("failed to get auction bids: %w", err)
	}
	defer rows.Close()

	var bids []models.AuctionBid
	for rows.Next() {
		var bid models.AuctionBid
		err := rows.Scan(&bid.ID, &bid.AuctionID, &bid.BidderID, &bid.Amount, &bid.BidTime, &bid.IsWinning)
		if err != nil {
			return nil, fmt.Errorf("failed to scan auction bid: %w", err)
		}
		bids = append(bids, bid)
	}

	return bids, nil
}