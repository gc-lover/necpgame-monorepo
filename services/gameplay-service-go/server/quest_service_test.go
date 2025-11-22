package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockQuestRepository struct {
	mock.Mock
}

func (m *mockQuestRepository) CreateQuestInstance(ctx context.Context, instance *models.QuestInstance) error {
	args := m.Called(ctx, instance)
	return args.Error(0)
}

func (m *mockQuestRepository) GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error) {
	args := m.Called(ctx, instanceID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.QuestInstance), args.Error(1)
}

func (m *mockQuestRepository) GetQuestInstanceByCharacterAndQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error) {
	args := m.Called(ctx, characterID, questID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.QuestInstance), args.Error(1)
}

func (m *mockQuestRepository) UpdateQuestInstance(ctx context.Context, instance *models.QuestInstance) error {
	args := m.Called(ctx, instance)
	return args.Error(0)
}

func (m *mockQuestRepository) ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) ([]models.QuestInstance, error) {
	args := m.Called(ctx, characterID, status, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.QuestInstance), args.Error(1)
}

func (m *mockQuestRepository) CountQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus) (int, error) {
	args := m.Called(ctx, characterID, status)
	return args.Int(0), args.Error(1)
}

func (m *mockQuestRepository) CreateDialogueState(ctx context.Context, dialogueState *models.DialogueState) error {
	args := m.Called(ctx, dialogueState)
	return args.Error(0)
}

func (m *mockQuestRepository) UpdateDialogueState(ctx context.Context, dialogueState *models.DialogueState) error {
	args := m.Called(ctx, dialogueState)
	return args.Error(0)
}

func (m *mockQuestRepository) GetDialogueState(ctx context.Context, questInstanceID uuid.UUID) (*models.DialogueState, error) {
	args := m.Called(ctx, questInstanceID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.DialogueState), args.Error(1)
}

func (m *mockQuestRepository) CreateSkillCheckResult(ctx context.Context, result *models.SkillCheckResult) error {
	args := m.Called(ctx, result)
	return args.Error(0)
}

type mockProgressionRepositoryForQuest struct {
	mock.Mock
}

func (m *mockProgressionRepositoryForQuest) GetSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string) (*models.SkillExperience, error) {
	args := m.Called(ctx, characterID, skillID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.SkillExperience), args.Error(1)
}

func setupTestQuestService(t *testing.T) (*QuestService, *mockQuestRepository, *mockProgressionRepositoryForQuest, *mockEventBus, func()) {
	mockRepo := new(mockQuestRepository)
	mockProgressionRepo := new(mockProgressionRepositoryForQuest)
	mockEventBus := new(mockEventBus)

	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})
	redisClient.FlushDB(context.Background())

	service := &QuestService{
		repo:            mockRepo,
		progressionRepo: mockProgressionRepo,
		cache:           redisClient,
		logger:          GetLogger(),
		eventBus:        mockEventBus,
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, mockProgressionRepo, mockEventBus, cleanup
}

