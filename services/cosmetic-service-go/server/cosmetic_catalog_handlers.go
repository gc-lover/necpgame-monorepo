package server

import (
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/necpgame/cosmetic-service-go/models"
	"github.com/sirupsen/logrus"
)

func (s *HTTPServer) getCosmeticCatalog(w http.ResponseWriter, r *http.Request) {
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

	catalog, err := s.catalogService.GetCatalog(r.Context(), categoryPtr, rarityPtr, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get catalog")
		s.respondError(w, http.StatusInternalServerError, "failed to get catalog")
		return
	}

	s.respondJSON(w, http.StatusOK, catalog)
}

func (s *HTTPServer) getCosmeticCategories(w http.ResponseWriter, r *http.Request) {
	categories, err := s.catalogService.GetCategories(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get categories")
		s.respondError(w, http.StatusInternalServerError, "failed to get categories")
		return
	}

	s.respondJSON(w, http.StatusOK, categories)
}

func (s *HTTPServer) getCosmeticDetails(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	cosmeticID := vars["cosmetic_id"]

	cosmetic, err := s.catalogService.GetCosmeticByID(r.Context(), cosmeticID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get cosmetic details")
		s.respondError(w, http.StatusInternalServerError, "failed to get cosmetic details")
		return
	}

	if cosmetic == nil {
		s.respondError(w, http.StatusNotFound, "cosmetic not found")
		return
	}

	s.respondJSON(w, http.StatusOK, cosmetic)
}

func parseIntQuery(r *http.Request, key string, defaultValue int) int {
	value := r.URL.Query().Get(key)
	if value == "" {
		return defaultValue
	}

	var intValue int
	if _, err := fmt.Sscanf(value, "%d", &intValue); err != nil {
		return defaultValue
	}

	return intValue
}

