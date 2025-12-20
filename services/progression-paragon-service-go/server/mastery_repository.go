package server

import (
	"context"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type MasteryRepositoryInterface interface {
	GetMasteryLevels(ctx context.Context, characterID uuid.UUID) (*MasteryLevels, error)
	GetMasteryProgress(ctx context.Context, characterID uuid.UUID, masteryType string) (*MasteryProgress, error)
	GetMasteryRewards(ctx context.Context, characterID uuid.UUID, masteryType *string) (*MasteryRewards, error)
	CreateOrUpdateMasteryLevel(ctx context.Context, characterID uuid.UUID, masteryType string, level int, expCurrent, expRequired, totalExp int64, completions int) error
	AddMasteryReward(ctx context.Context, characterID uuid.UUID, masteryType string, rewardLevel int, rewardType, rewardID string) error
	GetMasteryRewardsByCharacter(ctx context.Context, characterID uuid.UUID, masteryType *string) ([]MasteryRewardItem, error)
}

type MasteryRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func (r *MasteryRepository) GetMasteryLevels(ctx context.Context, characterID uuid.UUID) (*MasteryLevels, error) {
	query := `
		SELECT mastery_type, mastery_level, experience_current, experience_required
		FROM progression.mastery_levels
		WHERE character_id = $1
		ORDER BY mastery_type`

	rows, err := r.db.Query(ctx, query, characterID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get mastery levels")
		return nil, err
	}
	defer rows.Close()

	var masteries []Mastery
	masteryTypes := []string{"raid", "dungeon", "world_boss", "pvp", "exploration"}
	masteryMap := make(map[string]*Mastery)

	for rows.Next() {
		var m Mastery
		if err := rows.Scan(&m.MasteryType, &m.MasteryLevel, &m.ExperienceCurrent, &m.ExperienceRequired); err != nil {
			r.logger.WithError(err).Error("Failed to scan mastery level")
			continue
		}
		m.RewardsUnlocked = []MasteryReward{}
		masteryMap[m.MasteryType] = &m
	}

	for _, mt := range masteryTypes {
		if m, exists := masteryMap[mt]; exists {
			rewards, _ := r.GetMasteryRewardsByCharacter(ctx, characterID, &mt)
			for _, reward := range rewards {
				if reward.Unlocked {
					m.RewardsUnlocked = append(m.RewardsUnlocked, MasteryReward{
						Level:      reward.Level,
						RewardType: reward.RewardType,
						RewardID:   reward.RewardID,
						UnlockedAt: reward.UnlockedAt,
					})
				}
			}
			masteries = append(masteries, *m)
		} else {
			masteries = append(masteries, Mastery{
				MasteryType:        mt,
				MasteryLevel:       0,
				ExperienceCurrent:  0,
				ExperienceRequired: 1000,
				RewardsUnlocked:    []MasteryReward{},
			})
		}
	}

	return &MasteryLevels{
		CharacterID: characterID,
		Masteries:   masteries,
	}, nil
}

func (r *MasteryRepository) GetMasteryProgress(ctx context.Context, characterID uuid.UUID, masteryType string) (*MasteryProgress, error) {
	var progress MasteryProgress
	progress.CharacterID = characterID
	progress.MasteryType = masteryType

	query := `
		SELECT mastery_level, experience_current, experience_required,
		       total_experience_earned, completions_count
		FROM progression.mastery_levels
		WHERE character_id = $1 AND mastery_type = $2`

	err := r.db.QueryRow(ctx, query, characterID, masteryType).Scan(
		&progress.MasteryLevel, &progress.ExperienceCurrent, &progress.ExperienceRequired,
		&progress.TotalExperienceEarned, &progress.CompletionsCount,
	)

	if errors.Is(err, pgx.ErrNoRows) {
		progress.MasteryLevel = 0
		progress.ExperienceCurrent = 0
		progress.ExperienceRequired = 1000
		progress.TotalExperienceEarned = 0
		progress.CompletionsCount = 0
	} else if err != nil {
		r.logger.WithError(err).Error("Failed to get mastery progress")
		return nil, err
	}

	if progress.ExperienceRequired > 0 {
		progress.ProgressPercent = float64(progress.ExperienceCurrent) / float64(progress.ExperienceRequired) * 100.0
	} else {
		progress.ProgressPercent = 0.0
	}

	rewards, _ := r.GetMasteryRewardsByCharacter(ctx, characterID, &masteryType)
	progress.RewardsUnlocked = []MasteryReward{}
	for _, reward := range rewards {
		if reward.Unlocked {
			progress.RewardsUnlocked = append(progress.RewardsUnlocked, MasteryReward{
				Level:      reward.Level,
				RewardType: reward.RewardType,
				RewardID:   reward.RewardID,
				UnlockedAt: reward.UnlockedAt,
			})
		}
	}

	return &progress, nil
}

func (r *MasteryRepository) GetMasteryRewards(ctx context.Context, characterID uuid.UUID, masteryType *string) (*MasteryRewards, error) {
	rewards, err := r.GetMasteryRewardsByCharacter(ctx, characterID, masteryType)
	if err != nil {
		return nil, err
	}

	return &MasteryRewards{
		CharacterID: characterID,
		Rewards:     rewards,
	}, nil
}

func (r *MasteryRepository) GetMasteryRewardsByCharacter(ctx context.Context, characterID uuid.UUID, masteryType *string) ([]MasteryRewardItem, error) {
	query := `
		SELECT mastery_type, reward_level, reward_type, reward_id, unlocked_at
		FROM progression.mastery_rewards
		WHERE character_id = $1`
	args := []interface{}{characterID}

	if masteryType != nil {
		query += ` AND mastery_type = $2`
		args = append(args, *masteryType)
	}

	query += ` ORDER BY mastery_type, reward_level`

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get mastery rewards")
		return nil, err
	}
	defer rows.Close()

	var rewards []MasteryRewardItem
	rewardMap := make(map[string]bool)

	for rows.Next() {
		var reward MasteryRewardItem
		var unlockedAt *time.Time
		if err := rows.Scan(&reward.MasteryType, &reward.Level, &reward.RewardType, &reward.RewardID, &unlockedAt); err != nil {
			r.logger.WithError(err).Error("Failed to scan mastery reward")
			continue
		}
		reward.Unlocked = true
		reward.UnlockedAt = unlockedAt
		reward.RewardName = r.getRewardName(reward.RewardType, reward.RewardID)
		rewards = append(rewards, reward)
		rewardMap[reward.MasteryType+"_"+strconv.Itoa(reward.Level)+"_"+reward.RewardID] = true
	}

	standardRewards := r.getStandardRewards()
	for _, sr := range standardRewards {
		if masteryType != nil && sr.MasteryType != *masteryType {
			continue
		}
		key := sr.MasteryType + "_" + strconv.Itoa(sr.Level) + "_" + sr.RewardID
		if !rewardMap[key] {
			sr.Unlocked = false
			rewards = append(rewards, sr)
		}
	}

	return rewards, nil
}

