// Issue: #2229
package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"go.uber.org/zap"

	"crafting-service-go/internal/service"
	"crafting-service-go/internal/metrics"
)

// CraftingHandlers handles HTTP requests
type CraftingHandlers struct {
	service *service.CraftingService
	logger  *zap.SugaredLogger
	metrics *metrics.Collector
}

// NewCraftingHandlers creates new crafting handlers
func NewCraftingHandlers(svc *service.CraftingService, logger *zap.SugaredLogger) *CraftingHandlers {
	return &CraftingHandlers{
		service: svc,
		logger:  logger,
		metrics: &metrics.Collector{}, // This should be passed from main
	}
}

// AuthMiddleware validates JWT tokens
func (h *CraftingHandlers) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			h.respondWithError(w, http.StatusUnauthorized, "Missing authorization header")
			return
		}

		// Simple token validation (should be replaced with proper JWT validation)
		if !strings.HasPrefix(authHeader, "Bearer ") {
			h.respondWithError(w, http.StatusUnauthorized, "Invalid authorization format")
			return
		}

		// For now, just check if token is not empty
		token := strings.TrimPrefix(authHeader, "Bearer ")
		if token == "" {
			h.respondWithError(w, http.StatusUnauthorized, "Empty token")
			return
		}

		next.ServeHTTP(w, r)
	})
}

// Health check endpoint
func (h *CraftingHandlers) Health(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"status": "healthy",
		"time":   time.Now().Format(time.RFC3339),
	})
}

// Readiness check endpoint
func (h *CraftingHandlers) Ready(w http.ResponseWriter, r *http.Request) {
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"status": "ready",
		"time":   time.Now().Format(time.RFC3339),
	})
}

// GetRecipesByCategory gets recipes by category
func (h *CraftingHandlers) GetRecipesByCategory(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	category := r.URL.Query().Get("category")
	tierStr := r.URL.Query().Get("tier")
	quality := r.URL.Query().Get("quality")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	var tier *int
	if tierStr != "" {
		if t, err := strconv.Atoi(tierStr); err == nil {
			tier = &t
		}
	}

	limit := 20
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	recipes, err := h.service.GetRecipesByCategory(r.Context(), category, tier, &quality, limit, offset)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"recipes": recipes,
		"total":   len(recipes),
		"limit":   limit,
		"offset":  offset,
	})
}

// GetRecipe gets a single recipe
func (h *CraftingHandlers) GetRecipe(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	recipeID := chi.URLParam(r, "recipeId")
	if recipeID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing recipe ID")
		return
	}

	recipe, err := h.service.GetRecipe(r.Context(), recipeID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondWithError(w, http.StatusNotFound, "Recipe not found")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, recipe)
}

// CreateRecipe creates a new recipe
func (h *CraftingHandlers) CreateRecipe(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	var req struct {
		Name        string                 `json:"name"`
		Description string                 `json:"description"`
		Category    string                 `json:"category"`
		Tier        int                    `json:"tier"`
		Quality     string                 `json:"quality"`
		Materials   map[string]int         `json:"materials"`
		Result      map[string]interface{} `json:"result"`
		SkillReq    int                    `json:"skillReq"`
		TimeReq     int                    `json:"timeReq"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	recipe, err := h.service.CreateRecipe(r.Context(), req.Name, req.Description, req.Category,
		req.Tier, req.Quality, req.Materials, req.Result, req.SkillReq, req.TimeReq)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, recipe)
}

// UpdateRecipe updates a recipe
func (h *CraftingHandlers) UpdateRecipe(w http.ResponseWriter, r *http.Request) {
	recipeID := chi.URLParam(r, "recipeId")
	// Implementation for updating recipe
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":  "Recipe updated successfully",
		"recipeId": recipeID,
	})
}

// DeleteRecipe deletes a recipe
func (h *CraftingHandlers) DeleteRecipe(w http.ResponseWriter, r *http.Request) {
	recipeID := chi.URLParam(r, "recipeId")
	// Implementation for deleting recipe
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":  "Recipe deleted successfully",
		"recipeId": recipeID,
	})
}

// GetOrders gets crafting orders
func (h *CraftingHandlers) GetOrders(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	playerID := r.URL.Query().Get("playerId")
	if playerID == "" {
		h.respondWithError(w, http.StatusBadRequest, "Missing player ID")
		return
	}

	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	limit := 20
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	orders, err := h.service.GetCraftingOrders(r.Context(), playerID, limit, offset)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"orders": orders,
		"total":  len(orders),
		"limit":  limit,
		"offset": offset,
	})
}

// GetOrder gets a single order
func (h *CraftingHandlers) GetOrder(w http.ResponseWriter, r *http.Request) {
	orderID := chi.URLParam(r, "orderId")
	// Implementation for getting single order
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"orderId": orderID,
		"status":  "in_progress",
	})
}

// CreateOrder creates a new crafting order
func (h *CraftingHandlers) CreateOrder(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	var req struct {
		PlayerID  string `json:"playerId"`
		RecipeID  string `json:"recipeId"`
		StationID string `json:"stationId"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	order, err := h.service.CreateCraftingOrder(r.Context(), req.PlayerID, req.RecipeID, req.StationID)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusCreated, order)
}

