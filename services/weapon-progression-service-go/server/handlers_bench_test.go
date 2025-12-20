// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/gc-lover/necpgame-monorepo/services/weapon-progression-service-go/pkg/api"
	"github.com/google/uuid"
)

// BenchmarkAPIV1WeaponsProgressionWeaponIdGet benchmarks APIV1WeaponsProgressionWeaponIdGet handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAPIV1WeaponsProgressionWeaponIdGet(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.APIV1WeaponsProgressionWeaponIdGetParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.APIV1WeaponsProgressionWeaponIdGet(ctx, params)
	}
}

// BenchmarkAPIV1WeaponsProgressionWeaponIdPost benchmarks APIV1WeaponsProgressionWeaponIdPost handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAPIV1WeaponsProgressionWeaponIdPost(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.UpgradeWeaponRequest{
		// TODO: Fill request fields based on API spec
	}
	params := api.APIV1WeaponsProgressionWeaponIdPostParams{
		WeaponId: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.APIV1WeaponsProgressionWeaponIdPost(ctx, req, params)
	}
}

// BenchmarkAPIV1WeaponsMasteryGet benchmarks APIV1WeaponsMasteryGet handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAPIV1WeaponsMasteryGet(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.APIV1WeaponsMasteryGetParams{}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.APIV1WeaponsMasteryGet(ctx, params)
	}
}
