// Issue: #2203 - Production chain service implementation
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// ChainService handles production chain business logic
type ChainService struct {
	repo     *ChainRepository
	orderSvc OrderServiceInterface
	redis    *redis.Client
	logger   *logrus.Logger
}

// NewChainService creates new chain service
func NewChainService(repo *ChainRepository, orderSvc OrderServiceInterface, redisClient *redis.Client) ChainServiceInterface {
	return &ChainService{
		repo:     repo,
		orderSvc: orderSvc,
		redis:    redisClient,
		logger:   GetLogger(),
	}
}

// GetProductionChain retrieves production chain by ID
func (s *ChainService) GetProductionChain(ctx context.Context, chainID uuid.UUID) (*ProductionChain, error) {
	chain, err := s.repo.GetByID(ctx, chainID)
	if err != nil {
		return nil, fmt.Errorf("failed to get production chain: %w", err)
	}

	return chain, nil
}

// ListProductionChains retrieves chains with pagination
func (s *ChainService) ListProductionChains(ctx context.Context, playerID *uuid.UUID, status *string, limit, offset int) ([]ProductionChain, int, error) {
	chains, total, err := s.repo.List(ctx, playerID, status, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list production chains: %w", err)
	}

	return chains, total, nil
}

// CreateProductionChain creates new production chain
func (s *ChainService) CreateProductionChain(ctx context.Context, chain *ProductionChain) error {
	// Validate chain
	if err := s.validateChain(chain); err != nil {
		return fmt.Errorf("invalid production chain: %w", err)
	}

	// Set defaults
	now := time.Now()
	chain.ID = uuid.New()
	chain.Status = "planning"
	chain.CurrentStage = 0
	chain.TotalProgress = 0.0
	chain.CreatedAt = now

	// Validate and create orders for each stage
	for i, stage := range chain.Stages {
		stage.Sequence = i + 1

		// Check if recipe exists
		if _, err := s.getRecipeByID(ctx, stage.OrderID); err != nil {
			return fmt.Errorf("recipe not found for stage %d: %w", i+1, err)
		}
	}

	// Save to database
	if err := s.repo.Create(ctx, chain); err != nil {
		return fmt.Errorf("failed to create production chain: %w", err)
	}

	s.logger.WithFields(logrus.Fields{
		"chain_id":   chain.ID,
		"name":       chain.Name,
		"stages":     len(chain.Stages),
		"player_id":  chain.PlayerID,
	}).Info("Production chain created successfully")

	return nil
}

// UpdateProductionChain updates existing chain
func (s *ChainService) UpdateProductionChain(ctx context.Context, chain *ProductionChain) error {
	if err := s.validateChain(chain); err != nil {
		return fmt.Errorf("invalid production chain: %w", err)
	}

	now := time.Now()
	chain.UpdatedAt = &now

	if err := s.repo.Update(ctx, chain); err != nil {
		return fmt.Errorf("failed to update production chain: %w", err)
	}

	return nil
}

// DeleteProductionChain removes chain
func (s *ChainService) DeleteProductionChain(ctx context.Context, chainID uuid.UUID) error {
	chain, err := s.repo.GetByID(ctx, chainID)
	if err != nil {
		return fmt.Errorf("production chain not found: %w", err)
	}

	if chain.Status == "active" {
		return fmt.Errorf("cannot delete active production chain")
	}

	if err := s.repo.Delete(ctx, chainID); err != nil {
		return fmt.Errorf("failed to delete production chain: %w", err)
	}

	s.logger.WithField("chain_id", chainID).Info("Production chain deleted successfully")

	return nil
}

// StartChain starts chain processing
func (s *ChainService) StartChain(ctx context.Context, chainID uuid.UUID) error {
	chain, err := s.repo.GetByID(ctx, chainID)
	if err != nil {
		return fmt.Errorf("production chain not found: %w", err)
	}

	if chain.Status != "planning" {
		return fmt.Errorf("can only start chains in planning status")
	}

	now := time.Now()
	chain.Status = "active"
	chain.StartedAt = &now
	chain.UpdatedAt = &now

	// Start first stage
	if len(chain.Stages) > 0 {
		chain.CurrentStage = 1
		// TODO: Create order for first stage
	}

	if err := s.repo.Update(ctx, chain); err != nil {
		return fmt.Errorf("failed to start production chain: %w", err)
	}

	s.logger.WithField("chain_id", chainID).Info("Production chain started successfully")

	return nil
}

// PauseChain pauses active chain
func (s *ChainService) PauseChain(ctx context.Context, chainID uuid.UUID) error {
	chain, err := s.repo.GetByID(ctx, chainID)
	if err != nil {
		return fmt.Errorf("production chain not found: %w", err)
	}

	if chain.Status != "active" {
		return fmt.Errorf("can only pause active chains")
	}

	chain.Status = "paused"
	now := time.Now()
	chain.UpdatedAt = &now

	if err := s.repo.Update(ctx, chain); err != nil {
		return fmt.Errorf("failed to pause production chain: %w", err)
	}

	s.logger.WithField("chain_id", chainID).Info("Production chain paused successfully")

	return nil
}

// ResumeChain resumes paused chain
func (s *ChainService) ResumeChain(ctx context.Context, chainID uuid.UUID) error {
	chain, err := s.repo.GetByID(ctx, chainID)
	if err != nil {
		return fmt.Errorf("production chain not found: %w", err)
	}

	if chain.Status != "paused" {
		return fmt.Errorf("can only resume paused chains")
	}

	chain.Status = "active"
	now := time.Now()
	chain.UpdatedAt = &now

	if err := s.repo.Update(ctx, chain); err != nil {
		return fmt.Errorf("failed to resume production chain: %w", err)
	}

	s.logger.WithField("chain_id", chainID).Info("Production chain resumed successfully")

	return nil
}

// validateChain validates chain data
func (s *ChainService) validateChain(chain *ProductionChain) error {
	if chain.Name == "" {
		return fmt.Errorf("chain name is required")
	}

	if len(chain.Stages) < 2 {
		return fmt.Errorf("chain must have at least 2 stages")
	}

	if len(chain.Stages) > 10 {
		return fmt.Errorf("chain cannot have more than 10 stages")
	}

	// Validate stage dependencies
	for i, stage := range chain.Stages {
		if stage.Sequence != i+1 {
			return fmt.Errorf("stage sequence mismatch at index %d", i)
		}

		// Check dependencies are valid
		for _, dep := range stage.Dependencies {
			if dep < 1 || dep >= stage.Sequence {
				return fmt.Errorf("invalid dependency %d in stage %d", dep, stage.Sequence)
			}
		}
	}

	return nil
}

// getRecipeByID helper to get recipe (mock implementation)
func (s *ChainService) getRecipeByID(ctx context.Context, recipeID uuid.UUID) (*Recipe, error) {
	// TODO: Get recipe from recipe service
	// For now, return mock recipe
	return &Recipe{ID: recipeID, Name: "Mock Recipe"}, nil
}
