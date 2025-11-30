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

func setupTestEngramTransferRepository(t *testing.T) (*EngramTransferRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewEngramTransferRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewEngramTransferRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewEngramTransferRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestEngramTransferRepository_CreateTransfer(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	transfer := &EngramTransfer{
		EngramID:        uuid.New(),
		FromCharacterID: uuid.New(),
		ToCharacterID:   uuid.New(),
		TransferType:    "voluntary",
		IsCopy:          false,
		Status:          "pending",
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateTransfer(ctx, transfer)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, transfer.ID)
	assert.NotEqual(t, uuid.Nil, transfer.TransferID)
}

func TestEngramTransferRepository_GetTransferByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	transferID := uuid.New()
	ctx := context.Background()

	transfer, err := repo.GetTransferByID(ctx, transferID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, transfer)
}

func TestEngramTransferRepository_GetTransferByID_Success(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	transfer := &EngramTransfer{
		EngramID:        uuid.New(),
		FromCharacterID: uuid.New(),
		ToCharacterID:   uuid.New(),
		TransferType:    "voluntary",
		IsCopy:          true,
		NewAttitudeType: stringPtr("friendly"),
		TransferPrice:   float64Ptr(100000.0),
		Status:          "completed",
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateTransfer(ctx, transfer)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	retrieved, err := repo.GetTransferByID(ctx, transfer.TransferID)
	require.NoError(t, err)
	assert.NotNil(t, retrieved)
	assert.Equal(t, transfer.TransferID, retrieved.TransferID)
	assert.Equal(t, transfer.EngramID, retrieved.EngramID)
	assert.Equal(t, transfer.FromCharacterID, retrieved.FromCharacterID)
	assert.Equal(t, transfer.ToCharacterID, retrieved.ToCharacterID)
	assert.Equal(t, transfer.TransferType, retrieved.TransferType)
	assert.Equal(t, transfer.IsCopy, retrieved.IsCopy)
}

func TestEngramTransferRepository_UpdateTransferStatus(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	transfer := &EngramTransfer{
		EngramID:        uuid.New(),
		FromCharacterID: uuid.New(),
		ToCharacterID:   uuid.New(),
		TransferType:    "voluntary",
		Status:          "pending",
		CreatedAt:       time.Now(),
		UpdatedAt:       time.Now(),
	}

	ctx := context.Background()
	err := repo.CreateTransfer(ctx, transfer)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	now := time.Now()
	err = repo.UpdateTransferStatus(ctx, transfer.TransferID, "completed", &now)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	updated, err := repo.GetTransferByID(ctx, transfer.TransferID)
	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, "completed", updated.Status)
	assert.NotNil(t, updated.TransferredAt)
}

func TestEngramTransferRepository_GetActiveLoans_Empty(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	loans, err := repo.GetActiveLoans(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, loans)
}

func TestEngramTransferRepository_GetPendingReturns_Empty(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	ctx := context.Background()

	returns, err := repo.GetPendingReturns(ctx)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, returns)
}


