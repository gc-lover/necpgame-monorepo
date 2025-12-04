// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-format-service-go/pkg/api"
)

// BenchmarkFormatChatMessage benchmarks FormatChatMessage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkFormatChatMessage(b *testing.B) {
	handlers := NewChatFormatHandlers()

	ctx := context.Background()
	req := &api.FormatMessageRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.FormatChatMessage(ctx, req)
	}
}
