package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/world-service-go/models"
)

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









