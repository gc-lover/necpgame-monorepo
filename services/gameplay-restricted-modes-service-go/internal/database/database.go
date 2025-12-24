// Issue: #1499
package database

import (
	"context"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/rs/zerolog"
)

// Service provides database operations for the gameplay restricted modes service
type Service struct {
	db     *pgxpool.Pool
	redis  *redis.Client
	logger zerolog.Logger
}

// New creates a new database service
func New(db *pgxpool.Pool, redis *redis.Client, logger zerolog.Logger) *Service {
	return &Service{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// Health checks database connectivity
func (s *Service) Health(ctx context.Context) error {
	return s.db.Ping(ctx)
}

// PlayerRestrictedMode represents a player's restricted mode status
type PlayerRestrictedMode struct {
	PlayerID        string    `json:"player_id" db:"player_id"`
	ModeType        string    `json:"mode_type" db:"mode_type"`
	IsActive        bool      `json:"is_active" db:"is_active"`
	ActivatedAt     *time.Time `json:"activated_at,omitempty" db:"activated_at"`
	DeactivatedAt   *time.Time `json:"deactivated_at,omitempty" db:"deactivated_at"`
	TotalSessions   int       `json:"total_sessions" db:"total_sessions"`
	SuccessfulRuns  int       `json:"successful_runs" db:"successful_runs"`
	FailedRuns      int       `json:"failed_runs" db:"failed_runs"`
	BestScore       *int      `json:"best_score,omitempty" db:"best_score"`
	TotalTimePlayed time.Duration `json:"total_time_played" db:"total_time_played"`
	CreatedAt       time.Time `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time `json:"updated_at" db:"updated_at"`
}

// RestrictedModeSession represents an active restricted mode session
type RestrictedModeSession struct {
	SessionID       string    `json:"session_id" db:"session_id"`
	PlayerID        string    `json:"player_id" db:"player_id"`
	CharacterID     string    `json:"character_id" db:"character_id"`
	ModeType        string    `json:"mode_type" db:"mode_type"`
	ContentType     *string   `json:"content_type,omitempty" db:"content_type"`
	Difficulty      *string   `json:"difficulty,omitempty" db:"difficulty"`
	StartedAt       time.Time `json:"started_at" db:"started_at"`
	Progress        float64   `json:"progress" db:"progress"`
	IsActive        bool      `json:"is_active" db:"is_active"`
	CurrentScore    int       `json:"current_score" db:"current_score"`
	TimeElapsed     time.Duration `json:"time_elapsed" db:"time_elapsed"`
	Restrictions    []string  `json:"restrictions" db:"restrictions"`
	CreatedAt       time.Time `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time `json:"updated_at" db:"updated_at"`
}

// CompletedRestrictedModeSession represents a completed session
type CompletedRestrictedModeSession struct {
	SessionID     string        `json:"session_id" db:"session_id"`
	PlayerID      string        `json:"player_id" db:"player_id"`
	ModeType      string        `json:"mode_type" db:"mode_type"`
	CompletedAt   time.Time     `json:"completed_at" db:"completed_at"`
	Success       bool          `json:"success" db:"success"`
	CompletionTime time.Duration `json:"completion_time" db:"completion_time"`
	FinalScore    int           `json:"final_score" db:"final_score"`
	RankAchieved  *int          `json:"rank_achieved,omitempty" db:"rank_achieved"`
	Rewards       []string      `json:"rewards" db:"rewards"`
}

// RestrictedModeAchievement represents achievements for restricted modes
type RestrictedModeAchievement struct {
	AchievementID   string    `json:"achievement_id" db:"achievement_id"`
	PlayerID        string    `json:"player_id" db:"player_id"`
	ModeType        string    `json:"mode_type" db:"mode_type"`
	AchievementName string    `json:"achievement_name" db:"achievement_name"`
	Description     string    `json:"description" db:"description"`
	Rarity          string    `json:"rarity" db:"rarity"`
	UnlockedAt      time.Time `json:"unlocked_at" db:"unlocked_at"`
}

// LeaderboardEntry represents a leaderboard entry
type LeaderboardEntry struct {
	PlayerID        string    `json:"player_id" db:"player_id"`
	PlayerName      string    `json:"player_name" db:"player_name"`
	CharacterName   *string   `json:"character_name,omitempty" db:"character_name"`
	ModeType        string    `json:"mode_type" db:"mode_type"`
	Score           int       `json:"score" db:"score"`
	CompletionTime  time.Duration `json:"completion_time" db:"completion_time"`
	Rank            int       `json:"rank" db:"rank"`
	AchievedAt      time.Time `json:"achieved_at" db:"achieved_at"`
}

// Player operations

// GetPlayerRestrictedModesStatus returns all restricted mode statuses for a player
func (s *Service) GetPlayerRestrictedModesStatus(ctx context.Context, playerID string) ([]PlayerRestrictedMode, error) {
	query := `
		SELECT player_id, mode_type, is_active, activated_at, deactivated_at,
			   total_sessions, successful_runs, failed_runs, best_score,
			   total_time_played, created_at, updated_at
		FROM gameplay.player_restricted_modes
		WHERE player_id = $1
		ORDER BY created_at DESC
	`

	rows, err := s.db.Query(ctx, query, playerID)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", playerID).Msg("Failed to get player restricted modes status")
		return nil, err
	}
	defer rows.Close()

	var modes []PlayerRestrictedMode
	for rows.Next() {
		var mode PlayerRestrictedMode
		err := rows.Scan(&mode.PlayerID, &mode.ModeType, &mode.IsActive, &mode.ActivatedAt,
			&mode.DeactivatedAt, &mode.TotalSessions, &mode.SuccessfulRuns, &mode.FailedRuns,
			&mode.BestScore, &mode.TotalTimePlayed, &mode.CreatedAt, &mode.UpdatedAt)
		if err != nil {
			s.logger.Error().Err(err).Msg("Failed to scan player restricted mode")
			continue
		}
		modes = append(modes, mode)
	}

	return modes, rows.Err()
}

// CheckPlayerEligibility checks if a player is eligible for a restricted mode
func (s *Service) CheckPlayerEligibility(ctx context.Context, playerID, modeType string) (bool, error) {
	query := `
		SELECT COUNT(*) > 0
		FROM gameplay.player_restricted_modes
		WHERE player_id = $1 AND mode_type = $2 AND is_active = false
	`

	var eligible bool
	err := s.db.QueryRow(ctx, query, playerID, modeType).Scan(&eligible)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", playerID).Str("mode_type", modeType).Msg("Failed to check player eligibility")
		return false, err
	}

	return eligible, nil
}

