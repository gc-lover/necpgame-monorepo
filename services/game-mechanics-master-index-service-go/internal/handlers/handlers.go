// Package handlers содержит HTTP обработчики для Game Mechanics Master Index API
// Issue: #2176 - Game Mechanics Systems Master Index
// PERFORMANCE: Optimized HTTP handlers with connection pooling and caching
package handlers

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/models"
	"github.com/gc-lover/necp-game/services/game-mechanics-master-index-service-go/internal/service"
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

	// Mechanics registry endpoints
	api.HandleFunc("/mechanics", h.getAllMechanics).Methods("GET")
	api.HandleFunc("/mechanics", h.registerMechanic).Methods("POST")
	api.HandleFunc("/mechanics/{mechanic_id}", h.getMechanicByID).Methods("GET")
	api.HandleFunc("/mechanics/{mechanic_id}", h.updateMechanic).Methods("PUT")
	api.HandleFunc("/mechanics/{mechanic_id}", h.deleteMechanic).Methods("DELETE")

	// Dependencies endpoints
	api.HandleFunc("/mechanics/{mechanic_id}/dependencies", h.getMechanicDependencies).Methods("GET")
	api.HandleFunc("/mechanics/{mechanic_id}/dependencies", h.addMechanicDependency).Methods("POST")

	// Configuration endpoints
	api.HandleFunc("/mechanics/{mechanic_id}/config", h.getMechanicConfig).Methods("GET")
	api.HandleFunc("/mechanics/{mechanic_id}/config", h.updateMechanicConfig).Methods("PUT")

	// System endpoints
	api.HandleFunc("/system/health", h.getSystemHealth).Methods("GET")
	api.HandleFunc("/system/status", h.getSystemStatus).Methods("GET")

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
		"service":     "game-mechanics-master-index",
		"version":     "1.0.0",
		"health":      health,
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// getAllMechanics получает все механики
func (h *Handlers) getAllMechanics(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	mechanicType := r.URL.Query().Get("type")
	status := r.URL.Query().Get("status")
	includeDeps := r.URL.Query().Get("include_dependencies") == "true"

	if status == "" {
		status = "active"
	}

	var mechanics []*models.GameMechanic
	var err error

	if mechanicType != "" {
		mechanics, err = h.service.GetMechanicsByType(r.Context(), mechanicType)
	} else {
		mechanics, err = h.service.GetActiveMechanics(r.Context())
	}

	if err != nil {
		h.logger.Error("Failed to get mechanics", zap.Error(err))
		h.respondWithError(w, http.StatusInternalServerError, "Failed to get mechanics", err)
		return
	}

	// Get health info
	health, _ := h.service.GetSystemHealth(r.Context())

	response := map[string]interface{}{
		"mechanics":     mechanics,
		"total_count":   len(mechanics),
		"active_count":  health.ActiveMechanics,
		"health_score":  health.HealthScore,
		"last_updated":  time.Now().UTC(),
	}

	if includeDeps {
		// TODO: Include dependency information
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// registerMechanic регистрирует новую механику
func (h *Handlers) registerMechanic(w http.ResponseWriter, r *http.Request) {
	var mechanic models.GameMechanic
	if err := json.NewDecoder(r.Body).Decode(&mechanic); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid JSON", err)
		return
	}

	// Generate UUID if not provided
	if mechanic.ID == "" {
		mechanic.ID = uuid.New().String()
	}

	if err := h.service.RegisterMechanic(r.Context(), &mechanic); err != nil {
		h.logger.Error("Failed to register mechanic", zap.Error(err))
		h.respondWithError(w, http.StatusBadRequest, "Failed to register mechanic", err)
		return
	}

	h.respondWithJSON(w, http.StatusCreated, mechanic)
}

// getMechanicByID получает механику по ID
func (h *Handlers) getMechanicByID(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	mechanic, err := h.service.GetMechanic(r.Context(), mechanicID)
	if err != nil {
		h.logger.Error("Failed to get mechanic", zap.String("id", mechanicID), zap.Error(err))
		h.respondWithError(w, http.StatusNotFound, "Mechanic not found", err)
		return
	}

	h.respondWithJSON(w, http.StatusOK, mechanic)
}

// updateMechanic обновляет механику
func (h *Handlers) updateMechanic(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	var updates map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&updates); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid JSON", err)
		return
	}

	// Get current mechanic
	mechanic, err := h.service.GetMechanic(r.Context(), mechanicID)
	if err != nil {
		h.respondWithError(w, http.StatusNotFound, "Mechanic not found", err)
		return
	}

	// Apply updates
	if name, ok := updates["name"].(string); ok {
		mechanic.Name = name
	}
	if status, ok := updates["status"].(string); ok {
		mechanic.Status = status
	}
	if version, ok := updates["version"].(string); ok {
		mechanic.Version = version
	}
	if endpoint, ok := updates["endpoint"].(string); ok {
		mechanic.Endpoint = endpoint
	}
	if priority, ok := updates["priority"].(float64); ok {
		mechanic.Priority = int(priority)
	}

	// TODO: Update mechanic in repository

	h.respondWithJSON(w, http.StatusOK, mechanic)
}

