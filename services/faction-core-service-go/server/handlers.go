// Package server Issue: #1442
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
)

// DBTimeout Context timeout constants (Issue #1604)
const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// CreateFaction implements createFaction operation
func (h *Handlers) CreateFaction(ctx context.Context, req *api.CreateFactionRequest) (api.CreateFactionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	faction, err := h.service.CreateFaction(ctx, *req)
	if err != nil {
		return &api.CreateFactionBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return faction, nil
}

// GetFaction implements getFaction operation
func (h *Handlers) GetFaction(ctx context.Context, params api.GetFactionParams) (api.GetFactionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	details, err := h.service.GetFaction(ctx, params.FactionId.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{
				Error:   "NOT_FOUND",
				Message: "Faction not found",
			}, nil
		}
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return details, nil
}

// UpdateFaction implements updateFaction operation
func (h *Handlers) UpdateFaction(ctx context.Context, req *api.UpdateFactionRequest, params api.UpdateFactionParams) (api.UpdateFactionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	faction, err := h.service.UpdateFaction(ctx, params.FactionId.String(), *req)
	if err != nil {
		if err == ErrNotFound {
			return &api.UpdateFactionNotFound{
				Error:   "NOT_FOUND",
				Message: "Faction not found",
			}, nil
		}
		return &api.UpdateFactionBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return faction, nil
}

// DeleteFaction implements deleteFaction operation
func (h *Handlers) DeleteFaction(ctx context.Context, params api.DeleteFactionParams) (api.DeleteFactionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteFaction(ctx, params.FactionId.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{
				Error:   "NOT_FOUND",
				Message: "Faction not found",
			}, nil
		}
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return &api.DeleteFactionNoContent{}, nil
}

// ListFactions implements listFactions operation
func (h *Handlers) ListFactions(ctx context.Context, params api.ListFactionsParams) (*api.ListFactionsOK, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	factions, pagination, err := h.service.ListFactions(ctx, params)
	if err != nil {
		return nil, err
	}

	var paginationResp api.OptPaginationResponse
	if pagination != nil {
		page := 1
		if params.Page.Set {
			page = params.Page.Value
		}
		limit := 10
		if params.Limit.Set {
			limit = params.Limit.Value
		}
		total := 0
		if totalVal, ok := pagination["total"].(int); ok {
			total = totalVal
		}
		paginationResp = api.NewOptPaginationResponse(api.PaginationResponse{
			Total:   total,
			Limit:   api.NewOptInt(limit),
			Offset:  api.NewOptInt((page - 1) * limit),
			HasMore: api.NewOptBool(total > page*limit),
		})
	}

	return &api.ListFactionsOK{
		Factions:   factions,
		Pagination: paginationResp,
	}, nil
}

// GetHierarchy implements getHierarchy operation
func (h *Handlers) GetHierarchy(ctx context.Context, params api.GetHierarchyParams) (api.GetHierarchyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	hierarchy, err := h.service.GetHierarchy(ctx, params.FactionId.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.Error{
				Error:   "NOT_FOUND",
				Message: "Faction not found",
			}, nil
		}
		return &api.Error{
			Error:   "INTERNAL_SERVER_ERROR",
			Message: err.Error(),
		}, nil
	}

	return hierarchy, nil
}

// UpdateHierarchy implements updateHierarchy operation
func (h *Handlers) UpdateHierarchy(ctx context.Context, req *api.UpdateHierarchyRequest, params api.UpdateHierarchyParams) (api.UpdateHierarchyRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	hierarchy, err := h.service.UpdateHierarchy(ctx, params.FactionId.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.UpdateHierarchyNotFound{
				Error:   "NOT_FOUND",
				Message: "Faction not found",
			}, nil
		}
		return &api.UpdateHierarchyBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return hierarchy, nil
}
