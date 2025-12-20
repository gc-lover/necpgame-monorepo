package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1998 - Session management with concurrent access
func (s *AuthService) GetUserSessions(w http.ResponseWriter, r *http.Request) {
	// Get user from context
	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	var userSessions []*SessionInfo
	totalSessions := 0
	activeSessions := 0

	s.sessions.Range(func(key, value interface{}) bool {
		session := value.(*Session)
		if session.UserID == userID {
			totalSessions++
			if session.IsActive {
				activeSessions++
			}

			sessionInfo := &SessionInfo{
				SessionID:   session.SessionID,
				DeviceInfo:  session.DeviceInfo,
				IPAddress:   session.IPAddress,
				CreatedAt:   session.CreatedAt.Unix(),
				LastActivity: session.LastActivity.Unix(),
				ExpiresAt:   session.ExpiresAt.Unix(),
				IsCurrent:   false, // TODO: Check if this is current session
			}
			userSessions = append(userSessions, sessionInfo)
		}
		return true
	})

	resp := &GetSessionsResponse{
		Sessions:       userSessions,
		TotalSessions:  totalSessions,
		ActiveSessions: activeSessions,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// OPTIMIZATION: Issue #1998 - Session invalidation with cleanup
func (s *AuthService) InvalidateAllSessions(w http.ResponseWriter, r *http.Request) {
	// Get user from context
	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	invalidated := 0
	s.sessions.Range(func(key, value interface{}) bool {
		session := value.(*Session)
		if session.UserID == userID {
			session.IsActive = false
			s.sessions.Delete(key)
			invalidated++
		}
		return true
	})

	resp := &InvalidateSessionsResponse{
		Message:             "All sessions invalidated",
		SessionsInvalidated: invalidated,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"user_id":             userID,
		"sessions_invalidated": invalidated,
	}).Info("all user sessions invalidated")
}

// OPTIMIZATION: Issue #1998 - Individual session invalidation
func (s *AuthService) InvalidateSession(w http.ResponseWriter, r *http.Request) {
	sessionID := chi.URLParam(r, "sessionId")

	// Get user from context
	userID := r.Header.Get("X-User-ID")
	if userID == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	session, exists := s.sessions.Load(sessionID)
	if !exists {
		http.Error(w, "Session not found", http.StatusNotFound)
		return
	}

	if session.(*Session).UserID != userID {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	s.sessions.Delete(sessionID)

	resp := &InvalidateSessionResponse{
		Message:   "Session invalidated",
		SessionID: sessionID,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("session_id", sessionID).Info("session invalidated")
}
