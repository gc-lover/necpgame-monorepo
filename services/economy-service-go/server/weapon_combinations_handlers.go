// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/economy-service-go/pkg/weaponcombinationsapi"
	"github.com/sirupsen/logrus"
)

type WeaponCombinationsHandlers struct {
	service WeaponCombinationsServiceInterface
	logger  *logrus.Logger
}

func NewWeaponCombinationsHandlers(service WeaponCombinationsServiceInterface) *WeaponCombinationsHandlers {
	return &WeaponCombinationsHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *WeaponCombinationsHandlers) GenerateWeaponCombination(w http.ResponseWriter, r *http.Request) {
	var req weaponcombinationsapi.GenerateWeaponCombinationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	// WeaponType - это строка, нужно преобразовать в UUID
	baseWeaponTypeUUID := uuid.New() // TODO: преобразовать строку в UUID через lookup
	brandID := uuid.New()
	if req.BrandId != nil {
		brandID = uuid.UUID(*req.BrandId)
	}

	rarity := "common"
	if req.Rarity != nil {
		rarity = string(*req.Rarity)
	}

	var playerLevel *int
	weaponID, result, err := h.service.GenerateWeaponCombination(
		r.Context(),
		baseWeaponTypeUUID,
		brandID,
		rarity,
		req.Seed,
		playerLevel,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to generate weapon combination")
		h.respondError(w, http.StatusInternalServerError, "failed to generate weapon combination")
		return
	}

	response := weaponcombinationsapi.WeaponCombination{
		Id: (*openapi_types.UUID)(&weaponID),
	}
	if name, ok := result["name"].(string); ok {
		response.Name = &name
	}
	if rarity, ok := result["rarity"].(string); ok {
		response.Rarity = &rarity
	}
	if brandID, ok := result["brand_id"].(string); ok {
		if brandUUID, err := uuid.Parse(brandID); err == nil {
			response.BrandId = (*openapi_types.UUID)(&brandUUID)
		}
	}

	h.respondJSON(w, http.StatusCreated, response)
}

func (h *WeaponCombinationsHandlers) GetWeaponCombinationMatrix(w http.ResponseWriter, r *http.Request, params weaponcombinationsapi.GetWeaponCombinationMatrixParams) {
	matrix, err := h.service.GetWeaponCombinationMatrix(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get weapon combination matrix")
		h.respondError(w, http.StatusInternalServerError, "failed to get weapon combination matrix")
		return
	}

	response := weaponcombinationsapi.WeaponCombinationMatrixResponse{
		CompatibilityMatrix: &matrix,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCombinationsHandlers) GetWeaponModifiers(w http.ResponseWriter, r *http.Request, params weaponcombinationsapi.GetWeaponModifiersParams) {
	modifiers, err := h.service.GetWeaponModifiers(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get weapon modifiers")
		h.respondError(w, http.StatusInternalServerError, "failed to get weapon modifiers")
		return
	}

	modifierList := make([]weaponcombinationsapi.WeaponModifier, 0, len(modifiers))
	for _, m := range modifiers {
		modifier := weaponcombinationsapi.WeaponModifier{}
		if id, ok := m["id"].(string); ok {
			if idUUID, err := uuid.Parse(id); err == nil {
				modifier.Id = (*openapi_types.UUID)(&idUUID)
			}
		}
		if name, ok := m["name"].(string); ok {
			modifier.Name = &name
		}
		if modifierType, ok := m["type"].(string); ok {
			modifier.Type = &modifierType
		}
		modifierList = append(modifierList, modifier)
	}

	response := weaponcombinationsapi.WeaponModifiersResponse{
		Modifiers: &modifierList,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCombinationsHandlers) ApplyWeaponModifier(w http.ResponseWriter, r *http.Request) {
	var req weaponcombinationsapi.ApplyWeaponModifierRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	var characterID *uuid.UUID
	if req.CharacterId != nil {
		cid := uuid.UUID(*req.CharacterId)
		characterID = &cid
	}

	result, err := h.service.ApplyWeaponModifier(
		r.Context(),
		uuid.UUID(req.WeaponId),
		uuid.UUID(req.ModifierId),
		string(req.ModifierType),
		characterID,
	)
	if err != nil {
		h.logger.WithError(err).Error("Failed to apply weapon modifier")
		h.respondError(w, http.StatusInternalServerError, "failed to apply weapon modifier")
		return
	}

	response := weaponcombinationsapi.ApplyWeaponModifierResponse{
		WeaponId:   &req.WeaponId,
		ModifierId: &req.ModifierId,
		Result:     &result,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCombinationsHandlers) GetCorporations(w http.ResponseWriter, r *http.Request, params weaponcombinationsapi.GetCorporationsParams) {
	corporations, err := h.service.GetCorporations(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get corporations")
		h.respondError(w, http.StatusInternalServerError, "failed to get corporations")
		return
	}

	corpList := make([]weaponcombinationsapi.Corporation, 0, len(corporations))
	for _, c := range corporations {
		corp := weaponcombinationsapi.Corporation{}
		if id, ok := c["id"].(string); ok {
			if idUUID, err := uuid.Parse(id); err == nil {
				corp.Id = (*openapi_types.UUID)(&idUUID)
			}
		}
		if name, ok := c["name"].(string); ok {
			corp.Name = &name
		}
		if specialization, ok := c["specialization"].(string); ok {
			corp.Specialization = &specialization
		}
		corpList = append(corpList, corp)
	}

	response := weaponcombinationsapi.CorporationsResponse{
		Corporations: &corpList,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *WeaponCombinationsHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *WeaponCombinationsHandlers) respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(map[string]string{"error": message}); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}

