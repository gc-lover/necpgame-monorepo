package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type QuestRepositoryInterface interface {
	CreateQuestInstance(ctx context.Context, instance *models.QuestInstance) error
	GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error)
	GetQuestInstanceByCharacterAndQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error)
	UpdateQuestInstance(ctx context.Context, instance *models.QuestInstance) error
	ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) ([]models.QuestInstance, error)
	CountQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus) (int, error)
	CreateDialogueState(ctx context.Context, dialogueState *models.DialogueState) error
	UpdateDialogueState(ctx context.Context, dialogueState *models.DialogueState) error
	GetDialogueState(ctx context.Context, questInstanceID uuid.UUID) (*models.DialogueState, error)
	CreateSkillCheckResult(ctx context.Context, result *models.SkillCheckResult) error
}

type ProgressionRepositoryInterfaceForQuest interface {
	GetSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string) (*models.SkillExperience, error)
}

type QuestService struct {
	repo            QuestRepositoryInterface
	progressionRepo ProgressionRepositoryInterfaceForQuest
	cache           *redis.Client
	logger          *logrus.Logger
	eventBus        EventBus
}

func NewQuestService(dbURL, redisURL string, progressionRepo *ProgressionRepository, eventBus EventBus) (*QuestService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewQuestRepository(dbPool)

	return &QuestService{
		repo:            repo,
		progressionRepo: progressionRepo,
		cache:           redisClient,
		logger:          GetLogger(),
		eventBus:        eventBus,
	}, nil
}

func (s *QuestService) StartQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error) {
	existing, err := s.repo.GetQuestInstanceByCharacterAndQuest(ctx, characterID, questID)
	if err != nil {
		return nil, err
	}

	if existing != nil && existing.Status != models.QuestStatusFailed && existing.Status != models.QuestStatusAbandoned {
		return nil, fmt.Errorf("quest already started")
	}

	instance := &models.QuestInstance{
		CharacterID:   characterID,
		QuestID:       questID,
		Status:        models.QuestStatusInProgress,
		CurrentNode:   "start",
		DialogueState: make(map[string]interface{}),
		Objectives:   make(map[string]interface{}),
	}

	err = s.repo.CreateQuestInstance(ctx, instance)
	if err != nil {
		return nil, err
	}

	dialogueState := &models.DialogueState{
		QuestInstanceID: instance.ID,
		CharacterID:     characterID,
		CurrentNode:     "start",
		VisitedNodes:    []string{"start"},
		Choices:         make(map[string]interface{}),
	}

	err = s.repo.CreateDialogueState(ctx, dialogueState)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create dialogue state")
	}

	err = s.publishQuestStartedEvent(ctx, characterID, questID, instance.ID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish quest started event")
	}

	return instance, nil
}

func (s *QuestService) GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error) {
	return s.repo.GetQuestInstance(ctx, instanceID)
}

func (s *QuestService) UpdateDialogue(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, nodeID string, choiceID *string) error {
	instance, err := s.repo.GetQuestInstance(ctx, questInstanceID)
	if err != nil {
		return err
	}

	if instance == nil {
		return fmt.Errorf("quest instance not found")
	}

	if instance.CharacterID != characterID {
		return fmt.Errorf("quest instance does not belong to character")
	}

	if instance.Status != models.QuestStatusInProgress {
		return fmt.Errorf("quest is not in progress")
	}

	dialogueState, err := s.repo.GetDialogueState(ctx, questInstanceID)
	if err != nil {
		return err
	}

	if dialogueState == nil {
		dialogueState = &models.DialogueState{
			QuestInstanceID: questInstanceID,
			CharacterID:     characterID,
			CurrentNode:     nodeID,
			VisitedNodes:    []string{nodeID},
			Choices:         make(map[string]interface{}),
		}
		err = s.repo.CreateDialogueState(ctx, dialogueState)
	} else {
		if !contains(dialogueState.VisitedNodes, nodeID) {
			dialogueState.VisitedNodes = append(dialogueState.VisitedNodes, nodeID)
		}
		dialogueState.CurrentNode = nodeID
		if choiceID != nil {
			dialogueState.Choices[nodeID] = *choiceID
		}
		err = s.repo.UpdateDialogueState(ctx, dialogueState)
	}

	if err != nil {
		return err
	}

	instance.CurrentNode = nodeID
	instance.DialogueState["current_node"] = nodeID
	if choiceID != nil {
		instance.DialogueState["last_choice"] = *choiceID
	}

	return s.repo.UpdateQuestInstance(ctx, instance)
}

