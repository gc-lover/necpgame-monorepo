package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type EngramCyberpsychosisServiceInterface interface {
	GetCyberpsychosisRisk(ctx context.Context, characterID uuid.UUID) (*CyberpsychosisRiskResult, error)
	UpdateCyberpsychosisRisk(ctx context.Context, characterID uuid.UUID) (*CyberpsychosisRiskResult, error)
	GetBlockers(ctx context.Context, characterID uuid.UUID) ([]*EngramBlockerInfo, error)
	InstallBlocker(ctx context.Context, characterID uuid.UUID, blockerTier int) (*EngramBlockerInfo, error)
}

type CyberpsychosisRiskResult struct {
	CharacterID      uuid.UUID     `json:"character_id"`
	BaseRisk         float64       `json:"base_risk"`
	EngramRisk       float64       `json:"engram_risk"`
	TotalRisk        float64       `json:"total_risk"`
	BlockerReduction float64       `json:"blocker_reduction"`
	RiskFactors      []*RiskFactor `json:"risk_factors"`
}

type EngramBlockerInfo struct {
	BlockerID          uuid.UUID          `json:"blocker_id"`
	CharacterID        uuid.UUID          `json:"character_id"`
	Tier               int                `json:"tier"`
	RiskReduction      float64            `json:"risk_reduction"`
	InfluenceReduction float64            `json:"influence_reduction"`
	DurationDays       int                `json:"duration_days"`
	Buffs              map[string]float64 `json:"buffs,omitempty"`
	Debuffs            map[string]float64 `json:"debuffs,omitempty"`
	InstalledAt        time.Time          `json:"installed_at"`
	ExpiresAt          time.Time          `json:"expires_at"`
	IsActive           bool               `json:"is_active"`
}

var tierBlockers = map[int]struct {
	RiskReduction      float64
	InfluenceReduction float64
	DurationDays       int
	Buffs              map[string]float64
	Debuffs            map[string]float64
}{
	1: {10.0, 20.0, 30, map[string]float64{"mental_resistance": 5.0}, map[string]float64{"energy_recovery": -10.0}},
	2: {15.0, 30.0, 45, map[string]float64{"mental_resistance": 10.0, "concentration": 5.0}, map[string]float64{"energy_recovery": -15.0}},
	3: {20.0, 40.0, 60, map[string]float64{"mental_resistance": 15.0, "concentration": 10.0, "all_skills": 3.0}, map[string]float64{"energy_recovery": -20.0, "physical_strength": -5.0}},
	4: {25.0, 50.0, 90, map[string]float64{"mental_resistance": 20.0, "concentration": 15.0, "all_skills": 5.0, "hack_resistance": 10.0}, map[string]float64{"energy_recovery": -25.0, "physical_strength": -10.0}},
	5: {30.0, 60.0, 180, map[string]float64{"mental_resistance": 25.0, "concentration": 20.0, "all_skills": 7.0, "hack_resistance": 15.0}, map[string]float64{"energy_recovery": -30.0, "physical_strength": -15.0}},
}

type EngramCyberpsychosisService struct {
	repo          EngramCyberpsychosisRepositoryInterface
	engramService EngramServiceInterface
	cache         *redis.Client
	logger        *logrus.Logger
}

func NewEngramCyberpsychosisService(repo EngramCyberpsychosisRepositoryInterface, engramService EngramServiceInterface, cache *redis.Client) *EngramCyberpsychosisService {
	return &EngramCyberpsychosisService{
		repo:          repo,
		engramService: engramService,
		cache:         cache,
		logger:        GetLogger(),
	}
}

