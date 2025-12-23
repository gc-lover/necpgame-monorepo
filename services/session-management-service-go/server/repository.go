// Issue: #140889798
// PERFORMANCE: Database layer with connection pooling and prepared statements
// BACKEND: Database repository for session data

package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// SessionRepository handles database operations for sessions
type SessionRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewSessionRepository creates a new session repository
func NewSessionRepository(db *pgxpool.Pool, logger *zap.Logger) *SessionRepository {
	return &SessionRepository{
		db:     db,
		logger: logger,
	}
}

// HealthCheck performs a health check on the database connection
func (r *SessionRepository) HealthCheck(ctx context.Context) error {
	// PERFORMANCE: Simple health check query
	const query = "SELECT 1"

	conn, err := r.db.Acquire(ctx)
	if err != nil {
		return fmt.Errorf("failed to acquire connection: %w", err)
	}
	defer conn.Release()

	if err := conn.QueryRow(ctx, query).Scan(new(int)); err != nil {
		return fmt.Errorf("health check query failed: %w", err)
	}

	return nil
}

// CreateSession inserts a new session into the database
func (r *SessionRepository) CreateSession(ctx context.Context, session *Session) error {
	const query = `
		INSERT INTO sessions (
			id, player_id, login_time, last_activity_time, status,
			ip_address, client_version, platform
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.Exec(ctx, query,
		session.ID,
		session.PlayerID,
		session.LoginTime,
		session.LastActivityTime,
		session.Status,
		session.IPAddress,
		session.ClientVersion,
		session.Platform,
	)

	if err != nil {
		r.logger.Error("Failed to create session",
			zap.Error(err),
			zap.String("session_id", session.ID.String()),
			zap.String("player_id", session.PlayerID.String()))
		return fmt.Errorf("failed to create session: %w", err)
	}

	r.logger.Debug("Session created in database", zap.String("session_id", session.ID.String()))
	return nil
}

// GetActiveSessions retrieves active sessions with optional filters
func (r *SessionRepository) GetActiveSessions(ctx context.Context, playerID *uuid.UUID, status *SessionStatus) ([]*Session, error) {
	var query string
	var args []interface{}
	argCount := 0

	query = `
		SELECT id, player_id, login_time, last_activity_time, status,
			   ip_address, client_version, platform
		FROM sessions
		WHERE status != 'expired'
	`

	// Add player filter
	if playerID != nil {
		argCount++
		query += fmt.Sprintf(" AND player_id = $%d", argCount)
		args = append(args, *playerID)
	}

	// Add status filter
	if status != nil {
		argCount++
		query += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, *status)
	}

	query += " ORDER BY last_activity_time DESC"

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to query active sessions", zap.Error(err))
		return nil, fmt.Errorf("failed to query active sessions: %w", err)
	}
	defer rows.Close()

	var sessions []*Session
	for rows.Next() {
		session := &Session{}
		err := rows.Scan(
			&session.ID,
			&session.PlayerID,
			&session.LoginTime,
			&session.LastActivityTime,
			&session.Status,
			&session.IPAddress,
			&session.ClientVersion,
			&session.Platform,
		)
		if err != nil {
			r.logger.Error("Failed to scan session row", zap.Error(err))
			return nil, fmt.Errorf("failed to scan session: %w", err)
		}
		sessions = append(sessions, session)
	}

	if err := rows.Err(); err != nil {
		r.logger.Error("Error iterating session rows", zap.Error(err))
		return nil, fmt.Errorf("error iterating sessions: %w", err)
	}

	r.logger.Debug("Retrieved active sessions", zap.Int("count", len(sessions)))
	return sessions, nil
}

// GetSessionByID retrieves a session by its ID
func (r *SessionRepository) GetSessionByID(ctx context.Context, sessionID uuid.UUID) (*Session, error) {
	const query = `
		SELECT id, player_id, login_time, last_activity_time, status,
			   ip_address, client_version, platform
		FROM sessions
		WHERE id = $1
	`

	session := &Session{}
	err := r.db.QueryRow(ctx, query, sessionID).Scan(
		&session.ID,
		&session.PlayerID,
		&session.LoginTime,
		&session.LastActivityTime,
		&session.Status,
		&session.IPAddress,
		&session.ClientVersion,
		&session.Platform,
	)

	if err != nil {
		r.logger.Error("Failed to get session by ID",
			zap.Error(err),
			zap.String("session_id", sessionID.String()))
		return nil, fmt.Errorf("session not found: %w", err)
	}

	return session, nil
}

// GetActiveSessionByPlayerID gets the active session for a specific player
func (r *SessionRepository) GetActiveSessionByPlayerID(ctx context.Context, playerID uuid.UUID) (*Session, error) {
	const query = `
		SELECT id, player_id, login_time, last_activity_time, status,
			   ip_address, client_version, platform
		FROM sessions
		WHERE player_id = $1 AND status = 'active'
		ORDER BY login_time DESC
		LIMIT 1
	`

	session := &Session{}
	err := r.db.QueryRow(ctx, query, playerID).Scan(
		&session.ID,
		&session.PlayerID,
		&session.LoginTime,
		&session.LastActivityTime,
		&session.Status,
		&session.IPAddress,
		&session.ClientVersion,
		&session.Platform,
	)

	if err != nil {
		r.logger.Debug("No active session found for player",
			zap.Error(err),
			zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("session not found")
	}

	return session, nil
}

// UpdateSession updates an existing session
func (r *SessionRepository) UpdateSession(ctx context.Context, session *Session) error {
	const query = `
		UPDATE sessions
		SET last_activity_time = $2, status = $3,
			ip_address = $4, client_version = $5, platform = $6
		WHERE id = $1
	`

	result, err := r.db.Exec(ctx, query,
		session.ID,
		session.LastActivityTime,
		session.Status,
		session.IPAddress,
		session.ClientVersion,
		session.Platform,
	)

	if err != nil {
		r.logger.Error("Failed to update session",
			zap.Error(err),
			zap.String("session_id", session.ID.String()))
		return fmt.Errorf("failed to update session: %w", err)
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("No session updated", zap.String("session_id", session.ID.String()))
		return fmt.Errorf("session not found")
	}

	r.logger.Debug("Session updated in database", zap.String("session_id", session.ID.String()))
	return nil
}

// UpdateSessionStatus updates only the status of a session
func (r *SessionRepository) UpdateSessionStatus(ctx context.Context, sessionID uuid.UUID, status SessionStatus) error {
	const query = `UPDATE sessions SET status = $2 WHERE id = $1`

	result, err := r.db.Exec(ctx, query, sessionID, status)
	if err != nil {
		r.logger.Error("Failed to update session status",
			zap.Error(err),
			zap.String("session_id", sessionID.String()),
			zap.String("status", string(status)))
		return fmt.Errorf("failed to update session status: %w", err)
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("No session status updated", zap.String("session_id", sessionID.String()))
		return fmt.Errorf("session not found")
	}

	r.logger.Debug("Session status updated",
		zap.String("session_id", sessionID.String()),
		zap.String("status", string(status)))
	return nil
}

// DeleteSession deletes a session by its ID
func (r *SessionRepository) DeleteSession(ctx context.Context, sessionID uuid.UUID) error {
	const query = `DELETE FROM sessions WHERE id = $1`

	result, err := r.db.Exec(ctx, query, sessionID)
	if err != nil {
		r.logger.Error("Failed to delete session",
			zap.Error(err),
			zap.String("session_id", sessionID.String()))
		return fmt.Errorf("failed to delete session: %w", err)
	}

	if result.RowsAffected() == 0 {
		r.logger.Warn("No session deleted", zap.String("session_id", sessionID.String()))
		return fmt.Errorf("session not found")
	}

	r.logger.Info("Session deleted from database", zap.String("session_id", sessionID.String()))
	return nil
}

// CleanupExpiredSessions removes sessions that have been expired for too long
func (r *SessionRepository) CleanupExpiredSessions(ctx context.Context, expiredBefore time.Time) (int, error) {
	const query = `
		DELETE FROM sessions
		WHERE status = 'expired' AND last_activity_time < $1
	`

	result, err := r.db.Exec(ctx, query, expiredBefore)
	if err != nil {
		r.logger.Error("Failed to cleanup expired sessions",
			zap.Error(err),
			zap.Time("expired_before", expiredBefore))
		return 0, fmt.Errorf("failed to cleanup expired sessions: %w", err)
	}

	count := int(result.RowsAffected())
	r.logger.Info("Cleaned up expired sessions",
		zap.Int("count", count),
		zap.Time("expired_before", expiredBefore))

	return count, nil
}
