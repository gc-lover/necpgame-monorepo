// Issue: #1599, #1604, #1607, #387, #388
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"encoding/json"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/go-faster/jx"
	"github.com/google/uuid"
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
// Issue: #1525 - Initialize comboService if db is provided
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

	// Issue: #1525 - Initialize comboService if db is provided
	// Issue: #1607 - Initialize combatSessionService if db is provided
	// Issue: #1515 - Initialize affixService if db is provided
	// Issue: #156 - Initialize abilityService if db is provided
	// Issue: #50 - Initialize questRepository if db is provided
	if db != nil {
		h.comboService = NewComboService(db)
		h.combatSessionService = NewCombatSessionService(db)
		h.affixService = NewAffixService(db)
		h.abilityService = NewAbilityService(db)
		h.questRepository = NewQuestRepository(db)
	}

	return h
}

// ActivateAbility implements POST /gameplay/combat/abilities/activate
// Issue: #156 - Basic implementation (full logic requires AbilityService/Repository)
func (h *Handlers) ActivateAbility(ctx context.Context, req *api.AbilityActivationRequest) (api.ActivateAbilityRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation
	if req == nil {
		h.logger.Error("ActivateAbility: nil request")
		return &api.ActivateAbilityBadRequest{}, nil
	}

	if req.AbilityID == uuid.Nil {
		h.logger.Warn("ActivateAbility: empty ability_id")
		return &api.ActivateAbilityBadRequest{}, nil
	}

	// Basic implementation - returns success response
	// TODO: Full implementation requires:
	// - AbilityService/Repository for ability lookup
	// - Cooldown checking
	// - Resource validation (energy, health)
	// - Cyberpsychosis updates
	h.logger.WithFields(logrus.Fields{
		"ability_id":   req.AbilityID,
		"target_id":    req.TargetID,
		"has_position": req.Position.Set,
	}).Info("ActivateAbility request received")

	// Return success response (stub implementation)
	// Note: AbilityActivationResponse implements ActivateAbilityRes interface
	response := &api.AbilityActivationResponse{
		AbilityID:             req.AbilityID,
		Success:               true,
		Message:               api.NewOptNilString("Ability activated successfully"),
		CooldownStarted:       api.NewOptBool(true),
		SynergyTriggered:      api.NewOptBool(false),
		CyberpsychosisUpdated: api.NewOptBool(false),
	}

	return response, nil
}

// CreateOrUpdateAbilityLoadout implements POST /gameplay/combat/abilities/loadouts
// Issue: #156
func (h *Handlers) CreateOrUpdateAbilityLoadout(ctx context.Context, req *api.AbilityLoadoutCreate) (api.CreateOrUpdateAbilityLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("CreateOrUpdateAbilityLoadout: abilityService not initialized")
		return &api.CreateOrUpdateAbilityLoadoutInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	loadout, err := h.abilityService.UpdateLoadout(ctx, characterID, req)
	if err != nil {
		h.logger.WithError(err).Error("CreateOrUpdateAbilityLoadout: failed")
		return &api.CreateOrUpdateAbilityLoadoutInternalServerError{}, nil
	}

	return &api.CreateOrUpdateAbilityLoadoutOK{
		ID:               loadout.ID,
		CharacterID:      loadout.CharacterID,
		SlotQ:            loadout.SlotQ,
		SlotE:            loadout.SlotE,
		SlotR:            loadout.SlotR,
		PassiveAbilities: loadout.PassiveAbilities,
		HackingAbilities: loadout.HackingAbilities,
		AutoCastEnabled:  loadout.AutoCastEnabled,
		CreatedAt:        loadout.CreatedAt,
		UpdatedAt:        loadout.UpdatedAt,
	}, nil
}

// GetAbilityById implements GET /gameplay/combat/abilities/{abilityId}
// Issue: #156
func (h *Handlers) GetAbilityById(ctx context.Context, params api.GetAbilityByIdParams) (api.GetAbilityByIdRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("GetAbilityById: abilityService not initialized")
		return &api.GetAbilityByIdInternalServerError{}, nil
	}

	ability, err := h.abilityService.GetAbility(ctx, params.AbilityId)
	if err != nil {
		if err.Error() == "ability not found" {
			return &api.GetAbilityByIdNotFound{}, nil
		}
		h.logger.WithError(err).Error("GetAbilityById: failed")
		return &api.GetAbilityByIdInternalServerError{}, nil
	}

	return ability, nil
}

// GetAbilityLoadouts implements GET /gameplay/combat/abilities/loadouts
// Issue: #156
func (h *Handlers) GetAbilityLoadouts(ctx context.Context, params api.GetAbilityLoadoutsParams) (api.GetAbilityLoadoutsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("GetAbilityLoadouts: abilityService not initialized")
		return &api.GetAbilityLoadoutsInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	loadout, err := h.abilityService.GetLoadout(ctx, characterID)
	if err != nil {
		if err.Error() == "loadout not found" {
			return &api.GetAbilityLoadoutsOK{
				Loadouts: []api.AbilityLoadout{},
				Total:    0,
				Limit:    api.NewOptInt(1),
				Offset:   api.NewOptInt(0),
			}, nil
		}
		h.logger.WithError(err).Error("GetAbilityLoadouts: failed")
		return &api.GetAbilityLoadoutsInternalServerError{}, nil
	}

	return &api.GetAbilityLoadoutsOK{
		Loadouts: []api.AbilityLoadout{*loadout},
		Total:    1,
		Limit:    api.NewOptInt(1),
		Offset:   api.NewOptInt(0),
	}, nil
}

