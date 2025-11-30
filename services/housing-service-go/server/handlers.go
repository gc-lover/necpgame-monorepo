package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/models"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type HousingHandlers struct {
	service HousingServiceInterface
	logger  *logrus.Logger
}

func NewHousingHandlers(service HousingServiceInterface) *HousingHandlers {
	return &HousingHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *HousingHandlers) GetAvailableApartments(w http.ResponseWriter, r *http.Request, params api.GetAvailableApartmentsParams) {
	ctx := r.Context()

	var ownerID *uuid.UUID
	if params.OwnerId != nil {
		id := uuid.UUID(*params.OwnerId)
		ownerID = &id
	}

	var ownerType *string
	if params.OwnerType != nil {
		ownerType = params.OwnerType
	}

	var isPublic *bool
	if params.IsPublic != nil {
		isPublic = params.IsPublic
	}

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	apartments, total, err := h.service.ListApartments(ctx, ownerID, ownerType, isPublic, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list apartments")
		h.respondError(w, http.StatusInternalServerError, "failed to list apartments")
		return
	}

	apiApartments := make([]api.Apartment, len(apartments))
	for i, apt := range apartments {
		apiApartments[i] = toAPIApartment(&apt)
	}

	response := map[string]interface{}{
		"apartments": apiApartments,
		"total":      total,
		"limit":      limit,
		"offset":     offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) PurchaseApartment(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.PurchaseApartmentJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	modelReq := &models.PurchaseApartmentRequest{
		CharacterID:   characterID,
		ApartmentType: models.ApartmentType(req.ApartmentType),
		Location:      req.Location,
	}

	apartment, err := h.service.PurchaseApartment(ctx, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to purchase apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to purchase apartment")
		return
	}

	h.respondJSON(w, http.StatusCreated, toAPIApartment(apartment))
}

func (h *HousingHandlers) GetApartment(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	if apartment == nil {
		h.respondError(w, http.StatusNotFound, "apartment not found")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIApartment(apartment))
}

func (h *HousingHandlers) UpdateApartment(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	h.respondError(w, http.StatusNotImplemented, "UpdateApartment not implemented")
}

func (h *HousingHandlers) GetPlacedFurniture(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	detail, err := h.service.GetApartmentDetail(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get placed furniture")
		h.respondError(w, http.StatusInternalServerError, "failed to get placed furniture")
		return
	}

	apiFurniture := make([]api.PlacedFurniture, len(detail.Furniture))
	for i, f := range detail.Furniture {
		apiFurniture[i] = toAPIPlacedFurniture(&f)
	}

	response := map[string]interface{}{
		"furniture": apiFurniture,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) PlaceFurniture(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.PlaceFurnitureJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	modelReq := &models.PlaceFurnitureRequest{
		CharacterID:     characterID,
		FurnitureItemID: req.FurnitureItemId,
		Position: map[string]interface{}{
			"x": req.PositionX,
			"y": req.PositionY,
			"z": req.PositionZ,
		},
		Rotation: map[string]interface{}{
			"yaw": *req.RotationYaw,
		},
		Scale: map[string]interface{}{
			"uniform": *req.Scale,
		},
	}

	furniture, err := h.service.PlaceFurniture(ctx, apartmentID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to place furniture")
		h.respondError(w, http.StatusInternalServerError, "failed to place furniture")
		return
	}

	h.respondJSON(w, http.StatusCreated, toAPIPlacedFurniture(furniture))
}

func (h *HousingHandlers) RemoveFurniture(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID, furnitureId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)
	furnitureID := uuid.UUID(furnitureId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	err = h.service.RemoveFurniture(ctx, apartmentID, furnitureID, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to remove furniture")
		h.respondError(w, http.StatusInternalServerError, "failed to remove furniture")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *HousingHandlers) UpdateApartmentSettings(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	characterID, err := h.getCharacterID(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "invalid character ID")
		return
	}

	var req api.UpdateApartmentSettingsJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	isPublic := req.AccessSetting == api.UpdateApartmentSettingsRequestAccessSettingPUBLIC

	modelReq := &models.UpdateApartmentSettingsRequest{
		CharacterID: characterID,
		IsPublic:    &isPublic,
		Guests:      []uuid.UUID{},
	}

	if req.Guests != nil {
		for _, g := range *req.Guests {
			modelReq.Guests = append(modelReq.Guests, uuid.UUID(g))
		}
	}

	err = h.service.UpdateApartmentSettings(ctx, apartmentID, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update apartment settings")
		h.respondError(w, http.StatusInternalServerError, "failed to update apartment settings")
		return
	}

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIApartment(apartment))
}

func (h *HousingHandlers) VisitApartment(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	var req api.VisitApartmentJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	visitorID := uuid.UUID(req.VisitorId)
	modelReq := &models.VisitApartmentRequest{
		CharacterID: visitorID,
		ApartmentID: apartmentID,
	}

	err := h.service.VisitApartment(ctx, modelReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to visit apartment")
		h.respondError(w, http.StatusInternalServerError, "failed to visit apartment")
		return
	}

	apartment, err := h.service.GetApartment(ctx, apartmentID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIApartment(apartment))
}

func (h *HousingHandlers) GetPrestigeLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetPrestigeLeaderboardParams) {
	ctx := r.Context()

	limit := 20
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, total, err := h.service.GetPrestigeLeaderboard(ctx, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get prestige leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get prestige leaderboard")
		return
	}

	apiEntries := make([]api.PrestigeEntry, len(entries))
	for i, e := range entries {
		apiEntries[i] = toAPIPrestigeEntry(&e)
	}

	response := map[string]interface{}{
		"entries": apiEntries,
		"total":   total,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Issue: #141886468
func (h *HousingHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *HousingHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := map[string]string{
		"error": message,
	}
	h.respondJSON(w, status, errorResponse)
}

func (h *HousingHandlers) getCharacterID(r *http.Request) (uuid.UUID, error) {
	claims, ok := r.Context().Value("claims").(*Claims)
	if !ok || claims == nil {
		return uuid.Nil, nil
	}

	characterID, err := uuid.Parse(claims.Subject)
	if err != nil {
		return uuid.Nil, err
	}

	return characterID, nil
}
