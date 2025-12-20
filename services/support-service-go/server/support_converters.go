// Package server Issue: ogen migration
package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/models"
	supportapi "github.com/gc-lover/necpgame-monorepo/services/support-service-go/pkg/api"
)

func convertCreateTicketRequestFromAPI(req *supportapi.CreateTicketRequest) *models.CreateTicketRequest {
	result := &models.CreateTicketRequest{
		Subject:     req.Subject,
		Description: req.Description,
	}

	if req.Category.Set {
		category := convertCreateTicketRequestCategoryFromAPI(req.Category.Value)
		result.Category = category
	}

	if req.Priority.Set {
		priority := convertTicketPriorityFromAPI(req.Priority.Value)
		result.Priority = &priority
	}

	return result
}

func convertSupportTicketToAPI(ticket *models.SupportTicket) supportapi.SupportTicket {
	category := convertSupportTicketCategoryToAPI(ticket.Category)
	priority := convertTicketPriorityToAPI(ticket.Priority)
	status := convertTicketStatusToAPI(ticket.Status)

	result := supportapi.SupportTicket{
		ID:           supportapi.NewOptUUID(ticket.ID),
		TicketNumber: supportapi.NewOptString(ticket.Number),
		PlayerID:     supportapi.NewOptUUID(ticket.PlayerID),
		Category:     supportapi.NewOptSupportTicketCategory(category),
		Priority:     supportapi.NewOptTicketPriority(priority),
		Status:       supportapi.NewOptTicketStatus(status),
		Subject:      supportapi.NewOptString(ticket.Subject),
		Description:  supportapi.NewOptString(ticket.Description),
		CreatedAt:    supportapi.NewOptDateTime(ticket.CreatedAt),
		UpdatedAt:    supportapi.NewOptDateTime(ticket.UpdatedAt),
	}

	if ticket.AssignedAgentID != nil {
		result.AssignedAgentID = supportapi.NewOptNilUUID(*ticket.AssignedAgentID)
	}

	if ticket.ResolvedAt != nil {
		result.ResolvedAt = supportapi.NewOptNilDateTime(*ticket.ResolvedAt)
	}

	if ticket.ClosedAt != nil {
		result.ClosedAt = supportapi.NewOptNilDateTime(*ticket.ClosedAt)
	}

	if ticket.FirstResponseAt != nil {
		result.FirstResponseAt = supportapi.NewOptNilDateTime(*ticket.FirstResponseAt)
	}

	if ticket.AssignedAgentID != nil {
		assignedAt := ticket.UpdatedAt
		result.AssignedAt = supportapi.NewOptNilDateTime(assignedAt)
	}

	return result
}

func convertTicketListResponseToTicketsResponse(response *models.TicketListResponse, limit, offset int) supportapi.TicketsResponse {
	items := make([]supportapi.SupportTicket, len(response.Tickets))
	for i, ticket := range response.Tickets {
		items[i] = convertSupportTicketToAPI(&ticket)
	}

	hasMore := false
	if len(response.Tickets) > 0 {
		hasMore = response.Total > len(response.Tickets)
	}

	return supportapi.TicketsResponse{
		Items:   items,
		Total:   response.Total,
		HasMore: supportapi.NewOptBool(hasMore),
		Limit:   supportapi.NewOptInt(limit),
		Offset:  supportapi.NewOptInt(offset),
	}
}

