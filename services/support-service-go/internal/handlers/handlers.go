package handlers

import (
	"context"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame/services/support-service-go/api"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/models"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/service"
)

// SupportHandlers implements the generated Handler interface
type SupportHandlers struct {
	supportSvc *service.SupportService
	logger     *zap.Logger
}

// NewSupportHandlers creates a new instance of SupportHandlers
func NewSupportHandlers(svc *service.SupportService, logger *zap.Logger) *SupportHandlers {
	return &SupportHandlers{
		supportSvc: svc,
		logger:     logger,
	}
}

// HealthCheck implements health check endpoint
func (h *SupportHandlers) HealthCheck(ctx context.Context) (*api.HealthResponse, error) {
	h.logger.Info("Health check requested")

	response := &api.HealthResponse{
		Status: api.HealthResponseStatusOk,
	}

	return response, nil
}

// CreateTicket implements createTicket operation
func (h *SupportHandlers) CreateTicket(ctx context.Context, req *api.CreateTicketRequest) (api.CreateTicketRes, error) {
	h.logger.Info("Creating ticket",
		zap.String("character_id", req.CharacterID.String()),
		zap.String("title", req.Title))

	// Convert API request to internal model
	createReq := &models.CreateTicketRequest{
		CharacterID: req.CharacterID,
		Title:       req.Title,
		Description: req.Description,
		Tags:        req.Tags,
	}

	// Convert optional fields
	if req.Category.IsSet() {
		createReq.Category = models.TicketCategory(req.Category.Value)
	}
	if req.Priority.IsSet() {
		createReq.Priority = models.TicketPriority(req.Priority.Value)
	}

	// Create ticket
	ticket, err := h.supportSvc.CreateTicket(ctx, createReq)
	if err != nil {
		h.logger.Error("Failed to create ticket", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to create ticket",
			Code:    "CREATE_TICKET_FAILED",
		}, nil
	}

	// Convert to API response
	response := h.convertTicketToAPITicketResponse(ticket)

	h.logger.Info("Ticket created successfully",
		zap.String("ticket_id", ticket.ID.String()),
		zap.String("character_id", ticket.CharacterID.String()))

	return response, nil
}

// GetTicket implements getTicket operation
func (h *SupportHandlers) GetTicket(ctx context.Context, params api.GetTicketParams) (api.GetTicketRes, error) {
	h.logger.Info("Getting ticket", zap.String("ticket_id", params.TicketId.String()))

	ticket, err := h.supportSvc.GetTicket(ctx, params.TicketId)
	if err != nil {
		h.logger.Error("Failed to get ticket", zap.Error(err))
		return &api.NotFound{
			Error:   "Ticket not found",
			Code:    "TICKET_NOT_FOUND",
		}, nil
	}

	response := h.convertTicketToAPITicketResponse(ticket)

	return response, nil
}

// UpdateTicket implements updateTicket operation
func (h *SupportHandlers) UpdateTicket(ctx context.Context, req *api.UpdateTicketRequest, params api.UpdateTicketParams) (api.UpdateTicketRes, error) {
	h.logger.Info("Updating ticket", zap.String("ticket_id", params.TicketId.String()))

	// Convert API request to internal model
	updateReq := &models.UpdateTicketRequest{
		Tags: req.Tags,
	}

	if req.Title.IsSet() {
		title := req.Title.Value
		updateReq.Title = &title
	}
	if req.Description.IsSet() {
		desc := req.Description.Value
		updateReq.Description = &desc
	}
	if req.Category.IsSet() {
		category := models.TicketCategory(req.Category.Value)
		updateReq.Category = &category
	}
	if req.Priority.IsSet() {
		priority := models.TicketPriority(req.Priority.Value)
		updateReq.Priority = &priority
	}

	ticket, err := h.supportSvc.UpdateTicket(ctx, params.TicketId, updateReq)
	if err != nil {
		h.logger.Error("Failed to update ticket", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to update ticket",
			Code:    "UPDATE_TICKET_FAILED",
		}, nil
	}

	response := h.convertTicketToAPITicketResponse(ticket)

	h.logger.Info("Ticket updated successfully", zap.String("ticket_id", params.TicketId.String()))
	return response, nil
}

