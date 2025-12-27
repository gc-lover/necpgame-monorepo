// Issue: #2244 - Dynamic Quest System Repository
// Repository layer for Dynamic Quests Service - High-performance quest state management

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"dynamic-quests-service-go/pkg/models"
)

// RepositoryInterface defines the repository interface
type RepositoryInterface interface {
	// Dynamic Quest CRUD
	CreateDynamicQuest(ctx context.Context, quest *models.DynamicQuest) error
	GetDynamicQuest(ctx context.Context, questID string) (*models.DynamicQuest, error)
	UpdateDynamicQuest(ctx context.Context, quest *models.DynamicQuest) error
	DeleteDynamicQuest(ctx context.Context, questID string) error
	ListDynamicQuests(ctx context.Context, questType string, limit, offset int) ([]*models.DynamicQuest, error)

	// Player Quest State
	CreatePlayerQuestState(ctx context.Context, state *models.PlayerQuestState) error
	GetPlayerQuestState(ctx context.Context, playerID, questID string) (*models.PlayerQuestState, error)
	UpdatePlayerQuestState(ctx context.Context, state *models.PlayerQuestState) error
	GetPlayerActiveQuests(ctx context.Context, playerID string) ([]*models.PlayerQuestState, error)
	GetPlayerCompletedQuests(ctx context.Context, playerID string, limit, offset int) ([]*models.PlayerQuestState, error)

	// Choice Processing
	ProcessPlayerChoice(ctx context.Context, request *models.QuestChoiceRequest) (*models.QuestChoiceResponse, error)
	ValidateChoiceRequirements(ctx context.Context, playerID, questID, choicePointID, choiceID string) (bool, error)

	// Analytics
	GetQuestAnalytics(ctx context.Context, questID string) (*models.QuestAnalytics, error)
	UpdateQuestAnalytics(ctx context.Context, analytics *models.QuestAnalytics) error

	// Quest Templates
	CreateQuestTemplate(ctx context.Context, template *models.QuestTemplate) error
	GetQuestTemplate(ctx context.Context, templateID string) (*models.QuestTemplate, error)
	ListQuestTemplates(ctx context.Context, category string, limit, offset int) ([]*models.QuestTemplate, error)

	// Quest Generation
	GenerateDynamicQuest(ctx context.Context, request *models.QuestGenerationRequest) (*models.QuestGenerationResponse, error)

	// Health check
	HealthCheck(ctx context.Context) error
}

// Repository implements RepositoryInterface
type Repository struct {
	db *sql.DB
}

// NewRepository creates a new repository
func NewRepository(db *sql.DB) RepositoryInterface {
	return &Repository{db: db}
}

// CreateDynamicQuest creates a new dynamic quest
func (r *Repository) CreateDynamicQuest(ctx context.Context, quest *models.DynamicQuest) error {
	choicePointsData, _ := json.Marshal(quest.ChoicePoints)
	endingVariationsData, _ := json.Marshal(quest.EndingVariations)
	reputationImpactsData, _ := json.Marshal(quest.ReputationImpacts)
	narrativeSetupData, _ := json.Marshal(quest.NarrativeSetup)
	keyCharactersData, _ := json.Marshal(quest.KeyCharacters)
	themesData, _ := json.Marshal(quest.Themes)

	query := `
		INSERT INTO dynamic_quests (
			id, quest_id, title, description, quest_type, min_level, max_level,
			estimated_duration, difficulty, choice_points, ending_variations,
			reputation_impacts, narrative_setup, key_characters, themes,
			status, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18)
	`

	quest.ID = fmt.Sprintf("dq_%d", time.Now().UnixNano())
	quest.CreatedAt = time.Now()
	quest.UpdatedAt = time.Now()
	quest.Status = "active"

	_, err := r.db.ExecContext(ctx, query,
		quest.ID, quest.QuestID, quest.Title, quest.Description, quest.QuestType,
		quest.MinLevel, quest.MaxLevel, quest.EstimatedDuration, quest.Difficulty,
		choicePointsData, endingVariationsData, reputationImpactsData,
		narrativeSetupData, keyCharactersData, themesData,
		quest.Status, quest.CreatedAt, quest.UpdatedAt,
	)

	return err
}

