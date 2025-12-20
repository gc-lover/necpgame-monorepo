// Package server Issue: #1856
package server

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-war-service-go/pkg/api"
	"github.com/google/uuid"
)

// Common errors
var (
	ErrGuildWarNotFound = errors.New("guild war not found")
	_                   = errors.New("war is not active")
	ErrAlreadyInWar     = errors.New("user already in war")
	ErrWarFull          = errors.New("war is full")
)

// Service implements business logic for guild war service
// SOLID: Single Responsibility - business logic only
// Issue: #1856 - Memory pooling for hot path (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	warResponsePool         sync.Pool
	warListResponsePool     sync.Pool
	declareWarResponsePool  sync.Pool
	leaderboardResponsePool sync.Pool
}

// NewService creates new service with dependency injection and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.warResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetGuildWarOK{}
		},
	}
	s.warListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ListGuildWarsOK{}
		},
	}
	s.declareWarResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.DeclareWarOK{}
		},
	}
	s.leaderboardResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetWarLeaderboardOK{}
		},
	}

	return s
}

// GetGuildWars retrieves guild wars with optional filtering - BUSINESS LOGIC
func (s *Service) GetGuildWars(ctx context.Context, params api.ListGuildWarsParams) (*api.ListGuildWarsOK, error) {
	// Convert API parameters to internal types
	var status *string
	if params.Status.IsSet() {
		statusStr := string(params.Status.Value)
		status = &statusStr
	}

	var limit *int
	if params.Limit.IsSet() {
		limitVal := int(params.Limit.Value)
		limit = &limitVal
	}

	// Call repository
	wars, err := s.repo.GetGuildWars(ctx, status, limit)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := s.warListResponsePool.Get().(*api.ListGuildWarsOK)
	defer s.warListResponsePool.Put(response)

	// TODO: Convert internal GuildWar models to API War models
	// This will be implemented when the API models are defined

	return response, nil
}

// GetGuildWar retrieves a single guild war by ID - BUSINESS LOGIC
func (s *Service) GetGuildWar(ctx context.Context, params api.GetGuildWarParams) (*api.GetGuildWarOK, error) {
	// Call repository
	war, err := s.repo.GetGuildWarByID(ctx, params.WarID)
	if err != nil {
		if err == ErrGuildWarNotFound {
			return nil, errors.New("guild war not found")
		}
		return nil, err
	}

	// Convert to API response
	response := s.warResponsePool.Get().(*api.GetGuildWarOK)
	defer s.warResponsePool.Put(response)

	// TODO: Convert internal GuildWar model to API War model
	// This will be implemented when the API models are defined

	_ = war // Remove when implemented
	return response, nil
}

// DeclareWar creates a new guild war declaration - BUSINESS LOGIC
func (s *Service) DeclareWar(ctx context.Context, params api.DeclareWarParams, req *api.WarDeclarationRequest) (*api.DeclareWarOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user is guild leader
	// For now, allow any user

	// Validate duration (1-24 hours)
	if req.Duration < 1 || req.Duration > 24 {
		return nil, errors.New("war duration must be 1-24 hours")
	}

	duration := time.Duration(req.Duration) * time.Hour

	// Declare war
	war, err := s.repo.DeclareWar(ctx, params.AttackerGuildID, params.DefenderGuildID)
	if err != nil {
		return nil, err
	}

	// Return response
	response := s.declareWarResponsePool.Get().(*api.DeclareWarOK)
	defer s.declareWarResponsePool.Put(response)

	response.Message.Set("War declared successfully")
	return response, nil
}

// JoinWar allows a user to join an active war - BUSINESS LOGIC
func (s *Service) JoinWar(ctx context.Context, params api.JoinWarParams) (*api.JoinWarOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Get user's guild ID from context or database
	guildID := uuid.New() // Placeholder - should get from user context

	// Join war
	err := s.repo.JoinWar(ctx, params.WarID, userID, guildID)
	if err != nil {
		return nil, err
	}

	return &api.JoinWarOK{Message: api.OptString{Value: "Joined war successfully", Set: true}}, nil
}

// GetWarLeaderboard returns war leaderboard - BUSINESS LOGIC
func (s *Service) GetWarLeaderboard(ctx context.Context, params api.GetWarLeaderboardParams) (*api.GetWarLeaderboardOK, error) {
	// Convert API parameters to internal types
	var limit *int
	if params.Limit.IsSet() {
		limitVal := int(params.Limit.Value)
		limit = &limitVal
	}

	// Call repository
	participants, err := s.repo.GetWarLeaderboard(ctx, params.WarID, limit)
	if err != nil {
		return nil, err
	}

	// Convert to API response
	response := s.leaderboardResponsePool.Get().(*api.GetWarLeaderboardOK)
	defer s.leaderboardResponsePool.Put(response)

	// TODO: Convert internal WarParticipant models to API LeaderboardEntry models
	// This will be implemented when the API models are defined

	_ = participants // Remove when implemented
	return response, nil
}

// UpdateWarScore updates participant score - BUSINESS LOGIC
func (s *Service) UpdateWarScore(ctx context.Context, params api.UpdateWarScoreParams, req *api.ScoreUpdateRequest) (*api.UpdateWarScoreOK, error) {
	// Get user ID from context
	userID, ok := ctx.Value("user_id").(uuid.UUID)
	if !ok {
		return nil, errors.New("user not authenticated")
	}

	// TODO: Check if user has admin permissions or is war participant
	// For now, allow any user

	// Update score
	err := s.repo.UpdateWarScore(ctx, params.WarID, params.UserID, int(req.ScoreDelta), int(req.KillsDelta), int(req.DeathsDelta))
	if err != nil {
		return nil, err
	}

	return &api.UpdateWarScoreOK{Message: api.OptString{Value: "Score updated successfully", Set: true}}, nil
}
