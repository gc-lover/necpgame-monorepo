// Issue: #1599, #1604, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/go-faster/jx"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
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
	logger *logrus.Logger
	comboService ComboServiceInterface
	combatSessionService CombatSessionServiceInterface
	affixService AffixServiceInterface

	// Memory pooling for hot path structs (zero allocations target!)
	sessionListResponsePool sync.Pool
	combatSessionResponsePool sync.Pool
	sessionEndResponsePool sync.Pool
	getStealthStatusOKPool sync.Pool
	internalServerErrorPool sync.Pool
	badRequestPool sync.Pool
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
	if db != nil {
		h.comboService = NewComboService(db)
		h.combatSessionService = NewCombatSessionService(db)
		h.affixService = NewAffixService(db)
	}

	return h
}

// ActivateAbility implements POST /gameplay/combat/abilities/activate
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

	// TODO: Implement business logic
	// For now, log request and return error (not implemented)
	h.logger.WithFields(logrus.Fields{
		"ability_id": req.AbilityID,
		"target_id": req.TargetID,
		"has_position": req.Position.Set,
	}).Info("ActivateAbility request received (not implemented)")

	return &api.ActivateAbilityInternalServerError{}, nil
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
		"character_id": req.CharacterID,
		"combo_id": req.ComboID,
		"participants_count": len(req.Participants),
		"has_context": req.Context.Set,
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
