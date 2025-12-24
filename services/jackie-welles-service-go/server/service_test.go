package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/jackie-welles-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

// MockRepository is a mock implementation of Repository
type MockRepository struct {
	mock.Mock
}

func (m *MockRepository) GetJackieProfile(ctx context.Context) (*api.JackieProfileResponse, error) {
	args := m.Called(ctx)
	return args.Get(0).(*api.JackieProfileResponse), args.Error(1)
}

func (m *MockRepository) GetRelationshipStatus(ctx context.Context, playerID uuid.UUID) (*api.JackieRelationshipResponse, error) {
	args := m.Called(ctx, playerID)
	return args.Get(0).(*api.JackieRelationshipResponse), args.Error(1)
}

func (m *MockRepository) GetCurrentStatus(ctx context.Context) (*api.JackieStatusResponse, error) {
	args := m.Called(ctx)
	return args.Get(0).(*api.JackieStatusResponse), args.Error(1)
}

func (m *MockRepository) AcceptQuest(ctx context.Context, questID uuid.UUID, playerID uuid.UUID) (*api.AcceptJackieQuestOK, error) {
	args := m.Called(ctx, questID, playerID)
	return args.Get(0).(*api.AcceptJackieQuestOK), args.Error(1)
}

func (m *MockRepository) GetAvailableQuests(ctx context.Context, relationshipLevel string) (*api.GetJackieAvailableQuestsOK, error) {
	args := m.Called(ctx, relationshipLevel)
	return args.Get(0).(*api.GetJackieAvailableQuestsOK), args.Error(1)
}

func (m *MockRepository) PerformTrade(ctx context.Context, req *api.TradeRequest, playerID uuid.UUID) (*api.TradeWithJackieOK, error) {
	args := m.Called(ctx, req, playerID)
	return args.Get(0).(*api.TradeWithJackieOK), args.Error(1)
}

func (m *MockRepository) StartDialogue(ctx context.Context, req *api.DialogueStartRequest, rel *api.JackieRelationshipResponse) (*api.StartJackieDialogueOK, error) {
	args := m.Called(ctx, req, rel)
	return args.Get(0).(*api.StartJackieDialogueOK), args.Error(1)
}

func (m *MockRepository) RespondToDialogue(ctx context.Context, req *api.DialogueResponseRequest, dialogueID uuid.UUID) (*api.RespondToJackieDialogueOK, error) {
	args := m.Called(ctx, req, dialogueID)
	return args.Get(0).(*api.RespondToJackieDialogueOK), args.Error(1)
}

// MockCache is a mock implementation of Cache
type MockCache struct {
	mock.Mock
}

func (m *MockCache) GetProfile() (*api.JackieProfileResponse, bool) {
	args := m.Called()
	return args.Get(0).(*api.JackieProfileResponse), args.Bool(1)
}

func (m *MockCache) SetProfile(profile *api.JackieProfileResponse) {
	m.Called(profile)
}

func (m *MockCache) GetRelationship(key string) (*api.JackieRelationshipResponse, bool) {
	args := m.Called(key)
	return args.Get(0).(*api.JackieRelationshipResponse), args.Bool(1)
}

func (m *MockCache) SetRelationship(key string, rel *api.JackieRelationshipResponse) {
	m.Called(key, rel)
}

func (m *MockCache) GetStatus() (*api.JackieStatusResponse, bool) {
	args := m.Called()
	return args.Get(0).(*api.JackieStatusResponse), args.Bool(1)
}

func (m *MockCache) SetStatus(status *api.JackieStatusResponse) {
	m.Called(status)
}

func (m *MockCache) GetQuest(questID string) (*api.JackieQuest, bool) {
	args := m.Called(questID)
	return args.Get(0).(*api.JackieQuest), args.Bool(1)
}

func (m *MockCache) SetQuest(questID string, quest *api.JackieQuest) {
	m.Called(questID, quest)
}

func (m *MockCache) InvalidateQuestCache(questID string) {
	m.Called(questID)
}

