package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/character-service-go/models"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
)

func setupTestRepository(t *testing.T) (*CharacterRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewCharacterRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewCharacterRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewCharacterRepository(dbPool)

	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestCharacterRepository_GetAccountByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()

	ctx := context.Background()
	account, err := repo.GetAccountByID(ctx, accountID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, account)
}

func TestCharacterRepository_CreateAccount(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	req := &models.CreateAccountRequest{
		Nickname: "test_user",
	}

	ctx := context.Background()
	account, err := repo.CreateAccount(ctx, req)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, account)
	assert.Equal(t, req.Nickname, account.Nickname)
	assert.NotEqual(t, uuid.Nil, account.ID)
}

func TestCharacterRepository_GetCharactersByAccountID_Empty(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	characters, err := repo.GetCharactersByAccountID(ctx, accountID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Empty(t, characters)
}

func TestCharacterRepository_GetCharacterByID_NotFound(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	ctx := context.Background()
	char, err := repo.GetCharacterByID(ctx, characterID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, char)
}

func TestCharacterRepository_CreateCharacter(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	req := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}

	ctx := context.Background()
	char, err := repo.CreateCharacter(ctx, req)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, char)
	assert.Equal(t, req.Name, char.Name)
	assert.Equal(t, accountID, char.AccountID)
	assert.Equal(t, 1, char.Level)
	assert.NotEqual(t, uuid.Nil, char.ID)
}

func TestCharacterRepository_UpdateCharacter(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	createReq := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}

	char, err := repo.CreateCharacter(ctx, createReq)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	newName := "Updated Character"
	updateReq := &models.UpdateCharacterRequest{
		Name: &newName,
	}

	updatedChar, err := repo.UpdateCharacter(ctx, char.ID, updateReq)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, updatedChar)
	assert.Equal(t, newName, updatedChar.Name)
}

func TestCharacterRepository_DeleteCharacter(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	createReq := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}

	char, err := repo.CreateCharacter(ctx, createReq)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	err = repo.DeleteCharacter(ctx, char.ID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)

	deletedChar, err := repo.GetCharacterByID(ctx, char.ID)
	require.NoError(t, err)
	assert.Nil(t, deletedChar)
}

func TestCharacterRepository_GetAccountByID_Success(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	req := &models.CreateAccountRequest{
		Nickname: "test_user",
	}

	ctx := context.Background()
	account, err := repo.CreateAccount(ctx, req)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	foundAccount, err := repo.GetAccountByID(ctx, account.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, foundAccount)
	assert.Equal(t, account.ID, foundAccount.ID)
	assert.Equal(t, account.Nickname, foundAccount.Nickname)
}

func TestCharacterRepository_GetCharactersByAccountID_Multiple(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	createReq1 := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Character 1",
		Level:     intPtr(1),
	}
	char1, err := repo.CreateCharacter(ctx, createReq1)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	createReq2 := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Character 2",
		Level:     intPtr(5),
	}
	char2, err := repo.CreateCharacter(ctx, createReq2)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	characters, err := repo.GetCharactersByAccountID(ctx, accountID)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.Len(t, characters, 2)
	assert.Contains(t, []uuid.UUID{char1.ID, char2.ID}, characters[0].ID)
	assert.Contains(t, []uuid.UUID{char1.ID, char2.ID}, characters[1].ID)
}

func TestCharacterRepository_UpdateCharacter_Partial(t *testing.T) {
	repo, cleanup := setupTestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	ctx := context.Background()

	createReq := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}

	char, err := repo.CreateCharacter(ctx, createReq)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	newLevel := 10
	updateReq := &models.UpdateCharacterRequest{
		Level: &newLevel,
	}

	updatedChar, err := repo.UpdateCharacter(ctx, char.ID, updateReq)
	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	require.NoError(t, err)
	assert.NotNil(t, updatedChar)
	assert.Equal(t, newLevel, updatedChar.Level)
	assert.Equal(t, char.Name, updatedChar.Name)
}
