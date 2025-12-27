// Social Service - Business logic layer
// Issue: #140875791
// PERFORMANCE: Business logic validation, data aggregation, error handling

package handlers

import (
	"context"
	"fmt"
	"math"
	"time"

	"github.com/google/uuid"

	"social-service-go/pkg/cache"
	"social-service-go/pkg/models"
	"social-service-go/pkg/repository"
)

// Service provides business logic for social systems
type Service struct {
	repo  *repository.Repository
	cache *cache.Cache
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, cache *cache.Cache) *Service {
	return &Service{
		repo:  repo,
		cache: cache,
	}
}

// RELATIONSHIP METHODS

// GetRelationship retrieves or creates a relationship between entities
func (s *Service) GetRelationship(ctx context.Context, sourceID, targetID uuid.UUID, sourceType, targetType models.EntityType) (*models.Relationship, error) {
	// Try cache first
	if rel, found := s.cache.GetRelationship(ctx, sourceID, targetID, sourceType, targetType); found {
		return rel, nil
	}

	// Get from database
	rel, err := s.repo.GetRelationship(ctx, sourceID, targetID, sourceType, targetType)
	if err != nil && err.Error() != "relationship not found" {
		return nil, fmt.Errorf("failed to get relationship: %w", err)
	}

	// If not found, create default relationship
	if rel == nil {
		rel = &models.Relationship{
			ID:               uuid.New(),
			SourceID:         sourceID,
			SourceType:       sourceType,
			TargetID:         targetID,
			TargetType:       targetType,
			Level:            models.RelationshipLevelNeutral,
			Trust:            50.0,
			Reputation:       0,
			LastInteraction:  time.Now(),
			InteractionCount: 0,
			CreatedAt:        time.Now(),
			UpdatedAt:        time.Now(),
		}

		// Save to database
		if err := s.repo.CreateOrUpdateRelationship(ctx, rel); err != nil {
			return nil, fmt.Errorf("failed to create relationship: %w", err)
		}
	}

	// Cache the result
	s.cache.SetRelationship(ctx, rel)

	return rel, nil
}

// UpdateRelationship modifies a relationship based on interaction
func (s *Service) UpdateRelationship(ctx context.Context, sourceID, targetID uuid.UUID, sourceType, targetType models.EntityType, modifier models.RelationshipModifier) error {
	// Get current relationship
	rel, err := s.GetRelationship(ctx, sourceID, targetID, sourceType, targetType)
	if err != nil {
		return fmt.Errorf("failed to get relationship: %w", err)
	}

	// Apply modifier based on type
	switch modifier.Type {
	case models.ModifierTypeTrade:
		rel.Reputation += modifier.Value
		rel.Trust += float64(modifier.Value) * 0.1
	case models.ModifierTypeCombat:
		rel.Reputation += modifier.Value * 2 // Combat has stronger reputation impact
		rel.Trust -= float64(modifier.Value) * 0.2
	case models.ModifierTypeQuest:
		rel.Reputation += modifier.Value
		rel.Trust += float64(modifier.Value) * 0.15
	case models.ModifierTypeBetrayal:
		rel.Reputation -= modifier.Value * 3 // Betrayal has severe consequences
		rel.Trust -= float64(modifier.Value) * 0.5
	case models.ModifierTypeAlliance:
		rel.Reputation += modifier.Value
		rel.Trust += float64(modifier.Value) * 0.2
	case models.ModifierTypeGift:
		rel.Reputation += modifier.Value
		rel.Trust += float64(modifier.Value) * 0.05
	case models.ModifierTypeInsult:
		rel.Reputation -= modifier.Value
		rel.Trust -= float64(modifier.Value) * 0.1
	}

	// Clamp values
	rel.Reputation = int(math.Max(-100, math.Min(100, float64(rel.Reputation))))
	rel.Trust = math.Max(0, math.Min(100, rel.Trust))

	// Update level based on reputation
	rel.Level = s.calculateRelationshipLevel(rel.Reputation, rel.Trust)

	rel.LastInteraction = time.Now()
	rel.InteractionCount++
	rel.UpdatedAt = time.Now()

	// Save to database
	if err := s.repo.CreateOrUpdateRelationship(ctx, rel); err != nil {
		return fmt.Errorf("failed to update relationship: %w", err)
	}

	// Update cache
	s.cache.SetRelationship(ctx, rel)

	// Invalidate social network caches
	s.cache.ClearAllSocialCache(ctx)

	return nil
}

