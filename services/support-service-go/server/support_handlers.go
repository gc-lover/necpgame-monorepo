// Issue: #141886730, #141886751, #1607, ogen migration
package server

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
	supportapi "github.com/gc-lover/necpgame-monorepo/services/support-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	ticketService TicketServiceInterface
	logger        *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	supportTicketPool  sync.Pool
	ticketsResponsePool sync.Pool
}

func NewHandlers(ticketService TicketServiceInterface) *Handlers {
	h := &Handlers{
		ticketService: ticketService,
		logger:        GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.supportTicketPool = sync.Pool{
		New: func() interface{} {
			return &supportapi.SupportTicket{}
		},
	}
	h.ticketsResponsePool = sync.Pool{
		New: func() interface{} {
			return &supportapi.TicketsResponse{}
		},
	}

	return h
}

func (h *Handlers) CreateTicket(ctx context.Context, req *supportapi.CreateTicketRequest) (supportapi.CreateTicketRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	userID := ctx.Value("user_id")
	if userID == nil {
		return &supportapi.CreateTicketUnauthorized{
			Error:   "Unauthorized",
			Message: "user not authenticated",
		}, nil
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		return &supportapi.CreateTicketBadRequest{
			Error:   "Bad Request",
			Message: "invalid user id",
		}, nil
	}

	if req.Subject == "" || req.Description == "" {
		return &supportapi.CreateTicketBadRequest{
			Error:   "Bad Request",
			Message: "subject and description are required",
		}, nil
	}

	internalReq := convertCreateTicketRequestFromAPI(req, playerID)
	ticket, err := h.ticketService.CreateTicket(ctx, playerID, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to create ticket")
		return &supportapi.CreateTicketInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to create ticket",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiTicket := h.supportTicketPool.Get().(*supportapi.SupportTicket)
	// Note: Not returning to pool - struct is returned to caller
	*apiTicket = convertSupportTicketToAPI(ticket)
	return apiTicket, nil
}

func (h *Handlers) GetTickets(ctx context.Context, params supportapi.GetTicketsParams) (supportapi.GetTicketsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	userID := ctx.Value("user_id")
	if userID == nil {
		return &supportapi.GetTicketsUnauthorized{
			Error:   "Unauthorized",
			Message: "user not authenticated",
		}, nil
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		return &supportapi.GetTicketsUnauthorized{
			Error:   "Unauthorized",
			Message: "invalid user id",
		}, nil
	}

	limit := 50
	offset := 0
	if params.Limit.Set {
		if params.Limit.Value > 100 {
			limit = 100
		} else if params.Limit.Value > 0 {
			limit = params.Limit.Value
		}
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	if params.Status.Set {
		status := convertGetTicketsStatusFromAPI(params.Status.Value)
		response, err := h.ticketService.GetTicketsByStatus(ctx, status, limit, offset)
		if err != nil {
			h.logger.WithError(err).Error("Failed to get tickets by status")
			return &supportapi.GetTicketsInternalServerError{
				Error:   "Internal Server Error",
				Message: "failed to get tickets",
			}, nil
		}
		apiResponse := convertTicketListResponseToTicketsResponse(response, limit, offset)
		return &apiResponse, nil
	}

	response, err := h.ticketService.GetTicketsByPlayerID(ctx, playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get tickets")
		return &supportapi.GetTicketsInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get tickets",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiResponse := h.ticketsResponsePool.Get().(*supportapi.TicketsResponse)
	// Note: Not returning to pool - struct is returned to caller
	*apiResponse = convertTicketListResponseToTicketsResponse(response, limit, offset)
	return apiResponse, nil
}

func (h *Handlers) GetTicket(ctx context.Context, params supportapi.GetTicketParams) (supportapi.GetTicketRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	id := params.TicketID
	ticket, err := h.ticketService.GetTicket(ctx, id)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get ticket")
		return &supportapi.GetTicketInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get ticket",
		}, nil
	}

	if ticket == nil {
		return &supportapi.GetTicketNotFound{
			Error:   "Not Found",
			Message: "ticket not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiTicket := h.supportTicketPool.Get().(*supportapi.SupportTicket)
	// Note: Not returning to pool - struct is returned to caller
	*apiTicket = convertSupportTicketToAPI(ticket)
	return apiTicket, nil
}

func (h *Handlers) UpdateTicket(ctx context.Context, req *supportapi.UpdateTicketRequest, params supportapi.UpdateTicketParams) (supportapi.UpdateTicketRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	id := params.TicketID

	internalReq := convertUpdateTicketRequestFromAPI(req)
	ticket, err := h.ticketService.UpdateTicket(ctx, id, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update ticket")
		return &supportapi.UpdateTicketInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to update ticket",
		}, nil
	}

	if ticket == nil {
		return &supportapi.UpdateTicketNotFound{
			Error:   "Not Found",
			Message: "ticket not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiTicket := h.supportTicketPool.Get().(*supportapi.SupportTicket)
	// Note: Not returning to pool - struct is returned to caller
	*apiTicket = convertSupportTicketToAPI(ticket)
	return apiTicket, nil
}

func (h *Handlers) CloseTicket(ctx context.Context, req *supportapi.CloseTicketRequest, params supportapi.CloseTicketParams) (supportapi.CloseTicketRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	id := params.TicketID

	status := models.TicketStatusClosed
	updateReq := &models.UpdateTicketRequest{
		Status: &status,
	}

	ticket, err := h.ticketService.UpdateTicket(ctx, id, updateReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to close ticket")
		return &supportapi.CloseTicketInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to close ticket",
		}, nil
	}

	if ticket == nil {
		return &supportapi.CloseTicketNotFound{
			Error:   "Not Found",
			Message: "ticket not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiTicket := h.supportTicketPool.Get().(*supportapi.SupportTicket)
	// Note: Not returning to pool - struct is returned to caller
	*apiTicket = convertSupportTicketToAPI(ticket)
	return apiTicket, nil
}
