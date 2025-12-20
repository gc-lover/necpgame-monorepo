// Package server Issue: #75
package server

import (
	"context"
	"strings"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/NECPGAME/character-management-service-go/pkg/api"
)

// Handlers содержит обработчики HTTP запросов
type Handlers struct {
	service *Service
	logger  *zap.Logger
}

// NewHandlers создает новые обработчики
func NewHandlers(service *Service, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

// GetPlayerCharacters получает список персонажей игрока
func (h *Handlers) GetPlayerCharacters(ctx context.Context, params api.GetPlayerCharactersParams) (api.GetPlayerCharactersRes, error) {
	h.logger.Info("Handling get player characters", zap.String("player_id", params.PlayerId))

	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.GetPlayerCharactersBadRequest{}, nil
	}

	includeDeleted := false
	if params.IncludeDeleted != nil {
		includeDeleted = *params.IncludeDeleted
	}

	characters, err := h.service.GetPlayerCharacters(ctx, playerID, includeDeleted)
	if err != nil {
		h.logger.Error("Failed to get player characters", zap.Error(err))
		return &api.GetPlayerCharactersInternalServerError{}, err
	}

	// Получаем активного персонажа
	activeCharacterID, err := h.service.repo.GetActiveCharacterID(ctx, playerID)
	if err != nil {
		h.logger.Warn("Failed to get active character ID", zap.Error(err))
	}

	// Конвертируем в API формат
	var apiCharacters []api.Character
	for _, char := range characters {
		apiChar := api.Character{
			Id:             api.NewOptUUID(char.ID),
			PlayerId:       api.NewOptUUID(char.PlayerID),
			Name:           api.NewOptString(char.Name),
			CharacterClass: api.NewOptString(char.CharacterClass),
			Origin:         api.NewOptString(char.Origin),
			Level:          api.NewOptInt(char.Level),
			Experience:     api.NewOptInt(char.Experience),
			Status:         api.NewOptString(char.Status),
			CreatedAt:      api.NewOptDateTime(char.CreatedAt),
			UpdatedAt:      api.NewOptDateTime(char.UpdatedAt),
		}

		if char.Appearance != nil {
			apiChar.Appearance = char.Appearance
		}
		if char.Attributes != nil {
			apiChar.Attributes = char.Attributes
		}

		apiCharacters = append(apiCharacters, apiChar)
	}

	var activeID *string
	if activeCharacterID != nil {
		idStr := activeCharacterID.String()
		activeID = &idStr
	}

	response := api.CharacterListResponse{
		Characters:        apiCharacters,
		TotalCount:        len(apiCharacters),
		ActiveCharacterId: activeID,
	}

	return &api.GetPlayerCharactersOK{
		Data: response,
	}, nil
}

// CreateCharacter создает нового персонажа
func (h *Handlers) CreateCharacter(ctx context.Context, req *api.CreateCharacterRequest, params api.CreateCharacterParams) (api.CreateCharacterRes, error) {
	h.logger.Info("Handling create character", zap.String("player_id", params.PlayerId))

	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.CreateCharacterBadRequest{}, nil
	}

	character, err := h.service.CreateCharacter(ctx, playerID, *req)
	if err != nil {
		h.logger.Error("Failed to create character", zap.Error(err))
		if err.Error() == "no available character slots" {
			return &api.CreateCharacterConflict{}, nil
		}
		return &api.CreateCharacterBadRequest{}, nil
	}

	apiChar := api.Character{
		Id:             api.NewOptUUID(character.ID),
		PlayerId:       api.NewOptUUID(character.PlayerID),
		Name:           api.NewOptString(character.Name),
		CharacterClass: api.NewOptString(character.CharacterClass),
		Origin:         api.NewOptString(character.Origin),
		Level:          api.NewOptInt(character.Level),
		Experience:     api.NewOptInt(character.Experience),
		Status:         api.NewOptString(character.Status),
		CreatedAt:      api.NewOptDateTime(character.CreatedAt),
		UpdatedAt:      api.NewOptDateTime(character.UpdatedAt),
	}

	if character.Appearance != nil {
		apiChar.Appearance = character.Appearance
	}
	if character.Attributes != nil {
		apiChar.Attributes = character.Attributes
	}

	response := api.CharacterResponse{
		Character: &apiChar,
	}

	return &api.CreateCharacterCreated{
		Data: response,
	}, nil
}