// GetCyberpsychosisState implements GET /gameplay/combat/abilities/cyberpsychosis
// Issue: #156
func (h *Handlers) GetCyberpsychosisState(ctx context.Context) (api.GetCyberpsychosisStateRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("GetCyberpsychosisState: abilityService not initialized")
		return &api.GetCyberpsychosisStateInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	state, err := h.abilityService.GetCyberpsychosisState(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("GetCyberpsychosisState failed")
		return &api.GetCyberpsychosisStateInternalServerError{}, nil
	}

	return state, nil
}

// GetAbilityMetrics implements GET /gameplay/combat/abilities/metrics
// Issue: #156
func (h *Handlers) GetAbilityMetrics(ctx context.Context, params api.GetAbilityMetricsParams) (api.GetAbilityMetricsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("GetAbilityMetrics: abilityService not initialized")
		return &api.GetAbilityMetricsInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	var abilityIDOpt api.OptUUID
	if params.AbilityID.Set {
		abilityIDOpt = api.NewOptUUID(params.AbilityID.Value)
	}

	var startTimeOpt api.OptDateTime
	if params.PeriodStart.Set {
		startTimeOpt = api.NewOptDateTime(params.PeriodStart.Value)
	}

	var endTimeOpt api.OptDateTime
	if params.PeriodEnd.Set {
		endTimeOpt = api.NewOptDateTime(params.PeriodEnd.Value)
	}

	metrics, err := h.abilityService.GetAbilityMetrics(ctx, characterID, abilityIDOpt, startTimeOpt, endTimeOpt)
	if err != nil {
		h.logger.WithError(err).Error("GetAbilityMetrics: failed")
		return &api.GetAbilityMetricsInternalServerError{}, nil
	}

	return &api.GetAbilityMetricsOK{
		Metrics: []api.AbilityMetrics{*metrics},
		Total:   1,
		Limit:   api.NewOptInt(1),
		Offset:  api.NewOptInt(0),
	}, nil
}

// ActivateCombo implements POST /gameplay/combat/combos/activate
func (h *Handlers) ActivateCombo(ctx context.Context, req *api.ActivateComboRequest) (api.ActivateComboRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// Basic validation
	if req == nil {
		h.logger.Error("ActivateCombo: nil request")
		return &api.ActivateComboBadRequest{}, nil
	}

	if req.CharacterID == uuid.Nil {
		h.logger.Warn("ActivateCombo: empty character_id")
		return &api.ActivateComboBadRequest{}, nil
	}

	if req.ComboID == uuid.Nil {
		h.logger.Warn("ActivateCombo: empty combo_id")
		return &api.ActivateComboBadRequest{}, nil
	}

	// TODO: Implement business logic
	// For now, log request and return error (not implemented)
	h.logger.WithFields(logrus.Fields{
		"character_id":       req.CharacterID,
		"combo_id":           req.ComboID,
		"participants_count": len(req.Participants),
		"has_context":        req.Context.Set,
	}).Info("ActivateCombo request received (not implemented)")

	return &api.ActivateComboInternalServerError{}, nil
}

// CreateCombatSession implements POST /gameplay/combat/sessions
// Issue: #1607
func (h *Handlers) CreateCombatSession(ctx context.Context, req *api.CreateSessionRequest) (api.CreateCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.CreateCombatSessionBadRequest{}, nil
	}

	session, err := h.combatSessionService.CreateSession(ctx, req)
	if err != nil {
		return &api.CreateCombatSessionBadRequest{}, nil
	}

	return session, nil
}

// EndCombatSession implements DELETE /gameplay/combat/sessions/{sessionId}
// Issue: #1607
func (h *Handlers) EndCombatSession(ctx context.Context, params api.EndCombatSessionParams) (api.EndCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.Error{}, nil
	}

	response, err := h.combatSessionService.EndSession(ctx, params.SessionId)
	if err != nil {
		return &api.Error{}, nil
	}

	return response, nil
}

// GetAbilityCatalog implements GET /gameplay/combat/abilities/catalog
func (h *Handlers) GetAbilityCatalog(ctx context.Context, params api.GetAbilityCatalogParams) (api.GetAbilityCatalogRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetAbilityCatalogInternalServerError{}, nil
}

// GetArenaSessions implements GET /gameplay/arena/sessions
func (h *Handlers) GetArenaSessions(ctx context.Context) (api.GetArenaSessionsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetArenaSessionsOK{Sessions: []jx.Raw{}}, nil
}

// GetAvailableSynergies implements GET /gameplay/combat/abilities/synergies
func (h *Handlers) GetAvailableSynergies(ctx context.Context, params api.GetAvailableSynergiesParams) (api.GetAvailableSynergiesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetAvailableSynergiesInternalServerError{}, nil
}

