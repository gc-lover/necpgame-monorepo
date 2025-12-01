// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/models"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetApartmentBonuses(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	detail, err := h.service.GetApartmentDetail(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment detail")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment bonuses")
		return
	}

	bonuses := make(map[string]interface{})
	if detail.FunctionalBonuses != nil {
		bonuses = detail.FunctionalBonuses
	}

	response := api.ApartmentBonusesResponse{
		ApartmentId: &apartmentId,
		Bonuses: &struct {
			CRAFTSPEED           *float32 `json:"CRAFT_SPEED,omitempty"`
			HUMANITYREGENERATION *float32 `json:"HUMANITY_REGENERATION,omitempty"`
			STORAGESLOTS         *int     `json:"STORAGE_SLOTS,omitempty"`
			WEAPONSTORAGE        *int     `json:"WEAPON_STORAGE,omitempty"`
		}{},
	}

	if craftSpeed, ok := bonuses["CRAFT_SPEED"].(float64); ok {
		cs := float32(craftSpeed)
		response.Bonuses.CRAFTSPEED = &cs
	}
	if humanityRegen, ok := bonuses["HUMANITY_REGENERATION"].(float64); ok {
		hr := float32(humanityRegen)
		response.Bonuses.HUMANITYREGENERATION = &hr
	}
	if storageSlots, ok := bonuses["STORAGE_SLOTS"].(float64); ok {
		ss := int(storageSlots)
		response.Bonuses.STORAGESLOTS = &ss
	}
	if weaponStorage, ok := bonuses["WEAPON_STORAGE"].(float64); ok {
		ws := int(weaponStorage)
		response.Bonuses.WEAPONSTORAGE = &ws
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) UpdateFurniturePosition(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)
	furnitureID := uuid.UUID(furnitureId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.UpdateFurniturePositionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := make(map[string]interface{})
	if req.PositionX != nil || req.PositionY != nil || req.PositionZ != nil {
		if req.PositionX != nil {
			position["x"] = *req.PositionX
		}
		if req.PositionY != nil {
			position["y"] = *req.PositionY
		}
		if req.PositionZ != nil {
			position["z"] = *req.PositionZ
		}
	}

	rotation := make(map[string]interface{})
	if req.RotationYaw != nil {
		rotation["yaw"] = *req.RotationYaw
	}

	scale := make(map[string]interface{})
	if req.Scale != nil {
		scale["uniform"] = *req.Scale
	}

	var posMap, rotMap, scaleMap map[string]interface{}
	if len(position) > 0 {
		posMap = position
	}
	if len(rotation) > 0 {
		rotMap = rotation
	}
	if len(scale) > 0 {
		scaleMap = scale
	}

	furniture, err := h.service.UpdateFurniturePosition(ctx, apartmentID, furnitureID, posMap, rotMap, scaleMap)
	if err != nil {
		h.logger.WithError(err).WithFields(map[string]interface{}{
			"apartment_id": apartmentID,
			"furniture_id": furnitureID,
			"character_id":  characterID,
		}).Error("Failed to update furniture position")
		h.respondError(w, http.StatusInternalServerError, "failed to update furniture position")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIPlacedFurniture(furniture))
}

