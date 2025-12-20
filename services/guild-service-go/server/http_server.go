package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/sirupsen/logrus"
)

// GuildServiceConfig holds configuration for the Guild Service
type GuildServiceConfig struct {
	Port                    string
	ReadTimeout             time.Duration
	WriteTimeout            time.Duration
	MaxHeaderBytes          int
	RedisAddr               string
	TerritoryUpdateInterval time.Duration
	WarUpdateInterval       time.Duration
	StatsCleanupInterval    time.Duration
}

// GuildMetrics holds Prometheus metrics for the Guild Service
type GuildMetrics struct {
	// Metrics implementation would go here
	// For now, using placeholder counters
	ActiveGuilds       float64
	ActiveMembers      float64
	GuildCreations     float64
	GuildJoins         float64
	GuildLeaves        float64
	ValidationErrors   float64
	TerritoryClaims    float64
	WarDeclarations    float64
	AllianceFormations float64
	ContractCreations  float64
}

// HTTPServer HTTP server for Guild Service
type HTTPServer struct {
	service *GuildService
	logger  *logrus.Logger
	config  *GuildServiceConfig
}

// NewHTTPServer creates a new HTTP server for Guild Service
func NewHTTPServer(service *GuildService, logger *logrus.Logger, config *GuildServiceConfig) *HTTPServer {
	return &HTTPServer{
		service: service,
		logger:  logger,
		config:  config,
	}
}

// SetupRoutes configures all HTTP routes for the Guild Service
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
	r.Get("/health", hs.service.HealthCheck)

	// Guild management routes
	r.Route("/guilds", func(r chi.Router) {
		r.Post("/", hs.service.CreateGuild) // Create guild
		r.Get("/", hs.service.ListGuilds)   // List guilds with filtering

		r.Route("/{guildId}", func(r chi.Router) {
			r.Get("/", hs.service.GetGuild)       // Get guild details
			r.Put("/", hs.service.UpdateGuild)    // Update guild (leader only)
			r.Delete("/", hs.service.DeleteGuild) // Delete guild (leader only)

			// Member management
			r.Post("/join", hs.service.RequestJoinGuild)              // Request to join guild
			r.Post("/leave", hs.service.LeaveGuild)                   // Leave guild
			r.Get("/members", hs.service.GetGuildMembers)             // Get guild members
			r.Put("/members/{playerId}", hs.service.UpdateMemberRole) // Update member role (leader/officer only)

			// Territory management
			r.Post("/territories", hs.service.ClaimTerritory)                   // Claim territory
			r.Get("/territories", hs.service.ListGuildTerritories)              // List guild territories
			r.Delete("/territories/{territoryId}", hs.service.ReleaseTerritory) // Release territory

			// War management
			r.Post("/wars", hs.service.DeclareWar)       // Declare war
			r.Get("/wars", hs.service.ListGuildWars)     // List active wars
			r.Put("/wars/{warId}", hs.service.UpdateWar) // Update war status

			// Alliance management
			r.Post("/alliances", hs.service.FormAlliance)                 // Form alliance
			r.Get("/alliances", hs.service.ListGuildAlliances)            // List alliances
			r.Delete("/alliances/{allianceId}", hs.service.BreakAlliance) // Break alliance

			// Contract management
			r.Post("/contracts", hs.service.CreateContract)             // Create guild contract
			r.Get("/contracts", hs.service.ListGuildContracts)          // List guild contracts
			r.Put("/contracts/{contractId}", hs.service.UpdateContract) // Update contract

			// Guild bank/resources
			r.Get("/bank", hs.service.GetGuildBank)               // Get guild bank status
			r.Post("/bank/deposit", hs.service.DepositToBank)     // Deposit resources
			r.Post("/bank/withdraw", hs.service.WithdrawFromBank) // Withdraw resources (authorized members only)
		})
	})

	// Global routes
	r.Get("/leaderboard", hs.service.GetGuildLeaderboard) // Guild reputation/wealth leaderboard
	r.Get("/territories", hs.service.ListAllTerritories)  // List all territories and owners
	r.Get("/wars", hs.service.ListAllWars)                // List all active wars
	r.Get("/alliances", hs.service.ListAllAlliances)      // List all alliances

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

	hs.logger.WithField("port", hs.config.Port).Info("guild service HTTP server starting")

	if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		hs.logger.WithError(err).Error("guild service HTTP server failed")
		return err
	}

	return nil
}
