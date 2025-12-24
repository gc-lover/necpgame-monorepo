// Repository layer for Dynamic Quests Service
// Issue: #2244
// Agent: Backend

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"go.uber.org/zap"
)

// Repository handles all database operations
type Repository struct {
	db     *sql.DB
	logger *zap.SugaredLogger
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB, logger *zap.SugaredLogger) *Repository {
	return &Repository{
		db:     db,
		logger: logger,
	}
}

// QuestDefinition represents a quest definition from database
type QuestDefinition struct {
	QuestID         string          `json:"quest_id"`
	Title           string          `json:"title"`
	Description     string          `json:"description"`
	QuestType       string          `json:"quest_type"`
	MinLevel        int             `json:"min_level"`
	MaxLevel        int             `json:"max_level"`
	ChoicePoints    json.RawMessage `json:"choice_points"`
	EndingVariations json.RawMessage `json:"ending_variations"`
	ReputationImpacts json.RawMessage `json:"reputation_impacts"`
	CreatedAt       time.Time       `json:"created_at"`
	UpdatedAt       time.Time       `json:"updated_at"`
}

// PlayerQuestState represents a player's quest progress
type PlayerQuestState struct {
	PlayerID     string          `json:"player_id"`
	QuestID      string          `json:"quest_id"`
	CurrentState string          `json:"current_state"`
	ChoiceHistory json.RawMessage `json:"choice_history"`
	ReputationSnapshot json.RawMessage `json:"reputation_snapshot"`
	StartedAt    time.Time       `json:"started_at"`
	CompletedAt  *time.Time      `json:"completed_at"`
	EndingAchieved string         `json:"ending_achieved"`
}

// PlayerReputation represents player reputation scores
type PlayerReputation struct {
	PlayerID         string    `json:"player_id"`
	CorporateRep     int       `json:"corporate_rep"`
	StreetRep        int       `json:"street_rep"`
	HumanityScore    int       `json:"humanity_score"`
	FactionStanding  string    `json:"faction_standing"`
	LastUpdated      time.Time `json:"last_updated"`
}

// ChoiceHistory represents a recorded player choice
type ChoiceHistory struct {
	ChoiceID    string          `json:"choice_id"`
	PlayerID    string          `json:"player_id"`
	QuestID     string          `json:"quest_id"`
	ChoicePoint string          `json:"choice_point"`
	ChoiceValue string          `json:"choice_value"`
	Timestamp   time.Time       `json:"timestamp"`
	RepBefore   json.RawMessage `json:"reputation_before"`
	RepAfter    json.RawMessage `json:"reputation_after"`
}

// CreateQuestDefinition creates a new quest definition
func (r *Repository) CreateQuestDefinition(ctx context.Context, quest *QuestDefinition) error {
	query := `
		INSERT INTO gameplay.dynamic_quests (
			quest_id, title, description, quest_type, min_level, max_level,
			choice_points, ending_variations, reputation_impacts, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
		ON CONFLICT (quest_id) DO UPDATE SET
			title = EXCLUDED.title,
			description = EXCLUDED.description,
			quest_type = EXCLUDED.quest_type,
			min_level = EXCLUDED.min_level,
			max_level = EXCLUDED.max_level,
			choice_points = EXCLUDED.choice_points,
			ending_variations = EXCLUDED.ending_variations,
			reputation_impacts = EXCLUDED.reputation_impacts,
			updated_at = EXCLUDED.updated_at
	`

	now := time.Now()
	_, err := r.db.ExecContext(ctx, query,
		quest.QuestID, quest.Title, quest.Description, quest.QuestType,
		quest.MinLevel, quest.MaxLevel, quest.ChoicePoints,
		quest.EndingVariations, quest.ReputationImpacts, now, now)

	if err != nil {
		r.logger.Errorf("Failed to create quest definition: %v", err)
		return fmt.Errorf("failed to create quest definition: %w", err)
	}

	r.logger.Infof("Quest definition created/updated: %s", quest.QuestID)
	return nil
}

