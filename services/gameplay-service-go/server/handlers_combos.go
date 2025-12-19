// SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// Combo handlers

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

// GetComboCatalog implements GET /gameplay/combat/combos/catalog
func (h *Handlers) GetComboCatalog(ctx context.Context, params api.GetComboCatalogParams) (api.GetComboCatalogRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement logic
	return &api.GetComboCatalogInternalServerError{}, nil
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