// ActivateRestrictedMode activates a restricted mode for a player
func (s *Service) ActivateRestrictedMode(ctx context.Context, playerID, characterID, modeType string, contentType, difficulty *string, restrictions []string) (*RestrictedModeSession, error) {
	sessionID := generateUUID()

	query := `
		INSERT INTO gameplay.restricted_mode_sessions (
			session_id, player_id, character_id, mode_type, content_type, difficulty,
			started_at, progress, is_active, current_score, time_elapsed, restrictions
		) VALUES ($1, $2, $3, $4, $5, $6, NOW(), 0.0, true, 0, '0s', $7)
		RETURNING session_id, player_id, character_id, mode_type, content_type, difficulty,
				  started_at, progress, is_active, current_score, time_elapsed, restrictions,
				  created_at, updated_at
	`

	var session RestrictedModeSession
	err := s.db.QueryRow(ctx, query, sessionID, playerID, characterID, modeType, contentType, difficulty, restrictions).Scan(
		&session.SessionID, &session.PlayerID, &session.CharacterID, &session.ModeType,
		&session.ContentType, &session.Difficulty, &session.StartedAt, &session.Progress,
		&session.IsActive, &session.CurrentScore, &session.TimeElapsed, &session.Restrictions,
		&session.CreatedAt, &session.UpdatedAt,
	)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", playerID).Str("mode_type", modeType).Msg("Failed to activate restricted mode")
		return nil, err
	}

	// Update player restricted mode status
	updateQuery := `
		UPDATE gameplay.player_restricted_modes
		SET is_active = true, activated_at = NOW(), total_sessions = total_sessions + 1, updated_at = NOW()
		WHERE player_id = $1 AND mode_type = $2
	`
	_, err = s.db.Exec(ctx, updateQuery, playerID, modeType)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", playerID).Str("mode_type", modeType).Msg("Failed to update player restricted mode status")
	}

	return &session, nil
}

// GetActiveRestrictedModeSession returns the active session for a player
func (s *Service) GetActiveRestrictedModeSession(ctx context.Context, playerID string) (*RestrictedModeSession, error) {
	query := `
		SELECT session_id, player_id, character_id, mode_type, content_type, difficulty,
			   started_at, progress, is_active, current_score, time_elapsed, restrictions,
			   created_at, updated_at
		FROM gameplay.restricted_mode_sessions
		WHERE player_id = $1 AND is_active = true
		ORDER BY started_at DESC
		LIMIT 1
	`

	var session RestrictedModeSession
	err := s.db.QueryRow(ctx, query, playerID).Scan(
		&session.SessionID, &session.PlayerID, &session.CharacterID, &session.ModeType,
		&session.ContentType, &session.Difficulty, &session.StartedAt, &session.Progress,
		&session.IsActive, &session.CurrentScore, &session.TimeElapsed, &session.Restrictions,
		&session.CreatedAt, &session.UpdatedAt,
	)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", playerID).Msg("Failed to get active restricted mode session")
		return nil, err
	}

	return &session, nil
}

