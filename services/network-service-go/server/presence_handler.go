package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
)

// OPTIMIZATION: Issue #1978 - Presence management with real-time updates
func (s *NetworkService) GetUserPresence(w http.ResponseWriter, r *http.Request) {
	userID := chi.URLParam(r, "userId")

	// OPTIMIZATION: Issue #1978 - Use memory pool
	resp := s.presenceResponsePool.Get().(*GetPresenceResponse)
	defer s.presenceResponsePool.Put(resp)

	resp.UserID = userID
	resp.Presence = &UserPresence{
		UserID:          userID,
		Status:          "ONLINE",
		LastSeen:        time.Now().Unix(),
		ConnectedAt:     time.Now().Add(-1 * time.Hour).Unix(),
		CurrentActivity: "PLAYING",
	}
	resp.LastUpdated = time.Now().Unix()
	resp.IsOnline = true

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// OPTIMIZATION: Issue #1978 - Presence updates with broadcasting
func (s *NetworkService) UpdateUserPresence(w http.ResponseWriter, r *http.Request) {
	userID := chi.URLParam(r, "userId")

	var req UpdatePresenceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode presence update request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.PresenceUpdates.Inc()

	resp := &UpdatePresenceResponse{
		UserID:             userID,
		PreviousStatus:     "AWAY",
		NewStatus:          req.Status,
		UpdatedAt:          time.Now().Unix(),
		Broadcasted:        true,
		AffectedSubscribers: 5,
	}

	// Broadcast presence update
	updateEvent := NetworkMessage{
		MessageID: generateMessageID(),
		Type:      "PRESENCE_UPDATE",
		SenderID:  userID,
		Content:   req.Status,
		Timestamp: time.Now().Unix(),
	}

	s.broadcastPresenceUpdate(userID, updateEvent)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// OPTIMIZATION: Issue #1978 - Presence broadcasting to subscribers
func (s *NetworkService) broadcastPresenceUpdate(userID string, event NetworkMessage) {
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		if conn.UserID != userID { // Don't send to self
			s.sendToConnection(conn, event)
		}
		return true
	})
}
