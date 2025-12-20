// Package server Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-hacking-service-go/pkg/api"
	"github.com/google/uuid"
)

const DBTimeout = 50 * time.Millisecond

var (
	ErrSystemOverheated = errors.New("system overheated, cannot hack")
	ErrDemonOverheated  = errors.New("system overheated, cannot activate demon")
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service HackingService
}

// NewHandlers creates new handlers
func NewHandlers() *Handlers {
	repo := NewInMemoryRepository()
	logger := GetLogger()
	service := NewHackingService(repo, logger)
	return &Handlers{service: service}
}

// HackTarget - TYPED response!
func (h *Handlers) HackTarget(ctx context.Context, req *api.HackTargetRequest) (api.HackTargetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID, err := h.getPlayerIDFromContext()
	if err != nil {
		return &api.HackTargetUnauthorized{}, nil
	}

	result, err := h.service.HackTarget(ctx, playerID, req)
	if err != nil {
		if err == ErrSystemOverheated {
			return &api.HackTargetBadRequest{}, nil
		}
		return &api.HackTargetInternalServerError{}, err
	}

	return result, nil
}

// ActivateCountermeasures - TYPED response!
func (h *Handlers) ActivateCountermeasures(ctx context.Context, req *api.CountermeasureRequest) (api.ActivateCountermeasuresRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID, err := h.getPlayerIDFromContext()
	if err != nil {
		return &api.ActivateCountermeasuresUnauthorized{}, nil
	}

	result, err := h.service.ActivateCountermeasures(ctx, playerID, req)
	if err != nil {
		return &api.ActivateCountermeasuresInternalServerError{}, err
	}

	return result, nil
}

// GetDemons - TYPED response!
func (h *Handlers) GetDemons(ctx context.Context) (api.GetDemonsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID, err := h.getPlayerIDFromContext()
	if err != nil {
		return &api.GetDemonsUnauthorized{}, nil
	}

	demons, err := h.service.GetDemons(ctx, playerID)
	if err != nil {
		return &api.GetDemonsInternalServerError{}, err
	}

	return &api.GetDemonsOK{Demons: demons}, nil
}

// ActivateDemon - TYPED response!
func (h *Handlers) ActivateDemon(ctx context.Context, req *api.ActivateDemonRequest, params api.ActivateDemonParams) (api.ActivateDemonRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID, err := h.getPlayerIDFromContext()
	if err != nil {
		return &api.ActivateDemonUnauthorized{}, nil
	}

	result, err := h.service.ActivateDemon(ctx, playerID, params.DemonID, req)
	if err != nil {
		if err == ErrDemonOverheated {
			return &api.ActivateDemonBadRequest{}, nil
		}
		return &api.ActivateDemonInternalServerError{}, err
	}

	return result, nil
}

// GetICELevel - TYPED response!
func (h *Handlers) GetICELevel(ctx context.Context, params api.GetICELevelParams) (api.GetICELevelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	info, err := h.service.GetICELevel(ctx, params.TargetID)
	if err != nil {
		return &api.GetICELevelInternalServerError{}, err
	}

	return info, nil
}

// GetNetworkInfo - TYPED response!
func (h *Handlers) GetNetworkInfo(ctx context.Context, params api.GetNetworkInfoParams) (api.GetNetworkInfoRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	info, err := h.service.GetNetworkInfo(ctx, params.NetworkID)
	if err != nil {
		return &api.GetNetworkInfoInternalServerError{}, err
	}

	return info, nil
}

// AccessNetwork - TYPED response!
func (h *Handlers) AccessNetwork(ctx context.Context, req *api.NetworkAccessRequest, params api.AccessNetworkParams) (api.AccessNetworkRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID, err := h.getPlayerIDFromContext()
	if err != nil {
		return &api.AccessNetworkUnauthorized{}, nil
	}

	result, err := h.service.AccessNetwork(ctx, playerID, params.NetworkID, req)
	if err != nil {
		return &api.AccessNetworkInternalServerError{}, err
	}

	return result, nil
}

// GetOverheatStatus - TYPED response!
func (h *Handlers) GetOverheatStatus(ctx context.Context) (api.GetOverheatStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID, err := h.getPlayerIDFromContext()
	if err != nil {
		return &api.GetOverheatStatusUnauthorized{}, nil
	}

	status, err := h.service.GetOverheatStatus(ctx, playerID)
	if err != nil {
		return &api.GetOverheatStatusInternalServerError{}, err
	}

	return status, nil
}

func (h *Handlers) getPlayerIDFromContext() (uuid.UUID, error) {
	// TODO: Extract from JWT token in context
	return uuid.New(), nil
}
