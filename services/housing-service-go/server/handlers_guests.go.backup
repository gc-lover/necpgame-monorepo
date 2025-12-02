// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/models"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetApartmentGuests(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment guests")
		return
	}

	guests := make([]openapi_types.UUID, len(apartment.Guests))
	for i, g := range apartment.Guests {
		guests[i] = openapi_types.UUID(g)
	}

	response := map[string]interface{}{
		"apartment_id": apartmentId,
		"guests":       guests,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) AddGuest(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.AddGuestJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	guestID := uuid.UUID(req.CharacterId)
	modelReq := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		Guests:      []uuid.UUID{guestID},
	}

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	// Добавляем гостя, если его еще нет
	found := false
	for _, g := range apartment.Guests {
		if g == guestID {
			found = true
			break
		}
	}
	if !found {
		apartment.Guests = append(apartment.Guests, guestID)
		modelReq.Guests = apartment.Guests
		err = h.service.UpdateApartmentSettings(ctx, apartmentID, modelReq)
		if err != nil {
			h.logger.WithError(err).Error("Failed to add guest")
			h.respondError(w, http.StatusInternalServerError, "failed to add guest")
			return
		}
	}

	response := map[string]interface{}{
		"apartment_id": apartmentId,
		"character_id": req.CharacterId,
		"status":       "success",
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) RemoveGuest(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.RemoveGuestParams) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	guestID := uuid.UUID(params.PlayerId)
	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	// Удаляем гостя из списка
	newGuests := make([]uuid.UUID, 0, len(apartment.Guests))
	for _, g := range apartment.Guests {
		if g != guestID {
			newGuests = append(newGuests, g)
		}
	}

	modelReq := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		Guests:      newGuests,
	}

	err = h.service.UpdateApartmentSettings(ctx, apartmentID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove guest")
		h.respondError(w, http.StatusInternalServerError, "failed to remove guest")
		return
	}

	response := map[string]interface{}{
		"apartment_id": apartmentId,
		"player_id":    params.PlayerId,
		"status":       "success",
	}

	h.respondJSON(w, http.StatusOK, response)
}



