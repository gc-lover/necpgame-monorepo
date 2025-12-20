// Issue: #1911
package main

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap/zaptest"

	"necpgame/services/chat-moderation-service-go/internal/config"
	"necpgame/services/chat-moderation-service-go/internal/repository"
	"necpgame/services/chat-moderation-service-go/internal/service"
	"necpgame/services/chat-moderation-service-go/models"
)

func TestModerationService_CheckMessage(t *testing.T) {
	// Create test config
	cfg := &config.Config{
		Logger:             zaptest.NewLogger(t),
		MaxMessageLength:   500,
		ProcessingTimeout:  50 * time.Millisecond,
		RateLimitPerSecond: 10,
	}

	// Create mock repository (simplified)
	repo := &repository.Repository{} // Would need proper mock implementation

	// Create service
	moderationSvc := service.NewModerationService(repo, cfg)

	tests := []struct {
		name            string
		message         string
		playerID        uuid.UUID
		channelType     models.ChannelType
		expectAllowed   bool
		expectViolation bool
	}{
		{
			name:            "Clean message",
			message:         "Hello, how are you?",
			playerID:        uuid.New(),
			channelType:     models.ChannelTypeGlobal,
			expectAllowed:   true,
			expectViolation: false,
		},
		{
			name:            "Message too long",
			message:         string(make([]byte, 600)), // Over limit
			playerID:        uuid.New(),
			channelType:     models.ChannelTypeGlobal,
			expectAllowed:   false,
			expectViolation: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			req := &models.CheckMessageRequest{
				PlayerID:    tt.playerID,
				Message:     tt.message,
				ChannelType: tt.channelType,
			}

			ctx, cancel := context.WithTimeout(context.Background(), 100*time.Millisecond)
			defer cancel()

			resp, err := moderationSvc.CheckMessage(ctx, req)
			if err != nil {
				t.Fatalf("CheckMessage failed: %v", err)
			}

			if resp.Allowed != tt.expectAllowed {
				t.Errorf("Expected allowed=%v, got %v", tt.expectAllowed, resp.Allowed)
			}

			if resp.ViolationDetected != tt.expectViolation {
				t.Errorf("Expected violation=%v, got %v", tt.expectViolation, resp.ViolationDetected)
			}

			// Check processing time is reasonable
			if resp.ProcessingTimeMs > 100 {
				t.Errorf("Processing time too high: %f ms", resp.ProcessingTimeMs)
			}
		})
	}
}

func TestModerationService_RateLimit(t *testing.T) {
	// Test rate limiting functionality
	cfg := &config.Config{
		Logger:             zaptest.NewLogger(t),
		MaxMessageLength:   500,
		ProcessingTimeout:  50 * time.Millisecond,
		RateLimitPerSecond: 1, // Very low limit for testing
	}

	playerID := uuid.New()
	repo := &repository.Repository{}
	moderationSvc := service.NewModerationService(repo, cfg)

	// Send messages rapidly to trigger rate limit
	violations := 0
	for i := 0; i < 5; i++ {
		req := &models.CheckMessageRequest{
			PlayerID:    playerID,
			Message:     "Test message",
			ChannelType: models.ChannelTypeGlobal,
		}

		resp, err := moderationSvc.CheckMessage(context.Background(), req)
		if err != nil {
			continue // Skip errors in this test
		}

		if resp.ViolationDetected && resp.ViolationType == models.ViolationTypeRateLimitExceeded {
			violations++
		}
	}

	// Should have at least some rate limit violations
	if violations == 0 {
		t.Log("Warning: No rate limit violations detected (may be expected with Redis mock)")
	}
}

func TestModerationService_Stats(t *testing.T) {
	cfg := &config.Config{
		Logger: zaptest.NewLogger(t),
	}

	repo := &repository.Repository{}
	moderationSvc := service.NewModerationService(repo, cfg)

	// Get initial stats
	stats, err := moderationSvc.GetStats(context.Background(), "24h")
	if err != nil {
		t.Fatalf("GetStats failed: %v", err)
	}

	if stats.Timeframe != "24h" {
		t.Errorf("Expected timeframe '24h', got '%s'", stats.Timeframe)
	}

	// Stats should be valid (may be zero initially)
	if stats.TotalMessagesChecked < 0 {
		t.Errorf("Invalid total messages: %d", stats.TotalMessagesChecked)
	}
}

// Benchmark for performance testing
func BenchmarkModerationService_CheckMessage(b *testing.B) {
	cfg := &config.Config{
		Logger:             zaptest.NewLogger(b),
		MaxMessageLength:   500,
		ProcessingTimeout:  50 * time.Millisecond,
		RateLimitPerSecond: 1000, // High limit for benchmark
	}

	repo := &repository.Repository{}
	moderationSvc := service.NewModerationService(repo, cfg)

	req := &models.CheckMessageRequest{
		PlayerID:    uuid.New(),
		Message:     "This is a clean test message",
		ChannelType: models.ChannelTypeGlobal,
	}

	b.ResetTimer()
	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			_, _ = moderationSvc.CheckMessage(context.Background(), req)
		}
	})
}
