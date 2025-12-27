// Service layer with dynamic quest business logic
// Issue: #2244, #143576873
// Agent: Backend

package service

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"go.uber.org/zap"

	"necpgame/services/dynamic-quests-service-go/internal/repository"
	"necpgame/services/dynamic-quests-service-go/pkg/models"
)

// Service handles dynamic quest business logic
type Service struct {
	repo   *repository.Repository
	logger *zap.SugaredLogger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.SugaredLogger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// QuestChoice represents a player's choice in a quest
type QuestChoice struct {
	ChoicePoint string `json:"choice_point"`
	ChoiceValue string `json:"choice_value"`
}

// ReputationChange represents reputation score changes
type ReputationChange struct {
	Corporate int `json:"corporate"`
	Street    int `json:"street"`
	Humanity  int `json:"humanity"`
}

// ChoiceResult represents the result of processing a choice
type ChoiceResult struct {
	NewState          string                     `json:"new_state"`
	ReputationChanges ReputationChange           `json:"reputation_changes"`
	NextChoicePoint   string                     `json:"next_choice_point,omitempty"`
	QuestCompleted    bool                       `json:"quest_completed"`
	EndingAchieved    string                     `json:"ending_achieved,omitempty"`
	Consequences      []models.ConsequenceResult `json:"consequences,omitempty"`
}

// StartQuest starts a quest for a player
func (s *Service) StartQuest(ctx context.Context, playerID, questID string) error {
	// Validate quest exists
	_, err := s.repo.GetQuestDefinition(ctx, questID)
	if err != nil {
		return fmt.Errorf("quest not found: %w", err)
	}

	// Get current player reputation for snapshot
	reputation, err := s.repo.GetPlayerReputation(ctx, playerID)
	if err != nil {
		return fmt.Errorf("failed to get player reputation: %w", err)
	}

	// Convert reputation to JSON for snapshot
	repSnapshot, err := json.Marshal(reputation)
	if err != nil {
		return fmt.Errorf("failed to marshal reputation snapshot: %w", err)
	}

	// Start the quest
	return s.repo.StartPlayerQuest(ctx, playerID, questID, repSnapshot)
}

// ProcessChoice processes a player's choice and updates quest state
func (s *Service) ProcessChoice(ctx context.Context, playerID, questID string, choice QuestChoice) (*ChoiceResult, error) {
	// Get current quest state
	questState, err := s.repo.GetPlayerQuestState(ctx, playerID, questID)
	if err != nil {
		return nil, fmt.Errorf("failed to get quest state: %w", err)
	}

	if questState.CurrentState != "active" {
		return nil, fmt.Errorf("quest is not in active state: %s", questState.CurrentState)
	}

	// Get quest definition to validate choice
	questDef, err := s.repo.GetQuestDefinition(ctx, questID)
	if err != nil {
		return nil, fmt.Errorf("failed to get quest definition: %w", err)
	}

	// Convert to new model for advanced processing
	dynamicQuest, err := s.convertToDynamicQuest(questDef)
	if err != nil {
		return nil, fmt.Errorf("failed to convert quest: %w", err)
	}

	// Find the current choice point
	var currentChoicePoint *models.ChoicePoint
	for _, cp := range dynamicQuest.ChoicePoints {
		if cp.ID == choice.ChoicePoint {
			currentChoicePoint = &cp
			break
		}
	}

	if currentChoicePoint == nil {
		return nil, fmt.Errorf("invalid choice point: %s", choice.ChoicePoint)
	}

	// Find the selected choice
	var selectedChoice *models.Choice
	for _, ch := range currentChoicePoint.Choices {
		if ch.ID == choice.ChoiceValue {
			selectedChoice = &ch
			break
		}
	}

	if selectedChoice == nil {
		return nil, fmt.Errorf("invalid choice value: %s", choice.ChoiceValue)
	}

	// Process the choice and calculate consequences
	result, err := s.processAdvancedChoice(choice, selectedChoice, dynamicQuest)
	if err != nil {
		return nil, fmt.Errorf("failed to process choice: %w", err)
	}

	// Update player reputation
	if err := s.updatePlayerReputation(ctx, playerID, result.ReputationChanges); err != nil {
		return nil, fmt.Errorf("failed to update reputation: %w", err)
	}

	// Record choice in history
	choiceHistory := &repository.ChoiceHistory{
		ChoiceID:    fmt.Sprintf("%s-%s-%d", playerID, questID, time.Now().Unix()),
		PlayerID:    playerID,
		QuestID:     questID,
		ChoicePoint: choice.ChoicePoint,
		ChoiceValue: choice.ChoiceValue,
		Timestamp:   time.Now(),
		RepBefore:   questState.ReputationSnapshot,
	}

	// Get current reputation for after snapshot
	currentRep, err := s.repo.GetPlayerReputation(ctx, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get current reputation: %w", err)
	}

	repAfter, err := json.Marshal(currentRep)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal reputation after: %w", err)
	}
	choiceHistory.RepAfter = repAfter

	if err := s.repo.RecordPlayerChoice(ctx, choiceHistory); err != nil {
		s.logger.Errorf("Failed to record choice history: %v", err)
		// Don't fail the operation for history recording errors
	}

	// Update quest state
	var newChoiceHistory []QuestChoice
	if err := json.Unmarshal(questState.ChoiceHistory, &newChoiceHistory); err != nil {
		return nil, fmt.Errorf("failed to parse choice history: %w", err)
	}

	newChoiceHistory = append(newChoiceHistory, choice)
	choiceHistoryJSON, err := json.Marshal(newChoiceHistory)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal choice history: %w", err)
	}

	newState := result.NewState
	if result.QuestCompleted {
		newState = "completed"
		if err := s.repo.CompletePlayerQuest(ctx, playerID, questID, result.EndingAchieved); err != nil {
			return nil, fmt.Errorf("failed to complete quest: %w", err)
		}
	} else {
		if err := s.repo.UpdatePlayerQuestState(ctx, playerID, questID, newState, choiceHistoryJSON); err != nil {
			return nil, fmt.Errorf("failed to update quest state: %w", err)
		}
	}

	s.logger.Infof("Choice processed: player=%s, quest=%s, choice=%s:%s, new_state=%s",
		playerID, questID, choice.ChoicePoint, choice.ChoiceValue, newState)

	return result, nil
}

