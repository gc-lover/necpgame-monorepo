// Issue: #140894825 - Social Orders Service: ogen handlers implementation
// PERFORMANCE: Optimized adapter between ogen interfaces and business logic

package adapter

import (
	"context"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"social-orders-creation-service-go/internal/models"
	"social-orders-creation-service-go/internal/service"
	"social-orders-creation-service-go/pkg/api"
)

// OgenServerAdapter adapts the business service to ogen ServerInterface
type OgenServerAdapter struct {
	service service.SocialOrdersService
	logger  *zap.Logger
}

// NewOgenServerAdapter creates a new ogen server adapter
func NewOgenServerAdapter(svc service.SocialOrdersService, logger *zap.Logger) *OgenServerAdapter {
	return &OgenServerAdapter{
		service: svc,
		logger:  logger,
	}
}

// CreateOrderDraft implements ServerInterface
func (a *OgenServerAdapter) CreateOrderDraft(ctx context.Context, request api.CreateOrderDraftRequest) (api.OrderDraftResponse, error) {
	a.logger.Info("CreateOrderDraft called via ogen interface")

	// Convert API request to internal model
	clientID := uuid.New() // TODO: Extract from JWT context
	draftReq := models.CreateOrderDraftRequest{
		Title:               request.Title,
		Description:         request.Description,
		OrderType:           request.OrderType,
		Reward:              models.Reward{Currency: request.Reward.Currency, Amount: request.Reward.Amount},
		RiskLevel:           request.RiskLevel,
		Deadline:            request.Deadline,
		Requirements:        a.convertRequirements(request.Requirements),
		TargetContractorIDs: request.TargetContractorIDs,
	}

	// Call business service
	draft, err := a.service.CreateOrderDraft(ctx, clientID, draftReq)
	if err != nil {
		return api.OrderDraftResponse{}, err
	}

	// Convert response back to API types
	return api.OrderDraftResponse{
		DraftID:                draft.DraftID,
		OrderData:              a.convertToAPIDraft(draft.OrderData),
		ValidationResults:      a.convertValidationResponse(draft.ValidationResults),
		OptimizationSuggestions: a.convertOptimizationResponse(draft.OptimizationSuggestions),
		EstimatedCost:          a.convertEstimatedCost(draft.EstimatedCost),
		RecommendedContractors: a.convertContractorSuggestions(draft.RecommendedContractors),
	}, nil
}

// ValidateOrderParameters implements ServerInterface
func (a *OgenServerAdapter) ValidateOrderParameters(ctx context.Context, request api.ValidateOrderRequest) (api.OrderValidationResponse, error) {
	a.logger.Info("ValidateOrderParameters called via ogen interface")

	clientID := uuid.New() // TODO: Extract from JWT context
	if request.ClientID != nil {
		clientID = *request.ClientID
	}

	validateReq := models.ValidateOrderRequest{
		OrderParameters: a.convertToInternalDraft(request.OrderParameters),
		ClientID:        &clientID,
	}

	validation, err := a.service.ValidateOrderParameters(ctx, clientID, validateReq)
	if err != nil {
		return api.OrderValidationResponse{}, err
	}

	return *a.convertValidationResponse(&validation), nil
}

// OptimizeOrderParameters implements ServerInterface
func (a *OgenServerAdapter) OptimizeOrderParameters(ctx context.Context, request api.OptimizeOrderRequest) (api.OrderOptimizationResponse, error) {
	a.logger.Info("OptimizeOrderParameters called via ogen interface")

	clientID := uuid.New() // TODO: Extract from JWT context
	optimizeReq := models.OptimizeOrderRequest{
		OrderParameters:   a.convertToInternalDraft(request.OrderParameters),
		OptimizationGoals: request.OptimizationGoals,
	}

	optimization, err := a.service.OptimizeOrderParameters(ctx, clientID, optimizeReq)
	if err != nil {
		return api.OrderOptimizationResponse{}, err
	}

	return *a.convertOptimizationResponse(&optimization), nil
}

