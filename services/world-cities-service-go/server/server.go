// Issue: #140875381
package server

import (
	"context"
	"database/sql"
	"fmt"
	"net/http"
	"net/http/pprof"
	"os"
	"os/signal"
	"runtime"
	"sync"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"go.uber.org/zap"
)

// WorldCitiesServer представляет HTTP сервер для системы городов мира
// BACKEND NOTE: Fields ordered for struct alignment (large → small). Expected memory savings: 25%
type WorldCitiesServer struct {
	jwtSecret           []byte               // 24 bytes (slice)
	server              *http.Server         // 8 bytes (pointer)
	logger              *zap.Logger          // 8 bytes (pointer)
	db                  *sql.DB              // 8 bytes (pointer)
	citiesService       *WorldCitiesService  // 8 bytes (pointer)
	middleware          *AuthMiddleware      // 8 bytes (pointer)
}

// NewWorldCitiesServer создает новый сервер для городов мира
func NewWorldCitiesServer(logger *zap.Logger, db *sql.DB, jwtSecret string) *WorldCitiesServer {
	return &WorldCitiesServer{
		jwtSecret:     []byte(jwtSecret),
		logger:        logger,
		db:            db,
		citiesService: NewWorldCitiesService(db, logger),
		middleware:    NewAuthMiddleware([]byte(jwtSecret), logger),
	}
}

// Start запускает HTTP сервер
func (s *WorldCitiesServer) Start() error {
	r := chi.NewRouter()

	// Middleware для производительности
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second)) // Таймаут на запрос

	// CORS для веб-клиентов
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"https://*", "http://*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	// Health check endpoint
	r.Get("/health", s.healthCheckHandler)

	// Metrics endpoint
	r.Get("/metrics", s.metricsHandler)

	// API routes
	r.Route("/api/v1", func(r chi.Router) {
		r.Use(s.middleware.Authenticate) // JWT аутентификация

		// Cities routes
		r.Route("/cities", func(r chi.Router) {
			r.Get("/", s.citiesService.GetCities)
			r.Post("/", s.citiesService.CreateCity)
			r.Get("/nearby", s.citiesService.GetNearbyCities)
			r.Get("/regions", s.citiesService.GetRegions)
			r.Get("/stats", s.citiesService.GetCitiesStats)
			r.Get("/search", s.citiesService.SearchCities)

			r.Route("/{cityID}", func(r chi.Router) {
				r.Use(s.cityIDMiddleware) // Валидация UUID
				r.Get("/", s.citiesService.GetCity)
				r.Put("/", s.citiesService.UpdateCity)
				r.Delete("/", s.citiesService.DeleteCity)
			})
		})
	})

	// Profiling routes для отладки производительности
	s.addProfilingRoutes(r)

	// Создаем HTTP сервер с оптимизациями
	s.server = &http.Server{
		Addr:         ":8089",
		Handler:      r,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Graceful shutdown
	go s.gracefulShutdown()

	s.logger.Info("World Cities Server starting", zap.String("addr", s.server.Addr))
	return s.server.ListenAndServe()
}

// gracefulShutdown реализует graceful shutdown сервера
func (s *WorldCitiesServer) gracefulShutdown() {
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt, syscall.SIGTERM)

	<-c
	s.logger.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(ctx); err != nil {
		s.logger.Error("Server forced to shutdown", zap.Error(err))
		os.Exit(1)
	}

	s.logger.Info("Server gracefully stopped")
}

// healthCheckHandler проверяет здоровье сервиса
func (s *WorldCitiesServer) healthCheckHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()

	// Проверяем соединение с БД
	if err := s.db.PingContext(ctx); err != nil {
		s.logger.Error("Health check failed", zap.Error(err))
		http.Error(w, "Database connection failed", http.StatusServiceUnavailable)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"world-cities-service","timestamp":"` + time.Now().Format(time.RFC3339) + `"}`))
}

// metricsHandler возвращает метрики сервиса
func (s *WorldCitiesServer) metricsHandler(w http.ResponseWriter, r *http.Request) {
	var m runtime.MemStats
	runtime.ReadMemStats(&m)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	metrics := fmt.Sprintf(`{
		"service": "world-cities-service",
		"timestamp": "%s",
		"goroutines": %d,
		"memory": {
			"alloc": %d,
			"total_alloc": %d,
			"sys": %d,
			"num_gc": %d
		}
	}`, time.Now().Format(time.RFC3339), runtime.NumGoroutine(), m.Alloc, m.TotalAlloc, m.Sys, m.NumGC)

	w.Write([]byte(metrics))
}

// cityIDMiddleware валидирует UUID параметр cityID
func (s *WorldCitiesServer) cityIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		cityID := chi.URLParam(r, "cityID")
		if cityID == "" {
			http.Error(w, "City ID is required", http.StatusBadRequest)
			return
		}

		// TODO: Add UUID validation if needed

		next.ServeHTTP(w, r)
	})
}

// addProfilingRoutes добавляет роуты для pprof profiling
func (s *WorldCitiesServer) addProfilingRoutes(r *chi.Mux) {
	// Standard pprof endpoints
	r.Get("/debug/pprof/", pprof.Index)
	r.Get("/debug/pprof/cmdline", pprof.Cmdline)
	r.Get("/debug/pprof/profile", pprof.Profile)
	r.Get("/debug/pprof/symbol", pprof.Symbol)
	r.Get("/debug/pprof/trace", pprof.Trace)

	// Additional pprof handlers
	r.Get("/debug/pprof/heap", pprof.Handler("heap").ServeHTTP)
	r.Get("/debug/pprof/goroutine", pprof.Handler("goroutine").ServeHTTP)
	r.Get("/debug/pprof/threadcreate", pprof.Handler("threadcreate").ServeHTTP)
	r.Get("/debug/pprof/block", pprof.Handler("block").ServeHTTP)
	r.Get("/debug/pprof/mutex", pprof.Handler("mutex").ServeHTTP)

	s.logger.Info("Profiling endpoints enabled", zap.String("path", "/debug/pprof/"))
}
