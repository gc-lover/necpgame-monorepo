// Issue: #1943
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	_ "github.com/lib/pq"
)

// TournamentRepository handles data access with optimizations
type TournamentRepository struct {
	db *sql.DB
}

// NewTournamentRepository creates new repository with database connection
func NewTournamentRepository() *TournamentRepository {
	dbURL := getEnv("DATABASE_URL", "postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable")

	db, err := sql.Open("postgres", dbURL)
	if err != nil {
		// For now, just log and continue without DB
		fmt.Printf("Warning: Failed to connect to database: %v\n", err)
		return &TournamentRepository{db: nil}
	}

	if err := db.Ping(); err != nil {
		fmt.Printf("Warning: Failed to ping database: %v\n", err)
		return &TournamentRepository{db: nil}
	}

	// OPTIMIZATION: Database connection pool configuration (Issue #300)
	db.SetMaxOpenConns(25)           // Max 25 concurrent connections for tournament service
	db.SetMaxIdleConns(5)            // Keep 5 idle connections
	db.SetConnMaxLifetime(time.Hour) // Connection lifetime

	return &TournamentRepository{db: db}
}

// CreateTournament creates new tournament in database
func (r *TournamentRepository) CreateTournament(ctx context.Context, tournament *TournamentDefinition) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO tournaments.tournaments (
			tournament_id, name, description, guild_id, type, status,
			start_time, end_time, max_participants, current_participants,
			entry_fee, prize_pool, duration_minutes, rules, prize_distribution
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15)
	`

	_, err := r.db.ExecContext(ctx, query,
		tournament.TournamentID,
		tournament.Name,
		tournament.Description,
		tournament.GuildID,
		tournament.Type,
		tournament.Status,
		tournament.StartTime,
		tournament.EndTime,
		tournament.MaxParticipants,
		tournament.CurrentParticipants,
		tournament.EntryFee,
		tournament.PrizePool,
		tournament.DurationMinutes,
		tournament.Rules,
		tournament.PrizeDistribution,
	)

	if err != nil {
		return fmt.Errorf("failed to create tournament: %w", err)
	}

	return nil
}

// GetTournament retrieves tournament by ID
func (r *TournamentRepository) GetTournament(ctx context.Context, tournamentID string) (*TournamentDefinition, error) {
	if r.db == nil {
		return nil, fmt.Errorf("database not connected")
	}

	query := `
		SELECT tournament_id, name, description, guild_id, type, status,
		       start_time, end_time, max_participants, current_participants,
		       entry_fee, prize_pool, duration_minutes, rules, prize_distribution
		FROM tournaments.tournaments
		WHERE tournament_id = $1
	`

	var tournament TournamentDefinition
	err := r.db.QueryRowContext(ctx, query, tournamentID).Scan(
		&tournament.TournamentID,
		&tournament.Name,
		&tournament.Description,
		&tournament.GuildID,
		&tournament.Type,
		&tournament.Status,
		&tournament.StartTime,
		&tournament.EndTime,
		&tournament.MaxParticipants,
		&tournament.CurrentParticipants,
		&tournament.EntryFee,
		&tournament.PrizePool,
		&tournament.DurationMinutes,
		&tournament.Rules,
		&tournament.PrizeDistribution,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("tournament not found")
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get tournament: %w", err)
	}

	return &tournament, nil
}

// UpdateTournamentStatus updates tournament status
func (r *TournamentRepository) UpdateTournamentStatus(ctx context.Context, tournamentID, status string) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		UPDATE tournaments.tournaments SET
			status = $2,
			updated_at = CURRENT_TIMESTAMP
		WHERE tournament_id = $1
	`

	result, err := r.db.ExecContext(ctx, query, tournamentID, status)
	if err != nil {
		return fmt.Errorf("failed to update tournament status: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("tournament not found")
	}

	return nil
}

// RegisterParticipant registers a participant for tournament
func (r *TournamentRepository) RegisterParticipant(ctx context.Context, participant *TournamentParticipant) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO tournaments.participants (
			tournament_id, player_id, registered_at, status, score, rank
		) VALUES ($1, $2, $3, $4, $5, $6)
	`

	_, err := r.db.ExecContext(ctx, query,
		participant.TournamentID,
		participant.PlayerID,
		participant.RegisteredAt,
		participant.Status,
		participant.Score,
		participant.Rank,
	)

	if err != nil {
		return fmt.Errorf("failed to register participant: %w", err)
	}

	return nil
}

// GetTournamentParticipants returns all participants for tournament
func (r *TournamentRepository) GetTournamentParticipants(ctx context.Context, tournamentID string) ([]*TournamentParticipant, error) {
	if r.db == nil {
		return nil, fmt.Errorf("database not connected")
	}

	query := `
		SELECT tournament_id, player_id, registered_at, status, score, rank
		FROM tournaments.participants
		WHERE tournament_id = $1
		ORDER BY registered_at ASC
	`

	rows, err := r.db.QueryContext(ctx, query, tournamentID)
	if err != nil {
		return nil, fmt.Errorf("failed to query participants: %w", err)
	}
	defer rows.Close()

	var participants []*TournamentParticipant
	for rows.Next() {
		var participant TournamentParticipant
		err := rows.Scan(
			&participant.TournamentID,
			&participant.PlayerID,
			&participant.RegisteredAt,
			&participant.Status,
			&participant.Score,
			&participant.Rank,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan participant: %w", err)
		}
		participants = append(participants, &participant)
	}

	return participants, nil
}

// CreateMatch creates a new match
func (r *TournamentRepository) CreateMatch(ctx context.Context, match *Match) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO tournaments.matches (
			match_id, tournament_id, round, player1_id, player2_id,
			status, scheduled_time
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
	`

	_, err := r.db.ExecContext(ctx, query,
		match.MatchID,
		match.TournamentID,
		match.Round,
		match.Player1ID,
		match.Player2ID,
		match.Status,
		match.ScheduledTime,
	)

	if err != nil {
		return fmt.Errorf("failed to create match: %w", err)
	}

	return nil
}
