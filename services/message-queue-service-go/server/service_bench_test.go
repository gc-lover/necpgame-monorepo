package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2143 - Benchmark tests for message queue performance validation
func BenchmarkMessageQueueService_PublishMessage(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{
		QueueBufferSize: 1000,
	}
	service := NewMessageQueueService(logger, metrics, config)

	reqData := PublishMessageRequest{
		QueueName:    "test_queue",
		MessageBody:  "test message for benchmarking",
		ContentType:  "application/json",
		Priority:     5,
		Persistent:   true,
		UserID:       "test_user",
		AppID:        "test_app",
		CorrelationID: "test_correlation",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.PublishMessage(w, req)

		if w.Code != http.StatusAccepted {
			b.Fatalf("Expected status 202, got %d", w.Code)
		}
	}
}

func BenchmarkMessageQueueService_PublishBatchMessages(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{
		QueueBufferSize: 1000,
	}
	service := NewMessageQueueService(logger, metrics, config)

	messages := []PublishMessageRequest{}
	for i := 0; i < 10; i++ {
		messages = append(messages, PublishMessageRequest{
			QueueName:   "batch_test_queue",
			MessageBody: "batch message content",
			ContentType: "application/json",
			Priority:    3,
		})
	}

	reqData := PublishBatchRequest{
		Messages: messages,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/mq/messages/batch", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.PublishBatchMessages(w, req)

		if w.Code != http.StatusAccepted {
			b.Fatalf("Expected status 202, got %d", w.Code)
		}
	}
}

func BenchmarkMessageQueueService_ConsumeMessages(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{
		ConsumerTimeout: 5 * time.Second,
	}
	service := NewMessageQueueService(logger, metrics, config)

	reqData := ConsumeMessagesRequest{
		QueueName:    "consume_test_queue",
		MaxMessages:  5,
		AutoAck:      true,
		ConsumerTag:  "benchmark_consumer",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/mq/consume", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.ConsumeMessages(w, req)

		// Accept both 200 (messages found) and 204 (no messages)
		if w.Code != http.StatusOK && w.Code != http.StatusNoContent {
			b.Fatalf("Expected status 200 or 204, got %d", w.Code)
		}
	}
}

func BenchmarkMessageQueueService_PublishEvent(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{}
	service := NewMessageQueueService(logger, metrics, config)

	reqData := PublishEventRequest{
		EventType: "user.login",
		Exchange:  "events",
		RoutingKey: "user.login.success",
		EventData: map[string]interface{}{
			"user_id": "user_123",
			"timestamp": time.Now().Unix(),
			"ip_address": "127.0.0.1",
		},
		Metadata: map[string]interface{}{
			"source": "auth_service",
			"version": "1.0.0",
			"correlation_id": "corr_123",
		},
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/mq/events", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.PublishEvent(w, req)

		if w.Code != http.StatusAccepted {
			b.Fatalf("Expected status 202, got %d", w.Code)
		}
	}
}

