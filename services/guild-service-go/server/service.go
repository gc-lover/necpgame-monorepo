// Issue: #1943
package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

// OPTIMIZATION: Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo       *Repository
	guildCache *GuildCache // Issue: #1943 - 3-tier cache

	// Memory pooling for hot path structs (zero allocations target!)
	guildResponsePool      sync.Pool
	guildListResponsePool  sync.Pool
	memberResponsePool     sync.Pool
	invitationResponsePool sync.Pool
}

// OPTIMIZATION: Struct field alignment (large → small) Issue #300
type GuildDefinition struct {
	GuildID     string    // 16 bytes
	Name        string    // 16 bytes
	Description string    // 16 bytes
	LeaderID    uuid.UUID // 16 bytes
	Level       int       // 8 bytes
	MemberCount int       // 8 bytes
	Reputation  int       // 8 bytes
	CreatedAt   time.Time // 24 bytes (interface)
	UpdatedAt   time.Time // 24 bytes (interface)
	Region      string    // 16 bytes
	IsActive    bool      // 1 byte
}

// NewService creates new service with memory pooling
func NewService(repo *Repository, redisClient *redis.Client) *Service {
	guildCache := NewGuildCache(redisClient, repo)
	s := &Service{
		repo:       repo,
		guildCache: guildCache,
	}

	// Initialize memory pools (zero allocations target!)
	s.guildResponsePool = sync.Pool{
		New: func() interface{} {
			return &GuildResponse{}
		},
	}
	s.guildListResponsePool = sync.Pool{
		New: func() interface{} {
			return &GuildListResponse{}
		},
	}
	s.memberResponsePool = sync.Pool{
		New: func() interface{} {
			return &GuildMemberResponse{}
		},
	}
	s.invitationResponsePool = sync.Pool{
		New: func() interface{} {
			return &InvitationResponse{}
		},
	}

	return s
}

// CreateGuild creates a new guild (invalidates cache)
func (s *Service) CreateGuild(ctx context.Context, req *CreateGuildRequest) (*GuildResponse, error) {
	guildID := uuid.New()
	now := time.Now()

	guildDef := &GuildDefinition{
		GuildID:     guildID.String(),
		Name:        req.Name,
		Description: req.Description,
		LeaderID:    req.LeaderID,
		Level:       1,
		MemberCount: 1,
		Reputation:  0,
		CreatedAt:   now,
		UpdatedAt:   now,
		Region:      req.Region,
		IsActive:    true,
	}

	// Create guild in database
	if err := s.repo.CreateGuild(ctx, guildDef); err != nil {
		return nil, fmt.Errorf("failed to create guild: %w", err)
	}

	// Add leader as first member
	if err := s.repo.AddGuildMember(ctx, guildID.String(), req.LeaderID, "leader"); err != nil {
		return nil, fmt.Errorf("failed to add guild leader: %w", err)
	}

	// Get response from pool
	response := s.guildResponsePool.Get().(*GuildResponse)
	// Note: Not returning to pool - struct is returned to caller

	response.ID = guildID.String()
	response.Name = guildDef.Name
	response.Description = guildDef.Description
	response.LeaderID = guildDef.LeaderID.String()
	response.Level = guildDef.Level
	response.MemberCount = guildDef.MemberCount
	response.Reputation = guildDef.Reputation
	response.Region = guildDef.Region
	response.CreatedAt = guildDef.CreatedAt

	// Invalidate relevant caches
	s.guildCache.InvalidateGuildList(ctx)
	s.guildCache.InvalidatePlayerGuilds(ctx, req.LeaderID)

	return response, nil
}

