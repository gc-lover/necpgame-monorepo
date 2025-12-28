package repository

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"services/notification-system-service-go/pkg/models"
)

// Repository handles database operations for notification system
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

	// Configure connection pool
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)

	return db, nil
}

// NewRedisConnection creates a new Redis connection
func NewRedisConnection(url string) (*redis.Client, error) {
	opts, err := redis.ParseURL(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	client := redis.NewClient(opts)
	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	return client, nil
}

// CreateNotification creates a new notification
func (r *Repository) CreateNotification(ctx context.Context, notification *models.Notification) error {
	query := `
		INSERT INTO notifications (id, player_id, type, title, message, data, is_read, created_at, expires_at, priority)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	notification.ID = uuid.New()
	notification.CreatedAt = time.Now()
	notification.IsRead = false

	_, err := r.db.ExecContext(ctx, query,
		notification.ID,
		notification.PlayerID,
		notification.Type,
		notification.Title,
		notification.Message,
		notification.Data,
		notification.IsRead,
		notification.CreatedAt,
		notification.ExpiresAt,
		notification.Priority,
	)

	if err != nil {
		r.logger.Error("Failed to create notification", zap.Error(err))
		return fmt.Errorf("failed to create notification: %w", err)
	}

	// Invalidate cache
	r.invalidatePlayerCache(ctx, notification.PlayerID)

	return nil
}

// GetPlayerNotifications retrieves paginated notifications for a player
func (r *Repository) GetPlayerNotifications(ctx context.Context, playerID uuid.UUID, status, notificationType string, limit, offset int) ([]models.Notification, int, error) {
	var args []interface{}

	baseQuery := `
		SELECT id, player_id, type, title, message, data, is_read, created_at, read_at, expires_at, priority
		FROM notifications
		WHERE player_id = $1
	`

	args = append(args, playerID)

	// Add status filter
	if status == "unread" {
		baseQuery += " AND is_read = false"
	} else if status == "read" {
		baseQuery += " AND is_read = true"
	}
	// "all" doesn't add any filter

	// Add type filter
	if notificationType != "" {
		args = append(args, notificationType)
		baseQuery += fmt.Sprintf(" AND type = $%d", len(args))
	}

	// Add expiration filter
	args = append(args, time.Now())
	baseQuery += fmt.Sprintf(" AND (expires_at IS NULL OR expires_at > $%d)", len(args))

	// Add ordering and pagination
	baseQuery += " ORDER BY created_at DESC LIMIT $" + fmt.Sprintf("%d", len(args)+1) + " OFFSET $" + fmt.Sprintf("%d", len(args)+2)
	args = append(args, limit, offset)

	var notifications []models.Notification
	err := r.db.SelectContext(ctx, &notifications, baseQuery, args...)
	if err != nil {
		r.logger.Error("Failed to get player notifications", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get player notifications: %w", err)
	}

	// Get total count
	countQuery := `
		SELECT COUNT(*)
		FROM notifications
		WHERE player_id = $1
	`

	countArgs := []interface{}{playerID}

	if status == "unread" {
		countQuery += " AND is_read = false"
	} else if status == "read" {
		countQuery += " AND is_read = true"
	}

	if notificationType != "" {
		countQuery += " AND type = $2"
		countArgs = append(countArgs, notificationType)
	}

	countArgs = append(countArgs, time.Now())
	countQuery += " AND (expires_at IS NULL OR expires_at > $" + fmt.Sprintf("%d", len(countArgs)) + ")"

	var total int
	err = r.db.GetContext(ctx, &total, countQuery, countArgs...)
	if err != nil {
		r.logger.Error("Failed to get total count", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return notifications, total, nil
}

// GetNotificationByID retrieves a specific notification
func (r *Repository) GetNotificationByID(ctx context.Context, notificationID, playerID uuid.UUID) (*models.Notification, error) {
	var notification models.Notification
	err := r.db.GetContext(ctx, &notification, `
		SELECT id, player_id, type, title, message, data, is_read, created_at, read_at, expires_at, priority
		FROM notifications
		WHERE id = $1 AND player_id = $2
	`, notificationID, playerID)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("notification not found")
		}
		r.logger.Error("Failed to get notification", zap.Error(err))
		return nil, fmt.Errorf("failed to get notification: %w", err)
	}

	return &notification, nil
}

// MarkAsRead marks a notification as read
func (r *Repository) MarkAsRead(ctx context.Context, notificationID, playerID uuid.UUID) error {
	query := `
		UPDATE notifications
		SET is_read = true, read_at = $1
		WHERE id = $2 AND player_id = $3 AND is_read = false
	`

	result, err := r.db.ExecContext(ctx, query, time.Now(), notificationID, playerID)
	if err != nil {
		r.logger.Error("Failed to mark notification as read", zap.Error(err))
		return fmt.Errorf("failed to mark notification as read: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("notification not found or already read")
	}

	// Invalidate cache
	r.invalidatePlayerCache(ctx, playerID)

	return nil
}

// MarkBulkAsRead marks multiple notifications as read
func (r *Repository) MarkBulkAsRead(ctx context.Context, notificationIDs []uuid.UUID, playerID uuid.UUID) error {
	if len(notificationIDs) == 0 {
		return nil
	}

	query := `
		UPDATE notifications
		SET is_read = true, read_at = $1
		WHERE id = ANY($2) AND player_id = $3 AND is_read = false
	`

	_, err := r.db.ExecContext(ctx, query, time.Now(), notificationIDs, playerID)
	if err != nil {
		r.logger.Error("Failed to mark bulk notifications as read", zap.Error(err))
		return fmt.Errorf("failed to mark bulk notifications as read: %w", err)
	}

	// Invalidate cache
	r.invalidatePlayerCache(ctx, playerID)

	return nil
}

// DeleteNotification deletes a notification
func (r *Repository) DeleteNotification(ctx context.Context, notificationID, playerID uuid.UUID) error {
	query := `DELETE FROM notifications WHERE id = $1 AND player_id = $2`

	result, err := r.db.ExecContext(ctx, query, notificationID, playerID)
	if err != nil {
		r.logger.Error("Failed to delete notification", zap.Error(err))
		return fmt.Errorf("failed to delete notification: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("notification not found")
	}

	// Invalidate cache
	r.invalidatePlayerCache(ctx, playerID)

	return nil
}

// DeleteBulkNotifications deletes multiple notifications
func (r *Repository) DeleteBulkNotifications(ctx context.Context, notificationIDs []uuid.UUID, playerID uuid.UUID) error {
	if len(notificationIDs) == 0 {
		return nil
	}

	query := `DELETE FROM notifications WHERE id = ANY($1) AND player_id = $2`

	_, err := r.db.ExecContext(ctx, query, notificationIDs, playerID)
	if err != nil {
		r.logger.Error("Failed to delete bulk notifications", zap.Error(err))
		return fmt.Errorf("failed to delete bulk notifications: %w", err)
	}

	// Invalidate cache
	r.invalidatePlayerCache(ctx, playerID)

	return nil
}

// GetUnreadCount gets the count of unread notifications for a player
func (r *Repository) GetUnreadCount(ctx context.Context, playerID uuid.UUID) (int, error) {
	query := `
		SELECT COUNT(*)
		FROM notifications
		WHERE player_id = $1 AND is_read = false
		AND (expires_at IS NULL OR expires_at > $2)
	`

	var count int
	err := r.db.GetContext(ctx, &count, query, playerID, time.Now())
	if err != nil {
		r.logger.Error("Failed to get unread count", zap.Error(err))
		return 0, fmt.Errorf("failed to get unread count: %w", err)
	}

	return count, nil
}

// invalidatePlayerCache clears cache for a player
func (r *Repository) invalidatePlayerCache(ctx context.Context, playerID uuid.UUID) {
	// Note: In production, use SCAN for pattern deletion or specific keys
	r.redis.Del(ctx, fmt.Sprintf("notifications:%s:unread_count", playerID))
}
