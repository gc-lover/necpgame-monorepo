package service

import (
	"context"
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

// Health check
func (s *Service) HealthCheck(ctx context.Context) error {
	return s.repo.HealthCheck(ctx)
}
