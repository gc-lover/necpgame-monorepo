// Combat Combos Loadouts Service HTTP Handlers
// Issue: #141890005

package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/rs/zerolog/log"

	"combat-combos-loadouts-service-go/pkg/api"
)

// Handler implements the generated Handler interface
type Handler struct {
	service *Service
}

// NewHandler creates a new handler instance
func NewHandler(service *Service) *Handler {
	return &Handler{service: service}
}

// GetComboLoadout implements GET /gameplay/combat/combos/loadout
func (h *Handler) GetComboLoadout(ctx context.Context, params api.GetComboLoadoutParams) (api.GetComboLoadoutRes, error) {
	logger := log.With().
		Str("operation", "GetComboLoadout").
		Str("character_id", params.CharacterID.String()).
		Logger()

	logger.Info().Msg("Getting combo loadout")

	// Get character ID from query parameter
	characterID, err := uuid.Parse(params.CharacterID.String())
	if err != nil {
		logger.Error().Err(err).Msg("Invalid character ID format")
		return &api.GetComboLoadoutUnauthorized{
			Error: "Invalid character ID format",
		}, nil
	}

	// Get combo loadout from service
	loadout, err := h.service.GetComboLoadout(ctx, characterID)
	if err != nil {
		logger.Error().Err(err).Msg("Failed to get combo loadout")

		// Check if it's a not found error
		if err.Error() == "failed to get combo loadout: no rows in result set" {
			return &api.GetComboLoadoutNotFound{
				Error: "Combo loadout not found for character",
			}, nil
		}

		return &api.GetComboLoadoutInternalServerError{
			Error: "Internal server error",
		}, nil
	}

	logger.Info().Msg("Combo loadout retrieved successfully")

	// Convert to API response format
	apiLoadout := h.convertToAPILoadout(loadout)

	return &apiLoadout, nil
}

// UpdateComboLoadout implements POST /gameplay/combat/combos/loadout
func (h *Handler) UpdateComboLoadout(ctx context.Context, req *api.UpdateLoadoutRequest) (api.UpdateComboLoadoutRes, error) {
	logger := log.With().
		Str("operation", "UpdateComboLoadout").
		Logger()

	logger.Info().Msg("Updating combo loadout")

	// Validate request
	if req == nil {
		logger.Error().Msg("Request is nil")
		return &api.UpdateComboLoadoutBadRequest{
			Error: "Request body is required",
		}, nil
	}

	// Convert API request to service request
	serviceReq := h.convertFromAPIRequest(req)

	// Update combo loadout via service
	loadout, err := h.service.UpdateComboLoadout(ctx, serviceReq)
	if err != nil {
		logger.Error().Err(err).Msg("Failed to update combo loadout")

		return &api.UpdateComboLoadoutBadRequest{
			Error: err.Error(),
		}, nil
	}

	logger.Info().Msg("Combo loadout updated successfully")

	// Convert to API response format
	apiLoadout := h.convertToAPILoadout(loadout)

	return &apiLoadout, nil
}

// convertToAPILoadout converts service ComboLoadout to API ComboLoadout
func (h *Handler) convertToAPILoadout(loadout *ComboLoadout) api.ComboLoadout {
	// Convert active combos
	activeCombos := make([]uuid.UUID, len(loadout.ActiveCombos))
	copy(activeCombos, loadout.ActiveCombos)

	// Convert preferences
	preferences := api.ComboLoadoutPreferences{
		AutoActivate:  api.NewOptBool(loadout.Preferences.AutoActivate),
		PriorityOrder: make([]uuid.UUID, len(loadout.Preferences.PriorityOrder)),
	}

	copy(preferences.PriorityOrder, loadout.Preferences.PriorityOrder)

	return api.ComboLoadout{
		ID:           loadout.ID,
		CharacterID:  loadout.CharacterID,
		ActiveCombos: activeCombos,
		Preferences:  api.NewOptComboLoadoutPreferences(preferences),
		CreatedAt:    api.NewOptDateTime(loadout.CreatedAt),
		UpdatedAt:    api.NewOptDateTime(loadout.UpdatedAt),
	}
}

// convertFromAPIRequest converts API UpdateLoadoutRequest to service UpdateLoadoutRequest
func (h *Handler) convertFromAPIRequest(req *api.UpdateLoadoutRequest) *UpdateLoadoutRequest {
	characterID := req.CharacterID

	// Convert active combos
	activeCombos := make([]uuid.UUID, len(req.ActiveCombos))
	copy(activeCombos, req.ActiveCombos)

	// Get preferences from Opt field
	prefs := req.Preferences.Value

	// Convert preferences
	preferences := ComboLoadoutPreferences{
		AutoActivate:  prefs.AutoActivate.Value,
		PriorityOrder: make([]uuid.UUID, len(prefs.PriorityOrder)),
	}

	copy(preferences.PriorityOrder, prefs.PriorityOrder)

	return &UpdateLoadoutRequest{
		CharacterID:  characterID,
		ActiveCombos: activeCombos,
		Preferences:  preferences,
	}
}
