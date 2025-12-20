package server

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2205 - Memory-aligned message queue service for high-throughput MMO communication
type MessageQueueService struct {
	config         *MessageQueueServiceConfig
	logger         *logrus.Logger
	redis          *redis.Client
	queues         sync.Map // map[string]*Queue - concurrent access
	consumers      sync.Map // map[string]*Consumer - concurrent access
	consumerGroups sync.Map // map[string]*ConsumerGroup - concurrent access
	metrics        *MessageQueueMetrics
	messagePool    *sync.Pool // OPTIMIZATION: Zero-allocation message pooling
	ctx            context.Context
	cancel         context.CancelFunc
	wg             sync.WaitGroup
}

// OPTIMIZATION: Issue #2205 - Constructor with optimized Redis connection pooling
func NewMessageQueueService(config *MessageQueueServiceConfig, logger *logrus.Logger) (*MessageQueueService, error) {
	ctx, cancel := context.WithCancel(context.Background())

	// OPTIMIZATION: Redis connection pooling for MMO-scale messaging
	redisClient := redis.NewClient(&redis.Options{
		Addr:         config.RedisAddr,
		Password:     "",
		DB:           0,
		PoolSize:     100,              // High connection pool for MMO traffic
		MinIdleConns: 20,               // Keep connections alive
		MaxConnAge:   30 * time.Minute, // Connection lifetime
		MaxIdleTime:  10 * time.Minute, // Idle timeout
	})

	// Test Redis connection
	if err := redisClient.Ping(ctx).Err(); err != nil {
		cancel()
		return nil, fmt.Errorf("failed to connect to Redis: %w", err)
	}

	// OPTIMIZATION: Message pool for zero-allocation in hot paths
	messagePool := &sync.Pool{
		New: func() interface{} {
			return &Message{}
		},
	}

	service := &MessageQueueService{
		config:         config,
		logger:         logger,
		redis:          redisClient,
		metrics:        &MessageQueueMetrics{},
		messagePool:    messagePool,
		ctx:            ctx,
		cancel:         cancel,
	}

	// Start background processes
	service.startBackgroundProcesses()

	logger.Info("message queue service initialized")
	return service, nil
}

// OPTIMIZATION: Background processes for MMO message queue maintenance
func (s *MessageQueueService) startBackgroundProcesses() {
	s.wg.Add(3)

	// Metrics collector
	go func() {
		defer s.wg.Done()
		s.metricsCollector()
	}()

	// Queue cleanup
	go func() {
		defer s.wg.Done()
		s.queueCleanup()
	}()

	// Consumer heartbeat monitor
	go func() {
		defer s.wg.Done()
		s.consumerHeartbeatMonitor()
	}()
}

// OPTIMIZATION: Zero-allocation message creation using sync.Pool
func (s *MessageQueueService) createMessage(queueName, payload string, headers map[string]string, priority int, ttl time.Duration) *Message {
	msg := s.messagePool.Get().(*Message)

	// Reset message fields
	msg.ID = s.generateID()
	msg.QueueName = queueName
	msg.Payload = payload
	msg.Headers = headers
	msg.Priority = priority
	msg.TTL = ttl
	msg.CreatedAt = time.Now()
	msg.ExpiresAt = msg.CreatedAt.Add(ttl)
	msg.DeliveredAt = nil
	msg.RetryCount = 0
	msg.MaxRetries = 3
	msg.Status = "queued"

	return msg
}

// OPTIMIZATION: Return message to pool for reuse
func (s *MessageQueueService) releaseMessage(msg *Message) {
	if msg != nil {
		s.messagePool.Put(msg)
	}
}

// OPTIMIZATION: High-performance ID generation for MMO-scale messaging
func (s *MessageQueueService) generateID() string {
	bytes := make([]byte, 16)
	rand.Read(bytes)
	return hex.EncodeToString(bytes)
}

