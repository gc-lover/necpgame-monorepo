// Package server Issue: #141888646
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/admin-service-go/models"
	"github.com/necpgame/admin-service-go/pkg/api"
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
	addr         string
	router       http.Handler
	adminService AdminServiceInterface
	logger       *logrus.Logger
	server       *http.Server
	jwtValidator *JwtValidator
	authEnabled  bool
}

func NewHTTPServer(addr string, adminService AdminServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := http.NewServeMux()

	server := &HTTPServer{
		addr:         addr,
		router:       mux.NewRouter(),
		adminService: adminService,
		logger:       GetLogger(),
		jwtValidator: jwtValidator,
		authEnabled:  authEnabled,
	}

	// ogen handlers
	ogenHandlers := NewAdminHandlers(adminService, server.logger)
	secHandler := NewSecurityHandler(jwtValidator, authEnabled)

	// Create ogen server
	ogenServer, err := api.NewServer(ogenHandlers, secHandler)
	if err != nil {
		server.logger.WithError(err).Fatal("Failed to create ogen server")
	}

	// Mount ogen server at /api/v1/admin
	var handler http.Handler = ogenServer
	handler = corsMiddleware(handler)
	handler = loggingMiddleware(server.logger)(handler)
	handler = recoveryMiddleware(server.logger)(handler)
	handler = timeoutMiddleware(handler, 60*time.Second)
	router.Handle("/api/v1/admin/", handler)

	// Health and metrics endpoints
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		json.NewEncoder(w).Encode(map[string]string{"status": "healthy"})
	})
	router.Handle("/metrics", promhttp.Handler())

	// Legacy endpoints removed - all functionality available through ogen API at /api/v1/admin

	server.router = router

	return server
}

func corsMiddleware(next http.Handler) http.Handler {
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

func loggingMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			next.ServeHTTP(w, r)
			logger.WithFields(logrus.Fields{
				"method":   r.Method,
				"path":     r.URL.Path,
				"duration": time.Since(start),
			}).Info("HTTP request")
		})
	}
}

func recoveryMiddleware(logger *logrus.Logger) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			defer func() {
				if rec := recover(); rec != nil {
					logger.WithField("panic", rec).Error("recovered panic")
					http.Error(w, "internal error", http.StatusInternalServerError)
				}
			}()
			next.ServeHTTP(w, r)
		})
	}
}

func timeoutMiddleware(next http.Handler, d time.Duration) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		ctx, cancel := context.WithTimeout(r.Context(), d)
		defer cancel()
		next.ServeHTTP(w, r.WithContext(ctx))
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
		defer close(errChan)
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
