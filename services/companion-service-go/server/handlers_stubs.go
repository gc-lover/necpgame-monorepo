package server

import (
	"net/http"

	"github.com/necpgame/companion-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *CompanionHandlers) GetCompanionEvents(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID, params api.GetCompanionEventsParams) {
	h.respondError(w, http.StatusNotImplemented, "GetCompanionEvents not implemented")
}

func (h *CompanionHandlers) GetCompanionShopCatalog(w http.ResponseWriter, r *http.Request, params api.GetCompanionShopCatalogParams) {
	h.respondError(w, http.StatusNotImplemented, "GetCompanionShopCatalog not implemented")
}

func (h *CompanionHandlers) PurchaseCompanionFromShop(w http.ResponseWriter, r *http.Request) {
	h.respondError(w, http.StatusNotImplemented, "PurchaseCompanionFromShop not implemented")
}

func (h *CompanionHandlers) GetCompanionShopDetails(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetCompanionShopDetails not implemented")
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

func (h *CompanionHandlers) GetCompanionProgression(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "GetCompanionProgression not implemented")
}