// GetDynamicQuest retrieves a dynamic quest by ID
func (r *Repository) GetDynamicQuest(ctx context.Context, questID string) (*models.DynamicQuest, error) {
	query := `
		SELECT id, quest_id, title, description, quest_type, min_level, max_level,
			   estimated_duration, difficulty, choice_points, ending_variations,
			   reputation_impacts, narrative_setup, key_characters, themes,
			   status, created_at, updated_at
		FROM dynamic_quests WHERE quest_id = $1 AND status = 'active'
	`

	var quest models.DynamicQuest
	var choicePointsData, endingVariationsData, reputationImpactsData, narrativeSetupData, keyCharactersData, themesData []byte

	err := r.db.QueryRowContext(ctx, query, questID).Scan(
		&quest.ID, &quest.QuestID, &quest.Title, &quest.Description, &quest.QuestType,
		&quest.MinLevel, &quest.MaxLevel, &quest.EstimatedDuration, &quest.Difficulty,
		&choicePointsData, &endingVariationsData, &reputationImpactsData,
		&narrativeSetupData, &keyCharactersData, &themesData,
		&quest.Status, &quest.CreatedAt, &quest.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("dynamic quest not found: %s", questID)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get dynamic quest: %w", err)
	}

	// Unmarshal JSON data
	if err := json.Unmarshal(choicePointsData, &quest.ChoicePoints); err != nil {
		return nil, fmt.Errorf("failed to unmarshal choice points: %w", err)
	}
	if err := json.Unmarshal(endingVariationsData, &quest.EndingVariations); err != nil {
		return nil, fmt.Errorf("failed to unmarshal ending variations: %w", err)
	}
	if err := json.Unmarshal(reputationImpactsData, &quest.ReputationImpacts); err != nil {
		return nil, fmt.Errorf("failed to unmarshal reputation impacts: %w", err)
	}
	if err := json.Unmarshal(narrativeSetupData, &quest.NarrativeSetup); err != nil {
		return nil, fmt.Errorf("failed to unmarshal narrative setup: %w", err)
	}
	if err := json.Unmarshal(keyCharactersData, &quest.KeyCharacters); err != nil {
		return nil, fmt.Errorf("failed to unmarshal key characters: %w", err)
	}
	if err := json.Unmarshal(themesData, &quest.Themes); err != nil {
		return nil, fmt.Errorf("failed to unmarshal themes: %w", err)
	}

	return &quest, nil
}

// UpdateDynamicQuest updates an existing dynamic quest
func (r *Repository) UpdateDynamicQuest(ctx context.Context, quest *models.DynamicQuest) error {
	choicePointsData, _ := json.Marshal(quest.ChoicePoints)
	endingVariationsData, _ := json.Marshal(quest.EndingVariations)
	reputationImpactsData, _ := json.Marshal(quest.ReputationImpacts)
	narrativeSetupData, _ := json.Marshal(quest.NarrativeSetup)
	keyCharactersData, _ := json.Marshal(quest.KeyCharacters)
	themesData, _ := json.Marshal(quest.Themes)

	query := `
		UPDATE dynamic_quests
		SET title = $2, description = $3, quest_type = $4, min_level = $5, max_level = $6,
		    estimated_duration = $7, difficulty = $8, choice_points = $9, ending_variations = $10,
		    reputation_impacts = $11, narrative_setup = $12, key_characters = $13, themes = $14,
		    updated_at = $15
		WHERE quest_id = $1
	`

	quest.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		quest.QuestID, quest.Title, quest.Description, quest.QuestType,
		quest.MinLevel, quest.MaxLevel, quest.EstimatedDuration, quest.Difficulty,
		choicePointsData, endingVariationsData, reputationImpactsData,
		narrativeSetupData, keyCharactersData, themesData, quest.UpdatedAt,
	)

	return err
}

