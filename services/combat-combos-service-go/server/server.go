// Package server Issue: #158
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/golang-jwt/jwt/v5"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/models"
)

// CombatCombosServer handles HTTP requests for the combat combos service
type CombatCombosServer struct {
	service   *Service
	router    *chi.Mux
	server    *http.Server
	logger    *zap.Logger
	jwtSecret []byte
}

// NewCombatCombosServer creates a new HTTP server
func NewCombatCombosServer(service *Service, port string, logger *zap.Logger) *CombatCombosServer {
	jwtSecret := []byte("your-jwt-secret-here") // In production, load from config

	server := &CombatCombosServer{
		service:   service,
		logger:    logger,
		jwtSecret: jwtSecret,
	}

	server.setupRouter()
	server.server = &http.Server{
		Addr:         ":" + port,
		Handler:      server.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return server
}

// setupRouter configures the HTTP router with middleware and routes
func (s *CombatCombosServer) setupRouter() {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(s.structuredLogger())
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // In production, specify allowed origins
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health check endpoints
	r.Get("/health", s.healthCheckHandler)
	r.Get("/ready", s.readinessCheckHandler)
	r.Get("/metrics", s.metricsHandler)

	// API routes
	r.Route("/api/v1/combat/combos", func(r chi.Router) {
		r.Use(s.jwtAuthMiddleware)

		// Catalog endpoints
		r.Get("/catalog", s.getComboCatalogHandler)
		r.Get("/{comboID}", s.getComboDetailHandler)

		// Activation endpoints
		r.Post("/activate", s.activateComboHandler)

		// Loadout endpoints
		r.Get("/loadout", s.getComboLoadoutHandler)
		r.Post("/loadout", s.updateComboLoadoutHandler)

		// Analytics endpoints
		r.Get("/analytics", s.getComboAnalyticsHandler)
	})

	s.router = r
}

// Start starts the HTTP server
func (s *CombatCombosServer) Start() error {
	s.logger.Info("Starting HTTP server", zap.String("addr", s.server.Addr))
	return s.server.ListenAndServe()
}

// Shutdown gracefully shuts down the server
func (s *CombatCombosServer) Shutdown(ctx context.Context) error {
	return s.server.Shutdown(ctx)
}

// Middleware

func (s *CombatCombosServer) structuredLogger() func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Create a response writer wrapper to capture status code
			wrapped := &responseWriter{ResponseWriter: w, statusCode: http.StatusOK}

			next.ServeHTTP(wrapped, r)

			s.logger.Info("HTTP Request",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.String("remote", r.RemoteAddr),
				zap.Int("status", wrapped.statusCode),
				zap.Duration("duration", time.Since(start)),
			)
		})
	}
}

func (s *CombatCombosServer) jwtAuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		tokenString := r.Header.Get("Authorization")
		if tokenString == "" {
			s.respondError(w, http.StatusUnauthorized, "Missing authorization header")
			return
		}

		// Remove "Bearer " prefix if present
		if len(tokenString) > 7 && tokenString[:7] == "Bearer " {
			tokenString = tokenString[7:]
		}

		token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
			if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
				return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
			}
			return s.jwtSecret, nil
		})

		if err != nil || !token.Valid {
			s.respondError(w, http.StatusUnauthorized, "Invalid token")
			return
		}

		// Extract claims and add to context if needed
		if claims, ok := token.Claims.(jwt.MapClaims); ok {
			ctx := context.WithValue(r.Context(), "user_id", claims["sub"])
			r = r.WithContext(ctx)
		}

		next.ServeHTTP(w, r)
	})
}

// Health check handlers

func (s *CombatCombosServer) healthCheckHandler(w http.ResponseWriter, _ *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *CombatCombosServer) readinessCheckHandler(w http.ResponseWriter, _ *http.Request) {
	// In production, check database connectivity, etc.
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ready"})
}

