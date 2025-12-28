// Issue: #2210
package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// Tournament represents a tournament
type Tournament struct {
	ID                   uuid.UUID              `json:"id" db:"id"`
	Name                 string                 `json:"name" db:"name"`
	Description          string                 `json:"description" db:"description"`
	GameMode             string                 `json:"game_mode" db:"game_mode"`
	TournamentType       string                 `json:"tournament_type" db:"tournament_type"`
	MaxParticipants      int                    `json:"max_participants" db:"max_participants"`
	CurrentParticipants  int                    `json:"current_participants" db:"current_participants"`
	MinSkillLevel        int                    `json:"min_skill_level" db:"min_skill_level"`
	MaxSkillLevel        int                    `json:"max_skill_level" db:"max_skill_level"`
	EntryFee             int                    `json:"entry_fee" db:"entry_fee"`
	PrizePool            map[string]interface{} `json:"prize_pool" db:"prize_pool"`
	Status               string                 `json:"status" db:"status"`
	RegistrationStart    *time.Time             `json:"registration_start" db:"registration_start"`
	RegistrationEnd      *time.Time             `json:"registration_end" db:"registration_end"`
	StartTime            *time.Time             `json:"start_time" db:"start_time"`
	EndTime              *time.Time             `json:"end_time" db:"end_time"`
	Rules                map[string]interface{} `json:"rules" db:"rules"`
	Metadata             map[string]interface{} `json:"metadata" db:"metadata"`
	CreatedAt            time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt            time.Time              `json:"updated_at" db:"updated_at"`
}

// Participant represents a tournament participant
type Participant struct {
	ID               uuid.UUID              `json:"id" db:"id"`
	TournamentID     uuid.UUID              `json:"tournament_id" db:"tournament_id"`
	PlayerID         string                 `json:"player_id" db:"player_id"`
	PlayerName       string                 `json:"player_name" db:"player_name"`
	SkillRating      int                    `json:"skill_rating" db:"skill_rating"`
	RegistrationTime time.Time              `json:"registration_time" db:"registration_time"`
	Status           string                 `json:"status" db:"status"`
	Seed             *int                   `json:"seed" db:"seed"`
	Division         string                 `json:"division" db:"division"`
	Metadata         map[string]interface{} `json:"metadata" db:"metadata"`
}

// Bracket represents a tournament bracket
type Bracket struct {
	ID           uuid.UUID              `json:"id" db:"id"`
	TournamentID uuid.UUID              `json:"tournament_id" db:"tournament_id"`
	BracketName  string                 `json:"bracket_name" db:"bracket_name"`
	RoundNumber  int                    `json:"round_number" db:"round_number"`
	RoundName    string                 `json:"round_name" db:"round_name"`
	Status       string                 `json:"status" db:"status"`
	Metadata     map[string]interface{} `json:"metadata" db:"metadata"`
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
}