func (m *MockCache) InvalidateRelationshipCache(key string) {
	m.Called(key)
}

func (m *MockCache) GetInventory() ([]api.JackieInventoryItem, bool) {
	args := m.Called()
	return args.Get(0).([]api.JackieInventoryItem), args.Bool(1)
}

func (m *MockCache) SetInventory(items []api.JackieInventoryItem) {
	m.Called(items)
}

func (m *MockCache) GetDialogue(dialogueID string) (*api.StartJackieDialogueOK, bool) {
	args := m.Called(dialogueID)
	return args.Get(0).(*api.StartJackieDialogueOK), args.Bool(1)
}

func (m *MockCache) SetDialogue(dialogueID string, dialogue *api.StartJackieDialogueOK) {
	m.Called(dialogueID, dialogue)
}

func (m *MockCache) InvalidateDialogueCache(dialogueID string) {
	m.Called(dialogueID)
}

func (m *MockCache) GetJSON(key string) ([]byte, bool) {
	args := m.Called(key)
	return args.Get(0).([]byte), args.Bool(1)
}

func (m *MockCache) SetJSON(key string, data []byte, ttl time.Duration) error {
	args := m.Called(key, data, ttl)
	return args.Error(0)
}

func (m *MockCache) ClearAll() {
	m.Called()
}

func (m *MockCache) Stats() map[string]int {
	args := m.Called()
	return args.Get(0).(map[string]int)
}

func TestJackieWellesService_GetJackieProfile(t *testing.T) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	service := &JackieWellesService{
		repo:  mockRepo,
		cache: mockCache,
	}

	ctx := context.Background()
	expectedProfile := &api.JackieProfileResponse{
		ID:   api.NewOptUUID(uuid.New()),
		Name: api.NewOptString("Jackie Welles"),
	}

	// Test cache miss
	mockCache.On("GetProfile").Return((*api.JackieProfileResponse)(nil), false)
	mockRepo.On("GetJackieProfile", ctx).Return(expectedProfile, nil)
	mockCache.On("SetProfile", expectedProfile)

	profile, err := service.GetJackieProfile(ctx)

	assert.NoError(t, err)
	assert.Equal(t, expectedProfile, profile)
	mockRepo.AssertExpectations(t)
	mockCache.AssertExpectations(t)
}

func TestJackieWellesService_GetRelationshipStatus(t *testing.T) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	service := &JackieWellesService{
		repo:  mockRepo,
		cache: mockCache,
	}

	ctx := context.Background()
	playerID := uuid.New()
	cacheKey := "relationship:" + playerID.String()
	expectedRel := &api.JackieRelationshipResponse{
		Level: api.NewOptString("loyal_friend"),
		Loyalty: api.NewOptInt(95),
	}

	// Test cache miss
	mockCache.On("GetRelationship", cacheKey).Return((*api.JackieRelationshipResponse)(nil), false)
	mockRepo.On("GetRelationshipStatus", ctx, playerID).Return(expectedRel, nil)
	mockCache.On("SetRelationship", cacheKey, expectedRel)

	rel, err := service.GetRelationshipStatus(ctx, playerID)

	assert.NoError(t, err)
	assert.Equal(t, expectedRel, rel)
	mockRepo.AssertExpectations(t)
	mockCache.AssertExpectations(t)
}

func TestJackieWellesService_AcceptQuest(t *testing.T) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	mockValidator := &Validator{}
	service := &JackieWellesService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
	}

	ctx := context.Background()
	questID := uuid.New()
	playerID := uuid.New()
	expectedResult := &api.AcceptJackieQuestOK{
		QuestID:   api.NewOptUUID(questID),
		AcceptedAt: api.NewOptDateTime(time.Now()),
		Status:    api.NewOptString("accepted"),
	}

	mockRepo.On("AcceptQuest", ctx, questID, playerID).Return(expectedResult, nil)
	mockCache.On("InvalidateQuestCache", questID.String())
	mockCache.On("InvalidateRelationshipCache", "relationship:"+playerID.String())

	result, err := service.AcceptQuest(ctx, questID, playerID)

	assert.NoError(t, err)
	assert.Equal(t, expectedResult, result)
	mockRepo.AssertExpectations(t)
	mockCache.AssertExpectations(t)
}

