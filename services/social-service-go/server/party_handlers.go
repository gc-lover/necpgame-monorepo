package server

import (
	"bytes"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type PartyHandlers struct {
	service PartyServiceInterface
	logger  *logrus.Logger
}

func NewPartyHandlers(service PartyServiceInterface) *PartyHandlers {
	return &PartyHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *PartyHandlers) createParty(w http.ResponseWriter, r *http.Request) {
	var req models.CreatePartyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	accountIDStr := r.URL.Query().Get("account_id")
	if accountIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "account_id parameter is required")
		return
	}

	leaderID, err := uuid.Parse(accountIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid account_id")
		return
	}

	party, err := h.service.CreateParty(r.Context(), leaderID, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create party")
		h.respondError(w, http.StatusInternalServerError, "failed to create party")
		return
	}

	h.respondJSON(w, http.StatusCreated, party)
}

func (h *PartyHandlers) getParty(w http.ResponseWriter, r *http.Request) {
	partyIDStr := r.URL.Query().Get("party_id")
	if partyIDStr == "" {
		h.respondError(w, http.StatusBadRequest, "party_id parameter is required")
		return
	}

	partyID, err := uuid.Parse(partyIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid party_id")
		return
	}

	party, err := h.service.GetParty(r.Context(), partyID)
	if err != nil {
		if err.Error() == "party not found" {
			h.respondError(w, http.StatusNotFound, "Party not found")
		} else {
			h.logger.WithError(err).Error("Failed to get party")
			h.respondError(w, http.StatusInternalServerError, "failed to get party")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, party)
}

func (h *PartyHandlers) getPartyById(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	partyID, err := uuid.Parse(vars["partyId"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid party ID")
		return
	}

	party, err := h.service.GetParty(r.Context(), partyID)
	if err != nil {
		if err.Error() == "party not found" {
			h.respondError(w, http.StatusNotFound, "Party not found")
		} else {
			h.logger.WithError(err).Error("Failed to get party")
			h.respondError(w, http.StatusInternalServerError, "failed to get party")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, party)
}

func (h *PartyHandlers) transferLeadership(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	partyID, err := uuid.Parse(vars["partyId"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid party ID")
		return
	}

	var req models.TransferLeadershipRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	party, err := h.service.TransferLeadership(r.Context(), partyID, req.NewLeaderID)
	if err != nil {
		if err.Error() == "party not found" {
			h.respondError(w, http.StatusNotFound, "Party not found")
		} else if err.Error() == "new leader must be a member of the party" {
			h.respondError(w, http.StatusBadRequest, "New leader must be a member of the party")
		} else {
			h.logger.WithError(err).Error("Failed to transfer leadership")
			h.respondError(w, http.StatusInternalServerError, "failed to transfer leadership")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, party)
}

func (h *PartyHandlers) getPartyLeader(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	partyID, err := uuid.Parse(vars["partyId"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid party ID")
		return
	}

	leader, err := h.service.GetPartyLeader(r.Context(), partyID)
	if err != nil {
		if err.Error() == "party leader not found" {
			h.respondError(w, http.StatusNotFound, "Party leader not found")
		} else {
			h.logger.WithError(err).Error("Failed to get party leader")
			h.respondError(w, http.StatusInternalServerError, "failed to get party leader")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, leader)
}

func (h *PartyHandlers) getPlayerParty(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	accountID, err := uuid.Parse(vars["accountId"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid account ID")
		return
	}

	party, err := h.service.GetPartyByPlayerID(r.Context(), accountID)
	if err != nil {
		if err.Error() == "party not found" {
			h.respondError(w, http.StatusNotFound, "Party not found")
		} else {
			h.logger.WithError(err).Error("Failed to get player party")
			h.respondError(w, http.StatusInternalServerError, "failed to get player party")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, party)
}

func (h *PartyHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
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

func (h *PartyHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

