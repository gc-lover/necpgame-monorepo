package server

import (
	"context"
	"database/sql"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
)

func (r *worldRepository) GetPlayerLoginRewards(ctx context.Context, playerID uuid.UUID) (*models.PlayerLoginRewards, error) {
	var rewards models.PlayerLoginRewards
	rewards.PlayerID = playerID
	
	query := `
		SELECT reward_type, day_number, reward_data, claimed_at
		FROM login_rewards
		WHERE player_id = $1
		ORDER BY day_number
	`
	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	
	for rows.Next() {
		var reward models.LoginReward
		var rewardDataJSON []byte
		var claimedAt sql.NullTime
		
		if err := rows.Scan(&reward.RewardType, &reward.DayNumber, &rewardDataJSON, &claimedAt); err != nil {
			continue
		}
		
		if err := json.Unmarshal(rewardDataJSON, &reward.RewardData); err != nil {
			continue
		}
		
		if claimedAt.Valid {
			reward.ClaimedAt = &claimedAt.Time
			rewards.ClaimedRewards = append(rewards.ClaimedRewards, reward)
		} else {
			rewards.AvailableRewards = append(rewards.AvailableRewards, reward)
		}
	}
	
	streak, err := r.GetLoginStreak(ctx, playerID)
	if err == nil && streak != nil {
		rewards.StreakDays = streak.StreakDays
	}
	
	return &rewards, nil
}

func (r *worldRepository) ClaimLoginReward(ctx context.Context, playerID uuid.UUID, rewardType models.LoginRewardType, dayNumber int) error {
	query := `
		UPDATE login_rewards
		SET claimed_at = NOW()
		WHERE player_id = $1 AND reward_type = $2 AND day_number = $3 AND claimed_at IS NULL
	`
	_, err := r.db.ExecContext(ctx, query, playerID, rewardType, dayNumber)
	return err
}

func (r *worldRepository) GetLoginStreak(ctx context.Context, playerID uuid.UUID) (*models.LoginStreak, error) {
	var streak models.LoginStreak
	query := `
		SELECT player_id, streak_days, last_login_date, max_streak_days, created_at, updated_at
		FROM login_streaks
		WHERE player_id = $1
	`
	err := r.db.GetContext(ctx, &streak, query, playerID)
	if err == sql.ErrNoRows {
		return &models.LoginStreak{
			PlayerID:      playerID,
			StreakDays:    0,
			MaxStreakDays: 30,
		}, nil
	}
	return &streak, err
}

func (r *worldRepository) UpdateLoginStreak(ctx context.Context, streak *models.LoginStreak) error {
	query := `
		INSERT INTO login_streaks (player_id, streak_days, last_login_date, max_streak_days, created_at, updated_at)
		VALUES ($1, $2, $3, $4, NOW(), NOW())
		ON CONFLICT (player_id) DO UPDATE SET
			streak_days = EXCLUDED.streak_days,
			last_login_date = EXCLUDED.last_login_date,
			updated_at = EXCLUDED.updated_at
	`
	_, err := r.db.ExecContext(ctx, query, streak.PlayerID, streak.StreakDays, streak.LastLoginDate, streak.MaxStreakDays)
	return err
}