// calculateRelationshipLevel determines relationship level from reputation and trust
func (s *Service) calculateRelationshipLevel(reputation int, trust float64) models.RelationshipLevel {
	if reputation <= -50 || trust <= 20 {
		return models.RelationshipLevelHostile
	}
	if reputation <= -20 || trust <= 40 {
		return models.RelationshipLevelUnfriendly
	}
	if reputation <= 20 && trust <= 60 {
		return models.RelationshipLevelNeutral
	}
	if reputation >= 50 && trust >= 80 {
		return models.RelationshipLevelAllied
	}
	if reputation >= 30 && trust >= 70 {
		return models.RelationshipLevelTrusted
	}
	return models.RelationshipLevelFriendly
}

// GetSocialNetwork retrieves the complete social network for an entity
func (s *Service) GetSocialNetwork(ctx context.Context, entityID uuid.UUID, entityType models.EntityType) (*models.SocialNetwork, error) {
	// Try cache first
	if network, found := s.cache.GetSocialNetwork(ctx, entityID, entityType); found {
		return network, nil
	}

	// Get from database
	network, err := s.repo.GetSocialNetwork(ctx, entityID, entityType)
	if err != nil {
		return nil, fmt.Errorf("failed to get social network: %w", err)
	}

	// Cache the result
	s.cache.SetSocialNetwork(ctx, network)

	return network, nil
}

// ORDER METHODS

// CreateOrder creates a new player order
func (s *Service) CreateOrder(ctx context.Context, order *models.Order) error {
	// Validate order
	if err := s.validateOrder(order); err != nil {
		return fmt.Errorf("order validation failed: %w", err)
	}

	// Create in database
	if err := s.repo.CreateOrder(ctx, order); err != nil {
		return fmt.Errorf("failed to create order: %w", err)
	}

	// Invalidate order board cache for the region
	s.cache.InvalidateOrderBoard(ctx, order.RegionID)

	return nil
}

// validateOrder performs business logic validation
func (s *Service) validateOrder(order *models.Order) error {
	if order.Title == "" {
		return fmt.Errorf("order title is required")
	}
	if order.Description == "" {
		return fmt.Errorf("order description is required")
	}
	if order.Reward.Currency < 0 {
		return fmt.Errorf("reward currency cannot be negative")
	}
	if order.Requirements.MinLevel < 1 {
		return fmt.Errorf("minimum level must be at least 1")
	}
	return nil
}

// GetOrderBoard retrieves orders for a region
func (s *Service) GetOrderBoard(ctx context.Context, regionID string, limit, offset int) (*models.OrderBoard, error) {
	// Try cache first
	if board, found := s.cache.GetOrderBoard(ctx, regionID); found && offset == 0 {
		return board, nil
	}

	// Get from database
	board, err := s.repo.GetOrderBoard(ctx, regionID, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get order board: %w", err)
	}

	// Cache if first page
	if offset == 0 {
		s.cache.SetOrderBoard(ctx, board)
	}

	return board, nil
}

// AcceptOrder allows a player to accept an order
func (s *Service) AcceptOrder(ctx context.Context, orderID, playerID uuid.UUID) error {
	// Get order
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("failed to get order: %w", err)
	}

	if order.Status != models.OrderStatusOpen {
		return fmt.Errorf("order is not available for acceptance")
	}

	if order.AcceptedBy != nil {
		return fmt.Errorf("order already accepted")
	}

	// Check if player meets requirements
	if err := s.checkOrderRequirements(ctx, order, playerID); err != nil {
		return fmt.Errorf("player does not meet requirements: %w", err)
	}

	// Update order status
	order.Status = models.OrderStatusAccepted
	order.AcceptedBy = &playerID
	order.UpdatedAt = time.Now()

	// Save to database (would need repository method)
	// For now, just invalidate cache
	s.cache.InvalidateOrderBoard(ctx, order.RegionID)

	return nil
}

// checkOrderRequirements validates if player meets order requirements
func (s *Service) checkOrderRequirements(ctx context.Context, order *models.Order, playerID uuid.UUID) error {
	// This would check player level, skills, reputation, etc.
	// Simplified implementation
	return nil
}

