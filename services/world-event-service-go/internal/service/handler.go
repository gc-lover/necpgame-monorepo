package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"

	api "necpgame/services/world-event-service-go/api"
	"necpgame/services/world-event-service-go/internal/repository"
)

// HealthCheck implements healthCheck operation.
func (h *Handler) HealthCheck(ctx context.Context) (api.HealthCheckRes, error) {
	dbHealthy := true
	if err := h.repo.HealthCheck(ctx); err != nil {
		dbHealthy = false
	}

	status := api.HealthCheckOKStatusHealthy
	if !dbHealthy {
		status = api.HealthCheckOKStatusUnhealthy
	}

	return &api.HealthCheckOK{
		Status:    status,
		Timestamp: time.Now(),
		Version:   api.NewOptString("1.0.0"),
		Uptime:    api.NewOptInt(0),
	}, nil
}

// ListEvents implements listEvents operation.
func (h *Handler) ListEvents(ctx context.Context, params api.ListEventsParams) (api.ListEventsRes, error) {
	events, err := h.repo.GetActiveEvents(ctx)
	if err != nil {
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to list events: %v", err),
		}, nil
	}

	var apiEvents []api.WorldEvent
	for _, event := range events {
		apiEvents = append(apiEvents, api.WorldEvent{
			ID:                  event.ID,
			Name:                event.Name,
			Type:                api.WorldEventType(event.Type),
			Region:              event.Region,
			Status:              api.WorldEventStatus(event.Status),
			StartTime:           event.StartTime,
			Difficulty:          api.WorldEventDifficulty(event.Difficulty),
			CurrentParticipants: api.NewOptInt(event.CurrentParticipants),
			CreatedAt:           api.NewOptDateTime(event.CreatedAt),
			UpdatedAt:           api.NewOptDateTime(event.UpdatedAt),
		})
	}

	return &api.ListEventsOK{
		Events:     apiEvents,
		Pagination: api.PaginationMeta{Total: len(apiEvents), Offset: 0, Limit: 50},
	}, nil
}

// CreateEvent implements createEvent operation.
func (h *Handler) CreateEvent(ctx context.Context, req *api.CreateEventRequest) (api.CreateEventRes, error) {
	event := &repository.WorldEvent{
		EventID:             uuid.New().String(),
		Name:                req.Name,
		Type:                string(req.Type),
		Region:              req.Region,
		Status:              "announced",
		StartTime:           req.StartTime,
		CurrentParticipants: 0,
		Difficulty:          string(req.Difficulty),
	}

	if req.Description.Set {
		event.Description = &req.Description.Value
	}
	if req.EndTime.Set {
		event.EndTime = &req.EndTime.Value
	}
	if req.MaxParticipants.Set {
		event.MaxParticipants = &req.MaxParticipants.Value
	}

	created, err := h.repo.CreateWorldEvent(ctx, event)
	if err != nil {
		return &api.CreateEventBadRequest{
			Code:    "400",
			Message: fmt.Sprintf("Failed to create event: %v", err),
		}, nil
	}

	return &api.WorldEvent{
		ID:                  created.ID,
		Name:                created.Name,
		Type:                api.WorldEventType(created.Type),
		Region:              created.Region,
		Status:              api.WorldEventStatus(created.Status),
		StartTime:           created.StartTime,
		Difficulty:          api.WorldEventDifficulty(created.Difficulty),
		CurrentParticipants: api.NewOptInt(created.CurrentParticipants),
		CreatedAt:           api.NewOptDateTime(created.CreatedAt),
		UpdatedAt:           api.NewOptDateTime(created.UpdatedAt),
	}, nil
}

