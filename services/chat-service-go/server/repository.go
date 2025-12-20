// Package server Issue: #1598
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

// Repository handles database operations
type Repository struct {
	db *sql.DB
}

// NewRepository creates new repository
func NewRepository(connStr string) (*Repository, error) {
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance (Issue #1605)
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	if err := db.Ping(); err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() error {
	return r.db.Close()
}

// Channel operations

// CreateChannel creates a new channel
func (r *Repository) CreateChannel(ctx context.Context, ownerID *uuid.UUID, channelType, name, description string) (*uuid.UUID, error) {
	var id uuid.UUID
	query := `INSERT INTO social.chat_channels (owner_id, type, name, description, created_at, updated_at)
		VALUES ($1, $2, $3, $4, NOW(), NOW())
		RETURNING id`

	err := r.db.QueryRowContext(ctx, query, ownerID, channelType, name, description).Scan(&id)
	if err != nil {
		return nil, err
	}
	return &id, nil
}

// GetChannel retrieves channel by ID
func (r *Repository) GetChannel(ctx context.Context, channelID uuid.UUID) (*Channel, error) {
	var ch Channel
	query := `SELECT id, owner_id, type, name, description, created_at, updated_at, 
		cooldown_seconds, max_length, is_active
		FROM social.chat_channels
		WHERE id = $1 AND deleted_at IS NULL`

	err := r.db.QueryRowContext(ctx, query, channelID).Scan(
		&ch.ID, &ch.OwnerID, &ch.Type, &ch.Name, &ch.Description, &ch.CreatedAt, &ch.UpdatedAt,
		&ch.CooldownSeconds, &ch.MaxLength, &ch.IsActive,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}
	return &ch, nil
}

// ListChannels retrieves all active channels
func (r *Repository) ListChannels(ctx context.Context) ([]Channel, error) {
	query := `SELECT id, owner_id, type, name, description, created_at, updated_at,
		cooldown_seconds, max_length, is_active
		FROM social.chat_channels
		WHERE deleted_at IS NULL AND is_active = true
		ORDER BY created_at`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var channels []Channel
	for rows.Next() {
		var ch Channel
		err := rows.Scan(
			&ch.ID, &ch.OwnerID, &ch.Type, &ch.Name, &ch.Description, &ch.CreatedAt, &ch.UpdatedAt,
			&ch.CooldownSeconds, &ch.MaxLength, &ch.IsActive,
		)
		if err != nil {
			return nil, err
		}
		channels = append(channels, ch)
	}
	return channels, rows.Err()
}

// UpdateChannel updates channel settings
func (r *Repository) UpdateChannel(ctx context.Context, channelID uuid.UUID, name, description *string) error {
	query := `UPDATE social.chat_channels
		SET name = COALESCE($1, name),
		    description = COALESCE($2, description),
		    updated_at = NOW()
		WHERE id = $3 AND deleted_at IS NULL`

	result, err := r.db.ExecContext(ctx, query, name, description, channelID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return ErrNotFound
	}
	return nil
}

// DeleteChannel soft deletes a channel
func (r *Repository) DeleteChannel(ctx context.Context, channelID uuid.UUID) error {
	query := `UPDATE social.chat_channels
		SET deleted_at = NOW(), is_active = false
		WHERE id = $1 AND deleted_at IS NULL`

	result, err := r.db.ExecContext(ctx, query, channelID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return ErrNotFound
	}
	return nil
}

// GetChannelMemberCount gets member count for channel
func (r *Repository) GetChannelMemberCount(ctx context.Context, channelID uuid.UUID) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM social.chat_channel_members WHERE channel_id = $1`
	err := r.db.QueryRowContext(ctx, query, channelID).Scan(&count)
	return count, err
}

// Message operations

// CreateMessage creates a new message
func (r *Repository) CreateMessage(ctx context.Context, channelID, senderID uuid.UUID, content, formatted, channelType, senderName string) (*uuid.UUID, error) {
	var id uuid.UUID
	query := `INSERT INTO social.chat_messages (channel_id, sender_id, content, formatted, channel_type, sender_name, created_at)
		VALUES ($1, $2, $3, $4, $5, $6, NOW())
		RETURNING id`

	err := r.db.QueryRowContext(ctx, query, channelID, senderID, content, formatted, channelType, senderName).Scan(&id)
	if err != nil {
		return nil, err
	}
	return &id, nil
}

// GetChannelMessages retrieves messages for a channel
func (r *Repository) GetChannelMessages(ctx context.Context, channelID uuid.UUID, limit int, before, after *uuid.UUID) ([]Message, error) {
	var query string
	var args []interface{}

	if before != nil {
		query = `SELECT id, channel_id, sender_id, content, formatted, channel_type, sender_name, created_at
			FROM social.chat_messages
			WHERE channel_id = $1 AND deleted_at IS NULL AND id < $2
			ORDER BY created_at DESC
			LIMIT $3`
		args = []interface{}{channelID, *before, limit}
	} else if after != nil {
		query = `SELECT id, channel_id, sender_id, content, formatted, channel_type, sender_name, created_at
			FROM social.chat_messages
			WHERE channel_id = $1 AND deleted_at IS NULL AND id > $2
			ORDER BY created_at ASC
			LIMIT $3`
		args = []interface{}{channelID, *after, limit}
	} else {
		query = `SELECT id, channel_id, sender_id, content, formatted, channel_type, sender_name, created_at
			FROM social.chat_messages
			WHERE channel_id = $1 AND deleted_at IS NULL
			ORDER BY created_at DESC
			LIMIT $2`
		args = []interface{}{channelID, limit}
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var messages []Message
	for rows.Next() {
		var msg Message
		err := rows.Scan(
			&msg.ID, &msg.ChannelID, &msg.SenderID, &msg.Content, &msg.Formatted,
			&msg.ChannelType, &msg.SenderName, &msg.CreatedAt,
		)
		if err != nil {
			return nil, err
		}
		messages = append(messages, msg)
	}
	return messages, rows.Err()
}

// DeleteMessage soft deletes a message
func (r *Repository) DeleteMessage(ctx context.Context, messageID uuid.UUID) error {
	query := `UPDATE social.chat_messages
		SET deleted_at = NOW()
		WHERE id = $1 AND deleted_at IS NULL`

	result, err := r.db.ExecContext(ctx, query, messageID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return ErrNotFound
	}
	return nil
}

// Member operations

// AddChannelMember adds a member to a channel
func (r *Repository) AddChannelMember(ctx context.Context, channelID, playerID uuid.UUID) error {
	query := `INSERT INTO social.chat_channel_members (channel_id, player_id, joined_at)
		VALUES ($1, $2, NOW())
		ON CONFLICT (channel_id, player_id) DO NOTHING`

	_, err := r.db.ExecContext(ctx, query, channelID, playerID)
	return err
}

// RemoveChannelMember removes a member from a channel
func (r *Repository) RemoveChannelMember(ctx context.Context, channelID, playerID uuid.UUID) error {
	query := `DELETE FROM social.chat_channel_members
		WHERE channel_id = $1 AND player_id = $2`

	result, err := r.db.ExecContext(ctx, query, channelID, playerID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return ErrNotFound
	}
	return nil
}

// IsChannelMember checks if player is a member
func (r *Repository) IsChannelMember(ctx context.Context, channelID, playerID uuid.UUID) (bool, error) {
	var exists bool
	query := `SELECT EXISTS(SELECT 1 FROM social.chat_channel_members WHERE channel_id = $1 AND player_id = $2)`
	err := r.db.QueryRowContext(ctx, query, channelID, playerID).Scan(&exists)
	return exists, err
}

// Ban operations

// CreateBan creates a new ban
func (r *Repository) CreateBan(ctx context.Context, characterID uuid.UUID, channelID *uuid.UUID, reason string, expiresAt *time.Time) (*uuid.UUID, error) {
	var id uuid.UUID
	query := `INSERT INTO social.chat_bans (character_id, channel_id, reason, expires_at, created_at, is_active)
		VALUES ($1, $2, $3, $4, NOW(), true)
		RETURNING id`

	err := r.db.QueryRowContext(ctx, query, characterID, channelID, reason, expiresAt).Scan(&id)
	if err != nil {
		return nil, err
	}
	return &id, nil
}

// GetBan retrieves ban by ID
func (r *Repository) GetBan(ctx context.Context, banID uuid.UUID) (*Ban, error) {
	var ban Ban
	query := `SELECT id, character_id, channel_id, reason, expires_at, created_at, is_active
		FROM social.chat_bans
		WHERE id = $1 AND is_active = true`

	err := r.db.QueryRowContext(ctx, query, banID).Scan(
		&ban.ID, &ban.CharacterID, &ban.ChannelID, &ban.Reason, &ban.ExpiresAt, &ban.CreatedAt, &ban.IsActive,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}
	return &ban, nil
}

// DeleteBan removes a ban
func (r *Repository) DeleteBan(ctx context.Context, banID uuid.UUID) error {
	query := `UPDATE social.chat_bans
		SET is_active = false
		WHERE id = $1 AND is_active = true`

	result, err := r.db.ExecContext(ctx, query, banID)
	if err != nil {
		return err
	}
	rows, err := result.RowsAffected()
	if err != nil {
		return err
	}
	if rows == 0 {
		return ErrNotFound
	}
	return nil
}

// IsPlayerBanned checks if player is banned
func (r *Repository) IsPlayerBanned(ctx context.Context, playerID uuid.UUID, channelID *uuid.UUID) (bool, error) {
	var query string
	var args []interface{}

	if channelID != nil {
		query = `SELECT EXISTS(
			SELECT 1 FROM social.chat_bans
			WHERE character_id = $1 AND (channel_id = $2 OR channel_id IS NULL)
			AND is_active = true
			AND (expires_at IS NULL OR expires_at > NOW())
		)`
		args = []interface{}{playerID, *channelID}
	} else {
		query = `SELECT EXISTS(
			SELECT 1 FROM social.chat_bans
			WHERE character_id = $1 AND channel_id IS NULL
			AND is_active = true
			AND (expires_at IS NULL OR expires_at > NOW())
		)`
		args = []interface{}{playerID}
	}

	var exists bool
	err := r.db.QueryRowContext(ctx, query, args...).Scan(&exists)
	return exists, err
}

// Internal models

type Channel struct {
	ID              uuid.UUID
	OwnerID         *uuid.UUID
	Type            string
	Name            string
	Description     *string
	CreatedAt       time.Time
	UpdatedAt       time.Time
	CooldownSeconds int
	MaxLength       int
	IsActive        bool
}

type Message struct {
	ID          uuid.UUID
	ChannelID   uuid.UUID
	SenderID    uuid.UUID
	Content     string
	Formatted   *string
	ChannelType string
	SenderName  string
	CreatedAt   time.Time
}

type Ban struct {
	ID          uuid.UUID
	CharacterID uuid.UUID
	ChannelID   *uuid.UUID
	Reason      string
	ExpiresAt   *time.Time
	CreatedAt   time.Time
	IsActive    bool
}
