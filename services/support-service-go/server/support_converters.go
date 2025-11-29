package server

import (
	"github.com/google/uuid"
	supportapi "github.com/necpgame/support-service-go/pkg/api"
	"github.com/necpgame/support-service-go/models"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertCreateTicketRequestFromAPI(req *supportapi.CreateTicketRequest, playerID uuid.UUID) *models.CreateTicketRequest {
	result := &models.CreateTicketRequest{
		Subject:     req.Subject,
		Description: req.Description,
	}

	if req.Category != nil {
		category := convertCreateTicketRequestCategoryFromAPI(*req.Category)
		result.Category = category
	}

	if req.Priority != nil {
		priority := convertTicketPriorityFromAPI(*req.Priority)
		result.Priority = &priority
	}

	return result
}

func convertSupportTicketToAPI(ticket *models.SupportTicket) supportapi.SupportTicket {
	category := convertSupportTicketCategoryToAPI(ticket.Category)
	priority := convertTicketPriorityToAPI(ticket.Priority)
	status := convertTicketStatusToAPI(ticket.Status)
	
	result := supportapi.SupportTicket{
		Id:          (*openapi_types.UUID)(&ticket.ID),
		TicketNumber: &ticket.Number,
		PlayerId:    (*openapi_types.UUID)(&ticket.PlayerID),
		Category:    &category,
		Priority:    &priority,
		Status:      &status,
		Subject:     &ticket.Subject,
		Description: &ticket.Description,
		CreatedAt:   &ticket.CreatedAt,
		UpdatedAt:   &ticket.UpdatedAt,
	}

	if ticket.AssignedAgentID != nil {
		result.AssignedAgentId = (*openapi_types.UUID)(ticket.AssignedAgentID)
	}

	if ticket.ResolvedAt != nil {
		result.ResolvedAt = ticket.ResolvedAt
	}

	if ticket.ClosedAt != nil {
		result.ClosedAt = ticket.ClosedAt
	}

	if ticket.FirstResponseAt != nil {
		result.FirstResponseAt = ticket.FirstResponseAt
	}

	if ticket.AssignedAgentID != nil {
		assignedAt := ticket.UpdatedAt
		result.AssignedAt = &assignedAt
	}

	return result
}

func convertTicketListResponseToTicketsResponse(response *models.TicketListResponse) supportapi.TicketsResponse {
	items := make([]supportapi.SupportTicket, len(response.Tickets))
	for i, ticket := range response.Tickets {
		items[i] = convertSupportTicketToAPI(&ticket)
	}

	hasMore := false
	if len(response.Tickets) > 0 {
		hasMore = response.Total > len(response.Tickets)
	}

	limit := len(response.Tickets)
	offset := 0

	return supportapi.TicketsResponse{
		Items:   items,
		Total:   response.Total,
		HasMore: &hasMore,
		Limit:   &limit,
		Offset:  &offset,
	}
}

func convertUpdateTicketRequestFromAPI(req *supportapi.UpdateTicketRequest) *models.UpdateTicketRequest {
	result := &models.UpdateTicketRequest{}

	if req.Status != nil {
		status := convertTicketStatusFromAPI(*req.Status)
		result.Status = &status
	}

	if req.Priority != nil {
		priority := convertTicketPriorityFromAPI(*req.Priority)
		result.Priority = &priority
	}

	if req.Category != nil {
		category := convertUpdateTicketRequestCategoryFromAPI(*req.Category)
		result.Category = &category
	}

	return result
}

func convertCreateTicketRequestCategoryFromAPI(category supportapi.CreateTicketRequestCategory) models.TicketCategory {
	switch category {
	case supportapi.CreateTicketRequestCategoryTECHNICAL:
		return models.TicketCategoryTechnical
	case supportapi.CreateTicketRequestCategoryBILLING:
		return models.TicketCategoryBilling
	case supportapi.CreateTicketRequestCategoryACCOUNT:
		return models.TicketCategoryAccount
	case supportapi.CreateTicketRequestCategoryGAMEPLAY:
		return models.TicketCategoryGameplay
	case supportapi.CreateTicketRequestCategoryBUGREPORT:
		return models.TicketCategoryBug
	case supportapi.CreateTicketRequestCategoryFEATUREREQUEST:
		return models.TicketCategorySuggestion
	case supportapi.CreateTicketRequestCategoryOTHER:
		return models.TicketCategoryOther
	default:
		return models.TicketCategoryOther
	}
}

func convertUpdateTicketRequestCategoryFromAPI(category supportapi.UpdateTicketRequestCategory) models.TicketCategory {
	switch category {
	case supportapi.TECHNICAL:
		return models.TicketCategoryTechnical
	case supportapi.BILLING:
		return models.TicketCategoryBilling
	case supportapi.ACCOUNT:
		return models.TicketCategoryAccount
	case supportapi.GAMEPLAY:
		return models.TicketCategoryGameplay
	case supportapi.BUGREPORT:
		return models.TicketCategoryBug
	case supportapi.FEATUREREQUEST:
		return models.TicketCategorySuggestion
	case supportapi.OTHER:
		return models.TicketCategoryOther
	default:
		return models.TicketCategoryOther
	}
}

func convertSupportTicketCategoryToAPI(category models.TicketCategory) supportapi.SupportTicketCategory {
	switch category {
	case models.TicketCategoryTechnical:
		return supportapi.SupportTicketCategoryTECHNICAL
	case models.TicketCategoryBilling:
		return supportapi.SupportTicketCategoryBILLING
	case models.TicketCategoryAccount:
		return supportapi.SupportTicketCategoryACCOUNT
	case models.TicketCategoryGameplay:
		return supportapi.SupportTicketCategoryGAMEPLAY
	case models.TicketCategoryBug:
		return supportapi.SupportTicketCategoryBUGREPORT
	case models.TicketCategorySuggestion:
		return supportapi.SupportTicketCategoryFEATUREREQUEST
	case models.TicketCategoryOther:
		return supportapi.SupportTicketCategoryOTHER
	default:
		return supportapi.SupportTicketCategoryOTHER
	}
}

func convertTicketPriorityFromAPI(priority supportapi.TicketPriority) models.TicketPriority {
	switch priority {
	case supportapi.TicketPriorityLOW:
		return models.TicketPriorityLow
	case supportapi.TicketPriorityNORMAL:
		return models.TicketPriorityNormal
	case supportapi.TicketPriorityHIGH:
		return models.TicketPriorityHigh
	case supportapi.TicketPriorityURGENT:
		return models.TicketPriorityHigh
	case supportapi.TicketPriorityCRITICAL:
		return models.TicketPriorityCritical
	default:
		return models.TicketPriorityNormal
	}
}

func convertTicketPriorityToAPI(priority models.TicketPriority) supportapi.TicketPriority {
	switch priority {
	case models.TicketPriorityLow:
		return supportapi.TicketPriorityLOW
	case models.TicketPriorityNormal:
		return supportapi.TicketPriorityNORMAL
	case models.TicketPriorityHigh:
		return supportapi.TicketPriorityHIGH
	case models.TicketPriorityCritical:
		return supportapi.TicketPriorityCRITICAL
	default:
		return supportapi.TicketPriorityNORMAL
	}
}

func convertTicketStatusFromAPI(status supportapi.TicketStatus) models.TicketStatus {
	switch status {
	case supportapi.TicketStatusOPEN:
		return models.TicketStatusOpen
	case supportapi.TicketStatusASSIGNED:
		return models.TicketStatusAssigned
	case supportapi.TicketStatusINPROGRESS:
		return models.TicketStatusInProgress
	case supportapi.TicketStatusWAITINGFORPLAYER:
		return models.TicketStatusWaiting
	case supportapi.TicketStatusRESOLVED:
		return models.TicketStatusResolved
	case supportapi.TicketStatusCLOSED:
		return models.TicketStatusClosed
	case supportapi.TicketStatusCANCELLED:
		return models.TicketStatusClosed
	default:
		return models.TicketStatusOpen
	}
}

func convertTicketStatusToAPI(status models.TicketStatus) supportapi.TicketStatus {
	switch status {
	case models.TicketStatusOpen:
		return supportapi.TicketStatusOPEN
	case models.TicketStatusAssigned:
		return supportapi.TicketStatusASSIGNED
	case models.TicketStatusInProgress:
		return supportapi.TicketStatusINPROGRESS
	case models.TicketStatusWaiting:
		return supportapi.TicketStatusWAITINGFORPLAYER
	case models.TicketStatusResolved:
		return supportapi.TicketStatusRESOLVED
	case models.TicketStatusClosed:
		return supportapi.TicketStatusCLOSED
	default:
		return supportapi.TicketStatusOPEN
	}
}

func convertGetTicketsParamsStatusFromAPI(status supportapi.GetTicketsParamsStatus) models.TicketStatus {
	switch status {
	case supportapi.GetTicketsParamsStatusOPEN:
		return models.TicketStatusOpen
	case supportapi.GetTicketsParamsStatusASSIGNED:
		return models.TicketStatusAssigned
	case supportapi.GetTicketsParamsStatusINPROGRESS:
		return models.TicketStatusInProgress
	case supportapi.GetTicketsParamsStatusWAITINGFORPLAYER:
		return models.TicketStatusWaiting
	case supportapi.GetTicketsParamsStatusRESOLVED:
		return models.TicketStatusResolved
	case supportapi.GetTicketsParamsStatusCLOSED:
		return models.TicketStatusClosed
	case supportapi.GetTicketsParamsStatusCANCELLED:
		return models.TicketStatusClosed
	default:
		return models.TicketStatusOpen
	}
}

