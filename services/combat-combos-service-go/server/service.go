// Package server Issue: #158
package server

import (
	"context"
	"errors"
	"fmt"
	"sync"

	"github.com/eapache/go-resiliency/breaker"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/pkg/api"
)

// Error types for handlers
var (
	ErrNotFound           = errors.New("not found")
	ErrRequirementsNotMet = errors.New("requirements not met")
	ErrSynergyUnavailable = errors.New("synergy unavailable")
)

// Service implements the core business logic for combat combos
type Service struct {
	repo           *CombatCombosRepository
	circuitBreaker *breaker.Breaker
	logger         *zap.Logger

	// Performance optimizations
	responsePool     sync.Pool
	byteBufferPool   sync.Pool
	requestSemaphore chan struct{}
}

// NewService creates a new service instance
func NewService(repo *CombatCombosRepository) *Service {
	service := &Service{
		repo:             repo,
		circuitBreaker:   circuitBreaker,
		logger:           logger,
		requestSemaphore: make(chan struct{}, 100), // Load shedding: max 100 concurrent requests
	}

	// Initialize object pools for memory efficiency
	service.responsePool = sync.Pool{
		New: func() interface{} {
			return &models.ActivateComboResponse{}
		},
	}

	service.byteBufferPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 0, 1024)
		},
	}

	return service
}

// getComboCatalog retrieves combos from the catalog with filtering
func (s *Service) getComboCatalog(ctx context.Context, comboType *models.ComboType, complexity *models.ComboComplexity, limit, offset int) (*models.ComboCatalogResponse, error) {
	var response *models.ComboCatalogResponse
	var err error

	// Execute with circuit breaker
	circuitErr := s.circuitBreaker.Run(func() error {
		combos, total, repoErr := s.repo.GetComboCatalog(ctx, comboType, complexity, limit, offset)
		if repoErr != nil {
			return repoErr
		}

		response = &models.ComboCatalogResponse{
			Combos: combos,
			Total:  total,
		}
		return nil
	})

	if circuitErr != nil {
		s.logger.Error("Circuit breaker triggered for GetComboCatalog", zap.Error(circuitErr))
		return nil, fmt.Errorf("service temporarily unavailable: %w", circuitErr)
	}

	return response, err
}

// GetComboDetail retrieves detailed information about a specific combo
func (s *Service) GetComboDetail(ctx context.Context, comboID string) (*models.ComboDetailResponse, error) {
	combo, err := s.repo.GetComboByID(ctx, comboID)
	if err != nil {
		return nil, err
	}
	if combo == nil {
		return nil, fmt.Errorf("combo not found")
	}

	// For now, return combo without synergies (can be extended)
	return &models.ComboDetailResponse{
		Combo:     *combo,
		Synergies: []models.ComboSynergy{},
	}, nil
}

