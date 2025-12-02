// Issue: #104
package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/gameplay-service-go/models"
)

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

func (s *HTTPServer) reloadQuestContent(w http.ResponseWriter, r *http.Request) {
	var req models.ReloadQuestContentRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.questService.ReloadQuestContent(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to reload quest content")
		s.respondError(w, http.StatusInternalServerError, err.Error())
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

