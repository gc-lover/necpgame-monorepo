// Issue: #140894825 - Social Orders Service: ogen handlers implementation
// PERFORMANCE: Internal models optimized for struct alignment and memory efficiency

package models

import (
	"time"

	"github.com/google/uuid"
)

// CreateOrderDraftRequest represents a request to create an order draft
type CreateOrderDraftRequest struct {
	Title               string                 `json:"title"`
	Description         string                 `json:"description"`
	OrderType           string                 `json:"order_type"`
	Reward              Reward                 `json:"reward"`
	RiskLevel           string                 `json:"risk_level"`
	Deadline            *time.Time             `json:"deadline,omitempty"`
	Requirements        *OrderRequirements     `json:"requirements,omitempty"`
	TargetContractorIDs []uuid.UUID            `json:"target_contractor_ids,omitempty"`
}

// Reward represents the reward structure for an order
type Reward struct {
	Currency string `json:"currency"`
	Amount   int    `json:"amount"`
}

// OrderRequirements defines the requirements for order fulfillment
type OrderRequirements struct {
	MinLevel   *int     `json:"min_level,omitempty"`
	Skills     []string `json:"skills,omitempty"`
	Reputation *int     `json:"reputation,omitempty"`
}

// OrderDraftResponse represents the response after creating an order draft
type OrderDraftResponse struct {
	DraftID                uuid.UUID                    `json:"draft_id"`
	OrderData              CreateOrderDraftRequest     `json:"order_data"`
	ValidationResults      *OrderValidationResponse    `json:"validation_results"`
	OptimizationSuggestions *OrderOptimizationResponse `json:"optimization_suggestions"`
	EstimatedCost          interface{}                  `json:"estimated_cost"` // Placeholder for cost data
	RecommendedContractors []ContractorSuggestion      `json:"recommended_contractors"`
}

// ValidateOrderRequest represents a request to validate order parameters
type ValidateOrderRequest struct {
	OrderParameters CreateOrderDraftRequest `json:"order_parameters"`
	ClientID        *uuid.UUID               `json:"client_id,omitempty"`
}

// OrderValidationResponse contains validation results for an order
type OrderValidationResponse struct {
	IsValid  bool                      `json:"is_valid"`
	Errors   []ValidationError         `json:"errors,omitempty"`
	Warnings []ValidationWarning       `json:"warnings,omitempty"`
}

// ValidationError represents a validation error
type ValidationError struct {
	Field     string `json:"field"`
	ErrorType string `json:"error_type"`
	Message   string `json:"message"`
	Suggestion string `json:"suggestion,omitempty"`
}

// ValidationWarning represents a validation warning
type ValidationWarning struct {
	Field       string `json:"field"`
	WarningType string `json:"warning_type"`
	Message     string `json:"message"`
	Suggestion  string `json:"suggestion,omitempty"`
}

// OptimizeOrderRequest represents a request to optimize order parameters
type OptimizeOrderRequest struct {
	OrderParameters   CreateOrderDraftRequest `json:"order_parameters"`
	OptimizationGoals []string                `json:"optimization_goals,omitempty"`
}

// OrderOptimizationResponse contains optimization suggestions
type OrderOptimizationResponse struct {
	OriginalParameters  CreateOrderDraftRequest `json:"original_parameters"`
	OptimizedParameters CreateOrderDraftRequest `json:"optimized_parameters"`
	Improvements        []Improvement           `json:"improvements"`
	ExpectedOutcomes    ExpectedOutcomes        `json:"expected_outcomes"`
}

// Improvement represents a suggested improvement
type Improvement struct {
	Parameter      string `json:"parameter"`
	OriginalValue  string `json:"original_value"`
	OptimizedValue string `json:"optimized_value"`
	ImprovementType string `json:"improvement_type"`
	Impact         string `json:"impact"`
}

// ExpectedOutcomes represents expected outcomes after optimization
type ExpectedOutcomes struct {
	EstimatedSuccessRate   float64 `json:"estimated_success_rate"`
	EstimatedCompletionTime string `json:"estimated_completion_time"`
	TotalCostSavings       int     `json:"total_cost_savings"`
}

// SuggestContractorsRequest represents a request to suggest contractors
type SuggestContractorsRequest struct {
	OrderParameters CreateOrderDraftRequest `json:"order_parameters"`
	MaxSuggestions  int                     `json:"max_suggestions,omitempty"`
	FilterCriteria  *ContractorFilter       `json:"filter_criteria,omitempty"`
}

// ContractorFilter defines filtering criteria for contractors
type ContractorFilter struct {
	MinReputationScore float64  `json:"min_reputation_score,omitempty"`
	RequiredSkills     []string `json:"required_skills,omitempty"`
	Location          string   `json:"location,omitempty"`
	MaxHourlyRate     int      `json:"max_hourly_rate,omitempty"`
}

// SuggestedContractorsResponse contains suggested contractors
type SuggestedContractorsResponse struct {
	Suggestions    []ContractorSuggestion `json:"suggestions"`
	TotalAvailable int                    `json:"total_available"`
	SearchCriteria interface{}            `json:"search_criteria"`
}

// ContractorSuggestion represents a suggested contractor
type ContractorSuggestion struct {
	ContractorID          uuid.UUID `json:"contractor_id"`
	CharacterName         string    `json:"character_name"`
	ReputationScore       float64   `json:"reputation_score"`
	SpecializationMatch   float64   `json:"specialization_match"`
	EstimatedSuccessRate  float64   `json:"estimated_success_rate"`
	AverageCompletionTime string    `json:"average_completion_time"`
	HourlyRate           int       `json:"hourly_rate"`
	AvailabilityStatus   string    `json:"availability_status"`
	RecommendationReason  string    `json:"recommendation_reason"`
}

// CreateOrderWithValidationRequest represents a request to create an order with validation
type CreateOrderWithValidationRequest struct {
	OrderParameters     CreateOrderDraftRequest `json:"order_parameters"`
	ApplyOptimizations  bool                    `json:"apply_optimizations,omitempty"`
	NotifyContractors   bool                    `json:"notify_contractors,omitempty"`
}

// OrderCreatedResponse represents the response after creating an order
type OrderCreatedResponse struct {
	Order                  interface{} `json:"order"`
	AppliedOptimizations    []string   `json:"applied_optimizations,omitempty"`
	NotificationsSent      int        `json:"notifications_sent,omitempty"`
	EstimatedCompletionTime string     `json:"estimated_completion_time,omitempty"`
	SuccessProbability     float64    `json:"success_probability,omitempty"`
}