// GetCharacter получает персонажа по ID
func (h *Handlers) GetCharacter(ctx context.Context, params api.GetCharacterParams) (api.GetCharacterRes, error) {
	h.logger.Info("Handling get character", zap.String("character_id", params.CharacterId))

	characterID, err := uuid.Parse(params.CharacterId)
	if err != nil {
		return &api.GetCharacterBadRequest{}, nil
	}

	character, err := h.service.GetCharacter(ctx, characterID)
	if err != nil {
		h.logger.Error("Failed to get character", zap.Error(err))
		return &api.GetCharacterNotFound{}, nil
	}

	apiChar := api.Character{
		Id:             api.NewOptUUID(character.ID),
		PlayerId:       api.NewOptUUID(character.PlayerID),
		Name:           api.NewOptString(character.Name),
		CharacterClass: api.NewOptString(character.CharacterClass),
		Origin:         api.NewOptString(character.Origin),
		Level:          api.NewOptInt(character.Level),
		Experience:     api.NewOptInt(character.Experience),
		Status:         api.NewOptString(character.Status),
		CreatedAt:      api.NewOptDateTime(character.CreatedAt),
		UpdatedAt:      api.NewOptDateTime(character.UpdatedAt),
	}

	if character.Appearance != nil {
		apiChar.Appearance = character.Appearance
	}
	if character.Attributes != nil {
		apiChar.Attributes = character.Attributes
	}

	response := api.CharacterResponse{
		Character: &apiChar,
	}

	return &api.GetCharacterOK{
		Data: response,
	}, nil
}

// UpdateCharacter обновляет персонажа
func (h *Handlers) UpdateCharacter(ctx context.Context, req *api.UpdateCharacterRequest, params api.UpdateCharacterParams) (api.UpdateCharacterRes, error) {
	h.logger.Info("Handling update character", zap.String("character_id", params.CharacterId))

	characterID, err := uuid.Parse(params.CharacterId)
	if err != nil {
		return &api.UpdateCharacterBadRequest{}, nil
	}

	character, err := h.service.UpdateCharacter(ctx, characterID, *req)
	if err != nil {
		h.logger.Error("Failed to update character", zap.Error(err))
		return &api.UpdateCharacterBadRequest{}, nil
	}

	apiChar := api.Character{
		Id:             api.NewOptUUID(character.ID),
		PlayerId:       api.NewOptUUID(character.PlayerID),
		Name:           api.NewOptString(character.Name),
		CharacterClass: api.NewOptString(character.CharacterClass),
		Origin:         api.NewOptString(character.Origin),
		Level:          api.NewOptInt(character.Level),
		Experience:     api.NewOptInt(character.Experience),
		Status:         api.NewOptString(character.Status),
		CreatedAt:      api.NewOptDateTime(character.CreatedAt),
		UpdatedAt:      api.NewOptDateTime(character.UpdatedAt),
	}

	if character.Appearance != nil {
		apiChar.Appearance = character.Appearance
	}
	if character.Attributes != nil {
		apiChar.Attributes = character.Attributes
	}

	response := api.CharacterResponse{
		Character: &apiChar,
	}

	return &api.UpdateCharacterOK{
		Data: response,
	}, nil
}

// DeleteCharacter удаляет персонажа
func (h *Handlers) DeleteCharacter(ctx context.Context, params api.DeleteCharacterParams) (api.DeleteCharacterRes, error) {
	h.logger.Info("Handling delete character", zap.String("character_id", params.CharacterId))

	characterID, err := uuid.Parse(params.CharacterId)
	if err != nil {
		return &api.DeleteCharacterBadRequest{}, nil
	}

	err = h.service.DeleteCharacter(ctx, characterID)
	if err != nil {
		h.logger.Error("Failed to delete character", zap.Error(err))
		return &api.DeleteCharacterBadRequest{}, nil
	}

	return &api.DeleteCharacterOK{}, nil
}