// GetEvent implements getEvent operation.
func (h *Handler) GetEvent(ctx context.Context, params api.GetEventParams) (api.GetEventRes, error) {
	event, err := h.repo.GetWorldEvent(ctx, params.EventId)
	if err != nil {
		return &api.Error{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.WorldEventDetail{
		ID:                  event.ID,
		Name:                event.Name,
		Type:                api.WorldEventDetailType(event.Type),
		Region:              event.Region,
		Status:              api.WorldEventDetailStatus(event.Status),
		StartTime:           event.StartTime,
		Difficulty:          api.WorldEventDetailDifficulty(event.Difficulty),
		CurrentParticipants: api.NewOptInt(event.CurrentParticipants),
		CreatedAt:           api.NewOptDateTime(event.CreatedAt),
		UpdatedAt:           api.NewOptDateTime(event.UpdatedAt),
	}, nil
}

// UpdateEvent implements updateEvent operation.
func (h *Handler) UpdateEvent(ctx context.Context, req *api.UpdateEventRequest, params api.UpdateEventParams) (api.UpdateEventRes, error) {
	updates := make(map[string]interface{})

	if req.Name.Set {
		updates["name"] = req.Name.Value
	}
	if req.Description.Set {
		updates["description"] = &req.Description.Value
	}
	if req.Status.Set {
		updates["status"] = string(req.Status.Value)
	}
	if req.EndTime.Set {
		updates["end_time"] = &req.EndTime.Value
	}
	if req.MaxParticipants.Set {
		updates["max_participants"] = &req.MaxParticipants.Value
	}

	updated, err := h.repo.UpdateWorldEvent(ctx, params.EventId, updates)
	if err != nil {
		return &api.UpdateEventNotFound{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.WorldEvent{
		ID:                  updated.ID,
		Name:                updated.Name,
		Type:                api.WorldEventType(updated.Type),
		Region:              updated.Region,
		Status:              api.WorldEventStatus(updated.Status),
		StartTime:           updated.StartTime,
		Difficulty:          api.WorldEventDifficulty(updated.Difficulty),
		CurrentParticipants: api.NewOptInt(updated.CurrentParticipants),
		CreatedAt:           api.NewOptDateTime(updated.CreatedAt),
		UpdatedAt:           api.NewOptDateTime(updated.UpdatedAt),
	}, nil
}

// CancelEvent implements cancelEvent operation.
func (h *Handler) CancelEvent(ctx context.Context, params api.CancelEventParams) (api.CancelEventRes, error) {
	updates := map[string]interface{}{
		"status":   "cancelled",
		"end_time": time.Now(),
	}

	_, err := h.repo.UpdateWorldEvent(ctx, params.EventId, updates)
	if err != nil {
		return &api.CancelEventNotFound{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.CancelEventOK{
		Message: api.NewOptString("Event cancelled successfully"),
	}, nil
}

// JoinEvent implements joinEvent operation.
func (h *Handler) JoinEvent(ctx context.Context, req *api.JoinEventReq, params api.JoinEventParams) (api.JoinEventRes, error) {
	playerID, err := uuid.Parse(req.PlayerId)
	if err != nil {
		return &api.JoinEventBadRequest{
			Message: "Invalid player ID format",
		}, nil
	}

	participant, err := h.repo.JoinEvent(ctx, playerID, params.EventId)
	if err != nil {
		return &api.JoinEventBadRequest{
			Message: fmt.Sprintf("Failed to join event: %v", err),
		}, nil
	}

	return &api.EventParticipant{
		ID:             participant.ID,
		PlayerId:       participant.PlayerID.String(),
		EventId:        participant.EventID,
		Status:         api.ParticipationStatus(participant.Status),
		JoinedAt:       participant.JoinedAt,
		LastActivityAt: participant.LastActivityAt,
		CreatedAt:      participant.CreatedAt,
		UpdatedAt:      participant.UpdatedAt,
	}, nil
}

// LeaveEvent implements leaveEvent operation.
func (h *Handler) LeaveEvent(ctx context.Context, params api.LeaveEventParams) (api.LeaveEventRes, error) {
	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.LeaveEventBadRequest{
			Message: "Invalid player ID format",
		}, nil
	}

	err = h.repo.LeaveEvent(ctx, playerID, params.EventId)
	if err != nil {
		return &api.LeaveEventBadRequest{
			Message: fmt.Sprintf("Failed to leave event: %v", err),
		}, nil
	}

	return &api.LeaveEventOK{
		Message: "Successfully left event",
	}, nil
}

// GetEventParticipants implements getEventParticipants operation.
func (h *Handler) GetEventParticipants(ctx context.Context, params api.GetEventParticipantsParams) (api.GetEventParticipantsRes, error) {
	filter := &repository.ParticipationFilter{
		Limit:  &[]int{50}[0], // Default limit
		Offset: &[]int{0}[0],  // Default offset
	}

	participants, err := h.repo.GetEventParticipants(ctx, params.EventId, filter)
	if err != nil {
		return &api.Error{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get event participants: %v", err),
		}, nil
	}

	var apiParticipants []api.EventParticipant
	for _, p := range participants {
		apiParticipants = append(apiParticipants, api.EventParticipant{
			ID:             p.ID,
			PlayerId:       p.PlayerID.String(),
			EventId:        p.EventID,
			Status:         api.ParticipationStatus(p.Status),
			JoinedAt:       p.JoinedAt,
			LastActivityAt: p.LastActivityAt,
			CreatedAt:      p.CreatedAt,
			UpdatedAt:      p.UpdatedAt,
		})
	}

	return &api.GetEventParticipantsOK{
		Data: api.EventParticipantsList{
			Participants: apiParticipants,
			Total:        len(apiParticipants),
		},
	}, nil
}

// GetPlayerParticipation implements getPlayerParticipation operation.
func (h *Handler) GetPlayerParticipation(ctx context.Context, params api.GetPlayerParticipationParams) (api.GetPlayerParticipationRes, error) {
	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.GetPlayerParticipationBadRequest{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	participation, err := h.repo.GetPlayerParticipation(ctx, playerID, params.EventId)
	if err != nil {
		return &api.GetPlayerParticipationNotFound{
			Code:    "404",
			Message: fmt.Sprintf("Participation not found: %v", err),
		}, nil
	}

	return &api.GetPlayerParticipationOK{
		Data: api.EventParticipationResponse{
			Participation: api.EventParticipation{
				Id:             participation.ID,
				PlayerId:       participation.PlayerID,
				EventId:        participation.EventID,
				Status:         api.ParticipationStatus(participation.Status),
				JoinedAt:       participation.JoinedAt,
				LastActivityAt: participation.LastActivityAt,
				CreatedAt:      participation.CreatedAt,
				UpdatedAt:      participation.UpdatedAt,
			},
		},
	}, nil
}

// UpdatePlayerParticipation implements updatePlayerParticipation operation.
func (h *Handler) UpdatePlayerParticipation(ctx context.Context, req *api.UpdateParticipationRequest, params api.UpdatePlayerParticipationParams) (api.UpdatePlayerParticipationRes, error) {
	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.UpdatePlayerParticipationBadRequest{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	updates := map[string]interface{}{
		"status":           string(req.Status.Value),
		"last_activity_at": time.Now(),
	}

	participation, err := h.repo.UpdateParticipation(ctx, params.ParticipationId, updates)
	if err != nil {
		return &api.UpdatePlayerParticipationNotFound{
			Code:    "404",
			Message: fmt.Sprintf("Participation not found: %v", err),
		}, nil
	}

	return &api.UpdatePlayerParticipationOK{
		Data: api.EventParticipationResponse{
			Participation: api.EventParticipation{
				Id:             participation.ID,
				PlayerId:       participation.PlayerID,
				EventId:        participation.EventID,
				Status:         api.ParticipationStatus(participation.Status),
				JoinedAt:       participation.JoinedAt,
				LastActivityAt: participation.LastActivityAt,
				CreatedAt:      participation.CreatedAt,
				UpdatedAt:      participation.UpdatedAt,
			},
		},
	}, nil
}

// GetPlayerRewards implements getPlayerRewards operation.
func (h *Handler) GetPlayerRewards(ctx context.Context, params api.GetPlayerRewardsParams) (api.GetPlayerRewardsRes, error) {
	rewards, err := h.repo.GetPlayerRewards(ctx, params.PlayerId, params.EventId)
	if err != nil {
		return &api.GetPlayerRewardsInternalServerError{
			Code:    "500",
			Message: fmt.Sprintf("Failed to get player rewards: %v", err),
		}, nil
	}

	var apiRewards []api.EventReward
	for _, r := range rewards {
		var rewardId api.OptString
		if r.RewardID != nil {
			rewardId = api.NewOptString(*r.RewardID)
		}

		apiRewards = append(apiRewards, api.EventReward{
			Id:         r.ID,
			EventId:    r.EventID,
			PlayerId:   r.PlayerID,
			RewardType: api.RewardType(r.RewardType),
			RewardId:   rewardId,
			Amount:     api.OptInt{Value: r.Amount, Set: true},
			Claimed:    r.Claimed,
			CreatedAt:  api.NewOptDateTime(r.CreatedAt),
		})
	}

	return &api.GetPlayerRewardsOK{
		Data: api.PlayerRewardsList{
			Rewards: apiRewards,
			Total:   len(apiRewards),
		},
	}, nil
}

// ClaimReward implements claimReward operation.
func (h *Handler) ClaimReward(ctx context.Context, req *api.ClaimRewardReq, params api.ClaimRewardParams) (api.ClaimRewardRes, error) {
	err := h.repo.ClaimReward(ctx, req.RewardId)
	if err != nil {
		return &api.ClaimRewardBadRequest{
			Code:    "400",
			Message: fmt.Sprintf("Failed to claim reward: %v", err),
		}, nil
	}

	return &api.ClaimRewardOK{
		Data: api.RewardClaimResponse{
			RewardId:  req.RewardId,
			PlayerId:  req.PlayerId,
			EventId:   params.EventId,
			ClaimedAt: time.Now(),
			Status:    "claimed",
			Delivered: true,
		},
	}, nil
}

// ListEventTemplates implements listEventTemplates operation.
func (h *Handler) ListEventTemplates(ctx context.Context, params api.ListEventTemplatesParams) (api.ListEventTemplatesOK, error) {
	filter := &repository.TemplateFilter{
		Limit:  &[]int{50}[0], // Default limit
		Offset: &[]int{0}[0],  // Default offset
	}

	templates, err := h.repo.ListEventTemplates(ctx, filter)
	if err != nil {
		return api.ListEventTemplatesOK{
			Data: api.EventTemplatesList{
				Templates: []api.EventTemplateSummary{},
				Total:     0,
			},
		}, fmt.Errorf("failed to list templates: %w", err)
	}

	var apiTemplates []api.EventTemplateSummary
	for _, t := range templates {
		apiTemplates = append(apiTemplates, api.EventTemplateSummary{
			Id:         t.ID,
			Name:       t.Name,
			Type:       api.WorldEventType(t.Type),
			Difficulty: api.WorldEventDifficulty(t.Difficulty),
			IsActive:   t.IsActive,
			UsageCount: api.NewOptInt(t.UsageCount),
			CreatedAt:  t.CreatedAt,
		})
	}

	return api.ListEventTemplatesOK{
		Data: api.EventTemplatesList{
			Templates: apiTemplates,
			Total:     len(apiTemplates),
		},
	}, nil
}

// CreateEventTemplate implements createEventTemplate operation.
func (h *Handler) CreateEventTemplate(ctx context.Context, req *api.CreateTemplateRequest) (api.CreateEventTemplateRes, error) {
	template := &repository.EventTemplate{
		Name:        req.Name,
		Type:        string(req.Type),
		Difficulty:  string(req.Difficulty),
		Description: &req.Description,
		IsActive:    true,
		MinLevel:    &[]int{1}[0], // Default min level
	}

	created, err := h.repo.CreateEventTemplate(ctx, template)
	if err != nil {
		return &api.CreateEventTemplateInternalServerError{
			Code:    "500",
			Message: fmt.Sprintf("Failed to create event template: %v", err),
		}, nil
	}

	return &api.CreateEventTemplateCreated{
		Location: "/templates/" + created.ID.String(),
		Data: api.EventTemplateResponse{
			Template: api.EventTemplate{
				Id:          created.ID,
				Name:        created.Name,
				Description: req.Description,
				Type:        api.WorldEventType(created.Type),
				Difficulty:  api.WorldEventDifficulty(created.Difficulty),
				IsActive:    created.IsActive,
				CreatedAt:   api.NewOptDateTime(created.CreatedAt),
				UpdatedAt:   api.NewOptDateTime(created.UpdatedAt),
			},
		},
	}, nil
}

// GetEventAnalytics implements getEventAnalytics operation.
func (h *Handler) GetEventAnalytics(ctx context.Context, params api.GetEventAnalyticsParams) (api.GetEventAnalyticsRes, error) {
	analytics, err := h.repo.GetEventAnalytics(ctx, params.EventId)
	if err != nil {
		// If analytics don't exist yet, return default values
		return &api.GetEventAnalyticsOK{
			Data: api.EventAnalyticsResponse{
				EventId:               params.EventId,
				TotalParticipants:     0,
				CompletedParticipants: 0,
				AverageCompletionTime: "0s",
				AverageScore:          api.NewOptFloat64(0.0),
				ParticipationRate:     api.NewOptFloat64(0.0),
				SatisfactionRating:    api.NewOptFloat64(0.0),
				LastUpdated:           time.Now(),
			},
		}, nil
	}

	var avgScore api.OptFloat64
	if analytics.AverageScore != nil {
		avgScore = api.NewOptFloat64(float64(*analytics.AverageScore))
	}

	var participationRate api.OptFloat64
	if analytics.ParticipationRate != nil {
		participationRate = api.NewOptFloat64(float64(*analytics.ParticipationRate))
	}

	var satisfactionRating api.OptFloat64
	if analytics.SatisfactionRating != nil {
		satisfactionRating = api.NewOptFloat64(float64(*analytics.SatisfactionRating))
	}

	// Format average completion time properly
	avgCompletionTime := "0s" // Default fallback
	if analytics.AverageCompletionTime != nil && *analytics.AverageCompletionTime != "" {
		avgCompletionTime = *analytics.AverageCompletionTime
	}

	return &api.GetEventAnalyticsOK{
		Data: api.EventAnalyticsResponse{
			EventId:               analytics.EventID,
			TotalParticipants:     analytics.TotalParticipants,
			CompletedParticipants: analytics.CompletedParticipants,
			AverageCompletionTime: avgCompletionTime,
			AverageScore:          avgScore,
			ParticipationRate:     participationRate,
			SatisfactionRating:    satisfactionRating,
			LastUpdated:           analytics.LastUpdated,
		},
	}, nil
}