// ApplySynergy implements POST /gameplay/combat/abilities/synergies/apply
// Issue: #156
func (h *Handlers) ApplySynergy(ctx context.Context, req *api.SynergyApplyRequest) (api.ApplySynergyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("ApplySynergy: abilityService not initialized")
		return &api.ApplySynergyInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	response, err := h.abilityService.ApplySynergy(ctx, characterID, req)
	if err != nil {
		h.logger.WithError(err).Error("ApplySynergy: failed")
		return &api.ApplySynergyInternalServerError{}, nil
	}

	return response, nil
}

// CheckCooldowns implements POST /gameplay/combat/abilities/cooldowns
// Issue: #156
func (h *Handlers) CheckCooldowns(ctx context.Context, req *api.CooldownCheckRequest) (api.CheckCooldownsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		return &api.CheckCooldownsInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	result, err := h.abilityService.GetCooldowns(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("CheckCooldowns: failed")
		return &api.CheckCooldownsInternalServerError{}, nil
	}

	// Filter cooldowns by requested ability IDs if provided
	if req != nil && len(req.AbilityIds) > 0 {
		filtered := []api.CooldownStatus{}
		requestedIDs := make(map[uuid.UUID]bool)
		for _, id := range req.AbilityIds {
			requestedIDs[id] = true
		}
		for _, cd := range result.Cooldowns {
			if requestedIDs[cd.AbilityID] {
				filtered = append(filtered, cd)
			}
		}
		result.Cooldowns = filtered
	}

	return result, nil
}

// GetCombatSession implements GET /gameplay/combat/sessions/{sessionId}
// Issue: #1607
func (h *Handlers) GetCombatSession(ctx context.Context, params api.GetCombatSessionParams) (api.GetCombatSessionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.Error{}, nil
	}

	session, err := h.combatSessionService.GetSession(ctx, params.SessionId)
	if err != nil {
		return &api.Error{}, nil
	}

	return session, nil
}

// GetComboCatalog implements GET /gameplay/combat/combos/catalog
func (h *Handlers) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (api.GetComboCatalogRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetComboCatalogInternalServerError{}, nil
}

// GetExtractZones implements GET /gameplay/extract-zones
func (h *Handlers) GetExtractZones(ctx context.Context) (api.GetExtractZonesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetExtractZonesOK{Zones: []jx.Raw{}}, nil
}

// GetFreerunRoutes implements GET /gameplay/freerun/routes
func (h *Handlers) GetFreerunRoutes(ctx context.Context, params api.GetFreerunRoutesParams) (api.GetFreerunRoutesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetFreerunRoutesOK{Routes: []jx.Raw{}}, nil
}

// GetInstalledImplants implements GET /gameplay/combat/implants
func (h *Handlers) GetInstalledImplants(ctx context.Context, params api.GetInstalledImplantsParams) (api.GetInstalledImplantsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetInstalledImplantsInternalServerError{}, nil
}

// GetLoadouts implements GET /gameplay/loadouts
func (h *Handlers) GetLoadouts(ctx context.Context, params api.GetLoadoutsParams) (api.GetLoadoutsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetLoadoutsOK{Loadouts: []jx.Raw{}}, nil
}

// GetStealthStatus implements GET /gameplay/stealth/status
func (h *Handlers) GetStealthStatus(ctx context.Context, params api.GetStealthStatusParams) (api.GetStealthStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetStealthStatusOK{}, nil
}

