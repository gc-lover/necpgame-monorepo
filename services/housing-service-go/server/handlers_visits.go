// Issue: #141886468
package server

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetApartmentVisits(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, params api.GetApartmentVisitsParams) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	visits, total, err := h.service.GetApartmentVisits(ctx, apartmentID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment visits")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment visits")
		return
	}

	apiVisits := make([]api.ApartmentVisit, len(visits))
	for i, v := range visits {
		apiID := openapi_types.UUID(v.ID)
		apiApartmentID := openapi_types.UUID(v.ApartmentID)
		apiVisitorID := openapi_types.UUID(v.VisitorID)
		apiVisits[i] = api.ApartmentVisit{
			Id:          &apiID,
			ApartmentId: &apiApartmentID,
			VisitorId:   &apiVisitorID,
			VisitedAt:   &v.VisitedAt,
		}
	}

	response := api.ApartmentVisitsResponse{
		ApartmentId: &apartmentId,
		Visits:      &apiVisits,
		Total:       &total,
		Limit:       &limit,
		Offset:      &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetPlayerVisits(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetPlayerVisitsParams) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	visits, total, err := h.service.GetPlayerVisits(ctx, playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player visits")
		h.respondError(w, http.StatusInternalServerError, "failed to get player visits")
		return
	}

	apiVisits := make([]api.ApartmentVisit, len(visits))
	for i, v := range visits {
		apiID := openapi_types.UUID(v.ID)
		apiApartmentID := openapi_types.UUID(v.ApartmentID)
		apiVisitorID := openapi_types.UUID(v.VisitorID)
		apiVisits[i] = api.ApartmentVisit{
			Id:          &apiID,
			ApartmentId: &apiApartmentID,
			VisitorId:   &apiVisitorID,
			VisitedAt:   &v.VisitedAt,
		}
	}

	response := api.ApartmentVisitsResponse{
		Visits: &apiVisits,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}



