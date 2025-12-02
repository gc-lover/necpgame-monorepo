// Issue: #44
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/league-service-go/pkg/api"
	"github.com/jackc/pgx/v5/pgxpool"
)

type LeagueRepository struct {
	db *pgxpool.Pool
}

func NewLeagueRepository(db *pgxpool.Pool) *LeagueRepository {
	return &LeagueRepository{db: db}
}

// GetCurrentLeague получает текущую активную лигу из БД
func (r *LeagueRepository) GetCurrentLeague(ctx context.Context) (*api.League, error) {
	query := `
		SELECT league_id, season_number, start_time, end_time, phase, 
		       phase_start_time, phase_end_time, is_active
		FROM league_seasons
		WHERE is_active = true
		LIMIT 1
	`

	var league api.League
	err := r.db.QueryRow(ctx, query).Scan(
		&league.LeagueId,
		&league.SeasonNumber,
		&league.StartTime,
		&league.EndTime,
		&league.Phase,
		&league.PhaseStartTime,
		&league.PhaseEndTime,
		&league.IsActive,
	)

	if err != nil {
		return nil, err
	}

	return &league, nil
}

// GetLeagueByID получает лигу по ID
func (r *LeagueRepository) GetLeagueByID(ctx context.Context, leagueID string) (*api.League, error) {
	query := `
		SELECT league_id, season_number, start_time, end_time, phase,
		       phase_start_time, phase_end_time, is_active
		FROM league_seasons
		WHERE league_id = $1
	`

	var league api.League
	err := r.db.QueryRow(ctx, query, leagueID).Scan(
		&league.LeagueId,
		&league.SeasonNumber,
		&league.StartTime,
		&league.EndTime,
		&league.Phase,
		&league.PhaseStartTime,
		&league.PhaseEndTime,
		&league.IsActive,
	)

	if err != nil {
		return nil, err
	}

	return &league, nil
}

// GetPlayerProgress получает прогресс игрока в лиге
func (r *LeagueRepository) GetPlayerProgress(ctx context.Context, playerID string) (*api.PlayerLeagueProgress, error) {
	// Сначала получаем текущую лигу
	currentLeague, err := r.GetCurrentLeague(ctx)
	if err != nil {
		return nil, err
	}

	query := `
		SELECT player_id, league_id, rank_points, rank_tier, rank_division,
		       games_played, wins, losses, best_rank_tier, best_rank_division
		FROM player_league_progress
		WHERE player_id = $1 AND league_id = $2
	`

	var progress api.PlayerLeagueProgress
	err = r.db.QueryRow(ctx, query, playerID, currentLeague.LeagueId).Scan(
		&progress.PlayerId,
		&progress.LeagueId,
		&progress.RankPoints,
		&progress.RankTier,
		&progress.RankDivision,
		&progress.GamesPlayed,
		&progress.Wins,
		&progress.Losses,
		&progress.BestRankTier,
		&progress.BestRankDivision,
	)

	if err != nil {
		return nil, errors.New("player progress not found")
	}

	return &progress, nil
}

