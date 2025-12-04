// Issue: Performance benchmarks
// Auto-generated benchmark file
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/referral-service-go/pkg/api"
	"github.com/sirupsen/logrus"
)

// BenchmarkGetReferralCode benchmarks GetReferralCode handler
// Target: <100μs per operation, minimal allocs
func BenchmarkGetReferralCode(b *testing.B) {
	handlers := NewServiceHandlers(logrus.New())

	ctx := context.Background()
	params := api.GetReferralCodeParams{
		PlayerID: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.GetReferralCode(ctx, params)
	}
}
