// Package server Issue: #1442
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
	"github.com/google/uuid"
)

var (
	ErrNotFound     = errors.New("not found")
	_               = errors.New("already exists")
	ErrInvalidInput = errors.New("invalid input")
)

type Service struct {
	repo *Repository
}

func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) CreateFaction(ctx context.Context, req api.CreateFactionRequest) (*api.Faction, error) {
	// Validate input
	if req.Name == "" {
		return nil, ErrInvalidInput
	}

	// Create faction in repository
	faction, err := s.repo.CreateFaction(ctx, req)
	if err != nil {
		return nil, err
	}

	return faction, nil
}

func (s *Service) GetFaction(ctx context.Context, factionId string) (*api.FactionDetails, error) {
	faction, err := s.repo.GetFactionByID(ctx, factionId)
	if err != nil {
		return nil, err
	}

	// Get additional details
	memberCount, err := s.repo.GetMemberCount(ctx, factionId)
	if err != nil {
		return nil, err
	}

	clanCount, err := s.repo.GetClanCount(ctx, factionId)
	if err != nil {
		return nil, err
	}

	details := &api.FactionDetails{
		ID:           faction.ID,
		Name:         faction.Name,
		Type:         faction.Type,
		Ideology:     faction.Ideology,
		Description:  faction.Description,
		LeaderClanID: faction.LeaderClanID,
		Status:       faction.Status,
		CreatedAt:    faction.CreatedAt,
		UpdatedAt:    faction.UpdatedAt,
		MemberCount:  api.NewOptInt(memberCount),
		ClanCount:    api.NewOptInt(clanCount),
	}

	return details, nil
}

func (s *Service) UpdateFaction(ctx context.Context, factionId string, req api.UpdateFactionRequest) (*api.Faction, error) {
	// Check if faction exists
	_, err := s.repo.GetFactionByID(ctx, factionId)
	if err != nil {
		return nil, err
	}

	// Update faction
	faction, err := s.repo.UpdateFaction(ctx, factionId, req)
	if err != nil {
		return nil, err
	}

	return faction, nil
}

func (s *Service) DeleteFaction(ctx context.Context, factionId string) error {
	// Check if faction exists
	_, err := s.repo.GetFactionByID(ctx, factionId)
	if err != nil {
		return err
	}

	// Delete faction
	return s.repo.DeleteFaction(ctx, factionId)
}

func (s *Service) ListFactions(ctx context.Context, params api.ListFactionsParams) ([]api.Faction, map[string]interface{}, error) {
	factions, total, err := s.repo.ListFactions(ctx, params)
	if err != nil {
		return nil, nil, err
	}

	page := 1
	if params.Page.Set {
		page = params.Page.Value
	}

	limit := 10
	if params.Limit.Set {
		limit = params.Limit.Value
	}

	pagination := map[string]interface{}{
		"page":  page,
		"limit": limit,
		"total": total,
	}

	return factions, pagination, nil
}

func (s *Service) UpdateHierarchy(ctx context.Context, factionId string) (*api.FactionHierarchy, error) {
	// Check if faction exists
	_, err := s.repo.GetFactionByID(ctx, factionId)
	if err != nil {
		return nil, err
	}

	// Update hierarchy
	if err := s.repo.UpdateHierarchy(); err != nil {
		return nil, err
	}

	// Return updated hierarchy
	return s.GetHierarchy(ctx, factionId)
}

func (s *Service) GetHierarchy(ctx context.Context, factionId string) (*api.FactionHierarchy, error) {
	members, err := s.repo.GetHierarchy(ctx, factionId)
	if err != nil {
		return nil, err
	}

	totalMembers := len(members)

	factionUUID, err := uuid.Parse(factionId)
	if err != nil {
		return nil, err
	}

	hierarchy := &api.FactionHierarchy{
		FactionID:    api.NewOptUUID(factionUUID),
		Members:      members,
		TotalMembers: api.NewOptInt(totalMembers),
	}

	return hierarchy, nil
}
