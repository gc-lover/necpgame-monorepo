package server

import (
	"encoding/json"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"

	"necpgame/services/auth-service-go/pkg/api"
)

// OPTIMIZATION: Issue #1998 - Session management with concurrent access
func (s *AuthService) GetUserSessions(w http.ResponseWriter, r *http.Request) {
	// Get user from context
	userIDStr := r.Header.Get("X-User-ID")
	if userIDStr == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		http.Error(w, "Invalid user ID", http.StatusBadRequest)
		return
	}

	var userSessions []*api.SessionInfo
	totalSessions := 0
	activeSessions := 0

	s.sessions.Range(func(key, value interface{}) bool {
		session := value.(*Session)
		sessionUserID, err := uuid.Parse(session.UserID)
		if err != nil {
			return true
		}
		if sessionUserID == userID {
			totalSessions++
			if session.IsActive {
				activeSessions++
			}

			sessionInfo := &api.SessionInfo{
				SessionID:     session.SessionID,
				IPAddress:     session.IPAddress,
				CreatedAt:     session.CreatedAt.Unix(),
				LastActivity:  session.LastActivity.Unix(),
				ExpiresAt:     session.ExpiresAt.Unix(),
				IsCurrent:     false, // TODO: Check if this is current session
			}
			userSessions = append(userSessions, sessionInfo)
		}
		return true
	})

	resp := &api.GetSessionsResponse{
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
	userIDStr := r.Header.Get("X-User-ID")
	if userIDStr == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		http.Error(w, "Invalid user ID", http.StatusBadRequest)
		return
	}

	invalidated := 0
	s.sessions.Range(func(key, value interface{}) bool {
		session := value.(*Session)
		sessionUserID, err := uuid.Parse(session.UserID)
		if err != nil {
			return true
		}
		if sessionUserID == userID {
			session.IsActive = false
			s.sessions.Delete(key)
			invalidated++
		}
		return true
	})

	resp := &api.InvalidateSessionsResponse{
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
	userIDStr := r.Header.Get("X-User-ID")
	if userIDStr == "" {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		http.Error(w, "Invalid user ID", http.StatusBadRequest)
		return
	}

	session, exists := s.sessions.Load(sessionID)
	if !exists {
		http.Error(w, "Session not found", http.StatusNotFound)
		return
	}

	sessionUserID, err := uuid.Parse(session.(*Session).UserID)
	if err != nil || sessionUserID != userID {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}

	s.sessions.Delete(sessionID)

	resp := &api.InvalidateSessionResponse{
		Message:   "Session invalidated",
		SessionID: sessionID,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("session_id", sessionID).Info("session invalidated")
}