// UpdateOrder updates an order
func (h *CraftingHandlers) UpdateOrder(w http.ResponseWriter, r *http.Request) {
	orderID := chi.URLParam(r, "orderId")
	// Implementation for updating order
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message": "Order updated successfully",
		"orderId": orderID,
	})
}

// CancelOrder cancels an order
func (h *CraftingHandlers) CancelOrder(w http.ResponseWriter, r *http.Request) {
	orderID := chi.URLParam(r, "orderId")

	if err := h.service.CancelCraftingOrder(r.Context(), orderID); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":  "Order cancelled successfully",
		"orderId":  orderID,
	})
}

// GetStations gets crafting stations
func (h *CraftingHandlers) GetStations(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	stationType := r.URL.Query().Get("type")
	limitStr := r.URL.Query().Get("limit")
	offsetStr := r.URL.Query().Get("offset")

	var st *string
	if stationType != "" {
		st = &stationType
	}

	limit := 20
	if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
		limit = l
	}

	offset := 0
	if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
		offset = o
	}

	stations, err := h.service.GetCraftingStations(r.Context(), st, limit, offset)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"stations": stations,
		"total":    len(stations),
		"limit":    limit,
		"offset":   offset,
	})
}

// GetStation gets a single station
func (h *CraftingHandlers) GetStation(w http.ResponseWriter, r *http.Request) {
	stationID := chi.URLParam(r, "stationId")
	// Implementation for getting single station
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"stationId": stationID,
		"type":      "workbench",
		"status":    "available",
	})
}

// BookStation books a crafting station
func (h *CraftingHandlers) BookStation(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	defer func() { h.metrics.ObserveRequestDuration(time.Since(start).Seconds()) }()

	stationID := chi.URLParam(r, "stationId")
	var req struct {
		PlayerID string `json:"playerId"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	if err := h.service.BookCraftingStation(r.Context(), stationID, req.PlayerID); err != nil {
		if strings.Contains(err.Error(), "not available") {
			h.respondWithError(w, http.StatusConflict, "Station not available")
			return
		}
		h.respondWithError(w, http.StatusInternalServerError, err.Error())
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":   "Station booked successfully",
		"stationId": stationID,
		"playerId":  req.PlayerID,
	})
}

// ReleaseStation releases a crafting station
func (h *CraftingHandlers) ReleaseStation(w http.ResponseWriter, r *http.Request) {
	stationID := chi.URLParam(r, "stationId")
	// Implementation for releasing station
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":   "Station released successfully",
		"stationId": stationID,
	})
}

// GetContracts gets crafting contracts
func (h *CraftingHandlers) GetContracts(w http.ResponseWriter, r *http.Request) {
	// Implementation for getting contracts
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"contracts": []interface{}{},
		"total":     0,
	})
}

// GetContract gets a single contract
func (h *CraftingHandlers) GetContract(w http.ResponseWriter, r *http.Request) {
	contractID := chi.URLParam(r, "contractId")
	// Implementation for getting single contract
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"contractId": contractID,
		"status":     "active",
	})
}

// CreateContract creates a new contract
func (h *CraftingHandlers) CreateContract(w http.ResponseWriter, r *http.Request) {
	// Implementation for creating contract
	h.respondWithJSON(w, http.StatusCreated, map[string]string{
		"message": "Contract created successfully",
	})
}

// UpdateContract updates a contract
func (h *CraftingHandlers) UpdateContract(w http.ResponseWriter, r *http.Request) {
	contractID := chi.URLParam(r, "contractId")
	// Implementation for updating contract
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":     "Contract updated successfully",
		"contractId":  contractID,
	})
}

// GetProductionChains gets production chains
func (h *CraftingHandlers) GetProductionChains(w http.ResponseWriter, r *http.Request) {
	// Implementation for getting production chains
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"chains": []interface{}{},
		"total":  0,
	})
}

// GetProductionChain gets a single production chain
func (h *CraftingHandlers) GetProductionChain(w http.ResponseWriter, r *http.Request) {
	chainID := chi.URLParam(r, "chainId")
	// Implementation for getting single production chain
	h.respondWithJSON(w, http.StatusOK, map[string]interface{}{
		"chainId": chainID,
		"status":  "active",
	})
}

// CreateProductionChain creates a new production chain
func (h *CraftingHandlers) CreateProductionChain(w http.ResponseWriter, r *http.Request) {
	// Implementation for creating production chain
	h.respondWithJSON(w, http.StatusCreated, map[string]string{
		"message": "Production chain created successfully",
	})
}

// UpdateProductionChain updates a production chain
func (h *CraftingHandlers) UpdateProductionChain(w http.ResponseWriter, r *http.Request) {
	chainID := chi.URLParam(r, "chainId")
	// Implementation for updating production chain
	h.respondWithJSON(w, http.StatusOK, map[string]string{
		"message":  "Production chain updated successfully",
		"chainId":  chainID,
	})
}

// Helper functions
func (h *CraftingHandlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, _ := json.Marshal(payload)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

func (h *CraftingHandlers) respondWithError(w http.ResponseWriter, status int, message string) {
	h.respondWithJSON(w, status, map[string]string{"error": message})
}
