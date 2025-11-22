package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type ChatRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewChatRepository(db *pgxpool.Pool) *ChatRepository {
	return &ChatRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *ChatRepository) CreateMessage(ctx context.Context, message *models.ChatMessage) (*models.ChatMessage, error) {
	err := r.db.QueryRow(ctx,
		`INSERT INTO social.chat_messages 
		 (id, channel_id, channel_type, sender_id, sender_name, content, formatted, created_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		 RETURNING id, channel_id, channel_type, sender_id, sender_name, content, formatted, created_at`,
		message.ID, message.ChannelID, message.ChannelType, message.SenderID,
		message.SenderName, message.Content, message.Formatted, message.CreatedAt,
	).Scan(&message.ID, &message.ChannelID, &message.ChannelType, &message.SenderID,
		&message.SenderName, &message.Content, &message.Formatted, &message.CreatedAt)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create chat message")
		return nil, err
	}

	return message, nil
}

func (r *ChatRepository) GetMessagesByChannel(ctx context.Context, channelID uuid.UUID, limit, offset int) ([]models.ChatMessage, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, channel_id, channel_type, sender_id, sender_name, content, formatted, created_at
		 FROM social.chat_messages
		 WHERE channel_id = $1
		 ORDER BY created_at DESC
		 LIMIT $2 OFFSET $3`,
		channelID, limit, offset,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get chat messages")
		return nil, err
	}
	defer rows.Close()

	var messages []models.ChatMessage
	for rows.Next() {
		var message models.ChatMessage
		err := rows.Scan(&message.ID, &message.ChannelID, &message.ChannelType,
			&message.SenderID, &message.SenderName, &message.Content,
			&message.Formatted, &message.CreatedAt)

		if err != nil {
			r.logger.WithError(err).Error("Failed to scan chat message")
			continue
		}

		messages = append(messages, message)
	}

	return messages, nil
}

func (r *ChatRepository) GetChannels(ctx context.Context, channelType *models.ChannelType) ([]models.ChatChannel, error) {
	var rows pgx.Rows
	var err error

	if channelType != nil {
		rows, err = r.db.Query(ctx,
			`SELECT id, type, owner_id, name, description, cooldown_seconds, max_length, is_active, created_at
			 FROM social.chat_channels
			 WHERE type = $1 AND is_active = true
			 ORDER BY created_at ASC`,
			*channelType,
		)
	} else {
		rows, err = r.db.Query(ctx,
			`SELECT id, type, owner_id, name, description, cooldown_seconds, max_length, is_active, created_at
			 FROM social.chat_channels
			 WHERE is_active = true
			 ORDER BY created_at ASC`,
		)
	}

	if err != nil {
		r.logger.WithError(err).Error("Failed to get chat channels")
		return nil, err
	}
	defer rows.Close()

	var channels []models.ChatChannel
	for rows.Next() {
		var channel models.ChatChannel
		var ownerID *uuid.UUID
		err := rows.Scan(&channel.ID, &channel.Type, &ownerID,
			&channel.Name, &channel.Description, &channel.CooldownSeconds,
			&channel.MaxLength, &channel.IsActive, &channel.CreatedAt)

		if err != nil {
			r.logger.WithError(err).Error("Failed to scan chat channel")
			continue
		}

		channel.OwnerID = ownerID
		channels = append(channels, channel)
	}

	return channels, nil
}

func (r *ChatRepository) GetChannelByID(ctx context.Context, channelID uuid.UUID) (*models.ChatChannel, error) {
	var channel models.ChatChannel
	var ownerID *uuid.UUID

	err := r.db.QueryRow(ctx,
		`SELECT id, type, owner_id, name, description, cooldown_seconds, max_length, is_active, created_at
		 FROM social.chat_channels
		 WHERE id = $1 AND is_active = true`,
		channelID,
	).Scan(&channel.ID, &channel.Type, &ownerID,
		&channel.Name, &channel.Description, &channel.CooldownSeconds,
		&channel.MaxLength, &channel.IsActive, &channel.CreatedAt)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get chat channel")
		return nil, err
	}

	channel.OwnerID = ownerID
	return &channel, nil
}

func (r *ChatRepository) CountMessagesByChannel(ctx context.Context, channelID uuid.UUID) (int, error) {
	var count int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM social.chat_messages WHERE channel_id = $1`,
		channelID,
	).Scan(&count)

	if err != nil {
		r.logger.WithError(err).Error("Failed to count chat messages")
		return 0, err
	}

	return count, nil
}

