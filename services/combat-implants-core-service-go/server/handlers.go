// Issue: #58
package server

import (
	"encoding/json"
	"net/http"

	"github.com/necpgame/combat-implants-core-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type CoreHandlers struct {
	logger *logrus.Logger
}

func NewCoreHandlers() *CoreHandlers {
	return &CoreHandlers{
		logger: GetLogger(),
	}
}

// GetImplantCatalog - GET /gameplay/combat/implants/catalog
func (h *CoreHandlers) GetImplantCatalog(w http.ResponseWriter, r *http.Request, params api.GetImplantCatalogParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetImplantCatalog request")

	implants := []api.ImplantCatalogItem{}
	page := 1
	pageSize := 20
	total := 0

	response := api.ImplantCatalogResponse{
		Implants: &implants,
		Pagination: &struct {
			Page     *int `json:"page,omitempty"`
			PageSize *int `json:"page_size,omitempty"`
			Total    *int `json:"total,omitempty"`
		}{
			Page:     &page,
			PageSize: &pageSize,
			Total:    &total,
		},
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetImplantById - GET /gameplay/combat/implants/{implant_id}
func (h *CoreHandlers) GetImplantById(w http.ResponseWriter, r *http.Request, implantId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("implant_id", implantId).Info("GetImplantById request")

	name := "Mantis Blades"
	implantType := api.ImplantCatalogItemTypeCombat
	category := "offensive"
	rarity := api.ImplantCatalogItemRarityEpic
	description := "Retractable blades for close combat"
	slotType := "arms"

	response := api.ImplantCatalogItem{
		Id:          &implantId,
		Name:        &name,
		Type:        &implantType,
		Category:    &category,
		Rarity:      &rarity,
		Description: &description,
		SlotType:    &slotType,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetCharacterImplants - GET /gameplay/combat/implants/character/{character_id}
func (h *CoreHandlers) GetCharacterImplants(w http.ResponseWriter, r *http.Request, characterId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("character_id", characterId).Info("GetCharacterImplants request")

	implants := []struct {
		Id           *openapi_types.UUID `json:"id,omitempty"`
		ImplantId    *openapi_types.UUID `json:"implant_id,omitempty"`
		InstalledAt  *string             `json:"installed_at,omitempty"`
		IsActive     *bool               `json:"is_active,omitempty"`
		Slot         *string             `json:"slot,omitempty"`
		UpgradeLevel *int                `json:"upgrade_level,omitempty"`
	}{}

	response := api.CharacterImplantsResponse{
		Implants: &implants,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// InstallImplant - POST /gameplay/combat/implants/install
func (h *CoreHandlers) InstallImplant(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.InstallImplantJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"character_id": req.CharacterId,
		"implant_id":   req.ImplantId,
		"slot":         req.Slot,
	}).Info("InstallImplant request")

	success := true
	message := "Implant installed successfully"

	response := struct {
		Message *string `json:"message,omitempty"`
		Success *bool   `json:"success,omitempty"`
	}{
		Success: &success,
		Message: &message,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// UninstallImplant - POST /gameplay/combat/implants/uninstall
func (h *CoreHandlers) UninstallImplant(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	var req api.UninstallImplantJSONRequestBody
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	h.logger.WithFields(logrus.Fields{
		"character_id": req.CharacterId,
		"slot":         req.Slot,
	}).Info("UninstallImplant request")

	success := true
	message := "Implant uninstalled successfully"

	response := struct {
		Message *string `json:"message,omitempty"`
		Success *bool   `json:"success,omitempty"`
	}{
		Success: &success,
		Message: &message,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetImplantSlots - GET /gameplay/combat/implants/slots
func (h *CoreHandlers) GetImplantSlots(w http.ResponseWriter, r *http.Request, params api.GetImplantSlotsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("character_id", params.CharacterId).Info("GetImplantSlots request")

	slots := []struct {
		Available *bool   `json:"available,omitempty"`
		SlotType  *string `json:"slot_type,omitempty"`
		Slot      *string `json:"slot,omitempty"`
	}{
		{Slot: stringPtr("arms"), SlotType: stringPtr("combat"), Available: boolPtr(true)},
		{Slot: stringPtr("eyes"), SlotType: stringPtr("visual"), Available: boolPtr(true)},
		{Slot: stringPtr("os"), SlotType: stringPtr("os"), Available: boolPtr(true)},
	}

	response := api.ImplantSlotsResponse{
		Slots: &slots,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CoreHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *CoreHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

func stringPtr(s string) *string {
	return &s
}

func boolPtr(b bool) *bool {
	return &b
}

