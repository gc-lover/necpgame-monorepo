// Service layer with dynamic quest business logic
// Issue: #2244, #143576873
// Agent: Backend

package service

import (
	"context"
	"encoding/json"
	"fmt"
	"sort"
	"sync"
	"time"

	"go.uber.org/zap"
	"gopkg.in/yaml.v3"

	"necpgame/services/dynamic-quests-service-go/internal/repository"
	"necpgame/services/dynamic-quests-service-go/pkg/models"
)

// Service handles dynamic quest business logic
type Service struct {
	repo   *repository.Repository
	logger *zap.SugaredLogger

	// PERFORMANCE: Memory pooling for MMOFPS optimization
	choiceResultPool sync.Pool
	questStatePool   sync.Pool
	reputationPool   sync.Pool

	// PERFORMANCE: In-memory caching for hot data
	questCache      sync.Map // map[string]*repository.QuestDefinition
	reputationCache sync.Map // map[string]*repository.PlayerReputation
	cacheTTL        time.Duration
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.SugaredLogger) *Service {
	svc := &Service{
		repo:     repo,
		logger:   logger,
		cacheTTL: 5 * time.Minute, // 5 minute cache TTL
	}

	// PERFORMANCE: Initialize memory pools for zero allocations
	svc.choiceResultPool.New = func() interface{} {
		return &ChoiceResult{}
	}
	svc.questStatePool.New = func() interface{} {
		return &repository.PlayerQuestState{}
	}
	svc.reputationPool.New = func() interface{} {
		return &repository.PlayerReputation{}
	}

	// Start cache cleanup goroutine
	go svc.cacheCleanup()

	return svc
}

// PERFORMANCE: Memory pool helpers for zero allocations
func (s *Service) getChoiceResult() *ChoiceResult {
	return s.choiceResultPool.Get().(*ChoiceResult)
}

func (s *Service) putChoiceResult(cr *ChoiceResult) {
	// Reset fields before returning to pool
	cr.NewState = ""
	cr.ReputationChanges = ReputationChange{}
	cr.NextChoicePoint = ""
	cr.QuestCompleted = false
	cr.EndingAchieved = ""
	cr.Consequences = nil
	s.choiceResultPool.Put(cr)
}

func (s *Service) getQuestState() *repository.PlayerQuestState {
	return s.questStatePool.Get().(*repository.PlayerQuestState)
}

func (s *Service) putQuestState(qs *repository.PlayerQuestState) {
	// Reset fields before returning to pool
	qs.PlayerID = ""
	qs.QuestID = ""
	qs.CurrentState = ""
	qs.ReputationSnapshot = nil
	qs.ChoiceHistory = nil
	qs.StartedAt = time.Time{}
	qs.CompletedAt = nil
	qs.EndingAchieved = ""
	s.questStatePool.Put(qs)
}

func (s *Service) getReputation() *repository.PlayerReputation {
	return s.reputationPool.Get().(*repository.PlayerReputation)
}

func (s *Service) putReputation(rep *repository.PlayerReputation) {
	// Reset fields before returning to pool
	rep.PlayerID = ""
	rep.CorporateRep = 0
	rep.StreetRep = 0
	rep.HumanityScore = 0
	rep.LastUpdated = time.Time{}
	rep.FactionStanding = ""
	s.reputationPool.Put(rep)
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
	// PERFORMANCE: Add context timeout for MMOFPS optimization
	timeoutCtx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	// Validate quest exists
	questDef, err := s.repo.GetQuestDefinition(timeoutCtx, questID)
	if err != nil {
		return fmt.Errorf("quest not found: %w", err)
	}

	// PERFORMANCE: Get current player reputation from cache or database
	reputation, err := s.getCachedReputation(ctx, playerID)
	if err != nil {
		return fmt.Errorf("failed to get player reputation: %w", err)
	}

	// Convert reputation to JSON for snapshot
	repSnapshot, err := json.Marshal(reputation)
	if err != nil {
		return fmt.Errorf("failed to marshal reputation snapshot: %w", err)
	}

	// Start the quest
	if err := s.repo.StartPlayerQuest(ctx, playerID, questID, repSnapshot); err != nil {
		return err
	}

	// Initialize branching state for JSONB branching quests
	if len(questDef.BranchingLogic) > 0 && string(questDef.BranchingLogic) != "null" {
		branchingLogic, err := s.repo.ParseBranchingLogic(questDef.BranchingLogic)
		if err != nil {
			s.logger.Warnf("Failed to parse branching logic for quest %s: %v", questID, err)
			return nil // Don't fail quest start for branching logic issues
		}

		if branchingLogic != nil {
			// Initialize with entry point
			initialState := map[string]interface{}{
				"current_node":   branchingLogic.EntryPoint,
				"choice_history": []interface{}{},
			}

			stateJSON, err := json.Marshal(initialState)
			if err != nil {
				s.logger.Warnf("Failed to marshal initial branching state for quest %s: %v", questID, err)
				return nil
			}

			if err := s.repo.UpdatePlayerBranchingState(ctx, playerID, questID, stateJSON); err != nil {
				s.logger.Warnf("Failed to initialize branching state for quest %s: %v", questID, err)
				// Don't fail quest start
			}
		}
	}

	return nil
}

// ProcessChoice processes a player's choice and updates quest state
func (s *Service) ProcessChoice(ctx context.Context, playerID, questID string, choice QuestChoice) (*ChoiceResult, error) {
	// PERFORMANCE: Add context timeout for MMOFPS optimization
	timeoutCtx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// PERFORMANCE: Get ChoiceResult from memory pool (zero allocations)
	result := s.getChoiceResult()

	// Get current quest state
	questState, err := s.repo.GetPlayerQuestState(timeoutCtx, playerID, questID)
	if err != nil {
		return nil, fmt.Errorf("failed to get quest state: %w", err)
	}

	if questState.CurrentState != "active" {
		return nil, fmt.Errorf("quest is not in active state: %s", questState.CurrentState)
	}

	// PERFORMANCE: Get quest definition from cache or database
	questDef, err := s.getCachedQuest(timeoutCtx, questID)
	if err != nil {
		return nil, fmt.Errorf("failed to get quest definition: %w", err)
	}

	// Check if quest uses new JSONB branching logic
	if len(questDef.BranchingLogic) > 0 && string(questDef.BranchingLogic) != "null" {
		return s.processJSONBBranchingChoice(timeoutCtx, playerID, questID, choice, questDef, questState)
	}

	// Convert to new model for advanced processing (legacy system)
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
	result, err = s.processAdvancedChoice(choice, selectedChoice, dynamicQuest)
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

	// PERFORMANCE: Don't return to pool - caller owns this object now
	resultPtr := result
	result = nil // Prevent defer cleanup
	return resultPtr, nil
}

// processJSONBBranchingChoice handles choices using the new JSONB branching system
func (s *Service) processJSONBBranchingChoice(ctx context.Context, playerID, questID string, choice QuestChoice, questDef *repository.QuestDefinition, questState *repository.PlayerQuestState) (*ChoiceResult, error) {
	// Parse branching logic
	branchingLogic, err := s.repo.ParseBranchingLogic(questDef.BranchingLogic)
	if err != nil {
		return nil, fmt.Errorf("failed to parse branching logic: %w", err)
	}

	if branchingLogic == nil {
		return nil, fmt.Errorf("quest has no branching logic defined")
	}

	// Get current branching node
	currentNodeID, err := s.repo.GetCurrentBranchingNode(ctx, playerID, questID)
	if err != nil {
		// If no current node set, use entry point
		currentNodeID = branchingLogic.EntryPoint
	}

	// Find current node
	currentNode, exists := branchingLogic.Nodes[currentNodeID]
	if !exists {
		return nil, fmt.Errorf("current branching node not found: %s", currentNodeID)
	}

	// Validate this is a choice node
	if currentNode.Type != "choice" {
		return nil, fmt.Errorf("current node is not a choice node: %s (type: %s)", currentNodeID, currentNode.Type)
	}

	// Find the selected option
	var selectedOption *repository.BranchOption
	for _, option := range currentNode.Options {
		if option.ID == choice.ChoiceValue {
			selectedOption = &option
			break
		}
	}

	if selectedOption == nil {
		return nil, fmt.Errorf("invalid choice option: %s", choice.ChoiceValue)
	}

	// Process the choice
	result := s.getChoiceResult() // Already allocated above

	// Set result based on selected option
	result.NewState = selectedOption.NextNode
	result.ReputationChanges = ReputationChange{} // TODO: Parse from rewards
	result.NextChoicePoint = selectedOption.NextNode
	result.QuestCompleted = selectedOption.NextNode == "" || selectedOption.NextNode == "end"

	// Update player reputation if rewards contain reputation changes
	if repChanges, ok := selectedOption.Rewards["reputation"].(map[string]interface{}); ok {
		if corporate, ok := repChanges["corporate"].(float64); ok {
			result.ReputationChanges.Corporate = int(corporate)
		}
		if street, ok := repChanges["street"].(float64); ok {
			result.ReputationChanges.Street = int(street)
		}
		if humanity, ok := repChanges["humanity"].(float64); ok {
			result.ReputationChanges.Humanity = int(humanity)
		}
	}

	// Update reputation
	if err := s.updatePlayerReputation(ctx, playerID, result.ReputationChanges); err != nil {
		return nil, fmt.Errorf("failed to update reputation: %w", err)
	}

	// Update branching state
	newBranchingState := map[string]interface{}{
		"current_node": selectedOption.NextNode,
		"choice_history": []map[string]interface{}{
			{
				"node":      currentNodeID,
				"choice":    choice.ChoiceValue,
				"timestamp": time.Now().Format(time.RFC3339),
			},
		},
	}

	branchingStateJSON, err := json.Marshal(newBranchingState)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal branching state: %w", err)
	}

	if err := s.repo.UpdatePlayerBranchingState(ctx, playerID, questID, branchingStateJSON); err != nil {
		return nil, fmt.Errorf("failed to update branching state: %w", err)
	}

	// Update quest state if completed
	if result.QuestCompleted {
		if err := s.repo.CompletePlayerQuest(ctx, playerID, questID, "branched_ending"); err != nil {
			return nil, fmt.Errorf("failed to complete quest: %w", err)
		}
		result.EndingAchieved = "branched_ending"
	}

	s.logger.Infof("JSONB branching choice processed: player=%s, quest=%s, node=%s, choice=%s, next=%s",
		playerID, questID, currentNodeID, choice.ChoiceValue, selectedOption.NextNode)

	resultPtr := result
	result = nil
	return resultPtr, nil
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
	// PERFORMANCE: Add context timeout for MMOFPS optimization
	timeoutCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repo.GetPlayerQuestState(timeoutCtx, playerID, questID)
}

// GetPlayerReputation retrieves player reputation with caching
func (s *Service) GetPlayerReputation(ctx context.Context, playerID string) (*repository.PlayerReputation, error) {
	// PERFORMANCE: Add context timeout for MMOFPS optimization
	timeoutCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.getCachedReputation(timeoutCtx, playerID)
}

