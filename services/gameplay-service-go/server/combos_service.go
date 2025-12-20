// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1525
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type ComboServiceInterface interface {
	GetLoadout(ctx context.Context, characterID uuid.UUID) (*models.ComboLoadout, error)
	UpdateLoadout(ctx context.Context, characterID uuid.UUID, req *models.UpdateLoadoutRequest) (*models.ComboLoadout, error)
	SubmitScore(ctx context.Context, req *models.SubmitScoreRequest) (*models.ScoreSubmissionResponse, error)
	GetAnalytics(ctx context.Context, comboID, characterID *uuid.UUID, periodStart, periodEnd *time.Time, limit, offset int) (*models.AnalyticsResponse, error)
	ActivateCombo(ctx context.Context, characterID, comboID uuid.UUID, participants []uuid.UUID, context map[string]interface{}) (*models.ComboActivation, error)
}

type ComboService struct {
	repo   ComboRepositoryInterface
	logger *logrus.Logger
}

func NewComboService(db *pgxpool.Pool) *ComboService {
	return &ComboService{
		repo:   NewComboRepository(db),
		logger: logrus.New(),
	}
}

func (s *ComboService) GetLoadout(ctx context.Context, characterID uuid.UUID) (*models.ComboLoadout, error) {
	return s.repo.GetLoadout(ctx, characterID)
}

func (s *ComboService) UpdateLoadout(ctx context.Context, characterID uuid.UUID, req *models.UpdateLoadoutRequest) (*models.ComboLoadout, error) {
	loadout, err := s.repo.GetLoadout(ctx, characterID)
	if err != nil {
		loadout = &models.ComboLoadout{
			ID:          uuid.New(),
			CharacterID: characterID,
			CreatedAt:   time.Now(),
		}
	}

	if req.ActiveCombos != nil {
		loadout.ActiveCombos = req.ActiveCombos
	}

	if req.Preferences != nil {
		loadout.Preferences = req.Preferences
	}

	loadout.UpdatedAt = time.Now()

	return s.repo.SaveLoadout(ctx, loadout)
}

func (s *ComboService) SubmitScore(ctx context.Context, req *models.SubmitScoreRequest) (*models.ScoreSubmissionResponse, error) {
	_, err := s.repo.GetActivation(ctx, req.ActivationID)
	if err != nil {
		return nil, errors.New("activation not found")
	}

	totalScore := calculateTotalScore(req.ExecutionDifficulty, req.DamageOutput, req.VisualImpact, req.TeamCoordination)
	category := calculateCategory(totalScore)

	score := &models.ComboScore{
		ActivationID:        req.ActivationID,
		ExecutionDifficulty: req.ExecutionDifficulty,
		DamageOutput:        req.DamageOutput,
		VisualImpact:        req.VisualImpact,
		TeamCoordination:    req.TeamCoordination,
		TotalScore:          totalScore,
		Category:            category,
		Timestamp:           time.Now(),
	}

	err = s.repo.SaveScore(ctx, score)
	if err != nil {
		return nil, err
	}

	rewards := calculateRewards(totalScore, category)

	response := &models.ScoreSubmissionResponse{
		Success: true,
		Score:   *score,
		Rewards: rewards,
	}

	return response, nil
}

func (s *ComboService) GetAnalytics(ctx context.Context, comboID, characterID *uuid.UUID, periodStart, periodEnd *time.Time, limit, offset int) (*models.AnalyticsResponse, error) {
	if periodStart == nil {
		start := time.Now().AddDate(0, 0, -30)
		periodStart = &start
	}

	if periodEnd == nil {
		end := time.Now()
		periodEnd = &end
	}

	analytics, err := s.repo.GetAnalytics(ctx, comboID, characterID, periodStart, periodEnd, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.AnalyticsResponse{
		Analytics:   analytics,
		PeriodStart: *periodStart,
		PeriodEnd:   *periodEnd,
	}, nil
}

// ActivateCombo activates a combo for a character
func (s *ComboService) ActivateCombo(ctx context.Context, characterID, comboID uuid.UUID, _ []uuid.UUID, _ map[string]interface{}) (*models.ComboActivation, error) {
	// TODO: Validate combo exists and requirements are met
	activation := &models.ComboActivation{
		ID:          uuid.New(),
		ComboID:     comboID,
		CharacterID: characterID,
		ActivatedAt: time.Now(),
	}

	return s.repo.SaveActivation(ctx, activation)
}

func calculateTotalScore(executionDifficulty, damageOutput, visualImpact int, teamCoordination *int) int {
	baseScore := executionDifficulty*10 + damageOutput/10 + visualImpact*5

	if teamCoordination != nil {
		baseScore += *teamCoordination * 5
	}

	return baseScore
}

func calculateCategory(totalScore int) string {
	if totalScore >= 10000 {
		return "Legendary"
	} else if totalScore >= 7500 {
		return "Platinum"
	} else if totalScore >= 5000 {
		return "Gold"
	} else if totalScore >= 2500 {
		return "Silver"
	}
	return "Bronze"
}

func calculateRewards(totalScore int, category string) *models.ScoreRewards {
	experience := totalScore / 10
	currency := totalScore / 20

	if category == "Legendary" {
		experience *= 2
		currency *= 2
	} else if category == "Platinum" {
		experience = int(float64(experience) * 1.5)
		currency = int(float64(currency) * 1.5)
	}

	return &models.ScoreRewards{
		Experience: experience,
		Currency:   currency,
		Items:      []uuid.UUID{},
	}
}
