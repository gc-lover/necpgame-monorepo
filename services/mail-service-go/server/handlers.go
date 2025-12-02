// Issue: #151
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gc-lover/necpgame/services/mail-service-go/pkg/api"
)

type Handlers struct {
	service Service
}

func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) GetInbox(w http.ResponseWriter, r *http.Request, params api.GetInboxParams) {
	response, err := h.service.GetInbox(r.Context(), params)
	if err != nil {
		respondError(w, http.StatusInternalServerError, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetMail(w http.ResponseWriter, r *http.Request, mailId api.MailId) {
	response, err := h.service.GetMail(r.Context(), mailId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, "Mail not found")
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) DeleteMail(w http.ResponseWriter, r *http.Request, mailId api.MailId) {
	err := h.service.DeleteMail(r.Context(), mailId.String())
	if err != nil {
		respondError(w, http.StatusNotFound, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, map[string]string{"status": "deleted"})
}

func (h *Handlers) SendMail(w http.ResponseWriter, r *http.Request) {
	var req api.SendMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "Invalid request")
		return
	}
	
	response, err := h.service.SendMail(r.Context(), &req)
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) ClaimAttachments(w http.ResponseWriter, r *http.Request, mailId api.MailId) {
	response, err := h.service.ClaimAttachments(r.Context(), mailId.String())
	if err != nil {
		respondError(w, http.StatusBadRequest, err.Error())
		return
	}
	respondJSON(w, http.StatusOK, response)
}

func respondJSON(w http.ResponseWriter, code int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, code int, message string) {
	respondJSON(w, code, map[string]string{"error": message})
}