// DeleteDynamicQuest marks a quest as disabled
func (r *Repository) DeleteDynamicQuest(ctx context.Context, questID string) error {
	query := `UPDATE dynamic_quests SET status = 'disabled', updated_at = $2 WHERE quest_id = $1`
	_, err := r.db.ExecContext(ctx, query, questID, time.Now())
	return err
}

// ListDynamicQuests retrieves quests by type with pagination
func (r *Repository) ListDynamicQuests(ctx context.Context, questType string, limit, offset int) ([]*models.DynamicQuest, error) {
	query := `
		SELECT id, quest_id, title, description, quest_type, min_level, max_level,
			   estimated_duration, difficulty
		FROM dynamic_quests
		WHERE status = 'active' AND ($1 = '' OR quest_type = $1)
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, questType, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to list dynamic quests: %w", err)
	}
	defer rows.Close()

	var quests []*models.DynamicQuest
	for rows.Next() {
		var quest models.DynamicQuest
		err := rows.Scan(
			&quest.ID, &quest.QuestID, &quest.Title, &quest.Description,
			&quest.QuestType, &quest.MinLevel, &quest.MaxLevel,
			&quest.EstimatedDuration, &quest.Difficulty,
		)
		if err != nil {
			continue
		}
		quests = append(quests, &quest)
	}

	return quests, nil
}

// CreatePlayerQuestState creates player quest state
func (r *Repository) CreatePlayerQuestState(ctx context.Context, state *models.PlayerQuestState) error {
	madeChoicesData, _ := json.Marshal(state.MadeChoices)
	reputationChangesData, _ := json.Marshal(state.ReputationChanges)
	relationshipChangesData, _ := json.Marshal(state.RelationshipChanges)
	unlockedContentData, _ := json.Marshal(state.UnlockedContent)
	achievementsData, _ := json.Marshal(state.Achievements)

	query := `
		INSERT INTO player_quest_states (
			id, player_id, quest_id, current_state, current_choice_point,
			made_choices, reputation_changes, relationship_changes, unlocked_content,
			quest_ending, start_time, last_activity, completion_time, time_spent,
			difficulty, score, achievements, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19)
	`

	state.ID = fmt.Sprintf("pqs_%d", time.Now().UnixNano())
	state.CreatedAt = time.Now()
	state.UpdatedAt = time.Now()
	state.StartTime = time.Now()
	state.LastActivity = time.Now()
	state.CurrentState = "in_progress"

	_, err := r.db.ExecContext(ctx, query,
		state.ID, state.PlayerID, state.QuestID, state.CurrentState, state.CurrentChoicePoint,
		madeChoicesData, reputationChangesData, relationshipChangesData, unlockedContentData,
		state.QuestEnding, state.StartTime, state.LastActivity, state.CompletionTime,
		state.TimeSpent, state.Difficulty, state.Score, achievementsData,
		state.CreatedAt, state.UpdatedAt,
	)

	return err
}

// GetPlayerQuestState retrieves player quest state
func (r *Repository) GetPlayerQuestState(ctx context.Context, playerID, questID string) (*models.PlayerQuestState, error) {
	query := `
		SELECT id, player_id, quest_id, current_state, current_choice_point,
			   made_choices, reputation_changes, relationship_changes, unlocked_content,
			   quest_ending, start_time, last_activity, completion_time, time_spent,
			   difficulty, score, achievements, created_at, updated_at
		FROM player_quest_states
		WHERE player_id = $1 AND quest_id = $2
	`

	var state models.PlayerQuestState
	var madeChoicesData, reputationChangesData, relationshipChangesData, unlockedContentData, achievementsData []byte
	var completionTime sql.NullTime

	err := r.db.QueryRowContext(ctx, query, playerID, questID).Scan(
		&state.ID, &state.PlayerID, &state.QuestID, &state.CurrentState, &state.CurrentChoicePoint,
		&madeChoicesData, &reputationChangesData, &relationshipChangesData, &unlockedContentData,
		&state.QuestEnding, &state.StartTime, &state.LastActivity, &completionTime, &state.TimeSpent,
		&state.Difficulty, &state.Score, &achievementsData, &state.CreatedAt, &state.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("player quest state not found: %s-%s", playerID, questID)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get player quest state: %w", err)
	}

	if completionTime.Valid {
		state.CompletionTime = &completionTime.Time
	}

	// Unmarshal JSON data
	if err := json.Unmarshal(madeChoicesData, &state.MadeChoices); err != nil {
		state.MadeChoices = []models.MadeChoice{}
	}
	if err := json.Unmarshal(reputationChangesData, &state.ReputationChanges); err != nil {
		state.ReputationChanges = []models.ReputationChange{}
	}
	if err := json.Unmarshal(relationshipChangesData, &state.RelationshipChanges); err != nil {
		state.RelationshipChanges = []models.RelationshipChange{}
	}
	if err := json.Unmarshal(unlockedContentData, &state.UnlockedContent); err != nil {
		state.UnlockedContent = []string{}
	}
	if err := json.Unmarshal(achievementsData, &state.Achievements); err != nil {
		state.Achievements = []string{}
	}

	return &state, nil
}

// UpdatePlayerQuestState updates player quest state
func (r *Repository) UpdatePlayerQuestState(ctx context.Context, state *models.PlayerQuestState) error {
	madeChoicesData, _ := json.Marshal(state.MadeChoices)
	reputationChangesData, _ := json.Marshal(state.ReputationChanges)
	relationshipChangesData, _ := json.Marshal(state.RelationshipChanges)
	unlockedContentData, _ := json.Marshal(state.UnlockedContent)
	achievementsData, _ := json.Marshal(state.Achievements)

	query := `
		UPDATE player_quest_states
		SET current_state = $3, current_choice_point = $4, made_choices = $5,
		    reputation_changes = $6, relationship_changes = $7, unlocked_content = $8,
		    quest_ending = $9, last_activity = $10, completion_time = $11, time_spent = $12,
		    score = $13, achievements = $14, updated_at = $15
		WHERE player_id = $1 AND quest_id = $2
	`

	state.UpdatedAt = time.Now()
	state.LastActivity = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		state.PlayerID, state.QuestID, state.CurrentState, state.CurrentChoicePoint,
		madeChoicesData, reputationChangesData, relationshipChangesData, unlockedContentData,
		state.QuestEnding, state.LastActivity, state.CompletionTime, state.TimeSpent,
		state.Score, achievementsData, state.UpdatedAt,
	)

	return err
}

// GetPlayerActiveQuests retrieves active quests for player
func (r *Repository) GetPlayerActiveQuests(ctx context.Context, playerID string) ([]*models.PlayerQuestState, error) {
	query := `
		SELECT id, player_id, quest_id, current_state, current_choice_point, start_time, last_activity
		FROM player_quest_states
		WHERE player_id = $1 AND current_state = 'in_progress'
		ORDER BY last_activity DESC
	`

	rows, err := r.db.QueryContext(ctx, query, playerID)
	if err != nil {
		return nil, fmt.Errorf("failed to get active quests: %w", err)
	}
	defer rows.Close()

	var quests []*models.PlayerQuestState
	for rows.Next() {
		var quest models.PlayerQuestState
		err := rows.Scan(
			&quest.ID, &quest.PlayerID, &quest.QuestID, &quest.CurrentState,
			&quest.CurrentChoicePoint, &quest.StartTime, &quest.LastActivity,
		)
		if err != nil {
			continue
		}
		quests = append(quests, &quest)
	}

	return quests, nil
}

// GetPlayerCompletedQuests retrieves completed quests for player
func (r *Repository) GetPlayerCompletedQuests(ctx context.Context, playerID string, limit, offset int) ([]*models.PlayerQuestState, error) {
	query := `
		SELECT id, player_id, quest_id, quest_ending, completion_time, time_spent, score, achievements
		FROM player_quest_states
		WHERE player_id = $1 AND current_state = 'completed'
		ORDER BY completion_time DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get completed quests: %w", err)
	}
	defer rows.Close()

	var quests []*models.PlayerQuestState
	for rows.Next() {
		var quest models.PlayerQuestState
		var completionTime sql.NullTime
		var achievementsData []byte

		err := rows.Scan(
			&quest.ID, &quest.PlayerID, &quest.QuestID, &quest.QuestEnding,
			&completionTime, &quest.TimeSpent, &quest.Score, &achievementsData,
		)
		if err != nil {
			continue
		}

		if completionTime.Valid {
			quest.CompletionTime = &completionTime.Time
		}

		if err := json.Unmarshal(achievementsData, &quest.Achievements); err != nil {
			quest.Achievements = []string{}
		}

		quests = append(quests, &quest)
	}

	return quests, nil
}

