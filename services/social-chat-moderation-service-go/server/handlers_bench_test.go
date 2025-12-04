// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-moderation-service-go/pkg/api"
)

// BenchmarkModerateMessage benchmarks ModerateMessage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkModerateMessage(b *testing.B) {
	handlers := NewChatModerationHandlers()

	ctx := context.Background()
	req := &api.ModerateMessageRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ModerateMessage(ctx, req)
	}
}
