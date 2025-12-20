// Package server Issue: #1856
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for guild war service
type Repository struct {
	db *pgxpool.Pool
}

// GuildWar internal model
type GuildWar struct {
	ID            uuid.UUID
	AttackerID    uuid.UUID
	DefenderID    uuid.UUID
	Status        string // pending, active, completed, cancelled
	StartTime     *time.Time
	EndTime       *time.Time
	WinnerID      *uuid.UUID
	AttackerScore int
	DefenderScore int
	CreatedAt     time.Time
}

// WarParticipant internal model
type WarParticipant struct {
	ID       uuid.UUID
	WarID    uuid.UUID
	UserID   uuid.UUID
	GuildID  uuid.UUID
	Score    int
	Kills    int
	Deaths   int
	JoinedAt time.Time
}

// NewRepository creates new repository with database connection
func NewRepository(connStr string) (*Repository, error) {
	// DB connection pool configuration (optimization)
	config, err := pgxpool.ParseConfig(connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance (OPTIMIZATION: Issue #1856)
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() {
	r.db.Close()
}

// GetGuildWars retrieves guild wars with pagination
func (r *Repository) GetGuildWars(ctx context.Context, status *string, limit *int) ([]*GuildWar, error) {
	query := `
		SELECT id, attacker_id, defender_id, status, start_time, end_time, winner_id, attacker_score, defender_score, created_at
		FROM guilds.wars
		WHERE ($1::text IS NULL OR status = $1)
		ORDER BY created_at DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, status, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var wars []*GuildWar
	for rows.Next() {
		var w GuildWar
		var startTime, endTime sql.NullTime
		var winnerID sql.NullString

		err := rows.Scan(&w.ID, &w.AttackerID, &w.DefenderID, &w.Status, &startTime, &endTime, &winnerID, &w.AttackerScore, &w.DefenderScore, &w.CreatedAt)
		if err != nil {
			return nil, err
		}

		if startTime.Valid {
			w.StartTime = &startTime.Time
		}
		if endTime.Valid {
			w.EndTime = &endTime.Time
		}
		if winnerID.Valid {
			if id, err := uuid.Parse(winnerID.String); err == nil {
				w.WinnerID = &id
			}
		}

		wars = append(wars, &w)
	}

	return wars, rows.Err()
}

// GetGuildWarByID retrieves a guild war by ID
func (r *Repository) GetGuildWarByID(ctx context.Context, id uuid.UUID) (*GuildWar, error) {
	query := `
		SELECT id, attacker_id, defender_id, status, start_time, end_time, winner_id, attacker_score, defender_score, created_at
		FROM guilds.wars
		WHERE id = $1
	`

	var w GuildWar
	var startTime, endTime sql.NullTime
	var winnerID sql.NullString

	err := r.db.QueryRow(ctx, query, id).Scan(
		&w.ID, &w.AttackerID, &w.DefenderID, &w.Status, &startTime, &endTime, &winnerID, &w.AttackerScore, &w.DefenderScore, &w.CreatedAt,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, ErrGuildWarNotFound
		}
		return nil, err
	}

	if startTime.Valid {
		w.StartTime = &startTime.Time
	}
	if endTime.Valid {
		w.EndTime = &endTime.Time
	}
	if winnerID.Valid {
		if id, err := uuid.Parse(winnerID.String); err == nil {
			w.WinnerID = &id
		}
	}

	return &w, nil
}

// DeclareWar creates a new guild war declaration
func (r *Repository) DeclareWar(ctx context.Context, attackerID, defenderID uuid.UUID) (*GuildWar, error) {
	query := `
		INSERT INTO guilds.wars (id, attacker_id, defender_id, status, start_time, created_at)
		VALUES (gen_random_uuid(), $1, $2, 'pending', NOW() + INTERVAL '1 hour', NOW())
		RETURNING id, attacker_id, defender_id, status, start_time, end_time, winner_id, attacker_score, defender_score, created_at
	`

	var w GuildWar
	var startTime, endTime sql.NullTime
	var winnerID sql.NullString

	err := r.db.QueryRow(ctx, query, attackerID, defenderID).Scan(
		&w.ID, &w.AttackerID, &w.DefenderID, &w.Status, &startTime, &endTime, &winnerID, &w.AttackerScore, &w.DefenderScore, &w.CreatedAt,
	)
	if err != nil {
		return nil, err
	}

	if startTime.Valid {
		w.StartTime = &startTime.Time
	}
	if endTime.Valid {
		w.EndTime = &endTime.Time
	}

	return &w, nil
}

// StartWar begins an active guild war
func (r *Repository) StartWar(ctx context.Context, warID uuid.UUID) error {
	query := `
		UPDATE guilds.wars
		SET status = 'active', start_time = NOW()
		WHERE id = $1 AND status = 'pending'
	`
	_, err := r.db.Exec(ctx, query, warID)
	return err
}

// EndWar completes a guild war with a winner
func (r *Repository) EndWar(ctx context.Context, warID uuid.UUID, winnerID *uuid.UUID) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return err
	}
	defer tx.Rollback(ctx)

	// Update war status and winner
	var query string
	var args []interface{}
	if winnerID != nil {
		query = `UPDATE guilds.wars SET status = 'completed', end_time = NOW(), winner_id = $2 WHERE id = $1`
		args = []interface{}{warID, winnerID}
	} else {
		query = `UPDATE guilds.wars SET status = 'completed', end_time = NOW() WHERE id = $1`
		args = []interface{}{warID}
	}

	_, err = tx.Exec(ctx, query, args...)
	if err != nil {
		return err
	}

	// TODO: Award war rewards to winner
	// This would integrate with economy-service

	return tx.Commit(ctx)
}

// GetWarParticipants retrieves participants of a war
func (r *Repository) GetWarParticipants(ctx context.Context, warID uuid.UUID) ([]*WarParticipant, error) {
	query := `
		SELECT id, war_id, user_id, guild_id, score, kills, deaths, joined_at
		FROM guilds.war_participants
		WHERE war_id = $1
		ORDER BY score DESC
	`

	rows, err := r.db.Query(ctx, query, warID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var participants []*WarParticipant
	for rows.Next() {
		var p WarParticipant
		err := rows.Scan(&p.ID, &p.WarID, &p.UserID, &p.GuildID, &p.Score, &p.Kills, &p.Deaths, &p.JoinedAt)
		if err != nil {
			return nil, err
		}
		participants = append(participants, &p)
	}

	return participants, rows.Err()
}

// JoinWar adds a participant to a war
func (r *Repository) JoinWar(ctx context.Context, warID, userID, guildID uuid.UUID) error {
	query := `
		INSERT INTO guilds.war_participants (id, war_id, user_id, guild_id, score, kills, deaths, joined_at)
		VALUES (gen_random_uuid(), $1, $2, $3, 0, 0, 0, NOW())
		ON CONFLICT (war_id, user_id) DO NOTHING
	`
	_, err := r.db.Exec(ctx, query, warID, userID, guildID)
	return err
}

// UpdateWarScore updates participant score in a war
func (r *Repository) UpdateWarScore(ctx context.Context, warID, userID uuid.UUID, scoreDelta, killsDelta, deathsDelta int) error {
	query := `
		UPDATE guilds.war_participants
		SET score = score + $4, kills = kills + $5, deaths = deaths + $6
		WHERE war_id = $1 AND user_id = $2
	`
	_, err := r.db.Exec(ctx, query, warID, userID, scoreDelta, killsDelta, deathsDelta)
	return err
}

// GetWarLeaderboard returns war leaderboard
func (r *Repository) GetWarLeaderboard(ctx context.Context, warID uuid.UUID, limit *int) ([]*WarParticipant, error) {
	query := `
		SELECT id, war_id, user_id, guild_id, score, kills, deaths, joined_at
		FROM guilds.war_participants
		WHERE war_id = $1
		ORDER BY score DESC, kills DESC
		LIMIT $2
	`

	if limit == nil {
		defaultLimit := 10
		limit = &defaultLimit
	}

	rows, err := r.db.Query(ctx, query, warID, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var participants []*WarParticipant
	for rows.Next() {
		var p WarParticipant
		err := rows.Scan(&p.ID, &p.WarID, &p.UserID, &p.GuildID, &p.Score, &p.Kills, &p.Deaths, &p.JoinedAt)
		if err != nil {
			return nil, err
		}
		participants = append(participants, &p)
	}

	return participants, rows.Err()
}
