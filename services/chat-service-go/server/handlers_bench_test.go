// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/chat-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkSendMessage benchmarks SendMessage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkSendMessage(b *testing.B) {
	repo, _ := NewRepository("")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.SendMessageRequest{
		ChannelID: uuid.New(),
		Content:   "Test message",
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SendMessage(ctx, req)
	}
}

// BenchmarkGetChannelMessages benchmarks GetChannelMessages handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetChannelMessages(b *testing.B) {
	repo, _ := NewRepository("")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetChannelMessagesParams{
		ChannelID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetChannelMessages(ctx, params)
	}
}

// BenchmarkGetChannels benchmarks GetChannels handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetChannels(b *testing.B) {
	repo, _ := NewRepository("")
	service := NewService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetChannels(ctx)
	}
}