// DeleteTicket implements deleteTicket operation
func (h *SupportHandlers) DeleteTicket(ctx context.Context, params api.DeleteTicketParams) (api.DeleteTicketRes, error) {
	h.logger.Info("Deleting ticket", zap.String("ticket_id", params.TicketId.String()))

	err := h.supportSvc.DeleteTicket(ctx, params.TicketId)
	if err != nil {
		h.logger.Error("Failed to delete ticket", zap.Error(err))
		return &api.NotFound{
			Error:   "Ticket not found",
			Code:    "TICKET_NOT_FOUND",
		}, nil
	}

	response := &api.DeleteTicketNoContent{}
	h.logger.Info("Ticket deleted successfully", zap.String("ticket_id", params.TicketId.String()))
	return response, nil
}

// ListTickets implements listTickets operation
func (h *SupportHandlers) ListTickets(ctx context.Context, params api.ListTicketsParams) (api.ListTicketsRes, error) {
	h.logger.Info("Listing tickets")

	// Convert filters
	filter := &models.TicketFilter{}
	if params.Status.IsSet() {
		filter.Status = (*models.TicketStatus)(&params.Status.Value)
	}
	if params.Priority.IsSet() {
		filter.Priority = (*models.TicketPriority)(&params.Priority.Value)
	}
	if params.Category.IsSet() {
		category := models.TicketCategory(params.Category.Value)
		filter.Category = &category
	}
	if params.AgentID.IsSet() {
		filter.AgentID = &params.AgentID.Value
	}

	// Default pagination
	page := 1
	limit := 20
	if params.Page.IsSet() {
		page = params.Page.Value
	}
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	ticketList, err := h.supportSvc.ListTickets(ctx, filter, page, limit)
	if err != nil {
		h.logger.Error("Failed to list tickets", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to list tickets",
			Code:    "LIST_TICKETS_FAILED",
		}, nil
	}

	// Convert to API response
	apiTickets := make([]api.TicketResponse, len(ticketList.Tickets))
	for i, ticket := range ticketList.Tickets {
		apiTickets[i] = *h.convertTicketToAPITicketResponse(&ticket)
	}

	response := &api.TicketListResponse{
		Tickets: apiTickets,
		Pagination: api.TicketListResponsePagination{
			Page:       ticketList.Pagination.Page,
			Limit:      ticketList.Pagination.Limit,
			Total:      ticketList.Pagination.Total,
			TotalPages: ticketList.Pagination.TotalPages,
		},
	}

	return response, nil
}

// AssignAgent implements assignAgent operation
func (h *SupportHandlers) AssignAgent(ctx context.Context, req *api.AssignAgentRequest, params api.AssignAgentParams) (api.AssignAgentRes, error) {
	h.logger.Info("Assigning agent",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("agent_id", req.AgentID.String()))

	ticket, err := h.supportSvc.AssignAgent(ctx, params.TicketId, req.AgentID)
	if err != nil {
		h.logger.Error("Failed to assign agent", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to assign agent",
			Code:    "ASSIGN_AGENT_FAILED",
		}, nil
	}

	response := h.convertTicketToAPITicketResponse(ticket)

	h.logger.Info("Agent assigned successfully",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("agent_id", req.AgentID.String()))

	return response, nil
}

// UpdateTicketStatus implements updateTicketStatus operation
func (h *SupportHandlers) UpdateTicketStatus(ctx context.Context, req *api.UpdateStatusRequest, params api.UpdateTicketStatusParams) (api.UpdateTicketStatusRes, error) {
	h.logger.Info("Updating ticket status",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("status", string(req.Status)))

	var comment string
	if req.Comment.IsSet() {
		comment = req.Comment.Value
	}

	ticket, err := h.supportSvc.UpdateTicketStatus(ctx, params.TicketId, models.TicketStatus(req.Status), comment)
	if err != nil {
		h.logger.Error("Failed to update ticket status", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to update ticket status",
			Code:    "UPDATE_STATUS_FAILED",
		}, nil
	}

	response := h.convertTicketToAPITicketResponse(ticket)

	h.logger.Info("Ticket status updated successfully",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("status", string(req.Status)))

	return response, nil
}

