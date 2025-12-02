// Issue: #44
package server

import (
	"context"

	"github.com/gc-lover/necpgame-monorepo/services/league-service-go/pkg/api"
)

type LeagueService struct {
	repo *LeagueRepository
}

func NewLeagueService(repo *LeagueRepository) *LeagueService {
	return &LeagueService{repo: repo}
}

// GetCurrentLeague возвращает текущую активную лигу
func (s *LeagueService) GetCurrentLeague(ctx context.Context) (*api.League, error) {
	return s.repo.GetCurrentLeague(ctx)
}

// GetLeagueByID возвращает лигу по ID
func (s *LeagueService) GetLeagueByID(ctx context.Context, leagueID string) (*api.League, error) {
	return s.repo.GetLeagueByID(ctx, leagueID)
}

// GetPlayerProgress возвращает прогресс игрока в текущей лиге
func (s *LeagueService) GetPlayerProgress(ctx context.Context, playerID string) (*api.PlayerLeagueProgress, error) {
	return s.repo.GetPlayerProgress(ctx, playerID)
}

