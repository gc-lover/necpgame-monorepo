package server

import (
	"log/slog"
	"net/http"
	"time"

	"github.com/labstack/echo/v4"
	"necpgame/services/interactive-object-manager-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

// Handler implements the API handlers
type Handler struct {
	service Service
}

// NewHandler creates a new handler instance
func NewHandler(service Service) *Handler {
	return &Handler{
		service: service,
	}
}

// InteractiveObjectManagerHealthCheck handles health check requests
func (h *Handler) InteractiveObjectManagerHealthCheck(ctx echo.Context) error {
	status := "healthy"
	timestamp := time.Now().UTC()

	resp := map[string]interface{}{
		"status":    status,
		"timestamp": timestamp,
	}

	return ctx.JSON(http.StatusOK, resp)
}

// ListInteractiveObjects handles object listing requests
func (h *Handler) ListInteractiveObjects(ctx echo.Context, params api.ListInteractiveObjectsParams) error {
	// Call service
	resp, err := h.service.ListQuests(ctx.Request().Context(), &params)
	if err != nil {
		slog.Error("Failed to list quests", "error", err)
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": err.Error()})
	}

	return ctx.JSON(http.StatusOK, resp)
}

// CreateInteractiveObject handles object creation requests
func (h *Handler) CreateInteractiveObject(ctx echo.Context) error {
	var req api.CreateQuestJSONRequestBody
	if err := ctx.Bind(&req); err != nil {
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": "Invalid request body"})
	}

	// Call service
	resp, err := h.service.CreateQuest(ctx.Request().Context(), &req)
	if err != nil {
		slog.Error("Failed to create quest", "error", err)
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": err.Error()})
	}

	return ctx.JSON(http.StatusOK, resp)
}

// GetInteractiveObject handles object retrieval requests
func (h *Handler) GetInteractiveObject(ctx echo.Context, objectId openapi_types.UUID) error {
	// Call service
	resp, err := h.service.GetQuest(ctx.Request().Context(), questID)
	if err != nil {
		slog.Error("Failed to get quest", "quest_id", questID, "error", err)
		return ctx.JSON(http.StatusNotFound, map[string]string{"error": "Quest not found"})
	}

	return ctx.JSON(http.StatusOK, resp)
}

// UpdateInteractiveObject handles object update requests
func (h *Handler) UpdateInteractiveObject(ctx echo.Context, objectId openapi_types.UUID) error {
	var req api.UpdateQuestJSONRequestBody
	if err := ctx.Bind(&req); err != nil {
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": "Invalid request body"})
	}

	// Call service
	resp, err := h.service.UpdateQuest(ctx.Request().Context(), questID, &req)
	if err != nil {
		slog.Error("Failed to update quest", "quest_id", questID, "error", err)
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": err.Error()})
	}

	return ctx.JSON(http.StatusOK, resp)
}

// DeleteInteractiveObject handles object deletion requests
func (h *Handler) DeleteInteractiveObject(ctx echo.Context, objectId openapi_types.UUID) error {
	// Call service
	err := h.service.DeleteQuest(ctx.Request().Context(), questID)
	if err != nil {
		slog.Error("Failed to delete quest", "quest_id", questID, "error", err)
		return ctx.JSON(http.StatusNotFound, map[string]string{"error": "Quest not found"})
	}

	return ctx.JSON(http.StatusNoContent, nil)
}

// NewInteractiveObjectManagerServer creates the API server with all handlers
func NewInteractiveObjectManagerServer(service Service) api.ServerInterface {
	return NewHandler(service)
}