// Match represents a tournament match
type Match struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	TournamentID    uuid.UUID              `json:"tournament_id" db:"tournament_id"`
	BracketID       uuid.UUID              `json:"bracket_id" db:"bracket_id"`
	MatchNumber     int                    `json:"match_number" db:"match_number"`
	Status          string                 `json:"status" db:"status"`
	ScheduledTime   *time.Time             `json:"scheduled_time" db:"scheduled_time"`
	StartTime       *time.Time             `json:"start_time" db:"start_time"`
	EndTime         *time.Time             `json:"end_time" db:"end_time"`
	Duration        *time.Duration         `json:"duration" db:"duration"`
	WinnerID        *uuid.UUID             `json:"winner_id" db:"winner_id"`
	WinnerScore     *int                   `json:"winner_score" db:"winner_score"`
	LoserID         *uuid.UUID             `json:"loser_id" db:"loser_id"`
	LoserScore      *int                   `json:"loser_score" db:"loser_score"`
	MapName         string                 `json:"map_name" db:"map_name"`
	GameMode        string                 `json:"game_mode" db:"game_mode"`
	ServerID        string                 `json:"server_id" db:"server_id"`
	SpectatorCount  int                    `json:"spectator_count" db:"spectator_count"`
	ReplayAvailable bool                   `json:"replay_available" db:"replay_available"`
	ReplayURL       string                 `json:"replay_url" db:"replay_url"`
	Statistics      map[string]interface{} `json:"statistics" db:"statistics"`
	Events          map[string]interface{} `json:"events" db:"events"`
	Metadata        map[string]interface{} `json:"metadata" db:"metadata"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

// Spectator represents a tournament spectator
type Spectator struct {
	ID         string                 `json:"id" db:"id"`
	MatchID    string                 `json:"match_id" db:"match_id"`
	PlayerID   string                 `json:"player_id" db:"player_id"`
	PlayerName string                 `json:"player_name" db:"player_name"`
	JoinedAt   time.Time              `json:"joined_at" db:"joined_at"`
	LeftAt     *time.Time             `json:"left_at" db:"left_at"`
	ViewMode   string                 `json:"view_mode" db:"view_mode"`
	FollowID   string                 `json:"follow_id" db:"follow_id"`
	CameraPos  map[string]interface{} `json:"camera_pos" db:"camera_pos"`
	Status     string                 `json:"status" db:"status"`
	IsVIP      bool                   `json:"is_vip" db:"is_vip"`
	Metadata   map[string]interface{} `json:"metadata" db:"metadata"`
	CreatedAt  time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt  time.Time              `json:"updated_at" db:"updated_at"`
}

// TournamentRepository handles database operations
type TournamentRepository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewConnection creates a new database connection
func NewConnection(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(150) // Higher for tournament service
	db.SetMaxIdleConns(30)
	db.SetConnMaxLifetime(time.Hour)

	return db, nil
}

// NewRedisClient creates a new Redis client
func NewRedisClient(redisURL string) (*redis.Client, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx := context.Background()
	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// NewTournamentRepository creates a new tournament repository
func NewTournamentRepository(db *sql.DB, redis *redis.Client, logger *zap.SugaredLogger) *TournamentRepository {
	return &TournamentRepository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// GetTournaments retrieves tournaments with filtering
func (r *TournamentRepository) GetTournaments(ctx context.Context, status *string, gameMode *string, limit int, offset int) ([]*Tournament, error) {
	query := `
		SELECT id, name, description, game_mode, tournament_type, max_participants, current_participants,
			   min_skill_level, max_skill_level, entry_fee, prize_pool, status, registration_start,
			   registration_end, start_time, end_time, rules, metadata, created_at, updated_at
		FROM tournament.tournaments
		WHERE ($1 = '' OR status = $1)
		AND ($2 = '' OR game_mode = $2)
		ORDER BY created_at DESC
		LIMIT $3 OFFSET $4
	`

	rows, err := r.db.QueryContext(ctx, query, status, gameMode, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to get tournaments: %v", err)
		return nil, fmt.Errorf("failed to get tournaments: %w", err)
	}
	defer rows.Close()

	var tournaments []*Tournament
	for rows.Next() {
		var t Tournament
		var prizePoolJSON, rulesJSON, metadataJSON []byte

		err := rows.Scan(
			&t.ID, &t.Name, &t.Description, &t.GameMode, &t.TournamentType, &t.MaxParticipants,
			&t.CurrentParticipants, &t.MinSkillLevel, &t.MaxSkillLevel, &t.EntryFee, &prizePoolJSON,
			&t.Status, &t.RegistrationStart, &t.RegistrationEnd, &t.StartTime, &t.EndTime,
			&rulesJSON, &metadataJSON, &t.CreatedAt, &t.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan tournament: %v", err)
			continue
		}

		json.Unmarshal(prizePoolJSON, &t.PrizePool)
		json.Unmarshal(rulesJSON, &t.Rules)
		json.Unmarshal(metadataJSON, &t.Metadata)

		tournaments = append(tournaments, &t)
	}

	return tournaments, nil
}

// GetTournament retrieves a single tournament
func (r *TournamentRepository) GetTournament(ctx context.Context, tournamentID uuid.UUID) (*Tournament, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("tournament:%s", tournamentID.String())
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var tournament Tournament
		if err := json.Unmarshal([]byte(cached), &tournament); err == nil {
			return &tournament, nil
		}
	}

	// Fallback to database
	query := `
		SELECT id, name, description, game_mode, tournament_type, max_participants, current_participants,
			   min_skill_level, max_skill_level, entry_fee, prize_pool, status, registration_start,
			   registration_end, start_time, end_time, rules, metadata, created_at, updated_at
		FROM tournament.tournaments
		WHERE id = $1
	`

	var t Tournament
	var prizePoolJSON, rulesJSON, metadataJSON []byte

	err = r.db.QueryRowContext(ctx, query, tournamentID).Scan(
		&t.ID, &t.Name, &t.Description, &t.GameMode, &t.TournamentType, &t.MaxParticipants,
		&t.CurrentParticipants, &t.MinSkillLevel, &t.MaxSkillLevel, &t.EntryFee, &prizePoolJSON,
		&t.Status, &t.RegistrationStart, &t.RegistrationEnd, &t.StartTime, &t.EndTime,
		&rulesJSON, &metadataJSON, &t.CreatedAt, &t.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("tournament not found")
		}
		r.logger.Errorf("Failed to get tournament: %v", err)
		return nil, fmt.Errorf("failed to get tournament: %w", err)
	}

	json.Unmarshal(prizePoolJSON, &t.PrizePool)
	json.Unmarshal(rulesJSON, &t.Rules)
	json.Unmarshal(metadataJSON, &t.Metadata)

	// Cache result
	tournamentJSON, _ := json.Marshal(t)
	r.redis.Set(ctx, cacheKey, tournamentJSON, 30*time.Minute)

	return &t, nil
}

// CreateTournament creates a new tournament
func (r *TournamentRepository) CreateTournament(ctx context.Context, tournament *Tournament) error {
	prizePoolJSON, _ := json.Marshal(tournament.PrizePool)
	rulesJSON, _ := json.Marshal(tournament.Rules)
	metadataJSON, _ := json.Marshal(tournament.Metadata)

	query := `
		INSERT INTO tournament.tournaments (
			id, name, description, game_mode, tournament_type, max_participants,
			min_skill_level, max_skill_level, entry_fee, prize_pool, registration_start,
			registration_end, start_time, rules, metadata
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)
	`

	_, err := r.db.ExecContext(ctx, query,
		tournament.ID, tournament.Name, tournament.Description, tournament.GameMode,
		tournament.TournamentType, tournament.MaxParticipants, tournament.MinSkillLevel,
		tournament.MaxSkillLevel, tournament.EntryFee, prizePoolJSON, tournament.RegistrationStart,
		tournament.RegistrationEnd, tournament.StartTime, rulesJSON, metadataJSON)

	if err != nil {
		r.logger.Errorf("Failed to create tournament: %v", err)
		return fmt.Errorf("failed to create tournament: %w", err)
	}

	return nil
}

// RegisterParticipant registers a player for a tournament
func (r *TournamentRepository) RegisterParticipant(ctx context.Context, participant *Participant) error {
	metadataJSON, _ := json.Marshal(participant.Metadata)

	query := `
		INSERT INTO tournament.participants (
			id, tournament_id, player_id, player_name, skill_rating, seed, division, metadata
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		participant.ID, participant.TournamentID, participant.PlayerID, participant.PlayerName,
		participant.SkillRating, participant.Seed, participant.Division, metadataJSON)

	if err != nil {
		r.logger.Errorf("Failed to register participant: %v", err)
		return fmt.Errorf("failed to register participant: %w", err)
	}

	// Update participant count
	updateQuery := `UPDATE tournament.tournaments SET current_participants = current_participants + 1 WHERE id = $1`
	_, err = r.db.ExecContext(ctx, updateQuery, participant.TournamentID)
	if err != nil {
		r.logger.Errorf("Failed to update participant count: %v", err)
	}

	return nil
}

