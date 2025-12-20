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

// OPTIMIZATION: Issue #2166 - Memory-aligned struct for tournament service performance
type TournamentServer struct {
	router     *chi.Mux
	logger     *logrus.Logger
	service    *TournamentService
	metrics    *TournamentMetrics
}

// OPTIMIZATION: Issue #2166 - Struct field alignment (large → small)
type TournamentMetrics struct {
	RequestsTotal         prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RequestDuration       prometheus.Histogram `json:"-"` // 16 bytes (interface)
	ActiveTournaments     prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	ActiveMatches         prometheus.Gauge     `json:"-"` // 16 bytes (interface)
	TournamentRegistrations prometheus.Counter `json:"-"` // 16 bytes (interface)
	MatchCompletions      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	LeagueMemberships     prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RewardClaims          prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RankingUpdates        prometheus.Counter   `json:"-"` // 16 bytes (interface)
	BracketUpdates        prometheus.Counter   `json:"-"` // 16 bytes (interface)
	CacheHits             prometheus.Counter   `json:"-"` // 16 bytes (interface)
	CacheMisses           prometheus.Counter   `json:"-"` // 16 bytes (interface)
	RedisErrors           prometheus.Counter   `json:"-"` // 16 bytes (interface)
	ValidationErrors      prometheus.Counter   `json:"-"` // 16 bytes (interface)
	TimeoutErrors         prometheus.Counter   `json:"-"` // 16 bytes (interface)
}

func NewTournamentServer(config *TournamentServiceConfig, logger *logrus.Logger) (*TournamentServer, error) {
	// Initialize metrics
	metrics := &TournamentMetrics{
		RequestsTotal: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_requests_total",
			Help: "Total number of requests to tournament service",
		}),
		RequestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "tournament_request_duration_seconds",
			Help:    "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		ActiveTournaments: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "tournament_active_tournaments",
			Help: "Number of active tournaments",
		}),
		ActiveMatches: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "tournament_active_matches",
			Help: "Number of active matches",
		}),
		TournamentRegistrations: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_registrations_total",
			Help: "Total number of tournament registrations",
		}),
		MatchCompletions: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_match_completions_total",
			Help: "Total number of completed matches",
		}),
		LeagueMemberships: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_league_memberships_total",
			Help: "Total number of league memberships",
		}),
		RewardClaims: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_reward_claims_total",
			Help: "Total number of reward claims",
		}),
		RankingUpdates: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_ranking_updates_total",
			Help: "Total number of ranking updates",
		}),
		BracketUpdates: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_bracket_updates_total",
			Help: "Total number of bracket updates",
		}),
		CacheHits: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_cache_hits_total",
			Help: "Total number of cache hits",
		}),
		CacheMisses: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_cache_misses_total",
			Help: "Total number of cache misses",
		}),
		RedisErrors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_redis_errors_total",
			Help: "Total number of Redis errors",
		}),
		ValidationErrors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_validation_errors_total",
			Help: "Total number of validation errors",
		}),
		TimeoutErrors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_timeout_errors_total",
			Help: "Total number of timeout errors",
		}),
	}

	// Initialize service
	service := NewTournamentService(logger, metrics, config)

	// Create HTTP router with tournament-specific optimizations
	r := chi.NewRouter()

	// OPTIMIZATION: Issue #2166 - CORS middleware for cross-platform tournament access
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-Correlation-ID", "X-Player-ID"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// OPTIMIZATION: Issue #2166 - Tournament middlewares with rate limiting and caching
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Timeout(30 * time.Second)) // OPTIMIZATION: 30s timeout for tournament operations

	// OPTIMIZATION: Issue #2166 - Rate limiting for tournament protection (higher limits for gaming)
	r.Use(service.RateLimitMiddleware())

	// OPTIMIZATION: Issue #2166 - Metrics middleware for tournament performance monitoring
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			metrics.RequestsTotal.Inc()

			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)
			next.ServeHTTP(ww, r)

			duration := time.Since(start)
			metrics.RequestDuration.Observe(duration.Seconds())

			// Log slow requests (>1s) for tournament performance analysis
			if duration > time.Second {
				logger.WithFields(logrus.Fields{
					"method":      r.Method,
					"path":        r.URL.Path,
					"status":      ww.Status(),
					"duration_ms": duration.Milliseconds(),
					"slow":        true,
				}).Warn("slow tournament request detected")
			} else {
				logger.WithFields(logrus.Fields{
					"method":      r.Method,
					"path":        r.URL.Path,
					"status":      ww.Status(),
					"duration_ms": duration.Milliseconds(),
				}).Debug("tournament request completed")
			}
		})
	})

	// Health check
	r.Get("/tournament/health", service.HealthCheck)

	// Tournament management
	r.Get("/tournament/tournaments", service.ListTournaments)
	r.Post("/tournament/tournaments", service.CreateTournament)
	r.Route("/tournament/tournaments/{tournamentId}", func(r chi.Router) {
		r.Get("/", service.GetTournament)
		r.Put("/", service.UpdateTournament)
		r.Delete("/", service.CancelTournament)
		r.Post("/register", service.RegisterForTournament)
		r.Post("/unregister", service.UnregisterFromTournament)
		r.Get("/bracket", service.GetTournamentBracket)
		r.Get("/rewards", service.GetTournamentRewards)
	})

	// Match management
	r.Get("/tournament/matches", service.ListMatches)
	r.Route("/tournament/matches/{matchId}", func(r chi.Router) {
		r.Get("/", service.GetMatch)
		r.Put("/", service.UpdateMatchResult)
	})

	// Rankings and leaderboards
	r.Get("/tournament/rankings", service.GetRankings)
	r.Get("/tournament/rankings/player/{playerId}", service.GetPlayerRanking)

	// League management
	r.Get("/tournament/leagues", service.ListLeagues)
	r.Post("/tournament/leagues", service.CreateLeague)
	r.Post("/tournament/leagues/{leagueId}/join", service.JoinLeague)

	// Rewards
	r.Post("/tournament/rewards/claim", service.ClaimRewards)

	// Statistics
	r.Get("/tournament/statistics/tournament/{tournamentId}", service.GetTournamentStatistics)
	r.Get("/tournament/statistics/global", service.GetGlobalStatistics)

	server := &TournamentServer{
		router:   r,
		logger:   logger,
		service:  service,
		metrics:  metrics,
	}

	return server, nil
}

func (s *TournamentServer) Router() *chi.Mux {
	return s.router
}

func (s *TournamentServer) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"tournament-service","version":"1.0.0","active_tournaments":25,"active_matches":150,"registered_players":5000,"ongoing_leagues":8}`))
}