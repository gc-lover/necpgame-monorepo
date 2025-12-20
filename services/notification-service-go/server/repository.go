// Issue: #140874394
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"strings"
	"time"

	"github.com/lib/pq"
	"go.uber.org/zap"
)

// NotificationRepository предоставляет доступ к данным уведомлений
type NotificationRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewNotificationRepository создает новый репозиторий уведомлений
func NewNotificationRepository(db *sql.DB, logger *zap.Logger) *NotificationRepository {
	return &NotificationRepository{
		db:     db,
		logger: logger,
	}
}

// CreateNotification создает новое уведомление в БД
func (r *NotificationRepository) CreateNotification(ctx context.Context, notification *Notification) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		INSERT INTO notifications.notifications (
			id, user_id, type, title, body, data, priority, status, expires_at, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	dataJSON, err := json.Marshal(notification.Data)
	if err != nil {
		return fmt.Errorf("failed to marshal notification data: %w", err)
	}

	_, err = r.db.ExecContext(ctx, query,
		notification.ID,
		notification.UserID,
		notification.Type,
		notification.Title,
		notification.Body,
		dataJSON,
		notification.Priority,
		notification.Status,
		notification.ExpiresAt,
		notification.CreatedAt,
		notification.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create notification",
			zap.String("notification_id", notification.ID),
			zap.String("user_id", notification.UserID),
			zap.Error(err))
		return fmt.Errorf("failed to create notification: %w", err)
	}

	return nil
}

// GetUserNotifications получает уведомления пользователя с фильтрами
func (r *NotificationRepository) GetUserNotifications(ctx context.Context, userID string, limit, offset int, statusFilter string) ([]*Notification, int, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Строим запрос с фильтрами - используем безопасные параметризованные запросы
	args := []interface{}{userID}
	queryParts := []string{"user_id = $1"}
	argCount := 1

	if statusFilter != "all" {
		argCount++
		if statusFilter == "unread" {
			queryParts = append(queryParts, "status = $"+fmt.Sprintf("%d", argCount))
			args = append(args, "unread")
		} else {
			queryParts = append(queryParts, "status != $"+fmt.Sprintf("%d", argCount))
			args = append(args, "read")
		}
	}

	// Добавляем условие для неистекших уведомлений
	argCount++
	queryParts = append(queryParts, "(expires_at IS NULL OR expires_at > $"+fmt.Sprintf("%d", argCount)+")")
	args = append(args, time.Now())

	whereClause := strings.Join(queryParts, " AND ")

	// Получаем общее количество - используем параметризованный запрос
	countQuery := "SELECT COUNT(*) FROM notifications.notifications WHERE " + whereClause
	var total int
	err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count notifications: %w", err)
	}

	// Получаем уведомления с пагинацией - используем параметризованный запрос
	argCount += 2
	selectQuery := `
		SELECT id, user_id, type, title, message, data, priority, status, expires_at, created_at, updated_at
		FROM notifications.notifications
		WHERE ` + whereClause + `
		ORDER BY created_at DESC
		LIMIT $` + fmt.Sprintf("%d", argCount-1) + ` OFFSET $` + fmt.Sprintf("%d", argCount)

	args = append(args, limit, offset)
	rows, err := r.db.QueryContext(ctx, selectQuery, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query notifications: %w", err)
	}
	defer rows.Close()

	var notifications []*Notification
	for rows.Next() {
		var notification Notification
		var dataJSON []byte

		err := rows.Scan(
			&notification.ID,
			&notification.UserID,
			&notification.Type,
			&notification.Title,
			&notification.Body,
			&dataJSON,
			&notification.Priority,
			&notification.Status,
			&notification.ExpiresAt,
			&notification.CreatedAt,
			&notification.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan notification: %w", err)
		}

		// Разбираем JSON данные
		if len(dataJSON) > 0 {
			if err := json.Unmarshal(dataJSON, &notification.Data); err != nil {
				r.logger.Warn("Failed to unmarshal notification data",
					zap.String("notification_id", notification.ID),
					zap.Error(err))
				notification.Data = make(map[string]interface{})
			}
		} else {
			notification.Data = make(map[string]interface{})
		}

		notifications = append(notifications, &notification)
	}

	if err := rows.Err(); err != nil {
		return nil, 0, fmt.Errorf("error iterating notifications: %w", err)
	}

	return notifications, total, nil
}

