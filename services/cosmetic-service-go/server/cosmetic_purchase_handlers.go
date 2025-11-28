package server

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/necpgame/cosmetic-service-go/models"
)

func (s *HTTPServer) purchaseCosmetic(w http.ResponseWriter, r *http.Request) {
	var req models.PurchaseCosmeticRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerCosmetic, err := s.purchaseService.PurchaseCosmetic(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to purchase cosmetic")
		s.respondError(w, http.StatusInternalServerError, "failed to purchase cosmetic")
		return
	}

	s.respondJSON(w, http.StatusOK, playerCosmetic)
}

func (s *HTTPServer) getPurchaseHistory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID := vars["player_id"]

	limit := parseIntQuery(r, "limit", 50)
	offset := parseIntQuery(r, "offset", 0)

	history, err := s.purchaseService.GetPurchaseHistory(r.Context(), playerID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get purchase history")
		s.respondError(w, http.StatusInternalServerError, "failed to get purchase history")
		return
	}

	s.respondJSON(w, http.StatusOK, history)
}

