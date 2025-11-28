package server

import (
	"context"
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type ProgressionServiceInterface interface {
	GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error)
	AddExperience(ctx context.Context, characterID uuid.UUID, amount int64, source string) error
	AddSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string, amount int64) error
	AllocateAttributePoint(ctx context.Context, characterID uuid.UUID, attribute string) error
	AllocateSkillPoint(ctx context.Context, characterID uuid.UUID, skillID string) error
	GetSkillProgression(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.SkillProgressionResponse, error)
}

type QuestServiceInterface interface {
	StartQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error)
	GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error)
	UpdateDialogue(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, nodeID string, choiceID *string) error
	PerformSkillCheck(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, skillID string, requiredLevel int) (bool, error)
	CompleteObjective(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, objectiveID string) error
	CompleteQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error
	FailQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error
	ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) (*models.QuestListResponse, error)
}

type HTTPServer struct {
	addr             string
	router           *mux.Router
	progressionService ProgressionServiceInterface
	questService     QuestServiceInterface
	logger           *logrus.Logger
	server           *http.Server
}

func NewHTTPServer(addr string, progressionService ProgressionServiceInterface, questService QuestServiceInterface) *HTTPServer {
	router := mux.NewRouter()
	server := &HTTPServer{
		addr:             addr,
		router:           router,
		progressionService: progressionService,
		questService:     questService,
		logger:           GetLogger(),
	}

	router.Use(server.loggingMiddleware)
	router.Use(server.metricsMiddleware)
	router.Use(server.corsMiddleware)

	progressionAPI := router.PathPrefix("/api/v1/gameplay/progression").Subrouter()
	progressionAPI.HandleFunc("/characters/{character_id}", server.getProgression).Methods("GET")
	progressionAPI.HandleFunc("/characters/{character_id}/experience", server.addExperience).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/skills/experience", server.addSkillExperience).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/attributes/allocate", server.allocateAttributePoint).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/skills/allocate", server.allocateSkillPoint).Methods("POST")
	progressionAPI.HandleFunc("/characters/{character_id}/skills", server.getSkillProgression).Methods("GET")

	questAPI := router.PathPrefix("/api/v1/gameplay/quests").Subrouter()
	questAPI.HandleFunc("/start", server.startQuest).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}", server.getQuestInstance).Methods("GET")
	questAPI.HandleFunc("/instances/{instance_id}/dialogue", server.updateDialogue).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/skill-check", server.performSkillCheck).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/objectives/complete", server.completeObjective).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/complete", server.completeQuest).Methods("POST")
	questAPI.HandleFunc("/instances/{instance_id}/fail", server.failQuest).Methods("POST")
	questAPI.HandleFunc("/characters/{character_id}", server.listQuestInstances).Methods("GET")

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

func (s *HTTPServer) getProgression(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	progression, err := s.progressionService.GetProgression(r.Context(), characterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get progression")
		s.respondError(w, http.StatusInternalServerError, "failed to get progression")
		return
	}

	s.respondJSON(w, http.StatusOK, models.ProgressionResponse{Progression: progression})
}

