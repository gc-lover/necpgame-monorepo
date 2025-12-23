// Issue: Implement admin-service-go based on OpenAPI specification
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
	"admin-service-go/server/internal/models"
)

// AdminRepository handles database operations for admin service
// Optimized for admin query patterns with connection pooling
type AdminRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewAdminRepository creates a new admin repository instance
func NewAdminRepository(db *pgxpool.Pool, logger *zap.Logger) *AdminRepository {
	return &AdminRepository{
		db:     db,
		logger: logger,
	}
}

// CreateAdminAction logs an admin action to the database
func (r *AdminRepository) CreateAdminAction(ctx context.Context, action *models.AdminAction) error {
	query := `
		INSERT INTO admin_audit_log (
			id, admin_id, action, resource, timestamp,
			ip_address, user_agent, metadata
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	metadataJSON, err := json.Marshal(action.Metadata)
	if err != nil {
		return fmt.Errorf("failed to marshal metadata: %w", err)
	}

	_, err = r.db.Exec(ctx, query,
		action.ID,
		action.AdminID,
		action.Action,
		action.Resource,
		action.Timestamp,
		action.IPAddress,
		action.UserAgent,
		metadataJSON,
	)

	if err != nil {
		r.logger.Error("Failed to create admin action",
			zap.Error(err),
			zap.String("admin_id", action.AdminID.String()),
			zap.String("action", action.Action),
		)
		return fmt.Errorf("failed to create admin action: %w", err)
	}

	return nil
}

// GetAdminActions retrieves paginated admin actions for audit log
func (r *AdminRepository) GetAdminActions(ctx context.Context, filter *models.AuditLogFilter) ([]*models.AdminAction, error) {
	query := `
		SELECT id, admin_id, action, resource, timestamp,
		       ip_address, user_agent, metadata
		FROM admin_audit_log
		WHERE 1=1
	`

	args := []interface{}{}
	argCount := 0

	// Add filters
	if filter.AdminID != nil {
		argCount++
		query += fmt.Sprintf(" AND admin_id = $%d", argCount)
		args = append(args, *filter.AdminID)
	}

	if filter.Action != nil {
		argCount++
		query += fmt.Sprintf(" AND action = $%d", argCount)
		args = append(args, *filter.Action)
	}

	if filter.Resource != nil {
		argCount++
		query += fmt.Sprintf(" AND resource LIKE $%d", argCount)
		args = append(args, "%"+*filter.Resource+"%")
	}

	if filter.StartTime != nil {
		argCount++
		query += fmt.Sprintf(" AND timestamp >= $%d", argCount)
		args = append(args, *filter.StartTime)
	}

	if filter.EndTime != nil {
		argCount++
		query += fmt.Sprintf(" AND timestamp <= $%d", argCount)
		args = append(args, *filter.EndTime)
	}

	if filter.IPAddress != nil {
		argCount++
		query += fmt.Sprintf(" AND ip_address = $%d", argCount)
		args = append(args, *filter.IPAddress)
	}

	// Add ordering and pagination
	query += " ORDER BY timestamp DESC"
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, filter.Limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, filter.Offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to query admin actions", zap.Error(err))
		return nil, fmt.Errorf("failed to query admin actions: %w", err)
	}
	defer rows.Close()

	var actions []*models.AdminAction
	for rows.Next() {
		action := &models.AdminAction{}
		var metadataJSON []byte

		err := rows.Scan(
			&action.ID,
			&action.AdminID,
			&action.Action,
			&action.Resource,
			&action.Timestamp,
			&action.IPAddress,
			&action.UserAgent,
			&metadataJSON,
		)
		if err != nil {
			r.logger.Error("Failed to scan admin action", zap.Error(err))
			continue
		}

		// Unmarshal metadata
		if len(metadataJSON) > 0 {
			if err := json.Unmarshal(metadataJSON, &action.Metadata); err != nil {
				r.logger.Warn("Failed to unmarshal metadata", zap.Error(err))
			}
		}

		actions = append(actions, action)
	}

	if err := rows.Err(); err != nil {
		r.logger.Error("Error iterating admin actions", zap.Error(err))
		return nil, fmt.Errorf("error iterating admin actions: %w", err)
	}

	return actions, nil
}

// BanUser bans a user in the database
func (r *AdminRepository) BanUser(ctx context.Context, userID uuid.UUID, reason string, duration time.Duration, adminID uuid.UUID) error {
	query := `
		INSERT INTO user_bans (user_id, reason, duration, banned_by, banned_at, expires_at)
		VALUES ($1, $2, $3, $4, $5, $6)
		ON CONFLICT (user_id) DO UPDATE SET
			reason = EXCLUDED.reason,
			duration = EXCLUDED.duration,
			banned_by = EXCLUDED.banned_by,
			banned_at = EXCLUDED.banned_at,
			expires_at = EXCLUDED.expires_at
	`

	expiresAt := time.Now().Add(duration)

	_, err := r.db.Exec(ctx, query,
		userID,
		reason,
		duration,
		adminID,
		time.Now(),
		expiresAt,
	)

	if err != nil {
		r.logger.Error("Failed to ban user",
			zap.Error(err),
			zap.String("user_id", userID.String()),
			zap.String("admin_id", adminID.String()),
		)
		return fmt.Errorf("failed to ban user: %w", err)
	}

	return nil
}

// UnbanUser removes a user ban from the database
func (r *AdminRepository) UnbanUser(ctx context.Context, userID uuid.UUID) error {
	query := `DELETE FROM user_bans WHERE user_id = $1`

	result, err := r.db.Exec(ctx, query, userID)
	if err != nil {
		r.logger.Error("Failed to unban user",
			zap.Error(err),
			zap.String("user_id", userID.String()),
		)
		return fmt.Errorf("failed to unban user: %w", err)
	}

	if result.RowsAffected() == 0 {
		return models.ErrUserNotFound
	}

	return nil
}

// GetUserDetails retrieves detailed user information
func (r *AdminRepository) GetUserDetails(ctx context.Context, userID uuid.UUID) (*models.UserDetails, error) {
	query := `
		SELECT u.id, u.username, u.email, u.created_at, u.last_login,
		       u.is_active, u.role, ub.reason as ban_reason, ub.expires_at as ban_expires
		FROM users u
		LEFT JOIN user_bans ub ON u.id = ub.user_id AND ub.expires_at > NOW()
		WHERE u.id = $1
	`

	var user models.UserDetails
	var banReason, banExpires *string

	err := r.db.QueryRow(ctx, query, userID).Scan(
		&user.ID,
		&user.Username,
		&user.Email,
		&user.CreatedAt,
		&user.LastLogin,
		&user.IsActive,
		&user.Role,
		&banReason,
		&banExpires,
	)

	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, models.ErrUserNotFound
		}
		r.logger.Error("Failed to get user details",
			zap.Error(err),
			zap.String("user_id", userID.String()),
		)
		return nil, fmt.Errorf("failed to get user details: %w", err)
	}

	// Set ban information if present
	if banReason != nil {
		user.BanReason = banReason
	}
	if banExpires != nil {
		if expires, err := time.Parse(time.RFC3339, *banExpires); err == nil {
			user.BanExpires = &expires
		}
	}

	return &user, nil
}

// GetSystemMetrics retrieves current system performance metrics
func (r *AdminRepository) GetSystemMetrics(ctx context.Context) (*models.SystemMetrics, error) {
	metrics := &models.SystemMetrics{}

	// Get active connections
	connCountQuery := `SELECT count(*) FROM pg_stat_activity WHERE state = 'active'`
	err := r.db.QueryRow(ctx, connCountQuery).Scan(&metrics.ActiveConnections)
	if err != nil {
		r.logger.Warn("Failed to get active connections", zap.Error(err))
	}

	// TODO: Implement other metrics collection
	// This would typically involve querying various system tables and caches

	return metrics, nil
}

// IsUserBanned checks if a user is currently banned
func (r *AdminRepository) IsUserBanned(ctx context.Context, userID uuid.UUID) (bool, error) {
	query := `SELECT EXISTS(SELECT 1 FROM user_bans WHERE user_id = $1 AND expires_at > NOW())`

	var banned bool
	err := r.db.QueryRow(ctx, query, userID).Scan(&banned)
	if err != nil {
		r.logger.Error("Failed to check user ban status",
			zap.Error(err),
			zap.String("user_id", userID.String()),
		)
		return false, fmt.Errorf("failed to check user ban status: %w", err)
	}

	return banned, nil
}

// GetContentModerationQueue retrieves content requiring moderation
func (r *AdminRepository) GetContentModerationQueue(ctx context.Context, limit, offset int) ([]*models.ContentItem, error) {
	query := `
		SELECT id, content_type, content, author_id, submitted_at, status, priority
		FROM content_moderation_queue
		WHERE status = 'pending'
		ORDER BY priority DESC, submitted_at ASC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		r.logger.Error("Failed to get content moderation queue", zap.Error(err))
		return nil, fmt.Errorf("failed to get content moderation queue: %w", err)
	}
	defer rows.Close()

	var items []*models.ContentItem
	for rows.Next() {
		item := &models.ContentItem{}
		err := rows.Scan(
			&item.ID,
			&item.ContentType,
			&item.Content,
			&item.AuthorID,
			&item.SubmittedAt,
			&item.Status,
			&item.Priority,
		)
		if err != nil {
			r.logger.Error("Failed to scan content item", zap.Error(err))
			continue
		}
		items = append(items, item)
	}

	return items, rows.Err()
}

// ModerateContent processes a content moderation action
func (r *AdminRepository) ModerateContent(ctx context.Context, contentID uuid.UUID, action string, moderatorID uuid.UUID, reason string) error {
	query := `
		UPDATE content_moderation_queue
		SET status = $1, moderated_by = $2, moderated_at = NOW(), moderation_reason = $3
		WHERE id = $4
	`

	_, err := r.db.Exec(ctx, query, action, moderatorID, reason, contentID)
	if err != nil {
		r.logger.Error("Failed to moderate content",
			zap.Error(err),
			zap.String("content_id", contentID.String()),
			zap.String("action", action),
		)
		return fmt.Errorf("failed to moderate content: %w", err)
	}

	return nil
}
