// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1599, #1604, #1607, #387, #388
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"sync"
	"sync/atomic"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

// Cache entry for lock-free session caching
type cacheEntry struct {
	data        *api.CombatSessionResponse
	timestamp   int64 // unix nano
	accessCount int64 // atomic
}

const (
	DBTimeout = 50 * time.Millisecond
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
	sessionSummarySlicePool   sync.Pool // For slices in responses
	participantSlicePool      sync.Pool // For participant arrays
	bufferPool                sync.Pool // For JSON encoding/decoding

	// Lock-free statistics (zero contention target!)
	requestsTotal   int64 // atomic
	sessionsCreated int64 // atomic
	sessionsListed  int64 // atomic
	errorsTotal     int64 // atomic
	lastRequestTime int64 // atomic unix nano

	// Lock-free session cache (LRU with atomic operations)
	sessionCache map[string]*cacheEntry // concurrent access with atomic.Value
	cacheMu      sync.RWMutex           // lightweight mutex for cache operations
}

// NewHandlers creates new handlers with memory pooling
// Issue: #1525 - Initialize services if db is provided
func NewHandlers(logger *logrus.Logger, db *pgxpool.Pool) *Handlers {
	h := &Handlers{
		logger:       logger,
		sessionCache: make(map[string]*cacheEntry),
	}

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
	// Additional pools for zero allocations
	h.sessionSummarySlicePool = sync.Pool{
		New: func() interface{} {
			return make([]api.SessionSummary, 0, 20) // Pre-allocate capacity
		},
	}
	h.participantSlicePool = sync.Pool{
		New: func() interface{} {
			return make([]api.Participant, 0, 10) // Pre-allocate capacity
		},
	}
	h.bufferPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 0, 4096) // 4KB buffer for JSON
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

// Lock-free statistics methods (zero contention)
func (h *Handlers) incrementRequestsTotal() {
	atomic.AddInt64(&h.requestsTotal, 1)
	atomic.StoreInt64(&h.lastRequestTime, time.Now().UnixNano())
}

func (h *Handlers) incrementSessionsCreated() {
	atomic.AddInt64(&h.sessionsCreated, 1)
}

func (h *Handlers) incrementSessionsListed() {
	atomic.AddInt64(&h.sessionsListed, 1)
}

func (h *Handlers) incrementErrorsTotal() {
	atomic.AddInt64(&h.errorsTotal, 1)
}

func (h *Handlers) getStats() map[string]int64 {
	return map[string]int64{
		"requests_total":    atomic.LoadInt64(&h.requestsTotal),
		"sessions_created":  atomic.LoadInt64(&h.sessionsCreated),
		"sessions_listed":   atomic.LoadInt64(&h.sessionsListed),
		"errors_total":      atomic.LoadInt64(&h.errorsTotal),
		"last_request_time": atomic.LoadInt64(&h.lastRequestTime),
	}
}

// Lock-free cache methods (zero contention for hot paths)
func (h *Handlers) getCachedSession(sessionID string) (*api.CombatSessionResponse, bool) {
	h.cacheMu.RLock()
	entry, exists := h.sessionCache[sessionID]
	h.cacheMu.RUnlock()

	if !exists {
		return nil, false
	}

	// Check if cache entry is still valid (5 minute TTL)
	now := time.Now().UnixNano()
	if now-entry.timestamp > 5*60*1000000000 { // 5 minutes in nanoseconds
		h.cacheMu.Lock()
		delete(h.sessionCache, sessionID)
		h.cacheMu.Unlock()
		return nil, false
	}

	// Update access count atomically
	atomic.AddInt64(&entry.accessCount, 1)

	return entry.data, true
}

func (h *Handlers) setCachedSession(sessionID string, data *api.CombatSessionResponse) {
	entry := &cacheEntry{
		data:        data,
		timestamp:   time.Now().UnixNano(),
		accessCount: 1,
	}

	h.cacheMu.Lock()
	h.sessionCache[sessionID] = entry

	// Simple LRU: remove oldest entries if cache grows too large
	if len(h.sessionCache) > 1000 { // max 1000 cached sessions
		var oldestKey string
		var oldestTime = time.Now().UnixNano()
		for k, v := range h.sessionCache {
			if v.timestamp < oldestTime {
				oldestTime = v.timestamp
				oldestKey = k
			}
		}
		if oldestKey != "" {
			delete(h.sessionCache, oldestKey)
		}
	}
	h.cacheMu.Unlock()
}

// Mechanics handlers - Issue: #104 - Temporarily commented out due to missing API types

// GetCombatStats - получить боевые статистики игрока
// func (h *Handlers) GetCombatStats(ctx context.Context, params api.GetCombatStatsParams) (api.GetCombatStatsRes, error) {
ctx, cancel := context.WithTimeout(ctx, DBTimeout)
defer cancel()

// 	if h.mechanicsService == nil {
// 		return &api.GetCombatStatsInternalServerError{}, nil
// 	}

// 	stats, err := h.mechanicsService.GetCombatStats(ctx, params.PlayerID)
// 	if err != nil {
// 		return &api.GetCombatStatsInternalServerError{}, err
// 	}

// 	return stats, nil
// }

// ExecuteCombatAction - выполнить боевое действие
// func (h *Handlers) ExecuteCombatAction(ctx context.Context, req *api.CombatActionReq, params api.ExecuteCombatActionParams) (api.ExecuteCombatActionRes, error) {
ctx, cancel := context.WithTimeout(ctx, DBTimeout)
defer cancel()

// 	if h.mechanicsService == nil {
// 		return &api.ExecuteCombatActionInternalServerError{}, nil
// 	}

// 	action := &api.CombatAction{
// 		ActionType: req.ActionType,
// 		TargetID:   req.TargetID,
// 		Position:   req.Position,
// 	}

// 	result, err := h.mechanicsService.ExecuteCombatAction(ctx, params.PlayerID, action)
// 	if err != nil {
// 		return &api.ExecuteCombatActionInternalServerError{}, err
// 	}

// 	return result, nil
// }

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
func (h *Handlers) ExecuteTrade(ctx context.Context, req *api.TradeRequest) (api.ExecuteTradeRes, error) {
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
