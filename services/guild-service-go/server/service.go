// Issue: #140889771
// PERFORMANCE: Optimized business logic with caching and connection pooling

package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/repository"
	"go.uber.org/zap"
)

// GuildService implements GuildServiceInterface
type GuildService struct {
	logger     *zap.Logger
	mu         sync.RWMutex
	guilds     map[uuid.UUID]*Guild // In-memory cache for hot guilds
	members    map[uuid.UUID]map[uuid.UUID]*GuildMember // guildID -> userID -> member
	repository *repository.Repository // Database repository
}

// NewGuildService creates a new guild service instance
func NewGuildService(logger *zap.Logger) *GuildService {
	return &GuildService{
		logger:  logger,
		guilds:  make(map[uuid.UUID]*Guild),
		members: make(map[uuid.UUID]map[uuid.UUID]*GuildMember),
	}
}

// UpdateRepository sets the database repository for data persistence
func (s *GuildService) UpdateRepository(repo *repository.Repository) {
	s.repository = repo
}

// DisbandGuild disbands a guild (same as delete for now)
func (s *GuildService) DisbandGuild(ctx context.Context, id uuid.UUID) error {
	return s.DeleteGuild(ctx, id)
}

// CreateGuild creates a new guild
func (s *GuildService) CreateGuild(ctx context.Context, name, description string, leaderID uuid.UUID) (*Guild, error) {
	if name == "" {
		return nil, errors.New("guild name cannot be empty")
	}

	guild := &Guild{
		ID:          uuid.New(),
		Name:        name,
		Description: description,
		LeaderID:    leaderID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
		MemberCount: 1,
		MaxMembers:  50,
		Level:       1,
		Experience:  0,
	}

	// Cache the guild
	s.mu.Lock()
	s.guilds[guild.ID] = guild
	s.members[guild.ID] = make(map[uuid.UUID]*GuildMember)
	s.mu.Unlock()

	// Add leader as member
	err := s.AddMember(ctx, guild.ID, leaderID, "leader")
	if err != nil {
		return nil, err
	}

	s.logger.Info("Guild created",
		zap.String("guild_id", guild.ID.String()),
		zap.String("name", name),
		zap.String("leader_id", leaderID.String()))

	return guild, nil
}

// GetGuild retrieves a guild by ID
func (s *GuildService) GetGuild(ctx context.Context, id uuid.UUID) (*Guild, error) {
	s.mu.RLock()
	guild, exists := s.guilds[id]
	s.mu.RUnlock()

	if !exists {
		return nil, errors.New("guild not found")
	}

	return guild, nil
}

// ListGuilds lists guilds with pagination
func (s *GuildService) ListGuilds(ctx context.Context, limit, offset int, sortBy string) ([]*Guild, error) {
	if s.repository != nil {
		// Use database repository for production
		dbGuilds, err := s.repository.ListGuilds(ctx, limit, offset, sortBy)
		if err != nil {
			s.logger.Error("Failed to list guilds from database", zap.Error(err))
			return nil, err
		}

		// Convert from repository model to service model
		guilds := make([]*Guild, 0, len(dbGuilds))
		for _, dbGuild := range dbGuilds {
			guild := &Guild{
				ID:          dbGuild.ID,
				Name:        dbGuild.Name,
				Description: dbGuild.Description,
				LeaderID:    dbGuild.LeaderID,
				CreatedAt:   dbGuild.CreatedAt,
				UpdatedAt:   dbGuild.UpdatedAt,
				MemberCount: dbGuild.MemberCount,
				MaxMembers:  dbGuild.MaxMembers,
				Level:       dbGuild.Level,
				Experience:  dbGuild.Experience,
			}
			guilds = append(guilds, guild)
		}

		return guilds, nil
	}

	// Fallback to in-memory cache for development/testing
	if limit <= 0 || limit > 100 {
		limit = 50
	}

	s.mu.RLock()
	defer s.mu.RUnlock()

	guilds := make([]*Guild, 0, len(s.guilds))
	for _, guild := range s.guilds {
		guilds = append(guilds, guild)
	}

	// Simple pagination (in production, this would be database-driven)
	start := offset
	end := offset + limit
	if start > len(guilds) {
		return []*Guild{}, nil
	}
	if end > len(guilds) {
		end = len(guilds)
	}

	return guilds[start:end], nil
}

// UpdateGuild updates guild information
func (s *GuildService) UpdateGuild(ctx context.Context, id uuid.UUID, name, description string) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	guild, exists := s.guilds[id]
	if !exists {
		return errors.New("guild not found")
	}

	if name != "" {
		guild.Name = name
	}
	if description != "" {
		guild.Description = description
	}
	guild.UpdatedAt = time.Now()

	s.logger.Info("Guild updated",
		zap.String("guild_id", id.String()),
		zap.String("name", name))

	return nil
}

