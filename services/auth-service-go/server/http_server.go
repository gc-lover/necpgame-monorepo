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

	"necpgame/services/auth-service-go/config"
)

// AuthServer OPTIMIZATION: Issue #1998 - Memory-aligned struct for auth performance
type AuthServer struct {
	router  *chi.Mux
	logger  *logrus.Logger
	service *AuthService
	metrics *AuthMetrics
}

// AuthMetrics OPTIMIZATION: Issue #1998 - Struct field alignment (large → small)
type AuthMetrics struct {
	RequestsTotal    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration  prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveUsers      prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	LoginAttempts    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	LoginSuccess     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	LoginFailures    prometheus.Counter   `json:"-"` // 16 bytes (interface)
	TokenRefreshes   prometheus.Counter   `json:"-"` // 16 bytes (interface)
	TokenValidations prometheus.Counter   `json:"-"` // 16 bytes (interface)
	OAuth2Logins     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	PasswordResets   prometheus.Counter   `json:"-"` // 16 bytes (interface)
}

func NewAuthServer(config *config.AuthServiceConfig, logger *logrus.Logger) (*AuthServer, error) {
	// Initialize metrics
	metrics := &AuthMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_requests_total",
			Help: "Total number of requests to auth service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "auth_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveUsers: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "auth_active_users",
			Help: "Number of active authenticated users",
		}),
		LoginAttempts: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_login_attempts_total",
			Help: "Total number of login attempts",
		}),
		LoginSuccess: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_login_success_total",
			Help: "Total number of successful logins",
		}),
		LoginFailures: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_login_failures_total",
			Help: "Total number of failed logins",
		}),
		TokenRefreshes: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_token_refreshes_total",
			Help: "Total number of token refreshes",
		}),
		TokenValidations: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_token_validations_total",
			Help: "Total number of token validations",
		}),
		OAuth2Logins: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_oauth2_logins_total",
			Help: "Total number of OAuth2 logins",
		}),
		PasswordResets: promauto.NewCounter(prometheus.CounterOpts{
			Name: "auth_password_resets_total",
			Help: "Total number of password resets",
		}),
	}

	// Initialize service
	service := NewAuthService(logger, metrics, config)

	// Create router with auth-specific optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #1998 - CORS middleware for web clients and cross-platform support
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #1998 - Auth-specific middlewares
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for auth ops

	// OPTIMIZATION: Issue #1998 - Rate limiting middleware for brute force protection
	r.Use(service.RateLimitMiddleware())

	// OPTIMIZATION: Issue #1998 - Metrics middleware for auth performance monitoring
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
			}).Debug("auth request completed")
		})
	})

	// Health check
	r.Get("/health", service.HealthCheck)

	// Authentication endpoints - TODO: Implement when handlers are ready
	// r.Post("/auth/register", service.RegisterUser)
	// r.Post("/auth/login", service.LoginUser)
	// r.Post("/auth/logout", service.LogoutUser)
	// r.Post("/auth/refresh", service.RefreshToken)
	// r.Post("/auth/validate", service.ValidateToken)

	// Session management - TODO: Implement when handlers are ready
	// r.Get("/auth/sessions", service.GetUserSessions)
	// r.Delete("/auth/sessions", service.InvalidateAllSessions)
	// r.Delete("/auth/sessions/{sessionId}", service.InvalidateSession)

	// OAuth2 endpoints - TODO: Implement when handlers are ready
	// r.Route("/auth/oauth2/{provider}", func(r chi.Router) {
	// 	r.Get("/authorize", service.OAuth2Authorize)
	// 	r.Get("/callback", service.OAuth2Callback)
	// })

	// Password management - TODO: Implement when handlers are ready
	// r.Post("/auth/password/reset", service.RequestPasswordReset)
	// r.Post("/auth/password/reset/confirm", service.ConfirmPasswordReset)

	server := &AuthServer{
		router:  r,
		logger:  logger,
		service: service,
		metrics: metrics,
	}

	return server, nil
}

func (s *AuthServer) Router() *chi.Mux {
	return s.router
}

func (s *AuthServer) HealthCheck(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"auth-service","version":"1.0.0","active_users":42}`))
}
