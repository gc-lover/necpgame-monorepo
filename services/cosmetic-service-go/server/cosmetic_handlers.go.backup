package server

import (
	"encoding/json"
	"net/http"

	cosmeticapi "github.com/necpgame/cosmetic-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type CosmeticHandlers struct {
	catalogService    *CosmeticCatalogService
	shopService       *CosmeticShopService
	purchaseService   *CosmeticPurchaseService
	equipmentService  *CosmeticEquipmentService
	inventoryService  *CosmeticInventoryService
	logger            *logrus.Logger
}

func NewCosmeticHandlers(
	catalogService *CosmeticCatalogService,
	shopService *CosmeticShopService,
	purchaseService *CosmeticPurchaseService,
	equipmentService *CosmeticEquipmentService,
	inventoryService *CosmeticInventoryService,
	logger *logrus.Logger,
) *CosmeticHandlers {
	return &CosmeticHandlers{
		catalogService:   catalogService,
		shopService:      shopService,
		purchaseService:  purchaseService,
		equipmentService: equipmentService,
		inventoryService: inventoryService,
		logger:           logger,
	}
}

func (h *CosmeticHandlers) GetCosmeticCatalog(w http.ResponseWriter, r *http.Request, params cosmeticapi.GetCosmeticCatalogParams) {
	var categoryPtr *string
	if params.Category != nil {
		category := string(*params.Category)
		categoryPtr = &category
	}

	var rarityPtr *string
	if params.Rarity != nil {
		rarity := string(*params.Rarity)
		rarityPtr = &rarity
	}

	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	catalog, err := h.catalogService.GetCatalog(r.Context(), categoryPtr, rarityPtr, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get catalog")
		h.respondError(w, http.StatusInternalServerError, "failed to get catalog")
		return
	}

	response := convertCosmeticCatalogResponseToAPI(catalog)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetCosmeticCategories(w http.ResponseWriter, r *http.Request) {
	categories, err := h.catalogService.GetCategories(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get categories")
		h.respondError(w, http.StatusInternalServerError, "failed to get categories")
		return
	}

	response := convertCosmeticCategoriesResponseToAPI(categories)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetCosmeticDetails(w http.ResponseWriter, r *http.Request, cosmeticId openapi_types.UUID) {
	cosmetic, err := h.catalogService.GetCosmeticByID(r.Context(), cosmeticId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get cosmetic details")
		h.respondError(w, http.StatusInternalServerError, "failed to get cosmetic details")
		return
	}

	if cosmetic == nil {
		h.respondError(w, http.StatusNotFound, "cosmetic not found")
		return
	}

	response := convertCosmeticItemToAPI(cosmetic)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetDailyShop(w http.ResponseWriter, r *http.Request) {
	dailyShop, err := h.shopService.GetDailyShop(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get daily shop")
		h.respondError(w, http.StatusInternalServerError, "failed to get daily shop")
		return
	}

	response := convertDailyShopResponseToAPI(dailyShop)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetShopHistory(w http.ResponseWriter, r *http.Request, params cosmeticapi.GetShopHistoryParams) {
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	history, err := h.shopService.GetShopHistory(r.Context(), limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get shop history")
		h.respondError(w, http.StatusInternalServerError, "failed to get shop history")
		return
	}

	response := convertShopHistoryResponseToAPI(history)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) PurchaseCosmetic(w http.ResponseWriter, r *http.Request) {
	var req cosmeticapi.PurchaseCosmeticRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	internalReq := convertPurchaseCosmeticRequestFromAPI(&req)
	playerCosmetic, err := h.purchaseService.PurchaseCosmetic(r.Context(), internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to purchase cosmetic")
		h.respondError(w, http.StatusInternalServerError, "failed to purchase cosmetic")
		return
	}

	response := convertPlayerCosmeticToAPI(playerCosmetic)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetPurchaseHistory(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params cosmeticapi.GetPurchaseHistoryParams) {
	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	history, err := h.purchaseService.GetPurchaseHistory(r.Context(), playerId.String(), limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get purchase history")
		h.respondError(w, http.StatusInternalServerError, "failed to get purchase history")
		return
	}

	response := convertPurchaseHistoryResponseToAPI(history)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) EquipCosmetic(w http.ResponseWriter, r *http.Request, cosmeticId openapi_types.UUID) {
	var req cosmeticapi.EquipCosmeticRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	internalReq := convertEquipCosmeticRequestFromAPI(&req)
	equipped, err := h.equipmentService.EquipCosmetic(r.Context(), cosmeticId.String(), internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to equip cosmetic")
		h.respondError(w, http.StatusInternalServerError, "failed to equip cosmetic")
		return
	}

	response := convertEquippedCosmeticsToAPI(equipped)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) UnequipCosmetic(w http.ResponseWriter, r *http.Request, cosmeticId openapi_types.UUID) {
	var req cosmeticapi.UnequipCosmeticRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	internalReq := convertUnequipCosmeticRequestFromAPI(&req)
	err := h.equipmentService.UnequipCosmetic(r.Context(), cosmeticId.String(), internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to unequip cosmetic")
		h.respondError(w, http.StatusInternalServerError, "failed to unequip cosmetic")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "unequipped"})
}

func (h *CosmeticHandlers) GetEquippedCosmetics(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	equipped, err := h.equipmentService.GetEquippedCosmetics(r.Context(), playerId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get equipped cosmetics")
		h.respondError(w, http.StatusInternalServerError, "failed to get equipped cosmetics")
		return
	}

	response := convertEquippedCosmeticsToAPI(equipped)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetCosmeticsByRarity(w http.ResponseWriter, r *http.Request, rarity cosmeticapi.GetCosmeticsByRarityParamsRarity, params cosmeticapi.GetCosmeticsByRarityParams) {
	rarityStr := string(rarity)

	var categoryPtr *string
	if params.Category != nil {
		category := string(*params.Category)
		categoryPtr = &category
	}

	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	catalog, err := h.inventoryService.GetCosmeticsByRarity(r.Context(), rarityStr, categoryPtr, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get cosmetics by rarity")
		h.respondError(w, http.StatusInternalServerError, "failed to get cosmetics by rarity")
		return
	}

	response := convertCosmeticCatalogResponseToAPI(catalog)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetCosmeticInventory(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params cosmeticapi.GetCosmeticInventoryParams) {
	var categoryPtr *string
	if params.Category != nil {
		category := string(*params.Category)
		categoryPtr = &category
	}

	var rarityPtr *string
	if params.Rarity != nil {
		rarity := string(*params.Rarity)
		rarityPtr = &rarity
	}

	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	inventory, err := h.inventoryService.GetInventory(r.Context(), playerId.String(), categoryPtr, rarityPtr, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get inventory")
		h.respondError(w, http.StatusInternalServerError, "failed to get inventory")
		return
	}

	response := convertCosmeticInventoryResponseToAPI(inventory)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) CheckCosmeticOwnership(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params cosmeticapi.CheckCosmeticOwnershipParams) {
	status, err := h.inventoryService.CheckOwnership(r.Context(), playerId.String(), params.CosmeticId.String())
	if err != nil {
		h.logger.WithError(err).Error("Failed to check ownership")
		h.respondError(w, http.StatusInternalServerError, "failed to check ownership")
		return
	}

	response := convertOwnershipStatusResponseToAPI(status)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) GetCosmeticEvents(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params cosmeticapi.GetCosmeticEventsParams) {
	var eventTypePtr *string
	if params.EventType != nil {
		eventType := string(*params.EventType)
		eventTypePtr = &eventType
	}

	limit := 50
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil {
		offset = *params.Offset
	}

	events, err := h.inventoryService.GetEvents(r.Context(), playerId.String(), eventTypePtr, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get events")
		h.respondError(w, http.StatusInternalServerError, "failed to get events")
		return
	}

	response := convertCosmeticEventsResponseToAPI(events)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CosmeticHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *CosmeticHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

