package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) createGuild(w http.ResponseWriter, r *http.Request) {
	var req models.CreateGuildRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Name == "" || req.Tag == "" {
		s.respondError(w, http.StatusBadRequest, "name and tag are required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	guild, err := s.socialService.CreateGuild(r.Context(), leaderID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create guild")
		s.respondError(w, http.StatusInternalServerError, "failed to create guild")
		return
	}

	if guild == nil {
		s.respondError(w, http.StatusConflict, "guild name or tag already exists")
		return
	}

	s.respondJSON(w, http.StatusCreated, guild)
}

func (s *HTTPServer) listGuilds(w http.ResponseWriter, r *http.Request) {
	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.socialService.ListGuilds(r.Context(), limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list guilds")
		s.respondError(w, http.StatusInternalServerError, "failed to list guilds")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getGuild(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	response, err := s.socialService.GetGuild(r.Context(), id)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild")
		return
	}

	if response == nil {
		s.respondError(w, http.StatusNotFound, "guild not found")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) updateGuild(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.UpdateGuildRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	guild, err := s.socialService.UpdateGuild(r.Context(), id, leaderID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update guild")
		s.respondError(w, http.StatusInternalServerError, "failed to update guild")
		return
	}

	if guild == nil {
		s.respondError(w, http.StatusForbidden, "only guild leader can update guild")
		return
	}

	s.respondJSON(w, http.StatusOK, guild)
}

func (s *HTTPServer) disbandGuild(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = s.socialService.DisbandGuild(r.Context(), id, leaderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to disband guild")
		s.respondError(w, http.StatusInternalServerError, "failed to disband guild")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getGuildMembers(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	id, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	limit := 100
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 200 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.socialService.GetGuildMembers(r.Context(), id, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild members")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild members")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) inviteMember(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	guildID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	inviterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.InviteMemberRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	invitation, err := s.socialService.InviteMember(r.Context(), guildID, inviterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to invite member")
		s.respondError(w, http.StatusInternalServerError, "failed to invite member")
		return
	}

	if invitation == nil {
		s.respondError(w, http.StatusForbidden, "insufficient permissions or member already in guild")
		return
	}

	s.respondJSON(w, http.StatusCreated, invitation)
}

func (s *HTTPServer) updateMemberRank(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	characterIDStr := vars["characterId"]

	guildID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req models.UpdateMemberRankRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err = s.socialService.UpdateMemberRank(r.Context(), guildID, leaderID, characterID, req.Rank)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update member rank")
		s.respondError(w, http.StatusInternalServerError, "failed to update member rank")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) kickMember(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	characterIDStr := vars["characterId"]

	guildID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = s.socialService.KickMember(r.Context(), guildID, leaderID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to kick member")
		s.respondError(w, http.StatusInternalServerError, "failed to kick member")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) leaveGuild(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]
	characterIDStr := vars["characterId"]

	guildID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild id")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character id")
		return
	}

	err = s.socialService.RemoveMember(r.Context(), guildID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to leave guild")
		s.respondError(w, http.StatusInternalServerError, "failed to leave guild")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getInvitations(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	invitations, err := s.socialService.GetInvitationsByCharacter(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get invitations")
		s.respondError(w, http.StatusInternalServerError, "failed to get invitations")
		return
	}

	s.respondJSON(w, http.StatusOK, invitations)
}

func (s *HTTPServer) acceptInvitation(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	invitationID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid invitation id")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = s.socialService.AcceptInvitation(r.Context(), invitationID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to accept invitation")
		s.respondError(w, http.StatusInternalServerError, "failed to accept invitation")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) rejectInvitation(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	invitationID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid invitation id")
		return
	}

	err = s.socialService.RejectInvitation(r.Context(), invitationID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to reject invitation")
		s.respondError(w, http.StatusInternalServerError, "failed to reject invitation")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

