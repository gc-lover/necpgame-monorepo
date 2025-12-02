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

func (h *HousingHandlers) GetHousingShopCatalog(w http.ResponseWriter, r *http.Request, params api.GetHousingShopCatalogParams) {
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
		h.respondError(w, http.StatusInternalServerError, "failed to get housing shop catalog")
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

	response := api.HousingShopCatalogResponse{
		Furniture: &apiItems,
		Total:     &total,
		Limit:     &limit,
		Offset:    &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetHousingShopFurnitureDetails(w http.ResponseWriter, r *http.Request, furnitureId openapi_types.UUID) {
	ctx := r.Context()
	furnitureID := uuid.UUID(furnitureId).String()

	item, err := h.service.GetFurnitureItem(ctx, furnitureID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get furniture item")
		h.respondError(w, http.StatusInternalServerError, "failed to get furniture details")
		return
	}

	if item == nil {
		h.respondError(w, http.StatusNotFound, "furniture not found")
		return
	}

	cat := api.FurnitureItemCategory(item.Category)
	prestigePoints := item.PrestigeValue
	response := api.FurnitureItem{
		Id:             &item.ID,
		Category:       &cat,
		Name:           &item.Name,
		Description:    &item.Description,
		Price:          &item.Price,
		PrestigePoints: &prestigePoints,
		FunctionBonus:  &item.FunctionBonus,
		CreatedAt:      &item.CreatedAt,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) PurchaseFurnitureFromShop(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.PurchaseFurnitureFromShopJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	furnitureItemID := uuid.UUID(req.FurnitureItemId).String()

	// TODO: Реализовать покупку мебели через economy-service
	// Пока возвращаем информацию о мебели
	item, err := h.service.GetFurnitureItem(ctx, furnitureItemID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get furniture item")
		h.respondError(w, http.StatusInternalServerError, "failed to get furniture item")
		return
	}

	if item == nil {
		h.respondError(w, http.StatusNotFound, "furniture not found")
		return
	}

	cat := api.FurnitureItemCategory(item.Category)
	prestigePoints := item.PrestigeValue
	response := api.FurnitureItem{
		Id:             &item.ID,
		Category:       &cat,
		Name:           &item.Name,
		Description:    &item.Description,
		Price:          &item.Price,
		PrestigePoints: &prestigePoints,
		FunctionBonus:  &item.FunctionBonus,
		CreatedAt:      &item.CreatedAt,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetFurnitureShop(w http.ResponseWriter, r *http.Request, params api.GetFurnitureShopParams) {
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
		h.respondError(w, http.StatusInternalServerError, "failed to get furniture shop")
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
	// TODO: Реализовать когда будет таблица player_furniture_inventory
	// Пока возвращаем пустой список
	furniture := make([]api.PlayerFurnitureInventory, 0)
	total := 0

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	response := api.OwnedFurnitureResponse{
		PlayerId: &playerId,
		Furniture: &furniture,
		Total:     &total,
		Limit:     &limit,
		Offset:    &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) PurchaseFurniture(w http.ResponseWriter, r *http.Request) {
	// PurchaseFurniture - алиас для PurchaseFurnitureFromShop
	h.PurchaseFurnitureFromShop(w, r)
}

func (h *HousingHandlers) GetHousingEvents(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetHousingEventsParams) {
	// TODO: Реализовать когда будет таблица housing_events
	// Пока возвращаем пустой список
	events := make([]api.HousingEvent, 0)
	total := 0

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	response := api.HousingEventsResponse{
		Events: &events,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}













