// Issue: #141886468
package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type TimeTrialHandlers struct {
	service TimeTrialServiceInterface
	logger  *logrus.Logger
}

func NewTimeTrialHandlers(service TimeTrialServiceInterface) *TimeTrialHandlers {
	return &TimeTrialHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *TimeTrialHandlers) StartTimeTrial(w http.ResponseWriter, r *http.Request) {
	playerID := h.getPlayerID(r)
	if playerID == uuid.Nil {
		h.respondError(w, http.StatusUnauthorized, "unauthorized")
		return
	}

	var req models.StartTimeTrialRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	session, err := h.service.StartTimeTrial(r.Context(), playerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to start time trial")
		h.respondError(w, http.StatusInternalServerError, "failed to start time trial")
		return
	}

	h.respondJSON(w, http.StatusCreated, session)
}

func (h *TimeTrialHandlers) CompleteTimeTrial(w http.ResponseWriter, r *http.Request) {
	playerID := h.getPlayerID(r)
	if playerID == uuid.Nil {
		h.respondError(w, http.StatusUnauthorized, "unauthorized")
		return
	}

	var req models.CompleteTimeTrialRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := h.service.CompleteTimeTrial(r.Context(), playerID, &req)
	if err != nil {
		if err.Error() == "session not found" {
			h.respondError(w, http.StatusNotFound, "session not found")
			return
		}
		if err.Error() == "session does not belong to player" {
			h.respondError(w, http.StatusForbidden, "session does not belong to player")
			return
		}
		if err.Error() == "session is not in progress" {
			h.respondError(w, http.StatusBadRequest, "session is not in progress")
			return
		}
		h.logger.WithError(err).Error("Failed to complete time trial")
		h.respondError(w, http.StatusInternalServerError, "failed to complete time trial")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *TimeTrialHandlers) GetTimeTrialSession(w http.ResponseWriter, r *http.Request) {
	playerID := h.getPlayerID(r)
	if playerID == uuid.Nil {
		h.respondError(w, http.StatusUnauthorized, "unauthorized")
		return
	}

	vars := mux.Vars(r)
	sessionID, err := uuid.Parse(vars["session_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid session_id")
		return
	}

	session, err := h.service.GetTimeTrialSession(r.Context(), sessionID, playerID)
	if err != nil {
		if err.Error() == "session not found" {
			h.respondError(w, http.StatusNotFound, "session not found")
			return
		}
		if err.Error() == "session does not belong to player" {
			h.respondError(w, http.StatusForbidden, "session does not belong to player")
			return
		}
		h.logger.WithError(err).Error("Failed to get time trial session")
		h.respondError(w, http.StatusInternalServerError, "failed to get time trial session")
		return
	}

	h.respondJSON(w, http.StatusOK, session)
}

func (h *TimeTrialHandlers) GetCurrentWeeklyChallenge(w http.ResponseWriter, r *http.Request) {
	challenge, err := h.service.GetCurrentWeeklyChallenge(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get current weekly challenge")
		h.respondError(w, http.StatusInternalServerError, "failed to get current weekly challenge")
		return
	}

	if challenge == nil {
		h.respondJSON(w, http.StatusOK, nil)
		return
	}

	h.respondJSON(w, http.StatusOK, challenge)
}

func (h *TimeTrialHandlers) GetWeeklyChallengeHistory(w http.ResponseWriter, r *http.Request) {
	weeksBack := 4
	if weeksBackStr := r.URL.Query().Get("weeks_back"); weeksBackStr != "" {
		if wb, err := strconv.Atoi(weeksBackStr); err == nil && wb >= 1 && wb <= 52 {
			weeksBack = wb
		}
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

	response, err := h.service.GetWeeklyChallengeHistory(r.Context(), weeksBack, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get weekly challenge history")
		h.respondError(w, http.StatusInternalServerError, "failed to get weekly challenge history")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *TimeTrialHandlers) getPlayerID(r *http.Request) uuid.UUID {
	playerIDStr := r.Header.Get("X-Player-ID")
	if playerIDStr == "" {
		return uuid.Nil
	}
	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		return uuid.Nil
	}
	return playerID
}

func (h *TimeTrialHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *TimeTrialHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