func (s *EngramCyberpsychosisService) GetCyberpsychosisRisk(ctx context.Context, characterID uuid.UUID) (*CyberpsychosisRiskResult, error) {
	risk, err := s.repo.GetCyberpsychosisRisk(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if risk == nil {
		return &CyberpsychosisRiskResult{
			CharacterID:      characterID,
			BaseRisk:         0.0,
			EngramRisk:       0.0,
			TotalRisk:        0.0,
			BlockerReduction: 0.0,
			RiskFactors:      []*RiskFactor{},
		}, nil
	}

	return &CyberpsychosisRiskResult{
		CharacterID:      risk.CharacterID,
		BaseRisk:         risk.BaseRisk,
		EngramRisk:       risk.EngramRisk,
		TotalRisk:        risk.TotalRisk,
		BlockerReduction: risk.BlockerReduction,
		RiskFactors:      risk.RiskFactors,
	}, nil
}

func (s *EngramCyberpsychosisService) UpdateCyberpsychosisRisk(ctx context.Context, characterID uuid.UUID) (*CyberpsychosisRiskResult, error) {
	activeEngrams, err := s.engramService.GetActiveEngrams(ctx, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get active engrams for risk calculation")
		return nil, err
	}

	baseRisk := 20.0
	engramRisk := 0.0
	var riskFactors []*RiskFactor

	for _, slot := range activeEngrams {
		if slot.EngramID == nil {
			continue
		}

		engramRisk += 10.0
		riskFactors = append(riskFactors, &RiskFactor{
			FactorType: "base_per_engram",
			RiskAmount: 10.0,
			EngramID:   slot.EngramID,
		})

		// TODO: Add chip tier check when engram data includes chip tier
		// Chip tier should be retrieved from engram metadata, not from slot

		if slot.InfluenceLevel > 50.0 {
			engramRisk += 15.0
			riskFactors = append(riskFactors, &RiskFactor{
				FactorType: "high_influence",
				RiskAmount: 15.0,
				EngramID:   slot.EngramID,
			})
		}
	}

	activeBlockers, err := s.repo.GetActiveBlockers(ctx, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get active blockers")
	}

	blockerReduction := 0.0
	for _, blocker := range activeBlockers {
		blockerReduction += blocker.RiskReduction
	}

	totalRisk := baseRisk + engramRisk - blockerReduction
	if totalRisk < 0 {
		totalRisk = 0
	}
	if totalRisk > 100 {
		totalRisk = 100
	}

	risk := &EngramCyberpsychosisRisk{
		CharacterID:      characterID,
		BaseRisk:         baseRisk,
		EngramRisk:       engramRisk,
		TotalRisk:        totalRisk,
		BlockerReduction: blockerReduction,
		RiskFactors:      riskFactors,
	}

	err = s.repo.CreateOrUpdateCyberpsychosisRisk(ctx, risk)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update cyberpsychosis risk")
		return nil, err
	}

	return &CyberpsychosisRiskResult{
		CharacterID:      risk.CharacterID,
		BaseRisk:         risk.BaseRisk,
		EngramRisk:       risk.EngramRisk,
		TotalRisk:        risk.TotalRisk,
		BlockerReduction: risk.BlockerReduction,
		RiskFactors:      risk.RiskFactors,
	}, nil
}

func (s *EngramCyberpsychosisService) GetBlockers(ctx context.Context, characterID uuid.UUID) ([]*EngramBlockerInfo, error) {
	blockers, err := s.repo.GetBlockers(ctx, characterID)
	if err != nil {
		return nil, err
	}

	var result []*EngramBlockerInfo
	for _, blocker := range blockers {
		result = append(result, &EngramBlockerInfo{
			BlockerID:          blocker.BlockerID,
			CharacterID:        blocker.CharacterID,
			Tier:               blocker.Tier,
			RiskReduction:      blocker.RiskReduction,
			InfluenceReduction: blocker.InfluenceReduction,
			DurationDays:       blocker.DurationDays,
			Buffs:              blocker.Buffs,
			Debuffs:            blocker.Debuffs,
			InstalledAt:        blocker.InstalledAt,
			ExpiresAt:          blocker.ExpiresAt,
			IsActive:           blocker.IsActive,
		})
	}

	return result, nil
}

func (s *EngramCyberpsychosisService) InstallBlocker(ctx context.Context, characterID uuid.UUID, blockerTier int) (*EngramBlockerInfo, error) {
	tierData, ok := tierBlockers[blockerTier]
	if !ok {
		return nil, nil
	}

	expiresAt := time.Now().AddDate(0, 0, tierData.DurationDays)

	blocker := &EngramBlocker{
		CharacterID:        characterID,
		Tier:               blockerTier,
		RiskReduction:      tierData.RiskReduction,
		InfluenceReduction: tierData.InfluenceReduction,
		DurationDays:       tierData.DurationDays,
		Buffs:              tierData.Buffs,
		Debuffs:            tierData.Debuffs,
		ExpiresAt:          expiresAt,
		IsActive:           true,
	}

	err := s.repo.InstallBlocker(ctx, blocker)
	if err != nil {
		s.logger.WithError(err).Error("Failed to install blocker")
		return nil, err
	}

	return &EngramBlockerInfo{
		BlockerID:          blocker.BlockerID,
		CharacterID:        blocker.CharacterID,
		Tier:               blocker.Tier,
		RiskReduction:      blocker.RiskReduction,
		InfluenceReduction: blocker.InfluenceReduction,
		DurationDays:       blocker.DurationDays,
		Buffs:              blocker.Buffs,
		Debuffs:            blocker.Debuffs,
		InstalledAt:        blocker.InstalledAt,
		ExpiresAt:          blocker.ExpiresAt,
		IsActive:           blocker.IsActive,
	}, nil
}
