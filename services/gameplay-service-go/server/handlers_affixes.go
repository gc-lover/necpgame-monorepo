package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
)

// Affix handlers

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
