package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2143 - Queue management with RabbitMQ operations
func (s *MessageQueueService) CreateQueue(w http.ResponseWriter, r *http.Request) {
	var req CreateQueueRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create queue request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	args := amqp091.Table{}
	if req.MessageTTL > 0 {
		args["x-message-ttl"] = req.MessageTTL
	}
	if req.MaxLength > 0 {
		args["x-max-length"] = req.MaxLength
	}
	if req.MaxLengthBytes > 0 {
		args["x-max-length-bytes"] = req.MaxLengthBytes
	}
	if req.DeadLetterExchange != "" {
		args["x-dead-letter-exchange"] = req.DeadLetterExchange
	}
	if req.DeadLetterRoutingKey != "" {
		args["x-dead-letter-routing-key"] = req.DeadLetterRoutingKey
	}

	_, err := s.rabbitChannel.QueueDeclare(
		req.Name,           // name
		req.Durable,        // durable
		req.AutoDelete,     // delete when unused
		false,              // exclusive
		false,              // no-wait
		args,               // arguments
	)
	if err != nil {
		s.logger.WithError(err).WithField("queue_name", req.Name).Error("failed to declare queue")
		http.Error(w, "Failed to create queue", http.StatusInternalServerError)
		return
	}

	s.metrics.ActiveQueues.Inc()

	resp := &CreateQueueResponse{
		QueueName:     req.Name,
		QueueType:     req.Type,
		CreatedAt:     time.Now().Unix(),
		MessageCount:  0,
		ConsumerCount: 0,
		Settings: &QueueSettings{
			MaxLength:            req.MaxLength,
			MaxLengthBytes:       int64(req.MaxLengthBytes),
			MessageTTL:           req.MessageTTL,
			DeadLetterExchange:   req.DeadLetterExchange,
			DeadLetterRoutingKey: req.DeadLetterRoutingKey,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("queue_name", req.Name).Info("queue created successfully")
}

func (s *MessageQueueService) ListQueues(w http.ResponseWriter, r *http.Request) {
	// Get all queues from RabbitMQ management API
	// For now, return empty list (would need RabbitMQ management plugin)
	queues := []*QueueInfo{}

	resp := &ListQueuesResponse{
		Queues:     queues,
		TotalCount: len(queues),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *MessageQueueService) GetQueue(w http.ResponseWriter, r *http.Request) {
	queueName := chi.URLParam(r, "queueName")

	// Get queue info from RabbitMQ management API
	// For now, return mock data
	queue := &QueueDetails{
		Name:          queueName,
		Type:          "classic",
		Durable:       true,
		AutoDelete:    false,
		MessageCount:  0,
		ConsumerCount: 0,
		MemoryUsage:   0,
		DiskUsage:     0,
		PublishRate:   0.0,
		DeliverRate:   0.0,
		AckRate:       0.0,
		NackRate:      0.0,
		CreatedAt:     time.Now().Unix(),
		Settings:      &QueueSettings{},
		Bindings:      []*QueueBinding{},
	}

	resp := &GetQueueResponse{
		Queue: queue,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *MessageQueueService) UpdateQueue(w http.ResponseWriter, r *http.Request) {
	queueName := chi.URLParam(r, "queueName")

	var req UpdateQueueRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode update queue request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Update queue arguments (RabbitMQ doesn't support updating existing queues)
	// This would require deleting and recreating the queue

	resp := &UpdateQueueResponse{
		QueueName:     queueName,
		UpdatedFields: []string{}, // Would be populated with actual updated fields
		UpdatedAt:     time.Now().Unix(),
		Settings: &QueueSettings{
			MaxLength:            req.MaxLength,
			MaxLengthBytes:       int64(req.MaxLengthBytes),
			MessageTTL:           req.MessageTTL,
			DeadLetterExchange:   req.DeadLetterExchange,
			DeadLetterRoutingKey: req.DeadLetterRoutingKey,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("queue_name", queueName).Info("queue update requested (implementation pending)")
}

func (s *MessageQueueService) DeleteQueue(w http.ResponseWriter, r *http.Request) {
	queueName := chi.URLParam(r, "queueName")

	_, err := s.rabbitChannel.QueueDelete(
		queueName, // queue name
		false,     // if-unused
		false,     // if-empty
		false,     // no-wait
	)
	if err != nil {
		s.logger.WithError(err).WithField("queue_name", queueName).Error("failed to delete queue")
		http.Error(w, "Failed to delete queue", http.StatusInternalServerError)
		return
	}

	s.metrics.ActiveQueues.Dec()

	w.WriteHeader(http.StatusNoContent)

	s.logger.WithField("queue_name", queueName).Info("queue deleted successfully")
}
