// Issue: #227
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-service-go/pkg/api"
	"github.com/lib/pq"
)

type Repository struct {
	db *sql.DB
}

func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) GetCurrentSeason(ctx context.Context) (*api.Season, error) {
	query := `
		SELECT id, name, description, season_number, start_date, end_date, max_level, theme, status
		FROM battle_pass_seasons
		WHERE status = 'active'
		ORDER BY start_date DESC
		LIMIT 1
	`

	var season api.Season
	err := r.db.QueryRowContext(ctx, query).Scan(
		&season.Id, &season.Name, &season.Description, &season.SeasonNumber,
		&season.StartDate, &season.EndDate, &season.MaxLevel, &season.Theme, &season.Status,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &season, nil
}

func (r *Repository) GetPlayerProgress(ctx context.Context, playerId string) (*api.PlayerProgress, error) {
	// Get current season
	season, err := r.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}

	query := `
		SELECT player_id, season_id, current_level, current_xp, has_premium,
		       premium_purchased_at, claimed_levels_free, claimed_levels_premium
		FROM player_battle_pass_progress
		WHERE player_id = $1 AND season_id = $2
	`

	var progress api.PlayerProgress
	err = r.db.QueryRowContext(ctx, query, playerId, season.Id).Scan(
		&progress.PlayerId, &progress.SeasonId, &progress.CurrentLevel, &progress.CurrentXp,
		&progress.HasPremium, &progress.PremiumPurchasedAt,
		pq.Array(&progress.ClaimedLevelsFree), pq.Array(&progress.ClaimedLevelsPremium),
	)
	if err == sql.ErrNoRows {
		// Create initial progress
		return r.CreatePlayerProgress(ctx, playerId, season.Id)
	}
	if err != nil {
		return nil, err
	}

	progress.Season = season

	// Calculate XP to next level
	xpToNext := ((progress.CurrentLevel) * 1000) - progress.CurrentXp
	progress.XpToNextLevel = &xpToNext

	// Calculate unclaimed rewards
	unclaimed := progress.CurrentLevel - len(progress.ClaimedLevelsFree)
	if progress.HasPremium {
		unclaimed += progress.CurrentLevel - len(progress.ClaimedLevelsPremium)
	}
	progress.UnclaimedRewardsCount = &unclaimed

	return &progress, nil
}

func (r *Repository) CreatePlayerProgress(ctx context.Context, playerId, seasonId string) (*api.PlayerProgress, error) {
	query := `
		INSERT INTO player_battle_pass_progress (player_id, season_id, current_level, current_xp, has_premium)
		VALUES ($1, $2, 1, 0, false)
		RETURNING player_id, season_id, current_level, current_xp, has_premium,
		          premium_purchased_at, claimed_levels_free, claimed_levels_premium
	`

	var progress api.PlayerProgress
	err := r.db.QueryRowContext(ctx, query, playerId, seasonId).Scan(
		&progress.PlayerId, &progress.SeasonId, &progress.CurrentLevel, &progress.CurrentXp,
		&progress.HasPremium, &progress.PremiumPurchasedAt,
		pq.Array(&progress.ClaimedLevelsFree), pq.Array(&progress.ClaimedLevelsPremium),
	)
	if err != nil {
		return nil, err
	}

	// Get season
	season, _ := r.GetCurrentSeason(ctx)
	progress.Season = season

	return &progress, nil
}

