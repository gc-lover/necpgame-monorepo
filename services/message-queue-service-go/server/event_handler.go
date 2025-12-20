package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2143 - Event publishing and subscription patterns
func (s *MessageQueueService) PublishEvent(w http.ResponseWriter, r *http.Request) {
	var req PublishEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode publish event request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	exchange := req.Exchange
	if exchange == "" {
		exchange = "events"
	}

	eventID := uuid.New().String()

	headers := amqp091.Table{}
	if req.Metadata != nil {
		for k, v := range req.Metadata {
			headers[k] = v
		}
	}
	for k, v := range req.Headers {
		headers[k] = v
	}

	eventData, _ := json.Marshal(req.EventData)

	err := s.rabbitChannel.PublishWithContext(
		r.Context(),
		exchange,
		req.RoutingKey,
		false,
		false,
		amqp091.Publishing{
			ContentType:   "application/json",
			DeliveryMode:  2,
			CorrelationId: req.Metadata["correlation_id"].(string),
			MessageId:     eventID,
			Timestamp:     time.Now(),
			Type:          req.EventType,
			Body:          eventData,
			Headers:       headers,
		},
	)

	if err != nil {
		s.logger.WithError(err).WithField("event_type", req.EventType).Error("failed to publish event")
		s.metrics.ErrorRate.Inc()
		http.Error(w, "Failed to publish event", http.StatusInternalServerError)
		return
	}

	s.metrics.MessagesPublished.Inc()

	resp := &PublishEventResponse{
		EventID:         eventID,
		EventType:       req.EventType,
		Exchange:        exchange,
		RoutingKey:      req.RoutingKey,
		PublishedAt:     time.Now().Unix(),
		SubscriberCount: 0, // Would need to check actual subscribers
		Metadata:        req.Metadata,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusAccepted)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"event_id":   eventID,
		"event_type": req.EventType,
	}).Info("event published successfully")
}