func BenchmarkMessageQueueService_RegisterConsumer(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{}
	service := NewMessageQueueService(logger, metrics, config)

	reqData := RegisterConsumerRequest{
		ConsumerID:    "bench_consumer",
		QueueName:     "benchmark_queue",
		ConsumerTag:   "bench_tag",
		PrefetchCount: 100,
		AutoAck:       false,
		Exclusive:     false,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/mq/consumers", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.RegisterConsumer(w, req)

		if w.Code != http.StatusCreated {
			b.Fatalf("Expected status 201, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent message operations
func BenchmarkMessageQueueService_ConcurrentMessageOperations(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{
		QueueBufferSize: 1000,
	}
	service := NewMessageQueueService(logger, metrics, config)

	reqData := PublishMessageRequest{
		QueueName:   "concurrent_test_queue",
		MessageBody: "concurrent message",
		ContentType: "application/json",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		localReqBody := make([]byte, len(reqBody))
		copy(localReqBody, reqBody)

		for pb.Next() {
			req := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(localReqBody))
			req.Header.Set("X-User-ID", "user_123")
			w := httptest.NewRecorder()

			service.PublishMessage(w, req)

			if w.Code != http.StatusAccepted {
				b.Fatalf("Expected status 202, got %d", w.Code)
			}
		}
	})
}

// Performance target validation for message queue service
func TestMessageQueueService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{}
	service := NewMessageQueueService(logger, metrics, config)

	// Test message publishing performance
	reqData := PublishMessageRequest{
		QueueName:    "perf_test_queue",
		MessageBody:  "performance test message",
		ContentType:  "application/json",
		Priority:     5,
		Persistent:   true,
		UserID:       "perf_user",
		AppID:        "perf_app",
		CorrelationID: "perf_corr",
	}

	reqBody, _ := json.Marshal(reqData)

	// Warm up
	for i := 0; i < 100; i++ {
		req := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()
		service.PublishMessage(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			req := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(reqBody))
			req.Header.Set("X-User-ID", "user_123")
			w := httptest.NewRecorder()
			service.PublishMessage(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 1000 ops/sec for message publishing
	targetOpsPerSec := 1000.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("Message queue performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 20 {
		t.Errorf("Too many allocations: %.2f allocs/op > 20 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("Message Queue Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}

// Test concurrent message queue operations
func TestMessageQueueService_ConcurrentOperations(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{}
	service := NewMessageQueueService(logger, metrics, config)

	done := make(chan bool, 10)

	// Test concurrent message operations
	for i := 0; i < 10; i++ {
		go func(operationIndex int) {
			queueName := fmt.Sprintf("concurrent_queue_%d", operationIndex)

			// Create queue
			createReq := CreateQueueRequest{
				Name: queueName,
				Type: "classic",
			}
			createBody, _ := json.Marshal(createReq)
			createHttpReq := httptest.NewRequest("POST", "/mq/queues", bytes.NewReader(createBody))
			createHttpReq.Header.Set("X-User-ID", "creator")
			createW := httptest.NewRecorder()

			service.CreateQueue(createW, createHttpReq)

			if createW.Code != http.StatusCreated {
				t.Errorf("Failed to create queue %s: %d", queueName, createW.Code)
			}

			// Publish message
			publishReq := PublishMessageRequest{
				QueueName:   queueName,
				MessageBody: "test message",
			}
			publishBody, _ := json.Marshal(publishReq)
			publishHttpReq := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(publishBody))
			publishHttpReq.Header.Set("X-User-ID", "publisher")
			publishW := httptest.NewRecorder()

			service.PublishMessage(publishW, publishHttpReq)

			if publishW.Code != http.StatusAccepted {
				t.Errorf("Failed to publish to queue %s: %d", queueName, publishW.Code)
			}

			done <- true
		}(i)
	}

	// Wait for all goroutines to complete
	for i := 0; i < 10; i++ {
		<-done
	}

	t.Log("Concurrent message queue operations test passed")
}

// Test message queue reliability features
func TestMessageQueueService_ReliabilityFeatures(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{}
	service := NewMessageQueueService(logger, metrics, config)

	userID := "reliability_test_user"

	// Test consumer registration
	consumerReq := RegisterConsumerRequest{
		ConsumerID:    "reliability_consumer",
		QueueName:     "reliability_queue",
		ConsumerTag:   "reliability_tag",
		PrefetchCount: 50,
		AutoAck:       false,
	}
	consumerBody, _ := json.Marshal(consumerReq)
	consumerHttpReq := httptest.NewRequest("POST", "/mq/consumers", bytes.NewReader(consumerBody))
	consumerHttpReq.Header.Set("X-User-ID", userID)
	consumerW := httptest.NewRecorder()

	service.RegisterConsumer(consumerW, consumerHttpReq)

	if consumerW.Code != http.StatusCreated {
		t.Fatalf("Failed to register consumer: %d", consumerW.Code)
	}

	// Test event publishing
	eventReq := PublishEventRequest{
		EventType: "test.event",
		RoutingKey: "test.key",
		EventData: map[string]interface{}{
			"test_field": "test_value",
		},
	}
	eventBody, _ := json.Marshal(eventReq)
	eventHttpReq := httptest.NewRequest("POST", "/mq/events", bytes.NewReader(eventBody))
	eventHttpReq.Header.Set("X-User-ID", userID)
	eventW := httptest.NewRecorder()

	service.PublishEvent(eventW, eventHttpReq)

	if eventW.Code != http.StatusAccepted {
		t.Errorf("Failed to publish event: %d", eventW.Code)
	}

	// Test batch publishing
	batchReq := PublishBatchRequest{
		Messages: []PublishMessageRequest{
			{
				QueueName:   "batch_test_queue",
				MessageBody: "batch message 1",
			},
			{
				QueueName:   "batch_test_queue",
				MessageBody: "batch message 2",
			},
		},
	}
	batchBody, _ := json.Marshal(batchReq)
	batchHttpReq := httptest.NewRequest("POST", "/mq/messages/batch", bytes.NewReader(batchBody))
	batchHttpReq.Header.Set("X-User-ID", userID)
	batchW := httptest.NewRecorder()

	service.PublishBatchMessages(batchW, batchHttpReq)

	if batchW.Code != http.StatusAccepted {
		t.Errorf("Failed to publish batch messages: %d", batchW.Code)
	}

	t.Log("Message queue reliability features test passed")
}

// Test message queue error handling
func TestMessageQueueService_ErrorHandling(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &MessageQueueMetrics{}
	config := &MessageQueueServiceConfig{}
	service := NewMessageQueueService(logger, metrics, config)

	// Test invalid queue name
	invalidReq := PublishMessageRequest{
		QueueName:   "",
		MessageBody: "test",
	}
	invalidBody, _ := json.Marshal(invalidReq)
	invalidHttpReq := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(invalidBody))
	invalidHttpReq.Header.Set("X-User-ID", "user_123")
	invalidW := httptest.NewRecorder()

	service.PublishMessage(invalidW, invalidHttpReq)

	if invalidW.Code != http.StatusBadRequest {
		t.Errorf("Expected BadRequest for invalid queue name, got %d", invalidW.Code)
	}

	// Test unauthorized access
	noAuthReq := PublishMessageRequest{
		QueueName:   "test_queue",
		MessageBody: "test",
	}
	noAuthBody, _ := json.Marshal(noAuthReq)
	noAuthHttpReq := httptest.NewRequest("POST", "/mq/messages", bytes.NewReader(noAuthBody))
	// No X-User-ID header
	noAuthW := httptest.NewRecorder()

	service.PublishMessage(noAuthW, noAuthHttpReq)

	if noAuthW.Code != http.StatusUnauthorized {
		t.Errorf("Expected Unauthorized for missing auth, got %d", noAuthW.Code)
	}

	t.Log("Message queue error handling test passed")
}
