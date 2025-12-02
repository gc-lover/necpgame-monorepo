// Issue: #142
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame/services/loot-service-go/pkg/api"
)

type Service interface {
	GenerateLoot(ctx context.Context, req *api.GenerateLootRequest) (*api.GenerateLootResponse, error)
	DistributeLoot(ctx context.Context, req *api.DistributeLootRequest) (*api.DistributeLootResponse, error)
	GetWorldDrops(ctx context.Context, locationID string) (*api.WorldDropsListResponse, error)
	PickupWorldDrop(ctx context.Context, dropID string) (*api.PickupDropResponse, error)
	GetRollStatus(ctx context.Context, rollID string) (*api.RollStatusResponse, error)
	RollForItem(ctx context.Context, rollID string, req *api.RollRequest) (*api.RollResponse, error)
	PassRoll(ctx context.Context, rollID string) error
	GetPlayerLootHistory(ctx context.Context, playerID string, params api.GetPlayerLootHistoryParams) (*api.LootHistoryResponse, error)
}

type LootService struct {
	repository Repository
}

func NewLootService(repository Repository) Service {
	return &LootService{repository: repository}
}

func (s *LootService) GenerateLoot(ctx context.Context, req *api.GenerateLootRequest) (*api.GenerateLootResponse, error) {
	// TODO: Реализовать генерацию лута
	return nil, errors.New("not implemented")
}

func (s *LootService) DistributeLoot(ctx context.Context, req *api.DistributeLootRequest) (*api.DistributeLootResponse, error) {
	// TODO: Реализовать распределение
	return nil, errors.New("not implemented")
}

func (s *LootService) GetWorldDrops(ctx context.Context, locationID string) (*api.WorldDropsListResponse, error) {
	return &api.WorldDropsListResponse{Drops: &[]api.WorldDrop{}}, nil
}

func (s *LootService) PickupWorldDrop(ctx context.Context, dropID string) (*api.PickupDropResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *LootService) GetRollStatus(ctx context.Context, rollID string) (*api.RollStatusResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *LootService) RollForItem(ctx context.Context, rollID string, req *api.RollRequest) (*api.RollResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *LootService) PassRoll(ctx context.Context, rollID string) error {
	return nil
}

func (s *LootService) GetPlayerLootHistory(ctx context.Context, playerID string, params api.GetPlayerLootHistoryParams) (*api.LootHistoryResponse, error) {
	return &api.LootHistoryResponse{History: &[]api.LootHistoryEntry{}}, nil
}