func convertUpdateTicketRequestFromAPI(req *supportapi.UpdateTicketRequest) *models.UpdateTicketRequest {
	result := &models.UpdateTicketRequest{}

	if req.Status.Set {
		status := convertTicketStatusFromAPI(req.Status.Value)
		result.Status = &status
	}

	if req.Priority.Set {
		priority := convertTicketPriorityFromAPI(req.Priority.Value)
		result.Priority = &priority
	}

	if req.Category.Set {
		category := convertUpdateTicketRequestCategoryFromAPI(req.Category.Value)
		result.Category = &category
	}

	if req.Subject.Set {
		result.Subject = &req.Subject.Value
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
	case supportapi.UpdateTicketRequestCategoryTECHNICAL:
		return models.TicketCategoryTechnical
	case supportapi.UpdateTicketRequestCategoryBILLING:
		return models.TicketCategoryBilling
	case supportapi.UpdateTicketRequestCategoryACCOUNT:
		return models.TicketCategoryAccount
	case supportapi.UpdateTicketRequestCategoryGAMEPLAY:
		return models.TicketCategoryGameplay
	case supportapi.UpdateTicketRequestCategoryBUGREPORT:
		return models.TicketCategoryBug
	case supportapi.UpdateTicketRequestCategoryFEATUREREQUEST:
		return models.TicketCategorySuggestion
	case supportapi.UpdateTicketRequestCategoryOTHER:
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

func convertGetTicketsStatusFromAPI(status supportapi.GetTicketsStatus) models.TicketStatus {
	switch status {
	case supportapi.GetTicketsStatusOPEN:
		return models.TicketStatusOpen
	case supportapi.GetTicketsStatusASSIGNED:
		return models.TicketStatusAssigned
	case supportapi.GetTicketsStatusINPROGRESS:
		return models.TicketStatusInProgress
	case supportapi.GetTicketsStatusWAITINGFORPLAYER:
		return models.TicketStatusWaiting
	case supportapi.GetTicketsStatusRESOLVED:
		return models.TicketStatusResolved
	case supportapi.GetTicketsStatusCLOSED:
		return models.TicketStatusClosed
	case supportapi.GetTicketsStatusCANCELLED:
		return models.TicketStatusClosed
	default:
		return models.TicketStatusOpen
	}
}

// Issue: #1489 - SLA converters
func convertTicketSLAStatusToAPI(status *models.TicketSLAStatus) supportapi.TicketSLAStatus {
	result := supportapi.TicketSLAStatus{
		TicketID:            supportapi.NewOptUUID(status.TicketID),
		FirstResponseTarget: supportapi.NewOptDateTime(status.FirstResponseTarget),
		ResolutionTarget:    supportapi.NewOptDateTime(status.ResolutionTarget),
	}

	// Convert priority
	switch status.Priority {
	case "LOW":
		result.Priority = supportapi.NewOptTicketSLAStatusPriority(supportapi.TicketSLAStatusPriorityLOW)
	case "NORMAL":
		result.Priority = supportapi.NewOptTicketSLAStatusPriority(supportapi.TicketSLAStatusPriorityNORMAL)
	case "HIGH":
		result.Priority = supportapi.NewOptTicketSLAStatusPriority(supportapi.TicketSLAStatusPriorityHIGH)
	case "URGENT":
		result.Priority = supportapi.NewOptTicketSLAStatusPriority(supportapi.TicketSLAStatusPriorityURGENT)
	case "CRITICAL":
		result.Priority = supportapi.NewOptTicketSLAStatusPriority(supportapi.TicketSLAStatusPriorityCRITICAL)
	default:
		result.Priority = supportapi.NewOptTicketSLAStatusPriority(supportapi.TicketSLAStatusPriorityNORMAL)
	}

	if status.FirstResponseActual != nil {
		result.FirstResponseActual = supportapi.NewOptNilDateTime(*status.FirstResponseActual)
	}
	if status.ResolutionActual != nil {
		result.ResolutionActual = supportapi.NewOptNilDateTime(*status.ResolutionActual)
	}
	if status.TimeUntilFirstResponseTarget != nil {
		result.TimeUntilFirstResponseTarget = supportapi.NewOptNilInt(*status.TimeUntilFirstResponseTarget)
	}
	if status.TimeUntilResolutionTarget != nil {
		result.TimeUntilResolutionTarget = supportapi.NewOptNilInt(*status.TimeUntilResolutionTarget)
	}
	if status.FirstResponseSLAMet != nil {
		result.FirstResponseSLAMet = supportapi.NewOptNilBool(*status.FirstResponseSLAMet)
	}
	if status.ResolutionSLAMet != nil {
		result.ResolutionSLAMet = supportapi.NewOptNilBool(*status.ResolutionSLAMet)
	}

	return result
}

func convertSLAViolationToAPI(violation models.SLAViolation) supportapi.SLAViolation {
	result := supportapi.SLAViolation{
		TicketID:     supportapi.NewOptUUID(violation.TicketID),
		TicketNumber: supportapi.NewOptString(violation.TicketNumber),
		TargetTime:   supportapi.NewOptDateTime(violation.TargetTime),
	}

	// Convert priority
	switch violation.Priority {
	case "LOW":
		result.Priority = supportapi.NewOptSLAViolationPriority(supportapi.SLAViolationPriorityLOW)
	case "NORMAL":
		result.Priority = supportapi.NewOptSLAViolationPriority(supportapi.SLAViolationPriorityNORMAL)
	case "HIGH":
		result.Priority = supportapi.NewOptSLAViolationPriority(supportapi.SLAViolationPriorityHIGH)
	case "URGENT":
		result.Priority = supportapi.NewOptSLAViolationPriority(supportapi.SLAViolationPriorityURGENT)
	case "CRITICAL":
		result.Priority = supportapi.NewOptSLAViolationPriority(supportapi.SLAViolationPriorityCRITICAL)
	default:
		result.Priority = supportapi.NewOptSLAViolationPriority(supportapi.SLAViolationPriorityNORMAL)
	}

	// Convert violation type
	switch violation.ViolationType {
	case models.SLAViolationTypeFirstResponse:
		result.ViolationType = supportapi.NewOptSLAViolationViolationType(supportapi.SLAViolationViolationTypeFIRSTRESPONSE)
	case models.SLAViolationTypeResolution:
		result.ViolationType = supportapi.NewOptSLAViolationViolationType(supportapi.SLAViolationViolationTypeRESOLUTION)
	}

	if violation.ActualTime != nil {
		result.ActualTime = supportapi.NewOptNilDateTime(*violation.ActualTime)
	}
	if violation.ViolationDurationSeconds != nil {
		result.ViolationDurationSeconds = supportapi.NewOptNilInt(*violation.ViolationDurationSeconds)
	}

	return result
}

func convertSLAViolationsResponseToAPI(response *models.SLAViolationsResponse) supportapi.SLAViolationsResponse {
	items := make([]supportapi.SLAViolation, len(response.Items))
	for i, violation := range response.Items {
		items[i] = convertSLAViolationToAPI(violation)
	}

	hasMore := false
	if len(response.Items) > 0 {
		hasMore = response.Total > response.Offset+len(response.Items)
	}

	return supportapi.SLAViolationsResponse{
		Items:   items,
		Total:   response.Total,
		Limit:   supportapi.NewOptInt(response.Limit),
		Offset:  supportapi.NewOptInt(response.Offset),
		HasMore: supportapi.NewOptBool(hasMore),
	}
}
