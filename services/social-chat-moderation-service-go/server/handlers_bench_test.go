// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/social-chat-moderation-service-go/pkg/api"
)

// BenchmarkReportChatMessage benchmarks ReportChatMessage handler
// Target: <100μs per operation, minimal allocs
func BenchmarkReportChatMessage(b *testing.B) {
	handlers := NewChatModerationHandlers()

	ctx := context.Background()
	req := &api.ReportMessageRequest{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ReportChatMessage(ctx, req)
	}
}