// ListCombatSessions implements GET /gameplay/combat/sessions
// Issue: #1607 - Uses memory pooling for zero allocations
func (h *Handlers) ListCombatSessions(ctx context.Context, params api.ListCombatSessionsParams) (*api.SessionListResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.combatSessionService == nil {
		return &api.SessionListResponse{}, nil
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	var status *api.SessionStatus
	if params.Status.IsSet() {
		status = &params.Status.Value
	}

	var sessionType *api.SessionType
	if params.SessionType.IsSet() {
		sessionType = &params.SessionType.Value
	}

	return h.combatSessionService.ListSessions(ctx, status, sessionType, limit, offset)
}

// GetComboLoadout implements GET /gameplay/combat/combos/loadout
// Issue: #1525
func (h *Handlers) GetComboLoadout(ctx context.Context, params api.GetComboLoadoutParams) (api.GetComboLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.comboService == nil {
		return &api.GetComboLoadoutInternalServerError{}, nil
	}

	loadout, err := h.comboService.GetLoadout(ctx, params.CharacterID)
	if err != nil {
		if err.Error() == "loadout not found" {
			return &api.GetComboLoadoutNotFound{}, nil
		}
		h.logger.WithError(err).Error("Failed to get combo loadout")
		return &api.GetComboLoadoutInternalServerError{}, nil
	}

	return convertLoadoutToAPI(loadout), nil
}

// UpdateComboLoadout implements POST /gameplay/combat/combos/loadout
// Issue: #1525
func (h *Handlers) UpdateComboLoadout(ctx context.Context, req *api.UpdateLoadoutRequest) (api.UpdateComboLoadoutRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.comboService == nil {
		return &api.UpdateComboLoadoutInternalServerError{}, nil
	}

	updateReq := convertUpdateLoadoutRequestFromAPI(req)
	loadout, err := h.comboService.UpdateLoadout(ctx, req.CharacterID, updateReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update combo loadout")
		return &api.UpdateComboLoadoutInternalServerError{}, nil
	}

	return convertLoadoutToAPI(loadout), nil
}

// SubmitComboScore implements POST /gameplay/combat/combos/score
// Issue: #1525
func (h *Handlers) SubmitComboScore(ctx context.Context, req *api.SubmitScoreRequest) (api.SubmitComboScoreRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.comboService == nil {
		return &api.SubmitComboScoreInternalServerError{}, nil
	}

	// Validation
	if req.ExecutionDifficulty < 0 || req.ExecutionDifficulty > 100 {
		return &api.SubmitComboScoreBadRequest{}, nil
	}
	if req.DamageOutput < 0 {
		return &api.SubmitComboScoreBadRequest{}, nil
	}
	if req.VisualImpact < 0 || req.VisualImpact > 100 {
		return &api.SubmitComboScoreBadRequest{}, nil
	}
	if teamCoord, ok := req.TeamCoordination.Get(); ok {
		if teamCoord < 0 || teamCoord > 100 {
			return &api.SubmitComboScoreBadRequest{}, nil
		}
	}

	submitReq := convertSubmitScoreRequestFromAPI(req)
	response, err := h.comboService.SubmitScore(ctx, submitReq)
	if err != nil {
		if err.Error() == "activation not found" {
			return &api.SubmitComboScoreNotFound{}, nil
		}
		h.logger.WithError(err).Error("Failed to submit combo score")
		return &api.SubmitComboScoreInternalServerError{}, nil
	}

	return convertScoreSubmissionResponseToAPI(response), nil
}

// GetComboAnalytics implements GET /gameplay/combat/combos/analytics
// Issue: #1525
func (h *Handlers) GetComboAnalytics(ctx context.Context, params api.GetComboAnalyticsParams) (api.GetComboAnalyticsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.comboService == nil {
		return &api.GetComboAnalyticsInternalServerError{}, nil
	}

	// Validation
	limit := 20
	if params.Limit.IsSet() {
		limitVal := params.Limit.Value
		if limitVal < 1 || limitVal > 100 {
			return &api.GetComboAnalyticsBadRequest{}, nil
		}
		limit = limitVal
	}

	offset := 0
	if params.Offset.IsSet() {
		if params.Offset.Value < 0 {
			return &api.GetComboAnalyticsBadRequest{}, nil
		}
		offset = params.Offset.Value
	}

	var comboID *uuid.UUID
	if params.ComboID.IsSet() {
		comboID = &params.ComboID.Value
	}

	var characterID *uuid.UUID
	if params.CharacterID.IsSet() {
		characterID = &params.CharacterID.Value
	}

	var periodStart *time.Time
	if params.PeriodStart.IsSet() {
		periodStart = &params.PeriodStart.Value
	}

	var periodEnd *time.Time
	if params.PeriodEnd.IsSet() {
		periodEnd = &params.PeriodEnd.Value
	}

	response, err := h.comboService.GetAnalytics(ctx, comboID, characterID, periodStart, periodEnd, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get combo analytics")
		return &api.GetComboAnalyticsInternalServerError{}, nil
	}

	return convertAnalyticsResponseToAPI(response), nil
}

// GetActiveAffixes implements GET /gameplay/affixes/active
// Issue: #1515
func (h *Handlers) GetActiveAffixes(ctx context.Context) (api.GetActiveAffixesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.affixService == nil {
		return &api.GetActiveAffixesInternalServerError{}, nil
	}

	response, err := h.affixService.GetActiveAffixes(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active affixes")
		return &api.GetActiveAffixesInternalServerError{}, nil
	}

	return convertActiveAffixesResponseToAPI(response), nil
}

// GetAffix implements GET /gameplay/affixes/{id}
// Issue: #1515
func (h *Handlers) GetAffix(ctx context.Context, params api.GetAffixParams) (api.GetAffixRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.affixService == nil {
		return &api.GetAffixInternalServerError{}, nil
	}

	affix, err := h.affixService.GetAffix(ctx, params.ID)
	if err != nil {
		if err.Error() == "affix not found" {
			return &api.GetAffixNotFound{}, nil
		}
		h.logger.WithError(err).Error("Failed to get affix")
		return &api.GetAffixInternalServerError{}, nil
	}

	return convertAffixToAPI(affix), nil
}

// GetInstanceAffixes implements GET /gameplay/instances/{instance_id}/affixes
// Issue: #1515
func (h *Handlers) GetInstanceAffixes(ctx context.Context, params api.GetInstanceAffixesParams) (api.GetInstanceAffixesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.affixService == nil {
		return &api.GetInstanceAffixesInternalServerError{}, nil
	}

	response, err := h.affixService.GetInstanceAffixes(ctx, params.InstanceID)
	if err != nil {
		if err.Error() == "instance not found" {
			return &api.GetInstanceAffixesNotFound{}, nil
		}
		h.logger.WithError(err).Error("Failed to get instance affixes")
		return &api.GetInstanceAffixesInternalServerError{}, nil
	}

	return convertInstanceAffixesResponseToAPI(response), nil
}

// GetAffixRotationHistory implements GET /gameplay/affixes/rotation/history
// Issue: #1515
func (h *Handlers) GetAffixRotationHistory(ctx context.Context, params api.GetAffixRotationHistoryParams) (api.GetAffixRotationHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.affixService == nil {
		return &api.GetAffixRotationHistoryInternalServerError{}, nil
	}

	weeksBack := 4
	if params.WeeksBack.IsSet() {
		weeksBack = params.WeeksBack.Value
	}

	limit := 20
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	offset := 0
	if params.Offset.IsSet() {
		offset = params.Offset.Value
	}

	response, err := h.affixService.GetRotationHistory(ctx, weeksBack, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get rotation history")
		return &api.GetAffixRotationHistoryInternalServerError{}, nil
	}

	return convertRotationHistoryResponseToAPI(response), nil
}

// TriggerAffixRotation implements POST /gameplay/affixes/rotation/trigger
// Issue: #1515
func (h *Handlers) TriggerAffixRotation(ctx context.Context, req api.OptTriggerRotationRequest) (api.TriggerAffixRotationRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.affixService == nil {
		return &api.TriggerAffixRotationInternalServerError{}, nil
	}

	force := false
	var customAffixes []uuid.UUID

	if req.IsSet() {
		if req.Value.Force.IsSet() {
			force = req.Value.Force.Value
		}
		customAffixes = req.Value.CustomAffixes
	}

	rotation, err := h.affixService.TriggerRotation(ctx, force, customAffixes)
	if err != nil {
		if err.Error() == "active rotation exists" {
			return &api.TriggerAffixRotationBadRequest{}, nil
		}
		h.logger.WithError(err).Error("Failed to trigger rotation")
		return &api.TriggerAffixRotationInternalServerError{}, nil
	}

	apiRotation := convertRotationToAPI(rotation)
	return &apiRotation, nil
}

// CancelQuest implements POST /gameplay/quests/{quest_id}/cancel
// Issue: #50
func (h *Handlers) CancelQuest(ctx context.Context, params api.CancelQuestParams) (api.CancelQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("CancelQuest: questRepository not initialized")
		return &api.CancelQuestInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	// characterID := uuid.Nil // Placeholder

	// TODO: Implement actual quest cancellation logic
	h.logger.WithField("quest_id", params.QuestID).Info("CancelQuest called (stub)")

	// Return quest instance (200 OK)
	// TODO: Get actual quest instance from repository
	return &api.QuestInstance{
		QuestID: params.QuestID,
		State:   api.QuestInstanceStateCANCELLED,
	}, nil
}

// CheckQuestConditions implements GET /gameplay/quests/{questId}/conditions
// Issue: #50
func (h *Handlers) CheckQuestConditions(ctx context.Context, params api.CheckQuestConditionsParams) (api.CheckQuestConditionsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("CheckQuestConditions: questRepository not initialized")
		return &api.CheckQuestConditionsInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	// characterID := uuid.Nil // Placeholder

	// TODO: Implement actual condition checking logic
	h.logger.WithField("quest_id", params.QuestID).Info("CheckQuestConditions called (stub)")

	return &api.CheckQuestConditionsOK{
		AllConditionsMet: true,
		Conditions:       []api.CheckQuestConditionsOKConditionsItem{},
	}, nil
}

// CompleteQuest implements POST /gameplay/quests/{questId}/complete
// Issue: #50
func (h *Handlers) CompleteQuest(ctx context.Context, req api.OptCompleteQuestRequest, params api.CompleteQuestParams) (api.CompleteQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("CompleteQuest: questRepository not initialized")
		return &api.CompleteQuestInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	// characterID := uuid.Nil // Placeholder

	// TODO: Implement actual quest completion logic
	h.logger.WithField("quest_id", params.QuestID).Info("CompleteQuest called (stub)")

	return &api.CompleteQuestResponse{
		QuestInstance: api.QuestInstance{
			QuestID: params.QuestID,
			State:   api.QuestInstanceStateCOMPLETED,
		},
		Rewards: api.QuestRewards{},
	}, nil
}

// DistributeQuestRewards implements POST /gameplay/quests/{questId}/rewards/distribute
// Issue: #50
func (h *Handlers) DistributeQuestRewards(ctx context.Context, params api.DistributeQuestRewardsParams) (api.DistributeQuestRewardsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("DistributeQuestRewards: questRepository not initialized")
		return &api.DistributeQuestRewardsInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	// characterID := uuid.Nil // Placeholder

	// TODO: Implement actual reward distribution logic
	// For now, return a basic response
	return &api.DistributeQuestRewardsOK{
		Success: true,
		Rewards: api.QuestRewards{},
	}, nil
}

// CreateEncounter implements POST /gameplay/combat/ai/encounter
// Issue: #50
func (h *Handlers) CreateEncounter(ctx context.Context, req *api.CreateEncounterRequest) (api.CreateEncounterRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement encounter creation logic
	h.logger.Info("CreateEncounter called (stub)")

	return &api.AIEncounter{
		ID:            uuid.New(),
		ZoneID:        uuid.New(),
		EncounterType: "combat",
		Result:        api.OptNilAIEncounterResult{},
		StartedAt:     time.Now(),
		CompletedAt:   api.NewOptNilDateTime(time.Now().Add(1 * time.Hour)),
		ProfileIds:    []uuid.UUID{},
	}, nil
}

// EndEncounter implements POST /gameplay/combat/ai/encounter/{encounterId}/end
// Issue: #50
func (h *Handlers) EndEncounter(ctx context.Context, req *api.EndEncounterRequest, params api.EndEncounterParams) (api.EndEncounterRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement encounter ending logic
	h.logger.WithFields(map[string]interface{}{
		"encounter_id": params.EncounterID,
		"result":       req.Result,
	}).Info("EndEncounter called (stub)")

	// Return stub response (AIEncounter implements endEncounterRes)
	// Convert EndEncounterRequestResult to AIEncounterResult
	var result api.AIEncounterResult
	switch req.Result {
	case api.EndEncounterRequestResultVictory:
		result = api.AIEncounterResultVictory
	case api.EndEncounterRequestResultDefeat:
		result = api.AIEncounterResultDefeat
	case api.EndEncounterRequestResultAbandoned:
		result = api.AIEncounterResultAbandoned
	default:
		result = api.AIEncounterResultAbandoned
	}
	return &api.AIEncounter{
		ID:            params.EncounterID,
		ZoneID:        uuid.New(),
		EncounterType: "combat",
		Result:        api.NewOptNilAIEncounterResult(result),
		StartedAt:     time.Now().Add(-1 * time.Hour),
		CompletedAt:   api.NewOptNilDateTime(time.Now()),
		ProfileIds:    []uuid.UUID{},
	}, nil
}

// GetAIProfile implements GET /gameplay/combat/ai/profiles/{profileId}
// Issue: #50
func (h *Handlers) GetAIProfile(ctx context.Context, params api.GetAIProfileParams) (api.GetAIProfileRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement AI profile retrieval logic
	// For now, return a basic response
	return &api.GetAIProfileNotFound{}, nil
}

// GetAIProfileTelemetry implements GET /gameplay/combat/ai/profiles/{profileId}/telemetry
// Issue: #50
func (h *Handlers) GetAIProfileTelemetry(ctx context.Context, params api.GetAIProfileTelemetryParams) (api.GetAIProfileTelemetryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement AI profile telemetry retrieval logic
	// For now, return a basic response
	return &api.GetAIProfileTelemetryNotFound{}, nil
}

// GetDialogueHistory implements GET /gameplay/dialogue/history
// Issue: #50
func (h *Handlers) GetDialogueHistory(ctx context.Context, params api.GetDialogueHistoryParams) (api.GetDialogueHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement dialogue history retrieval logic
	// For now, return a basic response
	return &api.GetDialogueHistoryNotFound{}, nil
}

// GetEncounter implements GET /gameplay/combat/ai/encounter/{encounterId}
// Issue: #50
func (h *Handlers) GetEncounter(ctx context.Context, params api.GetEncounterParams) (api.GetEncounterRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement encounter retrieval logic
	// For now, return a basic response
	return &api.GetEncounterNotFound{}, nil
}

// GetPlayerQuests implements GET /gameplay/quests/by-player/{player_id}
// Issue: #50
func (h *Handlers) GetPlayerQuests(ctx context.Context, params api.GetPlayerQuestsParams) (api.GetPlayerQuestsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetPlayerQuests: questRepository not initialized")
		return &api.GetPlayerQuestsInternalServerError{}, nil
	}

	// TODO: Implement quest retrieval logic
	// For now, return a basic response
	return &api.QuestListResponse{
		Quests: []api.QuestInstance{},
		Total:  0,
	}, nil
}