// SuggestContractors implements ServerInterface
func (a *OgenServerAdapter) SuggestContractors(ctx context.Context, request api.SuggestContractorsRequest) (api.SuggestedContractorsResponse, error) {
	a.logger.Info("SuggestContractors called via ogen interface")

	// Convert to internal request
	suggestReq := models.SuggestContractorsRequest{
		OrderParameters: a.convertToInternalDraft(request.OrderParameters.(api.CreateOrderDraftRequest)), // Type assertion
		MaxSuggestions:  request.MaxSuggestions,
		FilterCriteria:  a.convertFilterCriteria(request.FilterCriteria),
	}

	suggestions, err := a.service.SuggestContractors(ctx, suggestReq, request.MaxSuggestions)
	if err != nil {
		return api.SuggestedContractorsResponse{}, err
	}

	return api.SuggestedContractorsResponse{
		Suggestions:    a.convertContractorSuggestions(suggestions.Suggestions),
		TotalAvailable: suggestions.TotalAvailable,
		SearchCriteria: suggestions.SearchCriteria,
	}, nil
}

// CreateOrderWithValidation implements ServerInterface
func (a *OgenServerAdapter) CreateOrderWithValidation(ctx context.Context, request api.CreateOrderWithValidationRequest) (api.OrderCreatedResponse, error) {
	a.logger.Info("CreateOrderWithValidation called via ogen interface")

	clientID := uuid.New() // TODO: Extract from JWT context
	createReq := models.CreateOrderWithValidationRequest{
		OrderParameters:    a.convertToInternalDraft(request.OrderParameters),
		ApplyOptimizations: request.ApplyOptimizations,
		NotifyContractors:  request.NotifyContractors,
	}

	result, err := a.service.CreateOrderWithValidation(ctx, clientID, createReq)
	if err != nil {
		return api.OrderCreatedResponse{}, err
	}

	return api.OrderCreatedResponse{
		Order:                  result.Order,
		AppliedOptimizations:    result.AppliedOptimizations,
		NotificationsSent:      result.NotificationsSent,
		EstimatedCompletionTime: result.EstimatedCompletionTime,
		SuccessProbability:     result.SuccessProbability,
	}, nil
}

// Helper conversion methods

func (a *OgenServerAdapter) convertRequirements(req *api.OrderRequirements) *models.OrderRequirements {
	if req == nil {
		return nil
	}
	return &models.OrderRequirements{
		MinLevel:   req.MinLevel,
		Skills:     req.Skills,
		Reputation: req.Reputation,
	}
}

func (a *OgenServerAdapter) convertToAPIDraft(draft models.CreateOrderDraftRequest) api.CreateOrderDraftRequest {
	return api.CreateOrderDraftRequest{
		Title:               draft.Title,
		Description:         draft.Description,
		OrderType:           draft.OrderType,
		Reward:              api.Reward{Currency: draft.Reward.Currency, Amount: draft.Reward.Amount},
		RiskLevel:           draft.RiskLevel,
		Deadline:            draft.Deadline,
		Requirements:        a.convertToAPIRequirements(draft.Requirements),
		TargetContractorIDs: draft.TargetContractorIDs,
	}
}

func (a *OgenServerAdapter) convertToAPIRequirements(req *models.OrderRequirements) *api.OrderRequirements {
	if req == nil {
		return nil
	}
	return &api.OrderRequirements{
		MinLevel:   req.MinLevel,
		Skills:     req.Skills,
		Reputation: req.Reputation,
	}
}

func (a *OgenServerAdapter) convertValidationResponse(resp *models.OrderValidationResponse) *api.OrderValidationResponse {
	if resp == nil {
		return nil
	}
	return &api.OrderValidationResponse{
		IsValid:  resp.IsValid,
		Errors:   a.convertValidationErrors(resp.Errors),
		Warnings: a.convertValidationWarnings(resp.Warnings),
	}
}

func (a *OgenServerAdapter) convertValidationErrors(errors []models.ValidationError) []api.ValidationError {
	if errors == nil {
		return nil
	}
	result := make([]api.ValidationError, len(errors))
	for i, err := range errors {
		result[i] = api.ValidationError{
			Field:     err.Field,
			ErrorType: err.ErrorType,
			Message:   err.Message,
			Suggestion: err.Suggestion,
		}
	}
	return result
}

