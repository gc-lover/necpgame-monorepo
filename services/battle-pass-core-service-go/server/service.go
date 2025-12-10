package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/rs/zerolog/log"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-core-service-go/pkg/api"
)

// BattlePassCoreService ...
type BattlePassCoreService struct {
	pool *pgxpool.Pool
}

func NewBattlePassCoreService(pool *pgxpool.Pool) *BattlePassCoreService {
	return &BattlePassCoreService{pool: pool}
}

func (s *BattlePassCoreService) GetCurrentBattlePass(ctx context.Context) (*api.BattlePassSeason, error) {
	// Имитация получения текущего Battle Pass из БД
	log.Ctx(ctx).Info().Msg("Fetching current battle pass")
	resp := &api.BattlePassSeason{
		ID:        api.NewOptUUID(uuid.New()),
		Name:      api.NewOptString("Season 1: First Strike"),
		StartDate: api.NewOptDateTime(time.Now()),
		EndDate:   api.NewOptDateTime(time.Now().Add(time.Hour * 24 * 30)),
		MaxLevel:  api.NewOptInt(100),
		IsActive:  api.NewOptBool(true),
	}

	return resp, nil
}

func (s *BattlePassCoreService) GetPlayerProgress(ctx context.Context, params api.GetPlayerProgressParams) (*api.PlayerBattlePassProgress, error) {
	// Имитация получения прогресса игрока из БД
	log.Ctx(ctx).Info().Str("character_id", params.CharacterID.String()).Msg("Fetching player progress")
	return &api.PlayerBattlePassProgress{
		CharacterID:        api.NewOptUUID(params.CharacterID),
		SeasonID:           api.NewOptUUID(uuid.New()),
		Level:              api.NewOptInt(10),
		Xp:                 api.NewOptInt(500),
		XpToNextLevel:      api.NewOptInt(1000),
		HasPremium:         api.NewOptBool(false),
		PremiumPurchasedAt: api.NewOptNilDateTime(time.Now()), // Пример, если премиум куплен
	}, nil
}

func (s *BattlePassCoreService) GetLevelRequirements(ctx context.Context, level int) (*api.LevelRequirements, error) {
	// Имитация получения требований уровня
	log.Ctx(ctx).Info().Int("level", level).Msg("Fetching level requirements")
	if level < 1 || level > 100 {
		return nil, fmt.Errorf("invalid level: %d", level)
	}
	return &api.LevelRequirements{
		Level:        api.NewOptInt(level),
		XpRequired:   api.NewOptInt(level * 100),
		CumulativeXp: api.NewOptInt((level * (level - 1) / 2) * 100),
	}, nil
}

func (s *BattlePassCoreService) PurchasePremium(ctx context.Context, req api.PurchasePremiumRequest) (*api.PlayerBattlePassProgress, error) {
	// Имитация покупки премиум пропуска
	log.Ctx(ctx).Info().Str("character_id", req.CharacterID.String()).Msg("Purchasing premium battle pass")
	// Проверяем, есть ли уже премиум
	progress, err := s.GetPlayerProgress(ctx, api.GetPlayerProgressParams{CharacterID: req.CharacterID})
	if err != nil {
		return nil, err
	}

	if progress != nil && progress.HasPremium.Value {
		return nil, fmt.Errorf("premium already purchased for character %s", req.CharacterID.String())
	}

	// Обновляем прогресс игрока (имитация)
	if progress == nil {
		// Если прогресс nil, создаем новый
		progress = &api.PlayerBattlePassProgress{
			CharacterID:        api.NewOptUUID(req.CharacterID),
			SeasonID:           api.NewOptUUID(uuid.New()), // Предполагаем новый сезон
			Level:              api.NewOptInt(1),
			Xp:                 api.NewOptInt(0),
			XpToNextLevel:      api.NewOptInt(100), // Базовое значение
			HasPremium:         api.NewOptBool(false),
			PremiumPurchasedAt: api.OptNilDateTime{},
		}
	}
	progress.HasPremium.SetTo(true)
	progress.PremiumPurchasedAt.SetTo(time.Now())

	return progress, nil
}