func TestJackieWellesService_PerformTrade(t *testing.T) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	mockValidator := &Validator{}
	service := &JackieWellesService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
	}

	ctx := context.Background()
	playerID := uuid.New()
	req := &api.TradeRequest{
		Items:       []string{uuid.New().String()},
		TotalAmount: api.NewOptInt(2500),
		TradeType:   api.NewOptString("buy"),
	}

	expectedResult := &api.TradeWithJackieOK{
		TransactionID: api.NewOptUUID(uuid.New()),
		Status:        api.NewOptString("completed"),
		TotalAmount:   api.NewOptInt(2500),
		Items:         req.Items,
	}

	mockRepo.On("PerformTrade", ctx, req, playerID).Return(expectedResult, nil)

	result, err := service.PerformTrade(ctx, req, playerID)

	assert.NoError(t, err)
	assert.Equal(t, expectedResult, result)
	mockRepo.AssertExpectations(t)
}

func TestJackieWellesService_StartDialogue(t *testing.T) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	service := &JackieWellesService{
		repo:  mockRepo,
		cache: mockCache,
	}

	ctx := context.Background()
	playerID := uuid.New()
	req := &api.DialogueStartRequest{
		PlayerID: api.NewOptString(playerID.String()),
		Context:  api.NewOptString("general_conversation"),
	}

	rel := &api.JackieRelationshipResponse{
		Level:   api.NewOptString("loyal_friend"),
		Loyalty: api.NewOptInt(95),
	}

	expectedResult := &api.StartJackieDialogueOK{
		DialogueID:      api.NewOptUUID(uuid.New()),
		InitialMessage:  api.NewOptString("Эй, друг! Что нового в Ночном Городе?"),
		DialogueOptions: []string{"Расскажи о себе", "Есть работа?", "Просто поболтать"},
	}

	mockRepo.On("StartDialogue", ctx, req, rel).Return(expectedResult, nil)

	result, err := service.StartDialogue(ctx, req, rel)

	assert.NoError(t, err)
	assert.Equal(t, expectedResult, result)
	mockRepo.AssertExpectations(t)
}

func BenchmarkJackieWellesService_GetJackieProfile(b *testing.B) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	service := &JackieWellesService{
		repo:  mockRepo,
		cache: mockCache,
	}

	ctx := context.Background()
	expectedProfile := &api.JackieProfileResponse{
		ID:   api.NewOptUUID(uuid.New()),
		Name: api.NewOptString("Jackie Welles"),
	}

	// Setup cache hit scenario
	mockCache.On("GetProfile").Return(expectedProfile, true)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = service.GetJackieProfile(ctx)
	}
}

func BenchmarkJackieWellesService_AcceptQuest(b *testing.B) {
	mockRepo := new(MockRepository)
	mockCache := new(MockCache)
	mockValidator := &Validator{}
	service := &JackieWellesService{
		repo:      mockRepo,
		cache:     mockCache,
		validator: mockValidator,
	}

	ctx := context.Background()
	questID := uuid.New()
	playerID := uuid.New()
	expectedResult := &api.AcceptJackieQuestOK{
		QuestID:    api.NewOptUUID(questID),
		AcceptedAt: api.NewOptDateTime(time.Now()),
		Status:     api.NewOptString("accepted"),
	}

	mockRepo.On("AcceptQuest", ctx, questID, playerID).Return(expectedResult, nil)
	mockCache.On("InvalidateQuestCache", questID.String())
	mockCache.On("InvalidateRelationshipCache", "relationship:"+playerID.String())

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = service.AcceptQuest(ctx, questID, playerID)
	}
}
