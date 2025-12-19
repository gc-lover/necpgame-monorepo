package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/go-faster/jx"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// Ability and loadout handlers

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

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.CreateOrUpdateAbilityLoadoutUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("CreateOrUpdateAbilityLoadout: invalid user_id format")
		return &api.CreateOrUpdateAbilityLoadoutInternalServerError{}, nil
	}

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

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.GetAbilityLoadoutsUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("GetAbilityLoadouts: invalid user_id format")
		return &api.GetAbilityLoadoutsInternalServerError{}, nil
	}

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

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.GetCyberpsychosisStateUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("GetCyberpsychosisState: invalid user_id format")
		return &api.GetCyberpsychosisStateInternalServerError{}, nil
	}

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

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.GetAbilityMetricsUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("GetAbilityMetrics: invalid user_id format")
		return &api.GetAbilityMetricsInternalServerError{}, nil
	}

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

// GetAbilityCatalog implements GET /gameplay/combat/abilities/catalog
func (h *Handlers) GetAbilityCatalog(ctx context.Context, params api.GetAbilityCatalogParams) (api.GetAbilityCatalogRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetAbilityCatalogInternalServerError{}, nil
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

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.ApplySynergyUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("ApplySynergy: invalid user_id format")
		return &api.ApplySynergyInternalServerError{}, nil
	}

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

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.CheckCooldownsUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("CheckCooldowns: invalid user_id format")
		return &api.CheckCooldownsInternalServerError{}, nil
	}

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

// UpdateCyberpsychosis implements POST /gameplay/combat/abilities/cyberpsychosis/update
// Issue: #156
func (h *Handlers) UpdateCyberpsychosis(ctx context.Context, req *api.CyberpsychosisUpdateRequest) (api.UpdateCyberpsychosisRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.abilityService == nil {
		h.logger.Error("UpdateCyberpsychosis: abilityService not initialized")
		return &api.UpdateCyberpsychosisInternalServerError{}, nil
	}

	// Get characterID from JWT auth context
	userIDValue := ctx.Value("user_id")
	if userIDValue == nil {
		return &api.UpdateCyberpsychosisUnauthorized{}, nil
	}

	characterID, err := uuid.Parse(userIDValue.(string))
	if err != nil {
		h.logger.WithError(err).Error("UpdateCyberpsychosis: invalid user_id format")
		return &api.UpdateCyberpsychosisInternalServerError{}, nil
	}

	// TODO: Implement cyberpsychosis update logic
	state, err := h.abilityService.GetCyberpsychosisState(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("UpdateCyberpsychosis: failed")
		return &api.UpdateCyberpsychosisInternalServerError{}, nil
	}

	return state, nil
}