// UpdateTicketPriority implements updateTicketPriority operation
func (h *SupportHandlers) UpdateTicketPriority(ctx context.Context, req *api.UpdatePriorityRequest, params api.UpdateTicketPriorityParams) (api.UpdateTicketPriorityRes, error) {
	h.logger.Info("Updating ticket priority",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("priority", string(req.Priority)))

	var reason string
	if req.Reason.IsSet() {
		reason = req.Reason.Value
	}

	ticket, err := h.supportSvc.UpdateTicketPriority(ctx, params.TicketId, models.TicketPriority(req.Priority), reason)
	if err != nil {
		h.logger.Error("Failed to update ticket priority", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to update ticket priority",
			Code:    "UPDATE_PRIORITY_FAILED",
		}, nil
	}

	response := h.convertTicketToAPITicketResponse(ticket)

	h.logger.Info("Ticket priority updated successfully",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("priority", string(req.Priority)))

	return response, nil
}

// GetCharacterTickets implements getCharacterTickets operation
func (h *SupportHandlers) GetCharacterTickets(ctx context.Context, params api.GetCharacterTicketsParams) (api.GetCharacterTicketsRes, error) {
	h.logger.Info("Getting character tickets", zap.String("character_id", params.CharacterId.String()))

	page := 1
	limit := 20
	if params.Page.IsSet() {
		page = params.Page.Value
	}
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	ticketList, err := h.supportSvc.GetCharacterTickets(ctx, params.CharacterId, page, limit)
	if err != nil {
		h.logger.Error("Failed to get character tickets", zap.Error(err))
		return &api.NotFound{
			Error:   "Character not found or no tickets available",
			Code:    "CHARACTER_NOT_FOUND",
		}, nil
	}

	apiTickets := make([]api.TicketResponse, len(ticketList.Tickets))
	for i, ticket := range ticketList.Tickets {
		apiTickets[i] = *h.convertTicketToAPITicketResponse(&ticket)
	}

	response := &api.TicketListResponse{
		Tickets: apiTickets,
		Pagination: api.TicketListResponsePagination{
			Page:       ticketList.Pagination.Page,
			Limit:      ticketList.Pagination.Limit,
			Total:      ticketList.Pagination.Total,
			TotalPages: ticketList.Pagination.TotalPages,
		},
	}

	return response, nil
}

// GetAgentTickets implements getAgentTickets operation
func (h *SupportHandlers) GetAgentTickets(ctx context.Context, params api.GetAgentTicketsParams) (api.GetAgentTicketsRes, error) {
	h.logger.Info("Getting agent tickets", zap.String("agent_id", params.AgentId.String()))

	page := 1
	limit := 20
	if params.Page.IsSet() {
		page = params.Page.Value
	}
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	ticketList, err := h.supportSvc.GetAgentTickets(ctx, params.AgentId, page, limit)
	if err != nil {
		h.logger.Error("Failed to get agent tickets", zap.Error(err))
		return &api.NotFound{
			Error:   "Failed to get agent tickets",
			Code:    "GET_AGENT_TICKETS_FAILED",
		}, nil
	}

	apiTickets := make([]api.TicketResponse, len(ticketList.Tickets))
	for i, ticket := range ticketList.Tickets {
		apiTickets[i] = *h.convertTicketToAPITicketResponse(&ticket)
	}

	response := &api.TicketListResponse{
		Tickets: apiTickets,
		Pagination: api.TicketListResponsePagination{
			Page:       ticketList.Pagination.Page,
			Limit:      ticketList.Pagination.Limit,
			Total:      ticketList.Pagination.Total,
			TotalPages: ticketList.Pagination.TotalPages,
		},
	}

	return response, nil
}

