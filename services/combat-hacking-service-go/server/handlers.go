// Issue: #57
package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/combat-hacking-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type HackingHandlers struct {
	service HackingService
	logger  *logrus.Logger
}

func NewHackingHandlers() *HackingHandlers {
	repo := NewInMemoryRepository()
	logger := GetLogger()
	service := NewHackingService(repo, logger)

	return &HackingHandlers{
		service: service,
		logger:  logger,
	}
}

func (h *HackingHandlers) HackTarget(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.HackTargetRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID, err := h.getPlayerIDFromContext(ctx)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "player ID not found")
		return
	}

	result, err := h.service.HackTarget(ctx, playerID, req)
	if err != nil {
		if err.Error() == "system overheated, cannot hack" {
			h.respondError(w, http.StatusConflict, err.Error())
			return
		}
		h.respondError(w, http.StatusInternalServerError, "failed to hack target")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *HackingHandlers) ActivateCountermeasures(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.CountermeasureRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID, err := h.getPlayerIDFromContext(ctx)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "player ID not found")
		return
	}

	result, err := h.service.ActivateCountermeasures(ctx, playerID, req)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to activate countermeasures")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *HackingHandlers) GetDemons(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	playerID, err := h.getPlayerIDFromContext(ctx)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "player ID not found")
		return
	}

	demons, err := h.service.GetDemons(ctx, playerID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to get demons")
		return
	}

	response := map[string]interface{}{
		"demons": demons,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) ActivateDemon(w http.ResponseWriter, r *http.Request, demonId openapi_types.UUID) {
	ctx := r.Context()

	var req api.ActivateDemonRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID, err := h.getPlayerIDFromContext(ctx)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "player ID not found")
		return
	}

	demonUUID, err := uuid.Parse(demonId.String())
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid demon ID")
		return
	}

	result, err := h.service.ActivateDemon(ctx, playerID, demonUUID, req)
	if err != nil {
		if err.Error() == "system overheated, cannot activate demon" {
			h.respondError(w, http.StatusConflict, err.Error())
			return
		}
		h.respondError(w, http.StatusInternalServerError, "failed to activate demon")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *HackingHandlers) GetICELevel(w http.ResponseWriter, r *http.Request, targetId openapi_types.UUID) {
	ctx := r.Context()

	targetUUID, err := uuid.Parse(targetId.String())
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid target ID")
		return
	}

	info, err := h.service.GetICELevel(ctx, targetUUID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to get ICE level")
		return
	}

	h.respondJSON(w, http.StatusOK, info)
}

func (h *HackingHandlers) GetNetworkInfo(w http.ResponseWriter, r *http.Request, networkId openapi_types.UUID) {
	ctx := r.Context()

	networkUUID, err := uuid.Parse(networkId.String())
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid network ID")
		return
	}

	info, err := h.service.GetNetworkInfo(ctx, networkUUID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to get network info")
		return
	}

	h.respondJSON(w, http.StatusOK, info)
}

func (h *HackingHandlers) AccessNetwork(w http.ResponseWriter, r *http.Request, networkId openapi_types.UUID) {
	ctx := r.Context()

	var req api.NetworkAccessRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID, err := h.getPlayerIDFromContext(ctx)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "player ID not found")
		return
	}

	networkUUID, err := uuid.Parse(networkId.String())
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid network ID")
		return
	}

	result, err := h.service.AccessNetwork(ctx, playerID, networkUUID, req)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to access network")
		return
	}

	h.respondJSON(w, http.StatusOK, result)
}

func (h *HackingHandlers) GetOverheatStatus(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	playerID, err := h.getPlayerIDFromContext(ctx)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "player ID not found")
		return
	}

	status, err := h.service.GetOverheatStatus(ctx, playerID)
	if err != nil {
		h.respondError(w, http.StatusInternalServerError, "failed to get overheat status")
		return
	}

	h.respondJSON(w, http.StatusOK, status)
}

func (h *HackingHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *HackingHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(errorResponse); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON error response")
	}
}

func (h *HackingHandlers) getPlayerIDFromContext(ctx context.Context) (uuid.UUID, error) {
	return uuid.New(), nil
}

