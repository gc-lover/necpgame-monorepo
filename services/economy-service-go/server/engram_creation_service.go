package server

import (
	"context"
	"errors"
	"math/rand"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

var (
	ErrInvalidChipTier = errors.New("invalid chip tier (must be 1-5)")
	_                  = errors.New("engram creation failed")
	_                  = errors.New("technology not available")
)

type EngramCreationServiceInterface interface {
	GetCreationCost(ctx context.Context, chipTier int) (*EngramCreationCost, error)
	ValidateCreation(ctx context.Context, characterID uuid.UUID, chipTier int, targetPersonID *uuid.UUID) (*ValidateCreationResult, error)
	CreateEngram(ctx context.Context, characterID uuid.UUID, chipTier int, attitudeType string, customAttitudeSettings map[string]interface{}, targetPersonID *uuid.UUID) (*CreateEngramResult, error)
}

type EngramCreationCost struct {
	ChipTier               int      `json:"chip_tier"`
	CreationCostMin        float64  `json:"creation_cost_min"`
	CreationCostMax        float64  `json:"creation_cost_max"`
	PurchaseCostMultiplier float64  `json:"purchase_cost_multiplier"`
	HistoricalMultiplier   *float64 `json:"historical_multiplier,omitempty"`
	MarketFluctuation      float64  `json:"market_fluctuation"`
}

type ValidateCreationResult struct {
	IsValid          bool                  `json:"is_valid"`
	ValidationErrors []string              `json:"validation_errors"`
	Requirements     *CreationRequirements `json:"requirements,omitempty"`
	EstimatedCost    *float64              `json:"estimated_cost,omitempty"`
}

type CreationRequirements struct {
	TechnologyAvailable bool `json:"technology_available"`
	EquipmentAvailable  bool `json:"equipment_available"`
	ReputationMet       bool `json:"reputation_met"`
	SkillsMet           bool `json:"skills_met"`
	FundsAvailable      bool `json:"funds_available"`
}

type CreateEngramResult struct {
	EngramID        uuid.UUID `json:"engram_id"`
	CreationID      uuid.UUID `json:"creation_id"`
	Success         bool      `json:"success"`
	CreationStage   string    `json:"creation_stage"`
	DataLossPercent *float64  `json:"data_loss_percent,omitempty"`
	IsComplete      bool      `json:"is_complete"`
	CreationCost    float64   `json:"creation_cost"`
	CreatedAt       time.Time `json:"created_at"`
}

type EngramCreationService struct {
	repo   EngramCreationRepositoryInterface
	cache  *redis.Client
	logger *logrus.Logger
}

var tierCosts = map[int]struct {
	Min, Max float64
}{
	1: {150000, 250000},
	2: {300000, 500000},
	3: {750000, 1200000},
	4: {1500000, 2500000},
	5: {3000000, 5000000},
}

var _ = map[int]int{
	1: 20,
	2: 50,
	3: 75,
	4: 90,
	5: 95,
}

func NewEngramCreationService(repo EngramCreationRepositoryInterface, cache *redis.Client) *EngramCreationService {
	return &EngramCreationService{
		repo:   repo,
		cache:  cache,
		logger: GetLogger(),
	}
}

func (s *EngramCreationService) GetCreationCost(_ context.Context, chipTier int) (*EngramCreationCost, error) {
	if chipTier < 1 || chipTier > 5 {
		return nil, ErrInvalidChipTier
	}

	costRange := tierCosts[chipTier]
	marketFluctuation := 20.0 + rand.Float64()*30.0

	return &EngramCreationCost{
		ChipTier:               chipTier,
		CreationCostMin:        costRange.Min,
		CreationCostMax:        costRange.Max,
		PurchaseCostMultiplier: 2.5,
		HistoricalMultiplier:   floatPtr(3.5),
		MarketFluctuation:      marketFluctuation,
	}, nil
}

func (s *EngramCreationService) ValidateCreation(ctx context.Context, _ uuid.UUID, chipTier int, _ *uuid.UUID) (*ValidateCreationResult, error) {
	if chipTier < 1 || chipTier > 5 {
		return &ValidateCreationResult{
			IsValid:          false,
			ValidationErrors: []string{"invalid chip tier"},
		}, nil
	}

	var errors []string
	requirements := &CreationRequirements{
		TechnologyAvailable: true,
		EquipmentAvailable:  true,
		ReputationMet:       true,
		SkillsMet:           true,
		FundsAvailable:      true,
	}

	cost, err := s.GetCreationCost(ctx, chipTier)
	if err != nil {
		return nil, err
	}

	estimatedCost := cost.CreationCostMin + (cost.CreationCostMax-cost.CreationCostMin)*0.7

	isValid := len(errors) == 0
	return &ValidateCreationResult{
		IsValid:          isValid,
		ValidationErrors: errors,
		Requirements:     requirements,
		EstimatedCost:    &estimatedCost,
	}, nil
}

func (s *EngramCreationService) CreateEngram(ctx context.Context, characterID uuid.UUID, chipTier int, attitudeType string, customAttitudeSettings map[string]interface{}, targetPersonID *uuid.UUID) (*CreateEngramResult, error) {
	if chipTier < 1 || chipTier > 5 {
		return nil, ErrInvalidChipTier
	}

	cost, err := s.GetCreationCost(ctx, chipTier)
	if err != nil {
		return nil, err
	}

	actualCost := cost.CreationCostMin + rand.Float64()*(cost.CreationCostMax-cost.CreationCostMin)

	creationID := uuid.New()
	engramID := uuid.New()

	dataLossPercent := 5.0 + rand.Float64()*15.0
	if chipTier <= 2 {
		dataLossPercent += 5.0
	}

	isComplete := dataLossPercent < 30.0 && rand.Float64() > 0.1

	creation := &EngramCreation{
		ID:                     uuid.New(),
		CreationID:             creationID,
		EngramID:               engramID,
		CharacterID:            characterID,
		TargetPersonID:         targetPersonID,
		ChipTier:               chipTier,
		AttitudeType:           attitudeType,
		CustomAttitudeSettings: customAttitudeSettings,
		CreationStage:          "completed",
		DataLossPercent:        dataLossPercent,
		IsComplete:             isComplete,
		CreationCost:           actualCost,
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
		CompletedAt:            timePtr(time.Now()),
	}

	err = s.repo.CreateCreationLog(ctx, creation)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create engram creation log")
		return nil, err
	}

	return &CreateEngramResult{
		EngramID:        engramID,
		CreationID:      creationID,
		Success:         true,
		CreationStage:   "completed",
		DataLossPercent: &dataLossPercent,
		IsComplete:      isComplete,
		CreationCost:    actualCost,
		CreatedAt:       time.Now(),
	}, nil
}

func floatPtr(f float64) *float64 {
	return &f
}

func timePtr(t time.Time) *time.Time {
	return &t
}
