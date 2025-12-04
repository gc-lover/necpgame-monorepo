// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/party-service-go/pkg/api"
)

// mockPartyService implements PartyService methods for benchmarks
type mockPartyService struct{}

func (m *mockPartyService) CreateParty(ctx context.Context, leaderID, name, lootMode string) (*Party, error) {
	return nil, nil
}

func (m *mockPartyService) GetParty(ctx context.Context, partyID string) (*Party, error) {
	return nil, nil
}

func (m *mockPartyService) DisbandParty(ctx context.Context, partyID string) error {
	return nil
}

// BenchmarkCreateParty benchmarks CreateParty handler
// Target: <100μs per operation, minimal allocs
func BenchmarkCreateParty(b *testing.B) {
	repo := NewPartyRepository()
	service := NewPartyService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := api.NewOptCreatePartyRequest(api.CreatePartyRequest{})

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.CreateParty(ctx, req)
	}
}

// BenchmarkGetParty benchmarks GetParty handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetParty(b *testing.B) {
	repo := NewPartyRepository()
	service := NewPartyService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.GetPartyParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetParty(ctx, params)
	}
}

// BenchmarkDisbandParty benchmarks DisbandParty handler
// Target: <100μs per operation, minimal allocs
func BenchmarkDisbandParty(b *testing.B) {
	repo := NewPartyRepository()
	service := NewPartyService(repo)
	handlers := NewHandlers(service)

	ctx := context.Background()
	b.ReportAllocs()
	b.ResetTimer()

	params := api.DisbandPartyParams{}

	for i := 0; i < b.N; i++ {
		_, _ = handlers.DisbandParty(ctx, params)
	}
}

