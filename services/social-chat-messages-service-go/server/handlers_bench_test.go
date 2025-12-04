// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-messages-service-go/pkg/api"
)

// BenchmarkSendChatMessage benchmarks SendChatMessage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkSendChatMessage(b *testing.B) {
	handlers := NewChatMessagesHandlers()

	ctx := context.Background()
	req := &api.SendMessageRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.SendChatMessage(ctx, req)
	}
}
