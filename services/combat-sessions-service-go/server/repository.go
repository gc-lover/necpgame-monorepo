// Issue: #130

package server

import (
	"context"
	"database/sql"

	_ "github.com/lib/pq"
	"github.com/combat-sessions-service-go/pkg/api"
)

// Repository interface for database operations
type Repository interface {
	CreateSession(ctx context.Context, session *CombatSession) error
	GetSession(ctx context.Context, sessionID string) (*CombatSession, error)
	UpdateSession(ctx context.Context, session *CombatSession) error
	ListSessions(ctx context.Context, params api.ListCombatSessionsParams) ([]*CombatSession, int, error)
	
	GetParticipants(ctx context.Context, sessionID string) ([]*CombatParticipant, error)
	CreateLog(ctx context.Context, log *CombatLog) error
	GetLogs(ctx context.Context, sessionID string, params api.GetCombatLogsParams) ([]*CombatLog, int, error)
	
	Close() error
}

// PostgresRepository implements Repository
type PostgresRepository struct {
	db *sql.DB
}

// NewPostgresRepository creates new repository
func NewPostgresRepository(dsn string) (*PostgresRepository, error) {
	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, err
	}

	if err := db.Ping(); err != nil {
		return nil, err
	}

	return &PostgresRepository{db: db}, nil
}

// CreateSession creates new combat session
func (r *PostgresRepository) CreateSession(ctx context.Context, session *CombatSession) error {
	query := `
		INSERT INTO combat_sessions (id, session_type, zone_id, status, max_participants, settings, created_at, expires_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`
	_, err := r.db.ExecContext(ctx, query,
		session.ID, session.SessionType, session.ZoneID, session.Status,
		session.MaxParticipants, session.Settings, session.CreatedAt, session.ExpiresAt,
	)
	return err
}

// GetSession gets session by ID
func (r *PostgresRepository) GetSession(ctx context.Context, sessionID string) (*CombatSession, error) {
	query := `
		SELECT id, session_type, zone_id, status, max_participants, created_at, started_at, ended_at, winner_team
		FROM combat_sessions
		WHERE id = $1
	`
	session := &CombatSession{}
	err := r.db.QueryRowContext(ctx, query, sessionID).Scan(
		&session.ID, &session.SessionType, &session.ZoneID, &session.Status,
		&session.MaxParticipants, &session.CreatedAt, &session.StartedAt,
		&session.EndedAt, &session.WinnerTeam,
	)
	if err == sql.ErrNoRows {
		return nil, ErrSessionNotFound
	}
	return session, err
}

// UpdateSession updates session
func (r *PostgresRepository) UpdateSession(ctx context.Context, session *CombatSession) error {
	query := `
		UPDATE combat_sessions
		SET status = $2, ended_at = $3, winner_team = $4
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query, session.ID, session.Status, session.EndedAt, session.WinnerTeam)
	return err
}

// ListSessions lists sessions with filtering
func (r *PostgresRepository) ListSessions(ctx context.Context, params api.ListCombatSessionsParams) ([]*CombatSession, int, error) {
	// TODO: implement filtering and pagination
	return []*CombatSession{}, 0, nil
}

// GetParticipants gets all participants
func (r *PostgresRepository) GetParticipants(ctx context.Context, sessionID string) ([]*CombatParticipant, error) {
	query := `
		SELECT player_id, character_id, team, role, health, max_health, status, damage_dealt, kills, deaths
		FROM combat_participants
		WHERE session_id = $1
	`
	rows, err := r.db.QueryContext(ctx, query, sessionID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var participants []*CombatParticipant
	for rows.Next() {
		p := &CombatParticipant{}
		err := rows.Scan(&p.PlayerID, &p.CharacterID, &p.Team, &p.Role,
			&p.Health, &p.MaxHealth, &p.Status, &p.DamageDealt, &p.Kills, &p.Deaths)
		if err != nil {
			return nil, err
		}
		participants = append(participants, p)
	}

	return participants, nil
}

// CreateLog creates combat log entry
func (r *PostgresRepository) CreateLog(ctx context.Context, log *CombatLog) error {
	query := `
		INSERT INTO combat_logs (session_id, event_type, actor_id, target_id, timestamp, sequence_number)
		VALUES ($1, $2, $3, $4, $5, $6)
	`
	_, err := r.db.ExecContext(ctx, query,
		log.SessionID, log.EventType, log.ActorID, log.TargetID, log.Timestamp, log.SequenceNumber,
	)
	return err
}

// GetLogs gets combat logs
func (r *PostgresRepository) GetLogs(ctx context.Context, sessionID string, params api.GetCombatLogsParams) ([]*CombatLog, int, error) {
	// TODO: implement pagination and filtering
	return []*CombatLog{}, 0, nil
}

// Close closes database connection
func (r *PostgresRepository) Close() error {
	return r.db.Close()
}

