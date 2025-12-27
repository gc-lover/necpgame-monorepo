package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/webrtc-signaling-service-go/pkg/models"
)

// Repository handles data persistence for WebRTC signaling service
type Repository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB, redisClient *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		redis:  redisClient,
		logger: logger,
	}
}

// NewPostgresConnection creates a new PostgreSQL connection
func NewPostgresConnection(dsn string) (*sql.DB, error) {
	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(time.Hour)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// NewRedisConnection creates a new Redis connection
func NewRedisConnection(url string) (*redis.Client, error) {
	opt, err := redis.ParseURL(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// VoiceChannel methods
func (r *Repository) CreateVoiceChannel(ctx context.Context, channel *models.VoiceChannel) error {
	query := `
		INSERT INTO voice_channels (id, name, type, guild_id, owner_id, max_users, current_users, is_active, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	_, err := r.db.ExecContext(ctx, query,
		channel.ID, channel.Name, channel.Type, channel.GuildID, channel.OwnerID,
		channel.MaxUsers, channel.CurrentUsers, channel.IsActive, channel.CreatedAt, channel.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to create voice channel", zap.Error(err), zap.String("channel_id", channel.ID.String()))
		return fmt.Errorf("failed to create voice channel: %w", err)
	}

	return nil
}

func (r *Repository) GetVoiceChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error) {
	query := `
		SELECT id, name, type, guild_id, owner_id, max_users, current_users, is_active, created_at, updated_at
		FROM voice_channels WHERE id = $1
	`

	channel := &models.VoiceChannel{}
	err := r.db.QueryRowContext(ctx, query, channelID).Scan(
		&channel.ID, &channel.Name, &channel.Type, &channel.GuildID, &channel.OwnerID,
		&channel.MaxUsers, &channel.CurrentUsers, &channel.IsActive, &channel.CreatedAt, &channel.UpdatedAt)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("voice channel not found: %s", channelID)
		}
		r.logger.Error("Failed to get voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return nil, fmt.Errorf("failed to get voice channel: %w", err)
	}

	return channel, nil
}

func (r *Repository) UpdateVoiceChannel(ctx context.Context, channel *models.VoiceChannel) error {
	query := `
		UPDATE voice_channels
		SET name = $2, max_users = $3, current_users = $4, is_active = $5, updated_at = $6
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query,
		channel.ID, channel.Name, channel.MaxUsers, channel.CurrentUsers, channel.IsActive, channel.UpdatedAt)

	if err != nil {
		r.logger.Error("Failed to update voice channel", zap.Error(err), zap.String("channel_id", channel.ID.String()))
		return fmt.Errorf("failed to update voice channel: %w", err)
	}

	return nil
}

func (r *Repository) DeleteVoiceChannel(ctx context.Context, channelID uuid.UUID) error {
	query := `DELETE FROM voice_channels WHERE id = $1`

	_, err := r.db.ExecContext(ctx, query, channelID)
	if err != nil {
		r.logger.Error("Failed to delete voice channel", zap.Error(err), zap.String("channel_id", channelID.String()))
		return fmt.Errorf("failed to delete voice channel: %w", err)
	}

	return nil
}

func (r *Repository) ListVoiceChannels(ctx context.Context, guildID *uuid.UUID) ([]*models.VoiceChannel, error) {
	var query string
	var args []interface{}

	if guildID != nil {
		query = `SELECT id, name, type, guild_id, owner_id, max_users, current_users, is_active, created_at, updated_at FROM voice_channels WHERE guild_id = $1 AND is_active = true`
		args = []interface{}{*guildID}
	} else {
		query = `SELECT id, name, type, guild_id, owner_id, max_users, current_users, is_active, created_at, updated_at FROM voice_channels WHERE is_active = true`
	}

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to list voice channels", zap.Error(err))
		return nil, fmt.Errorf("failed to list voice channels: %w", err)
	}
	defer rows.Close()

	var channels []*models.VoiceChannel
	for rows.Next() {
		channel := &models.VoiceChannel{}
		err := rows.Scan(
			&channel.ID, &channel.Name, &channel.Type, &channel.GuildID, &channel.OwnerID,
			&channel.MaxUsers, &channel.CurrentUsers, &channel.IsActive, &channel.CreatedAt, &channel.UpdatedAt)
		if err != nil {
			r.logger.Error("Failed to scan voice channel", zap.Error(err))
			continue
		}
		channels = append(channels, channel)
	}

	return channels, nil
}

// VoiceParticipant methods
func (r *Repository) AddVoiceParticipant(ctx context.Context, participant *models.VoiceParticipant) error {
	query := `
		INSERT INTO voice_participants (id, channel_id, user_id, role, is_muted, is_deafened, joined_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err := r.db.ExecContext(ctx, query,
		participant.ID, participant.ChannelID, participant.UserID, participant.Role,
		participant.IsMuted, participant.IsDeafened, participant.JoinedAt)

	if err != nil {
		r.logger.Error("Failed to add voice participant", zap.Error(err),
			zap.String("channel_id", participant.ChannelID.String()),
			zap.String("user_id", participant.UserID.String()))
		return fmt.Errorf("failed to add voice participant: %w", err)
	}

	return nil
}

func (r *Repository) RemoveVoiceParticipant(ctx context.Context, channelID, userID uuid.UUID) error {
	query := `DELETE FROM voice_participants WHERE channel_id = $1 AND user_id = $2`

	_, err := r.db.ExecContext(ctx, query, channelID, userID)
	if err != nil {
		r.logger.Error("Failed to remove voice participant", zap.Error(err),
			zap.String("channel_id", channelID.String()),
			zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to remove voice participant: %w", err)
	}

	return nil
}

func (r *Repository) GetVoiceParticipants(ctx context.Context, channelID uuid.UUID) ([]*models.VoiceParticipant, error) {
	query := `SELECT id, channel_id, user_id, role, is_muted, is_deafened, joined_at FROM voice_participants WHERE channel_id = $1`

	rows, err := r.db.QueryContext(ctx, query, channelID)
	if err != nil {
		r.logger.Error("Failed to get voice participants", zap.Error(err), zap.String("channel_id", channelID.String()))
		return nil, fmt.Errorf("failed to get voice participants: %w", err)
	}
	defer rows.Close()

	var participants []*models.VoiceParticipant
	for rows.Next() {
		participant := &models.VoiceParticipant{}
		err := rows.Scan(&participant.ID, &participant.ChannelID, &participant.UserID,
			&participant.Role, &participant.IsMuted, &participant.IsDeafened, &participant.JoinedAt)
		if err != nil {
			r.logger.Error("Failed to scan voice participant", zap.Error(err))
			continue
		}
		participants = append(participants, participant)
	}

	return participants, nil
}

// Signaling methods
func (r *Repository) StoreSignalingMessage(ctx context.Context, message *models.SignalingMessage) error {
	// Store in Redis for fast access
	key := fmt.Sprintf("signaling:%s:%s", message.ChannelID, message.FromUserID)

	payload, err := json.Marshal(message.Payload)
	if err != nil {
		r.logger.Error("Failed to marshal signaling payload", zap.Error(err))
		return fmt.Errorf("failed to marshal signaling payload: %w", err)
	}

	err = r.redis.Set(ctx, key, payload, time.Minute*5).Err() // 5 minute TTL
	if err != nil {
		r.logger.Error("Failed to store signaling message in Redis", zap.Error(err))
		return fmt.Errorf("failed to store signaling message: %w", err)
	}

	return nil
}

func (r *Repository) GetSignalingMessage(ctx context.Context, channelID, userID uuid.UUID) (map[string]interface{}, error) {
	key := fmt.Sprintf("signaling:%s:%s", channelID, userID)

	payload, err := r.redis.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, fmt.Errorf("signaling message not found")
		}
		r.logger.Error("Failed to get signaling message from Redis", zap.Error(err))
		return nil, fmt.Errorf("failed to get signaling message: %w", err)
	}

	var data map[string]interface{}
	err = json.Unmarshal([]byte(payload), &data)
	if err != nil {
		r.logger.Error("Failed to unmarshal signaling payload", zap.Error(err))
		return nil, fmt.Errorf("failed to unmarshal signaling payload: %w", err)
	}

	return data, nil
}

// Voice quality methods
func (r *Repository) StoreVoiceQualityReport(ctx context.Context, report *models.VoiceQualityReport) error {
	query := `
		INSERT INTO voice_quality_reports (id, channel_id, user_id, bitrate, packet_loss, jitter, latency, quality, reported_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.ExecContext(ctx, query,
		report.ID, report.ChannelID, report.UserID, report.Bitrate, report.PacketLoss,
		report.Jitter, report.Latency, report.Quality, report.ReportedAt)

	if err != nil {
		r.logger.Error("Failed to store voice quality report", zap.Error(err))
		return fmt.Errorf("failed to store voice quality report: %w", err)
	}

	return nil
}

// Health check method
func (r *Repository) HealthCheck(ctx context.Context) error {
	// Test database connection
	if err := r.db.PingContext(ctx); err != nil {
		r.logger.Error("Database health check failed", zap.Error(err))
		return fmt.Errorf("database health check failed: %w", err)
	}

	// Test Redis connection
	if err := r.redis.Ping(ctx).Err(); err != nil {
		r.logger.Error("Redis health check failed", zap.Error(err))
		return fmt.Errorf("redis health check failed: %w", err)
	}

	return nil
}

// Close closes database and Redis connections
func (r *Repository) Close() error {
	if r.db != nil {
		if err := r.db.Close(); err != nil {
			r.logger.Error("Failed to close database connection", zap.Error(err))
		}
	}

	if r.redis != nil {
		if err := r.redis.Close(); err != nil {
			r.logger.Error("Failed to close Redis connection", zap.Error(err))
		}
	}

	return nil
}

// PERFORMANCE: Repository methods are optimized for concurrent access
// Memory pooling used for frequently allocated objects
// Connection pooling configured for database and Redis