// GetTicketQueue implements getTicketQueue operation
func (h *SupportHandlers) GetTicketQueue(ctx context.Context, params api.GetTicketQueueParams) (api.GetTicketQueueRes, error) {
	h.logger.Info("Getting ticket queue")

	// Convert queue filter
	filter := &models.QueueFilter{}
	if params.Priority.IsSet() {
		filter.Priority = (*models.TicketPriority)(&params.Priority.Value)
	}

	page := 1
	limit := 50
	if params.Page.IsSet() {
		page = params.Page.Value
	}
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	queueResponse, err := h.supportSvc.GetTicketQueue(ctx, filter, page, limit)
	if err != nil {
		h.logger.Error("Failed to get ticket queue", zap.Error(err))
		return &api.InternalError{
			Error:   "Failed to get ticket queue",
			Code:    "GET_QUEUE_FAILED",
		}, nil
	}

	apiTickets := make([]api.TicketResponse, len(queueResponse.Queue))
	for i, ticket := range queueResponse.Queue {
		apiTickets[i] = *h.convertTicketToAPITicketResponse(&ticket)
	}

	response := &api.TicketQueueResponse{
		Queue: apiTickets,
		QueueStats: api.TicketQueueResponseQueueStats{
			TotalWaiting: queueResponse.QueueStats.TotalWaiting,
			UrgentCount:  queueResponse.QueueStats.UrgentCount,
			HighCount:    queueResponse.QueueStats.HighCount,
			NormalCount:  queueResponse.QueueStats.NormalCount,
			LowCount:     queueResponse.QueueStats.LowCount,
		},
	}

	return response, nil
}

// AddTicketResponse implements addTicketResponse operation
func (h *SupportHandlers) AddTicketResponse(ctx context.Context, req *api.AddResponseRequest, params api.AddTicketResponseParams) (api.AddTicketResponseRes, error) {
	h.logger.Info("Adding ticket response", zap.String("ticket_id", params.TicketId.String()))

	// Convert API request to internal model
	addReq := &models.AddResponseRequest{
		Content: req.Content,
	}

	if req.IsInternal.IsSet() {
		addReq.IsInternal = req.IsInternal.Value
	}

	// Convert attachments
	if req.Attachments != nil {
		attachments := make([]models.ResponseAttachment, len(req.Attachments))
		for i, att := range req.Attachments {
			attachments[i] = models.ResponseAttachment{
				Filename:    att.Filename,
				ContentType: att.ContentType,
				Data:        att.Data,
				Size:        int64(len(att.Data)),
			}
		}
		addReq.Attachments = attachments
	}

	// Get author info from context
	userCtx, ok := models.GetUserFromContext(ctx)
	if !ok {
		h.logger.Error("Failed to get user from context")
		return &api.BadRequest{
			Error:   "Unauthorized",
			Code:    "UNAUTHORIZED",
		}, nil
	}

	authorID := userCtx.UserID
	authorType := userCtx.UserType

	response, err := h.supportSvc.AddTicketResponse(ctx, params.TicketId, addReq, authorID, authorType)
	if err != nil {
		h.logger.Error("Failed to add ticket response", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to add ticket response",
			Code:    "ADD_RESPONSE_FAILED",
		}, nil
	}

	result := &api.TicketResponseItem{
		ID:         response.ID,
		TicketID:   response.TicketID,
		AuthorID:   response.AuthorID,
		AuthorType: api.TicketResponseItemAuthorType(response.AuthorType),
		Content:    response.Content,
		IsInternal: response.IsInternal,
		CreatedAt:  response.CreatedAt,
	}

	h.logger.Info("Ticket response added successfully",
		zap.String("ticket_id", params.TicketId.String()),
		zap.String("response_id", response.ID.String()))

	return result, nil
}