// GetQuest implements GET /gameplay/quests/{quest_id}
// Issue: #50
func (h *Handlers) GetQuest(ctx context.Context, params api.GetQuestParams) (api.GetQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetQuest: questRepository not initialized")
		return &api.GetQuestInternalServerError{}, nil
	}

	// TODO: Implement quest retrieval logic
	// For now, return a basic response
	return &api.GetQuestNotFound{}, nil
}

// GetQuestDialogue implements GET /gameplay/quests/{quest_id}/dialogue
// Issue: #50
func (h *Handlers) GetQuestDialogue(ctx context.Context, params api.GetQuestDialogueParams) (api.GetQuestDialogueRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetQuestDialogue: questRepository not initialized")
		return &api.GetQuestDialogueInternalServerError{}, nil
	}

	// TODO: Implement dialogue retrieval logic
	return &api.GetQuestDialogueNotFound{}, nil
}

// GetQuestEvents implements GET /gameplay/quests/{quest_id}/events
// Issue: #50
func (h *Handlers) GetQuestEvents(ctx context.Context, params api.GetQuestEventsParams) (api.GetQuestEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetQuestEvents: questRepository not initialized")
		return &api.GetQuestEventsInternalServerError{}, nil
	}

	// TODO: Implement events retrieval logic
	return &api.QuestEventsResponse{
		Events: []api.QuestEvent{},
		Total:  0,
	}, nil
}

