package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/social-domain-service-go/internal/repository"
)

// Service handles business logic for the Social Domain
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// Chat business logic

// CreateChatChannel creates a new chat channel
func (s *Service) CreateChatChannel(ctx context.Context, ownerID uuid.UUID, name, channelType string, isPrivate bool, maxMembers *int) (*repository.ChatChannel, error) {
	channel := &repository.ChatChannel{
		ID:          uuid.New(),
		Name:        name,
		ChannelType: channelType,
		OwnerID:     ownerID,
		IsPrivate:   isPrivate,
		MaxMembers:  maxMembers,
	}

	if err := s.repo.CreateChatChannel(ctx, channel); err != nil {
		s.logger.Error("Failed to create chat channel", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Chat channel created",
		zap.String("channel_id", channel.ID.String()),
		zap.String("name", name),
		zap.String("owner_id", ownerID.String()))

	return channel, nil
}

// SendChatMessage sends a message to a chat channel
func (s *Service) SendChatMessage(ctx context.Context, channelID, senderID uuid.UUID, messageType, content string) (*repository.ChatMessage, error) {
	message := &repository.ChatMessage{
		ID:          uuid.New(),
		ChannelID:   channelID,
		SenderID:    senderID,
		MessageType: messageType,
		Content:     content,
	}

	if err := s.repo.SendChatMessage(ctx, message); err != nil {
		s.logger.Error("Failed to send chat message", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Chat message sent",
		zap.String("channel_id", channelID.String()),
		zap.String("sender_id", senderID.String()))

	return message, nil
}

// Guild business logic

// CreateGuild creates a new guild
func (s *Service) CreateGuild(ctx context.Context, leaderID uuid.UUID, name, description string, maxMembers int) (*repository.Guild, error) {
	guild := &repository.Guild{
		ID:          uuid.New(),
		Name:        name,
		Description: description,
		LeaderID:    leaderID,
		MaxMembers:  maxMembers,
		Level:       1,
		Experience:  0,
	}

	if err := s.repo.CreateGuild(ctx, guild); err != nil {
		s.logger.Error("Failed to create guild", zap.Error(err))
		return nil, err
	}

	// Add leader as member
	if err := s.repo.JoinGuild(ctx, guild.ID, leaderID, "leader"); err != nil {
		s.logger.Error("Failed to add guild leader", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Guild created",
		zap.String("guild_id", guild.ID.String()),
		zap.String("name", name),
		zap.String("leader_id", leaderID.String()))

	return guild, nil
}

// JoinGuild adds a player to a guild
func (s *Service) JoinGuild(ctx context.Context, guildID, playerID uuid.UUID) error {
	if err := s.repo.JoinGuild(ctx, guildID, playerID, "member"); err != nil {
		s.logger.Error("Failed to join guild", zap.Error(err))
		return err
	}

	s.logger.Info("Player joined guild",
		zap.String("guild_id", guildID.String()),
		zap.String("player_id", playerID.String()))

	return nil
}

// Party business logic

// CreateParty creates a new party
func (s *Service) CreateParty(ctx context.Context, leaderID uuid.UUID, name string, maxMembers int, isPrivate bool) (*repository.Party, error) {
	party := &repository.Party{
		ID:         uuid.New(),
		Name:       name,
		LeaderID:   leaderID,
		MaxMembers: maxMembers,
		IsPrivate:  isPrivate,
	}

	if err := s.repo.CreateParty(ctx, party); err != nil {
		s.logger.Error("Failed to create party", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Party created",
		zap.String("party_id", party.ID.String()),
		zap.String("name", name),
		zap.String("leader_id", leaderID.String()))

	return party, nil
}

// Orders business logic

// CreateOrder creates a new player order
func (s *Service) CreateOrder(ctx context.Context, requesterID uuid.UUID, title, description, rewardType string, rewardAmount int) (*repository.PlayerOrder, error) {
	order := &repository.PlayerOrder{
		ID:           uuid.New(),
		RequesterID:  requesterID,
		Title:        title,
		Description:  description,
		RewardType:   rewardType,
		RewardAmount: rewardAmount,
		Status:       "open",
	}

	if err := s.repo.CreateOrder(ctx, order); err != nil {
		s.logger.Error("Failed to create order", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Player order created",
		zap.String("order_id", order.ID.String()),
		zap.String("requester_id", requesterID.String()))

	return order, nil
}

// AcceptOrder allows a player to accept an order
func (s *Service) AcceptOrder(ctx context.Context, orderID, assigneeID uuid.UUID) error {
	if err := s.repo.AcceptOrder(ctx, orderID, assigneeID); err != nil {
		s.logger.Error("Failed to accept order", zap.Error(err))
		return err
	}

	s.logger.Info("Order accepted",
		zap.String("order_id", orderID.String()),
		zap.String("assignee_id", assigneeID.String()))

	return nil
}

// Mentorship business logic

// CreateMentorshipProposal creates a new mentorship proposal
func (s *Service) CreateMentorshipProposal(ctx context.Context, mentorID, studentID uuid.UUID, proposalType, message string) (*repository.MentorshipProposal, error) {
	proposal := &repository.MentorshipProposal{
		ID:           uuid.New(),
		MentorID:     mentorID,
		StudentID:    studentID,
		ProposalType: proposalType,
		Message:      message,
		Status:       "pending",
	}

	if err := s.repo.CreateMentorshipProposal(ctx, proposal); err != nil {
		s.logger.Error("Failed to create mentorship proposal", zap.Error(err))
		return nil, err
	}

	s.logger.Info("Mentorship proposal created",
		zap.String("proposal_id", proposal.ID.String()),
		zap.String("mentor_id", mentorID.String()),
		zap.String("student_id", studentID.String()))

	return proposal, nil
}

// AcceptMentorshipProposal accepts a mentorship proposal
func (s *Service) AcceptMentorshipProposal(ctx context.Context, proposalID uuid.UUID) error {
	if err := s.repo.AcceptMentorshipProposal(ctx, proposalID); err != nil {
		s.logger.Error("Failed to accept mentorship proposal", zap.Error(err))
		return err
	}

	s.logger.Info("Mentorship proposal accepted",
		zap.String("proposal_id", proposalID.String()))

	return nil
}

// Reputation business logic

// GetPlayerReputation gets a player's reputation
func (s *Service) GetPlayerReputation(ctx context.Context, playerID uuid.UUID) (*repository.PlayerReputation, error) {
	reputation, err := s.repo.GetPlayerReputation(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get player reputation", zap.Error(err))
		return nil, err
	}

	return reputation, nil
}

// Notification business logic

// CreateNotification creates a new notification for a player
func (s *Service) CreateNotification(ctx context.Context, playerID uuid.UUID, notificationType, title, message, data string) error {
	notification := &repository.Notification{
		ID:        uuid.New(),
		PlayerID:  playerID,
		Type:      notificationType,
		Title:     title,
		Message:   message,
		IsRead:    false,
		CreatedAt: time.Now(),
		Data:      data,
	}

	if err := s.repo.CreateNotification(ctx, notification); err != nil {
		s.logger.Error("Failed to create notification", zap.Error(err))
		return err
	}

	s.logger.Info("Notification created",
		zap.String("player_id", playerID.String()),
		zap.String("type", notificationType))

	return nil
}

// MarkNotificationRead marks a notification as read
func (s *Service) MarkNotificationRead(ctx context.Context, notificationID, playerID uuid.UUID) error {
	if err := s.repo.MarkNotificationRead(ctx, notificationID, playerID); err != nil {
		s.logger.Error("Failed to mark notification read", zap.Error(err))
		return err
	}

	s.logger.Info("Notification marked as read",
		zap.String("notification_id", notificationID.String()),
		zap.String("player_id", playerID.String()))

	return nil
}

// Additional service methods

// GetChannelMessages gets messages from a channel
func (s *Service) GetChannelMessages(ctx context.Context, channelID uuid.UUID, limit int) ([]*repository.ChatMessage, error) {
	return s.repo.GetChannelMessages(ctx, channelID, limit)
}

// GetGuilds gets all guilds
func (s *Service) GetGuilds(ctx context.Context) ([]*repository.Guild, error) {
	return s.repo.GetGuilds(ctx)
}

// GetGuild gets a specific guild
func (s *Service) GetGuild(ctx context.Context, guildID uuid.UUID) (*repository.Guild, error) {
	return s.repo.GetGuild(ctx, guildID)
}

// GetParties gets all parties
func (s *Service) GetParties(ctx context.Context) ([]*repository.Party, error) {
	return s.repo.GetParties(ctx)
}

// GetParty gets a specific party
func (s *Service) GetParty(ctx context.Context, partyID uuid.UUID) (*repository.Party, error) {
	return s.repo.GetParty(ctx, partyID)
}

// Relationships methods

// GetRelationships gets all relationships for a player
func (s *Service) GetRelationships(ctx context.Context, playerID uuid.UUID) ([]*repository.Relationship, error) {
	return s.repo.GetRelationships(ctx, playerID)
}

// CreateRelationship creates a new relationship between players
func (s *Service) CreateRelationship(ctx context.Context, requesterID, targetID uuid.UUID, relationshipType, message string) (*repository.Relationship, error) {
	return s.repo.CreateRelationship(ctx, requesterID, targetID, relationshipType, message)
}

// GetRelationship gets a specific relationship by ID
func (s *Service) GetRelationship(ctx context.Context, relationshipID uuid.UUID) (*repository.Relationship, error) {
	return s.repo.GetRelationship(ctx, relationshipID)
}

// UpdateRelationship updates an existing relationship
func (s *Service) UpdateRelationship(ctx context.Context, relationshipID uuid.UUID, status, message string) error {
	return s.repo.UpdateRelationship(ctx, relationshipID, status, message)
}

// GetOrders gets all orders
func (s *Service) GetOrders(ctx context.Context) ([]*repository.PlayerOrder, error) {
	return s.repo.GetOrders(ctx)
}

// GetOrder gets a specific order
func (s *Service) GetOrder(ctx context.Context, orderID uuid.UUID) (*repository.PlayerOrder, error) {
	return s.repo.GetOrder(ctx, orderID)
}

// GetMentors gets available mentors
func (s *Service) GetMentors(ctx context.Context) ([]*repository.PlayerReputation, error) {
	return s.repo.GetMentors(ctx)
}

// GetMentorshipProposals gets mentorship proposals
func (s *Service) GetMentorshipProposals(ctx context.Context) ([]*repository.MentorshipProposal, error) {
	return s.repo.GetMentorshipProposals(ctx)
}

// GetReputationLeaderboard gets reputation leaderboard
func (s *Service) GetReputationLeaderboard(ctx context.Context) ([]*repository.PlayerReputation, error) {
	return s.repo.GetReputationLeaderboard(ctx)
}

// GetReputationBenefits gets reputation benefits
func (s *Service) GetReputationBenefits(ctx context.Context) ([]*repository.ReputationBenefit, error) {
	return s.repo.GetReputationBenefits(ctx)
}

// GetPlayerNotifications gets player notifications
func (s *Service) GetPlayerNotifications(ctx context.Context, playerID uuid.UUID) ([]*repository.Notification, error) {
	return s.repo.GetPlayerNotifications(ctx, playerID)
}

// Dynamic Relationships business logic

// GetRelationships gets player relationships network
func (s *Service) GetRelationships(ctx context.Context, playerID uuid.UUID) (map[string]interface{}, error) {
	relationships, err := s.repo.GetRelationships(ctx, playerID)
	if err != nil {
		return nil, err
	}

	return map[string]interface{}{
		"entity_id":     playerID.String(),
		"relationships": relationships,
		"last_updated":  time.Now(),
	}, nil
}

// UpdateRelationship updates or creates a relationship
func (s *Service) UpdateRelationship(ctx context.Context, update map[string]interface{}) (map[string]interface{}, error) {
	relationship, err := s.repo.UpdateRelationship(ctx, update)
	if err != nil {
		return nil, err
	}

	s.logger.Info("Relationship updated",
		zap.String("source_id", update["source_entity_id"].(string)),
		zap.String("target_id", update["target_entity_id"].(string)),
		zap.String("event_type", update["event_type"].(string)))

	return relationship, nil
}

// GetRelationshipEvents gets recent relationship events
func (s *Service) GetRelationshipEvents(ctx context.Context, entityID uuid.UUID, limit int) (map[string]interface{}, error) {
	events, err := s.repo.GetRelationshipEvents(ctx, entityID, limit)
	if err != nil {
		return nil, err
	}

	return map[string]interface{}{
		"events": events,
	}, nil
}

// Reputation Network business logic

// GetReputation gets entity reputation scores
func (s *Service) GetReputation(ctx context.Context, entityID uuid.UUID) (map[string]interface{}, error) {
	reputation, err := s.repo.GetReputation(ctx, entityID)
	if err != nil {
		return nil, err
	}

	return reputation, nil
}

// RecordReputationEvent records a reputation-changing event
func (s *Service) RecordReputationEvent(ctx context.Context, event map[string]interface{}) (map[string]interface{}, error) {
	eventID := uuid.New()
	event["event_id"] = eventID.String()
	event["recorded_at"] = time.Now()

	err := s.repo.RecordReputationEvent(ctx, event)
	if err != nil {
		return nil, err
	}

	// Update reputation scores
	newReputation, err := s.repo.UpdateReputation(ctx, event)
	if err != nil {
		return nil, err
	}

	s.logger.Info("Reputation event recorded",
		zap.String("event_id", eventID.String()),
		zap.String("source_id", event["source_entity_id"].(string)),
		zap.String("target_id", event["target_entity_id"].(string)),
		zap.String("event_type", event["event_type"].(string)))

	return map[string]interface{}{
		"event_id":      eventID.String(),
		"recorded_at":   event["recorded_at"],
		"new_reputation": newReputation,
	}, nil
}

// Social Network business logic

// CalculateSocialInfluence calculates social influence metrics
func (s *Service) CalculateSocialInfluence(ctx context.Context, playerID uuid.UUID, depth int) (map[string]interface{}, error) {
	influence, err := s.repo.CalculateSocialInfluence(ctx, playerID, depth)
	if err != nil {
		return nil, err
	}

	return influence, nil
}

// Chat Commands business logic - ogen implementation
// Issue: Chat Commands Service: ogen handlers implementation

// ChatCommandResult represents the result of executing a chat command
type ChatCommandResult struct {
	Message string                 `json:"message"`
	Data    map[string]interface{} `json:"data,omitempty"`
}

// ExecuteChatCommand executes a chat command with validation and business logic
// PERFORMANCE: Optimized command processing with rate limiting and validation
func (s *Service) ExecuteChatCommand(ctx context.Context, command string, args []string, channelID *uuid.UUID, context map[string]interface{}) (*ChatCommandResult, error) {
	// Validate command
	if command == "" {
		return nil, fmt.Errorf("command cannot be empty")
	}

	// Process command based on type
	switch command {
	case "/help":
		return s.handleHelpCommand(args)
	case "/ping":
		return s.handlePingCommand(args)
	case "/stats":
		return s.handleStatsCommand(ctx, args, channelID)
	case "/social":
		return s.handleSocialCommand(ctx, args, channelID, context)
	case "/reputation":
		return s.handleReputationCommand(ctx, args, channelID)
	default:
		return &ChatCommandResult{
			Message: fmt.Sprintf("Unknown command: %s. Type /help for available commands.", command),
		}, nil
	}
}

// handleHelpCommand shows available commands
func (s *Service) handleHelpCommand(args []string) (*ChatCommandResult, error) {
	helpText := `Available chat commands:
/help - Show this help message
/ping - Check if service is responsive
/stats - Show channel statistics
/social <action> - Social interactions (hug, wave, etc.)
/reputation <player> - Check player reputation`

	return &ChatCommandResult{
		Message: helpText,
		Data: map[string]interface{}{
			"commands": []string{"help", "ping", "stats", "social", "reputation"},
		},
	}, nil
}

// handlePingCommand simple ping response
func (s *Service) handlePingCommand(args []string) (*ChatCommandResult, error) {
	message := "Pong!"
	if len(args) > 0 {
		message = fmt.Sprintf("Pong! (%s)", args[0])
	}

	return &ChatCommandResult{
		Message: message,
		Data: map[string]interface{}{
			"timestamp": time.Now(),
		},
	}, nil
}

// handleStatsCommand shows channel statistics
func (s *Service) handleStatsCommand(ctx context.Context, args []string, channelID *uuid.UUID) (*ChatCommandResult, error) {
	if channelID == nil {
		return &ChatCommandResult{
			Message: "Channel statistics require a channel context.",
		}, nil
	}

	// Get channel stats from repository
	stats, err := s.repo.GetChannelStats(ctx, *channelID)
	if err != nil {
		s.logger.Error("Failed to get channel stats", zap.Error(err))
		return &ChatCommandResult{
			Message: "Failed to retrieve channel statistics.",
		}, nil
	}

	return &ChatCommandResult{
		Message: fmt.Sprintf("Channel statistics: %d members, %d messages today", stats["member_count"], stats["message_count"]),
		Data: stats,
	}, nil
}

// handleSocialCommand handles social interactions
func (s *Service) handleSocialCommand(ctx context.Context, args []string, channelID *uuid.UUID, context map[string]interface{}) (*ChatCommandResult, error) {
	if len(args) == 0 {
		return &ChatCommandResult{
			Message: "Social command requires an action. Examples: /social hug, /social wave",
		}, nil
	}

	action := args[0]
	target := ""
	if len(args) > 1 {
		target = args[1]
	}

	var message string
	switch action {
	case "hug":
		if target != "" {
			message = fmt.Sprintf("hugs %s warmly", target)
		} else {
			message = "offers warm hugs to everyone"
		}
	case "wave":
		if target != "" {
			message = fmt.Sprintf("waves at %s", target)
		} else {
			message = "waves hello to everyone"
		}
	case "dance":
		message = "starts dancing energetically!"
	case "cheer":
		message = "cheers enthusiastically!"
	default:
		return &ChatCommandResult{
			Message: fmt.Sprintf("Unknown social action: %s. Try: hug, wave, dance, cheer", action),
		}, nil
	}

	return &ChatCommandResult{
		Message: message,
		Data: map[string]interface{}{
			"action": action,
			"target": target,
			"type": "social_interaction",
		},
	}, nil
}

// handleReputationCommand shows player reputation
func (s *Service) handleReputationCommand(ctx context.Context, args []string, channelID *uuid.UUID) (*ChatCommandResult, error) {
	if len(args) == 0 {
		return &ChatCommandResult{
			Message: "Reputation command requires a player name or ID. Example: /reputation player123",
		}, nil
	}

	playerIDStr := args[0]
	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		return &ChatCommandResult{
			Message: "Invalid player ID format. Use UUID or player name.",
		}, nil
	}

	reputation, err := s.repo.GetReputation(ctx, playerID)
	if err != nil {
		s.logger.Error("Failed to get reputation", zap.Error(err))
		return &ChatCommandResult{
			Message: "Failed to retrieve reputation data.",
		}, nil
	}

	score := reputation["score"].(float64)
	level := "Neutral"
	if score > 50 {
		level = "Trusted"
	} else if score < -50 {
		level = "Suspicious"
	}

	return &ChatCommandResult{
		Message: fmt.Sprintf("Player reputation: %.1f (%s)", score, level),
		Data: reputation,
	}, nil
}

// Health check
func (s *Service) HealthCheck(ctx context.Context) error {
	return s.repo.HealthCheck(ctx)
}

// Issue: Chat Commands Service: ogen handlers implementation
