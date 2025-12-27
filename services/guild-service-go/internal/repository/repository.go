// Guild Repository - Database and cache access layer
// Issue: #2247

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/pkg/models"
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

// CachedGuild represents guild data for Redis caching
type CachedGuild struct {
	ID          uuid.UUID `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	LeaderID    uuid.UUID `json:"leader_id"`
	CreatedAt   time.Time `json:"created_at"`
	UpdatedAt   time.Time `json:"updated_at"`
	MemberCount int       `json:"member_count"`
	MaxMembers  int       `json:"max_members"`
	Level       int       `json:"level"`
	Experience  int64     `json:"experience"`
	Reputation  int       `json:"reputation"`
}

// serializeGuild converts models.Guild to CachedGuild for Redis storage
func serializeGuild(guild *models.Guild) *CachedGuild {
	return &CachedGuild{
		ID:          guild.ID,
		Name:        guild.Name,
		Description: guild.Description,
		LeaderID:    guild.LeaderID,
		CreatedAt:   guild.CreatedAt,
		UpdatedAt:   guild.UpdatedAt,
		MemberCount: guild.MemberCount,
		MaxMembers:  guild.MaxMembers,
		Level:       guild.Level,
		Experience:  guild.Experience,
		Reputation:  guild.Reputation,
	}
}

// deserializeGuild converts CachedGuild back to models.Guild
func deserializeGuild(cached *CachedGuild) *models.Guild {
	return &models.Guild{
		ID:          cached.ID,
		Name:        cached.Name,
		Description: cached.Description,
		LeaderID:    cached.LeaderID,
		CreatedAt:   cached.CreatedAt,
		UpdatedAt:   cached.UpdatedAt,
		MemberCount: cached.MemberCount,
		MaxMembers:  cached.MaxMembers,
		Level:       cached.Level,
		Experience:  cached.Experience,
		Reputation:  cached.Reputation,
	}
}

// setGuildCache stores guild in Redis cache
func (r *Repository) setGuildCache(ctx context.Context, guild *models.Guild) error {
	cacheKey := "guild:" + guild.ID.String()
	cached := serializeGuild(guild)

	data, err := json.Marshal(cached)
	if err != nil {
		r.logger.Errorf("Failed to marshal guild for cache: %v", err)
		return err
	}

	// Cache for 10 minutes
	err = r.redis.Set(ctx, cacheKey, data, 10*time.Minute).Err()
	if err != nil {
		r.logger.Errorf("Failed to set guild cache: %v", err)
		return err
	}

	r.logger.Debugf("Guild cached: %s", guild.ID)
	return nil
}

// getGuildCache retrieves guild from Redis cache
func (r *Repository) getGuildCache(ctx context.Context, guildID uuid.UUID) (*models.Guild, error) {
	cacheKey := "guild:" + guildID.String()

	data, err := r.redis.Get(ctx, cacheKey).Result()
	if err != nil {
		if err == redis.Nil {
			r.logger.Debugf("Guild not found in cache: %s", guildID)
			return nil, nil // Cache miss
		}
		r.logger.Errorf("Failed to get guild from cache: %v", err)
		return nil, err
	}

	var cached CachedGuild
	if err := json.Unmarshal([]byte(data), &cached); err != nil {
		r.logger.Errorf("Failed to unmarshal guild from cache: %v", err)
		return nil, err
	}

	guild := deserializeGuild(&cached)
	r.logger.Debugf("Guild retrieved from cache: %s", guildID)
	return guild, nil
}

// invalidateGuildCache removes guild from Redis cache
func (r *Repository) invalidateGuildCache(ctx context.Context, guildID uuid.UUID) error {
	cacheKey := "guild:" + guildID.String()

	err := r.redis.Del(ctx, cacheKey).Err()
	if err != nil {
		r.logger.Errorf("Failed to invalidate guild cache: %v", err)
		return err
	}

	r.logger.Debugf("Guild cache invalidated: %s", guildID)
	return nil
}

// ListGuilds retrieves a paginated list of guilds
func (r *Repository) ListGuilds(ctx context.Context, limit, offset int, sortBy string) ([]*models.Guild, error) {
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

	var guilds []*models.Guild
	for rows.Next() {
		var guild models.Guild
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
func (r *Repository) CreateGuild(ctx context.Context, name, description string, leaderID uuid.UUID) (*models.Guild, error) {
	r.logger.Infof("Creating guild in database: %s", name)

	query := `
		INSERT INTO social.guilds (name, description, leader_id, member_count, max_members, level, experience, reputation)
		VALUES ($1, $2, $3, 1, 50, 1, 0, 0)
		RETURNING id, name, description, leader_id, created_at, updated_at, member_count, max_members, level, experience, reputation
	`

	var guild models.Guild
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
func (r *Repository) GetGuild(ctx context.Context, guildID uuid.UUID) (*models.Guild, error) {
	r.logger.Infof("Getting guild: %s", guildID)

	// Try cache first
	if cachedGuild, err := r.getGuildCache(ctx, guildID); err != nil {
		r.logger.Errorf("Cache error for guild %s: %v", guildID, err)
		// Continue to database query on cache error
	} else if cachedGuild != nil {
		r.logger.Debugf("Guild found in cache: %s", guildID)
		return cachedGuild, nil
	}

	// Query database
	query := `
		SELECT id, name, description, leader_id, created_at, updated_at,
		       member_count, max_members, level, experience, reputation
		FROM social.guilds
		WHERE id = $1 AND is_active = true AND deleted_at IS NULL
	`

	var guild models.Guild
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
	if err := r.setGuildCache(ctx, &guild); err != nil {
		r.logger.Warnf("Failed to cache guild %s: %v", guildID, err)
		// Don't fail the request if caching fails
	}

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
	if err := r.invalidateGuildCache(ctx, guildID); err != nil {
		r.logger.Warnf("Failed to invalidate cache for guild %s: %v", guildID, err)
		// Don't fail the request if cache invalidation fails
	}

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
	if err := r.invalidateGuildCache(ctx, guildID); err != nil {
		r.logger.Warnf("Failed to invalidate cache for guild %s: %v", guildID, err)
		// Don't fail the request if cache invalidation fails
	}

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
	if err := r.invalidateGuildCache(ctx, guildID); err != nil {
		r.logger.Warnf("Failed to invalidate cache for guild %s: %v", guildID, err)
	}

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
	if err := r.invalidateGuildCache(ctx, guildID); err != nil {
		r.logger.Warnf("Failed to invalidate cache for guild %s: %v", guildID, err)
	}

	r.logger.Infof("Successfully removed member %s from guild %s", userID, guildID)
	return nil
}

// CreateAnnouncement creates a new announcement
func (r *Repository) CreateAnnouncement(ctx context.Context, guildID, authorID uuid.UUID, title, content string) (*models.GuildAnnouncement, error) {
	r.logger.Infof("Creating announcement for guild %s in database", guildID)

	query := `
		INSERT INTO social.guild_announcements (guild_id, author_id, title, content)
		VALUES ($1, $2, $3, $4)
		RETURNING id, guild_id, title, content, author_id, created_at, updated_at, is_pinned
	`

	var announcement models.GuildAnnouncement
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
func (r *Repository) ListMembers(ctx context.Context, guildID uuid.UUID) ([]*models.GuildMember, error) {
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

	var members []*models.GuildMember
	for rows.Next() {
		var member models.GuildMember
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
func (r *Repository) ListAnnouncements(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*models.GuildAnnouncement, error) {
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

	var announcements []*models.GuildAnnouncement
	for rows.Next() {
		var announcement models.GuildAnnouncement
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
func (r *Repository) GetPlayerGuilds(ctx context.Context, playerID uuid.UUID) ([]*models.Guild, error) {
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

	var guilds []*models.Guild
	for rows.Next() {
		var guild models.Guild
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

// Voice Channel Repository Methods
// Issue: #2263 - WebRTC Signaling Service Integration with Guild System

// CreateVoiceChannel creates a new voice channel in the database
func (r *Repository) CreateVoiceChannel(ctx context.Context, channel *models.GuildVoiceChannel) error {
	r.logger.Infof("Creating voice channel: %s for guild: %s", channel.Name, channel.GuildID)

	query := `
		INSERT INTO social.guild_voice_channels (
			id, guild_id, name, description, channel_id, max_users, is_private,
			created_by, created_at, updated_at, status
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.ExecContext(ctx, query,
		channel.ID, channel.GuildID, channel.Name, channel.Description,
		channel.ChannelID, channel.MaxUsers, channel.IsPrivate,
		channel.CreatedBy, channel.CreatedAt, channel.UpdatedAt, channel.Status)

	if err != nil {
		r.logger.Errorf("Failed to create voice channel: %v", err)
		return err
	}

	r.logger.Infof("Voice channel created successfully: %s", channel.ID)
	return nil
}

// GetVoiceChannel retrieves a voice channel by ID
func (r *Repository) GetVoiceChannel(ctx context.Context, channelID uuid.UUID) (*models.GuildVoiceChannel, error) {
	r.logger.Infof("Getting voice channel: %s", channelID)

	query := `
		SELECT id, guild_id, name, description, channel_id, max_users, is_private,
		       created_by, created_at, updated_at, status
		FROM social.guild_voice_channels
		WHERE id = $1 AND status = 'active'
	`

	var channel models.GuildVoiceChannel
	err := r.db.QueryRowContext(ctx, query, channelID).Scan(
		&channel.ID, &channel.GuildID, &channel.Name, &channel.Description,
		&channel.ChannelID, &channel.MaxUsers, &channel.IsPrivate,
		&channel.CreatedBy, &channel.CreatedAt, &channel.UpdatedAt, &channel.Status)

	if err != nil {
		r.logger.Errorf("Failed to get voice channel: %v", err)
		return nil, err
	}

	return &channel, nil
}

// ListVoiceChannels lists all voice channels for a guild
func (r *Repository) ListVoiceChannels(ctx context.Context, guildID uuid.UUID) ([]*models.GuildVoiceChannel, error) {
	r.logger.Infof("Listing voice channels for guild: %s", guildID)

	query := `
		SELECT id, guild_id, name, description, channel_id, max_users, is_private,
		       created_by, created_at, updated_at, status
		FROM social.guild_voice_channels
		WHERE guild_id = $1 AND status = 'active'
		ORDER BY created_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, guildID)
	if err != nil {
		r.logger.Errorf("Failed to list voice channels: %v", err)
		return nil, err
	}
	defer rows.Close()

	var channels []*models.GuildVoiceChannel
	for rows.Next() {
		var channel models.GuildVoiceChannel
		err := rows.Scan(
			&channel.ID, &channel.GuildID, &channel.Name, &channel.Description,
			&channel.ChannelID, &channel.MaxUsers, &channel.IsPrivate,
			&channel.CreatedBy, &channel.CreatedAt, &channel.UpdatedAt, &channel.Status)
		if err != nil {
			r.logger.Errorf("Failed to scan voice channel: %v", err)
			return nil, err
		}
		channels = append(channels, &channel)
	}

	if err = rows.Err(); err != nil {
		r.logger.Errorf("Error iterating voice channels: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully retrieved %d voice channels for guild %s", len(channels), guildID)
	return channels, nil
}

// UpdateVoiceChannel updates voice channel settings
func (r *Repository) UpdateVoiceChannel(ctx context.Context, channelID uuid.UUID, name, description string, maxUsers int) error {
	r.logger.Infof("Updating voice channel: %s", channelID)

	query := `
		UPDATE social.guild_voice_channels
		SET name = $1, description = $2, max_users = $3, updated_at = CURRENT_TIMESTAMP
		WHERE id = $4 AND status = 'active'
	`

	result, err := r.db.ExecContext(ctx, query, name, description, maxUsers, channelID)
	if err != nil {
		r.logger.Errorf("Failed to update voice channel: %v", err)
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		r.logger.Errorf("Failed to get rows affected: %v", err)
		return err
	}

	if rowsAffected == 0 {
		return fmt.Errorf("voice channel not found or not active")
	}

	r.logger.Infof("Voice channel updated successfully: %s", channelID)
	return nil
}

// DeleteVoiceChannel marks a voice channel as deleted
func (r *Repository) DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error {
	r.logger.Infof("Deleting voice channel: %s", channelID)

	query := `
		UPDATE social.guild_voice_channels
		SET status = 'inactive', updated_at = CURRENT_TIMESTAMP
		WHERE id = $1 AND status = 'active'
	`

	result, err := r.db.ExecContext(ctx, query, channelID)
	if err != nil {
		r.logger.Errorf("Failed to delete voice channel: %v", err)
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		r.logger.Errorf("Failed to get rows affected: %v", err)
		return err
	}

	if rowsAffected == 0 {
		return fmt.Errorf("voice channel not found or not active")
	}

	r.logger.Infof("Voice channel deleted successfully: %s", channelID)
	return nil
}

// Voice Participant Methods

// AddVoiceParticipant adds a user to a voice channel
func (r *Repository) AddVoiceParticipant(ctx context.Context, participant *models.GuildVoiceParticipant) error {
	r.logger.Infof("Adding voice participant: %s to channel: %s", participant.UserID, participant.ChannelID)

	query := `
		INSERT INTO social.guild_voice_participants (
			user_id, channel_id, guild_id, joined_at, is_muted, is_deafened, webrtc_id
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err := r.db.ExecContext(ctx, query,
		participant.UserID, participant.ChannelID, participant.GuildID,
		participant.JoinedAt, participant.IsMuted, participant.IsDeafened, participant.WebRTCID)

	if err != nil {
		r.logger.Errorf("Failed to add voice participant: %v", err)
		return err
	}

	r.logger.Infof("Voice participant added successfully")
	return nil
}

// RemoveVoiceParticipant removes a user from a voice channel
func (r *Repository) RemoveVoiceParticipant(ctx context.Context, channelID, userID uuid.UUID) error {
	r.logger.Infof("Removing voice participant: %s from channel: %s", userID, channelID)

	query := `
		DELETE FROM social.guild_voice_participants
		WHERE channel_id = $1 AND user_id = $2
	`

	result, err := r.db.ExecContext(ctx, query, channelID, userID)
	if err != nil {
		r.logger.Errorf("Failed to remove voice participant: %v", err)
		return err
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		r.logger.Errorf("Failed to get rows affected: %v", err)
		return err
	}

	if rowsAffected == 0 {
		return fmt.Errorf("participant not found in voice channel")
	}

	r.logger.Infof("Voice participant removed successfully")
	return nil
}

// GetVoiceParticipant checks if a user is in a voice channel
func (r *Repository) GetVoiceParticipant(ctx context.Context, channelID, userID uuid.UUID) (*models.GuildVoiceParticipant, error) {
	r.logger.Infof("Getting voice participant: %s in channel: %s", userID, channelID)

	query := `
		SELECT user_id, channel_id, guild_id, joined_at, is_muted, is_deafened, webrtc_id
		FROM social.guild_voice_participants
		WHERE channel_id = $1 AND user_id = $2
	`

	var participant models.GuildVoiceParticipant
	err := r.db.QueryRowContext(ctx, query, channelID, userID).Scan(
		&participant.UserID, &participant.ChannelID, &participant.GuildID,
		&participant.JoinedAt, &participant.IsMuted, &participant.IsDeafened, &participant.WebRTCID)

	if err != nil {
		r.logger.Errorf("Failed to get voice participant: %v", err)
		return nil, err
	}

	return &participant, nil
}

// ListVoiceParticipants lists all participants in a voice channel
func (r *Repository) ListVoiceParticipants(ctx context.Context, channelID uuid.UUID) ([]*models.GuildVoiceParticipant, error) {
	r.logger.Infof("Listing voice participants for channel: %s", channelID)

	query := `
		SELECT user_id, channel_id, guild_id, joined_at, is_muted, is_deafened, webrtc_id
		FROM social.guild_voice_participants
		WHERE channel_id = $1
		ORDER BY joined_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, channelID)
	if err != nil {
		r.logger.Errorf("Failed to list voice participants: %v", err)
		return nil, err
	}
	defer rows.Close()

	var participants []*models.GuildVoiceParticipant
	for rows.Next() {
		var participant models.GuildVoiceParticipant
		err := rows.Scan(
			&participant.UserID, &participant.ChannelID, &participant.GuildID,
			&participant.JoinedAt, &participant.IsMuted, &participant.IsDeafened, &participant.WebRTCID)
		if err != nil {
			r.logger.Errorf("Failed to scan voice participant: %v", err)
			return nil, err
		}
		participants = append(participants, &participant)
	}

	if err = rows.Err(); err != nil {
		r.logger.Errorf("Error iterating voice participants: %v", err)
		return nil, err
	}

	r.logger.Infof("Successfully retrieved %d voice participants for channel %s", len(participants), channelID)
	return participants, nil
}

// CountVoiceParticipants counts participants in a voice channel
func (r *Repository) CountVoiceParticipants(ctx context.Context, channelID uuid.UUID) (int, error) {
	r.logger.Infof("Counting voice participants for channel: %s", channelID)

	query := `SELECT COUNT(*) FROM social.guild_voice_participants WHERE channel_id = $1`

	var count int
	err := r.db.QueryRowContext(ctx, query, channelID).Scan(&count)
	if err != nil {
		r.logger.Errorf("Failed to count voice participants: %v", err)
		return 0, err
	}

	return count, nil
}

// RemoveAllVoiceParticipants removes all participants from a voice channel
func (r *Repository) RemoveAllVoiceParticipants(ctx context.Context, channelID uuid.UUID) error {
	r.logger.Infof("Removing all voice participants from channel: %s", channelID)

	query := `DELETE FROM social.guild_voice_participants WHERE channel_id = $1`

	_, err := r.db.ExecContext(ctx, query, channelID)
	if err != nil {
		r.logger.Errorf("Failed to remove all voice participants: %v", err)
		return err
	}

	r.logger.Infof("All voice participants removed from channel: %s", channelID)
	return nil
}