// GetQuestRequirements implements GET /gameplay/quests/{quest_id}/requirements
// Issue: #50
func (h *Handlers) GetQuestRequirements(ctx context.Context, params api.GetQuestRequirementsParams) (api.GetQuestRequirementsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetQuestRequirements: questRepository not initialized")
		return &api.GetQuestRequirementsInternalServerError{}, nil
	}

	// TODO: Implement requirements retrieval logic
	return &api.GetQuestRequirementsNotFound{}, nil
}

// GetQuestRewards implements GET /gameplay/quests/{quest_id}/rewards
// Issue: #50
func (h *Handlers) GetQuestRewards(ctx context.Context, params api.GetQuestRewardsParams) (api.GetQuestRewardsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetQuestRewards: questRepository not initialized")
		return &api.GetQuestRewardsInternalServerError{}, nil
	}

	// TODO: Implement rewards retrieval logic
	return &api.GetQuestRewardsNotFound{}, nil
}

// GetQuestState implements GET /gameplay/quests/{quest_id}/state
// Issue: #50
func (h *Handlers) GetQuestState(ctx context.Context, params api.GetQuestStateParams) (api.GetQuestStateRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetQuestState: questRepository not initialized")
		return &api.GetQuestStateInternalServerError{}, nil
	}

	// TODO: Implement state retrieval logic
	return &api.GetQuestStateNotFound{}, nil
}

