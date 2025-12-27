// Guild Repository - Database and cache access layer
// Issue: #2247

package repository

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/server"
	_ "github.com/lib/pq" // PostgreSQL driver
)

// Repository handles data access
type Repository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB, redis *redis.Client, logger *zap.SugaredLogger) *Repository {
	return &Repository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// NewDatabaseConnection creates a new PostgreSQL connection
func NewDatabaseConnection(cfg DatabaseConfig) (*sql.DB, error) {
	db, err := sql.Open("postgres", cfg.GetDSN())
	if err != nil {
		return nil, err
	}

	// Configure connection pool
	db.SetMaxOpenConns(cfg.MaxConns)
	db.SetMaxIdleConns(cfg.MaxConns / 2)
	db.SetConnMaxLifetime(5 * time.Minute)

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, err
	}

	return db, nil
}

// NewRedisConnection creates a new Redis connection
func NewRedisConnection(cfg RedisConfig) (*redis.Client, error) {
	client := redis.NewClient(&redis.Options{
		Addr:     cfg.GetAddr(),
		Password: cfg.Password,
		DB:       cfg.DB,
	})

	// Test connection
	if err := client.Ping(nil).Err(); err != nil {
		return nil, err
	}

	return client, nil
}

// DatabaseConfig holds database configuration
type DatabaseConfig struct {
	Host     string
	Port     int
	User     string
	Password string
	Database string
	SSLMode  string
	MaxConns int
}

// GetDSN returns database connection string
func (d DatabaseConfig) GetDSN() string {
	return "host=" + d.Host + " port=" + string(rune(d.Port)) +
		" user=" + d.User + " password=" + d.Password +
		" dbname=" + d.Database + " sslmode=" + d.SSLMode
}

// RedisConfig holds Redis configuration
type RedisConfig struct {
	Host     string
	Port     int
	Password string
	DB       int
}

// GetAddr returns Redis address
func (r RedisConfig) GetAddr() string {
	return r.Host + ":" + string(rune(r.Port))
}

