// Package server Issue: #140875766
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"
)

// SandevistanServer представляет HTTP сервер для Sandevistan service
type SandevistanServer struct {
	server     *http.Server
	logger     *zap.Logger
	service    *SandevistanService
	middleware *AuthMiddleware
}

// NewSandevistanServer создает новый сервер
func NewSandevistanServer(logger *zap.Logger, db *sql.DB, jwtSecret string) *SandevistanServer {
	service := NewSandevistanService(db, logger)
	authMiddleware := NewAuthMiddleware(logger, jwtSecret)

	// Создаем Chi роутер
	r := chi.NewRouter()

	// Performance middleware
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Security middleware
	r.Use(authMiddleware.SecurityHeadersMiddleware)
	r.Use(authMiddleware.CORSMiddleware)

	// Logging middleware
	r.Use(authMiddleware.LoggingMiddleware)

	// Recovery middleware
	r.Use(authMiddleware.RecoveryMiddleware)

	// Health check endpoints
	r.Get("/health", service.HealthCheckHandler)
	r.Get("/ready", service.ReadinessCheckHandler)
	r.Get("/metrics", service.MetricsHandler)

	// Profiling endpoints для MMOFPS оптимизаций
	r.HandleFunc("/debug/pprof/*", service.PprofHandler)
	r.Get("/debug/vars", service.ExpvarHandler)

	// API endpoints
	r.Route("/api/v1", func(r chi.Router) {
		// Protected endpoints
		r.Group(func(r chi.Router) {
			r.Use(authMiddleware.JWTAuth)

			r.Post("/sandevistan/activate", service.ActivateSandevistanHandler)
			r.Post("/sandevistan/deactivate", service.DeactivateSandevistanHandler)
			r.Get("/sandevistan/state", service.GetSandevistanStateHandler)
			r.Post("/sandevistan/upgrade", service.UpgradeSandevistanHandler)
			r.Get("/sandevistan/stats", service.GetSandevistanStatsHandler)
		})
	})

	server := &http.Server{
		Addr:         ":8084",
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &SandevistanServer{
		server:     server,
		logger:     logger,
		service:    service,
		middleware: authMiddleware,
	}
}

// Start запускает HTTP сервер
func (s *SandevistanServer) Start() error {
	s.logger.Info("Starting Sandevistan server", zap.String("addr", s.server.Addr))

	// Graceful shutdown
	shutdown := make(chan os.Signal, 1)
	signal.Notify(shutdown, os.Interrupt, syscall.SIGTERM)

	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	<-shutdown
	s.logger.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(ctx); err != nil {
		s.logger.Error("Server forced to shutdown", zap.Error(err))
		return err
	}

	s.logger.Info("Server shutdown complete")
	return nil
}

// HTTP Handlers

// ActivateSandevistanHandler обрабатывает POST /api/v1/sandevistan/activate
func (s *SandevistanService) ActivateSandevistanHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	var req struct {
		DurationOverride *float64 `json:"duration_override"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	state, err := s.ActivateSandevistan(r.Context(), userID, req.DurationOverride)
	if err != nil {
		if cooldownErr, ok := err.(*CooldownError); ok {
			w.Header().Set("Content-Type", "application/json")
			w.WriteHeader(http.StatusTooManyRequests)
			json.NewEncoder(w).Encode(map[string]interface{}{
				"error":              "cooldown_active",
				"cooldown_remaining": cooldownErr.CooldownRemaining,
				"message":            cooldownErr.Message,
			})
			return
		}
		s.logger.Error("Failed to activate Sandevistan", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(state)
}

// DeactivateSandevistanHandler обрабатывает POST /api/v1/sandevistan/deactivate
func (s *SandevistanService) DeactivateSandevistanHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	state, err := s.DeactivateSandevistan(r.Context(), userID)
	if err != nil {
		if err.Error() == "Sandevistan is not active" {
			http.Error(w, "Sandevistan is not active", http.StatusBadRequest)
			return
		}
		s.logger.Error("Failed to deactivate Sandevistan", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(state)
}

// GetSandevistanStateHandler обрабатывает GET /api/v1/sandevistan/state
func (s *SandevistanService) GetSandevistanStateHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	state, err := s.GetSandevistanState(r.Context(), userID)
	if err != nil {
		s.logger.Error("Failed to get Sandevistan state", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(state)
}

// UpgradeSandevistanHandler обрабатывает POST /api/v1/sandevistan/upgrade
func (s *SandevistanService) UpgradeSandevistanHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	var req struct {
		UpgradeType string `json:"upgrade_type"`
		Level       int    `json:"level"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	stats, err := s.UpgradeSandevistan(r.Context(), userID, req.UpgradeType, req.Level)
	if err != nil {
		s.logger.Error("Failed to upgrade Sandevistan", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(stats)
}

// GetSandevistanStatsHandler обрабатывает GET /api/v1/sandevistan/stats
func (s *SandevistanService) GetSandevistanStatsHandler(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id").(string)

	stats, err := s.GetSandevistanStats(r.Context(), userID)
	if err != nil {
		s.logger.Error("Failed to get Sandevistan stats", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(stats)
}

// HealthCheckHandler Health check handlers
func (s *SandevistanService) HealthCheckHandler(w http.ResponseWriter, _ *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "healthy"}`))
}

func (s *SandevistanService) ReadinessCheckHandler(w http.ResponseWriter, _ *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status": "ready"}`))
}

func (s *SandevistanService) MetricsHandler(w http.ResponseWriter, _ *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"service": "sandevistan-service", "version": "1.0.0"}`))
}

// PprofHandler Profiling handlers для MMOFPS оптимизаций
func (s *SandevistanService) PprofHandler(w http.ResponseWriter, r *http.Request) {
	// Используем Chi роутер для pprof endpoints
	switch r.URL.Path {
	case "/debug/pprof/":
		pprof.Index(w, r)
	case "/debug/pprof/cmdline":
		pprof.Cmdline(w, r)
	case "/debug/pprof/profile":
		pprof.Profile(w, r)
	case "/debug/pprof/symbol":
		pprof.Symbol(w, r)
	case "/debug/pprof/trace":
		pprof.Trace(w, r)
	default:
		// Для heap, goroutine, etc.
		pprof.Handler(r.URL.Path[13:]).ServeHTTP(w, r) // убираем "/debug/pprof/"
	}
}

func (s *SandevistanService) ExpvarHandler(w http.ResponseWriter, _ *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"service": "sandevistan-service", "status": "operational"}`))
}