// GetTournamentParticipants gets all participants for a tournament
func (r *TournamentRepository) GetTournamentParticipants(ctx context.Context, tournamentID uuid.UUID, limit int, offset int) ([]*Participant, error) {
	query := `
		SELECT id, tournament_id, player_id, player_name, skill_rating, registration_time,
			   status, seed, division, metadata
		FROM tournament.participants
		WHERE tournament_id = $1
		ORDER BY registration_time ASC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, tournamentID, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to get participants: %v", err)
		return nil, fmt.Errorf("failed to get participants: %w", err)
	}
	defer rows.Close()

	var participants []*Participant
	for rows.Next() {
		var p Participant
		var metadataJSON []byte

		err := rows.Scan(
			&p.ID, &p.TournamentID, &p.PlayerID, &p.PlayerName, &p.SkillRating,
			&p.RegistrationTime, &p.Status, &p.Seed, &p.Division, &metadataJSON,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan participant: %v", err)
			continue
		}

		json.Unmarshal(metadataJSON, &p.Metadata)
		participants = append(participants, &p)
	}

	return participants, nil
}

// GetMatchesByBracket gets matches for a specific bracket
func (r *TournamentRepository) GetMatchesByBracket(ctx context.Context, bracketID uuid.UUID) ([]*Match, error) {
	query := `
		SELECT id, tournament_id, bracket_id, match_number, status, scheduled_time, start_time,
			   end_time, winner_id, winner_score, loser_id, loser_score, map_name, game_mode,
			   server_id, spectator_count, replay_available, replay_url, statistics, events,
			   metadata, created_at, updated_at
		FROM tournament.matches
		WHERE bracket_id = $1
		ORDER BY match_number ASC
	`

	rows, err := r.db.QueryContext(ctx, query, bracketID)
	if err != nil {
		r.logger.Errorf("Failed to get matches: %v", err)
		return nil, fmt.Errorf("failed to get matches: %w", err)
	}
	defer rows.Close()

	var matches []*Match
	for rows.Next() {
		var m Match
		var statisticsJSON, eventsJSON, metadataJSON []byte

		err := rows.Scan(
			&m.ID, &m.TournamentID, &m.BracketID, &m.MatchNumber, &m.Status, &m.ScheduledTime,
			&m.StartTime, &m.EndTime, &m.WinnerID, &m.WinnerScore, &m.LoserID, &m.LoserScore,
			&m.MapName, &m.GameMode, &m.ServerID, &m.SpectatorCount, &m.ReplayAvailable,
			&m.ReplayURL, &statisticsJSON, &eventsJSON, &metadataJSON, &m.CreatedAt, &m.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan match: %v", err)
			continue
		}

		json.Unmarshal(statisticsJSON, &m.Statistics)
		json.Unmarshal(eventsJSON, &m.Events)
		json.Unmarshal(metadataJSON, &m.Metadata)

		matches = append(matches, &m)
	}

	return matches, nil
}

// SPECTATOR METHODS
// Issue: #2213 - Tournament Spectator Mode Implementation

// CreateSpectator creates a new spectator record
func (r *TournamentRepository) CreateSpectator(ctx context.Context, spectator *Spectator) error {
	query := `
		INSERT INTO tournament.spectators (
			id, match_id, player_id, player_name, joined_at, view_mode,
			follow_id, camera_pos, status, is_vip, metadata, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)
	`

	cameraPosJSON, _ := json.Marshal(spectator.CameraPos)
	metadataJSON, _ := json.Marshal(spectator.Metadata)

	_, err := r.db.ExecContext(ctx, query,
		spectator.ID, spectator.MatchID, spectator.PlayerID, spectator.PlayerName,
		spectator.JoinedAt, spectator.ViewMode, spectator.FollowID, cameraPosJSON,
		spectator.Status, spectator.IsVIP, metadataJSON, spectator.CreatedAt, spectator.UpdatedAt,
	)

	if err != nil {
		r.logger.Errorf("Failed to create spectator: %v", err)
		return fmt.Errorf("failed to create spectator: %w", err)
	}

	// Cache spectator info in Redis
	cacheKey := fmt.Sprintf("spectator:%s", spectator.ID)
	spectatorJSON, _ := json.Marshal(spectator)
	r.redis.Set(ctx, cacheKey, spectatorJSON, time.Hour)

	return nil
}

// GetSpectator retrieves a spectator by ID
func (r *TournamentRepository) GetSpectator(ctx context.Context, spectatorID string) (*Spectator, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("spectator:%s", spectatorID)
	if spectatorJSON, err := r.redis.Get(ctx, cacheKey).Result(); err == nil {
		var spectator Spectator
		if json.Unmarshal([]byte(spectatorJSON), &spectator) == nil {
			return &spectator, nil
		}
	}

	query := `
		SELECT id, match_id, player_id, player_name, joined_at, left_at, view_mode,
			   follow_id, camera_pos, status, is_vip, metadata, created_at, updated_at
		FROM tournament.spectators
		WHERE id = $1
	`

	var spectator Spectator
	var cameraPosJSON, metadataJSON []byte
	var leftAt sql.NullTime

	err := r.db.QueryRowContext(ctx, query, spectatorID).Scan(
		&spectator.ID, &spectator.MatchID, &spectator.PlayerID, &spectator.PlayerName,
		&spectator.JoinedAt, &leftAt, &spectator.ViewMode, &spectator.FollowID,
		&cameraPosJSON, &spectator.Status, &spectator.IsVIP, &metadataJSON,
		&spectator.CreatedAt, &spectator.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("spectator not found")
		}
		r.logger.Errorf("Failed to get spectator: %v", err)
		return nil, fmt.Errorf("failed to get spectator: %w", err)
	}

	if leftAt.Valid {
		spectator.LeftAt = &leftAt.Time
	}

	json.Unmarshal(cameraPosJSON, &spectator.CameraPos)
	json.Unmarshal(metadataJSON, &spectator.Metadata)

	// Cache the result
	spectatorJSON, _ := json.Marshal(spectator)
	r.redis.Set(ctx, cacheKey, spectatorJSON, time.Hour)

	return &spectator, nil
}

// UpdateSpectator updates spectator information
func (r *TournamentRepository) UpdateSpectator(ctx context.Context, spectator *Spectator) error {
	query := `
		UPDATE tournament.spectators SET
			left_at = $1, view_mode = $2, follow_id = $3, camera_pos = $4,
			status = $5, metadata = $6, updated_at = $7
		WHERE id = $8
	`

	cameraPosJSON, _ := json.Marshal(spectator.CameraPos)
	metadataJSON, _ := json.Marshal(spectator.Metadata)

	_, err := r.db.ExecContext(ctx, query,
		spectator.LeftAt, spectator.ViewMode, spectator.FollowID, cameraPosJSON,
		spectator.Status, metadataJSON, spectator.UpdatedAt, spectator.ID,
	)

	if err != nil {
		r.logger.Errorf("Failed to update spectator: %v", err)
		return fmt.Errorf("failed to update spectator: %w", err)
	}

	// Update cache
	cacheKey := fmt.Sprintf("spectator:%s", spectator.ID)
	spectatorJSON, _ := json.Marshal(spectator)
	r.redis.Set(ctx, cacheKey, spectatorJSON, time.Hour)

	return nil
}

// GetMatchSpectators gets all spectators for a specific match
func (r *TournamentRepository) GetMatchSpectators(ctx context.Context, matchID uuid.UUID) ([]*Spectator, error) {
	query := `
		SELECT id, match_id, player_id, player_name, joined_at, left_at, view_mode,
			   follow_id, camera_pos, status, is_vip, metadata, created_at, updated_at
		FROM tournament.spectators
		WHERE match_id = $1 AND status = 'active'
		ORDER BY joined_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, matchID.String())
	if err != nil {
		r.logger.Errorf("Failed to get match spectators: %v", err)
		return nil, fmt.Errorf("failed to get match spectators: %w", err)
	}
	defer rows.Close()

	var spectators []*Spectator
	for rows.Next() {
		var spectator Spectator
		var cameraPosJSON, metadataJSON []byte
		var leftAt sql.NullTime

		err := rows.Scan(
			&spectator.ID, &spectator.MatchID, &spectator.PlayerID, &spectator.PlayerName,
			&spectator.JoinedAt, &leftAt, &spectator.ViewMode, &spectator.FollowID,
			&cameraPosJSON, &spectator.Status, &spectator.IsVIP, &metadataJSON,
			&spectator.CreatedAt, &spectator.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan spectator: %v", err)
			continue
		}

		if leftAt.Valid {
			spectator.LeftAt = &leftAt.Time
		}

		json.Unmarshal(cameraPosJSON, &spectator.CameraPos)
		json.Unmarshal(metadataJSON, &spectator.Metadata)

		spectators = append(spectators, &spectator)
	}

	return spectators, nil
}