// ProcessPlayerChoice processes a player choice and returns consequences
func (r *Repository) ProcessPlayerChoice(ctx context.Context, request *models.QuestChoiceRequest) (*models.QuestChoiceResponse, error) {
	// Get quest definition
	quest, err := r.GetDynamicQuest(ctx, request.QuestID)
	if err != nil {
		return &models.QuestChoiceResponse{Success: false, Error: err.Error()}, nil
	}

	// Get player state
	state, err := r.GetPlayerQuestState(ctx, request.PlayerID, request.QuestID)
	if err != nil {
		return &models.QuestChoiceResponse{Success: false, Error: err.Error()}, nil
	}

	// Validate choice requirements
	valid, err := r.ValidateChoiceRequirements(ctx, request.PlayerID, request.QuestID, request.ChoicePointID, request.ChoiceID)
	if err != nil {
		return &models.QuestChoiceResponse{Success: false, Error: err.Error()}, nil
	}
	if !valid {
		return &models.QuestChoiceResponse{Success: false, Error: "Choice requirements not met"}, nil
	}

	// Find the choice point and choice
	var choicePoint *models.ChoicePoint
	var choice *models.Choice
	for _, cp := range quest.ChoicePoints {
		if cp.ID == request.ChoicePointID {
			choicePoint = &cp
			for _, c := range cp.Choices {
				if c.ID == request.ChoiceID {
					choice = &c
					break
				}
			}
			break
		}
	}

	if choice == nil {
		return &models.QuestChoiceResponse{Success: false, Error: "Invalid choice"}, nil
	}

	// Apply consequences
	consequenceResults := r.applyChoiceConsequences(ctx, request.PlayerID, request.QuestID, choice.Consequences)

	// Record choice
	madeChoice := models.MadeChoice{
		ChoicePointID: request.ChoicePointID,
		ChoiceID:      request.ChoiceID,
		Timestamp:     time.Now(),
		TimeToDecide:  request.TimeToDecide,
		Context:       request.Context,
	}
	state.MadeChoices = append(state.MadeChoices, madeChoice)

	// Update state
	state.LastActivity = time.Now()
	if err := r.UpdatePlayerQuestState(ctx, state); err != nil {
		return &models.QuestChoiceResponse{Success: false, Error: err.Error()}, nil
	}

	response := &models.QuestChoiceResponse{
		Success:      true,
		NewState:     state.CurrentState,
		Consequences: consequenceResults,
	}

	// Check if quest is completed
	if r.isQuestCompleted(quest, state) {
		response.QuestCompleted = true
		ending := r.determineQuestEnding(quest, state)
		response.EndingVariation = &ending
		state.CurrentState = "completed"
		state.QuestEnding = &ending.ID
		now := time.Now()
		state.CompletionTime = &now
		r.UpdatePlayerQuestState(ctx, state)
	}

	return response, nil
}

