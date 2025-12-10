// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/pkg/api"
)

// BenchmarkCreateChannel benchmarks CreateChannel handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateChannel(b *testing.B) {
	service := &mockVoiceService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.CreateChannelRequest{
		// TODO: Fill request fields based on API spec
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateChannel(ctx, req)
	}
}

// BenchmarkGetChannel benchmarks GetChannel handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetChannel(b *testing.B) {
	service := &mockVoiceService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetChannelParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetChannel(ctx, params)
	}
}

// BenchmarkListChannels benchmarks ListChannels handler
// Target: <100μs per operation, minimal allocs
func BenchmarkListChannels(b *testing.B) {
	service := &mockVoiceService{}
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.ListChannelsParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ListChannels(ctx, params)
	}
}

