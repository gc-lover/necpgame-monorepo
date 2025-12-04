// Issue: #141888646
package server

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	chiMiddleware "github.com/go-chi/chi/v5/middleware"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	api "github.com/necpgame/admin-service-go/pkg/api"
	"github.com/necpgame/admin-service-go/models"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

type AdminServiceInterface interface {
	LogAction(ctx context.Context, adminID uuid.UUID, actionType models.AdminActionType, targetID *uuid.UUID, targetType string, details map[string]interface{}, ipAddress, userAgent string) error
	BanPlayer(ctx context.Context, adminID uuid.UUID, req *models.BanPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	KickPlayer(ctx context.Context, adminID uuid.UUID, req *models.KickPlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	MutePlayer(ctx context.Context, adminID uuid.UUID, req *models.MutePlayerRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	GiveItem(ctx context.Context, adminID uuid.UUID, req *models.GiveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	RemoveItem(ctx context.Context, adminID uuid.UUID, req *models.RemoveItemRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	SetCurrency(ctx context.Context, adminID uuid.UUID, req *models.SetCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	AddCurrency(ctx context.Context, adminID uuid.UUID, req *models.AddCurrencyRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	SetWorldFlag(ctx context.Context, adminID uuid.UUID, req *models.SetWorldFlagRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	CreateEvent(ctx context.Context, adminID uuid.UUID, req *models.CreateEventRequest, ipAddress, userAgent string) (*models.AdminActionResponse, error)
	SearchPlayers(ctx context.Context, req *models.SearchPlayersRequest) (*models.PlayerSearchResponse, error)
	GetAnalytics(ctx context.Context) (*models.AnalyticsResponse, error)
	GetAuditLogs(ctx context.Context, adminID *uuid.UUID, actionType *models.AdminActionType, limit, offset int) (*models.AuditLogListResponse, error)
	GetAuditLog(ctx context.Context, logID uuid.UUID) (*models.AdminAuditLog, error)
}

type HTTPServer struct {
	addr          string
	router        http.Handler // Can be chi.Router or mux.Router
	adminService  AdminServiceInterface
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, adminService AdminServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	// Use chi router for ogen integration
	router := chi.NewRouter()
	
	server := &HTTPServer{
		addr:         addr,
		router:       mux.NewRouter(), // Keep mux for legacy endpoints
		adminService: adminService,
		logger:       GetLogger(),
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}

	// Chi middleware for ogen server
	router.Use(chiMiddleware.RequestID)
	router.Use(chiMiddleware.RealIP)
	router.Use(chiMiddleware.Logger)
	router.Use(chiMiddleware.Recoverer)
	router.Use(corsMiddlewareChi)

	// ogen handlers
	ogenHandlers := NewAdminHandlers(adminService, server.logger)
	secHandler := NewSecurityHandler(jwtValidator, authEnabled)

	// Create ogen server
	ogenServer, err := api.NewServer(ogenHandlers, secHandler)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	// Mount ogen server at /api/v1/admin
	router.Mount("/api/v1/admin", ogenServer)

	// Legacy endpoints (keep for backward compatibility)
	legacyRouter := server.router.(*mux.Router)
	legacyRouter.Use(server.loggingMiddleware)
	legacyRouter.Use(server.metricsMiddleware)
	legacyRouter.Use(server.corsMiddleware)

	if authEnabled {
		legacyRouter.Use(server.authMiddleware)
		legacyRouter.Use(server.permissionMiddleware)
	}

	apiRouter := legacyRouter.PathPrefix("/api/v1/admin").Subrouter()

	apiRouter.HandleFunc("/players/ban", server.banPlayer).Methods("POST")
	apiRouter.HandleFunc("/players/kick", server.kickPlayer).Methods("POST")
	apiRouter.HandleFunc("/players/mute", server.mutePlayer).Methods("POST")
	apiRouter.HandleFunc("/players/unban", server.unbanPlayer).Methods("POST")
	apiRouter.HandleFunc("/players/unmute", server.unmutePlayer).Methods("POST")
	apiRouter.HandleFunc("/players/search", server.searchPlayers).Methods("POST")

	apiRouter.HandleFunc("/inventory/give", server.giveItem).Methods("POST")
	apiRouter.HandleFunc("/inventory/remove", server.removeItem).Methods("POST")

	apiRouter.HandleFunc("/economy/set-currency", server.setCurrency).Methods("POST")
	apiRouter.HandleFunc("/economy/add-currency", server.addCurrency).Methods("POST")

	apiRouter.HandleFunc("/world/flags", server.setWorldFlag).Methods("POST")

	apiRouter.HandleFunc("/events", server.createEvent).Methods("POST")

	apiRouter.HandleFunc("/analytics", server.getAnalytics).Methods("GET")

	apiRouter.HandleFunc("/audit-logs", server.getAuditLogs).Methods("GET")
	apiRouter.HandleFunc("/audit-logs/{log_id}", server.getAuditLog).Methods("GET")

	legacyRouter.HandleFunc("/health", server.healthCheck).Methods("GET")
	legacyRouter.Handle("/metrics", promhttp.Handler())

	// Mount legacy router to chi
	router.Mount("/legacy", legacyRouter)

	// Set router to chi router
	server.router = router

	return server
}

// corsMiddlewareChi is CORS middleware for chi router
func corsMiddlewareChi(next http.Handler) http.Handler {
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

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

func (s *HTTPServer) getAdminID(r *http.Request) (uuid.UUID, error) {
	claims, ok := r.Context().Value("claims").(*Claims)
	if !ok || claims == nil {
		return uuid.Nil, fmt.Errorf("invalid claims")
	}

	adminID, err := uuid.Parse(claims.Subject)
	if err != nil {
		return uuid.Nil, err
	}

	return adminID, nil
}

func (s *HTTPServer) getIPAddress(r *http.Request) string {
	ip := r.Header.Get("X-Forwarded-For")
	if ip == "" {
		ip = r.Header.Get("X-Real-IP")
	}
	if ip == "" {
		ip = strings.Split(r.RemoteAddr, ":")[0]
	}
	return ip
}

func (s *HTTPServer) banPlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.BanPlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.BanPlayer(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to ban player")
		s.respondError(w, http.StatusInternalServerError, "failed to ban player")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) kickPlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.KickPlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.KickPlayer(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to kick player")
		s.respondError(w, http.StatusInternalServerError, "failed to kick player")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) mutePlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.MutePlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.MutePlayer(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to mute player")
		s.respondError(w, http.StatusInternalServerError, "failed to mute player")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) unbanPlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
		Reason      string    `json:"reason"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
	}

	err = s.adminService.LogAction(r.Context(), adminID, models.AdminActionTypeUnban, &req.CharacterID, "character", details, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to log unban action")
		s.respondError(w, http.StatusInternalServerError, "failed to unban player")
		return
	}

	RecordAdminAction(string(models.AdminActionTypeUnban))

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) unmutePlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
		Reason      string    `json:"reason"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
	}

	err = s.adminService.LogAction(r.Context(), adminID, models.AdminActionTypeUnmute, &req.CharacterID, "character", details, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to log unmute action")
		s.respondError(w, http.StatusInternalServerError, "failed to unmute player")
		return
	}

	RecordAdminAction(string(models.AdminActionTypeUnmute))

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) searchPlayers(w http.ResponseWriter, r *http.Request) {
	var req models.SearchPlayersRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.SearchPlayers(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to search players")
		s.respondError(w, http.StatusInternalServerError, "failed to search players")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) giveItem(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.GiveItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.GiveItem(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to give item")
		s.respondError(w, http.StatusInternalServerError, "failed to give item")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) removeItem(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.RemoveItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.RemoveItem(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove item")
		s.respondError(w, http.StatusInternalServerError, "failed to remove item")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) setCurrency(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.SetCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.SetCurrency(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to set currency")
		s.respondError(w, http.StatusInternalServerError, "failed to set currency")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) addCurrency(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.AddCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.AddCurrency(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to add currency")
		s.respondError(w, http.StatusInternalServerError, "failed to add currency")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) setWorldFlag(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.SetWorldFlagRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.SetWorldFlag(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to set world flag")
		s.respondError(w, http.StatusInternalServerError, "failed to set world flag")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) createEvent(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.CreateEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.CreateEvent(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to create event")
		s.respondError(w, http.StatusInternalServerError, "failed to create event")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAnalytics(w http.ResponseWriter, r *http.Request) {
	response, err := s.adminService.GetAnalytics(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get analytics")
		s.respondError(w, http.StatusInternalServerError, "failed to get analytics")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAuditLogs(w http.ResponseWriter, r *http.Request) {
	var adminID *uuid.UUID
	if adminIDStr := r.URL.Query().Get("admin_id"); adminIDStr != "" {
		if id, err := uuid.Parse(adminIDStr); err == nil {
			adminID = &id
		}
	}

	var actionType *models.AdminActionType
	if actionTypeStr := r.URL.Query().Get("action_type"); actionTypeStr != "" {
		at := models.AdminActionType(actionTypeStr)
		actionType = &at
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.adminService.GetAuditLogs(r.Context(), adminID, actionType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get audit logs")
		s.respondError(w, http.StatusInternalServerError, "failed to get audit logs")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAuditLog(w http.ResponseWriter, r *http.Request) {
	logIDStr := chi.URLParam(r, "log_id")
	logID, err := uuid.Parse(logIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid log_id")
		return
	}

	log, err := s.adminService.GetAuditLog(r.Context(), logID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get audit log")
		s.respondError(w, http.StatusInternalServerError, "failed to get audit log")
		return
	}

	if log == nil {
		s.respondError(w, http.StatusNotFound, "audit log not found")
		return
	}

	s.respondJSON(w, http.StatusOK, log)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      recorder.statusCode,
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
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

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		ctx = context.WithValue(ctx, "user_id", claims.Subject)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func (s *HTTPServer) permissionMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		claims, ok := r.Context().Value("claims").(*Claims)
		if !ok || claims == nil {
			s.respondError(w, http.StatusUnauthorized, "invalid claims")
			return
		}

		hasAdminRole := false
		for _, role := range claims.RealmAccess.Roles {
			if role == "admin" || role == "moderator" {
				hasAdminRole = true
				break
			}
		}

		if !hasAdminRole {
			s.respondError(w, http.StatusForbidden, "insufficient permissions")
			return
		}

		next.ServeHTTP(w, r)
	})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