// ValidateChoiceRequirements checks if player meets choice requirements
func (r *Repository) ValidateChoiceRequirements(ctx context.Context, playerID, questID, choicePointID, choiceID string) (bool, error) {
	// Implementation would check player reputation, inventory, etc.
	// For now, return true
	return true, nil
}

// GetQuestAnalytics retrieves quest analytics
func (r *Repository) GetQuestAnalytics(ctx context.Context, questID string) (*models.QuestAnalytics, error) {
	query := `
		SELECT total_players, completion_rate, average_time, popular_choices, ending_distribution,
			   difficulty_ratings, player_retention
		FROM quest_analytics WHERE quest_id = $1
	`

	var analytics models.QuestAnalytics
	analytics.QuestID = questID
	var popularChoicesData, endingDistributionData, difficultyRatingsData, playerRetentionData []byte

	err := r.db.QueryRowContext(ctx, query, questID).Scan(
		&analytics.TotalPlayers, &analytics.CompletionRate, &analytics.AverageTime,
		&popularChoicesData, &endingDistributionData, &difficultyRatingsData, &playerRetentionData,
	)

	if err == sql.ErrNoRows {
		return &models.QuestAnalytics{
			QuestID:            questID,
			TotalPlayers:       0,
			CompletionRate:     0,
			AverageTime:        0,
			PopularChoices:     make(map[string]int64),
			EndingDistribution: make(map[string]int64),
			DifficultyRatings:  make(map[string]float64),
			PlayerRetention:    make(map[string]int64),
		}, nil
	}

	// Unmarshal JSON data
	json.Unmarshal(popularChoicesData, &analytics.PopularChoices)
	json.Unmarshal(endingDistributionData, &analytics.EndingDistribution)
	json.Unmarshal(difficultyRatingsData, &analytics.DifficultyRatings)
	json.Unmarshal(playerRetentionData, &analytics.PlayerRetention)

	return &analytics, err
}

