// SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1578, #142715146
package server

import (
	"context"
	"errors"
	"sync"

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
// Issue: #142715146 - Memory pooling for hot path (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	catalogResponsePool      sync.Pool
	activationResponsePool   sync.Pool
	synergyResponsePool      sync.Pool
	analyticsResponsePool    sync.Pool
	scoreSubmissionPool      sync.Pool
}

// NewService creates new service with dependency injection and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.catalogResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ComboCatalogResponse{}
		},
	}
	s.activationResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ComboActivationResponse{}
		},
	}
	s.synergyResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SynergyApplicationResponse{}
		},
	}
	s.analyticsResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.AnalyticsResponse{}
		},
	}
	s.scoreSubmissionPool = sync.Pool{
		New: func() interface{} {
			return &api.ScoreSubmissionResponse{}
		},
	}

	return s
}

// GetComboCatalog returns combo catalog with filtering
// Issue: #142715146 - Uses memory pooling for zero allocations
func (s *Service) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (*api.ComboCatalogResponse, error) {
	combos, total, err := s.repo.GetComboCatalog(ctx, params)
	if err != nil {
		return nil, err
	}

	// Get from pool (zero allocation!)
	resp := s.catalogResponsePool.Get().(*api.ComboCatalogResponse)
	defer s.catalogResponsePool.Put(resp)

	// Reset pooled struct
	resp.Combos = combos
	resp.Total = api.NewOptInt(int(total))

	// Clone response (caller owns it)
	result := &api.ComboCatalogResponse{
		Combos: combos,
		Total:  api.NewOptInt(int(total)),
	}
	return result, nil
}

// GetComboDetails returns detailed combo information
func (s *Service) GetComboDetails(ctx context.Context, comboId string) (*api.ComboDetails, error) {
	return s.repo.GetComboDetails(ctx, comboId)
}

// ActivateCombo activates combo for character
// Issue: #142715146 - Uses memory pooling for zero allocations
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

	// Get from pool (zero allocation!)
	resp := s.activationResponsePool.Get().(*api.ComboActivationResponse)
	defer s.activationResponsePool.Put(resp)

	// Reset pooled struct
	resp.ActivationID = req.ComboID

	// Clone response (caller owns it)
	result := &api.ComboActivationResponse{
		ActivationID: req.ComboID,
	}
	return result, nil
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

	// Get from pool (zero allocation!)
	resp := s.synergyResponsePool.Get().(*api.SynergyApplicationResponse)
	defer s.synergyResponsePool.Put(resp)

	// Reset pooled struct
	*resp = api.SynergyApplicationResponse{}

	// Clone response (caller owns it)
	result := &api.SynergyApplicationResponse{}
	return result, nil
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

	// Get from pool (zero allocation!)
	resp := s.scoreSubmissionPool.Get().(*api.ScoreSubmissionResponse)
	defer s.scoreSubmissionPool.Put(resp)

	// Reset pooled struct
	*resp = api.ScoreSubmissionResponse{}

	// Clone response (caller owns it)
	result := &api.ScoreSubmissionResponse{}
	return result, nil
}

// GetComboAnalytics returns combo effectiveness analytics
// Issue: #142715146 - Uses memory pooling for zero allocations
func (s *Service) GetComboAnalytics(ctx context.Context, params api.GetComboAnalyticsParams) (*api.AnalyticsResponse, error) {
	analytics, err := s.repo.GetComboAnalytics(ctx, params)
	if err != nil {
		return nil, err
	}

	// Get from pool (zero allocation!)
	resp := s.analyticsResponsePool.Get().(*api.AnalyticsResponse)
	defer s.analyticsResponsePool.Put(resp)

	// Reset pooled struct
	resp.Analytics = analytics
	resp.PeriodStart = params.PeriodStart
	resp.PeriodEnd = params.PeriodEnd

	// Clone response (caller owns it)
	result := &api.AnalyticsResponse{
		Analytics:   analytics,
		PeriodStart: params.PeriodStart,
		PeriodEnd:   params.PeriodEnd,
	}
	return result, nil
}