// RestoreCharacter восстанавливает персонажа
func (h *Handlers) RestoreCharacter(ctx context.Context, params api.RestoreCharacterParams) (api.RestoreCharacterRes, error) {
	h.logger.Info("Handling restore character", zap.String("character_id", params.CharacterId))

	characterID, err := uuid.Parse(params.CharacterId)
	if err != nil {
		return &api.RestoreCharacterBadRequest{}, nil
	}

	character, err := h.service.RestoreCharacter(ctx, characterID)
	if err != nil {
		h.logger.Error("Failed to restore character", zap.Error(err))
		return &api.RestoreCharacterBadRequest{}, nil
	}

	apiChar := api.Character{
		Id:             api.NewOptUUID(character.ID),
		PlayerId:       api.NewOptUUID(character.PlayerID),
		Name:           api.NewOptString(character.Name),
		CharacterClass: api.NewOptString(character.CharacterClass),
		Origin:         api.NewOptString(character.Origin),
		Level:          api.NewOptInt(character.Level),
		Experience:     api.NewOptInt(character.Experience),
		Status:         api.NewOptString(character.Status),
		CreatedAt:      api.NewOptDateTime(character.CreatedAt),
		UpdatedAt:      api.NewOptDateTime(character.UpdatedAt),
	}

	if character.Appearance != nil {
		apiChar.Appearance = character.Appearance
	}
	if character.Attributes != nil {
		apiChar.Attributes = character.Attributes
	}

	response := api.CharacterResponse{
		Character: &apiChar,
	}

	return &api.RestoreCharacterOK{
		Data: response,
	}, nil
}

// SwitchCharacter переключает активного персонажа
func (h *Handlers) SwitchCharacter(ctx context.Context, req *api.SwitchCharacterRequest, params api.SwitchCharacterParams) (api.SwitchCharacterRes, error) {
	h.logger.Info("Handling switch character", zap.String("player_id", params.PlayerId))

	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.SwitchCharacterBadRequest{}, nil
	}

	characterID, err := uuid.Parse(req.CharacterId)
	if err != nil {
		return &api.SwitchCharacterBadRequest{}, nil
	}

	err = h.service.SwitchCharacter(ctx, playerID, characterID)
	if err != nil {
		h.logger.Error("Failed to switch character", zap.Error(err))
		return &api.SwitchCharacterBadRequest{}, nil
	}

	activeCharacterID, _ := h.service.repo.GetActiveCharacterID(ctx, playerID)
	var previousID, newID string
	if activeCharacterID != nil {
		newID = activeCharacterID.String()
	}

	response := api.SwitchCharacterResponse{
		PreviousCharacterId: &previousID,
		NewCharacterId:      newID,
		SwitchedAt:          time.Now().Format(time.RFC3339),
	}

	return &api.SwitchCharacterOK{
		Data: response,
	}, nil
}

// GetActiveCharacter получает активного персонажа
func (h *Handlers) GetActiveCharacter(ctx context.Context, params api.GetActiveCharacterParams) (api.GetActiveCharacterRes, error) {
	h.logger.Info("Handling get active character", zap.String("player_id", params.PlayerId))

	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.GetActiveCharacterBadRequest{}, nil
	}

	character, err := h.service.GetActiveCharacter(ctx, playerID)
	if err != nil {
		h.logger.Error("Failed to get active character", zap.Error(err))
		return &api.GetActiveCharacterNotFound{}, nil
	}

	apiChar := api.Character{
		Id:             api.NewOptUUID(character.ID),
		PlayerId:       api.NewOptUUID(character.PlayerID),
		Name:           api.NewOptString(character.Name),
		CharacterClass: api.NewOptString(character.CharacterClass),
		Origin:         api.NewOptString(character.Origin),
		Level:          api.NewOptInt(character.Level),
		Experience:     api.NewOptInt(character.Experience),
		Status:         api.NewOptString(character.Status),
		CreatedAt:      api.NewOptDateTime(character.CreatedAt),
		UpdatedAt:      api.NewOptDateTime(character.UpdatedAt),
	}

	if character.Appearance != nil {
		apiChar.Appearance = character.Appearance
	}
	if character.Attributes != nil {
		apiChar.Attributes = character.Attributes
	}

	response := api.CharacterResponse{
		Character: &apiChar,
	}

	return &api.GetActiveCharacterOK{
		Data: response,
	}, nil
}

