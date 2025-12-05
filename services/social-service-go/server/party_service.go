// Issue: #1488
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

type PartyServiceInterface interface {
	CreateParty(ctx context.Context, leaderID uuid.UUID, req *models.CreatePartyRequest) (*models.Party, error)
	GetParty(ctx context.Context, partyID uuid.UUID) (*models.Party, error)
	GetPartyByPlayerID(ctx context.Context, accountID uuid.UUID) (*models.Party, error)
	GetPartyLeader(ctx context.Context, partyID uuid.UUID) (*models.PartyMember, error)
	TransferLeadership(ctx context.Context, partyID, newLeaderID uuid.UUID) (*models.Party, error)
}

type PartyService struct {
	repo PartyRepositoryInterface
}

func NewPartyService(repo PartyRepositoryInterface) *PartyService {
	return &PartyService{
		repo: repo,
	}
}

func (s *PartyService) CreateParty(ctx context.Context, leaderID uuid.UUID, req *models.CreatePartyRequest) (*models.Party, error) {
	if req.MaxSize != nil {
		if *req.MaxSize < 2 || *req.MaxSize > 5 {
			return nil, fmt.Errorf("max_size must be between 2 and 5")
		}
	}

	if req.LootMode != nil {
		validModes := []models.LootMode{
			models.LootModeFreeForAll,
			models.LootModeRoundRobin,
			models.LootModeNeedBeforeGreed,
			models.LootModeMasterLooter,
		}
		valid := false
		for _, mode := range validModes {
			if *req.LootMode == mode {
				valid = true
				break
			}
		}
		if !valid {
			return nil, fmt.Errorf("invalid loot_mode")
		}
	}

	party, err := s.repo.Create(ctx, leaderID, req)
	if err != nil {
		return nil, fmt.Errorf("failed to create party: %w", err)
	}

	return party, nil
}

func (s *PartyService) GetParty(ctx context.Context, partyID uuid.UUID) (*models.Party, error) {
	party, err := s.repo.GetByID(ctx, partyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party: %w", err)
	}
	if party == nil {
		return nil, fmt.Errorf("party not found")
	}
	return party, nil
}

func (s *PartyService) GetPartyByPlayerID(ctx context.Context, accountID uuid.UUID) (*models.Party, error) {
	party, err := s.repo.GetByPlayerID(ctx, accountID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party by player: %w", err)
	}
	if party == nil {
		return nil, fmt.Errorf("party not found")
	}
	return party, nil
}

func (s *PartyService) GetPartyLeader(ctx context.Context, partyID uuid.UUID) (*models.PartyMember, error) {
	leader, err := s.repo.GetLeader(ctx, partyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party leader: %w", err)
	}
	if leader == nil {
		return nil, fmt.Errorf("party leader not found")
	}
	return leader, nil
}

func (s *PartyService) TransferLeadership(ctx context.Context, partyID, newLeaderID uuid.UUID) (*models.Party, error) {
	party, err := s.repo.GetByID(ctx, partyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get party: %w", err)
	}
	if party == nil {
		return nil, fmt.Errorf("party not found")
	}

	isMember := false
	for _, member := range party.Members {
		if member.CharacterID == newLeaderID {
			isMember = true
			break
		}
	}
	if !isMember {
		return nil, fmt.Errorf("new leader must be a member of the party")
	}

	err = s.repo.TransferLeadership(ctx, partyID, newLeaderID)
	if err != nil {
		return nil, fmt.Errorf("failed to transfer leadership: %w", err)
	}

	party, err = s.repo.GetByID(ctx, partyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get updated party: %w", err)
	}

	return party, nil
}

