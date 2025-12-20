// Package server Issue: #1637 - P2P Trade Repository
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/trade-p2p-service-go/pkg/api"
	"github.com/google/uuid"
)

type Repository struct {
	db *sql.DB
}

func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

// CreateTradeSession creates a new trade session in database
func (r *Repository) CreateTradeSession(ctx context.Context, session *TradeSession) error {
	query := `
		INSERT INTO mvp_core.trade_sessions (
			id, initiator_id, target_id, status, zone_id, difficulty,
			initiator_offer, target_offer, initiator_confirmed, target_confirmed,
			expires_at, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	_, err := r.db.ExecContext(ctx, query,
		session.ID,
		session.InitiatorID,
		session.TargetID,
		string(session.Status),
		session.ZoneID,
		session.Difficulty,
		nil, // initiator_offer (JSONB)
		nil, // target_offer (JSONB)
		session.InitiatorConfirmed,
		session.TargetConfirmed,
		session.ExpiresAt,
		session.CreatedAt,
	)

	return err
}

// GetTradeSession retrieves a trade session by ID
func (r *Repository) GetTradeSession(ctx context.Context, sessionID uuid.UUID) (*TradeSession, error) {
	query := `
		SELECT id, initiator_id, target_id, status, zone_id, difficulty,
			   initiator_offer, target_offer, initiator_confirmed, target_confirmed,
			   expires_at, created_at
		FROM mvp_core.trade_sessions
		WHERE id = $1
	`

	var session TradeSession
	var status string
	var zoneID, difficulty sql.NullString
	var initiatorOffer, targetOffer []byte

	err := r.db.QueryRowContext(ctx, query, sessionID).Scan(
		&session.ID,
		&session.InitiatorID,
		&session.TargetID,
		&status,
		&zoneID,
		&difficulty,
		&initiatorOffer,
		&targetOffer,
		&session.InitiatorConfirmed,
		&session.TargetConfirmed,
		&session.ExpiresAt,
		&session.CreatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	// Convert status
	switch status {
	case "pending":
		session.Status = api.TradeStatusPending
	case "offering":
		session.Status = api.TradeStatusOffering
	case "confirmed":
		session.Status = api.TradeStatusConfirmed
	case "completed":
		session.Status = api.TradeStatusCompleted
	case "cancelled":
		session.Status = api.TradeStatusCancelled
	case "expired":
		session.Status = api.TradeStatusExpired
	default:
		session.Status = api.TradeStatusPending
	}

	if zoneID.Valid {
		session.ZoneID = &zoneID.String
	}

	if difficulty.Valid {
		session.Difficulty = &difficulty.String
	}

	// TODO: Deserialize offers from JSONB
	// session.InitiatorOffer = deserializeOffer(initiatorOffer)
	// session.TargetOffer = deserializeOffer(targetOffer)

	return &session, nil
}

// UpdateTradeSessionStatus updates the status of a trade session
func (r *Repository) UpdateTradeSessionStatus(ctx context.Context, sessionID uuid.UUID, status api.TradeStatus) error {
	query := `
		UPDATE mvp_core.trade_sessions
		SET status = $2, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, sessionID, string(status))
	return err
}

// UpdateTradeOffer updates a trade offer for a session
func (r *Repository) UpdateTradeOffer(ctx context.Context, sessionID uuid.UUID, isInitiator bool) error {
	var column string
	if isInitiator {
		column = "initiator_offer"
	} else {
		column = "target_offer"
	}

	// TODO: Serialize offer to JSONB
	offerJSON := []byte("{}") // Placeholder

	query := `
		UPDATE mvp_core.trade_sessions
		SET ` + column + ` = $2, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, sessionID, offerJSON)
	return err
}

// ClearTradeOffer clears a trade offer for a session
func (r *Repository) ClearTradeOffer(ctx context.Context, sessionID uuid.UUID, isInitiator bool) error {
	var column string
	if isInitiator {
		column = "initiator_offer"
	} else {
		column = "target_offer"
	}

	query := `
		UPDATE mvp_core.trade_sessions
		SET ` + column + ` = NULL, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, sessionID)
	return err
}

// SetConfirmation sets confirmation status for a party
func (r *Repository) SetConfirmation(ctx context.Context, sessionID uuid.UUID, initiatorConfirmed, targetConfirmed bool) error {
	query := `
		UPDATE mvp_core.trade_sessions
		SET initiator_confirmed = $2, target_confirmed = $3, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, sessionID, initiatorConfirmed, targetConfirmed)
	return err
}

// ResetConfirmations resets both confirmations to false
func (r *Repository) ResetConfirmations(ctx context.Context, sessionID uuid.UUID) error {
	query := `
		UPDATE mvp_core.trade_sessions
		SET initiator_confirmed = false, target_confirmed = false, updated_at = CURRENT_TIMESTAMP
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, sessionID)
	return err
}

// GetTradeHistory retrieves trade history with pagination
func (r *Repository) GetTradeHistory(ctx context.Context, limit, offset int) ([]api.TradeHistory, int, error) {
	// Get total count
	countQuery := `SELECT COUNT(*) FROM mvp_core.trade_history`
	var total int
	err := r.db.QueryRowContext(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	// Get history items
	query := `
		SELECT id, session_id, player1_id, player2_id, completed_at,
			   player1_currency, player2_currency, player1_items, player2_items, suspicious_flag
		FROM mvp_core.trade_history
		ORDER BY completed_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.QueryContext(ctx, query, limit, offset)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var history []api.TradeHistory
	for rows.Next() {
		var item api.TradeHistory
		var player1Currency, player2Currency []byte
		var player1Items, player2Items []byte

		err := rows.Scan(
			&item.ID,
			&item.SessionID,
			&item.Player1ID,
			&item.Player2ID,
			&item.CompletedAt,
			&player1Currency,
			&player2Currency,
			&player1Items,
			&player2Items,
			&item.SuspiciousFlag,
		)
		if err != nil {
			return nil, 0, err
		}

		// TODO: Deserialize JSONB fields
		// item.Player1Currency = deserializeCurrency(player1Currency)
		// item.Player2Currency = deserializeCurrency(player2Currency)
		// item.Player1Items = deserializeItems(player1Items)
		// item.Player2Items = deserializeItems(player2Items)

		history = append(history, item)
	}

	return history, total, nil
}

// SaveTradeToHistory saves a completed trade to history
func (r *Repository) SaveTradeToHistory(ctx context.Context, session *TradeSession) error {
	query := `
		INSERT INTO mvp_core.trade_history (
			id, session_id, player1_id, player2_id, completed_at,
			player1_currency, player2_currency, player1_items, player2_items, suspicious_flag
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	historyID := uuid.New()

	// TODO: Serialize offers to JSONB
	player1Currency := []byte("{}")
	player2Currency := []byte("{}")
	player1Items := []byte("[]")
	player2Items := []byte("[]")

	_, err := r.db.ExecContext(ctx, query,
		historyID,
		session.ID,
		session.InitiatorID,
		session.TargetID,
		time.Now(),
		player1Currency,
		player2Currency,
		player1Items,
		player2Items,
		false, // suspicious_flag
	)

	return err
}