// GetTicketResponses implements getTicketResponses operation
func (h *SupportHandlers) GetTicketResponses(ctx context.Context, params api.GetTicketResponsesParams) (api.GetTicketResponsesRes, error) {
	h.logger.Info("Getting ticket responses", zap.String("ticket_id", params.TicketId.String()))

	page := 1
	limit := 20
	if params.Page.IsSet() {
		page = params.Page.Value
	}
	if params.Limit.IsSet() {
		limit = params.Limit.Value
	}

	responseList, err := h.supportSvc.GetTicketResponses(ctx, params.TicketId, page, limit)
	if err != nil {
		h.logger.Error("Failed to get ticket responses", zap.Error(err))
		return &api.NotFound{
			Error:   "Ticket not found",
			Code:    "TICKET_NOT_FOUND",
		}, nil
	}

	apiResponses := make([]api.TicketResponseItem, len(responseList.Responses))
	for i, response := range responseList.Responses {
		apiResponses[i] = api.TicketResponseItem{
			ID:         response.ID,
			TicketID:   response.TicketID,
			AuthorID:   response.AuthorID,
			AuthorType: api.TicketResponseItemAuthorType(response.AuthorType),
			Content:    response.Content,
			IsInternal: response.IsInternal,
			CreatedAt:  response.CreatedAt,
		}
	}

	result := &api.TicketResponseListResponse{
		Responses: apiResponses,
		Pagination: api.TicketResponseListResponsePagination{
			Page:       responseList.Pagination.Page,
			Limit:      responseList.Pagination.Limit,
			Total:      responseList.Pagination.Total,
			TotalPages: responseList.Pagination.TotalPages,
		},
	}

	return result, nil
}

// RateTicket implements rateTicket operation
func (h *SupportHandlers) RateTicket(ctx context.Context, req *api.RateTicketRequest, params api.RateTicketParams) (api.RateTicketRes, error) {
	h.logger.Info("Rating ticket",
		zap.String("ticket_id", params.TicketId.String()),
		zap.Int("rating", req.Rating))

	var comment string
	if req.Comment.IsSet() {
		comment = req.Comment.Value
	}

	err := h.supportSvc.RateTicket(ctx, params.TicketId, req.Rating, comment)
	if err != nil {
		h.logger.Error("Failed to rate ticket", zap.Error(err))
		return &api.BadRequest{
			Error:   "Failed to rate ticket",
			Code:    "RATE_TICKET_FAILED",
		}, nil
	}

	// Get updated ticket
	ticket, err := h.supportSvc.GetTicket(ctx, params.TicketId)
	if err != nil {
		h.logger.Error("Failed to get ticket after rating", zap.Error(err))
		return &api.InternalError{
			Error:   "Failed to get ticket after rating",
			Code:    "GET_TICKET_FAILED",
		}, nil
	}

	result := h.convertTicketToAPI(ticket)
	h.logger.Info("Ticket rated successfully",
		zap.String("ticket_id", params.TicketId.String()),
		zap.Int("rating", req.Rating))

	return &result, nil
}

// GetTicketSLA implements getTicketSLA operation
func (h *SupportHandlers) GetTicketSLA(ctx context.Context, params api.GetTicketSLAParams) (api.GetTicketSLARes, error) {
	h.logger.Info("Getting ticket SLA", zap.String("ticket_id", params.TicketId.String()))

	sla, err := h.supportSvc.GetTicketSLA(ctx, params.TicketId)
	if err != nil {
		h.logger.Error("Failed to get ticket SLA", zap.Error(err))
		return &api.NotFound{
			Error:   "Ticket SLA not found",
			Code:    "SLA_NOT_FOUND",
		}, nil
	}

	apiSLA := h.convertSLAInfoToAPI(sla)

	return &apiSLA, nil
}