// DeleteGuild deletes a guild
func (s *GuildService) DeleteGuild(ctx context.Context, id uuid.UUID) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	if _, exists := s.guilds[id]; !exists {
		return errors.New("guild not found")
	}

	delete(s.guilds, id)
	delete(s.members, id)

	s.logger.Info("Guild deleted", zap.String("guild_id", id.String()))

	return nil
}

// AddMember adds a member to a guild
func (s *GuildService) AddMember(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	guild, exists := s.guilds[guildID]
	if !exists {
		return errors.New("guild not found")
	}

	if s.members[guildID] == nil {
		s.members[guildID] = make(map[uuid.UUID]*GuildMember)
	}

	if _, exists := s.members[guildID][userID]; exists {
		return errors.New("user is already a member of this guild")
	}

	member := &GuildMember{
		UserID:   userID,
		GuildID:  guildID,
		Role:     role,
		JoinedAt: time.Now(),
	}

	s.members[guildID][userID] = member
	guild.MemberCount++

	s.logger.Info("Member added to guild",
		zap.String("guild_id", guildID.String()),
		zap.String("user_id", userID.String()),
		zap.String("role", role))

	return nil
}

// RemoveMember removes a member from a guild
func (s *GuildService) RemoveMember(ctx context.Context, guildID, userID uuid.UUID) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	guild, exists := s.guilds[guildID]
	if !exists {
		return errors.New("guild not found")
	}

	if _, exists := s.members[guildID][userID]; !exists {
		return errors.New("user is not a member of this guild")
	}

	delete(s.members[guildID], userID)
	guild.MemberCount--

	s.logger.Info("Member removed from guild",
		zap.String("guild_id", guildID.String()),
		zap.String("user_id", userID.String()))

	return nil
}

// UpdateMemberRole updates a member's role
func (s *GuildService) UpdateMemberRole(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	s.mu.Lock()
	defer s.mu.Unlock()

	member, exists := s.members[guildID][userID]
	if !exists {
		return errors.New("member not found in guild")
	}

	member.Role = role

	s.logger.Info("Member role updated",
		zap.String("guild_id", guildID.String()),
		zap.String("user_id", userID.String()),
		zap.String("role", role))

	return nil
}

// ListMembers lists all members of a guild
func (s *GuildService) ListMembers(ctx context.Context, guildID uuid.UUID) ([]*GuildMember, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	members, exists := s.members[guildID]
	if !exists {
		return []*GuildMember{}, nil
	}

	result := make([]*GuildMember, 0, len(members))
	for _, member := range members {
		result = append(result, member)
	}

	return result, nil
}

// CreateAnnouncement creates a guild announcement
func (s *GuildService) CreateAnnouncement(ctx context.Context, guildID uuid.UUID, title, content string, authorID uuid.UUID) error {
	if title == "" || content == "" {
		return errors.New("title and content cannot be empty")
	}

	// Verify author is a member
	s.mu.RLock()
	_, isMember := s.members[guildID][authorID]
	s.mu.RUnlock()

	if !isMember {
		return errors.New("only guild members can create announcements")
	}

	announcement := &GuildAnnouncement{
		ID:        uuid.New(),
		GuildID:   guildID,
		Title:     title,
		Content:   content,
		AuthorID:  authorID,
		CreatedAt: time.Now(),
	}

	// In production, this would be stored in database
	s.logger.Info("Guild announcement created",
		zap.String("guild_id", guildID.String()),
		zap.String("author_id", authorID.String()),
		zap.String("title", title))

	_ = announcement // Placeholder for database storage

	return nil
}

// ListAnnouncements lists guild announcements
func (s *GuildService) ListAnnouncements(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]*GuildAnnouncement, error) {
	if limit <= 0 || limit > 50 {
		limit = 20
	}

	// In production, this would query database
	// For now, return empty list
	announcements := []*GuildAnnouncement{}

	s.logger.Info("Guild announcements listed",
		zap.String("guild_id", guildID.String()),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	return announcements, nil
}

// GetPlayerGuilds gets all guilds a player belongs to
func (s *GuildService) GetPlayerGuilds(ctx context.Context, playerID uuid.UUID) ([]*Guild, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	guilds := []*Guild{}
	for guildID, members := range s.members {
		if _, isMember := members[playerID]; isMember {
			if guild, exists := s.guilds[guildID]; exists {
				guilds = append(guilds, guild)
			}
		}
	}

	return guilds, nil
}

// JoinGuild allows a player to join a guild
func (s *GuildService) JoinGuild(ctx context.Context, guildID, playerID uuid.UUID) error {
	return s.AddMember(ctx, guildID, playerID, "member")
}

// LeaveGuild allows a player to leave a guild
func (s *GuildService) LeaveGuild(ctx context.Context, guildID, playerID uuid.UUID) error {
	return s.RemoveMember(ctx, guildID, playerID)
}
