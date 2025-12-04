// Issue: #139
package server

import (
	"context"
	"fmt"
	"math/rand"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/party-service-go/pkg/api"
	"github.com/google/uuid"
)

type Party struct {
	ID         string    `json:"id"`
	Name       string    `json:"name"`
	LeaderID   string    `json:"leader_id"`
	Members    []string  `json:"members"`
	MaxMembers int       `json:"max_members"`
	LootMode   string    `json:"loot_mode"`
	CreatedAt  time.Time `json:"created_at"`
	UpdatedAt  time.Time `json:"updated_at"`
}

type PartyService struct {
	repo *PartyRepository
}

func NewPartyService(repo *PartyRepository) *PartyService {
	return &PartyService{
		repo: repo,
	}
}

// CreateParty creates a new party
func (s *PartyService) CreateParty(ctx context.Context, leaderID, name, lootMode string) (*Party, error) {
	partyID := fmt.Sprintf("party-%d", time.Now().Unix())

	party := &Party{
		ID:         partyID,
		Name:       name,
		LeaderID:   leaderID,
		Members:    []string{leaderID},
		MaxMembers: 5,
		LootMode:   lootMode,
		CreatedAt:  time.Now(),
		UpdatedAt:  time.Now(),
	}

	if err := s.repo.CreateParty(ctx, party); err != nil {
		return nil, fmt.Errorf("failed to create party: %w", err)
	}

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.party.created", party)

	return party, nil
}

// GetParty retrieves party by ID
func (s *PartyService) GetParty(ctx context.Context, partyID string) (*Party, error) {
	return s.repo.GetParty(ctx, partyID)
}

// DisbandParty disbands a party
func (s *PartyService) DisbandParty(ctx context.Context, partyID string) error {
	if err := s.repo.DeleteParty(ctx, partyID); err != nil {
		return fmt.Errorf("failed to disband party: %w", err)
	}

	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.party.disbanded", partyID)

	return nil
}

// InvitePlayer invites a player to the party
func (s *PartyService) InvitePlayer(ctx context.Context, partyID, playerID string) (*api.InviteResponse, error) {
	inviteID := uuid.New()

	invite := &api.InviteResponse{
		InviteId:  inviteID,
		ExpiresAt: time.Now().Add(5 * time.Minute),
	}

	// TODO: Store invite in database
	// TODO: Publish event to Event Bus
	// eventBus.Publish("social.party.invited", invite)

	return invite, nil
}

// AcceptInvite accepts a party invite
func (s *PartyService) AcceptInvite(ctx context.Context, inviteID, playerID string) (*Party, error) {
	// TODO: Get party from invite
	// TODO: Add player to party
	// TODO: Delete invite
	// TODO: Publish event

	return &Party{
		ID:       "party-001",
		Name:     "Sample Party",
		LeaderID: "leader-001",
		Members:  []string{"leader-001", playerID},
	}, nil
}

// DeclineInvite declines a party invite
func (s *PartyService) DeclineInvite(ctx context.Context, inviteID string) error {
	// TODO: Delete invite
	// TODO: Publish event
	return nil
}

// LeaveParty removes a player from the party
func (s *PartyService) LeaveParty(ctx context.Context, partyID, playerID string) error {
	party, err := s.repo.GetParty(ctx, partyID)
	if err != nil {
		return fmt.Errorf("party not found: %w", err)
	}

	// Remove player from members
	newMembers := make([]string, 0, len(party.Members)-1)
	for _, member := range party.Members {
		if member != playerID {
			newMembers = append(newMembers, member)
		}
	}

	party.Members = newMembers
	party.UpdatedAt = time.Now()

	if err := s.repo.UpdateParty(ctx, party); err != nil {
		return fmt.Errorf("failed to leave party: %w", err)
	}

	// TODO: Publish event
	return nil
}

// KickMember kicks a player from the party
func (s *PartyService) KickMember(ctx context.Context, partyID, playerID string) error {
	return s.LeaveParty(ctx, partyID, playerID)
}

// UpdateSettings updates party settings
func (s *PartyService) UpdateSettings(ctx context.Context, partyID string, settings *api.PartySettingsRequest) error {
	party, err := s.repo.GetParty(ctx, partyID)
	if err != nil {
		return fmt.Errorf("party not found: %w", err)
	}

	if settings.LootMode.IsSet() {
		party.LootMode = string(settings.LootMode.Value)
	}

	party.UpdatedAt = time.Now()

	if err := s.repo.UpdateParty(ctx, party); err != nil {
		return fmt.Errorf("failed to update settings: %w", err)
	}

	// TODO: Publish event
	return nil
}

// RollForLoot handles loot roll
func (s *PartyService) RollForLoot(ctx context.Context, partyID, playerID, itemID, rollType string) (*api.LootRollResponse, error) {
	// TODO: Implement loot roll logic
	playerUUID, _ := uuid.Parse(playerID)
	rollValue := rand.Intn(100) + 1 // 1-100
	
	return &api.LootRollResponse{
		RollValue: rollValue,
		PlayerId:  playerUUID,
		RollType:  api.LootRollResponseRollType(rollType),
	}, nil
}
