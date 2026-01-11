// Tournament Bracket Repository - PostgreSQL data access layer
// Issue: #2210
// Agent: Backend Agent
package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"

	"necpgame/services/tournament-bracket-service-go/internal/models"
)

// Repository handles database operations for tournament brackets
type Repository struct {
	pool *pgxpool.Pool
}

// NewRepository creates a new repository instance
func NewRepository(databaseURL string) (*Repository, error) {
	config, err := pgxpool.ParseConfig(databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Configure connection pool for high performance
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	return &Repository{pool: pool}, nil
}

// Close closes the database connection pool
func (r *Repository) Close() error {
	r.pool.Close()
	return nil
}

// BRACKET OPERATIONS

// CreateBracket creates a new tournament bracket
func (r *Repository) CreateBracket(ctx context.Context, bracket *models.Bracket) error {
	prizePoolJSON, _ := json.Marshal(bracket.PrizePool)
	rulesJSON, _ := json.Marshal(bracket.Rules)
	metadataJSON, _ := json.Marshal(bracket.Metadata)

	query := `
		INSERT INTO tournament.brackets (
			id, tournament_id, name, description, bracket_type, max_participants,
			current_round, total_rounds, status, start_date, end_date,
			prize_pool, rules, metadata, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16)`

	_, err := r.pool.Exec(ctx, query,
		bracket.ID, bracket.TournamentID, bracket.Name, bracket.Description,
		bracket.BracketType, bracket.MaxParticipants, bracket.CurrentRound,
		bracket.TotalRounds, bracket.Status, bracket.StartDate, bracket.EndDate,
		prizePoolJSON, rulesJSON, metadataJSON, bracket.CreatedAt, bracket.UpdatedAt)

	return err
}

// GetBracket retrieves a bracket by ID
func (r *Repository) GetBracket(ctx context.Context, bracketID uuid.UUID) (*models.Bracket, error) {
	query := `
		SELECT id, tournament_id, name, description, bracket_type, max_participants,
			   current_round, total_rounds, status, start_date, end_date,
			   winner_id, winner_name, prize_pool, rules, metadata, created_at, updated_at
		FROM tournament.brackets WHERE id = $1`

	var bracket models.Bracket
	var prizePoolJSON, rulesJSON, metadataJSON []byte

	err := r.pool.QueryRow(ctx, query, bracketID).Scan(
		&bracket.ID, &bracket.TournamentID, &bracket.Name, &bracket.Description,
		&bracket.BracketType, &bracket.MaxParticipants, &bracket.CurrentRound,
		&bracket.TotalRounds, &bracket.Status, &bracket.StartDate, &bracket.EndDate,
		&bracket.WinnerID, &bracket.WinnerName, &prizePoolJSON, &rulesJSON,
		&metadataJSON, &bracket.CreatedAt, &bracket.UpdatedAt)

	if err != nil {
		return nil, err
	}

	// Unmarshal JSON fields
	json.Unmarshal(prizePoolJSON, &bracket.PrizePool)
	json.Unmarshal(rulesJSON, &bracket.Rules)
	json.Unmarshal(metadataJSON, &bracket.Metadata)

	return &bracket, nil
}

// UpdateBracket updates an existing bracket
func (r *Repository) UpdateBracket(ctx context.Context, bracket *models.Bracket) error {
	prizePoolJSON, _ := json.Marshal(bracket.PrizePool)
	rulesJSON, _ := json.Marshal(bracket.Rules)
	metadataJSON, _ := json.Marshal(bracket.Metadata)

	query := `
		UPDATE tournament.brackets SET
			name = $2, description = $3, status = $4, start_date = $5, end_date = $6,
			winner_id = $7, winner_name = $8, prize_pool = $9, rules = $10,
			metadata = $11, updated_at = $12
		WHERE id = $1`

	_, err := r.pool.Exec(ctx, query,
		bracket.ID, bracket.Name, bracket.Description, bracket.Status,
		bracket.StartDate, bracket.EndDate, bracket.WinnerID, bracket.WinnerName,
		prizePoolJSON, rulesJSON, metadataJSON, time.Now().UTC())

	return err
}

// ListBrackets retrieves brackets with optional filtering
func (r *Repository) ListBrackets(ctx context.Context, tournamentID *string, status *models.BracketStatus, limit, offset int) ([]*models.Bracket, error) {
	query := `
		SELECT id, tournament_id, name, description, bracket_type, max_participants,
			   current_round, total_rounds, status, start_date, end_date,
			   winner_id, winner_name, prize_pool, rules, metadata, created_at, updated_at
		FROM tournament.brackets WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	if tournamentID != nil {
		argCount++
		query += fmt.Sprintf(" AND tournament_id = $%d", argCount)
		args = append(args, *tournamentID)
	}

	if status != nil {
		argCount++
		query += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, *status)
	}

	query += " ORDER BY created_at DESC"

	if limit > 0 {
		argCount++
		query += fmt.Sprintf(" LIMIT $%d", argCount)
		args = append(args, limit)
	}

	if offset > 0 {
		argCount++
		query += fmt.Sprintf(" OFFSET $%d", argCount)
		args = append(args, offset)
	}

	rows, err := r.pool.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var brackets []*models.Bracket
	for rows.Next() {
		var bracket models.Bracket
		var prizePoolJSON, rulesJSON, metadataJSON []byte

		err := rows.Scan(
			&bracket.ID, &bracket.TournamentID, &bracket.Name, &bracket.Description,
			&bracket.BracketType, &bracket.MaxParticipants, &bracket.CurrentRound,
			&bracket.TotalRounds, &bracket.Status, &bracket.StartDate, &bracket.EndDate,
			&bracket.WinnerID, &bracket.WinnerName, &prizePoolJSON, &rulesJSON,
			&metadataJSON, &bracket.CreatedAt, &bracket.UpdatedAt)

		if err != nil {
			return nil, err
		}

		// Unmarshal JSON fields
		json.Unmarshal(prizePoolJSON, &bracket.PrizePool)
		json.Unmarshal(rulesJSON, &bracket.Rules)
		json.Unmarshal(metadataJSON, &bracket.Metadata)

		brackets = append(brackets, &bracket)
	}

	return brackets, rows.Err()
}

// ROUND OPERATIONS

// CreateBracketRound creates a new bracket round
func (r *Repository) CreateBracketRound(ctx context.Context, round *models.BracketRound) error {
	metadataJSON, _ := json.Marshal(round.Metadata)

	query := `
		INSERT INTO tournament.bracket_rounds (
			id, bracket_id, round_number, round_name, round_type, status,
			start_date, end_date, total_matches, completed_matches, bye_count,
			metadata, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)`

	_, err := r.pool.Exec(ctx, query,
		round.ID, round.BracketID, round.RoundNumber, round.RoundName,
		round.RoundType, round.Status, round.StartDate, round.EndDate,
		round.TotalMatches, round.CompletedMatches, round.ByeCount,
		metadataJSON, round.CreatedAt, round.UpdatedAt)

	return err
}

// GetBracketRounds retrieves all rounds for a bracket
func (r *Repository) GetBracketRounds(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketRound, error) {
	query := `
		SELECT id, bracket_id, round_number, round_name, round_type, status,
			   start_date, end_date, total_matches, completed_matches, bye_count,
			   metadata, created_at, updated_at
		FROM tournament.bracket_rounds
		WHERE bracket_id = $1 ORDER BY round_number`

	rows, err := r.pool.Query(ctx, query, bracketID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var rounds []*models.BracketRound
	for rows.Next() {
		var round models.BracketRound
		var metadataJSON []byte

		err := rows.Scan(
			&round.ID, &round.BracketID, &round.RoundNumber, &round.RoundName,
			&round.RoundType, &round.Status, &round.StartDate, &round.EndDate,
			&round.TotalMatches, &round.CompletedMatches, &round.ByeCount,
			&metadataJSON, &round.CreatedAt, &round.UpdatedAt)

		if err != nil {
			return nil, err
		}

		json.Unmarshal(metadataJSON, &round.Metadata)
		rounds = append(rounds, &round)
	}

	return rounds, rows.Err()
}

// MATCH OPERATIONS

// CreateBracketMatch creates a new bracket match
func (r *Repository) CreateBracketMatch(ctx context.Context, match *models.BracketMatch) error {
	scoreDetailsJSON, _ := json.Marshal(match.ScoreDetails)
	matchStatsJSON, _ := json.Marshal(match.MatchStats)
	metadataJSON, _ := json.Marshal(match.Metadata)

	query := `
		INSERT INTO tournament.bracket_matches (
			id, bracket_id, round_id, match_number,
			participant1_id, participant1_name, participant1_seed, participant1_score, participant1_status,
			participant2_id, participant2_name, participant2_seed, participant2_score, participant2_status,
			winner_id, winner_name, loser_id, loser_name, status,
			scheduled_start, actual_start, completed_at, duration,
			map_name, game_mode, spectator_count, stream_url, replay_url,
			score_details, match_stats, metadata, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21, $22, $23, $24, $25, $26, $27, $28, $29, $30, $31, $32, $33, $34)`

	_, err := r.pool.Exec(ctx, query,
		match.ID, match.BracketID, match.RoundID, match.MatchNumber,
		match.Participant1ID, match.Participant1Name, match.Participant1Seed, match.Participant1Score, match.Participant1Status,
		match.Participant2ID, match.Participant2Name, match.Participant2Seed, match.Participant2Score, match.Participant2Status,
		match.WinnerID, match.WinnerName, match.LoserID, match.LoserName, match.Status,
		match.ScheduledStart, match.ActualStart, match.CompletedAt, match.Duration,
		match.MapName, match.GameMode, match.SpectatorCount, match.StreamURL, match.ReplayURL,
		scoreDetailsJSON, matchStatsJSON, metadataJSON, match.CreatedAt, match.UpdatedAt)

	return err
}

// UpdateBracketMatch updates an existing match
func (r *Repository) UpdateBracketMatch(ctx context.Context, match *models.BracketMatch) error {
	scoreDetailsJSON, _ := json.Marshal(match.ScoreDetails)
	matchStatsJSON, _ := json.Marshal(match.MatchStats)
	metadataJSON, _ := json.Marshal(match.Metadata)

	query := `
		UPDATE tournament.bracket_matches SET
			participant1_score = $2, participant1_status = $3,
			participant2_score = $4, participant2_status = $5,
			winner_id = $6, winner_name = $7, loser_id = $8, loser_name = $9,
			status = $10, actual_start = $11, completed_at = $12, duration = $13,
			spectator_count = $14, stream_url = $15, replay_url = $16,
			score_details = $17, match_stats = $18, metadata = $19, updated_at = $20
		WHERE id = $1`

	_, err := r.pool.Exec(ctx, query,
		match.ID, match.Participant1Score, match.Participant1Status,
		match.Participant2Score, match.Participant2Status,
		match.WinnerID, match.WinnerName, match.LoserID, match.LoserName,
		match.Status, match.ActualStart, match.CompletedAt, match.Duration,
		match.SpectatorCount, match.StreamURL, match.ReplayURL,
		scoreDetailsJSON, matchStatsJSON, metadataJSON, time.Now().UTC())

	return err
}

// GetBracketMatches retrieves matches for a round
func (r *Repository) GetBracketMatches(ctx context.Context, roundID uuid.UUID) ([]*models.BracketMatch, error) {
	query := `
		SELECT id, bracket_id, round_id, match_number,
			   participant1_id, participant1_name, participant1_seed, participant1_score, participant1_status,
			   participant2_id, participant2_name, participant2_seed, participant2_score, participant2_status,
			   winner_id, winner_name, loser_id, loser_name, status,
			   scheduled_start, actual_start, completed_at, duration,
			   map_name, game_mode, spectator_count, stream_url, replay_url,
			   score_details, match_stats, metadata, created_at, updated_at
		FROM tournament.bracket_matches
		WHERE round_id = $1 ORDER BY match_number`

	rows, err := r.pool.Query(ctx, query, roundID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var matches []*models.BracketMatch
	for rows.Next() {
		var match models.BracketMatch
		var scoreDetailsJSON, matchStatsJSON, metadataJSON []byte

		err := rows.Scan(
			&match.ID, &match.BracketID, &match.RoundID, &match.MatchNumber,
			&match.Participant1ID, &match.Participant1Name, &match.Participant1Seed, &match.Participant1Score, &match.Participant1Status,
			&match.Participant2ID, &match.Participant2Name, &match.Participant2Seed, &match.Participant2Score, &match.Participant2Status,
			&match.WinnerID, &match.WinnerName, &match.LoserID, &match.LoserName, &match.Status,
			&match.ScheduledStart, &match.ActualStart, &match.CompletedAt, &match.Duration,
			&match.MapName, &match.GameMode, &match.SpectatorCount, &match.StreamURL, &match.ReplayURL,
			&scoreDetailsJSON, &matchStatsJSON, &metadataJSON, &match.CreatedAt, &match.UpdatedAt)

		if err != nil {
			return nil, err
		}

		json.Unmarshal(scoreDetailsJSON, &match.ScoreDetails)
		json.Unmarshal(matchStatsJSON, &match.MatchStats)
		json.Unmarshal(metadataJSON, &match.Metadata)

		matches = append(matches, &match)
	}

	return matches, rows.Err()
}

// PARTICIPANT OPERATIONS

// CreateBracketParticipant adds a participant to a bracket
func (r *Repository) CreateBracketParticipant(ctx context.Context, participant *models.BracketParticipant) error {
	performanceStatsJSON, _ := json.Marshal(participant.PerformanceStats)
	metadataJSON, _ := json.Marshal(participant.Metadata)

	query := `
		INSERT INTO tournament.bracket_participants (
			id, bracket_id, participant_id, participant_name, participant_type,
			seed_number, current_round, status, joined_at, total_score, total_wins,
			total_losses, total_draws, average_score, performance_stats, metadata,
			created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18)`

	_, err := r.pool.Exec(ctx, query,
		participant.ID, participant.BracketID, participant.ParticipantID,
		participant.ParticipantName, participant.ParticipantType,
		participant.SeedNumber, participant.CurrentRound, participant.Status,
		participant.JoinedAt, participant.TotalScore, participant.TotalWins,
		participant.TotalLosses, participant.TotalDraws, participant.AverageScore,
		performanceStatsJSON, metadataJSON, participant.CreatedAt, participant.UpdatedAt)

	return err
}

// UpdateBracketParticipant updates a participant
func (r *Repository) UpdateBracketParticipant(ctx context.Context, participant *models.BracketParticipant) error {
	performanceStatsJSON, _ := json.Marshal(participant.PerformanceStats)
	metadataJSON, _ := json.Marshal(participant.Metadata)

	query := `
		UPDATE tournament.bracket_participants SET
			current_round = $2, status = $3, eliminated_at = $4, eliminated_round = $5,
			final_rank = $6, total_score = $7, total_wins = $8, total_losses = $9,
			total_draws = $10, average_score = $11, performance_stats = $12,
			metadata = $13, updated_at = $14
		WHERE id = $1`

	_, err := r.pool.Exec(ctx, query,
		participant.ID, participant.CurrentRound, participant.Status,
		participant.EliminatedAt, participant.EliminatedRound, participant.FinalRank,
		participant.TotalScore, participant.TotalWins, participant.TotalLosses,
		participant.TotalDraws, participant.AverageScore, performanceStatsJSON,
		metadataJSON, time.Now().UTC())

	return err
}

// GetBracketParticipants retrieves participants for a bracket
func (r *Repository) GetBracketParticipants(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketParticipant, error) {
	query := `
		SELECT id, bracket_id, participant_id, participant_name, participant_type,
			   seed_number, current_round, status, joined_at, eliminated_at,
			   eliminated_round, final_rank, total_score, total_wins, total_losses,
			   total_draws, average_score, performance_stats, metadata, created_at, updated_at
		FROM tournament.bracket_participants
		WHERE bracket_id = $1 ORDER BY seed_number NULLS LAST, participant_name`

	rows, err := r.pool.Query(ctx, query, bracketID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var participants []*models.BracketParticipant
	for rows.Next() {
		var participant models.BracketParticipant
		var performanceStatsJSON, metadataJSON []byte

		err := rows.Scan(
			&participant.ID, &participant.BracketID, &participant.ParticipantID,
			&participant.ParticipantName, &participant.ParticipantType,
			&participant.SeedNumber, &participant.CurrentRound, &participant.Status,
			&participant.JoinedAt, &participant.EliminatedAt, &participant.EliminatedRound,
			&participant.FinalRank, &participant.TotalScore, &participant.TotalWins,
			&participant.TotalLosses, &participant.TotalDraws, &participant.AverageScore,
			&performanceStatsJSON, &metadataJSON, &participant.CreatedAt, &participant.UpdatedAt)

		if err != nil {
			return nil, err
		}

		json.Unmarshal(performanceStatsJSON, &participant.PerformanceStats)
		json.Unmarshal(metadataJSON, &participant.Metadata)

		participants = append(participants, &participant)
	}

	return participants, rows.Err()
}

// GetBracketProgress calculates overall bracket progress
func (r *Repository) GetBracketProgress(ctx context.Context, bracketID uuid.UUID) (*models.BracketProgress, error) {
	query := `
		WITH bracket_stats AS (
			SELECT
				b.id as bracket_id,
				b.total_rounds,
				b.current_round,
				COALESCE(r.total_matches, 0) as total_matches,
				COALESCE(r.completed_matches, 0) as completed_matches,
				COUNT(p.id) as total_participants
			FROM tournament.brackets b
			LEFT JOIN tournament.bracket_rounds br ON br.bracket_id = b.id
			LEFT JOIN (
				SELECT bracket_id, SUM(total_matches) as total_matches, SUM(completed_matches) as completed_matches
				FROM tournament.bracket_rounds
				GROUP BY bracket_id
			) r ON r.bracket_id = b.id
			LEFT JOIN tournament.bracket_participants p ON p.bracket_id = b.id
			WHERE b.id = $1
			GROUP BY b.id, b.total_rounds, b.current_round, r.total_matches, r.completed_matches
		),
		active_stats AS (
			SELECT
				COUNT(*) as active_matches,
				COUNT(CASE WHEN status = 'active' THEN 1 END) as active_participants,
				COUNT(CASE WHEN status = 'eliminated' THEN 1 END) as eliminated_participants
			FROM tournament.bracket_matches m
			CROSS JOIN tournament.bracket_participants p
			WHERE m.bracket_id = $1 AND m.status IN ('scheduled', 'in_progress')
				AND p.bracket_id = $1
		)
		SELECT
			bs.bracket_id, bs.total_rounds, bs.current_round,
			bs.total_matches, bs.completed_matches,
			COALESCE(ast.active_matches, 0) as active_matches,
			bs.total_participants,
			COALESCE(ast.active_participants, 0) as active_participants,
			COALESCE(ast.eliminated_participants, 0) as eliminated_participants
		FROM bracket_stats bs
		CROSS JOIN active_stats ast`

	var progress models.BracketProgress
	err := r.pool.QueryRow(ctx, query, bracketID).Scan(
		&progress.BracketID, &progress.TotalRounds, &progress.CurrentRound,
		&progress.TotalMatches, &progress.CompletedMatches, &progress.ActiveMatches,
		&progress.TotalParticipants, &progress.ActiveParticipants, &progress.EliminatedParticipants)

	if err != nil {
		return nil, err
	}

	// Calculate progress percentage
	if progress.TotalMatches > 0 {
		progress.ProgressPercent = float64(progress.CompletedMatches) / float64(progress.TotalMatches) * 100
	}

	return &progress, nil
}

// DeleteBracket deletes a bracket by ID
func (r *Repository) DeleteBracket(ctx context.Context, bracketID uuid.UUID) error {
	query := `
		DELETE FROM tournament.brackets
		WHERE id = $1
	`

	_, err := r.pool.Exec(ctx, query, bracketID)
	if err != nil {
		return fmt.Errorf("failed to delete bracket: %w", err)
	}

	return nil
}

// UpdateBracketStatus updates the status of a bracket
func (r *Repository) UpdateBracketStatus(ctx context.Context, bracketID uuid.UUID, status models.BracketStatus) error {
	query := `
		UPDATE tournament.brackets
		SET status = $1, updated_at = NOW()
		WHERE id = $2
	`

	_, err := r.pool.Exec(ctx, query, status, bracketID)
	if err != nil {
		return fmt.Errorf("failed to update bracket status: %w", err)
	}

	return nil
}

// UpdateBracketCurrentRound updates the current round of a bracket
func (r *Repository) UpdateBracketCurrentRound(ctx context.Context, bracketID uuid.UUID, currentRound int) error {
	query := `
		UPDATE tournament.brackets
		SET current_round = $1, updated_at = NOW()
		WHERE id = $2
	`

	_, err := r.pool.Exec(ctx, query, currentRound, bracketID)
	if err != nil {
		return fmt.Errorf("failed to update bracket current round: %w", err)
	}

	return nil
}

// GetRoundsByBracketID retrieves all rounds for a bracket
func (r *Repository) GetRoundsByBracketID(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketRound, error) {
	query := `
		SELECT id, bracket_id, round_number, name, status, start_date, end_date,
			   created_at, updated_at
		FROM tournament.bracket_rounds
		WHERE bracket_id = $1
		ORDER BY round_number ASC
	`

	rows, err := r.pool.Query(ctx, query, bracketID)
	if err != nil {
		return nil, fmt.Errorf("failed to query rounds: %w", err)
	}
	defer rows.Close()

	var rounds []*models.BracketRound
	for rows.Next() {
		var round models.BracketRound
		var startDate, endDate *time.Time

		err := rows.Scan(&round.ID, &round.BracketID, &round.RoundNumber, &round.RoundName,
			&round.Status, &startDate, &endDate, &round.CreatedAt, &round.UpdatedAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan round: %w", err)
		}

		round.StartDate = startDate
		round.EndDate = endDate
		rounds = append(rounds, &round)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("rows error: %w", err)
	}

	return rounds, nil
}

// GetRound retrieves a round by ID
func (r *Repository) GetRound(ctx context.Context, roundID uuid.UUID) (*models.BracketRound, error) {
	query := `
		SELECT id, bracket_id, round_number, name, status, start_date, end_date,
			   created_at, updated_at
		FROM tournament.bracket_rounds
		WHERE id = $1
	`

	var round models.BracketRound
	var startDate, endDate *time.Time

	err := r.pool.QueryRow(ctx, query, roundID).Scan(
		&round.ID, &round.BracketID, &round.RoundNumber, &round.RoundName,
		&round.Status, &startDate, &endDate, &round.CreatedAt, &round.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get round: %w", err)
	}

	round.StartDate = startDate
	round.EndDate = endDate
	return &round, nil
}

// UpdateRound updates an existing round
func (r *Repository) UpdateRound(ctx context.Context, round *models.BracketRound) error {
	query := `
		UPDATE tournament.bracket_rounds
		SET name = $1, status = $2, start_date = $3, end_date = $4, updated_at = NOW()
		WHERE id = $5
	`

	_, err := r.pool.Exec(ctx, query, round.RoundName, round.Status, round.StartDate,
		round.EndDate, round.ID)
	if err != nil {
		return fmt.Errorf("failed to update round: %w", err)
	}

	return nil
}

// DeleteRound deletes a round by ID
func (r *Repository) DeleteRound(ctx context.Context, roundID uuid.UUID) error {
	query := `
		DELETE FROM tournament.bracket_rounds
		WHERE id = $1
	`

	_, err := r.pool.Exec(ctx, query, roundID)
	if err != nil {
		return fmt.Errorf("failed to delete round: %w", err)
	}

	return nil
}

// GetMatch retrieves a match by ID
func (r *Repository) GetMatch(ctx context.Context, matchID uuid.UUID) (*models.BracketMatch, error) {
	query := `
		SELECT id, bracket_id, round_id, match_number, participant1_id, participant1_name,
			   participant1_seed, participant2_id, participant2_name, participant2_seed,
			   status, winner_id, winner_name, score_participant1, score_participant2,
			   scheduled_time, started_at, completed_at, duration, metadata, created_at, updated_at
		FROM tournament.bracket_matches
		WHERE id = $1
	`

	var match models.BracketMatch
	var scheduledTime, startedAt, completedAt *time.Time
	var duration time.Duration

	err := r.pool.QueryRow(ctx, query, matchID).Scan(
		&match.ID, &match.BracketID, &match.RoundID, &match.MatchNumber,
		&match.Participant1ID, &match.Participant1Name, &match.Participant1Seed,
		&match.Participant2ID, &match.Participant2Name, &match.Participant2Seed,
		&match.Status, &match.WinnerID, &match.WinnerName,
		&match.Participant1Score, &match.Participant2Score,
		&scheduledTime, &startedAt, &completedAt, &duration,
		&match.Metadata, &match.CreatedAt, &match.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get match: %w", err)
	}

	match.ScheduledStart = scheduledTime
	match.ActualStart = startedAt
	match.CompletedAt = completedAt
	match.Duration = duration
	return &match, nil
}

// DeleteMatch deletes a match by ID
func (r *Repository) DeleteMatch(ctx context.Context, matchID uuid.UUID) error {
	query := `
		DELETE FROM tournament.bracket_matches
		WHERE id = $1
	`

	_, err := r.pool.Exec(ctx, query, matchID)
	if err != nil {
		return fmt.Errorf("failed to delete match: %w", err)
	}

	return nil
}

// GetParticipant retrieves a participant by ID
func (r *Repository) GetParticipant(ctx context.Context, participantID string) (*models.BracketParticipant, error) {
	query := `
		SELECT id, bracket_id, user_id, display_name, seed_number, status,
			   current_round, final_rank, total_score, total_wins, total_losses,
			   total_draws, performance_stats, metadata, joined_at, updated_at
		FROM tournament.bracket_participants
		WHERE id = $1
	`

	var participant models.BracketParticipant
	var joinedAt time.Time

	err := r.pool.QueryRow(ctx, query, participantID).Scan(
		&participant.ID, &participant.BracketID, &participant.ParticipantID,
		&participant.ParticipantName, &participant.SeedNumber, &participant.Status,
		&participant.CurrentRound, &participant.FinalRank, &participant.TotalScore,
		&participant.TotalWins, &participant.TotalLosses, &participant.TotalDraws,
		&participant.PerformanceStats, &participant.Metadata,
		&joinedAt, &participant.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to get participant: %w", err)
	}

	participant.JoinedAt = joinedAt
	return &participant, nil
}

// RemoveParticipant removes a participant from bracket
func (r *Repository) RemoveParticipant(ctx context.Context, participantID string) error {
	query := `
		DELETE FROM tournament.bracket_participants
		WHERE id = $1
	`

	_, err := r.pool.Exec(ctx, query, participantID)
	if err != nil {
		return fmt.Errorf("failed to remove participant: %w", err)
	}

	return nil
}

// GetParticipantsByBracketID retrieves all participants for a bracket
func (r *Repository) GetParticipantsByBracketID(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketParticipant, error) {
	query := `
		SELECT id, bracket_id, user_id, display_name, seed_number, status,
			   current_round, final_rank, total_score, total_wins, total_losses,
			   total_draws, performance_stats, metadata, joined_at, updated_at
		FROM tournament.bracket_participants
		WHERE bracket_id = $1
		ORDER BY seed_number ASC
	`

	rows, err := r.pool.Query(ctx, query, bracketID)
	if err != nil {
		return nil, fmt.Errorf("failed to query participants: %w", err)
	}
	defer rows.Close()

	var participants []*models.BracketParticipant
	for rows.Next() {
		var participant models.BracketParticipant
		var joinedAt time.Time

		err := rows.Scan(&participant.ID, &participant.BracketID, &participant.ParticipantID,
			&participant.ParticipantName, &participant.SeedNumber, &participant.Status,
			&participant.CurrentRound, &participant.FinalRank, &participant.TotalScore,
			&participant.TotalWins, &participant.TotalLosses, &participant.TotalDraws,
			&participant.PerformanceStats, &participant.Metadata,
			&joinedAt, &participant.UpdatedAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan participant: %w", err)
		}

		participant.JoinedAt = joinedAt
		participants = append(participants, &participant)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("rows error: %w", err)
	}

	return participants, nil
}

// GetMatchesByBracketID retrieves all matches for a bracket
func (r *Repository) GetMatchesByBracketID(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketMatch, error) {
	query := `
		SELECT m.id, m.bracket_id, m.round_id, m.match_number, m.participant1_id, m.participant1_name,
			   m.participant1_seed, m.participant2_id, m.participant2_name, m.participant2_seed,
			   m.status, m.winner_id, m.winner_name, m.score_participant1, m.score_participant2,
			   m.scheduled_time, m.started_at, m.completed_at, m.duration, m.metadata,
			   m.created_at, m.updated_at, r.round_number
		FROM tournament.bracket_matches m
		JOIN tournament.bracket_rounds r ON m.round_id = r.id
		WHERE m.bracket_id = $1
		ORDER BY r.round_number ASC, m.match_number ASC
	`

	rows, err := r.pool.Query(ctx, query, bracketID)
	if err != nil {
		return nil, fmt.Errorf("failed to query matches: %w", err)
	}
	defer rows.Close()

	var matches []*models.BracketMatch
	for rows.Next() {
		var match models.BracketMatch
		var scheduledTime, startedAt, completedAt *time.Time
		var duration time.Duration

		err := rows.Scan(&match.ID, &match.BracketID, &match.RoundID, &match.MatchNumber,
			&match.Participant1ID, &match.Participant1Name, &match.Participant1Seed,
			&match.Participant2ID, &match.Participant2Name, &match.Participant2Seed,
			&match.Status, &match.WinnerID, &match.WinnerName,
			&match.Participant1Score, &match.Participant2Score,
			&scheduledTime, &startedAt, &completedAt, &duration,
			&match.Metadata, &match.CreatedAt, &match.UpdatedAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan match: %w", err)
		}

		match.ScheduledStart = scheduledTime
		match.ActualStart = startedAt
		match.CompletedAt = completedAt
		match.Duration = duration
		matches = append(matches, &match)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("rows error: %w", err)
	}

	return matches, nil
}

// UpdateMatchStatus updates the status of a match
func (r *Repository) UpdateMatchStatus(ctx context.Context, matchID uuid.UUID, status models.MatchStatus) error {
	query := `
		UPDATE tournament.bracket_matches
		SET status = $1,
		    started_at = CASE WHEN $1 = 'in_progress' AND started_at IS NULL THEN NOW() ELSE started_at END,
		    completed_at = CASE WHEN $1 = 'completed' THEN NOW() ELSE completed_at END,
		    updated_at = NOW()
		WHERE id = $2
	`

	_, err := r.pool.Exec(ctx, query, status, matchID)
	if err != nil {
		return fmt.Errorf("failed to update match status: %w", err)
	}

	return nil
}