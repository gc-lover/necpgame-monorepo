// Issue: #140894825 - Social Orders Service: ogen handlers implementation
// PERFORMANCE: Service implementation that adapts existing business logic to new interfaces

package service

import (
	"context"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"social-orders-creation-service-go/internal/models"
	"social-orders-creation-service-go/pkg/orders-creation"
)

// socialOrdersServiceImpl implements SocialOrdersService using existing business logic
type socialOrdersServiceImpl struct {
	existingService *orderscreation.Service
	logger          *zap.Logger
}

// NewSocialOrdersService creates a new social orders service implementation
func NewSocialOrdersService(existingService *orderscreation.Service, logger *zap.Logger) SocialOrdersService {
	return &socialOrdersServiceImpl{
		existingService: existingService,
		logger:          logger,
	}
}

// CreateOrderDraft implements SocialOrdersService
func (s *socialOrdersServiceImpl) CreateOrderDraft(ctx context.Context, clientID uuid.UUID, req models.CreateOrderDraftRequest) (*models.OrderDraftResponse, error) {
	s.logger.Info("CreateOrderDraft called",
		zap.String("client_id", clientID.String()),
		zap.String("title", req.Title))

	// Convert to existing service types
	existingReq := orderscreation.CreateOrderDraftRequest{
		Title:               req.Title,
		Description:         req.Description,
		OrderType:           req.OrderType,
		Reward:              orderscreation.Reward{Currency: req.Reward.Currency, Amount: req.Reward.Amount},
		RiskLevel:           req.RiskLevel,
		Deadline:            req.Deadline,
		Requirements:        s.convertRequirementsToExisting(req.Requirements),
		TargetContractorIDs: req.TargetContractorIDs,
	}

	// Call existing service
	existingResponse, err := s.existingService.CreateOrderDraft(ctx, clientID, existingReq)
	if err != nil {
		return nil, err
	}

	// Convert response back to new types
	return &models.OrderDraftResponse{
		DraftID:                existingResponse.DraftID,
		OrderData:              s.convertFromExistingDraft(existingResponse.OrderData),
		ValidationResults:      s.convertValidationResponseFromExisting(existingResponse.ValidationResults),
		OptimizationSuggestions: s.convertOptimizationResponseFromExisting(existingResponse.OptimizationSuggestions),
		EstimatedCost:          existingResponse.EstimatedCost,
		RecommendedContractors: s.convertContractorSuggestionsFromExisting(existingResponse.RecommendedContractors),
	}, nil
}

// ValidateOrderParameters implements SocialOrdersService
func (s *socialOrdersServiceImpl) ValidateOrderParameters(ctx context.Context, clientID uuid.UUID, req models.ValidateOrderRequest) (*models.OrderValidationResponse, error) {
	s.logger.Info("ValidateOrderParameters called",
		zap.String("client_id", clientID.String()))

	// Convert to existing service types
	existingReq := orderscreation.ValidateOrderRequest{
		OrderParameters: s.convertToExistingDraft(req.OrderParameters),
		ClientID:        req.ClientID,
	}

	// Call existing service
	existingResponse, err := s.existingService.ValidateOrderParameters(ctx, clientID, existingReq)
	if err != nil {
		return nil, err
	}

	// Convert response back
	return s.convertValidationResponseFromExisting(&existingResponse), nil
}

// OptimizeOrderParameters implements SocialOrdersService
func (s *socialOrdersServiceImpl) OptimizeOrderParameters(ctx context.Context, clientID uuid.UUID, req models.OptimizeOrderRequest) (*models.OrderOptimizationResponse, error) {
	s.logger.Info("OptimizeOrderParameters called",
		zap.String("client_id", clientID.String()))

	// Convert to existing service types
	existingReq := orderscreation.OptimizeOrderRequest{
		OrderParameters:   s.convertToExistingDraft(req.OrderParameters),
		OptimizationGoals: req.OptimizationGoals,
	}

	// Call existing service
	existingResponse, err := s.existingService.OptimizeOrderParameters(ctx, clientID, existingReq)
	if err != nil {
		return nil, err
	}

	// Convert response back
	return s.convertOptimizationResponseFromExisting(&existingResponse), nil
}