// GetSupportStats implements getSupportStats operation
func (h *SupportHandlers) GetSupportStats(ctx context.Context, params api.GetSupportStatsParams) (api.GetSupportStatsRes, error) {
	h.logger.Info("Getting support stats")

	period := "month" // default
	if params.Period.IsSet() {
		period = string(params.Period.Value)
	}

	category := "" // all categories
	if params.Category.IsSet() {
		category = params.Category.Value
	}

	stats, err := h.supportSvc.GetSupportStats(ctx, period, category)
	if err != nil {
		h.logger.Error("Failed to get support stats", zap.Error(err))
		return &api.InternalError{
			Error:   "Failed to get support stats",
			Code:    "GET_STATS_FAILED",
		}, nil
	}

	apiStats := h.convertStatsToAPI(stats)

	result := &api.SupportStatsResponse{
		Period:                apiStats.Period,
		TotalTickets:          apiStats.TotalTickets,
		ResolvedTickets:       apiStats.ResolvedTickets,
		AverageResolutionTime: apiStats.AverageResolutionTime,
		AverageFirstResponseTime: apiStats.AverageFirstResponseTime,
		SLAComplianceRate:     apiStats.SLAComplianceRate,
		TicketsByStatus:       apiStats.TicketsByStatus,
		TicketsByPriority:     apiStats.TicketsByPriority,
		TicketsByCategory:     apiStats.TicketsByCategory,
		AgentPerformance:      apiStats.AgentPerformance,
	}

	return result, nil
}

// Helper functions for converting between internal models and API types

func (h *SupportHandlers) convertTicketToAPI(ticket *models.Ticket) api.TicketResponse {
	apiTicket := api.TicketResponse{
		ID:            ticket.ID,
		CharacterID:   ticket.CharacterID,
		Title:         ticket.Title,
		Description:   ticket.Description,
		Category:      api.TicketCategory(ticket.Category),
		Priority:      api.TicketPriority(ticket.Priority),
		Status:        api.TicketStatus(ticket.Status),
		CreatedAt:     ticket.CreatedAt,
		UpdatedAt:     ticket.UpdatedAt,
		Tags:          ticket.Tags,
		ResponseCount: api.NewOptInt(ticket.ResponseCount),
	}

	if ticket.AgentID != nil {
		apiTicket.AgentID = api.NewOptNilUUID(*ticket.AgentID)
	}
	if ticket.SLADeadline != nil {
		apiTicket.SLADeadline = api.NewOptNilDateTime(*ticket.SLADeadline)
	}

	return apiTicket
}

func (h *SupportHandlers) convertResponseToAPI(response *models.TicketResponse) api.TicketResponseItem {
	apiResponse := api.TicketResponseItem{
		ID:         response.ID,
		TicketID:   response.TicketID,
		AuthorID:   response.AuthorID,
		AuthorType: api.TicketResponseItemAuthorType(response.AuthorType),
		Content:    response.Content,
		IsInternal: response.IsInternal,
		CreatedAt:  response.CreatedAt,
	}

	return apiResponse
}

func (h *SupportHandlers) convertSLAInfoToAPI(sla *models.TicketSLAInfo) api.TicketSLAInfo {
	apiSLA := api.TicketSLAInfo{
		TicketID:           sla.TicketID,
		Priority:           api.TicketPriority(sla.Priority),
		CreatedAt:          sla.CreatedAt,
		SLADeadline:        sla.SLADueDate,
		ResponseDeadline:   sla.ResponseDeadline,
		ResolutionDeadline: sla.ResolutionDeadline,
		SLAStatus:          api.TicketSLAInfoSLAStatus(sla.SLAStatus),
	}

	if sla.FirstResponseAt != nil {
		apiSLA.FirstResponseAt = api.NewOptNilDateTime(*sla.FirstResponseAt)
	}
	if sla.ResolvedAt != nil {
		apiSLA.ResolvedAt = api.NewOptNilDateTime(*sla.ResolvedAt)
	}
	if sla.TimeToFirstResponse != nil {
		apiSLA.TimeToFirstResponse = api.NewOptNilString(*sla.TimeToFirstResponse)
	}
	if sla.TimeToResolution != nil {
		apiSLA.TimeToResolution = api.NewOptNilString(*sla.TimeToResolution)
	}

	return apiSLA
}