func (r *MasteryRepository) getStandardRewards() []MasteryRewardItem {
	var rewards []MasteryRewardItem
	masteryTypes := []string{"raid", "dungeon", "world_boss", "pvp", "exploration"}
	rewardLevels := []int{25, 50, 75, 100}

	for _, mt := range masteryTypes {
		for _, level := range rewardLevels {
			levelStr := strconv.Itoa(level)
			rewards = append(rewards, MasteryRewardItem{
				MasteryType: mt,
				Level:       level,
				RewardType:  "title",
				RewardID:    mt + "_master_" + levelStr,
				RewardName:  r.getRewardName("title", mt+"_master_"+levelStr),
				Unlocked:    false,
			})
			if level >= 50 {
				rewards = append(rewards, MasteryRewardItem{
					MasteryType: mt,
					Level:       level,
					RewardType:  "cosmetic",
					RewardID:    mt + "_master_cosmetic_" + levelStr,
					RewardName:  r.getRewardName("cosmetic", mt+"_master_cosmetic_"+levelStr),
					Unlocked:    false,
				})
			}
		}
	}

	return rewards
}

func (r *MasteryRepository) getRewardName(rewardType, rewardID string) string {
	if rewardType == "title" {
		return "Master " + rewardID
	}
	if rewardType == "cosmetic" {
		return "Elite " + rewardID + " Armor"
	}
	return rewardID
}

func (r *MasteryRepository) CreateOrUpdateMasteryLevel(ctx context.Context, characterID uuid.UUID, masteryType string, level int, expCurrent, expRequired, totalExp int64, completions int) error {
	query := `
		INSERT INTO progression.mastery_levels
		(character_id, mastery_type, mastery_level, experience_current, experience_required,
		 total_experience_earned, completions_count, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, NOW())
		ON CONFLICT (character_id, mastery_type) DO UPDATE SET
			mastery_level = EXCLUDED.mastery_level,
			experience_current = EXCLUDED.experience_current,
			experience_required = EXCLUDED.experience_required,
			total_experience_earned = EXCLUDED.total_experience_earned,
			completions_count = EXCLUDED.completions_count,
			updated_at = NOW()`

	_, err := r.db.Exec(ctx, query, characterID, masteryType, level, expCurrent, expRequired, totalExp, completions)
	return err
}

func (r *MasteryRepository) AddMasteryReward(ctx context.Context, characterID uuid.UUID, masteryType string, rewardLevel int, rewardType, rewardID string) error {
	query := `
		INSERT INTO progression.mastery_rewards
		(character_id, mastery_type, reward_level, reward_type, reward_id, unlocked_at)
		VALUES ($1, $2, $3, $4, $5, NOW())
		ON CONFLICT (character_id, mastery_type, reward_level, reward_id) DO NOTHING`

	_, err := r.db.Exec(ctx, query, characterID, masteryType, rewardLevel, rewardType, rewardID)
	return err
}
