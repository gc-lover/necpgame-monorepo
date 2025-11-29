package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type TimeTrialRepositoryInterface interface {
	CreateSession(ctx context.Context, session *models.TimeTrialSession) error
	GetSession(ctx context.Context, sessionID uuid.UUID) (*models.TimeTrialSession, error)
	UpdateSession(ctx context.Context, session *models.TimeTrialSession) error
	GetPersonalBest(ctx context.Context, playerID uuid.UUID, trialType models.TrialType, contentID uuid.UUID) (*models.TimeTrialSession, error)
	GetGlobalRank(ctx context.Context, trialType models.TrialType, contentID uuid.UUID, completionTimeMs int64) (int, error)
	CreateRecord(ctx context.Context, sessionID uuid.UUID, playerID uuid.UUID, trialType models.TrialType, contentID uuid.UUID, teamID *uuid.UUID, completionTimeMs int64, rank int, isPersonalBest bool) error
	GetCurrentWeeklyChallenge(ctx context.Context) (*models.WeeklyTimeChallenge, error)
	GetWeeklyChallengeHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.WeeklyChallengeSummary, int, error)
}

type TimeTrialRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewTimeTrialRepository(db *pgxpool.Pool) *TimeTrialRepository {
	return &TimeTrialRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *TimeTrialRepository) CreateSession(ctx context.Context, session *models.TimeTrialSession) error {
	var teamID sql.NullString
	if session.TeamID != nil {
		teamID.String = session.TeamID.String()
		teamID.Valid = true
	}

	err := r.db.QueryRow(ctx,
		`INSERT INTO gameplay.time_trial_sessions 
		 (trial_type, content_id, player_id, team_id, start_time, status)
		 VALUES ($1, $2, $3, $4, $5, $6)
		 RETURNING id, start_time`,
		session.TrialType, session.ContentID, session.PlayerID, teamID, session.StartTime, session.Status,
	).Scan(&session.ID, &session.StartTime)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create time trial session")
		return err
	}

	return nil
}

func (r *TimeTrialRepository) GetSession(ctx context.Context, sessionID uuid.UUID) (*models.TimeTrialSession, error) {
	var session models.TimeTrialSession
	var teamID sql.NullString
	var endTime sql.NullTime
	var completionTimeMs sql.NullInt64

	err := r.db.QueryRow(ctx,
		`SELECT id, trial_type, content_id, player_id, team_id, start_time, end_time,
		 completion_time_ms, status
		 FROM gameplay.time_trial_sessions WHERE id = $1`,
		sessionID,
	).Scan(
		&session.ID, &session.TrialType, &session.ContentID, &session.PlayerID,
		&teamID, &session.StartTime, &endTime, &completionTimeMs, &session.Status,
	)

	if err == pgx.ErrNoRows {
		return nil, errors.New("session not found")
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get time trial session")
		return nil, err
	}

	if teamID.Valid {
		teamUUID, _ := uuid.Parse(teamID.String)
		session.TeamID = &teamUUID
	}
	if endTime.Valid {
		session.EndTime = &endTime.Time
	}
	if completionTimeMs.Valid {
		ms := completionTimeMs.Int64
		session.CompletionTimeMs = &ms
	}

	if session.Status == models.SessionStatusInProgress {
		elapsed := time.Since(session.StartTime).Milliseconds()
		session.ElapsedTimeMs = elapsed
	}

	return &session, nil
}

func (r *TimeTrialRepository) UpdateSession(ctx context.Context, session *models.TimeTrialSession) error {
	var teamID sql.NullString
	if session.TeamID != nil {
		teamID.String = session.TeamID.String()
		teamID.Valid = true
	}

	var endTime sql.NullTime
	if session.EndTime != nil {
		endTime.Time = *session.EndTime
		endTime.Valid = true
	}

	var completionTimeMs sql.NullInt64
	if session.CompletionTimeMs != nil {
		completionTimeMs.Int64 = *session.CompletionTimeMs
		completionTimeMs.Valid = true
	}

	_, err := r.db.Exec(ctx,
		`UPDATE gameplay.time_trial_sessions
		 SET end_time = $1, completion_time_ms = $2, status = $3
		 WHERE id = $4`,
		endTime, completionTimeMs, session.Status, session.ID,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to update time trial session")
		return err
	}

	return nil
}