// UpdateQuestAnalytics updates quest analytics
func (r *Repository) UpdateQuestAnalytics(ctx context.Context, analytics *models.QuestAnalytics) error {
	popularChoicesData, _ := json.Marshal(analytics.PopularChoices)
	endingDistributionData, _ := json.Marshal(analytics.EndingDistribution)
	difficultyRatingsData, _ := json.Marshal(analytics.DifficultyRatings)
	playerRetentionData, _ := json.Marshal(analytics.PlayerRetention)

	query := `
		INSERT INTO quest_analytics (
			quest_id, total_players, completion_rate, average_time, popular_choices,
			ending_distribution, difficulty_ratings, player_retention
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		ON CONFLICT (quest_id) DO UPDATE SET
			total_players = EXCLUDED.total_players,
			completion_rate = EXCLUDED.completion_rate,
			average_time = EXCLUDED.average_time,
			popular_choices = EXCLUDED.popular_choices,
			ending_distribution = EXCLUDED.ending_distribution,
			difficulty_ratings = EXCLUDED.difficulty_ratings,
			player_retention = EXCLUDED.player_retention
	`

	_, err := r.db.ExecContext(ctx, query,
		analytics.QuestID, analytics.TotalPlayers, analytics.CompletionRate, analytics.AverageTime,
		popularChoicesData, endingDistributionData, difficultyRatingsData, playerRetentionData,
	)

	return err
}

// CreateQuestTemplate creates a quest template
func (r *Repository) CreateQuestTemplate(ctx context.Context, template *models.QuestTemplate) error {
	choicePointTemplatesData, _ := json.Marshal(template.ChoicePointTemplates)
	endingTemplatesData, _ := json.Marshal(template.EndingTemplates)
	variablesData, _ := json.Marshal(template.Variables)

	query := `
		INSERT INTO quest_templates (id, name, description, category, difficulty,
									choice_point_templates, ending_templates, variables, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
	`

	template.ID = fmt.Sprintf("qt_%d", time.Now().UnixNano())
	template.CreatedAt = time.Now()
	template.UpdatedAt = time.Now()

	_, err := r.db.ExecContext(ctx, query,
		template.ID, template.Name, template.Description, template.Category, template.Difficulty,
		choicePointTemplatesData, endingTemplatesData, variablesData,
		template.CreatedAt, template.UpdatedAt,
	)

	return err
}