func (s *QuestService) PerformSkillCheck(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, skillID string, requiredLevel int) (bool, error) {
	instance, err := s.repo.GetQuestInstance(ctx, questInstanceID)
	if err != nil {
		return false, err
	}

	if instance == nil {
		return false, fmt.Errorf("quest instance not found")
	}

	if instance.CharacterID != characterID {
		return false, fmt.Errorf("quest instance does not belong to character")
	}

	skillExp, err := s.progressionRepo.GetSkillExperience(ctx, characterID, skillID)
	if err != nil {
		return false, err
	}

	actualLevel := 1
	if skillExp != nil {
		actualLevel = skillExp.Level
	}

	passed := actualLevel >= requiredLevel

	result := &models.SkillCheckResult{
		QuestInstanceID: questInstanceID,
		CharacterID:     characterID,
		SkillID:         skillID,
		RequiredLevel:   requiredLevel,
		ActualLevel:     actualLevel,
		Passed:          passed,
	}

	err = s.repo.CreateSkillCheckResult(ctx, result)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create skill check result")
	}

	return passed, nil
}

func (s *QuestService) CompleteObjective(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID, objectiveID string) error {
	instance, err := s.repo.GetQuestInstance(ctx, questInstanceID)
	if err != nil {
		return err
	}

	if instance == nil {
		return fmt.Errorf("quest instance not found")
	}

	if instance.CharacterID != characterID {
		return fmt.Errorf("quest instance does not belong to character")
	}

	if instance.Status != models.QuestStatusInProgress {
		return fmt.Errorf("quest is not in progress")
	}

	if instance.Objectives == nil {
		instance.Objectives = make(map[string]interface{})
	}

	instance.Objectives[objectiveID] = models.ObjectiveStatusCompleted

	err = s.repo.UpdateQuestInstance(ctx, instance)
	if err != nil {
		return err
	}

	err = s.publishObjectiveCompletedEvent(ctx, characterID, instance.QuestID, objectiveID, instance.ID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish objective completed event")
	}

	return nil
}

func (s *QuestService) CompleteQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error {
	instance, err := s.repo.GetQuestInstance(ctx, questInstanceID)
	if err != nil {
		return err
	}

	if instance == nil {
		return fmt.Errorf("quest instance not found")
	}

	if instance.CharacterID != characterID {
		return fmt.Errorf("quest instance does not belong to character")
	}

	if instance.Status != models.QuestStatusInProgress {
		return fmt.Errorf("quest is not in progress")
	}

	now := time.Now()
	instance.Status = models.QuestStatusCompleted
	instance.CompletedAt = &now

	err = s.repo.UpdateQuestInstance(ctx, instance)
	if err != nil {
		return err
	}

	err = s.publishQuestCompletedEvent(ctx, characterID, instance.QuestID, instance.ID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish quest completed event")
	}

	return nil
}

func (s *QuestService) FailQuest(ctx context.Context, questInstanceID uuid.UUID, characterID uuid.UUID) error {
	instance, err := s.repo.GetQuestInstance(ctx, questInstanceID)
	if err != nil {
		return err
	}

	if instance == nil {
		return fmt.Errorf("quest instance not found")
	}

	if instance.CharacterID != characterID {
		return fmt.Errorf("quest instance does not belong to character")
	}

	instance.Status = models.QuestStatusFailed

	err = s.repo.UpdateQuestInstance(ctx, instance)
	if err != nil {
		return err
	}

	err = s.publishQuestFailedEvent(ctx, characterID, instance.QuestID, instance.ID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to publish quest failed event")
	}

	return nil
}

func (s *QuestService) ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) (*models.QuestListResponse, error) {
	instances, err := s.repo.ListQuestInstances(ctx, characterID, status, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountQuestInstances(ctx, characterID, status)
	if err != nil {
		return nil, err
	}

	return &models.QuestListResponse{
		Quests: instances,
		Total:  total,
	}, nil
}

func (s *QuestService) publishQuestStartedEvent(ctx context.Context, characterID uuid.UUID, questID string, instanceID uuid.UUID) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"quest_id":     questID,
		"instance_id":  instanceID.String(),
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "quest:started", payload)
}