// GetPlayerSlots получает слоты персонажей
func (h *Handlers) GetPlayerSlots(ctx context.Context, params api.GetPlayerSlotsParams) (api.GetPlayerSlotsRes, error) {
	h.logger.Info("Handling get player slots", zap.String("player_id", params.PlayerId))

	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.GetPlayerSlotsBadRequest{}, nil
	}

	slots, err := h.service.GetPlayerSlots(ctx, playerID)
	if err != nil {
		h.logger.Error("Failed to get player slots", zap.Error(err))
		return &api.GetPlayerSlotsInternalServerError{}, err
	}

	response := api.PlayerSlotsResponse{
		TotalSlots:      slots.TotalSlots,
		UsedSlots:       slots.UsedSlots,
		AvailableSlots:  slots.AvailableSlots,
		CanPurchaseMore: slots.CanPurchaseMore,
	}

	return &api.GetPlayerSlotsOK{
		Data: response,
	}, nil
}

// PurchaseSlots покупает слоты
func (h *Handlers) PurchaseSlots(ctx context.Context, req *api.PurchaseSlotsRequest, params api.PurchaseSlotsParams) (api.PurchaseSlotsRes, error) {
	h.logger.Info("Handling purchase slots", zap.String("player_id", params.PlayerId))

	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.PurchaseSlotsBadRequest{}, nil
	}

	err = h.service.PurchaseSlots(ctx, playerID, req.SlotsCount)
	if err != nil {
		h.logger.Error("Failed to purchase slots", zap.Error(err))
		if strings.Contains(err.Error(), "maximum slots limit") {
			return &api.PurchaseSlotsBadRequest{}, nil
		}
		return &api.PurchaseSlotsPaymentRequired{}, nil
	}

	response := api.PurchaseSlotsResponse{
		NewTotalSlots:  0, // TODO: вернуть актуальное значение
		PurchasedSlots: req.SlotsCount,
		Cost: &api.PurchaseSlotsResponse_Cost{
			Currency: "eddies",
			Amount:   float64(req.SlotsCount * 100), // 100 eddies per slot
		},
	}

	return &api.PurchaseSlotsOK{
		Data: response,
	}, nil
}

// RecalculateCharacterStats пересчитывает статы
func (h *Handlers) RecalculateCharacterStats(ctx context.Context, params api.RecalculateCharacterStatsParams) (api.RecalculateCharacterStatsRes, error) {
	h.logger.Info("Handling recalculate character stats", zap.String("character_id", params.CharacterId))

	characterID, err := uuid.Parse(params.CharacterId)
	if err != nil {
		return &api.RecalculateCharacterStatsBadRequest{}, nil
	}

	stats, err := h.service.RecalculateCharacterStats(ctx, characterID)
	if err != nil {
		h.logger.Error("Failed to recalculate character stats", zap.Error(err))
		return &api.RecalculateCharacterStatsInternalServerError{}, err
	}

	response := api.CharacterStatsResponse{
		CharacterId: characterID.String(),
		Stats: &api.CharacterStats{
			Health:       api.NewOptInt(stats.Health),
			MaxHealth:    api.NewOptInt(stats.MaxHealth),
			Armor:        api.NewOptInt(stats.Armor),
			ActionPoints: api.NewOptInt(stats.ActionPoints),
			Humanity:     api.NewOptInt(stats.Humanity),
			MaxHumanity:  api.NewOptInt(stats.MaxHumanity),
			Reputation:   api.NewOptInt(stats.Reputation),
			StreetCred:   api.NewOptInt(stats.StreetCred),
		},
		RecalculatedAt: time.Now().Format(time.RFC3339),
	}

	return &api.RecalculateCharacterStatsOK{
		Data: response,
	}, nil
}

// GetCharacterActivity получает активность персонажа
func (h *Handlers) GetCharacterActivity(_ context.Context, params api.GetCharacterActivityParams) (api.GetCharacterActivityRes, error) {
	h.logger.Info("Handling get character activity", zap.String("character_id", params.CharacterId))

	characterID, err := uuid.Parse(params.CharacterId)
	if err != nil {
		return &api.GetCharacterActivityBadRequest{}, nil
	}

	// Заглушка - возвращаем пустой список активности
	response := api.CharacterActivityResponse{
		CharacterId: characterID.String(),
		Activities:  []api.CharacterActivity{},
		TotalCount:  0,
		HasMore:     false,
	}

	return &api.GetCharacterActivityOK{
		Data: response,
	}, nil
}
