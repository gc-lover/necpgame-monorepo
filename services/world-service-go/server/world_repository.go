package server

import (
	"context"
	"database/sql"
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
