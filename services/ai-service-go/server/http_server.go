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

// AIServer OPTIMIZATION: Issue #1968 - Memory-aligned struct for AI performance
type AIServer struct {
	router  *chi.Mux
	logger  *logrus.Logger
	service *AIService
	metrics *AIMetrics
}

// AIMetrics OPTIMIZATION: Issue #1968 - Struct field alignment (large → small)
type AIMetrics struct {
	RequestsTotal   prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveAI        prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	PathfindingOps  prometheus.Counter   `json:"-"` // 16 bytes (interface)
	DecisionOps     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	BehaviorOps     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	ConcurrentAI    prometheus.Gauge     `json:"-"` // 16 bytes (interface)
}

func NewAIServer(logger *logrus.Logger) (*AIServer, error) {
	// Initialize metrics
	metrics := &AIMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "ai_requests_total",
			Help: "Total number of requests to AI service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "ai_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveAI: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "ai_active_entities",
			Help: "Number of active AI entities",
		}),
		PathfindingOps: promauto.NewCounter(prometheus.CounterOpts{
			Name: "ai_pathfinding_operations_total",
			Help: "Total number of pathfinding operations",
		}),
		DecisionOps: promauto.NewCounter(prometheus.CounterOpts{
			Name: "ai_decision_operations_total",
			Help: "Total number of decision operations",
		}),
		BehaviorOps: promauto.NewCounter(prometheus.CounterOpts{
			Name: "ai_behavior_operations_total",
			Help: "Total number of behavior operations",
		}),
		ConcurrentAI: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "ai_concurrent_entities",
			Help: "Number of concurrently processed AI entities",
		}),
	}

	// Initialize service
	service := NewAIService(logger, metrics)

	// Create router with AI-specific optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #1968 - CORS middleware for game clients
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #1968 - AI-specific middlewares
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(5 * time.Second)) // OPTIMIZATION: 5s timeout for AI operations

	// OPTIMIZATION: Issue #1968 - Metrics middleware for AI performance monitoring
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
			}).Debug("AI request completed")
		})
	})

	// Health check
	r.Get("/health", service.HealthCheck)

	// AI behavior endpoints
	r.Route("/ai/{npcId}", func(r chi.Router) {
		r.Get("/behavior", service.GetNPCBehavior)
		r.Post("/behavior/execute", service.ExecuteNPCBehavior)
		r.Post("/pathfind", service.CalculatePath)
		r.Post("/decision", service.MakeDecision)
	})

	// Behavior trees endpoints
	r.Route("/ai/behavior-trees", func(r chi.Router) {
		r.Get("/", service.ListBehaviorTrees)
		r.Get("/{treeId}", service.GetBehaviorTree)
		r.Post("/{treeId}/execute", service.ExecuteBehaviorTree)
	})

	server := &AIServer{
		router:  r,
		logger:  logger,
		service: service,
		metrics: metrics,
	}

	return server, nil
}

func (s *AIServer) Router() *chi.Mux {
	return s.router
}

func (s *AIServer) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"ai-service","version":"1.0.0"}`))
}
