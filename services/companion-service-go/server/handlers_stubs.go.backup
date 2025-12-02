// Issue: #141886633, #141886669
package server

import (
	"encoding/json"
	"math"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/companion-service-go/models"
	"github.com/necpgame/companion-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *CompanionHandlers) GetCompanionProgression(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	companion, err := h.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion progression")
		return
	}

	if companion == nil {
		h.respondError(w, http.StatusNotFound, "companion not found")
		return
	}

	// Вычисляем опыт до следующего уровня (используем формулу из сервиса)
	baseExp := 100.0
	multiplier := 1.4
	expForNextLevel := int64(baseExp * math.Pow(float64(companion.Level), multiplier))
	expToNextLevel := expForNextLevel - companion.Experience
	if expToNextLevel < 0 {
		expToNextLevel = 0
	}

	health := 0
	damage := 0
	if companion.Stats != nil {
		if h, ok := companion.Stats["health"].(float64); ok {
			health = int(h)
		}
		if d, ok := companion.Stats["damage"].(float64); ok {
			damage = int(d)
		}
	}

	leveledUp := false
	if expToNextLevel <= 0 {
		leveledUp = true
	}

	expInt := int(companion.Experience)
	expToNextInt := int(expToNextLevel)

	response := api.CompanionProgressionResponse{
		CompanionId:           &companionId,
		Level:                 &companion.Level,
		Experience:            &expInt,
		ExperienceToNextLevel: &expToNextInt,
		Health:                &health,
		Damage:                &damage,
		LeveledUp:             &leveledUp,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) GetCompanionShopCatalog(w http.ResponseWriter, r *http.Request, params api.GetCompanionShopCatalogParams) {
	ctx := r.Context()

	var category *models.CompanionCategory
	if params.Category != nil {
		cat := models.CompanionCategory(string(*params.Category))
		category = &cat
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	types, err := h.service.ListCompanionTypes(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list companion types")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion shop catalog")
		return
	}

	apiTypes := make([]api.CompanionType, len(types.Types))
	for i, ct := range types.Types {
		apiTypes[i] = toAPICompanionType(&ct)
	}

	response := api.CompanionShopCatalogResponse{
		Companions: &apiTypes,
		Total:      &types.Total,
		Limit:      &limit,
		Offset:     &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) GetCompanionShopDetails(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionTypeID := uuid.UUID(companionId).String()

	companionType, err := h.service.GetCompanionType(ctx, companionTypeID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion type")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion shop details")
		return
	}

	if companionType == nil {
		h.respondError(w, http.StatusNotFound, "companion type not found")
		return
	}

	response := toAPICompanionType(companionType)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) PurchaseCompanionFromShop(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.PurchaseCompanionFromShopJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)
	companionTypeID := uuid.UUID(req.CompanionTypeId).String()

	companion, err := h.service.PurchaseCompanion(ctx, characterID, companionTypeID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to purchase companion")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	apiCompanion := toAPIPlayerCompanion(companion)
	h.respondJSON(w, http.StatusOK, apiCompanion)
}

func (h *CompanionHandlers) GetCompanionEvents(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID, params api.GetCompanionEventsParams) {
	_ = r.Context()
	_ = uuid.UUID(companionId)

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	// TODO: Реализовать получение событий из БД когда будет таблица companion_events
	// Пока возвращаем пустой список
	events := make([]api.CompanionEvent, 0)
	total := 0

	response := api.CompanionEventsResponse{
		Events: &events,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) CompanionAttack(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.CompanionAttackJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)
	_ = uuid.UUID(req.TargetId)

	companion, err := h.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion")
		return
	}

	if companion == nil {
		h.respondError(w, http.StatusNotFound, "companion not found")
		return
	}

	if companion.CharacterID != characterID {
		h.respondError(w, http.StatusForbidden, "not your companion")
		return
	}

	if companion.Status != models.CompanionStatusSummoned {
		h.respondError(w, http.StatusBadRequest, "companion is not summoned")
		return
	}

	// Вычисляем урон на основе уровня и статов компаньона
	damage := 10
	if companion.Stats != nil {
		if d, ok := companion.Stats["damage"].(float64); ok {
			damage = int(d)
		}
	}
	damage = damage + (companion.Level * 2)

	success := true

	response := api.CompanionAttackResponse{
		Success:     &success,
		TargetId:    &req.TargetId,
		DamageDealt: &damage,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) CompanionDefend(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.CompanionDefendJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	companion, err := h.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion")
		return
	}

	if companion == nil {
		h.respondError(w, http.StatusNotFound, "companion not found")
		return
	}

	if companion.CharacterID != characterID {
		h.respondError(w, http.StatusForbidden, "not your companion")
		return
	}

	if companion.Status != models.CompanionStatusSummoned {
		h.respondError(w, http.StatusBadRequest, "companion is not summoned")
		return
	}

	// Активируем защиту на 30 секунд
	defenseActive := true
	defenseDurationSeconds := 30
	success := true

	response := api.CompanionDefendResponse{
		Success:                &success,
		DefenseActive:          &defenseActive,
		DefenseDurationSeconds: &defenseDurationSeconds,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) CustomizeCompanion(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.CustomizeCompanionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	companion, err := h.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion")
		return
	}

	if companion == nil {
		h.respondError(w, http.StatusNotFound, "companion not found")
		return
	}

	if companion.CharacterID != characterID {
		h.respondError(w, http.StatusForbidden, "not your companion")
		return
	}

	// Обновляем кастомизацию (сохраняем в Equipment или Stats)
	if req.Customization != nil {
		if companion.Equipment == nil {
			companion.Equipment = make(map[string]interface{})
		}
		for key, value := range *req.Customization {
			companion.Equipment[key] = value
		}
	}

	err = h.repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update companion customization")
		h.respondError(w, http.StatusInternalServerError, "failed to customize companion")
		return
	}

	apiCompanion := toAPIPlayerCompanion(companion)
	h.respondJSON(w, http.StatusOK, apiCompanion)
}

func (h *CompanionHandlers) EquipCompanionItem(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.EquipCompanionItemJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)
	itemID := uuid.UUID(req.ItemId)

	companion, err := h.repo.GetPlayerCompanion(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion")
		return
	}

	if companion == nil {
		h.respondError(w, http.StatusNotFound, "companion not found")
		return
	}

	if companion.CharacterID != characterID {
		h.respondError(w, http.StatusForbidden, "not your companion")
		return
	}

	// Экипируем предмет
	if companion.Equipment == nil {
		companion.Equipment = make(map[string]interface{})
	}

	slot := "default"
	if req.Slot != nil {
		slot = *req.Slot
	}

	companion.Equipment[slot] = itemID.String()

	err = h.repo.UpdatePlayerCompanion(ctx, companion)
	if err != nil {
		h.logger.WithError(err).Error("Failed to equip item")
		h.respondError(w, http.StatusInternalServerError, "failed to equip companion item")
		return
	}

	apiCompanion := toAPIPlayerCompanion(companion)
	h.respondJSON(w, http.StatusOK, apiCompanion)
}














