package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/world-service-go/models"
	"github.com/necpgame/world-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

type HTTPServer struct {
	addr              string
	router            *mux.Router
	worldService      WorldService
	worldEventsService WorldEventsServiceInterface
	logger            *logrus.Logger
	server            *http.Server
	jwtValidator      *JwtValidator
	authEnabled       bool
}

func NewHTTPServer(addr string, worldService WorldService, worldEventsService WorldEventsServiceInterface, jwtValidator *JwtValidator, authEnabled bool) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:              addr,
		router:            router,
		worldService:      worldService,
		worldEventsService: worldEventsService,
		logger:            GetLogger(),
		jwtValidator:      jwtValidator,
		authEnabled:       authEnabled,
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	worldAPI := router.PathPrefix("/api/v1/world").Subrouter()

	if authEnabled {
		worldAPI.Use(server.authMiddleware)
	}

	worldAPI.HandleFunc("/reset/daily/execute", server.executeDailyReset).Methods("POST")
	worldAPI.HandleFunc("/reset/daily/status", server.getDailyResetStatus).Methods("GET")
	worldAPI.HandleFunc("/reset/daily/next", server.getNextDailyReset).Methods("GET")
	worldAPI.HandleFunc("/reset/weekly/execute", server.executeWeeklyReset).Methods("POST")
	worldAPI.HandleFunc("/reset/weekly/status", server.getWeeklyResetStatus).Methods("GET")
	worldAPI.HandleFunc("/reset/weekly/next", server.getNextWeeklyReset).Methods("GET")

	worldAPI.HandleFunc("/reset/quests/pool", server.getQuestPool).Methods("GET")
	worldAPI.HandleFunc("/reset/quests/assign", server.assignQuestToPlayer).Methods("POST")
	worldAPI.HandleFunc("/reset/quests/player/{player_id}", server.getPlayerQuests).Methods("GET")

	worldAPI.HandleFunc("/reset/rewards/login/{player_id}", server.getPlayerLoginRewards).Methods("GET")
	worldAPI.HandleFunc("/reset/rewards/login/claim", server.claimLoginReward).Methods("POST")
	worldAPI.HandleFunc("/reset/rewards/login/streak/{player_id}", server.getPlayerLoginStreak).Methods("GET")

	worldAPI.HandleFunc("/reset/schedule", server.getResetSchedule).Methods("GET")
	worldAPI.HandleFunc("/reset/schedule/update", server.updateResetSchedule).Methods("POST")

	worldAPI.HandleFunc("/reset/execute/daily", server.executeDailyResetBatch).Methods("POST")
	worldAPI.HandleFunc("/reset/execute/weekly", server.executeWeeklyResetBatch).Methods("POST")
	worldAPI.HandleFunc("/reset/execute/status", server.getResetExecutionStatus).Methods("GET")
	worldAPI.HandleFunc("/reset/events", server.getResetEvents).Methods("GET")

	worldAPI.HandleFunc("/travel-events/trigger", server.triggerTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/available", server.getAvailableTravelEvents).Methods("GET")
	worldAPI.HandleFunc("/travel-events/{eventId}", server.getTravelEvent).Methods("GET")
	worldAPI.HandleFunc("/travel-events/{eventId}/start", server.startTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/{eventId}/skill-check", server.performTravelEventSkillCheck).Methods("POST")
	worldAPI.HandleFunc("/travel-events/{eventId}/complete", server.completeTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/{eventId}/cancel", server.cancelTravelEvent).Methods("POST")
	worldAPI.HandleFunc("/travel-events/epoch/{epochId}", server.getEpochTravelEvents).Methods("GET")
	worldAPI.HandleFunc("/travel-events/cooldown/{characterId}", server.getCharacterTravelEventCooldowns).Methods("GET")
	worldAPI.HandleFunc("/travel-events/probability", server.calculateTravelEventProbability).Methods("GET")
	worldAPI.HandleFunc("/travel-events/rewards/{eventId}", server.getTravelEventRewards).Methods("GET")
	worldAPI.HandleFunc("/travel-events/penalties/{eventId}", server.getTravelEventPenalties).Methods("GET")

	worldEventsAPI := router.PathPrefix("/api/v1").Subrouter()
	if authEnabled {
		worldEventsAPI.Use(server.authMiddleware)
	}
	worldEventsHandlers := NewWorldEventsHandlers(worldEventsService)
	worldEventsHandler := api.HandlerFromMux(worldEventsHandlers, worldEventsAPI)
	router.PathPrefix("/api/v1").Handler(worldEventsHandler)

	router.HandleFunc("/health", server.healthCheck).Methods("GET")

	return server
}

func (s *HTTPServer) Start(ctx context.Context) error {
	s.server = &http.Server{
		Addr:         s.addr,
		Handler:      s.router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	errChan := make(chan error, 1)
	go func() {
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			errChan <- err
		}
	}()

	select {
	case err := <-errChan:
		return err
	case <-ctx.Done():
		return s.Shutdown(context.Background())
	}
}

func (s *HTTPServer) Shutdown(ctx context.Context) error {
	if s.server != nil {
		return s.server.Shutdown(ctx)
	}
	return nil
}

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

func (s *HTTPServer) triggerTravelEvent(w http.ResponseWriter, r *http.Request) {
	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
		ZoneID      uuid.UUID `json:"zone_id"`
		EpochID     *string   `json:"epoch_id,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	instance, err := s.worldService.TriggerTravelEvent(r.Context(), req.CharacterID, req.ZoneID, req.EpochID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to trigger travel event")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, instance)
}

func (s *HTTPServer) getAvailableTravelEvents(w http.ResponseWriter, r *http.Request) {
	zoneIDStr := r.URL.Query().Get("zone_id")
	if zoneIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "zone_id is required")
		return
	}

	zoneID, err := uuid.Parse(zoneIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid zone_id")
		return
	}

	var epochID *string
	if epochIDStr := r.URL.Query().Get("epoch_id"); epochIDStr != "" {
		epochID = &epochIDStr
	}

	events, err := s.worldService.GetAvailableTravelEvents(r.Context(), zoneID, epochID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get available travel events")
		s.respondError(w, http.StatusInternalServerError, "failed to get available travel events")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"zone_id": zoneID,
		"events":  events,
		"total":   len(events),
	})
}

func (s *HTTPServer) getTravelEvent(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	event, err := s.worldService.GetTravelEvent(r.Context(), eventID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get travel event")
		s.respondError(w, http.StatusInternalServerError, "failed to get travel event")
		return
	}

	if event == nil {
		s.respondError(w, http.StatusNotFound, "travel event not found")
		return
	}

	s.respondJSON(w, http.StatusOK, event)
}

func (s *HTTPServer) getEpochTravelEvents(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	epochID := vars["epochId"]

	events, err := s.worldService.GetEpochTravelEvents(r.Context(), epochID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get epoch travel events")
		s.respondError(w, http.StatusInternalServerError, "failed to get epoch travel events")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"epoch_id": epochID,
		"events":   events,
		"total":    len(events),
	})
}

func (s *HTTPServer) getCharacterTravelEventCooldowns(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterIDStr := vars["characterId"]

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	cooldowns, err := s.worldService.GetCharacterTravelEventCooldowns(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get character travel event cooldowns")
		s.respondError(w, http.StatusInternalServerError, "failed to get character travel event cooldowns")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"character_id": characterID,
		"cooldowns":    cooldowns,
	})
}

func (s *HTTPServer) startTravelEvent(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	var req models.StartTravelEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	instance, err := s.worldService.StartTravelEvent(r.Context(), eventID, req.CharacterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to start travel event")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, instance)
}

func (s *HTTPServer) performTravelEventSkillCheck(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	var req models.SkillCheckRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	result, err := s.worldService.PerformTravelEventSkillCheck(r.Context(), eventID, req.Skill, req.CharacterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to perform skill check")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

func (s *HTTPServer) completeTravelEvent(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	var req models.CompleteTravelEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.worldService.CompleteTravelEvent(r.Context(), eventID, req.CharacterID, req.Success)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete travel event")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) cancelTravelEvent(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	instance, err := s.worldService.CancelTravelEvent(r.Context(), eventID, characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to cancel travel event")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, instance)
}

func (s *HTTPServer) calculateTravelEventProbability(w http.ResponseWriter, r *http.Request) {
	eventType := r.URL.Query().Get("event_type")
	if eventType == "" {
		s.respondError(w, http.StatusBadRequest, "event_type is required")
		return
	}

	characterIDStr := r.URL.Query().Get("character_id")
	if characterIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "character_id is required")
		return
	}

	characterID, err := uuid.Parse(characterIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	zoneIDStr := r.URL.Query().Get("zone_id")
	if zoneIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "zone_id is required")
		return
	}

	zoneID, err := uuid.Parse(zoneIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid zone_id")
		return
	}

	response, err := s.worldService.CalculateTravelEventProbability(r.Context(), eventType, characterID, zoneID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to calculate travel event probability")
		s.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getTravelEventRewards(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	response, err := s.worldService.GetTravelEventRewards(r.Context(), eventID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get travel event rewards")
		s.respondError(w, http.StatusNotFound, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getTravelEventPenalties(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	eventIDStr := vars["eventId"]

	eventID, err := uuid.Parse(eventIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid event_id")
		return
	}

	response, err := s.worldService.GetTravelEventPenalties(r.Context(), eventID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get travel event penalties")
		s.respondError(w, http.StatusNotFound, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		next.ServeHTTP(w, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
		}).Info("HTTP request")
	})
}

func (s *HTTPServer) metricsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start).Seconds()
		RecordRequest(r.Method, r.URL.Path, http.StatusText(recorder.statusCode))
		RecordRequestDuration(r.Method, r.URL.Path, duration)
	})
}

func (s *HTTPServer) corsMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func (s *HTTPServer) authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if !s.authEnabled || s.jwtValidator == nil {
			next.ServeHTTP(w, r)
			return
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			s.respondError(w, http.StatusUnauthorized, "authorization header required")
			return
		}

		claims, err := s.jwtValidator.Verify(r.Context(), authHeader)
		if err != nil {
			s.logger.WithError(err).Warn("JWT validation failed")
			s.respondError(w, http.StatusUnauthorized, "invalid or expired token")
			return
		}

		ctx := context.WithValue(r.Context(), "claims", claims)
		ctx = context.WithValue(ctx, "user_id", claims.Subject)
		ctx = context.WithValue(ctx, "username", claims.PreferredUsername)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

