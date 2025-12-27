// Issue: #140895495
// PERFORMANCE: Database layer with connection pooling and prepared statements

package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// VoiceChatRepositoryInterface defines the contract for voice chat data operations
type VoiceChatRepositoryInterface interface {
	CreateVoiceChannel(ctx context.Context, channel *VoiceChannel) error
	GetVoiceChannelByID(ctx context.Context, channelID uuid.UUID) (*VoiceChannel, error)
	ListVoiceChannels(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*VoiceChannel, error)
	UpdateVoiceChannel(ctx context.Context, channel *VoiceChannel) error
	DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error

	AddUserToChannel(ctx context.Context, userID, channelID uuid.UUID) error
	RemoveUserFromChannel(ctx context.Context, userID, channelID uuid.UUID) error
	GetChannelUsers(ctx context.Context, channelID uuid.UUID) ([]*VoiceChannelUser, error)

	CreateVoiceSession(ctx context.Context, session *VoiceSession) error
	GetVoiceSession(ctx context.Context, sessionID uuid.UUID) (*VoiceSession, error)
	EndVoiceSession(ctx context.Context, sessionID uuid.UUID) error
}

// VoiceChatRepository implements VoiceChatRepositoryInterface
type VoiceChatRepository struct {
	logger *zap.Logger
	db     *pgxpool.Pool
}

// NewVoiceChatRepository creates a new voice chat repository
func NewVoiceChatRepository(logger *zap.Logger, db *pgxpool.Pool) *VoiceChatRepository {
	return &VoiceChatRepository{
		logger: logger,
		db:     db,
	}
}

// CreateVoiceChannel creates a new voice channel
func (r *VoiceChatRepository) CreateVoiceChannel(ctx context.Context, channel *VoiceChannel) error {
	query := `
		INSERT INTO voice_channels (id, guild_id, name, description, max_users, is_locked, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.Exec(ctx, query,
		channel.ID, channel.GuildID, channel.Name, channel.Description,
		channel.MaxUsers, channel.IsLocked, channel.CreatedAt, channel.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to create voice channel", zap.Error(err), zap.String("channel_id", channel.ID.String()))
		return err
	}

	r.logger.Info("Voice channel created", zap.String("channel_id", channel.ID.String()))
	return nil
}

// GetVoiceChannelByID retrieves a voice channel by ID
func (r *VoiceChatRepository) GetVoiceChannelByID(ctx context.Context, channelID uuid.UUID) (*VoiceChannel, error) {
	query := `
		SELECT id, guild_id, name, description, max_users, is_locked, created_at, updated_at
		FROM voice_channels
		WHERE id = $1
	`

	var channel VoiceChannel
	err := r.db.QueryRow(ctx, query, channelID).Scan(
		&channel.ID, &channel.GuildID, &channel.Name, &channel.Description,
		&channel.MaxUsers, &channel.IsLocked, &channel.CreatedAt, &channel.UpdatedAt)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		r.logger.Error("Failed to get voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return nil, err
	}

	return &channel, nil
}

// ListVoiceChannels lists voice channels for a guild with pagination
func (r *VoiceChatRepository) ListVoiceChannels(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*VoiceChannel, error) {
	query := `
		SELECT id, guild_id, name, description, max_users, is_locked, created_at, updated_at
		FROM voice_channels
		WHERE guild_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, guildID, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list voice channels", zap.Error(err), zap.String("guild_id", guildID.String()))
		return nil, err
	}
	defer rows.Close()

	var channels []*VoiceChannel
	for rows.Next() {
		var channel VoiceChannel
		err := rows.Scan(
			&channel.ID, &channel.GuildID, &channel.Name, &channel.Description,
			&channel.MaxUsers, &channel.IsLocked, &channel.CreatedAt, &channel.UpdatedAt)
		if err != nil {
			r.logger.Error("Failed to scan voice channel", zap.Error(err))
			return nil, err
		}
		channels = append(channels, &channel)
	}

	return channels, rows.Err()
}

// UpdateVoiceChannel updates a voice channel
func (r *VoiceChatRepository) UpdateVoiceChannel(ctx context.Context, channel *VoiceChannel) error {
	query := `
		UPDATE voice_channels
		SET name = $1, description = $2, max_users = $3, is_locked = $4, updated_at = $5
		WHERE id = $6
	`

	result, err := r.db.Exec(ctx, query,
		channel.Name, channel.Description, channel.MaxUsers,
		channel.IsLocked, time.Now(), channel.ID)

	if err != nil {
		r.logger.Error("Failed to update voice channel", zap.Error(err), zap.String("channel_id", channel.ID.String()))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		return sql.ErrNoRows
	}

	r.logger.Info("Voice channel updated", zap.String("channel_id", channel.ID.String()))
	return nil
}

