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

func (h *HousingHandlers) UpdateFurniturePosition(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)
	furnitureID := uuid.UUID(furnitureId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.UpdateFurniturePositionJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := make(map[string]interface{})
	if req.PositionX != nil || req.PositionY != nil || req.PositionZ != nil {
		if req.PositionX != nil {
			position["x"] = *req.PositionX
		}
		if req.PositionY != nil {
			position["y"] = *req.PositionY
		}
		if req.PositionZ != nil {
			position["z"] = *req.PositionZ
		}
	}

	rotation := make(map[string]interface{})
	if req.RotationYaw != nil {
		rotation["yaw"] = *req.RotationYaw
	}

	scale := make(map[string]interface{})
	if req.Scale != nil {
		scale["uniform"] = *req.Scale
	}

	var posMap, rotMap, scaleMap map[string]interface{}
	if len(position) > 0 {
		posMap = position
	}
	if len(rotation) > 0 {
		rotMap = rotation
	}
	if len(scale) > 0 {
		scaleMap = scale
	}

	furniture, err := h.service.UpdateFurniturePosition(ctx, apartmentID, furnitureID, posMap, rotMap, scaleMap)
	if err != nil {
		h.logger.WithError(err).WithFields(map[string]interface{}{
			"apartment_id": apartmentID,
			"furniture_id": furnitureID,
			"character_id":  characterID,
		}).Error("Failed to update furniture position")
		h.respondError(w, http.StatusInternalServerError, "failed to update furniture position")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIPlacedFurniture(furniture))
}

func (h *HousingHandlers) MoveFurniture(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)
	furnitureID := uuid.UUID(furnitureId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.MoveFurnitureJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	position := make(map[string]interface{})
	if req.PositionX != nil || req.PositionY != nil || req.PositionZ != nil {
		if req.PositionX != nil {
			position["x"] = *req.PositionX
		}
		if req.PositionY != nil {
			position["y"] = *req.PositionY
		}
		if req.PositionZ != nil {
			position["z"] = *req.PositionZ
		}
	}

	rotation := make(map[string]interface{})
	if req.RotationX != nil || req.RotationY != nil || req.RotationZ != nil {
		if req.RotationX != nil {
			rotation["x"] = *req.RotationX
		}
		if req.RotationY != nil {
			rotation["y"] = *req.RotationY
		}
		if req.RotationZ != nil {
			rotation["z"] = *req.RotationZ
		}
	}

	scale := make(map[string]interface{})
	if req.ScaleX != nil || req.ScaleY != nil || req.ScaleZ != nil {
		if req.ScaleX != nil {
			scale["x"] = *req.ScaleX
		}
		if req.ScaleY != nil {
			scale["y"] = *req.ScaleY
		}
		if req.ScaleZ != nil {
			scale["z"] = *req.ScaleZ
		}
	}

	var posMap, rotMap, scaleMap map[string]interface{}
	if len(position) > 0 {
		posMap = position
	}
	if len(rotation) > 0 {
		rotMap = rotation
	}
	if len(scale) > 0 {
		scaleMap = scale
	}

	furniture, err := h.service.UpdateFurniturePosition(ctx, apartmentID, furnitureID, posMap, rotMap, scaleMap)
	if err != nil {
		h.logger.WithError(err).WithFields(map[string]interface{}{
			"apartment_id": apartmentID,
			"furniture_id": furnitureID,
			"character_id":  characterID,
		}).Error("Failed to move furniture")
		h.respondError(w, http.StatusInternalServerError, "failed to move furniture")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIPlacedFurniture(furniture))
}

func (h *HousingHandlers) GetFurnitureCatalog(w http.ResponseWriter, r *http.Request, params api.GetFurnitureCatalogParams) {
	ctx := r.Context()

	var category *models.FurnitureCategory
	if params.Category != nil {
		cat := models.FurnitureCategory(string(*params.Category))
		category = &cat
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	items, total, err := h.service.ListFurnitureItems(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list furniture items")
		h.respondError(w, http.StatusInternalServerError, "failed to get furniture catalog")
		return
	}

	apiItems := make([]api.FurnitureItem, len(items))
	for i, item := range items {
		cat := api.FurnitureItemCategory(item.Category)
		prestigePoints := item.PrestigeValue
		apiItems[i] = api.FurnitureItem{
			Id:             &item.ID,
			Category:       &cat,
			Name:           &item.Name,
			Description:    &item.Description,
			Price:          &item.Price,
			PrestigePoints: &prestigePoints,
			FunctionBonus:  &item.FunctionBonus,
			CreatedAt:      &item.CreatedAt,
		}
	}

	response := api.FurnitureCatalogResponse{
		Furniture: &apiItems,
		Total:     &total,
		Limit:     &limit,
		Offset:    &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

