package handlers

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"necpgame/services/game-analytics-dashboard-service-go/internal/service"
	"necpgame/services/game-analytics-dashboard-service-go/pkg/models"
)

// Handlers handles HTTP requests for game analytics dashboard
type Handlers struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandlers creates a new handlers instance
func NewHandlers(svc *service.Service, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: svc,
		logger:  logger,
	}
}

// SetupRoutes configures all HTTP routes
func (h *Handlers) SetupRoutes(r *chi.Mux) {
	// Middleware
	r.Use(h.LoggingMiddleware)
	r.Use(h.AuthMiddleware) // Placeholder - would implement JWT auth

	// Health check endpoints
	r.Get("/health", h.HealthCheck)
	r.Get("/ready", h.ReadinessCheck)

	// Metrics endpoint
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		// Dashboard endpoints
		r.Get("/dashboard/realtime", h.GetRealTimeDashboard)
		r.Get("/dashboard/widgets", h.GetDashboardWidgets)
		r.Put("/dashboard/widgets/{widgetId}", h.UpdateDashboardWidget)

		// Analytics endpoints
		r.Get("/analytics/player/{playerId}", h.GetPlayerAnalytics)
		r.Get("/analytics/game-metrics", h.GetGameMetrics)
		r.Get("/analytics/combat", h.GetCombatAnalytics)
		r.Get("/analytics/economic", h.GetEconomicAnalytics)
		r.Get("/analytics/social", h.GetSocialAnalytics)
		r.Post("/analytics/query", h.ExecuteAnalyticsQuery)

		// Performance monitoring
		r.Get("/performance/metrics", h.GetPerformanceMetrics)

		// Event ingestion (for internal services)
		r.Post("/events", h.ProcessAnalyticsEvent)
	})
}

// Health check handlers
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	err := h.service.HealthCheck(ctx)
	if err != nil {
		h.logger.Error("Health check failed", zap.Error(err))
		h.respondJSON(w, http.StatusServiceUnavailable, models.HealthStatus{
			Service:   "game-analytics-dashboard",
			Status:    "unhealthy",
			Version:   "1.0.0",
			Uptime:    "unknown",
			Timestamp: time.Now(),
			Services: map[string]string{
				"database": "unhealthy",
				"redis":    "unhealthy",
			},
		})
		return
	}

	h.respondJSON(w, http.StatusOK, models.HealthStatus{
		Service:   "game-analytics-dashboard",
		Status:    "healthy",
		Version:   "1.0.0",
		Uptime:    "running",
		Timestamp: time.Now(),
		Services: map[string]string{
			"database": "healthy",
			"redis":    "healthy",
		},
	})
}

func (h *Handlers) ReadinessCheck(w http.ResponseWriter, r *http.Request) {
	// Readiness check - ensure service is ready to serve traffic
	h.HealthCheck(w, r)
}