func (s *HTTPServer) addExperience(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req models.AddExperienceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CharacterID = characterID
	if req.Source == "" {
		req.Source = "unknown"
	}

	err = s.progressionService.AddExperience(r.Context(), req.CharacterID, req.Amount, req.Source)
	if err != nil {
		s.logger.WithError(err).Error("Failed to add experience")
		s.respondError(w, http.StatusInternalServerError, "failed to add experience")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) addSkillExperience(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req models.AddSkillExperienceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CharacterID = characterID

	err = s.progressionService.AddSkillExperience(r.Context(), req.CharacterID, req.SkillID, req.Amount)
	if err != nil {
		s.logger.WithError(err).Error("Failed to add skill experience")
		s.respondError(w, http.StatusInternalServerError, "failed to add skill experience")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) allocateAttributePoint(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req models.AllocateAttributePointRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CharacterID = characterID

	err = s.progressionService.AllocateAttributePoint(r.Context(), req.CharacterID, req.Attribute)
	if err != nil {
		s.logger.WithError(err).Error("Failed to allocate attribute point")
		if err.Error() == "not enough attribute points" || err.Error() == "attribute "+req.Attribute+" is at maximum" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to allocate attribute point")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) allocateSkillPoint(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var req models.AllocateSkillPointRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.CharacterID = characterID

	err = s.progressionService.AllocateSkillPoint(r.Context(), req.CharacterID, req.SkillID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to allocate skill point")
		if err.Error() == "not enough skill points" {
			s.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			s.respondError(w, http.StatusInternalServerError, "failed to allocate skill point")
		}
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getSkillProgression(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
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

	response, err := s.progressionService.GetSkillProgression(r.Context(), characterID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get skill progression")
		s.respondError(w, http.StatusInternalServerError, "failed to get skill progression")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) startQuest(w http.ResponseWriter, r *http.Request) {
	var req models.StartQuestRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	instance, err := s.questService.StartQuest(r.Context(), req.CharacterID, req.QuestID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to start quest")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, models.QuestInstanceResponse{QuestInstance: instance})
}

func (s *HTTPServer) getQuestInstance(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	instance, err := s.questService.GetQuestInstance(r.Context(), instanceID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get quest instance")
		s.respondError(w, http.StatusInternalServerError, "failed to get quest instance")
		return
	}

	if instance == nil {
		s.respondError(w, http.StatusNotFound, "quest instance not found")
		return
	}

	s.respondJSON(w, http.StatusOK, models.QuestInstanceResponse{QuestInstance: instance})
}

func (s *HTTPServer) updateDialogue(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	var req models.UpdateDialogueRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.QuestInstanceID = instanceID

	err = s.questService.UpdateDialogue(r.Context(), req.QuestInstanceID, req.CharacterID, req.NodeID, req.ChoiceID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update dialogue")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) performSkillCheck(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	var req models.PerformSkillCheckRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.QuestInstanceID = instanceID

	passed, err := s.questService.PerformSkillCheck(r.Context(), req.QuestInstanceID, req.CharacterID, req.SkillID, req.RequiredLevel)
	if err != nil {
		s.logger.WithError(err).Error("Failed to perform skill check")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{"passed": passed})
}

func (s *HTTPServer) completeObjective(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	var req models.CompleteObjectiveRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	req.QuestInstanceID = instanceID

	err = s.questService.CompleteObjective(r.Context(), req.QuestInstanceID, req.CharacterID, req.ObjectiveID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete objective")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) completeQuest(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err = s.questService.CompleteQuest(r.Context(), instanceID, req.CharacterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to complete quest")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) failQuest(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	instanceID, err := uuid.Parse(vars["instance_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid instance_id")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	err = s.questService.FailQuest(r.Context(), instanceID, req.CharacterID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to fail quest")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) listQuestInstances(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	characterID, err := uuid.Parse(vars["character_id"])
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid character_id")
		return
	}

	var status *models.QuestStatus
	if statusStr := r.URL.Query().Get("status"); statusStr != "" {
		qs := models.QuestStatus(statusStr)
		status = &qs
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

	response, err := s.questService.ListQuestInstances(r.Context(), characterID, status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to list quest instances")
		s.respondError(w, http.StatusInternalServerError, "failed to list quest instances")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) loggingMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()

		recorder := &statusRecorder{ResponseWriter: w, statusCode: http.StatusOK}
		next.ServeHTTP(recorder, r)

		duration := time.Since(start)
		s.logger.WithFields(logrus.Fields{
			"method":      r.Method,
			"path":        r.URL.Path,
			"duration_ms": duration.Milliseconds(),
			"status":      recorder.statusCode,
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

type statusRecorder struct {
	http.ResponseWriter
	statusCode int
}

func (sr *statusRecorder) WriteHeader(code int) {
	sr.statusCode = code
	sr.ResponseWriter.WriteHeader(code)
}