func (r *TimeTrialRepository) GetPersonalBest(ctx context.Context, playerID uuid.UUID, trialType models.TrialType, contentID uuid.UUID) (*models.TimeTrialSession, error) {
	var session models.TimeTrialSession
	var teamID sql.NullString
	var endTime sql.NullTime
	var completionTimeMs sql.NullInt64

	err := r.db.QueryRow(ctx,
		`SELECT id, trial_type, content_id, player_id, team_id, start_time, end_time,
		 completion_time_ms, status
		 FROM gameplay.time_trial_sessions
		 WHERE player_id = $1 AND trial_type = $2 AND content_id = $3
		   AND status = 'completed' AND completion_time_ms IS NOT NULL
		 ORDER BY completion_time_ms ASC
		 LIMIT 1`,
		playerID, trialType, contentID,
	).Scan(
		&session.ID, &session.TrialType, &session.ContentID, &session.PlayerID,
		&teamID, &session.StartTime, &endTime, &completionTimeMs, &session.Status,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get personal best")
		return nil, err
	}

	if teamID.Valid {
		teamUUID, _ := uuid.Parse(teamID.String)
		session.TeamID = &teamUUID
	}
	if endTime.Valid {
		session.EndTime = &endTime.Time
	}
	if completionTimeMs.Valid {
		ms := completionTimeMs.Int64
		session.CompletionTimeMs = &ms
	}

	return &session, nil
}

func (r *TimeTrialRepository) GetGlobalRank(ctx context.Context, trialType models.TrialType, contentID uuid.UUID, completionTimeMs int64) (int, error) {
	var rank int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) + 1
		 FROM gameplay.time_trial_sessions
		 WHERE trial_type = $1 AND content_id = $2
		   AND status = 'completed' AND completion_time_ms IS NOT NULL
		   AND completion_time_ms < $3`,
		trialType, contentID, completionTimeMs,
	).Scan(&rank)

	if err != nil {
		r.logger.WithError(err).Error("Failed to get global rank")
		return 0, err
	}

	return rank, nil
}

func (r *TimeTrialRepository) CreateRecord(ctx context.Context, sessionID uuid.UUID, playerID uuid.UUID, trialType models.TrialType, contentID uuid.UUID, teamID *uuid.UUID, completionTimeMs int64, rank int, isPersonalBest bool) error {
	var teamIDVal sql.NullString
	if teamID != nil {
		teamIDVal.String = teamID.String()
		teamIDVal.Valid = true
	}

	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.time_trial_records
		 (trial_type, content_id, player_id, team_id, session_id, completion_time_ms, rank, is_personal_best)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`,
		trialType, contentID, playerID, teamIDVal, sessionID, completionTimeMs, rank, isPersonalBest,
	)

	if err != nil {
		r.logger.WithError(err).Error("Failed to create time trial record")
		return err
	}

	return nil
}

func (r *TimeTrialRepository) GetCurrentWeeklyChallenge(ctx context.Context) (*models.WeeklyTimeChallenge, error) {
	now := time.Now()

	var challenge models.WeeklyTimeChallenge
	var conditionsJSON, rewardsJSON sql.NullString

	err := r.db.QueryRow(ctx,
		`SELECT id, week_start, week_end, challenge_type, content_id, time_limit_ms,
		 conditions, rewards, created_at
		 FROM gameplay.weekly_time_challenges
		 WHERE week_start <= $1 AND week_end > $1`,
		now,
	).Scan(
		&challenge.ID, &challenge.WeekStart, &challenge.WeekEnd, &challenge.ChallengeType,
		&challenge.ContentID, &challenge.TimeLimitMs, &conditionsJSON, &rewardsJSON, &challenge.CreatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		r.logger.WithError(err).Error("Failed to get current weekly challenge")
		return nil, err
	}

	if conditionsJSON.Valid {
		json.Unmarshal([]byte(conditionsJSON.String), &challenge.Conditions)
	}
	if rewardsJSON.Valid {
		json.Unmarshal([]byte(rewardsJSON.String), &challenge.Rewards)
	}

	return &challenge, nil
}

func (r *TimeTrialRepository) GetWeeklyChallengeHistory(ctx context.Context, weeksBack, limit, offset int) ([]models.WeeklyChallengeSummary, int, error) {
	cutoffDate := time.Now().AddDate(0, 0, -weeksBack*7)

	var total int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM gameplay.weekly_time_challenges WHERE week_start >= $1`,
		cutoffDate,
	).Scan(&total)
	if err != nil {
		r.logger.WithError(err).Error("Failed to count weekly challenges")
		return nil, 0, err
	}

	rows, err := r.db.Query(ctx,
		`SELECT id, week_start, week_end, challenge_type, content_id, created_at
		 FROM gameplay.weekly_time_challenges
		 WHERE week_start >= $1
		 ORDER BY week_start DESC
		 LIMIT $2 OFFSET $3`,
		cutoffDate, limit, offset,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get weekly challenge history")
		return nil, 0, err
	}
	defer rows.Close()

	var challenges []models.WeeklyChallengeSummary
	for rows.Next() {
		var challenge models.WeeklyChallengeSummary
		err := rows.Scan(
			&challenge.ID, &challenge.WeekStart, &challenge.WeekEnd,
			&challenge.ChallengeType, &challenge.ContentID, &challenge.CreatedAt,
		)
		if err != nil {
			r.logger.WithError(err).Error("Failed to scan weekly challenge")
			continue
		}
		challenges = append(challenges, challenge)
	}

	return challenges, total, nil
}