// activateCombo processes a combo activation request
func (s *Service) activateCombo(ctx context.Context, req *models.ActivateComboRequest) (*models.ActivateComboResponse, error) {
	// Acquire semaphore for load shedding
	select {
	case s.requestSemaphore <- struct{}{}:
		defer func() { <-s.requestSemaphore }()
	default:
		s.logger.Warn("Request rejected due to load shedding")
		return nil, fmt.Errorf("service is currently overloaded")
	}

	// Get combo from catalog
	combo, err := s.repo.GetComboByID(ctx, req.ComboID)
	if err != nil {
		return nil, fmt.Errorf("failed to get combo: %w", err)
	}
	if combo == nil {
		return nil, fmt.Errorf("combo not found")
	}

	// Validate requirements
	if err := s.validateComboRequirements(combo, req); err != nil {
		return nil, fmt.Errorf("combo requirements not met: %w", err)
	}

	// Create activation record
	activation := &models.ComboActivation{
		ComboID:      req.ComboID,
		CharacterID:  req.CharacterID,
		Participants: req.Participants,
		Context:      req.Context,
		Success:      true,
		Score:        0, // Will be calculated
		Duration:     0, // Will be calculated
	}

	// Calculate scoring
	score := s.calculateComboScore(combo, req)
	activation.Score = score

	// Apply synergies
	bonuses := s.calculateSynergies(combo, req)

	// Save activation
	if err := s.repo.ActivateCombo(ctx, activation); err != nil {
		s.logger.Error("Failed to save combo activation", zap.Error(err))
		return nil, fmt.Errorf("failed to save activation: %w", err)
	}

	// Save scoring
	scoring := &models.ComboScoring{
		ActivationID:        activation.ID,
		ExecutionDifficulty: s.calculateExecutionDifficulty(combo),
		DamageOutput:        s.calculateDamageOutput(combo, req),
		VisualImpact:        s.calculateVisualImpact(combo),
		TeamCoordination:    s.calculateTeamCoordination(req),
		TotalScore:          score,
		Category:            s.determineScoreCategory(score),
	}
	if err := s.repo.SaveComboScoring(ctx, scoring); err != nil {
		s.logger.Error("Failed to save combo scoring", zap.Error(err))
		// Don't fail the request for scoring errors
	}

	// Get response from pool
	response := s.responsePool.Get().(*models.ActivateComboResponse)
	response.ActivationID = activation.ID
	response.Success = true
	response.Score = score
	response.Bonuses = bonuses

	// Clean up and return to pool (deferred to avoid leaks)
	defer func() {
		response.ActivationID = ""
		response.Success = false
		response.Score = 0
		response.Bonuses = models.Bonuses{}
		s.responsePool.Put(response)
	}()

	s.logger.Info("Combo activated successfully",
		zap.String("comboID", req.ComboID),
		zap.String("characterID", req.CharacterID),
		zap.Int("score", score))

	return response, nil
}

