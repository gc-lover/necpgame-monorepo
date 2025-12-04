// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-history-service-go/pkg/api"
)

// BenchmarkGetChatHistory benchmarks GetChatHistory handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetChatHistory(b *testing.B) {
	handlers := NewChatHistoryHandlers()

	ctx := context.Background()
	params := api.GetChatHistoryParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetChatHistory(ctx, params)
	}
}
