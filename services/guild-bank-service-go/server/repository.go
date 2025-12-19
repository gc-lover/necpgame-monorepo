// Issue: #1856
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for guild bank service
type Repository struct {
	db *pgxpool.Pool
}

// GuildBank internal model
type GuildBank struct {
	GuildID   uuid.UUID
	Balance   int64
	CreatedAt time.Time
	UpdatedAt time.Time
}

// BankTransaction internal model
type BankTransaction struct {
	ID          uuid.UUID
	GuildID     uuid.UUID
	UserID      uuid.UUID
	Type        string // deposit, withdrawal, tax, reward
	Amount      int64
	Description sql.NullString
	CreatedAt   time.Time
}

// NewRepository creates new repository with database connection
func NewRepository(connStr string) (*Repository, error) {
	// DB connection pool configuration (optimization)
	config, err := pgxpool.ParseConfig(connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance (OPTIMIZATION: Issue #1856)
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() {
	r.db.Close()
}

// GetGuildBank retrieves guild bank information
func (r *Repository) GetGuildBank(ctx context.Context, guildID uuid.UUID) (*GuildBank, error) {
	query := `
		SELECT guild_id, balance, created_at, updated_at
		FROM guilds.guild_banks
		WHERE guild_id = $1
	`

	var bank GuildBank
	err := r.db.QueryRow(ctx, query, guildID).Scan(
		&bank.GuildID, &bank.Balance, &bank.CreatedAt, &bank.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrGuildBankNotFound
		}
		return nil, err
	}

	return &bank, nil
}

// CreateGuildBank creates a new guild bank
func (r *Repository) CreateGuildBank(ctx context.Context, guildID uuid.UUID) (*GuildBank, error) {
	query := `
		INSERT INTO guilds.guild_banks (guild_id, balance, created_at, updated_at)
		VALUES ($1, 0, NOW(), NOW())
		ON CONFLICT (guild_id) DO NOTHING
		RETURNING guild_id, balance, created_at, updated_at
	`

	var bank GuildBank
	err := r.db.QueryRow(ctx, query, guildID).Scan(
		&bank.GuildID, &bank.Balance, &bank.CreatedAt, &bank.UpdatedAt,
	)
	if err != nil {
		return nil, err
	}

	return &bank, nil
}

// DepositToGuildBank deposits money to guild bank
func (r *Repository) DepositToGuildBank(ctx context.Context, guildID, userID uuid.UUID, amount int64, description *string) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	// Update bank balance
	bankQuery := `
		UPDATE guilds.guild_banks
		SET balance = balance + $2, updated_at = NOW()
		WHERE guild_id = $1
	`
	_, err = tx.Exec(ctx, bankQuery, guildID, amount)
	if err != nil {
		return err
	}

	// Record transaction
	var desc sql.NullString
	if description != nil {
		desc.String = *description
		desc.Valid = true
	}

	transactionQuery := `
		INSERT INTO guilds.bank_transactions (id, guild_id, user_id, type, amount, description, created_at)
		VALUES (gen_random_uuid(), $1, $2, 'deposit', $3, $4, NOW())
	`
	_, err = tx.Exec(ctx, transactionQuery, guildID, userID, amount, desc)
	if err != nil {
		return err
	}

	return tx.Commit(ctx)
}

// WithdrawFromGuildBank withdraws money from guild bank
func (r *Repository) WithdrawFromGuildBank(ctx context.Context, guildID, userID uuid.UUID, amount int64, description *string) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	// Check sufficient balance
	balanceQuery := `SELECT balance FROM guilds.guild_banks WHERE guild_id = $1 FOR UPDATE`
	var balance int64
	err = tx.QueryRow(ctx, balanceQuery, guildID).Scan(&balance)
	if err != nil {
		return err
	}

	if balance < amount {
		return ErrInsufficientFunds
	}

	// Update bank balance
	bankQuery := `
		UPDATE guilds.guild_banks
		SET balance = balance - $2, updated_at = NOW()
		WHERE guild_id = $1
	`
	_, err = tx.Exec(ctx, bankQuery, guildID, amount)
	if err != nil {
		return err
	}

	// Record transaction
	var desc sql.NullString
	if description != nil {
		desc.String = *description
		desc.Valid = true
	}

	transactionQuery := `
		INSERT INTO guilds.bank_transactions (id, guild_id, user_id, type, amount, description, created_at)
		VALUES (gen_random_uuid(), $1, $2, 'withdrawal', $3, $4, NOW())
	`
	_, err = tx.Exec(ctx, transactionQuery, guildID, userID, amount, desc)
	if err != nil {
		return err
	}

	return tx.Commit(ctx)
}

// GetBankTransactions retrieves bank transactions with pagination
func (r *Repository) GetBankTransactions(ctx context.Context, guildID uuid.UUID, limit *int, offset *int) ([]*BankTransaction, error) {
	query := `
		SELECT id, guild_id, user_id, type, amount, description, created_at
		FROM guilds.bank_transactions
		WHERE guild_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	if limit == nil {
		defaultLimit := 50
		limit = &defaultLimit
	}
	if offset == nil {
		defaultOffset := 0
		offset = &defaultOffset
	}

	rows, err := r.db.Query(ctx, query, guildID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var transactions []*BankTransaction
	for rows.Next() {
		var t BankTransaction
		var desc sql.NullString

		err := rows.Scan(&t.ID, &t.GuildID, &t.UserID, &t.Type, &t.Amount, &desc, &t.CreatedAt)
		if err != nil {
			return nil, err
		}

		t.Description = desc
		transactions = append(transactions, &t)
	}

	return transactions, rows.Err()
}

// CollectGuildTax collects tax from guild members
func (r *Repository) CollectGuildTax(ctx context.Context, guildID uuid.UUID, taxRate float64) (int64, error) {
	// This would integrate with economy-service to collect taxes
	// For now, return 0 as placeholder
	return 0, nil
}

// GrantGuildReward grants reward to guild bank
func (r *Repository) GrantGuildReward(ctx context.Context, guildID uuid.UUID, amount int64, description string) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	// Update bank balance
	bankQuery := `
		UPDATE guilds.guild_banks
		SET balance = balance + $2, updated_at = NOW()
		WHERE guild_id = $1
	`
	_, err = tx.Exec(ctx, bankQuery, guildID, amount)
	if err != nil {
		return err
	}

	// Record transaction
	transactionQuery := `
		INSERT INTO guilds.bank_transactions (id, guild_id, user_id, type, amount, description, created_at)
		VALUES (gen_random_uuid(), $1, '00000000-0000-0000-0000-000000000000'::uuid, 'reward', $2, $3, NOW())
	`
	_, err = tx.Exec(ctx, transactionQuery, guildID, amount, description)
	if err != nil {
		return err
	}

	return tx.Commit(ctx)
}

// TransferBetweenGuilds transfers money between guild banks (for future guild alliances)
func (r *Repository) TransferBetweenGuilds(ctx context.Context, fromGuildID, toGuildID uuid.UUID, amount int64, description string) error {
	// This would be implemented when guild alliances are added
	return nil
}
