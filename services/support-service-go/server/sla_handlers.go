package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/sirupsen/logrus"
)

type SLAHandlers struct {
	service SLAServiceInterface
	logger  *logrus.Logger
}

func NewSLAHandlers(service SLAServiceInterface) *SLAHandlers {
	return &SLAHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *SLAHandlers) getTicketSLA(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	ticketID, err := uuid.Parse(vars["ticket_id"])
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid ticket ID")
		return
	}

	status, err := h.service.GetTicketSLAStatus(r.Context(), ticketID)
	if err != nil {
		if err.Error() == "ticket not found" {
			h.respondError(w, http.StatusNotFound, "Ticket not found")
		} else {
			h.logger.WithError(err).Error("Failed to get ticket SLA status")
			h.respondError(w, http.StatusInternalServerError, "failed to get ticket SLA status")
		}
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

func (h *SLAHandlers) getSLAViolations(w http.ResponseWriter, r *http.Request) {
	var priority *string
	if priorityStr := r.URL.Query().Get("priority"); priorityStr != "" {
		priority = &priorityStr
	}

	var violationType *string
	if violationTypeStr := r.URL.Query().Get("violation_type"); violationTypeStr != "" {
		violationType = &violationTypeStr
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

	response, err := h.service.GetSLAViolations(r.Context(), priority, violationType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get SLA violations")
		h.respondError(w, http.StatusInternalServerError, "failed to get SLA violations")
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SLAHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
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

func (h *SLAHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

