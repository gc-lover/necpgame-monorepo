package service

import (
	"context"
	"time"

	"github.com/google/uuid"

	api "necpgame/services/world-event-service-go/api"
)

// CreateExample implements createExample operation.
func (h *Handler) CreateExample(ctx context.Context, req *api.CreateExampleRequest) (api.CreateExampleRes, error) {
	return &api.ExampleCreatedHeaders{
		Response: api.ExampleResponse{
			Example: api.Example{
				ID:        uuid.New(),
				Name:      req.Name,
				Status:    api.ExampleStatusActive,
				CreatedAt: time.Now(),
			},
		},
	}, nil
}

// DeleteExample implements deleteExample operation.
func (h *Handler) DeleteExample(ctx context.Context, params api.DeleteExampleParams) (api.DeleteExampleRes, error) {
	return &api.ExampleDeleted{}, nil
}

// ExampleDomainBatchHealthCheck implements exampleDomainBatchHealthCheck operation.
func (h *Handler) ExampleDomainBatchHealthCheck(ctx context.Context, req *api.ExampleDomainBatchHealthCheckReq) (api.ExampleDomainBatchHealthCheckRes, error) {
	return &api.ExampleDomainBatchHealthCheckOKHeaders{
		Response: api.ExampleDomainBatchHealthCheckOK{
			TotalTimeMs: 10,
		},
	}, nil
}

// GetExample implements getExample operation.
func (h *Handler) GetExample(ctx context.Context, params api.GetExampleParams) (api.GetExampleRes, error) {
	return &api.ExampleRetrievedHeaders{
		Response: api.ExampleResponse{
			Example: api.Example{
				ID:        params.ExampleID,
				Name:      "Mock Example",
				Status:    api.ExampleStatusActive,
				CreatedAt: time.Now(),
			},
		},
	}, nil
}

// ListWorldEvents implements listWorldEvents operation.
func (h *Handler) ListWorldEvents(ctx context.Context, params api.ListWorldEventsParams) (api.ListWorldEventsRes, error) {
	return &api.ExampleListSuccessHeaders{
		Response: api.ExampleListResponse{
			Examples:   []api.Example{},
			TotalCount: 0,
			HasMore:    false,
		},
	}, nil
}

// UpdateExample implements updateExample operation.
func (h *Handler) UpdateExample(ctx context.Context, req *api.UpdateExampleRequest, params api.UpdateExampleParams) (api.UpdateExampleRes, error) {
	name := "Updated Example"
	if v, ok := req.Name.Get(); ok {
		name = v
	}

	return &api.ExampleUpdatedHeaders{
		Response: api.ExampleResponse{
			Example: api.Example{
				ID:        params.ExampleID,
				Name:      name,
				Status:    api.ExampleStatusActive,
				CreatedAt: time.Now(),
				UpdatedAt: api.NewOptDateTime(time.Now()),
			},
		},
	}, nil
}

// WorldEventServiceHealthCheck implements worldEventServiceHealthCheck operation.
func (h *Handler) WorldEventServiceHealthCheck(ctx context.Context, params api.WorldEventServiceHealthCheckParams) (api.WorldEventServiceHealthCheckRes, error) {
	return &api.WorldEventServiceHealthCheckOKHeaders{
		Response: api.WorldEventServiceHealthCheckOK{
			Status:    api.WorldEventServiceHealthCheckOKStatusHealthy,
			Timestamp: time.Now(),
		},
	}, nil
}

// WorldEventServiceHealthWebSocket implements worldEventServiceHealthWebSocket operation.
func (h *Handler) WorldEventServiceHealthWebSocket(ctx context.Context, params api.WorldEventServiceHealthWebSocketParams) (api.WorldEventServiceHealthWebSocketRes, error) {
	return &api.WorldEventServiceHealthWebSocketOK{
		WebsocketURL: api.NewOptString("ws://localhost:8080/api/v1/world-event/health/ws"),
	}, nil
}
