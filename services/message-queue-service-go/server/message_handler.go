import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// PublishMessage OPTIMIZATION: Issue #2143 - Message publishing and consumption operations
func (s *MessageQueueService) PublishMessage(w http.ResponseWriter, r *http.Request) {
	var req PublishMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode publish message request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	exchange := req.Exchange
	if exchange == "" {
		exchange = "" // Default exchange for direct queue publishing
	}

	messageID := uuid.New().String()
	correlationID := req.CorrelationID
	if correlationID == "" {
		correlationID = messageID
	}

	headers := amqp091.Table{}
	for k, v := range req.Headers {
		headers[k] = v
	}

	priority := uint8(req.Priority)
	if priority > 255 {
		priority = 255
	}

	err := s.rabbitChannel.PublishWithContext(
		r.Context(),
		exchange,      // exchange
		req.QueueName, // routing key
		false,         // mandatory
		false,         // immediate
		amqp091.Publishing{
			ContentType:     req.ContentType,
			ContentEncoding: "",
			DeliveryMode:    2, // persistent
			Priority:        priority,
			CorrelationId:   correlationID,
			ReplyTo:         req.ReplyTo,
			Expiration:      "",
			MessageId:       messageID,
			Timestamp:       time.Now(),
			Type:            "",
			UserId:          req.UserID,
			AppId:           req.AppID,
			Body:            []byte(req.MessageBody),
			Headers:         headers,
		},
	)

	if err != nil {
		s.logger.WithError(err).WithField("queue_name", req.QueueName).Error("failed to publish message")
		s.metrics.ErrorRate.Inc()
		http.Error(w, "Failed to publish message", http.StatusInternalServerError)
		return
	}

	s.metrics.MessagesPublished.Inc()
	s.metrics.MessageSize.Observe(float64(len(req.MessageBody)))

	resp := &PublishMessageResponse{
		MessageID:     messageID,
		CorrelationID: correlationID,
		PublishedAt:   time.Now().Unix(),
		RoutingKey:    req.QueueName,
		Exchange:      exchange,
		QueueName:     req.QueueName,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusAccepted)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"message_id": messageID,
		"queue_name": req.QueueName,
	}).Info("message published successfully")
}

func (s *MessageQueueService) PublishBatchMessages(w http.ResponseWriter, r *http.Request) {
	var req PublishBatchRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode batch publish request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	publishedCount := 0
	failedCount := 0
	var messageIDs []string
	var failedMessages []*FailedMessage

	for i, msg := range req.Messages {
		messageID := uuid.New().String()

		exchange := msg.Exchange
		if exchange == "" {
			exchange = ""
		}

		headers := amqp091.Table{}
		for k, v := range msg.Headers {
			headers[k] = v
		}

		priority := uint8(msg.Priority)
		if priority > 255 {
			priority = 255
		}

		err := s.rabbitChannel.PublishWithContext(
			r.Context(),
			exchange,
			msg.QueueName,
			false,
			false,
			amqp091.Publishing{
				ContentType:   msg.ContentType,
				DeliveryMode:  2,
				Priority:      priority,
				CorrelationId: msg.CorrelationID,
				ReplyTo:       msg.ReplyTo,
				MessageId:     messageID,
				Timestamp:     time.Now(),
				UserId:        msg.UserID,
				AppId:         msg.AppID,
				Body:          []byte(msg.MessageBody),
				Headers:       headers,
			},
		)

		if err != nil {
			failedCount++
			failedMessages = append(failedMessages, &FailedMessage{
				Index:   i,
				Error:   err.Error(),
				Message: &msg,
			})
			s.metrics.ErrorRate.Inc()
		} else {
			publishedCount++
			messageIDs = append(messageIDs, messageID)
			s.metrics.MessagesPublished.Inc()
			s.metrics.MessageSize.Observe(float64(len(msg.MessageBody)))
		}
	}

	resp := &PublishBatchResponse{
		PublishedCount: publishedCount,
		FailedCount:    failedCount,
		MessageIDs:     messageIDs,
		FailedMessages: failedMessages,
		PublishedAt:    time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusAccepted)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"published_count": publishedCount,
		"failed_count":    failedCount,
	}).Info("batch messages published")
}

func (s *MessageQueueService) ConsumeMessages(w http.ResponseWriter, r *http.Request) {
	var req ConsumeMessagesRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode consume messages request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Get messages from queue
	msgs, err := s.rabbitChannel.Consume(
		req.QueueName,   // queue
		req.ConsumerTag, // consumer
		req.AutoAck,     // auto-ack
		req.Exclusive,   // exclusive
		false,           // no-local
		req.NoWait,      // no-wait
		nil,             // args
	)
	if err != nil {
		s.logger.WithError(err).WithField("queue_name", req.QueueName).Error("failed to consume messages")
		http.Error(w, "Failed to consume messages", http.StatusInternalServerError)
		return
	}

	var messages []*ConsumedMessage
	deliveryTag := uint64(0)

	// Collect messages
	for i := 0; i < req.MaxMessages && len(messages) < req.MaxMessages; i++ {
		select {
		case msg := <-msgs:
			deliveryTag = msg.DeliveryTag

			headers := make(map[string]string)
			for k, v := range msg.Headers {
				if strVal, ok := v.(string); ok {
					headers[k] = strVal
				}
			}

			message := &ConsumedMessage{
				MessageID:     msg.MessageId,
				CorrelationID: msg.CorrelationId,
				Body:          string(msg.Body),
				ContentType:   msg.ContentType,
				Headers:       headers,
				RoutingKey:    msg.RoutingKey,
				Exchange:      msg.Exchange,
				Priority:      int(msg.Priority),
				Timestamp:     msg.Timestamp.Unix(),
				UserID:        msg.UserId,
				AppID:         msg.AppId,
				DeliveryMode:  int(msg.DeliveryMode),
			}
			messages = append(messages, message)

			s.metrics.MessagesConsumed.Inc()

			if !req.AutoAck {
				msg.Ack(false) // Acknowledge message
			}

		case <-time.After(s.config.ConsumerTimeout):
			// Timeout - return what we have
			break
		}
	}

	if len(messages) == 0 {
		w.WriteHeader(http.StatusNoContent)
		return
	}

	resp := &ConsumeMessagesResponse{
		Messages:    messages,
		DeliveryTag: int64(deliveryTag),
		ConsumerTag: req.ConsumerTag,
		Redelivered: false,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"queue_name":     req.QueueName,
		"messages_count": len(messages),
	}).Info("messages consumed successfully")
}
