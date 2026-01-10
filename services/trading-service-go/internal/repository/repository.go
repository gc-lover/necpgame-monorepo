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