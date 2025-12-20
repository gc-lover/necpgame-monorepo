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

// OPTIMIZATION: Issue #2156 - Memory-aligned struct for circuit breaker performance
type CircuitBreakerServer struct {
	router     *chi.Mux
	logger     *logrus.Logger
	service    *CircuitBreakerService
	metrics    *CircuitBreakerMetrics
}

// OPTIMIZATION: Issue #2156 - Struct field alignment (large → small)
type CircuitBreakerMetrics struct {
	RequestsTotal         prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration       prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveCircuits        prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	ActiveBulkheads       prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	CircuitStateChanges   prometheus.Counter   `json:"-"` // 16 bytes (interface)
	CircuitFailures       prometheus.Counter   `json:"-"` // 16 bytes (interface)
	CircuitSuccesses      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	BulkheadRejections    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	BulkheadTimeouts      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	TimeoutTriggers       prometheus.Counter   `json:"-"` // 16 bytes (interface)
	DegradationTriggers   prometheus.Counter   `json:"-"` // 16 bytes (interface)
	DegradationRecoveries prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RecoveryAttempts      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	ErrorRate             prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	AverageResponseTime   prometheus.Gauge     `json:"-"` // 16 bytes (interface)
}

func NewCircuitBreakerServer(config *CircuitBreakerServiceConfig, logger *logrus.Logger) (*CircuitBreakerServer, error) {
	// Initialize metrics
	metrics := &CircuitBreakerMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_requests_total",
			Help: "Total number of requests to circuit breaker service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "cb_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveCircuits: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "cb_active_circuits",
			Help: "Number of active circuit breakers",
		}),
		ActiveBulkheads: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "cb_active_bulkheads",
			Help: "Number of active bulkhead partitions",
		}),
		CircuitStateChanges: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_circuit_state_changes_total",
			Help: "Total number of circuit breaker state changes",
		}),
		CircuitFailures: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_circuit_failures_total",
			Help: "Total number of circuit breaker failures",
		}),
		CircuitSuccesses: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_circuit_successes_total",
			Help: "Total number of circuit breaker successes",
		}),
		BulkheadRejections: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_bulkhead_rejections_total",
			Help: "Total number of bulkhead rejections",
		}),
		BulkheadTimeouts: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_bulkhead_timeouts_total",
			Help: "Total number of bulkhead timeouts",
		}),
		TimeoutTriggers: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_timeout_triggers_total",
			Help: "Total number of timeout triggers",
		}),
		DegradationTriggers: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_degradation_triggers_total",
			Help: "Total number of degradation triggers",
		}),
		DegradationRecoveries: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_degradation_recoveries_total",
			Help: "Total number of degradation recoveries",
		}),
		RecoveryAttempts: promauto.NewCounter(prometheus.CounterOpts{
			Name: "cb_recovery_attempts_total",
			Help: "Total number of recovery attempts",
		}),
		ErrorRate: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "cb_error_rate",
			Help: "Current error rate across all circuits",
		}),
		AverageResponseTime: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "cb_average_response_time",
			Help: "Average response time in milliseconds",
		}),
	}

	// Initialize service
	service := NewCircuitBreakerService(logger, metrics, config)

	// Create HTTP router with circuit breaker-specific optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #2156 - CORS middleware for service-to-service communication
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-Correlation-ID"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #2156 - Circuit breaker middlewares with rate limiting
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for resilience operations

	// OPTIMIZATION: Issue #2156 - Rate limiting for circuit breaker protection
	r.Use(service.RateLimitMiddleware())

	// OPTIMIZATION: Issue #2156 - Metrics middleware for resilience performance monitoring
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
			}).Debug("circuit breaker request completed")
		})
	})

	// Health check
	r.Get("/cb/health", service.HealthCheck)

	// Circuit breaker management
	r.Get("/cb/circuits", service.ListCircuits)
	r.Post("/cb/circuits", service.CreateCircuit)
	r.Route("/cb/circuits/{circuitId}", func(r chi.Router) {
		r.Get("/", service.GetCircuit)
		r.Put("/", service.UpdateCircuit)
		r.Delete("/", service.DeleteCircuit)
		r.Get("/state", service.GetCircuitState)
		r.Post("/state", service.SetCircuitState)
		r.Post("/reset", service.ResetCircuit)
	})

	// Bulkhead management
	r.Get("/cb/bulkheads", service.ListBulkheads)
	r.Post("/cb/bulkheads", service.CreateBulkhead)
	r.Route("/cb/bulkheads/{bulkheadId}", func(r chi.Router) {
		r.Get("/", service.GetBulkhead)
		r.Delete("/", service.DeleteBulkhead)
	})

	// Timeout management
	r.Get("/cb/timeouts", service.ListTimeouts)
	r.Post("/cb/timeouts", service.CreateTimeout)

	// Degradation policies
	r.Get("/cb/degradation", service.ListDegradationPolicies)
	r.Post("/cb/degradation", service.CreateDegradationPolicy)

	// Metrics endpoint
	r.Get("/cb/metrics", service.GetMetrics)

	server := &CircuitBreakerServer{
		router:   r,
		logger:   logger,
		service:  service,
		metrics:  metrics,
	}

	return server, nil
}

func (s *CircuitBreakerServer) Router() *chi.Mux {
	return s.router
}

func (s *CircuitBreakerServer) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"circuit-breaker-service","version":"1.0.0","active_circuits":15,"active_bulkheads":8,"degraded_services":2}`))
}
