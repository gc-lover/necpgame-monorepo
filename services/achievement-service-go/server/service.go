// Issue: #138
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame/services/achievement-service-go/pkg/api"
)

type Service interface {
	ListAchievements(ctx context.Context, params api.ListAchievementsParams) (*api.AchievementsListResponse, error)
	GetAchievement(ctx context.Context, achievementID string) (*api.AchievementResponse, error)
	GetPlayerAchievements(ctx context.Context, playerID string, params api.GetPlayerAchievementsParams) (*api.PlayerAchievementsResponse, error)
	ClaimAchievement(ctx context.Context, achievementID string) (*api.ClaimAchievementResponse, error)
	UpdateProgress(ctx context.Context, req *api.UpdateProgressRequest) (*api.ProgressUpdateResponse, error)
	GetAchievementStats(ctx context.Context, playerID string) (*api.AchievementStatsResponse, error)
}

type AchievementService struct {
	repository Repository
}

func NewAchievementService(repository Repository) Service {
	return &AchievementService{repository: repository}
}

func (s *AchievementService) ListAchievements(ctx context.Context, params api.ListAchievementsParams) (*api.AchievementsListResponse, error) {
	return &api.AchievementsListResponse{Achievements: &[]api.AchievementResponse{}}, nil
}

func (s *AchievementService) GetAchievement(ctx context.Context, achievementID string) (*api.AchievementResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *AchievementService) GetPlayerAchievements(ctx context.Context, playerID string, params api.GetPlayerAchievementsParams) (*api.PlayerAchievementsResponse, error) {
	return &api.PlayerAchievementsResponse{Achievements: &[]api.PlayerAchievementProgress{}}, nil
}

func (s *AchievementService) ClaimAchievement(ctx context.Context, achievementID string) (*api.ClaimAchievementResponse, error) {
	return nil, errors.New("not implemented")
}

func (s *AchievementService) UpdateProgress(ctx context.Context, req *api.UpdateProgressRequest) (*api.ProgressUpdateResponse, error) {
	return &api.ProgressUpdateResponse{}, nil
}

func (s *AchievementService) GetAchievementStats(ctx context.Context, playerID string) (*api.AchievementStatsResponse, error) {
	return nil, errors.New("not implemented")
}

