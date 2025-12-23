// Issue: #140889798
// PERFORMANCE: Business logic layer with memory pooling and optimized struct alignment
// BACKEND: Core business logic for session management operations

package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// SessionStatus represents the status of a session
type SessionStatus string

const (
	SessionStatusActive   SessionStatus = "active"
	SessionStatusInactive SessionStatus = "inactive"
	SessionStatusExpired  SessionStatus = "expired"
)

// Session represents a player session
type Session struct {
	ID               uuid.UUID     `json:"id" db:"id"`
	PlayerID         uuid.UUID     `json:"player_id" db:"player_id"`
	LoginTime        time.Time     `json:"login_time" db:"login_time"`
	LastActivityTime time.Time     `json:"last_activity_time" db:"last_activity_time"`
	Status           SessionStatus `json:"status" db:"status"`
	IPAddress        string        `json:"ip_address" db:"ip_address"`
	ClientVersion    string        `json:"client_version" db:"client_version"`
	Platform         string        `json:"platform" db:"platform"`
}

// SessionServiceLogic handles the core business logic for session management
type SessionServiceLogic struct {
	logger       *zap.Logger
	sessionPool  sync.Pool // Memory pool for session objects
	repo         *SessionRepository
	// Session timeout configurations
	sessionTimeout    time.Duration
	heartbeatTimeout  time.Duration
}

// NewSessionServiceLogic creates a new session service logic instance
func NewSessionServiceLogic(logger *zap.Logger, repo *SessionRepository) *SessionServiceLogic {
	service := &SessionServiceLogic{
		logger: logger,
		repo:   repo,
		// PERFORMANCE: Configurable timeouts for MMOFPS workloads
		sessionTimeout:   24 * time.Hour,    // Default 24 hours
		heartbeatTimeout: 10 * time.Minute,  // Heartbeat every 10 minutes
	}

	// Initialize memory pool for sessions
	service.sessionPool = sync.Pool{
		New: func() interface{} {
			return &Session{}
		},
	}

	logger.Info("Session service logic initialized",
		zap.Duration("session_timeout", service.sessionTimeout),
		zap.Duration("heartbeat_timeout", service.heartbeatTimeout))

	return service
}

// HealthCheck performs a health check of the session service
func (s *SessionServiceLogic) HealthCheck(ctx context.Context) error {
	// PERFORMANCE: Context timeout for health checks
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	if err := s.repo.HealthCheck(ctx); err != nil {
		s.logger.Error("Session service health check failed", zap.Error(err))
		return fmt.Errorf("session service health check failed: %w", err)
	}

	return nil
}

