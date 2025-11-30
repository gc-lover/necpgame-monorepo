package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) getGuildBank(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	bank, err := s.socialService.GetGuildBank(r.Context(), guildID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild bank")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild bank")
		return
	}

	if bank == nil {
		s.respondError(w, http.StatusNotFound, "guild bank not found")
		return
	}

	s.respondJSON(w, http.StatusOK, bank)
}

func (s *HTTPServer) depositToGuildBank(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	accountID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user_id")
		return
	}

	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	var req models.GuildBankDepositRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	transaction, err := s.socialService.DepositToGuildBank(r.Context(), guildID, accountID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to deposit to guild bank")
		s.respondError(w, http.StatusInternalServerError, "failed to deposit to guild bank")
		return
	}

	if transaction == nil {
		s.respondError(w, http.StatusForbidden, "guild not found or member not found")
		return
	}

	s.respondJSON(w, http.StatusOK, transaction)
}

func (s *HTTPServer) withdrawFromGuildBank(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	accountID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user_id")
		return
	}

	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	var req models.GuildBankWithdrawRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	transaction, err := s.socialService.WithdrawFromGuildBank(r.Context(), guildID, accountID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to withdraw from guild bank")
		s.respondError(w, http.StatusInternalServerError, "failed to withdraw from guild bank")
		return
	}

	if transaction == nil {
		s.respondError(w, http.StatusForbidden, "insufficient permissions or insufficient funds")
		return
	}

	s.respondJSON(w, http.StatusOK, transaction)
}

func (s *HTTPServer) getGuildBankTransactions(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

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

	response, err := s.socialService.GetGuildBankTransactions(r.Context(), guildID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild bank transactions")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild bank transactions")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getGuildRanks(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	response, err := s.socialService.GetGuildRanks(r.Context(), guildID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get guild ranks")
		s.respondError(w, http.StatusInternalServerError, "failed to get guild ranks")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) createGuildRank(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user_id")
		return
	}

	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	var req models.CreateGuildRankRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	rank, err := s.socialService.CreateGuildRank(r.Context(), guildID, leaderID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create guild rank")
		s.respondError(w, http.StatusInternalServerError, "failed to create guild rank")
		return
	}

	if rank == nil {
		s.respondError(w, http.StatusForbidden, "insufficient permissions or guild not found")
		return
	}

	s.respondJSON(w, http.StatusOK, rank)
}

func (s *HTTPServer) updateGuildRank(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user_id")
		return
	}

	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]
	rankIDStr := vars["rank_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	rankID, err := uuid.Parse(rankIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid rank_id")
		return
	}

	var req models.UpdateGuildRankRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	rank, err := s.socialService.UpdateGuildRank(r.Context(), guildID, rankID, leaderID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update guild rank")
		s.respondError(w, http.StatusInternalServerError, "failed to update guild rank")
		return
	}

	if rank == nil {
		s.respondError(w, http.StatusForbidden, "insufficient permissions or rank not found")
		return
	}

	s.respondJSON(w, http.StatusOK, rank)
}

func (s *HTTPServer) deleteGuildRank(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	leaderID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user_id")
		return
	}

	vars := mux.Vars(r)
	guildIDStr := vars["guild_id"]
	rankIDStr := vars["rank_id"]

	guildID, err := uuid.Parse(guildIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid guild_id")
		return
	}

	rankID, err := uuid.Parse(rankIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid rank_id")
		return
	}

	err = s.socialService.DeleteGuildRank(r.Context(), guildID, rankID, leaderID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to delete guild rank")
		s.respondError(w, http.StatusInternalServerError, "failed to delete guild rank")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