func (s *QuestService) publishObjectiveCompletedEvent(ctx context.Context, characterID uuid.UUID, questID, objectiveID string, instanceID uuid.UUID) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"quest_id":     questID,
		"objective_id": objectiveID,
		"instance_id":  instanceID.String(),
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "quest:objective-completed", payload)
}

func (s *QuestService) publishQuestCompletedEvent(ctx context.Context, characterID uuid.UUID, questID string, instanceID uuid.UUID) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"quest_id":     questID,
		"instance_id":  instanceID.String(),
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "quest:completed", payload)
}

func (s *QuestService) publishQuestFailedEvent(ctx context.Context, characterID uuid.UUID, questID string, instanceID uuid.UUID) error {
	payload := map[string]interface{}{
		"character_id": characterID.String(),
		"quest_id":     questID,
		"instance_id":  instanceID.String(),
		"timestamp":    time.Now().Format(time.RFC3339),
	}

	return s.eventBus.PublishEvent(ctx, "quest:failed", payload)
}

func contains(slice []string, item string) bool {
	for _, s := range slice {
		if s == item {
			return true
		}
	}
	return false
}

func (s *QuestService) ReloadQuestContent(ctx context.Context, req *models.ReloadQuestContentRequest) (*models.ReloadQuestContentResponse, error) {
	yamlContent := req.YAMLContent
	
	// Extract metadata
	metadata, ok := yamlContent["metadata"].(map[string]interface{})
	if !ok {
		return nil, fmt.Errorf("invalid YAML: missing metadata")
	}
	
	questID, ok := metadata["id"].(string)
	if !ok || questID == "" {
		return nil, fmt.Errorf("invalid YAML: missing or invalid metadata.id")
	}
	
	title, _ := metadata["title"].(string)
	if title == "" {
		title = questID
	}
	
	// Extract summary for description
	description := ""
	if summary, ok := yamlContent["summary"].(map[string]interface{}); ok {
		if goal, ok := summary["goal"].(string); ok {
			description = goal
		}
	}
	
	// Extract quest type (default to "side")
	questType := "side"
	if category, ok := metadata["category"].(string); ok {
		if category == "main-quest" || category == "main" {
			questType = "main"
		}
	}
	
	// Extract level range
	var levelMin, levelMax *int
	if levelMinVal, ok := yamlContent["level_min"].(int); ok {
		levelMin = &levelMinVal
	}
	if levelMaxVal, ok := yamlContent["level_max"].(int); ok {
		levelMax = &levelMaxVal
	}
	
	// Extract objectives, rewards, branches from content
	objectives := make(map[string]interface{})
	rewards := make(map[string]interface{})
	branches := make(map[string]interface{})
	
	if content, ok := yamlContent["content"].(map[string]interface{}); ok {
		if sections, ok := content["sections"].([]interface{}); ok {
			objectives["sections"] = sections
		}
	}
	
	if summary, ok := yamlContent["summary"].(map[string]interface{}); ok {
		if keyPoints, ok := summary["key_points"].([]interface{}); ok {
			objectives["key_points"] = keyPoints
		}
	}
	
	// Store full YAML content in content_data
	definition := &models.QuestDefinition{
		QuestID:      questID,
		Title:        title,
		Description:  description,
		QuestType:    questType,
		LevelMin:     levelMin,
		LevelMax:     levelMax,
		Requirements: make(map[string]interface{}),
		Objectives:   objectives,
		Rewards:      rewards,
		Branches:     branches,
		ContentData:  yamlContent,
		Version:      1,
		IsActive:     true,
	}
	
	err := s.repo.UpsertQuestDefinition(ctx, definition)
	if err != nil {
		return nil, fmt.Errorf("failed to upsert quest definition: %w", err)
	}
	
	return &models.ReloadQuestContentResponse{
		QuestID:    questID,
		Message:    fmt.Sprintf("Quest '%s' successfully imported", title),
		ImportedAt: time.Now(),
	}, nil
}

