// Issue: Social Service ogen Migration
// Handlers for social-service-go - implements api.Handler (ogen)
package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/necpgame/social-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// Constants moved to handlers.go to avoid duplication

// SocialHandlersOgen implements api.Handler (ogen)
type SocialHandlersOgen struct {
	logger       *logrus.Logger
	partyService PartyServiceInterface
	orderService OrderServiceInterface
}

// NewSocialHandlersOgen creates new ogen handlers
func NewSocialHandlersOgen(logger *logrus.Logger, db *pgxpool.Pool) *SocialHandlersOgen {
	h := &SocialHandlersOgen{
		logger: logger,
	}
	if db != nil {
		h.orderService = NewOrderService(db, logger)
	}
	return h
}

// SetPartyService sets party service (called from http_server_ogen.go after DB init)
func (h *SocialHandlersOgen) SetPartyService(service PartyServiceInterface) {
	h.partyService = service
}

// GetFriends implements getFriends operation
// Hot path: 2k RPS - требует оптимизаций (caching, pooling)
func (h *SocialHandlersOgen) GetFriends(ctx context.Context, params api.GetFriendsParams) (api.GetFriendsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.WithFields(logrus.Fields{
		"online_only": params.OnlineOnly.Value,
		"limit":       params.Limit.Value,
		"offset":      params.Offset.Value,
	}).Debug("GetFriends called")

	// TODO: Implement with service layer
	// - 3-tier caching (memory → Redis → DB)
	// - Memory pooling for response objects
	// - Batch DB queries if multiple requests
	
	// Mock response
	response := &api.FriendListResponse{
		Friends: []api.Friendship{},
		Total:   api.NewOptInt(0),
	}

	return response, nil
}

// GetFriend implements getFriend operation
func (h *SocialHandlersOgen) GetFriend(ctx context.Context, params api.GetFriendParams) (api.GetFriendRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.WithField("friend_id", params.FriendID).Debug("GetFriend called")

	// TODO: Implement with service layer
	// - Check cache first
	// - Single query to DB with covering index
	
	// Mock: Friend not found
	return &api.GetFriendNotFound{}, nil
}

// GetOnlineFriends implements getOnlineFriends operation
// Hot path: needs optimization
func (h *SocialHandlersOgen) GetOnlineFriends(ctx context.Context, params api.GetOnlineFriendsParams) (api.GetOnlineFriendsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.WithFields(logrus.Fields{
		"limit":  params.Limit.Value,
		"offset": params.Offset.Value,
	}).Debug("GetOnlineFriends called")

	// TODO: Implement with service layer
	// - Redis ZSET for online users (sorted by last_seen)
	// - No DB queries for online check
	// - Memory pooling
	
	response := &api.FriendListResponse{
		Friends: []api.Friendship{},
		Total:   api.NewOptInt(0),
	}

	return response, nil
}

// GetFriendsCount implements getFriendsCount operation
func (h *SocialHandlersOgen) GetFriendsCount(ctx context.Context) (api.GetFriendsCountRes, error) {
	ctx, cancel := context.WithTimeout(ctx, CacheTimeout)
	defer cancel()

	h.logger.Debug("GetFriendsCount called")

	// TODO: Implement with service layer
	// - Cache count in Redis (5 min TTL)
	// - Update on friend add/remove
	
	response := &api.FriendsCountResponse{
		Count: api.NewOptInt(0),
	}

	return response, nil
}

// RemoveFriend implements removeFriend operation
func (h *SocialHandlersOgen) RemoveFriend(ctx context.Context, params api.RemoveFriendParams) (api.RemoveFriendRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	h.logger.WithField("friend_id", params.FriendID).Info("RemoveFriend called")

	// TODO: Implement with service layer
	// - Transaction for consistency
	// - Invalidate cache
	// - Notify other player via WebSocket
	
	response := &api.StatusResponse{
		Status: api.NewOptString("removed"),
	}

	return response, nil
}

