// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/pkg/api"
)

// BenchmarkCreateCharacterV2 benchmarks CreateCharacterV2 handler
// Target: <200μs per operation, minimal allocs
func BenchmarkCreateCharacterV2(b *testing.B) {
	service, _ := NewCharacterService("", "", "")
	handlers := NewCharacterHandlersOgen(service)

	ctx := context.Background()
	req := &api.CreateCharacterV2Request{
		Name: "TestCharacter",
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateCharacterV2(ctx, req)
	}
}

// BenchmarkGetCharacterV2 benchmarks GetCharacterV2 handler
// Target: <100μs per operation, minimal allocs (hot path: 1.5k RPS)
func BenchmarkGetCharacterV2(b *testing.B) {
	service, _ := NewCharacterService("", "", "")
	handlers := NewCharacterHandlersOgen(service)

	ctx := context.Background()
	params := api.GetCharacterV2Params{
		CharacterID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCharacterV2(ctx, params)
	}
}

// BenchmarkGetCurrentCharacter benchmarks GetCurrentCharacter handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetCurrentCharacter(b *testing.B) {
	service, _ := NewCharacterService("", "", "")
	handlers := NewCharacterHandlersOgen(service)

	ctx := context.Background()
	params := api.GetCurrentCharacterParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetCurrentCharacter(ctx, params)
	}
}

// BenchmarkDeleteCharacterV2 benchmarks DeleteCharacterV2 handler
// Target: <200μs per operation, minimal allocs
func BenchmarkDeleteCharacterV2(b *testing.B) {
	service, _ := NewCharacterService("", "", "")
	handlers := NewCharacterHandlersOgen(service)

	ctx := context.Background()
	req := &api.DeleteCharacterRequest{}
	params := api.DeleteCharacterV2Params{
		CharacterID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DeleteCharacterV2(ctx, req, params)
	}
}

// BenchmarkValidateCharacterName benchmarks ValidateCharacterName handler
// Target: <100μs per operation, minimal allocs
func BenchmarkValidateCharacterName(b *testing.B) {
	service, _ := NewCharacterService("", "", "")
	handlers := NewCharacterHandlersOgen(service)

	ctx := context.Background()
	params := api.ValidateCharacterNameParams{
		Name: "TestCharacter",
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.ValidateCharacterName(ctx, params)
	}
}
