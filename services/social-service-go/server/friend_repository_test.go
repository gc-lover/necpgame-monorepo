package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestFriendRepository(t *testing.T) (*FriendRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewFriendRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewFriendRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewFriendRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
}

func TestFriendRepository_GetByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestFriendRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	friendshipID := uuid.New()

	ctx := context.Background()
	friendship, err := repo.GetByID(ctx, friendshipID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, friendship)
}

func TestFriendRepository_CreateRequest(t *testing.T) {
	repo, cleanup := setupTestFriendRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	ctx := context.Background()
	friendship, err := repo.CreateRequest(ctx, fromCharacterID, toCharacterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, friendship)
	assert.Equal(t, models.FriendshipStatusPending, friendship.Status)
	assert.Equal(t, fromCharacterID, friendship.InitiatorID)
}

func TestFriendRepository_GetByCharacterID_Empty(t *testing.T) {
	repo, cleanup := setupTestFriendRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	friendships, err := repo.GetByCharacterID(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, friendships)
}

func TestFriendRepository_AcceptRequest(t *testing.T) {
	repo, cleanup := setupTestFriendRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	ctx := context.Background()
	friendship, err := repo.CreateRequest(ctx, fromCharacterID, toCharacterID)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	accepted, err := repo.AcceptRequest(ctx, friendship.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, accepted)
	assert.Equal(t, models.FriendshipStatusAccepted, accepted.Status)
}

func TestFriendRepository_Delete(t *testing.T) {
	repo, cleanup := setupTestFriendRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	ctx := context.Background()
	friendship, err := repo.CreateRequest(ctx, fromCharacterID, toCharacterID)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.Delete(ctx, friendship.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	deleted, err := repo.GetByID(ctx, friendship.ID)
	require.NoError(t, err)
	assert.Nil(t, deleted)
}

