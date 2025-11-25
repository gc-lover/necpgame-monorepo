package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/pkg/api/world"
)

type WorldServiceInterface interface {
	ListWorldEvents(ctx context.Context, status *string, eventType *string, scale *string, limit, offset int) ([]world.WorldEvent, int, error)
	CreateWorldEvent(ctx context.Context, req *world.CreateWorldEventRequest) (*world.WorldEvent, error)
	GetWorldEvent(ctx context.Context, eventID uuid.UUID) (*world.WorldEvent, error)
	UpdateWorldEvent(ctx context.Context, eventID uuid.UUID, req *world.UpdateWorldEventRequest) (*world.WorldEvent, error)
	DeleteWorldEvent(ctx context.Context, eventID uuid.UUID) error
}

