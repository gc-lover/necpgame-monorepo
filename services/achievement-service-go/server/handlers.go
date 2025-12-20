// Package server Issue: #1595
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

const DBTimeout = 50 * time.Millisecond

type Handlers struct {
	service *Service
}

func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

func (h *Handlers) ClaimAchievementReward(ctx context.Context, params api.ClaimAchievementRewardParams) (api.ClaimAchievementRewardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ClaimAchievementReward(params.AchievementId)
	if err != nil {
		if err == ErrNotFound {
			return &api.ClaimAchievementRewardNotFound{}, nil
		}
		return &api.ClaimAchievementRewardInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetAchievementDetails(ctx context.Context, params api.GetAchievementDetailsParams) (api.GetAchievementDetailsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAchievementDetails(params.AchievementId)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetAchievementDetailsNotFound{}, nil
		}
		return &api.GetAchievementDetailsInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetAchievements(ctx context.Context, params api.GetAchievementsParams) (api.GetAchievementsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetAchievements(params)
	if err != nil {
		return &api.GetAchievementsInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetPlayerProgress(ctx context.Context, _ api.GetPlayerProgressParams) (api.GetPlayerProgressRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetPlayerProgress()
	if err != nil {
		return &api.GetPlayerProgressInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) GetPlayerTitles(ctx context.Context, _ api.GetPlayerTitlesParams) (api.GetPlayerTitlesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetPlayerTitles()
	if err != nil {
		return &api.GetPlayerTitlesInternalServerError{}, err
	}

	return result, nil
}

func (h *Handlers) SetActiveTitle(ctx context.Context, req *api.SetActiveTitleReq, params api.SetActiveTitleParams) (api.SetActiveTitleRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.SetActiveTitle(req)
	if err != nil {
		if err == ErrNotFound {
			return &api.SetActiveTitleNotFound{}, nil
		}
		return &api.SetActiveTitleInternalServerError{}, err
	}

	return result, nil
}