func (a *OgenServerAdapter) convertValidationWarnings(warnings []models.ValidationWarning) []api.ValidationWarning {
	if warnings == nil {
		return nil
	}
	result := make([]api.ValidationWarning, len(warnings))
	for i, warn := range warnings {
		result[i] = api.ValidationWarning{
			Field:       warn.Field,
			WarningType: warn.WarningType,
			Message:     warn.Message,
			Suggestion:  warn.Suggestion,
		}
	}
	return result
}

func (a *OgenServerAdapter) convertOptimizationResponse(resp *models.OrderOptimizationResponse) *api.OrderOptimizationResponse {
	if resp == nil {
		return nil
	}
	return &api.OrderOptimizationResponse{
		OriginalParameters:  a.convertToAPIDraft(resp.OriginalParameters),
		OptimizedParameters: a.convertToAPIDraft(resp.OptimizedParameters),
		Improvements:        a.convertImprovements(resp.Improvements),
		ExpectedOutcomes:    a.convertExpectedOutcomes(resp.ExpectedOutcomes),
	}
}

func (a *OgenServerAdapter) convertImprovements(improvements []models.Improvement) []api.Improvement {
	if improvements == nil {
		return nil
	}
	result := make([]api.Improvement, len(improvements))
	for i, imp := range improvements {
		result[i] = api.Improvement{
			Parameter:      imp.Parameter,
			OriginalValue:  imp.OriginalValue,
			OptimizedValue: imp.OptimizedValue,
			ImprovementType: imp.ImprovementType,
			Impact:         imp.Impact,
		}
	}
	return result
}

func (a *OgenServerAdapter) convertExpectedOutcomes(outcomes models.ExpectedOutcomes) api.ExpectedOutcomes {
	return api.ExpectedOutcomes{
		EstimatedSuccessRate: outcomes.EstimatedSuccessRate,
		EstimatedCompletionTime: outcomes.EstimatedCompletionTime,
		TotalCostSavings:      outcomes.TotalCostSavings,
	}
}

func (a *OgenServerAdapter) convertEstimatedCost(cost interface{}) *api.OrderReputationCost {
	if cost == nil {
		return nil
	}
	// Convert from interface{} to proper type - simplified for now
	return &api.OrderReputationCost{
		BaseCost:  0,
		FinalCost: 0,
		Currency:  "USD",
		Multipliers: make(map[string]interface{}),
	}
}

func (a *OgenServerAdapter) convertContractorSuggestions(suggestions []models.ContractorSuggestion) []api.ContractorSuggestion {
	if suggestions == nil {
		return nil
	}
	result := make([]api.ContractorSuggestion, len(suggestions))
	for i, sug := range suggestions {
		result[i] = api.ContractorSuggestion{
			ContractorID:          sug.ContractorID,
			CharacterName:         sug.CharacterName,
			ReputationScore:       sug.ReputationScore,
			SpecializationMatch:   sug.SpecializationMatch,
			EstimatedSuccessRate:  sug.EstimatedSuccessRate,
			AverageCompletionTime: sug.AverageCompletionTime,
			HourlyRate:           sug.HourlyRate,
			AvailabilityStatus:   sug.AvailabilityStatus,
			RecommendationReason:  sug.RecommendationReason,
		}
	}
	return result
}

func (a *OgenServerAdapter) convertToInternalDraft(draft api.CreateOrderDraftRequest) models.CreateOrderDraftRequest {
	return models.CreateOrderDraftRequest{
		Title:               draft.Title,
		Description:         draft.Description,
		OrderType:           draft.OrderType,
		Reward:              models.Reward{Currency: draft.Reward.Currency, Amount: draft.Reward.Amount},
		RiskLevel:           draft.RiskLevel,
		Deadline:            draft.Deadline,
		Requirements:        a.convertRequirements(draft.Requirements),
		TargetContractorIDs: draft.TargetContractorIDs,
	}
}

func (a *OgenServerAdapter) convertFilterCriteria(filter *api.ContractorFilter) *models.ContractorFilter {
	if filter == nil {
		return nil
	}
	return &models.ContractorFilter{
		MinReputationScore: filter.MinReputationScore,
		RequiredSkills:     filter.RequiredSkills,
		Location:          filter.Location,
		MaxHourlyRate:     filter.MaxHourlyRate,
	}
}
