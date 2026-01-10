package handlers

import (
	"context"
	"log"
	"time"

	"github.com/google/uuid"
	"technology_progression/internal/service"
	api "technology_progression"
)

// TechnologyProgressionHandlers implements the generated Handler interface
type TechnologyProgressionHandlers struct {
	techProgressionSvc *service.TechnologyProgressionService
}

// NewTechnologyProgressionHandlers creates a new instance of TechnologyProgressionHandlers
func NewTechnologyProgressionHandlers(svc *service.TechnologyProgressionService) *TechnologyProgressionHandlers {
	return &TechnologyProgressionHandlers{
		techProgressionSvc: svc,
	}
}

// TechnologyProgressionHealthCheck implements health check endpoint
func (h *TechnologyProgressionHandlers) TechnologyProgressionHealthCheck(ctx context.Context) (*api.HealthResponseHeaders, error) {
	log.Println("Technology progression health check requested")

	response := &api.HealthResponseHeaders{}
	response.Response.Status = "healthy"
	response.Response.Domain.SetTo("technology-progression-service")
	response.Response.Timestamp = time.Now()

	return response, nil
}

// BatchHealthCheck implements batch health check endpoint
func (h *TechnologyProgressionHandlers) BatchHealthCheck(ctx context.Context, req *api.BatchHealthCheckReq) (*api.BatchHealthCheckOK, error) {
	log.Printf("Batch health check requested for %d domains", len(req.Domains))

	results := make([]api.HealthResponse, len(req.Domains))
	totalTime := 50 // Mock processing time

	for i, domain := range req.Domains {
		results[i] = api.HealthResponse{
			Status:    "healthy",
			Timestamp: time.Now(),
		}
		results[i].Domain.SetTo(domain)
	}

	response := &api.BatchHealthCheckOK{}
	response.Results = results
	response.TotalTimeMs.SetTo(totalTime)

	return response, nil
}

// GetAvailableTechnologies implements get available technologies endpoint
func (h *TechnologyProgressionHandlers) GetAvailableTechnologies(ctx context.Context, params api.GetAvailableTechnologiesParams) (*api.TechnologyListResponseHeaders, error) {
	log.Printf("Getting available technologies for character: %s", params.CharacterID)

	var category *string
	if params.Category.IsSet() {
		cat := string(params.Category.Value)
		category = &cat
	}

	technologies, totalAvailable, totalLocked, nextUnlockYear, err := h.techProgressionSvc.GetAvailableTechnologies(ctx, params.CharacterID.String(), params.IncludeLocked.Value, category)
	if err != nil {
		log.Printf("Failed to get available technologies: %v", err)
		return nil, err
	}

	// Convert to API response format
	apiTechnologies := make([]api.Technology, len(technologies))
	for i, tech := range technologies {
		apiTechnologies[i] = h.convertTechnologyToAPI(tech)
	}

	response := &api.TechnologyListResponseHeaders{}
	response.Response = api.TechnologyListResponse{
		Technologies:         apiTechnologies,
		CurrentLeagueYear:    2025, // Mock
		TotalAvailable:       totalAvailable,
		TotalLocked:          totalLocked,
		UpcomingTechnologies: api.OptInt{Value: 8, Set: true},
	}
	if nextUnlockYear != nil {
		response.Response.NextUnlockYear = api.OptInt{Value: *nextUnlockYear, Set: true}
	}

	return response, nil
}

// CheckTechnologyAvailability implements check technology availability endpoint
func (h *TechnologyProgressionHandlers) CheckTechnologyAvailability(ctx context.Context, params api.CheckTechnologyAvailabilityParams) (*api.TechnologyAvailabilityResponse, error) {
	log.Printf("Checking technology availability for %s, character: %s", params.TechnologyID, params.CharacterID)

	available, status, reason, requirements, unlockTime, err := h.techProgressionSvc.CheckTechnologyAvailability(ctx, string(params.TechnologyID), params.CharacterID.String())
	if err != nil {
		log.Printf("Failed to check technology availability: %v", err)
		return nil, err
	}

	// Convert requirements
	apiRequirements := make([]api.UnlockCondition, len(requirements))
	for i, req := range requirements {
		apiRequirements[i] = api.UnlockCondition{
			Type:      api.UnlockConditionType(req.Type),
			Condition: req.Condition,
		}
		apiRequirements[i].Description.SetTo(req.Description)
	}

	response := &api.TechnologyAvailabilityResponse{}
	response.TechnologyID = string(params.TechnologyID)
	response.Available = available
	response.AvailabilityStatus = api.TechnologyAvailabilityResponseAvailabilityStatus(status)
	if reason != "" {
		response.Reason.SetTo(reason)
	}
	response.UnlockRequirements = apiRequirements
	if unlockTime != "" {
		response.EstimatedUnlockTime.SetTo(unlockTime)
	}

	return response, nil
}

