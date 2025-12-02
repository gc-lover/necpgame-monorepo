// Issue: #158
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-go/pkg/api"
)

// Service errors
var (
	ErrNotFound             = errors.New("not found")
	ErrRequirementsNotMet   = errors.New("requirements not met")
	ErrSynergyUnavailable   = errors.New("synergy unavailable")
	ErrInvalidRequest       = errors.New("invalid request")
)

// Service provides business logic for combat combos
type Service struct {
	repo *Repository
}

// NewService creates new service with DI
func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

// GetComboCatalog returns catalog of combos with filtering
func (s *Service) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (*api.ComboCatalogResponse, error) {
	// Add context timeout for external calls
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	combos, total, err := s.repo.GetComboCatalog(ctx, params)
	if err != nil {
		return nil, err
	}

	return &api.ComboCatalogResponse{
		Combos: combos,
		Total:  &total,
	}, nil
}

// GetComboDetails returns detailed information about combo
func (s *Service) GetComboDetails(ctx context.Context, comboId string) (*api.ComboDetails, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	details, err := s.repo.GetComboDetails(ctx, comboId)
	if err != nil {
		return nil, err
	}

	return details, nil
}

// ActivateCombo activates combo for character
func (s *Service) ActivateCombo(ctx context.Context, req *api.ActivateComboRequest) (*api.ComboActivationResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// Validate combo exists
	combo, err := s.repo.GetComboByID(ctx, req.ComboId)
	if err != nil {
		return nil, ErrNotFound
	}

	// Check requirements
	if !s.checkRequirements(ctx, req.CharacterId, combo) {
		return nil, ErrRequirementsNotMet
	}

	// Activate combo
	activation, err := s.repo.CreateActivation(ctx, req)
	if err != nil {
		return nil, err
	}

	// Apply bonuses and synergies
	appliedSynergies, bonuses := s.applySynergiesAndBonuses(ctx, activation, combo)

	return &api.ComboActivationResponse{
		Success:          true,
		ActivationId:     activation.ID,
		Combo:            combo,
		AppliedSynergies: appliedSynergies,
		BonusesApplied:   bonuses,
		ChainComboAvailable: &combo.ChainCompatible,
		Message:          stringPtr("Combo activated successfully"),
	}, nil
}

// ApplySynergy applies synergy to activated combo
func (s *Service) ApplySynergy(ctx context.Context, req *api.ApplySynergyRequest) (*api.SynergyApplicationResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// Validate activation exists
	activation, err := s.repo.GetActivation(ctx, req.ActivationId)
	if err != nil {
		return nil, ErrNotFound
	}

	// Validate synergy exists
	synergy, err := s.repo.GetSynergy(ctx, req.SynergyId)
	if err != nil {
		return nil, ErrNotFound
	}

	// Check synergy requirements
	if !s.checkSynergyRequirements(ctx, activation, synergy) {
		return nil, ErrSynergyUnavailable
	}

	// Apply synergy
	bonuses := s.calculateSynergyBonuses(synergy)

	// Save synergy application
	if err := s.repo.SaveSynergyApplication(ctx, activation.ID, synergy.Id); err != nil {
		return nil, err
	}

	return &api.SynergyApplicationResponse{
		Success:       true,
		Synergy:       synergy,
		BonusesApplied: bonuses,
		Message:       stringPtr("Synergy applied successfully"),
	}, nil
}

// GetComboLoadout returns character's combo loadout
func (s *Service) GetComboLoadout(ctx context.Context, characterId string) (*api.ComboLoadout, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	loadout, err := s.repo.GetComboLoadout(ctx, characterId)
	if err != nil {
		return nil, err
	}

	return loadout, nil
}

// UpdateComboLoadout updates character's combo loadout
func (s *Service) UpdateComboLoadout(ctx context.Context, req *api.UpdateLoadoutRequest) (*api.ComboLoadout, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	loadout, err := s.repo.UpdateComboLoadout(ctx, req)
	if err != nil {
		return nil, err
	}

	return loadout, nil
}