// ListAvailableQuests returns quests available to a player based on level and reputation
func (s *Service) ListAvailableQuests(ctx context.Context, playerLevel int, reputation *repository.PlayerReputation) ([]*repository.QuestDefinition, error) {
	// PERFORMANCE: Add context timeout for MMOFPS optimization
	timeoutCtx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	// Get all quests (in production, this would be cached)
	allQuests, err := s.repo.ListQuestDefinitions(timeoutCtx, 100, 0)
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

// QuestYAML represents the structure of YAML quest files from knowledge/canon
type QuestYAML struct {
	Metadata struct {
		ID    string `yaml:"id"`
		Title string `yaml:"title"`
	} `yaml:"metadata"`
	QuestDefinition struct {
		QuestType string `yaml:"quest_type"`
		LevelMin  int    `yaml:"level_min"`
		LevelMax  int    `yaml:"level_max"`
		Objectives []struct {
			ID    string `yaml:"id"`
			Text  string `yaml:"text"`
			Type  string `yaml:"type"`
			Target string `yaml:"target"`
		} `yaml:"objectives"`
		Rewards struct {
			XP         int            `yaml:"xp"`
			Currency   int            `yaml:"currency"`
			Reputation map[string]int `yaml:"reputation"`
			SkillBoosts map[string]int `yaml:"skill_boosts"`
		} `yaml:"rewards"`
		Branches []struct {
			Condition string `yaml:"condition"`
			Outcome   string `yaml:"outcome"`
		} `yaml:"branches"`
	} `yaml:"quest_definition"`
}

// ImportQuestsFromYAML imports quests from YAML files
func (s *Service) ImportQuestsFromYAML(ctx context.Context, yamlData []byte) error {
	s.logger.Info("Starting quest import from YAML")

	var questYAML QuestYAML
	if err := yaml.Unmarshal(yamlData, &questYAML); err != nil {
		s.logger.Errorf("Failed to parse YAML quest data: %v", err)
		return fmt.Errorf("failed to parse YAML: %w", err)
	}

	// Convert YAML to DynamicQuest model
	dynamicQuest := &models.DynamicQuest{
		QuestID:      questYAML.Metadata.ID,
		Title:        questYAML.Metadata.Title,
		QuestType:    questYAML.QuestDefinition.QuestType,
		MinLevel:     questYAML.QuestDefinition.LevelMin,
		MaxLevel:     questYAML.QuestDefinition.LevelMax,
		Difficulty:   "medium", // Default difficulty
		Status:       "active",
		Themes:       []string{"outdoor", "lifestyle", "exploration"},
		ChoicePoints: s.convertObjectivesToChoicePoints(questYAML.QuestDefinition.Objectives),
		EndingVariations: []models.EndingVariation{
			{
				ID:          "default_ending",
				Title:       "Outdoor Day Completed",
				Description: "Successfully completed the outdoor lifestyle day",
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: questYAML.QuestDefinition.Rewards.XP,
					},
					{
						Type:  "currency",
						Value: questYAML.QuestDefinition.Rewards.Currency,
					},
				},
			},
		},
		ReputationImpacts: s.convertReputationToImpacts(questYAML.QuestDefinition.Rewards.Reputation),
		NarrativeSetup: models.NarrativeSetup{
			Location:   "Denver",
			TimePeriod: "2020-2029",
			Weather:    "sunny",
			Objectives: s.convertObjectivesToStrings(questYAML.QuestDefinition.Objectives),
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "local_guide",
				Name:        "Local Outdoor Guide",
				Role:        "Guide",
				Description: "Experienced outdoor enthusiast",
				Importance:  "secondary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	// Save to database
	if err := s.repo.CreateDynamicQuest(ctx, dynamicQuest); err != nil {
		s.logger.Errorf("Failed to save quest to database: %v", err)
		return fmt.Errorf("failed to save quest: %w", err)
	}

	s.logger.Infof("Successfully imported quest: %s", questYAML.Metadata.Title)
	return nil
}

// CreateQuestDefinition creates a new quest definition
func (s *Service) CreateQuestDefinition(ctx context.Context, quest *repository.QuestDefinition) error {
	return s.repo.CreateQuestDefinition(ctx, quest)
}

// GetQuestDefinition retrieves a quest definition by ID
func (s *Service) GetQuestDefinition(ctx context.Context, questID string) (*repository.QuestDefinition, error) {
	return s.repo.GetQuestDefinition(ctx, questID)
}

// Helper functions for conversion
func (s *Service) convertObjectivesToChoicePoints(objectives []struct {
	ID    string `yaml:"id"`
	Text  string `yaml:"text"`
	Type  string `yaml:"type"`
	Target string `yaml:"target"`
}) []models.ChoicePoint {
	choicePoints := make([]models.ChoicePoint, len(objectives))
	for i, obj := range objectives {
		choicePoints[i] = models.ChoicePoint{
			ID:       obj.ID,
			Sequence: i + 1,
			Title:    obj.Text,
			Description: fmt.Sprintf("Complete: %s", obj.Text),
			Context:  fmt.Sprintf("Objective type: %s, Target: %s", obj.Type, obj.Target),
			Choices: []models.Choice{
				{
					ID:             fmt.Sprintf("%s_complete", obj.ID),
					Text:           "Complete this objective",
					Description:    fmt.Sprintf("Successfully finish: %s", obj.Text),
					Consequences:   []models.Consequence{},
					RiskLevel:      "low",
					MoralAlignment: "neutral",
				},
			},
			Critical: false,
		}
	}
	return choicePoints
}

func (s *Service) convertReputationToImpacts(reputation map[string]int) []models.ReputationImpact {
	impacts := make([]models.ReputationImpact, 0, len(reputation))
	for faction, change := range reputation {
		impacts = append(impacts, models.ReputationImpact{
			Faction:     faction,
			Change:      change,
			Description: fmt.Sprintf("Reputation with %s changed by %d", faction, change),
			ChoiceID:    "quest_completion",
		})
	}
	return impacts
}

func (s *Service) convertObjectivesToStrings(objectives []struct {
	ID    string `yaml:"id"`
	Text  string `yaml:"text"`
	Type  string `yaml:"type"`
	Target string `yaml:"target"`
}) []string {
	objectivesStr := make([]string, len(objectives))
	for i, obj := range objectives {
		objectivesStr[i] = obj.Text
	}
	return objectivesStr
}

// GenerateQuestAnalytics generates comprehensive analytics for quest performance
func (s *Service) GenerateQuestAnalytics(ctx context.Context, questID string) (*models.QuestAnalytics, error) {
	// PERFORMANCE: Add context timeout for analytics generation
	timeoutCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	analytics := &models.QuestAnalytics{
		QuestID:            questID,
		PopularChoices:     make(map[string]int64),
		EndingDistribution: make(map[string]int64),
		DifficultyRatings:  make(map[string]float64),
		PlayerRetention:    make(map[string]int64),
	}

	// Get total players who started the quest
	totalPlayers, err := s.repo.GetQuestPlayerCount(timeoutCtx, questID)
	if err != nil {
		s.logger.Warnf("Failed to get player count for quest %s: %v", questID, err)
		// Continue with partial analytics
	}

	analytics.TotalPlayers = totalPlayers

	// Calculate completion rate
	if totalPlayers > 0 {
		completedCount, err := s.repo.GetQuestCompletedCount(timeoutCtx, questID)
		if err != nil {
			s.logger.Warnf("Failed to get completed count for quest %s: %v", questID, err)
		} else {
			analytics.CompletionRate = float64(completedCount) / float64(totalPlayers)
		}
	}

	// Analyze choice popularity
	choiceStats, err := s.repo.GetQuestChoiceStatistics(timeoutCtx, questID)
	if err != nil {
		s.logger.Warnf("Failed to get choice statistics for quest %s: %v", questID, err)
	} else {
		for _, stat := range choiceStats {
			analytics.PopularChoices[fmt.Sprintf("%s:%s", stat.ChoicePoint, stat.ChoiceValue)] = stat.Count
		}
	}

	// Analyze ending distribution
	endingStats, err := s.repo.GetQuestEndingStatistics(timeoutCtx, questID)
	if err != nil {
		s.logger.Warnf("Failed to get ending statistics for quest %s: %v", questID, err)
	} else {
		for _, stat := range endingStats {
			analytics.EndingDistribution[stat.Ending] = stat.Count
		}
	}

	// Calculate average difficulty ratings
	ratings, err := s.repo.GetQuestDifficultyRatings(timeoutCtx, questID)
	if err != nil {
		s.logger.Warnf("Failed to get difficulty ratings for quest %s: %v", questID, err)
	} else {
		ratingMap := make(map[string][]float64)
		for _, rating := range ratings {
			ratingMap[rating.Difficulty] = append(ratingMap[rating.Difficulty], rating.Rating)
		}

		for difficulty, ratingSlice := range ratingMap {
			sum := 0.0
			for _, r := range ratingSlice {
				sum += r
			}
			analytics.DifficultyRatings[difficulty] = sum / float64(len(ratingSlice))
		}
	}

	// Calculate player retention (players who completed vs started)
	if analytics.TotalPlayers > 0 && analytics.CompletionRate > 0 {
		retainedPlayers := int64(float64(analytics.TotalPlayers) * analytics.CompletionRate)
		analytics.PlayerRetention["completed"] = retainedPlayers
		analytics.PlayerRetention["abandoned"] = analytics.TotalPlayers - retainedPlayers
	}

	analytics.LastUpdated = time.Now()

	s.logger.Infof("Generated comprehensive analytics for quest %s: %d players, %.2f%% completion rate",
		questID, analytics.TotalPlayers, analytics.CompletionRate*100)

	return analytics, nil
}

// RecommendQuests recommends quests based on player preferences and history
func (s *Service) RecommendQuests(ctx context.Context, playerID string, limit int) ([]*models.DynamicQuest, error) {
	// PERFORMANCE: Add context timeout for recommendation generation
	timeoutCtx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	// Get player reputation for preference analysis
	reputation, err := s.repo.GetPlayerReputation(timeoutCtx, playerID)
	if err != nil {
		s.logger.Warnf("Failed to get reputation for recommendations: %v", err)
		// Continue with basic recommendations
	}

	// Get player's quest history
	history, err := s.repo.GetPlayerQuestHistory(timeoutCtx, playerID, 10) // Last 10 quests
	if err != nil {
		s.logger.Warnf("Failed to get quest history for recommendations: %v", err)
	}

	// Analyze player preferences from history
	preferences := s.analyzePlayerPreferences(history, reputation)

	// Get available quests
	availableQuests, err := s.ListAvailableQuests(timeoutCtx, s.calculatePlayerLevel(reputation), reputation)
	if err != nil {
		return nil, fmt.Errorf("failed to get available quests: %w", err)
	}

	// Score and rank quests based on preferences
	scoredQuests := s.scoreQuestsForPlayer(availableQuests, preferences, history)

	// Sort by score and return top recommendations
	sort.Slice(scoredQuests, func(i, j int) bool {
		return scoredQuests[i].score > scoredQuests[j].score
	})

	result := make([]*models.DynamicQuest, 0, limit)
	for i, scored := range scoredQuests {
		if i >= limit {
			break
		}
		result = append(result, scored.quest)
	}

	s.logger.Infof("Generated %d quest recommendations for player %s", len(result), playerID)
	return result, nil
}

// Quest scoring structure for recommendations
type scoredQuest struct {
	quest *models.DynamicQuest
	score float64
}

// analyzePlayerPreferences analyzes player's quest preferences from history
func (s *Service) analyzePlayerPreferences(history []*repository.PlayerQuestState, reputation *repository.PlayerReputation) map[string]float64 {
	preferences := make(map[string]float64)

	if len(history) == 0 {
		// Default preferences for new players
		preferences["main_story"] = 0.8
		preferences["side_quest"] = 0.6
		preferences["faction"] = 0.4
		return preferences
	}

	// Analyze completion patterns
	for _, quest := range history {
		if quest.EndingAchieved != "" {
			// Player completed this quest type
			preferences[quest.CurrentState] += 1.0
		} else {
			// Player abandoned this quest type
			preferences[quest.CurrentState] -= 0.5
		}
	}

	// Factor in reputation alignment
	if reputation != nil {
		if reputation.CorporateRep > 50 {
			preferences["corporate"] += 0.3
		}
		if reputation.StreetRep > 50 {
			preferences["street"] += 0.3
		}
		if reputation.HumanityScore > 50 {
			preferences["humanity"] += 0.3
		}
	}

	// Normalize preferences
	total := 0.0
	for _, score := range preferences {
		total += score
	}
	if total > 0 {
		for key := range preferences {
			preferences[key] /= total
		}
	}

	return preferences
}

// scoreQuestsForPlayer scores quests based on player preferences
func (s *Service) scoreQuestsForPlayer(availableQuests []*repository.QuestDefinition, preferences map[string]float64, history []*repository.PlayerQuestState) []scoredQuest {
	var scored []scoredQuest

	// Get recently played quest IDs to avoid repetition
	recentQuestIDs := make(map[string]bool)
	for _, quest := range history {
		recentQuestIDs[quest.QuestID] = true
	}

	for _, quest := range availableQuests {
		score := 0.0

		// Base score from preferences
		if pref, exists := preferences[quest.QuestType]; exists {
			score += pref * 50.0
		}

		// Penalty for recently played quests
		if recentQuestIDs[quest.QuestID] {
			score -= 30.0
		}

		// Bonus for quest difficulty matching player level
		playerLevel := s.calculatePlayerLevelFromHistory(history)
		levelDiff := abs(quest.MinLevel - playerLevel)
		if levelDiff <= 5 {
			score += 20.0
		} else if levelDiff <= 10 {
			score += 10.0
		}

		// Bonus for quests with branching logic (more replayable)
		if len(quest.BranchingLogic) > 0 {
			score += 15.0
		}

		scored = append(scored, scoredQuest{
			quest: &models.DynamicQuest{
				ID:          quest.QuestID,
				QuestID:     quest.QuestID,
				Title:       quest.Title,
				Description: quest.Description,
				QuestType:   quest.QuestType,
				MinLevel:    quest.MinLevel,
				MaxLevel:    quest.MaxLevel,
				Status:      "available",
			},
			score: score,
		})
	}

	return scored
}

// calculatePlayerLevel estimates player level from quest history
func (s *Service) calculatePlayerLevel(reputation *repository.PlayerReputation) int {
	if reputation == nil {
		return 1
	}

	// Simple level calculation based on reputation
	level := 1
	totalRep := reputation.CorporateRep + reputation.StreetRep + reputation.HumanityScore
	level += int(totalRep / 100) // Every 100 rep points = 1 level

	return clamp(level, 1, 50)
}

// calculatePlayerLevelFromHistory estimates player level from completed quests
func (s *Service) calculatePlayerLevelFromHistory(history []*repository.PlayerQuestState) int {
	maxLevel := 1
	for _, quest := range history {
		if quest.EndingAchieved != "" {
			// Estimate quest level from ID or other indicators
			// For now, use a simple heuristic
			maxLevel = max(maxLevel, 5) // Assume minimum level 5 for completed quests
		}
	}
	return maxLevel
}

// Helper functions
func abs(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

// Cache entry with timestamp for TTL management
type cacheEntry struct {
	data      interface{}
	timestamp time.Time
}

// getCachedQuest retrieves quest from cache or database
func (s *Service) getCachedQuest(ctx context.Context, questID string) (*repository.QuestDefinition, error) {
	// Check cache first
	if cached, ok := s.questCache.Load(questID); ok {
		entry := cached.(*cacheEntry)
		if time.Since(entry.timestamp) < s.cacheTTL {
			return entry.data.(*repository.QuestDefinition), nil
		}
		// Cache expired, remove it
		s.questCache.Delete(questID)
	}

	// Fetch from database
	quest, err := s.repo.GetQuestDefinition(ctx, questID)
	if err != nil {
		return nil, err
	}

	// Cache the result
	s.questCache.Store(questID, &cacheEntry{
		data:      quest,
		timestamp: time.Now(),
	})

	return quest, nil
}

// getCachedReputation retrieves player reputation from cache or database
func (s *Service) getCachedReputation(ctx context.Context, playerID string) (*repository.PlayerReputation, error) {
	// Check cache first
	if cached, ok := s.reputationCache.Load(playerID); ok {
		entry := cached.(*cacheEntry)
		if time.Since(entry.timestamp) < s.cacheTTL {
			return entry.data.(*repository.PlayerReputation), nil
		}
		// Cache expired, remove it
		s.reputationCache.Delete(playerID)
	}

	// Fetch from database
	reputation, err := s.repo.GetPlayerReputation(ctx, playerID)
	if err != nil {
		return nil, err
	}

	// Cache the result
	s.reputationCache.Store(playerID, &cacheEntry{
		data:      reputation,
		timestamp: time.Now(),
	})

	return reputation, nil
}

// invalidateQuestCache removes quest from cache (useful when quest is updated)
func (s *Service) invalidateQuestCache(questID string) {
	s.questCache.Delete(questID)
	s.logger.Debugf("Invalidated quest cache for: %s", questID)
}

// invalidateReputationCache removes player reputation from cache
func (s *Service) invalidateReputationCache(playerID string) {
	s.reputationCache.Delete(playerID)
	s.logger.Debugf("Invalidated reputation cache for player: %s", playerID)
}

// cacheCleanup periodically cleans expired cache entries
func (s *Service) cacheCleanup() {
	ticker := time.NewTicker(10 * time.Minute)
	defer ticker.Stop()

	for range ticker.C {
		now := time.Now()

		// Clean quest cache
		s.questCache.Range(func(key, value interface{}) bool {
			entry := value.(*cacheEntry)
			if now.Sub(entry.timestamp) > s.cacheTTL {
				s.questCache.Delete(key)
			}
			return true
		})

		// Clean reputation cache
		s.reputationCache.Range(func(key, value interface{}) bool {
			entry := value.(*cacheEntry)
			if now.Sub(entry.timestamp) > s.cacheTTL {
				s.reputationCache.Delete(key)
			}
			return true
		})

		s.logger.Debug("Cache cleanup completed")
	}
}

// Detroit Quests Implementation
// Issues: #140927952, #140927958, #140927959, #140927961, #140927963

// GetConeyIslandHotDogsQuest returns the Coney Island Hot Dogs quest for Detroit
func (s *Service) GetConeyIslandHotDogsQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Coney Island Hot Dogs quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "coney-island-hot-dogs-detroit-2020-2029",
		Title:            "Хот-доги Кони-Айленд",
		Description:      "Восстановить традицию хот-догов Кони-Айленд в заброшенном парке развлечений Детройта",
		QuestType:        "narrative_side",
		MinLevel:         8,
		MaxLevel:         15,
		EstimatedDuration: 45,
		Difficulty:       "medium",
		Themes:           []string{"street_food", "corporate_resistance", "cultural_revitalization"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "park_exploration",
				Sequence:    1,
				Title:       "Исследование парка",
				Description: "Исследовать заброшенный парк Кони-Айленд",
				Context:     "Парк полон воспоминаний о прошлом великолепии Детройта",
				Choices: []models.Choice{
					{
						ID:             "careful_search",
						Text:           "Тщательно обыскать парк",
						Description:    "Найти подсказки о рецепте хот-догов",
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "corporate_bribe",
						Text:           "Подкупить корпоративного менеджера",
						Description:    "Получить информацию от корпорации",
						RiskLevel:      "medium",
						MoralAlignment: "evil",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "park_restored",
				Title:       "Парк восстановлен",
				Description: "Традиция хот-догов спасена",
				Rewards: []models.Reward{
					{Type: "experience", Value: 8900},
					{Type: "currency", Value: 6200},
					{Type: "item", ItemID: "coney_island_recipe", Rarity: "legendary"},
				},
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Detroit, Coney Island Amusement Park",
			TimePeriod:  "2020-2029",
			Weather:     "overcast with occasional rain",
			Situation:   "The park stands abandoned, a relic of Detroit's golden age",
			Objectives: []string{
				"Find the legendary hot dog recipe",
				"Restore the park's food stalls",
				"Host a street food festival",
				"Protect the park from corporate demolition",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "old_cook",
				Name:        "Марио 'Красавчик' Росси",
				Role:        "Старый повар",
				Description: "Последний хранитель рецепта хот-догов Кони-Айленд",
				Importance:  "primary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// Get1967RiotsLegacyQuest returns the 1967 Riots Legacy quest for Detroit
func (s *Service) Get1967RiotsLegacyQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving 1967 Riots Legacy quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "1967-riots-legacy-detroit-2020-2029",
		Title:            "Наследие бунтов 1967 года",
		Description:      "Исследовать последствия бунтов 1967 года и их влияние на современный Детройт",
		QuestType:        "narrative_main",
		MinLevel:         12,
		MaxLevel:         20,
		EstimatedDuration: 60,
		Difficulty:       "hard",
		Themes:           []string{"historical_trauma", "racial_justice", "urban_decay", "social_change"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "family_history",
				Sequence:    1,
				Title:       "Семейная история",
				Description: "Узнать правду о участии семьи в событиях 1967 года",
				Context:     "Семья хранит темные секреты о тех событиях",
				Choices: []models.Choice{
					{
						ID:             "investigate_gently",
						Text:           "Аккуратно расспросить родственников",
						Description:    "Избежать конфликтов в семье",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "confront_directly",
						Text:           "Прямо потребовать правду",
						Description:    "Риск разрыва отношений",
						RiskLevel:      "high",
						MoralAlignment: "neutral",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "reconciliation",
				Title:       "Примирение",
				Description: "Семья обретает мир с прошлым",
				Rewards: []models.Reward{
					{Type: "experience", Value: 12500},
					{Type: "reputation", Value: "detroit_community:+25"},
					{Type: "item", ItemID: "family_relic", Rarity: "epic"},
				},
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Detroit, Various Neighborhoods",
			TimePeriod:  "2020-2029",
			Weather:     "mixed, reflecting the city's turbulent history",
			Situation:   "The wounds of 1967 still bleed in Detroit's collective consciousness",
			Objectives: []string{
				"Investigate family involvement in the 1967 riots",
				"Interview survivors and witnesses",
				"Visit historical sites from the riots",
				"Confront buried truths about the past",
				"Help community healing process",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "grandmother",
				Name:        "Бабушка Эстер",
				Role:        "Семейный хранитель истории",
				Description: "Хранит семейные секреты о событиях 1967 года",
				Importance:  "primary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// Get8MileRoadJourneyQuest returns the 8 Mile Road Journey quest for Detroit
func (s *Service) Get8MileRoadJourneyQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving 8 Mile Road Journey quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "8-mile-road-journey-detroit-2020-2029",
		Title:            "Путешествие по дороге 8-Майл",
		Description:      "Пройти по легендарной дороге 8-Майл и раскрыть её секреты",
		QuestType:        "narrative_side",
		MinLevel:         10,
		MaxLevel:         18,
		EstimatedDuration: 50,
		Difficulty:       "medium",
		Themes:           []string{"urban_exploration", "personal_growth", "detroit_mythology", "racial_divide"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "road_choice",
				Sequence:    1,
				Title:       "Выбор пути",
				Description: "Как пройти по дороге 8-Майл",
				Context:     "Дорога символизирует разделение между мирами",
				Choices: []models.Choice{
					{
						ID:             "walk_alone",
						Text:           "Пойти пешком в одиночку",
						Description:    "Личное путешествие самопознания",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
					{
						ID:             "take_transport",
						Text:           "Использовать транспорт",
						Description:    "Быстрый и безопасный маршрут",
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "journey_completed",
				Title:       "Путешествие завершено",
				Description: "Секреты 8-Майл раскрыты",
				Rewards: []models.Reward{
					{Type: "experience", Value: 10200},
					{Type: "currency", Value: 7500},
					{Type: "reputation", Value: "detroit_underground:+20"},
				},
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Detroit, 8 Mile Road",
			TimePeriod:  "2020-2029",
			Weather:     "overcast with urban haze",
			Situation:   "The legendary road divides Detroit's worlds, holding ancient secrets",
			Objectives: []string{
				"Travel the length of 8 Mile Road",
				"Discover hidden landmarks and stories",
				"Interact with local legends",
				"Uncover the road's mystical significance",
				"Complete personal transformation",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "road_mentor",
				Name:        "Джеймс 'Призрак' Уилсон",
				Role:        "Хранитель секретов 8-Майл",
				Description: "Легендарная фигура, знающая все тайны дороги",
				Importance:  "primary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetLaBocaCaminitoQuest returns the La Boca Caminito quest for Buenos Aires
// Issue: #140929844
func (s *Service) GetLaBocaCaminitoQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving La Boca Caminito quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-buenos-aires-2029-002-caminito",
		Title:            "Буэнос-Айрес: La Boca и Caminito",
		Description:      "Исследуйте яркий район La Boca, прогуляйтесь по знаменитой улице Caminito и погрузитесь в культуру итальянских иммигрантов",
		QuestType:        "side",
		MinLevel:         1,
		MaxLevel:         0, // No max level
		EstimatedDuration: 120,
		Difficulty:       "easy",
		Themes:           []string{"culture", "art", "sports", "history", "tourism"},
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "caminito_color_walk",
				Sequence:    1,
				Title:       "Прогулка по Caminito",
				Description: "Пройдитесь по знаменитой пешеходной улице с разноцветными домами",
				Context:     "Начало вашего путешествия по самому яркому району Буэнос-Айреса",
				Choices: []models.Choice{
					{
						ID:             "photograph_colors",
						Text:           "Фотографировать разноцветные дома",
						Description:    "Сделать серию фотографий уникальной архитектуры района",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "aesthetics",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение эстетической репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(5),
								Probability: 1.0,
								Description: "Улучшение навыка культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "buy_art_souvenirs",
						Text:           "Купить художественные сувениры",
						Description:    "Приобрести местные поделки и картины",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "aesthetics",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение эстетической репутации",
							},
							{
								Type:        "currency",
								Target:      "player_wallet",
								Value:       float64(-10),
								Probability: 1.0,
								Description: "Покупка сувениров",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "street_artists_dialogue",
				Sequence:    2,
				Title:       "Встреча с уличными артистами",
				Description: "Поговорите с танцорами и музыкантами на улицах La Boca",
				Context:     "Продолжите знакомство с живой культурой района",
				Choices: []models.Choice{
					{
						ID:             "learn_tango_basics",
						Text:           "Взять мини-урок танго",
						Description:    "Освоить базовые шаги аргентинского танго",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "dance_skill",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Улучшение навыка танцев",
							},
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "listen_to_stories",
						Text:           "Послушать истории артистов",
						Description:    "Узнать о жизни района от местных жителей",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Улучшение навыка культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "artist_workshop_visit",
				Sequence:    3,
				Title:       "Мастерская художников",
				Description: "Посетите студию, где узнаете историю разноцветных красок района",
				Context:     "Погрузитесь в художественное наследие La Boca",
				Choices: []models.Choice{
					{
						ID:             "paint_with_artists",
						Text:           "Попробовать рисовать",
						Description:    "Взять кисть и создать собственное произведение",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "artistic_skill",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Улучшение художественных навыков",
							},
							{
								Type:        "reputation",
								Target:      "aesthetics",
								Value:       float64(18),
								Probability: 1.0,
								Description: "Значительное увеличение эстетической репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "learn_paint_history",
						Text:           "Изучить историю красок",
						Description:    "Узнать о корабельной краске и традициях района",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное увеличение культурной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Значительное улучшение культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "bombonera_stadium",
				Sequence:    4,
				Title:       "Стадион La Bombonera",
				Description: "Посетите легендарный стадион Boca Juniors и познакомьтесь с фан-культурой",
				Context:     "Кульминация путешествия - сердце аргентинского футбола",
				Choices: []models.Choice{
					{
						ID:             "join_fans",
						Text:           "Присоединиться к фанатам",
						Description:    "Петь песни и поддерживать команду вместе с болельщиками",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное увеличение социальной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "social_connections",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Улучшение навыка социальных связей",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
					{
						ID:             "explore_history",
						Text:           "Изучить историю стадиона",
						Description:    "Узнать о легендах Boca Juniors и Диего Марадоне",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Значительное увеличение культурной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное улучшение культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "final_event_choice",
				Sequence:    5,
				Title:       "Финальное событие",
				Description: "Выберите, как завершить день в La Boca",
				Context:     "Ваш последний выбор определит впечатление от района",
				Choices: []models.Choice{
					{
						ID:             "evening_milonga",
						Text:           "Вечерняя милонга",
						Description:    "Закончить день танцами под звездами",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(30),
								Probability: 1.0,
								Description: "Максимальное увеличение культурной репутации",
							},
							{
								Type:        "achievement",
								Target:      "la_boca_master",
								Value:       float64(1),
								Probability: 1.0,
								Description: "Получение достижения 'Хозяин La Boca'",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "street_celebration",
						Text:           "Уличное празднование",
						Description:    "Присоединиться к спонтанному празднику на улицах",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Значительное увеличение социальной репутации",
							},
							{
								Type:        "achievement",
								Target:      "la_boca_master",
								Value:       float64(1),
								Probability: 1.0,
								Description: "Получение достижения 'Хозяин La Boca'",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "la_boca_artist",
				Title:       "Художник La Boca",
				Description: "Стали частью художественного наследия района",
				Requirements: []string{"paint_with_artists", "learn_paint_history", "evening_milonga"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1200},
					{Type: "currency", Value: -20},
					{Type: "achievement", Value: "la_boca_artist"},
				},
				Narrative: "Искусство La Boca теперь течет в ваших венах, как разноцветная краска на стенах района.",
			},
			{
				ID:          "boca_fan",
				Title:       "Фанат Boca Juniors",
				Description: "Завоевали сердца болельщиков легендарного клуба",
				Requirements: []string{"join_fans", "explore_history", "street_celebration"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1100},
					{Type: "currency", Value: -15},
					{Type: "achievement", Value: "boca_fan"},
				},
				Narrative: "Вы стали частью великой традиции - поклонения команде, которая живет в сердцах миллионов.",
			},
			{
				ID:          "cultural_explorer",
				Title:       "Культурный исследователь",
				Description: "Глубоко поняли дух итальянских иммигрантов La Boca",
				Requirements: []string{"photograph_colors", "listen_to_stories", "learn_paint_history"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1000},
					{Type: "currency", Value: -10},
					{Type: "achievement", Value: "cultural_explorer_la_boca"},
				},
				Narrative: "История района открылась вам, как страницы старой книги о мечтах иммигрантов.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "la_boca_locals",
				Change:      20,
				Description: "Уважение местных жителей за интерес к их культуре",
				ChoiceID:    "evening_milonga",
			},
			{
				Faction:     "boca_juniors_fans",
				Change:      15,
				Description: "Симпатия фанатов Boca Juniors",
				ChoiceID:    "join_fans",
			},
			{
				Faction:     "buenos_aires_artists",
				Change:      18,
				Description: "Уважение художественного сообщества",
				ChoiceID:    "paint_with_artists",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "La Boca, Buenos Aires, Argentina",
			TimePeriod:  "2020-2029",
			Weather:     "warm and colorful afternoon",
			Situation:   "The vibrant streets of La Boca call to you with their rainbow houses and passionate culture",
			Objectives: []string{
				"Walk the colorful Caminito street and capture its beauty",
				"Meet street artists and learn about tango traditions",
				"Visit artist workshops and discover the paint history",
				"Explore La Bombonera stadium and Boca Juniors culture",
				"Choose your final celebration in La Boca",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "street_artist",
				Name:        "Мария 'Ла Артиста' Гонсалес",
				Role:        "Уличная художница",
				Description: "Местная артистка, знающая все секреты красок La Boca",
				Importance:  "primary",
			},
			{
				ID:          "tango_dancer",
				Name:        "Карлос 'Эль Тангуэро' Родригес",
				Role:        "Уличный танцор",
				Description: "Страстный тангеро, готовый научить основам танго",
				Importance:  "secondary",
			},
			{
				ID:          "boca_fan",
				Name:        "Хуан 'Ла Бомбонера' Мартинес",
				Role:        "Фанат Boca Juniors",
				Description: "Завзятый болельщик, знающий все легенды клуба",
				Importance:  "secondary",
			},
			{
				ID:          "museum_curator",
				Name:        "Доктор Анна 'Ла Хисторика' Лопес",
				Role:        "Куратор музея",
				Description: "Эксперт по истории итальянских иммигрантов в La Boca",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetAsadoBBQQuest returns the Asado BBQ quest for Buenos Aires
// Issue: #140929848
func (s *Service) GetAsadoBBQQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Asado BBQ quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-buenos-aires-2029-003-asado",
		Title:            "Буэнос-Айрес: Асадо",
		Description:      "Присоединитесь к традиционному аргентинскому барбекю - священному социальному ритуалу, где мясо, вино и дружба создают незабываемую атмосферу",
		QuestType:        "side",
		MinLevel:         2,
		MaxLevel:         0, // No max level
		EstimatedDuration: 240,
		Difficulty:       "easy",
		Themes:           []string{"cuisine", "social", "culture", "tradition", "food"},
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "asado_invitation",
				Sequence:    1,
				Title:       "Приглашение на асадо",
				Description: "Получите приглашение на традиционное аргентинское барбекю",
				Context:     "Начало вашего погружения в культуру аргентинской кухни",
				Choices: []models.Choice{
					{
						ID:             "accept_formal",
						Text:           "Принять формальное приглашение",
						Description:    "Получить официальное приглашение от семьи",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(5),
								Probability: 1.0,
								Description: "Улучшение навыка культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "join_spontaneous",
						Text:           "Присоединиться спонтанно",
						Description:    "Услышать о мероприятии и присоединиться",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "social_connections",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Улучшение навыка социальных связей",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "grill_preparation",
				Sequence:    2,
				Title:       "Подготовка паррильи",
				Description: "Помогите настроить традиционный аргентинский гриль",
				Context:     "Ключевой момент - правильная подготовка огня",
				Choices: []models.Choice{
					{
						ID:             "arrange_coals",
						Text:           "Уложить угли правильно",
						Description:    "Создать идеальный жар для разных типов мяса",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "cooking_skill",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Улучшение навыка приготовления пищи",
							},
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "learn_fire_technique",
						Text:           "Изучить технику огня",
						Description:    "Освоить традиционные методы разжигания огня",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "survival_skill",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Улучшение навыка выживания",
							},
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "meat_selection",
				Sequence:    3,
				Title:       "Выбор и подготовка мяса",
				Description: "Помогите выбрать лучшие куски говядины пампасов",
				Context:     "Сердце асадо - правильный выбор мяса",
				Choices: []models.Choice{
					{
						ID:             "select_premium",
						Text:           "Выбрать премиум куски",
						Description:    "Отобрать лучшие стейки и колбаски",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
							{
								Type:        "currency",
								Target:      "player_wallet",
								Value:       float64(-15),
								Probability: 1.0,
								Description: "Вклад в покупку мяса",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "prepare_traditional",
						Text:           "Подготовить традиционно",
						Description:    "Нарезать и замариновать мясо по традициям",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "cooking_skill",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Улучшение навыка приготовления пищи",
							},
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(18),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "wine_service",
				Sequence:    4,
				Title:       "Сервис вин и закусок",
				Description: "Подайте знаменитое вино Мальбек и традиционные закуски",
				Context:     "Вино - обязательный элемент настоящего асадо",
				Choices: []models.Choice{
					{
						ID:             "serve_malbec",
						Text:           "Подать Мальбек",
						Description:    "Выбрать и разлить идеальное аргентинское вино",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное увеличение социальной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "social_grace",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Улучшение навыка светского общения",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "prepare_chimichurri",
						Text:           "Приготовить чиммичурри",
						Description:    "Сделать традиционный аргентинский соус",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "cooking_skill",
								Value:       float64(18),
								Probability: 1.0,
								Description: "Значительное улучшение навыка приготовления пищи",
							},
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(22),
								Probability: 1.0,
								Description: "Значительное увеличение культурной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "social_gathering",
				Sequence:    5,
				Title:       "Социальное общение",
				Description: "Проведите время в разговорах и историях у костра",
				Context:     "Кульминация асадо - укрепление социальных связей",
				Choices: []models.Choice{
					{
						ID:             "share_stories",
						Text:           "Делиться историями",
						Description:    "Рассказать о своих приключениях и выслушать других",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Значительное увеличение социальной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "social_connections",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Значительное улучшение навыка социальных связей",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "learn_traditions",
						Text:           "Изучить традиции",
						Description:    "Узнать больше об истории и культуре асадо",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное увеличение культурной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(18),
								Probability: 1.0,
								Description: "Значительное улучшение культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "asado_master",
				Title:       "Мастер Асадо",
				Description: "Стали настоящим экспертом в аргентинском барбекю",
				Requirements: []string{"arrange_coals", "prepare_traditional", "prepare_chimichurri", "share_stories"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 2000},
					{Type: "achievement", Value: "asado_master"},
					{Type: "buff", Value: "hp_buff:+35"}, // 12 hour duration
				},
				Narrative: "Вы стали частью великой традиции асадо, где мясо, вино и дружба создают настоящую магию.",
			},
			{
				ID:          "social_connector",
				Title:       "Социальный коннектор",
				Description: "Укрепили связи и создали новые дружбы",
				Requirements: []string{"join_spontaneous", "select_premium", "serve_malbec", "share_stories"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1800},
					{Type: "achievement", Value: "social_connector"},
					{Type: "buff", Value: "social_buff:+25"}, // 24 hour duration
				},
				Narrative: "Асадо стало мостом между культурами, а вы - тем, кто помогает людям находить общий язык.",
			},
			{
				ID:          "cultural_enthusiast",
				Title:       "Культурный энтузиаст",
				Description: "Глубоко прониклись аргентинскими традициями",
				Requirements: []string{"learn_fire_technique", "prepare_traditional", "learn_traditions"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1600},
					{Type: "achievement", Value: "cultural_enthusiast"},
					{Type: "buff", Value: "cultural_buff:+30"}, // 18 hour duration
				},
				Narrative: "Традиции асадо открыли вам дверь в богатую культуру Аргентины и ее людей.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "buenos_aires_families",
				Change:      25,
				Description: "Уважение аргентинских семей за участие в традиции",
				ChoiceID:    "share_stories",
			},
			{
				Faction:     "argentinian_culture",
				Change:      20,
				Description: "Уважение за интерес к аргентинской культуре",
				ChoiceID:    "learn_traditions",
			},
			{
				Faction:     "social_buenos_aires",
				Change:      18,
				Description: "Улучшение социальных связей в городе",
				ChoiceID:    "serve_malbec",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Buenos Aires Backyard, Argentina",
			TimePeriod:  "2020-2029",
			Weather:     "warm summer evening with gentle breeze",
			Situation:   "The sacred tradition of asado calls you to join the gathering of family and friends around the parrilla",
			Objectives: []string{
				"Receive invitation and prepare ingredients for the traditional barbecue",
				"Help the parrillero set up the grill and control cooking temperatures",
				"Select and serve Malbec wine with traditional accompaniments",
				"Engage in social conversation and storytelling around the fire",
				"Complete the ritual with a group toast and family photo",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "parrilla_master",
				Name:        "Карлос 'Эль Паррильеро' Сантьяго",
				Role:        "Мастер гриля",
				Description: "Опытный повар, знающий все секреты идеального асадо",
				Importance:  "primary",
			},
			{
				ID:          "family_host",
				Name:        "Мария 'Ла Ама де Каса' Гонсалес",
				Role:        "Хозяйка дома",
				Description: "Теплая и гостеприимная мать семейства, организатор традиций",
				Importance:  "secondary",
			},
			{
				ID:          "wine_expert",
				Name:        "Антонио 'Виньеро' Мендес",
				Role:        "Знаток вин",
				Description: "Эксперт по аргентинским винам, особенно Мальбеку",
				Importance:  "secondary",
			},
			{
				ID:          "storyteller",
				Name:        "Хорхе 'Эль Куэнтиста' Лопес",
				Role:        "Рассказчик",
				Description: "Мастер устных историй и традиций старой Аргентины",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetRecoletaCemeteryQuest returns the Recoleta Cemetery quest for Buenos Aires
// Issue: #140929854
func (s *Service) GetRecoletaCemeteryQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Recoleta Cemetery quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-buenos-aires-2029-004-recoleta",
		Title:            "Буэнос-Айрес: Кладбище Реколета",
		Description:      "Исследуйте 'город мёртвых' - кладбище Реколета с более чем 6400 мавзолеями, узнайте о культурном наследии Эвиты Перон и познакомьтесь с кошачьими хранителями.",
		QuestType:        "side",
		MinLevel:         1,
		MaxLevel:         0, // No max level
		EstimatedDuration: 90,
		Difficulty:       "easy",
		Themes:           []string{"heritage", "memorial", "architecture", "cultural"},
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "cemetery_entrance",
				Sequence:    1,
				Title:       "Вход на кладбище",
				Description: "Как вы подойдете к этому священному месту?",
				Context:     "Вы стоите у входа на кладбище Реколета, одного из самых красивых кладбищ мира.",
				Choices: []models.Choice{
					{
						ID:             "respectful_entrance",
						Text:           "Войти с уважением и почтением",
						Description:    "Показать уважение к месту упокоения",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "heritage",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение репутации наследия",
							},
							{
								Type:        "xp",
								Value:       float64(30),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "curious_entrance",
						Text:           "Войти с любопытством и интересом",
						Description:    "Показать искренний интерес к истории",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "xp",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "mausoleum_exploration",
				Sequence:    2,
				Title:       "Исследование мавзолеев",
				Description: "Как вы будете изучать мавзолеи?",
				Context:     "Вы гуляете среди величественных мраморных мавзолеев, каждый из которых рассказывает историю семьи.",
				Choices: []models.Choice{
					{
						ID:             "read_inscriptions",
						Text:           "Читать надписи и изучать истории семей",
						Description:    "Узнать о людях, чьи жизни запечатлены здесь",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "heritage",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Увеличение репутации наследия",
							},
							{
								Type:        "xp",
								Value:       float64(50),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "admire_architecture",
						Text:           "Восхищаться архитектурой и искусством",
						Description:    "Оценить красоту и мастерство скульпторов",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "xp",
								Value:       float64(45),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "evita_tomb_encounter",
				Sequence:    3,
				Title:       "Гробница Эвиты Перон",
				Description: "Как вы подойдете к гробнице Эвиты?",
				Context:     "Вы нашли гробницу Эвиты Перон - самой посещаемой на кладбище. Здесь стоит хранитель памяти.",
				Choices: []models.Choice{
					{
						ID:             "pay_respects",
						Text:           "Почтить память и выразить уважение",
						Description:    "Показать глубокое уважение к исторической фигуре",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "heritage",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение репутации наследия",
							},
							{
								Type:        "xp",
								Value:       float64(80),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "learn_history",
						Text:           "Узнать о ее жизни и наследии",
						Description:    "Поговорить с хранителем о истории Эвиты",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "xp",
								Value:       float64(70),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "cat_guardian_interaction",
				Sequence:    4,
				Title:       "Кошачьи хранители",
				Description: "Как вы будете взаимодействовать с кошками-стражами?",
				Context:     "На узких улицах склепов вы встречаете кошек, которые считаются хранителями кладбища.",
				Choices: []models.Choice{
					{
						ID:             "befriend_cats",
						Text:           "Подружиться с кошками",
						Description:    "Показать доброту и уважение к местным традициям",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "heritage",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Увеличение репутации наследия",
							},
							{
								Type:        "xp",
								Value:       float64(60),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "observe_tradition",
						Text:           "Наблюдать за традицией кошачьей охраны",
						Description:    "Изучить роль кошек в истории кладбища",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "xp",
								Value:       float64(55),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "memory_ritual_conclusion",
				Sequence:    5,
				Title:       "Ритуал памяти",
				Description: "Как вы завершите свою экскурсию?",
				Context:     "Вы завершаете экскурсию по кладбищу Реколета и можете выполнить мини-ритуал памяти.",
				Choices: []models.Choice{
					{
						ID:             "reflect_quietly",
						Text:           "Размышлять в тишине о бренности жизни",
						Description:    "Найти момент для личных размышлений",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "heritage",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение репутации наследия",
							},
							{
								Type:        "xp",
								Value:       float64(90),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "leave_respectful",
						Text:           "Уйти с уважением к этому месту",
						Description:    "Показать полное уважение к традиции",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "xp",
								Value:       float64(75),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "heritage_guardian",
				Title:       "Хранитель Наследия",
				Description: "Вы глубоко прониклись уважением к истории и культурному наследию кладбища Реколета.",
				Requirements: []string{"respectful_entrance", "read_inscriptions", "pay_respects", "befriend_cats", "reflect_quietly"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1500},
					{Type: "achievement", Value: "Recoleta Guardian"},
					{Type: "item", ItemID: "recoleta_memorial_token", Rarity: "rare"},
				},
				Narrative: "Реколета стала частью вашей души. Вы чувствуете связь с поколениями, которые нашли здесь свой последний приют.",
			},
			{
				ID:          "cultural_explorer",
				Title:       "Культурный Исследователь",
				Description: "Вы изучили архитектуру, историю и традиции кладбища Реколета.",
				Requirements: []string{"curious_entrance", "admire_architecture", "learn_history", "observe_tradition", "leave_respectful"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1400},
					{Type: "achievement", Value: "Recoleta Scholar"},
					{Type: "item", ItemID: "recoleta_history_book", Rarity: "uncommon"},
				},
				Narrative: "Вы уносите с собой не только фотографии, но и глубокое понимание культурного значения этого уникального места.",
			},
			{
				ID:          "memory_keeper",
				Title:       "Хранитель Памяти",
				Description: "Вы нашли особое значение в ритуалах памяти и уважении к прошлому.",
				Requirements: []string{"respectful_entrance", "pay_respects", "befriend_cats", "reflect_quietly"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1450},
					{Type: "achievement", Value: "Memory Keeper"},
					{Type: "item", ItemID: "recoleta_ritual_item", Rarity: "rare"},
				},
				Narrative: "Память о прошлом стала вашим компасом. Вы чувствуете ответственность за сохранение этих историй.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "buenos_aires_heritage",
				Change:      20,
				Description: "Уважение к культурному наследию Буэнос-Айреса",
				ChoiceID:    "pay_respects",
			},
			{
				Faction:     "recoleta_tradition",
				Change:      15,
				Description: "Понимание традиций кладбища Реколета",
				ChoiceID:    "befriend_cats",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Recoleta Cemetery, Buenos Aires, Argentina",
			TimePeriod:  "2020-2029",
			Weather:     "calm, reflective atmosphere",
			Situation:   "Explore the 'City of the Dead' - Recoleta Cemetery with over 6400 mausoleums, learn about Evita Peron's cultural heritage and meet the feline guardians.",
			Objectives: []string{
				"Enter Recoleta Cemetery and get a route map",
				"Examine marble mausoleums and learn about family histories",
				"Find Evita Peron's tomb and speak with the memory keeper",
				"Explore the narrow streets of crypts with 'feline guardians'",
				"Complete the excursion with a mini memory ritual",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "memory_keeper_elena",
				Name:        "Элена 'Хранительница Памяти' Гонсалес",
				Role:        "Хранительница памяти",
				Description: "Пожилая женщина, посвятившая жизнь сохранению историй кладбища Реколета",
				Importance:  "primary",
			},
			{
				ID:          "cat_caretaker_miguel",
				Name:        "Мигель 'Кошачий Хранитель' Сантьяго",
				Role:        "Ухаживающий за кошками",
				Description: "Местный волонтер, заботящийся о популяции кошек на кладбище",
				Importance:  "secondary",
			},
			{
				ID:          "architectural_guide",
				Name:        "Доктор Карлос 'Архитектор' Мендоса",
				Role:        "Историк архитектуры",
				Description: "Эксперт по мавзолеям и истории кладбищенской архитектуры",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}
	return quest, nil
}

// GetParisOfTheSouthQuest returns the Paris of the South quest for Buenos Aires
// Issue: #140929855
func (s *Service) GetParisOfTheSouthQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Paris of the South quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-paris-of-the-south-buenos-aires-2020-2029",
		Title:            "Буэнос-Айрес: Париж Южной Америки",
		Description:      "Буэнос-Айрес, известный как 'Париж Южной Америки', находится под угрозой глобальной стандартизации. Корпорации сносят исторические здания belle epoque, заменяют европейские кафе на азиатские фастфуды. Сохраните уникальное сочетание европейской элегантности и латиноамериканской страсти.",
		QuestType:        "narrative_side",
		MinLevel:         20,
		MaxLevel:         0, // No max level
		EstimatedDuration: 360,
		Difficulty:       "hard",
		Themes:           []string{"cultural_preservation", "resistance", "heritage", "globalization", "identity"},
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "investigate_corporate_threat",
				Sequence:    1,
				Title:       "Расследование корпоративной угрозы",
				Description: "Как вы начнете борьбу с глобальной стандартизацией?",
				Context:     "Вы узнаете о планах корпораций снести исторический квартал Recoleta и заменить его на азиатский торговый комплекс.",
				Choices: []models.Choice{
					{
						ID:             "gather_evidence",
						Text:           "Собрать доказательства культурной ценности",
						Description:    "Документировать историческую и архитектурную ценность зданий",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural_heritage_defenders",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение репутации защитников наследия",
							},
							{
								Type:        "xp",
								Value:       float64(200),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
					{
						ID:             "contact_locals",
						Text:           "Связаться с местными жителями",
						Description:    "Найти людей, которые помнят старую Европу",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "european_tradition_preservation",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Увеличение репутации хранителей традиций",
							},
							{
								Type:        "xp",
								Value:       float64(180),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "form_resistance_group",
				Sequence:    2,
				Title:       "Формирование группы сопротивления",
				Description: "Как вы объедините защитников культурного наследия?",
				Context:     "Вы нашли несколько людей, готовых бороться за сохранение европейской идентичности Буэнос-Айреса.",
				Choices: []models.Choice{
					{
						ID:             "architectural_experts",
						Text:           "Привлечь архитекторов и историков",
						Description:    "Собрать команду профессионалов для защиты зданий",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "architectural_conservationists",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Увеличение репутации архитекторов-консерваторов",
							},
							{
								Type:        "xp",
								Value:       float64(300),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
					{
						ID:             "cultural_activists",
						Text:           "Организовать культурных активистов",
						Description:    "Собрать художников, музыкантов и артистов",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural_diversity_advocates",
								Value:       float64(18),
								Probability: 1.0,
								Description: "Увеличение репутации защитников культурного разнообразия",
							},
							{
								Type:        "xp",
								Value:       float64(280),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "defend_historic_building",
				Sequence:    3,
				Title:       "Защита исторического здания",
				Description: "Как вы защитите здание от сноса?",
				Context:     "Корпорация начинает демонтаж исторического здания в стиле belle epoque. Ваша группа готова действовать.",
				Choices: []models.Choice{
					{
						ID:             "legal_protest",
						Text:           "Организовать юридический протест",
						Description:    "Подать иски и собрать юридические доказательства",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "corporate_globalization",
								Value:       float64(-15),
								Probability: 1.0,
								Description: "Снижение репутации у корпораций",
							},
							{
								Type:        "xp",
								Value:       float64(400),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "public_campaign",
						Text:           "Запустить общественную кампанию",
						Description:    "Организовать митинги и медиа-кампанию",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural_heritage_defenders",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Значительное увеличение репутации защитников",
							},
							{
								Type:        "xp",
								Value:       float64(350),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "cultural_renaissance",
				Sequence:    4,
				Title:       "Культурное возрождение",
				Description: "Как вы возродите европейскую культуру в городе?",
				Context:     "Здание спасено, но нужно показать жителям ценность европейского наследия.",
				Choices: []models.Choice{
					{
						ID:             "underground_cafes",
						Text:           "Организовать подпольные кафе",
						Description:    "Создать тайные места с европейской атмосферой",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "european_tradition_preservation",
								Value:       float64(30),
								Probability: 1.0,
								Description: "Значительное увеличение репутации хранителей традиций",
							},
							{
								Type:        "xp",
								Value:       float64(500),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "art_exhibitions",
						Text:           "Устроить художественные выставки",
						Description:    "Показать европейское искусство и архитектуру",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural_diversity_advocates",
								Value:       float64(22),
								Probability: 1.0,
								Description: "Увеличение репутации защитников разнообразия",
							},
							{
								Type:        "xp",
								Value:       float64(450),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "final_stand",
				Sequence:    5,
				Title:       "Последний рубеж",
				Description: "Как вы завершите борьбу за культурную идентичность?",
				Context:     "Корпорации готовят финальную атаку на европейскую идентичность города. Все зависит от вашего решения.",
				Choices: []models.Choice{
					{
						ID:             "symbolic_restoration",
						Text:           "Символическое восстановление",
						Description:    "Восстановить ключевые элементы европейской архитектуры",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "architectural_conservationists",
								Value:       float64(35),
								Probability: 1.0,
								Description: "Максимальное увеличение репутации архитекторов",
							},
							{
								Type:        "xp",
								Value:       float64(600),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "cultural_manifesto",
						Text:           "Культурный манифест",
						Description:    "Опубликовать манифест о важности культурного разнообразия",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural_diversity_advocates",
								Value:       float64(40),
								Probability: 1.0,
								Description: "Максимальное увеличение репутации защитников разнообразия",
							},
							{
								Type:        "xp",
								Value:       float64(550),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "cultural_victory",
				Title:       "Культурная победа",
				Description: "Вы успешно защитили европейскую идентичность Буэнос-Айреса от глобальной стандартизации.",
				Requirements: []string{"gather_evidence", "architectural_experts", "legal_protest", "underground_cafes", "symbolic_restoration"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 24000},
					{Type: "currency", Value: 20000},
					{Type: "item", ItemID: "belle_epoque_blueprint", Rarity: "legendary"},
					{Type: "item", ItemID: "parisian_cafe_access", Rarity: "epic"},
					{Type: "item", ItemID: "cultural_heritage_badge", Rarity: "rare"},
					{Type: "achievement", Value: "Cultural Preservation Hero"},
				},
				Narrative: "Буэнос-Айрес сохранил свою уникальную идентичность 'Парижа Южной Америки'. Европейская элегантность и латиноамериканская страсть продолжают жить в гармонии.",
			},
			{
				ID:          "resistance_legend",
				Title:       "Легенда сопротивления",
				Description: "Ваша борьба стала символом сопротивления глобализации и вдохновила другие города.",
				Requirements: []string{"contact_locals", "cultural_activists", "public_campaign", "art_exhibitions", "cultural_manifesto"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 22000},
					{Type: "currency", Value: 18000},
					{Type: "item", ItemID: "resistance_manifesto", Rarity: "legendary"},
					{Type: "item", ItemID: "cultural_network_access", Rarity: "epic"},
					{Type: "achievement", Value: "Globalization Resistance Icon"},
				},
				Narrative: "Ваша история борьбы за культурное разнообразие стала легендой. Города по всему миру берут пример с Буэнос-Айреса.",
			},
			{
				ID:          "balanced_compromise",
				Title:       "Сбалансированный компромисс",
				Description: "Вы нашли баланс между сохранением наследия и необходимостью прогресса.",
				Requirements: []string{"gather_evidence", "cultural_activists", "public_campaign", "art_exhibitions", "symbolic_restoration"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 20000},
					{Type: "currency", Value: 15000},
					{Type: "item", ItemID: "heritage_compromise_agreement", Rarity: "epic"},
					{Type: "item", ItemID: "cultural_bridge_token", Rarity: "rare"},
					{Type: "achievement", Value: "Cultural Bridge Builder"},
				},
				Narrative: "Буэнос-Айрес нашел баланс между европейским наследием и современными вызовами. Город продолжает развиваться, сохраняя свою уникальность.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "cultural_heritage_defenders",
				Change:      35,
				Description: "Защита культурного наследия от корпоративной стандартизации",
				ChoiceID:    "gather_evidence",
			},
			{
				Faction:     "european_tradition_preservation",
				Change:      30,
				Description: "Сохранение европейских традиций в латиноамериканском контексте",
				ChoiceID:    "contact_locals",
			},
			{
				Faction:     "architectural_conservationists",
				Change:      25,
				Description: "Защита исторической архитектуры belle epoque",
				ChoiceID:    "architectural_experts",
			},
			{
				Faction:     "corporate_globalization",
				Change:      -25,
				Description: "Противодействие корпоративной глобализации",
				ChoiceID:    "legal_protest",
			},
			{
				Faction:     "cultural_diversity_advocates",
				Change:      40,
				Description: "Защита культурного разнообразия как ценности",
				ChoiceID:    "cultural_activists",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Buenos Aires, Argentina - Historic Districts",
			TimePeriod:  "2020-2029",
			Weather:     "variable, symbolic of cultural change",
			Situation:   "Defend the European cultural heritage of Buenos Aires against corporate globalization and standardization.",
			Objectives: []string{
				"Document cultural heritage value of historic buildings",
				"Unite heritage defenders and cultural activists",
				"Defend historic buildings from demolition",
				"Organize underground cultural events and exhibitions",
				"Preserve European identity through symbolic restoration",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "architect_elena",
				Name:        "Элена 'Архитектор' Мендоса",
				Role:        "Историк архитектуры",
				Description:    "Эксперт по зданиям belle epoque, страстный защитник европейского наследия",
				Importance:  "primary",
			},
			{
				ID:          "cultural_activist_carlos",
				Name:        "Карлос 'Культурный Воин' Родригес",
				Role:        "Культурный активист",
				Description:    "Организатор подпольных культурных мероприятий, борец с глобализацией",
				Importance:  "secondary",
			},
			{
				ID:          "corporate_rep_sarah",
				Name:        "Сара 'Корпоративный Разработчик' Чен",
				Role:        "Корпоративный представитель",
				Description:    "Представитель азиатской корпорации, продвигающей стандартизацию",
				Importance:  "antagonist",
			},
			{
				ID:          "elderly_resident_maria",
				Name:        "Донья Мария Вальдес",
				Role:        "Старожил квартала",
				Description:    "Пожилая женщина, помнящая довоенный Буэнос-Айрес и европейские традиции",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}
	return quest, nil
}

// GetEconomicCrisisQuest returns the Economic Crisis quest for Buenos Aires
// Issue: #140929878
func (s *Service) GetEconomicCrisisQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Economic Crisis quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-economic-crisis-buenos-aires-2020-2029",
		Title:            "Буэнос-Айрес: Экономический Кризис",
		Description:      "В 2020-х годах корпорации установили тотальный контроль над экономикой Аргентины. Все транзакции проходят через импланты, инфляция манипулируется искусственно, а независимые предприниматели преследуются как 'экономические террористы'. Создайте альтернативную экономику, основанную на доверии и сообществе.",
		QuestType:        "narrative_side",
		MinLevel:         25,
		MaxLevel:         0, // No max level
		EstimatedDuration: 480,
		Difficulty:       "legendary",
		Themes:           []string{"economic_freedom", "corporate_control", "trust_economy", "resistance", "community"},
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "investigate_corporate_control",
				Sequence:    1,
				Title:       "Расследование корпоративного контроля",
				Description: "Как вы начнете борьбу с экономическим контролем корпораций?",
				Context:     "Вы узнаете, что корпорации контролируют всю экономику через импланты и манипулируют инфляцией. Независимые предприниматели исчезают один за другим.",
				Choices: []models.Choice{
					{
						ID:             "hack_corporate_systems",
						Text:           "Взломать корпоративные системы",
						Description:    "Получить доказательства манипуляций с внутренней стороны",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "corporate_economic_control",
								Value:       float64(-20),
								Probability: 1.0,
								Description: "Снижение репутации корпораций",
							},
							{
								Type:        "xp",
								Value:       float64(350),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "high",
						MoralAlignment: "chaotic",
					},
					{
						ID:             "interview_business_owners",
						Text:           "Опросить пострадавших предпринимателей",
						Description:    "Собрать свидетельства от жертв корпоративного контроля",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "independent_business_alliance",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение репутации независимых предпринимателей",
							},
							{
								Type:        "xp",
								Value:       float64(250),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "build_resistance_network",
				Sequence:    2,
				Title:       "Создание сети сопротивления",
				Description: "Как вы объедините независимых предпринимателей?",
				Context:     "Вы нашли несколько выживших независимых бизнесменов. Они готовы бороться, но боятся репрессий корпораций.",
				Choices: []models.Choice{
					{
						ID:             "create_secret_alliance",
						Text:           "Создать тайный альянс предпринимателей",
						Description:    "Объединить бизнесменов в подпольную организацию",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "economic_freedom_fighters",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Увеличение репутации борцов за экономическую свободу",
							},
							{
								Type:        "xp",
								Value:       float64(400),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
					{
						ID:             "establish_community_trust",
						Text:           "Установить систему доверия сообщества",
						Description:    "Создать альтернативную экономику на основе взаимного доверия",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "community_trust_builders",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Увеличение репутации строителей доверия",
							},
							{
								Type:        "xp",
								Value:       float64(380),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "create_alternative_currency",
				Sequence:    3,
				Title:       "Создание альтернативной валюты",
				Description: "Как вы создадите валюту, свободную от корпоративного контроля?",
				Context:     "Ваша сеть растет, но для торговли нужна валюта, не зависящая от корпоративных имплантов.",
				Choices: []models.Choice{
					{
						ID:             "digital_trust_tokens",
						Text:           "Создать цифровые токены доверия",
						Description:    "Ввести криптовалюту, основанную на репутации в сообществе",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "independent_business_alliance",
								Value:       float64(30),
								Probability: 1.0,
								Description: "Значительное увеличение репутации независимых бизнесменов",
							},
							{
								Type:        "xp",
								Value:       float64(500),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "high",
						MoralAlignment: "neutral",
					},
					{
						ID:             "barter_system",
						Text:           "Восстановить систему бартера",
						Description:    "Вернуться к прямому обмену товарами и услугами",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "traditional_economists",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Увеличение репутации традиционных экономистов",
							},
							{
								Type:        "xp",
								Value:       float64(450),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "establish_underground_markets",
				Sequence:    4,
				Title:       "Организация подпольных рынков",
				Description: "Как вы организуете торговлю вне корпоративного контроля?",
				Context:     "Альтернативная валюта работает. Теперь нужно создать места, где люди могут торговать свободно.",
				Choices: []models.Choice{
					{
						ID:             "mobile_trading_networks",
						Text:           "Создать мобильные торговые сети",
						Description:    "Организовать передвижные рынки, которые трудно отследить",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "economic_freedom_fighters",
								Value:       float64(35),
								Probability: 1.0,
								Description: "Значительное увеличение репутации борцов за свободу",
							},
							{
								Type:        "xp",
								Value:       float64(600),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "chaotic",
					},
					{
						ID:             "community_cooperatives",
						Text:           "Организовать кооперативы сообщества",
						Description:    "Создать коллективные предприятия с совместным владением",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "community_trust_builders",
								Value:       float64(40),
								Probability: 1.0,
								Description: "Максимальное увеличение репутации строителей доверия",
							},
							{
								Type:        "xp",
								Value:       float64(550),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "final_economic_revolution",
				Sequence:    5,
				Title:       "Экономическая революция",
				Description: "Как вы завершите борьбу за экономическую свободу?",
				Context:     "Корпорации знают о вашем движении и готовят финальный удар. Все зависит от вашей стратегии.",
				Choices: []models.Choice{
					{
						ID:             "public_expose",
						Text:           "Общественное разоблачение",
						Description:    "Опубликовать все доказательства корпоративных преступлений",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "corporate_economic_control",
								Value:       float64(-40),
								Probability: 1.0,
								Description: "Максимальное снижение репутации корпораций",
							},
							{
								Type:        "xp",
								Value:       float64(800),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "peaceful_transition",
						Text:           "Мирный переход",
						Description:    "Предложить корпорациям компромисс и совместное развитие",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "independent_business_alliance",
								Value:       float64(30),
								Probability: 1.0,
								Description: "Увеличение репутации независимых бизнесменов",
							},
							{
								Type:        "xp",
								Value:       float64(700),
								Probability: 1.0,
								Description: "Получение опыта",
							},
						},
						Requirements:   []models.Requirement{},
						Unlocks:        []string{},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "economic_revolution_success",
				Title:       "Экономическая Революция",
				Description: "Вы успешно создали альтернативную экономику и свергли корпоративный контроль над финансами Аргентины.",
				Requirements: []string{"hack_corporate_systems", "create_secret_alliance", "digital_trust_tokens", "mobile_trading_networks", "public_expose"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 32000},
					{Type: "currency", Value: 28000},
					{Type: "item", ItemID: "community_trust_token", Rarity: "legendary"},
					{Type: "item", ItemID: "economic_freedom_manifesto", Rarity: "epic"},
					{Type: "item", ItemID: "underground_market_access", Rarity: "rare"},
					{Type: "achievement", Value: "Economic Revolutionary"},
				},
				Narrative: "Ваша революция изменила лицо аргентинской экономики. Теперь люди торгуют на основе доверия, а не корпоративного контроля.",
			},
			{
				ID:          "trust_based_economy",
				Title:       "Экономика Доверия",
				Description: "Вы создали устойчивую экономику, основанную на взаимном доверии и традиционных ценностях.",
				Requirements: []string{"interview_business_owners", "establish_community_trust", "barter_system", "community_cooperatives", "peaceful_transition"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 30000},
					{Type: "currency", Value: 25000},
					{Type: "item", ItemID: "trust_economy_foundation", Rarity: "legendary"},
					{Type: "item", ItemID: "cooperative_network_access", Rarity: "epic"},
					{Type: "achievement", Value: "Trust Economy Pioneer"},
				},
				Narrative: "Ваша экономика доверия стала моделью для других городов. Корпорации вынуждены считаться с силой сообщества.",
			},
			{
				ID:          "hybrid_economic_system",
				Title:       "Гибридная Экономическая Система",
				Description: "Вы нашли баланс между корпоративным контролем и альтернативной экономикой.",
				Requirements: []string{"hack_corporate_systems", "establish_community_trust", "digital_trust_tokens", "community_cooperatives", "peaceful_transition"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 28000},
					{Type: "currency", Value: 22000},
					{Type: "item", ItemID: "economic_balance_accord", Rarity: "epic"},
					{Type: "item", ItemID: "dual_currency_system", Rarity: "rare"},
					{Type: "achievement", Value: "Economic Bridge Builder"},
				},
				Narrative: "Две экономические системы теперь сосуществуют. Корпорации контролируют крупный бизнес, а сообщество - повседневную торговлю.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "independent_business_alliance",
				Change:      40,
				Description: "Поддержка независимых предпринимателей и малого бизнеса",
				ChoiceID:    "interview_business_owners",
			},
			{
				Faction:     "economic_freedom_fighters",
				Change:      35,
				Description: "Борьба за экономическую свободу от корпоративного контроля",
				ChoiceID:    "create_secret_alliance",
			},
			{
				Faction:     "community_trust_builders",
				Change:      30,
				Description: "Создание экономики на основе взаимного доверия",
				ChoiceID:    "establish_community_trust",
			},
			{
				Faction:     "corporate_economic_control",
				Change:      -30,
				Description: "Противодействие корпоративному контролю над экономикой",
				ChoiceID:    "hack_corporate_systems",
			},
			{
				Faction:     "traditional_economists",
				Change:      25,
				Description: "Восстановление традиционных экономических практик",
				ChoiceID:    "barter_system",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Buenos Aires, Argentina - Economic Underground",
			TimePeriod:  "2020-2029",
			Weather:     "tense, economically unstable atmosphere",
			Situation:   "Fight against corporate economic control and create an alternative economy based on community trust.",
			Objectives: []string{
				"Investigate corporate economic control mechanisms",
				"Gather independent business owners and artisans",
				"Create alternative currency based on community trust",
				"Establish underground markets and trading networks",
				"Expose corporate manipulation and restore economic freedom",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "business_owner_maria",
				Name:        "Мария 'Независимая' Гонсалес",
				Role:        "Владелица семейного магазина",
				Description:    "Последняя независимая предпринимательница в районе, потеряла бизнес из-за корпоративных репрессий",
				Importance:  "primary",
			},
			{
				ID:          "corporate_economist_sarah",
				Name:        "Сара 'Корпоративная' Чен",
				Role:        "Экономический аналитик корпорации",
				Description:    "Представитель корпорации, отвечающая за контроль над местной экономикой",
				Importance:  "antagonist",
			},
			{
				ID:          "trust_economist_carlos",
				Name:        "Карлос 'Доверие' Родригес",
				Role:        "Экономист альтернативных систем",
				Description:    "Эксперт по экономике доверия и бартерным системам",
				Importance:  "secondary",
			},
			{
				ID:          "underground_trader_ana",
				Name:        "Ана 'Подпольная' Фернандес",
				Role:        "Координатор подпольной торговли",
				Description:    "Опытный торговец, знающий все тайные рынки города",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}
	return quest, nil
}

// GetRedWingsHockeyQuest returns the Red Wings Hockey quest for Detroit
func (s *Service) GetRedWingsHockeyQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Red Wings Hockey quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "red-wings-hockey-detroit-2020-2029",
		Title:            "Хоккей Ред Уингс",
		Description:      "Восстановить славу команды Детройт Ред Уингс",
		QuestType:        "narrative_side",
		MinLevel:         14,
		MaxLevel:         22,
		EstimatedDuration: 75,
		Difficulty:       "hard",
		Themes:           []string{"sports_revitalization", "team_building", "corporate_sports", "detroit_pride"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "team_recruitment",
				Sequence:    1,
				Title:       "Набор команды",
				Description: "Собрать команду для возрождения Ред Уингс",
				Context:     "Город нуждается в спортивных героях",
				Choices: []models.Choice{
					{
						ID:             "recruit_locals",
						Text:           "Набрать местных талантов",
						Description:    "Поддержка местного сообщества",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "corporate_sponsors",
						Text:           "Привлечь корпоративных игроков",
						Description:    "Быстрый путь к победе",
						RiskLevel:      "medium",
						MoralAlignment: "evil",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "wings_restored",
				Title:       "Ред Уингс возрождены",
				Description: "Команда вернула славу Детройту",
				Rewards: []models.Reward{
					{Type: "experience", Value: 15800},
					{Type: "currency", Value: 12500},
					{Type: "reputation", Value: "detroit_sports:+30"},
					{Type: "item", ItemID: "wings_jersey", Rarity: "epic"},
				},
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Detroit, Joe Louis Arena",
			TimePeriod:  "2020-2029",
			Weather:     "indoor arena, electric atmosphere",
			Situation:   "The legendary Red Wings franchise needs revival in a changed Detroit",
			Objectives: []string{
				"Assemble a competitive hockey team",
				"Restore Joe Louis Arena",
				"Win championship games",
				"Build fan support across Detroit",
				"Defeat corporate rival teams",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "coach_legend",
				Name:        "Майк 'Ледяной' Стивенсон",
				Role:        "Бывший тренер Ред Уингс",
				Description: "Легенда хоккея, готовый вернуться",
				Importance:  "primary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetRevivalHopeQuest returns the Revival and Hope quest for Detroit
func (s *Service) GetRevivalHopeQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Revival and Hope quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "revival-hope-detroit-2020-2029",
		Title:            "Возрождение и надежда",
		Description:      "Стать катализатором возрождения Детройта",
		QuestType:        "narrative_main",
		MinLevel:         16,
		MaxLevel:         25,
		EstimatedDuration: 90,
		Difficulty:       "legendary",
		Themes:           []string{"urban_revitalization", "community_leadership", "hope_vs_despair", "detroit_future"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "revival_approach",
				Sequence:    1,
				Title:       "Подход к возрождению",
				Description: "Выбрать стратегию возрождения города",
				Context:     "Детройт на перепутье: корпорации или сообщество?",
				Choices: []models.Choice{
					{
						ID:             "community_first",
						Text:           "Приоритет сообществу",
						Description:    "Медленное, но устойчивое развитие",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "corporate_deals",
						Text:           "Корпоративные сделки",
						Description:    "Быстрое развитие с компромиссами",
						RiskLevel:      "high",
						MoralAlignment: "evil",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "detroit_reborn",
				Title:       "Детройт возрожден",
				Description: "Город обрел новую жизнь и надежду",
				Rewards: []models.Reward{
					{Type: "experience", Value: 25000},
					{Type: "currency", Value: 20000},
					{Type: "reputation", Value: "detroit_legendary:+50"},
					{Type: "item", ItemID: "revival_medal", Rarity: "legendary"},
				},
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Detroit, Multiple Districts",
			TimePeriod:  "2020-2029",
			Weather:     "symbolic of the city's changing fortunes",
			Situation:   "Detroit stands at the crossroads of despair and hope",
			Objectives: []string{
				"Lead community revitalization projects",
				"Combat corporate exploitation",
				"Build alliances across divided communities",
				"Create sustainable economic opportunities",
				"Inspire city-wide hope and unity",
				"Transform Detroit's image from ruin to renaissance",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "community_leader",
				Name:        "Мария 'Надежда' Гарсия",
				Role:        "Лидер сообщества",
				Description: "Видит потенциал Детройта и борется за его будущее",
				Importance:  "primary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetDeepDishPizzaQuest returns the Deep Dish Pizza quest for Chicago
// Issue: #140928949
func (s *Service) GetDeepDishPizzaQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Deep Dish Pizza quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "deep-dish-pizza-chicago-2020-2029",
		Title:            "Глубокая пицца",
		Description:      "Сохранить традиции чикагской глубокой пиццы и противостоять корпоративной стандартизации",
		QuestType:        "narrative_side",
		MinLevel:         9,
		MaxLevel:         18,
		EstimatedDuration: 60,
		Difficulty:       "medium",
		Themes:           []string{"culinary_tradition", "corporate_resistance", "chicago_pride", "food_culture", "artisanal_craftsmanship"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "recipe_search",
				Sequence:    1,
				Title:       "Поиск рецептов",
				Description: "Выбрать подход к поиску семейных рецептов пиццы",
				Context:     "Легендарные рецепты глубокой пиццы спрятаны в старых семейных пекарнях",
				Choices: []models.Choice{
					{
						ID:             "visit_old_bakeries",
						Text:           "Посетить старые пекарни",
						Description:    "Найти информацию у традиционных производителей",
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "hack_corporate",
						Text:           "Взломать корпоративные базы",
						Description:    "Получить рецепты от конкурентов",
						RiskLevel:      "high",
						MoralAlignment: "evil",
					},
					{
						ID:             "ask_locals",
						Text:           "Спросить у местных жителей",
						Description:    "Собрать информацию через слухи и истории",
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
				},
				Critical: true,
			},
			{
				ID:          "corporate_response",
				Sequence:    2,
				Title:       "Реакция на корпорации",
				Description: "Как ответить на корпоративное давление",
				Context:     "Корпорации предлагают купить рецепты или угрожают судебными исками",
				Choices: []models.Choice{
					{
						ID:             "create_cooperative",
						Text:           "Создать кооператив",
						Description:    "Объединить независимых производителей",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "direct_confrontation",
						Text:           "Прямое противостояние",
						Description:    "Открыто бороться с корпорациями",
						RiskLevel:      "high",
						MoralAlignment: "neutral",
					},
					{
						ID:             "underground_network",
						Text:           "Подпольная сеть",
						Description:    "Создать тайную сеть производителей",
						RiskLevel:      "medium",
						MoralAlignment: "evil",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "tradition_saved",
				Title:       "Традиции спасены",
				Description: "Глубокая пицца Чикаго сохранена для будущих поколений",
				Rewards: []models.Reward{
					{Type: "experience", Value: 11200},
					{Type: "currency", Value: 7800},
					{Type: "item", ItemID: "deep_dish_recipe", Rarity: "legendary"},
					{Type: "reputation", Value: "chicago_culinary:+35"},
				},
			},
			{
				ID:          "corporate_compromise",
				Title:       "Компромисс достигнут",
				Description: "Найден баланс между традициями и современностью",
				Rewards: []models.Reward{
					{Type: "experience", Value: 9800},
					{Type: "currency", Value: 10200},
					{Type: "reputation", Value: "corporate_alliance:+15"},
				},
			},
			{
				ID:          "tradition_lost",
				Title:       "Традиции утеряны",
				Description: "Корпоративная стандартизация победила",
				Rewards: []models.Reward{
					{Type: "experience", Value: 5600},
					{Type: "currency", Value: 11200},
					{Type: "reputation", Value: "corporate_alliance:+25"},
				},
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "chicago_locals",
				Change:      30,
				Description: "Помощь в сохранении кулинарных традиций",
				ChoiceID:    "ask_locals",
			},
			{
				Faction:     "corporate_food",
				Change:      -35,
				Description: "Противодействие корпоративным планам",
				ChoiceID:    "direct_confrontation",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Chicago, Various Neighborhoods",
			TimePeriod:  "2020-2029",
			Weather:     "classic Chicago winter with occasional warm spells",
			Situation:   "The deep dish pizza, Chicago's culinary crown jewel, faces extinction at the hands of corporate standardization",
			Objectives: []string{
				"Find legendary pizzaiolos of Chicago",
				"Gather authentic family recipes",
				"Restore traditional pizzerias",
				"Organize deep dish pizza festival",
				"Counter corporate competitors",
				"Create cooperative of independent producers",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "master_pizzaiolo",
				Name:        "Дон Антонио 'Толстый Борт' Росси",
				Role:        "Мастер-пиццамейкер",
				Description: "Последний хранитель настоящих традиций глубокой пиццы Чикаго",
				Importance:  "primary",
			},
			{
				ID:          "corporate_exec",
				Name:        "Мисс Эмма Синтезис",
				Role:        "Корпоративный менеджер",
				Description: "Представитель корпорации, которая хочет стандартизировать все рецепты",
				Importance:  "antagonist",
			},
			{
				ID:          "food_critic",
				Name:        "Профессор Марио Дель Конти",
				Role:        "Кулинарный критик",
				Description: "Эксперт по итальянской кухне, страстный защитник аутентичности",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetOilLegacyQuest returns the Oil Legacy quest for Dallas
// Issue: #140928929
func (s *Service) GetOilLegacyQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Oil Legacy quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "oil-legacy-dallas-2020-2029",
		Title:            "Нефтяное наследие",
		Description:      "Расследовать экологические преступления нефтяных компаний и сохранить нефтяное наследие Техаса",
		QuestType:        "narrative_main",
		MinLevel:         15,
		MaxLevel:         25,
		EstimatedDuration: 95,
		Difficulty:       "legendary",
		Themes:           []string{"environmental_disaster", "corporate_corruption", "texas_heritage", "oil_industry", "ecological_activism"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "investigation_method",
				Sequence:    1,
				Title:       "Метод расследования",
				Description: "Выбрать подход к расследованию экологических преступлений",
				Context:     "Нефтяные компании хорошо защищены, но доказательства загрязнения повсюду",
				Choices: []models.Choice{
					{
						ID:             "scientific_analysis",
						Text:           "Провести научный анализ",
						Description:    "Собрать образцы и провести лабораторные тесты",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
					{
						ID:             "whistleblower_network",
						Text:           "Создать сеть информаторов",
						Description:    "Найти бывших сотрудников и свидетелей",
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "corporate_infiltration",
						Text:           "Проникнуть в корпорацию",
						Description:    "Внедриться и получить доступ к документам",
						RiskLevel:      "high",
						MoralAlignment: "evil",
					},
				},
				Critical: true,
			},
			{
				ID:          "corporate_response",
				Sequence:    2,
				Title:       "Реакция на корпоративное давление",
				Description: "Как ответить на угрозы и подкуп от нефтяных компаний",
				Context:     "Корпорации используют все средства - от взяток до угроз",
				Choices: []models.Choice{
					{
						ID:             "accept_settlement",
						Text:           "Принять компенсацию",
						Description:    "Взять деньги и прекратить расследование",
						RiskLevel:      "low",
						MoralAlignment: "evil",
					},
					{
						ID:             "full_exposure",
						Text:           "Полное разоблачение",
						Description:    "Предать огласке все преступления",
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "reform_compromise",
						Text:           "Реформы через компромисс",
						Description:    "Добиться изменений через переговоры",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "environment_saved",
				Title:       "Окружающая среда спасена",
				Description: "Нефтяные компании привлечены к ответственности, экология восстановлена",
				Rewards: []models.Reward{
					{Type: "experience", Value: 18400},
					{Type: "currency", Value: 12800},
					{Type: "item", ItemID: "texas_oil_talisman", Rarity: "legendary"},
					{Type: "reputation", Value: "texas_ecology:+50"},
				},
			},
			{
				ID:          "compromise_achieved",
				Title:       "Компромисс достигнут",
				Description: "Нефтяная промышленность реформирована с учетом экологии",
				Rewards: []models.Reward{
					{Type: "experience", Value: 16100},
					{Type: "currency", Value: 18400},
					{Type: "reputation", Value: "texas_business:+35"},
				},
			},
			{
				ID:          "corporate_victory",
				Title:       "Победа корпораций",
				Description: "Нефтяные компании продолжают загрязнять без последствий",
				Rewards: []models.Reward{
					{Type: "experience", Value: 9200},
					{Type: "currency", Value: 23000},
					{Type: "reputation", Value: "corporate_alliance:+30"},
				},
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "texas_locals",
				Change:      40,
				Description: "Помощь в защите окружающей среды",
				ChoiceID:    "whistleblower_network",
			},
			{
				Faction:     "oil_companies",
				Change:      -60,
				Description: "Противодействие нефтяным корпорациям",
				ChoiceID:    "full_exposure",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Dallas, Texas Oil Fields",
			TimePeriod:  "2020-2029",
			Weather:     "hot and dusty with occasional chemical storms",
			Situation:   "The Texas oil legacy, once a source of pride, now threatens to become an environmental nightmare",
			Objectives: []string{
				"Investigate contaminated territories around Dallas",
				"Gather evidence of corporate corruption",
				"Interview victims of environmental disasters",
				"Organize environmental responsibility campaign",
				"Counter corporate lobbyists",
				"Find alternative energy sources",
				"Protect the oil legacy from destruction",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "environmental_scientist",
				Name:        "Доктор Мария Санчес",
				Role:        "Экологический ученый",
				Description:    "Эксперт по загрязнению окружающей среды, борется с корпорациями",
				Importance:  "primary",
			},
			{
				ID:          "oil_executive",
				Name:        "Мистер Джек 'Блэк Голд' Миллер",
				Role:        "Исполнительный директор нефтяной компании",
				Description:    "Хладнокровный корпоративный магнат, готовый на всё ради прибыли",
				Importance:  "antagonist",
			},
			{
				ID:          "victim_activist",
				Name:        "Сара Джонсон",
				Role:        "Активистка и жертва загрязнения",
				Description:    "Потеряла дом из-за утечки нефти, борется за справедливость",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetCapitalBuildingQuest returns the Capital Building quest for Denver
// Issue: #140928923
func (s *Service) GetCapitalBuildingQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Capital Building quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "capital-building-denver-2020-2029",
		Title:            "Капитолий Колорадо",
		Description:      "Защитить Капитолий штата Колорадо от корпоративного захвата и сохранить демократические традиции",
		QuestType:        "narrative_side",
		MinLevel:         12,
		MaxLevel:         22,
		EstimatedDuration: 90,
		Difficulty:       "hard",
		Themes:           []string{"democratic_preservation", "political_corruption", "corporate_lobbying", "state_sovereignty", "gothic_architecture"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "investigation_approach",
				Sequence:    1,
				Title:       "Подход к расследованию",
				Description: "Выбрать метод проведения расследования коррупции",
				Context:     "Капитолий требует тщательного расследования, но корпоративные интересы усложняют задачу",
				Choices: []models.Choice{
					{
						ID:             "official_investigation",
						Text:           "Провести официальное расследование",
						Description:    "Получить разрешение и провести полную проверку",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
					{
						ID:             "undercover_operation",
						Text:           "Провести тайное расследование",
						Description:    "Внедриться под прикрытием и собрать доказательства",
						RiskLevel:      "high",
						MoralAlignment: "evil",
					},
					{
						ID:             "public_expose",
						Text:           "Начать публичное разоблачение",
						Description:    "Привлечь СМИ и общественность",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
				},
				Critical: true,
			},
			{
				ID:          "corporate_response",
				Sequence:    2,
				Title:       "Реакция на корпоративное давление",
				Description: "Как ответить на корпоративные угрозы и взятки",
				Context:     "Корпорации не сдаются легко, предлагая взятки и политическое давление",
				Choices: []models.Choice{
					{
						ID:             "accept_corruption",
						Text:           "Принять взятку и отступить",
						Description:    "Получить деньги, но предать демократию",
						RiskLevel:      "low",
						MoralAlignment: "evil",
					},
					{
						ID:             "expose_scandal",
						Text:           "Разоблачить скандал",
						Description:    "Собрать доказательства и предать огласке",
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "political_compromise",
						Text:           "Достичь политического компромисса",
						Description:    "Найти баланс между интересами",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "capitol_saved",
				Title:       "Капитолий спасен",
				Description: "Капитолий Колорадо сохранен для будущих поколений",
				Rewards: []models.Reward{
					{Type: "experience", Value: 15200},
					{Type: "currency", Value: 10600},
					{Type: "item", ItemID: "colorado_capitol_key", Rarity: "legendary"},
					{Type: "reputation", Value: "colorado_democracy:+45"},
				},
			},
			{
				ID:          "compromise_reached",
				Title:       "Компромисс достигнут",
				Description: "Капитолий модернизирован с сохранением демократических традиций",
				Rewards: []models.Reward{
					{Type: "experience", Value: 13300},
					{Type: "currency", Value: 9300},
					{Type: "reputation", Value: "colorado_business:+30"},
				},
			},
			{
				ID:          "capitol_corporatized",
				Title:       "Капитолий корпоратизирован",
				Description: "Корпоративные интересы победили демократию",
				Rewards: []models.Reward{
					{Type: "experience", Value: 7600},
					{Type: "currency", Value: 15200},
					{Type: "reputation", Value: "corporate_alliance:+25"},
				},
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "colorado_citizens",
				Change:      35,
				Description: "Помощь в сохранении демократических традиций",
				ChoiceID:    "public_expose",
			},
			{
				Faction:     "corporate_alliance",
				Change:      -50,
				Description: "Противодействие корпоративным планам",
				ChoiceID:    "expose_scandal",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Denver, Colorado State Capitol",
			TimePeriod:  "2020-2029",
			Weather:     "clear mountain air with occasional political storms",
			Situation:   "The Colorado State Capitol stands as both a monument to democratic governance and a target for corporate greed",
			Objectives: []string{
				"Conduct inspection of the Colorado State Capitol",
				"Gather evidence of corporate lobbying",
				"Organize democracy preservation campaign",
				"Counter corporate lobbyists",
				"Find alternative solutions to governance problems",
				"Protect the building's historical significance",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "state_inspector",
				Name:        "Доктор Элизабет Харпер",
				Role:        "Главный инспектор государственных зданий",
				Description: "Эксперт по сохранению исторических зданий, скептически относится к корпоративным планам",
				Importance:  "primary",
			},
			{
				ID:          "corporate_lobbyist",
				Name:        "Мистер Роберт Вон",
				Role:        "Корпоративный лоббист",
				Description:    "Хладнокровный представитель корпораций, готовый на всё ради прибыли",
				Importance:  "antagonist",
			},
			{
				ID:          "historian",
				Name:        "Профессор Джеймс Коллинз",
				Role:        "Историк демократии",
				Description:    "Страстный защитник демократических традиций Колорадо",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetOutdoorLifestyleQuest returns the Outdoor Lifestyle quest for Denver
// Issue: #140928921
func (s *Service) GetOutdoorLifestyleQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Outdoor Lifestyle quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-denver-2029-009-outdoor-lifestyle",
		Title:            "Денвер: жизнь под открытым небом",
		Description:      "Проведите день на свежем воздухе в Денвере, сочетая активные виды спорта и социальные мероприятия",
		QuestType:        "side",
		MinLevel:         2,
		MaxLevel:         0, // No max level
		EstimatedDuration: 45,
		Difficulty:       "easy",
		Themes:           []string{"outdoor", "lifestyle", "sports", "social", "nature"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "morning_bike_ride",
				Sequence:    1,
				Title:       "Утренний велозаезд по Cherry Creek Trail",
				Description: "Выберите подход к утреннему велозаезду",
				Context:     "Начните день с активного велоспорта по живописной трассе Cherry Creek",
				Choices: []models.Choice{
					{
						ID:             "leisure_ride",
						Text:           "Прогулочный заезд",
						Description:    "Спокойная поездка для наслаждения природой",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "outdoor_flow",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Небольшое улучшение навыка outdoor flow",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "speed_ride",
						Text:           "Скоростной заезд",
						Description:    "Интенсивная тренировка с высокой скоростью",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "outdoor_flow",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Значительное улучшение навыка outdoor flow",
							},
							{
								Type:     "health_risk",
								Target:   "stamina",
								Value:    float64(-5),
								Probability: 0.3,
								Description: "Риск переутомления",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "midday_kayaking",
				Sequence:    2,
				Title:       "Полуденный каякинг на Confluence Park",
				Description: "Выберите стиль каякинга",
				Context:     "Продолжите день водными активностями на реке",
				Choices: []models.Choice{
					{
						ID:             "guided_tour",
						Text:           "Экскурсионный тур",
						Description:    "Безопасный тур с гидом по спокойным водам",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "outdoor_flow",
								Value:    float64(12),
								Probability: 1.0,
								Description: "Среднее улучшение навыка outdoor flow",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "extreme_rapids",
						Text:           "Экстремальный сплав",
						Description:    "Тренировка на бурных порогах",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "outdoor_flow",
								Value:    float64(20),
								Probability: 1.0,
								Description: "Большое улучшение навыка outdoor flow",
							},
							{
								Type:     "health_risk",
								Target:   "health",
								Value:    float64(-10),
								Probability: 0.4,
								Description: "Риск травмы при экстремальном сплаве",
							},
						},
						RiskLevel:      "high",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "afternoon_yoga",
				Sequence:    3,
				Title:       "Послеобеденный йога-митап на Sloan's Lake",
				Description: "Выберите тип йога-сессии",
				Context:     "Завершите спортивную часть дня йогой у озера",
				Choices: []models.Choice{
					{
						ID:             "relaxation_yoga",
						Text:           "Йога релаксации",
						Description:    "Спокойная сессия для восстановления",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Улучшение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "power_yoga",
						Text:           "Силовая йога",
						Description:    "Интенсивная тренировка для продвинутых",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "outdoor_flow",
								Value:    float64(18),
								Probability: 1.0,
								Description: "Значительное улучшение навыка outdoor flow",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "evening_patio",
				Sequence:    4,
				Title:       "Вечерний патио-вечер с живой музыкой",
				Description: "Выберите стиль вечера",
				Context:     "Завершите день социальным мероприятием",
				Choices: []models.Choice{
					{
						ID:             "casual_social",
						Text:           "Неформальное общение",
						Description:    "Расслабленный вечер с друзьями",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Улучшение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "networking",
						Text:           "Нетворкинг",
						Description:    "Вечер для деловых знакомств",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "corporate",
								Value:    float64(8),
								Probability: 1.0,
								Description: "Небольшое улучшение корпоративной репутации",
							},
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Улучшение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "sports_focus",
				Title:       "Спортивный день",
				Description: "Фокус на активных видах спорта",
				Requirements: []string{"speed_ride", "extreme_rapids", "power_yoga"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 2500,
					},
					{
						Type:  "currency",
						Value: 50,
					},
				},
				Narrative: "Вы провели идеальный спортивный день в Денвере, полностью сосредоточившись на физической активности.",
			},
			{
				ID:          "social_focus",
				Title:       "Социальный день",
				Description: "Фокус на общении и отдыхе",
				Requirements: []string{"leisure_ride", "guided_tour", "relaxation_yoga", "casual_social"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 2000,
					},
					{
						Type:  "currency",
						Value: 60,
					},
				},
				Narrative: "Вы провели расслабляющий день, наслаждаясь природой и общением с людьми.",
			},
			{
				ID:          "balanced_day",
				Title:       "Сбалансированный день",
				Description: "Сочетание спорта и социального взаимодействия",
				Requirements: []string{}, // Default ending
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 2200,
					},
					{
						Type:  "currency",
						Value: 55,
					},
				},
				Narrative: "Вы нашли идеальный баланс между активным отдыхом и социальным взаимодействием.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "sports",
				Change:      20,
				Description: "Репутация среди спортсменов",
				ChoiceID:    "sports_focus",
			},
			{
				Faction:     "social",
				Change:      15,
				Description: "Социальная репутация",
				ChoiceID:    "social_focus",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Denver, Colorado",
			TimePeriod:  "2029",
			Weather:     "Sunny, 75°F",
			Situation:   "A perfect day for outdoor activities in the Mile High City",
			Objectives:  []string{
				"Morning bike ride along Cherry Creek Trail",
				"Midday kayaking at Confluence Park",
				"Afternoon yoga at Sloan's Lake",
				"Evening patio gathering with live music",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "bike_instructor",
				Name:        "Bike Instructor",
				Role:        "Guide",
				Description: "Local cycling expert",
				Importance:  "secondary",
			},
			{
				ID:          "kayak_guide",
				Name:        "Kayak Guide",
				Role:        "Guide",
				Description: "Experienced river guide",
				Importance:  "secondary",
			},
			{
				ID:          "yoga_instructor",
				Name:        "Yoga Instructor",
				Role:        "Instructor",
				Description: "Certified yoga teacher",
				Importance:  "secondary",
			},
			{
				ID:          "bar_owner",
				Name:        "Bar Owner",
				Role:        "Host",
				Description: "Local business owner hosting the patio event",
				Importance:  "tertiary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetEverythingBiggerQuest returns the Everything Bigger quest for Dallas
// Issue: #140928943
func (s *Service) GetEverythingBiggerQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Everything Bigger quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "everything-bigger-dallas-2020-2029",
		Title:            "Everything Bigger",
		Description:      "Переосмыслить девиз 'Everything is bigger in Texas' и показать, что истинная сила штата в индивидуальности",
		QuestType:        "narrative_side",
		MinLevel:         14,
		MaxLevel:         24,
		EstimatedDuration: 70,
		Difficulty:       "medium",
		Themes:           []string{"texas_identity", "corporate_mythology", "cultural_reinterpretation", "individualism_vs_corporate", "tradition_vs_modernity"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "tradition_reinterpretation",
				Sequence:    1,
				Title:       "Переосмысление традиций",
				Description: "Как интерпретировать девиз 'Everything is bigger in Texas'",
				Context:     "Девиз искажается корпорациями для оправдания гигантских проектов",
				Choices: []models.Choice{
					{
						ID:             "embrace_individuality",
						Text:           "Подчеркнуть индивидуальность",
						Description:    "Показать, что сила Техаса в уникальности каждого",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "corporate_giantism",
						Text:           "Поддержать гигантоманию",
						Description:    "Принять корпоративную интерпретацию",
						RiskLevel:      "medium",
						MoralAlignment: "evil",
					},
					{
						ID:             "balanced_approach",
						Text:           "Найти баланс",
						Description:    "Соединить традицию с современностью",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: true,
			},
			{
				ID:          "corporate_response",
				Sequence:    2,
				Title:       "Реакция на корпоративное давление",
				Description: "Как ответить на корпоративные проекты",
				Context:     "Корпорации предлагают деньги и влияние за поддержку их видения",
				Choices: []models.Choice{
					{
						ID:             "active_resistance",
						Text:           "Активное сопротивление",
						Description:    "Бороться против корпоративных проектов",
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "negotiate_changes",
						Text:           "Переговоры об изменениях",
						Description:    "Внести коррективы в проекты",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
					{
						ID:             "corporate_alliance",
						Text:           "Союз с корпорациями",
						Description:    "Присоединиться к их видению",
						RiskLevel:      "low",
						MoralAlignment: "evil",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "texas_individuality",
				Title:       "Техасская индивидуальность",
				Description: "Девиз переосмыслен как призыв к индивидуальности",
				Rewards: []models.Reward{
					{Type: "experience", Value: 15600},
					{Type: "currency", Value: 10900},
					{Type: "item", ItemID: "texas_star", Rarity: "legendary"},
					{Type: "reputation", Value: "texas_individuality:+40"},
				},
			},
			{
				ID:          "balanced_growth",
				Title:       "Сбалансированный рост",
				Description: "Найден баланс между традициями и развитием",
				Rewards: []models.Reward{
					{Type: "experience", Value: 13700},
					{Type: "currency", Value: 15600},
					{Type: "reputation", Value: "texas_balance:+30"},
				},
			},
			{
				ID:          "corporate_dominance",
				Title:       "Корпоративное доминирование",
				Description: "Корпорации закрепили контроль над традициями",
				Rewards: []models.Reward{
					{Type: "experience", Value: 7800},
					{Type: "currency", Value: 19500},
					{Type: "reputation", Value: "corporate_alliance:+25"},
				},
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "texas_locals",
				Change:      35,
				Description: "Помощь в сохранении истинных техасских традиций",
				ChoiceID:    "embrace_individuality",
			},
			{
				Faction:     "corporate_texas",
				Change:      -45,
				Description:    "Противодействие корпоративным планам",
				ChoiceID:    "active_resistance",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Dallas, Texas State Capitol Area",
			TimePeriod:  "2020-2029",
			Weather:     "hot and humid with occasional thunderstorms",
			Situation:   "The Texas motto 'Everything is bigger in Texas' has been corrupted by corporate interests",
			Objectives: []string{
				"Research the history of 'Everything is bigger in Texas'",
				"Find examples of true Texas individuality",
				"Counter corporate mega-projects",
				"Organize campaign to reinterpret traditions",
				"Show that 'bigger' doesn't always mean 'better'",
				"Restore balance between tradition and modernity",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "texas_historian",
				Name:        "Доктор Джим 'Истинный Техасец' Миллер",
				Role:        "Историк техасских традиций",
				Description:    "Эксперт по истории Техаса, страстный защитник индивидуальности штата",
				Importance:  "primary",
			},
			{
				ID:          "corporate_ceo",
				Name:        "Мисс Виктория 'Биг' Джонсон",
				Role:        "Корпоративный CEO",
				Description:    "Лидер корпорации, продвигающей гигантские проекты",
				Importance:  "antagonist",
			},
			{
				ID:          "local_artist",
				Name:        "Лола 'Маленькая Звезда' Гарсия",
				Role:        "Местный художник",
				Description:    "Представляет индивидуальность и творчество техасцев",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetTangoDanceQuest returns the Tango Dance quest for Buenos Aires
// Issue: #140929841
func (s *Service) GetTangoDanceQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Tango Dance quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-buenos-aires-2029-001-tango",
		Title:            "Буэнос-Айрес: Танго",
		Description:      "Погрузитесь в мир аргентинского танго - от уличных выступлений до ночной милонги",
		QuestType:        "side",
		MinLevel:         1,
		MaxLevel:         0, // No max level
		EstimatedDuration: 180,
		Difficulty:       "easy",
		Themes:           []string{"culture", "dance", "social", "history"},
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "san_telmo_visit",
				Sequence:    1,
				Title:       "Посещение района San Telmo",
				Description: "Исследуйте исторический район, родину танго",
				Context:     "Первый шаг в мир аргентинского танго",
				Choices: []models.Choice{
					{
						ID:             "explore_historically",
						Text:           "Исторический тур",
						Description:    "Изучите историю танго в музее",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(5),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "watch_street_performers",
						Text:           "Наблюдать уличных артистов",
						Description:    "Посмотрите на импровизированные выступления",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "plaza_dorrego_performance",
				Sequence:    2,
				Title:       "Уличные танцоры на Plaza Dorrego",
				Description: "Наблюдайте за профессиональными выступлениями",
				Context:     "Продолжите знакомство с танго в живом исполнении",
				Choices: []models.Choice{
					{
						ID:             "join_as_spectator",
						Text:           "Остаться зрителем",
						Description:    "Наслаждайтесь выступлением со стороны",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Улучшение навыка культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "ask_for_quick_lesson",
						Text:           "Попросить быстрый урок",
						Description:    "Попробуйте сделать несколько шагов",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "currency",
								Target:      "player_wallet",
								Value:       float64(-5),
								Probability: 1.0,
								Description: "Оплата урока",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "tango_school_enrollment",
				Sequence:    3,
				Title:       "Запись в школу танго",
				Description: "Найдите наставника и начните обучение",
				Context:     "Время перейти от наблюдения к практике",
				Choices: []models.Choice{
					{
						ID:             "traditional_master",
						Text:           "Традиционный мастер",
						Description:    "Учиться у опытного тангеро старой школы",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "dance_skill",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Улучшение навыка танцев",
							},
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(10),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "modern_instructor",
						Text:           "Современный инструктор",
						Description:    "Учиться у прогрессивного преподавателя",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "dance_skill",
								Value:       float64(12),
								Probability: 1.0,
								Description: "Улучшение навыка танцев",
							},
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(8),
								Probability: 1.0,
								Description: "Увеличение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "evening_milonga",
				Sequence:    4,
				Title:       "Вечерняя милонга",
				Description: "Присоединитесь к ночному танцевальному мероприятию",
				Context:     "Кульминация - настоящее погружение в мир танго",
				Choices: []models.Choice{
					{
						ID:             "dance_with_locals",
						Text:           "Танцевать с местными",
						Description:    "Выйти на паркет и танцевать с аргентинцами",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "social",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное увеличение социальной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "dance_skill",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное улучшение навыка танцев",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
					{
						ID:             "observe_and_learn",
						Text:           "Наблюдать и учиться",
						Description:    "Изучать танец со стороны, делая заметки",
						Consequences: []models.Consequence{
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Улучшение навыка культурного восприятия",
							},
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(15),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
			{
				ID:          "dawn_tango_reflection",
				Sequence:    5,
				Title:       "Рассвет и размышления о танго",
				Description: "Завершите ночь танцев глубокими размышлениями",
				Context:     "Финальное понимание культурного значения танго",
				Choices: []models.Choice{
					{
						ID:             "embrace_tango_spirit",
						Text:           "Принять дух танго",
						Description:    "Полностью погрузиться в философию танго",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(25),
								Probability: 1.0,
								Description: "Значительное увеличение культурной репутации",
							},
							{
								Type:        "achievement",
								Target:      "tango_master",
								Value:       float64(1),
								Probability: 1.0,
								Description: "Получение достижения 'Мастер танго'",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "cultural_appreciation",
						Text:           "Культурное понимание",
						Description:    "Оценить танго как часть культурного наследия",
						Consequences: []models.Consequence{
							{
								Type:        "reputation",
								Target:      "cultural",
								Value:       float64(18),
								Probability: 1.0,
								Description: "Увеличение культурной репутации",
							},
							{
								Type:        "skill_boost",
								Target:      "cultural_awareness",
								Value:       float64(20),
								Probability: 1.0,
								Description: "Значительное улучшение культурного восприятия",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				TimeLimit: nil,
				Critical:  false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "tango_aficionado",
				Title:       "Афисионадо танго",
				Description: "Стали истинным ценителем аргентинского танго",
				Requirements: []string{"traditional_master", "dance_with_locals", "embrace_tango_spirit"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 2000},
					{Type: "currency", Value: -40},
					{Type: "achievement", Value: "tango_aficionado"},
				},
				Narrative: "Вы полностью погрузились в мир танго, став частью этой живой традиции.",
			},
			{
				ID:          "cultural_explorer",
				Title:       "Культурный исследователь",
				Description: "Глубоко поняли культурное значение танго",
				Requirements: []string{"explore_historically", "observe_and_learn", "cultural_appreciation"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1800},
					{Type: "currency", Value: -25},
					{Type: "achievement", Value: "cultural_explorer"},
				},
				Narrative: "Ваше путешествие в мир танго было путешествием к пониманию аргентинской культуры.",
			},
			{
				ID:          "social_dancer",
				Title:       "Социальный танцор",
				Description: "Освоили основы и наслаждаетесь танцем",
				Requirements: []string{"ask_for_quick_lesson", "dance_with_locals"},
				Rewards: []models.Reward{
					{Type: "experience", Value: 1600},
					{Type: "currency", Value: -30},
					{Type: "achievement", Value: "social_dancer"},
				},
				Narrative: "Танго стало для вас способом общения и самовыражения в социальной среде.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "cultural_buenos_aires",
				Change:      20,
				Description: "Уважение за интерес к аргентинской культуре",
				ChoiceID:    "embrace_tango_spirit",
			},
			{
				Faction:     "social_buenos_aires",
				Change:      15,
				Description: "Социальные связи через танго",
				ChoiceID:    "dance_with_locals",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Buenos Aires, Argentina",
			TimePeriod:  "2020-2029",
			Weather:     "warm summer evening",
			Situation:   "The soul of Argentine tango calls to you from the historic streets of Buenos Aires",
			Objectives: []string{
				"Explore the birthplace of tango in San Telmo",
				"Witness authentic tango performances on Plaza Dorrego",
				"Find a tango instructor and begin your journey",
				"Participate in an evening milonga",
				"Reflect on the cultural significance of tango at dawn",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "master_tanguero",
				Name:        "Маэстро Карлос 'Эль Рей' Родригес",
				Role:        "Мастер танго",
				Description: "Легендарный тангеро, хранитель традиций танго",
				Importance:  "primary",
			},
			{
				ID:          "young_instructor",
				Name:        "Мария 'Ла Принсеса' Гонсалес",
				Role:        "Современный инструктор",
				Description: "Прогрессивный преподаватель, сочетающий традицию и современность",
				Importance:  "secondary",
			},
			{
				ID:          "cultural_historian",
				Name:        "Доктор Анна 'Ла Професора' Мендес",
				Role:        "Историк культуры",
				Description: "Эксперт по истории аргентинского танго",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetLakeMichiganQuest returns the Lake Michigan quest for Chicago
// Issue: #140928958
func (s *Service) GetLakeMichiganQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Lake Michigan quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-004-lake-michigan",
		Title:            "Чикаго — Озеро Мичиган (Third Coast)",
		Description:      "Исследуйте 'Third Coast' США вдоль Lakefront Trail с сезонными активностями и атмосферой",
		QuestType:        "side",
		MinLevel:         1,
		MaxLevel:         0, // No max level
		EstimatedDuration: 30,
		Difficulty:       "easy",
		Themes:           []string{"nature", "exploration", "seasonal", "urban-outdoor", "chicago"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "season_choice",
				Sequence:    1,
				Title:       "Выбор сезона для прогулки",
				Description: "Какое время года выбрать для исследования озера?",
				Context:     "Lake Michigan предлагает совершенно разные впечатления зимой и летом",
				Choices: []models.Choice{
					{
						ID:             "summer_exploration",
						Text:           "Летнее исследование",
						Description:    "Пляжи, теплое озеро, пикники и велосипеды",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "outdoor_flow",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Улучшение навыка outdoor flow",
							},
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(8),
								Probability: 1.0,
								Description: "Социальная репутация от общения с отдыхающими",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "winter_challenge",
						Text:           "Зимний вызов",
						Description:    "Ледяные ветра, замерзшее озеро, экстремальные условия",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "endurance",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Значительное улучшение выносливости",
							},
							{
								Type:     "health_risk",
								Target:   "health",
								Value:    float64(-5),
								Probability: 0.2,
								Description: "Риск обморожения в экстремальных условиях",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "trail_points",
				Sequence:    2,
				Title:       "Посещение ключевых точек Lakefront Trail",
				Description: "Какие достопримечательности посетить?",
				Context:     "29-километровый маршрут предлагает множество интересных мест",
				Choices: []models.Choice{
					{
						ID:             "beach_focus",
						Text:           "Фокус на пляжах",
						Description:    "Посетить пляжи и прибрежные зоны отдыха",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "nature",
								Value:    float64(12),
								Probability: 1.0,
								Description: "Репутация среди любителей природы",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "pier_focus",
						Text:           "Фокус на Navy Pier",
						Description:    "Посетить развлекательный комплекс Navy Pier",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Социальная репутация от посещения мероприятий",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "viewpoints_focus",
						Text:           "Фокус на смотровые площадки",
						Description:    "Посетить обзорные точки с видом на озеро",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "perception",
								Value:    float64(8),
								Probability: 1.0,
								Description: "Улучшение восприятия от красивых видов",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "weather_interaction",
				Sequence:    3,
				Title:       "Взаимодействие с погодными условиями",
				Description: "Как отреагировать на текущую погоду?",
				Context:     "Погода на озере может быть непредсказуемой",
				Choices: []models.Choice{
					{
						ID:             "embrace_weather",
						Text:           "Принять погоду",
						Description:    "Адаптироваться к условиям и насладиться моментом",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "adaptability",
								Value:    float64(12),
								Probability: 1.0,
								Description: "Улучшение адаптивности",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
					{
						ID:             "seek_shelter",
						Text:           "Искать укрытие",
						Description:    "Найти ближайшее укрытие от непогоды",
						Consequences: []models.Consequence{
							{
								Type:     "experience",
								Target:   "practicality",
								Value:    float64(5),
								Probability: 1.0,
								Description: "Практический опыт",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "local_interaction",
				Sequence:    4,
				Title:       "Взаимодействие с местными жителями",
				Description: "Как пообщаться с жителями Чикаго?",
				Context:     "Местные могут поделиться историями и открыть новые возможности",
				Choices: []models.Choice{
					{
						ID:             "deep_conversation",
						Text:           "Глубокий разговор",
						Description:    "Обсудить историю и культуру Чикаго",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "local",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Репутация среди местных жителей",
							},
							{
								Type:     "unlock",
								Target:   "side_quests",
								Value:    float64(1),
								Probability: 1.0,
								Description: "Доступ к дополнительным квестам",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "casual_chat",
						Text:           "Легкий разговор",
						Description:    "Короткий разговор о погоде и озере",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "local",
								Value:    float64(8),
								Probability: 1.0,
								Description: "Небольшое улучшение репутации у местных",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "summer_master",
				Title:       "Мастер летнего озера",
				Description: "Полностью насладились летней атмосферой Lake Michigan",
				Requirements: []string{"summer_exploration"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 700,
					},
					{
						Type:  "currency",
						Value: 25,
					},
				},
				Narrative: "Вы стали настоящим мастером летнего отдыха на Lake Michigan, насладившись всеми радостями теплого сезона.",
			},
			{
				ID:          "winter_survivor",
				Title:       "Выживший зимнего озера",
				Description: "Преодолели экстремальные зимние условия",
				Requirements: []string{"winter_challenge"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 800,
					},
					{
						Type:  "currency",
						Value: 30,
					},
				},
				Narrative: "Вы доказали свою стойкость, преодолев ледяные ветра и экстремальные условия зимнего Lake Michigan.",
			},
			{
				ID:          "third_coast_explorer",
				Title:       "Исследователь Third Coast",
				Description: "Полностью изучили все аспекты Lake Michigan",
				Requirements: []string{}, // Default ending
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 600,
					},
					{
						Type:  "currency",
						Value: 20,
					},
				},
				Narrative: "Вы стали настоящим исследователем 'Third Coast', открыв для себя все тайны и красоты Lake Michigan.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "nature",
				Change:      15,
				Description: "Репутация среди любителей природы и экологии",
				ChoiceID:    "beach_focus",
			},
			{
				Faction:     "local",
				Change:      10,
				Description: "Репутация среди местных жителей Чикаго",
				ChoiceID:    "deep_conversation",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Chicago, Illinois - Lake Michigan Shoreline",
			TimePeriod:  "2020-2029",
			Weather:     "Variable - Summer warmth or Winter chill",
			Situation:   "Exploring the 'Third Coast' of America",
			Objectives:  []string{
				"Choose seasonal exploration path",
				"Visit key Lakefront Trail landmarks",
				"Interact with weather conditions",
				"Connect with local Chicago residents",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "trail_runner",
				Name:        "Trail Runner",
				Role:        "Local Guide",
				Description: "Experienced runner who knows every inch of Lakefront Trail",
				Importance:  "secondary",
			},
			{
				ID:          "beach_vendor",
				Name:        "Beach Vendor",
				Role:        "Merchant",
				Description: "Seasonal vendor selling refreshments and local goods",
				Importance:  "tertiary",
			},
			{
				ID:          "weather_monitor",
				Name:        "Weather Monitor",
				Role:        "Observer",
				Description: "Local weather enthusiast tracking lake conditions",
				Importance:  "secondary",
			},
			{
				ID:          "navy_pier_worker",
				Name:        "Navy Pier Worker",
				Role:        "Entertainer",
				Description: "Works at Navy Pier, knows all the entertainment options",
				Importance:  "tertiary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetCubsWrigleyFieldQuest returns the Chicago Cubs Wrigley Field quest for Chicago
// Issue: #140928959
func (s *Service) GetCubsWrigleyFieldQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Chicago Cubs Wrigley Field quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "canon-quest-005-cubs-wrigley-field",
		Title:            "Чикаго — Chicago Cubs на Wrigley Field",
		Description:      "Посетите исторический матч Chicago Cubs на легендарном Wrigley Field с традициями и атмосферой",
		QuestType:        "side",
		MinLevel:         5,
		MaxLevel:         0, // No max level
		EstimatedDuration: 45,
		Difficulty:       "medium",
		Themes:           []string{"sports", "baseball", "tradition", "chicago", "culture", "history"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "ticket_purchase",
				Sequence:    1,
				Title:       "Приобретение билетов",
				Description: "Выберите тип билетов на матч",
				Context:     "Wrigley Field предлагает разные зоны с уникальной атмосферой",
				Choices: []models.Choice{
					{
						ID:             "bleacher_seats",
						Text:           "Билеты на bleacher seats",
						Description:    "Стоячие места с лучшей атмосферой и криками фанатов",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "sport",
								Value:    float64(25),
								Probability: 1.0,
								Description: "Максимальная спортивная репутация",
							},
							{
								Type:     "currency",
								Target:   "spending",
								Value:    float64(-80),
								Probability: 1.0,
								Description: "Более дорогие билеты",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "box_seats",
						Text:           "Билеты в ложу",
						Description:    "Комфортные сидячие места с едой и напитками",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "elite",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Репутация среди элиты",
							},
							{
								Type:     "currency",
								Target:   "spending",
								Value:    float64(-120),
								Probability: 1.0,
								Description: "Самые дорогие билеты",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "standing_room",
						Text:           "Стоячие места у поля",
						Description:    "Дешевые билеты близко к полю, но без сидений",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "sport",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Хорошая спортивная репутация",
							},
							{
								Type:     "currency",
								Target:   "spending",
								Value:    float64(-40),
								Probability: 1.0,
								Description: "Самые дешевые билеты",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: true,
			},
			{
				ID:          "stadium_exploration",
				Sequence:    2,
				Title:       "Исследование стадиона",
				Description: "Что исследовать на стадионе?",
				Context:     "Wrigley Field полон исторических достопримечательностей",
				Choices: []models.Choice{
					{
						ID:             "ivy_wall",
						Text:           "Изучить плющ на стене",
						Description:    "Легендарный плющ, который растет на стене с 1937 года",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "knowledge",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Увеличение знаний о истории",
							},
							{
								Type:     "achievement",
								Target:   "ivy_explorer",
								Value:    float64(1),
								Probability: 1.0,
								Description: "Достижение 'Исследователь плюща'",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "manual_scoreboard",
						Text:           "Посмотреть ручное табло",
						Description:    "Уникальное ручное табло, которое обновляется вручную",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "attention",
								Value:    float64(8),
								Probability: 1.0,
								Description: "Улучшение внимательности",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "dugout_area",
						Text:           "Подойти к скамейке игроков",
						Description:    "Посмотреть на игроков и тренерский состав",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "sport",
								Value:    float64(12),
								Probability: 1.0,
								Description: "Дополнительная спортивная репутация",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "seventh_inning",
				Sequence:    3,
				Title:       "Седьмой иннинг",
				Description: "Как участвовать в традиции седьмого иннинга?",
				Context:     "В седьмом иннинге все встают и поют 'Take Me Out to the Ball Game'",
				Choices: []models.Choice{
					{
						ID:             "lead_singing",
						Text:           "Вести хор",
						Description:    "Стать лидером и вести толпу в пении",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(20),
								Probability: 1.0,
								Description: "Максимальная социальная репутация",
							},
							{
								Type:     "skill_boost",
								Target:   "charisma",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Увеличение харизмы",
							},
						},
						RiskLevel:      "medium",
						MoralAlignment: "good",
					},
					{
						ID:             "enthusiastic_participation",
						Text:           "Активно участвовать",
						Description:    "Петь громко и танцевать вместе со всеми",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(12),
								Probability: 1.0,
								Description: "Хорошая социальная репутация",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
					{
						ID:             "quiet_observation",
						Text:           "Наблюдать молча",
						Description:    "Стоять и смотреть, как поют другие",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(-5),
								Probability: 1.0,
								Description: "Небольшое снижение социальной репутации",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
				},
				Critical: false,
			},
			{
				ID:          "curse_story",
				Sequence:    4,
				Title:       "История проклятия козла",
				Description: "Как отнестись к легенде о проклятии?",
				Context:     "Фанат расскажет легенду о проклятии козла, которое держало Cubs без чемпионства 71 год",
				Choices: []models.Choice{
					{
						ID:             "believe_curse",
						Text:           "Поверить в проклятие",
						Description:    "Слушать внимательно и верить в сверхъестественное",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "superstitious",
								Value:    float64(15),
								Probability: 1.0,
								Description: "Репутация среди суеверных",
							},
							{
								Type:     "unlock",
								Target:   "curse_lore",
								Value:    float64(1),
								Probability: 1.0,
								Description: "Доступ к дополнительным легендам",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "skeptical_view",
						Text:           "Отнестись скептически",
						Description:    "Слушать историю, но считать её вымыслом",
						Consequences: []models.Consequence{
							{
								Type:     "skill_boost",
								Target:   "critical_thinking",
								Value:    float64(10),
								Probability: 1.0,
								Description: "Улучшение критического мышления",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "neutral",
					},
					{
						ID:             "dismiss_story",
						Text:           "Проигнорировать рассказ",
						Description:    "Не слушать историю и уйти",
						Consequences: []models.Consequence{
							{
								Type:     "reputation",
								Target:   "social",
								Value:    float64(-10),
								Probability: 1.0,
								Description: "Снижение социальной репутации",
							},
							{
								Type:     "missed_reward",
								Target:   "merch",
								Value:    float64(1),
								Probability: 1.0,
								Description: "Пропуск редкого мерча",
							},
						},
						RiskLevel:      "low",
						MoralAlignment: "bad",
					},
				},
				Critical: false,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "ultimate_fan",
				Title:       "Истинный фанат Cubs",
				Description: "Полностью погрузились в культуру Chicago Cubs",
				Requirements: []string{"bleacher_seats", "ivy_wall", "lead_singing", "believe_curse"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 1800,
					},
					{
						Type:  "currency",
						Value: 50,
					},
					{
						Type:  "item",
						Value: "cubs_vintage_hat",
					},
				},
				Narrative: "Вы стали истинным фанатом Chicago Cubs, познав все традиции и легенды Wrigley Field.",
			},
			{
				ID:          "casual_fan",
				Title:       "Казуальный болельщик",
				Description: "Насладились матчем, но не погрузились глубоко",
				Requirements: []string{"standing_room", "manual_scoreboard", "enthusiastic_participation"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 1500,
					},
					{
						Type:  "currency",
						Value: 30,
					},
				},
				Narrative: "Вы хорошо провели время на матче Chicago Cubs, но остались обычным зрителем.",
			},
			{
				ID:          "elite_experience",
				Title:       "Элитный опыт",
				Description: "Посетили матч в комфорте ложи",
				Requirements: []string{"box_seats"},
				Rewards: []models.Reward{
					{
						Type:  "experience",
						Value: 1600,
					},
					{
						Type:  "currency",
						Value: 20,
					},
					{
						Type:  "item",
						Value: "cubs_premium_merch",
					},
				},
				Narrative: "Вы насладились матчем Chicago Cubs в комфорте элитной ложи с лучшим сервисом.",
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "sport",
				Change:      20,
				Description: "Репутация среди спортивных фанатов",
				ChoiceID:    "bleacher_seats",
			},
			{
				Faction:     "social",
				Change:      15,
				Description: "Социальная репутация от участия в традициях",
				ChoiceID:    "lead_singing",
			},
			{
				Faction:     "elite",
				Change:      10,
				Description: "Репутация среди элиты города",
				ChoiceID:    "box_seats",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Wrigley Field, Chicago, Illinois",
			TimePeriod:  "2020-2029",
			Weather:     "Perfect baseball weather - 75°F, sunny",
			Situation:   "Historic Chicago Cubs baseball game at legendary Wrigley Field",
			Objectives:  []string{
				"Purchase tickets and enter through fan zone",
				"Explore Wrigley Field landmarks (ivy wall, manual scoreboard, bleacher seats)",
				"Join the seventh inning stretch singing tradition",
				"Learn about the Curse of the Billy Goat from fan NPC",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "ticket_vendor",
				Name:        "Ticket Vendor",
				Role:        "Merchant",
				Description: "Friendly vendor selling game tickets",
				Importance:  "secondary",
			},
			{
				ID:          "seasoned_fan",
				Name:        "Seasoned Cubs Fan",
				Role:        "Storyteller",
				Description: "Long-time fan who knows all the legends and traditions",
				Importance:  "primary",
			},
			{
				ID:          "stadium_guide",
				Name:        "Stadium Guide",
				Role:        "Tour Guide",
				Description: "Official Wrigley Field guide showing historic sites",
				Importance:  "secondary",
			},
			{
				ID:          "concession_worker",
				Name:        "Concession Worker",
				Role:        "Vendor",
				Description: "Sells food and drinks, shares game insights",
				Importance:  "tertiary",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}

// GetWillisTowerQuest returns the Willis Tower quest for Chicago
// Issue: #140928947
func (s *Service) GetWillisTowerQuest(ctx context.Context) (*models.DynamicQuest, error) {
	s.logger.Info("Retrieving Willis Tower quest definition")

	quest := &models.DynamicQuest{
		QuestID:          "willis-tower-chicago-2020-2029",
		Title:            "Башня Уиллис",
		Description:      "Защитить Башню Уиллис от корпоративного сноса и сохранить архитектурное наследие Чикаго",
		QuestType:        "narrative_side",
		MinLevel:         16,
		MaxLevel:         25,
		EstimatedDuration: 75,
		Difficulty:       "hard",
		Themes:           []string{"architectural_preservation", "corporate_conspiracy", "urban_heritage", "chicago_pride", "structural_engineering"},
		Status:           "active",
		ChoicePoints: []models.ChoicePoint{
			{
				ID:          "inspection_approach",
				Sequence:    1,
				Title:       "Подход к инспекции",
				Description: "Выбрать метод проведения структурной инспекции башни",
				Context:     "Башня Уиллис требует тщательной проверки, но корпоративные интересы усложняют задачу",
				Choices: []models.Choice{
					{
						ID:             "official_inspection",
						Text:           "Провести официальную инспекцию",
						Description:    "Получить разрешение и провести полную проверку",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
					{
						ID:             "covert_investigation",
						Text:           "Провести скрытное расследование",
						Description:    "Взломать системы и собрать данные тайно",
						RiskLevel:      "high",
						MoralAlignment: "evil",
					},
					{
						ID:             "public_campaign",
						Text:           "Начать публичную кампанию",
						Description:    "Привлечь СМИ и общественность",
						RiskLevel:      "low",
						MoralAlignment: "good",
					},
				},
				Critical: true,
			},
			{
				ID:          "corporate_response",
				Sequence:    2,
				Title:       "Реакция на корпоративное давление",
				Description: "Как ответить на корпоративные угрозы и подкуп",
				Context:     "Корпорации не сдаются легко, предлагая взятки и угрозы",
				Choices: []models.Choice{
					{
						ID:             "accept_bribe",
						Text:           "Принять взятку и отступить",
						Description:    "Получить деньги, но предать идеалы",
						RiskLevel:      "low",
						MoralAlignment: "evil",
					},
					{
						ID:             "expose_corruption",
						Text:           "Разоблачить коррупцию",
						Description:    "Собрать доказательства и предать огласке",
						RiskLevel:      "high",
						MoralAlignment: "good",
					},
					{
						ID:             "negotiate_compromise",
						Text:           "Достичь компромисса",
						Description:    "Найти альтернативное решение",
						RiskLevel:      "medium",
						MoralAlignment: "neutral",
					},
				},
				Critical: true,
			},
		},
		EndingVariations: []models.EndingVariation{
			{
				ID:          "tower_saved",
				Title:       "Башня спасена",
				Description: "Башня Уиллис сохранена для будущих поколений",
				Rewards: []models.Reward{
					{Type: "experience", Value: 19200},
					{Type: "currency", Value: 13400},
					{Type: "item", ItemID: "willis_tower_key", Rarity: "legendary"},
					{Type: "reputation", Value: "chicago_architecture:+40"},
				},
			},
			{
				ID:          "compromise_reached",
				Title:       "Компромисс достигнут",
				Description: "Башня модернизирована с сохранением исторической ценности",
				Rewards: []models.Reward{
					{Type: "experience", Value: 16800},
					{Type: "currency", Value: 11700},
					{Type: "reputation", Value: "chicago_business:+25"},
				},
			},
			{
				ID:          "tower_demolished",
				Title:       "Башня снесена",
				Description: "Корпоративные интересы победили",
				Rewards: []models.Reward{
					{Type: "experience", Value: 9600},
					{Type: "currency", Value: 16800},
					{Type: "reputation", Value: "corporate_alliance:+20"},
				},
			},
		},
		ReputationImpacts: []models.ReputationImpact{
			{
				Faction:     "chicago_architects",
				Change:      30,
				Description: "Помощь в сохранении архитектурного наследия",
				ChoiceID:    "public_campaign",
			},
			{
				Faction:     "corporate_alliance",
				Change:      -40,
				Description: "Противодействие корпоративным планам",
				ChoiceID:    "expose_corruption",
			},
		},
		NarrativeSetup: models.NarrativeSetup{
			Location:    "Chicago, Willis Tower",
			TimePeriod:  "2020-2029",
			Weather:     "windy with occasional rain, accentuating the tower's height",
			Situation:   "The iconic Willis Tower stands as both a monument to human achievement and a target for corporate greed",
			Objectives: []string{
				"Conduct structural integrity inspection",
				"Gather evidence of corporate demolition plans",
				"Organize preservation campaign",
				"Counter corporate lobbyists",
				"Find alternative solutions to building problems",
				"Protect the tower's historical significance",
			},
		},
		KeyCharacters: []models.KeyCharacter{
			{
				ID:          "chief_engineer",
				Name:        "Доктор Сара Макдональд",
				Role:        "Главный инженер по инспекциям",
				Description: "Эксперт по структурной целостности небоскребов, скептически относится к корпоративным планам",
				Importance:  "primary",
			},
			{
				ID:          "corporate_rep",
				Name:        "Мистер Виктор Рейнольдс",
				Role:        "Представитель корпорации-агрессора",
				Description: "Хладнокровный корпоративный лоббист, готовый на всё ради прибыли",
				Importance:  "antagonist",
			},
			{
				ID:          "historian",
				Name:        "Профессор Элизабет Чанг",
				Role:        "Историк архитектуры",
				Description: "Страстный защитник архитектурного наследия Чикаго",
				Importance:  "ally",
			},
		},
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	return quest, nil
}