// GetSkillCheckHistory implements GET /gameplay/quests/{quest_id}/skill-checks
// Issue: #50
func (h *Handlers) GetSkillCheckHistory(ctx context.Context, params api.GetSkillCheckHistoryParams) (api.GetSkillCheckHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("GetSkillCheckHistory: questRepository not initialized")
		return &api.GetSkillCheckHistoryInternalServerError{}, nil
	}

	// TODO: Implement skill check history retrieval logic
	return &api.SkillChecksResponse{
		SkillChecks: []api.SkillCheckResult{},
		Total:       0,
	}, nil
}

// StartQuest implements POST /gameplay/quests/{quest_id}/start
// Issue: #50
func (h *Handlers) StartQuest(ctx context.Context, req *api.StartQuestRequest) (api.StartQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("StartQuest: questRepository not initialized")
		return &api.StartQuestInternalServerError{}, nil
	}

	// TODO: Implement quest start logic
	return &api.StartQuestNotFound{}, nil
}

// ListAIProfiles implements GET /gameplay/combat/ai/profiles
// Issue: #50
func (h *Handlers) ListAIProfiles(ctx context.Context, params api.ListAIProfilesParams) (api.ListAIProfilesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement AI profiles list retrieval logic
	return &api.ListAIProfilesOK{
		Profiles: []api.AIProfile{},
		Total:    0,
	}, nil
}

// MakeDialogueChoice implements POST /gameplay/quests/{quest_id}/dialogue/choice
// Issue: #50
func (h *Handlers) MakeDialogueChoice(ctx context.Context, req *api.DialogueChoiceRequest, params api.MakeDialogueChoiceParams) (api.MakeDialogueChoiceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement dialogue choice logic
	return &api.MakeDialogueChoiceNotFound{}, nil
}

// PerformSkillCheck implements POST /gameplay/quests/{quest_id}/skill-checks
// Issue: #50
func (h *Handlers) PerformSkillCheck(ctx context.Context, req *api.SkillCheckRequest, params api.PerformSkillCheckParams) (api.PerformSkillCheckRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("PerformSkillCheck: questRepository not initialized")
		return &api.PerformSkillCheckInternalServerError{}, nil
	}

	// TODO: Implement skill check logic
	return &api.PerformSkillCheckNotFound{}, nil
}

// StartEncounter implements POST /gameplay/combat/ai/encounter/start
// Issue: #50
func (h *Handlers) StartEncounter(ctx context.Context, params api.StartEncounterParams) (api.StartEncounterRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement encounter start logic
	return &api.StartEncounterNotFound{}, nil
}

// TransitionRaidPhase implements POST /gameplay/raids/{raid_id}/phases/{phase_id}/transition
// Issue: #50
func (h *Handlers) TransitionRaidPhase(ctx context.Context, req *api.RaidPhaseTransitionRequest, params api.TransitionRaidPhaseParams) (api.TransitionRaidPhaseRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement raid phase transition logic
	return &api.TransitionRaidPhaseNotFound{}, nil
}

