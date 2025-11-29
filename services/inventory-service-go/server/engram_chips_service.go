package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

var (
	ErrChipTierNotFound = errors.New("chip tier not found")
	ErrChipNotFound     = errors.New("chip not found")
)

type EngramChipsServiceInterface interface {
	GetChipTiers(ctx context.Context, leagueYear *int) ([]*EngramChipTierInfo, error)
	GetChipTier(ctx context.Context, chipID uuid.UUID) (*EngramChipTierInfo, error)
	GetChipDecay(ctx context.Context, chipID uuid.UUID) (*EngramChipDecayInfo, error)
	UpdateDecayCalculation(ctx context.Context, chipID uuid.UUID) error
}

type EngramChipTierInfo struct {
	Tier                int     `json:"tier"`
	TierName            string  `json:"tier_name"`
	StabilityLevel      string  `json:"stability_level"`
	LifespanYears       int     `json:"lifespan_years"`
	LifespanRange       *LifespanRange `json:"lifespan_range,omitempty"`
	CorruptionRisk      string  `json:"corruption_risk"`
	CorruptionRiskPercent float64 `json:"corruption_risk_percent"`
	ProtectionLevel     string  `json:"protection_level"`
	CreationCostMin     float64 `json:"creation_cost_min"`
	CreationCostMax     float64 `json:"creation_cost_max"`
	AvailableFromYear   int     `json:"available_from_year"`
	IsAvailable         bool    `json:"is_available"`
}

type LifespanRange struct {
	Min int `json:"min"`
	Max int `json:"max"`
}

type EngramChipDecayInfo struct {
	ChipID                 uuid.UUID              `json:"chip_id"`
	DecayPercent           float64                `json:"decay_percent"`
	DecayRisk              string                 `json:"decay_risk"`
	StorageConditions      *StorageConditions     `json:"storage_conditions"`
	TimeUntilCritical      *int                   `json:"time_until_critical,omitempty"`
	DecayEffects           []string               `json:"decay_effects"`
}

type StorageConditions struct {
	Temperature           string `json:"temperature"`
	Humidity              string `json:"humidity"`
	ElectromagneticShield bool   `json:"electromagnetic_shield"`
	StorageTimeOutside    int    `json:"storage_time_outside"`
}

type EngramChipsService struct {
	repo     EngramChipsRepositoryInterface
	cache    *redis.Client
	logger   *logrus.Logger
}

func NewEngramChipsService(repo EngramChipsRepositoryInterface, cache *redis.Client) *EngramChipsService {
	return &EngramChipsService{
		repo:   repo,
		cache:  cache,
		logger: GetLogger(),
	}
}

func (s *EngramChipsService) GetChipTiers(ctx context.Context, leagueYear *int) ([]*EngramChipTierInfo, error) {
	tiers, err := s.repo.GetChipTiers(ctx, leagueYear)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chip tiers")
		return nil, err
	}

	result := make([]*EngramChipTierInfo, 0, len(tiers))
	for _, tier := range tiers {
		info := &EngramChipTierInfo{
			Tier:                tier.Tier,
			TierName:            tier.TierName,
			StabilityLevel:      tier.StabilityLevel,
			LifespanYears:       tier.LifespanYearsMin,
			CorruptionRisk:      tier.CorruptionRisk,
			CorruptionRiskPercent: tier.CorruptionRiskPercent,
			ProtectionLevel:     tier.ProtectionLevel,
			CreationCostMin:     tier.CreationCostMin,
			CreationCostMax:     tier.CreationCostMax,
			AvailableFromYear:   tier.AvailableFromYear,
		}

		if tier.LifespanYearsMin != tier.LifespanYearsMax {
			info.LifespanRange = &LifespanRange{
				Min: tier.LifespanYearsMin,
				Max: tier.LifespanYearsMax,
			}
		}

		if leagueYear != nil {
			info.IsAvailable = tier.AvailableFromYear <= *leagueYear
		} else {
			info.IsAvailable = true
		}

		result = append(result, info)
	}

	return result, nil
}

func (s *EngramChipsService) GetChipTier(ctx context.Context, chipID uuid.UUID) (*EngramChipTierInfo, error) {
	tier, err := s.repo.GetChipTierByChipID(ctx, chipID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chip tier")
		return nil, err
	}

	if tier == nil {
		return nil, ErrChipNotFound
	}

	info := &EngramChipTierInfo{
		Tier:                tier.Tier,
		TierName:            tier.TierName,
		StabilityLevel:      tier.StabilityLevel,
		LifespanYears:       tier.LifespanYearsMin,
		CorruptionRisk:      tier.CorruptionRisk,
		CorruptionRiskPercent: tier.CorruptionRiskPercent,
		ProtectionLevel:     tier.ProtectionLevel,
		CreationCostMin:     tier.CreationCostMin,
		CreationCostMax:     tier.CreationCostMax,
		AvailableFromYear:   tier.AvailableFromYear,
		IsAvailable:         true,
	}

	if tier.LifespanYearsMin != tier.LifespanYearsMax {
		info.LifespanRange = &LifespanRange{
			Min: tier.LifespanYearsMin,
			Max: tier.LifespanYearsMax,
		}
	}

	return info, nil
}

