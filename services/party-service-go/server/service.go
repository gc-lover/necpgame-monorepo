// Issue: #139
package server

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"math/rand"
	"net/http"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/party-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
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
	repo       *PartyRepository
	logger     *logrus.Logger
	lootClient *LootServiceClient // HTTP client for loot-service
	eventBus   EventBus           // Event bus for publishing events
}

// EventBus interface for publishing events
type EventBus interface {
	PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error
}

// RedisEventBus implements EventBus using Redis
type RedisEventBus struct {
	client *redis.Client
	logger *logrus.Logger
}

func NewRedisEventBus(redisClient *redis.Client) *RedisEventBus {
	return &RedisEventBus{
		client: redisClient,
		logger: logrus.New(),
	}
}

func (b *RedisEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	eventData, err := json.Marshal(payload)
	if err != nil {
		return err
	}

	channel := "events:" + eventType
	return b.client.Publish(ctx, channel, eventData).Err()
}

// LootServiceClient for calling loot-service API
type LootServiceClient struct {
	baseURL    string
	httpClient *http.Client
}

func NewLootServiceClient(baseURL string) *LootServiceClient {
	return &LootServiceClient{
		baseURL: baseURL,
		httpClient: &http.Client{
			Timeout: 5 * time.Second,
		},
	}
}

func NewPartyService(repo *PartyRepository, eventBus EventBus) *PartyService {
	return &PartyService{
		repo:       repo,
		logger:     logrus.New(),
		lootClient: NewLootServiceClient("http://loot-service:8085"), // TODO: Get from config
		eventBus:   eventBus,
	}
}

