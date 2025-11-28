package server

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/admin-service-go/models"
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
	router        *mux.Router
	adminService  AdminServiceInterface
	logger        *logrus.Logger
	server        *http.Server
	jwtValidator  *JwtValidator
	authEnabled   bool
}

func NewHTTPServer(addr string, adminService AdminServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:         addr,
		router:       router,
		adminService: adminService,
		logger:       GetLogger(),
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	if authEnabled {
		router.Use(server.authMiddleware)
		router.Use(server.permissionMiddleware)
	}

	api := router.PathPrefix("/api/v1/admin").Subrouter()

	api.HandleFunc("/players/ban", server.banPlayer).Methods("POST")
	api.HandleFunc("/players/kick", server.kickPlayer).Methods("POST")
	api.HandleFunc("/players/mute", server.mutePlayer).Methods("POST")
	api.HandleFunc("/players/unban", server.unbanPlayer).Methods("POST")
	api.HandleFunc("/players/unmute", server.unmutePlayer).Methods("POST")
	api.HandleFunc("/players/search", server.searchPlayers).Methods("POST")

	api.HandleFunc("/inventory/give", server.giveItem).Methods("POST")
	api.HandleFunc("/inventory/remove", server.removeItem).Methods("POST")

	api.HandleFunc("/economy/set-currency", server.setCurrency).Methods("POST")
	api.HandleFunc("/economy/add-currency", server.addCurrency).Methods("POST")

	api.HandleFunc("/world/flags", server.setWorldFlag).Methods("POST")

	api.HandleFunc("/events", server.createEvent).Methods("POST")

	api.HandleFunc("/analytics", server.getAnalytics).Methods("GET")

	api.HandleFunc("/audit-logs", server.getAuditLogs).Methods("GET")
	api.HandleFunc("/audit-logs/{log_id}", server.getAuditLog).Methods("GET")

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

	return server
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

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode response")
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusInternalServerError)
		errorResponse := map[string]string{"error": "Internal server error"}
		json.NewEncoder(w).Encode(errorResponse)
		return
	}
	
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	w.Write(buf.Bytes())
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}
