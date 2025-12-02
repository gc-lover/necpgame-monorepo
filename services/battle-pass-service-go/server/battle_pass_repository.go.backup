package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/battle-pass-service-go/models"
	"github.com/sirupsen/logrus"
)

type BattlePassRepositoryInterface interface {
	CreateSeason(ctx context.Context, season *models.BattlePassSeason) error
	GetSeasonByID(ctx context.Context, id uuid.UUID) (*models.BattlePassSeason, error)
	GetCurrentSeason(ctx context.Context) (*models.BattlePassSeason, error)
	CreateReward(ctx context.Context, reward *models.BattlePassReward) error
	GetRewardsBySeason(ctx context.Context, seasonID uuid.UUID) ([]models.BattlePassReward, error)
	GetRewardByID(ctx context.Context, id uuid.UUID) (*models.BattlePassReward, error)
	GetProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*models.PlayerBattlePassProgress, error)
	CreateProgress(ctx context.Context, progress *models.PlayerBattlePassProgress) error
	UpdateProgress(ctx context.Context, progress *models.PlayerBattlePassProgress) error
	GetClaimedRewards(ctx context.Context, characterID, seasonID uuid.UUID) ([]uuid.UUID, error)
	ClaimReward(ctx context.Context, characterID, rewardID uuid.UUID) error
	CreateWeeklyChallenge(ctx context.Context, challenge *models.WeeklyChallenge) error
	GetWeeklyChallenges(ctx context.Context, seasonID uuid.UUID, weekNumber *int) ([]models.WeeklyChallenge, error)
	GetChallengeProgress(ctx context.Context, characterID, challengeID uuid.UUID) (*models.PlayerChallengeProgress, error)
	CreateChallengeProgress(ctx context.Context, progress *models.PlayerChallengeProgress) error
	UpdateChallengeProgress(ctx context.Context, progress *models.PlayerChallengeProgress) error
	GetLevelRequirements(ctx context.Context, level int) (*models.LevelRequirements, error)
}

type BattlePassRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewBattlePassRepository(db *pgxpool.Pool) *BattlePassRepository {
	return &BattlePassRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *BattlePassRepository) CreateSeason(ctx context.Context, season *models.BattlePassSeason) error {
	query := `
		INSERT INTO mvp_core.battle_pass_seasons (
			id, name, start_date, end_date, max_level, is_active, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`

	_, err := r.db.Exec(ctx, query,
		season.ID, season.Name, season.StartDate, season.EndDate,
		season.MaxLevel, season.IsActive, season.CreatedAt, season.UpdatedAt,
	)
	return err
}

