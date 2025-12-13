// Issue: #1599, #1604, #1607, #387, #388
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	logger               *logrus.Logger
	mechanicsService     MechanicsService
	comboService         ComboServiceInterface
	combatSessionService CombatSessionServiceInterface
	affixService         AffixServiceInterface
	abilityService       AbilityServiceInterface
	questRepository      QuestRepositoryInterface

	// Memory pooling for hot path structs (zero allocations target!)
	sessionListResponsePool   sync.Pool
	combatSessionResponsePool sync.Pool
	sessionEndResponsePool    sync.Pool
	getStealthStatusOKPool    sync.Pool
	internalServerErrorPool   sync.Pool
	badRequestPool            sync.Pool
}

// NewHandlers creates new handlers with memory pooling
// Issue: #1525 - Initialize services if db is provided
func NewHandlers(logger *logrus.Logger, db *pgxpool.Pool) *Handlers {
	h := &Handlers{logger: logger}

	// Initialize memory pools (zero allocations target!)
	h.sessionListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SessionListResponse{}
		},
	}
	h.combatSessionResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.CombatSessionResponse{}
		},
	}
	h.sessionEndResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SessionEndResponse{}
		},
	}
	h.getStealthStatusOKPool = sync.Pool{
		New: func() interface{} {
			return &api.GetStealthStatusOK{}
		},
	}
	h.internalServerErrorPool = sync.Pool{
		New: func() interface{} {
			return &api.ActivateAbilityInternalServerError{}
		},
	}
	h.badRequestPool = sync.Pool{
		New: func() interface{} {
			return &api.CreateCombatSessionBadRequest{}
		},
	}

	if db != nil {
		h.comboService = NewComboService(db)
		h.combatSessionService = NewCombatSessionService(db)
		h.affixService = NewAffixService(db)
		h.abilityService = NewAbilityService(db)
		h.questRepository = NewQuestRepository(db)

		// Initialize mechanics sub-services
		combatSvc := NewCombatServiceImpl(logger)
		progressionSvc := NewProgressionServiceImpl(logger)
		economySvc := NewEconomyServiceImpl(logger)
		socialSvc := NewSocialServiceImpl(logger)
		worldSvc := NewWorldServiceImpl(logger)

		// Initialize main mechanics service
		h.mechanicsService = NewMechanicsService(
			combatSvc,
			progressionSvc,
			economySvc,
			socialSvc,
			worldSvc,
			logger,
		)
	}

	return h
}

// Mechanics handlers - Issue: #104

// GetCombatStats - получить боевые статистики игрока
func (h *Handlers) GetCombatStats(ctx context.Context, params api.GetCombatStatsParams) (api.GetCombatStatsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.GetCombatStatsInternalServerError{}, nil
	}

	stats, err := h.mechanicsService.GetCombatStats(ctx, params.PlayerID)
	if err != nil {
		return &api.GetCombatStatsInternalServerError{}, err
	}

	return stats, nil
}

// ExecuteCombatAction - выполнить боевое действие
func (h *Handlers) ExecuteCombatAction(ctx context.Context, req *api.CombatActionReq, params api.ExecuteCombatActionParams) (api.ExecuteCombatActionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.ExecuteCombatActionInternalServerError{}, nil
	}

	action := &api.CombatAction{
		ActionType: req.ActionType,
		TargetID:   req.TargetID,
		Position:   req.Position,
	}

	result, err := h.mechanicsService.ExecuteCombatAction(ctx, params.PlayerID, action)
	if err != nil {
		return &api.ExecuteCombatActionInternalServerError{}, err
	}

	return result, nil
}

// GetProgressionStats - получить статистику прогрессии
func (h *Handlers) GetProgressionStats(ctx context.Context, params api.GetProgressionStatsParams) (api.GetProgressionStatsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.GetProgressionStatsInternalServerError{}, nil
	}

	stats, err := h.mechanicsService.GetProgressionStats(ctx, params.PlayerID)
	if err != nil {
		return &api.GetProgressionStatsInternalServerError{}, err
	}

	return stats, nil
}

// ApplyExperience - применить опыт
func (h *Handlers) ApplyExperience(ctx context.Context, req *api.ApplyExperienceReq, params api.ApplyExperienceParams) (api.ApplyExperienceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.ApplyExperienceInternalServerError{}, nil
	}

	result, err := h.mechanicsService.ApplyExperience(ctx, params.PlayerID, int(req.Experience))
	if err != nil {
		return &api.ApplyExperienceInternalServerError{}, err
	}

	return result, nil
}

// GetPlayerEconomy - получить экономику игрока
func (h *Handlers) GetPlayerEconomy(ctx context.Context, params api.GetPlayerEconomyParams) (api.GetPlayerEconomyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.GetPlayerEconomyInternalServerError{}, nil
	}

	economy, err := h.mechanicsService.GetPlayerEconomy(ctx, params.PlayerID)
	if err != nil {
		return &api.GetPlayerEconomyInternalServerError{}, err
	}

	return economy, nil
}

// ExecuteTrade - выполнить торговую операцию
func (h *Handlers) ExecuteTrade(ctx context.Context, req *api.TradeRequest, params api.ExecuteTradeParams) (api.ExecuteTradeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.ExecuteTradeInternalServerError{}, nil
	}

	result, err := h.mechanicsService.ExecuteTrade(ctx, req)
	if err != nil {
		return &api.ExecuteTradeInternalServerError{}, err
	}

	return result, nil
}

// GetSocialRelations - получить социальные связи
func (h *Handlers) GetSocialRelations(ctx context.Context, params api.GetSocialRelationsParams) (api.GetSocialRelationsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.GetSocialRelationsInternalServerError{}, nil
	}

	relations, err := h.mechanicsService.GetSocialRelations(ctx, params.PlayerID)
	if err != nil {
		return &api.GetSocialRelationsInternalServerError{}, err
	}

	return relations, nil
}

// UpdateNPCRelation - обновить отношение с NPC
func (h *Handlers) UpdateNPCRelation(ctx context.Context, req *api.UpdateNPCRelationReq, params api.UpdateNPCRelationParams) (api.UpdateNPCRelationRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.UpdateNPCRelationInternalServerError{}, nil
	}

	err := h.mechanicsService.UpdateNPCRelation(ctx, params.PlayerID, req.NpcID, int(req.RelationChange))
	if err != nil {
		return &api.UpdateNPCRelationInternalServerError{}, err
	}

	return &api.StatusResponse{Status: api.NewOptString("updated")}, nil
}

// GetWorldState - получить состояние мира
func (h *Handlers) GetWorldState(ctx context.Context, params api.GetWorldStateParams) (api.GetWorldStateRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.GetWorldStateInternalServerError{}, nil
	}

	state, err := h.mechanicsService.GetWorldState(ctx, params.PlayerID)
	if err != nil {
		return &api.GetWorldStateInternalServerError{}, err
	}

	return state, nil
}

// TriggerWorldEvent - запустить мировое событие
func (h *Handlers) TriggerWorldEvent(ctx context.Context, params api.TriggerWorldEventParams) (api.TriggerWorldEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.mechanicsService == nil {
		return &api.TriggerWorldEventInternalServerError{}, nil
	}

	result, err := h.mechanicsService.TriggerWorldEvent(ctx, params.EventID)
	if err != nil {
		return &api.TriggerWorldEventInternalServerError{}, err
	}

	return result, nil
}