// GetNotificationByID получает уведомление по ID для конкретного пользователя
func (r *NotificationRepository) GetNotificationByID(ctx context.Context, notificationID, userID string) (*Notification, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		SELECT id, user_id, type, title, message, data, priority, status, expires_at, created_at, updated_at
		FROM notifications.notifications
		WHERE id = $1 AND user_id = $2 AND (expires_at IS NULL OR expires_at > $3)
	`

	var notification Notification
	var dataJSON []byte

	err := r.db.QueryRowContext(ctx, query, notificationID, userID, time.Now()).Scan(
		&notification.ID,
		&notification.UserID,
		&notification.Type,
		&notification.Title,
		&notification.Body,
		&dataJSON,
		&notification.Priority,
		&notification.Status,
		&notification.ExpiresAt,
		&notification.CreatedAt,
		&notification.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("notification not found")
		}
		return nil, fmt.Errorf("failed to get notification: %w", err)
	}

	// Разбираем JSON данные
	if len(dataJSON) > 0 {
		if err := json.Unmarshal(dataJSON, &notification.Data); err != nil {
			r.logger.Warn("Failed to unmarshal notification data",
				zap.String("notification_id", notification.ID),
				zap.Error(err))
			notification.Data = make(map[string]interface{})
		}
	} else {
		notification.Data = make(map[string]interface{})
	}

	return &notification, nil
}

// UpdateNotification обновляет уведомление
func (r *NotificationRepository) UpdateNotification(ctx context.Context, notificationID, userID string, updates map[string]interface{}) (*Notification, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Строим запрос обновления
	setParts := []string{}
	args := []interface{}{}
	argCount := 0

	for field, value := range updates {
		argCount++
		setParts = append(setParts, fmt.Sprintf("%s = $%d", field, argCount))
		args = append(args, value)
	}

	// Всегда обновляем updated_at
	argCount++
	setParts = append(setParts, fmt.Sprintf("updated_at = $%d", argCount))
	args = append(args, time.Now())

	setClause := strings.Join(setParts, ", ")

	query := fmt.Sprintf(`
		UPDATE notifications.notifications
		SET %s
		WHERE id = $%d AND user_id = $%d
		RETURNING id, user_id, type, title, message, data, priority, status, expires_at, created_at, updated_at
	`, setClause, argCount+1, argCount+2)

	args = append(args, notificationID, userID)

	var notification Notification
	var dataJSON []byte

	err := r.db.QueryRowContext(ctx, query, args...).Scan(
		&notification.ID,
		&notification.UserID,
		&notification.Type,
		&notification.Title,
		&notification.Body,
		&dataJSON,
		&notification.Priority,
		&notification.Status,
		&notification.ExpiresAt,
		&notification.CreatedAt,
		&notification.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("notification not found")
		}
		return nil, fmt.Errorf("failed to update notification: %w", err)
	}

	// Разбираем JSON данные
	if len(dataJSON) > 0 {
		if err := json.Unmarshal(dataJSON, &notification.Data); err != nil {
			r.logger.Warn("Failed to unmarshal notification data",
				zap.String("notification_id", notification.ID),
				zap.Error(err))
			notification.Data = make(map[string]interface{})
		}
	} else {
		notification.Data = make(map[string]interface{})
	}

	return &notification, nil
}

// MarkAsRead отмечает уведомление как прочитанное
func (r *NotificationRepository) MarkAsRead(ctx context.Context, notificationID, userID string) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		UPDATE notifications.notifications
		SET status = 'read', updated_at = $1
		WHERE id = $2 AND user_id = $3 AND status = 'unread'
	`

	result, err := r.db.ExecContext(ctx, query, time.Now(), notificationID, userID)
	if err != nil {
		return fmt.Errorf("failed to mark as read: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("notification not found or already read")
	}

	return nil
}

// MarkBulkAsRead отмечает несколько уведомлений как прочитанные
func (r *NotificationRepository) MarkBulkAsRead(ctx context.Context, notificationIDs []string, userID string) (int, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	query := `
		UPDATE notifications.notifications
		SET status = 'read', updated_at = $1
		WHERE id = ANY($2) AND user_id = $3 AND status = 'unread'
	`

	result, err := r.db.ExecContext(ctx, query, time.Now(), pq.Array(notificationIDs), userID)
	if err != nil {
		return 0, fmt.Errorf("failed to mark bulk as read: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return 0, fmt.Errorf("failed to get rows affected: %w", err)
	}

	return int(rowsAffected), nil
}

// GetUnreadCount получает количество непрочитанных уведомлений пользователя
func (r *NotificationRepository) GetUnreadCount(ctx context.Context, userID string) (int, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT COUNT(*)
		FROM notifications.notifications
		WHERE user_id = $1 AND status = 'unread' AND (expires_at IS NULL OR expires_at > $2)
	`

	var count int
	err := r.db.QueryRowContext(ctx, query, userID, time.Now()).Scan(&count)
	if err != nil {
		return 0, fmt.Errorf("failed to get unread count: %w", err)
	}

	return count, nil
}

// CleanExpiredNotifications удаляет истекшие уведомления
func (r *NotificationRepository) CleanExpiredNotifications(ctx context.Context) (int64, error) {
	ctx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	query := `DELETE FROM notifications.notifications WHERE expires_at < $1`

	result, err := r.db.ExecContext(ctx, query, time.Now())
	if err != nil {
		return 0, fmt.Errorf("failed to clean expired notifications: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return 0, fmt.Errorf("failed to get rows affected: %w", err)
	}

	r.logger.Info("Cleaned expired notifications", zap.Int64("count", rowsAffected))
	return rowsAffected, nil
}

// GetNotificationStats получает статистику уведомлений для аналитики
func (r *NotificationRepository) GetNotificationStats(ctx context.Context, userID string) (map[string]int, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		SELECT
			COUNT(*) as total,
			COUNT(CASE WHEN status = 'unread' THEN 1 END) as unread,
			COUNT(CASE WHEN type = 'system' THEN 1 END) as system,
			COUNT(CASE WHEN type = 'achievement' THEN 1 END) as achievement,
			COUNT(CASE WHEN type = 'quest' THEN 1 END) as quest,
			COUNT(CASE WHEN type = 'social' THEN 1 END) as social,
			COUNT(CASE WHEN type = 'combat' THEN 1 END) as combat,
			COUNT(CASE WHEN type = 'economy' THEN 1 END) as economy
		FROM notifications.notifications
		WHERE user_id = $1 AND (expires_at IS NULL OR expires_at > $2)
	`

	stats := make(map[string]int)
	var total, unread, system, achievement, quest, social, combat, economy int

	err := r.db.QueryRowContext(ctx, query, userID, time.Now()).Scan(
		&total, &unread, &system, &achievement, &quest, &social, &combat, &economy,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to get notification stats: %w", err)
	}

	stats["total"] = total
	stats["unread"] = unread
	stats["system"] = system
	stats["achievement"] = achievement
	stats["quest"] = quest
	stats["social"] = social
	stats["combat"] = combat
	stats["economy"] = economy

	return stats, nil
}