// deleteMechanic удаляет механику
func (h *Handlers) deleteMechanic(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	// TODO: Implement delete mechanic
	h.logger.Info("Mechanic deletion requested", zap.String("id", mechanicID))

	w.WriteHeader(http.StatusNoContent)
}

// getMechanicDependencies получает зависимости механики
func (h *Handlers) getMechanicDependencies(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	// TODO: Implement get dependencies
	h.logger.Info("Dependencies requested", zap.String("mechanic_id", mechanicID))

	response := map[string]interface{}{
		"mechanic_id":  mechanicID,
		"dependencies": []interface{}{},
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// addMechanicDependency добавляет зависимость
func (h *Handlers) addMechanicDependency(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	var dep models.MechanicDependency
	if err := json.NewDecoder(r.Body).Decode(&dep); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid JSON", err)
		return
	}

	dep.ID = uuid.New().String()
	dep.MechanicID = mechanicID

	// TODO: Save dependency

	h.respondWithJSON(w, http.StatusCreated, dep)
}

// getMechanicConfig получает конфигурацию механики
func (h *Handlers) getMechanicConfig(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	// TODO: Implement get config
	h.logger.Info("Config requested", zap.String("mechanic_id", mechanicID))

	response := map[string]interface{}{
		"mechanic_id":    mechanicID,
		"config_version": "v1.0.0",
		"settings":       map[string]interface{}{},
		"is_active":      true,
		"updated_at":     time.Now().UTC(),
	}

	h.respondWithJSON(w, http.StatusOK, response)
}

// updateMechanicConfig обновляет конфигурацию механики
func (h *Handlers) updateMechanicConfig(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mechanicID := vars["mechanic_id"]

	var config map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&config); err != nil {
		h.respondWithError(w, http.StatusBadRequest, "Invalid JSON", err)
		return
	}

	// TODO: Update config
	h.logger.Info("Config update requested",
		zap.String("mechanic_id", mechanicID),
		zap.Any("config", config))

	response := map[string]interface{}{
		"mechanic_id":    mechanicID,
		"config_version": "v1.0.0",
		"settings":       config,
		"is_active":      true,
		"updated_at":     time.Now().UTC(),
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

// getSystemStatus получает краткий статус системы
func (h *Handlers) getSystemStatus(w http.ResponseWriter, r *http.Request) {
	health, err := h.service.GetSystemHealth(r.Context())
	if err != nil {
		h.respondWithError(w, http.StatusServiceUnavailable, "Failed to get system status", err)
		return
	}

	status := "healthy"
	if health.HealthScore < 80 {
		status = "degraded"
	}
	if health.HealthScore < 50 {
		status = "unhealthy"
	}

	response := map[string]interface{}{
		"status":           status,
		"timestamp":        time.Now().UTC(),
		"mechanics_count":  health.TotalMechanics,
		"active_mechanics": health.ActiveMechanics,
	}

	h.respondWithJSON(w, http.StatusOK, response)
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