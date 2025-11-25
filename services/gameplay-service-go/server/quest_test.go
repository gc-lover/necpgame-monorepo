package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/stretchr/testify/assert"
)

type MockQuestRepository struct{}

func (m *MockQuestRepository) GetQuest(ctx context.Context, characterID, questID uuid.UUID) (*models.QuestProgress, error) {
	return &models.QuestProgress{
		ID:          questID,
		CharacterID: characterID,
		QuestID:     questID,
		Status:      models.QuestStatusActive,
		Progress:    50,
		StartedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}, nil
}

func (m *MockQuestRepository) ListQuests(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) ([]models.QuestProgress, error) {
	return []models.QuestProgress{}, nil
}

func (m *MockQuestRepository) StartQuest(ctx context.Context, progress *models.QuestProgress) error {
	return nil
}

func (m *MockQuestRepository) UpdateProgress(ctx context.Context, characterID, questID uuid.UUID, progress int) error {
	return nil
}

func (m *MockQuestRepository) CompleteQuest(ctx context.Context, characterID, questID uuid.UUID) error {
	return nil
}

func (m *MockQuestRepository) AbandonQuest(ctx context.Context, characterID, questID uuid.UUID) error {
	return nil
}

func (m *MockQuestRepository) GetObjectives(ctx context.Context, characterID, questID uuid.UUID) ([]models.QuestObjective, error) {
	return []models.QuestObjective{}, nil
}

func (m *MockQuestRepository) UpdateObjective(ctx context.Context, objective *models.QuestObjective) error {
	return nil
}

func (m *MockQuestRepository) Close() error {
	return nil
}

func TestNewQuestService(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	assert.NotNil(t, service)
}

func TestGetQuest(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	
	quest, err := service.GetQuest(ctx, characterID, questID)
	assert.NoError(t, err)
	assert.NotNil(t, quest)
	assert.Equal(t, characterID, quest.CharacterID)
	assert.Equal(t, models.QuestStatusActive, quest.Status)
}

func TestListQuests(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	status := models.QuestStatusActive
	
	quests, err := service.ListQuests(ctx, characterID, &status, 50, 0)
	assert.NoError(t, err)
	assert.NotNil(t, quests)
}

func TestStartQuest(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	
	quest, err := service.StartQuest(ctx, characterID, questID)
	assert.NoError(t, err)
	assert.NotNil(t, quest)
	assert.Equal(t, models.QuestStatusActive, quest.Status)
}

func TestUpdateProgress(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	newProgress := 75
	
	err := service.UpdateProgress(ctx, characterID, questID, newProgress)
	assert.NoError(t, err)
}

func TestCompleteQuest(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	
	err := service.CompleteQuest(ctx, characterID, questID)
	assert.NoError(t, err)
}

func TestAbandonQuest(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	
	err := service.AbandonQuest(ctx, characterID, questID)
	assert.NoError(t, err)
}

func TestGetObjectives(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	
	objectives, err := service.GetObjectives(ctx, characterID, questID)
	assert.NoError(t, err)
	assert.NotNil(t, objectives)
}

func TestUpdateObjective(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	objective := &models.QuestObjective{
		ID:            uuid.New(),
		QuestID:       uuid.New(),
		Description:   "Test objective",
		IsCompleted:   false,
		CurrentValue:  5,
		RequiredValue: 10,
	}
	
	err := service.UpdateObjective(ctx, objective)
	assert.NoError(t, err)
}

func TestQuestServiceNilRepository(t *testing.T) {
	defer func() {
		if r := recover(); r == nil {
			t.Errorf("Expected panic with nil repository")
		}
	}()
	NewQuestService(nil)
}

func TestQuestLifecycle(t *testing.T) {
	repo := &MockQuestRepository{}
	service := NewQuestService(repo)
	
	ctx := context.Background()
	characterID := uuid.New()
	questID := uuid.New()
	
	quest, err := service.StartQuest(ctx, characterID, questID)
	assert.NoError(t, err)
	assert.Equal(t, models.QuestStatusActive, quest.Status)
	
	err = service.UpdateProgress(ctx, characterID, questID, 50)
	assert.NoError(t, err)
	
	err = service.CompleteQuest(ctx, characterID, questID)
	assert.NoError(t, err)
}

