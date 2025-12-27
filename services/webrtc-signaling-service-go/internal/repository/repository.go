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

// Repository handles data access operations
type Repository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB, redis *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// NewPostgresConnection creates a new PostgreSQL connection
func NewPostgresConnection(url string) (*sql.DB, error) {
	db, err := sql.Open("postgres", url)
	if err != nil {
		return nil, fmt.Errorf("failed to open database connection: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(5)
	db.SetConnMaxLifetime(5 * time.Minute)

	// Test connection
	if err := db.Ping(); err != nil {
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

// CreateVoiceChannel creates a new voice channel
func (r *Repository) CreateVoiceChannel(ctx context.Context, req models.CreateVoiceChannelRequest) (*models.VoiceChannel, error) {
	channel := &models.VoiceChannel{
		ID:               uuid.New().String(),
		Name:             req.Name,
		Type:             req.Type,
		MaxParticipants:  req.MaxParticipants,
		Description:      req.Description,
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
		Status:           "active",
		IsActive:         true,
		QualitySettings: models.VoiceQualitySettings{
			Bitrate:           64000,
			SampleRate:        48000,
			Channels:          1,
			EchoCancellation:  true,
			NoiseSuppression:  true,
		},
	}

	if channel.MaxParticipants == 0 {
		channel.MaxParticipants = 10
	}

	query := `
		INSERT INTO webrtc.voice_channels (
			id, name, type, max_participants, description,
			created_at, updated_at, status, is_active, quality_settings
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	qualityJSON, err := json.Marshal(channel.QualitySettings)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal quality settings: %w", err)
	}

	_, err = r.db.ExecContext(ctx, query,
		channel.ID, channel.Name, channel.Type, channel.MaxParticipants, channel.Description,
		channel.CreatedAt, channel.UpdatedAt, channel.Status, channel.IsActive, qualityJSON)
	if err != nil {
		return nil, fmt.Errorf("failed to create voice channel: %w", err)
	}

	r.logger.Info("Created voice channel",
		zap.String("channel_id", channel.ID),
		zap.String("name", channel.Name))

	return channel, nil
}

// GetVoiceChannel retrieves a voice channel by ID
func (r *Repository) GetVoiceChannel(ctx context.Context, channelID string) (*models.VoiceChannel, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("voice_channel:%s", channelID)
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var channel models.VoiceChannel
		if err := json.Unmarshal([]byte(cached), &channel); err == nil {
			return &channel, nil
		}
	}

	query := `
		SELECT id, name, type, max_participants, current_participants, description,
			   created_at, updated_at, status, is_active, quality_settings
		FROM webrtc.voice_channels
		WHERE id = $1 AND is_active = true
	`

	var channel models.VoiceChannel
	var qualityJSON []byte

	err = r.db.QueryRowContext(ctx, query, channelID).Scan(
		&channel.ID, &channel.Name, &channel.Type, &channel.MaxParticipants,
		&channel.CurrentParticipants, &channel.Description, &channel.CreatedAt,
		&channel.UpdatedAt, &channel.Status, &channel.IsActive, &qualityJSON)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("voice channel not found: %s", channelID)
		}
		return nil, fmt.Errorf("failed to get voice channel: %w", err)
	}

	if err := json.Unmarshal(qualityJSON, &channel.QualitySettings); err != nil {
		r.logger.Warn("Failed to unmarshal quality settings, using defaults",
			zap.String("channel_id", channelID), zap.Error(err))
		channel.QualitySettings = models.VoiceQualitySettings{
			Bitrate: 64000, SampleRate: 48000, Channels: 1,
			EchoCancellation: true, NoiseSuppression: true,
		}
	}

	// Cache the result
	if data, err := json.Marshal(channel); err == nil {
		r.redis.Set(ctx, cacheKey, data, 5*time.Minute)
	}

	return &channel, nil
}

// UpdateVoiceChannel updates a voice channel
func (r *Repository) UpdateVoiceChannel(ctx context.Context, channelID string, req models.UpdateVoiceChannelRequest) (*models.VoiceChannel, error) {
	channel, err := r.GetVoiceChannel(ctx, channelID)
	if err != nil {
		return nil, err
	}

	if req.Name != nil {
		channel.Name = *req.Name
	}
	if req.MaxParticipants != nil {
		channel.MaxParticipants = *req.MaxParticipants
	}
	if req.Description != nil {
		channel.Description = *req.Description
	}
	if req.Status != nil {
		channel.Status = *req.Status
	}

	channel.UpdatedAt = time.Now()

	query := `
		UPDATE webrtc.voice_channels
		SET name = $2, max_participants = $3, description = $4,
		    status = $5, updated_at = $6
		WHERE id = $1
	`

	_, err = r.db.ExecContext(ctx, query,
		channelID, channel.Name, channel.MaxParticipants,
		channel.Description, channel.Status, channel.UpdatedAt)
	if err != nil {
		return nil, fmt.Errorf("failed to update voice channel: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("voice_channel:%s", channelID)
	r.redis.Del(ctx, cacheKey)

	r.logger.Info("Updated voice channel",
		zap.String("channel_id", channelID),
		zap.String("name", channel.Name))

	return channel, nil
}

// DeleteVoiceChannel marks a voice channel as inactive
func (r *Repository) DeleteVoiceChannel(ctx context.Context, channelID string) error {
	query := `
		UPDATE webrtc.voice_channels
		SET is_active = false, status = 'closed', updated_at = $2
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, channelID, time.Now())
	if err != nil {
		return fmt.Errorf("failed to delete voice channel: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("voice_channel:%s", channelID)
	r.redis.Del(ctx, cacheKey)

	r.logger.Info("Deleted voice channel", zap.String("channel_id", channelID))
	return nil
}

// ListVoiceChannels retrieves a paginated list of voice channels
func (r *Repository) ListVoiceChannels(ctx context.Context, limit, offset int, channelType, status string) ([]models.VoiceChannel, int, error) {
	whereClause := "WHERE is_active = true"
	args := []interface{}{}
	argCount := 0

	if channelType != "" {
		argCount++
		whereClause += fmt.Sprintf(" AND type = $%d", argCount)
		args = append(args, channelType)
	}

	if status != "" {
		argCount++
		whereClause += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, status)
	}

	// Get total count
	countQuery := fmt.Sprintf("SELECT COUNT(*) FROM webrtc.voice_channels %s", whereClause)
	var total int
	err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	// Get channels
	query := fmt.Sprintf(`
		SELECT id, name, type, max_participants, current_participants, description,
			   created_at, updated_at, status, is_active, quality_settings
		FROM webrtc.voice_channels %s
		ORDER BY created_at DESC
		LIMIT $%d OFFSET $%d
	`, whereClause, argCount+1, argCount+2)

	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list voice channels: %w", err)
	}
	defer rows.Close()

	var channels []models.VoiceChannel
	for rows.Next() {
		var channel models.VoiceChannel
		var qualityJSON []byte

		err := rows.Scan(
			&channel.ID, &channel.Name, &channel.Type, &channel.MaxParticipants,
			&channel.CurrentParticipants, &channel.Description, &channel.CreatedAt,
			&channel.UpdatedAt, &channel.Status, &channel.IsActive, &qualityJSON)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan voice channel: %w", err)
		}

		if err := json.Unmarshal(qualityJSON, &channel.QualitySettings); err != nil {
			r.logger.Warn("Failed to unmarshal quality settings",
				zap.String("channel_id", channel.ID), zap.Error(err))
		}

		channels = append(channels, channel)
	}

	return channels, total, nil
}

// JoinVoiceChannel records a participant joining
func (r *Repository) JoinVoiceChannel(ctx context.Context, channelID, userID string) error {
	// Update participant count
	query := `
		UPDATE webrtc.voice_channels
		SET current_participants = current_participants + 1, updated_at = $2
		WHERE id = $1 AND current_participants < max_participants
	`

	result, err := r.db.ExecContext(ctx, query, channelID, time.Now())
	if err != nil {
		return fmt.Errorf("failed to join voice channel: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("voice channel is full or not found: %s", channelID)
	}

	// Record participant
	participantQuery := `
		INSERT INTO webrtc.voice_channel_participants (channel_id, user_id, joined_at)
		VALUES ($1, $2, $3)
		ON CONFLICT (channel_id, user_id) DO UPDATE SET joined_at = $3
	`

	_, err = r.db.ExecContext(ctx, participantQuery, channelID, userID, time.Now())
	if err != nil {
		return fmt.Errorf("failed to record participant: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("voice_channel:%s", channelID)
	r.redis.Del(ctx, cacheKey)

	r.logger.Info("User joined voice channel",
		zap.String("channel_id", channelID),
		zap.String("user_id", userID))

	return nil
}

// LeaveVoiceChannel records a participant leaving
func (r *Repository) LeaveVoiceChannel(ctx context.Context, channelID, userID string) error {
	// Update participant count
	query := `
		UPDATE webrtc.voice_channels
		SET current_participants = GREATEST(current_participants - 1, 0), updated_at = $2
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query, channelID, time.Now())
	if err != nil {
		return fmt.Errorf("failed to leave voice channel: %w", err)
	}

	// Remove participant record
	participantQuery := `
		DELETE FROM webrtc.voice_channel_participants
		WHERE channel_id = $1 AND user_id = $2
	`

	_, err = r.db.ExecContext(ctx, participantQuery, channelID, userID)
	if err != nil {
		return fmt.Errorf("failed to remove participant: %w", err)
	}

	// Invalidate cache
	cacheKey := fmt.Sprintf("voice_channel:%s", channelID)
	r.redis.Del(ctx, cacheKey)

	r.logger.Info("User left voice channel",
		zap.String("channel_id", channelID),
		zap.String("user_id", userID))

	return nil
}

// GetChannelParticipants retrieves current participants
func (r *Repository) GetChannelParticipants(ctx context.Context, channelID string) ([]models.VoiceParticipant, error) {
	query := `
		SELECT p.user_id, COALESCE(u.display_name, ''), p.joined_at, p.is_muted
		FROM webrtc.voice_channel_participants p
		LEFT JOIN users u ON p.user_id = u.id
		WHERE p.channel_id = $1
		ORDER BY p.joined_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, channelID)
	if err != nil {
		return nil, fmt.Errorf("failed to get participants: %w", err)
	}
	defer rows.Close()

	var participants []models.VoiceParticipant
	for rows.Next() {
		var p models.VoiceParticipant
		err := rows.Scan(&p.UserID, &p.DisplayName, &p.JoinedAt, &p.IsMuted)
		if err != nil {
			return nil, fmt.Errorf("failed to scan participant: %w", err)
		}
		p.ConnectionQuality = "good" // Default, can be updated with quality reports
		participants = append(participants, p)
	}

	return participants, nil
}

// RecordSignalingMessage logs signaling activity for analytics
func (r *Repository) RecordSignalingMessage(ctx context.Context, msg models.SignalingMessage) error {
	query := `
		INSERT INTO webrtc.signaling_messages (
			channel_id, sender_id, target_id, message_type, timestamp
		) VALUES ($1, $2, $3, $4, $5)
	`

	// Extract channel_id from context or derive from participants
	channelID := "unknown" // This should be passed in context

	_, err := r.db.ExecContext(ctx, query,
		channelID, msg.SenderID, msg.TargetID, msg.Type, msg.Timestamp)
	if err != nil {
		return fmt.Errorf("failed to record signaling message: %w", err)
	}

	return nil
}

// RecordVoiceQualityReport stores quality metrics
func (r *Repository) RecordVoiceQualityReport(ctx context.Context, channelID string, report models.VoiceQualityReport) error {
	query := `
		INSERT INTO webrtc.voice_quality_reports (
			channel_id, user_id, latency_ms, packet_loss_percent, jitter_ms,
			bitrate_bps, volume_level, timestamp
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		channelID, report.UserID, report.Metrics.LatencyMs,
		report.Metrics.PacketLossPercent, report.Metrics.JitterMs,
		report.Metrics.BitrateBps, report.Metrics.VolumeLevel, report.Timestamp)
	if err != nil {
		return fmt.Errorf("failed to record quality report: %w", err)
	}

	return nil
}
