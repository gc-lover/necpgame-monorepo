// Package server Issue: #140876112
package server

import (
	"context"
	"database/sql"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"go.uber.org/zap"
)

// RomanceCoreServer представляет HTTP сервер для romance-core-service
type RomanceCoreServer struct {
	server         *http.Server
	logger         *zap.Logger
	db             *sql.DB
	romanceService *RomanceCoreService
}

// NewRomanceCoreServer создает новый сервер romance-core-service
func NewRomanceCoreServer(logger *zap.Logger, db *sql.DB) *RomanceCoreServer {
	// Создаем сервис романтики
	romanceService := NewRomanceCoreService(db, logger)

	// Создаем Chi роутер с оптимизациями для MMOFPS
	r := chi.NewRouter()

	// Performance middleware
	r.Use(middleware.RealIP)
	r.Use(middleware.RequestID)
	r.Use(middleware.Recoverer)

	// Request size limit для защиты от DoS
	r.Use(middleware.RequestSize(1024 * 1024)) // 1MB limit

	// Logging middleware с structured logging
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Создаем response writer с захватом статуса
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			next.ServeHTTP(ww, r)

			logger.Info("HTTP Request",
				zap.String("method", r.Method),
				zap.String("path", r.URL.Path),
				zap.Int("status", ww.Status()),
				zap.Duration("duration", time.Since(start)),
				zap.String("remote_addr", r.RemoteAddr),
			)
		})
	})

	// Health check endpoints (не требуют аутентификации)
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status": "healthy", "service": "romance-core-service"}`))
	})
	r.Get("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status": "ready", "service": "romance-core-service"}`))
	})
	r.Get("/metrics", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"service": "romance-core-service", "version": "1.0.0"}`))
	})

	// API endpoints (требуют Bearer token для production)
	r.Route("/api/v1", func(r chi.Router) {
		// Пока без аутентификации для разработки
		r.Post("/romance/calculate-chemistry", func(w http.ResponseWriter, r *http.Request) {
			// Обертка для вызова метода с правильным receiver
			server := &RomanceCoreServer{romanceService: romanceService, logger: logger, db: db}
			server.CalculateChemistryHandler(w, r)
		})

		r.Post("/romance/calculate-event-score", func(w http.ResponseWriter, r *http.Request) {
			server := &RomanceCoreServer{romanceService: romanceService, logger: logger, db: db}
			server.CalculateEventScoreHandler(w, r)
		})

		r.Post("/romance/select-events", func(w http.ResponseWriter, r *http.Request) {
			server := &RomanceCoreServer{romanceService: romanceService, logger: logger, db: db}
			server.SelectEventsHandler(w, r)
		})

		r.Post("/romance/adapt-event", func(w http.ResponseWriter, r *http.Request) {
			server := &RomanceCoreServer{romanceService: romanceService, logger: logger, db: db}
			server.AdaptEventHandler(w, r)
		})

		r.Post("/romance/validate-triggers", func(w http.ResponseWriter, r *http.Request) {
			server := &RomanceCoreServer{romanceService: romanceService, logger: logger, db: db}
			server.ValidateTriggersHandler(w, r)
		})

		r.Get("/romance/stats", func(w http.ResponseWriter, r *http.Request) {
			server := &RomanceCoreServer{romanceService: romanceService, logger: logger, db: db}
			server.GetRomanceStatsHandler(w, r)
		})
	})

	server := &http.Server{
		Addr:         ":8084",
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	return &RomanceCoreServer{
		server:         server,
		logger:         logger,
		db:             db,
		romanceService: romanceService,
	}
}

// Start запускает HTTP сервер с graceful shutdown
func (s *RomanceCoreServer) Start() error {
	s.logger.Info("Starting romance-core-service", zap.String("addr", s.server.Addr))

	// Канал для graceful shutdown
	shutdown := make(chan os.Signal, 1)
	signal.Notify(shutdown, os.Interrupt, syscall.SIGTERM)

	// Запускаем сервер в горутине
	go func() {
		s.logger.Info("Romance core service started", zap.String("addr", s.server.Addr))
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Ждем сигнала завершения
	<-shutdown
	s.logger.Info("Shutting down romance-core-service...")

	// Graceful shutdown с таймаутом
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(ctx); err != nil {
		s.logger.Error("Server forced to shutdown", zap.Error(err))
		return err
	}

	s.logger.Info("Romance core service shutdown complete")
	return nil
}
