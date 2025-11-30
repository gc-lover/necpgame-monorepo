package server

import (
	"net/http"

	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetApartmentBonuses(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetApartmentBonuses not implemented")
}

func (h *HousingHandlers) UpdateFurniturePosition(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "UpdateFurniturePosition not implemented")
}

func (h *HousingHandlers) MoveFurniture(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "MoveFurniture not implemented")
}

func (h *HousingHandlers) RemoveGuest(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.RemoveGuestParams) {
	h.respondError(w, http.StatusNotImplemented, "RemoveGuest not implemented")
}

func (h *HousingHandlers) GetApartmentGuests(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetApartmentGuests not implemented")
}

func (h *HousingHandlers) AddGuest(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "AddGuest not implemented")
}

func (h *HousingHandlers) GetApartmentPrestige(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetApartmentPrestige not implemented")
}

func (h *HousingHandlers) GetApartmentVisits(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetApartmentVisitsParams) {
	h.respondError(w, http.StatusNotImplemented, "GetApartmentVisits not implemented")
}

func (h *HousingHandlers) GetHousingEvents(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetHousingEventsParams) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingEvents not implemented")
}

func (h *HousingHandlers) GetFurnitureCatalog(w http.ResponseWriter, r *http.Request, params api.GetFurnitureCatalogParams) {
	h.respondError(w, http.StatusNotImplemented, "GetFurnitureCatalog not implemented")
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
	h.respondError(w, http.StatusNotImplemented, "GetPlayerVisits not implemented")
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












