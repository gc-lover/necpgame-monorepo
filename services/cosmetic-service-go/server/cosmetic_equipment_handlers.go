package server

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/necpgame/cosmetic-service-go/models"
)

func (s *HTTPServer) equipCosmetic(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	cosmeticID := vars["cosmetic_id"]

	var req models.EquipCosmeticRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	equipped, err := s.equipmentService.EquipCosmetic(r.Context(), cosmeticID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to equip cosmetic")
		s.respondError(w, http.StatusInternalServerError, "failed to equip cosmetic")
		return
	}

	s.respondJSON(w, http.StatusOK, equipped)
}

func (s *HTTPServer) unequipCosmetic(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	cosmeticID := vars["cosmetic_id"]

	var req models.UnequipCosmeticRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err := s.equipmentService.UnequipCosmetic(r.Context(), cosmeticID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to unequip cosmetic")
		s.respondError(w, http.StatusInternalServerError, "failed to unequip cosmetic")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "unequipped"})
}

func (s *HTTPServer) getEquippedCosmetics(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	equipped, err := s.equipmentService.GetEquippedCosmetics(r.Context(), playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get equipped cosmetics")
		s.respondError(w, http.StatusInternalServerError, "failed to get equipped cosmetics")
		return
	}

	if equipped == nil {
		s.respondError(w, http.StatusNotFound, "equipped cosmetics not found")
		return
	}

	s.respondJSON(w, http.StatusOK, equipped)
}