// SubmitComboScore submits combo scoring results
func (s *Service) SubmitComboScore(ctx context.Context, req *api.SubmitScoreRequest) (*api.ScoreSubmissionResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	// Validate activation exists
	activation, err := s.repo.GetActivation(ctx, req.ActivationId)
	if err != nil {
		return nil, ErrNotFound
	}

	// Calculate total score
	totalScore := s.calculateTotalScore(req)
	category := s.determineScoreCategory(totalScore)

	// Calculate rewards based on score
	rewards := s.calculateRewards(totalScore, category)

	// Save score
	score := &ScoreRecord{
		ActivationID:         req.ActivationId,
		ExecutionDifficulty:  req.ExecutionDifficulty,
		DamageOutput:         req.DamageOutput,
		VisualImpact:         req.VisualImpact,
		TeamCoordination:     req.TeamCoordination,
		TotalScore:           totalScore,
		Category:             category,
		Rewards:              rewards,
	}

	if err := s.repo.SaveScore(ctx, score); err != nil {
		return nil, err
	}

	return &api.ScoreSubmissionResponse{
		Success:  true,
		Score:    convertScoreToAPI(score),
		Rewards:  &rewards,
	}, nil
}

// GetComboAnalytics returns combo effectiveness analytics
func (s *Service) GetComboAnalytics(ctx context.Context, params api.GetComboAnalyticsParams) (*api.AnalyticsResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	analytics, err := s.repo.GetComboAnalytics(ctx, params)
	if err != nil {
		return nil, err
	}

	return &api.AnalyticsResponse{
		Analytics:   analytics,
		PeriodStart: params.PeriodStart,
		PeriodEnd:   params.PeriodEnd,
	}, nil
}

// Helper functions

func (s *Service) checkRequirements(ctx context.Context, characterId string, combo *api.Combo) bool {
	// TODO: Implement requirements check
	// - Check character level
	// - Check required skills
	// - Check required abilities
	return true
}

func (s *Service) applySynergiesAndBonuses(ctx context.Context, activation *Activation, combo *api.Combo) ([]api.Synergy, *struct {
	CooldownReduction *int      `json:"cooldown_reduction,omitempty"`
	DamageMultiplier  *float32  `json:"damage_multiplier,omitempty"`
	SpecialEffects    *[]string `json:"special_effects,omitempty"`
}) {
	// TODO: Implement synergies and bonuses application
	synergies := []api.Synergy{}
	
	// Return combo bonuses
	return synergies, combo.Bonuses
}

func (s *Service) checkSynergyRequirements(ctx context.Context, activation *Activation, synergy *api.Synergy) bool {
	// TODO: Implement synergy requirements check
	return true
}

func (s *Service) calculateSynergyBonuses(synergy *api.Synergy) *struct {
	CooldownReduction *int      `json:"cooldown_reduction,omitempty"`
	DamageMultiplier  *float32  `json:"damage_multiplier,omitempty"`
	SpecialEffects    *[]string `json:"special_effects,omitempty"`
} {
	// TODO: Implement synergy bonuses calculation
	return synergy.Bonuses
}

func (s *Service) calculateTotalScore(req *api.SubmitScoreRequest) int32 {
	// Simple formula: average of all metrics * 100
	total := (req.ExecutionDifficulty + req.DamageOutput + req.VisualImpact)
	if req.TeamCoordination != nil {
		total += *req.TeamCoordination
		total = total / 4
	} else {
		total = total / 3
	}
	return total * 100
}

func (s *Service) determineScoreCategory(score int32) string {
	switch {
	case score >= 9000:
		return "Legendary"
	case score >= 7000:
		return "Platinum"
	case score >= 5000:
		return "Gold"
	case score >= 3000:
		return "Silver"
	default:
		return "Bronze"
	}
}

func (s *Service) calculateRewards(score int32, category string) *struct {
	Currency   *int32 `json:"currency,omitempty"`
	Experience *int32 `json:"experience,omitempty"`
	Items      *[]string `json:"items,omitempty"`
} {
	// Calculate rewards based on score and category
	experience := score / 10
	currency := score / 20

	return &struct {
		Currency   *int32 `json:"currency,omitempty"`
		Experience *int32 `json:"experience,omitempty"`
		Items      *[]string `json:"items,omitempty"`
	}{
		Experience: &experience,
		Currency:   &currency,
	}
}

func convertScoreToAPI(score *ScoreRecord) api.ComboScore {
	return api.ComboScore{
		ActivationId:        score.ActivationID,
		ExecutionDifficulty: score.ExecutionDifficulty,
		DamageOutput:        score.DamageOutput,
		VisualImpact:        score.VisualImpact,
		TeamCoordination:    &score.TeamCoordination,
		TotalScore:          score.TotalScore,
		Category:            score.Category,
	}
}

func stringPtr(s string) *string {
	return &s
}

