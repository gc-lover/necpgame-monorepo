// Issue: #141886468
package server

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetApartmentPrestige(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment prestige")
		return
	}

	prestigeScore := apartment.PrestigeScore
	basePrestige := 0
	furniturePrestige := 0
	uniquenessBonus := 0
	locationMultiplier := float32(1.0)

	response := api.ApartmentPrestigeResponse{
		ApartmentId:        &apartmentId,
		PrestigeScore:      &prestigeScore,
		BasePrestige:       &basePrestige,
		FurniturePrestige:  &furniturePrestige,
		UniquenessBonus:    &uniquenessBonus,
		LocationMultiplier: &locationMultiplier,
	}

	h.respondJSON(w, http.StatusOK, response)
}

