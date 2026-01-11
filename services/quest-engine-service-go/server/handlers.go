package server

import (
	"log/slog"
	"net/http"
	"time"

	"github.com/labstack/echo/v4"
	"necpgame/services/quest-engine-service-go/pkg/api"
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

// QuestEngineHealthCheck handles health check requests
func (h *Handler) QuestEngineHealthCheck(ctx echo.Context) error {
	status := "healthy"
	timestamp := time.Now().UTC()

	resp := map[string]interface{}{
		"status":    status,
		"timestamp": timestamp,
	}

	return ctx.JSON(http.StatusOK, resp)
}

// ListQuests handles quest listing requests
func (h *Handler) ListQuests(ctx echo.Context, params api.ListQuestsParams) error {
	// Call service
	resp, err := h.service.ListQuests(ctx.Request().Context(), &params)
	if err != nil {
		slog.Error("Failed to list quests", "error", err)
		return ctx.JSON(http.StatusBadRequest, map[string]string{"error": err.Error()})
	}

	return ctx.JSON(http.StatusOK, resp)
}

// CreateQuest handles quest creation requests
func (h *Handler) CreateQuest(ctx echo.Context) error {
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

// GetQuest handles quest retrieval requests
func (h *Handler) GetQuest(ctx echo.Context, questID openapi_types.UUID) error {
	// Call service
	resp, err := h.service.GetQuest(ctx.Request().Context(), questID)
	if err != nil {
		slog.Error("Failed to get quest", "quest_id", questID, "error", err)
		return ctx.JSON(http.StatusNotFound, map[string]string{"error": "Quest not found"})
	}

	return ctx.JSON(http.StatusOK, resp)
}

// UpdateQuest handles quest update requests
func (h *Handler) UpdateQuest(ctx echo.Context, questID openapi_types.UUID) error {
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

// DeleteQuest handles quest deletion requests
func (h *Handler) DeleteQuest(ctx echo.Context, questID openapi_types.UUID) error {
	// Call service
	err := h.service.DeleteQuest(ctx.Request().Context(), questID)
	if err != nil {
		slog.Error("Failed to delete quest", "quest_id", questID, "error", err)
		return ctx.JSON(http.StatusNotFound, map[string]string{"error": "Quest not found"})
	}

	return ctx.JSON(http.StatusNoContent, nil)
}

// NewQuestEngineServer creates the API server with all handlers
func NewQuestEngineServer(service Service) api.ServerInterface {
	return NewHandler(service)
}