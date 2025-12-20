package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestRepository(t *testing.T) (*MovementRepository, func()) {
	dbURL := requireTestDBURL(t)
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	require.NoError(t, err)

	repo := NewMovementRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewMovementRepository(t *testing.T) {
	dbURL := requireTestDBURL(t)
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	require.NoError(t, err)
	defer dbPool.Close()

	repo := NewMovementRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestMovementRepository_GetPositionByCharacterID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	pos, err := repo.GetPositionByCharacterID(ctx, characterID)

	require.NoError(t, err)
	assert.Nil(t, pos)
}

func TestMovementRepository_SavePosition(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: 1.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	ctx := context.Background()
	pos, err := repo.SavePosition(ctx, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, pos)
	assert.Equal(t, characterID, pos.CharacterID)
	assert.Equal(t, req.PositionX, pos.PositionX)
	assert.Equal(t, req.PositionY, pos.PositionY)
	assert.Equal(t, req.PositionZ, pos.PositionZ)
	assert.Equal(t, req.Yaw, pos.Yaw)
	assert.Equal(t, req.VelocityX, pos.VelocityX)
	assert.Equal(t, req.VelocityY, pos.VelocityY)
	assert.Equal(t, req.VelocityZ, pos.VelocityZ)
}

func TestMovementRepository_SavePosition_UpdateExisting(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req1 := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: 1.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	ctx := context.Background()
	pos1, err := repo.SavePosition(ctx, characterID, req1)
	require.NoError(t, err)
	require.NotNil(t, pos1)

	req2 := &models.SavePositionRequest{
		PositionX: 11.5,
		PositionY: 21.3,
		PositionZ: 31.1,
		Yaw:       46.0,
		VelocityX: 2.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	pos2, err := repo.SavePosition(ctx, characterID, req2)
	require.NoError(t, err)
	assert.NotNil(t, pos2)
	assert.Equal(t, characterID, pos2.CharacterID)
	assert.Equal(t, req2.PositionX, pos2.PositionX)
	assert.Equal(t, req2.PositionY, pos2.PositionY)
	assert.Equal(t, req2.PositionZ, pos2.PositionZ)
	assert.Equal(t, req2.Yaw, pos2.Yaw)
}

func TestMovementRepository_SavePosition_InvalidCoordinates(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 1e10,
		PositionY: -1e10,
		PositionZ: 0.0,
		Yaw:       0.0,
		VelocityX: 0.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	ctx := context.Background()
	pos, err := repo.SavePosition(ctx, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, pos)
	assert.Equal(t, req.PositionX, pos.PositionX)
	assert.Equal(t, req.PositionY, pos.PositionY)
}

func TestMovementRepository_GetPositionByCharacterID_AfterSave(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: 1.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	ctx := context.Background()
	savedPos, err := repo.SavePosition(ctx, characterID, req)
	require.NoError(t, err)
	require.NotNil(t, savedPos)

	retrievedPos, err := repo.GetPositionByCharacterID(ctx, characterID)

	require.NoError(t, err)
	require.NotNil(t, retrievedPos)
	assert.Equal(t, savedPos.CharacterID, retrievedPos.CharacterID)
	assert.Equal(t, savedPos.PositionX, retrievedPos.PositionX)
	assert.Equal(t, savedPos.PositionY, retrievedPos.PositionY)
	assert.Equal(t, savedPos.PositionZ, retrievedPos.PositionZ)
	assert.Equal(t, savedPos.Yaw, retrievedPos.Yaw)
}

func TestMovementRepository_GetPositionHistory(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: 1.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	ctx := context.Background()
	_, err := repo.SavePosition(ctx, characterID, req)
	require.NoError(t, err)

	time.Sleep(10 * time.Millisecond)

	req2 := &models.SavePositionRequest{
		PositionX: 11.5,
		PositionY: 21.3,
		PositionZ: 31.1,
		Yaw:       46.0,
		VelocityX: 2.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	_, err = repo.SavePosition(ctx, characterID, req2)
	require.NoError(t, err)

	history, err := repo.GetPositionHistory(ctx, characterID, 10)

	require.NoError(t, err)
	assert.NotNil(t, history)
	assert.GreaterOrEqual(t, len(history), 2)
}

func TestMovementRepository_GetPositionHistory_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	history, err := repo.GetPositionHistory(ctx, characterID, 10)

	require.NoError(t, err)
	assert.NotNil(t, history)
	assert.Len(t, history, 0)
}

func TestMovementRepository_GetPositionHistory_Limit(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: 1.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	ctx := context.Background()
	for i := 0; i < 5; i++ {
		_, err := repo.SavePosition(ctx, characterID, req)
		require.NoError(t, err)
		time.Sleep(10 * time.Millisecond)
	}

	history, err := repo.GetPositionHistory(ctx, characterID, 3)

	require.NoError(t, err)
	assert.NotNil(t, history)
	assert.LessOrEqual(t, len(history), 3)
}

func TestMovementRepository_GetPositionHistory_InvalidLimit(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	history, err := repo.GetPositionHistory(ctx, characterID, 0)

	require.NoError(t, err)
	assert.NotNil(t, history)
}

func TestMovementRepository_GetPositionHistory_ExceedsMaxLimit(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	history, err := repo.GetPositionHistory(ctx, characterID, 200)

	require.NoError(t, err)
	assert.NotNil(t, history)
}