// UpdateMatchSpectatorCount updates the spectator count for a match
func (r *TournamentRepository) UpdateMatchSpectatorCount(ctx context.Context, matchID uuid.UUID, count int) error {
	query := `UPDATE tournament.matches SET spectator_count = $1, updated_at = NOW() WHERE id = $2`

	_, err := r.db.ExecContext(ctx, query, count, matchID)
	if err != nil {
		r.logger.Errorf("Failed to update match spectator count: %v", err)
		return fmt.Errorf("failed to update match spectator count: %w", err)
	}

	// Update cache
	cacheKey := fmt.Sprintf("match:%s", matchID.String())
	r.redis.Del(ctx, cacheKey) // Invalidate cache

	return nil
}

// GetTournamentMatches gets all matches for a tournament (helper for spectator stats)
func (r *TournamentRepository) GetTournamentMatches(ctx context.Context, tournamentID uuid.UUID) ([]*Match, error) {
	query := `
		SELECT id, tournament_id, bracket_id, match_number, status, scheduled_time, start_time,
			   end_time, winner_id, winner_score, loser_id, loser_score, map_name, game_mode,
			   server_id, spectator_count, replay_available, replay_url, statistics, events,
			   metadata, created_at, updated_at
		FROM tournament.matches
		WHERE tournament_id = $1
		ORDER BY match_number ASC
	`

	rows, err := r.db.QueryContext(ctx, query, tournamentID)
	if err != nil {
		r.logger.Errorf("Failed to get tournament matches: %v", err)
		return nil, fmt.Errorf("failed to get tournament matches: %w", err)
	}
	defer rows.Close()

	var matches []*Match
	for rows.Next() {
		var m Match
		var statisticsJSON, eventsJSON, metadataJSON []byte

		err := rows.Scan(
			&m.ID, &m.TournamentID, &m.BracketID, &m.MatchNumber, &m.Status, &m.ScheduledTime,
			&m.StartTime, &m.EndTime, &m.WinnerID, &m.WinnerScore, &m.LoserID, &m.LoserScore,
			&m.MapName, &m.GameMode, &m.ServerID, &m.SpectatorCount, &m.ReplayAvailable,
			&m.ReplayURL, &statisticsJSON, &eventsJSON, &metadataJSON, &m.CreatedAt, &m.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan match: %v", err)
			continue
		}

		json.Unmarshal(statisticsJSON, &m.Statistics)
		json.Unmarshal(eventsJSON, &m.Events)
		json.Unmarshal(metadataJSON, &m.Metadata)

		matches = append(matches, &m)
	}

	return matches, nil
}