// calculateChoiceResult calculates the consequences of a choice
func (s *Service) calculateChoiceResult(choice QuestChoice, choiceData map[string]interface{}) (*ChoiceResult, error) {
	result := &ChoiceResult{
		NewState:       "active",
		QuestCompleted: false,
	}

	// Parse choice consequences
	if consequences, ok := choiceData["consequences"].(map[string]interface{}); ok {
		if repImpacts, ok := consequences["reputation_impact"].(map[string]interface{}); ok {
			result.ReputationChanges = ReputationChange{
				Corporate: int(repImpacts["corporate"].(float64)),
				Street:    int(repImpacts["street"].(float64)),
				Humanity:  int(repImpacts["humanity"].(float64)),
			}
		}

		if unlocks, ok := consequences["unlocks"].([]interface{}); ok {
			for _, unlock := range unlocks {
				if unlockStr, ok := unlock.(string); ok {
					if unlockStr == "ending_condition" {
						result.QuestCompleted = true
						result.EndingAchieved = choice.ChoiceValue
					}
				}
			}
		}

		if nextChoice, ok := consequences["next_choice_point"].(string); ok {
			result.NextChoicePoint = nextChoice
		}
	}

	// Determine next state based on choice
	switch choice.ChoiceValue {
	case "corporate_loyal":
		result.NewState = "corporate_path"
	case "street_loyal":
		result.NewState = "street_path"
	case "neutral":
		result.NewState = "balanced_path"
	default:
		result.NewState = "branching"
	}

	return result, nil
}