// NewOptInt creates OptInt from int value
func NewOptInt(v int) api.OptInt {
	return api.OptInt{Value: v, Set: true}
}

// Issue: #1509 - Order handlers moved to order_handlers.go (chi router)
// Ogen handlers require generated types from social-service.yaml
// Using chi router handlers until ogen code is generated

// Note: Order methods removed - use order_handlers.go instead

// Issue: #1488 - Party Core Service handlers

// CreateParty implements createParty operation
func (h *SocialHandlersOgen) CreateParty(ctx context.Context, req *api.CreatePartyRequest) (api.CreatePartyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.partyService == nil {
		h.logger.Error("Party service not initialized")
		return &api.CreatePartyInternalServerError{}, nil
	}

	// Get leader ID from context (from auth middleware)
	leaderIDStr := getCharacterIDFromContext(ctx)
	leaderID, err := uuid.Parse(leaderIDStr)
	if err != nil {
		h.logger.WithError(err).Error("Failed to parse leader ID")
		return &api.CreatePartyBadRequest{}, nil
	}

	// Convert API request to model
	partyReq := &models.CreatePartyRequest{}
	if req.MaxSize.IsSet() {
		maxSize := req.MaxSize.Value
		partyReq.MaxSize = &maxSize
	}
	if req.LootMode.IsSet() {
		lootMode := convertLootModeFromAPI(req.LootMode.Value)
		partyReq.LootMode = &lootMode
	}

	party, err := h.partyService.CreateParty(ctx, leaderID, partyReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create party")
		return &api.CreatePartyInternalServerError{}, nil
	}

	// Convert model to API response
	response := convertPartyToAPI(party)
	return response, nil
}

// GetParty implements getParty operation
func (h *SocialHandlersOgen) GetParty(ctx context.Context, params api.GetPartyParams) (api.GetPartyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.partyService == nil {
		return &api.GetPartyInternalServerError{}, nil
	}

	if !params.PartyID.IsSet() {
		return &api.GetPartyNotFound{}, nil
	}

	partyID := params.PartyID.Value

	party, err := h.partyService.GetParty(ctx, partyID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get party")
		return &api.GetPartyInternalServerError{}, nil
	}

	if party == nil {
		return &api.GetPartyNotFound{}, nil
	}

	response := convertPartyToAPI(party)
	return response, nil
}

// GetPartyById implements getPartyById operation
func (h *SocialHandlersOgen) GetPartyById(ctx context.Context, params api.GetPartyByIdParams) (api.GetPartyByIdRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.partyService == nil {
		return &api.GetPartyByIdInternalServerError{}, nil
	}

	partyID := params.PartyId

	party, err := h.partyService.GetParty(ctx, partyID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get party")
		return &api.GetPartyByIdInternalServerError{}, nil
	}

	if party == nil {
		return &api.GetPartyByIdNotFound{}, nil
	}

	response := convertPartyToAPI(party)
	return response, nil
}

// GetPartyLeader implements getPartyLeader operation
func (h *SocialHandlersOgen) GetPartyLeader(ctx context.Context, params api.GetPartyLeaderParams) (api.GetPartyLeaderRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.partyService == nil {
		return &api.GetPartyLeaderInternalServerError{}, nil
	}

	partyID := params.PartyId

	leader, err := h.partyService.GetPartyLeader(ctx, partyID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get party leader")
		return &api.GetPartyLeaderInternalServerError{}, nil
	}

	if leader == nil {
		return &api.GetPartyLeaderNotFound{}, nil
	}

	response := convertPartyMemberToAPI(leader)
	return response, nil
}

// GetPlayerParty implements getPlayerParty operation
func (h *SocialHandlersOgen) GetPlayerParty(ctx context.Context, params api.GetPlayerPartyParams) (api.GetPlayerPartyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.partyService == nil {
		return &api.GetPlayerPartyInternalServerError{}, nil
	}

	accountID := params.AccountId

	party, err := h.partyService.GetPartyByPlayerID(ctx, accountID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player party")
		return &api.GetPlayerPartyInternalServerError{}, nil
	}

	if party == nil {
		return &api.GetPlayerPartyNotFound{}, nil
	}

	response := convertPartyToAPI(party)
	return response, nil
}

