package server

import (
	"context"
	"math/rand"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
	"github.com/sirupsen/logrus"
)

type TravelEventService struct {
	repo     WorldRepository
	logger   *logrus.Logger
	eventBus EventBus
	rng      *rand.Rand
}

func NewTravelEventService(repo WorldRepository, logger *logrus.Logger, eventBus EventBus) *TravelEventService {
	return &TravelEventService{
		repo:     repo,
		logger:    logger,
		eventBus:  eventBus,
		rng:       rand.New(rand.NewSource(time.Now().UnixNano())),
	}
}

func (s *TravelEventService) CalculateProbability(ctx context.Context, event *models.TravelEvent, characterID, zoneID uuid.UUID) float64 {
	baseChance := event.BaseProbability
	
	levelModifier := 1.0
	reputationModifier := 1.0
	timeModifier := 1.0
	zoneModifier := 1.0
	
	probability := baseChance * levelModifier * reputationModifier * timeModifier * zoneModifier
	
	if probability > 1.0 {
		probability = 1.0
	}
	if probability < 0.0 {
		probability = 0.0
	}
	
	return probability
}

func (s *TravelEventService) CheckCooldown(ctx context.Context, characterID uuid.UUID, eventType string, cooldownHours int) (bool, *time.Time, error) {
	cooldowns, err := s.repo.GetCharacterTravelEventCooldowns(ctx, characterID)
	if err != nil {
		return false, nil, err
	}
	
	for _, cd := range cooldowns {
		if cd.EventType == eventType {
			if time.Now().Before(cd.CooldownUntil) {
				return false, &cd.CooldownUntil, nil
			}
		}
	}
	
	return true, nil, nil
}

func (s *TravelEventService) PerformSkillCheck(ctx context.Context, skill string, dc int, characterID uuid.UUID) (*models.SkillCheckResponse, error) {
	roll := s.rng.Intn(20) + 1
	
	skillLevel := 0
	modifiers := map[string]interface{}{
		"skill_level": skillLevel,
		"base_roll":   roll,
	}
	
	totalRoll := roll + skillLevel
	
	success := totalRoll >= dc
	criticalSuccess := totalRoll >= dc+10
	criticalFailure := totalRoll <= dc-10
	
	response := &models.SkillCheckResponse{
		Success:         success,
		CriticalSuccess: criticalSuccess,
		CriticalFailure: criticalFailure,
		RollResult:      totalRoll,
		DC:              dc,
		Modifiers:       modifiers,
	}
	
	return response, nil
}

func (s *TravelEventService) DistributeRewards(ctx context.Context, event *models.TravelEvent, instance *models.TravelEventInstance, skillCheckResult *models.SkillCheckResponse) ([]models.TravelEventReward, error) {
	var rewards []models.TravelEventReward
	
	if event.Rewards == nil {
		return rewards, nil
	}
	
	multiplier := 1.0
	if skillCheckResult != nil && skillCheckResult.CriticalSuccess {
		multiplier = 2.0
	}
	
	if lootRewards, ok := event.Rewards["loot"].(map[string]interface{}); ok {
		if probability, ok := lootRewards["probability"].(float64); ok {
			if s.rng.Float64() < probability {
				rewards = append(rewards, models.TravelEventReward{
					Type: "loot",
					Data: map[string]interface{}{
						"rarity":     lootRewards["rarity"],
						"multiplier": multiplier,
					},
				})
			}
		}
	}
	
	if eddiesRewards, ok := event.Rewards["eddies"].(map[string]interface{}); ok {
		minEddies := 0.0
		maxEddies := 0.0
		if min, ok := eddiesRewards["min"].(float64); ok {
			minEddies = min
		}
		if max, ok := eddiesRewards["max"].(float64); ok {
			maxEddies = max
		}
		
		if maxEddies > 0 {
			amount := int((minEddies + s.rng.Float64()*(maxEddies-minEddies)) * multiplier)
			rewards = append(rewards, models.TravelEventReward{
				Type: "eddies",
				Data: map[string]interface{}{
					"amount": amount,
				},
			})
		}
	}
	
	if reputationRewards, ok := event.Rewards["reputation"].(map[string]interface{}); ok {
		faction := ""
		amount := 0.0
		if f, ok := reputationRewards["faction"].(string); ok {
			faction = f
		}
		if a, ok := reputationRewards["amount"].(float64); ok {
			amount = a
		}
		
		if faction != "" && amount > 0 {
			rewards = append(rewards, models.TravelEventReward{
				Type: "reputation",
				Data: map[string]interface{}{
					"faction": faction,
					"amount":  int(amount * multiplier),
				},
			})
		}
	}
	
	return rewards, nil
}

func (s *TravelEventService) ApplyPenalties(ctx context.Context, event *models.TravelEvent, instance *models.TravelEventInstance, skillCheckResult *models.SkillCheckResponse) ([]models.TravelEventPenalty, error) {
	var penalties []models.TravelEventPenalty
	
	if event.Penalties == nil {
		return penalties, nil
	}
	
	multiplier := 1.0
	if skillCheckResult != nil && skillCheckResult.CriticalFailure {
		multiplier = 2.0
	}
	
	if damagePenalties, ok := event.Penalties["damage"].(map[string]interface{}); ok {
		severity := ""
		if s, ok := damagePenalties["severity"].(string); ok {
			severity = s
		}
		
		healthLoss := 0
		switch severity {
		case "light":
			healthLoss = int(10 * multiplier)
		case "medium":
			healthLoss = int(25 * multiplier)
		case "heavy":
			healthLoss = int(50 * multiplier)
		}
		
		if healthLoss > 0 {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "damage",
				Data: map[string]interface{}{
					"severity":   severity,
					"health_loss": healthLoss,
				},
			})
		}
	}
	
	if heatPenalties, ok := event.Penalties["heat"].(map[string]interface{}); ok {
		amount := 0.0
		if a, ok := heatPenalties["amount"].(float64); ok {
			amount = a
		}
		
		if amount > 0 {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "heat",
				Data: map[string]interface{}{
					"amount": int(amount * multiplier),
				},
			})
		}
	}
	
	if reputationPenalties, ok := event.Penalties["reputation"].(map[string]interface{}); ok {
		faction := ""
		amount := 0.0
		if f, ok := reputationPenalties["faction"].(string); ok {
			faction = f
		}
		if a, ok := reputationPenalties["amount"].(float64); ok {
			amount = a
		}
		
		if faction != "" && amount < 0 {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "reputation",
				Data: map[string]interface{}{
					"faction": faction,
					"amount":  int(amount * multiplier),
				},
			})
		}
	}
	
	if confiscationPenalties, ok := event.Penalties["confiscation"].(map[string]interface{}); ok {
		probability := 0.0
		if p, ok := confiscationPenalties["probability"].(float64); ok {
			probability = p
		}
		
		if s.rng.Float64() < probability {
			penalties = append(penalties, models.TravelEventPenalty{
				Type: "confiscation",
				Data: map[string]interface{}{
					"probability": probability,
				},
			})
		}
	}
	
	return penalties, nil
}

func (s *TravelEventService) UpdateCooldown(ctx context.Context, characterID uuid.UUID, eventType string, cooldownHours int) error {
	cooldownUntil := time.Now().Add(time.Duration(cooldownHours) * time.Hour)
	
	if s.eventBus != nil {
		payload := map[string]interface{}{
			"character_id":    characterID.String(),
			"event_type":      eventType,
			"cooldown_until":  cooldownUntil,
		}
		s.eventBus.PublishEvent(ctx, "world:travel-event:cooldown-updated", payload)
	}
	
	return nil
}

