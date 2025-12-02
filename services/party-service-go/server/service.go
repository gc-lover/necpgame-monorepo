// Issue: #139
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame/services/party-service-go/pkg/api"
	"github.com/oapi-codegen/runtime/types"
)

// Service интерфейс бизнес-логики
type Service interface {
	CreateParty(ctx context.Context, req *api.CreatePartyRequest) (*api.PartyResponse, error)
	GetParty(ctx context.Context, partyID string) (*api.PartyResponse, error)
	DeleteParty(ctx context.Context, partyID string) error
	InviteToParty(ctx context.Context, partyID string, req *api.InviteRequest) (*api.InviteResponse, error)
	AcceptInvite(ctx context.Context, inviteID string) error
	DeclineInvite(ctx context.Context, inviteID string) error
	LeaveParty(ctx context.Context, partyID string) error
	KickMember(ctx context.Context, partyID, memberID string) error
	UpdateSettings(ctx context.Context, partyID string, req *api.PartySettingsRequest) (*api.PartyResponse, error)
	RollForLoot(ctx context.Context, partyID string, req *api.LootRollRequest) (*api.LootRollResponse, error)
	PassLootRoll(ctx context.Context, partyID, rollID string) error
}

// PartyService реализует Service
type PartyService struct {
	repository Repository
}

// NewPartyService создает новый сервис
func NewPartyService(repository Repository) Service {
	return &PartyService{repository: repository}
}

func (s *PartyService) CreateParty(ctx context.Context, req *api.CreatePartyRequest) (*api.PartyResponse, error) {
	// TODO: Реализовать создание группы
	leaderID := parseUUID("550e8400-e29b-41d4-a716-446655440000")
	partyID := parseUUID("660e8400-e29b-41d4-a716-446655440001")
	lootMode := api.PartyResponseLootModeFreeForAll
	return &api.PartyResponse{
		PartyId:  partyID,
		LeaderId: leaderID,
		Members:  []api.PartyMember{},
		LootMode: lootMode,
	}, nil
}

func (s *PartyService) GetParty(ctx context.Context, partyID string) (*api.PartyResponse, error) {
	// TODO: Реализовать получение группы
	return nil, errors.New("not implemented")
}

func (s *PartyService) DeleteParty(ctx context.Context, partyID string) error {
	// TODO: Реализовать удаление группы
	return nil
}

func (s *PartyService) InviteToParty(ctx context.Context, partyID string, req *api.InviteRequest) (*api.InviteResponse, error) {
	// TODO: Реализовать приглашение
	return nil, errors.New("not implemented")
}

func (s *PartyService) AcceptInvite(ctx context.Context, inviteID string) error {
	// TODO: Реализовать принятие приглашения
	return nil
}

func (s *PartyService) DeclineInvite(ctx context.Context, inviteID string) error {
	// TODO: Реализовать отклонение приглашения
	return nil
}

func (s *PartyService) LeaveParty(ctx context.Context, partyID string) error {
	// TODO: Реализовать выход из группы
	return nil
}

func (s *PartyService) KickMember(ctx context.Context, partyID, memberID string) error {
	// TODO: Реализовать кик
	return nil
}

func (s *PartyService) UpdateSettings(ctx context.Context, partyID string, req *api.PartySettingsRequest) (*api.PartyResponse, error) {
	// TODO: Реализовать обновление настроек
	return nil, errors.New("not implemented")
}

func (s *PartyService) RollForLoot(ctx context.Context, partyID string, req *api.LootRollRequest) (*api.LootRollResponse, error) {
	// TODO: Реализовать roll
	return nil, errors.New("not implemented")
}

func (s *PartyService) PassLootRoll(ctx context.Context, partyID, rollID string) error {
	// TODO: Реализовать pass
	return nil
}

func parseUUID(s string) types.UUID {
	uuid := types.UUID{}
	copy(uuid[:], s)
	return uuid
}