func (s *EngramChipsService) GetChipDecay(ctx context.Context, chipID uuid.UUID) (*EngramChipDecayInfo, error) {
	err := s.UpdateDecayCalculation(ctx, chipID)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to update decay calculation")
	}

	decay, err := s.repo.GetChipDecay(ctx, chipID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get chip decay")
		return nil, err
	}

	if decay == nil {
		tier, err := s.repo.GetChipTierByChipID(ctx, chipID)
		if err != nil || tier == nil {
			return nil, ErrChipNotFound
		}

		decay, err = s.repo.CreateChipDecay(ctx, chipID, tier.Tier)
		if err != nil {
			return nil, err
		}
	}

	info := &EngramChipDecayInfo{
		ChipID:       decay.ChipID,
		DecayPercent: decay.DecayPercent,
		DecayRisk:    decay.DecayRisk,
		StorageConditions: &StorageConditions{
			Temperature:           decay.StorageTemperature,
			Humidity:              decay.StorageHumidity,
			ElectromagneticShield: decay.ElectromagneticShield,
			StorageTimeOutside:    decay.StorageTimeOutsideHours,
		},
		TimeUntilCritical: decay.TimeUntilCriticalHours,
		DecayEffects:      decay.DecayEffects,
	}

	return info, nil
}

func (s *EngramChipsService) UpdateDecayCalculation(ctx context.Context, chipID uuid.UUID) error {
	decay, err := s.repo.GetChipDecay(ctx, chipID)
	if err != nil {
		return err
	}

	if decay == nil {
		return nil
	}

	tier, err := s.repo.GetChipTierByTier(ctx, decay.Tier)
	if err != nil || tier == nil {
		return ErrChipTierNotFound
	}

	timeSinceLastCheck := time.Since(decay.LastCheckedAt).Hours()
	decay.StorageTimeOutsideHours += int(timeSinceLastCheck)

	decayPercent := s.calculateDecayPercent(decay, tier)
	decay.DecayPercent = decayPercent
	decay.DecayRisk = s.calculateDecayRisk(decayPercent)
	decay.DecayEffects = s.calculateDecayEffects(decayPercent)
	decay.TimeUntilCriticalHours = s.calculateTimeUntilCritical(decay, tier)

	err = s.repo.UpdateChipDecay(ctx, decay)
	if err != nil {
		return err
	}

	return nil
}

func (s *EngramChipsService) calculateDecayPercent(decay *EngramChipDecay, tier *EngramChipTier) float64 {
	baseDecayRate := tier.CorruptionRiskPercent / 100.0

	temperatureMultiplier := 1.0
	if decay.StorageTemperature == "poor" {
		temperatureMultiplier = 1.5
	} else if decay.StorageTemperature == "critical" {
		temperatureMultiplier = 2.0
	}

	humidityMultiplier := 1.0
	if decay.StorageHumidity == "poor" {
		humidityMultiplier = 1.3
	} else if decay.StorageHumidity == "critical" {
		humidityMultiplier = 1.8
	}

	shieldMultiplier := 1.0
	if !decay.ElectromagneticShield {
		shieldMultiplier = 1.2
	}

	hoursMultiplier := 1.0 + (float64(decay.StorageTimeOutsideHours) / 8760.0) * 0.1

	newDecay := decay.DecayPercent + (baseDecayRate * temperatureMultiplier * humidityMultiplier * shieldMultiplier * hoursMultiplier * 0.01)

	if newDecay > 100.0 {
		return 100.0
	}

	return newDecay
}

func (s *EngramChipsService) calculateDecayRisk(decayPercent float64) string {
	if decayPercent < 10 {
		return "none"
	} else if decayPercent < 25 {
		return "low"
	} else if decayPercent < 50 {
		return "medium"
	} else if decayPercent < 75 {
		return "high"
	} else {
		return "critical"
	}
}

func (s *EngramChipsService) calculateDecayEffects(decayPercent float64) []string {
	effects := []string{}

	if decayPercent >= 20 {
		effects = append(effects, "data_loss")
	}
	if decayPercent >= 40 {
		effects = append(effects, "instability")
	}
	if decayPercent >= 60 {
		effects = append(effects, "aggressive_behavior")
	}
	if decayPercent >= 80 {
		effects = append(effects, "complete_loss")
	}

	return effects
}

func (s *EngramChipsService) calculateTimeUntilCritical(decay *EngramChipDecay, tier *EngramChipTier) *int {
	if decay.DecayPercent >= 100 {
		return nil
	}

	if decay.DecayRisk == "critical" {
		hours := int((100.0 - decay.DecayPercent) / (tier.CorruptionRiskPercent / 100.0 / 365.0 / 24.0))
		return &hours
	}

	return nil
}

