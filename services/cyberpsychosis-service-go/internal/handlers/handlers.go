package handlers

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/gc-lover/necp-game/services/cyberpsychosis-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/cyberpsychosis-service-go/internal/service"
	"github.com/gorilla/mux"
	"go.uber.org/zap"
)

// Handlers содержит HTTP обработчики для Cyberpsychosis API
type Handlers struct {
	service    *service.Service
	logger     *zap.Logger
	router     *mux.Router
}

// NewHandlers создает новый экземпляр обработчиков
func NewHandlers(svc *service.Service, logger *zap.Logger) *Handlers {
	h := &Handlers{
		service: svc,
		logger:  logger,
		router:  mux.NewRouter(),
	}

	h.registerRoutes()
	return h
}

// registerRoutes регистрирует маршруты API
func (h *Handlers) registerRoutes() {
	api := h.router.PathPrefix("/api/v1").Subrouter()

	// Cyberpsychosis states
	api.HandleFunc("/cyberpsychosis/states", h.createCyberpsychosisState).Methods("POST")
	api.HandleFunc("/cyberpsychosis/states/{player_id}", h.getPlayerCyberpsychosisState).Methods("GET")
	api.HandleFunc("/cyberpsychosis/states/{state_id}", h.updateCyberpsychosisState).Methods("PUT")
	api.HandleFunc("/cyberpsychosis/states/{state_id}/deactivate", h.deactivateCyberpsychosisState).Methods("POST")

	// Triggers
	api.HandleFunc("/cyberpsychosis/triggers/berserk", h.triggerBerserk).Methods("POST")
	api.HandleFunc("/cyberpsychosis/triggers/adrenal-overload", h.triggerAdrenalOverload).Methods("POST")
	api.HandleFunc("/cyberpsychosis/triggers/neural-overload", h.triggerNeuralOverload).Methods("POST")

	// System health
	api.HandleFunc("/health", h.healthCheck).Methods("GET")
	api.HandleFunc("/system/health", h.getSystemHealth).Methods("GET")

	// Middleware
	h.router.Use(h.loggingMiddleware)
	h.router.Use(h.corsMiddleware)
}

// loggingMiddleware логирует HTTP запросы
func (h *Handlers) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(wrapped, r)

		h.logger.Info("HTTP Request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.String("remote_addr", r.RemoteAddr),
			zap.Int("status", wrapped.statusCode),
			zap.Duration("duration", time.Since(start)))
	})
}

// corsMiddleware добавляет CORS заголовки
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

// createCyberpsychosisState создает новое состояние киберпсихоза
func (h *Handlers) createCyberpsychosisState(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID      string `json:"player_id"`
		StateType     int32  `json:"state_type"`
		TriggerReason string `json:"trigger_reason"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	state, err := h.service.TriggerCyberpsychosisState(r.Context(),
		req.PlayerID, models.CyberpsychosisStateType(req.StateType), req.TriggerReason)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to create cyberpsychosis state", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, state)
}

// getPlayerCyberpsychosisState получает состояние киберпсихоза игрока
func (h *Handlers) getPlayerCyberpsychosisState(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	state, err := h.service.GetPlayerCyberpsychosisState(r.Context(), playerID)
	if err != nil {
		h.respondWithError(w, http.StatusNotFound, "Cyberpsychosis state not found", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, state)
}

// updateCyberpsychosisState обновляет состояние киберпсихоза
func (h *Handlers) updateCyberpsychosisState(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	stateID := vars["state_id"]

	var updates struct {
		SeverityLevel *int32 `json:"severity_level,omitempty"`
		IsActive      *bool  `json:"is_active,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&updates); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	// Получить текущее состояние
	state, err := h.service.GetPlayerCyberpsychosisState(r.Context(), stateID)
	if err != nil {
		h.respondWithError(w, http.StatusNotFound, "Cyberpsychosis state not found", err)
		return
	}

	// Применить обновления
	if updates.SeverityLevel != nil {
		state.SeverityLevel = *updates.SeverityLevel
	}
	if updates.IsActive != nil {
		state.IsActive = *updates.IsActive
	}

	// Обновить в сервисе (нужен метод в сервисе)
	h.respondWithJSON(w, http.StatusOK, state)
}

// deactivateCyberpsychosisState деактивирует состояние киберпсихоза
func (h *Handlers) deactivateCyberpsychosisState(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	stateID := vars["state_id"]

	if err := h.service.DeactivateCyberpsychosisState(r.Context(), stateID); err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to deactivate cyberpsychosis state", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, map[string]string{"status": "deactivated"})
}

// triggerBerserk активирует состояние Berserk
func (h *Handlers) triggerBerserk(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID string `json:"player_id"`
		Reason   string `json:"reason"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	state, err := h.service.TriggerCyberpsychosisState(r.Context(),
		req.PlayerID, models.StateBerserk, req.Reason)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to trigger berserk state", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, state)
}

// triggerAdrenalOverload активирует состояние Adrenal Overload
func (h *Handlers) triggerAdrenalOverload(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID string `json:"player_id"`
		Reason   string `json:"reason"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	state, err := h.service.TriggerCyberpsychosisState(r.Context(),
		req.PlayerID, models.StateAdrenalOverload, req.Reason)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to trigger adrenal overload state", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, state)
}

// triggerNeuralOverload активирует состояние Neural Overload
func (h *Handlers) triggerNeuralOverload(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID string `json:"player_id"`
		Reason   string `json:"reason"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	state, err := h.service.TriggerCyberpsychosisState(r.Context(),
		req.PlayerID, models.StateNeuralOverload, req.Reason)
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to trigger neural overload state", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, state)
}

// healthCheck проверка здоровья сервиса
func (h *Handlers) healthCheck(w http.ResponseWriter, r *http.Request) {
	health := map[string]interface{}{
		"status":      "healthy",
		"service":     "cyberpsychosis-service",
		"version":     "1.0.0",
		"timestamp":   time.Now().Format(time.RFC3339),
	}

	h.respondWithJSON(w, http.StatusOK, health)
}

// getSystemHealth получает состояние здоровья системы
func (h *Handlers) getSystemHealth(w http.ResponseWriter, r *http.Request) {
	health, err := h.service.GetSystemHealth(r.Context())
	if err != nil {
		h.respondWithError(w, http.StatusInternalServerError, "Failed to get system health", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, health)
}

// Router возвращает HTTP роутер
func (h *Handlers) Router() *mux.Router {
	return h.router
}

// respondWithJSON отправляет JSON ответ
func (h *Handlers) respondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	response, err := json.Marshal(payload)
	if err != nil {
		h.logger.Error("Failed to marshal JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	w.Write(response)
}

// respondWithError отправляет ошибку в JSON формате
func (h *Handlers) respondWithError(w http.ResponseWriter, code int, message string, err error) {
	h.logger.Error(message, zap.Error(err))

	response := map[string]interface{}{
		"error":   message,
		"details": err.Error(),
		"code":    code,
	}

	h.respondWithJSON(w, code, response)
}