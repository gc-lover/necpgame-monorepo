// Orders Reputation Integration Service
// Issue: #140894823

package ordersreputation

import (
	"context"
	"math"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Service provides integration between orders and reputation systems
type Service struct {
	logger *zap.Logger
	// TODO: Add database connections, external service clients
}

// NewService creates a new orders reputation integration service
func NewService(logger *zap.Logger) (*Service, error) {
	return &Service{
		logger: logger,
	}, nil
}

// CharacterRole represents the role of a character in an order
type CharacterRole string

const (
	RoleClient     CharacterRole = "client"
	RoleContractor CharacterRole = "contractor"
)

// ReputationTier represents reputation levels
type ReputationTier string

const (
	TierHated      ReputationTier = "hated"
	TierHostile    ReputationTier = "hostile"
	TierUnfriendly ReputationTier = "unfriendly"
	TierNeutral    ReputationTier = "neutral"
	TierFriendly   ReputationTier = "friendly"
	TierHonored    ReputationTier = "honored"
	TierRevered    ReputationTier = "revered"
	TierExalted    ReputationTier = "exalted"
)

// OrderReputationCost represents calculated cost with reputation modifiers
type OrderReputationCost struct {
	OrderID             uuid.UUID          `json:"order_id"`
	BaseCost            OrderCost          `json:"base_cost"`
	ClientModifier      CostModifier       `json:"client_modifier"`
	ContractorModifier  *CostModifier      `json:"contractor_modifier,omitempty"`
	FinalClientCost     OrderCost          `json:"final_client_cost"`
	FinalContractorReward OrderCost         `json:"final_contractor_reward"`
}

// OrderCost represents a cost in the game economy
type OrderCost struct {
	Amount   int    `json:"amount"`
	Currency string `json:"currency"`
}

// CostModifier represents a cost modifier
type CostModifier struct {
	Type      string  `json:"type"` // "discount", "surcharge", "bonus", "penalty"
	Percentage float64 `json:"percentage"`
	Reason    string  `json:"reason"`
}

// ReputationRequirementsCheck represents the result of reputation requirements check
type ReputationRequirementsCheck struct {
	CharacterID       uuid.UUID            `json:"character_id"`
	OrderID           uuid.UUID            `json:"order_id"`
	Role              CharacterRole        `json:"role"`
	MeetsRequirements bool                 `json:"meets_requirements"`
	RequiredReputation ReputationRequirement `json:"required_reputation"`
	CurrentReputation ReputationStatus     `json:"current_reputation"`
	BlockingReasons   []string             `json:"blocking_reasons,omitempty"`
}

// ReputationRequirement represents required reputation for an order
type ReputationRequirement struct {
	FactionID uuid.UUID      `json:"faction_id"`
	MinTier   ReputationTier `json:"min_tier"`
	MinValue  int            `json:"min_value"`
}

// ReputationStatus represents current reputation status
type ReputationStatus struct {
	FactionID uuid.UUID      `json:"faction_id"`
	Tier      ReputationTier `json:"tier"`
	Value     int            `json:"value"`
}

// OrderCompletionReputationRequest represents request for reputation changes after order completion
type OrderCompletionReputationRequest struct {
	CompletionQuality     string `json:"completion_quality"` // "failed", "poor", "average", "good", "excellent"
	WasOnTime            bool   `json:"was_on_time"`
	ClientFeedbackRating int    `json:"client_feedback_rating"` // 1-5
	AdditionalNotes      string `json:"additional_notes,omitempty"`
}

// OrderCompletionReputationResponse represents reputation changes after order completion
type OrderCompletionReputationResponse struct {
	OrderID                  uuid.UUID         `json:"order_id"`
	ClientReputationChanges  []ReputationChange `json:"client_reputation_changes"`
	ContractorReputationChanges []ReputationChange `json:"contractor_reputation_changes"`
	OrderRating              float64           `json:"order_rating"`
	ExperienceGained         int               `json:"experience_gained"`
}

// ReputationChange represents a reputation change
type ReputationChange struct {
	FactionID   uuid.UUID `json:"faction_id"`
	OldValue    int       `json:"old_value"`
	NewValue    int       `json:"new_value"`
	Change      int       `json:"change"`
	Reason      string    `json:"reason"`
	Timestamp   string    `json:"timestamp"`
}

// ContractorsReputationRanking represents contractors ranking by reputation
type ContractorsReputationRanking struct {
	Ranking    []ContractorRanking `json:"ranking"`
	TotalCount int                 `json:"total_count"`
}

// ContractorRanking represents individual contractor ranking
type ContractorRanking struct {
	ContractorID    uuid.UUID `json:"contractor_id"`
	CharacterName   string    `json:"character_name"`
	FactionID       uuid.UUID `json:"faction_id"`
	ReputationScore float64   `json:"reputation_score"` // 0-100
	CompletedOrders int       `json:"completed_orders"`
	SuccessRate     float64   `json:"success_rate"` // 0-100
	AverageRating   float64   `json:"average_rating"` // 0-5
	Specialization  []string  `json:"specialization,omitempty"`
}

// ReputationBonuses represents reputation bonuses for orders
type ReputationBonuses struct {
	CharacterID    uuid.UUID                   `json:"character_id"`
	FactionBonuses []FactionReputationBonus    `json:"faction_bonuses"`
	GlobalBonuses  GlobalReputationBonuses     `json:"global_bonuses"`
}

// FactionReputationBonus represents bonuses for specific faction
type FactionReputationBonus struct {
	FactionID         uuid.UUID      `json:"faction_id"`
	FactionName       string         `json:"faction_name"`
	ReputationTier    ReputationTier `json:"reputation_tier"`
	CostModifier      float64        `json:"cost_modifier"` // positive = discount, negative = surcharge
	RewardModifier    float64        `json:"reward_modifier"` // positive = bonus, negative = penalty
	AvailableOrders   []string       `json:"available_orders"`
}

// GlobalReputationBonuses represents global bonuses regardless of faction
type GlobalReputationBonuses struct {
	HighReputationDiscount float64 `json:"high_reputation_discount"` // % discount for honored+ players
	LowReputationPenalty   float64 `json:"low_reputation_penalty"`   // % penalty for hostile- players
}

// CalculateOrderReputationCost calculates order cost with reputation modifiers
func (s *Service) CalculateOrderReputationCost(ctx context.Context, orderID, clientID uuid.UUID, contractorID *uuid.UUID) (*OrderReputationCost, error) {
	s.logger.Info("Calculating order reputation cost",
		zap.String("order_id", orderID.String()),
		zap.String("client_id", clientID.String()))

	// TODO: Get base order cost from orders service
	baseCost := OrderCost{
		Amount:   1000, // placeholder
		Currency: "eurodollar",
	}

	// Calculate client modifier based on reputation
	clientModifier := s.calculateClientModifier(ctx, clientID, baseCost)

	// Calculate contractor modifier if contractor is specified
	var contractorModifier *CostModifier
	if contractorID != nil {
		mod := s.calculateContractorModifier(ctx, *contractorID, baseCost)
		contractorModifier = &mod
	}

	// Apply modifiers
	finalClientCost := s.applyCostModifier(baseCost, clientModifier)

	finalContractorReward := baseCost
	if contractorModifier != nil {
		finalContractorReward = s.applyCostModifier(baseCost, *contractorModifier)
	}

	return &OrderReputationCost{
		OrderID:             orderID,
		BaseCost:            baseCost,
		ClientModifier:      clientModifier,
		ContractorModifier:  contractorModifier,
		FinalClientCost:     finalClientCost,
		FinalContractorReward: finalContractorReward,
	}, nil
}

// CheckOrderReputationRequirements checks if character meets reputation requirements for order
func (s *Service) CheckOrderReputationRequirements(ctx context.Context, orderID, characterID uuid.UUID, role CharacterRole) (*ReputationRequirementsCheck, error) {
	s.logger.Info("Checking reputation requirements",
		zap.String("order_id", orderID.String()),
		zap.String("character_id", characterID.String()),
		zap.String("role", string(role)))

	// TODO: Get order requirements from orders service
	requiredRep := ReputationRequirement{
		FactionID: uuid.New(), // placeholder
		MinTier:   TierNeutral,
		MinValue:  0,
	}

	// TODO: Get current reputation from reputation service
	currentRep := ReputationStatus{
		FactionID: requiredRep.FactionID,
		Tier:      TierFriendly,
		Value:     500,
	}

	meetsRequirements := s.checkReputationRequirement(currentRep, ReputationStatus{
		FactionID: requiredRep.FactionID,
		Tier:      requiredRep.MinTier,
		Value:     requiredRep.MinValue,
	})
	var blockingReasons []string

	if !meetsRequirements {
		blockingReasons = []string{"insufficient_reputation"}
	}

	return &ReputationRequirementsCheck{
		CharacterID:       characterID,
		OrderID:           orderID,
		Role:              role,
		MeetsRequirements: meetsRequirements,
		RequiredReputation: requiredRep,
		CurrentReputation: currentRep,
		BlockingReasons:   blockingReasons,
	}, nil
}

// ApplyOrderCompletionReputation applies reputation changes after order completion
func (s *Service) ApplyOrderCompletionReputation(ctx context.Context, orderID uuid.UUID, req OrderCompletionReputationRequest) (*OrderCompletionReputationResponse, error) {
	s.logger.Info("Applying reputation changes after order completion",
		zap.String("order_id", orderID.String()),
		zap.String("completion_quality", req.CompletionQuality))

	// Calculate reputation changes based on completion quality
	clientChanges := s.calculateClientReputationChanges(req)
	contractorChanges := s.calculateContractorReputationChanges(req)

	// Calculate order rating
	orderRating := s.calculateOrderRating(req)

	// Calculate experience gained
	experienceGained := s.calculateExperienceGained(req)

	return &OrderCompletionReputationResponse{
		OrderID:                  orderID,
		ClientReputationChanges:  clientChanges,
		ContractorReputationChanges: contractorChanges,
		OrderRating:              orderRating,
		ExperienceGained:         experienceGained,
	}, nil
}

// GetContractorsReputationRanking returns ranking of contractors by reputation
func (s *Service) GetContractorsReputationRanking(ctx context.Context, factionID *uuid.UUID, orderType *string, limit, offset int) (*ContractorsReputationRanking, error) {
	s.logger.Info("Getting contractors reputation ranking",
		zap.String("faction_id", factionID.String()),
		zap.String("order_type", *orderType),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	// TODO: Get ranking from database/reputation service
	ranking := []ContractorRanking{
		{
			ContractorID:    uuid.New(),
			CharacterName:   "John Doe",
			FactionID:       uuid.New(),
			ReputationScore: 85.5,
			CompletedOrders: 150,
			SuccessRate:     92.0,
			AverageRating:   4.2,
			Specialization:  []string{"assassination", "protection"},
		},
		{
			ContractorID:    uuid.New(),
			CharacterName:   "Jane Smith",
			FactionID:       uuid.New(),
			ReputationScore: 78.3,
			CompletedOrders: 89,
			SuccessRate:     88.7,
			AverageRating:   3.9,
			Specialization:  []string{"delivery", "investigation"},
		},
	}

	return &ContractorsReputationRanking{
		Ranking:    ranking,
		TotalCount: len(ranking),
	}, nil
}

// GetReputationBonuses returns reputation bonuses for orders
func (s *Service) GetReputationBonuses(ctx context.Context, characterID uuid.UUID, factionID *uuid.UUID) (*ReputationBonuses, error) {
	s.logger.Info("Getting reputation bonuses",
		zap.String("character_id", characterID.String()))

	// TODO: Get bonuses from reputation service
	factionBonuses := []FactionReputationBonus{
		{
			FactionID:       uuid.New(),
			FactionName:     "Arasaka Corporation",
			ReputationTier:  TierFriendly,
			CostModifier:    10.0, // 10% discount
			RewardModifier:  5.0,  // 5% bonus
			AvailableOrders: []string{"assassination", "protection", "bounty"},
		},
	}

	globalBonuses := GlobalReputationBonuses{
		HighReputationDiscount: 15.0, // 15% discount for honored+ players
		LowReputationPenalty:   25.0, // 25% penalty for hostile- players
	}

	return &ReputationBonuses{
		CharacterID:    characterID,
		FactionBonuses: factionBonuses,
		GlobalBonuses:  globalBonuses,
	}, nil
}

// Helper methods

func (s *Service) calculateClientModifier(ctx context.Context, clientID uuid.UUID, baseCost OrderCost) CostModifier {
	// TODO: Get client reputation from reputation service
	reputationScore := 500 // placeholder

	var modifier CostModifier
	if reputationScore >= 750 { // High reputation
		modifier = CostModifier{
			Type:       "discount",
			Percentage: 15.0,
			Reason:     "high_reputation",
		}
	} else if reputationScore <= 250 { // Low reputation
		modifier = CostModifier{
			Type:       "surcharge",
			Percentage: 25.0,
			Reason:     "low_reputation",
		}
	} else { // Neutral reputation
		modifier = CostModifier{
			Type:       "discount",
			Percentage: 5.0,
			Reason:     "neutral_reputation",
		}
	}

	return modifier
}

func (s *Service) calculateContractorModifier(ctx context.Context, contractorID uuid.UUID, baseReward OrderCost) CostModifier {
	// TODO: Get contractor reputation from reputation service
	reputationScore := 600 // placeholder

	var modifier CostModifier
	if reputationScore >= 800 { // Excellent reputation
		modifier = CostModifier{
			Type:       "bonus",
			Percentage: 20.0,
			Reason:     "high_reputation",
		}
	} else if reputationScore <= 300 { // Poor reputation
		modifier = CostModifier{
			Type:       "penalty",
			Percentage: 30.0,
			Reason:     "low_reputation",
		}
	} else { // Average reputation
		modifier = CostModifier{
			Type:       "bonus",
			Percentage: 5.0,
			Reason:     "average_reputation",
		}
	}

	return modifier
}

func (s *Service) applyCostModifier(baseCost OrderCost, modifier CostModifier) OrderCost {
	multiplier := 1.0
	if modifier.Type == "discount" || modifier.Type == "bonus" {
		multiplier = 1.0 - (modifier.Percentage / 100.0)
	} else if modifier.Type == "surcharge" || modifier.Type == "penalty" {
		multiplier = 1.0 + (modifier.Percentage / 100.0)
	}

	newAmount := int(math.Round(float64(baseCost.Amount) * multiplier))
	if newAmount < 0 {
		newAmount = 0
	}

	return OrderCost{
		Amount:   newAmount,
		Currency: baseCost.Currency,
	}
}

func (s *Service) checkReputationRequirement(current, required ReputationStatus) bool {
	// Compare reputation values
	if current.Value < required.Value {
		return false
	}

	// Compare tiers (higher tier = better reputation)
	currentTierWeight := s.getReputationTierWeight(current.Tier)
	requiredTierWeight := s.getReputationTierWeight(required.Tier)

	return currentTierWeight >= requiredTierWeight
}

func (s *Service) getReputationTierWeight(tier ReputationTier) int {
	weights := map[ReputationTier]int{
		TierHated:      1,
		TierHostile:    2,
		TierUnfriendly: 3,
		TierNeutral:    4,
		TierFriendly:   5,
		TierHonored:    6,
		TierRevered:    7,
		TierExalted:    8,
	}

	if weight, exists := weights[tier]; exists {
		return weight
	}
	return 4 // default to neutral
}

func (s *Service) calculateClientReputationChanges(req OrderCompletionReputationRequest) []ReputationChange {
	// Client reputation changes are minimal and mostly negative for failed orders
	var changes []ReputationChange

	if req.CompletionQuality == "failed" {
		changes = append(changes, ReputationChange{
			FactionID: uuid.New(),
			OldValue:  500,
			NewValue:  450,
			Change:    -50,
			Reason:    "order_failed",
			Timestamp: "2025-12-27T12:00:00Z",
		})
	}

	return changes
}

func (s *Service) calculateContractorReputationChanges(req OrderCompletionReputationRequest) []ReputationChange {
	// Contractor reputation changes based on completion quality
	var changes []ReputationChange

	change := 0
	reason := ""

	switch req.CompletionQuality {
	case "excellent":
		change = 100
		reason = "excellent_order_completion"
	case "good":
		change = 50
		reason = "good_order_completion"
	case "average":
		change = 10
		reason = "average_order_completion"
	case "poor":
		change = -25
		reason = "poor_order_completion"
	case "failed":
		change = -100
		reason = "order_failed"
	}

	// Additional bonuses/penalties
	if req.WasOnTime && change > 0 {
		change += 25
		reason += "_on_time"
	} else if !req.WasOnTime && change < 0 {
		change -= 25
		reason += "_late"
	}

	if req.ClientFeedbackRating >= 4 && change > 0 {
		change += 25
		reason += "_good_feedback"
	} else if req.ClientFeedbackRating <= 2 && change < 0 {
		change -= 25
		reason += "_bad_feedback"
	}

	if change != 0 {
		changes = append(changes, ReputationChange{
			FactionID: uuid.New(),
			OldValue:  600,
			NewValue:  600 + change,
			Change:    change,
			Reason:    reason,
			Timestamp: "2025-12-27T12:00:00Z",
		})
	}

	return changes
}

func (s *Service) calculateOrderRating(req OrderCompletionReputationRequest) float64 {
	baseRating := 3.0 // neutral

	// Adjust based on completion quality
	switch req.CompletionQuality {
	case "excellent":
		baseRating = 5.0
	case "good":
		baseRating = 4.0
	case "average":
		baseRating = 3.0
	case "poor":
		baseRating = 2.0
	case "failed":
		baseRating = 1.0
	}

	// Adjust based on timeliness
	if req.WasOnTime {
		baseRating += 0.5
	} else {
		baseRating -= 0.5
	}

	// Adjust based on client feedback
	if req.ClientFeedbackRating > 0 {
		feedbackAdjustment := float64(req.ClientFeedbackRating-3) * 0.3
		baseRating += feedbackAdjustment
	}

	// Clamp to 1-5 range
	if baseRating > 5.0 {
		baseRating = 5.0
	} else if baseRating < 1.0 {
		baseRating = 1.0
	}

	return math.Round(baseRating*10) / 10
}

func (s *Service) calculateExperienceGained(req OrderCompletionReputationRequest) int {
	baseXP := 100

	// Adjust based on completion quality
	switch req.CompletionQuality {
	case "excellent":
		baseXP = 200
	case "good":
		baseXP = 150
	case "average":
		baseXP = 100
	case "poor":
		baseXP = 50
	case "failed":
		baseXP = 10
	}

	// Bonus for timeliness
	if req.WasOnTime {
		baseXP = int(float64(baseXP) * 1.2)
	}

	return baseXP
}