// ListGuilds retrieves a paginated list of guilds
func (r *Repository) ListGuilds(ctx context.Context, limit, offset int, sortBy string) ([]*server.Guild, error) {
	r.logger.Infof("Listing guilds with limit: %d, offset: %d, sort: %s", limit, offset, sortBy)

	if limit <= 0 || limit > 100 {
		limit = 20 // default limit
	}
	if offset < 0 {
		offset = 0
	}

	// Build order by clause
	orderBy := "created_at DESC"
	switch sortBy {
	case "level":
		orderBy = "level DESC, experience DESC"
	case "reputation":
		orderBy = "reputation DESC"
	case "members":
		orderBy = "member_count DESC"
	case "name":
		orderBy = "name ASC"
	}

	query := `
		SELECT id, name, description, leader_id, created_at, updated_at,
		       member_count, max_members, level, experience, reputation
		FROM social.guilds
		WHERE is_active = true AND deleted_at IS NULL
		ORDER BY ` + orderBy + `
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.QueryContext(ctx, query, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to list guilds: %v", err)
		return nil, err
	}
	defer rows.Close()

	var guilds []*server.Guild
	for rows.Next() {
		var guild server.Guild
		err := rows.Scan(
			&guild.ID, &guild.Name, &guild.Description, &guild.LeaderID,
			&guild.CreatedAt, &guild.UpdatedAt, &guild.MemberCount,
			&guild.MaxMembers, &guild.Level, &guild.Experience,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan guild: %v", err)
			return nil, err
		}
		guilds = append(guilds, &guild)
	}

	if err = rows.Err(); err != nil {
		r.logger.Errorf("Error iterating guilds: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully retrieved %d guilds", len(guilds))
	return guilds, nil
}

// CreateGuild creates a new guild in the database
func (r *Repository) CreateGuild(ctx context.Context, name, description string, leaderID uuid.UUID) (*server.Guild, error) {
	r.logger.Infof("Creating guild in database: %s", name)

	query := `
		INSERT INTO social.guilds (name, description, leader_id, member_count, max_members, level, experience, reputation)
		VALUES ($1, $2, $3, 1, 50, 1, 0, 0)
		RETURNING id, name, description, leader_id, created_at, updated_at, member_count, max_members, level, experience, reputation
	`

	var guild server.Guild
	err := r.db.QueryRowContext(ctx, query, name, description, leaderID).Scan(
		&guild.ID, &guild.Name, &guild.Description, &guild.LeaderID,
		&guild.CreatedAt, &guild.UpdatedAt, &guild.MemberCount,
		&guild.MaxMembers, &guild.Level, &guild.Experience,
	)
	if err != nil {
		r.logger.Errorf("Failed to create guild: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully created guild with ID: %s", guild.ID)
	return &guild, nil
}

// GetGuild retrieves a guild from database or cache
func (r *Repository) GetGuild(ctx context.Context, guildID uuid.UUID) (*server.Guild, error) {
	r.logger.Infof("Getting guild from database: %s", guildID)

	// Try cache first
	cacheKey := "guild:" + guildID.String()
	if cached, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		r.logger.Debugf("Guild found in cache: %s", guildID)
		// TODO: Parse cached guild data
	}

	// Query database
	query := `
		SELECT id, name, description, leader_id, created_at, updated_at,
		       member_count, max_members, level, experience, reputation
		FROM social.guilds
		WHERE id = $1 AND is_active = true AND deleted_at IS NULL
	`

	var guild server.Guild
	err := r.db.QueryRowContext(ctx, query, guildID).Scan(
		&guild.ID, &guild.Name, &guild.Description, &guild.LeaderID,
		&guild.CreatedAt, &guild.UpdatedAt, &guild.MemberCount,
		&guild.MaxMembers, &guild.Level, &guild.Experience,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			r.logger.Warnf("Guild not found: %s", guildID)
			return nil, err
		}
		r.logger.Errorf("Failed to get guild: %v", err)
		return nil, err
	}

	// Cache the result
	// TODO: Serialize and cache guild data

	r.logger.Debugf("Successfully retrieved guild: %s", guildID)
	return &guild, nil
}

// UpdateGuild updates guild information
func (r *Repository) UpdateGuild(ctx context.Context, guildID uuid.UUID, name, description string) error {
	r.logger.Infof("Updating guild in database: %s", guildID)

	query := `
		UPDATE social.guilds
		SET name = $2, description = $3, updated_at = NOW()
		WHERE id = $1 AND is_active = true AND deleted_at IS NULL
	`

	result, err := r.db.ExecContext(ctx, query, guildID, name, description)
	if err != nil {
		r.logger.Errorf("Failed to update guild: %v", err)
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		r.logger.Errorf("Failed to get rows affected: %v", err)
		return err
	}

	if rowsAffected == 0 {
		r.logger.Warnf("Guild not found or not updated: %s", guildID)
		return sql.ErrNoRows
	}

	// Invalidate cache
	cacheKey := "guild:" + guildID.String()
	r.redis.Del(ctx, cacheKey)

	r.logger.Infof("Successfully updated guild: %s", guildID)
	return nil
}

// DeleteGuild marks a guild as deleted (soft delete)
func (r *Repository) DeleteGuild(ctx context.Context, guildID uuid.UUID) error {
	r.logger.Infof("Soft deleting guild from database: %s", guildID)

	query := `
		UPDATE social.guilds
		SET is_active = false, deleted_at = NOW(), updated_at = NOW()
		WHERE id = $1 AND is_active = true AND deleted_at IS NULL
	`

	result, err := r.db.ExecContext(ctx, query, guildID)
	if err != nil {
		r.logger.Errorf("Failed to delete guild: %v", err)
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		r.logger.Errorf("Failed to get rows affected: %v", err)
		return err
	}

	if rowsAffected == 0 {
		r.logger.Warnf("Guild not found or already deleted: %s", guildID)
		return sql.ErrNoRows
	}

	// Invalidate cache
	cacheKey := "guild:" + guildID.String()
	r.redis.Del(ctx, cacheKey)

	r.logger.Infof("Successfully soft deleted guild: %s", guildID)
	return nil
}

// AddGuildMember adds a member to a guild
func (r *Repository) AddGuildMember(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	r.logger.Infof("Adding member %s to guild %s with role %s in database", userID, guildID, role)

	// Start transaction
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return err
	}
	defer tx.Rollback()

	// Check if user is already in another guild
	var existingGuildID uuid.UUID
	checkQuery := `SELECT guild_id FROM social.guild_members WHERE user_id = $1`
	err = tx.QueryRowContext(ctx, checkQuery, userID).Scan(&existingGuildID)
	if err != nil && err != sql.ErrNoRows {
		r.logger.Errorf("Failed to check existing membership: %v", err)
		return err
	}
	if err != sql.ErrNoRows {
		r.logger.Warnf("User %s is already in guild %s", userID, existingGuildID)
		return sql.ErrNoRows // User already in a guild
	}

	// Add member to guild
	insertQuery := `
		INSERT INTO social.guild_members (user_id, guild_id, role)
		VALUES ($1, $2, $3)
	`
	_, err = tx.ExecContext(ctx, insertQuery, userID, guildID, role)
	if err != nil {
		r.logger.Errorf("Failed to add guild member: %v", err)
		return err
	}

	// Update member count
	updateQuery := `
		UPDATE social.guilds
		SET member_count = member_count + 1, updated_at = NOW()
		WHERE id = $1 AND is_active = true
	`
	_, err = tx.ExecContext(ctx, updateQuery, guildID)
	if err != nil {
		r.logger.Errorf("Failed to update member count: %v", err)
		return err
	}

	// Commit transaction
	if err = tx.Commit(); err != nil {
		r.logger.Errorf("Failed to commit transaction: %v", err)
		return err
	}

	// Invalidate cache
	cacheKey := "guild:" + guildID.String()
	r.redis.Del(ctx, cacheKey)

	r.logger.Infof("Successfully added member %s to guild %s", userID, guildID)
	return nil
}

// RemoveGuildMember removes a member from a guild
func (r *Repository) RemoveGuildMember(ctx context.Context, guildID, userID uuid.UUID) error {
	r.logger.Infof("Removing member %s from guild %s in database", userID, guildID)

	// Start transaction
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		return err
	}
	defer tx.Rollback()

	// Remove member
	deleteQuery := `DELETE FROM social.guild_members WHERE guild_id = $1 AND user_id = $2`
	result, err := tx.ExecContext(ctx, deleteQuery, guildID, userID)
	if err != nil {
		r.logger.Errorf("Failed to remove guild member: %v", err)
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		r.logger.Errorf("Failed to get rows affected: %v", err)
		return err
	}

	if rowsAffected == 0 {
		r.logger.Warnf("Member %s not found in guild %s", userID, guildID)
		return sql.ErrNoRows
	}

	// Update member count
	updateQuery := `
		UPDATE social.guilds
		SET member_count = GREATEST(member_count - 1, 1), updated_at = NOW()
		WHERE id = $1 AND is_active = true
	`
	_, err = tx.ExecContext(ctx, updateQuery, guildID)
	if err != nil {
		r.logger.Errorf("Failed to update member count: %v", err)
		return err
	}

	// Commit transaction
	if err = tx.Commit(); err != nil {
		r.logger.Errorf("Failed to commit transaction: %v", err)
		return err
	}

	// Invalidate cache
	cacheKey := "guild:" + guildID.String()
	r.redis.Del(ctx, cacheKey)

	r.logger.Infof("Successfully removed member %s from guild %s", userID, guildID)
	return nil
}

// CreateAnnouncement creates a new announcement
func (r *Repository) CreateAnnouncement(ctx context.Context, guildID, authorID uuid.UUID, title, content string) (*server.GuildAnnouncement, error) {
	r.logger.Infof("Creating announcement for guild %s in database", guildID)

	query := `
		INSERT INTO social.guild_announcements (guild_id, author_id, title, content)
		VALUES ($1, $2, $3, $4)
		RETURNING id, guild_id, title, content, author_id, created_at, updated_at, is_pinned
	`

	var announcement server.GuildAnnouncement
	err := r.db.QueryRowContext(ctx, query, guildID, authorID, title, content).Scan(
		&announcement.ID, &announcement.GuildID, &announcement.Title,
		&announcement.Content, &announcement.AuthorID, &announcement.CreatedAt,
		&announcement.UpdatedAt, &announcement.IsPinned,
	)
	if err != nil {
		r.logger.Errorf("Failed to create announcement: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully created announcement with ID: %s", announcement.ID)
	return &announcement, nil
}

// ListMembers retrieves guild members
func (r *Repository) ListMembers(ctx context.Context, guildID uuid.UUID) ([]*server.GuildMember, error) {
	r.logger.Infof("Listing members for guild: %s", guildID)

	query := `
		SELECT user_id, guild_id, role, joined_at
		FROM social.guild_members
		WHERE guild_id = $1
		ORDER BY role DESC, joined_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, guildID)
	if err != nil {
		r.logger.Errorf("Failed to list members: %v", err)
		return nil, err
	}
	defer rows.Close()

	var members []*server.GuildMember
	for rows.Next() {
		var member server.GuildMember
		err := rows.Scan(&member.UserID, &member.GuildID, &member.Role, &member.JoinedAt)
		if err != nil {
			r.logger.Errorf("Failed to scan member: %v", err)
			return nil, err
		}
		members = append(members, &member)
	}

	if err = rows.Err(); err != nil {
		r.logger.Errorf("Error iterating members: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully retrieved %d members for guild %s", len(members), guildID)
	return members, nil
}

