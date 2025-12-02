// Issue: #138
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

var (
	ErrNotFound       = errors.New("not found")
	ErrAlreadyClaimed = errors.New("already claimed")
	ErrNotUnlocked    = errors.New("not unlocked")
)

type Service struct {
	repo *Repository
}

func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) GetAchievements(ctx context.Context, params api.GetAchievementsParams) ([]api.Achievement, error) {
	return s.repo.GetAchievements(ctx, params)
}

func (s *Service) GetAchievementDetails(ctx context.Context, achievementId string) (*api.AchievementDetails, error) {
	return s.repo.GetAchievementDetails(ctx, achievementId)
}

func (s *Service) GetPlayerProgress(ctx context.Context, playerId string, params api.GetPlayerProgressParams) (map[string]interface{}, error) {
	progress, err := s.repo.GetPlayerProgress(ctx, playerId, params)
	if err != nil {
		return nil, err
	}

	unlockedCount := 0
	claimedCount := 0
	for _, p := range progress {
		if p.UnlockedAt != nil {
			unlockedCount++
		}
		if p.ClaimedAt != nil {
			claimedCount++
		}
	}

	return map[string]interface{}{
		"total_achievements": len(progress),
		"unlocked_count":     unlockedCount,
		"claimed_count":      claimedCount,
		"achievements":       progress,
	}, nil
}

func (s *Service) ClaimReward(ctx context.Context, playerId, achievementId string) (map[string]interface{}, error) {
	// Check if achievement is unlocked
	progress, err := s.repo.GetPlayerAchievementProgress(ctx, playerId, achievementId)
	if err != nil {
		return nil, err
	}

	if progress.UnlockedAt == nil {
		return nil, ErrNotUnlocked
	}

	if progress.ClaimedAt != nil {
		return nil, ErrAlreadyClaimed
	}

	// Get achievement details for rewards
	achievement, err := s.repo.GetAchievementDetails(ctx, achievementId)
	if err != nil {
		return nil, err
	}

	// Mark as claimed
	if err := s.repo.MarkAsClaimed(ctx, playerId, achievementId); err != nil {
		return nil, err
	}

	// Prepare rewards
	rewards := []api.Reward{}
	if achievement.RewardCurrency != nil && achievement.RewardAmount != nil {
		rewards = append(rewards, api.Reward{
			Type: "currency",
			Data: map[string]interface{}{
				"currency": *achievement.RewardCurrency,
				"amount":   *achievement.RewardAmount,
			},
		})
	}

	// TODO: Distribute rewards to player (integrate with economy service)

	return map[string]interface{}{
		"rewards":        rewards,
		"title_unlocked": achievement.TitleId != nil,
	}, nil
}

func (s *Service) GetPlayerTitles(ctx context.Context, playerId string) (map[string]interface{}, error) {
	titles, err := s.repo.GetPlayerTitles(ctx, playerId)
	if err != nil {
		return nil, err
	}

	var activeTitle *api.PlayerTitle
	for i := range titles {
		if titles[i].IsActive != nil && *titles[i].IsActive {
			activeTitle = &titles[i]
			break
		}
	}

	return map[string]interface{}{
		"active_title": activeTitle,
		"titles":       titles,
	}, nil
}

func (s *Service) SetActiveTitle(ctx context.Context, playerId, titleId string) (*api.PlayerTitle, error) {
	// Deactivate all titles
	if err := s.repo.DeactivateAllTitles(ctx, playerId); err != nil {
		return nil, err
	}

	// Activate selected title
	title, err := s.repo.ActivateTitle(ctx, playerId, titleId)
	if err != nil {
		return nil, err
	}

	return title, nil
}
