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
	h.respondError(w, http.StatusNotImplemented, "GetCompanionEvents not implemented")
}

func (h *CompanionHandlers) CompanionAttack(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "CompanionAttack not implemented")
}

func (h *CompanionHandlers) CompanionDefend(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "CompanionDefend not implemented")
}

func (h *CompanionHandlers) CustomizeCompanion(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "CustomizeCompanion not implemented")
}

func (h *CompanionHandlers) EquipCompanionItem(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "EquipCompanionItem not implemented")
}