// SuggestContractors implements SocialOrdersService
func (s *socialOrdersServiceImpl) SuggestContractors(ctx context.Context, req models.SuggestContractorsRequest, maxSuggestions int) (*models.SuggestedContractorsResponse, error) {
	s.logger.Info("SuggestContractors called",
		zap.Int("max_suggestions", maxSuggestions))

	// Convert to existing service types
	existingReq := orderscreation.SuggestContractorsRequest{
		OrderParameters: s.convertToExistingDraft(req.OrderParameters),
		MaxSuggestions:  req.MaxSuggestions,
		FilterCriteria:  s.convertFilterCriteriaToExisting(req.FilterCriteria),
	}

	// Call existing service
	existingResponse, err := s.existingService.SuggestContractors(ctx, existingReq, maxSuggestions)
	if err != nil {
		return nil, err
	}

	// Convert response back
	return &models.SuggestedContractorsResponse{
		Suggestions:    s.convertContractorSuggestionsFromExisting(existingResponse.Suggestions),
		TotalAvailable: existingResponse.TotalAvailable,
		SearchCriteria: existingResponse.SearchCriteria,
	}, nil
}

// CreateOrderWithValidation implements SocialOrdersService
func (s *socialOrdersServiceImpl) CreateOrderWithValidation(ctx context.Context, clientID uuid.UUID, req models.CreateOrderWithValidationRequest) (*models.OrderCreatedResponse, error) {
	s.logger.Info("CreateOrderWithValidation called",
		zap.String("client_id", clientID.String()))

	// Convert to existing service types
	existingReq := orderscreation.CreateOrderWithValidationRequest{
		OrderParameters:    s.convertToExistingDraft(req.OrderParameters),
		ApplyOptimizations: req.ApplyOptimizations,
		NotifyContractors:  req.NotifyContractors,
	}

	// Call existing service
	existingResponse, err := s.existingService.CreateOrderWithValidation(ctx, clientID, existingReq)
	if err != nil {
		return nil, err
	}

	// Convert response back
	return &models.OrderCreatedResponse{
		Order:                  existingResponse.Order,
		AppliedOptimizations:    existingResponse.AppliedOptimizations,
		NotificationsSent:      existingResponse.NotificationsSent,
		EstimatedCompletionTime: existingResponse.EstimatedCompletionTime,
		SuccessProbability:     existingResponse.SuccessProbability,
	}, nil
}

// Helper conversion methods

func (s *socialOrdersServiceImpl) convertRequirementsToExisting(req *models.OrderRequirements) *orderscreation.OrderRequirements {
	if req == nil {
		return nil
	}
	return &orderscreation.OrderRequirements{
		MinLevel:   req.MinLevel,
		Skills:     req.Skills,
		Reputation: req.Reputation,
	}
}

func (s *socialOrdersServiceImpl) convertFromExistingDraft(draft interface{}) models.CreateOrderDraftRequest {
	// Type assertion and conversion
	if existingDraft, ok := draft.(orderscreation.CreateOrderDraftRequest); ok {
		return models.CreateOrderDraftRequest{
			Title:               existingDraft.Title,
			Description:         existingDraft.Description,
			OrderType:           existingDraft.OrderType,
			Reward:              models.Reward{Currency: existingDraft.Reward.Currency, Amount: existingDraft.Reward.Amount},
			RiskLevel:           existingDraft.RiskLevel,
			Deadline:            existingDraft.Deadline,
			Requirements:        s.convertRequirementsFromExisting(existingDraft.Requirements),
			TargetContractorIDs: existingDraft.TargetContractorIDs,
		}
	}
	// Return empty if conversion fails
	return models.CreateOrderDraftRequest{}
}

func (s *socialOrdersServiceImpl) convertRequirementsFromExisting(req *orderscreation.OrderRequirements) *models.OrderRequirements {
	if req == nil {
		return nil
	}
	return &models.OrderRequirements{
		MinLevel:   req.MinLevel,
		Skills:     req.Skills,
		Reputation: req.Reputation,
	}
}

func (s *socialOrdersServiceImpl) convertValidationResponseFromExisting(resp *orderscreation.OrderValidationResponse) *models.OrderValidationResponse {
	if resp == nil {
		return nil
	}
	return &models.OrderValidationResponse{
		IsValid:  resp.IsValid,
		Errors:   s.convertValidationErrorsFromExisting(resp.Errors),
		Warnings: s.convertValidationWarningsFromExisting(resp.Warnings),
	}
}

