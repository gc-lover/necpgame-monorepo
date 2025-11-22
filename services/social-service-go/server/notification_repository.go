package server

import (
	"context"
	"database/sql/driver"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type NotificationRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewNotificationRepository(db *pgxpool.Pool) *NotificationRepository {
	return &NotificationRepository{
		db:     db,
		logger: GetLogger(),
	}
}

type StringArray []string

func (a StringArray) Value() (driver.Value, error) {
	if len(a) == 0 {
		return "{}", nil
	}
	return json.Marshal(a)
}

func (a *StringArray) Scan(value interface{}) error {
	if value == nil {
		*a = []string{}
		return nil
	}

	var bytes []byte
	switch v := value.(type) {
	case []byte:
		bytes = v
	case string:
		bytes = []byte(v)
	default:
		return nil
	}

	return json.Unmarshal(bytes, a)
}

func (r *NotificationRepository) Create(ctx context.Context, notification *models.Notification) (*models.Notification, error) {
	channelsJSON, _ := json.Marshal(notification.Channels)
	dataJSON, _ := json.Marshal(notification.Data)

	err := r.db.QueryRow(ctx,
		`INSERT INTO social.notifications 
		 (id, account_id, type, priority, title, content, data, status, channels, created_at, expires_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
		 RETURNING id, account_id, type, priority, title, content, data, status, channels, created_at, read_at, expires_at`,
		notification.ID, notification.AccountID, notification.Type, notification.Priority,
		notification.Title, notification.Content, dataJSON, notification.Status,
		channelsJSON, notification.CreatedAt, notification.ExpiresAt,
	).Scan(&notification.ID, &notification.AccountID, &notification.Type, &notification.Priority,
		&notification.Title, &notification.Content, &dataJSON, &notification.Status,
		&channelsJSON, &notification.CreatedAt, &notification.ReadAt, &notification.ExpiresAt)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create notification")
		return nil, err
	}

	json.Unmarshal(channelsJSON, &notification.Channels)
	json.Unmarshal(dataJSON, &notification.Data)

	return notification, nil
}

func (r *NotificationRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Notification, error) {
	var notification models.Notification
	var channelsJSON, dataJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT id, account_id, type, priority, title, content, data, status, channels, created_at, read_at, expires_at
		 FROM social.notifications
		 WHERE id = $1 AND (expires_at IS NULL OR expires_at > NOW())`,
		id,
	).Scan(&notification.ID, &notification.AccountID, &notification.Type, &notification.Priority,
		&notification.Title, &notification.Content, &dataJSON, &notification.Status,
		&channelsJSON, &notification.CreatedAt, &notification.ReadAt, &notification.ExpiresAt)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get notification")
		return nil, err
	}

	json.Unmarshal(channelsJSON, &notification.Channels)
	json.Unmarshal(dataJSON, &notification.Data)

	return &notification, nil
}

func (r *NotificationRepository) GetByAccountID(ctx context.Context, accountID uuid.UUID, limit, offset int) ([]models.Notification, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, account_id, type, priority, title, content, data, status, channels, created_at, read_at, expires_at
		 FROM social.notifications
		 WHERE account_id = $1 AND (expires_at IS NULL OR expires_at > NOW())
		 ORDER BY created_at DESC
		 LIMIT $2 OFFSET $3`,
		accountID, limit, offset,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get notifications")
		return nil, err
	}
	defer rows.Close()

	var notifications []models.Notification
	for rows.Next() {
		var notification models.Notification
		var channelsJSON, dataJSON []byte

		err := rows.Scan(&notification.ID, &notification.AccountID, &notification.Type, &notification.Priority,
			&notification.Title, &notification.Content, &dataJSON, &notification.Status,
			&channelsJSON, &notification.CreatedAt, &notification.ReadAt, &notification.ExpiresAt)

		if err != nil {
			r.logger.WithError(err).Error("Failed to scan notification")
			continue
		}

		json.Unmarshal(channelsJSON, &notification.Channels)
		json.Unmarshal(dataJSON, &notification.Data)

		notifications = append(notifications, notification)
	}

	return notifications, nil
}

func (r *NotificationRepository) CountByAccountID(ctx context.Context, accountID uuid.UUID) (int, error) {
	var count int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM social.notifications
		 WHERE account_id = $1 AND (expires_at IS NULL OR expires_at > NOW())`,
		accountID,
	).Scan(&count)

	if err != nil {
		r.logger.WithError(err).Error("Failed to count notifications")
		return 0, err
	}

	return count, nil
}

func (r *NotificationRepository) CountUnreadByAccountID(ctx context.Context, accountID uuid.UUID) (int, error) {
	var count int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM social.notifications
		 WHERE account_id = $1 AND status = $2 AND (expires_at IS NULL OR expires_at > NOW())`,
		accountID, models.NotificationStatusUnread,
	).Scan(&count)

	if err != nil {
		r.logger.WithError(err).Error("Failed to count unread notifications")
		return 0, err
	}

	return count, nil
}

func (r *NotificationRepository) UpdateStatus(ctx context.Context, id uuid.UUID, status models.NotificationStatus) (*models.Notification, error) {
	var readAt *time.Time
	if status == models.NotificationStatusRead {
		now := time.Now()
		readAt = &now
	}

	var notification models.Notification
	var channelsJSON, dataJSON []byte

	err := r.db.QueryRow(ctx,
		`UPDATE social.notifications
		 SET status = $1, read_at = $2, updated_at = NOW()
		 WHERE id = $3
		 RETURNING id, account_id, type, priority, title, content, data, status, channels, created_at, read_at, expires_at`,
		status, readAt, id,
	).Scan(&notification.ID, &notification.AccountID, &notification.Type, &notification.Priority,
		&notification.Title, &notification.Content, &dataJSON, &notification.Status,
		&channelsJSON, &notification.CreatedAt, &notification.ReadAt, &notification.ExpiresAt)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to update notification status")
		return nil, err
	}

	json.Unmarshal(channelsJSON, &notification.Channels)
	json.Unmarshal(dataJSON, &notification.Data)

	return &notification, nil
}
