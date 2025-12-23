// Issue: Implement admin-service-go based on OpenAPI specification
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"
	"admin-service-go/server/internal/models"
)

// Router creates and configures the HTTP router with admin-specific middleware
func (s *AdminService) Router() http.Handler {
	r := chi.NewRouter()

	// CORS middleware for admin panel access
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"https://admin.necpgame.com", "http://localhost:3000"}, // Admin panel origins
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	}))

	// Security middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second)) // Admin operations can be complex

	// Rate limiting for admin endpoints (stricter than user endpoints)
	r.Use(s.rateLimitMiddleware())

	// Authentication middleware for protected routes
	r.Use(s.authMiddleware())

	// Health check endpoint (public)
	r.Get("/health", s.handleHealth)

	// Metrics endpoint for monitoring
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1/admin", func(r chi.Router) {
		// System management
		r.Get("/system/health", s.handleGetSystemHealth)
		r.Get("/system/metrics", s.handleGetSystemMetrics)

		// Session management
		r.Get("/sessions", s.handleGetActiveSessions)

		// User management
		r.Post("/users/{userID}/ban", s.handleBanUser)
		r.Delete("/users/{userID}/ban", s.handleUnbanUser)
		r.Get("/users/{userID}", s.handleGetUserDetails)

		// Content moderation
		r.Get("/content", s.handleGetContentModerationQueue)
		r.Post("/content/{contentID}/moderate", s.handleModerateContent)

		// Audit logging (critical for compliance)
		r.Get("/audit", s.handleGetAuditLog)

		// Configuration management
		r.Get("/config", s.handleGetSystemConfig)
		r.Put("/config", s.handleUpdateSystemConfig)
	})

	return r
}


// authMiddleware validates JWT tokens and admin permissions
func (s *AdminService) authMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// Skip auth for health checks
			if r.URL.Path == "/health" || r.URL.Path == "/metrics" {
				next.ServeHTTP(w, r)
				return
			}

			// Extract JWT token from Authorization header
			authHeader := r.Header.Get("Authorization")
			if authHeader == "" {
				s.writeError(w, http.StatusUnauthorized, "missing authorization header")
				return
			}

			// TODO: Validate JWT token and extract admin user
			// For now, mock authentication
			adminUser := &models.AdminUser{
				ID:          uuid.New(),
				Username:    "admin",
				Role:        "super_admin",
				Permissions: []string{"read", "write", "delete", "admin"},
			}

			// Add admin user to request context
			ctx := context.WithValue(r.Context(), "admin_user", adminUser)
			next.ServeHTTP(w, r.WithContext(ctx))
		})
	}
}

// handleHealth provides basic health check endpoint
func (s *AdminService) handleHealth(w http.ResponseWriter, r *http.Request) {
	health := map[string]interface{}{
		"status":    "healthy",
		"service":   "admin-service-go",
		"version":   "1.0.0",
		"timestamp": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(health)
}

// handleGetSystemHealth returns comprehensive system health information
func (s *AdminService) handleGetSystemHealth(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	health, err := s.GetSystemHealth(ctx)
	if err != nil {
		s.logger.Error("Failed to get system health", zap.Error(err))
		s.writeError(w, http.StatusInternalServerError, "failed to get system health")
		return
	}

	s.writeJSON(w, http.StatusOK, health)
}

// handleGetSystemMetrics returns system performance metrics
func (s *AdminService) handleGetSystemMetrics(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement metrics collection
	metrics := map[string]interface{}{
		"active_connections": 42,
		"total_requests":     1337,
		"error_rate":         0.01,
		"average_latency_ms": 15.5,
		"memory_usage_mb":    85.2,
		"cpu_usage_percent":  23.4,
	}

	s.writeJSON(w, http.StatusOK, metrics)
}

// handleGetActiveSessions returns all active admin sessions
func (s *AdminService) handleGetActiveSessions(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	sessions, err := s.GetActiveAdminSessions(ctx)
	if err != nil {
		s.logger.Error("Failed to get active sessions", zap.Error(err))
		s.writeError(w, http.StatusInternalServerError, "failed to get active sessions")
		return
	}

	s.writeJSON(w, http.StatusOK, sessions)
}

// handleBanUser bans a user account
func (s *AdminService) handleBanUser(w http.ResponseWriter, r *http.Request) {
	userIDStr := chi.URLParam(r, "userID")
	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	var req models.UserBanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.UserID = userID

	adminUser := r.Context().Value("admin_user").(*models.AdminUser)

	err = s.BanUser(r.Context(), adminUser.ID, req.UserID, req.Reason, req.Duration)
	if err != nil {
		s.logger.Error("Failed to ban user", zap.Error(err), zap.String("user_id", userID.String()))
		s.writeError(w, http.StatusInternalServerError, "failed to ban user")
		return
	}

	s.writeJSON(w, http.StatusOK, map[string]string{"status": "user banned successfully"})
}

// handleUnbanUser unbans a user account
func (s *AdminService) handleUnbanUser(w http.ResponseWriter, r *http.Request) {
	userIDStr := chi.URLParam(r, "userID")
	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	var req models.UserUnbanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.UserID = userID

	adminUser := r.Context().Value("admin_user").(*models.AdminUser)

	err = s.UnbanUser(r.Context(), adminUser.ID, req.UserID, req.Reason)
	if err != nil {
		s.logger.Error("Failed to unban user", zap.Error(err), zap.String("user_id", userID.String()))
		s.writeError(w, http.StatusInternalServerError, "failed to unban user")
		return
	}

	s.writeJSON(w, http.StatusOK, map[string]string{"status": "user unbanned successfully"})
}

// handleGetUserDetails returns detailed user information
func (s *AdminService) handleGetUserDetails(w http.ResponseWriter, r *http.Request) {
	userIDStr := chi.URLParam(r, "userID")
	_, err := uuid.Parse(userIDStr)
	if err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid user ID")
		return
	}

	// TODO: Implement user details retrieval
	userDetails := map[string]interface{}{
		"id":         userIDStr,
		"username":   "example_user",
		"email":      "user@example.com",
		"status":     "active",
		"created_at": time.Now().Add(-30 * 24 * time.Hour),
		"last_login": time.Now().Add(-2 * time.Hour),
	}

	s.writeJSON(w, http.StatusOK, userDetails)
}

