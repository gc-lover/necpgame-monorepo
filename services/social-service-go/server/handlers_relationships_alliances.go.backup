package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type RelationshipAllianceHandlers struct {
	service RelationshipServiceInterface
	logger  *logrus.Logger
}

func NewRelationshipAllianceHandlers(service RelationshipServiceInterface) *RelationshipAllianceHandlers {
	return &RelationshipAllianceHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *RelationshipAllianceHandlers) CreateAlliance(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.CreateAllianceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	alliance, err := h.service.CreateAlliance(r.Context(), leaderID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create alliance")
		h.respondError(w, http.StatusInternalServerError, "failed to create alliance")
		return
	}

	h.respondJSON(w, http.StatusCreated, alliance)
}

func (h *RelationshipAllianceHandlers) GetAlliances(w http.ResponseWriter, r *http.Request) {
	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.GetAlliances(r.Context(), limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get alliances")
		h.respondError(w, http.StatusInternalServerError, "failed to get alliances")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *RelationshipAllianceHandlers) GetAlliance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	allianceID, err := uuid.Parse(vars["alliance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid alliance_id")
		return
	}

	alliance, err := h.service.GetAlliance(r.Context(), allianceID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get alliance")
		h.respondError(w, http.StatusInternalServerError, "failed to get alliance")
		return
	}

	if alliance == nil {
		h.respondError(w, http.StatusNotFound, "alliance not found")
		return
	}

	h.respondJSON(w, http.StatusOK, alliance)
}

func (h *RelationshipAllianceHandlers) TerminateAlliance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	allianceID, err := uuid.Parse(vars["alliance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid alliance_id")
		return
	}

	err = h.service.TerminateAlliance(r.Context(), allianceID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to terminate alliance")
		h.respondError(w, http.StatusInternalServerError, "failed to terminate alliance")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "terminated"})
}

func (h *RelationshipAllianceHandlers) InviteToAlliance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	allianceID, err := uuid.Parse(vars["alliance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid alliance_id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	inviterID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.AllianceInviteRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err = h.service.InviteToAlliance(r.Context(), allianceID, inviterID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to invite to alliance")
		h.respondError(w, http.StatusInternalServerError, "failed to invite to alliance")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "invited"})
}

func (h *RelationshipAllianceHandlers) JoinAlliance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	allianceID, err := uuid.Parse(vars["alliance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid alliance_id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = h.service.JoinAlliance(r.Context(), allianceID, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to join alliance")
		h.respondError(w, http.StatusInternalServerError, "failed to join alliance")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "joined"})
}

func (h *RelationshipAllianceHandlers) LeaveAlliance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	allianceID, err := uuid.Parse(vars["alliance_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid alliance_id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = h.service.LeaveAlliance(r.Context(), allianceID, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to leave alliance")
		h.respondError(w, http.StatusInternalServerError, "failed to leave alliance")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "left"})
}

func (h *RelationshipAllianceHandlers) GetPlayerRatings(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.GetPlayerRatings(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player ratings")
		h.respondError(w, http.StatusInternalServerError, "failed to get player ratings")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *RelationshipAllianceHandlers) UpdateRating(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var req models.UpdateRatingRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	rating, err := h.service.UpdateRating(r.Context(), playerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update rating")
		h.respondError(w, http.StatusInternalServerError, "failed to update rating")
		return
	}

	h.respondJSON(w, http.StatusOK, rating)
}

func (h *RelationshipAllianceHandlers) GetSocialCapital(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	capital, err := h.service.GetSocialCapital(r.Context(), playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get social capital")
		h.respondError(w, http.StatusInternalServerError, "failed to get social capital")
		return
	}

	if capital == nil {
		h.respondError(w, http.StatusNotFound, "social capital not found")
		return
	}

	h.respondJSON(w, http.StatusOK, capital)
}

func (h *RelationshipAllianceHandlers) GetInteractionHistory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = l
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := h.service.GetInteractionHistory(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get interaction history")
		h.respondError(w, http.StatusInternalServerError, "failed to get interaction history")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *RelationshipAllianceHandlers) RequestArbitration(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	requesterID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.RequestArbitrationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	case_, err := h.service.RequestArbitration(r.Context(), requesterID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to request arbitration")
		h.respondError(w, http.StatusInternalServerError, "failed to request arbitration")
		return
	}

	h.respondJSON(w, http.StatusCreated, case_)
}

func (h *RelationshipAllianceHandlers) GetArbitrationCase(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	caseID, err := uuid.Parse(vars["case_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid case_id")
		return
	}

	case_, err := h.service.GetArbitrationCase(r.Context(), caseID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get arbitration case")
		h.respondError(w, http.StatusInternalServerError, "failed to get arbitration case")
		return
	}

	if case_ == nil {
		h.respondError(w, http.StatusNotFound, "arbitration case not found")
		return
	}

	h.respondJSON(w, http.StatusOK, case_)
}

func (h *RelationshipAllianceHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	var buf bytes.Buffer
	if err := json.NewEncoder(&buf).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *RelationshipAllianceHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

