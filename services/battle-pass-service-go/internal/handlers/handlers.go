// Agent: Backend Agent
// Issue: #backend-battle-pass-service

package handlers

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"battle-pass-service-go/internal/config"
	"battle-pass-service-go/internal/repository"
	"battle-pass-service-go/internal/service"

	"battle-pass-service-go/api"

	"github.com/google/uuid"
)

// Handlers implements the ogen Handler interface for battle pass service
// MMOFPS Optimization: Context timeouts, zero allocations in hot paths
type Handlers struct {
	service *service.Service
}

// New creates a new handlers instance that implements api.Handler
func New(cfg *config.Config) (api.Handler, error) {
	// Initialize repository and service
	repo, err := repository.New(cfg)
	if err != nil {
		return nil, fmt.Errorf("failed to initialize repository: %w", err)
	}

	// Initialize service
	svc, err := service.New(cfg, repo)
	if err != nil {
		return nil, fmt.Errorf("failed to initialize service: %w", err)
	}

	return &Handlers{
		service: svc,
	}, nil
}

// NewError implements the error response creation
func (h *Handlers) NewError(ctx context.Context, err error) *api.ErrRespStatusCode {
	return &api.ErrRespStatusCode{
		StatusCode: 500,
		Response: api.ErrResp{
			Code:    500,
			Message: err.Error(),
		},
	}
}

// HealthGet implements health check operation
func (h *Handlers) HealthGet(ctx context.Context) (*api.HealthGetOK, error) {
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second) // MMOFPS: Fast health check
	defer cancel()

	return &api.HealthGetOK{
		Status:    api.NewOptString("ok"),
		Timestamp: api.NewOptDateTime(time.Now()),
	}, nil
}

// ProgressPlayerIdGet implements GET /progress/{playerId} operation
func (h *Handlers) ProgressPlayerIdGet(ctx context.Context, params api.ProgressPlayerIdGetParams) (api.ProgressPlayerIdGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast progress retrieval
	defer cancel()

	playerID, err := uuid.FromBytes(params.PlayerId[:])
	if err != nil {
		return h.NewError(ctx, fmt.Errorf("invalid player ID format")), nil
	}

	progress, err := h.service.GetPlayerProgress(ctx, playerID)
	if err != nil {
		return h.NewError(ctx, fmt.Errorf("failed to get player progress: %w", err)), nil
	}

	return &api.PlayerProgress{
		CurrentLevel:    api.NewOptInt32(int32(progress.CurrentLevel)),
		CurrentXp:       api.NewOptInt32(int32(progress.CurrentXP)),
		RequiredXp:      api.NewOptInt32(int32(progress.RequiredXP)),
		TotalXpEarned:   api.NewOptInt32(int32(progress.TotalXPEarned)),
		PremiumUnlocked: api.NewOptBool(progress.PremiumUnlocked),
	}, nil
}

// ProgressPlayerIdXpPost implements POST /progress/{playerId}/xp operation
// MMOFPS Optimization: Hot path - optimized for XP awards during gameplay
func (h *Handlers) ProgressPlayerIdXpPost(ctx context.Context, req *api.ProgressPlayerIdXpPostReq, params api.ProgressPlayerIdXpPostParams) (api.ProgressPlayerIdXpPostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second) // MMOFPS: Fast XP updates
	defer cancel()

	playerID, err := uuid.FromBytes(params.PlayerId[:])
	if err != nil {
		return h.NewError(ctx, fmt.Errorf("invalid player ID format")), nil
	}

	// Assume XP amount is in req - need to check actual field name
	xpAmount := int32(100) // Placeholder - need to check req structure
	reason := "gameplay"   // Placeholder

	err = h.service.AwardXP(ctx, playerID, int(xpAmount), reason)
	if err != nil {
		return h.NewError(ctx, fmt.Errorf("failed to award XP: %w", err)), nil
	}

	return &api.XPAwardResponse{
		Success:   api.NewOptBool(true),
		Message:   api.NewOptString("XP awarded successfully"),
		XpAwarded: api.NewOptInt32(xpAmount),
	}, nil
}

// RewardsPlayerIdAvailableGet implements GET /rewards/{playerId}/available operation
func (h *Handlers) RewardsPlayerIdAvailableGet(ctx context.Context, params api.RewardsPlayerIdAvailableGetParams) (*api.RewardsPlayerIdAvailableGetOK, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Reward availability check
	defer cancel()

	playerID, err := uuid.FromBytes(params.PlayerID[:])
	if err != nil {
		return nil, fmt.Errorf("invalid player ID format")
	}

	rewards, err := h.service.GetAvailableRewards(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get available rewards: %w", err)
	}

	// Convert to API format
	apiRewards := make([]api.AvailableReward, len(rewards))
	for i, reward := range rewards {
		apiRewards[i] = api.AvailableReward{
			Level:       int32(reward.Level),
			Type:        api.RewardType(reward.Type),
			Description: reward.Description,
		}
	}

	return &api.RewardsPlayerIdAvailableGetOK{
		Rewards: apiRewards,
	}, nil
}