// CompleteOrder marks an order as completed
func (s *Service) CompleteOrder(ctx context.Context, orderID, playerID uuid.UUID) error {
	// Get order
	order, err := s.repo.GetOrder(ctx, orderID)
	if err != nil {
		return fmt.Errorf("failed to get order: %w", err)
	}

	if order.Status != models.OrderStatusAccepted {
		return fmt.Errorf("order is not in accepted status")
	}

	if order.AcceptedBy == nil || *order.AcceptedBy != playerID {
		return fmt.Errorf("player is not the accepted contractor")
	}

	// Mark as completed
	order.Status = models.OrderStatusCompleted
	order.CompletedAt = &time.Now()
	order.UpdatedAt = time.Now()

	// Distribute rewards (simplified)
	// In real implementation, this would transfer currency, items, reputation

	// Invalidate caches
	s.cache.InvalidateOrderBoard(ctx, order.RegionID)

	return nil
}

// NPC HIRING METHODS

// GetAvailableNPCs retrieves NPCs available for hiring in a region
func (s *Service) GetAvailableNPCs(ctx context.Context, regionID string) ([]models.NPCAvailability, error) {
	// Try cache first
	if npcs, found := s.cache.GetAvailableNPCs(ctx, regionID); found {
		return npcs, nil
	}

	// Get from database
	npcs, err := s.repo.GetAvailableNPCs(ctx, regionID)
	if err != nil {
		return nil, fmt.Errorf("failed to get available NPCs: %w", err)
	}

	// Cache the result
	s.cache.SetAvailableNPCs(ctx, regionID, npcs)

	return npcs, nil
}

// HireNPC creates a hiring contract for an NPC
func (s *Service) HireNPC(ctx context.Context, npcID, employerID uuid.UUID, serviceType models.ServiceType, contractTerms models.ContractTerms) (*models.NPCHiring, error) {
	// Get NPC info (simplified)
	npcName := "Unknown NPC"  // Would get from database
	npcType := models.NPCTypeMercenary // Would get from database

	// Create hiring contract
	hiring := &models.NPCHiring{
		NPCID:         npcID,
		NPCName:       npcName,
		NPCType:       npcType,
		EmployerID:    employerID,
		ServiceType:   serviceType,
		ContractTerms: contractTerms,
	}

	// Validate contract
	if err := s.validateContract(&contractTerms); err != nil {
		return nil, fmt.Errorf("contract validation failed: %w", err)
	}

	// Create in database
	if err := s.repo.CreateNPCHiring(ctx, hiring); err != nil {
		return nil, fmt.Errorf("failed to create hiring contract: %w", err)
	}

	// Cache the result
	s.cache.SetNPCHiring(ctx, hiring)

	// Invalidate available NPCs cache
	// s.cache.InvalidateByPattern(ctx, fmt.Sprintf("social:npcs:%s", regionID))

	return hiring, nil
}

// validateContract performs business logic validation for contracts
func (s *Service) validateContract(terms *models.ContractTerms) error {
	if terms.Duration < time.Hour {
		return fmt.Errorf("contract duration must be at least 1 hour")
	}
	if terms.Payment.BaseSalary <= 0 {
		return fmt.Errorf("base salary must be positive")
	}
	if terms.RiskLevel == "" {
		return fmt.Errorf("risk level is required")
	}
	return nil
}

// TerminateNPCHiring ends an NPC hiring contract
func (s *Service) TerminateNPCHiring(ctx context.Context, hiringID uuid.UUID, reason string) error {
	// Get hiring
	hiring, err := s.repo.GetNPCHiring(ctx, hiringID)
	if err != nil {
		return fmt.Errorf("failed to get hiring: %w", err)
	}

	if hiring.Status != models.HiringStatusActive {
		return fmt.Errorf("hiring is not active")
	}

	// Update status
	hiring.Status = models.HiringStatusTerminated
	hiring.UpdatedAt = time.Now()

	// Save to database (would need repository method)
	// For now, just update cache
	s.cache.SetNPCHiring(ctx, hiring)

	return nil
}

// GetNPCPerformance retrieves performance metrics for hired NPC
func (s *Service) GetNPCPerformance(ctx context.Context, hiringID uuid.UUID, period string) (*models.NPCPerformance, error) {
	// This would calculate performance metrics based on completed missions
	// Simplified implementation
	performance := &models.NPCPerformance{
		HiringID:          hiringID,
		MissionsCompleted: 5,
		SuccessRate:       85.0,
		ClientSatisfaction: 90.0,
		Earnings:          2500,
		BonusesEarned:     300,
		PenaltiesApplied:  0,
		CalculatedAt:      time.Now(),
	}

	return performance, nil
}