// getComboLoadout retrieves a character's combo loadout
func (s *Service) getComboLoadout(ctx context.Context, characterID string) (*models.ComboLoadoutResponse, error) {
	loadout, err := s.repo.GetComboLoadout(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return &models.ComboLoadoutResponse{Loadout: *loadout}, nil
}

// updateComboLoadout updates a character's combo loadout
func (s *Service) updateComboLoadout(ctx context.Context, characterID string, req *models.ComboLoadoutRequest) (*models.ComboLoadoutResponse, error) {
	loadout := &models.ComboLoadout{
		CharacterID:  characterID,
		ActiveCombos: req.ActiveCombos,
		Preferences:  req.Preferences,
		AutoActivate: req.AutoActivate,
	}

	if err := s.repo.UpdateComboLoadout(ctx, loadout); err != nil {
		return nil, err
	}

	return &models.ComboLoadoutResponse{Loadout: *loadout}, nil
}

// getComboAnalytics retrieves analytics data
func (s *Service) getComboAnalytics(ctx context.Context, days int) (*models.ComboAnalyticsResponse, error) {
	return s.repo.GetComboAnalytics(ctx, days)
}

// Helper methods for combo logic

func (s *Service) validateComboRequirements(combo *models.ComboCatalog, req *models.ActivateComboRequest) error {
	// Basic validation - can be extended with external service calls
	participantCount := len(req.Participants)
	if participantCount == 0 {
		participantCount = 1 // Solo combo
	}

	if combo.Requirements.MinParticipants > 0 && participantCount < combo.Requirements.MinParticipants {
		return fmt.Errorf("insufficient participants: need %d, got %d", combo.Requirements.MinParticipants, participantCount)
	}

	if combo.Requirements.MaxParticipants > 0 && participantCount > combo.Requirements.MaxParticipants {
		return fmt.Errorf("too many participants: max %d, got %d", combo.Requirements.MaxParticipants, participantCount)
	}

	return nil
}

func (s *Service) calculateComboScore(combo *models.ComboCatalog, req *models.ActivateComboRequest) int {
	// Simplified scoring calculation
	baseScore := 100

	// Complexity multiplier
	switch combo.Complexity {
	case models.ComboComplexityBronze:
		baseScore *= 1
	case models.ComboComplexitySilver:
		baseScore *= 2
	case models.ComboComplexityGold:
		baseScore *= 3
	case models.ComboComplexityPlatinum:
		baseScore *= 4
	case models.ComboComplexityLegendary:
		baseScore *= 5
	}

	// Team bonus
	if len(req.Participants) > 1 {
		baseScore = int(float64(baseScore) * 1.5)
	}

	return baseScore
}

// Methods to match generated handlers interface

// GetComboCatalog retrieves combos from the catalog with filtering
func (s *Service) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (api.GetComboCatalogRes, error) {
	var comboType *models.ComboType
	if params.Type != nil {
		ct := models.ComboType(*params.Type)
		comboType = &ct
	}

	var complexity *models.ComboComplexity
	if params.Complexity != nil {
		c := models.ComboComplexity(*params.Complexity)
		complexity = &c
	}

	limit := 50 // default
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = int(*params.Limit)
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = int(*params.Offset)
	}

	response, err := s.getComboCatalog(ctx, comboType, complexity, limit, offset)
	if err != nil {
		return &api.GetComboCatalogInternalServerError{}, err
	}

	// Convert to API response format
	combos := make([]api.ComboCatalog, len(response.Combos))
	for i, combo := range response.Combos {
		sequence := make([]string, len(combo.Sequence))
		copy(sequence, combo.Sequence)

		combos[i] = api.ComboCatalog{
			ID:              api.OptString{Value: combo.ID, Set: true},
			Name:            api.OptString{Value: combo.Name, Set: true},
			Description:     api.OptString{Value: combo.Description, Set: true},
			ComboType:       api.OptComboType{Value: api.ComboType(combo.ComboType), Set: true},
			Complexity:      api.OptComboComplexity{Value: api.ComboComplexity(combo.Complexity), Set: true},
			Cooldown:        api.OptInt{Value: combo.Cooldown, Set: true},
			ChainCompatible: api.OptBool{Value: combo.ChainCompatible, Set: true},
			CreatedAt:       api.OptDateTime{Value: api.DateTime{Time: combo.CreatedAt}, Set: true},
			// Note: Requirements, Sequence, Bonuses would need conversion - simplified for now
		}
	}

	return &api.GetComboCatalogOK{
		Combos: combos,
		Total:  api.OptInt{Value: response.Total, Set: true},
	}, nil
}

// GetComboDetails retrieves detailed information about a specific combo
func (s *Service) GetComboDetails(ctx context.Context, comboID string) (*api.ComboDetails, error) {
	response, err := s.GetComboDetail(ctx, comboID)
	if err != nil {
		if err.Error() == "combo not found" {
			return nil, ErrNotFound
		}
		return nil, err
	}

	// Convert to API response format
	combo := api.ComboCatalog{
		ID:              api.OptString{Value: response.Combo.ID, Set: true},
		Name:            api.OptString{Value: response.Combo.Name, Set: true},
		Description:     api.OptString{Value: response.Combo.Description, Set: true},
		ComboType:       api.OptComboType{Value: api.ComboType(response.Combo.ComboType), Set: true},
		Complexity:      api.OptComboComplexity{Value: api.ComboComplexity(response.Combo.Complexity), Set: true},
		Cooldown:        api.OptInt{Value: response.Combo.Cooldown, Set: true},
		ChainCompatible: api.OptBool{Value: response.Combo.ChainCompatible, Set: true},
		CreatedAt:       api.OptDateTime{Value: api.DateTime{Time: response.Combo.CreatedAt}, Set: true},
	}

	synergies := make([]api.ComboSynergy, len(response.Synergies))
	for i, synergy := range response.Synergies {
		synergies[i] = api.ComboSynergy{
			ID:          api.OptString{Value: synergy.ID, Set: true},
			SynergyType: api.OptSynergyType{Value: api.SynergyType(synergy.SynergyType), Set: true},
			ComboID:     api.OptString{Value: synergy.ComboID, Set: true},
			// Other fields would need conversion
		}
	}

	return &api.ComboDetails{
		Combo:     combo,
		Synergies: synergies,
	}, nil
}

