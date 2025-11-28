package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/social-service-go/models"
)

func (r *GuildRepository) GetBank(ctx context.Context, guildID uuid.UUID) (*models.GuildBank, error) {
	var bank models.GuildBank
	var currencyJSON []byte
	var itemsJSON []byte

	query := `
		SELECT id, guild_id, currency, items, updated_at
		FROM social.guild_banks
		WHERE guild_id = $1`

	err := r.db.QueryRow(ctx, query, guildID).Scan(
		&bank.ID, &bank.GuildID, &currencyJSON, &itemsJSON, &bank.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(currencyJSON) > 0 {
		if err := json.Unmarshal(currencyJSON, &bank.Currency); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal bank currency JSON")
		}
	}
	if len(itemsJSON) > 0 {
		if err := json.Unmarshal(itemsJSON, &bank.Items); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal bank items JSON")
		}
	}

	return &bank, nil
}

func (r *GuildRepository) CreateBank(ctx context.Context, bank *models.GuildBank) error {
	currencyJSON, err := json.Marshal(bank.Currency)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal bank currency JSON")
		return err
	}
	itemsJSON, err := json.Marshal(bank.Items)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal bank items JSON")
		return err
	}

	query := `
		INSERT INTO social.guild_banks (
			id, guild_id, currency, items, updated_at
		) VALUES (
			$1, $2, $3, $4, $5
		)`

	_, err = r.db.Exec(ctx, query,
		bank.ID, bank.GuildID, currencyJSON, itemsJSON, bank.UpdatedAt,
	)

	return err
}

func (r *GuildRepository) UpdateBank(ctx context.Context, bank *models.GuildBank) error {
	currencyJSON, err := json.Marshal(bank.Currency)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal bank currency JSON")
		return err
	}
	itemsJSON, err := json.Marshal(bank.Items)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal bank items JSON")
		return err
	}

	query := `
		UPDATE social.guild_banks
		SET currency = $1, items = $2, updated_at = $3
		WHERE guild_id = $4`

	_, err = r.db.Exec(ctx, query, currencyJSON, itemsJSON, time.Now(), bank.GuildID)
	return err
}

func (r *GuildRepository) CreateBankTransaction(ctx context.Context, transaction *models.GuildBankTransaction) error {
	itemsJSON, err := json.Marshal(transaction.Items)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal transaction items JSON")
		return err
	}

	query := `
		INSERT INTO social.guild_bank_transactions (
			id, guild_id, account_id, type, currency, items, created_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7
		)`

	_, err = r.db.Exec(ctx, query,
		transaction.ID, transaction.GuildID, transaction.AccountID,
		transaction.Type, transaction.Currency, itemsJSON, transaction.CreatedAt,
	)

	return err
}

func (r *GuildRepository) GetBankTransactions(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]models.GuildBankTransaction, error) {
	query := `
		SELECT id, guild_id, account_id, type, currency, items, created_at
		FROM social.guild_bank_transactions
		WHERE guild_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, guildID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var transactions []models.GuildBankTransaction
	for rows.Next() {
		var transaction models.GuildBankTransaction
		var itemsJSON []byte

		err := rows.Scan(
			&transaction.ID, &transaction.GuildID, &transaction.AccountID,
			&transaction.Type, &transaction.Currency, &itemsJSON, &transaction.CreatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(itemsJSON) > 0 {
			if err := json.Unmarshal(itemsJSON, &transaction.Items); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal transaction items JSON")
			}
		}

		transactions = append(transactions, transaction)
	}

	return transactions, rows.Err()
}

func (r *GuildRepository) CountBankTransactions(ctx context.Context, guildID uuid.UUID) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM social.guild_bank_transactions WHERE guild_id = $1`

	err := r.db.QueryRow(ctx, query, guildID).Scan(&count)
	return count, err
}

