// Issue: #1578
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/combat-combos-service-ogen-go/pkg/api"
)

// Common errors
var (
	ErrNotFound            = errors.New("not found")
	ErrRequirementsNotMet  = errors.New("requirements not met")
	ErrSynergyUnavailable  = errors.New("synergy unavailable")
)

// Service implements business logic for combat combos
// SOLID: Single Responsibility - business logic only
type Service struct {
	repo *Repository
}

// NewService creates new service with dependency injection
func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

// GetComboCatalog returns combo catalog with filtering
func (s *Service) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (*api.ComboCatalogResponse, error) {
	combos, total, err := s.repo.GetComboCatalog(ctx, params)
	if err != nil {
		return nil, err
	}

	return &api.ComboCatalogResponse{
		Combos: combos,
		Total:  api.NewOptInt(int(total)),
	}, nil
}

// GetComboDetails returns detailed combo information
func (s *Service) GetComboDetails(ctx context.Context, comboId string) (*api.ComboDetails, error) {
	return s.repo.GetComboDetails(ctx, comboId)
}

// ActivateCombo activates combo for character
func (s *Service) ActivateCombo(ctx context.Context, req *api.ActivateComboRequest) (*api.ComboActivationResponse, error) {
	// Validate combo exists
	combo, err := s.repo.GetComboByID(ctx, req.ComboID.String())
	if err != nil {
		return nil, ErrNotFound
	}

	// TODO: Check requirements (character level, stats, etc.)
	// For now, simplified validation
	_ = combo

	// Create activation record
	activation, err := s.repo.CreateActivation(ctx, req)
	if err != nil {
		return nil, err
	}

	// TODO: Calculate effects and bonuses
	_ = activation // Will be used for effects calculation

	return &api.ComboActivationResponse{
		ActivationID: req.ComboID,
	}, nil
}

// ApplySynergy applies synergy to activated combo
func (s *Service) ApplySynergy(ctx context.Context, req *api.ApplySynergyRequest) (*api.SynergyApplicationResponse, error) {
	// Validate activation exists
	activation, err := s.repo.GetActivation(ctx, req.ActivationID.String())
	if err != nil {
		return nil, ErrNotFound
	}

	// Validate synergy exists
	synergy, err := s.repo.GetSynergy(ctx, req.SynergyID.String())
	if err != nil {
		return nil, ErrNotFound
	}

	// TODO: Check synergy availability and requirements
	_ = activation
	_ = synergy

	// Save synergy application
	if err := s.repo.SaveSynergyApplication(ctx, req.ActivationID.String(), req.SynergyID.String()); err != nil {
		return nil, err
	}

	// Return synergy application response
	return &api.SynergyApplicationResponse{}, nil
}

// GetComboLoadout returns character's combo loadout
func (s *Service) GetComboLoadout(ctx context.Context, characterId string) (*api.ComboLoadout, error) {
	return s.repo.GetComboLoadout(ctx, characterId)
}

// UpdateComboLoadout updates character's combo loadout
func (s *Service) UpdateComboLoadout(ctx context.Context, req *api.UpdateLoadoutRequest) (*api.ComboLoadout, error) {
	// TODO: Validate combo IDs exist
	return s.repo.UpdateComboLoadout(ctx, req)
}

// SubmitComboScore submits combo scoring results
func (s *Service) SubmitComboScore(ctx context.Context, req *api.SubmitScoreRequest) (*api.ScoreSubmissionResponse, error) {
	// Validate activation exists
	activation, err := s.repo.GetActivation(ctx, req.ActivationID.String())
	if err != nil {
		return nil, ErrNotFound
	}

	// Calculate total score (fields are direct in SubmitScoreRequest)
	teamCoord := 0
	if req.TeamCoordination.IsSet() {
		teamCoord = req.TeamCoordination.Value
	}
	
	totalScore := int32(req.ExecutionDifficulty + req.DamageOutput +
		req.VisualImpact + teamCoord)

	// Determine category based on score
	category := "Bronze"
	switch {
	case totalScore >= 400:
		category = "Legendary"
	case totalScore >= 300:
		category = "Platinum"
	case totalScore >= 200:
		category = "Gold"
	case totalScore >= 100:
		category = "Silver"
	}

	// Save score
	scoreRecord := &ScoreRecord{
		ActivationID:        activation.ID,
		ExecutionDifficulty: int32(req.ExecutionDifficulty),
		DamageOutput:        int32(req.DamageOutput),
		VisualImpact:        int32(req.VisualImpact),
		TeamCoordination:    int32(teamCoord),
		TotalScore:          totalScore,
		Category:            category,
	}

	if err := s.repo.SaveScore(ctx, scoreRecord); err != nil {
		return nil, err
	}

	return &api.ScoreSubmissionResponse{}, nil
}

// GetComboAnalytics returns combo effectiveness analytics
func (s *Service) GetComboAnalytics(ctx context.Context, params api.GetComboAnalyticsParams) (*api.AnalyticsResponse, error) {
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