// CompleteRestrictedModeSession completes a restricted mode session
func (s *Service) CompleteRestrictedModeSession(ctx context.Context, sessionID string, success bool, completionTime time.Duration, finalScore int) (*CompletedRestrictedModeSession, error) {
	query := `
		UPDATE gameplay.restricted_mode_sessions
		SET is_active = false, progress = 1.0, time_elapsed = $2, current_score = $3, updated_at = NOW()
		WHERE session_id = $1 AND is_active = true
		RETURNING session_id, player_id, mode_type, current_score as final_score, time_elapsed as completion_time
	`

	var completed CompletedRestrictedModeSession
	var completionTimePg time.Duration
	err := s.db.QueryRow(ctx, query, sessionID, completionTime, finalScore).Scan(
		&completed.SessionID, &completed.PlayerID, &completed.ModeType,
		&completed.FinalScore, &completionTimePg,
	)
	if err != nil {
		s.logger.Error().Err(err).Str("session_id", sessionID).Msg("Failed to complete restricted mode session")
		return nil, err
	}

	completed.CompletedAt = time.Now()
	completed.Success = success
	completed.CompletionTime = completionTimePg

	// Update player statistics
	updateQuery := `
		UPDATE gameplay.player_restricted_modes
		SET
			is_active = false,
			deactivated_at = NOW(),
			successful_runs = successful_runs + CASE WHEN $3 THEN 1 ELSE 0 END,
			failed_runs = failed_runs + CASE WHEN $3 THEN 0 ELSE 1 END,
			best_score = GREATEST(best_score, $4),
			total_time_played = total_time_played + $5,
			updated_at = NOW()
		WHERE player_id = $1 AND mode_type = $2
	`
	_, err = s.db.Exec(ctx, updateQuery, completed.PlayerID, completed.ModeType, success, finalScore, completionTime)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", completed.PlayerID).Str("mode_type", completed.ModeType).Msg("Failed to update player statistics")
	}

	// Calculate rank if successful
	if success {
		rank, err := s.calculateLeaderboardRank(ctx, completed.ModeType, finalScore, completionTime)
		if err == nil {
			completed.RankAchieved = &rank
		}
	}

	return &completed, nil
}

// FailRestrictedModeSession fails a restricted mode session
func (s *Service) FailRestrictedModeSession(ctx context.Context, sessionID, failureReason string) error {
	query := `
		UPDATE gameplay.restricted_mode_sessions
		SET is_active = false, updated_at = NOW()
		WHERE session_id = $1 AND is_active = true
	`

	_, err := s.db.Exec(ctx, query, sessionID)
	if err != nil {
		s.logger.Error().Err(err).Str("session_id", sessionID).Msg("Failed to fail restricted mode session")
		return err
	}

	// Get session info for updating player stats
	infoQuery := `
		SELECT player_id, mode_type FROM gameplay.restricted_mode_sessions WHERE session_id = $1
	`
	var playerID, modeType string
	err = s.db.QueryRow(ctx, infoQuery, sessionID).Scan(&playerID, &modeType)
	if err != nil {
		s.logger.Error().Err(err).Str("session_id", sessionID).Msg("Failed to get session info for failure update")
		return err
	}

	// Update player statistics for failure
	updateQuery := `
		UPDATE gameplay.player_restricted_modes
		SET
			is_active = false,
			deactivated_at = NOW(),
			failed_runs = failed_runs + 1,
			updated_at = NOW()
		WHERE player_id = $1 AND mode_type = $2
	`
	_, err = s.db.Exec(ctx, updateQuery, playerID, modeType)
	if err != nil {
		s.logger.Error().Err(err).Str("player_id", playerID).Str("mode_type", modeType).Msg("Failed to update player statistics for failure")
	}

	return nil
}

