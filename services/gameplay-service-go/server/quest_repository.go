// Package server Issue: #50, #387, #388
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

type QuestRepositoryInterface interface {
	ImportQuest(ctx context.Context, quest *models.QuestDefinition) (*models.QuestDefinition, error)
	GetQuestByQuestID(ctx context.Context, questID string) (*models.QuestDefinition, error)
	CancelQuest(ctx context.Context, characterID uuid.UUID, questID uuid.UUID) error
	SaveQuest(ctx context.Context, questID string, version int, title, description, questType string, isActive bool, contentData map[string]interface{}) (*models.QuestDefinition, error)
}

type QuestRepository struct {
	db *pgxpool.Pool
}

func NewQuestRepository(db *pgxpool.Pool) QuestRepositoryInterface {
	return &QuestRepository{db: db}
}

func (r *QuestRepository) ImportQuest(ctx context.Context, quest *models.QuestDefinition) (*models.QuestDefinition, error) {
	requirementsJSON, _ := json.Marshal(quest.Requirements)
	objectivesJSON, _ := json.Marshal(quest.Objectives)
	rewardsJSON, _ := json.Marshal(quest.Rewards)
	branchesJSON, _ := json.Marshal(quest.Branches)
	contentDataJSON, _ := json.Marshal(quest.ContentData)

	now := time.Now()
	if quest.ID == uuid.Nil {
		quest.ID = uuid.New()
	}
	if quest.CreatedAt.IsZero() {
		quest.CreatedAt = now
	}
	quest.UpdatedAt = now

	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.quest_definitions 
		 (id, quest_id, title, description, quest_type, level_min, level_max,
		  requirements, objectives, rewards, branches, dialogue_id, content_data, version, is_active, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17)
		 ON CONFLICT (quest_id) DO UPDATE SET
		 title = $3, description = $4, quest_type = $5, level_min = $6, level_max = $7,
		 requirements = $8, objectives = $9, rewards = $10, branches = $11, dialogue_id = $12,
		 content_data = $13, version = $14, is_active = $15, updated_at = $17`,
		quest.ID, quest.QuestID, quest.Title, quest.Description, quest.QuestType,
		quest.LevelMin, quest.LevelMax, requirementsJSON, objectivesJSON, rewardsJSON,
		branchesJSON, quest.DialogueID, contentDataJSON, quest.Version, quest.IsActive,
		quest.CreatedAt, quest.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	return quest, nil
}

func (r *QuestRepository) GetQuestByQuestID(ctx context.Context, questID string) (*models.QuestDefinition, error) {
	var quest models.QuestDefinition
	var requirementsJSON, objectivesJSON, rewardsJSON, branchesJSON, contentDataJSON []byte
	var dialogueID sql.NullString

	err := r.db.QueryRow(ctx,
		`SELECT id, quest_id, title, description, quest_type, level_min, level_max,
		 requirements, objectives, rewards, branches, dialogue_id, content_data, version, is_active, created_at, updated_at
		 FROM gameplay.quest_definitions WHERE quest_id = $1`,
		questID).Scan(
		&quest.ID, &quest.QuestID, &quest.Title, &quest.Description, &quest.QuestType,
		&quest.LevelMin, &quest.LevelMax, &requirementsJSON, &objectivesJSON, &rewardsJSON,
		&branchesJSON, &dialogueID, &contentDataJSON, &quest.Version, &quest.IsActive,
		&quest.CreatedAt, &quest.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	json.Unmarshal(requirementsJSON, &quest.Requirements)
	json.Unmarshal(objectivesJSON, &quest.Objectives)
	json.Unmarshal(rewardsJSON, &quest.Rewards)
	json.Unmarshal(branchesJSON, &quest.Branches)
	json.Unmarshal(contentDataJSON, &quest.ContentData)

	if dialogueID.Valid {
		if id, err := uuid.Parse(dialogueID.String); err == nil {
			quest.DialogueID = &id
		}
	}

	return &quest, nil
}

func (r *QuestRepository) CancelQuest(ctx context.Context, characterID uuid.UUID, questID uuid.UUID) error {
	// Update quest instance status to cancelled
	_, err := r.db.Exec(ctx,
		`UPDATE gameplay.quest_instances 
		 SET state = 'CANCELLED', updated_at = $1
		 WHERE player_id = $2 AND quest_id = $3`,
		time.Now(), characterID, questID)

	if err != nil {
		return err
	}

	// Check if any row was affected
	var rowsAffected int64
	err = r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM gameplay.quest_instances 
		 WHERE player_id = $1 AND quest_id = $2`,
		characterID, questID).Scan(&rowsAffected)

	if err != nil {
		return err
	}

	if rowsAffected == 0 {
		return errors.New("quest not found")
	}

	return nil
}

// SaveQuest upserts quest definition from content import payload.
// It maps minimal fields into QuestDefinition and delegates to ImportQuest for DB upsert.
func (r *QuestRepository) SaveQuest(ctx context.Context, questID string, version int, title, description, questType string, isActive bool, contentData map[string]interface{}) (*models.QuestDefinition, error) {
	quest := &models.QuestDefinition{
		QuestID:      questID,
		Title:        title,
		Description:  description,
		QuestType:    questType,
		Requirements: map[string]interface{}{},
		Objectives:   map[string]interface{}{},
		Rewards:      map[string]interface{}{},
		Branches:     map[string]interface{}{},
		ContentData:  contentData,
		Version:      version,
		IsActive:     isActive,
	}

	return r.ImportQuest(ctx, quest)
}
