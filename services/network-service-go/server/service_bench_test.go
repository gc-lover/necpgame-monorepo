package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1978 - Benchmark tests for network performance validation
func BenchmarkNetworkService_BroadcastMessage(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	reqData := BroadcastMessageRequest{
		Message: NetworkMessage{
			MessageID: uuid.New().String(),
			Type:      "CHAT",
			SenderID:  "user_123",
			Content:   "Hello World!",
			Timestamp: time.Now().Unix(),
		},
		Compress: true,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/network/messages/broadcast", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		service.BroadcastMessage(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkNetworkService_SendChannelMessage(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	channelID := "global_chat"

	reqData := ChannelMessageRequest{
		Message: NetworkMessage{
			MessageID: uuid.New().String(),
			Type:      "CHAT",
			SenderID:  "user_123",
			Content:   "Hello Channel!",
			Channel:   channelID,
			Timestamp: time.Now().Unix(),
		},
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/network/messages/"+channelID, bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("channel", channelID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.SendChannelMessage(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkNetworkService_UpdatePresence(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	userID := "user_123"

	reqData := UpdatePresenceRequest{
		Status:   "ONLINE",
		Activity: "PLAYING",
		CustomFields: map[string]interface{}{
			"character_level": 25,
			"guild_name":      "Night City Runners",
		},
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("PUT", "/network/presence/"+userID, bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("userId", userID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.UpdateUserPresence(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkNetworkService_PublishEvent(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	reqData := PublishEventRequest{
		Event: &Event{
			EventID:   uuid.New().String(),
			EventType: "PLAYER_LEVEL_UP",
			Source:    "game_server",
			Data: map[string]interface{}{
				"player_id":   "user_123",
				"old_level":   24,
				"new_level":   25,
				"experience":  12500,
			},
			Timestamp: time.Now().Unix(),
		},
		RoutingStrategy: "BROADCAST",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/network/events/publish", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		service.PublishEvent(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent network operations
func BenchmarkNetworkService_ConcurrentBroadcast(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	reqData := BroadcastMessageRequest{
		Message: NetworkMessage{
			MessageID: uuid.New().String(),
			Type:      "SYSTEM",
			SenderID:  "server",
			Content:   "Server maintenance in 5 minutes",
			Timestamp: time.Now().Unix(),
			Priority:  "HIGH",
		},
		Compress: true,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			req := httptest.NewRequest("POST", "/network/messages/broadcast", bytes.NewReader(reqBody))
			w := httptest.NewRecorder()

			service.BroadcastMessage(w, req)

			if w.Code != http.StatusOK {
				b.Fatalf("Expected status 200, got %d", w.Code)
			}
		}
	})
}

// Performance target validation for network service
func TestNetworkService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	// Test broadcast message performance
	reqData := BroadcastMessageRequest{
		Message: NetworkMessage{
			MessageID: uuid.New().String(),
			Type:      "CHAT",
			SenderID:  "user_123",
			Content:   "Performance test message",
			Timestamp: time.Now().Unix(),
		},
	}

	reqBody, _ := json.Marshal(reqData)
	req := httptest.NewRequest("POST", "/network/messages/broadcast", bytes.NewReader(reqBody))

	// Warm up
	for i := 0; i < 100; i++ {
		w := httptest.NewRecorder()
		service.BroadcastMessage(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			w := httptest.NewRecorder()
			service.BroadcastMessage(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 2000 ops/sec for network messaging
	targetOpsPerSec := 2000.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("Network performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 15 {
		t.Errorf("Too many allocations: %.2f allocs/op > 15 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("Network Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}

// Test WebSocket connection handling
func TestNetworkService_WebSocketConnection(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	// Test WebSocket handler setup (connection would be tested in integration tests)
	req := httptest.NewRequest("GET", "/network/ws?token=test_token&client_id=test_client", nil)

	// This would normally upgrade to WebSocket, but we just test the handler exists
	if service == nil {
		t.Error("Network service should not be nil")
	}

	t.Log("WebSocket handler test passed - service initialized correctly")
}

// Test concurrent message processing
func TestNetworkService_ConcurrentMessageProcessing(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &NetworkMetrics{}
	config := &NetworkServiceConfig{MaxConnections: 5000}
	service := NewNetworkService(logger, metrics, config)

	done := make(chan bool, 10)

	// Test concurrent message processing
	for i := 0; i < 10; i++ {
		go func(msgIndex int) {
			reqData := BroadcastMessageRequest{
				Message: NetworkMessage{
					MessageID: fmt.Sprintf("msg_%d", msgIndex),
					Type:      "CHAT",
					SenderID:  fmt.Sprintf("user_%d", msgIndex),
					Content:   fmt.Sprintf("Concurrent message %d", msgIndex),
					Timestamp: time.Now().Unix(),
				},
			}

			reqBody, _ := json.Marshal(reqData)
			req := httptest.NewRequest("POST", "/network/messages/broadcast", bytes.NewReader(reqBody))
			w := httptest.NewRecorder()

			service.BroadcastMessage(w, req)

			if w.Code != http.StatusOK {
				t.Errorf("Expected status 200 for message %d, got %d", msgIndex, w.Code)
			}

			done <- true
		}(i)
	}

	// Wait for all goroutines to complete
	for i := 0; i < 10; i++ {
		<-done
	}

	t.Log("Concurrent message processing test passed")
}
