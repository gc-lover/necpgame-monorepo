// Package server Issue: #151
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

// NewPostgresDB creates a new PostgreSQL database connection
func NewPostgresDB(connStr string) (*sql.DB, error) {
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	if err := db.Ping(); err != nil {
		return nil, err
	}

	return db, nil
}

type Repository interface {
	GetPlayerMails(ctx context.Context, playerID uuid.UUID, filter string, limit, offset int) ([]Mail, int, error)
	GetMail(ctx context.Context, mailID uuid.UUID) (*Mail, error)
	CreateMail(ctx context.Context, mail *Mail) (*uuid.UUID, error)
	DeleteMail(ctx context.Context, mailID uuid.UUID) error
	UpdateMailStatus(ctx context.Context, mailID uuid.UUID, status string) error
	MarkAsRead(ctx context.Context, mailID uuid.UUID) error
	ClaimAttachments(ctx context.Context, mailID uuid.UUID) (*Mail, error)
	GetUnreadCount(ctx context.Context, playerID uuid.UUID) (int, error)
}

type PostgresRepository struct {
	db *sql.DB
}

func NewPostgresRepository(db *sql.DB) Repository {
	return &PostgresRepository{db: db}
}

type Mail struct {
	ID          uuid.UUID
	SenderID    *uuid.UUID
	SenderName  string
	RecipientID uuid.UUID
	Type        string
	Subject     string
	Content     string
	Status      string
	Attachments *json.RawMessage
	CODAmount   *int
	SentAt      time.Time
	ReadAt      *time.Time
	ExpiresAt   *time.Time
	IsRead      bool
	IsClaimed   bool
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

func (r *PostgresRepository) GetPlayerMails(ctx context.Context, playerID uuid.UUID, filter string, limit, offset int) ([]Mail, int, error) {
	var query string
	var args []interface{}

	baseQuery := `SELECT id, sender_id, sender_name, recipient_id, type, subject, content, status, 
		attachments, cod_amount, sent_at, read_at, expires_at, is_read, is_claimed, created_at, updated_at
		FROM social.mail_messages
		WHERE recipient_id = $1 AND deleted_at IS NULL`

	args = []interface{}{playerID}

	if filter != "" && filter != "all" {
		switch filter {
		case "unread":
			baseQuery += " AND is_read = false"
		case "read":
			baseQuery += " AND is_read = true AND is_claimed = false"
		case "claimed":
			baseQuery += " AND is_claimed = true"
		}
	}

	// Count total
	countQuery := `SELECT COUNT(*) FROM social.mail_messages WHERE recipient_id = $1 AND deleted_at IS NULL`
	if filter != "" && filter != "all" {
		switch filter {
		case "unread":
			countQuery += " AND is_read = false"
		case "read":
			countQuery += " AND is_read = true AND is_claimed = false"
		case "claimed":
			countQuery += " AND is_claimed = true"
		}
	}

	var total int
	err := r.db.QueryRowContext(ctx, countQuery, playerID).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	query = baseQuery + " ORDER BY sent_at DESC LIMIT $2 OFFSET $3"
	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var mails []Mail
	for rows.Next() {
		var mail Mail
		err := rows.Scan(
			&mail.ID, &mail.SenderID, &mail.SenderName, &mail.RecipientID, &mail.Type,
			&mail.Subject, &mail.Content, &mail.Status, &mail.Attachments, &mail.CODAmount,
			&mail.SentAt, &mail.ReadAt, &mail.ExpiresAt, &mail.IsRead, &mail.IsClaimed,
			&mail.CreatedAt, &mail.UpdatedAt,
		)
		if err != nil {
			return nil, 0, err
		}
		mails = append(mails, mail)
	}

	return mails, total, rows.Err()
}

func (r *PostgresRepository) GetMail(ctx context.Context, mailID uuid.UUID) (*Mail, error) {
	var mail Mail
	query := `SELECT id, sender_id, sender_name, recipient_id, type, subject, content, status,
		attachments, cod_amount, sent_at, read_at, expires_at, is_read, is_claimed, created_at, updated_at
		FROM social.mail_messages
		WHERE id = $1 AND deleted_at IS NULL`

	err := r.db.QueryRowContext(ctx, query, mailID).Scan(
		&mail.ID, &mail.SenderID, &mail.SenderName, &mail.RecipientID, &mail.Type,
		&mail.Subject, &mail.Content, &mail.Status, &mail.Attachments, &mail.CODAmount,
		&mail.SentAt, &mail.ReadAt, &mail.ExpiresAt, &mail.IsRead, &mail.IsClaimed,
		&mail.CreatedAt, &mail.UpdatedAt,
	)
	if err == sql.ErrNoRows {
		return nil, errors.New("mail not found")
	}
	if err != nil {
		return nil, err
	}
	return &mail, nil
}

func (r *PostgresRepository) CreateMail(ctx context.Context, mail *Mail) (*uuid.UUID, error) {
	var id uuid.UUID
	query := `INSERT INTO social.mail_messages 
		(sender_id, sender_name, recipient_id, type, subject, content, status, attachments, cod_amount, expires_at, sent_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, NOW(), NOW(), NOW())
		RETURNING id`

	err := r.db.QueryRowContext(ctx, query,
		mail.SenderID, mail.SenderName, mail.RecipientID, mail.Type, mail.Subject,
		mail.Content, mail.Status, mail.Attachments, mail.CODAmount, mail.ExpiresAt,
	).Scan(&id)
	if err != nil {
		return nil, err
	}
	return &id, nil
}

func (r *PostgresRepository) DeleteMail(ctx context.Context, mailID uuid.UUID) error {
	query := `UPDATE social.mail_messages
		SET deleted_at = NOW(), updated_at = NOW()
		WHERE id = $1 AND deleted_at IS NULL`

	result, err := r.db.ExecContext(ctx, query, mailID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return errors.New("mail not found")
	}
	return nil
}

func (r *PostgresRepository) UpdateMailStatus(ctx context.Context, mailID uuid.UUID, status string) error {
	query := `UPDATE social.mail_messages
		SET status = $1, updated_at = NOW()
		WHERE id = $2 AND deleted_at IS NULL`

	result, err := r.db.ExecContext(ctx, query, status, mailID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return errors.New("mail not found")
	}
	return nil
}

func (r *PostgresRepository) MarkAsRead(ctx context.Context, mailID uuid.UUID) error {
	query := `UPDATE social.mail_messages
		SET is_read = true, read_at = NOW(), status = 'read', updated_at = NOW()
		WHERE id = $1 AND deleted_at IS NULL`

	result, err := r.db.ExecContext(ctx, query, mailID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return errors.New("mail not found")
	}
	return nil
}

func (r *PostgresRepository) ClaimAttachments(ctx context.Context, mailID uuid.UUID) (*Mail, error) {
	// Mark as claimed
	query := `UPDATE social.mail_messages
		SET is_claimed = true, status = 'claimed', updated_at = NOW()
		WHERE id = $1 AND deleted_at IS NULL AND is_claimed = false`

	result, err := r.db.ExecContext(ctx, query, mailID)
	if err != nil {
		return nil, err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return nil, err
	}
	if rows == 0 {
		return nil, errors.New("mail not found or already claimed")
	}

	// Get updated mail
	return r.GetMail(ctx, mailID)
}

func (r *PostgresRepository) GetUnreadCount(ctx context.Context, playerID uuid.UUID) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM social.mail_messages
		WHERE recipient_id = $1 AND deleted_at IS NULL AND is_read = false`

	err := r.db.QueryRowContext(ctx, query, playerID).Scan(&count)
	return count, err
}