// NewPartyServiceSimple creates service without event bus (for testing)
func NewPartyServiceSimple(repo *PartyRepository) *PartyService {
	return &PartyService{
		repo:       repo,
		logger:     logrus.New(),
		lootClient: NewLootServiceClient("http://loot-service:8085"),
		eventBus:   nil,
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

	// Publish event to Event Bus
	if s.eventBus != nil {
		s.eventBus.PublishEvent(ctx, "social.party.created", map[string]interface{}{
			"party_id":  party.ID,
			"leader_id": party.LeaderID,
			"name":      party.Name,
			"loot_mode": party.LootMode,
			"timestamp": party.CreatedAt,
		})
	}

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

	// Publish event to Event Bus
	if s.eventBus != nil {
		s.eventBus.PublishEvent(ctx, "social.party.disbanded", map[string]interface{}{
			"party_id":  partyID,
			"timestamp": time.Now(),
		})
	}

	return nil
}

// InvitePlayer invites a player to the party
func (s *PartyService) InvitePlayer(ctx context.Context, partyID, playerID, inviterID string) (*api.InviteResponse, error) {
	// Check if party exists and player is leader
	party, err := s.repo.GetParty(ctx, partyID)
	if err != nil {
		return nil, fmt.Errorf("party not found: %w", err)
	}

	if party.LeaderID != inviterID {
		return nil, fmt.Errorf("only party leader can invite players")
	}

	// Check if player is already in party
	for _, member := range party.Members {
		if member == playerID {
			return nil, fmt.Errorf("player is already in party")
		}
	}

	// Check party capacity
	if len(party.Members) >= party.MaxMembers {
		return nil, fmt.Errorf("party is full")
	}

	inviteID := uuid.New()
	expiresAt := time.Now().Add(5 * time.Minute)

	invite := &PartyInvite{
		ID:        inviteID,
		PartyID:   partyID,
		InviterID: inviterID,
		InviteeID: playerID,
		Status:    "pending",
		ExpiresAt: expiresAt,
		CreatedAt: time.Now(),
	}

	if err := s.repo.CreatePartyInvite(ctx, invite); err != nil {
		return nil, fmt.Errorf("failed to create invite: %w", err)
	}

	// Publish event to Event Bus
	if s.eventBus != nil {
		s.eventBus.PublishEvent(ctx, "social.party.invited", map[string]interface{}{
			"party_id":   partyID,
			"invite_id":  inviteID.String(),
			"inviter_id": inviterID,
			"invitee_id": playerID,
			"expires_at": expiresAt,
			"timestamp":  time.Now(),
		})
	}

	response := &api.InviteResponse{
		InviteId:  inviteID,
		ExpiresAt: expiresAt,
	}

	return response, nil
}

// AcceptInvite accepts a party invite
func (s *PartyService) AcceptInvite(ctx context.Context, inviteID, playerID string) (*Party, error) {
	// Get invite from database
	invite, err := s.repo.GetPartyInvite(ctx, inviteID)
	if err != nil {
		return nil, fmt.Errorf("invite not found: %w", err)
	}

	// Check if invite is for this player
	if invite.InviteeID != playerID {
		return nil, fmt.Errorf("invite is not for this player")
	}

	// Check if invite is still pending and not expired
	if invite.Status != "pending" {
		return nil, fmt.Errorf("invite is not pending")
	}

	if time.Now().After(invite.ExpiresAt) {
		return nil, fmt.Errorf("invite has expired")
	}

	// Get party
	party, err := s.repo.GetParty(ctx, invite.PartyID)
	if err != nil {
		return nil, fmt.Errorf("party not found: %w", err)
	}

	// Check party capacity
	if len(party.Members) >= party.MaxMembers {
		return nil, fmt.Errorf("party is full")
	}

	// Check if player is already in party
	for _, member := range party.Members {
		if member == playerID {
			return nil, fmt.Errorf("player is already in party")
		}
	}

	// Add player to party
	party.Members = append(party.Members, playerID)
	party.UpdatedAt = time.Now()

	if err := s.repo.UpdateParty(ctx, party); err != nil {
		return nil, fmt.Errorf("failed to add player to party: %w", err)
	}

	// Update invite status
	if err := s.repo.UpdatePartyInviteStatus(ctx, inviteID, "accepted"); err != nil {
		return nil, fmt.Errorf("failed to update invite status: %w", err)
	}

	// Publish events to Event Bus
	if s.eventBus != nil {
		s.eventBus.PublishEvent(ctx, "social.party.invite-accepted", map[string]interface{}{
			"party_id":  party.ID,
			"invite_id": inviteID,
			"player_id": playerID,
			"timestamp": time.Now(),
		})

		s.eventBus.PublishEvent(ctx, "social.party.member-joined", map[string]interface{}{
			"party_id":  party.ID,
			"player_id": playerID,
			"timestamp": time.Now(),
		})
	}

	return party, nil
}

// DeclineInvite declines a party invite
func (s *PartyService) DeclineInvite(ctx context.Context, inviteID, playerID string) error {
	// Get invite to verify ownership
	invite, err := s.repo.GetPartyInvite(ctx, inviteID)
	if err != nil {
		return fmt.Errorf("invite not found: %w", err)
	}

	// Check if invite is for this player
	if invite.InviteeID != playerID {
		return fmt.Errorf("invite is not for this player")
	}

	// Update invite status
	if err := s.repo.UpdatePartyInviteStatus(ctx, inviteID, "declined"); err != nil {
		return fmt.Errorf("failed to decline invite: %w", err)
	}

	// Publish event to Event Bus
	if s.eventBus != nil {
		s.eventBus.PublishEvent(ctx, "social.party.invite-declined", map[string]interface{}{
			"invite_id": inviteID,
			"party_id":  invite.PartyID,
			"player_id": playerID,
			"timestamp": time.Now(),
		})
	}

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

// GetPlayerInvites retrieves active invitations for a player
func (s *PartyService) GetPlayerInvites(ctx context.Context, playerID string) ([]*PartyInvite, error) {
	return s.repo.GetPartyInvitesByPlayer(ctx, playerID)
}

// RollForLoot handles loot roll
func (s *PartyService) RollForLoot(ctx context.Context, partyID, playerID, itemID, rollType string) (*api.LootRollResponse, error) {
	// Check if player is in party
	party, err := s.repo.GetParty(ctx, partyID)
	if err != nil {
		return nil, fmt.Errorf("party not found: %w", err)
	}

	playerInParty := false
	for _, member := range party.Members {
		if member == playerID {
			playerInParty = true
			break
		}
	}

	if !playerInParty {
		return nil, fmt.Errorf("player is not in party")
	}

	// Call loot-service for proper loot distribution
	playerUUID, _ := uuid.Parse(playerID)
	itemUUID, _ := uuid.Parse(itemID)

	rollRequest := map[string]interface{}{
		"party_id":  partyID,
		"player_id": playerUUID.String(),
		"item_id":   itemUUID.String(),
		"roll_type": rollType,
	}

	// Call loot-service API
	rollResult, err := s.callLootService(ctx, "/api/v1/loot/roll", rollRequest)
	if err != nil {
		s.logger.WithError(err).Error("Failed to call loot service")
		// Fallback to simple roll
		rollValue := rand.Intn(100) + 1
		return &api.LootRollResponse{
			RollValue: rollValue,
			PlayerId:  playerUUID,
			RollType:  api.LootRollResponseRollType(rollType),
		}, nil
	}

	// Parse response from loot-service
	rollValue := int(rollResult["roll_value"].(float64))

	return &api.LootRollResponse{
		RollValue: rollValue,
		PlayerId:  playerUUID,
		RollType:  api.LootRollResponseRollType(rollType),
	}, nil
}

// callLootService makes HTTP call to loot-service
func (s *PartyService) callLootService(ctx context.Context, endpoint string, payload interface{}) (map[string]interface{}, error) {
	jsonData, err := json.Marshal(payload)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal payload: %w", err)
	}

	req, err := http.NewRequestWithContext(ctx, "POST", s.lootClient.baseURL+endpoint, bytes.NewBuffer(jsonData))
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	req.Header.Set("Content-Type", "application/json")

	resp, err := s.lootClient.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("failed to call loot service: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("loot service returned status %d", resp.StatusCode)
	}

	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return result, nil
}
