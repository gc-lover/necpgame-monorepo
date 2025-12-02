package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/world-service-go/models"
)

func (s *HTTPServer) executeDailyReset(w http.ResponseWriter, r *http.Request) {
	execution, err := s.worldService.ExecuteDailyReset(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to execute daily reset")
		s.respondError(w, http.StatusInternalServerError, "failed to execute daily reset")
		return
	}

	s.respondJSON(w, http.StatusOK, execution)
}

func (s *HTTPServer) executeWeeklyReset(w http.ResponseWriter, r *http.Request) {
	execution, err := s.worldService.ExecuteWeeklyReset(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to execute weekly reset")
		s.respondError(w, http.StatusInternalServerError, "failed to execute weekly reset")
		return
	}

	s.respondJSON(w, http.StatusOK, execution)
}

func (s *HTTPServer) getDailyResetStatus(w http.ResponseWriter, r *http.Request) {
	status, err := s.worldService.GetDailyResetStatus(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get daily reset status")
		s.respondError(w, http.StatusInternalServerError, "failed to get daily reset status")
		return
	}

	s.respondJSON(w, http.StatusOK, status)
}

func (s *HTTPServer) getWeeklyResetStatus(w http.ResponseWriter, r *http.Request) {
	status, err := s.worldService.GetWeeklyResetStatus(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get weekly reset status")
		s.respondError(w, http.StatusInternalServerError, "failed to get weekly reset status")
		return
	}

	s.respondJSON(w, http.StatusOK, status)
}

func (s *HTTPServer) getNextDailyReset(w http.ResponseWriter, r *http.Request) {
	nextReset, err := s.worldService.GetNextDailyReset(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get next daily reset")
		s.respondError(w, http.StatusInternalServerError, "failed to get next daily reset")
		return
	}

	s.respondJSON(w, http.StatusOK, nextReset)
}

func (s *HTTPServer) getNextWeeklyReset(w http.ResponseWriter, r *http.Request) {
	nextReset, err := s.worldService.GetNextWeeklyReset(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get next weekly reset")
		s.respondError(w, http.StatusInternalServerError, "failed to get next weekly reset")
		return
	}

	s.respondJSON(w, http.StatusOK, nextReset)
}

func (s *HTTPServer) getQuestPool(w http.ResponseWriter, r *http.Request) {
	poolTypeStr := r.URL.Query().Get("type")
	if poolTypeStr == "" {
		s.respondError(w, http.StatusBadRequest, "type is required")
		return
	}

	poolType := models.QuestPoolType(poolTypeStr)
	if poolType != models.QuestPoolTypeDaily && poolType != models.QuestPoolTypeWeekly && poolType != models.QuestPoolTypeGuild {
		s.respondError(w, http.StatusBadRequest, "invalid pool type")
		return
	}

	var playerLevel *int
	if levelStr := r.URL.Query().Get("player_level"); levelStr != "" {
		level, err := strconv.Atoi(levelStr)
		if err != nil || level < 1 {
			s.respondError(w, http.StatusBadRequest, "invalid player_level")
			return
		}
		playerLevel = &level
	}

	pool, err := s.worldService.GetQuestPool(r.Context(), poolType, playerLevel)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get quest pool")
		s.respondError(w, http.StatusInternalServerError, "failed to get quest pool")
		return
	}

	s.respondJSON(w, http.StatusOK, pool)
}

func (s *HTTPServer) assignQuestToPlayer(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID uuid.UUID              `json:"player_id"`
		QuestID  uuid.UUID              `json:"quest_id"`
		PoolType models.QuestPoolType   `json:"pool_type"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	quest, err := s.worldService.AssignQuestToPlayer(r.Context(), req.PlayerID, req.QuestID, req.PoolType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to assign quest")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, quest)
}

func (s *HTTPServer) getPlayerQuests(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var poolType *models.QuestPoolType
	if poolTypeStr := r.URL.Query().Get("type"); poolTypeStr != "" {
		pt := models.QuestPoolType(poolTypeStr)
		poolType = &pt
	}

	quests, err := s.worldService.GetPlayerQuests(r.Context(), playerID, poolType)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player quests")
		s.respondError(w, http.StatusInternalServerError, "failed to get player quests")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"player_id": playerID,
		"quests":    quests,
		"total":     len(quests),
	})
}

func (s *HTTPServer) getPlayerLoginRewards(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	rewards, err := s.worldService.GetPlayerLoginRewards(r.Context(), playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player login rewards")
		s.respondError(w, http.StatusInternalServerError, "failed to get player login rewards")
		return
	}

	s.respondJSON(w, http.StatusOK, rewards)
}

func (s *HTTPServer) claimLoginReward(w http.ResponseWriter, r *http.Request) {
	var req struct {
		PlayerID   uuid.UUID              `json:"player_id"`
		RewardType models.LoginRewardType `json:"reward_type"`
		DayNumber  int                    `json:"day_number"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err := s.worldService.ClaimLoginReward(r.Context(), req.PlayerID, req.RewardType, req.DayNumber)
	if err != nil {
		s.logger.WithError(err).Error("Failed to claim login reward")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{"success": true})
}

