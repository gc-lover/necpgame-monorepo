package service

import (
	"net/http"

	"go.uber.org/zap"

	"necpgame/services/economy-service-go/config"
	"necpgame/services/economy-service-go/internal/handlers"
	"necpgame/services/economy-service-go/internal/repository"
	api "necpgame/services/economy-service-go/pkg/services/economy-service-go/pkg/api"
)

// Service represents the economy service
type Service struct {
	logger     *zap.Logger
	repo       *repository.Repository
	config     *config.Config
	server     *api.Server
	handlers   *handlers.EconomyHandlers
	security   *SecurityHandler
}

// NewService creates a new economy service
func NewService(logger *zap.Logger, repo *repository.Repository, cfg *config.Config) *Service {
	// Create handlers
	economyHandlers := handlers.NewEconomyHandlers(logger)

	// Create security handler
	securityHandler := NewSecurityHandler(cfg, logger)

	// Create server
	server, err := api.NewServer(economyHandlers, securityHandler)
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	return &Service{
		logger:   logger,
		repo:     repo,
		config:   cfg,
		server:   server,
		handlers: economyHandlers,
		security: securityHandler,
	}
}

// ServeHTTP implements http.Handler interface
func (s *Service) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	s.server.ServeHTTP(w, r)
}