// ListAnnouncements retrieves guild announcements
func (r *Repository) ListAnnouncements(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*server.GuildAnnouncement, error) {
	r.logger.Infof("Listing announcements for guild: %s", guildID)

	if limit <= 0 || limit > 50 {
		limit = 20
	}
	if offset < 0 {
		offset = 0
	}

	query := `
		SELECT id, guild_id, title, content, author_id, created_at, updated_at, is_pinned
		FROM social.guild_announcements
		WHERE guild_id = $1
		ORDER BY is_pinned DESC, created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, guildID, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to list announcements: %v", err)
		return nil, err
	}
	defer rows.Close()

	var announcements []*server.GuildAnnouncement
	for rows.Next() {
		var announcement server.GuildAnnouncement
		err := rows.Scan(
			&announcement.ID, &announcement.GuildID, &announcement.Title,
			&announcement.Content, &announcement.AuthorID, &announcement.CreatedAt,
			&announcement.UpdatedAt, &announcement.IsPinned,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan announcement: %v", err)
			return nil, err
		}
		announcements = append(announcements, &announcement)
	}

	if err = rows.Err(); err != nil {
		r.logger.Errorf("Error iterating announcements: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully retrieved %d announcements for guild %s", len(announcements), guildID)
	return announcements, nil
}

// GetPlayerGuilds retrieves guilds for a specific player
func (r *Repository) GetPlayerGuilds(ctx context.Context, playerID uuid.UUID) ([]*server.Guild, error) {
	r.logger.Infof("Getting guilds for player: %s", playerID)

	query := `
		SELECT g.id, g.name, g.description, g.leader_id, g.created_at, g.updated_at,
		       g.member_count, g.max_members, g.level, g.experience, g.reputation
		FROM social.guilds g
		JOIN social.guild_members gm ON g.id = gm.guild_id
		WHERE gm.user_id = $1 AND g.is_active = true AND g.deleted_at IS NULL
		ORDER BY g.created_at DESC
	`

	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		r.logger.Errorf("Failed to get player guilds: %v", err)
		return nil, err
	}
	defer rows.Close()

	var guilds []*server.Guild
	for rows.Next() {
		var guild server.Guild
		err := rows.Scan(
			&guild.ID, &guild.Name, &guild.Description, &guild.LeaderID,
			&guild.CreatedAt, &guild.UpdatedAt, &guild.MemberCount,
			&guild.MaxMembers, &guild.Level, &guild.Experience,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan guild: %v", err)
			return nil, err
		}
		guilds = append(guilds, &guild)
	}

	if err = rows.Err(); err != nil {
		r.logger.Errorf("Error iterating player guilds: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully retrieved %d guilds for player %s", len(guilds), playerID)
	return guilds, nil
}
