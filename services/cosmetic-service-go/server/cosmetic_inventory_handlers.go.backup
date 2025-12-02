package server

import (
	"net/http"

	"github.com/gorilla/mux"
)

func (s *HTTPServer) getCosmeticsByRarity(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	rarity := vars["rarity"]

	category := r.URL.Query().Get("category")
	limit := parseIntQuery(r, "limit", 50)
	offset := parseIntQuery(r, "offset", 0)

	var categoryPtr *string
	if category != "" {
		categoryPtr = &category
	}

	catalog, err := s.inventoryService.GetCosmeticsByRarity(r.Context(), rarity, categoryPtr, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get cosmetics by rarity")
		s.respondError(w, http.StatusInternalServerError, "failed to get cosmetics by rarity")
		return
	}

	s.respondJSON(w, http.StatusOK, catalog)
}

func (s *HTTPServer) getCosmeticInventory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	category := r.URL.Query().Get("category")
	rarity := r.URL.Query().Get("rarity")
	limit := parseIntQuery(r, "limit", 50)
	offset := parseIntQuery(r, "offset", 0)

	var categoryPtr *string
	if category != "" {
		categoryPtr = &category
	}

	var rarityPtr *string
	if rarity != "" {
		rarityPtr = &rarity
	}

	inventory, err := s.inventoryService.GetInventory(r.Context(), playerID, categoryPtr, rarityPtr, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get inventory")
		s.respondError(w, http.StatusInternalServerError, "failed to get inventory")
		return
	}

	s.respondJSON(w, http.StatusOK, inventory)
}

func (s *HTTPServer) checkCosmeticOwnership(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]
	cosmeticID := r.URL.Query().Get("cosmetic_id")

	if cosmeticID == "" {
		s.respondError(w, http.StatusBadRequest, "cosmetic_id is required")
		return
	}

	status, err := s.inventoryService.CheckOwnership(r.Context(), playerID, cosmeticID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to check ownership")
		s.respondError(w, http.StatusInternalServerError, "failed to check ownership")
		return
	}

	s.respondJSON(w, http.StatusOK, status)
}

func (s *HTTPServer) getCosmeticEvents(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	eventType := r.URL.Query().Get("event_type")
	limit := parseIntQuery(r, "limit", 50)
	offset := parseIntQuery(r, "offset", 0)

	var eventTypePtr *string
	if eventType != "" {
		eventTypePtr = &eventType
	}

	events, err := s.inventoryService.GetEvents(r.Context(), playerID, eventTypePtr, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get events")
		s.respondError(w, http.StatusInternalServerError, "failed to get events")
		return
	}

	s.respondJSON(w, http.StatusOK, events)
}