// ActivateCombo processes a combo activation request
func (s *Service) ActivateCombo(ctx context.Context, req api.ActivateCombo) (*api.ActivateComboNotFound, error) {
	request := &models.ActivateComboRequest{
		ComboID:      req.ComboID,
		CharacterID:  req.CharacterID,
		Participants: req.Participants.Value,
		Context:      make(map[string]interface{}), // Simplified
	}

	response, err := s.activateCombo(ctx, request)
	if err != nil {
		if err.Error() == "combo requirements not met" {
			return &api.ActivateComboBadRequest{}, nil
		}
		if err.Error() == "combo not found" {
			return &api.ActivateComboNotFound{}, nil
		}
		return &api.ActivateComboInternalServerError{}, err
	}

	return &api.ComboActivationResponse{
		ActivationID: api.OptString{Value: response.ActivationID, Set: true},
		Success:      api.OptBool{Value: response.Success, Set: true},
		Score:        api.OptInt{Value: response.Score, Set: true},
	}, nil
}

// ApplySynergy applies a synergy effect
func (s *Service) ApplySynergy() (*api.SynergyApplicationResponse, error) {
	// Simplified implementation - synergy application logic would go here
	return &api.SynergyApplicationResponse{}, nil
}

// GetComboLoadout retrieves a character's combo loadout
func (s *Service) GetComboLoadout(ctx context.Context, params string) (*api.ComboLoadout, error) {
	characterID := params.CharacterID.String()
	response, err := s.getComboLoadout(ctx, characterID)
	if err != nil {
		return &api.GetComboLoadoutInternalServerError{}, err
	}

	loadout := api.ComboLoadout{
		ID:           api.OptString{Value: response.Loadout.ID, Set: true},
		CharacterID:  api.OptString{Value: response.Loadout.CharacterID, Set: true},
		AutoActivate: api.OptBool{Value: response.Loadout.AutoActivate, Set: true},
		CreatedAt:    api.OptDateTime{Value: api.DateTime{Time: response.Loadout.CreatedAt}, Set: true},
		UpdatedAt:    api.OptDateTime{Value: api.DateTime{Time: response.Loadout.UpdatedAt}, Set: true},
		// ActiveCombos and Preferences would need conversion
	}

	return &loadout, nil
}

// UpdateComboLoadout updates a character's combo loadout
func (s *Service) UpdateComboLoadout(ctx context.Context, req api.UpdateComboLoadout) (*api.ComboLoadout, error) {
	characterID := req.CharacterID
	request := &models.ComboLoadoutRequest{
		ActiveCombos: req.ActiveCombos.Value,
		AutoActivate: req.AutoActivate.Value,
		Preferences:  make(map[string]interface{}), // Simplified
	}

	response, err := s.updateComboLoadout(ctx, characterID, request)
	if err != nil {
		return &api.UpdateComboLoadoutInternalServerError{}, err
	}

	loadout := api.ComboLoadout{
		ID:           api.OptString{Value: response.Loadout.ID, Set: true},
		CharacterID:  api.OptString{Value: response.Loadout.CharacterID, Set: true},
		AutoActivate: api.OptBool{Value: response.Loadout.AutoActivate, Set: true},
		CreatedAt:    api.OptDateTime{Value: api.DateTime{Time: response.Loadout.CreatedAt}, Set: true},
		UpdatedAt:    api.OptDateTime{Value: api.DateTime{Time: response.Loadout.UpdatedAt}, Set: true},
	}

	return &loadout, nil
}