func (r *BattlePassRepository) GetSeasonByID(ctx context.Context, id uuid.UUID) (*models.BattlePassSeason, error) {
	var season models.BattlePassSeason
	query := `
		SELECT id, name, start_date, end_date, max_level, is_active, created_at, updated_at
		FROM mvp_core.battle_pass_seasons
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&season.ID, &season.Name, &season.StartDate, &season.EndDate,
		&season.MaxLevel, &season.IsActive, &season.CreatedAt, &season.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &season, nil
}

func (r *BattlePassRepository) GetCurrentSeason(ctx context.Context) (*models.BattlePassSeason, error) {
	var season models.BattlePassSeason
	now := time.Now()
	query := `
		SELECT id, name, start_date, end_date, max_level, is_active, created_at, updated_at
		FROM mvp_core.battle_pass_seasons
		WHERE is_active = true AND start_date <= $1 AND end_date >= $1
		ORDER BY start_date DESC
		LIMIT 1`

	err := r.db.QueryRow(ctx, query, now).Scan(
		&season.ID, &season.Name, &season.StartDate, &season.EndDate,
		&season.MaxLevel, &season.IsActive, &season.CreatedAt, &season.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &season, nil
}

func (r *BattlePassRepository) CreateReward(ctx context.Context, reward *models.BattlePassReward) error {
	rewardDataJSON, _ := json.Marshal(reward.RewardData)
	query := `
		INSERT INTO mvp_core.battle_pass_rewards (
			id, season_id, level, track, reward_type, reward_data, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`

	_, err := r.db.Exec(ctx, query,
		reward.ID, reward.SeasonID, reward.Level, reward.Track,
		reward.RewardType, rewardDataJSON, reward.CreatedAt, reward.UpdatedAt,
	)
	return err
}

func (r *BattlePassRepository) GetRewardsBySeason(ctx context.Context, seasonID uuid.UUID) ([]models.BattlePassReward, error) {
	query := `
		SELECT id, season_id, level, track, reward_type, reward_data, created_at, updated_at
		FROM mvp_core.battle_pass_rewards
		WHERE season_id = $1
		ORDER BY level, track`

	rows, err := r.db.Query(ctx, query, seasonID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var rewards []models.BattlePassReward
	for rows.Next() {
		var reward models.BattlePassReward
		var rewardDataJSON []byte

		err := rows.Scan(
			&reward.ID, &reward.SeasonID, &reward.Level, &reward.Track,
			&reward.RewardType, &rewardDataJSON, &reward.CreatedAt, &reward.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(rewardDataJSON) > 0 {
			json.Unmarshal(rewardDataJSON, &reward.RewardData)
		}

		rewards = append(rewards, reward)
	}

	return rewards, nil
}

func (r *BattlePassRepository) GetRewardByID(ctx context.Context, id uuid.UUID) (*models.BattlePassReward, error) {
	var reward models.BattlePassReward
	var rewardDataJSON []byte
	query := `
		SELECT id, season_id, level, track, reward_type, reward_data, created_at, updated_at
		FROM mvp_core.battle_pass_rewards
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&reward.ID, &reward.SeasonID, &reward.Level, &reward.Track,
		&reward.RewardType, &rewardDataJSON, &reward.CreatedAt, &reward.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(rewardDataJSON) > 0 {
		json.Unmarshal(rewardDataJSON, &reward.RewardData)
	}

	return &reward, nil
}

func (r *BattlePassRepository) GetProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*models.PlayerBattlePassProgress, error) {
	var progress models.PlayerBattlePassProgress
	query := `
		SELECT id, character_id, season_id, level, xp, xp_to_next_level,
			has_premium, premium_purchased_at, created_at, updated_at
		FROM mvp_core.player_battle_pass_progress
		WHERE character_id = $1 AND season_id = $2`

	err := r.db.QueryRow(ctx, query, characterID, seasonID).Scan(
		&progress.ID, &progress.CharacterID, &progress.SeasonID, &progress.Level,
		&progress.XP, &progress.XPToNextLevel, &progress.HasPremium,
		&progress.PremiumPurchasedAt, &progress.CreatedAt, &progress.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &progress, nil
}

func (r *BattlePassRepository) CreateProgress(ctx context.Context, progress *models.PlayerBattlePassProgress) error {
	query := `
		INSERT INTO mvp_core.player_battle_pass_progress (
			id, character_id, season_id, level, xp, xp_to_next_level,
			has_premium, premium_purchased_at, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`

	_, err := r.db.Exec(ctx, query,
		progress.ID, progress.CharacterID, progress.SeasonID, progress.Level,
		progress.XP, progress.XPToNextLevel, progress.HasPremium,
		progress.PremiumPurchasedAt, progress.CreatedAt, progress.UpdatedAt,
	)
	return err
}

func (r *BattlePassRepository) UpdateProgress(ctx context.Context, progress *models.PlayerBattlePassProgress) error {
	query := `
		UPDATE mvp_core.player_battle_pass_progress
		SET level = $1, xp = $2, xp_to_next_level = $3, has_premium = $4,
			premium_purchased_at = $5, updated_at = $6
		WHERE id = $7`

	_, err := r.db.Exec(ctx, query,
		progress.Level, progress.XP, progress.XPToNextLevel, progress.HasPremium,
		progress.PremiumPurchasedAt, progress.UpdatedAt, progress.ID,
	)
	return err
}

func (r *BattlePassRepository) GetClaimedRewards(ctx context.Context, characterID, seasonID uuid.UUID) ([]uuid.UUID, error) {
	query := `
		SELECT cr.reward_id
		FROM mvp_core.claimed_rewards cr
		JOIN mvp_core.battle_pass_rewards bpr ON cr.reward_id = bpr.id
		WHERE cr.character_id = $1 AND bpr.season_id = $2`

	rows, err := r.db.Query(ctx, query, characterID, seasonID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var rewardIDs []uuid.UUID
	for rows.Next() {
		var rewardID uuid.UUID
		if err := rows.Scan(&rewardID); err != nil {
			return nil, err
		}
		rewardIDs = append(rewardIDs, rewardID)
	}

	return rewardIDs, nil
}

func (r *BattlePassRepository) ClaimReward(ctx context.Context, characterID, rewardID uuid.UUID) error {
	query := `
		INSERT INTO mvp_core.claimed_rewards (id, character_id, reward_id, claimed_at)
		VALUES ($1, $2, $3, $4)
		ON CONFLICT (character_id, reward_id) DO NOTHING`

	_, err := r.db.Exec(ctx, query, uuid.New(), characterID, rewardID, time.Now())
	return err
}

func (r *BattlePassRepository) CreateWeeklyChallenge(ctx context.Context, challenge *models.WeeklyChallenge) error {
	query := `
		INSERT INTO mvp_core.weekly_challenges (
			id, season_id, title, description, xp_reward, target, week_number, is_active, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)`

	_, err := r.db.Exec(ctx, query,
		challenge.ID, challenge.SeasonID, challenge.Title, challenge.Description,
		challenge.XPReward, challenge.Target, challenge.WeekNumber, challenge.IsActive,
		challenge.CreatedAt, challenge.UpdatedAt,
	)
	return err
}

func (r *BattlePassRepository) GetWeeklyChallenges(ctx context.Context, seasonID uuid.UUID, weekNumber *int) ([]models.WeeklyChallenge, error) {
	var query string
	var args []interface{}

	if weekNumber != nil {
		query = `
			SELECT id, season_id, title, description, xp_reward, target, week_number, is_active, created_at, updated_at
			FROM mvp_core.weekly_challenges
			WHERE season_id = $1 AND week_number = $2 AND is_active = true
			ORDER BY week_number, title`
		args = []interface{}{seasonID, *weekNumber}
	} else {
		query = `
			SELECT id, season_id, title, description, xp_reward, target, week_number, is_active, created_at, updated_at
			FROM mvp_core.weekly_challenges
			WHERE season_id = $1 AND is_active = true
			ORDER BY week_number, title`
		args = []interface{}{seasonID}
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var challenges []models.WeeklyChallenge
	for rows.Next() {
		var challenge models.WeeklyChallenge
		err := rows.Scan(
			&challenge.ID, &challenge.SeasonID, &challenge.Title, &challenge.Description,
			&challenge.XPReward, &challenge.Target, &challenge.WeekNumber, &challenge.IsActive,
			&challenge.CreatedAt, &challenge.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		challenges = append(challenges, challenge)
	}

	return challenges, nil
}

func (r *BattlePassRepository) GetChallengeProgress(ctx context.Context, characterID, challengeID uuid.UUID) (*models.PlayerChallengeProgress, error) {
	var progress models.PlayerChallengeProgress
	query := `
		SELECT id, character_id, challenge_id, progress, is_completed, completed_at, created_at, updated_at
		FROM mvp_core.player_challenge_progress
		WHERE character_id = $1 AND challenge_id = $2`

	err := r.db.QueryRow(ctx, query, characterID, challengeID).Scan(
		&progress.ID, &progress.CharacterID, &progress.ChallengeID, &progress.Progress,
		&progress.IsCompleted, &progress.CompletedAt, &progress.CreatedAt, &progress.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &progress, nil
}

func (r *BattlePassRepository) CreateChallengeProgress(ctx context.Context, progress *models.PlayerChallengeProgress) error {
	query := `
		INSERT INTO mvp_core.player_challenge_progress (
			id, character_id, challenge_id, progress, is_completed, completed_at, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`

	_, err := r.db.Exec(ctx, query,
		progress.ID, progress.CharacterID, progress.ChallengeID, progress.Progress,
		progress.IsCompleted, progress.CompletedAt, progress.CreatedAt, progress.UpdatedAt,
	)
	return err
}

func (r *BattlePassRepository) UpdateChallengeProgress(ctx context.Context, progress *models.PlayerChallengeProgress) error {
	query := `
		UPDATE mvp_core.player_challenge_progress
		SET progress = $1, is_completed = $2, completed_at = $3, updated_at = $4
		WHERE id = $5`

	_, err := r.db.Exec(ctx, query,
		progress.Progress, progress.IsCompleted, progress.CompletedAt, progress.UpdatedAt, progress.ID,
	)
	return err
}

func (r *BattlePassRepository) GetLevelRequirements(ctx context.Context, level int) (*models.LevelRequirements, error) {
	baseXP := 1000
	xpPerLevel := 250
	xpRequired := baseXP + (level-1)*xpPerLevel

	cumulativeXP := 0
	for i := 1; i < level; i++ {
		cumulativeXP += baseXP + (i-1)*xpPerLevel
	}

	return &models.LevelRequirements{
		Level:        level,
		XPRequired:   xpRequired,
		CumulativeXP: cumulativeXP,
	}, nil
}

