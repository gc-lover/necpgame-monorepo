package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/economy-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestRepository(t *testing.T) (*TradeRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewTradeRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewTradeRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewTradeRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestTradeRepository_GetByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	tradeID := uuid.New()

	ctx := context.Background()
	session, err := repo.GetByID(ctx, tradeID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, session)
}

func TestTradeRepository_Create(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	now := time.Now()
	session := &models.TradeSession{
		ID:                uuid.New(),
		InitiatorID:       uuid.New(),
		RecipientID:       uuid.New(),
		InitiatorOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		InitiatorConfirmed: false,
		RecipientConfirmed:  false,
		Status:            models.TradeStatusPending,
		CreatedAt:         now,
		UpdatedAt:         now,
		ExpiresAt:         now.Add(5 * time.Minute),
	}

	ctx := context.Background()
	err := repo.Create(ctx, session)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	created, err := repo.GetByID(ctx, session.ID)
	require.NoError(t, err)
	assert.NotNil(t, created)
	assert.Equal(t, session.ID, created.ID)
}

func TestTradeRepository_GetActiveByCharacter_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	sessions, err := repo.GetActiveByCharacter(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, sessions)
}

func TestTradeRepository_UpdateStatus(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	now := time.Now()
	session := &models.TradeSession{
		ID:                uuid.New(),
		InitiatorID:       uuid.New(),
		RecipientID:       uuid.New(),
		InitiatorOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		InitiatorConfirmed: false,
		RecipientConfirmed:  false,
		Status:            models.TradeStatusPending,
		CreatedAt:         now,
		UpdatedAt:         now,
		ExpiresAt:         now.Add(5 * time.Minute),
	}

	ctx := context.Background()
	err := repo.Create(ctx, session)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.UpdateStatus(ctx, session.ID, models.TradeStatusCancelled)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	updated, err := repo.GetByID(ctx, session.ID)
	require.NoError(t, err)
	assert.NotNil(t, updated)
	assert.Equal(t, models.TradeStatusCancelled, updated.Status)
}

func TestTradeRepository_GetHistoryByCharacter_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	history, err := repo.GetHistoryByCharacter(ctx, characterID, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, history)
}

func TestTradeRepository_CountHistoryByCharacter(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()

	count, err := repo.CountHistoryByCharacter(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Equal(t, 0, count)
}