func (s *socialOrdersServiceImpl) convertValidationErrorsFromExisting(errors []orderscreation.ValidationError) []models.ValidationError {
	if errors == nil {
		return nil
	}
	result := make([]models.ValidationError, len(errors))
	for i, err := range errors {
		result[i] = models.ValidationError{
			Field:     err.Field,
			ErrorType: err.ErrorType,
			Message:   err.Message,
			Suggestion: err.Suggestion,
		}
	}
	return result
}

func (s *socialOrdersServiceImpl) convertValidationWarningsFromExisting(warnings []orderscreation.ValidationWarning) []models.ValidationWarning {
	if warnings == nil {
		return nil
	}
	result := make([]models.ValidationWarning, len(warnings))
	for i, warn := range warnings {
		result[i] = models.ValidationWarning{
			Field:       warn.Field,
			WarningType: warn.WarningType,
			Message:     warn.Message,
			Suggestion:  warn.Suggestion,
		}
	}
	return result
}

func (s *socialOrdersServiceImpl) convertOptimizationResponseFromExisting(resp *orderscreation.OrderOptimizationResponse) *models.OrderOptimizationResponse {
	if resp == nil {
		return nil
	}
	return &models.OrderOptimizationResponse{
		OriginalParameters:  s.convertFromExistingDraft(resp.OriginalParameters),
		OptimizedParameters: s.convertFromExistingDraft(resp.OptimizedParameters),
		Improvements:        s.convertImprovementsFromExisting(resp.Improvements),
		ExpectedOutcomes:    s.convertExpectedOutcomesFromExisting(resp.ExpectedOutcomes),
	}
}

func (s *socialOrdersServiceImpl) convertImprovementsFromExisting(improvements []orderscreation.Improvement) []models.Improvement {
	if improvements == nil {
		return nil
	}
	result := make([]models.Improvement, len(improvements))
	for i, imp := range improvements {
		result[i] = models.Improvement{
			Parameter:      imp.Parameter,
			OriginalValue:  imp.OriginalValue,
			OptimizedValue: imp.OptimizedValue,
			ImprovementType: imp.ImprovementType,
			Impact:         imp.Impact,
		}
	}
	return result
}

func (s *socialOrdersServiceImpl) convertExpectedOutcomesFromExisting(outcomes orderscreation.ExpectedOutcomes) models.ExpectedOutcomes {
	return models.ExpectedOutcomes{
		EstimatedSuccessRate:   outcomes.EstimatedSuccessRate,
		EstimatedCompletionTime: outcomes.EstimatedCompletionTime,
		TotalCostSavings:       outcomes.TotalCostSavings,
	}
}

func (s *socialOrdersServiceImpl) convertContractorSuggestionsFromExisting(suggestions []orderscreation.ContractorSuggestion) []models.ContractorSuggestion {
	if suggestions == nil {
		return nil
	}
	result := make([]models.ContractorSuggestion, len(suggestions))
	for i, sug := range suggestions {
		result[i] = models.ContractorSuggestion{
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

func (s *socialOrdersServiceImpl) convertToExistingDraft(draft models.CreateOrderDraftRequest) orderscreation.CreateOrderDraftRequest {
	return orderscreation.CreateOrderDraftRequest{
		Title:               draft.Title,
		Description:         draft.Description,
		OrderType:           draft.OrderType,
		Reward:              orderscreation.Reward{Currency: draft.Reward.Currency, Amount: draft.Reward.Amount},
		RiskLevel:           draft.RiskLevel,
		Deadline:            draft.Deadline,
		Requirements:        s.convertRequirementsToExisting(draft.Requirements),
		TargetContractorIDs: draft.TargetContractorIDs,
	}
}

func (s *socialOrdersServiceImpl) convertFilterCriteriaToExisting(filter *models.ContractorFilter) *orderscreation.ContractorFilter {
	if filter == nil {
		return nil
	}
	return &orderscreation.ContractorFilter{
		MinReputationScore: filter.MinReputationScore,
		RequiredSkills:     filter.RequiredSkills,
		Location:          filter.Location,
		MaxHourlyRate:     filter.MaxHourlyRate,
	}
}
