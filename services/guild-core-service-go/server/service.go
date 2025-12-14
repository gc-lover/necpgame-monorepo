// Issue: #1856
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-core-service-go/pkg/api"
	"github.com/google/uuid"
)

// Common errors
var (
	ErrGuildNotFound    = errors.New("guild not found")
	ErrNotGuildMember   = errors.New("user is not guild member")
	ErrNotGuildLeader   = errors.New("user is not guild leader")
	ErrGuildNameTaken   = errors.New("guild name already taken")
	ErrGuildTagTaken    = errors.New("guild tag already taken")
	ErrInvalidRole      = errors.New("invalid role")
)

// Service implements business logic for guild core service
// SOLID: Single Responsibility - business logic only
// Issue: #1856 - Memory pooling for hot path (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	guildResponsePool         sync.Pool
	guildListResponsePool     sync.Pool
	guildMemberResponsePool   sync.Pool
	createGuildResponsePool   sync.Pool
}

// NewService creates new service with dependency injection and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.guildResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetGuildOK{}
		},
	}
	s.guildListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ListGuildsOK{}
		},
	}
	s.guildMemberResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GuildWebSocketSwitchingProtocols{}
		},
	}
	s.createGuildResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.CreateGuildCreated{}
		},
	}

	return s
}

// GetGuilds retrieves guilds with optional filtering - BUSINESS LOGIC
func (s *Service) GetGuilds(ctx context.Context, params api.ListGuildsParams) (*api.ListGuildsOK, error) {
	// Convert API parameters to internal types
	var search *string
	if params.Search.IsSet() {
		search = &params.Search.Value
	}

	var limit *int
	if params.Limit.IsSet() {
		limitVal := int(params.Limit.Value)
		limit = &limitVal
	}

	// Call repository
	guilds, err := s.repo.GetGuilds(ctx, search, limit)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := s.guildListResponsePool.Get().(*api.ListGuildsOK)
	defer s.guildListResponsePool.Put(response)

	// TODO: Convert internal Guild models to API Guild models
	// This will be implemented when the API models are defined

	return response, nil
}

// GetGuild retrieves a single guild by ID - BUSINESS LOGIC
func (s *Service) GetGuild(ctx context.Context, params api.GetGuildParams) (*api.GetGuildOK, error) {
	// Call repository
	guild, err := s.repo.GetGuildByID(ctx, params.GuildID)
	if err != nil {
		if err == ErrGuildNotFound {
			return nil, errors.New("guild not found")
		}
		return nil, err
	}

	// Convert to API response
	response := s.guildResponsePool.Get().(*api.GetGuildOK)
	defer s.guildResponsePool.Put(response)

	// TODO: Convert internal Guild model to API Guild model
	// This will be implemented when the API models are defined

	_ = guild // Remove when implemented
	return response, nil
}

// CreateGuild creates a new guild - BUSINESS LOGIC
func (s *Service) CreateGuild(ctx context.Context, req *api.CreateGuildRequest) (*api.CreateGuildCreated, error) {
	// Get user ID from context (set by authentication middleware)
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// Validate guild name and tag
	if len(req.Name) < 3 || len(req.Name) > 50 {
		return nil, errors.New("guild name must be 3-50 characters")
	}
	if len(req.Tag) < 2 || len(req.Tag) > 5 {
		return nil, errors.New("guild tag must be 2-5 characters")
	}

	// TODO: Check if name/tag is already taken
	// This validation will be added when we have the database schema

	// Create guild
	guild, err := s.repo.CreateGuild(ctx, req, userID)
	if err != nil {
		return nil, err
	}

	// Add creator as guild leader
	err = s.repo.AddGuildMember(ctx, guild.ID, userID, "leader")
	if err != nil {
		return nil, err
	}

	// Return response
	response := s.createGuildResponsePool.Get().(*api.CreateGuildCreated)
	defer s.createGuildResponsePool.Put(response)

	return response, nil
}

// UpdateGuild updates guild information - BUSINESS LOGIC
func (s *Service) UpdateGuild(ctx context.Context, params api.UpdateGuildParams, req *api.CreateGuildRequest) (*api.UpdateGuildOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// Check if user is guild leader
	isLeader, err := s.repo.IsUserGuildLeader(ctx, params.GuildID, userID)
	if err != nil {
		return nil, err
	}
	if !isLeader {
		return nil, ErrNotGuildLeader
	}

	// Update guild
	err = s.repo.UpdateGuild(ctx, params.GuildID, &req.Name, &req.Tag, &req.Description)
	if err != nil {
		return nil, err
	}

	return &api.UpdateGuildOK{}, nil
}

// DeleteGuild deletes a guild - BUSINESS LOGIC
func (s *Service) DeleteGuild(ctx context.Context, params api.DeleteGuildParams) error {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return errors.New("user not authenticated")
	}

	// Check if user is guild leader
	isLeader, err := s.repo.IsUserGuildLeader(ctx, params.GuildID, userID)
	if err != nil {
		return err
	}
	if !isLeader {
		return ErrNotGuildLeader
	}

	// Delete guild
	return s.repo.DeleteGuild(ctx, params.GuildID)
}

// GuildWebSocket handles WebSocket connections for real-time guild updates
func (s *Service) GuildWebSocket(ctx context.Context, params api.GetGuildParams) (*api.GuildWebSocketSwitchingProtocols, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// Check if user is guild member
	isMember, err := s.repo.IsUserInGuild(ctx, params.GuildID, userID)
	if err != nil {
		return nil, err
	}
	if !isMember {
		return nil, ErrNotGuildMember
	}

	// TODO: Implement WebSocket upgrade logic
	// This will establish real-time connection for guild events

	response := s.guildMemberResponsePool.Get().(*api.GuildWebSocketSwitchingProtocols)
	defer s.guildMemberResponsePool.Put(response)

	return response, nil
}

// validateGuildName validates guild name format
func validateGuildName(name string) bool {
	if len(name) < 3 || len(name) > 50 {
		return false
	}
	// TODO: Add more validation rules (no special characters, etc.)
	return true
}

// validateGuildTag validates guild tag format
func validateGuildTag(tag string) bool {
	if len(tag) < 2 || len(tag) > 5 {
		return false
	}
	// TODO: Add more validation rules (alphanumeric only, etc.)
	return true
}

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)