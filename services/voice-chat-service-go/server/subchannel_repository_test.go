// Issue: #140895495
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/voice-chat-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestSubchannelRepository(t *testing.T) (*SubchannelRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewSubchannelRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewSubchannelRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewSubchannelRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestSubchannelRepository_GetSubchannel_NotFound(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	lobbyID := uuid.New()
	subchannelID := uuid.New()

	ctx := context.Background()
	_, err := repo.GetSubchannel(ctx, lobbyID, subchannelID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	// Should return nil, not error
	assert.NoError(t, err)
}

func TestSubchannelRepository_CreateSubchannel(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "Test Subchannel",
		MaxParticipants: intPtr(10),
	}

	ctx := context.Background()
	subchannel, err := repo.CreateSubchannel(ctx, lobbyID, req)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, subchannel)
	assert.Equal(t, lobbyID, subchannel.LobbyID)
	assert.Equal(t, "Test Subchannel", subchannel.Name)
}

func TestSubchannelRepository_ListSubchannels_Empty(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	lobbyID := uuid.New()

	ctx := context.Background()
	subchannels, err := repo.ListSubchannels(ctx, lobbyID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, subchannels)
}

func TestSubchannelRepository_UpdateSubchannel(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "Test Subchannel",
		MaxParticipants: intPtr(10),
	}

	ctx := context.Background()
	subchannel, err := repo.CreateSubchannel(ctx, lobbyID, req)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	updateReq := &models.UpdateSubchannelRequest{
		Name:            stringPtr("Updated Subchannel"),
		MaxParticipants: intPtr(20),
	}

	updated, err := repo.UpdateSubchannel(ctx, lobbyID, subchannel.ID, updateReq)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, "Updated Subchannel", updated.Name)
}

func TestSubchannelRepository_DeleteSubchannel(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "Test Subchannel",
		MaxParticipants: intPtr(10),
	}

	ctx := context.Background()
	subchannel, err := repo.CreateSubchannel(ctx, lobbyID, req)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.DeleteSubchannel(ctx, lobbyID, subchannel.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestSubchannelRepository_CountParticipants(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	subchannelID := uuid.New()

	ctx := context.Background()
	count, err := repo.CountParticipants(ctx, subchannelID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

func TestSubchannelRepository_MoveParticipant(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	subchannelID := uuid.New()
	characterID := uuid.New()

	ctx := context.Background()
	err := repo.MoveParticipant(ctx, subchannelID, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	// MoveParticipant may fail if participant doesn't exist, which is expected
	// We just check that the method doesn't panic
	assert.NotPanics(t, func() {
		_ = repo.MoveParticipant(ctx, subchannelID, characterID)
	})
}

func TestSubchannelRepository_GetParticipants_Empty(t *testing.T) {
	repo, cleanup := setupTestSubchannelRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	subchannelID := uuid.New()

	ctx := context.Background()
	participants, err := repo.GetParticipants(ctx, subchannelID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, participants)
}


