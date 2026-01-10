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
		return &api.Error{
			Code:    "404",
			Message: fmt.Sprintf("Event not found: %s", params.EventId.String()),
		}, nil
	}

	return &api.Error{
		Code:    "200",
		Message: "Event cancelled successfully",
	}, nil
}

// JoinEvent implements joinEvent operation.
func (h *Handler) JoinEvent(ctx context.Context, req *api.JoinEventReq, params api.JoinEventParams) (api.JoinEventRes, error) {
	playerID, err := uuid.Parse(req.PlayerId)
	if err != nil {
		return &api.JoinEventBadRequest{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	_, err = h.repo.JoinEvent(ctx, playerID, params.EventId)
	if err != nil {
		return &api.JoinEventBadRequest{
			Code:    "400",
			Message: fmt.Sprintf("Failed to join event: %v", err),
		}, nil
	}

	return &api.Error{
		Code:    "200",
		Message: "Successfully joined event",
	}, nil
}

// LeaveEvent implements leaveEvent operation.
func (h *Handler) LeaveEvent(ctx context.Context, params api.LeaveEventParams) (api.LeaveEventRes, error) {
	playerID, err := uuid.Parse(params.PlayerId)
	if err != nil {
		return &api.Error{
			Code:    "400",
			Message: "Invalid player ID format",
		}, nil
	}

	err = h.repo.LeaveEvent(ctx, playerID, params.EventId)
	if err != nil {
		return &api.Error{
			Code:    "400",
			Message: fmt.Sprintf("Failed to leave event: %v", err),
		}, nil
	}

	return &api.Error{
		Code:    "200",
		Message: "Successfully left event",
	}, nil
}

// GetEventParticipants implements getEventParticipants operation.
func (h *Handler) GetEventParticipants(ctx context.Context, params api.GetEventParticipantsParams) (api.GetEventParticipantsRes, error) {
	return &api.GetEventParticipantsOK{
		Body: api.EventParticipantsList{
			Participants: []api.EventParticipant{},
			Total:        0,
		},
	}, nil
}

// GetPlayerParticipation implements getPlayerParticipation operation.
func (h *Handler) GetPlayerParticipation(ctx context.Context, params api.GetPlayerParticipationParams) (api.GetPlayerParticipationRes, error) {
	return &api.GetPlayerParticipationOK{
		Body: api.EventParticipationResponse{
			Participation: api.EventParticipation{
				ID:             uuid.New(),
				PlayerID:       params.PlayerId,
				EventID:        params.EventId,
				Status:         api.ParticipationStatusActive,
				JoinedAt:       time.Now(),
				LastActivityAt: time.Now(),
				CreatedAt:      time.Now(),
				UpdatedAt:      time.Now(),
			},
		},
	}, nil
}

// UpdatePlayerParticipation implements updatePlayerParticipation operation.
func (h *Handler) UpdatePlayerParticipation(ctx context.Context, req *api.UpdateParticipationRequest, params api.UpdatePlayerParticipationParams) (api.UpdatePlayerParticipationRes, error) {
	return &api.UpdatePlayerParticipationOK{
		Body: api.EventParticipationResponse{
			Participation: api.EventParticipation{
				ID:             uuid.New(),
				PlayerID:       params.PlayerId,
				EventID:        params.EventId,
				Status:         api.ParticipationStatus(req.Status),
				JoinedAt:       time.Now(),
				LastActivityAt: time.Now(),
				CreatedAt:      time.Now(),
				UpdatedAt:      time.Now(),
			},
		},
	}, nil
}

// GetPlayerRewards implements getPlayerRewards operation.
func (h *Handler) GetPlayerRewards(ctx context.Context, params api.GetPlayerRewardsParams) (api.GetPlayerRewardsRes, error) {
	return &api.GetPlayerRewardsOK{
		Body: api.PlayerRewardsList{
			Rewards: []api.EventReward{},
			Total:   0,
		},
	}, nil
}

// ClaimReward implements claimReward operation.
func (h *Handler) ClaimReward(ctx context.Context, req *api.ClaimRewardReq, params api.ClaimRewardParams) (api.ClaimRewardRes, error) {
	return &api.ClaimRewardOK{
		Body: api.RewardClaimResponse{
			RewardID:  req.RewardID,
			PlayerID:  req.PlayerID,
			EventID:   params.EventID,
			ClaimedAt: time.Now(),
			Status:    "claimed",
			Delivered: true,
		},
	}, nil
}

// ListEventTemplates implements listEventTemplates operation.
func (h *Handler) ListEventTemplates(ctx context.Context, params api.ListEventTemplatesParams) (api.ListEventTemplatesOK, error) {
	return api.ListEventTemplatesOK{
		Templates: []api.EventTemplateSummary{},
		Total:     0,
	}, nil
}

// CreateEventTemplate implements createEventTemplate operation.
func (h *Handler) CreateEventTemplate(ctx context.Context, req *api.CreateTemplateRequest) (api.CreateEventTemplateRes, error) {
	return &api.CreateEventTemplateCreated{
		Headers: api.CreateEventTemplateCreatedHeaders{
			Location: "/templates/mock-id",
		},
		Body: api.EventTemplateResponse{
			Template: api.EventTemplate{
				ID:          uuid.New(),
				Name:        req.Name,
				Description: req.Description,
				Type:        api.WorldEventType(req.Type),
				Difficulty:  api.WorldEventDifficulty(req.Difficulty),
				IsActive:    true,
				CreatedAt:   time.Now(),
				UpdatedAt:   time.Now(),
			},
		},
	}, nil
}

// GetEventAnalytics implements getEventAnalytics operation.
func (h *Handler) GetEventAnalytics(ctx context.Context, params api.GetEventAnalyticsParams) (api.GetEventAnalyticsRes, error) {
	return &api.GetEventAnalyticsOK{
		Body: api.EventAnalyticsResponse{
			EventID:               params.EventID,
			TotalParticipants:     100,
			CompletedParticipants: 80,
			AverageCompletionTime: "2h 30m",
			AverageScore:          api.NewOptFloat64(85.5),
			ParticipationRate:     api.NewOptFloat64(0.8),
			SatisfactionRating:    api.NewOptFloat64(4.2),
			LastUpdated:           time.Now(),
		},
	}, nil
}
