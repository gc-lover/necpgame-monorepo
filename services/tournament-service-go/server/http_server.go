package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/sirupsen/logrus"
)

// TournamentServiceConfig holds configuration for the Tournament Service
type TournamentServiceConfig struct {
	Port                   string
	ReadTimeout            time.Duration
	WriteTimeout           time.Duration
	MaxHeaderBytes         int
	RedisAddr              string
	TournamentUpdateInterval time.Duration
	MatchUpdateInterval    time.Duration
	RankingUpdateInterval  time.Duration
	LeagueUpdateInterval   time.Duration
	StatsCleanupInterval   time.Duration
}

// TournamentMetrics holds Prometheus metrics for the Tournament Service
type TournamentMetrics struct {
	// Metrics implementation would go here
	// For now, using placeholder counters
	ActiveTournaments     float64
	ActiveMatches         float64
	TotalParticipants     float64
	CompletedTournaments  float64
	ActiveLeagues         float64
	ValidationErrors      float64
	MatchCreations        float64
	RankingUpdates        float64
	RewardClaims          float64
}

// HTTP server for Tournament Service
type HTTPServer struct {
	service *TournamentService
	logger  *logrus.Logger
	config  *TournamentServiceConfig
}

// NewHTTPServer creates a new HTTP server for Tournament Service
func NewHTTPServer(service *TournamentService, logger *logrus.Logger, config *TournamentServiceConfig) *HTTPServer {
	return &HTTPServer{
		service: service,
		logger:  logger,
		config:  config,
	}
}

// SetupRoutes configures all HTTP routes for the Tournament Service
func (hs *HTTPServer) SetupRoutes() *chi.Mux {
	r := chi.NewRouter()

	// Middleware stack
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	// CORS middleware for web client support
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure based on environment
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	// Rate limiting middleware
	r.Use(hs.service.RateLimitMiddleware())

	// Health check endpoint
	r.Get("/tournament/health", hs.service.HealthCheck)

	// Tournament management routes
	r.Route("/tournament/tournaments", func(r chi.Router) {
		r.Get("/", hs.service.ListTournaments)           // List tournaments with filtering
		r.Post("/", hs.service.CreateTournament)         // Create tournament

		r.Route("/{tournamentId}", func(r chi.Router) {
			r.Get("/", hs.service.GetTournament)               // Get tournament details
			r.Put("/", hs.service.UpdateTournament)            // Update tournament
			r.Delete("/", hs.service.CancelTournament)         // Cancel tournament

			// Registration management
			r.Post("/register", hs.service.RegisterForTournament)     // Register for tournament
			r.Post("/unregister", hs.service.UnregisterFromTournament) // Unregister from tournament

			// Tournament data
			r.Get("/bracket", hs.service.GetTournamentBracket) // Get tournament bracket
			r.Get("/rewards", hs.service.GetTournamentRewards)  // Get tournament rewards
			r.Post("/rewards/claim", hs.service.ClaimRewards)   // Claim rewards
		})
	})

	// Match management routes
	r.Route("/tournament/matches", func(r chi.Router) {
		r.Get("/", hs.service.ListMatches) // List matches with filtering

		r.Route("/{matchId}", func(r chi.Router) {
			r.Get("/", hs.service.GetMatch)                    // Get match details
			r.Put("/", hs.service.UpdateMatchResult)          // Update match result
		})
	})

	// Ranking routes
	r.Get("/tournament/rankings", hs.service.GetRankings) // Get global rankings
	r.Route("/tournament/rankings/{playerId}", func(r chi.Router) {
		r.Get("/", hs.service.GetPlayerRanking) // Get player ranking
	})

	// League routes
	r.Route("/tournament/leagues", func(r chi.Router) {
		r.Get("/", hs.service.ListLeagues)   // List leagues
		r.Post("/", hs.service.CreateLeague) // Create league

		r.Route("/{leagueId}/join", func(r chi.Router) {
			r.Post("/", hs.service.JoinLeague) // Join league
		})
	})

	// Statistics routes
	r.Get("/tournament/statistics", hs.service.GetGlobalStatistics) // Get global statistics

	return r
}

// Start starts the HTTP server
func (hs *HTTPServer) Start() error {
	r := hs.SetupRoutes()

	server := &http.Server{
		Addr:           ":" + hs.config.Port,
		Handler:        r,
		ReadTimeout:    hs.config.ReadTimeout,
		WriteTimeout:   hs.config.WriteTimeout,
		MaxHeaderBytes: hs.config.MaxHeaderBytes,
	}

	hs.logger.WithField("port", hs.config.Port).Info("tournament service HTTP server starting")

	if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		hs.logger.WithError(err).Error("tournament service HTTP server failed")
		return err
	}

	return nil
}