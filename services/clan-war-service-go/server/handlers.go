// Issue: #1607, ogen migration
package server

import (
	"context"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/clan-war-service-go/models"
	clanwarapi "github.com/necpgame/clan-war-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	clanWarService ClanWarServiceInterface
	logger         *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	clanWarPool          sync.Pool
	activeWarsResponsePool sync.Pool
	warResolutionPool    sync.Pool
}

func NewHandlers(clanWarService ClanWarServiceInterface) *Handlers {
	h := &Handlers{
		clanWarService: clanWarService,
		logger:         GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.clanWarPool = sync.Pool{
		New: func() interface{} {
			return &clanwarapi.ClanWar{}
		},
	}
	h.activeWarsResponsePool = sync.Pool{
		New: func() interface{} {
			return &clanwarapi.ActiveWarsResponse{}
		},
	}
	h.warResolutionPool = sync.Pool{
		New: func() interface{} {
			return &clanwarapi.WarResolution{}
		},
	}

	return h
}

func (h *Handlers) DeclareWar(ctx context.Context, req *clanwarapi.DeclareWarRequest) (clanwarapi.DeclareWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	internalReq := convertDeclareWarRequestFromAPI(req)
	war, err := h.clanWarService.DeclareWar(ctx, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to declare war")
		return &clanwarapi.DeclareWarInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to declare war",
		}, nil
	}

	apiWarValue := convertClanWarToAPI(war)
	// Issue: #1607 - Use memory pooling (convert value to pointer)
	apiWar := h.clanWarPool.Get().(*clanwarapi.ClanWar)
	// Note: Not returning to pool - struct is returned to caller
	*apiWar = apiWarValue
	return apiWar, nil
}

func (h *Handlers) GetWar(ctx context.Context, params clanwarapi.GetWarParams) (clanwarapi.GetWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	war, err := h.clanWarService.GetWar(ctx, params.WarID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get war")
		return &clanwarapi.GetWarInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get war",
		}, nil
	}

	if war == nil {
		return &clanwarapi.GetWarNotFound{
			Error:   "Not Found",
			Message: "war not found",
		}, nil
	}

	apiWarValue := convertClanWarToAPI(war)
	// Issue: #1607 - Use memory pooling (convert value to pointer)
	apiWar := h.clanWarPool.Get().(*clanwarapi.ClanWar)
	// Note: Not returning to pool - struct is returned to caller
	*apiWar = apiWarValue
	return apiWar, nil
}

func (h *Handlers) GetActiveWars(ctx context.Context, params clanwarapi.GetActiveWarsParams) (clanwarapi.GetActiveWarsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var guildID *uuid.UUID
	if params.GuildID.Set {
		guildID = &params.GuildID.Value
	}

	status := models.WarStatusOngoing
	limit := 20
	offset := 0
	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	wars, total, err := h.clanWarService.ListWars(ctx, guildID, &status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active wars")
		return &clanwarapi.GetActiveWarsInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to get active wars",
		}, nil
	}

	apiResponseValue := convertWarListToAPI(wars, total)
	// Issue: #1607 - Use memory pooling (convert value to pointer)
	apiResponse := h.activeWarsResponsePool.Get().(*clanwarapi.ActiveWarsResponse)
	// Note: Not returning to pool - struct is returned to caller
	*apiResponse = apiResponseValue
	return apiResponse, nil
}

func (h *Handlers) CancelWar(ctx context.Context, req *clanwarapi.CancelWarRequest, params clanwarapi.CancelWarParams) (clanwarapi.CancelWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement cancel war logic
	h.logger.Warn("CancelWar not yet implemented")
	return &clanwarapi.CancelWarInternalServerError{
		Error:   "Internal Server Error",
		Message: "cancel war not implemented",
	}, nil
}

func (h *Handlers) ResolveWar(ctx context.Context, params clanwarapi.ResolveWarParams) (clanwarapi.ResolveWarRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.clanWarService.CompleteWar(ctx, params.WarID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to resolve war")
		return &clanwarapi.ResolveWarInternalServerError{
			Error:   "Internal Server Error",
			Message: "failed to resolve war",
		}, nil
	}

	war, err := h.clanWarService.GetWar(ctx, params.WarID)
	if err != nil || war == nil {
		return &clanwarapi.ResolveWarNotFound{
			Error:   "Not Found",
			Message: "war not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	resolution := h.warResolutionPool.Get().(*clanwarapi.WarResolution)
	// Note: Not returning to pool - struct is returned to caller

	resolution.WarID = clanwarapi.NewOptUUID(war.ID)
	resolution.AttackerScore = clanwarapi.NewOptInt(war.AttackerScore)
	resolution.DefenderScore = clanwarapi.NewOptInt(war.DefenderScore)
	resolution.ResolvedAt = clanwarapi.NewOptDateTime(war.UpdatedAt)

	if war.WinnerGuildID != nil {
		resolution.WinnerGuildID = clanwarapi.NewOptUUID(*war.WinnerGuildID)
	}

	return resolution, nil
}