// GetQuestDefinition retrieves a quest definition by ID
func (r *Repository) GetQuestDefinition(ctx context.Context, questID string) (*QuestDefinition, error) {
	query := `
		SELECT quest_id, title, description, quest_type, min_level, max_level,
			   choice_points, ending_variations, reputation_impacts, created_at, updated_at
		FROM gameplay.dynamic_quests
		WHERE quest_id = $1
	`

	var quest QuestDefinition
	err := r.db.QueryRowContext(ctx, query, questID).Scan(
		&quest.QuestID, &quest.Title, &quest.Description, &quest.QuestType,
		&quest.MinLevel, &quest.MaxLevel, &quest.ChoicePoints,
		&quest.EndingVariations, &quest.ReputationImpacts,
		&quest.CreatedAt, &quest.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("quest not found: %s", questID)
		}
		r.logger.Errorf("Failed to get quest definition: %v", err)
		return nil, fmt.Errorf("failed to get quest definition: %w", err)
	}

	return &quest, nil
}

// ListQuestDefinitions retrieves quest definitions with pagination
func (r *Repository) ListQuestDefinitions(ctx context.Context, limit, offset int) ([]*QuestDefinition, error) {
	query := `
		SELECT quest_id, title, description, quest_type, min_level, max_level,
			   choice_points, ending_variations, reputation_impacts, created_at, updated_at
		FROM gameplay.dynamic_quests
		ORDER BY created_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.QueryContext(ctx, query, limit, offset)
	if err != nil {
		r.logger.Errorf("Failed to list quest definitions: %v", err)
		return nil, fmt.Errorf("failed to list quest definitions: %w", err)
	}
	defer rows.Close()

	var quests []*QuestDefinition
	for rows.Next() {
		var quest QuestDefinition
		err := rows.Scan(
			&quest.QuestID, &quest.Title, &quest.Description, &quest.QuestType,
			&quest.MinLevel, &quest.MaxLevel, &quest.ChoicePoints,
			&quest.EndingVariations, &quest.ReputationImpacts,
			&quest.CreatedAt, &quest.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan quest definition: %v", err)
			continue
		}
		quests = append(quests, &quest)
	}

	return quests, nil
}

// StartPlayerQuest starts a quest for a player
func (r *Repository) StartPlayerQuest(ctx context.Context, playerID, questID string, reputationSnapshot json.RawMessage) error {
	query := `
		INSERT INTO gameplay.player_quest_states (
			player_id, quest_id, current_state, choice_history,
			reputation_snapshot, started_at
		) VALUES ($1, $2, $3, $4, $5, $6)
		ON CONFLICT (player_id, quest_id) DO UPDATE SET
			current_state = EXCLUDED.current_state,
			reputation_snapshot = EXCLUDED.reputation_snapshot,
			started_at = EXCLUDED.started_at
		WHERE current_state = 'available'
	`

	_, err := r.db.ExecContext(ctx, query,
		playerID, questID, "active", "[]", reputationSnapshot, time.Now())

	if err != nil {
		r.logger.Errorf("Failed to start player quest: %v", err)
		return fmt.Errorf("failed to start player quest: %w", err)
	}

	r.logger.Infof("Player quest started: player=%s, quest=%s", playerID, questID)
	return nil
}

// GetPlayerQuestState retrieves a player's quest state
func (r *Repository) GetPlayerQuestState(ctx context.Context, playerID, questID string) (*PlayerQuestState, error) {
	query := `
		SELECT player_id, quest_id, current_state, choice_history,
			   reputation_snapshot, started_at, completed_at, ending_achieved
		FROM gameplay.player_quest_states
		WHERE player_id = $1 AND quest_id = $2
	`

	var state PlayerQuestState
	err := r.db.QueryRowContext(ctx, query, playerID, questID).Scan(
		&state.PlayerID, &state.QuestID, &state.CurrentState,
		&state.ChoiceHistory, &state.ReputationSnapshot,
		&state.StartedAt, &state.CompletedAt, &state.EndingAchieved,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("player quest state not found: player=%s, quest=%s", playerID, questID)
		}
		r.logger.Errorf("Failed to get player quest state: %v", err)
		return nil, fmt.Errorf("failed to get player quest state: %w", err)
	}

	return &state, nil
}

// UpdatePlayerQuestState updates a player's quest progress
func (r *Repository) UpdatePlayerQuestState(ctx context.Context, playerID, questID, newState string, choiceHistory json.RawMessage) error {
	query := `
		UPDATE gameplay.player_quest_states
		SET current_state = $3, choice_history = $4, updated_at = $5
		WHERE player_id = $1 AND quest_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, playerID, questID, newState, choiceHistory, time.Now())

	if err != nil {
		r.logger.Errorf("Failed to update player quest state: %v", err)
		return fmt.Errorf("failed to update player quest state: %w", err)
	}

	r.logger.Infof("Player quest state updated: player=%s, quest=%s, state=%s", playerID, questID, newState)
	return nil
}

