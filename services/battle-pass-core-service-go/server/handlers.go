package server

import (
	"context"
	"fmt"

	"github.com/rs/zerolog/log"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-core-service-go/pkg/api"
)

// Handlers ...
type Handlers struct {
	Service *BattlePassCoreService
}

func NewHandlers(service *BattlePassCoreService) *Handlers {
	return &Handlers{Service: service}
}

func (h *Handlers) GetCurrentBattlePass(ctx context.Context) (api.GetCurrentBattlePassRes, error) {
	log.Ctx(ctx).Info().Msg("Handling GetCurrentBattlePass request")
	season, err := h.Service.GetCurrentBattlePass(ctx)
	if err != nil {
		log.Ctx(ctx).Error().Err(err).Msg("Failed to get current battle pass")
		return (*api.GetCurrentBattlePassInternalServerError)(h.NewError(ctx, err)), nil
	}
	return season, nil
}

func (h *Handlers) GetPlayerProgress(ctx context.Context, params api.GetPlayerProgressParams) (api.GetPlayerProgressRes, error) {
	log.Ctx(ctx).Info().Str("character_id", params.CharacterID.String()).Msg("Handling GetPlayerProgress request")
	progress, err := h.Service.GetPlayerProgress(ctx, params)
	if err != nil {
		log.Ctx(ctx).Error().Err(err).Msg("Failed to get player progress")
		return (*api.GetPlayerProgressInternalServerError)(h.NewError(ctx, err)), nil
	}
	return progress, nil
}

func (h *Handlers) GetLevelRequirements(ctx context.Context, params api.GetLevelRequirementsParams) (api.GetLevelRequirementsRes, error) {
	log.Ctx(ctx).Info().Int("level", params.Level).Msg("Handling GetLevelRequirements request")
	req, err := h.Service.GetLevelRequirements(ctx, params.Level)
	if err != nil {
		log.Ctx(ctx).Error().Err(err).Msg("Failed to get level requirements")
		return (*api.GetLevelRequirementsInternalServerError)(h.NewError(ctx, err)), nil
	}
	return req, nil
}

func (h *Handlers) PurchasePremium(ctx context.Context, req *api.PurchasePremiumRequest) (api.PurchasePremiumRes, error) {
	log.Ctx(ctx).Info().Str("character_id", req.CharacterID.String()).Msg("Handling PurchasePremium request")
	progress, err := h.Service.PurchasePremium(ctx, *req)
	if err != nil {
		log.Ctx(ctx).Error().Err(err).Msg("Failed to purchase premium battle pass")
		// Тут можно добавить логику для 400 Bad Request, если err указывает на это
		return (*api.PurchasePremiumInternalServerError)(h.NewError(ctx, err)), nil
	}
	return progress, nil
}

func (h *Handlers) NewError(ctx context.Context, err error) *api.Error {
	log.Ctx(ctx).Error().Err(err).Msg("API Error")
	return &api.Error{
		Error:   fmt.Sprintf("Internal Server Error: %v", err),
		Message: err.Error(),
	}
}

func (h *Handlers) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
	// Здесь должна быть логика аутентификации.
	// Для примера просто логируем и разрешаем.
	log.Ctx(ctx).Info().Str("token", t.Token).Str("operation", operationName).Msg("BearerAuth handler placeholder")
	return ctx, nil
}