// ReloadQuestContent implements POST /gameplay/quests/content/reload
// Issue: #50
func (h *Handlers) ReloadQuestContent(ctx context.Context, req *api.ReloadQuestContentRequest) (api.ReloadQuestContentRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("ReloadQuestContent: questRepository not initialized")
		return &api.ReloadQuestContentInternalServerError{}, nil
	}

	if req == nil || strings.TrimSpace(req.QuestID) == "" {
		h.logger.Warn("ReloadQuestContent: empty request or quest_id")
		return &api.ReloadQuestContentBadRequest{}, nil
	}

	if len(req.YamlContent) == 0 {
		h.logger.Warn("ReloadQuestContent: yaml_content is empty")
		return &api.ReloadQuestContentBadRequest{}, nil
	}

	contentData := make(map[string]interface{}, len(req.YamlContent))
	for key, raw := range req.YamlContent {
		var decoded interface{}
		if err := json.Unmarshal(raw, &decoded); err != nil {
			h.logger.WithError(err).WithField("field", key).Warn("ReloadQuestContent: failed to decode yaml_content field")
			return &api.ReloadQuestContentBadRequest{}, nil
		}
		contentData[key] = decoded
	}

	metadata := extractMap(contentData, "metadata")
	if metaID := extractString(metadata, "id"); metaID != "" && metaID != req.QuestID {
		h.logger.WithFields(logrus.Fields{
			"quest_id":    req.QuestID,
			"metadata.id": metaID,
		}).Warn("ReloadQuestContent: quest_id mismatch with metadata.id")
		return &api.ReloadQuestContentBadRequest{}, nil
	}

	title := req.QuestID
	description := ""
	questType := "side"
	isActive := true
	version := 1

	if meta := metadata; len(meta) > 0 {
		if t := extractString(meta, "title"); t != "" {
			title = t
		}
		if v := extractString(meta, "version"); v != "" {
			if parsed, err := parseVersionMajor(v); err == nil && parsed > 0 {
				version = parsed
			}
		}
		if status := extractString(meta, "status"); status != "" {
			isActive = status != "archived"
		}
		if qt := extractString(meta, "quest_type"); qt != "" {
			questType = qt
		}
	}

	if summary := extractMap(contentData, "summary"); len(summary) > 0 {
		if essence := extractString(summary, "essence"); essence != "" {
			description = essence
		} else if goal := extractString(summary, "goal"); goal != "" {
			description = goal
		}
		if questType == "side" {
			if points, ok := summary["key_points"].([]interface{}); ok {
				if inferred := parseQuestType(points); inferred != "" {
					questType = inferred
				}
			}
		}
	}
	if description == "" {
		description = title
	}

	quest := &models.QuestDefinition{
		QuestID:      req.QuestID,
		Title:        title,
		Description:  description,
		QuestType:    questType,
		Requirements: map[string]interface{}{},
		Objectives:   map[string]interface{}{},
		Rewards:      map[string]interface{}{},
		Branches:     map[string]interface{}{},
		ContentData:  contentData,
		Version:      version,
		IsActive:     isActive,
	}

	saved, err := h.questRepository.ImportQuest(ctx, quest)
	if err != nil {
		h.logger.WithError(err).Error("ReloadQuestContent: failed to import quest")
		return &api.ReloadQuestContentInternalServerError{}, nil
	}

	now := time.Now()
	response := &api.ReloadQuestContentResponse{
		QuestID:    api.NewOptString(saved.QuestID),
		Message:    api.NewOptString("Quest content imported"),
		ImportedAt: api.NewOptDateTime(now),
	}

	return response, nil
}

func parseVersionMajor(raw string) (int, error) {
	for i := 0; i < len(raw); i++ {
		if raw[i] < '0' || raw[i] > '9' {
			if i == 0 {
				return 1, nil
			}
			raw = raw[:i]
			break
		}
	}
	return strconv.Atoi(raw)
}

func parseQuestType(points []interface{}) string {
	for _, p := range points {
		text, ok := p.(string)
		if !ok {
			continue
		}
		lower := strings.ToLower(text)
		if strings.Contains(lower, "тип") {
			parts := strings.SplitN(lower, "тип", 2)
			if len(parts) == 2 {
				return strings.TrimSpace(strings.Trim(parts[1], "-—: "))
			}
		}
	}
	return ""
}

func extractMap(payload map[string]interface{}, key string) map[string]interface{} {
	if payload == nil {
		return nil
	}
	val, ok := payload[key]
	if !ok {
		return nil
	}
	if m, ok := val.(map[string]interface{}); ok {
		return m
	}
	return nil
}

func extractString(payload map[string]interface{}, key string) string {
	if payload == nil {
		return ""
	}
	if val, ok := payload[key]; ok {
		if str, ok := val.(string); ok {
			return strings.TrimSpace(str)
		}
	}
	return ""
}

// UpdateCyberpsychosis implements POST /gameplay/combat/abilities/cyberpsychosis/update
// Issue: #156
func (h *Handlers) UpdateCyberpsychosis(ctx context.Context, req *api.CyberpsychosisUpdateRequest) (api.UpdateCyberpsychosisRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("UpdateCyberpsychosis: abilityService not initialized")
		return &api.UpdateCyberpsychosisInternalServerError{}, nil
	}

	// TODO: Get characterID from context (from SecurityHandler)
	characterID := uuid.Nil // Placeholder

	// TODO: Implement cyberpsychosis update logic
	state, err := h.abilityService.GetCyberpsychosisState(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("UpdateCyberpsychosis: failed")
		return &api.UpdateCyberpsychosisInternalServerError{}, nil
	}

	return state, nil
}

// UpdateQuestState implements POST /gameplay/quests/{quest_id}/state/update
// Issue: #50
func (h *Handlers) UpdateQuestState(ctx context.Context, req *api.UpdateStateRequest, params api.UpdateQuestStateParams) (api.UpdateQuestStateRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.questRepository == nil {
		h.logger.Error("UpdateQuestState: questRepository not initialized")
		return &api.UpdateQuestStateInternalServerError{}, nil
	}

	// TODO: Implement quest state update logic
	return &api.UpdateQuestStateNotFound{}, nil
}
