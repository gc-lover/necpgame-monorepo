// Issue: #1595
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

var ErrNotFound = errors.New("not found")

type Service struct {
	repo *Repository
}

func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) ClaimAchievementReward(ctx context.Context, playerID, achievementID string) (*api.ClaimAchievementRewardOK, error) {
	return &api.ClaimAchievementRewardOK{}, nil
}

func (s *Service) GetAchievementDetails(ctx context.Context, achievementID string) (*api.AchievementDetails, error) {
	return &api.AchievementDetails{}, nil
}

func (s *Service) GetAchievements(ctx context.Context, params api.GetAchievementsParams) (*api.GetAchievementsOK, error) {
	return &api.GetAchievementsOK{}, nil
}

func (s *Service) GetPlayerProgress(ctx context.Context, playerID string, params api.GetPlayerProgressParams) (*api.GetPlayerProgressOK, error) {
	return &api.GetPlayerProgressOK{}, nil
}

func (s *Service) GetPlayerTitles(ctx context.Context, playerID string) (*api.GetPlayerTitlesOK, error) {
	return &api.GetPlayerTitlesOK{}, nil
}

func (s *Service) SetActiveTitle(ctx context.Context, playerID string, req *api.SetActiveTitleReq) (*api.PlayerTitle, error) {
	return &api.PlayerTitle{}, nil
}
