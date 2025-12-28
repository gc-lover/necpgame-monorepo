package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"services/social-domain-service-go/internal/config"
)

// Repository handles data access for the Social Domain
type Repository struct {
	db     *sqlx.DB
	redis  *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *sqlx.DB, redis *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// NewDBConnection creates a new database connection with MMOFPS optimizations
func NewDBConnection(url string, config *config.Config) (*sqlx.DB, error) {
	db, err := sqlx.Connect("postgres", url)
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	// Configure connection pool for MMOFPS performance
	db.SetMaxOpenConns(config.DBMaxOpenConns)
	db.SetMaxIdleConns(config.DBMaxIdleConns)
	db.SetConnMaxLifetime(config.DBConnMaxLifetime)

	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	return db, nil
}

// NewRedisClient creates a new Redis client with MMOFPS optimizations
func NewRedisClient(url string, config *config.Config) (*redis.Client, error) {
	opts, err := redis.ParseURL(url)
	if err != nil {
		return nil, fmt.Errorf("failed to parse Redis URL: %w", err)
	}

	// Configure Redis pool size for MMOFPS real-time social features
	opts.PoolSize = config.RedisPoolSize

	client := redis.NewClient(opts)

	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// Chat operations

// CreateChatChannel creates a new chat channel
func (r *Repository) CreateChatChannel(ctx context.Context, channel *ChatChannel) error {
	query := `
		INSERT INTO chat_channels (id, name, channel_type, owner_id, is_private, max_members, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`
	channel.CreatedAt = time.Now()
	channel.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		channel.ID, channel.Name, channel.ChannelType, channel.OwnerID,
		channel.IsPrivate, channel.MaxMembers, channel.CreatedAt, channel.UpdatedAt)

	if err != nil {
		return fmt.Errorf("failed to create chat channel: %w", err)
	}

	return r.cacheChatChannel(ctx, channel)
}

// SendChatMessage sends a message to a chat channel
func (r *Repository) SendChatMessage(ctx context.Context, message *ChatMessage) error {
	query := `
		INSERT INTO chat_messages (id, channel_id, sender_id, message_type, content, created_at)
		VALUES ($1, $2, $3, $4, $5, $6)
	`
	message.CreatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		message.ID, message.ChannelID, message.SenderID, message.MessageType,
		message.Content, message.CreatedAt)

	if err != nil {
		return fmt.Errorf("failed to send chat message: %w", err)
	}

	// Cache recent messages
	return r.cacheChatMessage(ctx, message)
}

// Guild operations

// CreateGuild creates a new guild
func (r *Repository) CreateGuild(ctx context.Context, guild *Guild) error {
	query := `
		INSERT INTO guilds (id, name, description, leader_id, max_members, level, experience, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	guild.CreatedAt = time.Now()
	guild.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		guild.ID, guild.Name, guild.Description, guild.LeaderID,
		guild.MaxMembers, guild.Level, guild.Experience, guild.CreatedAt, guild.UpdatedAt)

	return err
}

// JoinGuild adds a player to a guild
func (r *Repository) JoinGuild(ctx context.Context, guildID, playerID uuid.UUID, role string) error {
	query := `
		INSERT INTO guild_members (guild_id, player_id, role, joined_at)
		VALUES ($1, $2, $3, $4)
	`

	_, err := r.db.ExecContext(ctx, query, guildID, playerID, role, time.Now())
	return err
}

// Party operations

// CreateParty creates a new party
func (r *Repository) CreateParty(ctx context.Context, party *Party) error {
	query := `
		INSERT INTO parties (id, name, leader_id, max_members, is_private, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`
	party.CreatedAt = time.Now()
	party.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		party.ID, party.Name, party.LeaderID, party.MaxMembers,
		party.IsPrivate, party.CreatedAt, party.UpdatedAt)

	return err
}

// Orders operations

// CreateOrder creates a new player order/commission
func (r *Repository) CreateOrder(ctx context.Context, order *PlayerOrder) error {
	query := `
		INSERT INTO player_orders (id, requester_id, title, description, reward_type, reward_amount, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	order.CreatedAt = time.Now()
	order.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		order.ID, order.RequesterID, order.Title, order.Description,
		order.RewardType, order.RewardAmount, order.Status, order.CreatedAt, order.UpdatedAt)

	return err
}

// Mentorship operations

// CreateMentorshipProposal creates a new mentorship proposal
func (r *Repository) CreateMentorshipProposal(ctx context.Context, proposal *MentorshipProposal) error {
	query := `
		INSERT INTO mentorship_proposals (id, mentor_id, student_id, proposal_type, message, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`
	proposal.CreatedAt = time.Now()
	proposal.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		proposal.ID, proposal.MentorID, proposal.StudentID, proposal.ProposalType,
		proposal.Message, proposal.Status, proposal.CreatedAt, proposal.UpdatedAt)

	return err
}

// Reputation operations

// GetPlayerReputation gets a player's reputation
func (r *Repository) GetPlayerReputation(ctx context.Context, playerID uuid.UUID) (*PlayerReputation, error) {
	query := `SELECT * FROM player_reputation WHERE player_id = $1`
	var reputation PlayerReputation
	err := r.db.GetContext(ctx, &reputation, query, playerID)
	if err != nil {
		if err == sql.ErrNoRows {
			// Create default reputation
			reputation = PlayerReputation{
				PlayerID: playerID,
				Score:    0,
				Level:    1,
				Title:    "Новичок",
			}
			return &reputation, nil
		}
		return nil, fmt.Errorf("failed to get player reputation: %w", err)
	}
	return &reputation, nil
}

// Cache helper methods
func (r *Repository) cacheChatChannel(ctx context.Context, channel *ChatChannel) error {
	cacheKey := fmt.Sprintf("chat_channel:%s", channel.ID)
	data, err := json.Marshal(channel)
	if err != nil {
		return err
	}
	return r.redis.Set(ctx, cacheKey, data, time.Hour).Err()
}

func (r *Repository) cacheChatMessage(ctx context.Context, message *ChatMessage) error {
	cacheKey := fmt.Sprintf("chat_message:%s", message.ID)
	data, err := json.Marshal(message)
	if err != nil {
		return err
	}
	// Cache messages for 24 hours
	return r.redis.Set(ctx, cacheKey, data, 24*time.Hour).Err()
}

// Health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	return r.db.PingContext(ctx)
}

// Close closes database and Redis connections
func (r *Repository) Close() error {
	if err := r.redis.Close(); err != nil {
		return err
	}
	return r.db.Close()
}