// handleGetContentModerationQueue returns content requiring moderation
func (s *AdminService) handleGetContentModerationQueue(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement content moderation queue
	queue := []map[string]interface{}{
		{
			"id":          uuid.New().String(),
			"type":        "user_post",
			"content":     "Example content requiring moderation",
			"author_id":   uuid.New().String(),
			"submitted_at": time.Now().Add(-1 * time.Hour),
			"priority":    "high",
		},
	}

	s.writeJSON(w, http.StatusOK, queue)
}

// handleModerateContent processes content moderation actions
func (s *AdminService) handleModerateContent(w http.ResponseWriter, r *http.Request) {
	contentIDStr := chi.URLParam(r, "contentID")
	_, err := uuid.Parse(contentIDStr)
	if err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid content ID")
		return
	}

	var req models.ContentModerationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// TODO: Implement content moderation logic
	s.logger.Info("Content moderation action",
		zap.String("content_id", contentIDStr),
		zap.String("action", req.Action),
		zap.String("reason", req.Reason))

	s.writeJSON(w, http.StatusOK, map[string]string{"status": "content moderated successfully"})
}

// handleGetAuditLog returns paginated audit log entries
func (s *AdminService) handleGetAuditLog(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	limitStr := r.URL.Query().Get("limit")
	limit := 50 // default
	if limitStr != "" {
		if parsed, err := strconv.Atoi(limitStr); err == nil && parsed > 0 && parsed <= 1000 {
			limit = parsed
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0 // default
	if offsetStr != "" {
		if parsed, err := strconv.Atoi(offsetStr); err == nil && parsed >= 0 {
			offset = parsed
		}
	}

	actions, err := s.GetAdminAuditLog(r.Context(), limit, offset)
	if err != nil {
		s.logger.Error("Failed to get audit log", zap.Error(err))
		s.writeError(w, http.StatusInternalServerError, "failed to get audit log")
		return
	}

	response := map[string]interface{}{
		"actions": actions,
		"pagination": map[string]interface{}{
			"limit":  limit,
			"offset": offset,
			"total":  len(actions), // TODO: Return actual total count
		},
	}

	s.writeJSON(w, http.StatusOK, response)
}

// handleGetSystemConfig returns current system configuration
func (s *AdminService) handleGetSystemConfig(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement system configuration retrieval
	config := map[string]interface{}{
		"database": map[string]interface{}{
			"host":     "localhost",
			"port":     5432,
			"database": "necpgame",
		},
		"redis": map[string]interface{}{
			"host": "localhost",
			"port": 6379,
		},
		"features": map[string]interface{}{
			"user_registration": true,
			"content_moderation": true,
			"analytics": true,
		},
	}

	s.writeJSON(w, http.StatusOK, config)
}

// handleUpdateSystemConfig updates system configuration
func (s *AdminService) handleUpdateSystemConfig(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement system configuration updates
	// This should be very carefully implemented with validation and audit logging

	var config map[string]interface{}
	if err := json.NewDecoder(r.Body).Decode(&config); err != nil {
		s.writeError(w, http.StatusBadRequest, "invalid configuration")
		return
	}

	s.logger.Info("System configuration updated", zap.Any("config", config))
	s.writeJSON(w, http.StatusOK, map[string]string{"status": "configuration updated"})
}

// writeJSON writes JSON response with proper headers
func (s *AdminService) writeJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

// writeError writes error response
func (s *AdminService) writeError(w http.ResponseWriter, status int, message string) {
	response := map[string]interface{}{
		"error":   http.StatusText(status),
		"message": message,
		"status":  status,
	}

	s.writeJSON(w, status, response)
}
