package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #2030 - Benchmark tests for voice chat performance validation
func BenchmarkVoiceChatService_CreateChannel(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{
		MaxChannelSize: 50,
	}
	service := NewVoiceChatService(logger, metrics, config)

	reqData := CreateChannelRequest{
		Name:            "test-channel",
		Type:            "group",
		Description:     "Test voice channel",
		MaxParticipants: 10,
		AudioSettings: AudioSettings{
			SampleRate: 22050,
			Channels:   1,
			Bitrate:    64000,
			Codec:      "OPUS",
		},
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/voice/channels", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.CreateChannel(w, req)

		if w.Code != http.StatusCreated {
			b.Fatalf("Expected status 201, got %d", w.Code)
		}
	}
}

func BenchmarkVoiceChatService_JoinChannel(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{
		MaxChannelSize: 50,
	}
	service := NewVoiceChatService(logger, metrics, config)

	// Create a test channel
	channel := &VoiceChannel{
		ChannelID:       "channel_123",
		Name:            "test-channel",
		Type:            "group",
		CreatorID:       "creator_123",
		CreatedAt:       time.Now(),
		MaxParticipants: 10,
		ParticipantCount: 0,
		Participants:    make(map[string]*ChannelParticipant),
		LastActivity:    time.Now(),
	}
	service.channels.Store(channel.ChannelID, channel)

	reqData := JoinChannelRequest{
		DeviceInfo: DeviceInfo{
			DeviceType: "DESKTOP",
		},
		AudioCapabilities: AudioCapabilities{
			SupportedCodecs: []string{"OPUS", "SPEEX"},
		},
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/voice/channels/channel_123/join", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.JoinChannel(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkVoiceChatService_StartAudioStream(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{}
	service := NewVoiceChatService(logger, metrics, config)

	reqData := StartStreamRequest{
		ChannelID: "channel_123",
		AudioFormat: AudioSettings{
			SampleRate: 22050,
			Channels:   1,
			Bitrate:    64000,
			Codec:      "OPUS",
		},
		StreamType: "voice",
		Priority:   "normal",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/voice/stream/start", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.StartAudioStream(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkVoiceChatService_TextToSpeech(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{}
	service := NewVoiceChatService(logger, metrics, config)

	reqData := TextToSpeechRequest{
		Text:    "Hello, this is a test message for voice chat",
		Voice:   "neutral",
		Language: "en-US",
		Speed:   1.0,
		Volume:  0.8,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/voice/tts/synthesize", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.TextToSpeech(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkVoiceChatService_GetProximityAudio(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{
		ProximityRadius: 25.0,
	}
	service := NewVoiceChatService(logger, metrics, config)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/voice/proximity?location=10,20,5&radius=25", nil)
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()

		service.GetProximityAudio(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent voice operations
func BenchmarkVoiceChatService_ConcurrentChannelOperations(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{
		MaxChannelSize: 50,
	}
	service := NewVoiceChatService(logger, metrics, config)

	reqData := CreateChannelRequest{
		Name:            "concurrent-test",
		Type:            "group",
		MaxParticipants: 10,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		localReqBody := make([]byte, len(reqBody))
		copy(localReqBody, reqBody)

		for pb.Next() {
			req := httptest.NewRequest("POST", "/voice/channels", bytes.NewReader(localReqBody))
			req.Header.Set("X-User-ID", "user_123")
			w := httptest.NewRecorder()

			service.CreateChannel(w, req)

			if w.Code != http.StatusCreated {
				b.Fatalf("Expected status 201, got %d", w.Code)
			}
		}
	})
}

// Performance target validation for voice chat service
func TestVoiceChatService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{}
	service := NewVoiceChatService(logger, metrics, config)

	// Test channel creation performance
	reqData := CreateChannelRequest{
		Name:            "perf-test-channel",
		Type:            "group",
		MaxParticipants: 10,
	}

	reqBody, _ := json.Marshal(reqData)

	// Warm up
	for i := 0; i < 100; i++ {
		req := httptest.NewRequest("POST", "/voice/channels", bytes.NewReader(reqBody))
		req.Header.Set("X-User-ID", "user_123")
		w := httptest.NewRecorder()
		service.CreateChannel(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			req := httptest.NewRequest("POST", "/voice/channels", bytes.NewReader(reqBody))
			req.Header.Set("X-User-ID", "user_123")
			w := httptest.NewRecorder()
			service.CreateChannel(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 500 ops/sec for voice channel operations
	targetOpsPerSec := 500.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("Voice chat performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 50 {
		t.Errorf("Too many allocations: %.2f allocs/op > 50 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("Voice Chat Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}

// Test concurrent voice channel management
func TestVoiceChatService_ConcurrentChannels(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{
		MaxChannelSize: 50,
	}
	service := NewVoiceChatService(logger, metrics, config)

	done := make(chan bool, 5)

	// Test concurrent channel operations
	for i := 0; i < 5; i++ {
		go func(channelIndex int) {
			channelName := fmt.Sprintf("channel-%d", channelIndex)

			// Create channel
			reqData := CreateChannelRequest{
				Name:            channelName,
				Type:            "group",
				MaxParticipants: 10,
			}
			reqBody, _ := json.Marshal(reqData)
			req := httptest.NewRequest("POST", "/voice/channels", bytes.NewReader(reqBody))
			req.Header.Set("X-User-ID", "user_123")
			w := httptest.NewRecorder()

			service.CreateChannel(w, req)

			if w.Code != http.StatusCreated {
				t.Errorf("Expected status 201 for channel %s, got %d", channelName, w.Code)
			}

			done <- true
		}(i)
	}

	// Wait for all goroutines to complete
	for i := 0; i < 5; i++ {
		<-done
	}

	t.Log("Concurrent channel management test passed")
}

// Test voice channel participant limits
func TestVoiceChatService_ChannelLimits(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{}
	service := NewVoiceChatService(logger, metrics, config)

	// Create a small channel
	reqData := CreateChannelRequest{
		Name:            "small-channel",
		Type:            "group",
		MaxParticipants: 2, // Very small for testing
	}
	reqBody, _ := json.Marshal(reqData)
	req := httptest.NewRequest("POST", "/voice/channels", bytes.NewReader(reqBody))
	req.Header.Set("X-User-ID", "creator")
	w := httptest.NewRecorder()
	service.CreateChannel(w, req)

	if w.Code != http.StatusCreated {
		t.Fatalf("Failed to create channel: %d", w.Code)
	}

	// Try to join with multiple users
	users := []string{"user1", "user2", "user3"}
	for i, userID := range users {
		joinReq := JoinChannelRequest{
			DeviceInfo: DeviceInfo{DeviceType: "DESKTOP"},
		}
		joinBody, _ := json.Marshal(joinReq)
		joinHttpReq := httptest.NewRequest("POST", "/voice/channels/channel_123/join", bytes.NewReader(joinBody))
		joinHttpReq.Header.Set("X-User-ID", userID)
		joinW := httptest.NewRecorder()

		service.JoinChannel(joinW, joinHttpReq)

		if i < 2 { // First two should succeed
			if joinW.Code != http.StatusOK {
				t.Errorf("User %s should join successfully, got %d", userID, joinW.Code)
			}
		} else { // Third should fail (channel full)
			if joinW.Code != http.StatusTooManyRequests {
				t.Errorf("User %s should be rejected (channel full), got %d", userID, joinW.Code)
			}
		}
	}

	t.Log("Channel participant limits test passed")
}

// Test audio stream lifecycle
func TestVoiceChatService_AudioStreamLifecycle(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &VoiceChatMetrics{}
	config := &VoiceChatServiceConfig{}
	service := NewVoiceChatService(logger, metrics, config)

	userID := "test_user"

	// Start stream
	startReq := StartStreamRequest{
		ChannelID: "test_channel",
		AudioFormat: AudioSettings{
			SampleRate: 22050,
			Channels:   1,
			Bitrate:    64000,
			Codec:      "OPUS",
		},
		StreamType: "voice",
	}
	startBody, _ := json.Marshal(startReq)
	startHttpReq := httptest.NewRequest("POST", "/voice/stream/start", bytes.NewReader(startBody))
	startHttpReq.Header.Set("X-User-ID", userID)
	startW := httptest.NewRecorder()

	service.StartAudioStream(startW, startHttpReq)

	if startW.Code != http.StatusOK {
		t.Fatalf("Failed to start audio stream: %d", startW.Code)
	}

	// Stop stream
	stopHttpReq := httptest.NewRequest("POST", "/voice/stream/stop", nil)
	stopHttpReq.Header.Set("X-User-ID", userID)
	stopW := httptest.NewRecorder()

	service.StopAudioStream(stopW, stopHttpReq)

	if stopW.Code != http.StatusOK {
		t.Errorf("Failed to stop audio stream: %d", stopW.Code)
	}

	// Try to stop again (should fail)
	stopAgainW := httptest.NewRecorder()
	service.StopAudioStream(stopAgainW, stopHttpReq)

	if stopAgainW.Code != http.StatusNotFound {
		t.Errorf("Second stop should fail, got %d", stopAgainW.Code)
	}

	t.Log("Audio stream lifecycle test passed")
}
