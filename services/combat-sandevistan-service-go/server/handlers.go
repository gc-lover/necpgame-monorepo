// Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sandevistan-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

var (
	ErrSandevistanAlreadyActive = errors.New("sandevistan already active")
	ErrSandevistanNotActive     = errors.New("sandevistan not active")
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service SandevistanService
}

// NewHandlers creates new handlers
func NewHandlers() *Handlers {
	repo := NewInMemoryRepository()
	logger := GetLogger()
	service := NewSandevistanService(repo, logger)
	return &Handlers{service: service}
}

// ActivateSandevistan - TYPED response!
func (h *Handlers) ActivateSandevistan(ctx context.Context, params api.ActivateSandevistanParams) (api.ActivateSandevistanRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	activation, err := h.service.Activate(ctx, params.PlayerID)
	if err != nil {
		if err == ErrSandevistanAlreadyActive {
			return &api.ActivateSandevistanConflict{}, nil
		}
		return &api.ActivateSandevistanInternalServerError{}, err
	}

	return activation, nil
}

// DeactivateSandevistan - TYPED response!
func (h *Handlers) DeactivateSandevistan(ctx context.Context, params api.DeactivateSandevistanParams) (api.DeactivateSandevistanRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if err := h.service.Deactivate(ctx, params.PlayerID); err != nil {
		if err == ErrSandevistanNotActive {
			return &api.DeactivateSandevistanNotFound{}, nil
		}
		return &api.DeactivateSandevistanInternalServerError{}, err
	}

		return &api.DeactivateSandevistanNotFound{}, nil
}

// GetSandevistanStatus - TYPED response!
func (h *Handlers) GetSandevistanStatus(ctx context.Context, params api.GetSandevistanStatusParams) (api.GetSandevistanStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	status, err := h.service.GetStatus(ctx, params.PlayerID)
	if err != nil {
		return &api.GetSandevistanStatusInternalServerError{}, err
	}

	return status, nil
}

// GetTemporalMarks - TYPED response!
func (h *Handlers) GetTemporalMarks(ctx context.Context, params api.GetTemporalMarksParams) (api.GetTemporalMarksRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	marks, err := h.service.GetTemporalMarks(ctx, params.PlayerID)
	if err != nil {
		return &api.GetTemporalMarksInternalServerError{}, err
	}

	return &api.GetTemporalMarksOK{Marks: marks}, nil
}

// ApplyCoolingCartridge - TYPED response!
func (h *Handlers) ApplyCoolingCartridge(ctx context.Context, req *api.ApplyCoolingCartridgeReq, params api.ApplyCoolingCartridgeParams) (api.ApplyCoolingCartridgeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ApplyCooling(ctx, params.PlayerID, req.CartridgeID)
	if err != nil {
		if err.Error() == "no active sandevistan activation" {
			return &api.ApplyCoolingCartridgeNotFound{}, nil
		}
		return &api.ApplyCoolingCartridgeInternalServerError{}, err
	}

	return result, nil
}

// GetHeatStatus - TYPED response!
func (h *Handlers) GetHeatStatus(ctx context.Context, params api.GetHeatStatusParams) (api.GetHeatStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	status, err := h.service.GetHeatStatus(ctx, params.PlayerID)
	if err != nil {
		return &api.GetHeatStatusInternalServerError{}, err
	}

	return status, nil
}

// ApplyCounterplay - TYPED response!
func (h *Handlers) ApplyCounterplay(ctx context.Context, req *api.ApplyCounterplayReq, params api.ApplyCounterplayParams) (api.ApplyCounterplayRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ApplyCounterplay(ctx, params.PlayerID, string(req.EffectType), req.SourcePlayerID)
	if err != nil {
		if err == ErrSandevistanNotActive {
			return &api.ApplyCounterplayNotFound{}, nil
		}
		return &api.ApplyCounterplayInternalServerError{}, err
	}

	return result, nil
}

// SetTemporalMarks - TYPED response!
func (h *Handlers) SetTemporalMarks(ctx context.Context, req *api.SetTemporalMarksReq, params api.SetTemporalMarksParams) (api.SetTemporalMarksRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if err := h.service.SetTemporalMarks(ctx, params.PlayerID, req.TargetIds); err != nil {
		if err.Error() == "too many temporal marks" || err.Error() == "sandevistan not active" {
			return &api.SetTemporalMarksBadRequest{}, nil
		}
		return &api.SetTemporalMarksInternalServerError{}, err
	}

	// TODO: Check correct response type - using BadRequest for now
	return &api.SetTemporalMarksBadRequest{}, nil
}

// UseActionBudget - TYPED response!
func (h *Handlers) UseActionBudget(ctx context.Context, req *api.UseActionBudgetReq, params api.UseActionBudgetParams) (api.UseActionBudgetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UseActionBudget(ctx, params.PlayerID, req.Actions)
	if err != nil {
		if err.Error() == "too many actions in batch" || err.Error() == "insufficient action budget" {
			return &api.UseActionBudgetBadRequest{}, nil
		}
		if err.Error() == "sandevistan not in active phase" {
			return &api.UseActionBudgetConflict{}, nil
		}
		return &api.UseActionBudgetInternalServerError{}, err
	}

	return result, nil
}

// ApplyTemporalMarks - TYPED response!
func (h *Handlers) ApplyTemporalMarks(ctx context.Context, params api.ApplyTemporalMarksParams) (api.ApplyTemporalMarksRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ApplyTemporalMarks(ctx, params.PlayerID)
	if err != nil {
		return &api.ApplyTemporalMarksInternalServerError{}, err
	}

	return result, nil
}

// GetSandevistanBonuses - TYPED response!
func (h *Handlers) GetSandevistanBonuses(ctx context.Context, params api.GetSandevistanBonusesParams) (api.GetSandevistanBonusesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	bonuses, err := h.service.GetBonuses(ctx, params.PlayerID)
	if err != nil {
		return &api.GetSandevistanBonusesInternalServerError{}, err
	}

	return bonuses, nil
}

// PublishPerceptionDragEvent - TYPED response!
func (h *Handlers) PublishPerceptionDragEvent(ctx context.Context, req *api.PerceptionDragEvent, params api.PublishPerceptionDragEventParams) (api.PublishPerceptionDragEventRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if err := h.service.PublishPerceptionDragEvent(ctx, params.PlayerID, req); err != nil {
		if err.Error() == "event player_id does not match request player_id" {
			return &api.PublishPerceptionDragEventBadRequest{}, nil
		}
		return &api.PublishPerceptionDragEventInternalServerError{}, err
	}

	return &api.StatusResponse{Status: api.NewOptString("published")}, nil
}
