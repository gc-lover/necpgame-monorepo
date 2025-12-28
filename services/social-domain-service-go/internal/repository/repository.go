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

// AcceptOrder allows a player to accept an order
func (r *Repository) AcceptOrder(ctx context.Context, orderID, assigneeID uuid.UUID) error {
	query := `
		UPDATE player_orders
		SET assignee_id = $2, status = 'in_progress', updated_at = $3
		WHERE id = $1 AND status = 'open'
	`

	_, err := r.db.ExecContext(ctx, query, orderID, assigneeID, time.Now())
	return err
}

// AcceptMentorshipProposal accepts a mentorship proposal
func (r *Repository) AcceptMentorshipProposal(ctx context.Context, proposalID uuid.UUID) error {
	query := `
		UPDATE mentorship_proposals
		SET status = 'accepted', updated_at = $2
		WHERE id = $1 AND status = 'pending'
	`

	_, err := r.db.ExecContext(ctx, query, proposalID, time.Now())
	return err
}

// CreateNotification creates a new notification
func (r *Repository) CreateNotification(ctx context.Context, notification *Notification) error {
	query := `
		INSERT INTO notifications (id, player_id, type, title, message, is_read, created_at, data)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		notification.ID, notification.PlayerID, notification.Type,
		notification.Title, notification.Message, notification.IsRead,
		notification.CreatedAt, notification.Data)

	return err
}

// MarkNotificationRead marks a notification as read
func (r *Repository) MarkNotificationRead(ctx context.Context, notificationID, playerID uuid.UUID) error {
	query := `
		UPDATE notifications
		SET is_read = true
		WHERE id = $1 AND player_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, notificationID, playerID)
	return err
}

// GetChannelMessages gets messages from a channel
func (r *Repository) GetChannelMessages(ctx context.Context, channelID uuid.UUID, limit int) ([]*ChatMessage, error) {
	query := `
		SELECT * FROM chat_messages
		WHERE channel_id = $1
		ORDER BY created_at DESC
		LIMIT $2
	`

	var messages []*ChatMessage
	err := r.db.SelectContext(ctx, &messages, query, channelID, limit)
	return messages, err
}

// GetGuilds gets all guilds
func (r *Repository) GetGuilds(ctx context.Context) ([]*Guild, error) {
	query := `SELECT * FROM guilds ORDER BY created_at DESC`
	var guilds []*Guild
	err := r.db.SelectContext(ctx, &guilds, query)
	return guilds, err
}

// GetGuild gets a specific guild
func (r *Repository) GetGuild(ctx context.Context, guildID uuid.UUID) (*Guild, error) {
	query := `SELECT * FROM guilds WHERE id = $1`
	var guild Guild
	err := r.db.GetContext(ctx, &guild, query, guildID)
	if err != nil {
		return nil, err
	}
	return &guild, nil
}

// GetParties gets all parties
func (r *Repository) GetParties(ctx context.Context) ([]*Party, error) {
	query := `SELECT * FROM parties WHERE is_private = false ORDER BY created_at DESC`
	var parties []*Party
	err := r.db.SelectContext(ctx, &parties, query)
	return parties, err
}

// GetParty gets a specific party
func (r *Repository) GetParty(ctx context.Context, partyID uuid.UUID) (*Party, error) {
	query := `SELECT * FROM parties WHERE id = $1`
	var party Party
	err := r.db.GetContext(ctx, &party, query, partyID)
	if err != nil {
		return nil, err
	}
	return &party, nil
}

// Relationships methods

// GetRelationships gets all relationships for a player
func (r *Repository) GetRelationships(ctx context.Context, playerID uuid.UUID) ([]*Relationship, error) {
	query := `
		SELECT * FROM relationships
		WHERE (requester_id = $1 OR target_id = $1)
		AND status = 'accepted'
		ORDER BY created_at DESC
	`
	var relationships []*Relationship
	err := r.db.SelectContext(ctx, &relationships, query, playerID)
	return relationships, err
}

