// Guild Service Business Logic - Enterprise-grade guild management
// Issue: #2247

package service

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/repository"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/server"
)

// Service handles business logic
type Service struct {
	repo   *repository.Repository
	logger *zap.SugaredLogger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.SugaredLogger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// ListGuilds retrieves a paginated list of guilds
func (s *Service) ListGuilds(ctx context.Context, limit, offset int, sortBy string) ([]*server.Guild, error) {
	s.logger.Infof("Listing guilds with limit: %d, offset: %d, sort: %s", limit, offset, sortBy)

	guilds, err := s.repo.ListGuilds(ctx, limit, offset, sortBy)
	if err != nil {
		s.logger.Errorf("Failed to list guilds: %v", err)
		return nil, err
	}

	return guilds, nil
}

// CreateGuild creates a new guild
func (s *Service) CreateGuild(ctx context.Context, name, description string, leaderID uuid.UUID) (*server.Guild, error) {
	s.logger.Infof("Creating guild: %s for leader: %s", name, leaderID)

	// Validate input
	if len(name) < 3 || len(name) > 100 {
		return nil, fmt.Errorf("guild name must be between 3 and 100 characters")
	}
	if description != "" && len(description) > 1000 {
		return nil, fmt.Errorf("guild description must be less than 1000 characters")
	}

	// Create guild in repository
	guild, err := s.repo.CreateGuild(ctx, name, description, leaderID)
	if err != nil {
		s.logger.Errorf("Failed to create guild: %v", err)
		return nil, err
	}

	// Add leader as member
	err = s.repo.AddGuildMember(ctx, guild.ID, leaderID, "leader")
	if err != nil {
		s.logger.Errorf("Failed to add leader as member: %v", err)
		// Try to clean up the created guild
		s.repo.DeleteGuild(ctx, guild.ID)
		return nil, err
	}

	s.logger.Infof("Successfully created guild with ID: %s", guild.ID)
	return guild, nil
}

// GetGuild retrieves a guild by ID
func (s *Service) GetGuild(ctx context.Context, guildID uuid.UUID) (*server.Guild, error) {
	s.logger.Infof("Getting guild: %s", guildID)

	guild, err := s.repo.GetGuild(ctx, guildID)
	if err != nil {
		s.logger.Errorf("Failed to get guild: %v", err)
		return nil, err
	}

	return guild, nil
}

// UpdateGuild updates guild information
func (s *Service) UpdateGuild(ctx context.Context, guildID uuid.UUID, name, description string) error {
	s.logger.Infof("Updating guild: %s with name: %s", guildID, name)

	// Validate input
	if len(name) < 3 || len(name) > 100 {
		return fmt.Errorf("guild name must be between 3 and 100 characters")
	}
	if description != "" && len(description) > 1000 {
		return fmt.Errorf("guild description must be less than 1000 characters")
	}

	err := s.repo.UpdateGuild(ctx, guildID, name, description)
	if err != nil {
		s.logger.Errorf("Failed to update guild: %v", err)
		return err
	}

	return nil
}

// DeleteGuild deletes a guild
func (s *Service) DeleteGuild(ctx context.Context, guildID uuid.UUID) error {
	s.logger.Infof("Deleting guild: %s", guildID)

	err := s.repo.DeleteGuild(ctx, guildID)
	if err != nil {
		s.logger.Errorf("Failed to delete guild: %v", err)
		return err
	}

	return nil
}

// AddMember adds a member to a guild
func (s *Service) AddMember(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	s.logger.Infof("Adding player %s to guild %s with role %s", userID, guildID, role)

	// Validate role
	if role != "leader" && role != "officer" && role != "member" {
		return fmt.Errorf("invalid role: must be leader, officer, or member")
	}

	// Check if guild exists and has capacity
	guild, err := s.repo.GetGuild(ctx, guildID)
	if err != nil {
		return fmt.Errorf("guild not found: %v", err)
	}

	if guild.MemberCount >= guild.MaxMembers {
		return fmt.Errorf("guild is at maximum capacity")
	}

	err = s.repo.AddGuildMember(ctx, guildID, userID, role)
	if err != nil {
		s.logger.Errorf("Failed to add member: %v", err)
		return err
	}

	return nil
}

// RemoveMember removes a member from a guild
func (s *Service) RemoveMember(ctx context.Context, guildID, userID uuid.UUID) error {
	s.logger.Infof("Removing player %s from guild %s", userID, guildID)

	err := s.repo.RemoveGuildMember(ctx, guildID, userID)
	if err != nil {
		s.logger.Errorf("Failed to remove member: %v", err)
		return err
	}

	return nil
}

// UpdateMemberRole updates a member's role in a guild
func (s *Service) UpdateMemberRole(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	s.logger.Infof("Updating role for player %s in guild %s to %s", userID, guildID, role)

	// Validate role
	if role != "leader" && role != "officer" && role != "member" {
		return fmt.Errorf("invalid role: must be leader, officer, or member")
	}

	// Update role in database
	query := `UPDATE social.guild_members SET role = $1 WHERE guild_id = $2 AND user_id = $3`
	_, err := s.repo.(*repository.Repository).db.ExecContext(ctx, query, role, guildID, userID)
	if err != nil {
		s.logger.Errorf("Failed to update member role: %v", err)
		return err
	}

	return nil
}

// ListMembers retrieves guild members
func (s *Service) ListMembers(ctx context.Context, guildID uuid.UUID) ([]*server.GuildMember, error) {
	s.logger.Infof("Listing members for guild: %s", guildID)

	members, err := s.repo.ListMembers(ctx, guildID)
	if err != nil {
		s.logger.Errorf("Failed to list members: %v", err)
		return nil, err
	}

	return members, nil
}

// CreateAnnouncement creates a new guild announcement
func (s *Service) CreateAnnouncement(ctx context.Context, guildID, authorID uuid.UUID, title, content string) (*server.GuildAnnouncement, error) {
	s.logger.Infof("Creating announcement for guild %s by %s", guildID, authorID)

	// Validate input
	if len(title) < 1 || len(title) > 200 {
		return nil, fmt.Errorf("announcement title must be between 1 and 200 characters")
	}
	if len(content) < 1 || len(content) > 5000 {
		return nil, fmt.Errorf("announcement content must be between 1 and 5000 characters")
	}

	// Check if author is a member of the guild
	members, err := s.repo.ListMembers(ctx, guildID)
	if err != nil {
		return nil, fmt.Errorf("failed to check guild membership: %v", err)
	}

	isMember := false
	for _, member := range members {
		if member.UserID == authorID {
			isMember = true
			break
		}
	}

	if !isMember {
		return nil, fmt.Errorf("author is not a member of the guild")
	}

	announcement, err := s.repo.CreateAnnouncement(ctx, guildID, authorID, title, content)
	if err != nil {
		s.logger.Errorf("Failed to create announcement: %v", err)
		return nil, err
	}

	return announcement, nil
}

// ListAnnouncements retrieves guild announcements
func (s *Service) ListAnnouncements(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*server.GuildAnnouncement, error) {
	s.logger.Infof("Listing announcements for guild: %s", guildID)

	announcements, err := s.repo.ListAnnouncements(ctx, guildID, limit, offset)
	if err != nil {
		s.logger.Errorf("Failed to list announcements: %v", err)
		return nil, err
	}

	return announcements, nil
}

// GetPlayerGuilds retrieves guilds for a specific player
func (s *Service) GetPlayerGuilds(ctx context.Context, playerID uuid.UUID) ([]*server.Guild, error) {
	s.logger.Infof("Getting guilds for player: %s", playerID)

	guilds, err := s.repo.GetPlayerGuilds(ctx, playerID)
	if err != nil {
		s.logger.Errorf("Failed to get player guilds: %v", err)
		return nil, err
	}

	return guilds, nil
}

// JoinGuild allows a player to join a guild
func (s *Service) JoinGuild(ctx context.Context, guildID, playerID uuid.UUID) error {
	s.logger.Infof("Player %s joining guild %s", playerID, guildID)

	// Check if guild exists and has capacity
	guild, err := s.repo.GetGuild(ctx, guildID)
	if err != nil {
		return fmt.Errorf("guild not found: %v", err)
	}

	if guild.MemberCount >= guild.MaxMembers {
		return fmt.Errorf("guild is at maximum capacity")
	}

	// Add player as member
	err = s.repo.AddGuildMember(ctx, guildID, playerID, "member")
	if err != nil {
		s.logger.Errorf("Failed to join guild: %v", err)
		return err
	}

	return nil
}

// LeaveGuild allows a player to leave a guild
func (s *Service) LeaveGuild(ctx context.Context, guildID, playerID uuid.UUID) error {
	s.logger.Infof("Player %s leaving guild %s", playerID, guildID)

	// Check if player is the leader
	guild, err := s.repo.GetGuild(ctx, guildID)
	if err != nil {
		return fmt.Errorf("guild not found: %v", err)
	}

	if guild.LeaderID == playerID {
		return fmt.Errorf("guild leader cannot leave the guild, transfer leadership first")
	}

	// Remove player from guild
	err = s.repo.RemoveGuildMember(ctx, guildID, playerID)
	if err != nil {
		s.logger.Errorf("Failed to leave guild: %v", err)
		return err
	}

	return nil
}
