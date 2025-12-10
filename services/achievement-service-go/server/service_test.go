package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

// Issue: #1633
func TestAchievementService_GetAchievements(t *testing.T) {
	svc := NewAchievementService(nil)
	ctx := context.Background()

	params := api.GetAchievementsParams{IncludeHidden: api.NewOptBool(false)}
	achievements, err := svc.GetAchievements(ctx, params)
	assert.NoError(t, err)
	assert.NotNil(t, achievements)
	assert.NotEmpty(t, achievements.Achievements)
	assert.Len(t, achievements.Achievements, 2)

	params.Category = api.NewOptAchievementCategory(api.AchievementCategoryCombat)
	achievements, err = svc.GetAchievements(ctx, params)
	assert.NoError(t, err)
	assert.NotNil(t, achievements)
	assert.Len(t, achievements.Achievements, 1)
	assert.Equal(t, "First Blood", achievements.Achievements[0].Name)
}

func TestAchievementService_GetAchievementDetails(t *testing.T) {
	svc := NewAchievementService(nil)
	ctx := context.Background()

	// Existing achievement
	achID := uuid.MustParse("123e4567-e89b-12d3-a456-426614174000")
	details, err := svc.GetAchievementDetails(ctx, achID)
	assert.NoError(t, err)
	assert.NotNil(t, details)
	assert.Equal(t, "First Blood", details.Name)

	// Non-existing achievement
	_, err = svc.GetAchievementDetails(ctx, uuid.New())
	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not found")
}

func TestAchievementService_GetPlayerProgress(t *testing.T) {
	svc := NewAchievementService(nil)
	ctx := context.Background()
	playerID := uuid.New()

	params := api.GetPlayerProgressParams{UnlockedOnly: api.NewOptBool(false), ClaimedOnly: api.NewOptBool(false)}
	progress, err := svc.GetPlayerProgress(ctx, playerID, params)
	assert.NoError(t, err)
	assert.NotNil(t, progress)
	assert.NotEmpty(t, progress.Achievements)
	assert.Len(t, progress.Achievements, 1)
}

func TestAchievementService_ClaimAchievementReward(t *testing.T) {
	svc := NewAchievementService(nil)
	ctx := context.Background()
	playerID := uuid.New()
	achID := uuid.New()

	rewards, err := svc.ClaimAchievementReward(ctx, playerID, achID)
	assert.NoError(t, err)
	assert.NotNil(t, rewards)
	assert.True(t, rewards.TitleUnlocked.Value)
}

func TestAchievementService_GetPlayerTitles(t *testing.T) {
	svc := NewAchievementService(nil)
	ctx := context.Background()
	playerID := uuid.New()

	titles, err := svc.GetPlayerTitles(ctx, playerID)
	assert.NoError(t, err)
	assert.NotNil(t, titles)
	assert.NotEmpty(t, titles.Titles)
	assert.True(t, titles.ActiveTitle.IsSet())
}

func TestAchievementService_SetActiveTitle(t *testing.T) {
	svc := NewAchievementService(nil)
	ctx := context.Background()
	playerID := uuid.New()
	titleID := uuid.New()

	req := &api.SetActiveTitleReq{TitleID: titleID}
	activeTitle, err := svc.SetActiveTitle(ctx, playerID, req)
	assert.NoError(t, err)
	assert.NotNil(t, activeTitle)
	assert.Equal(t, titleID, activeTitle.ID)
	assert.True(t, activeTitle.IsActive.Value)
}
