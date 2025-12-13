// Issue: #104
package server

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// MechanicsService объединяет все игровые механики
type MechanicsService interface {
	// Combat mechanics
	GetCombatStats(ctx context.Context, playerID uuid.UUID) (*api.CombatStats, error)
	ExecuteCombatAction(ctx context.Context, playerID uuid.UUID, action *api.CombatAction) (*api.CombatResult, error)

	// Progression mechanics
	GetProgressionStats(ctx context.Context, playerID uuid.UUID) (*api.ProgressionStats, error)
	ApplyExperience(ctx context.Context, playerID uuid.UUID, exp int) (*api.ExperienceResult, error)

	// Economy mechanics
	GetPlayerEconomy(ctx context.Context, playerID uuid.UUID) (*api.PlayerEconomy, error)
	ExecuteTrade(ctx context.Context, trade *api.TradeRequest) (*api.TradeResult, error)

	// Social mechanics
	GetSocialRelations(ctx context.Context, playerID uuid.UUID) (*api.SocialRelations, error)
	UpdateNPCRelation(ctx context.Context, playerID uuid.UUID, npcID uuid.UUID, change int) error

	// World mechanics
	GetWorldState(ctx context.Context, playerID uuid.UUID) (*api.WorldState, error)
	TriggerWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEventResult, error)
}

type mechanicsService struct {
	logger *logrus.Logger

	// Sub-services
	combatService     CombatService
	progressionService ProgressionService
	economyService    EconomyService
	socialService     SocialService
	worldService      WorldService

	// Memory pooling for hot paths
	combatStatsPool      sync.Pool
	progressionStatsPool sync.Pool
	economyPool         sync.Pool
	socialPool          sync.Pool
	worldPool           sync.Pool
}

func NewMechanicsService(
	combatSvc CombatService,
	progressionSvc ProgressionService,
	economySvc EconomyService,
	socialSvc SocialService,
	worldSvc WorldService,
	logger *logrus.Logger,
) MechanicsService {
	ms := &mechanicsService{
		logger:             logger,
		combatService:      combatSvc,
		progressionService: progressionSvc,
		economyService:     economySvc,
		socialService:      socialSvc,
		worldService:       worldSvc,
	}

	// Initialize memory pools
	ms.combatStatsPool = sync.Pool{
		New: func() interface{} {
			return &api.CombatStats{}
		},
	}
	ms.progressionStatsPool = sync.Pool{
		New: func() interface{} {
			return &api.ProgressionStats{}
		},
	}
	ms.economyPool = sync.Pool{
		New: func() interface{} {
			return &api.PlayerEconomy{}
		},
	}
	ms.socialPool = sync.Pool{
		New: func() interface{} {
			return &api.SocialRelations{}
		},
	}
	ms.worldPool = sync.Pool{
		New: func() interface{} {
			return &api.WorldState{}
		},
	}

	return ms
}

// Combat mechanics implementation
func (ms *mechanicsService) GetCombatStats(ctx context.Context, playerID uuid.UUID) (*api.CombatStats, error) {
	ms.logger.WithField("player_id", playerID).Debug("Getting combat stats")

	stats, err := ms.combatService.GetPlayerCombatStats(ctx, playerID)
	if err != nil {
		return nil, err
	}

	// Use memory pooling
	result := ms.combatStatsPool.Get().(*api.CombatStats)
	defer ms.combatStatsPool.Put(result)

	result.Health = api.NewOptFloat32(float32(stats.Health))
	result.MaxHealth = api.NewOptFloat32(float32(stats.MaxHealth))
	result.Stamina = api.NewOptFloat32(float32(stats.Stamina))
	result.Level = api.NewOptInt(stats.Level)

	return result, nil
}

