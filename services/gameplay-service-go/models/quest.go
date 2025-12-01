package models

import (
	"time"

	"github.com/google/uuid"
)

type QuestStatus string

const (
	QuestStatusNotStarted QuestStatus = "not_started"
	QuestStatusInProgress QuestStatus = "in_progress"
	QuestStatusCompleted  QuestStatus = "completed"
	QuestStatusFailed     QuestStatus = "failed"
	QuestStatusAbandoned  QuestStatus = "abandoned"
)

type ObjectiveStatus string

const (
	ObjectiveStatusPending   ObjectiveStatus = "pending"
	ObjectiveStatusInProgress ObjectiveStatus = "in_progress"
	ObjectiveStatusCompleted ObjectiveStatus = "completed"
	ObjectiveStatusFailed    ObjectiveStatus = "failed"
)

type QuestInstance struct {
	ID            uuid.UUID              `json:"id" db:"id"`
	CharacterID   uuid.UUID              `json:"character_id" db:"character_id"`
	QuestID       string                 `json:"quest_id" db:"quest_id"`
	Status        QuestStatus            `json:"status" db:"status"`
	CurrentNode   string                 `json:"current_node" db:"current_node"`
	DialogueState map[string]interface{} `json:"dialogue_state" db:"dialogue_state"`
	Objectives    map[string]interface{}  `json:"objectives" db:"objectives"`
	StartedAt     time.Time               `json:"started_at" db:"started_at"`
	CompletedAt   *time.Time              `json:"completed_at,omitempty" db:"completed_at"`
	UpdatedAt     time.Time               `json:"updated_at" db:"updated_at"`
}

type DialogueState struct {
	ID            uuid.UUID              `json:"id" db:"id"`
	QuestInstanceID uuid.UUID            `json:"quest_instance_id" db:"quest_instance_id"`
	CharacterID   uuid.UUID              `json:"character_id" db:"character_id"`
	CurrentNode   string                 `json:"current_node" db:"current_node"`
	VisitedNodes  []string               `json:"visited_nodes" db:"visited_nodes"`
	Choices       map[string]interface{} `json:"choices" db:"choices"`
	UpdatedAt     time.Time              `json:"updated_at" db:"updated_at"`
}

type SkillCheckResult struct {
	ID            uuid.UUID `json:"id" db:"id"`
	QuestInstanceID uuid.UUID `json:"quest_instance_id" db:"quest_instance_id"`
	CharacterID   uuid.UUID `json:"character_id" db:"character_id"`
	SkillID       string    `json:"skill_id" db:"skill_id"`
	RequiredLevel int       `json:"required_level" db:"required_level"`
	ActualLevel   int       `json:"actual_level" db:"actual_level"`
	Passed        bool      `json:"passed" db:"passed"`
	CheckedAt     time.Time `json:"checked_at" db:"checked_at"`
}

type StartQuestRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	QuestID     string    `json:"quest_id"`
}

type UpdateDialogueRequest struct {
	QuestInstanceID uuid.UUID `json:"quest_instance_id"`
	CharacterID     uuid.UUID `json:"character_id"`
	NodeID          string    `json:"node_id"`
	ChoiceID        *string   `json:"choice_id,omitempty"`
}

type PerformSkillCheckRequest struct {
	QuestInstanceID uuid.UUID `json:"quest_instance_id"`
	CharacterID     uuid.UUID `json:"character_id"`
	SkillID         string    `json:"skill_id"`
	RequiredLevel   int       `json:"required_level"`
}

type CompleteObjectiveRequest struct {
	QuestInstanceID uuid.UUID `json:"quest_instance_id"`
	CharacterID     uuid.UUID `json:"character_id"`
	ObjectiveID     string    `json:"objective_id"`
}

type QuestInstanceResponse struct {
	QuestInstance *QuestInstance `json:"quest_instance"`
}

type QuestListResponse struct {
	Quests []QuestInstance `json:"quests"`
	Total  int            `json:"total"`
}

type QuestDefinition struct {
	ID          uuid.UUID              `json:"id" db:"id"`
	QuestID     string                 `json:"quest_id" db:"quest_id"`
	Title       string                  `json:"title" db:"title"`
	Description string                 `json:"description" db:"description"`
	QuestType   string                 `json:"quest_type" db:"quest_type"`
	LevelMin    *int                   `json:"level_min,omitempty" db:"level_min"`
	LevelMax    *int                   `json:"level_max,omitempty" db:"level_max"`
	Requirements map[string]interface{} `json:"requirements" db:"requirements"`
	Objectives   map[string]interface{} `json:"objectives" db:"objectives"`
	Rewards      map[string]interface{} `json:"rewards" db:"rewards"`
	Branches     map[string]interface{} `json:"branches" db:"branches"`
	DialogueID   *uuid.UUID             `json:"dialogue_id,omitempty" db:"dialogue_id"`
	ContentData  map[string]interface{} `json:"content_data" db:"content_data"`
	Version      int                    `json:"version" db:"version"`
	IsActive     bool                   `json:"is_active" db:"is_active"`
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time              `json:"updated_at" db:"updated_at"`
}

type ReloadQuestContentRequest struct {
	QuestID    string                 `json:"quest_id"`
	YAMLContent map[string]interface{} `json:"yaml_content"`
}

type ReloadQuestContentResponse struct {
	QuestID    string    `json:"quest_id"`
	Message    string    `json:"message"`
	ImportedAt time.Time `json:"imported_at"`
}