func (h *HousingHandlers) MoveFurniture(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)
	furnitureID := uuid.UUID(furnitureId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.MoveFurnitureJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := make(map[string]interface{})
	if req.PositionX != nil || req.PositionY != nil || req.PositionZ != nil {
		if req.PositionX != nil {
			position["x"] = *req.PositionX
		}
		if req.PositionY != nil {
			position["y"] = *req.PositionY
		}
		if req.PositionZ != nil {
			position["z"] = *req.PositionZ
		}
	}

	rotation := make(map[string]interface{})
	if req.RotationX != nil || req.RotationY != nil || req.RotationZ != nil {
		if req.RotationX != nil {
			rotation["x"] = *req.RotationX
		}
		if req.RotationY != nil {
			rotation["y"] = *req.RotationY
		}
		if req.RotationZ != nil {
			rotation["z"] = *req.RotationZ
		}
	}

	scale := make(map[string]interface{})
	if req.ScaleX != nil || req.ScaleY != nil || req.ScaleZ != nil {
		if req.ScaleX != nil {
			scale["x"] = *req.ScaleX
		}
		if req.ScaleY != nil {
			scale["y"] = *req.ScaleY
		}
		if req.ScaleZ != nil {
			scale["z"] = *req.ScaleZ
		}
	}

	var posMap, rotMap, scaleMap map[string]interface{}
	if len(position) > 0 {
		posMap = position
	}
	if len(rotation) > 0 {
		rotMap = rotation
	}
	if len(scale) > 0 {
		scaleMap = scale
	}

	furniture, err := h.service.UpdateFurniturePosition(ctx, apartmentID, furnitureID, posMap, rotMap, scaleMap)
	if err != nil {
		h.logger.WithError(err).WithFields(map[string]interface{}{
			"apartment_id": apartmentID,
			"furniture_id": furnitureID,
			"character_id":  characterID,
		}).Error("Failed to move furniture")
		h.respondError(w, http.StatusInternalServerError, "failed to move furniture")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIPlacedFurniture(furniture))
}