func TestQuestService_StartQuest_Success(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	questID := "quest_001"

	mockRepo.On("GetQuestInstanceByCharacterAndQuest", context.Background(), characterID, questID).Return(nil, nil)
	mockRepo.On("CreateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(nil)
	mockRepo.On("CreateDialogueState", context.Background(), mock.AnythingOfType("*models.DialogueState")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "quest:started", mock.Anything).Return(nil)

	result, err := service.StartQuest(context.Background(), characterID, questID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, questID, result.QuestID)
	assert.Equal(t, models.QuestStatusInProgress, result.Status)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestQuestService_StartQuest_AlreadyStarted(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	questID := "quest_001"
	existingInstance := &models.QuestInstance{
		ID:          uuid.New(),
		CharacterID: characterID,
		QuestID:     questID,
		Status:      models.QuestStatusInProgress,
	}

	mockRepo.On("GetQuestInstanceByCharacterAndQuest", context.Background(), characterID, questID).Return(existingInstance, nil)

	result, err := service.StartQuest(context.Background(), characterID, questID)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "already started")
	mockRepo.AssertExpectations(t)
}

func TestQuestService_GetQuestInstance_Success(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	instanceID := uuid.New()
	expectedInstance := &models.QuestInstance{
		ID:          instanceID,
		CharacterID: uuid.New(),
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		CurrentNode: "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
		StartedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), instanceID).Return(expectedInstance, nil)

	result, err := service.GetQuestInstance(context.Background(), instanceID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, instanceID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_UpdateDialogue_Success(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	nodeID := "node_001"
	choiceID := "choice_001"
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		CurrentNode: "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
		StartedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("GetDialogueState", context.Background(), questInstanceID).Return(nil, nil)
	mockRepo.On("CreateDialogueState", context.Background(), mock.AnythingOfType("*models.DialogueState")).Return(nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(nil)

	err := service.UpdateDialogue(context.Background(), questInstanceID, characterID, nodeID, &choiceID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_PerformSkillCheck_Success(t *testing.T) {
	service, mockRepo, mockProgressionRepo, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	skillID := "combat"
	requiredLevel := 5
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	skillExp := &models.SkillExperience{
		ID:          uuid.New(),
		CharacterID: characterID,
		SkillID:     skillID,
		Level:       10,
		Experience:  1000,
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockProgressionRepo.On("GetSkillExperience", context.Background(), characterID, skillID).Return(skillExp, nil)
	mockRepo.On("CreateSkillCheckResult", context.Background(), mock.AnythingOfType("*models.SkillCheckResult")).Return(nil)

	passed, err := service.PerformSkillCheck(context.Background(), questInstanceID, characterID, skillID, requiredLevel)

	assert.NoError(t, err)
	assert.True(t, passed)
	mockRepo.AssertExpectations(t)
	mockProgressionRepo.AssertExpectations(t)
}

func TestQuestService_PerformSkillCheck_Failed(t *testing.T) {
	service, mockRepo, mockProgressionRepo, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	skillID := "combat"
	requiredLevel := 10
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	skillExp := &models.SkillExperience{
		ID:          uuid.New(),
		CharacterID: characterID,
		SkillID:     skillID,
		Level:       5,
		Experience:  500,
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockProgressionRepo.On("GetSkillExperience", context.Background(), characterID, skillID).Return(skillExp, nil)
	mockRepo.On("CreateSkillCheckResult", context.Background(), mock.AnythingOfType("*models.SkillCheckResult")).Return(nil)

	passed, err := service.PerformSkillCheck(context.Background(), questInstanceID, characterID, skillID, requiredLevel)

	assert.NoError(t, err)
	assert.False(t, passed)
	mockRepo.AssertExpectations(t)
	mockProgressionRepo.AssertExpectations(t)
}

func TestQuestService_CompleteObjective_Success(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	objectiveID := "objective_001"
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		Objectives:  make(map[string]interface{}),
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "quest:objective-completed", mock.Anything).Return(nil)

	err := service.CompleteObjective(context.Background(), questInstanceID, characterID, objectiveID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestQuestService_CompleteQuest_Success(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "quest:completed", mock.Anything).Return(nil)

	err := service.CompleteQuest(context.Background(), questInstanceID, characterID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestQuestService_FailQuest_Success(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "quest:failed", mock.Anything).Return(nil)

	err := service.FailQuest(context.Background(), questInstanceID, characterID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestQuestService_ListQuestInstances_Success(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	instances := []models.QuestInstance{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			QuestID:     "quest_001",
			Status:      models.QuestStatusInProgress,
			StartedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockRepo.On("ListQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil), 10, 0).Return(instances, nil)
	mockRepo.On("CountQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil)).Return(1, nil)

	result, err := service.ListQuestInstances(context.Background(), characterID, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Quests, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_StartQuest_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	questID := "quest_001"

	mockRepo.On("GetQuestInstanceByCharacterAndQuest", context.Background(), characterID, questID).Return(nil, assert.AnError)

	result, err := service.StartQuest(context.Background(), characterID, questID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_StartQuest_CreateError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	questID := "quest_001"

	mockRepo.On("GetQuestInstanceByCharacterAndQuest", context.Background(), characterID, questID).Return(nil, nil)
	mockRepo.On("CreateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(assert.AnError)

	result, err := service.StartQuest(context.Background(), characterID, questID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_GetQuestInstance_NotFound(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	instanceID := uuid.New()

	mockRepo.On("GetQuestInstance", context.Background(), instanceID).Return(nil, nil)

	result, err := service.GetQuestInstance(context.Background(), instanceID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_GetQuestInstance_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	instanceID := uuid.New()

	mockRepo.On("GetQuestInstance", context.Background(), instanceID).Return(nil, assert.AnError)

	result, err := service.GetQuestInstance(context.Background(), instanceID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_UpdateDialogue_NotFound(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	nodeID := "node_001"
	choiceID := "choice_001"

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, nil)

	err := service.UpdateDialogue(context.Background(), questInstanceID, characterID, nodeID, &choiceID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not found")
	mockRepo.AssertExpectations(t)
}

func TestQuestService_UpdateDialogue_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	nodeID := "node_001"
	choiceID := "choice_001"
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		CurrentNode: "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
		StartedAt:    time.Now(),
		UpdatedAt:    time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("GetDialogueState", context.Background(), questInstanceID).Return(nil, assert.AnError)

	err := service.UpdateDialogue(context.Background(), questInstanceID, characterID, nodeID, &choiceID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_PerformSkillCheck_NotFound(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	skillID := "combat"
	requiredLevel := 5

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, nil)

	passed, err := service.PerformSkillCheck(context.Background(), questInstanceID, characterID, skillID, requiredLevel)

	assert.Error(t, err)
	assert.False(t, passed)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_PerformSkillCheck_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	skillID := "combat"
	requiredLevel := 5

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, assert.AnError)

	passed, err := service.PerformSkillCheck(context.Background(), questInstanceID, characterID, skillID, requiredLevel)

	assert.Error(t, err)
	assert.False(t, passed)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_PerformSkillCheck_SkillNotFound(t *testing.T) {
	service, mockRepo, mockProgressionRepo, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	skillID := "combat"
	requiredLevel := 5
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockProgressionRepo.On("GetSkillExperience", context.Background(), characterID, skillID).Return(nil, nil)
	mockRepo.On("CreateSkillCheckResult", context.Background(), mock.AnythingOfType("*models.SkillCheckResult")).Return(nil)

	passed, err := service.PerformSkillCheck(context.Background(), questInstanceID, characterID, skillID, requiredLevel)

	assert.NoError(t, err)
	assert.False(t, passed)
	mockRepo.AssertExpectations(t)
	mockProgressionRepo.AssertExpectations(t)
}

func TestQuestService_CompleteObjective_NotFound(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	objectiveID := "objective_001"

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, nil)

	err := service.CompleteObjective(context.Background(), questInstanceID, characterID, objectiveID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not found")
	mockRepo.AssertExpectations(t)
}

func TestQuestService_CompleteObjective_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	objectiveID := "objective_001"

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, assert.AnError)

	err := service.CompleteObjective(context.Background(), questInstanceID, characterID, objectiveID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_CompleteObjective_UpdateError(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	objectiveID := "objective_001"
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		Objectives:  make(map[string]interface{}),
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(assert.AnError)

	err := service.CompleteObjective(context.Background(), questInstanceID, characterID, objectiveID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertNotCalled(t, "PublishEvent", context.Background(), "quest:objective-completed", mock.Anything)
}

func TestQuestService_CompleteQuest_NotFound(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, nil)

	err := service.CompleteQuest(context.Background(), questInstanceID, characterID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not found")
	mockRepo.AssertExpectations(t)
}

func TestQuestService_CompleteQuest_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, assert.AnError)

	err := service.CompleteQuest(context.Background(), questInstanceID, characterID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_CompleteQuest_UpdateError(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(assert.AnError)

	err := service.CompleteQuest(context.Background(), questInstanceID, characterID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertNotCalled(t, "PublishEvent", context.Background(), "quest:completed", mock.Anything)
}

func TestQuestService_FailQuest_NotFound(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, nil)

	err := service.FailQuest(context.Background(), questInstanceID, characterID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not found")
	mockRepo.AssertExpectations(t)
}

func TestQuestService_FailQuest_DatabaseError(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(nil, assert.AnError)

	err := service.FailQuest(context.Background(), questInstanceID, characterID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_FailQuest_UpdateError(t *testing.T) {
	service, mockRepo, _, mockEventBus, cleanup := setupTestQuestService(t)
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	instance := &models.QuestInstance{
		ID:          questInstanceID,
		CharacterID: characterID,
		QuestID:     "quest_001",
		Status:      models.QuestStatusInProgress,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetQuestInstance", context.Background(), questInstanceID).Return(instance, nil)
	mockRepo.On("UpdateQuestInstance", context.Background(), mock.AnythingOfType("*models.QuestInstance")).Return(assert.AnError)

	err := service.FailQuest(context.Background(), questInstanceID, characterID)

	assert.Error(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertNotCalled(t, "PublishEvent", context.Background(), "quest:failed", mock.Anything)
}

func TestQuestService_ListQuestInstances_EmptyList(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()

	mockRepo.On("ListQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil), 10, 0).Return([]models.QuestInstance{}, nil)
	mockRepo.On("CountQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil)).Return(0, nil)

	result, err := service.ListQuestInstances(context.Background(), characterID, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Quests, 0)
	assert.Equal(t, 0, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_ListQuestInstances_WithFilters(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	status := models.QuestStatusCompleted
	instances := []models.QuestInstance{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			QuestID:     "quest_001",
			Status:      models.QuestStatusCompleted,
			StartedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockRepo.On("ListQuestInstances", context.Background(), characterID, &status, 10, 0).Return(instances, nil)
	mockRepo.On("CountQuestInstances", context.Background(), characterID, &status).Return(1, nil)

	result, err := service.ListQuestInstances(context.Background(), characterID, &status, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Quests, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_ListQuestInstances_Pagination(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	instances := []models.QuestInstance{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			QuestID:     "quest_001",
			Status:      models.QuestStatusInProgress,
			StartedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			QuestID:     "quest_002",
			Status:      models.QuestStatusInProgress,
			StartedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockRepo.On("ListQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil), 2, 0).Return(instances, nil)
	mockRepo.On("CountQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil)).Return(5, nil)

	result, err := service.ListQuestInstances(context.Background(), characterID, nil, 2, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Quests, 2)
	assert.Equal(t, 5, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_ListQuestInstances_DatabaseError_List(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()

	mockRepo.On("ListQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil), 10, 0).Return(nil, assert.AnError)

	result, err := service.ListQuestInstances(context.Background(), characterID, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestQuestService_ListQuestInstances_DatabaseError_Count(t *testing.T) {
	service, mockRepo, _, _, cleanup := setupTestQuestService(t)
	defer cleanup()

	characterID := uuid.New()
	instances := []models.QuestInstance{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			QuestID:     "quest_001",
			Status:      models.QuestStatusInProgress,
			StartedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockRepo.On("ListQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil), 10, 0).Return(instances, nil)
	mockRepo.On("CountQuestInstances", context.Background(), characterID, (*models.QuestStatus)(nil)).Return(0, assert.AnError)

	result, err := service.ListQuestInstances(context.Background(), characterID, nil, 10, 0)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

