package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jmoiron/sqlx"
	"github.com/necpgame/world-service-go/models"
)

type WorldRepository interface {
	CreateResetExecution(ctx context.Context, execution *models.ResetExecution) error
	GetResetExecution(ctx context.Context, id uuid.UUID) (*models.ResetExecution, error)
	UpdateResetExecution(ctx context.Context, execution *models.ResetExecution) error
	GetLastReset(ctx context.Context, resetType models.ResetType) (*time.Time, error)
	GetResetSchedule(ctx context.Context) (*models.ResetSchedule, error)
	UpdateResetSchedule(ctx context.Context, schedule *models.ResetSchedule) error
	
	GetQuestPool(ctx context.Context, poolType models.QuestPoolType, playerLevel *int) ([]models.QuestPoolEntry, error)
	AssignQuest(ctx context.Context, playerID, questID uuid.UUID, poolType models.QuestPoolType) error
	GetPlayerQuests(ctx context.Context, playerID uuid.UUID, poolType *models.QuestPoolType) ([]models.PlayerQuest, error)
	
	GetPlayerLoginRewards(ctx context.Context, playerID uuid.UUID) (*models.PlayerLoginRewards, error)
	ClaimLoginReward(ctx context.Context, playerID uuid.UUID, rewardType models.LoginRewardType, dayNumber int) error
	GetLoginStreak(ctx context.Context, playerID uuid.UUID) (*models.LoginStreak, error)
	UpdateLoginStreak(ctx context.Context, streak *models.LoginStreak) error
	
	CreateResetEvent(ctx context.Context, event *models.ResetEvent) error
	GetResetEvents(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetEvent, int, error)
	
	GetTravelEvent(ctx context.Context, id uuid.UUID) (*models.TravelEvent, error)
	GetTravelEventsByEpoch(ctx context.Context, epochID string) ([]models.TravelEvent, error)
	GetAvailableTravelEvents(ctx context.Context, zoneID uuid.UUID, epochID *string) ([]models.TravelEvent, error)
	CreateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error
	GetTravelEventInstance(ctx context.Context, id uuid.UUID) (*models.TravelEventInstance, error)
	GetTravelEventInstancesByCharacterAndEvent(ctx context.Context, characterID, eventID uuid.UUID) ([]models.TravelEventInstance, error)
	GetTravelEventInstancesByEvent(ctx context.Context, eventID uuid.UUID) ([]models.TravelEventInstance, error)
	UpdateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error
	GetCharacterTravelEventCooldowns(ctx context.Context, characterID uuid.UUID) ([]models.TravelEventCooldown, error)
}

type worldRepository struct {
	db *sqlx.DB
}

func NewWorldRepository(db *sqlx.DB) WorldRepository {
	return &worldRepository{db: db}
}

func (r *worldRepository) CreateResetExecution(ctx context.Context, execution *models.ResetExecution) error {
	query := `
		INSERT INTO reset_executions (id, reset_type, status, started_at, players_processed, players_total, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`
	_, err := r.db.ExecContext(ctx, query,
		execution.ID, execution.ResetType, execution.Status, execution.StartedAt,
		execution.PlayersProcessed, execution.PlayersTotal, execution.CreatedAt, execution.UpdatedAt)
	return err
}

func (r *worldRepository) GetResetExecution(ctx context.Context, id uuid.UUID) (*models.ResetExecution, error) {
	var execution models.ResetExecution
	query := `
		SELECT id, reset_type, status, started_at, completed_at, players_processed, players_total, created_at, updated_at
		FROM reset_executions
		WHERE id = $1
	`
	err := r.db.GetContext(ctx, &execution, query, id)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &execution, err
}