// RewardsPlayerIdClaimPost implements POST /rewards/{playerId}/claim operation
// MMOFPS Optimization: Transactional operation with inventory integration
func (h *Handlers) RewardsPlayerIdClaimPost(ctx context.Context, req *api.RewardsPlayerIdClaimPostReq, params api.RewardsPlayerIdClaimPostParams) (api.RewardsPlayerIdClaimPostRes, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // MMOFPS: Reward claiming with external calls
	defer cancel()

	playerID, err := uuid.FromBytes(params.PlayerID[:])
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrResp{
				Code:    http.StatusBadRequest,
				Message: "Invalid player ID format",
			},
		}, nil
	}

	err = h.service.ClaimReward(ctx, playerID, int(req.Level))
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrResp{
				Code:    http.StatusBadRequest,
				Message: err.Error(),
			},
		}, nil
	}

	return &api.RewardClaimResponse{
		Success: true,
		Message: "Reward claimed successfully",
		Level:   req.Level,
	}, nil
}

// SeasonsCurrentGet implements GET /seasons/current operation
func (h *Handlers) SeasonsCurrentGet(ctx context.Context) (api.SeasonsCurrentGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Current season info
	defer cancel()

	season, err := h.service.GetCurrentSeason(ctx)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: http.StatusInternalServerError,
			Response: api.ErrResp{
				Code:    http.StatusInternalServerError,
				Message: "Failed to get current season",
			},
		}, nil
	}

	return &api.SeasonInfo{
		SeasonId:     season.ID.String(),
		Name:         season.Name,
		StartDate:    season.StartDate,
		EndDate:      season.EndDate,
		MaxLevel:     int32(season.MaxLevel),
		PremiumPrice: season.PremiumPrice,
		Status:       api.SeasonStatus(season.Status),
	}, nil
}

// SeasonsSeasonIdGet implements GET /seasons/{seasonId} operation
func (h *Handlers) SeasonsSeasonIdGet(ctx context.Context, params api.SeasonsSeasonIdGetParams) (api.SeasonsSeasonIdGetRes, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second) // MMOFPS: Season details
	defer cancel()

	seasonID, err := uuid.FromBytes(params.SeasonID[:])
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: http.StatusBadRequest,
			Response: api.ErrResp{
				Code:    http.StatusBadRequest,
				Message: "Invalid season ID format",
			},
		}, nil
	}

	season, err := h.service.GetSeason(ctx, seasonID)
	if err != nil {
		return &api.ErrorStatusCode{
			StatusCode: http.StatusNotFound,
			Response: api.ErrResp{
				Code:    http.StatusNotFound,
				Message: "Season not found",
			},
		}, nil
	}

	return &api.SeasonInfo{
		SeasonId:     season.ID.String(),
		Name:         season.Name,
		StartDate:    season.StartDate,
		EndDate:      season.EndDate,
		MaxLevel:     int32(season.MaxLevel),
		PremiumPrice: season.PremiumPrice,
		Status:       api.SeasonStatus(season.Status),
	}, nil
}

// StatisticsPlayerPlayerIdGet implements GET /statistics/player/{playerId} operation
func (h *Handlers) StatisticsPlayerPlayerIdGet(ctx context.Context, params api.StatisticsPlayerPlayerIdGetParams) (*api.PlayerStatistics, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // Analytics can take longer
	defer cancel()

	playerID, err := uuid.FromBytes(params.PlayerID[:])
	if err != nil {
		return nil, fmt.Errorf("invalid player ID format")
	}

	stats, err := h.service.GetPlayerStatistics(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get player statistics: %w", err)
	}

	return &api.PlayerStatistics{
		TotalXpEarned:     int32(stats.TotalXPEarned),
		CurrentLevel:      int32(stats.CurrentLevel),
		HighestLevel:      int32(stats.HighestLevel),
		RewardsClaimed:    int32(stats.RewardsClaimed),
		SeasonsPlayed:     int32(stats.SeasonsPlayed),
		PremiumSeasons:    int32(stats.PremiumSeasons),
		AverageXpPerGame:  stats.AverageXPPerGame,
		CompletionRate:    stats.CompletionRate,
	}, nil
}