// GetTechnologyTimeline implements get technology timeline endpoint
func (h *TechnologyProgressionHandlers) GetTechnologyTimeline(ctx context.Context, params api.GetTechnologyTimelineParams) (*api.TechnologyTimelineResponse, error) {
	log.Printf("Getting technology timeline for character: %s", params.CharacterID)

	currentYear, timeline, err := h.techProgressionSvc.GetTechnologyTimeline(ctx, params.CharacterID.String(), int(params.FutureYears.Value))
	if err != nil {
		log.Printf("Failed to get technology timeline: %v", err)
		return nil, err
	}

	// Convert timeline
	apiTimeline := make([]api.TimelineEntry, len(timeline))
	for i, entry := range timeline {
		apiTechnologies := make([]api.Technology, len(entry.Technologies))
		for j, tech := range entry.Technologies {
			apiTechnologies[j] = h.convertTechnologyToAPI(tech)
		}

		apiTimeline[i] = api.TimelineEntry{
			Year:         entry.Year,
			Phase:        api.TimelineEntryPhase(entry.Phase),
			Technologies: apiTechnologies,
		}
		apiTimeline[i].TotalTechnologies.SetTo(entry.TotalTechnologies)
	}

	response := &api.TechnologyTimelineResponse{}
	response.CurrentYear = currentYear
	response.Timeline = apiTimeline

	return response, nil
}

// GetTechnologyNotifications implements get technology notifications endpoint
func (h *TechnologyProgressionHandlers) GetTechnologyNotifications(ctx context.Context, params api.GetTechnologyNotificationsParams) (*api.TechnologyNotificationsResponse, error) {
	log.Printf("Getting technology notifications for character: %s", params.CharacterID)

	notifications, totalCount, err := h.techProgressionSvc.GetTechnologyNotifications(ctx, params.CharacterID.String(), int(params.Limit.Value))
	if err != nil {
		log.Printf("Failed to get technology notifications: %v", err)
		return nil, err
	}

	// Convert notifications
	apiNotifications := make([]api.TechnologyNotification, len(notifications))
	for i, notif := range notifications {
		additionalInfo := make(map[string]interface{})
		if notif.AdditionalInfo != nil {
			for k, v := range notif.AdditionalInfo {
				additionalInfo[k] = v
			}
		}

		notification := api.TechnologyNotification{
			TechnologyID:   notif.TechnologyID,
			TechnologyName: notif.TechnologyName,
			UnlockedAt:     notif.UnlockedAt,
			UnlockReason:   api.TechnologyNotificationUnlockReason(notif.UnlockReason),
		}

		if notif.ID != "" {
			if id, err := uuid.Parse(notif.ID); err == nil {
				notification.ID = id
			}
		}

		if notif.CharacterID != "" {
			if id, err := uuid.Parse(notif.CharacterID); err == nil {
				notification.CharacterID.SetTo(id)
			}
		}

		if additionalInfo != nil {
			// Create structured additional info
			apiAdditionalInfo := api.TechnologyNotificationAdditionalInfo{}
			if questName, ok := additionalInfo["quest_name"].(string); ok {
				apiAdditionalInfo.QuestName.SetTo(questName)
			}
			if reputation, ok := additionalInfo["reputation_gained"].(int); ok {
				apiAdditionalInfo.ReputationGained.SetTo(reputation)
			}
			notification.AdditionalInfo.SetTo(apiAdditionalInfo)
		}

		apiNotifications[i] = notification
	}

	response := &api.TechnologyNotificationsResponse{}
	response.Notifications = apiNotifications
	response.TotalCount = totalCount

	return response, nil
}

// Helper method to convert internal Technology to API Technology
func (h *TechnologyProgressionHandlers) convertTechnologyToAPI(tech *service.Technology) api.Technology {
	// Convert unlock conditions
	apiConditions := make([]api.UnlockCondition, len(tech.UnlockConditions))
	for i, condition := range tech.UnlockConditions {
		apiConditions[i] = api.UnlockCondition{
			Type:      api.UnlockConditionType(condition.Type),
			Condition: condition.Condition,
		}
		apiConditions[i].Description.SetTo(condition.Description)
	}

	// Convert availability
	apiAvailability := api.TechnologyAvailability{
		Corporations: tech.Availability.Corporations,
		Restrictions: tech.Availability.Restrictions,
	}
	apiAvailability.Vendors.SetTo(api.TechnologyAvailabilityVendors(tech.Availability.Vendors))
	apiAvailability.BlackMarket.SetTo(tech.Availability.BlackMarket)

	result := api.Technology{
		ID:                 tech.ID,
		Name:               tech.Name,
		Description:        tech.Description,
		Category:           api.TechnologyCategory(tech.Category),
		UnlockYear:         tech.UnlockYear,
		UnlockPhase:        api.TechnologyUnlockPhase(tech.UnlockPhase),
		AvailabilityStatus: api.TechnologyAvailabilityStatus(tech.AvailabilityStatus),
		UnlockConditions:   apiConditions,
		Examples:           tech.Examples,
	}
	result.Availability.SetTo(apiAvailability)
	result.LoreContext.SetTo(tech.LoreContext)

	return result
}

// NewError creates *ErrorStatusCode from error returned by handler
func (h *TechnologyProgressionHandlers) NewError(ctx context.Context, err error) *api.ErrorStatusCode {
	r := new(api.ErrorStatusCode)
	return r
}