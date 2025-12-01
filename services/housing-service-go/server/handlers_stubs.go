// Issue: #141886468
package server

import (
	"net/http"

	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetHousingEvents(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetHousingEventsParams) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingEvents not implemented")
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

func (h *HousingHandlers) GetHousingShopCatalog(w http.ResponseWriter, r *http.Request, params api.GetHousingShopCatalogParams) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingShopCatalog not implemented")
}

func (h *HousingHandlers) GetHousingShopFurnitureDetails(w http.ResponseWriter, r *http.Request, furnitureId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetHousingShopFurnitureDetails not implemented")
}

func (h *HousingHandlers) PurchaseFurnitureFromShop(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "PurchaseFurnitureFromShop not implemented")
}













