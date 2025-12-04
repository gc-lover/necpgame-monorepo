// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-channels-service-go/pkg/api"
)

// BenchmarkGetChannels benchmarks GetChannels handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetChannels(b *testing.B) {
	handlers := NewChatChannelsHandlers()

	ctx := context.Background()
	params := api.GetChannelsParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetChannels(ctx, params)
	}
}
