// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-commands-service-go/pkg/api"
)

// BenchmarkExecuteChatCommand benchmarks ExecuteChatCommand handler
// Target: <100μs per operation, minimal allocs
func BenchmarkExecuteChatCommand(b *testing.B) {
	handlers := NewChatCommandsHandlers()

	ctx := context.Background()
	req := &api.ExecuteCommandRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ExecuteChatCommand(ctx, req)
	}
}
