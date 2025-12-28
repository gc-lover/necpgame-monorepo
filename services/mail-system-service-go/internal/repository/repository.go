package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"services/mail-system-service-go/pkg/models"
)

// Repository handles database operations for mail system
type Repository struct {
	db     *sqlx.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *sqlx.DB, redis *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// NewDBConnection creates a new database connection
func NewDBConnection(url string) (*sqlx.DB, error) {
	db, err := sqlx.Connect("postgres", url)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// NewRedisClient creates a new Redis client
func NewRedisClient(url string) (*redis.Client, error) {
	opts, err := redis.ParseURL(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	client := redis.NewClient(opts)

	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// GetMailbox retrieves mails for a user with pagination and filtering
func (r *Repository) GetMailbox(ctx context.Context, userID uuid.UUID, folder, status, category string, limit, offset int) ([]models.MailSummary, int, error) {
	query := `
		SELECT
			m.id,
			COALESCE(p.display_name, 'System') as sender_name,
			m.subject,
			m.category,
			m.priority,
			m.sent_at,
			m.expires_at,
			CASE WHEN COUNT(a.id) > 0 THEN true ELSE false END as has_attachments,
			CASE WHEN m.read_at IS NOT NULL THEN true ELSE false END as is_read,
			COUNT(*) OVER() as total_count
		FROM mails m
		LEFT JOIN players p ON m.sender_id = p.id
		LEFT JOIN mail_attachments a ON m.id = a.mail_id
		WHERE m.recipient_id = $1
		AND m.is_deleted = false
	`

	args := []interface{}{userID}
	argCount := 1

	if folder != "" && folder != "all" {
		argCount++
		query += fmt.Sprintf(" AND m.folder = $%d", argCount)
		args = append(args, folder)
	}

	if status == "unread" {
		query += " AND m.read_at IS NULL"
	} else if status == "read" {
		query += " AND m.read_at IS NOT NULL"
	}

	if category != "" && category != "all" {
		argCount++
		query += fmt.Sprintf(" AND m.category = $%d", argCount)
		args = append(args, category)
	}

	query += `
		GROUP BY m.id, p.display_name, m.subject, m.category, m.priority, m.sent_at, m.expires_at, m.read_at
		ORDER BY m.sent_at DESC
		LIMIT $` + fmt.Sprintf("%d", argCount+1) + ` OFFSET $` + fmt.Sprintf("%d", argCount+2)

	args = append(args, limit, offset)

	var mails []models.MailSummary
	var totalCount int

	rows, err := r.db.QueryxContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query mailbox: %w", err)
	}
	defer rows.Close()

	for rows.Next() {
		var mail models.MailSummary
		err := rows.Scan(
			&mail.ID,
			&mail.SenderName,
			&mail.Subject,
			&mail.Category,
			&mail.Priority,
			&mail.SentAt,
			&mail.ExpiresAt,
			&mail.HasAttachments,
			&mail.IsRead,
			&totalCount,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan mail row: %w", err)
		}
		mails = append(mails, mail)
	}

	return mails, totalCount, nil
}

// GetMail retrieves a specific mail by ID
func (r *Repository) GetMail(ctx context.Context, mailID, userID uuid.UUID) (*models.Mail, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("mail:%s", mailID)
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var mail models.Mail
		if err := json.Unmarshal([]byte(cached), &mail); err == nil {
			return &mail, nil
		}
	}

	query := `
		SELECT
			m.id,
			m.sender_id,
			m.recipient_id,
			COALESCE(p.display_name, 'System') as sender_name,
			m.subject,
			m.category,
			m.priority,
			m.sent_at,
			m.read_at,
			m.expires_at,
			m.folder,
			m.content,
			ARRAY_AGG(
				JSON_BUILD_OBJECT(
					'attachment_id', a.id,
					'mail_id', a.mail_id,
					'filename', a.filename,
					'content_type', a.content_type,
					'size_bytes', a.size_bytes
				)
			) FILTER (WHERE a.id IS NOT NULL) as attachments
		FROM mails m
		LEFT JOIN players p ON m.sender_id = p.id
		LEFT JOIN mail_attachments a ON m.id = a.mail_id
		WHERE m.id = $1 AND m.recipient_id = $2 AND m.is_deleted = false
		GROUP BY m.id, m.sender_id, m.recipient_id, p.display_name, m.subject,
				 m.category, m.priority, m.sent_at, m.read_at, m.expires_at, m.folder, m.content
	`

	var mail models.Mail
	var contentJSON []byte
	var attachmentsJSON []byte

	err := r.db.QueryRowxContext(ctx, query, mailID, userID).Scan(
		&mail.ID,
		&mail.SenderID,
		&mail.RecipientID,
		&mail.SenderName,
		&mail.Subject,
		&mail.Category,
		&mail.Priority,
		&mail.SentAt,
		&mail.ReadAt,
		&mail.ExpiresAt,
		&mail.Folder,
		&contentJSON,
		&attachmentsJSON,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("mail not found")
		}
		return nil, fmt.Errorf("failed to get mail: %w", err)
	}

	// Parse content
	if err := json.Unmarshal(contentJSON, &mail.Content); err != nil {
		return nil, fmt.Errorf("failed to parse mail content: %w", err)
	}

	// Parse attachments
	if attachmentsJSON != nil {
		if err := json.Unmarshal(attachmentsJSON, &mail.Attachments); err != nil {
			r.logger.Warn("Failed to parse attachments", zap.Error(err))
		}
	}

	// Cache the result
	if data, err := json.Marshal(mail); err == nil {
		r.redis.Set(ctx, cacheKey, data, 10*time.Minute)
	}

	return &mail, nil
}

// SendMail creates a new mail
func (r *Repository) SendMail(ctx context.Context, mail *models.Mail) error {
	contentJSON, err := json.Marshal(mail.Content)
	if err != nil {
		return fmt.Errorf("failed to marshal content: %w", err)
	}

	tx, err := r.db.BeginTxx(ctx, nil)
	if err != nil {
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Insert mail
	query := `
		INSERT INTO mails (
			id, sender_id, recipient_id, subject, category, priority,
			sent_at, expires_at, folder, content
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10
		)
	`

	expiresAt := mail.SentAt.Add(time.Duration(168) * time.Hour) // Default 7 days
	if mail.ExpiresAt != nil {
		expiresAt = *mail.ExpiresAt
	}

	_, err = tx.ExecContext(ctx, query,
		mail.ID, mail.SenderID, mail.RecipientID, mail.Subject,
		mail.Category, mail.Priority, mail.SentAt, expiresAt,
		"inbox", contentJSON,
	)
	if err != nil {
		return fmt.Errorf("failed to insert mail: %w", err)
	}

	// Insert attachments if any
	for _, attachment := range mail.Attachments {
		_, err = tx.ExecContext(ctx, `
			INSERT INTO mail_attachments (
				id, mail_id, filename, content_type, size_bytes, data
			) VALUES ($1, $2, $3, $4, $5, $6)
		`, attachment.ID, mail.ID, attachment.Filename,
		   attachment.ContentType, attachment.SizeBytes, attachment.Data)
		if err != nil {
			return fmt.Errorf("failed to insert attachment: %w", err)
		}
	}

	if err := tx.Commit(); err != nil {
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	// Invalidate cache
	r.invalidateUserCache(ctx, mail.RecipientID)

	return nil
}

// MarkAsRead marks a mail as read
func (r *Repository) MarkAsRead(ctx context.Context, mailID, userID uuid.UUID) error {
	query := `
		UPDATE mails
		SET read_at = $1
		WHERE id = $2 AND recipient_id = $3 AND read_at IS NULL
	`

	result, err := r.db.ExecContext(ctx, query, time.Now(), mailID, userID)
	if err != nil {
		return fmt.Errorf("failed to mark mail as read: %w", err)
	}

	if rows, _ := result.RowsAffected(); rows == 0 {
		return fmt.Errorf("mail not found or already read")
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("mail:%s", mailID)
	r.redis.Del(ctx, cacheKey)
	r.invalidateUserCache(ctx, userID)

	return nil
}

// DeleteMail marks a mail as deleted
func (r *Repository) DeleteMail(ctx context.Context, mailID, userID uuid.UUID) error {
	query := `
		UPDATE mails
		SET is_deleted = true, folder = 'trash'
		WHERE id = $1 AND recipient_id = $2 AND is_deleted = false
	`

	result, err := r.db.ExecContext(ctx, query, mailID, userID)
	if err != nil {
		return fmt.Errorf("failed to delete mail: %w", err)
	}

	if rows, _ := result.RowsAffected(); rows == 0 {
		return fmt.Errorf("mail not found")
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("mail:%s", mailID)
	r.redis.Del(ctx, cacheKey)
	r.invalidateUserCache(ctx, userID)

	return nil
}

// ArchiveMail moves mail to archive
func (r *Repository) ArchiveMail(ctx context.Context, mailID, userID uuid.UUID) error {
	query := `
		UPDATE mails
		SET folder = 'archived', is_archived = true
		WHERE id = $1 AND recipient_id = $2 AND is_deleted = false
	`

	result, err := r.db.ExecContext(ctx, query, mailID, userID)
	if err != nil {
		return fmt.Errorf("failed to archive mail: %w", err)
	}

	if rows, _ := result.RowsAffected(); rows == 0 {
		return fmt.Errorf("mail not found")
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("mail:%s", mailID)
	r.redis.Del(ctx, cacheKey)
	r.invalidateUserCache(ctx, userID)

	return nil
}

// GetAttachment retrieves a mail attachment
func (r *Repository) GetAttachment(ctx context.Context, attachmentID, userID uuid.UUID) (*models.Attachment, error) {
	query := `
		SELECT a.id, a.mail_id, a.filename, a.content_type, a.size_bytes, a.data
		FROM mail_attachments a
		JOIN mails m ON a.mail_id = m.id
		WHERE a.id = $1 AND m.recipient_id = $2 AND m.is_deleted = false
	`

	var attachment models.Attachment
	err := r.db.QueryRowxContext(ctx, query, attachmentID, userID).Scan(
		&attachment.ID,
		&attachment.MailID,
		&attachment.Filename,
		&attachment.ContentType,
		&attachment.SizeBytes,
		&attachment.Data,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("attachment not found")
		}
		return nil, fmt.Errorf("failed to get attachment: %w", err)
	}

	return &attachment, nil
}

// ReportMail creates a moderation report for a mail
func (r *Repository) ReportMail(ctx context.Context, report *models.Report) error {
	query := `
		INSERT INTO mail_reports (
			id, mail_id, reporter_id, reason, description, severity, submitted_at, status
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		report.ID, report.MailID, report.ReporterID, report.Reason,
		report.Description, report.Severity, report.SubmittedAt, "submitted",
	)

	if err != nil {
		return fmt.Errorf("failed to create report: %w", err)
	}

	return nil
}

// GetUnreadCount returns the count of unread mails for a user
func (r *Repository) GetUnreadCount(ctx context.Context, userID uuid.UUID) (int, error) {
	query := `
		SELECT COUNT(*) FROM mails
		WHERE recipient_id = $1 AND read_at IS NULL AND is_deleted = false
	`

	var count int
	err := r.db.GetContext(ctx, &count, query, userID)
	if err != nil {
		return 0, fmt.Errorf("failed to get unread count: %w", err)
	}

	return count, nil
}

// InvalidateUserCache clears cache for a user
func (r *Repository) invalidateUserCache(ctx context.Context, userID uuid.UUID) {
	// Note: In production, use SCAN for pattern deletion
	r.redis.Del(ctx, fmt.Sprintf("mailbox:%s:inbox", userID))
	r.redis.Del(ctx, fmt.Sprintf("mailbox:%s:sent", userID))
	r.redis.Del(ctx, fmt.Sprintf("mailbox:%s:archived", userID))
	r.redis.Del(ctx, fmt.Sprintf("unread_count:%s", userID))
}
