package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/sirupsen/logrus"
)

// CombatServiceConfig configuration for combat service
type CombatServiceConfig struct {
	// Add configuration fields here
}

// OPTIMIZATION: Issue #1936 - Memory-aligned struct for performance
type CombatServer struct {
	router  *chi.Mux
	logger  *logrus.Logger
	service *CombatService
	metrics *CombatMetrics
}

// OPTIMIZATION: Issue #1936 - Struct field alignment (large → small)
type CombatMetrics struct {
	RequestsTotal      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration    prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveCombats      prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	CombatActions      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	DamageCalculations prometheus.Counter   `json:"-"` // 16 bytes (interface)
}

func NewCombatServer(config *CombatServiceConfig, logger *logrus.Logger) (*CombatServer, error) {
	// Initialize metrics
	metrics := &CombatMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_requests_total",
			Help: "Total number of requests to combat service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "combat_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveCombats: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "combat_active_sessions",
			Help: "Number of active combat sessions",
		}),
		CombatActions: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_actions_total",
			Help: "Total number of combat actions executed",
		}),
		DamageCalculations: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_damage_calculations_total",
			Help: "Total number of damage calculations performed",
		}),
	}

	// Initialize service
	service := NewCombatService(logger, metrics)

	// Create router with optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #1935 - CORS middleware for web clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #1935 - Security middlewares
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for MMO combat

	// OPTIMIZATION: Issue #1936 - Metrics middleware
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			metrics.RequestsTotal.Inc()

			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)
			next.ServeHTTP(ww, r)

			duration := time.Since(start)
			metrics.RequestDuration.Observe(duration.Seconds())

			logger.WithFields(logrus.Fields{
				"method":      r.Method,
				"path":        r.URL.Path,
				"status":      ww.Status(),
				"duration_ms": duration.Milliseconds(),
			}).Debug("request completed")
		})
	})

	// Health check
	r.Get("/health", service.HealthCheck)

	// Combat endpoints
	r.Route("/combat", func(r chi.Router) {
		r.Post("/initiate", service.InitiateCombat)
		r.Get("/{combatId}/status", service.GetCombatStatus)
		r.Post("/{combatId}/action", service.ExecuteCombatAction)
		r.Post("/{combatId}/end", service.EndCombat)
	})

	// Status effects endpoints
	r.Route("/status-effects", func(r chi.Router) {
		r.Get("/{characterId}", service.GetStatusEffects)
		r.Post("/{characterId}/apply", service.ApplyStatusEffect)
	})

	// Damage calculation endpoint
	r.Post("/damage/calculate", service.CalculateDamage)

	server := &CombatServer{
		router:  r,
		logger:  logger,
		service: service,
		metrics: metrics,
	}

	return server, nil
}

func (s *CombatServer) Router() *chi.Mux {
	return s.router
}

func (s *CombatServer) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"combat-service","version":"1.0.0"}`))
}
