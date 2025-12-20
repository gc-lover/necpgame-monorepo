// Package server Issue: #140890170 - Crafting mechanics implementation
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/economy-service-go/models"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

// CraftingHandlers содержит обработчики для крафта
type CraftingHandlers struct {
	craftingService *CraftingService
	logger          *logrus.Logger
}

// NewCraftingHandlers создает новые обработчики крафта
func NewCraftingHandlers(craftingService *CraftingService) *CraftingHandlers {
	return &CraftingHandlers{
		craftingService: craftingService,
		logger:          GetLogger(),
	}
}

// GetRecipeHandler получает рецепт по ID
func (h *CraftingHandlers) GetRecipeHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	recipeID := vars["recipeId"]

	if recipeID == "" {
		h.writeError(w, "recipe ID is required", http.StatusBadRequest)
		return
	}

	recipe, err := h.craftingService.GetRecipe(ctx, recipeID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get recipe")
		h.writeError(w, "failed to get recipe", http.StatusInternalServerError)
		return
	}

	h.writeJSON(w, recipe)
}

// GetRecipesByCategoryHandler получает рецепты по категории
func (h *CraftingHandlers) GetRecipesByCategoryHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	category := r.URL.Query().Get("category")
	if category == "" {
		h.writeError(w, "category parameter is required", http.StatusBadRequest)
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 50 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	recipes, err := h.craftingService.GetRecipesByCategory(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get recipes by category")
		h.writeError(w, "failed to get recipes", http.StatusInternalServerError)
		return
	}

	h.writeJSON(w, map[string]interface{}{
		"recipes": recipes,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"count":  len(recipes),
		},
	})
}

// StartCraftingHandler начинает процесс крафта
func (h *CraftingHandlers) StartCraftingHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	var req struct {
		RecipeID  string                `json:"recipe_id"`
		StationID string                `json:"station_id"`
		Materials []models.UsedMaterial `json:"materials"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "invalid request body", http.StatusBadRequest)
		return
	}

	playerID := h.getPlayerIDFromContext()
	if playerID == "" {
		h.writeError(w, "player authentication required", http.StatusUnauthorized)
		return
	}

	if req.RecipeID == "" || req.StationID == "" {
		h.writeError(w, "recipe_id and station_id are required", http.StatusBadRequest)
		return
	}

	order, err := h.craftingService.StartCrafting(ctx, playerID, req.RecipeID, req.StationID, req.Materials)
	if err != nil {
		h.logger.WithError(err).Error("Failed to start crafting")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, order)
}

// GetPlayerOrdersHandler получает заказы игрока
func (h *CraftingHandlers) GetPlayerOrdersHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	playerID := h.getPlayerIDFromContext()
	if playerID == "" {
		h.writeError(w, "player authentication required", http.StatusUnauthorized)
		return
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 20 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	orders, err := h.craftingService.GetPlayerOrders(ctx, playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player orders")
		h.writeError(w, "failed to get orders", http.StatusInternalServerError)
		return
	}

	h.writeJSON(w, map[string]interface{}{
		"orders": orders,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"count":  len(orders),
		},
	})
}

// GetOrderHandler получает заказ по ID
func (h *CraftingHandlers) GetOrderHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	orderID := vars["orderId"]

	if orderID == "" {
		h.writeError(w, "order ID is required", http.StatusBadRequest)
		return
	}

	playerID := h.getPlayerIDFromContext()
	if playerID == "" {
		h.writeError(w, "player authentication required", http.StatusUnauthorized)
		return
	}

	order, err := h.craftingService.GetOrder(ctx, orderID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get order")
		h.writeError(w, "failed to get order", http.StatusInternalServerError)
		return
	}

	// Проверяем, что заказ принадлежит игроку
	if order.PlayerID != playerID {
		h.writeError(w, "access denied", http.StatusForbidden)
		return
	}

	h.writeJSON(w, order)
}

// CancelOrderHandler отменяет заказ
func (h *CraftingHandlers) CancelOrderHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	orderID := vars["orderId"]

	if orderID == "" {
		h.writeError(w, "order ID is required", http.StatusBadRequest)
		return
	}

	playerID := h.getPlayerIDFromContext()
	if playerID == "" {
		h.writeError(w, "player authentication required", http.StatusUnauthorized)
		return
	}

	if err := h.craftingService.CancelOrder(ctx, orderID, playerID); err != nil {
		h.logger.WithError(err).Error("Failed to cancel order")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, map[string]interface{}{
		"success": true,
		"message": "order cancelled successfully",
	})
}

// CalculateCostHandler рассчитывает стоимость крафта
func (h *CraftingHandlers) CalculateCostHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()
	vars := mux.Vars(r)
	recipeID := vars["recipeId"]

	if recipeID == "" {
		h.writeError(w, "recipe ID is required", http.StatusBadRequest)
		return
	}

	costs, err := h.craftingService.CalculateCraftingCost(ctx, recipeID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to calculate crafting cost")
		h.writeError(w, "failed to calculate cost", http.StatusInternalServerError)
		return
	}

	h.writeJSON(w, map[string]interface{}{
		"recipe_id": recipeID,
		"costs":     costs,
		"timestamp": time.Now().Unix(),
	})
}

// CreateContractHandler создает контракт на крафт
func (h *CraftingHandlers) CreateContractHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	var req struct {
		Title       string `json:"title"`
		Description string `json:"description"`
		ClientID    string `json:"client_id"`
		CrafterID   string `json:"crafter_id"`
		RecipeID    string `json:"recipe_id"`
		Reward      struct {
			Currency string  `json:"currency"`
			Amount   float64 `json:"amount"`
			Bonus    float64 `json:"bonus"`
		} `json:"reward"`
		Deadline *time.Time `json:"deadline"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.writeError(w, "invalid request body", http.StatusBadRequest)
		return
	}

	contract := &models.CraftingContract{
		Title:       req.Title,
		Description: req.Description,
		ClientID:    req.ClientID,
		CrafterID:   req.CrafterID,
		RecipeID:    req.RecipeID,
		Reward: models.ContractReward{
			Currency: req.Reward.Currency,
			Amount:   req.Reward.Amount,
			Bonus:    req.Reward.Bonus,
		},
		Deadline: req.Deadline,
	}

	if err := h.craftingService.CreateContract(ctx, contract); err != nil {
		h.logger.WithError(err).Error("Failed to create contract")
		h.writeError(w, err.Error(), http.StatusBadRequest)
		return
	}

	h.writeJSON(w, contract)
}

// GetContractsHandler получает контракты по статусу
func (h *CraftingHandlers) GetContractsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(r.Context(), 30*time.Second)
	defer cancel()

	status := r.URL.Query().Get("status")
	if status == "" {
		status = "open" // default
	}

	limitStr := r.URL.Query().Get("limit")
	limit := 20 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	contracts, err := h.craftingService.GetContractsByStatus(ctx, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get contracts")
		h.writeError(w, "failed to get contracts", http.StatusInternalServerError)
		return
	}

	h.writeJSON(w, map[string]interface{}{
		"contracts": contracts,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"count":  len(contracts),
		},
	})
}

// Вспомогательные методы

func (h *CraftingHandlers) getPlayerIDFromContext() string {
	// TODO: извлечь player ID из JWT токена или контекста
	// Пока возвращаем заглушку
	return "player-123"
}

func (h *CraftingHandlers) writeJSON(w http.ResponseWriter, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(data)
}

func (h *CraftingHandlers) writeError(w http.ResponseWriter, message string, statusCode int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"error":     true,
		"message":   message,
		"timestamp": time.Now().Unix(),
	})
}