// CompletePlayerQuest marks a quest as completed
func (r *Repository) CompletePlayerQuest(ctx context.Context, playerID, questID, endingAchieved string) error {
	query := `
		UPDATE gameplay.player_quest_states
		SET current_state = 'completed', completed_at = $3, ending_achieved = $4
		WHERE player_id = $1 AND quest_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, playerID, questID, time.Now(), endingAchieved)

	if err != nil {
		r.logger.Errorf("Failed to complete player quest: %v", err)
		return fmt.Errorf("failed to complete player quest: %w", err)
	}

	r.logger.Infof("Player quest completed: player=%s, quest=%s, ending=%s", playerID, questID, endingAchieved)
	return nil
}

// RecordPlayerChoice records a player's choice for audit trail
func (r *Repository) RecordPlayerChoice(ctx context.Context, choice *ChoiceHistory) error {
	query := `
		INSERT INTO gameplay.player_choice_history (
			choice_id, player_id, quest_id, choice_point, choice_value,
			timestamp, reputation_before, reputation_after
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		choice.ChoiceID, choice.PlayerID, choice.QuestID,
		choice.ChoicePoint, choice.ChoiceValue, choice.Timestamp,
		choice.RepBefore, choice.RepAfter)

	if err != nil {
		r.logger.Errorf("Failed to record player choice: %v", err)
		return fmt.Errorf("failed to record player choice: %w", err)
	}

	return nil
}

// GetPlayerReputation retrieves player reputation
func (r *Repository) GetPlayerReputation(ctx context.Context, playerID string) (*PlayerReputation, error) {
	query := `
		SELECT player_id, corporate_rep, street_rep, humanity_score,
			   faction_standing, last_updated
		FROM gameplay.player_reputation
		WHERE player_id = $1
	`

	var rep PlayerReputation
	err := r.db.QueryRowContext(ctx, query, playerID).Scan(
		&rep.PlayerID, &rep.CorporateRep, &rep.StreetRep,
		&rep.HumanityScore, &rep.FactionStanding, &rep.LastUpdated,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			// Return default reputation for new players
			return &PlayerReputation{
				PlayerID:        playerID,
				CorporateRep:    0,
				StreetRep:       0,
				HumanityScore:   50,
				FactionStanding: "neutral",
				LastUpdated:     time.Now(),
			}, nil
		}
		r.logger.Errorf("Failed to get player reputation: %v", err)
		return nil, fmt.Errorf("failed to get player reputation: %w", err)
	}

	return &rep, nil
}

// UpdatePlayerReputation updates player reputation scores
func (r *Repository) UpdatePlayerReputation(ctx context.Context, rep *PlayerReputation) error {
	query := `
		INSERT INTO gameplay.player_reputation (
			player_id, corporate_rep, street_rep, humanity_score,
			faction_standing, last_updated
		) VALUES ($1, $2, $3, $4, $5, $6)
		ON CONFLICT (player_id) DO UPDATE SET
			corporate_rep = EXCLUDED.corporate_rep,
			street_rep = EXCLUDED.street_rep,
			humanity_score = EXCLUDED.humanity_score,
			faction_standing = EXCLUDED.faction_standing,
			last_updated = EXCLUDED.last_updated
	`

	rep.LastUpdated = time.Now()
	_, err := r.db.ExecContext(ctx, query,
		rep.PlayerID, rep.CorporateRep, rep.StreetRep,
		rep.HumanityScore, rep.FactionStanding, rep.LastUpdated)

	if err != nil {
		r.logger.Errorf("Failed to update player reputation: %v", err)
		return fmt.Errorf("failed to update player reputation: %w", err)
	}

	r.logger.Infof("Player reputation updated: player=%s", rep.PlayerID)
	return nil
}

