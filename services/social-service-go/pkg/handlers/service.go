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

// CHAT COMMANDS METHODS
// Issue: #1490 - Chat Commands Service: ogen handlers implementation

// ExecuteChatCommand executes a chat command
func (s *Service) ExecuteChatCommand(ctx context.Context, req models.ExecuteCommandRequest, userID uuid.UUID) (*models.CommandResponse, error) {
	// Validate command
	if req.Command == "" {
		return &models.CommandResponse{
			Command: req.Command,
			Error:   stringPtr("Command cannot be empty"),
			Success: false,
		}, nil
	}

	// Execute command based on type
	result := s.executeCommandLogic(ctx, req.Command, req.Args, userID)

	// Build response
	response := &models.CommandResponse{
		Command: req.Command,
		Success: result.Success,
	}

	if result.Success {
		response.Result = &result.Result
	} else {
		response.Error = &result.Error
	}

	return response, nil
}

// executeCommandLogic contains the actual command execution logic
func (s *Service) executeCommandLogic(ctx context.Context, command string, args []string, userID uuid.UUID) models.CommandResult {
	switch command {
	case "/help":
		return s.handleHelpCommand()
	case "/who":
		return s.handleWhoCommand(ctx)
	case "/status":
		return s.handleStatusCommand(ctx, userID)
	case "/social":
		return s.handleSocialCommand(ctx, args, userID)
	case "/order":
		return s.handleOrderCommand(ctx, args, userID)
	case "/npc":
		return s.handleNPCCommand(ctx, args, userID)
	default:
		return models.CommandResult{
			Success: false,
			Error:   "Unknown command. Use /help to see available commands.",
		}
	}
}

// handleHelpCommand shows available commands
func (s *Service) handleHelpCommand() models.CommandResult {
	helpText := `Available commands:
/help - Show this help message
/who - Show online players
/status - Show your current status
/social - Social network commands
/order - Order management commands
/npc - NPC hiring commands`

	return models.CommandResult{
		Success: true,
		Result:  helpText,
	}
}

// handleWhoCommand shows online players
func (s *Service) handleWhoCommand(ctx context.Context) models.CommandResult {
	// Mock online players count
	onlineCount := 42 // In real implementation, get from cache/session service

	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("Online players: %d", onlineCount),
	}
}

// handleStatusCommand shows user status
func (s *Service) handleStatusCommand(ctx context.Context, userID uuid.UUID) models.CommandResult {
	// Mock user status - in real implementation, get from user service
	status := fmt.Sprintf("Player ID: %s\nStatus: Active\nLocation: Unknown", userID.String())

	return models.CommandResult{
		Success: true,
		Result:  status,
	}
}

// handleSocialCommand handles social network commands
func (s *Service) handleSocialCommand(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) == 0 {
		return models.CommandResult{
			Success: false,
			Error:   "Social command requires subcommand. Usage: /social [friends|guild|relationships]",
		}
	}

	subcommand := args[0]
	switch subcommand {
	case "friends":
		return s.handleSocialFriends(ctx, userID)
	case "guild":
		return s.handleSocialGuild(ctx, userID)
	case "relationships":
		return s.handleSocialRelationships(ctx, userID)
	default:
		return models.CommandResult{
			Success: false,
			Error:   "Unknown social subcommand. Use: friends, guild, or relationships",
		}
	}
}

// handleOrderCommand handles order management commands
func (s *Service) handleOrderCommand(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) == 0 {
		return models.CommandResult{
			Success: false,
			Error:   "Order command requires subcommand. Usage: /order [list|create|accept]",
		}
	}

	subcommand := args[0]
	switch subcommand {
	case "list":
		return s.handleOrderList(ctx, userID)
	case "create":
		return s.handleOrderCreate(ctx, args[1:], userID)
	case "accept":
		return s.handleOrderAccept(ctx, args[1:], userID)
	default:
		return models.CommandResult{
			Success: false,
			Error:   "Unknown order subcommand. Use: list, create, or accept",
		}
	}
}

// handleNPCCommand handles NPC hiring commands
func (s *Service) handleNPCCommand(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) == 0 {
		return models.CommandResult{
			Success: false,
			Error:   "NPC command requires subcommand. Usage: /npc [hire|list|terminate]",
		}
	}

	subcommand := args[0]
	switch subcommand {
	case "hire":
		return s.handleNPCHire(ctx, args[1:], userID)
	case "list":
		return s.handleNPCList(ctx, userID)
	case "terminate":
		return s.handleNPCTerminate(ctx, args[1:], userID)
	default:
		return models.CommandResult{
			Success: false,
			Error:   "Unknown NPC subcommand. Use: hire, list, or terminate",
		}
	}
}

// Helper methods for social commands
func (s *Service) handleSocialFriends(ctx context.Context, userID uuid.UUID) models.CommandResult {
	// Mock friends count
	friendsCount := 5
	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("You have %d friends online.", friendsCount),
	}
}

func (s *Service) handleSocialGuild(ctx context.Context, userID uuid.UUID) models.CommandResult {
	// Mock guild info
	return models.CommandResult{
		Success: true,
		Result:  "Guild: Shadow Runners\nMembers: 12\nRank: Member",
	}
}

func (s *Service) handleSocialRelationships(ctx context.Context, userID uuid.UUID) models.CommandResult {
	// Mock relationships
	return models.CommandResult{
		Success: true,
		Result:  "Active relationships: 3 positive, 1 neutral, 0 negative",
	}
}

// Helper methods for order commands
func (s *Service) handleOrderList(ctx context.Context, userID uuid.UUID) models.CommandResult {
	// Mock active orders
	activeOrders := 2
	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("You have %d active orders.", activeOrders),
	}
}

func (s *Service) handleOrderCreate(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) < 2 {
		return models.CommandResult{
			Success: false,
			Error:   "Order create requires type and reward. Usage: /order create <type> <reward>",
		}
	}

	orderType := args[0]
	reward := args[1]

	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("Order created: %s with reward %s. Order ID: %s", orderType, reward, uuid.New().String()),
	}
}

func (s *Service) handleOrderAccept(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) < 1 {
		return models.CommandResult{
			Success: false,
			Error:   "Order accept requires order ID. Usage: /order accept <order_id>",
		}
	}

	orderID := args[0]
	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("Accepted order: %s", orderID),
	}
}

// Helper methods for NPC commands
func (s *Service) handleNPCHire(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) < 1 {
		return models.CommandResult{
			Success: false,
			Error:   "NPC hire requires NPC type. Usage: /npc hire <type>",
		}
	}

	npcType := args[0]
	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("Hired NPC: %s. Hiring ID: %s", npcType, uuid.New().String()),
	}
}

func (s *Service) handleNPCList(ctx context.Context, userID uuid.UUID) models.CommandResult {
	// Mock hired NPCs
	hiredNPCs := 1
	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("You have %d hired NPCs.", hiredNPCs),
	}
}

func (s *Service) handleNPCTerminate(ctx context.Context, args []string, userID uuid.UUID) models.CommandResult {
	if len(args) < 1 {
		return models.CommandResult{
			Success: false,
			Error:   "NPC terminate requires hiring ID. Usage: /npc terminate <hiring_id>",
		}
	}

	hiringID := args[0]
	return models.CommandResult{
		Success: true,
		Result:  fmt.Sprintf("Terminated NPC hiring: %s", hiringID),
	}
}

// stringPtr creates a string pointer
func stringPtr(s string) *string {
	return &s
}