// GetQuestTemplate retrieves a quest template
func (r *Repository) GetQuestTemplate(ctx context.Context, templateID string) (*models.QuestTemplate, error) {
	query := `SELECT id, name, description, category, difficulty, choice_point_templates,
					 ending_templates, variables, created_at, updated_at
			  FROM quest_templates WHERE id = $1`

	var template models.QuestTemplate
	var choicePointTemplatesData, endingTemplatesData, variablesData []byte

	err := r.db.QueryRowContext(ctx, query, templateID).Scan(
		&template.ID, &template.Name, &template.Description, &template.Category, &template.Difficulty,
		&choicePointTemplatesData, &endingTemplatesData, &variablesData,
		&template.CreatedAt, &template.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, fmt.Errorf("quest template not found: %s", templateID)
	}

	json.Unmarshal(choicePointTemplatesData, &template.ChoicePointTemplates)
	json.Unmarshal(endingTemplatesData, &template.EndingTemplates)
	json.Unmarshal(variablesData, &template.Variables)

	return &template, err
}

// ListQuestTemplates lists quest templates
func (r *Repository) ListQuestTemplates(ctx context.Context, category string, limit, offset int) ([]*models.QuestTemplate, error) {
	query := `
		SELECT id, name, description, category, difficulty
		FROM quest_templates
		WHERE ($1 = '' OR category = $1)
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, category, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to list quest templates: %w", err)
	}
	defer rows.Close()

	var templates []*models.QuestTemplate
	for rows.Next() {
		var template models.QuestTemplate
		err := rows.Scan(
			&template.ID, &template.Name, &template.Description,
			&template.Category, &template.Difficulty,
		)
		if err != nil {
			continue
		}
		templates = append(templates, &template)
	}

	return templates, nil
}

// GenerateDynamicQuest generates a quest from template
func (r *Repository) GenerateDynamicQuest(ctx context.Context, request *models.QuestGenerationRequest) (*models.QuestGenerationResponse, error) {
	template, err := r.GetQuestTemplate(ctx, request.TemplateID)
	if err != nil {
		return &models.QuestGenerationResponse{
			Success: false,
			Error:   err.Error(),
		}, nil
	}

	// Generate quest from template (simplified implementation)
	quest := &models.DynamicQuest{
		QuestID:   fmt.Sprintf("gen_%d", time.Now().UnixNano()),
		Title:     template.Name,
		Description: template.Description,
		QuestType: "generated",
		Difficulty: request.Difficulty,
		Status:    "active",
	}

	// Convert templates to actual quest components
	// This would be more complex in a real implementation

	return &models.QuestGenerationResponse{
		Success: true,
		Quest:   quest,
	}, nil
}

// HealthCheck performs database health check
func (r *Repository) HealthCheck(ctx context.Context) error {
	return r.db.PingContext(ctx)
}

// Helper methods

func (r *Repository) applyChoiceConsequences(ctx context.Context, playerID, questID string, consequences []models.Consequence) []models.ConsequenceResult {
	results := make([]models.ConsequenceResult, len(consequences))
	for i, consequence := range consequences {
		results[i] = models.ConsequenceResult{
			Type:        consequence.Type,
			Description: consequence.Description,
			Value:       consequence.Value,
			Success:     true, // Simplified
		}
	}
	return results
}

func (r *Repository) isQuestCompleted(quest *models.DynamicQuest, state *models.PlayerQuestState) bool {
	// Simplified completion check
	return len(state.MadeChoices) >= len(quest.ChoicePoints)
}

func (r *Repository) determineQuestEnding(quest *models.DynamicQuest, state *models.PlayerQuestState) models.EndingVariation {
	// Simplified ending determination
	if len(quest.EndingVariations) > 0 {
		return quest.EndingVariations[0]
	}
	return models.EndingVariation{
		ID:          "default_ending",
		Title:       "Quest Completed",
		Description: "You have completed this quest.",
	}
}
