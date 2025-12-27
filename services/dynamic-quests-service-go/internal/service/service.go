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
	cr.Completed = false
	cr.Rewards = nil
	cr.NewObjectives = nil
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
	qs.UpdatedAt = time.Time{}
	s.questStatePool.Put(qs)
}

func (s *Service) getReputation() *repository.PlayerReputation {
	return s.reputationPool.Get().(*repository.PlayerReputation)
}

func (s *Service) putReputation(rep *repository.PlayerReputation) {
	// Reset fields before returning to pool
	rep.PlayerID = ""
	rep.Corporate = 0
	rep.Street = 0
	rep.Humanity = 0
	rep.UpdatedAt = time.Time{}
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

// ImportQuestsFromYAML imports quests from YAML files
func (s *Service) ImportQuestsFromYAML(ctx context.Context, yamlData []byte) error {
	// This would parse YAML and create quest definitions
	// For now, return placeholder implementation
	s.logger.Info("Quest import from YAML - placeholder implementation")
	return fmt.Errorf("YAML import not implemented yet")
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

func clamp(value, min, max int) int {
	if value < min {
		return min
	}
	if value > max {
		return max
	}
	return value
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
