package server

import (
	"net/http"
)

func (s *HTTPServer) getDailyShop(w http.ResponseWriter, r *http.Request) {
	dailyShop, err := s.shopService.GetDailyShop(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get daily shop")
		s.respondError(w, http.StatusInternalServerError, "failed to get daily shop")
		return
	}

	s.respondJSON(w, http.StatusOK, dailyShop)
}

func (s *HTTPServer) getShopHistory(w http.ResponseWriter, r *http.Request) {
	limit := parseIntQuery(r, "limit", 50)
	offset := parseIntQuery(r, "offset", 0)

	history, err := s.shopService.GetShopHistory(r.Context(), limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get shop history")
		s.respondError(w, http.StatusInternalServerError, "failed to get shop history")
		return
	}

	s.respondJSON(w, http.StatusOK, history)
}

