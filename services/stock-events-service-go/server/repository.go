package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/stock-events-service-go/pkg/api"
	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type Repository interface {
	GetStockEventImpacts(ctx context.Context, stockID uuid.UUID, status *string, limit, offset int) ([]api.EventImpact, int, error)
	GetStockEventHistory(ctx context.Context, stockID uuid.UUID, eventType *string, fromDate, toDate *time.Time, limit, offset int) ([]api.StockEventHistoryEntry, int, error)
	GetAllActiveImpacts(ctx context.Context, eventType *string, limit, offset int) ([]api.EventImpact, int, error)
	ApplyEventImpact(ctx context.Context, request *api.EventApplicationRequest) (*api.EventImpact, error)
	SimulateEventImpact(ctx context.Context, request *api.EventSimulationRequest) (*api.EventSimulationResult, error)
	ReverseEventImpact(ctx context.Context, impactID uuid.UUID) error
}

type InMemoryRepository struct {
	logger      *logrus.Logger
	impacts     map[uuid.UUID]*api.EventImpact
	history     map[uuid.UUID][]api.StockEventHistoryEntry
}

func NewInMemoryRepository(logger *logrus.Logger) *InMemoryRepository {
	return &InMemoryRepository{
		logger:  logger,
		impacts: make(map[uuid.UUID]*api.EventImpact),
		history: make(map[uuid.UUID][]api.StockEventHistoryEntry),
	}
}

func (r *InMemoryRepository) GetStockEventImpacts(ctx context.Context, stockID uuid.UUID, status *string, limit, offset int) ([]api.EventImpact, int, error) {
	var filtered []api.EventImpact
	for _, impact := range r.impacts {
		if uuid.UUID(impact.StockId) == stockID {
			if status != nil && string(impact.Status) != *status {
				continue
			}
			filtered = append(filtered, *impact)
		}
	}

	total := len(filtered)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.EventImpact{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) GetStockEventHistory(ctx context.Context, stockID uuid.UUID, eventType *string, fromDate, toDate *time.Time, limit, offset int) ([]api.StockEventHistoryEntry, int, error) {
	history, exists := r.history[stockID]
	if !exists {
		return []api.StockEventHistoryEntry{}, 0, nil
	}

	var filtered []api.StockEventHistoryEntry
	for _, entry := range history {
		if eventType != nil && string(entry.EventType) != *eventType {
			continue
		}
		if fromDate != nil && entry.AppliedAt.Before(*fromDate) {
			continue
		}
		if toDate != nil && entry.AppliedAt.After(*toDate) {
			continue
		}
		filtered = append(filtered, entry)
	}

	total := len(filtered)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.StockEventHistoryEntry{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) GetAllActiveImpacts(ctx context.Context, eventType *string, limit, offset int) ([]api.EventImpact, int, error) {
	var filtered []api.EventImpact
	for _, impact := range r.impacts {
		if impact.Status != api.EventImpactStatusActive {
			continue
		}
		if eventType != nil && string(impact.EventType) != *eventType {
			continue
		}
		filtered = append(filtered, *impact)
	}

	total := len(filtered)
	start := offset
	if start > total {
		start = total
	}
	end := start + limit
	if end > total {
		end = total
	}

	if start >= end {
		return []api.EventImpact{}, total, nil
	}

	return filtered[start:end], total, nil
}

func (r *InMemoryRepository) ApplyEventImpact(ctx context.Context, request *api.EventApplicationRequest) (*api.EventImpact, error) {
	impactID := uuid.New()
	now := time.Now()
	expiresAt := now.Add(time.Duration(168) * time.Hour)

	modifiers := make([]api.EventModifier, 0)
	if request.Modifiers != nil {
		for _, m := range *request.Modifiers {
			modifier := api.EventModifier{
				Type:          api.EventModifierType(m.Type),
				EffectPercent: 0.0,
			}
			modifiers = append(modifiers, modifier)
		}
	}

	impact := &api.EventImpact{
		Id:                 openapi_types.UUID(impactID),
		StockId:            request.StockId,
		StockSymbol:        "",
		EventType:          request.EventType,
		EventId:            request.EventId,
		EventName:          request.EventName,
		BaseImpactPercent:  request.BaseImpactPercent,
		Modifiers:          &modifiers,
		TotalImpactPercent: request.BaseImpactPercent,
		DurationType:       request.DurationType,
		DurationHours:      168,
		DecayCurve:         request.DecayCurve,
		AppliedAt:          now,
		ExpiresAt:          &expiresAt,
		CurrentEffectPercent: &request.BaseImpactPercent,
		Status:             api.EventImpactStatusActive,
		CreatedAt:          &now,
		UpdatedAt:          &now,
	}

	r.impacts[impactID] = impact
	return impact, nil
}

func (r *InMemoryRepository) SimulateEventImpact(ctx context.Context, request *api.EventSimulationRequest) (*api.EventSimulationResult, error) {
	duration := 168
	simulation := &api.EventSimulationResult{
		BaseImpactPercent:   &request.BaseImpactPercent,
		TotalImpactPercent:  &request.BaseImpactPercent,
		DurationHours:       &duration,
		DecayCurve:          &request.DecayCurve,
	}
	return simulation, nil
}

func (r *InMemoryRepository) ReverseEventImpact(ctx context.Context, impactID uuid.UUID) error {
	impact, exists := r.impacts[impactID]
	if !exists {
		return nil
	}

	now := time.Now()
	impact.Status = api.EventImpactStatusReversed
	impact.UpdatedAt = &now
	return nil
}