func (h *HousingHandlers) RemoveGuest(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.RemoveGuestParams) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	guestID := uuid.UUID(params.PlayerId)
	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	// Удаляем гостя из списка
	newGuests := make([]uuid.UUID, 0, len(apartment.Guests))
	for _, g := range apartment.Guests {
		if g != guestID {
			newGuests = append(newGuests, g)
		}
	}

	modelReq := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		Guests:      newGuests,
	}

	err = h.service.UpdateApartmentSettings(ctx, apartmentID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove guest")
		h.respondError(w, http.StatusInternalServerError, "failed to remove guest")
		return
	}

	response := map[string]interface{}{
		"apartment_id": apartmentId,
		"player_id":    params.PlayerId,
		"status":       "success",
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetApartmentGuests(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment guests")
		return
	}

	guests := make([]openapi_types.UUID, len(apartment.Guests))
	for i, g := range apartment.Guests {
		guests[i] = openapi_types.UUID(g)
	}

	response := map[string]interface{}{
		"apartment_id": apartmentId,
		"guests":       guests,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) AddGuest(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.AddGuestJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	guestID := uuid.UUID(req.CharacterId)
	modelReq := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		Guests:      []uuid.UUID{guestID},
	}

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	// Добавляем гостя, если его еще нет
	found := false
	for _, g := range apartment.Guests {
		if g == guestID {
			found = true
			break
		}
	}
	if !found {
		apartment.Guests = append(apartment.Guests, guestID)
		modelReq.Guests = apartment.Guests
		err = h.service.UpdateApartmentSettings(ctx, apartmentID, modelReq)
		if err != nil {
			h.logger.WithError(err).Error("Failed to add guest")
			h.respondError(w, http.StatusInternalServerError, "failed to add guest")
			return
		}
	}

	response := map[string]interface{}{
		"apartment_id": apartmentId,
		"character_id": req.CharacterId,
		"status":       "success",
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetApartmentPrestige(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment prestige")
		return
	}

	prestigeScore := apartment.PrestigeScore
	basePrestige := 0
	furniturePrestige := 0
	uniquenessBonus := 0
	locationMultiplier := float32(1.0)

	response := api.ApartmentPrestigeResponse{
		ApartmentId:        &apartmentId,
		PrestigeScore:      &prestigeScore,
		BasePrestige:       &basePrestige,
		FurniturePrestige:  &furniturePrestige,
		UniquenessBonus:    &uniquenessBonus,
		LocationMultiplier: &locationMultiplier,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetApartmentVisits(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetApartmentVisitsParams) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	visits, total, err := h.service.GetApartmentVisits(ctx, apartmentID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment visits")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment visits")
		return
	}

	apiVisits := make([]api.ApartmentVisit, len(visits))
	for i, v := range visits {
		apiID := openapi_types.UUID(v.ID)
		apiApartmentID := openapi_types.UUID(v.ApartmentID)
		apiVisitorID := openapi_types.UUID(v.VisitorID)
		apiVisits[i] = api.ApartmentVisit{
			Id:          &apiID,
			ApartmentId: &apiApartmentID,
			VisitorId:   &apiVisitorID,
			VisitedAt:   &v.VisitedAt,
		}
	}

	response := api.ApartmentVisitsResponse{
		ApartmentId: &apartmentId,
		Visits:      &apiVisits,
		Total:       &total,
		Limit:       &limit,
		Offset:      &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetHousingEvents(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetHousingEventsParams) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingEvents not implemented")
}

func (h *HousingHandlers) GetFurnitureCatalog(w http.ResponseWriter, r *http.Request, params api.GetFurnitureCatalogParams) {
	ctx := r.Context()

	var category *models.FurnitureCategory
	if params.Category != nil {
		cat := models.FurnitureCategory(string(*params.Category))
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

	items, total, err := h.service.ListFurnitureItems(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list furniture items")
		h.respondError(w, http.StatusInternalServerError, "failed to get furniture catalog")
		return
	}

	apiItems := make([]api.FurnitureItem, len(items))
	for i, item := range items {
		cat := api.FurnitureItemCategory(item.Category)
		prestigePoints := item.PrestigeValue
		apiItems[i] = api.FurnitureItem{
			Id:             &item.ID,
			Category:       &cat,
			Name:           &item.Name,
			Description:    &item.Description,
			Price:          &item.Price,
			PrestigePoints: &prestigePoints,
			FunctionBonus:  &item.FunctionBonus,
			CreatedAt:      &item.CreatedAt,
		}
	}

	response := api.FurnitureCatalogResponse{
		Furniture: &apiItems,
		Total:     &total,
		Limit:     &limit,
		Offset:    &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetOwnedFurniture(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetOwnedFurnitureParams) {
	h.respondError(w, http.StatusNotImplemented, "GetOwnedFurniture not implemented")
}

func (h *HousingHandlers) PurchaseFurniture(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "PurchaseFurniture not implemented")
}

func (h *HousingHandlers) GetFurnitureShop(w http.ResponseWriter, r *http.Request, params api.GetFurnitureShopParams) {
	h.respondError(w, http.StatusNotImplemented, "GetFurnitureShop not implemented")
}

func (h *HousingHandlers) GetPlayerBonuses(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetPlayerBonuses not implemented")
}

func (h *HousingHandlers) GetPlayerVisits(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetPlayerVisitsParams) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	visits, total, err := h.service.GetPlayerVisits(ctx, playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player visits")
		h.respondError(w, http.StatusInternalServerError, "failed to get player visits")
		return
	}

	apiVisits := make([]api.ApartmentVisit, len(visits))
	for i, v := range visits {
		apiID := openapi_types.UUID(v.ID)
		apiApartmentID := openapi_types.UUID(v.ApartmentID)
		apiVisitorID := openapi_types.UUID(v.VisitorID)
		apiVisits[i] = api.ApartmentVisit{
			Id:          &apiID,
			ApartmentId: &apiApartmentID,
			VisitorId:   &apiVisitorID,
			VisitedAt:   &v.VisitedAt,
		}
	}

	response := api.ApartmentVisitsResponse{
		Visits: &apiVisits,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetHousingShopCatalog(w http.ResponseWriter, r *http.Request, params api.GetHousingShopCatalogParams) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingShopCatalog not implemented")
}

func (h *HousingHandlers) GetHousingShopFurnitureDetails(w http.ResponseWriter, r *http.Request, furnitureId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingShopFurnitureDetails not implemented")
}

func (h *HousingHandlers) PurchaseFurnitureFromShop(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "PurchaseFurnitureFromShop not implemented")
}













