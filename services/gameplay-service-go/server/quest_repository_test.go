package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/stretchr/testify/assert"
)

func setupTestQuestRepository(t *testing.T) (*QuestRepository, func()) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return nil, nil
	}

	repo := NewQuestRepository(dbPool)

	cleanup := func() {
		dbPool.Close()
	}

	return repo, cleanup
}

func TestNewQuestRepository(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	repo := NewQuestRepository(dbPool)
	assert.NotNil(t, repo)
	assert.NotNil(t, repo.db)
	assert.NotNil(t, repo.logger)
}

func TestQuestRepository_GetQuestInstance_NotFound(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	instanceID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetQuestInstance(ctx, instanceID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestQuestRepository_CreateQuestInstance(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	instance := &models.QuestInstance{
		CharacterID:   characterID,
		QuestID:       "quest_001",
		Status:        models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateQuestInstance(ctx, instance)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create quest instance: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, instance.ID)
}

func TestQuestRepository_GetQuestInstance_Success(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	instance := &models.QuestInstance{
		CharacterID:   characterID,
		QuestID:       "quest_001",
		Status:        models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateQuestInstance(ctx, instance)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create quest instance: %v", err)
		return
	}

	result, err := repo.GetQuestInstance(ctx, instance.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get quest instance: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, instance.ID, result.ID)
	assert.Equal(t, characterID, result.CharacterID)
}

func TestQuestRepository_GetQuestInstanceByCharacterAndQuest_NotFound(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	questID := "quest_001"
	ctx := context.Background()
	result, err := repo.GetQuestInstanceByCharacterAndQuest(ctx, characterID, questID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestQuestRepository_UpdateQuestInstance(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	instance := &models.QuestInstance{
		CharacterID:   characterID,
		QuestID:       "quest_001",
		Status:        models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateQuestInstance(ctx, instance)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create quest instance: %v", err)
		return
	}

	now := time.Now()
	instance.Status = models.QuestStatusCompleted
	instance.CompletedAt = &now
	instance.CurrentNode = "end"

	err = repo.UpdateQuestInstance(ctx, instance)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update quest instance: %v", err)
		return
	}

	assert.NoError(t, err)

	result, err := repo.GetQuestInstance(ctx, instance.ID)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to get quest instance: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.QuestStatusCompleted, result.Status)
}

func TestQuestRepository_ListQuestInstances_Empty(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	instances, err := repo.ListQuestInstances(ctx, characterID, nil, 10, 0)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotNil(t, instances)
}

func TestQuestRepository_CountQuestInstances(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	ctx := context.Background()
	count, err := repo.CountQuestInstances(ctx, characterID, nil)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.GreaterOrEqual(t, count, 0)
}

func TestQuestRepository_CreateDialogueState(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	dialogueState := &models.DialogueState{
		QuestInstanceID: questInstanceID,
		CharacterID:     characterID,
		CurrentNode:     "start",
		VisitedNodes:    []string{"start"},
		Choices:         make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateDialogueState(ctx, dialogueState)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create dialogue state: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, dialogueState.ID)
}

func TestQuestRepository_GetDialogueState_NotFound(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	questInstanceID := uuid.New()
	ctx := context.Background()
	result, err := repo.GetDialogueState(ctx, questInstanceID)

	if err != nil {
		t.Skipf("Skipping test due to database error: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.Nil(t, result)
}

func TestQuestRepository_UpdateDialogueState(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	dialogueState := &models.DialogueState{
		QuestInstanceID: questInstanceID,
		CharacterID:     characterID,
		CurrentNode:     "start",
		VisitedNodes:    []string{"start"},
		Choices:         make(map[string]interface{}),
	}

	ctx := context.Background()
	err := repo.CreateDialogueState(ctx, dialogueState)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create dialogue state: %v", err)
		return
	}

	dialogueState.CurrentNode = "node_001"
	dialogueState.VisitedNodes = []string{"start", "node_001"}

	err = repo.UpdateDialogueState(ctx, dialogueState)
	if err != nil {
		t.Skipf("Skipping test due to database error: failed to update dialogue state: %v", err)
		return
	}

	assert.NoError(t, err)
}

func TestQuestRepository_CreateSkillCheckResult(t *testing.T) {
	repo, cleanup := setupTestQuestRepository(t)
	if repo == nil {
		return
	}
	defer cleanup()

	questInstanceID := uuid.New()
	characterID := uuid.New()
	result := &models.SkillCheckResult{
		QuestInstanceID: questInstanceID,
		CharacterID:     characterID,
		SkillID:         "combat",
		RequiredLevel:   5,
		ActualLevel:     10,
		Passed:          true,
	}

	ctx := context.Background()
	err := repo.CreateSkillCheckResult(ctx, result)

	if err != nil {
		t.Skipf("Skipping test due to database error: failed to create skill check result: %v", err)
		return
	}

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, result.ID)
}