// updatePlayerReputation updates player reputation based on choice consequences
func (s *Service) updatePlayerReputation(ctx context.Context, playerID string, changes ReputationChange) error {
	currentRep, err := s.repo.GetPlayerReputation(ctx, playerID)
	if err != nil {
		return err
	}

	// Apply changes with bounds checking (-100 to +100)
	currentRep.CorporateRep = clamp(currentRep.CorporateRep+changes.Corporate, -100, 100)
	currentRep.StreetRep = clamp(currentRep.StreetRep+changes.Street, -100, 100)
	currentRep.HumanityScore = clamp(currentRep.HumanityScore+changes.Humanity, 0, 100)

	// Update faction standing based on reputation
	currentRep.FactionStanding = s.calculateFactionStanding(currentRep)

	return s.repo.UpdatePlayerReputation(ctx, currentRep)
}

// calculateFactionStanding determines faction standing based on reputation scores
func (s *Service) calculateFactionStanding(rep *repository.PlayerReputation) string {
	corporate := rep.CorporateRep
	street := rep.StreetRep

	if corporate >= 50 && corporate > street+20 {
		return "corporate"
	} else if street >= 50 && street > corporate+20 {
		return "street"
	} else if corporate <= -30 && street <= -30 {
		return "outcast"
	} else {
		return "neutral"
	}
}

// GetPlayerQuestState retrieves a player's quest state with processed data
func (s *Service) GetPlayerQuestState(ctx context.Context, playerID, questID string) (*repository.PlayerQuestState, error) {
	return s.repo.GetPlayerQuestState(ctx, playerID, questID)
}

// GetPlayerReputation retrieves player reputation
func (s *Service) GetPlayerReputation(ctx context.Context, playerID string) (*repository.PlayerReputation, error) {
	return s.repo.GetPlayerReputation(ctx, playerID)
}

// ListAvailableQuests returns quests available to a player based on level and reputation
func (s *Service) ListAvailableQuests(ctx context.Context, playerLevel int, reputation *repository.PlayerReputation) ([]*repository.QuestDefinition, error) {
	// Get all quests (in production, this would be cached)
	allQuests, err := s.repo.ListQuestDefinitions(ctx, 100, 0)
	if err != nil {
		return nil, err
	}

	var available []*repository.QuestDefinition
	for _, quest := range allQuests {
		if s.isQuestAvailable(quest, playerLevel, reputation) {
			available = append(available, quest)
		}
	}

	return available, nil
}

// isQuestAvailable checks if a quest is available to a player
func (s *Service) isQuestAvailable(quest *repository.QuestDefinition, playerLevel int, reputation *repository.PlayerReputation) bool {
	// Check level requirements
	if playerLevel < quest.MinLevel || playerLevel > quest.MaxLevel {
		return false
	}

	// Parse reputation requirements (if any)
	if len(quest.ReputationImpacts) > 0 {
		var repReqs map[string]int
		if err := json.Unmarshal(quest.ReputationImpacts, &repReqs); err == nil {
			if corpReq, ok := repReqs["corporate_min"]; ok && reputation.CorporateRep < corpReq {
				return false
			}
			if streetReq, ok := repReqs["street_min"]; ok && reputation.StreetRep < streetReq {
				return false
			}
			if humanityReq, ok := repReqs["humanity_min"]; ok && reputation.HumanityScore < humanityReq {
				return false
			}
		}
	}

	return true
}

// clamp restricts a value to a specified range
func clamp(value, min, max int) int {
	if value < min {
		return min
	}
	if value > max {
		return max
	}
	return value
}