// CreateRelationship creates a new relationship between players
func (r *Repository) CreateRelationship(ctx context.Context, requesterID, targetID uuid.UUID, relationshipType, message string) (*Relationship, error) {
	relationship := &Relationship{
		ID:               uuid.New(),
		RequesterID:      requesterID,
		TargetID:         targetID,
		RelationshipType: relationshipType,
		Message:          message,
		Status:           "pending",
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
	}

	query := `
		INSERT INTO relationships (id, requester_id, target_id, relationship_type, message, status, created_at, updated_at)
		VALUES (:id, :requester_id, :target_id, :relationship_type, :message, :status, :created_at, :updated_at)
	`

	_, err := r.db.NamedExecContext(ctx, query, relationship)
	if err != nil {
		return nil, err
	}

	return relationship, nil
}

// GetRelationship gets a specific relationship by ID
func (r *Repository) GetRelationship(ctx context.Context, relationshipID uuid.UUID) (*Relationship, error) {
	query := `SELECT * FROM relationships WHERE id = $1`
	var relationship Relationship
	err := r.db.GetContext(ctx, &relationship, query, relationshipID)
	if err != nil {
		return nil, err
	}
	return &relationship, nil
}

// UpdateRelationship updates an existing relationship
func (r *Repository) UpdateRelationship(ctx context.Context, relationshipID uuid.UUID, status, message string) error {
	query := `
		UPDATE relationships
		SET status = $2, message = $3, updated_at = $4
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query, relationshipID, status, message, time.Now())
	return err
}

// GetOrders gets all orders
func (r *Repository) GetOrders(ctx context.Context) ([]*PlayerOrder, error) {
	query := `SELECT * FROM player_orders WHERE status = 'open' ORDER BY created_at DESC`
	var orders []*PlayerOrder
	err := r.db.SelectContext(ctx, &orders, query)
	return orders, err
}

// GetOrder gets a specific order
func (r *Repository) GetOrder(ctx context.Context, orderID uuid.UUID) (*PlayerOrder, error) {
	query := `SELECT * FROM player_orders WHERE id = $1`
	var order PlayerOrder
	err := r.db.GetContext(ctx, &order, query, orderID)
	if err != nil {
		return nil, err
	}
	return &order, nil
}

// GetMentors gets available mentors
func (r *Repository) GetMentors(ctx context.Context) ([]*PlayerReputation, error) {
	query := `
		SELECT pr.* FROM player_reputation pr
		WHERE pr.level >= 10
		ORDER BY pr.score DESC
		LIMIT 50
	`
	var mentors []*PlayerReputation
	err := r.db.SelectContext(ctx, &mentors, query)
	return mentors, err
}

// GetMentorshipProposals gets mentorship proposals
func (r *Repository) GetMentorshipProposals(ctx context.Context) ([]*MentorshipProposal, error) {
	query := `SELECT * FROM mentorship_proposals WHERE status = 'pending' ORDER BY created_at DESC`
	var proposals []*MentorshipProposal
	err := r.db.SelectContext(ctx, &proposals, query)
	return proposals, err
}

// GetReputationLeaderboard gets reputation leaderboard
func (r *Repository) GetReputationLeaderboard(ctx context.Context) ([]*PlayerReputation, error) {
	query := `SELECT * FROM player_reputation ORDER BY score DESC LIMIT 100`
	var reputations []*PlayerReputation
	err := r.db.SelectContext(ctx, &reputations, query)
	return reputations, err
}

// GetReputationBenefits gets reputation benefits
func (r *Repository) GetReputationBenefits(ctx context.Context) ([]*ReputationBenefit, error) {
	query := `SELECT * FROM reputation_benefits ORDER BY min_level ASC`
	var benefits []*ReputationBenefit
	err := r.db.SelectContext(ctx, &benefits, query)
	return benefits, err
}

// GetPlayerNotifications gets player notifications
func (r *Repository) GetPlayerNotifications(ctx context.Context, playerID uuid.UUID) ([]*Notification, error) {
	query := `SELECT * FROM notifications WHERE player_id = $1 ORDER BY created_at DESC LIMIT 50`
	var notifications []*Notification
	err := r.db.SelectContext(ctx, &notifications, query, playerID)
	return notifications, err
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