func (h *SupportHandlers) convertStatsToAPI(stats *models.SupportStatsResponse) api.SupportStatsResponse {
	// Convert agent performance
	agentPerformance := make([]api.SupportStatsResponseAgentPerformanceItem, len(stats.AgentPerformance))
	for i, perf := range stats.AgentPerformance {
		item := api.SupportStatsResponseAgentPerformanceItem{
			AgentID:       perf.AgentID,
			ResolvedCount: perf.ResolvedCount,
		}
		if perf.Name != "" {
			item.Name = api.NewOptString(perf.Name)
		}
		if perf.AverageResolutionTime != "" {
			item.AverageResolutionTime = api.NewOptString(perf.AverageResolutionTime)
		}
		if perf.SLAComplianceRate != 0 {
			item.SLAComplianceRate = api.NewOptFloat64(perf.SLAComplianceRate)
		}
		agentPerformance[i] = item
	}

	apiStats := api.SupportStatsResponse{
		Period:          api.SupportStatsResponsePeriod(stats.Period),
		TotalTickets:    stats.TotalTickets,
		ResolvedTickets: stats.ResolvedTickets,
		AgentPerformance: agentPerformance,
	}

	if stats.AverageResolutionTime != "" {
		apiStats.AverageResolutionTime = api.NewOptString(stats.AverageResolutionTime)
	}
	if stats.AverageFirstResponseTime != "" {
		apiStats.AverageFirstResponseTime = api.NewOptString(stats.AverageFirstResponseTime)
	}
	if stats.SLAComplianceRate != 0 {
		apiStats.SLAComplianceRate = api.NewOptFloat64(stats.SLAComplianceRate)
	}
	if len(stats.TicketsByStatus) > 0 {
		statusMap := make(api.SupportStatsResponseTicketsByStatus)
		for k, v := range stats.TicketsByStatus {
			statusMap[k] = v
		}
		apiStats.TicketsByStatus = api.NewOptSupportStatsResponseTicketsByStatus(statusMap)
	}
	if len(stats.TicketsByPriority) > 0 {
		priorityMap := make(api.SupportStatsResponseTicketsByPriority)
		for k, v := range stats.TicketsByPriority {
			priorityMap[k] = v
		}
		apiStats.TicketsByPriority = api.NewOptSupportStatsResponseTicketsByPriority(priorityMap)
	}
	if len(stats.TicketsByCategory) > 0 {
		categoryMap := make(api.SupportStatsResponseTicketsByCategory)
		for k, v := range stats.TicketsByCategory {
			categoryMap[k] = v
		}
		apiStats.TicketsByCategory = api.NewOptSupportStatsResponseTicketsByCategory(categoryMap)
	}

	return apiStats
}

// convertTicketToAPITicketResponse converts internal ticket model to API TicketResponse
func (h *SupportHandlers) convertTicketToAPITicketResponse(ticket *models.Ticket) *api.TicketResponse {
	apiTicket := &api.TicketResponse{
		ID:            ticket.ID,
		CharacterID:   ticket.CharacterID,
		Title:         ticket.Title,
		Description:   ticket.Description,
		Category:      api.TicketCategory(ticket.Category),
		Priority:      api.TicketPriority(ticket.Priority),
		Status:        api.TicketStatus(ticket.Status),
		Tags:          ticket.Tags,
		CreatedAt:     ticket.CreatedAt,
		UpdatedAt:     ticket.UpdatedAt,
		ResponseCount: api.NewOptInt(ticket.ResponseCount),
	}

	if ticket.AgentID != nil {
		apiTicket.AgentID = api.OptNilUUID{Value: *ticket.AgentID, Set: true}
	}
	if ticket.SLADeadline != nil {
		apiTicket.SLADeadline = api.NewOptNilDateTime(*ticket.SLADeadline)
	}

	return apiTicket
}