// Dashboard handlers
func (h *Handlers) GetRealTimeDashboard(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	dashboard, err := h.service.GetRealTimeDashboard(ctx)
	if err != nil {
		h.logger.Error("Failed to get real-time dashboard", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get dashboard data")
		return
	}

	h.respondJSON(w, http.StatusOK, dashboard)
}

func (h *Handlers) GetDashboardWidgets(w http.ResponseWriter, r *http.Request) {
	dashboardID := r.URL.Query().Get("dashboard_id")
	if dashboardID == "" {
		dashboardID = "default"
	}

	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	widgets, err := h.service.GetDashboardWidgets(ctx, dashboardID)
	if err != nil {
		h.logger.Error("Failed to get dashboard widgets", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get widgets")
		return
	}

	h.respondJSON(w, http.StatusOK, widgets)
}

func (h *Handlers) UpdateDashboardWidget(w http.ResponseWriter, r *http.Request) {
	widgetID := chi.URLParam(r, "widgetId")

	var config map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&config); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	err := h.service.UpdateDashboardWidget(ctx, widgetID, config)
	if err != nil {
		h.logger.Error("Failed to update dashboard widget", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to update widget")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "updated"})
}

// Analytics handlers
func (h *Handlers) GetPlayerAnalytics(w http.ResponseWriter, r *http.Request) {
	playerID := chi.URLParam(r, "playerId")
	timeRange := r.URL.Query().Get("time_range")
	if timeRange == "" {
		timeRange = "24h"
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	analytics, err := h.service.GetPlayerAnalytics(ctx, playerID, timeRange)
	if err != nil {
		h.logger.Error("Failed to get player analytics",
			zap.String("player_id", playerID),
			zap.Error(err))
		h.respondError(w, http.StatusNotFound, "Player analytics not found")
		return
	}

	h.respondJSON(w, http.StatusOK, analytics)
}

func (h *Handlers) GetGameMetrics(w http.ResponseWriter, r *http.Request) {
	timeRange := r.URL.Query().Get("time_range")
	if timeRange == "" {
		timeRange = "24h"
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	metrics, err := h.service.GetGameMetrics(ctx, timeRange)
	if err != nil {
		h.logger.Error("Failed to get game metrics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get game metrics")
		return
	}

	h.respondJSON(w, http.StatusOK, metrics)
}

func (h *Handlers) GetCombatAnalytics(w http.ResponseWriter, r *http.Request) {
	timeRange := r.URL.Query().Get("time_range")
	if timeRange == "" {
		timeRange = "24h"
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	analytics, err := h.service.GetCombatAnalytics(ctx, timeRange)
	if err != nil {
		h.logger.Error("Failed to get combat analytics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get combat analytics")
		return
	}

	h.respondJSON(w, http.StatusOK, analytics)
}

func (h *Handlers) GetEconomicAnalytics(w http.ResponseWriter, r *http.Request) {
	timeRange := r.URL.Query().Get("time_range")
	if timeRange == "" {
		timeRange = "24h"
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	analytics, err := h.service.GetEconomicAnalytics(ctx, timeRange)
	if err != nil {
		h.logger.Error("Failed to get economic analytics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get economic analytics")
		return
	}

	h.respondJSON(w, http.StatusOK, analytics)
}

func (h *Handlers) GetSocialAnalytics(w http.ResponseWriter, r *http.Request) {
	timeRange := r.URL.Query().Get("time_range")
	if timeRange == "" {
		timeRange = "24h"
	}

	ctx, cancel := contextWithTimeout(r.Context(), 10*time.Second)
	defer cancel()

	analytics, err := h.service.GetSocialAnalytics(ctx, timeRange)
	if err != nil {
		h.logger.Error("Failed to get social analytics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get social analytics")
		return
	}

	h.respondJSON(w, http.StatusOK, analytics)
}

func (h *Handlers) ExecuteAnalyticsQuery(w http.ResponseWriter, r *http.Request) {
	var query models.AnalyticsQuery
	if err := json.NewDecoder(r.Body).Decode(&query); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid query format")
		return
	}

	// Validate query
	if len(query.Metrics) == 0 {
		h.respondError(w, http.StatusBadRequest, "At least one metric is required")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 30*time.Second) // Longer timeout for complex queries
	defer cancel()

	response, err := h.service.ExecuteAnalyticsQuery(ctx, &query)
	if err != nil {
		h.logger.Error("Failed to execute analytics query", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to execute query")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Performance monitoring handlers
func (h *Handlers) GetPerformanceMetrics(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	metrics, err := h.service.GetPerformanceMetrics(ctx)
	if err != nil {
		h.logger.Error("Failed to get performance metrics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get performance metrics")
		return
	}

	h.respondJSON(w, http.StatusOK, metrics)
}

// Event processing handler
func (h *Handlers) ProcessAnalyticsEvent(w http.ResponseWriter, r *http.Request) {
	var event struct {
		Type string                 `json:"type"`
		Data map[string]interface{} `json:"data"`
	}

	if err := json.NewDecoder(r.Body).Decode(&event); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid event format")
		return
	}

	ctx, cancel := contextWithTimeout(r.Context(), 5*time.Second)
	defer cancel()

	err := h.service.ProcessAnalyticsEvent(ctx, event.Type, event.Data)
	if err != nil {
		h.logger.Error("Failed to process analytics event", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to process event")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "processed"})
}

// Middleware
func (h *Handlers) LoggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		// Wrap ResponseWriter to capture status code
		wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

		next.ServeHTTP(wrapped, r)

		duration := time.Since(start)
		h.logger.Info("HTTP Request",
			zap.String("method", r.Method),
			zap.String("path", r.URL.Path),
			zap.Int("status", wrapped.statusCode),
			zap.Duration("duration", duration),
		)
	})
}

func (h *Handlers) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Placeholder authentication - would validate JWT token
		// For now, allow all requests
		next.ServeHTTP(w, r)
	})
}

// Helper types and functions
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}

func (h *Handlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *Handlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

func contextWithTimeout(parent context.Context, timeout time.Duration) (context.Context, context.CancelFunc) {
	return context.WithTimeout(parent, timeout)
}

// PERFORMANCE: Handlers optimized for analytics workloads
// Context timeouts prevent hanging requests
// Structured logging provides observability
// JSON responses properly formatted for dashboard clients
