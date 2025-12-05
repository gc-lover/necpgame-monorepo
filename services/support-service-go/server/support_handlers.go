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
// Issue: #1489 - SLA handlers
type Handlers struct {
	ticketService TicketServiceInterface
	slaService    SLAServiceInterface
	logger        *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	supportTicketPool      sync.Pool
	ticketsResponsePool    sync.Pool
	ticketSLAStatusPool    sync.Pool
	slaViolationsPool     sync.Pool
}

func NewHandlers(ticketService TicketServiceInterface, slaService SLAServiceInterface) *Handlers {
	h := &Handlers{
		ticketService: ticketService,
		slaService:    slaService,
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
	h.ticketSLAStatusPool = sync.Pool{
		New: func() interface{} {
			return &supportapi.TicketSLAStatus{}
		},
	}
	h.slaViolationsPool = sync.Pool{
		New: func() interface{} {
			return &supportapi.SLAViolationsResponse{}
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

// Issue: #1489 - GetTicketSLA implements getTicketSLA operation
func (h *Handlers) GetTicketSLA(ctx context.Context, params supportapi.GetTicketSLAParams) (supportapi.GetTicketSLARes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	ticketID := params.TicketID
	status, err := h.slaService.GetTicketSLAStatus(ctx, ticketID)
	if err != nil {
		if err.Error() == "ticket not found" {
			return &supportapi.GetTicketSLANotFound{
				Error:   "Not Found",
				Message: "ticket not found",
			}, nil
		}
		h.logger.WithError(err).Error("Failed to get ticket SLA status")
		return &supportapi.GetTicketSLAInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get ticket SLA status",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiStatus := h.ticketSLAStatusPool.Get().(*supportapi.TicketSLAStatus)
	// Note: Not returning to pool - struct is returned to caller
	*apiStatus = convertTicketSLAStatusToAPI(status)
	return apiStatus, nil
}

// Issue: #1489 - GetSLAViolations implements getSLAViolations operation
func (h *Handlers) GetSLAViolations(ctx context.Context, params supportapi.GetSLAViolationsParams) (supportapi.GetSLAViolationsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var priority *string
	if params.Priority.Set {
		priorityStr := string(params.Priority.Value)
		priority = &priorityStr
	}

	var violationType *string
	if params.ViolationType.Set {
		vtStr := string(params.ViolationType.Value)
		violationType = &vtStr
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

	response, err := h.slaService.GetSLAViolations(ctx, priority, violationType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get SLA violations")
		return &supportapi.GetSLAViolationsInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get SLA violations",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiResponse := h.slaViolationsPool.Get().(*supportapi.SLAViolationsResponse)
	// Note: Not returning to pool - struct is returned to caller
	*apiResponse = convertSLAViolationsResponseToAPI(response)
	return apiResponse, nil
}
