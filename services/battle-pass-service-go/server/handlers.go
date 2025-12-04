// Issue: #1599 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-service-go/pkg/api"
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

// GetCurrentSeason implements getCurrentSeason operation.
func (h *Handlers) GetCurrentSeason(ctx context.Context) (api.GetCurrentSeasonRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	season, err := h.service.GetCurrentSeason(ctx)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetCurrentSeasonNotFound{
				Error:   "NotFound",
				Message: "No active season",
			}, nil
		}
		return &api.GetCurrentSeasonInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return season, nil
}

// GetPlayerProgress implements getPlayerProgress operation.
func (h *Handlers) GetPlayerProgress(ctx context.Context, params api.GetPlayerProgressParams) (api.GetPlayerProgressRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID
	progress, err := h.service.GetPlayerProgress(ctx, playerID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.GetPlayerProgressNotFound{
				Error:   "NotFound",
				Message: "Player progress not found",
			}, nil
		}
		return &api.GetPlayerProgressInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	return progress, nil
}

// ClaimReward implements claimReward operation.
func (h *Handlers) ClaimReward(ctx context.Context, req *api.ClaimRewardReq) (api.ClaimRewardRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.ClaimReward(ctx, req.PlayerID.String(), req.Level, req.Track)
	if err != nil {
		if err == ErrNotFound {
			return &api.ClaimRewardNotFound{
				Error:   "NotFound",
				Message: "Reward not found",
			}, nil
		}
		if err == ErrAlreadyClaimed {
			return &api.ClaimRewardBadRequest{
				Error:   "BadRequest",
				Message: "Reward already claimed",
			}, nil
		}
		if err == ErrPremiumRequired {
			return &api.ClaimRewardBadRequest{
				Error:   "BadRequest",
				Message: "Premium required",
			}, nil
		}
		return &api.ClaimRewardInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	// Convert result to ogen response
	rewards := []api.Reward{}
	if rewardsData, ok := result["rewards"].([]api.Reward); ok {
		rewards = rewardsData
	}

	return &api.ClaimRewardOK{
		Rewards: rewards,
	}, nil
}

// PurchasePremium implements purchasePremium operation.
func (h *Handlers) PurchasePremium(ctx context.Context, req *api.PurchasePremiumReq) (api.PurchasePremiumRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.PurchasePremium(ctx, req.PlayerID.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.PurchasePremiumInternalServerError{
				Error:   "NotFound",
				Message: "Player not found",
			}, nil
		}
		if err == ErrAlreadyPremium {
			return &api.PurchasePremiumBadRequest{
				Error:   "BadRequest",
				Message: "Premium already purchased",
			}, nil
		}
		return &api.PurchasePremiumInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	// Convert result to ogen response
	premiumStatus := false
	if premium, ok := result["is_premium"].(bool); ok {
		premiumStatus = premium
	}
	retroactiveRewards := []api.Reward{}
	if rewards, ok := result["retroactive_rewards"].([]api.Reward); ok {
		retroactiveRewards = rewards
	}

	return &api.PurchasePremiumOK{
		PremiumStatus:      api.NewOptBool(premiumStatus),
		RetroactiveRewards: retroactiveRewards,
	}, nil
}

// GetWeeklyChallenges implements getWeeklyChallenges operation.
func (h *Handlers) GetWeeklyChallenges(ctx context.Context, params api.GetWeeklyChallengesParams) (api.GetWeeklyChallengesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	playerID := params.PlayerID
	challenges, err := h.service.GetWeeklyChallenges(ctx, playerID.String())
	if err != nil {
		return &api.GetWeeklyChallengesInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	// challenges is already []api.WeeklyChallenge from service
	weekNumber := 1 // TODO: Calculate from season start date
	return &api.GetWeeklyChallengesOK{
		WeekNumber: api.NewOptInt(weekNumber),
		Challenges: challenges,
	}, nil
}

// CompleteChallenge implements completeChallenge operation.
func (h *Handlers) CompleteChallenge(ctx context.Context, req *api.CompleteChallengeReq, params api.CompleteChallengeParams) (api.CompleteChallengeRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CompleteChallenge(ctx, req.PlayerID.String(), params.ChallengeId.String())
	if err != nil {
		if err == ErrNotFound {
			return &api.CompleteChallengeNotFound{
				Error:   "NotFound",
				Message: "Challenge not found",
			}, nil
		}
		if err == ErrAlreadyCompleted {
			return &api.CompleteChallengeBadRequest{
				Error:   "BadRequest",
				Message: "Challenge already completed",
			}, nil
		}
		return &api.CompleteChallengeInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	// Convert result to ogen response
	xpAwarded := 0
	if xp, ok := result["xp_awarded"].(int); ok {
		xpAwarded = xp
	}
	newLevel := 0
	if level, ok := result["new_level"].(int); ok {
		newLevel = level
	}

	return &api.CompleteChallengeOK{
		XpAwarded: api.NewOptInt(xpAwarded),
		NewLevel:  api.NewOptInt(newLevel),
	}, nil
}

// AddXP implements addXP operation.
func (h *Handlers) AddXP(ctx context.Context, req *api.AddXPReq) (api.AddXPRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.AddXP(ctx, req.PlayerID.String(), req.XpAmount, req.Source)
	if err != nil {
		return &api.AddXPInternalServerError{
			Error:   "InternalServerError",
			Message: err.Error(),
		}, nil
	}

	// Convert result to ogen response
	newXP := 0
	if xp, ok := result["new_xp"].(int); ok {
		newXP = xp
	}
	newLevel := 0
	if level, ok := result["new_level"].(int); ok {
		newLevel = level
	}
	levelUp := false
	if up, ok := result["level_up"].(bool); ok {
		levelUp = up
	}
	rewardsUnlocked := []api.Reward{}
	if rewards, ok := result["rewards_unlocked"].([]api.Reward); ok {
		rewardsUnlocked = rewards
	}

	return &api.AddXPOK{
		NewXp:           api.NewOptInt(newXP),
		NewLevel:        api.NewOptInt(newLevel),
		LevelUp:         api.NewOptBool(levelUp),
		RewardsUnlocked: rewardsUnlocked,
	}, nil
}
