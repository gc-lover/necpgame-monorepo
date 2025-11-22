package server

import (
	"context"
	"encoding/json"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockCharacterRepository struct {
	mock.Mock
}

func (m *mockCharacterRepository) GetAccountByID(ctx context.Context, accountID uuid.UUID) (*models.PlayerAccount, error) {
	args := m.Called(ctx, accountID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerAccount), args.Error(1)
}

func (m *mockCharacterRepository) CreateAccount(ctx context.Context, req *models.CreateAccountRequest) (*models.PlayerAccount, error) {
	args := m.Called(ctx, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerAccount), args.Error(1)
}

func (m *mockCharacterRepository) GetCharacterByID(ctx context.Context, characterID uuid.UUID) (*models.Character, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Character), args.Error(1)
}

func (m *mockCharacterRepository) GetCharactersByAccountID(ctx context.Context, accountID uuid.UUID) ([]models.Character, error) {
	args := m.Called(ctx, accountID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.Character), args.Error(1)
}

func (m *mockCharacterRepository) CreateCharacter(ctx context.Context, req *models.CreateCharacterRequest) (*models.Character, error) {
	args := m.Called(ctx, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Character), args.Error(1)
}

func (m *mockCharacterRepository) UpdateCharacter(ctx context.Context, characterID uuid.UUID, req *models.UpdateCharacterRequest) (*models.Character, error) {
	args := m.Called(ctx, characterID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Character), args.Error(1)
}

func (m *mockCharacterRepository) DeleteCharacter(ctx context.Context, characterID uuid.UUID) error {
	args := m.Called(ctx, characterID)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*CharacterService, *mockCharacterRepository, func()) {
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return nil, nil, nil
	}
	redisClient := redis.NewClient(redisOpts)

	mockRepo := new(mockCharacterRepository)
	service := &CharacterService{
		repo:       mockRepo,
		cache:      redisClient,
		logger:     GetLogger(),
		keycloakURL: "http://localhost:8080",
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, cleanup
}