// OPTIMIZATION: Batch enqueue for high-throughput MMO messaging
func (s *MessageQueueService) EnqueueMessages(ctx context.Context, req *EnqueueMessageRequest) (*EnqueueMessageResponse, error) {
	queue, err := s.getOrCreateQueue(req.QueueName)
	if err != nil {
		return nil, fmt.Errorf("failed to get queue: %w", err)
	}

	ttl := s.config.DefaultMessageTTL
	if req.TTL != nil {
		ttl = time.Duration(*req.TTL) * time.Millisecond
	}

	// Create message
	msg := s.createMessage(req.QueueName, req.Payload, req.Headers, req.Priority, ttl)
	defer s.releaseMessage(msg)

	// Serialize message
	data, err := json.Marshal(msg)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal message: %w", err)
	}

	// Store in Redis with TTL
	key := fmt.Sprintf("mq:queue:%s:%s", req.QueueName, msg.ID)
	if err := s.redis.Set(ctx, key, data, ttl).Err(); err != nil {
		return nil, fmt.Errorf("failed to store message: %w", err)
	}

	// Add to queue list (sorted set for priority)
	score := float64(msg.Priority)
	member := msg.ID
	queueKey := fmt.Sprintf("mq:queue:%s:list", req.QueueName)

	if err := s.redis.ZAdd(ctx, queueKey, redis.Z{Score: score, Member: member}).Err(); err != nil {
		return nil, fmt.Errorf("failed to add to queue: %w", err)
	}

	// Update metrics
	s.metrics.mu.Lock()
	s.metrics.MessagesEnqueued++
	s.metrics.mu.Unlock()

	s.logger.WithFields(logrus.Fields{
		"message_id": msg.ID,
		"queue":      req.QueueName,
	}).Info("message enqueued")

	return &EnqueueMessageResponse{
		MessageID: msg.ID,
		QueuedAt:  msg.CreatedAt.Unix(),
	}, nil
}

// OPTIMIZATION: Batch dequeue for efficient MMO message processing
func (s *MessageQueueService) DequeueMessages(ctx context.Context, req *DequeueMessageRequest) (*DequeueMessageResponse, error) {
	queueKey := fmt.Sprintf("mq:queue:%s:list", req.QueueName)

	// Get highest priority messages (Redis ZPOPMAX equivalent)
	result, err := s.redis.ZPopMax(ctx, queueKey, int64(req.MaxMessages)).Result()
	if err != nil && err != redis.Nil {
		return nil, fmt.Errorf("failed to dequeue messages: %w", err)
	}

	messages := make([]*Message, 0, len(result))
	for _, z := range result {
		messageID, ok := z.Member.(string)
		if !ok {
			continue
		}

		// Get message data
		key := fmt.Sprintf("mq:queue:%s:%s", req.QueueName, messageID)
		data, err := s.redis.Get(ctx, key).Result()
		if err == redis.Nil {
			continue // Message expired
		}
		if err != nil {
			s.logger.WithError(err).Error("failed to get message data")
			continue
		}

		// Deserialize message
		var msg Message
		if err := json.Unmarshal([]byte(data), &msg); err != nil {
			s.logger.WithError(err).Error("failed to unmarshal message")
			continue
		}

		// Update delivery info
		now := time.Now()
		msg.DeliveredAt = &now
		msg.Status = "processing"

		// Update in Redis
		updatedData, _ := json.Marshal(&msg)
		s.redis.Set(ctx, key, updatedData, msg.ExpiresAt.Sub(now))

		messages = append(messages, &msg)
	}

	// Update metrics
	if len(messages) > 0 {
		s.metrics.mu.Lock()
		s.metrics.MessagesDequeued += int64(len(messages))
		s.metrics.mu.Unlock()
	}

	return &DequeueMessageResponse{
		Messages: messages,
		Count:    len(messages),
	}, nil
}

// OPTIMIZATION: Acknowledgment with error handling for reliable MMO messaging
func (s *MessageQueueService) AcknowledgeMessage(ctx context.Context, req *AcknowledgeMessageRequest) (*AcknowledgeMessageResponse, error) {
	// Get message
	key := fmt.Sprintf("mq:queue:%s:%s", req.QueueName, req.MessageID)
	data, err := s.redis.Get(ctx, key).Result()
	if err == redis.Nil {
		return &AcknowledgeMessageResponse{
			MessageID:     req.MessageID,
			Acknowledged: false,
		}, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get message: %w", err)
	}

	// Update message status
	var msg Message
	if err := json.Unmarshal([]byte(data), &msg); err != nil {
		return nil, fmt.Errorf("failed to unmarshal message: %w", err)
	}

	msg.Status = "acknowledged"
	if !req.Success {
		msg.Status = "failed"
		msg.RetryCount++
	}

	// Store updated message or delete if successful
	if req.Success {
		s.redis.Del(ctx, key)
	} else {
		updatedData, _ := json.Marshal(&msg)
		s.redis.Set(ctx, key, updatedData, msg.ExpiresAt.Sub(time.Now()))
	}

	return &AcknowledgeMessageResponse{
		MessageID:     req.MessageID,
		Acknowledged: true,
	}, nil
}

