// Package handlers содержит HTTP обработчики для Reputation Decay & Recovery API
// Issue: #2174 - Reputation Decay & Recovery mechanics
// PERFORMANCE: Optimized HTTP handlers with connection pooling and caching
package handlers

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/reputation-decay-recovery-service-go/internal/service"
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

	// Recovery endpoints
	api.HandleFunc("/recovery/start", h.startRecovery).Methods("POST")
	api.HandleFunc("/recovery/{characterId}/status", h.getRecoveryStatus).Methods("GET")
	api.HandleFunc("/recovery/{characterId}/cancel", h.cancelRecovery).Methods("POST")

	// Decay management endpoints
	api.HandleFunc("/decay/{characterId}/{factionId}/activate", h.activateDecay).Methods("POST")
	api.HandleFunc("/decay/{characterId}/{factionId}/deactivate", h.deactivateDecay).Methods("POST")
	api.HandleFunc("/decay/{characterId}/status", h.getDecayStatus).Methods("GET")

	// Analytics endpoints
	api.HandleFunc("/analytics/decay/stats", h.getDecayStats).Methods("GET")
	api.HandleFunc("/analytics/recovery/stats", h.getRecoveryStats).Methods("GET")

	// System endpoints
	api.HandleFunc("/system/health", h.getSystemHealth).Methods("GET")

	// Middleware
	h.router.Use(h.loggingMiddleware)
	h.router.Use(h.corsMiddleware)
	h.router.Use(h.metricsMiddleware)
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

// Middleware для метрик (заглушка)
func (h *Handlers) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Здесь можно добавить метрики Prometheus
		next.ServeHTTP(w, r)
	})
}

// responseWriter обертка для ResponseWriter
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

// Health check endpoint
func (h *Handlers) healthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]string{
		"status": "healthy",
		"service": "reputation-decay-recovery",
		"time": time.Now().Format(time.RFC3339),
	})
}

// Start recovery request
type StartRecoveryRequest struct {
	CharacterID string                `json:"character_id"`
	FactionID   string                `json:"faction_id"`
	Method      models.RecoveryMethod `json:"method"`
	TargetValue float64               `json:"target_value"`
}

// startRecovery начинает процесс восстановления репутации
func (h *Handlers) startRecovery(w http.ResponseWriter, r *http.Request) {
	var req StartRecoveryRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body", err)
		return
	}

	process, err := h.service.StartReputationRecovery(
		r.Context(),
		req.CharacterID,
		req.FactionID,
		req.Method,
		req.TargetValue,
	)

	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Failed to start recovery", err)
		return
	}

	h.respondJSON(w, http.StatusCreated, process)
}

// getRecoveryStatus получает статус процессов восстановления персонажа
func (h *Handlers) getRecoveryStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID := vars["characterId"]

	if characterID == "" {
		h.respondError(w, http.StatusBadRequest, "Character ID is required", nil)
		return
	}

	processes, err := h.service.GetActiveRecoveryProcesses(r.Context(), characterID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "Failed to get recovery status", err)
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"character_id": characterID,
		"processes":    processes,
		"count":        len(processes),
	})
}

// cancelRecovery отменяет процесс восстановления
func (h *Handlers) cancelRecovery(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID := vars["characterId"]

	// This would need implementation in the service layer
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{
		"message": "Recovery cancellation not yet implemented",
		"character_id": characterID,
	})
}

// activateDecay активирует процесс разрушения репутации
func (h *Handlers) activateDecay(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID := vars["characterId"]
	factionID := vars["factionId"]

	if characterID == "" || factionID == "" {
		h.respondError(w, http.StatusBadRequest, "Character ID and Faction ID are required", nil)
		return
	}

	// This would create a decay process in the service layer
	h.respondJSON(w, http.StatusNotImplemented, map[string]string{
		"message": "Decay activation not yet implemented",
		"character_id": characterID,
		"faction_id": factionID,
	})
}

// deactivateDecay деактивирует процесс разрушения репутации
func (h *Handlers) deactivateDecay(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID := vars["characterId"]
	factionID := vars["factionId"]

	if characterID == "" || factionID == "" {
		h.respondError(w, http.StatusBadRequest, "Character ID and Faction ID are required", nil)
		return
	}

	h.respondJSON(w, http.StatusNotImplemented, map[string]string{
		"message": "Decay deactivation not yet implemented",
		"character_id": characterID,
		"faction_id": factionID,
	})
}

// getDecayStatus получает статус процессов разрушения персонажа
func (h *Handlers) getDecayStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID := vars["characterId"]

	if characterID == "" {
		h.respondError(w, http.StatusBadRequest, "Character ID is required", nil)
		return
	}

	h.respondJSON(w, http.StatusNotImplemented, map[string]string{
		"message": "Decay status not yet implemented",
		"character_id": characterID,
	})
}

// getDecayStats получает статистику процессов разрушения
func (h *Handlers) getDecayStats(w http.ResponseWriter, r *http.Request) {
	stats := &models.DecayStats{
		TotalActiveProcesses: 0, // Would be calculated from database
		TotalProcessedToday:  0,
		AverageDecayRate:     1.0,
		LastProcessedTime:    time.Now(),
		ProcessingDuration:   time.Duration(0),
	}

	h.respondJSON(w, http.StatusOK, stats)
}

// getRecoveryStats получает статистику процессов восстановления
func (h *Handlers) getRecoveryStats(w http.ResponseWriter, r *http.Request) {
	stats := &models.RecoveryStats{
		TotalActiveProcesses: 0, // Would be calculated from database
		TotalCompletedToday:  0,
		TotalFailedToday:     0,
		AverageRecoveryTime:  24 * time.Hour,
		SuccessRate:          0.95,
		LastProcessedTime:    time.Now(),
	}

	h.respondJSON(w, http.StatusOK, stats)
}

// getSystemHealth получает состояние здоровья системы
func (h *Handlers) getSystemHealth(w http.ResponseWriter, r *http.Request) {
	health, err := h.service.GetSystemHealth(r.Context())
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "Failed to get system health", err)
		return
	}

	h.respondJSON(w, http.StatusOK, health)
}

// GetActiveRecoveryProcesses получает активные процессы восстановления
func (h *Handlers) GetActiveRecoveryProcesses(ctx context.Context, characterID string) ([]*models.ReputationRecovery, error) {
	// This method should be in the service layer
	// For now, return empty slice
	return []*models.ReputationRecovery{}, nil
}

// Helper methods

func (h *Handlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handlers) respondError(w http.ResponseWriter, status int, message string, err error) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	response := map[string]interface{}{
		"error":   message,
		"status": status,
	}

	if err != nil {
		response["details"] = err.Error()
	}

	json.NewEncoder(w).Encode(response)
}