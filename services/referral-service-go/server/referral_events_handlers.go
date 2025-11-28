package server

import (
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/referral-service-go/models"
)

func (s *HTTPServer) getEvents(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var eventType *models.ReferralEventType
	if eventTypeStr := r.URL.Query().Get("event_type"); eventTypeStr != "" {
		et := models.ReferralEventType(eventTypeStr)
		eventType = &et
	}

	limit := 20
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	events, total, err := s.referralService.GetEvents(r.Context(), playerID, eventType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get events")
		s.respondError(w, http.StatusInternalServerError, "failed to get events")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"events": events,
		"total":  total,
		"limit":  limit,
		"offset": offset,
	})
}

