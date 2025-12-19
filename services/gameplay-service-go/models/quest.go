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
	ObjectiveStatusPending    ObjectiveStatus = "pending"
	ObjectiveStatusInProgress ObjectiveStatus = "in_progress"
	ObjectiveStatusCompleted  ObjectiveStatus = "completed"
	ObjectiveStatusFailed     ObjectiveStatus = "failed"
)

type QuestInstance struct {
	// OPTIMIZATION: Field alignment - large fields first
	DialogueState map[string]interface{} `json:"dialogue_state" db:"dialogue_state"`       // 24 bytes (map header)
	Objectives    map[string]interface{} `json:"objectives" db:"objectives"`               // 24 bytes (map header)
	StartedAt     time.Time              `json:"started_at" db:"started_at"`               // 24 bytes (time.Time)
	UpdatedAt     time.Time              `json:"updated_at" db:"updated_at"`               // 24 bytes (time.Time)
	CompletedAt   *time.Time             `json:"completed_at,omitempty" db:"completed_at"` // 8 bytes (pointer)
	ID            uuid.UUID              `json:"id" db:"id"`                               // 16 bytes (uuid.UUID)
	CharacterID   uuid.UUID              `json:"character_id" db:"character_id"`           // 16 bytes (uuid.UUID)
	QuestID       string                 `json:"quest_id" db:"quest_id"`                   // 16 bytes (string header)
	CurrentNode   string                 `json:"current_node" db:"current_node"`           // 16 bytes (string header)
	Status        QuestStatus            `json:"status" db:"status"`                       // 16 bytes (string)
}

type DialogueState struct {
	// OPTIMIZATION: Field alignment - large fields first
	Choices         map[string]interface{} `json:"choices" db:"choices"`                     // 24 bytes (map header)
	VisitedNodes    []string               `json:"visited_nodes" db:"visited_nodes"`         // 24 bytes (slice header)
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`               // 24 bytes (time.Time)
	ID              uuid.UUID              `json:"id" db:"id"`                               // 16 bytes (uuid.UUID)
	QuestInstanceID uuid.UUID              `json:"quest_instance_id" db:"quest_instance_id"` // 16 bytes (uuid.UUID)
	CharacterID     uuid.UUID              `json:"character_id" db:"character_id"`           // 16 bytes (uuid.UUID)
	CurrentNode     string                 `json:"current_node" db:"current_node"`           // 16 bytes (string header)
}

type SkillCheckResult struct {
	ID              uuid.UUID `json:"id" db:"id"`
	QuestInstanceID uuid.UUID `json:"quest_instance_id" db:"quest_instance_id"`
	CharacterID     uuid.UUID `json:"character_id" db:"character_id"`
	SkillID         string    `json:"skill_id" db:"skill_id"`
	RequiredLevel   int       `json:"required_level" db:"required_level"`
	ActualLevel     int       `json:"actual_level" db:"actual_level"`
	Passed          bool      `json:"passed" db:"passed"`
	CheckedAt       time.Time `json:"checked_at" db:"checked_at"`
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
	Total  int             `json:"total"`
}

type QuestDefinition struct {
	// OPTIMIZATION: Field alignment - large fields first
	ContentData  map[string]interface{} `json:"content_data" db:"content_data"`         // 24 bytes (map header)
	Rewards      map[string]interface{} `json:"rewards" db:"rewards"`                   // 24 bytes (map header)
	Branches     map[string]interface{} `json:"branches" db:"branches"`                 // 24 bytes (map header)
	Objectives   map[string]interface{} `json:"objectives" db:"objectives"`             // 24 bytes (map header)
	Requirements map[string]interface{} `json:"requirements" db:"requirements"`         // 24 bytes (map header)
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`             // 24 bytes (time.Time)
	UpdatedAt    time.Time              `json:"updated_at" db:"updated_at"`             // 24 bytes (time.Time)
	DialogueID   *uuid.UUID             `json:"dialogue_id,omitempty" db:"dialogue_id"` // 8 bytes (pointer)
	LevelMin     *int                   `json:"level_min,omitempty" db:"level_min"`     // 8 bytes (pointer)
	LevelMax     *int                   `json:"level_max,omitempty" db:"level_max"`     // 8 bytes (pointer)
	ID           uuid.UUID              `json:"id" db:"id"`                             // 16 bytes (uuid.UUID)
	QuestID      string                 `json:"quest_id" db:"quest_id"`                 // 16 bytes (string header)
	Title        string                 `json:"title" db:"title"`                       // 16 bytes (string header)
	Description  string                 `json:"description" db:"description"`           // 16 bytes (string header)
	QuestType    string                 `json:"quest_type" db:"quest_type"`             // 16 bytes (string header)
	Version      int                    `json:"version" db:"version"`                   // 8 bytes (int)
	IsActive     bool                   `json:"is_active" db:"is_active"`               // 1 byte (bool)
}

type ReloadQuestContentRequest struct {
	QuestID     string                 `json:"quest_id"`
	YAMLContent map[string]interface{} `json:"yaml_content"`
}

type ReloadQuestContentResponse struct {
	QuestID    string    `json:"quest_id"`
	Message    string    `json:"message"`
	ImportedAt time.Time `json:"imported_at"`
}
