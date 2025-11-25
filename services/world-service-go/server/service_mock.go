package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/pkg/api/world"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type MockWorldService struct{}

func NewMockWorldService() *MockWorldService {
	return &MockWorldService{}
}

func (m *MockWorldService) ListWorldEvents(ctx context.Context, status *string, eventType *string, scale *string, limit, offset int) ([]world.WorldEvent, int, error) {
	return []world.WorldEvent{}, 0, nil
}

func (m *MockWorldService) CreateWorldEvent(ctx context.Context, req *world.CreateWorldEventRequest) (*world.WorldEvent, error) {
	now := time.Now()
	newUUID := openapi_types.UUID(uuid.New())
	event := &world.WorldEvent{
		Id:          newUUID,
		Title:       req.Title,
		Description: req.Description,
		Type:        req.Type,
		Status:      world.PLANNED,
		Frequency:   req.Frequency,
		Scale:       req.Scale,
		CreatedAt:   now,
		UpdatedAt:   now,
	}
	return event, nil
}

func (m *MockWorldService) GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*world.WorldEvent, error) {
	now := time.Now()
	uuidType := openapi_types.UUID(eventID)
	event := &world.WorldEvent{
		Id:          uuidType,
		Title:       "Mock Event",
		Description: "Mock Description",
		Type:        world.STORY,
		Status:      world.ACTIVE,
		Frequency:   world.ONETIME,
		Scale:       world.GLOBAL,
		CreatedAt:   now,
		UpdatedAt:   now,
	}
	return event, nil
}

func (m *MockWorldService) UpdateWorldEvent(ctx context.Context, eventID uuid.UUID, req *world.UpdateWorldEventRequest) (*world.WorldEvent, error) {
	now := time.Now()
	uuidType := openapi_types.UUID(eventID)
	event := &world.WorldEvent{
		Id:          uuidType,
		Title:       "Updated Event",
		Description: "Updated Description",
		Type:        world.STORY,
		Status:      world.ACTIVE,
		Frequency:   world.ONETIME,
		Scale:       world.GLOBAL,
		CreatedAt:   now,
		UpdatedAt:   now,
	}
	if req.Title != nil {
		event.Title = *req.Title
	}
	if req.Type != nil {
		event.Type = *req.Type
	}
	return event, nil
}

func (m *MockWorldService) DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error {
	return nil
}

