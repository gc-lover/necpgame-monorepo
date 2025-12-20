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

// InventoryServer OPTIMIZATION: Issue #1950 - Memory-aligned struct for performance
type InventoryServer struct {
	router  *chi.Mux
	logger  *logrus.Logger
	service *InventoryService
	metrics *InventoryMetrics
}

// InventoryMetrics OPTIMIZATION: Issue #1950 - Struct field alignment (large → small)
type InventoryMetrics struct {
	RequestsTotal     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration   prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveInventories prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	ItemOperations    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	EquipmentChanges  prometheus.Counter   `json:"-"` // 16 bytes (interface)
	SearchQueries     prometheus.Counter   `json:"-"` // 16 bytes (interface)
}

func NewInventoryServer(logger *logrus.Logger) (*InventoryServer, error) {
	// Initialize metrics
	metrics := &InventoryMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "inventory_requests_total",
			Help: "Total number of requests to inventory service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "inventory_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveInventories: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "inventory_active_sessions",
			Help: "Number of active inventory operations",
		}),
		ItemOperations: promauto.NewCounter(prometheus.CounterOpts{
			Name: "inventory_item_operations_total",
			Help: "Total number of item operations performed",
		}),
		EquipmentChanges: promauto.NewCounter(prometheus.CounterOpts{
			Name: "inventory_equipment_changes_total",
			Help: "Total number of equipment changes",
		}),
		SearchQueries: promauto.NewCounter(prometheus.CounterOpts{
			Name: "inventory_search_queries_total",
			Help: "Total number of item search queries",
		}),
	}

	// Initialize service
	service := NewInventoryService(logger, metrics)

	// Create router with optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #1951 - CORS middleware for web clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #1951 - Security middlewares
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for MMO inventory ops

	// OPTIMIZATION: Issue #1950 - Metrics middleware
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

	// Inventory endpoints
	r.Route("/inventory/{characterId}", func(r chi.Router) {
		r.Get("/", service.GetInventory)
		r.Get("/items", service.ListInventoryItems)
		r.Post("/move", service.MoveItem)
		r.Post("/equip", service.EquipItem)
		r.Post("/unequip", service.UnequipItem)
		r.Post("/use", service.UseItem)
		r.Post("/drop", service.DropItem)
		r.Get("/containers", service.GetContainers)
	})

	// Equipment endpoints
	r.Route("/equipment/{characterId}", func(r chi.Router) {
		r.Get("/", service.GetEquipment)
		r.Get("/stats", service.GetEquipmentStats)
	})

	// Item endpoints
	r.Get("/items/{itemId}", service.GetItem)
	r.Post("/items/search", service.SearchItems)

	server := &InventoryServer{
		router:  r,
		logger:  logger,
		service: service,
		metrics: metrics,
	}

	return server, nil
}

func (s *InventoryServer) Router() *chi.Mux {
	return s.router
}

func (s *InventoryServer) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"inventory-service","version":"1.0.0"}`))
}