// convertToDynamicQuest converts repository quest definition to new model
func (s *Service) convertToDynamicQuest(questDef *repository.QuestDefinition) (*models.DynamicQuest, error) {
	dynamicQuest := &models.DynamicQuest{
		ID:          questDef.QuestID,
		QuestID:     questDef.QuestID,
		Title:       questDef.Title,
		Description: questDef.Description,
		QuestType:   questDef.QuestType,
		MinLevel:    questDef.MinLevel,
		MaxLevel:    questDef.MaxLevel,
		CreatedAt:   questDef.CreatedAt,
		UpdatedAt:   questDef.UpdatedAt,
	}

	// Parse choice points
	if questDef.ChoicePoints != nil {
		var choicePoints []models.ChoicePoint
		if err := json.Unmarshal(questDef.ChoicePoints, &choicePoints); err != nil {
			return nil, fmt.Errorf("failed to unmarshal choice points: %w", err)
		}
		dynamicQuest.ChoicePoints = choicePoints
	}

	// Parse ending variations
	if questDef.EndingVariations != nil {
		var endingVariations []models.EndingVariation
		if err := json.Unmarshal(questDef.EndingVariations, &endingVariations); err != nil {
			return nil, fmt.Errorf("failed to unmarshal ending variations: %w", err)
		}
		dynamicQuest.EndingVariations = endingVariations
	}

	// Parse reputation impacts
	if questDef.ReputationImpacts != nil {
		var reputationImpacts []models.ReputationImpact
		if err := json.Unmarshal(questDef.ReputationImpacts, &reputationImpacts); err != nil {
			return nil, fmt.Errorf("failed to unmarshal reputation impacts: %w", err)
		}
		dynamicQuest.ReputationImpacts = reputationImpacts
	}

	return dynamicQuest, nil
}

// processAdvancedChoice processes a choice using the new model structure
func (s *Service) processAdvancedChoice(choice QuestChoice, selectedChoice *models.Choice, dynamicQuest *models.DynamicQuest) (*ChoiceResult, error) {
	result := &ChoiceResult{
		NewState:       "active",
		QuestCompleted: false,
		Consequences:   []models.ConsequenceResult{},
	}

	// Process consequences
	for _, consequence := range selectedChoice.Consequences {
		consequenceResult := models.ConsequenceResult{
			Type:        consequence.Type,
			Description: consequence.Description,
			Success:     true, // Assume success for now, could add probability logic
		}

		switch consequence.Type {
		case "reputation":
			if consequence.Target == "corporate" {
				result.ReputationChanges.Corporate = int(consequence.Value.(float64))
			} else if consequence.Target == "street" {
				result.ReputationChanges.Street = int(consequence.Value.(float64))
			} else if consequence.Target == "humanity" {
				result.ReputationChanges.Humanity = int(consequence.Value.(float64))
			}
			consequenceResult.Value = consequence.Value

		case "item":
			consequenceResult.Value = consequence.Value

		case "experience":
			consequenceResult.Value = consequence.Value

		case "quest_state":
			if consequence.Target == "completed" {
				result.QuestCompleted = true
				result.EndingAchieved = choice.ChoiceValue
			}
			consequenceResult.Value = consequence.Value
		}

		result.Consequences = append(result.Consequences, consequenceResult)
	}

	// Determine next choice point (simplified logic)
	if len(dynamicQuest.ChoicePoints) > 0 {
		// Find next choice point in sequence
		for i, cp := range dynamicQuest.ChoicePoints {
			if cp.ID == choice.ChoicePoint && i+1 < len(dynamicQuest.ChoicePoints) {
				result.NextChoicePoint = dynamicQuest.ChoicePoints[i+1].ID
				break
			}
		}
	}

	// Determine new state based on choice
	switch selectedChoice.MoralAlignment {
	case "good":
		result.NewState = "positive_path"
	case "evil":
		result.NewState = "negative_path"
	default:
		result.NewState = "neutral_path"
	}

	return result, nil
}

// ImportQuestsFromYAML imports quests from YAML files
func (s *Service) ImportQuestsFromYAML(ctx context.Context, yamlData []byte) error {
	// This would parse YAML and create quest definitions
	// For now, return placeholder implementation
	s.logger.Info("Quest import from YAML - placeholder implementation")
	return fmt.Errorf("YAML import not implemented yet")
}

// GenerateQuestAnalytics generates analytics for quest performance
func (s *Service) GenerateQuestAnalytics(ctx context.Context, questID string) (*models.QuestAnalytics, error) {
	// Placeholder for analytics generation
	analytics := &models.QuestAnalytics{
		QuestID:         questID,
		TotalPlayers:    0,
		CompletionRate:  0.0,
		PopularChoices:  make(map[string]int64),
		EndingDistribution: make(map[string]int64),
		DifficultyRatings: make(map[string]float64),
		PlayerRetention: make(map[string]int64),
	}

	s.logger.Infof("Generated analytics for quest: %s", questID)
	return analytics, nil
}

