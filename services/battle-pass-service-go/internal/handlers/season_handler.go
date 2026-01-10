package handlers

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"

	"go.uber.org/zap"

	"battle-pass-service-go/internal/models"
	"battle-pass-service-go/internal/services"
)

// SeasonHandler handles season-related HTTP requests
type SeasonHandler struct {
	seasonService *services.SeasonService
	logger        *zap.Logger
}

// NewSeasonHandler creates a new SeasonHandler
func NewSeasonHandler(seasonService *services.SeasonService, logger *zap.Logger) *SeasonHandler {
	return &SeasonHandler{
		seasonService: seasonService,
		logger:        logger,
	}
}

// ListSeasons handles GET /seasons
func (h *SeasonHandler) ListSeasons(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Parse query parameters
	limit := 50 // default
	offset := 0 // default

	if limitParam := r.URL.Query().Get("limit"); limitParam != "" {
		if l, err := strconv.Atoi(limitParam); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	if offsetParam := r.URL.Query().Get("offset"); offsetParam != "" {
		if o, err := strconv.Atoi(offsetParam); err == nil && o >= 0 {
			offset = o
		}
	}

	seasons, err := h.seasonService.ListSeasons(limit, offset)
	if err != nil {
		h.logger.Error("Failed to list seasons", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"seasons": seasons,
		"limit":   limit,
		"offset":  offset,
	})
}

// GetSeason handles GET /seasons/{id}
func (h *SeasonHandler) GetSeason(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Extract season ID from URL path
	pathParts := strings.Split(strings.Trim(r.URL.Path, "/"), "/")
	if len(pathParts) < 2 {
		http.Error(w, "Invalid URL path", http.StatusBadRequest)
		return
	}

	seasonID := pathParts[1] // seasons/{id}

	season, err := h.seasonService.GetSeason(seasonID)
	if err != nil {
		h.logger.Error("Failed to get season", zap.String("seasonID", seasonID), zap.Error(err))
		if strings.Contains(err.Error(), "not found") {
			http.Error(w, "Season not found", http.StatusNotFound)
		} else {
			http.Error(w, "Internal server error", http.StatusInternalServerError)
		}
		return
	}

	h.respondJSON(w, http.StatusOK, season)
}

// respondJSON sends a JSON response
func (h *SeasonHandler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}