func (r *Repository) GetReward(ctx context.Context, seasonId string, level int, track api.RewardTrack) (*api.Reward, error) {
	query := `
		SELECT level, track, reward_type, reward_data
		FROM battle_pass_rewards
		WHERE season_id = $1 AND level = $2 AND track = $3
	`

	var reward api.Reward
	err := r.db.QueryRowContext(ctx, query, seasonId, level, track).Scan(
		&reward.Level, &reward.Track, &reward.RewardType, &reward.RewardData,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	claimed := false
	reward.Claimed = &claimed

	return &reward, nil
}

func (r *Repository) MarkRewardClaimed(ctx context.Context, playerId string, level int, track api.RewardTrack) error {
	column := "claimed_levels_free"
	if track == "premium" {
		column = "claimed_levels_premium"
	}

	query := `
		UPDATE player_battle_pass_progress
		SET ` + column + ` = array_append(` + column + `, $1), updated_at = $2
		WHERE player_id = $3
	`

	_, err := r.db.ExecContext(ctx, query, level, time.Now(), playerId)
	return err
}

func (r *Repository) ActivatePremium(ctx context.Context, playerId string) error {
	query := `
		UPDATE player_battle_pass_progress
		SET has_premium = true, premium_purchased_at = $1, updated_at = $1
		WHERE player_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, time.Now(), playerId)
	return err
}

func (r *Repository) GetRetroactiveRewards(ctx context.Context, seasonId string, currentLevel int) ([]api.Reward, error) {
	query := `
		SELECT level, track, reward_type, reward_data
		FROM battle_pass_rewards
		WHERE season_id = $1 AND track = 'premium' AND level <= $2
		ORDER BY level
	`

	rows, err := r.db.QueryContext(ctx, query, seasonId, currentLevel)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var rewards []api.Reward
	for rows.Next() {
		var reward api.Reward
		err := rows.Scan(&reward.Level, &reward.Track, &reward.RewardType, &reward.RewardData)
		if err != nil {
			return nil, err
		}
		rewards = append(rewards, reward)
	}

	return rewards, nil
}

func (r *Repository) GetWeeklyChallenges(ctx context.Context, playerId string) ([]api.WeeklyChallenge, error) {
	// Get current season
	season, err := r.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}

	// Get current week challenges
	query := `
		SELECT c.id, c.title, c.description, c.objective_type, c.objective_count, c.xp_reward,
		       c.start_date, c.end_date, pc.current_progress, pc.completed_at, pc.claimed_at
		FROM weekly_challenges c
		LEFT JOIN player_weekly_challenges pc ON c.id = pc.challenge_id AND pc.player_id = $1
		WHERE c.season_id = $2 AND c.start_date <= NOW() AND c.end_date >= NOW()
		ORDER BY c.week_number, c.title
	`

	rows, err := r.db.QueryContext(ctx, query, playerId, season.Id)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var challenges []api.WeeklyChallenge
	for rows.Next() {
		var ch api.WeeklyChallenge
		err := rows.Scan(
			&ch.Id, &ch.Title, &ch.Description, &ch.ObjectiveType, &ch.ObjectiveCount, &ch.XpReward,
			&ch.StartDate, &ch.EndDate, &ch.CurrentProgress, &ch.CompletedAt, &ch.ClaimedAt,
		)
		if err != nil {
			return nil, err
		}
		challenges = append(challenges, ch)
	}

	return challenges, nil
}

func (r *Repository) GetPlayerChallenge(ctx context.Context, playerId, challengeId string) (*api.WeeklyChallenge, error) {
	query := `
		SELECT c.id, c.title, c.description, c.objective_type, c.objective_count, c.xp_reward,
		       c.start_date, c.end_date, pc.current_progress, pc.completed_at, pc.claimed_at
		FROM weekly_challenges c
		LEFT JOIN player_weekly_challenges pc ON c.id = pc.challenge_id AND pc.player_id = $1
		WHERE c.id = $2
	`

	var ch api.WeeklyChallenge
	err := r.db.QueryRowContext(ctx, query, playerId, challengeId).Scan(
		&ch.Id, &ch.Title, &ch.Description, &ch.ObjectiveType, &ch.ObjectiveCount, &ch.XpReward,
		&ch.StartDate, &ch.EndDate, &ch.CurrentProgress, &ch.CompletedAt, &ch.ClaimedAt,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &ch, nil
}

func (r *Repository) GetChallengeDetails(ctx context.Context, challengeId string) (*api.WeeklyChallenge, error) {
	query := `
		SELECT id, title, description, objective_type, objective_count, xp_reward, start_date, end_date
		FROM weekly_challenges
		WHERE id = $1
	`

	var ch api.WeeklyChallenge
	err := r.db.QueryRowContext(ctx, query, challengeId).Scan(
		&ch.Id, &ch.Title, &ch.Description, &ch.ObjectiveType, &ch.ObjectiveCount,
		&ch.XpReward, &ch.StartDate, &ch.EndDate,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &ch, nil
}

func (r *Repository) MarkChallengeCompleted(ctx context.Context, playerId, challengeId string) error {
	query := `
		UPDATE player_weekly_challenges
		SET completed_at = $1, updated_at = $1
		WHERE player_id = $2 AND challenge_id = $3
	`

	result, err := r.db.ExecContext(ctx, query, time.Now(), playerId, challengeId)
	if err != nil {
		return err
	}

	rows, _ := result.RowsAffected()
	if rows == 0 {
		// Create record if not exists
		insertQuery := `
			INSERT INTO player_weekly_challenges (player_id, challenge_id, current_progress, completed_at)
			VALUES ($1, $2, 0, $3)
		`
		_, err = r.db.ExecContext(ctx, insertQuery, playerId, challengeId, time.Now())
		return err
	}

	return nil
}

func (r *Repository) UpdateProgress(ctx context.Context, playerId string, newLevel, newXP int) error {
	query := `
		UPDATE player_battle_pass_progress
		SET current_level = $1, current_xp = $2, updated_at = $3
		WHERE player_id = $4
	`

	_, err := r.db.ExecContext(ctx, query, newLevel, newXP, time.Now(), playerId)
	return err
}




