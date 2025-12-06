// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// CRITICAL for combat sessions - prevents session manipulation, invalid participants
package server

import (
	"errors"
	"sync"
	"sync/atomic"
	"time"

	"github.com/sirupsen/logrus"
)

var (
	ErrTooManyParticipants = errors.New("too many participants (max 200)")
	ErrInvalidSession      = errors.New("invalid session state")
	ErrSessionManipulation = errors.New("suspicious session manipulation")
)

// SessionValidator validates combat session creation and management
type SessionValidator struct {
	sessionStats sync.Map // sessionID -> *SessionStats
}

type SessionStats struct {
	CreatedAt    time.Time
	ParticipantCount atomic.Int32
	ActionCount  atomic.Int64
	Flags        atomic.Int32
}

// NewSessionValidator creates a new session validator
func NewSessionValidator() *SessionValidator {
	return &SessionValidator{}
}

// ValidateSessionCreation validates session creation request
func (sv *SessionValidator) ValidateSessionCreation(participantCount int, sessionType string) error {
	// 1. Participant count check (max 200 for massive battles)
	if participantCount > 200 {
		logrus.WithFields(logrus.Fields{
			"participant_count": participantCount,
			"session_type":      sessionType,
		}).Warn("Too many participants (potential abuse)")
		return ErrTooManyParticipants
	}

	// 2. Minimum participants check
	if participantCount < 1 {
		return errors.New("at least 1 participant required")
	}

	return nil
}

// ValidateSessionAction validates actions within a session
func (sv *SessionValidator) ValidateSessionAction(sessionID string, actionType string) error {
	stats := sv.getOrCreateStats(sessionID)

	// Rate check: max 100 actions/sec per session
	actionCount := stats.ActionCount.Add(1)
	
	// Reset counter every second (simplified - in production use sliding window)
	if time.Since(stats.CreatedAt) > 1*time.Second {
		stats.ActionCount.Store(0)
		stats.CreatedAt = time.Now()
	}

	if actionCount > 100 {
		logrus.WithFields(logrus.Fields{
			"session_id":  sessionID,
			"action_type": actionType,
			"action_count": actionCount,
		}).Warn("Session action rate too high (potential abuse)")
		return ErrSessionManipulation
	}

	return nil
}

// getOrCreateStats gets or creates session stats
func (sv *SessionValidator) getOrCreateStats(sessionID string) *SessionStats {
	statsInterface, _ := sv.sessionStats.LoadOrStore(sessionID, &SessionStats{
		CreatedAt: time.Now(),
	})
	return statsInterface.(*SessionStats)
}

