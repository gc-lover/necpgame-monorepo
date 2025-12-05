// Issue: Performance benchmarks
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	api "github.com/gc-lover/necpgame-monorepo/services/weapon-resource-service-go/pkg/api"
)

// BenchmarkAPIV1WeaponsResourcesWeaponIdGet benchmarks APIV1WeaponsResourcesWeaponIdGet handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAPIV1WeaponsResourcesWeaponIdGet(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	params := api.APIV1WeaponsResourcesWeaponIdGetParams{
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.APIV1WeaponsResourcesWeaponIdGet(ctx, params)
	}
}

// BenchmarkAPIV1WeaponsResourcesWeaponIdConsumePost benchmarks APIV1WeaponsResourcesWeaponIdConsumePost handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAPIV1WeaponsResourcesWeaponIdConsumePost(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.ConsumeResourceRequest{
		// TODO: Fill request fields based on API spec
	}
	params := api.APIV1WeaponsResourcesWeaponIdConsumePostParams{
		WeaponId: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.APIV1WeaponsResourcesWeaponIdConsumePost(ctx, req, params)
	}
}

// BenchmarkAPIV1WeaponsResourcesWeaponIdCooldownPost benchmarks APIV1WeaponsResourcesWeaponIdCooldownPost handler
// Target: <100μs per operation, minimal allocs
func BenchmarkAPIV1WeaponsResourcesWeaponIdCooldownPost(b *testing.B) {
	service := NewService(nil)
	handlers := NewHandlers(service)

	ctx := context.Background()
	req := &api.ApplyCooldownRequest{
		// TODO: Fill request fields based on API spec
	}
	params := api.APIV1WeaponsResourcesWeaponIdCooldownPostParams{
		WeaponId: uuid.New(),
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = handlers.APIV1WeaponsResourcesWeaponIdCooldownPost(ctx, req, params)
	}
}

