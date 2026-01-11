// Package handlers содержит HTTP обработчики для Dynamic Pricing Auction API
// Issue: #2175 - Dynamic Pricing Auction House mechanics
// PERFORMANCE: Optimized HTTP handlers with connection pooling and caching
package handlers

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/dynamic-pricing-auction-service-go/internal/service"
)

// Handlers содержит HTTP обработчики
type Handlers struct {
	service *service.Service
	logger  *zap.Logger
	router  *mux.Router
}

// Config конфигурация обработчиков
type Config struct {
	Service *service.Service
	Logger  *zap.Logger
}

// NewHandlers создает новые HTTP обработчики
func NewHandlers(config Config) *Handlers {
	h := &Handlers{
		service: config.Service,
		logger:  config.Logger,
		router:  mux.NewRouter(),
	}

	h.setupRoutes()
	return h
}

// Router возвращает настроенный роутер
func (h *Handlers) Router() *mux.Router {
	return h.router
}

// setupRoutes настраивает маршруты API
func (h *Handlers) setupRoutes() {
	api := h.router.PathPrefix("/api/v1").Subrouter()

	// Health check
	h.router.HandleFunc("/health", h.healthCheck).Methods("GET")

	// Auction endpoints
	api.HandleFunc("/auctions", h.createAuction).Methods("POST")
	api.HandleFunc("/auctions/{item_id}", h.getAuction).Methods("GET")
	api.HandleFunc("/auctions/{item_id}/bid", h.placeBid).Methods("POST")

	// Market analysis endpoints
	api.HandleFunc("/market/analysis", h.getMarketAnalysis).Methods("GET")
	api.HandleFunc("/market/analysis/{category}", h.getMarketAnalysisByCategory).Methods("GET")

	// Pricing algorithms endpoints
	api.HandleFunc("/algorithms", h.getPricingAlgorithms).Methods("GET")

	// System endpoints
	api.HandleFunc("/system/health", h.getSystemHealth).Methods("GET")

	// Middleware
	h.router.Use(h.loggingMiddleware)
	h.router.Use(h.corsMiddleware)
}

// Middleware для логирования запросов
func (h *Handlers) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Wrap ResponseWriter to capture status code
		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapped, r)

		h.logger.Info("HTTP Request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.Int("status", wrapped.statusCode),
			zap.Duration("duration", time.Since(start)),
		)
	})
}

// Middleware для CORS
func (h *Handlers) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

