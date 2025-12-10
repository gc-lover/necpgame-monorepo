// Issue: #1510 - Combat Acrobatics Wall Run handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-acrobatics-wall-run-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// GetWallRunSurfaces implements getWallRunSurfaces operation.
//
// Возвращает список доступных поверхностей для Wall Run
// в текущей области персонажа.
//
// GET /wall-run/surfaces
func (h *Handlers) GetWallRunSurfaces(ctx context.Context, params api.GetWallRunSurfacesParams) (api.GetWallRunSurfacesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	surfaces, err := h.service.GetWallRunSurfaces(ctx, params)
	if err != nil {
		return &api.GetWallRunSurfacesInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return surfaces, nil
}

// StartWallRun implements startWallRun operation.
//
// Начинает бег по стене. Автоматически определяет
// подходящую поверхность
// и начинает отслеживание состояния с расходом
// выносливости.
//
// POST /wall-run/start
func (h *Handlers) StartWallRun(ctx context.Context, req api.OptStartWallRunRequest) (api.StartWallRunRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	session, err := h.service.StartWallRun(ctx, req)
	if err != nil {
		return &api.StartWallRunBadRequest{
			Error:   "BadRequest",
			Message: err.Error(),
		}, nil
	}

	return session, nil
}

// StopWallRun implements stopWallRun operation.
//
// Останавливает бег по стене. Сохраняет финальное
// состояние
// и восстанавливает выносливость.
//
// POST /wall-run/stop
func (h *Handlers) StopWallRun(ctx context.Context) (api.StopWallRunRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.StopWallRun(ctx)
	if err != nil {
		return &api.StopWallRunBadRequest{
			Error:   "BadRequest",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// WallKick implements wallKick operation.
//
// Выполняет отталкивание от стены во время Wall Run.
// Изменяет направление движения и применяет импульс.
//
// POST /wall-run/kick
func (h *Handlers) WallKick(ctx context.Context, req *api.WallKickRequest) (api.WallKickRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.WallKick(ctx, req)
	if err != nil {
		return &api.WallKickBadRequest{
			Error:   "BadRequest",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}
