// Issue: #141886468
package server

import (
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/housing-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func (h *HousingHandlers) GetApartmentBonuses(w http.ResponseWriter, r *http.Request, apartmentId openapi_types.UUID) {
	ctx := r.Context()
	apartmentID := uuid.UUID(apartmentId)

	detail, err := h.service.GetApartmentDetail(ctx, apartmentID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get apartment detail")
		h.respondError(w, http.StatusInternalServerError, "failed to get apartment bonuses")
		return
	}

	bonuses := make(map[string]interface{})
	if detail.FunctionalBonuses != nil {
		bonuses = detail.FunctionalBonuses
	}

	response := api.ApartmentBonusesResponse{
		ApartmentId: &apartmentId,
		Bonuses: &struct {
			CRAFTSPEED           *float32 `json:"CRAFT_SPEED,omitempty"`
			HUMANITYREGENERATION *float32 `json:"HUMANITY_REGENERATION,omitempty"`
			STORAGESLOTS         *int     `json:"STORAGE_SLOTS,omitempty"`
			WEAPONSTORAGE        *int     `json:"WEAPON_STORAGE,omitempty"`
		}{},
	}

	if craftSpeed, ok := bonuses["CRAFT_SPEED"].(float64); ok {
		cs := float32(craftSpeed)
		response.Bonuses.CRAFTSPEED = &cs
	}
	if humanityRegen, ok := bonuses["HUMANITY_REGENERATION"].(float64); ok {
		hr := float32(humanityRegen)
		response.Bonuses.HUMANITYREGENERATION = &hr
	}
	if storageSlots, ok := bonuses["STORAGE_SLOTS"].(float64); ok {
		ss := int(storageSlots)
		response.Bonuses.STORAGESLOTS = &ss
	}
	if weaponStorage, ok := bonuses["WEAPON_STORAGE"].(float64); ok {
		ws := int(weaponStorage)
		response.Bonuses.WEAPONSTORAGE = &ws
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HousingHandlers) GetPlayerBonuses(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	// Получаем все квартиры игрока
	ownerID := &playerID
	ownerType := "character"
	apartments, _, err := h.service.ListApartments(ctx, ownerID, &ownerType, nil, 100, 0)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list apartments")
		h.respondError(w, http.StatusInternalServerError, "failed to get player bonuses")
		return
	}

	// Агрегируем бонусы из всех квартир
	aggregatedBonuses := make(map[string]interface{})
	for _, apt := range apartments {
		detail, err := h.service.GetApartmentDetail(ctx, apt.ID)
		if err != nil {
			h.logger.WithError(err).WithField("apartment_id", apt.ID).Warn("Failed to get apartment detail for bonuses")
			continue
		}

		if detail.FunctionalBonuses != nil {
			for key, value := range detail.FunctionalBonuses {
				if existing, ok := aggregatedBonuses[key]; ok {
					if num, ok := existing.(float64); ok {
						if val, ok := value.(float64); ok {
							aggregatedBonuses[key] = num + val
						}
					}
				} else {
					aggregatedBonuses[key] = value
				}
			}
		}
	}

	response := api.PlayerBonusesResponse{
		PlayerId: &playerId,
		Bonuses: &struct {
			CRAFTSPEED           *float32 `json:"CRAFT_SPEED,omitempty"`
			HUMANITYREGENERATION *float32 `json:"HUMANITY_REGENERATION,omitempty"`
			STORAGESLOTS         *int     `json:"STORAGE_SLOTS,omitempty"`
			WEAPONSTORAGE        *int     `json:"WEAPON_STORAGE,omitempty"`
		}{},
	}

	if craftSpeed, ok := aggregatedBonuses["CRAFT_SPEED"].(float64); ok {
		cs := float32(craftSpeed)
		response.Bonuses.CRAFTSPEED = &cs
	}
	if humanityRegen, ok := aggregatedBonuses["HUMANITY_REGENERATION"].(float64); ok {
		hr := float32(humanityRegen)
		response.Bonuses.HUMANITYREGENERATION = &hr
	}
	if storageSlots, ok := aggregatedBonuses["STORAGE_SLOTS"].(float64); ok {
		ss := int(storageSlots)
		response.Bonuses.STORAGESLOTS = &ss
	}
	if weaponStorage, ok := aggregatedBonuses["WEAPON_STORAGE"].(float64); ok {
		ws := int(weaponStorage)
		response.Bonuses.WEAPONSTORAGE = &ws
	}

	h.respondJSON(w, http.StatusOK, response)
}

