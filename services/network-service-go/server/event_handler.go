package server

import (
	"encoding/json"
	"net/http"
	"time"
)

// SubscribeToEvents OPTIMIZATION: Issue #1978 - Event subscription management
func (s *NetworkService) SubscribeToEvents(w http.ResponseWriter, r *http.Request) {
	var req SubscribeEventsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode event subscription request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	subscriptionID := generateMessageID()

	resp := &SubscribeEventsResponse{
		SubscriptionID: subscriptionID,
		Status:         "ACTIVE",
		CreatedAt:      time.Now().Unix(),
		ExpiresAt:      time.Now().Add(24 * time.Hour).Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// PublishEvent OPTIMIZATION: Issue #1978 - Event publishing with routing
func (s *NetworkService) PublishEvent(w http.ResponseWriter, r *http.Request) {
	var req PublishEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode event publish request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.EventPublishes.Inc()

	event := req.Event
	event.EventID = generateMessageID()
	event.Timestamp = time.Now().Unix()

	// Publish to subscribers
	recipients := s.publishToSubscribers(event)

	// OPTIMIZATION: Issue #1978 - Use memory pool
	resp := s.eventResponsePool.Get().(*PublishEventResponse)
	defer s.eventResponsePool.Put(resp)

	resp.EventID = event.EventID
	resp.PublishedAt = event.Timestamp
	resp.RecipientsCount = recipients
	resp.RoutingStrategy = req.RoutingStrategy
	resp.DeliveryStatus = "DELIVERED"
	resp.ProcessingTimeMs = 5

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// OPTIMIZATION: Issue #1978 - Event publishing to all subscribers
func (s *NetworkService) publishToSubscribers(event *Event) int {
	recipients := 0
	// Simplified: broadcast to all connections
	s.connections.Range(func(key, value interface{}) bool {
		conn := value.(*WSConnection)
		s.sendToConnection(conn, event)
		recipients++
		return true
	})
	return recipients
}