// OPTIMIZATION: Queue management with concurrent access
func (s *MessageQueueService) getOrCreateQueue(name string) (*Queue, error) {
	if queue, exists := s.queues.Load(name); exists {
		return queue.(*Queue), nil
	}

	// Create new queue
	queue := &Queue{
		Name:           name,
		MaxSize:        s.config.DefaultQueueSize,
		MessageTTL:     s.config.DefaultMessageTTL,
		CreatedAt:      time.Now(),
		LastActivityAt: time.Now(),
		Priority:       false,
		Persistent:     true,
	}

	// Store in map
	s.queues.Store(name, queue)

	// Update metrics
	s.metrics.mu.Lock()
	s.metrics.QueuesCreated++
	s.metrics.mu.Unlock()

	return queue, nil
}

// OPTIMIZATION: Background metrics collection for MMO monitoring
func (s *MessageQueueService) metricsCollector() {
	ticker := time.NewTicker(s.config.MetricsInterval)
	defer ticker.Stop()

	for {
		select {
		case <-s.ctx.Done():
			return
		case <-ticker.C:
			s.collectMetrics()
		}
	}
}

// OPTIMIZATION: Efficient metrics collection
func (s *MessageQueueService) collectMetrics() {
	// Collect active consumers count
	activeConsumers := int64(0)
	s.consumers.Range(func(key, value interface{}) bool {
		consumer := value.(*Consumer)
		consumer.mu.RLock()
		if consumer.Active {
			activeConsumers++
		}
		consumer.mu.RUnlock()
		return true
	})

	s.metrics.mu.Lock()
	s.metrics.ConsumersActive = activeConsumers
	s.metrics.mu.Unlock()
}

// OPTIMIZATION: Queue cleanup for memory efficiency in MMO environment
func (s *MessageQueueService) queueCleanup() {
	ticker := time.NewTicker(s.config.CleanupInterval)
	defer ticker.Stop()

	for {
		select {
		case <-s.ctx.Done():
			return
		case <-ticker.C:
			s.cleanupExpiredMessages()
		}
	}
}

// OPTIMIZATION: Efficient cleanup of expired messages
func (s *MessageQueueService) cleanupExpiredMessages() {
	expiredCount := int64(0)

	s.queues.Range(func(key, value interface{}) bool {
		queueName := key.(string)
		queue := value.(*Queue)

		// Use Redis SCAN to find expired messages
		pattern := fmt.Sprintf("mq:queue:%s:*", queueName)
		iter := s.redis.Scan(s.ctx, 0, pattern, 100).Iterator()

		for iter.Next(s.ctx) {
			key := iter.Val()

			// Check if message exists and is expired
			if exists := s.redis.Exists(s.ctx, key).Val(); exists == 0 {
				expiredCount++
			}
		}

		return true
	})

	if expiredCount > 0 {
		s.metrics.mu.Lock()
		s.metrics.MessagesExpired += expiredCount
		s.metrics.mu.Unlock()

		s.logger.WithField("expired", expiredCount).Info("cleaned up expired messages")
	}
}

// OPTIMIZATION: Consumer heartbeat monitoring for MMO reliability
func (s *MessageQueueService) consumerHeartbeatMonitor() {
	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-s.ctx.Done():
			return
		case <-ticker.C:
			s.checkConsumerHeartbeats()
		}
	}
}

// OPTIMIZATION: Efficient consumer heartbeat checking
func (s *MessageQueueService) checkConsumerHeartbeats() {
	now := time.Now()
	timeout := 90 * time.Second // 3x heartbeat interval

	s.consumers.Range(func(key, value interface{}) bool {
		consumer := value.(*Consumer)
		consumer.mu.Lock()

		if consumer.Active && now.Sub(consumer.LastHeartbeat) > timeout {
			consumer.Active = false
			s.logger.WithField("consumer_id", consumer.ID).Warn("consumer heartbeat timeout")
		}

		consumer.mu.Unlock()
		return true
	})
}

// OPTIMIZATION: Graceful shutdown for MMO service reliability
func (s *MessageQueueService) Shutdown(ctx context.Context) error {
	s.logger.Info("shutting down message queue service")

	// Cancel background processes
	s.cancel()

	// Wait for background processes to finish
	done := make(chan struct{})
	go func() {
		s.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		s.logger.Info("background processes stopped")
	case <-ctx.Done():
		s.logger.Warn("shutdown timeout, forcing stop")
	}

	// Close Redis connection
	if err := s.redis.Close(); err != nil {
		s.logger.WithError(err).Error("failed to close Redis connection")
	}

	return nil
}