// GetLeaderboard returns leaderboard entries for a mode type
func (s *Service) GetLeaderboard(ctx context.Context, modeType, timeframe string, limit int) ([]LeaderboardEntry, error) {
	var query string
	var args []interface{}

	baseQuery := `
		SELECT
			p.player_id,
			COALESCE(p.display_name, 'Anonymous') as player_name,
			c.name as character_name,
			rms.mode_type,
			COALESCE(rms.current_score, 0) as score,
			COALESCE(rms.time_elapsed, '0s') as completion_time,
			RANK() OVER (ORDER BY rms.current_score DESC, rms.time_elapsed ASC) as rank,
			rms.updated_at as achieved_at
		FROM gameplay.restricted_mode_sessions rms
		JOIN players p ON rms.player_id = p.id
		LEFT JOIN characters c ON rms.character_id = c.id
		WHERE rms.mode_type = $1 AND rms.is_active = false
	`

	args = append(args, modeType)

	// Add timeframe filter
	switch timeframe {
	case "daily":
		query = baseQuery + " AND rms.updated_at >= CURRENT_DATE"
	case "weekly":
		query = baseQuery + " AND rms.updated_at >= date_trunc('week', CURRENT_DATE)"
	case "monthly":
		query = baseQuery + " AND rms.updated_at >= date_trunc('month', CURRENT_DATE)"
	default: // alltime
		query = baseQuery
	}

	query += " ORDER BY rms.current_score DESC, rms.time_elapsed ASC LIMIT $2"
	args = append(args, limit)

	rows, err := s.db.Query(ctx, query, args...)
	if err != nil {
		s.logger.Error().Err(err).Str("mode_type", modeType).Str("timeframe", timeframe).Msg("Failed to get leaderboard")
		return nil, err
	}
	defer rows.Close()

	var entries []LeaderboardEntry
	for rows.Next() {
		var entry LeaderboardEntry
		err := rows.Scan(&entry.PlayerID, &entry.PlayerName, &entry.CharacterName,
			&entry.ModeType, &entry.Score, &entry.CompletionTime, &entry.Rank, &entry.AchievedAt)
		if err != nil {
			s.logger.Error().Err(err).Msg("Failed to scan leaderboard entry")
			continue
		}
		entries = append(entries, entry)
	}

	return entries, rows.Err()
}

// GetAvailableRestrictedModes returns the available restricted modes configuration
func (s *Service) GetAvailableRestrictedModes(ctx context.Context) (map[string]interface{}, error) {
	// This would typically come from a configuration table or cache
	// For now, return static configuration based on the OpenAPI spec
	modes := map[string]interface{}{
		"ironman": map[string]interface{}{
			"mode_type": "ironman",
			"name": "Ironman Mode",
			"description": "Permadeath - permanent character death on failure",
			"requirements": map[string]interface{}{
				"required_achievements": []string{"survivor"},
				"min_character_level": 10,
				"cooldown_period": 604800, // 7 days in seconds
			},
			"rewards": map[string]interface{}{
				"unique_titles": []string{"Ironman", "Undefeated"},
				"base_xp_multiplier": 2.0,
				"item_drops_multiplier": 1.5,
				"leaderboard_bonuses": true,
			},
		},
		"hardcore": map[string]interface{}{
			"mode_type": "hardcore",
			"name": "Hardcore Mode",
			"description": "Limited resources and equipment",
			"requirements": map[string]interface{}{
				"required_achievements": []string{"resourceful"},
				"min_character_level": 5,
				"cooldown_period": 86400, // 1 day in seconds
			},
			"rewards": map[string]interface{}{
				"unique_titles": []string{"Hardcore Survivor", "Resource Master"},
				"base_xp_multiplier": 1.8,
				"item_drops_multiplier": 1.3,
				"leaderboard_bonuses": true,
			},
		},
		"solo": map[string]interface{}{
			"mode_type": "solo",
			"name": "Solo Challenge",
			"description": "Complete content without group assistance",
			"requirements": map[string]interface{}{
				"min_character_level": 1,
				"cooldown_period": 3600, // 1 hour in seconds
			},
			"rewards": map[string]interface{}{
				"unique_titles": []string{"Solo Master", "Independent"},
				"base_xp_multiplier": 1.5,
				"item_drops_multiplier": 1.2,
				"leaderboard_bonuses": true,
			},
		},
		"nodeath": map[string]interface{}{
			"mode_type": "nodeath",
			"name": "No-Death Run",
			"description": "Complete content without dying",
			"requirements": map[string]interface{}{
				"min_character_level": 1,
				"cooldown_period": 1800, // 30 minutes in seconds
			},
			"rewards": map[string]interface{}{
				"unique_titles": []string{"Immortal", "Flawless"},
				"base_xp_multiplier": 1.3,
				"item_drops_multiplier": 1.1,
				"leaderboard_bonuses": true,
			},
		},
	}

	return modes, nil
}

// Helper functions

func (s *Service) calculateLeaderboardRank(ctx context.Context, modeType string, score int, completionTime time.Duration) (int, error) {
	query := `
		SELECT COUNT(*) + 1 as rank
		FROM gameplay.restricted_mode_sessions
		WHERE mode_type = $1 AND is_active = false AND (
			current_score > $2 OR
			(current_score = $2 AND time_elapsed < $3)
		)
	`

	var rank int
	err := s.db.QueryRow(ctx, query, modeType, score, completionTime).Scan(&rank)
	return rank, err
}

// generateUUID generates a simple UUID for session IDs
func generateUUID() string {
	// In production, use a proper UUID library like google/uuid
	return time.Now().Format("20060102-150405-") + fmt.Sprintf("%06d", time.Now().Nanosecond()%1000000)
}