func TestCharacterService_GetAccount_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	account := &models.PlayerAccount{
		ID:       accountID,
		Nickname: "test_user",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("GetAccountByID", mock.Anything, accountID).Return(account, nil)

	ctx := context.Background()
	result, err := service.GetAccount(ctx, accountID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, account.ID, result.ID)
	assert.Equal(t, account.Nickname, result.Nickname)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_GetAccount_Cache(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	account := &models.PlayerAccount{
		ID:       accountID,
		Nickname: "test_user",
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	accountJSON, _ := json.Marshal(account)

	ctx := context.Background()
	service.cache.Set(ctx, "account:"+accountID.String(), accountJSON, 5*time.Minute)

	cachedAccount, err := service.GetAccount(ctx, accountID)

	require.NoError(t, err)
	assert.NotNil(t, cachedAccount)
	assert.Equal(t, account.ID, cachedAccount.ID)
	mockRepo.AssertNotCalled(t, "GetAccountByID", mock.Anything, accountID)
}

func TestCharacterService_CreateAccount_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	req := &models.CreateAccountRequest{
		Nickname: "test_user",
	}
	account := &models.PlayerAccount{
		ID:       accountID,
		Nickname: req.Nickname,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("CreateAccount", mock.Anything, req).Return(account, nil)

	ctx := context.Background()
	result, err := service.CreateAccount(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, account.ID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_GetCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	accountID := uuid.New()
	char := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      "Test Character",
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(char, nil)

	ctx := context.Background()
	result, err := service.GetCharacter(ctx, characterID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, char.ID, result.ID)
	assert.Equal(t, char.Name, result.Name)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_GetCharacter_Cache(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	accountID := uuid.New()
	char := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      "Test Character",
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	charJSON, _ := json.Marshal(char)

	ctx := context.Background()
	service.cache.Set(ctx, "character:"+characterID.String(), charJSON, 5*time.Minute)

	cachedChar, err := service.GetCharacter(ctx, characterID)

	require.NoError(t, err)
	assert.NotNil(t, cachedChar)
	assert.Equal(t, char.ID, cachedChar.ID)
	mockRepo.AssertNotCalled(t, "GetCharacterByID", mock.Anything, characterID)
}

func TestCharacterService_GetCharactersByAccountID_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characters := []models.Character{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Character 2",
			Level:     5,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(characters, nil)

	ctx := context.Background()
	result, err := service.GetCharactersByAccountID(ctx, accountID)

	require.NoError(t, err)
	assert.Len(t, result, 2)
	assert.Equal(t, characters[0].Name, result[0].Name)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_GetCharactersByAccountID_Cache(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characters := []models.Character{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	charactersJSON, _ := json.Marshal(characters)

	ctx := context.Background()
	service.cache.Set(ctx, "characters:account:"+accountID.String(), charactersJSON, 5*time.Minute)

	cachedCharacters, err := service.GetCharactersByAccountID(ctx, accountID)

	require.NoError(t, err)
	assert.Len(t, cachedCharacters, 1)
	mockRepo.AssertNotCalled(t, "GetCharactersByAccountID", mock.Anything, accountID)
}

func TestCharacterService_CreateCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID := uuid.New()
	req := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}
	char := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      req.Name,
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("CreateCharacter", mock.Anything, req).Return(char, nil)

	ctx := context.Background()
	result, err := service.CreateCharacter(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, char.ID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_UpdateCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	accountID := uuid.New()
	newName := "Updated Character"
	req := &models.UpdateCharacterRequest{
		Name: &newName,
	}
	char := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      newName,
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("UpdateCharacter", mock.Anything, characterID, req).Return(char, nil)

	ctx := context.Background()
	result, err := service.UpdateCharacter(ctx, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, newName, result.Name)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_DeleteCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	accountID := uuid.New()
	char := &models.Character{
		ID:        characterID,
		AccountID: accountID,
		Name:      "Test Character",
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(char, nil)
	mockRepo.On("DeleteCharacter", mock.Anything, characterID).Return(nil)

	ctx := context.Background()
	err := service.DeleteCharacter(ctx, characterID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_DeleteCharacter_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(nil, nil)
	mockRepo.On("DeleteCharacter", mock.Anything, characterID).Return(nil)

	ctx := context.Background()
	err := service.DeleteCharacter(ctx, characterID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_ValidateCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	char := &models.Character{
		ID:        characterID,
		AccountID: uuid.New(),
		Name:      "Test Character",
		Level:     1,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(char, nil)

	ctx := context.Background()
	valid, err := service.ValidateCharacter(ctx, characterID)

	require.NoError(t, err)
	assert.True(t, valid)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_ValidateCharacter_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()

	mockRepo.On("GetCharacterByID", mock.Anything, characterID).Return(nil, nil)

	ctx := context.Background()
	valid, err := service.ValidateCharacter(ctx, characterID)

	require.NoError(t, err)
	assert.False(t, valid)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_SwitchCharacter_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID1 := uuid.New()
	characterID2 := uuid.New()
	characters := []models.Character{
		{
			ID:        characterID1,
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
		{
			ID:        characterID2,
			AccountID: accountID,
			Name:      "Character 2",
			Level:     5,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(characters, nil)

	ctx := context.Background()
	result, err := service.SwitchCharacter(ctx, accountID, characterID2)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.True(t, result.Success)
	assert.NotNil(t, result.CurrentCharacter)
	assert.Equal(t, characterID2, result.CurrentCharacter.ID)
	assert.NotNil(t, result.PreviousCharacterID)
	assert.Equal(t, characterID1, *result.PreviousCharacterID)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_SwitchCharacter_NotFound(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	characterID := uuid.New()
	characters := []models.Character{
		{
			ID:        uuid.New(),
			AccountID: accountID,
			Name:      "Character 1",
			Level:     1,
			CreatedAt: time.Now(),
			UpdatedAt: time.Now(),
		},
	}

	mockRepo.On("GetCharactersByAccountID", mock.Anything, accountID).Return(characters, nil)

	ctx := context.Background()
	result, err := service.SwitchCharacter(ctx, accountID, characterID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.False(t, result.Success)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_GetAccount_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	expectedErr := errors.New("database error")

	mockRepo.On("GetAccountByID", mock.Anything, accountID).Return(nil, expectedErr)

	ctx := context.Background()
	account, err := service.GetAccount(ctx, accountID)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, account)
	mockRepo.AssertExpectations(t)
}

func TestCharacterService_CreateCharacter_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	accountID := uuid.New()
	req := &models.CreateCharacterRequest{
		AccountID: accountID,
		Name:      "Test Character",
		Level:     intPtr(1),
	}
	expectedErr := errors.New("database error")

	mockRepo.On("CreateCharacter", mock.Anything, req).Return(nil, expectedErr)

	ctx := context.Background()
	char, err := service.CreateCharacter(ctx, req)

	assert.Error(t, err)
	assert.Equal(t, expectedErr, err)
	assert.Nil(t, char)
	mockRepo.AssertExpectations(t)
}