// GetGuild returns guild by ID (with 3-tier cache)
func (s *Service) GetGuild(ctx context.Context, guildID uuid.UUID) (*GuildResponse, error) {
	// Try cache first (L1 → L2 → L3)
	guild, err := s.guildCache.GetGuild(ctx, guildID.String())
	if err == nil && guild != nil {
		return guild, nil
	}

	// Fallback to database
	guildDef, err := s.repo.GetGuild(ctx, guildID.String())
	if err != nil {
		return nil, fmt.Errorf("failed to get guild: %w", err)
	}

	// Get response from pool
	response := s.guildResponsePool.Get().(*GuildResponse)

	response.ID = guildDef.GuildID
	response.Name = guildDef.Name
	response.Description = guildDef.Description
	response.LeaderID = guildDef.LeaderID.String()
	response.Level = guildDef.Level
	response.MemberCount = guildDef.MemberCount
	response.Reputation = guildDef.Reputation
	response.Region = guildDef.Region
	response.CreatedAt = guildDef.CreatedAt

	// Cache the result
	s.guildCache.storeInRedisGuild(ctx, guildID.String(), response)
	s.guildCache.storeInMemoryGuild(guildID.String(), response)

	return response, nil
}

// GetGuilds returns paginated list of guilds (with cache)
func (s *Service) GetGuilds(ctx context.Context, params *GetGuildsParams) (*GuildListResponse, error) {
	// Try cache first
	guilds, err := s.guildCache.GetGuildList(ctx, params)
	if err == nil && len(guilds) > 0 {
		total := len(guilds)
		if params.Limit > 0 && len(guilds) > params.Limit {
			guilds = guilds[:params.Limit]
		}

		response := s.guildListResponsePool.Get().(*GuildListResponse)
		response.Guilds = guilds
		response.Total = total
		response.Page = params.Page
		response.Limit = params.Limit

		return response, nil
	}

	// Fallback to database
	guilds, total, err := s.repo.GetGuilds(ctx, params)
	if err != nil {
		return nil, fmt.Errorf("failed to get guilds: %w", err)
	}

	response := s.guildListResponsePool.Get().(*GuildListResponse)
	response.Guilds = guilds
	response.Total = total
	response.Page = params.Page
	response.Limit = params.Limit

	// Cache the result
	s.guildCache.storeInRedisGuildList(ctx, params, guilds, total)
	s.guildCache.storeInMemoryGuildList(fmt.Sprintf("guilds:list:%v", params), guilds)

	return response, nil
}

// InviteMember invites a player to join the guild
func (s *Service) InviteMember(ctx context.Context, guildID string, playerID uuid.UUID, inviterID uuid.UUID, message string) (*InvitationResponse, error) {
	invitationID := uuid.New()
	now := time.Now()
	expiresAt := now.Add(7 * 24 * time.Hour) // 7 days

	invitation := &Invitation{
		ID:           invitationID,
		GuildID:      guildID,
		PlayerID:     playerID,
		InvitedBy:    inviterID,
		Message:      message,
		Status:       "pending",
		CreatedAt:    now,
		ExpiresAt:    expiresAt,
	}

	if err := s.repo.CreateInvitation(ctx, invitation); err != nil {
		return nil, fmt.Errorf("failed to create invitation: %w", err)
	}

	response := s.invitationResponsePool.Get().(*InvitationResponse)
	response.ID = invitationID.String()
	response.GuildID = guildID
	response.PlayerID = playerID.String()
	response.InvitedBy = inviterID.String()
	response.Status = "pending"
	response.CreatedAt = now
	response.ExpiresAt = expiresAt

	return response, nil
}

// GetGuildMembers returns guild members with roles
func (s *Service) GetGuildMembers(ctx context.Context, guildID string, params *GetMembersParams) ([]*GuildMemberResponse, error) {
	members, err := s.repo.GetGuildMembers(ctx, guildID, params)
	if err != nil {
		return nil, fmt.Errorf("failed to get guild members: %w", err)
	}

	responses := make([]*GuildMemberResponse, len(members))
	for i, member := range members {
		response := s.memberResponsePool.Get().(*GuildMemberResponse)
		response.PlayerID = member.PlayerID
		response.Username = member.Username
		response.Role = member.Role
		response.JoinedAt = member.JoinedAt
		response.LastActive = member.LastActive
		response.ContributionScore = member.ContributionScore
		response.Permissions = member.Permissions
		responses[i] = response
	}

	return responses, nil
}