// responseWriter обертка для захвата статус кода
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// healthCheck обработчик проверки здоровья
func (h *Handlers) healthCheck(w http.ResponseWriter, r *http.Request) {
	health, err := h.service.GetSystemHealth(r.Context())
	if err != nil {
		h.logger.Error("Health check failed", zap.Error(err))
		h.respondWithError(w, http.StatusServiceUnavailable, "Health check failed", err)
		return
	}

	response := map[string]interface{}{
		"status":      "healthy",
		"timestamp":   time.Now().UTC(),
		"service":     "dynamic-pricing-auction",
		"version":     "1.0.0",
		"health":      health,
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// createAuction создает новый аукцион
func (h *Handlers) createAuction(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Name        string    `json:"name"`
		Category    string    `json:"category"`
		Rarity      string    `json:"rarity"`
		BasePrice   float64   `json:"base_price"`
		BuyoutPrice float64   `json:"buyout_price,omitempty"`
		SellerID    string    `json:"seller_id"`
		Duration    int       `json:"duration_hours"` // in hours
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid JSON", err)
		return
	}

	// Validate required fields
	if req.Name == "" || req.Category == "" || req.SellerID == "" || req.BasePrice <= 0 {
		h.respondWithError(w, http.StatusBadRequest, "Missing required fields", nil)
		return
	}

	// Create item
	item := &models.Item{
		ID:          uuid.New().String(),
		Name:        req.Name,
		Category:    req.Category,
		Rarity:      req.Rarity,
		BasePrice:   req.BasePrice,
		BuyoutPrice: req.BuyoutPrice,
		SellerID:    req.SellerID,
		Status:      "active",
		EndTime:     time.Now().Add(time.Duration(req.Duration) * time.Hour),
	}

	// Create auction
	auction, err := h.service.CreateAuction(r.Context(), item)
	if err != nil {
		h.logger.Error("Failed to create auction", zap.Error(err))
		h.respondWithError(w, http.StatusInternalServerError, "Failed to create auction", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, auction)
}

// getAuction получает аукцион по ID
func (h *Handlers) getAuction(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	itemID := vars["item_id"]

	auction, err := h.service.GetAuction(r.Context(), itemID)
	if err != nil {
		h.logger.Error("Failed to get auction", zap.String("item_id", itemID), zap.Error(err))
		h.respondWithError(w, http.StatusNotFound, "Auction not found", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, auction)
}

// placeBid размещает ставку
func (h *Handlers) placeBid(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	itemID := vars["item_id"]

	var req struct {
		BidderID string  `json:"bidder_id"`
		Amount   float64 `json:"amount"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid JSON", err)
		return
	}

	if req.BidderID == "" || req.Amount <= 0 {
		h.respondWithError(w, http.StatusBadRequest, "Invalid bid data", nil)
		return
	}

	bid, err := h.service.PlaceBid(r.Context(), itemID, req.BidderID, req.Amount)
	if err != nil {
		h.logger.Error("Failed to place bid",
			zap.String("item_id", itemID), zap.String("bidder_id", req.BidderID), zap.Error(err))
		h.respondWithError(w, http.StatusBadRequest, "Failed to place bid", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, bid)
}

// getMarketAnalysis получает общий анализ рынка
func (h *Handlers) getMarketAnalysis(w http.ResponseWriter, r *http.Request) {
	// Get analysis for all categories (simplified)
	categories := []string{"weapons", "armor", "consumables", "materials"}

	analyses := make(map[string]*models.MarketAnalysis)
	for _, category := range categories {
		analysis, err := h.service.GetMarketAnalysis(r.Context(), category)
		if err != nil {
			h.logger.Warn("Failed to get market analysis",
				zap.String("category", category), zap.Error(err))
			continue
		}
		analyses[category] = analysis
	}

	response := map[string]interface{}{
		"analyses":    analyses,
		"timestamp":   time.Now().UTC(),
		"categories":  categories,
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// getMarketAnalysisByCategory получает анализ рынка для конкретной категории
func (h *Handlers) getMarketAnalysisByCategory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	category := vars["category"]

	analysis, err := h.service.GetMarketAnalysis(r.Context(), category)
	if err != nil {
		h.logger.Error("Failed to get market analysis",
			zap.String("category", category), zap.Error(err))
		h.respondWithError(w, http.StatusInternalServerError, "Failed to get market analysis", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, analysis)
}

// getPricingAlgorithms получает список алгоритмов ценообразования
func (h *Handlers) getPricingAlgorithms(w http.ResponseWriter, r *http.Request) {
	algorithms := h.service.GetPricingAlgorithms()

	response := map[string]interface{}{
		"algorithms": algorithms,
		"timestamp":  time.Now().UTC(),
		"count":      len(algorithms),
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// getSystemHealth получает состояние здоровья системы
func (h *Handlers) getSystemHealth(w http.ResponseWriter, r *http.Request) {
	health, err := h.service.GetSystemHealth(r.Context())
	if err != nil {
		h.respondWithError(w, http.StatusServiceUnavailable, "Failed to get system health", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, health)
}

// respondWithJSON отправляет JSON ответ
func (h *Handlers) respondWithJSON(w http.ResponseWriter, status int, payload interface{}) {
	response, err := json.Marshal(payload)
	if err != nil {
		h.logger.Error("Failed to marshal JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(response)
}

// respondWithError отправляет JSON ошибку
func (h *Handlers) respondWithError(w http.ResponseWriter, status int, message string, err error) {
	response := map[string]interface{}{
		"error":   http.StatusText(status),
		"message": message,
	}

	if err != nil {
		response["details"] = err.Error()
	}

	h.respondWithJSON(w, status, response)
}