func (s *CombatCombosServer) metricsHandler(w http.ResponseWriter, _ *http.Request) {
	// Basic metrics - in production, integrate with Prometheus
	metrics := map[string]interface{}{
		"service": "combat-combos-service-go",
		"version": "1.0.0",
		"uptime":  time.Since(time.Now()).String(), // Simplified
	}
	s.respondJSON(w, http.StatusOK, metrics)
}

// API handlers

func (s *CombatCombosServer) getComboCatalogHandler(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Parse query parameters
	comboTypeStr := r.URL.Query().Get("type")
	var comboType *models.ComboType
	if comboTypeStr != "" {
		ct := models.ComboType(comboTypeStr)
		comboType = &ct
	}

	complexityStr := r.URL.Query().Get("complexity")
	var complexity *models.ComboComplexity
	if complexityStr != "" {
		c := models.ComboComplexity(complexityStr)
		complexity = &c
	}

	limit := 50 // default
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.service.GetComboCatalog(ctx, comboType, complexity, limit, offset)
	if err != nil {
		s.logger.Error("Failed to get combo catalog", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get combo catalog")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CombatCombosServer) getComboDetailHandler(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	comboID := chi.URLParam(r, "comboID")

	response, err := s.service.GetComboDetail(ctx, comboID)
	if err != nil {
		if err.Error() == "combo not found" {
			s.respondError(w, http.StatusNotFound, "Combo not found")
			return
		}
		s.logger.Error("Failed to get combo detail", zap.String("comboID", comboID), zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get combo detail")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CombatCombosServer) activateComboHandler(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req models.ActivateComboRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	// Validate required fields
	if req.ComboID == "" || req.CharacterID == "" {
		s.respondError(w, http.StatusBadRequest, "combo_id and character_id are required")
		return
	}

	response, err := s.service.ActivateCombo(ctx, &req)
	if err != nil {
		s.logger.Error("Failed to activate combo", zap.Error(err))
		if err.Error() == "service is currently overloaded" {
			s.respondError(w, http.StatusServiceUnavailable, "Service temporarily overloaded")
			return
		}
		if err.Error() == "combo requirements not met" {
			s.respondError(w, http.StatusBadRequest, err.Error())
			return
		}
		s.respondError(w, http.StatusInternalServerError, "Failed to activate combo")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CombatCombosServer) getComboLoadoutHandler(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Extract character ID from JWT context (simplified)
	characterID := ctx.Value("user_id").(string)
	if characterID == "" {
		s.respondError(w, http.StatusUnauthorized, "Invalid user context")
		return
	}

	response, err := s.service.GetComboLoadout(ctx, characterID)
	if err != nil {
		s.logger.Error("Failed to get combo loadout", zap.String("characterID", characterID), zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get combo loadout")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CombatCombosServer) updateComboLoadoutHandler(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Extract character ID from JWT context (simplified)
	characterID := ctx.Value("user_id").(string)
	if characterID == "" {
		s.respondError(w, http.StatusUnauthorized, "Invalid user context")
		return
	}

	var req models.ComboLoadoutRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	response, err := s.service.UpdateComboLoadout(ctx, characterID, &req)
	if err != nil {
		s.logger.Error("Failed to update combo loadout", zap.String("characterID", characterID), zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to update combo loadout")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *CombatCombosServer) getComboAnalyticsHandler(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	days := 7 // default
	if daysStr := r.URL.Query().Get("days"); daysStr != "" {
		if d, err := strconv.Atoi(daysStr); err == nil && d > 0 && d <= 90 {
			days = d
		}
	}

	response, err := s.service.GetComboAnalytics(ctx, days)
	if err != nil {
		s.logger.Error("Failed to get combo analytics", zap.Error(err))
		s.respondError(w, http.StatusInternalServerError, "Failed to get combo analytics")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

// Helper methods

func (s *CombatCombosServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *CombatCombosServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

// responseWriter wraps http.ResponseWriter to capture status code
type responseWriter struct {
	http.ResponseWriter
	statusCode int
}

func (rw *responseWriter) WriteHeader(code int) {
	rw.statusCode = code
	rw.ResponseWriter.WriteHeader(code)
}
