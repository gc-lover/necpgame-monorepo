// Issue: #1911
// Unit tests for chat moderation service
package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-go/pkg/api"
	"github.com/google/uuid"
)

func TestService_CheckMessage_WordFilter(t *testing.T) {
	service := NewService(nil)

	req := &api.CheckMessageRequest{
		PlayerID: uuid.New(),
		Message:  "This message contains badword",
	}

	// Create test rule
	rule := api.ModerationRule{
		ID:       uuid.New(),
		Name:     "Test Word Filter",
		Type:     api.ModerationRuleTypeWordFilter,
		Pattern:  "badword",
		Severity: api.ModerationRuleSeverityMedium,
		Action:   api.ModerationRuleActionWarn,
		Active:   true,
	}

	// Mock repository to return our test rule
	// This is a simplified test - in real scenario we'd use a test DB

	// For now, test the basic logic
	violated, desc := service.checkWordFilter(req.Message, rule.Pattern)
	if !violated {
		t.Error("Expected word filter to trigger")
	}
	if desc == "" {
		t.Error("Expected description for violation")
	}
}

func TestService_CheckMessage_SpamDetection(t *testing.T) {
	service := NewService(nil)

	tests := []struct {
		name     string
		message  string
		expected bool
	}{
		{"Normal message", "Hello world", false},
		{"Excessive caps", "HELLO WORLD THIS IS A TEST", true},
		{"Repeated chars", "Heeeelllllooooo", true},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			playerID := uuid.New().String()
			violated, desc := service.checkSpamDetection(context.Background(), playerID, tt.message, "spam")
			if violated != tt.expected {
				t.Errorf("Expected violation=%v, got %v. Description: %s", tt.expected, violated, desc)
			}
		})
	}
}

func TestService_CheckMessage_ToxicityAnalysis(t *testing.T) {
	service := NewService(nil)

	tests := []struct {
		name     string
		message  string
		expected bool
	}{
		{"Clean message", "Hello everyone", false},
		{"Toxic message", "You are toxic and hate this game", true},
		{"Insult", "You are an insult to gamers", true},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			violated, desc := service.checkToxicityAnalysis(tt.message, "toxic")
			if violated != tt.expected {
				t.Errorf("Expected violation=%v, got %v. Description: %s", tt.expected, violated, desc)
			}
		})
	}
}

// Benchmark for CheckMessage - critical for P99 <50ms requirement
func BenchmarkCheckMessage(b *testing.B) {
	service := NewService(nil)

	req := &api.CheckMessageRequest{
		PlayerID: uuid.New(),
		Message:  "This is a normal chat message from a player",
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, err := service.CheckMessage(context.Background(), req)
		if err != nil {
			b.Fatal(err)
		}
	}
}

// Test memory pooling effectiveness
func TestService_MemoryPooling(t *testing.T) {
	service := NewService(nil)

	// Test that pool returns same instance
	resp1 := service.checkResponsePool.Get().(*api.CheckMessageResponse)
	service.checkResponsePool.Put(resp1)

	resp2 := service.checkResponsePool.Get().(*api.CheckMessageResponse)
	if resp1 != resp2 {
		t.Error("Memory pool should reuse instances")
	}
}

// Test concurrent access safety
func TestService_ConcurrentAccess(t *testing.T) {
	service := NewService(nil)

	// Run multiple goroutines accessing the service
	done := make(chan bool, 10)

	for i := 0; i < 10; i++ {
		go func(id int) {
			req := &api.CheckMessageRequest{
				PlayerID: uuid.New(),
				Message:  "Test message",
			}

			_, err := service.CheckMessage(context.Background(), req)
			if err != nil {
				t.Errorf("Concurrent access failed: %v", err)
			}
			done <- true
		}(i)
	}

	// Wait for all goroutines
	for i := 0; i < 10; i++ {
		select {
		case <-done:
			// OK
		case <-time.After(5 * time.Second):
			t.Error("Concurrent test timed out")
		}
	}
}