func (s *HTTPServer) getPlayerLoginStreak(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerIDStr := vars["player_id"]

	playerID, err := uuid.Parse(playerIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	streak, err := s.worldService.GetPlayerLoginStreak(r.Context(), playerID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get player login streak")
		s.respondError(w, http.StatusInternalServerError, "failed to get player login streak")
		return
	}

	s.respondJSON(w, http.StatusOK, streak)
}

func (s *HTTPServer) getResetSchedule(w http.ResponseWriter, r *http.Request) {
	schedule, err := s.worldService.GetResetSchedule(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reset schedule")
		s.respondError(w, http.StatusInternalServerError, "failed to get reset schedule")
		return
	}

	s.respondJSON(w, http.StatusOK, schedule)
}

func (s *HTTPServer) updateResetSchedule(w http.ResponseWriter, r *http.Request) {
	var schedule models.ResetSchedule
	if err := json.NewDecoder(r.Body).Decode(&schedule); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if err := s.worldService.UpdateResetSchedule(r.Context(), &schedule); err != nil {
		s.logger.WithError(err).Error("Failed to update reset schedule")
		s.respondError(w, http.StatusInternalServerError, "failed to update reset schedule")
		return
	}

	s.respondJSON(w, http.StatusOK, schedule)
}

func (s *HTTPServer) executeDailyResetBatch(w http.ResponseWriter, r *http.Request) {
	var req struct {
		ResetType  models.ResetType `json:"reset_type"`
		BatchSize  int              `json:"batch_size"`
		MaxWorkers int              `json:"max_workers"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	execution, err := s.worldService.ExecuteDailyReset(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to execute daily reset batch")
		s.respondError(w, http.StatusInternalServerError, "failed to execute daily reset batch")
		return
	}

	s.respondJSON(w, http.StatusOK, execution)
}

func (s *HTTPServer) executeWeeklyResetBatch(w http.ResponseWriter, r *http.Request) {
	var req struct {
		ResetType  models.ResetType `json:"reset_type"`
		BatchSize  int              `json:"batch_size"`
		MaxWorkers int              `json:"max_workers"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	execution, err := s.worldService.ExecuteWeeklyReset(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to execute weekly reset batch")
		s.respondError(w, http.StatusInternalServerError, "failed to execute weekly reset batch")
		return
	}

	s.respondJSON(w, http.StatusOK, execution)
}

func (s *HTTPServer) getResetExecutionStatus(w http.ResponseWriter, r *http.Request) {
	executionIDStr := r.URL.Query().Get("execution_id")
	if executionIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "execution_id is required")
		return
	}

	executionID, err := uuid.Parse(executionIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid execution_id")
		return
	}

	execution, err := s.worldService.GetResetExecution(r.Context(), executionID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reset execution status")
		s.respondError(w, http.StatusInternalServerError, "failed to get reset execution status")
		return
	}

	if execution == nil {
		s.respondError(w, http.StatusNotFound, "execution not found")
		return
	}

	var progressPercentage float64
	if execution.PlayersTotal > 0 {
		progressPercentage = float64(execution.PlayersProcessed) / float64(execution.PlayersTotal) * 100
	}

	response := map[string]interface{}{
		"execution_id":        execution.ID,
		"reset_type":          execution.ResetType,
		"status":              execution.Status,
		"started_at":          execution.StartedAt,
		"completed_at":       execution.CompletedAt,
		"players_processed":   execution.PlayersProcessed,
		"players_total":       execution.PlayersTotal,
		"progress_percentage": progressPercentage,
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getResetEvents(w http.ResponseWriter, r *http.Request) {
	var resetType *models.ResetType
	if resetTypeStr := r.URL.Query().Get("type"); resetTypeStr != "" {
		rt := models.ResetType(resetTypeStr)
		resetType = &rt
	}

	limit := 50
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

	events, total, err := s.worldService.GetResetEvents(r.Context(), resetType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reset events")
		s.respondError(w, http.StatusInternalServerError, "failed to get reset events")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"events": events,
		"total":  total,
	})
}












