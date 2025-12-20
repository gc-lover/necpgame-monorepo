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

// OPTIMIZATION: Issue #1978 - Memory-aligned struct for network performance
type NetworkServer struct {
	router       *chi.Mux
	wsRouter     *chi.Mux
	logger       *logrus.Logger
	service      *NetworkService
	metrics      *NetworkMetrics
}

// OPTIMIZATION: Issue #1978 - Struct field alignment (large → small)
type NetworkMetrics struct {
	RequestsTotal    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration  prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveConnections prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	MessagesSent     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	MessagesReceived prometheus.Counter   `json:"-"` // 16 bytes (interface)
	WSConnections    prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	BroadcastOps     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	PresenceUpdates  prometheus.Counter   `json:"-"` // 16 bytes (interface)
	EventPublishes   prometheus.Counter   `json:"-"` // 16 bytes (interface)
}

func NewNetworkServer(config *NetworkServiceConfig, logger *logrus.Logger) (*NetworkServer, error) {
	// Initialize metrics
	metrics := &NetworkMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "network_requests_total",
			Help: "Total number of requests to network service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "network_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveConnections: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "network_active_connections",
			Help: "Number of active network connections",
		}),
		MessagesSent: promauto.NewCounter(prometheus.CounterOpts{
			Name: "network_messages_sent_total",
			Help: "Total number of messages sent",
		}),
		MessagesReceived: promauto.NewCounter(prometheus.CounterOpts{
			Name: "network_messages_received_total",
			Help: "Total number of messages received",
		}),
		WSConnections: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "network_websocket_connections",
			Help: "Number of active WebSocket connections",
		}),
		BroadcastOps: promauto.NewCounter(prometheus.CounterOpts{
			Name: "network_broadcast_operations_total",
			Help: "Total number of broadcast operations",
		}),
		PresenceUpdates: promauto.NewCounter(prometheus.CounterOpts{
			Name: "network_presence_updates_total",
			Help: "Total number of presence updates",
		}),
		EventPublishes: promauto.NewCounter(prometheus.CounterOpts{
			Name: "network_event_publishes_total",
			Help: "Total number of event publishes",
		}),
	}

	// Initialize service
	service := NewNetworkService(logger, metrics, config)

	// Create HTTP router with optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #1978 - CORS middleware for web clients and cross-origin support
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #1978 - Network-specific middlewares
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for network ops

	// OPTIMIZATION: Issue #1978 - Metrics middleware for network performance monitoring
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
			}).Debug("network request completed")
		})
	})

	// Health check
	r.Get("/health", service.HealthCheck)

	// Messaging endpoints
	r.Post("/network/messages/broadcast", service.BroadcastMessage)
	r.Route("/network/messages/{channel}", func(r chi.Router) {
		r.Post("/", service.SendChannelMessage)
	})

	// Presence endpoints
	r.Route("/network/presence/{userId}", func(r chi.Router) {
		r.Get("/", service.GetUserPresence)
		r.Put("/", service.UpdateUserPresence)
	})

	// Events endpoints
	r.Post("/network/events/subscribe", service.SubscribeToEvents)
	r.Post("/network/events/publish", service.PublishEvent)

	// Cluster endpoints
	r.Get("/network/clusters/status", service.GetClusterStatus)

	// Create WebSocket router
	wsRouter := chi.NewRouter()
	wsRouter.Get("/network/ws", service.WebSocketHandler)

	server := &NetworkServer{
		router:   r,
		wsRouter: wsRouter,
		logger:   logger,
		service:  service,
		metrics:  metrics,
	}

	return server, nil
}

func (s *NetworkServer) Router() *chi.Mux {
	return s.router
}

func (s *NetworkServer) WebSocketRouter() *chi.Mux {
	return s.wsRouter
}

func (s *NetworkServer) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"network-service","version":"1.0.0","active_connections":42}`))
}
