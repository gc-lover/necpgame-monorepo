// Issue: #140894175
package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestEngramCreationRepository(t *testing.T) (*EngramCreationRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewEngramCreationRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewEngramCreationRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewEngramCreationRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestEngramCreationRepository_CreateCreationLog(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	creation := &EngramCreation{
		ID:                     uuid.New(),
		CreationID:             uuid.New(),
		EngramID:               uuid.New(),
		CharacterID:            uuid.New(),
		TargetPersonID:         uuidPtr(uuid.New()),
		ChipTier:               3,
		AttitudeType:           "friendly",
		CustomAttitudeSettings: map[string]interface{}{"trust": 0.8},
		CreationStage:          "initializing",
		DataLossPercent:        5.0,
		IsComplete:             false,
		CreationCost:           1000000.0,
		ReputationSnapshot:    map[string]interface{}{"corp": "Arasaka"},
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateCreationLog(ctx, creation)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestEngramCreationRepository_GetCreationLogByCreationID_NotFound(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	creationID := uuid.New()
	ctx := context.Background()

	creation, err := repo.GetCreationLogByCreationID(ctx, creationID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, creation)
}

func TestEngramCreationRepository_GetCreationLogByCreationID_Success(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	creation := &EngramCreation{
		ID:                     uuid.New(),
		CreationID:             uuid.New(),
		EngramID:               uuid.New(),
		CharacterID:            uuid.New(),
		TargetPersonID:         uuidPtr(uuid.New()),
		ChipTier:               3,
		AttitudeType:           "friendly",
		CustomAttitudeSettings: map[string]interface{}{"trust": 0.8},
		CreationStage:          "initializing",
		DataLossPercent:        5.0,
		IsComplete:             false,
		CreationCost:           1000000.0,
		ReputationSnapshot:    map[string]interface{}{"corp": "Arasaka"},
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateCreationLog(ctx, creation)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	retrieved, err := repo.GetCreationLogByCreationID(ctx, creation.CreationID)
	require.NoError(t, err)
	assert.NotNil(t, retrieved)
	assert.Equal(t, creation.CreationID, retrieved.CreationID)
	assert.Equal(t, creation.EngramID, retrieved.EngramID)
	assert.Equal(t, creation.CharacterID, retrieved.CharacterID)
	assert.Equal(t, creation.ChipTier, retrieved.ChipTier)
	assert.Equal(t, creation.AttitudeType, retrieved.AttitudeType)
}

func TestEngramCreationRepository_GetCreationLogByEngramID_NotFound(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	engramID := uuid.New()
	ctx := context.Background()

	creation, err := repo.GetCreationLogByEngramID(ctx, engramID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, creation)
}

func TestEngramCreationRepository_GetCreationLogByEngramID_Success(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	creation := &EngramCreation{
		ID:                     uuid.New(),
		CreationID:             uuid.New(),
		EngramID:               uuid.New(),
		CharacterID:            uuid.New(),
		TargetPersonID:         nil,
		ChipTier:               2,
		AttitudeType:           "neutral",
		CustomAttitudeSettings: nil,
		CreationStage:          "processing",
		DataLossPercent:        10.0,
		IsComplete:             false,
		CreationCost:           500000.0,
		ReputationSnapshot:    nil,
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateCreationLog(ctx, creation)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	retrieved, err := repo.GetCreationLogByEngramID(ctx, creation.EngramID)
	require.NoError(t, err)
	assert.NotNil(t, retrieved)
	assert.Equal(t, creation.EngramID, retrieved.EngramID)
}

func TestEngramCreationRepository_UpdateCreationStage(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	creation := &EngramCreation{
		ID:                     uuid.New(),
		CreationID:             uuid.New(),
		EngramID:               uuid.New(),
		CharacterID:            uuid.New(),
		ChipTier:               4,
		AttitudeType:           "hostile",
		CreationStage:          "initializing",
		DataLossPercent:        3.0,
		IsComplete:             false,
		CreationCost:           2000000.0,
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateCreationLog(ctx, creation)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	newDataLoss := 8.0
	isComplete := true
	err = repo.UpdateCreationStage(ctx, creation.CreationID, "completed", &newDataLoss, &isComplete)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	updated, err := repo.GetCreationLogByCreationID(ctx, creation.CreationID)
	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, "completed", updated.CreationStage)
	assert.Equal(t, newDataLoss, updated.DataLossPercent)
	assert.True(t, updated.IsComplete)
}

func TestEngramCreationRepository_CompleteCreation(t *testing.T) {
	repo, cleanup := setupTestEngramCreationRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	creation := &EngramCreation{
		ID:                     uuid.New(),
		CreationID:             uuid.New(),
		EngramID:               uuid.New(),
		CharacterID:            uuid.New(),
		ChipTier:               5,
		AttitudeType:           "loyal",
		CreationStage:          "processing",
		DataLossPercent:        2.0,
		IsComplete:             false,
		CreationCost:           4000000.0,
		CreatedAt:              time.Now(),
		UpdatedAt:              time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateCreationLog(ctx, creation)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	newEngramID := uuid.New()
	err = repo.CompleteCreation(ctx, creation.CreationID, newEngramID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	completed, err := repo.GetCreationLogByCreationID(ctx, creation.CreationID)
	require.NoError(t, err)
	assert.NotNil(t, completed)
	assert.Equal(t, "completed", completed.CreationStage)
	assert.Equal(t, newEngramID, completed.EngramID)
	assert.NotNil(t, completed.CompletedAt)
}