// DeleteVoiceChannel deletes a voice channel
func (r *VoiceChatRepository) DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error {
	query := `DELETE FROM voice_channels WHERE id = $1`

	result, err := r.db.Exec(ctx, query, channelID)
	if err != nil {
		r.logger.Error("Failed to delete voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		return sql.ErrNoRows
	}

	r.logger.Info("Voice channel deleted", zap.String("channel_id", channelID.String()))
	return nil
}

// AddUserToChannel adds a user to a voice channel
func (r *VoiceChatRepository) AddUserToChannel(ctx context.Context, userID, channelID uuid.UUID) error {
	query := `
		INSERT INTO voice_channel_users (user_id, channel_id, joined_at)
		VALUES ($1, $2, $3)
	`

	_, err := r.db.Exec(ctx, query, userID, channelID, time.Now())
	if err != nil {
		r.logger.Error("Failed to add user to voice channel",
			zap.Error(err),
			zap.String("user_id", userID.String()),
			zap.String("channel_id", channelID.String()))
		return err
	}

	r.logger.Info("User added to voice channel",
		zap.String("user_id", userID.String()),
		zap.String("channel_id", channelID.String()))
	return nil
}

// RemoveUserFromChannel removes a user from a voice channel
func (r *VoiceChatRepository) RemoveUserFromChannel(ctx context.Context, userID, channelID uuid.UUID) error {
	query := `DELETE FROM voice_channel_users WHERE user_id = $1 AND channel_id = $2`

	result, err := r.db.Exec(ctx, query, userID, channelID)
	if err != nil {
		r.logger.Error("Failed to remove user from voice channel",
			zap.Error(err),
			zap.String("user_id", userID.String()),
			zap.String("channel_id", channelID.String()))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		return sql.ErrNoRows
	}

	r.logger.Info("User removed from voice channel",
		zap.String("user_id", userID.String()),
		zap.String("channel_id", channelID.String()))
	return nil
}

// GetChannelUsers gets all users in a voice channel
func (r *VoiceChatRepository) GetChannelUsers(ctx context.Context, channelID uuid.UUID) ([]*VoiceChannelUser, error) {
	query := `
		SELECT u.user_id, u.channel_id, u.joined_at, usr.username
		FROM voice_channel_users u
		JOIN users usr ON u.user_id = usr.id
		WHERE u.channel_id = $1
		ORDER BY u.joined_at ASC
	`

	rows, err := r.db.Query(ctx, query, channelID)
	if err != nil {
		r.logger.Error("Failed to get channel users", zap.Error(err), zap.String("channel_id", channelID.String()))
		return nil, err
	}
	defer rows.Close()

	var users []*VoiceChannelUser
	for rows.Next() {
		var user VoiceChannelUser
		err := rows.Scan(&user.UserID, &user.ChannelID, &user.JoinedAt, &user.Username)
		if err != nil {
			r.logger.Error("Failed to scan channel user", zap.Error(err))
			return nil, err
		}
		users = append(users, &user)
	}

	return users, rows.Err()
}

// CreateVoiceSession creates a new voice session
func (r *VoiceChatRepository) CreateVoiceSession(ctx context.Context, session *VoiceSession) error {
	query := `
		INSERT INTO voice_sessions (id, user_id, channel_id, started_at, is_muted, is_deafened)
		VALUES ($1, $2, $3, $4, $5, $6)
	`

	_, err := r.db.Exec(ctx, query,
		session.ID, session.UserID, session.ChannelID,
		session.StartedAt, session.IsMuted, session.IsDeafened)

	if err != nil {
		r.logger.Error("Failed to create voice session",
			zap.Error(err),
			zap.String("session_id", session.ID.String()))
		return err
	}

	r.logger.Info("Voice session created", zap.String("session_id", session.ID.String()))
	return nil
}

// GetVoiceSession gets a voice session by ID
func (r *VoiceChatRepository) GetVoiceSession(ctx context.Context, sessionID uuid.UUID) (*VoiceSession, error) {
	query := `
		SELECT id, user_id, channel_id, started_at, ended_at, is_muted, is_deafened
		FROM voice_sessions
		WHERE id = $1
	`

	var session VoiceSession
	var endedAt sql.NullTime
	err := r.db.QueryRow(ctx, query, sessionID).Scan(
		&session.ID, &session.UserID, &session.ChannelID,
		&session.StartedAt, &endedAt, &session.IsMuted, &session.IsDeafened)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil
		}
		r.logger.Error("Failed to get voice session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return nil, err
	}

	if endedAt.Valid {
		session.EndedAt = &endedAt.Time
	}

	return &session, nil
}

// EndVoiceSession ends a voice session
func (r *VoiceChatRepository) EndVoiceSession(ctx context.Context, sessionID uuid.UUID) error {
	query := `UPDATE voice_sessions SET ended_at = $1 WHERE id = $2 AND ended_at IS NULL`

	result, err := r.db.Exec(ctx, query, time.Now(), sessionID)
	if err != nil {
		r.logger.Error("Failed to end voice session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		return sql.ErrNoRows
	}

	r.logger.Info("Voice session ended", zap.String("session_id", sessionID.String()))
	return nil
}
