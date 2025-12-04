// Issue: #44 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	api "github.com/gc-lover/necpgame-monorepo/services/world-events-analytics-service-go/pkg/api"
	"go.uber.org/zap"
)

const (
	DBTimeout = 50 * time.Millisecond
)

type Handlers struct {
	service Service
	logger  *zap.Logger
}

func NewHandlers(service Service, logger *zap.Logger) *Handlers {
	return &Handlers{service: service, logger: logger}
}

func (h *Handlers) GetWorldEventMetrics(ctx context.Context, params api.GetWorldEventMetricsParams) (api.GetWorldEventMetricsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	eventID := uuid.UUID(params.ID)

	metrics, err := h.service.GetEventMetrics(ctx, eventID)
	if err != nil || metrics == nil {
		h.logger.Error("Failed to get metrics", zap.Error(err))
		return &api.GetWorldEventMetricsNotFound{
			Error:   "NotFound",
			Message: "Metrics not found",
		}, nil
	}

	playerCount := int(metrics.ParticipantCount)
	engagementRate := float32(metrics.PlayerEngagement)
	economicImpact := float32(0.0)
	socialImpact := float32(0.0)

	return &api.WorldEventMetrics{
		EventID:        params.ID,
		PlayerCount:    api.NewOptInt(playerCount),
		EngagementRate: api.NewOptFloat32(engagementRate),
		EconomicImpact: api.NewOptFloat32(economicImpact),
		SocialImpact:   api.NewOptFloat32(socialImpact),
		Uptime:         0,
	}, nil
}

func (h *Handlers) GetWorldEventEngagement(ctx context.Context, params api.GetWorldEventEngagementParams) (api.GetWorldEventEngagementRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	return &api.WorldEventEngagement{
		EventID:                params.ID,
		EngagementRate:         api.NewOptFloat32(0.0),
		ActivePlayers:          api.NewOptInt(0),
		TotalPlayers:           api.NewOptInt(0),
		AverageSessionDuration: api.NewOptDuration(0),
		ParticipationRate:      api.NewOptFloat32(0.0),
	}, nil
}

func (h *Handlers) GetWorldEventImpact(ctx context.Context, params api.GetWorldEventImpactParams) (api.GetWorldEventImpactRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	return &api.WorldEventImpact{
		EventID: params.ID,
		EconomyImpact: api.NewOptWorldEventImpactEconomyImpact(api.WorldEventImpactEconomyImpact{
			PriceChanges:    api.NewOptFloat32(0.0),
			CurrencyChanges: api.NewOptFloat32(0.0),
			TradeVolume:     api.NewOptFloat32(0.0),
		}),
		SocialImpact: api.NewOptWorldEventImpactSocialImpact(api.WorldEventImpactSocialImpact{
			ReputationChanges:       api.NewOptFloat32(0.0),
			FactionRelationsChanges: api.NewOptFloat32(0.0),
		}),
		GameplayImpact: api.NewOptWorldEventImpactGameplayImpact(api.WorldEventImpactGameplayImpact{
			TtkChanges:          api.NewOptFloat32(0.0),
			ContentAvailability: api.NewOptFloat32(0.0),
		}),
	}, nil
}

func (h *Handlers) GetWorldEventAlerts(ctx context.Context) (api.GetWorldEventAlertsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	return &api.WorldEventAlertsResponse{
		Alerts: []api.WorldEventAlertsResponseAlertsItem{},
	}, nil
}
