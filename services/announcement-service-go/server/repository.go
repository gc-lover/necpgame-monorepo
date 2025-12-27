// Database repository for Announcement Service
// Issue: #323
// PERFORMANCE: Optimized queries, connection pooling, prepared statements

package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/announcement-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// Repository handles database operations for Announcement service
type Repository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository creates a new repository with database connection
func NewRepository(logger *zap.Logger) *Repository {
	// PERFORMANCE: Connection pooling configured for MMO load
	// In production, this would be injected via dependency injection
	connStr := "postgresql://postgres:postgres@postgres:5432/necpgame?sslmode=disable" // TODO: Use config
	config, err := pgxpool.ParseConfig(connStr)
	if err != nil {
		logger.Fatal("Failed to parse PostgreSQL config", zap.Error(err))
	}

	// TODO: Configure connection pool settings for performance
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = time.Minute * 30

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		logger.Fatal("Failed to connect to PostgreSQL", zap.Error(err))
	}

	// Test connection
	if err := db.Ping(context.Background()); err != nil {
		logger.Fatal("Failed to ping PostgreSQL", zap.Error(err))
	}

	logger.Info("Connected to PostgreSQL successfully")

	return &Repository{
		db:     db,
		logger: logger,
	}
}

// GetAnnouncements retrieves announcements with pagination and filtering
func (r *Repository) GetAnnouncements(ctx context.Context, announcementType, priority *string, limit, offset int) ([]*api.Announcement, int, error) {
	r.logger.Debug("Getting announcements from DB",
		zap.Stringp("type", announcementType),
		zap.Stringp("priority", priority),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	// Build query with filters
	query := `
		SELECT id, created_by, title, content, announcement_type, priority,
			   display_style, status, scheduled_publish_at, published_at,
			   archived_at, created_at, updated_at, targeting, media, delivery_channels
		FROM announcements
		WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	if announcementType != nil {
		argCount++
		query += fmt.Sprintf(" AND announcement_type = $%d", argCount)
		args = append(args, *announcementType)
	}

	if priority != nil {
		argCount++
		query += fmt.Sprintf(" AND priority = $%d", argCount)
		args = append(args, *priority)
	}

	query += fmt.Sprintf(" ORDER BY created_at DESC LIMIT %d OFFSET %d", limit, offset+1)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query announcements: %w", err)
	}
	defer rows.Close()

	var announcements []*api.Announcement
	for rows.Next() {
		var ann api.Announcement
		var targeting, media sql.NullString
		var deliveryChannels []string

		err := rows.Scan(
			&ann.ID.Value, &ann.CreatedBy.Value, &ann.Title, &ann.Content,
			&ann.AnnouncementType, &ann.Priority.Value, &ann.DisplayStyle.Value,
			&ann.Status.Value, &ann.ScheduledPublishAt.Value, &ann.PublishedAt.Value,
			&ann.ArchivedAt.Value, &ann.CreatedAt.Value, &ann.UpdatedAt.Value,
			&targeting, &media, &deliveryChannels,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan announcement: %w", err)
		}

		// Handle optional fields
		if targeting.Valid {
			ann.Targeting = &targeting.String
		}
		if media.Valid {
			ann.Media = &media.String
		}
		if len(deliveryChannels) > 0 {
			ann.DeliveryChannels = deliveryChannels
		}

		announcements = append(announcements, &ann)
	}

	if err := rows.Err(); err != nil {
		return nil, 0, fmt.Errorf("error iterating rows: %w", err)
	}

	// Get total count
	countQuery := "SELECT COUNT(*) FROM announcements WHERE 1=1"
	if announcementType != nil {
		countQuery += " AND announcement_type = $1"
		args = []interface{}{*announcementType}
	}
	if priority != nil {
		if announcementType != nil {
			countQuery += " AND priority = $2"
		} else {
			countQuery += " AND priority = $1"
		}
		args = append(args, *priority)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return announcements, total, nil
}

// GetAnnouncement retrieves a single announcement by ID
func (r *Repository) GetAnnouncement(ctx context.Context, id uuid.UUID) (*api.Announcement, error) {
	r.logger.Debug("Getting announcement from DB", zap.String("id", id.String()))

	query := `
		SELECT id, created_by, title, content, announcement_type, priority,
			   display_style, status, scheduled_publish_at, published_at,
			   archived_at, created_at, updated_at, targeting, media, delivery_channels
		FROM announcements
		WHERE id = $1`

	var ann api.Announcement
	var targeting, media sql.NullString
	var deliveryChannels []string

	err := r.db.QueryRow(ctx, query, id).Scan(
		&ann.ID.Value, &ann.CreatedBy.Value, &ann.Title, &ann.Content,
		&ann.AnnouncementType, &ann.Priority.Value, &ann.DisplayStyle.Value,
		&ann.Status.Value, &ann.ScheduledPublishAt.Value, &ann.PublishedAt.Value,
		&ann.ArchivedAt.Value, &ann.CreatedAt.Value, &ann.UpdatedAt.Value,
		&targeting, &media, &deliveryChannels,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("announcement not found")
		}
		return nil, fmt.Errorf("failed to get announcement: %w", err)
	}

	// Handle optional fields
	if targeting.Valid {
		ann.Targeting = &targeting.String
	}
	if media.Valid {
		ann.Media = &media.String
	}
	if len(deliveryChannels) > 0 {
		ann.DeliveryChannels = deliveryChannels
	}

	return &ann, nil
}

// CreateAnnouncement stores a new announcement in the database
func (r *Repository) CreateAnnouncement(ctx context.Context, announcement *api.Announcement) error {
	r.logger.Debug("Creating announcement in DB", zap.String("id", announcement.ID.Value.String()))

	query := `
		INSERT INTO announcements (
			id, created_by, title, content, announcement_type, priority,
			display_style, status, scheduled_publish_at, published_at,
			archived_at, created_at, updated_at, targeting, media, delivery_channels
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16
		)`

	_, err := r.db.Exec(ctx, query,
		announcement.ID.Value,
		announcement.CreatedBy.Value,
		announcement.Title,
		announcement.Content,
		announcement.AnnouncementType,
		announcement.Priority.Value,
		announcement.DisplayStyle.Value,
		announcement.Status.Value,
		announcement.ScheduledPublishAt.Value,
		announcement.PublishedAt.Value,
		announcement.ArchivedAt.Value,
		announcement.CreatedAt.Value,
		announcement.UpdatedAt.Value,
		announcement.Targeting,
		announcement.Media,
		announcement.DeliveryChannels,
	)

	if err != nil {
		return fmt.Errorf("failed to insert announcement: %w", err)
	}

	r.logger.Info("Announcement created in DB", zap.String("id", announcement.ID.Value.String()))
	return nil
}

// UpdateAnnouncement updates an existing announcement in the database
func (r *Repository) UpdateAnnouncement(ctx context.Context, announcement *api.Announcement) error {
	r.logger.Debug("Updating announcement in DB", zap.String("id", announcement.ID.Value.String()))

	query := `
		UPDATE announcements SET
			title = $2, content = $3, priority = $4, display_style = $5,
			status = $6, scheduled_publish_at = $7, published_at = $8,
			archived_at = $9, updated_at = $10, targeting = $11, media = $12,
			delivery_channels = $13
		WHERE id = $1`

	_, err := r.db.Exec(ctx, query,
		announcement.ID.Value,
		announcement.Title,
		announcement.Content,
		announcement.Priority.Value,
		announcement.DisplayStyle.Value,
		announcement.Status.Value,
		announcement.ScheduledPublishAt.Value,
		announcement.PublishedAt.Value,
		announcement.ArchivedAt.Value,
		announcement.UpdatedAt.Value,
		announcement.Targeting,
		announcement.Media,
		announcement.DeliveryChannels,
	)

	if err != nil {
		return fmt.Errorf("failed to update announcement: %w", err)
	}

	r.logger.Info("Announcement updated in DB", zap.String("id", announcement.ID.Value.String()))
	return nil
}

// DeleteAnnouncement deletes an announcement from the database
func (r *Repository) DeleteAnnouncement(ctx context.Context, id uuid.UUID) error {
	r.logger.Debug("Deleting announcement from DB", zap.String("id", id.String()))

	query := "DELETE FROM announcements WHERE id = $1"

	result, err := r.db.Exec(ctx, query, id)
	if err != nil {
		return fmt.Errorf("failed to delete announcement: %w", err)
	}

	rowsAffected := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("announcement not found")
	}

	r.logger.Info("Announcement deleted from DB", zap.String("id", id.String()))
	return nil
}