func (r *worldRepository) UpdateResetExecution(ctx context.Context, execution *models.ResetExecution) error {
	query := `
		UPDATE reset_executions
		SET status = $2, completed_at = $3, players_processed = $4, players_total = $5, updated_at = $6
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query,
		execution.ID, execution.Status, execution.CompletedAt,
		execution.PlayersProcessed, execution.PlayersTotal, execution.UpdatedAt)
	return err
}

func (r *worldRepository) GetLastReset(ctx context.Context, resetType models.ResetType) (*time.Time, error) {
	var lastReset time.Time
	query := `
		SELECT MAX(started_at)
		FROM reset_executions
		WHERE reset_type = $1 AND status = $2
	`
	err := r.db.GetContext(ctx, &lastReset, query, resetType, models.ResetStatusCompleted)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &lastReset, err
}

func (r *worldRepository) GetResetSchedule(ctx context.Context) (*models.ResetSchedule, error) {
	var schedule models.ResetSchedule
	query := `SELECT daily_time, daily_timezone, weekly_day, weekly_time, weekly_timezone FROM reset_schedule LIMIT 1`
	err := r.db.GetContext(ctx, &schedule, query)
	if err == sql.ErrNoRows {
		return &models.ResetSchedule{
			DailyReset: models.DailyResetSchedule{
				Time:     "00:00:00",
				Timezone: "UTC",
			},
			WeeklyReset: models.WeeklyResetSchedule{
				DayOfWeek: 1,
				Time:      "00:00:00",
				Timezone:  "UTC",
			},
		}, nil
	}
	return &schedule, err
}

func (r *worldRepository) UpdateResetSchedule(ctx context.Context, schedule *models.ResetSchedule) error {
	query := `
		INSERT INTO reset_schedule (id, daily_time, daily_timezone, weekly_day, weekly_time, weekly_timezone, updated_at)
		VALUES (gen_random_uuid(), $1, $2, $3, $4, $5, NOW())
		ON CONFLICT (id) DO UPDATE SET
			daily_time = EXCLUDED.daily_time,
			daily_timezone = EXCLUDED.daily_timezone,
			weekly_day = EXCLUDED.weekly_day,
			weekly_time = EXCLUDED.weekly_time,
			weekly_timezone = EXCLUDED.weekly_timezone,
			updated_at = EXCLUDED.updated_at
	`
	_, err := r.db.ExecContext(ctx, query,
		schedule.DailyReset.Time, schedule.DailyReset.Timezone,
		schedule.WeeklyReset.DayOfWeek, schedule.WeeklyReset.Time, schedule.WeeklyReset.Timezone)
	return err
}

func (r *worldRepository) GetQuestPool(ctx context.Context, poolType models.QuestPoolType, playerLevel *int) ([]models.QuestPoolEntry, error) {
	var entries []models.QuestPoolEntry
	query := `
		SELECT quest_id, weight, min_level, max_level, is_active
		FROM quest_pools
		WHERE pool_type = $1 AND is_active = true
	`
	args := []interface{}{poolType}
	
	if playerLevel != nil {
		query += ` AND min_level <= $2 AND (max_level IS NULL OR max_level >= $2)`
		args = append(args, *playerLevel)
	}
	
	err := r.db.SelectContext(ctx, &entries, query, args...)
	return entries, err
}

func (r *worldRepository) AssignQuest(ctx context.Context, playerID, questID uuid.UUID, poolType models.QuestPoolType) error {
	query := `
		INSERT INTO player_quests (id, player_id, quest_id, pool_type, assigned_at, reset_date, created_at, updated_at)
		VALUES (gen_random_uuid(), $1, $2, $3, NOW(), CURRENT_DATE, NOW(), NOW())
	`
	_, err := r.db.ExecContext(ctx, query, playerID, questID, poolType)
	return err
}

func (r *worldRepository) GetPlayerQuests(ctx context.Context, playerID uuid.UUID, poolType *models.QuestPoolType) ([]models.PlayerQuest, error) {
	var quests []models.PlayerQuest
	query := `
		SELECT id, player_id, quest_id, pool_type, assigned_at, completed_at, reset_date, created_at, updated_at
		FROM player_quests
		WHERE player_id = $1
	`
	args := []interface{}{playerID}
	
	if poolType != nil {
		query += ` AND pool_type = $2`
		args = append(args, *poolType)
	}
	
	err := r.db.SelectContext(ctx, &quests, query, args...)
	return quests, err
}

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

func (r *worldRepository) CreateResetEvent(ctx context.Context, event *models.ResetEvent) error {
	eventDataJSON, _ := json.Marshal(event.EventData)
	query := `
		INSERT INTO reset_events (id, event_type, reset_type, player_id, event_data, created_at)
		VALUES ($1, $2, $3, $4, $5, $6)
	`
	_, err := r.db.ExecContext(ctx, query, event.ID, event.EventType, event.ResetType, event.PlayerID, eventDataJSON, event.CreatedAt)
	return err
}

func (r *worldRepository) GetResetEvents(ctx context.Context, resetType *models.ResetType, limit, offset int) ([]models.ResetEvent, int, error) {
	query := `
		SELECT id, event_type, reset_type, player_id, event_data, created_at
		FROM reset_events
		WHERE 1=1
	`
	args := []interface{}{}
	
	if resetType != nil {
		query += ` AND reset_type = $1`
		args = append(args, *resetType)
	}
	
	query += fmt.Sprintf(` ORDER BY created_at DESC LIMIT $%d OFFSET $%d`, len(args)+1, len(args)+2)
	args = append(args, limit, offset)
	
	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()
	
	var events []models.ResetEvent
	for rows.Next() {
		var event models.ResetEvent
		var eventDataJSON []byte
		var resetTypeStr sql.NullString
		var playerIDStr sql.NullString
		
		if err := rows.Scan(&event.ID, &event.EventType, &resetTypeStr, &playerIDStr, &eventDataJSON, &event.CreatedAt); err != nil {
			continue
		}
		
		if resetTypeStr.Valid {
			rt := models.ResetType(resetTypeStr.String)
			event.ResetType = &rt
		}
		if playerIDStr.Valid {
			if pid, err := uuid.Parse(playerIDStr.String); err == nil {
				event.PlayerID = &pid
			}
		}
		if len(eventDataJSON) > 0 {
			json.Unmarshal(eventDataJSON, &event.EventData)
		}
		
		events = append(events, event)
	}
	
	var total int
	countQuery := `SELECT COUNT(*) FROM reset_events WHERE 1=1`
	if resetType != nil {
		countQuery += ` AND reset_type = $1`
		r.db.GetContext(ctx, &total, countQuery, *resetType)
	} else {
		r.db.GetContext(ctx, &total, countQuery)
	}
	
	return events, total, nil
}

func (r *worldRepository) GetTravelEvent(ctx context.Context, id uuid.UUID) (*models.TravelEvent, error) {
	var event models.TravelEvent
	var skillChecksJSON, rewardsJSON, penaltiesJSON []byte
	
	query := `
		SELECT id, event_code, event_name, event_type, epoch_id, description, base_probability, cooldown_hours, skill_checks, rewards, penalties, created_at, updated_at
		FROM travel_events
		WHERE id = $1
	`
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&event.ID, &event.EventCode, &event.EventName, &event.EventType, &event.EpochID,
		&event.Description, &event.BaseProbability, &event.CooldownHours,
		&skillChecksJSON, &rewardsJSON, &penaltiesJSON,
		&event.CreatedAt, &event.UpdatedAt)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	
	if len(skillChecksJSON) > 0 {
		json.Unmarshal(skillChecksJSON, &event.SkillChecks)
	}
	if len(rewardsJSON) > 0 {
		json.Unmarshal(rewardsJSON, &event.Rewards)
	}
	if len(penaltiesJSON) > 0 {
		json.Unmarshal(penaltiesJSON, &event.Penalties)
	}
	
	return &event, nil
}

func (r *worldRepository) GetTravelEventsByEpoch(ctx context.Context, epochID string) ([]models.TravelEvent, error) {
	query := `
		SELECT id, event_code, event_name, event_type, epoch_id, description, base_probability, cooldown_hours, skill_checks, rewards, penalties, created_at, updated_at
		FROM travel_events
		WHERE epoch_id = $1
	`
	rows, err := r.db.QueryContext(ctx, query, epochID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	
	var events []models.TravelEvent
	for rows.Next() {
		var event models.TravelEvent
		var skillChecksJSON, rewardsJSON, penaltiesJSON []byte
		
		if err := rows.Scan(
			&event.ID, &event.EventCode, &event.EventName, &event.EventType, &event.EpochID,
			&event.Description, &event.BaseProbability, &event.CooldownHours,
			&skillChecksJSON, &rewardsJSON, &penaltiesJSON,
			&event.CreatedAt, &event.UpdatedAt); err != nil {
			continue
		}
		
		if len(skillChecksJSON) > 0 {
			json.Unmarshal(skillChecksJSON, &event.SkillChecks)
		}
		if len(rewardsJSON) > 0 {
			json.Unmarshal(rewardsJSON, &event.Rewards)
		}
		if len(penaltiesJSON) > 0 {
			json.Unmarshal(penaltiesJSON, &event.Penalties)
		}
		
		events = append(events, event)
	}
	
	return events, nil
}

func (r *worldRepository) GetAvailableTravelEvents(ctx context.Context, zoneID uuid.UUID, epochID *string) ([]models.TravelEvent, error) {
	query := `
		SELECT te.id, te.event_code, te.event_name, te.event_type, te.epoch_id, te.description, te.base_probability, te.cooldown_hours, te.skill_checks, te.rewards, te.penalties, te.created_at, te.updated_at
		FROM travel_events te
		INNER JOIN travel_event_zones tez ON te.id = tez.event_id
		WHERE tez.zone_id = $1
	`
	args := []interface{}{zoneID}
	
	if epochID != nil {
		query += ` AND te.epoch_id = $2`
		args = append(args, *epochID)
	}
	
	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	
	var events []models.TravelEvent
	for rows.Next() {
		var event models.TravelEvent
		var skillChecksJSON, rewardsJSON, penaltiesJSON []byte
		
		if err := rows.Scan(
			&event.ID, &event.EventCode, &event.EventName, &event.EventType, &event.EpochID,
			&event.Description, &event.BaseProbability, &event.CooldownHours,
			&skillChecksJSON, &rewardsJSON, &penaltiesJSON,
			&event.CreatedAt, &event.UpdatedAt); err != nil {
			continue
		}
		
		if len(skillChecksJSON) > 0 {
			json.Unmarshal(skillChecksJSON, &event.SkillChecks)
		}
		if len(rewardsJSON) > 0 {
			json.Unmarshal(rewardsJSON, &event.Rewards)
		}
		if len(penaltiesJSON) > 0 {
			json.Unmarshal(penaltiesJSON, &event.Penalties)
		}
		
		events = append(events, event)
	}
	
	return events, nil
}

func (r *worldRepository) CreateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error {
	query := `
		INSERT INTO travel_event_instances (id, event_id, character_id, zone_id, epoch_id, state, started_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	_, err := r.db.ExecContext(ctx, query,
		instance.ID, instance.EventID, instance.CharacterID, instance.ZoneID,
		instance.EpochID, instance.State, instance.StartedAt, instance.CreatedAt, instance.UpdatedAt)
	return err
}

func (r *worldRepository) GetTravelEventInstance(ctx context.Context, id uuid.UUID) (*models.TravelEventInstance, error) {
	var instance models.TravelEventInstance
	query := `
		SELECT id, event_id, character_id, zone_id, epoch_id, state, started_at, completed_at, created_at, updated_at
		FROM travel_event_instances
		WHERE id = $1
	`
	err := r.db.GetContext(ctx, &instance, query, id)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &instance, err
}

func (r *worldRepository) UpdateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error {
	query := `
		UPDATE travel_event_instances
		SET state = $2, completed_at = $3, updated_at = $4
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query, instance.ID, instance.State, instance.CompletedAt, instance.UpdatedAt)
	return err
}

func (r *worldRepository) GetTravelEventInstancesByCharacterAndEvent(ctx context.Context, characterID, eventID uuid.UUID) ([]models.TravelEventInstance, error) {
	var instances []models.TravelEventInstance
	query := `
		SELECT id, event_id, character_id, zone_id, epoch_id, state, started_at, completed_at, created_at, updated_at
		FROM travel_event_instances
		WHERE character_id = $1 AND event_id = $2
		ORDER BY created_at DESC
	`
	err := r.db.SelectContext(ctx, &instances, query, characterID, eventID)
	return instances, err
}

func (r *worldRepository) GetTravelEventInstancesByEvent(ctx context.Context, eventID uuid.UUID) ([]models.TravelEventInstance, error) {
	var instances []models.TravelEventInstance
	query := `
		SELECT id, event_id, character_id, zone_id, epoch_id, state, started_at, completed_at, created_at, updated_at
		FROM travel_event_instances
		WHERE event_id = $1
		ORDER BY created_at DESC
	`
	err := r.db.SelectContext(ctx, &instances, query, eventID)
	return instances, err
}

func (r *worldRepository) GetCharacterTravelEventCooldowns(ctx context.Context, characterID uuid.UUID) ([]models.TravelEventCooldown, error) {
	var cooldowns []models.TravelEventCooldown
	query := `
		SELECT te.event_type, tei.started_at as last_triggered_at,
			(tei.started_at + (te.cooldown_hours || ' hours')::interval) as cooldown_until
		FROM travel_event_instances tei
		INNER JOIN travel_events te ON tei.event_id = te.id
		WHERE tei.character_id = $1 AND tei.state != 'cancelled'
		ORDER BY tei.started_at DESC
	`
	err := r.db.SelectContext(ctx, &cooldowns, query, characterID)
	return cooldowns, err
}

