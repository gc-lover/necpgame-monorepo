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

type RelationshipHandlers struct {
	service RelationshipServiceInterface
	logger  *logrus.Logger
}

func NewRelationshipHandlers(service RelationshipServiceInterface) *RelationshipHandlers {
	return &RelationshipHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *RelationshipHandlers) GetRelationships(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var relationshipType *models.RelationshipType
	if typeStr := r.URL.Query().Get("relationship_type"); typeStr != "" {
		t := models.RelationshipType(typeStr)
		relationshipType = &t
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

	response, err := h.service.GetRelationships(r.Context(), playerID, relationshipType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get relationships")
		h.respondError(w, http.StatusInternalServerError, "failed to get relationships")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *RelationshipHandlers) SetRelationship(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var req models.SetRelationshipRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	relationship, err := h.service.SetRelationship(r.Context(), playerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to set relationship")
		h.respondError(w, http.StatusInternalServerError, "failed to set relationship")
		return
	}

	h.respondJSON(w, http.StatusOK, relationship)
}

func (h *RelationshipHandlers) GetRelationshipBetween(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID1, err := uuid.Parse(vars["player_id1"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id1")
		return
	}

	playerID2, err := uuid.Parse(vars["player_id2"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id2")
		return
	}

	relationship, err := h.service.GetRelationshipBetween(r.Context(), playerID1, playerID2)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get relationship between")
		h.respondError(w, http.StatusInternalServerError, "failed to get relationship between")
		return
	}

	if relationship == nil {
		h.respondError(w, http.StatusNotFound, "relationship not found")
		return
	}

	h.respondJSON(w, http.StatusOK, relationship)
}

func (h *RelationshipHandlers) GetTrustLevel(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	targetID, err := uuid.Parse(vars["target_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid target_id")
		return
	}

	trust, err := h.service.GetTrustLevel(r.Context(), playerID, targetID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get trust level")
		h.respondError(w, http.StatusInternalServerError, "failed to get trust level")
		return
	}

	if trust == nil {
		h.respondError(w, http.StatusNotFound, "trust level not found")
		return
	}

	h.respondJSON(w, http.StatusOK, trust)
}

func (h *RelationshipHandlers) UpdateTrust(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var req models.UpdateTrustRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	trust, err := h.service.UpdateTrust(r.Context(), playerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update trust")
		h.respondError(w, http.StatusInternalServerError, "failed to update trust")
		return
	}

	h.respondJSON(w, http.StatusOK, trust)
}

func (h *RelationshipHandlers) CreateTrustContract(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	playerID, err := uuid.Parse(vars["player_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	var req models.CreateTrustContractRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	contract, err := h.service.CreateTrustContract(r.Context(), playerID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create trust contract")
		h.respondError(w, http.StatusInternalServerError, "failed to create trust contract")
		return
	}

	h.respondJSON(w, http.StatusCreated, contract)
}

func (h *RelationshipHandlers) GetTrustContract(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	contractID, err := uuid.Parse(vars["contract_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid contract_id")
		return
	}

	contract, err := h.service.GetTrustContract(r.Context(), contractID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get trust contract")
		h.respondError(w, http.StatusInternalServerError, "failed to get trust contract")
		return
	}

	if contract == nil {
		h.respondError(w, http.StatusNotFound, "trust contract not found")
		return
	}

	h.respondJSON(w, http.StatusOK, contract)
}

func (h *RelationshipHandlers) TerminateTrustContract(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	contractID, err := uuid.Parse(vars["contract_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid contract_id")
		return
	}

	err = h.service.TerminateTrustContract(r.Context(), contractID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to terminate trust contract")
		h.respondError(w, http.StatusInternalServerError, "failed to terminate trust contract")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "terminated"})
}

func (h *RelationshipHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
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

func (h *RelationshipHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