// SubmitComboScore submits scoring metrics for a combo
func (s *Service) SubmitComboScore() (*api.ScoreSubmissionResponse, error) {
	// Simplified implementation - scoring submission logic would go here
	return &api.ScoreSubmissionResponse{}, nil
}

// GetComboAnalytics retrieves analytics data
func (s *Service) GetComboAnalytics(ctx context.Context, params api.GetComboAnalyticsParams) (api.GetComboAnalyticsRes, error) {
	days := 7
	if params.Days != nil {
		days = int(*params.Days)
	}

	response, err := s.getComboAnalytics(ctx, days)
	if err != nil {
		return &api.GetComboAnalyticsInternalServerError{}, err
	}

	return &api.GetComboAnalyticsOK{
		TotalActivations: api.OptInt{Value: response.TotalActivations, Set: true},
		SuccessRate:      api.OptFloat64{Value: response.SuccessRate, Set: true},
	}, nil
}

func (s *Service) calculateSynergies(combo *models.ComboCatalog, req *models.ActivateComboRequest) models.Bonuses {
	// Simplified synergy calculation - in production would integrate with other services
	bonuses := models.Bonuses{
		DamageMultiplier: combo.Bonuses.DamageMultiplier,
		EffectBonuses:    make(map[string]float64),
	}

	// Add team synergy bonus
	if len(req.Participants) > 1 {
		bonuses.DamageMultiplier *= 1.2
		bonuses.EffectBonuses["team_synergy"] = 0.15
	}

	// Add timing bonus (simplified)
	if timingBonus, exists := req.Context["timing_bonus"]; exists {
		if timing, ok := timingBonus.(float64); ok {
			bonuses.DamageMultiplier *= 1.0 + timing
		}
	}

	return bonuses
}

func (s *Service) calculateExecutionDifficulty(combo *models.ComboCatalog) int {
	// Simplified calculation based on combo properties
	difficulty := 1

	switch combo.Complexity {
	case models.ComboComplexityBronze:
		difficulty = 1
	case models.ComboComplexitySilver:
		difficulty = 2
	case models.ComboComplexityGold:
		difficulty = 3
	case models.ComboComplexityPlatinum:
		difficulty = 4
	case models.ComboComplexityLegendary:
		difficulty = 5
	}

	if len(combo.Sequence) > 3 {
		difficulty += 1
	}

	return difficulty
}

func (s *Service) calculateDamageOutput(combo *models.ComboCatalog, req *models.ActivateComboRequest) int {
	// Simplified damage calculation
	baseDamage := 100 * len(combo.Sequence)

	// Apply damage multiplier from bonuses
	baseDamage = int(float64(baseDamage) * combo.Bonuses.DamageMultiplier)

	// Team multiplier
	if len(req.Participants) > 1 {
		baseDamage = int(float64(baseDamage) * 1.3)
	}

	return baseDamage
}

func (s *Service) calculateVisualImpact(combo *models.ComboCatalog) int {
	// Simplified visual impact calculation
	impact := 1

	switch combo.Complexity {
	case models.ComboComplexityBronze:
		impact = 1
	case models.ComboComplexitySilver:
		impact = 2
	case models.ComboComplexityGold:
		impact = 3
	case models.ComboComplexityPlatinum:
		impact = 4
	case models.ComboComplexityLegendary:
		impact = 5
	}

	if len(combo.Bonuses.SpecialEffects) > 0 {
		impact += 1
	}

	return impact
}

func (s *Service) calculateTeamCoordination(req *models.ActivateComboRequest) int {
	participantCount := len(req.Participants)
	if participantCount <= 1 {
		return 0 // No coordination needed for solo
	}

	// Simplified coordination score
	return participantCount * 10
}

func (s *Service) determineScoreCategory(score int) string {
	switch {
	case score >= 500:
		return "Legendary"
	case score >= 400:
		return "Platinum"
	case score >= 300:
		return "Gold"
	case score >= 200:
		return "Silver"
	default:
		return "Bronze"
	}
}
