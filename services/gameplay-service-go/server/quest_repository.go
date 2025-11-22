package server

import (
	"context"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/sirupsen/logrus"
)

type QuestRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewQuestRepository(db *pgxpool.Pool) *QuestRepository {
	return &QuestRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *QuestRepository) CreateQuestInstance(ctx context.Context, instance *models.QuestInstance) error {
	dialogueStateJSON, _ := json.Marshal(instance.DialogueState)
	objectivesJSON, _ := json.Marshal(instance.Objectives)

	query := `
		INSERT INTO gameplay.quest_instances (
			id, character_id, quest_id, status, current_node,
			dialogue_state, objectives, started_at, completed_at, updated_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, NOW(), $7, NOW()
		) RETURNING id, started_at, updated_at`

	err := r.db.QueryRow(ctx, query,
		instance.CharacterID, instance.QuestID, instance.Status, instance.CurrentNode,
		dialogueStateJSON, objectivesJSON, instance.CompletedAt,
	).Scan(&instance.ID, &instance.StartedAt, &instance.UpdatedAt)

	return err
}

func (r *QuestRepository) GetQuestInstance(ctx context.Context, instanceID uuid.UUID) (*models.QuestInstance, error) {
	var instance models.QuestInstance
	var dialogueStateJSON, objectivesJSON []byte

	query := `
		SELECT id, character_id, quest_id, status, current_node,
		       dialogue_state, objectives, started_at, completed_at, updated_at
		FROM gameplay.quest_instances
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, instanceID).Scan(
		&instance.ID, &instance.CharacterID, &instance.QuestID, &instance.Status,
		&instance.CurrentNode, &dialogueStateJSON, &objectivesJSON,
		&instance.StartedAt, &instance.CompletedAt, &instance.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(dialogueStateJSON) > 0 {
		json.Unmarshal(dialogueStateJSON, &instance.DialogueState)
	} else {
		instance.DialogueState = make(map[string]interface{})
	}

	if len(objectivesJSON) > 0 {
		json.Unmarshal(objectivesJSON, &instance.Objectives)
	} else {
		instance.Objectives = make(map[string]interface{})
	}

	return &instance, nil
}

func (r *QuestRepository) GetQuestInstanceByCharacterAndQuest(ctx context.Context, characterID uuid.UUID, questID string) (*models.QuestInstance, error) {
	var instance models.QuestInstance
	var dialogueStateJSON, objectivesJSON []byte

	query := `
		SELECT id, character_id, quest_id, status, current_node,
		       dialogue_state, objectives, started_at, completed_at, updated_at
		FROM gameplay.quest_instances
		WHERE character_id = $1 AND quest_id = $2`

	err := r.db.QueryRow(ctx, query, characterID, questID).Scan(
		&instance.ID, &instance.CharacterID, &instance.QuestID, &instance.Status,
		&instance.CurrentNode, &dialogueStateJSON, &objectivesJSON,
		&instance.StartedAt, &instance.CompletedAt, &instance.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(dialogueStateJSON) > 0 {
		json.Unmarshal(dialogueStateJSON, &instance.DialogueState)
	} else {
		instance.DialogueState = make(map[string]interface{})
	}

	if len(objectivesJSON) > 0 {
		json.Unmarshal(objectivesJSON, &instance.Objectives)
	} else {
		instance.Objectives = make(map[string]interface{})
	}

	return &instance, nil
}

func (r *QuestRepository) UpdateQuestInstance(ctx context.Context, instance *models.QuestInstance) error {
	dialogueStateJSON, _ := json.Marshal(instance.DialogueState)
	objectivesJSON, _ := json.Marshal(instance.Objectives)

	query := `
		UPDATE gameplay.quest_instances
		SET status = $1, current_node = $2, dialogue_state = $3,
		    objectives = $4, completed_at = $5, updated_at = NOW()
		WHERE id = $6`

	_, err := r.db.Exec(ctx, query,
		instance.Status, instance.CurrentNode, dialogueStateJSON,
		objectivesJSON, instance.CompletedAt, instance.ID,
	)

	return err
}

func (r *QuestRepository) ListQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus, limit, offset int) ([]models.QuestInstance, error) {
	var query string
	var args []interface{}

	if status != nil {
		query = `
			SELECT id, character_id, quest_id, status, current_node,
			       dialogue_state, objectives, started_at, completed_at, updated_at
			FROM gameplay.quest_instances
			WHERE character_id = $1 AND status = $2
			ORDER BY updated_at DESC
			LIMIT $3 OFFSET $4`
		args = []interface{}{characterID, *status, limit, offset}
	} else {
		query = `
			SELECT id, character_id, quest_id, status, current_node,
			       dialogue_state, objectives, started_at, completed_at, updated_at
			FROM gameplay.quest_instances
			WHERE character_id = $1
			ORDER BY updated_at DESC
			LIMIT $2 OFFSET $3`
		args = []interface{}{characterID, limit, offset}
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var instances []models.QuestInstance
	for rows.Next() {
		var instance models.QuestInstance
		var dialogueStateJSON, objectivesJSON []byte

		err := rows.Scan(
			&instance.ID, &instance.CharacterID, &instance.QuestID, &instance.Status,
			&instance.CurrentNode, &dialogueStateJSON, &objectivesJSON,
			&instance.StartedAt, &instance.CompletedAt, &instance.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(dialogueStateJSON) > 0 {
			json.Unmarshal(dialogueStateJSON, &instance.DialogueState)
		} else {
			instance.DialogueState = make(map[string]interface{})
		}

		if len(objectivesJSON) > 0 {
			json.Unmarshal(objectivesJSON, &instance.Objectives)
		} else {
			instance.Objectives = make(map[string]interface{})
		}

		instances = append(instances, instance)
	}

	return instances, nil
}

func (r *QuestRepository) CountQuestInstances(ctx context.Context, characterID uuid.UUID, status *models.QuestStatus) (int, error) {
	var count int
	var query string
	var args []interface{}

	if status != nil {
		query = `SELECT COUNT(*) FROM gameplay.quest_instances WHERE character_id = $1 AND status = $2`
		args = []interface{}{characterID, *status}
	} else {
		query = `SELECT COUNT(*) FROM gameplay.quest_instances WHERE character_id = $1`
		args = []interface{}{characterID}
	}

	err := r.db.QueryRow(ctx, query, args...).Scan(&count)
	return count, err
}

func (r *QuestRepository) CreateDialogueState(ctx context.Context, dialogueState *models.DialogueState) error {
	visitedNodesJSON, _ := json.Marshal(dialogueState.VisitedNodes)
	choicesJSON, _ := json.Marshal(dialogueState.Choices)

	query := `
		INSERT INTO gameplay.dialogue_state (
			id, quest_instance_id, character_id, current_node,
			visited_nodes, choices, updated_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, NOW()
		) RETURNING id, updated_at`

	err := r.db.QueryRow(ctx, query,
		dialogueState.QuestInstanceID, dialogueState.CharacterID, dialogueState.CurrentNode,
		visitedNodesJSON, choicesJSON,
	).Scan(&dialogueState.ID, &dialogueState.UpdatedAt)

	return err
}

func (r *QuestRepository) UpdateDialogueState(ctx context.Context, dialogueState *models.DialogueState) error {
	visitedNodesJSON, _ := json.Marshal(dialogueState.VisitedNodes)
	choicesJSON, _ := json.Marshal(dialogueState.Choices)

	query := `
		UPDATE gameplay.dialogue_state
		SET current_node = $1, visited_nodes = $2, choices = $3, updated_at = NOW()
		WHERE quest_instance_id = $4`

	_, err := r.db.Exec(ctx, query,
		dialogueState.CurrentNode, visitedNodesJSON, choicesJSON,
		dialogueState.QuestInstanceID,
	)

	return err
}

func (r *QuestRepository) GetDialogueState(ctx context.Context, questInstanceID uuid.UUID) (*models.DialogueState, error) {
	var dialogueState models.DialogueState
	var visitedNodesJSON, choicesJSON []byte

	query := `
		SELECT id, quest_instance_id, character_id, current_node,
		       visited_nodes, choices, updated_at
		FROM gameplay.dialogue_state
		WHERE quest_instance_id = $1`

	err := r.db.QueryRow(ctx, query, questInstanceID).Scan(
		&dialogueState.ID, &dialogueState.QuestInstanceID, &dialogueState.CharacterID,
		&dialogueState.CurrentNode, &visitedNodesJSON, &choicesJSON, &dialogueState.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(visitedNodesJSON) > 0 {
		json.Unmarshal(visitedNodesJSON, &dialogueState.VisitedNodes)
	} else {
		dialogueState.VisitedNodes = []string{}
	}

	if len(choicesJSON) > 0 {
		json.Unmarshal(choicesJSON, &dialogueState.Choices)
	} else {
		dialogueState.Choices = make(map[string]interface{})
	}

	return &dialogueState, nil
}

func (r *QuestRepository) CreateSkillCheckResult(ctx context.Context, result *models.SkillCheckResult) error {
	query := `
		INSERT INTO gameplay.skill_check_results (
			id, quest_instance_id, character_id, skill_id,
			required_level, actual_level, passed, checked_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, NOW()
		) RETURNING id, checked_at`

	err := r.db.QueryRow(ctx, query,
		result.QuestInstanceID, result.CharacterID, result.SkillID,
		result.RequiredLevel, result.ActualLevel, result.Passed,
	).Scan(&result.ID, &result.CheckedAt)

	return err
}