// TransferPartyLeadership implements transferPartyLeadership operation
func (h *SocialHandlersOgen) TransferPartyLeadership(ctx context.Context, req *api.TransferLeadershipRequest, params api.TransferPartyLeadershipParams) (api.TransferPartyLeadershipRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.partyService == nil {
		return &api.TransferPartyLeadershipInternalServerError{}, nil
	}

	partyID := params.PartyId
	newLeaderID := req.NewLeaderID

	party, err := h.partyService.TransferLeadership(ctx, partyID, newLeaderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to transfer leadership")
		return &api.TransferPartyLeadershipBadRequest{}, nil
	}

	if party == nil {
		return &api.TransferPartyLeadershipNotFound{}, nil
	}

	response := convertPartyToAPI(party)
	return response, nil
}

// convertPartyToAPI converts model to API response
func convertPartyToAPI(party *models.Party) *api.Party {
	response := &api.Party{
		ID:        api.NewOptUUID(party.ID),
		LeaderID:  api.NewOptUUID(party.LeaderID),
		MaxSize:   api.NewOptInt(party.MaxSize),
		LootMode:  api.NewOptPartyLootMode(convertLootModeToAPI(party.LootMode)),
		CreatedAt: api.NewOptDateTime(party.CreatedAt),
		UpdatedAt: api.NewOptDateTime(party.UpdatedAt),
		Members:   make([]api.PartyMember, len(party.Members)),
	}

	for i, member := range party.Members {
		response.Members[i] = convertPartyMemberToAPI(&member)
	}

	return response
}

// convertPartyMemberToAPI converts model to API response
func convertPartyMemberToAPI(member *models.PartyMember) api.PartyMember {
	return api.PartyMember{
		CharacterID: api.NewOptUUID(member.CharacterID),
		Role:        api.NewOptPartyMemberRole(convertRoleToAPI(member.Role)),
		JoinedAt:    api.NewOptDateTime(member.JoinedAt),
	}
}

// convertLootModeToAPI converts model LootMode to API PartyLootMode
func convertLootModeToAPI(mode models.LootMode) api.PartyLootMode {
	switch mode {
	case models.LootModeFreeForAll:
		return api.PartyLootModeFreeForAll
	case models.LootModeRoundRobin:
		return api.PartyLootModeRoundRobin
	case models.LootModeNeedBeforeGreed:
		return api.PartyLootModeNeedBeforeGreed
	case models.LootModeMasterLooter:
		return api.PartyLootModeMasterLooter
	default:
		return api.PartyLootModeFreeForAll
	}
}

// convertLootModeFromAPI converts API CreatePartyRequestLootMode to model LootMode
func convertLootModeFromAPI(mode api.CreatePartyRequestLootMode) models.LootMode {
	switch mode {
	case api.CreatePartyRequestLootModeFreeForAll:
		return models.LootModeFreeForAll
	case api.CreatePartyRequestLootModeRoundRobin:
		return models.LootModeRoundRobin
	case api.CreatePartyRequestLootModeNeedBeforeGreed:
		return models.LootModeNeedBeforeGreed
	case api.CreatePartyRequestLootModeMasterLooter:
		return models.LootModeMasterLooter
	default:
		return models.LootModeFreeForAll
	}
}

// convertRoleToAPI converts model PartyRole to API PartyMemberRole
func convertRoleToAPI(role models.PartyRole) api.PartyMemberRole {
	switch role {
	case models.PartyRoleLeader:
		return api.PartyMemberRoleLeader
	case models.PartyRoleMember:
		return api.PartyMemberRoleMember
	default:
		return api.PartyMemberRoleMember
	}
}