func (ms *mechanicsService) ExecuteCombatAction(ctx context.Context, playerID uuid.UUID, action *api.CombatAction) (*api.CombatResult, error) {
	ms.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"action":    action.ActionType,
	}).Debug("Executing combat action")

	result, err := ms.combatService.ExecuteAction(ctx, playerID, action)
	if err != nil {
		return nil, err
	}

	return &api.CombatResult{
		Success:       result.Success,
		DamageDealt:   api.NewOptFloat32(float32(result.DamageDealt)),
		ExperienceGained: api.NewOptInt(result.ExperienceGained),
		StatusEffects: result.StatusEffects,
	}, nil
}

// Progression mechanics implementation
func (ms *mechanicsService) GetProgressionStats(ctx context.Context, playerID uuid.UUID) (*api.ProgressionStats, error) {
	ms.logger.WithField("player_id", playerID).Debug("Getting progression stats")

	stats, err := ms.progressionService.GetPlayerProgression(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := ms.progressionStatsPool.Get().(*api.ProgressionStats)
	defer ms.progressionStatsPool.Put(result)

	result.Level = api.NewOptInt(stats.Level)
	result.Experience = api.NewOptInt(stats.Experience)
	result.ExperienceToNext = api.NewOptInt(stats.ExperienceToNext)
	result.Attributes = stats.Attributes
	result.Skills = stats.Skills

	return result, nil
}

func (ms *mechanicsService) ApplyExperience(ctx context.Context, playerID uuid.UUID, exp int) (*api.ExperienceResult, error) {
	ms.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"exp":       exp,
	}).Debug("Applying experience")

	result, err := ms.progressionService.AddExperience(ctx, playerID, exp)
	if err != nil {
		return nil, err
	}

	return &api.ExperienceResult{
		NewLevel:     api.NewOptInt(result.NewLevel),
		LeveledUp:    api.NewOptBool(result.LeveledUp),
		ExperienceGained: api.NewOptInt(result.ExperienceGained),
	}, nil
}

