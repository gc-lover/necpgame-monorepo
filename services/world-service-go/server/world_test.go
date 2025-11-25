package server

import (
	"context"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/pkg/api/world"
	"github.com/stretchr/testify/assert"
)

func TestNewMockWorldService(t *testing.T) {
	service := NewMockWorldService()
	assert.NotNil(t, service)
}

func TestListWorldEvents(t *testing.T) {
	service := NewMockWorldService()
	ctx := context.Background()
	
	events, total, err := service.ListWorldEvents(ctx, nil, nil, nil, 50, 0)
	assert.NoError(t, err)
	assert.NotNil(t, events)
	assert.Equal(t, 0, total)
}

func TestCreateWorldEvent(t *testing.T) {
	service := NewMockWorldService()
	ctx := context.Background()
	
	req := &world.CreateWorldEventRequest{
		Title:       "Test Event",
		Description: "Test Description",
		Type:        world.STORY,
		Frequency:   world.ONETIME,
		Scale:       world.GLOBAL,
	}
	
	event, err := service.CreateWorldEvent(ctx, req)
	assert.NoError(t, err)
	assert.NotNil(t, event)
	assert.Equal(t, req.Title, event.Title)
	assert.Equal(t, world.PLANNED, event.Status)
}

func TestGetWorldEvent(t *testing.T) {
	service := NewMockWorldService()
	ctx := context.Background()
	eventID := uuid.New()
	
	event, err := service.GetWorldEvent(ctx, eventID)
	assert.NoError(t, err)
	assert.NotNil(t, event)
	assert.Equal(t, eventID.String(), event.Id.String())
}

func TestUpdateWorldEvent(t *testing.T) {
	service := NewMockWorldService()
	ctx := context.Background()
	eventID := uuid.New()
	
	newTitle := "Updated Event"
	newType := world.ECONOMIC
	req := &world.UpdateWorldEventRequest{
		Title: &newTitle,
		Type:  &newType,
	}
	
	event, err := service.UpdateWorldEvent(ctx, eventID, req)
	assert.NoError(t, err)
	assert.NotNil(t, event)
	assert.Equal(t, newTitle, event.Title)
}

func TestDeleteWorldEvent(t *testing.T) {
	service := NewMockWorldService()
	ctx := context.Background()
	eventID := uuid.New()
	
	err := service.DeleteWorldEvent(ctx, eventID)
	assert.NoError(t, err)
}

func TestNewHTTPServer(t *testing.T) {
	service := NewMockWorldService()
	server := NewHTTPServer(":8090", service)
	assert.NotNil(t, server)
}

func TestHealthCheck(t *testing.T) {
	service := NewMockWorldService()
	server := NewHTTPServer(":8090", service)
	
	req, _ := http.NewRequest("GET", "/health", nil)
	rr := httptest.NewRecorder()
	server.router.ServeHTTP(rr, req)
	
	assert.Equal(t, http.StatusOK, rr.Code)
	assert.Equal(t, "OK", rr.Body.String())
}

func TestCORSMiddleware(t *testing.T) {
	service := NewMockWorldService()
	server := NewHTTPServer(":8090", service)
	
	req, _ := http.NewRequest("OPTIONS", "/api/v1/world-events", nil)
	req.Header.Set("Origin", "http://example.com")
	req.Header.Set("Access-Control-Request-Method", "GET")
	req.Header.Set("Access-Control-Request-Headers", "Content-Type")
	
	rr := httptest.NewRecorder()
	server.router.ServeHTTP(rr, req)
	
	assert.Equal(t, http.StatusOK, rr.Code)
	assert.Equal(t, "*", rr.Header().Get("Access-Control-Allow-Origin"))
	assert.Equal(t, "GET, POST, PUT, DELETE, OPTIONS", rr.Header().Get("Access-Control-Allow-Methods"))
	assert.Equal(t, "Content-Type, Authorization", rr.Header().Get("Access-Control-Allow-Headers"))
}

func TestWorldEventLifecycle(t *testing.T) {
	service := NewMockWorldService()
	ctx := context.Background()
	
	createReq := &world.CreateWorldEventRequest{
		Title:       "Lifecycle Event",
		Description: "Test lifecycle",
		Type:        world.MILITARY,
		Frequency:   world.ONETIME,
		Scale:       world.REGIONAL,
	}
	
	event, err := service.CreateWorldEvent(ctx, createReq)
	assert.NoError(t, err)
	assert.NotNil(t, event)
	
	eventID := uuid.UUID(event.Id)
	fetchedEvent, err := service.GetWorldEvent(ctx, eventID)
	assert.NoError(t, err)
	assert.NotNil(t, fetchedEvent)
	
	newTitle := "Updated Lifecycle Event"
	updateReq := &world.UpdateWorldEventRequest{
		Title: &newTitle,
	}
	
	updatedEvent, err := service.UpdateWorldEvent(ctx, eventID, updateReq)
	assert.NoError(t, err)
	assert.Equal(t, newTitle, updatedEvent.Title)
	
	err = service.DeleteWorldEvent(ctx, eventID)
	assert.NoError(t, err)
}

