package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-core-service-go/pkg/api"
)

// Issue: #1636
func TestBattlePassCoreService_GetCurrentBattlePass(t *testing.T) {
	svc := NewBattlePassCoreService(nil)
	ctx := context.Background()

	season, err := svc.GetCurrentBattlePass(ctx)
	assert.NoError(t, err)
	assert.NotEmpty(t, season.ID.Value)
	assert.Equal(t, "Season 1: First Strike", season.Name.Value)
	assert.True(t, season.IsActive.Value)
}

func TestBattlePassCoreService_GetPlayerProgress(t *testing.T) {
	svc := NewBattlePassCoreService(nil)
	ctx := context.Background()
	charID := uuid.New()

	progress, err := svc.GetPlayerProgress(ctx, api.GetPlayerProgressParams{CharacterID: charID})
	assert.NoError(t, err)
	assert.Equal(t, charID, progress.CharacterID.Value)
	assert.Equal(t, 10, progress.Level.Value)
	assert.False(t, progress.HasPremium.Value)
}

func TestBattlePassCoreService_GetLevelRequirements(t *testing.T) {
	svc := NewBattlePassCoreService(nil)
	ctx := context.Background()

	req, err := svc.GetLevelRequirements(ctx, 10)
	assert.NoError(t, err)
	assert.Equal(t, 10, req.Level.Value)
	assert.Equal(t, 1000, req.XpRequired.Value)

	_, err = svc.GetLevelRequirements(ctx, 0)
	assert.Error(t, err)

	_, err = svc.GetLevelRequirements(ctx, 101)
	assert.Error(t, err)
}

//func TestBattlePassCoreService_PurchasePremium(t *testing.T) {
//	svc := NewBattlePassCoreService(nil)
//	ctx := context.Background()
//	charID := uuid.New()
//
//	// Первая покупка
//	req := api.PurchasePremiumRequest{CharacterID: charID}
//	progress, err := svc.PurchasePremium(ctx, req)
//	assert.NoError(t, err)
//	assert.True(t, progress.HasPremium.Value)
//	assert.True(t, progress.PremiumPurchasedAt.IsSet())
//
//	// Повторная покупка
//	progressAfterSecondPurchase, err := svc.PurchasePremium(ctx, req)
//	assert.Error(t, err)
//	assert.Contains(t, err.Error(), "premium already purchased")
//	assert.Nil(t, progressAfterSecondPurchase)
//
//}
