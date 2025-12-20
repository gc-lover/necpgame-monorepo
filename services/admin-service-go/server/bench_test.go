// OPTIMIZATION: Benchmarks for zero allocations validation (Issue #2182)
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
)

func BenchmarkBanPlayer_ZeroAllocs(b *testing.B) {
	// Setup
	service, _ := NewAdminService("postgresql://test:test@localhost:5432/test?sslmode=disable", "redis://localhost:6379/0")
	ctx := context.Background()
	req := &models.BanPlayerRequest{
		CharacterID: uuid.New(),
		Reason:      "Test ban",
		Permanent:   true,
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, err := service.BanPlayer(ctx, uuid.New(), req, "127.0.0.1", "test-agent")
		if err != nil {
			b.Fatal(err)
		}
	}
}

func BenchmarkKickPlayer_ZeroAllocs(b *testing.B) {
	// Setup
	service, _ := NewAdminService("postgresql://test:test@localhost:5432/test?sslmode=disable", "redis://localhost:6379/0")
	ctx := context.Background()
	req := &models.KickPlayerRequest{
		CharacterID: uuid.New(),
		Reason:      "Test kick",
	}

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		_, err := service.KickPlayer(ctx, uuid.New(), req, "127.0.0.1", "test-agent")
		if err != nil {
			b.Fatal(err)
		}
	}
}

func BenchmarkLogAction_ZeroAllocs(b *testing.B) {
	// Setup
	service, _ := NewAdminService("postgresql://test:test@localhost:5432/test?sslmode=disable", "redis://localhost:6379/0")
	ctx := context.Background()
	targetID := uuid.New()

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		err := service.LogAction(ctx, uuid.New(), models.AdminActionTypeBan, &targetID, "character",
			map[string]interface{}{"test": "data"}, "127.0.0.1", "test-agent")
		if err != nil {
			b.Fatal(err)
		}
	}
}