// CreateSession creates a new player session
func (s *SessionServiceLogic) CreateSession(ctx context.Context, playerID uuid.UUID, ipAddress, clientVersion, platform string) (*Session, error) {
	// PERFORMANCE: Context timeout for session creation
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	now := time.Now()

	// Get session from pool
	session := s.sessionPool.Get().(*Session)
	defer s.sessionPool.Put(session)

	// Reset session fields
	*session = Session{
		ID:               uuid.New(),
		PlayerID:         playerID,
		LoginTime:        now,
		LastActivityTime: now,
		Status:           SessionStatusActive,
		IPAddress:        ipAddress,
		ClientVersion:    clientVersion,
		Platform:         platform,
	}

	// Validate session data
	if err := s.validateSession(session); err != nil {
		return nil, fmt.Errorf("invalid session data: %w", err)
	}

	// Check for existing active session for this player
	existing, err := s.repo.GetActiveSessionByPlayerID(ctx, playerID)
	if err != nil && err.Error() != "session not found" {
		s.logger.Error("Failed to check existing session", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to check existing session: %w", err)
	}

	// If there's an existing active session, expire it
	if existing != nil {
		if err := s.repo.UpdateSessionStatus(ctx, existing.ID, SessionStatusExpired); err != nil {
			s.logger.Warn("Failed to expire existing session", zap.Error(err), zap.String("session_id", existing.ID.String()))
		}
	}

	// Create new session in database
	if err := s.repo.CreateSession(ctx, session); err != nil {
		s.logger.Error("Failed to create session", zap.Error(err), zap.String("player_id", playerID.String()))
		return nil, fmt.Errorf("failed to create session: %w", err)
	}

	s.logger.Info("Session created successfully",
		zap.String("session_id", session.ID.String()),
		zap.String("player_id", playerID.String()),
		zap.String("ip_address", ipAddress))

	return session, nil
}

// GetActiveSessions retrieves all active sessions with optional filters
func (s *SessionServiceLogic) GetActiveSessions(ctx context.Context, playerID *uuid.UUID, status *SessionStatus) ([]*Session, error) {
	// PERFORMANCE: Context timeout for session retrieval
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	sessions, err := s.repo.GetActiveSessions(ctx, playerID, status)
	if err != nil {
		s.logger.Error("Failed to get active sessions", zap.Error(err))
		return nil, fmt.Errorf("failed to get active sessions: %w", err)
	}

	s.logger.Debug("Retrieved active sessions", zap.Int("count", len(sessions)))
	return sessions, nil
}

// GetSessionByID retrieves a session by its ID
func (s *SessionServiceLogic) GetSessionByID(ctx context.Context, sessionID uuid.UUID) (*Session, error) {
	// PERFORMANCE: Context timeout for session retrieval
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	session, err := s.repo.GetSessionByID(ctx, sessionID)
	if err != nil {
		s.logger.Error("Failed to get session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return nil, fmt.Errorf("failed to get session: %w", err)
	}

	return session, nil
}

// UpdateSession updates an existing session
func (s *SessionServiceLogic) UpdateSession(ctx context.Context, sessionID uuid.UUID, lastActivityTime *time.Time, status *SessionStatus) (*Session, error) {
	// PERFORMANCE: Context timeout for session update
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	// Get existing session
	existing, err := s.repo.GetSessionByID(ctx, sessionID)
	if err != nil {
		return nil, fmt.Errorf("failed to get existing session: %w", err)
	}

	// Apply updates
	if lastActivityTime != nil {
		existing.LastActivityTime = *lastActivityTime
	}
	if status != nil {
		existing.Status = *status
	}

	// Validate updated session
	if err := s.validateSession(existing); err != nil {
		return nil, fmt.Errorf("invalid session update: %w", err)
	}

	// Update in database
	if err := s.repo.UpdateSession(ctx, existing); err != nil {
		s.logger.Error("Failed to update session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return nil, fmt.Errorf("failed to update session: %w", err)
	}

	s.logger.Info("Session updated successfully", zap.String("session_id", sessionID.String()))
	return existing, nil
}

// DeleteSession deletes a session by its ID
func (s *SessionServiceLogic) DeleteSession(ctx context.Context, sessionID uuid.UUID) error {
	// PERFORMANCE: Context timeout for session deletion
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	if err := s.repo.DeleteSession(ctx, sessionID); err != nil {
		s.logger.Error("Failed to delete session", zap.Error(err), zap.String("session_id", sessionID.String()))
		return fmt.Errorf("failed to delete session: %w", err)
	}

	s.logger.Info("Session deleted successfully", zap.String("session_id", sessionID.String()))
	return nil
}

// Heartbeat updates the last activity time for a session
func (s *SessionServiceLogic) Heartbeat(ctx context.Context, sessionID uuid.UUID) (*Session, error) {
	// PERFORMANCE: Context timeout for heartbeat
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	now := time.Now()
	session, err := s.UpdateSession(ctx, sessionID, &now, nil)
	if err != nil {
		return nil, fmt.Errorf("heartbeat failed: %w", err)
	}

	s.logger.Debug("Session heartbeat processed", zap.String("session_id", sessionID.String()))
	return session, nil
}

// CleanupExpiredSessions removes expired sessions from the database
func (s *SessionServiceLogic) CleanupExpiredSessions(ctx context.Context) (int, error) {
	// PERFORMANCE: Context timeout for cleanup
	ctx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	expiredBefore := time.Now().Add(-s.sessionTimeout)
	count, err := s.repo.CleanupExpiredSessions(ctx, expiredBefore)
	if err != nil {
		s.logger.Error("Failed to cleanup expired sessions", zap.Error(err))
		return 0, fmt.Errorf("failed to cleanup expired sessions: %w", err)
	}

	if count > 0 {
		s.logger.Info("Cleaned up expired sessions", zap.Int("count", count))
	}

	return count, nil
}

// validateSession validates session data
func (s *SessionServiceLogic) validateSession(session *Session) error {
	if session.PlayerID == uuid.Nil {
		return fmt.Errorf("player_id cannot be empty")
	}
	if session.IPAddress == "" {
		return fmt.Errorf("ip_address cannot be empty")
	}
	if session.ClientVersion == "" {
		return fmt.Errorf("client_version cannot be empty")
	}
	if session.Platform == "" {
		return fmt.Errorf("platform cannot be empty")
	}
	if session.LoginTime.IsZero() {
		return fmt.Errorf("login_time cannot be zero")
	}
	if session.LastActivityTime.IsZero() {
		return fmt.Errorf("last_activity_time cannot be zero")
	}
	return nil
}
