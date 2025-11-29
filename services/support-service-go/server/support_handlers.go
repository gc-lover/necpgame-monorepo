package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	supportapi "github.com/necpgame/support-service-go/pkg/api"
	"github.com/necpgame/support-service-go/models"
	"github.com/sirupsen/logrus"
)

type SupportHandlers struct {
	ticketService TicketServiceInterface
	logger        *logrus.Logger
}

func NewSupportHandlers(ticketService TicketServiceInterface) *SupportHandlers {
	return &SupportHandlers{
		ticketService: ticketService,
		logger:        GetLogger(),
	}
}

func (h *SupportHandlers) CreateTicket(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req supportapi.CreateTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Subject == "" || req.Description == "" {
		h.respondError(w, http.StatusBadRequest, "subject and description are required")
		return
	}

	internalReq := convertCreateTicketRequestFromAPI(&req, playerID)
	ticket, err := h.ticketService.CreateTicket(r.Context(), playerID, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create ticket")
		h.respondError(w, http.StatusInternalServerError, "failed to create ticket")
		return
	}

	apiTicket := convertSupportTicketToAPI(ticket)
	h.respondJSON(w, http.StatusCreated, apiTicket)
}

func (h *SupportHandlers) GetTickets(w http.ResponseWriter, r *http.Request, params supportapi.GetTicketsParams) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	limit := 50
	offset := 0
	if params.Limit != nil {
		limit = *params.Limit
	}
	if params.Offset != nil {
		offset = *params.Offset
	}

	if params.Status != nil {
		status := convertGetTicketsParamsStatusFromAPI(*params.Status)
		response, err := h.ticketService.GetTicketsByStatus(r.Context(), status, limit, offset)
		if err != nil {
			h.logger.WithError(err).Error("Failed to get tickets by status")
			h.respondError(w, http.StatusInternalServerError, "failed to get tickets")
			return
		}
		apiResponse := convertTicketListResponseToTicketsResponse(response)
		h.respondJSON(w, http.StatusOK, apiResponse)
		return
	}

	response, err := h.ticketService.GetTicketsByPlayerID(r.Context(), playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get tickets")
		h.respondError(w, http.StatusInternalServerError, "failed to get tickets")
		return
	}

	apiResponse := convertTicketListResponseToTicketsResponse(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *SupportHandlers) GetTicket(w http.ResponseWriter, r *http.Request, ticketId supportapi.TicketId) {
	id := uuid.UUID(ticketId)
	ticket, err := h.ticketService.GetTicket(r.Context(), id)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get ticket")
		h.respondError(w, http.StatusInternalServerError, "failed to get ticket")
		return
	}

	if ticket == nil {
		h.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	apiTicket := convertSupportTicketToAPI(ticket)
	h.respondJSON(w, http.StatusOK, apiTicket)
}

func (h *SupportHandlers) UpdateTicket(w http.ResponseWriter, r *http.Request, ticketId supportapi.TicketId) {
	id := uuid.UUID(ticketId)

	var req supportapi.UpdateTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	internalReq := convertUpdateTicketRequestFromAPI(&req)
	ticket, err := h.ticketService.UpdateTicket(r.Context(), id, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update ticket")
		h.respondError(w, http.StatusInternalServerError, "failed to update ticket")
		return
	}

	if ticket == nil {
		h.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	apiTicket := convertSupportTicketToAPI(ticket)
	h.respondJSON(w, http.StatusOK, apiTicket)
}

func (h *SupportHandlers) CloseTicket(w http.ResponseWriter, r *http.Request, ticketId supportapi.TicketId) {
	id := uuid.UUID(ticketId)

	var req supportapi.CloseTicketRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	status := models.TicketStatusClosed
	updateReq := &models.UpdateTicketRequest{
		Status: &status,
	}

	ticket, err := h.ticketService.UpdateTicket(r.Context(), id, updateReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to close ticket")
		h.respondError(w, http.StatusInternalServerError, "failed to close ticket")
		return
	}

	if ticket == nil {
		h.respondError(w, http.StatusNotFound, "ticket not found")
		return
	}

	apiTicket := convertSupportTicketToAPI(ticket)
	h.respondJSON(w, http.StatusOK, apiTicket)
}

func (h *SupportHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *SupportHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, map[string]string{"error": message})
}

