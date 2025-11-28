package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/economy-service-go/models"
	"github.com/sirupsen/logrus"
)

type TradeRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewTradeRepository(db *pgxpool.Pool) *TradeRepository {
	return &TradeRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *TradeRepository) Create(ctx context.Context, session *models.TradeSession) error {
	initiatorOfferJSON, err := json.Marshal(session.InitiatorOffer)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal initiator offer JSON")
		return err
	}
	recipientOfferJSON, err := json.Marshal(session.RecipientOffer)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal recipient offer JSON")
		return err
	}

	query := `
		INSERT INTO economy.trade_sessions (
			id, initiator_id, recipient_id, initiator_offer, recipient_offer,
			initiator_confirmed, recipient_confirmed, status, zone_id,
			created_at, updated_at, expires_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12
		)`

	_, err = r.db.Exec(ctx, query,
		session.ID, session.InitiatorID, session.RecipientID,
		initiatorOfferJSON, recipientOfferJSON,
		session.InitiatorConfirmed, session.RecipientConfirmed,
		session.Status, session.ZoneID,
		session.CreatedAt, session.UpdatedAt, session.ExpiresAt,
	)

	return err
}

func (r *TradeRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.TradeSession, error) {
	var session models.TradeSession
	var initiatorOfferJSON []byte
	var recipientOfferJSON []byte
	var zoneID *uuid.UUID

	query := `
		SELECT id, initiator_id, recipient_id, initiator_offer, recipient_offer,
			initiator_confirmed, recipient_confirmed, status, zone_id,
			created_at, updated_at, expires_at, completed_at
		FROM economy.trade_sessions
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&session.ID, &session.InitiatorID, &session.RecipientID,
		&initiatorOfferJSON, &recipientOfferJSON,
		&session.InitiatorConfirmed, &session.RecipientConfirmed,
		&session.Status, &zoneID,
		&session.CreatedAt, &session.UpdatedAt, &session.ExpiresAt,
		&session.CompletedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	session.ZoneID = zoneID
	if len(initiatorOfferJSON) > 0 {
		if err := json.Unmarshal(initiatorOfferJSON, &session.InitiatorOffer); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal initiator offer JSON")
		}
	}
	if len(recipientOfferJSON) > 0 {
		if err := json.Unmarshal(recipientOfferJSON, &session.RecipientOffer); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal recipient offer JSON")
		}
	}

	return &session, nil
}

func (r *TradeRepository) GetActiveByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.TradeSession, error) {
	query := `
		SELECT id, initiator_id, recipient_id, initiator_offer, recipient_offer,
			initiator_confirmed, recipient_confirmed, status, zone_id,
			created_at, updated_at, expires_at, completed_at
		FROM economy.trade_sessions
		WHERE (initiator_id = $1 OR recipient_id = $1)
			AND status IN ('pending', 'active', 'confirmed')
			AND expires_at > NOW()
		ORDER BY created_at DESC`

	rows, err := r.db.Query(ctx, query, characterID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var sessions []models.TradeSession
	for rows.Next() {
		var session models.TradeSession
		var initiatorOfferJSON []byte
		var recipientOfferJSON []byte
		var zoneID *uuid.UUID

		err := rows.Scan(
			&session.ID, &session.InitiatorID, &session.RecipientID,
			&initiatorOfferJSON, &recipientOfferJSON,
			&session.InitiatorConfirmed, &session.RecipientConfirmed,
			&session.Status, &zoneID,
			&session.CreatedAt, &session.UpdatedAt, &session.ExpiresAt,
			&session.CompletedAt,
		)
		if err != nil {
			return nil, err
		}

		session.ZoneID = zoneID
		if len(initiatorOfferJSON) > 0 {
			if err := json.Unmarshal(initiatorOfferJSON, &session.InitiatorOffer); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal initiator offer JSON")
			}
		}
		if len(recipientOfferJSON) > 0 {
			if err := json.Unmarshal(recipientOfferJSON, &session.RecipientOffer); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal recipient offer JSON")
			}
		}

		sessions = append(sessions, session)
	}

	return sessions, nil
}

func (r *TradeRepository) Update(ctx context.Context, session *models.TradeSession) error {
	initiatorOfferJSON, err := json.Marshal(session.InitiatorOffer)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal initiator offer JSON")
		return err
	}
	recipientOfferJSON, err := json.Marshal(session.RecipientOffer)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal recipient offer JSON")
		return err
	}

	query := `
		UPDATE economy.trade_sessions
		SET initiator_offer = $1, recipient_offer = $2,
			initiator_confirmed = $3, recipient_confirmed = $4,
			status = $5, updated_at = $6, completed_at = $7
		WHERE id = $8`

	_, err = r.db.Exec(ctx, query,
		initiatorOfferJSON, recipientOfferJSON,
		session.InitiatorConfirmed, session.RecipientConfirmed,
		session.Status, session.UpdatedAt, session.CompletedAt,
		session.ID,
	)

	return err
}

func (r *TradeRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.TradeStatus) error {
	query := `
		UPDATE economy.trade_sessions
		SET status = $1, updated_at = $2
		WHERE id = $3`

	_, err := r.db.Exec(ctx, query, status, time.Now(), id)
	return err
}

func (r *TradeRepository) CreateHistory(ctx context.Context, history *models.TradeHistory) error {
	initiatorOfferJSON, err := json.Marshal(history.InitiatorOffer)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal initiator offer JSON")
		return err
	}
	recipientOfferJSON, err := json.Marshal(history.RecipientOffer)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal recipient offer JSON")
		return err
	}

	query := `
		INSERT INTO economy.trade_history (
			id, trade_session_id, initiator_id, recipient_id,
			initiator_offer, recipient_offer, status, zone_id,
			created_at, completed_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10
		)`

	_, err = r.db.Exec(ctx, query,
		history.ID, history.TradeSessionID, history.InitiatorID, history.RecipientID,
		initiatorOfferJSON, recipientOfferJSON,
		history.Status, history.ZoneID,
		history.CreatedAt, history.CompletedAt,
	)

	return err
}

func (r *TradeRepository) GetHistoryByCharacter(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.TradeHistory, error) {
	query := `
		SELECT id, trade_session_id, initiator_id, recipient_id,
			initiator_offer, recipient_offer, status, zone_id,
			created_at, completed_at
		FROM economy.trade_history
		WHERE initiator_id = $1 OR recipient_id = $1
		ORDER BY completed_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, characterID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var history []models.TradeHistory
	for rows.Next() {
		var h models.TradeHistory
		var initiatorOfferJSON []byte
		var recipientOfferJSON []byte
		var zoneID *uuid.UUID

		err := rows.Scan(
			&h.ID, &h.TradeSessionID, &h.InitiatorID, &h.RecipientID,
			&initiatorOfferJSON, &recipientOfferJSON,
			&h.Status, &zoneID,
			&h.CreatedAt, &h.CompletedAt,
		)
		if err != nil {
			return nil, err
		}

		h.ZoneID = zoneID
		if len(initiatorOfferJSON) > 0 {
			if err := json.Unmarshal(initiatorOfferJSON, &h.InitiatorOffer); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal initiator offer JSON")
			}
		}
		if len(recipientOfferJSON) > 0 {
			if err := json.Unmarshal(recipientOfferJSON, &h.RecipientOffer); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal recipient offer JSON")
			}
		}

		history = append(history, h)
	}

	return history, nil
}

func (r *TradeRepository) CountHistoryByCharacter(ctx context.Context, characterID uuid.UUID) (int, error) {
	var count int
	query := `
		SELECT COUNT(*)
		FROM economy.trade_history
		WHERE initiator_id = $1 OR recipient_id = $1`

	err := r.db.QueryRow(ctx, query, characterID).Scan(&count)
	return count, err
}

func (r *TradeRepository) CleanupExpired(ctx context.Context) error {
	query := `
		UPDATE economy.trade_sessions
		SET status = 'expired', updated_at = NOW()
		WHERE status IN ('pending', 'active', 'confirmed')
			AND expires_at < NOW()`

	_, err := r.db.Exec(ctx, query)
	return err
}

