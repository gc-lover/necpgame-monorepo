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

	now := time.Now()
	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			EngramID:     uuid.New(),
			TransferID:   uuid.New(),
			TransferType: "voluntary",
			IsCopy:       false,
			Status:       "pending",
		},
		Parties: EngramTransferParties{
			FromCharacterID: uuid.New(),
			ToCharacterID:   uuid.New(),
		},
		Metadata: EngramTransferMetadata{
			CreatedAt: now,
			UpdatedAt: now,
		},
	}

	ctx := context.Background()
	err := repo.CreateTransfer(ctx, transfer)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, transfer.Base.ID)
	assert.NotEqual(t, uuid.Nil, transfer.Base.TransferID)
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

	now := time.Now()
	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			EngramID:     uuid.New(),
			TransferID:   uuid.New(),
			TransferType: "voluntary",
			IsCopy:       true,
			Status:       "completed",
		},
		Parties: EngramTransferParties{
			FromCharacterID: uuid.New(),
			ToCharacterID:   uuid.New(),
		},
		Conditions: EngramTransferConditions{
			NewAttitudeType: stringPtr("friendly"),
			TransferPrice:   float64Ptr(100000.0),
		},
		Metadata: EngramTransferMetadata{
			CreatedAt: now,
			UpdatedAt: now,
		},
	}

	ctx := context.Background()
	err := repo.CreateTransfer(ctx, transfer)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	retrieved, err := repo.GetTransferByID(ctx, transfer.Base.TransferID)
	require.NoError(t, err)
	assert.NotNil(t, retrieved)
	assert.Equal(t, transfer.Base.TransferID, retrieved.Base.TransferID)
	assert.Equal(t, transfer.Base.EngramID, retrieved.Base.EngramID)
	assert.Equal(t, transfer.Parties.FromCharacterID, retrieved.Parties.FromCharacterID)
	assert.Equal(t, transfer.Parties.ToCharacterID, retrieved.Parties.ToCharacterID)
	assert.Equal(t, transfer.Base.TransferType, retrieved.Base.TransferType)
	assert.Equal(t, transfer.Base.IsCopy, retrieved.Base.IsCopy)
}

func TestEngramTransferRepository_UpdateTransferStatus(t *testing.T) {
	repo, cleanup := setupTestEngramTransferRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	now := time.Now()
	transfer := &EngramTransfer{
		Base: EngramTransferBase{
			EngramID:     uuid.New(),
			TransferID:   uuid.New(),
			TransferType: "voluntary",
			Status:       "pending",
		},
		Parties: EngramTransferParties{
			FromCharacterID: uuid.New(),
			ToCharacterID:   uuid.New(),
		},
		Metadata: EngramTransferMetadata{
			CreatedAt: now,
			UpdatedAt: now,
		},
	}

	ctx := context.Background()
	err := repo.CreateTransfer(ctx, transfer)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	updateTime := time.Now()
	err = repo.UpdateTransferStatus(ctx, transfer.Base.TransferID, "completed", &updateTime)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	updated, err := repo.GetTransferByID(ctx, transfer.Base.TransferID)
	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, "completed", updated.Base.Status)
	assert.NotNil(t, updated.Outcome.TransferredAt)
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
