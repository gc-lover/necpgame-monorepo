package service

import (
	"context"
	"fmt"
	"net/http"

	"go.uber.org/zap"

	"necpgame/services/economy-service-go/config"
	"necpgame/services/economy-service-go/internal/consumer"
	"necpgame/services/economy-service-go/internal/handlers"
	"necpgame/services/economy-service-go/internal/repository"
	"necpgame/services/economy-service-go/internal/simulation/bazaar"
	api "necpgame/services/economy-service-go/pkg/services/economy-service-go/pkg/api"
)

// Service represents the economy service
// Implements consumer.Service interface for event-driven operations
// Issue: #2237
type Service struct {
	logger   *zap.Logger
	repo     *repository.Repository
	config   *config.Config
	server   *api.Server
	handlers *handlers.EconomyHandlers
	consumer *consumer.TickConsumer // Kafka consumer for tick events
}

// NewService creates a new economy service
// Initializes Kafka consumer for event-driven market clearing
// Issue: #2237
func NewService(logger *zap.Logger, repo *repository.Repository, cfg *config.Config) *Service {
	// Create handlers
	economyHandlers := handlers.NewEconomyHandlers(logger)

	// Create server
	server, err := api.NewServer(economyHandlers)
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	service := &Service{
		logger:   logger,
		repo:     repo,
		config:   cfg,
		server:   server,
		handlers: economyHandlers,
	}

	// Initialize Kafka consumer for event-driven architecture
	// Issue: #2237
	consumerConfig := consumer.ConsumerConfig{
		Brokers:            cfg.Kafka.Brokers,
		GroupID:            cfg.Kafka.GroupID,
		Topic:              "world.tick.hourly",
		SessionTimeout:     cfg.Kafka.SessionTimeout,
		HeartbeatInterval:  cfg.Kafka.HeartbeatInterval,
		CommitInterval:     cfg.Kafka.CommitInterval,
		MaxProcessingTime:  cfg.Kafka.MaxProcessingTime,
	}

	service.consumer = consumer.NewTickConsumer(service, consumerConfig)

	return service
}

// ServeHTTP implements http.Handler interface
func (s *Service) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	s.server.ServeHTTP(w, r)
}

// ClearMarkets implements consumer.Service interface
// Triggers bazaar market clearing for all commodities when tick event is received
// Returns market results and coordinates with bazaar simulation
// Issue: #2237
func (s *Service) ClearMarkets(ctx context.Context, tickID string) ([]bazaar.MarketResult, error) {
	s.logger.Info("Starting market clearing for tick",
		zap.String("tick_id", tickID))

	commodities := []bazaar.Commodity{
		bazaar.CommodityFood,
		bazaar.CommodityWood,
		bazaar.CommodityMetal,
		bazaar.CommodityWeapon,
		bazaar.CommodityCrystal,
	}

	results := make([]bazaar.MarketResult, 0, len(commodities))

	for _, commodity := range commodities {
		s.logger.Info("Processing market clearing",
			zap.String("tick_id", tickID),
			zap.String("commodity", string(commodity)))

		// Ensure default agents exist for this commodity
		if err := s.repo.CreateDefaultAgents(ctx, commodity); err != nil {
			s.logger.Warn("Failed to create default agents",
				zap.String("commodity", string(commodity)),
				zap.Error(err))
		}

		// Get active agents from database for this commodity
		dbAgents, err := s.repo.GetActiveAgents(ctx, commodity, 20)
		if err != nil {
			s.logger.Error("Failed to get agents from database",
				zap.String("commodity", string(commodity)),
				zap.Error(err))
			// Use empty agent list as fallback
			agents := []*bazaar.AgentLogic{}
			market := bazaar.NewMarketLogic(commodity)
			result := market.Clear(agents)

			results = append(results, result)
			s.logger.Info("Market cleared with empty agents",
				zap.String("commodity", string(commodity)),
				zap.Float64("price", result.NewPrices[commodity]),
				zap.Int("volume", result.TotalVolume))
			continue
		}

		// Convert database agents to bazaar agents
		agents := make([]*bazaar.AgentLogic, len(dbAgents))
		for i, dbAgent := range dbAgents {
			agents[i] = s.repo.ConvertToBazaarAgent(dbAgent)
		}

		// Create market instance and clear with agents
		market := bazaar.NewMarketLogic(commodity)
		result := market.Clear(agents)

		// Update agent states in database after market clearing
		for i, agent := range agents {
			dbAgent := dbAgents[i]
			if err := s.repo.UpdateAgentState(ctx, dbAgent.ID, agent.State.Wealth, agent.State.Inventory[commodity]); err != nil {
				s.logger.Warn("Failed to update agent state",
					zap.String("agent_name", dbAgent.Name),
					zap.Error(err))
			}
		}

		results = append(results, result)
		s.logger.Info("Market cleared with agents",
			zap.String("commodity", string(commodity)),
			zap.Float64("price", result.NewPrices[commodity]),
			zap.Int("volume", result.TotalVolume),
			zap.Float64("efficiency", result.MarketEfficiency))
	}

	s.logger.Info("Market clearing completed",
		zap.String("tick_id", tickID),
		zap.Int("markets_processed", len(results)))

	return results, nil
}

// GetLogger implements consumer.Service interface
// Returns the service logger for consumer operations
// Issue: #2237
func (s *Service) GetLogger() *zap.Logger {
	return s.logger
}

// StartConsumer starts the Kafka consumer for tick events
// Begins processing world.tick.hourly events
// Issue: #2237
func (s *Service) StartConsumer(ctx context.Context) error {
	if s.consumer == nil {
		return fmt.Errorf("consumer not initialized")
	}

	s.logger.Info("Starting Kafka consumer for tick events")
	return s.consumer.Start(ctx)
}

// StopConsumer gracefully stops the Kafka consumer
// Ensures all pending messages are processed
// Issue: #2237
func (s *Service) StopConsumer() error {
	if s.consumer == nil {
		return nil // Nothing to stop
	}

	s.logger.Info("Stopping Kafka consumer")
	return s.consumer.Stop()
}

// HealthCheck returns overall service health including consumer status
// Issue: #2237
func (s *Service) HealthCheck() error {
	// Check database connectivity
	if err := s.repo.HealthCheck(); err != nil {
		return fmt.Errorf("database health check failed: %w", err)
	}

	// Check Kafka consumer health
	if s.consumer != nil {
		if err := s.consumer.HealthCheck(); err != nil {
			return fmt.Errorf("consumer health check failed: %w", err)
		}
	}

	return nil
}