// Economy mechanics implementation
func (ms *mechanicsService) GetPlayerEconomy(ctx context.Context, playerID uuid.UUID) (*api.PlayerEconomy, error) {
	ms.logger.WithField("player_id", playerID).Debug("Getting player economy")

	economy, err := ms.economyService.GetPlayerEconomy(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := ms.economyPool.Get().(*api.PlayerEconomy)
	defer ms.economyPool.Put(result)

	result.Balance = economy.Balance
	result.Inventory = economy.Inventory
	result.TradingStatus = economy.TradingStatus

	return result, nil
}

func (ms *mechanicsService) ExecuteTrade(ctx context.Context, trade *api.TradeRequest) (*api.TradeResult, error) {
	ms.logger.WithFields(logrus.Fields{
		"buyer_id":  trade.BuyerID,
		"seller_id": trade.SellerID,
		"amount":    trade.Amount,
	}).Debug("Executing trade")

	result, err := ms.economyService.ExecuteTrade(ctx, trade)
	if err != nil {
		return nil, err
	}

	return &api.TradeResult{
		Success:      result.Success,
		TransactionID: api.NewOptString(result.TransactionID.String()),
		NewBalance:   result.NewBalance,
	}, nil
}

// Social mechanics implementation
func (ms *mechanicsService) GetSocialRelations(ctx context.Context, playerID uuid.UUID) (*api.SocialRelations, error) {
	ms.logger.WithField("player_id", playerID).Debug("Getting social relations")

	relations, err := ms.socialService.GetPlayerRelations(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := ms.socialPool.Get().(*api.SocialRelations)
	defer ms.socialPool.Put(result)

	result.NpcRelations = relations.NpcRelations
	result.PlayerRelations = relations.PlayerRelations
	result.Reputation = relations.Reputation

	return result, nil
}

func (ms *mechanicsService) UpdateNPCRelation(ctx context.Context, playerID uuid.UUID, npcID uuid.UUID, change int) error {
	ms.logger.WithFields(logrus.Fields{
		"player_id": playerID,
		"npc_id":    npcID,
		"change":    change,
	}).Debug("Updating NPC relation")

	return ms.socialService.UpdateNPCRelation(ctx, playerID, npcID, change)
}

// World mechanics implementation
func (ms *mechanicsService) GetWorldState(ctx context.Context, playerID uuid.UUID) (*api.WorldState, error) {
	ms.logger.WithField("player_id", playerID).Debug("Getting world state")

	state, err := ms.worldService.GetPlayerWorldState(ctx, playerID)
	if err != nil {
		return nil, err
	}

	result := ms.worldPool.Get().(*api.WorldState)
	defer ms.worldPool.Put(result)

	result.ActiveEvents = state.ActiveEvents
	result.PlayerPosition = state.PlayerPosition
	result.WorldTime = api.NewOptDateTime(state.WorldTime)

	return result, nil
}

func (ms *mechanicsService) TriggerWorldEvent(ctx context.Context, eventID uuid.UUID) (*api.WorldEventResult, error) {
	ms.logger.WithField("event_id", eventID).Debug("Triggering world event")

	result, err := ms.worldService.TriggerEvent(ctx, eventID)
	if err != nil {
		return nil, err
	}

	return &api.WorldEventResult{
		EventID:     result.EventID,
		TriggeredAt: result.TriggeredAt,
		Participants: result.Participants,
		Rewards:     result.Rewards,
	}, nil
}

// Sub-service interfaces (to be implemented)
type CombatService interface {
	GetPlayerCombatStats(ctx context.Context, playerID uuid.UUID) (*CombatStats, error)
	ExecuteAction(ctx context.Context, playerID uuid.UUID, action *api.CombatAction) (*CombatResult, error)
}

type ProgressionService interface {
	GetPlayerProgression(ctx context.Context, playerID uuid.UUID) (*ProgressionStats, error)
	AddExperience(ctx context.Context, playerID uuid.UUID, exp int) (*ExperienceResult, error)
}

type EconomyService interface {
	GetPlayerEconomy(ctx context.Context, playerID uuid.UUID) (*PlayerEconomy, error)
	ExecuteTrade(ctx context.Context, trade *api.TradeRequest) (*TradeResult, error)
}

type SocialService interface {
	GetPlayerRelations(ctx context.Context, playerID uuid.UUID) (*SocialRelations, error)
	UpdateNPCRelation(ctx context.Context, playerID uuid.UUID, npcID uuid.UUID, change int) error
}

type WorldService interface {
	GetPlayerWorldState(ctx context.Context, playerID uuid.UUID) (*WorldState, error)
	TriggerEvent(ctx context.Context, eventID uuid.UUID) (*WorldEventResult, error)
}

// Internal data structures (simplified)
type CombatStats struct {
	Health    float64
	MaxHealth float64
	Stamina   float64
	Level     int
}

type CombatResult struct {
	Success         bool
	DamageDealt     float64
	ExperienceGained int
	StatusEffects   []string
}

type ProgressionStats struct {
	Level            int
	Experience       int
	ExperienceToNext int
	Attributes       map[string]int
	Skills           map[string]int
}

type ExperienceResult struct {
	NewLevel         int
	LeveledUp        bool
	ExperienceGained int
}

type PlayerEconomy struct {
	Balance       map[string]float64
	Inventory     []string
	TradingStatus string
}

type TradeResult struct {
	Success       bool
	TransactionID uuid.UUID
	NewBalance    map[string]float64
}

type SocialRelations struct {
	NpcRelations    map[string]int
	PlayerRelations map[string]int
	Reputation      map[string]int
}

type WorldState struct {
	ActiveEvents   []string
	PlayerPosition [3]float64
	WorldTime      time.Time
}

type WorldEventResult struct {
	EventID      uuid.UUID
	TriggeredAt  time.Time
	Participants []uuid.UUID
	Rewards      map[string]int
}