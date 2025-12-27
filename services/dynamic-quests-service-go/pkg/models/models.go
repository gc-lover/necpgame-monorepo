// Issue: #2244 - Dynamic Quest System Implementation
// Models for Dynamic Quests Service - Player-driven narrative system

package models

import (
	"time"
)

// DynamicQuest represents a dynamic quest with branching narrative
type DynamicQuest struct {
	ID              string                `json:"id" db:"id"`
	QuestID         string                `json:"quest_id" db:"quest_id"`               // Unique quest identifier
	Title           string                `json:"title" db:"title"`
	Description     string                `json:"description" db:"description"`
	QuestType       string                `json:"quest_type" db:"quest_type"`           // "main", "side", "faction"
	MinLevel        int                   `json:"min_level" db:"min_level"`
	MaxLevel        int                   `json:"max_level" db:"max_level"`
	EstimatedDuration int                 `json:"estimated_duration" db:"estimated_duration"` // minutes
	Difficulty      string                `json:"difficulty" db:"difficulty"`           // "easy", "medium", "hard", "legendary"
	ChoicePoints    []ChoicePoint         `json:"choice_points" db:"choice_points"`     // JSON array
	EndingVariations []EndingVariation    `json:"ending_variations" db:"ending_variations"` // JSON array
	ReputationImpacts []ReputationImpact  `json:"reputation_impacts" db:"reputation_impacts"` // JSON array
	NarrativeSetup  NarrativeSetup        `json:"narrative_setup" db:"narrative_setup"` // JSON object
	KeyCharacters   []KeyCharacter        `json:"key_characters" db:"key_characters"`   // JSON array
	Themes          []string              `json:"themes" db:"themes"`                   // JSON array
	Status          string                `json:"status" db:"status"`                   // "active", "disabled", "maintenance"
	CreatedAt       time.Time             `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time             `json:"updated_at" db:"updated_at"`
}

// ChoicePoint represents a major decision moment in the quest
type ChoicePoint struct {
	ID          string      `json:"id"`
	Sequence    int         `json:"sequence"`     // Order in quest progression
	Title       string      `json:"title"`
	Description string      `json:"description"`
	Context     string      `json:"context"`      // Narrative context
	Choices     []Choice    `json:"choices"`      // Available choices
	TimeLimit   *int        `json:"time_limit,omitempty"` // Seconds to decide (optional)
	Critical    bool        `json:"critical"`     // True if choice is irreversible
}

// Choice represents a single choice option at a choice point
type Choice struct {
	ID             string            `json:"id"`
	Text           string            `json:"text"`
	Description    string            `json:"description"`
	Consequences   []Consequence     `json:"consequences"`
	Requirements   []Requirement     `json:"requirements,omitempty"`
	Unlocks        []string          `json:"unlocks,omitempty"` // IDs of content this unlocks
	RiskLevel      string            `json:"risk_level"`         // "low", "medium", "high"
	MoralAlignment string            `json:"moral_alignment"`    // "good", "neutral", "evil"
}

// Consequence represents the outcome of making a choice
type Consequence struct {
	Type         string      `json:"type"`         // "reputation", "relationship", "quest_state", "item", "experience"
	Target       string      `json:"target"`       // What is affected
	Value        interface{} `json:"value"`        // Change value
	Probability  float64     `json:"probability"` // 0-1, chance this happens
	Description  string      `json:"description"`
}

// Requirement represents prerequisites for a choice
type Requirement struct {
	Type     string      `json:"type"`     // "reputation", "skill", "item", "quest_completed"
	Target   string      `json:"target"`
	Operator string      `json:"operator"` // "gte", "lte", "eq", "has"
	Value    interface{} `json:"value"`
}

// EndingVariation represents different possible quest conclusions
type EndingVariation struct {
	ID          string   `json:"id"`
	Title       string   `json:"title"`
	Description string   `json:"description"`
	Requirements []string `json:"requirements"` // Required choices/conditions
	Rewards     []Reward `json:"rewards"`
	Narrative   string   `json:"narrative"` // Ending story text
}

// Reward represents quest completion rewards
type Reward struct {
	Type     string      `json:"type"`     // "experience", "currency", "item", "reputation"
	Value    interface{} `json:"value"`
	Rarity   string      `json:"rarity,omitempty"`   // For items
	ItemID   string      `json:"item_id,omitempty"`  // For items
}

// ReputationImpact describes how choices affect player reputation
type ReputationImpact struct {
	Faction     string  `json:"faction"`
	Change      int     `json:"change"`       // -100 to +100
	Description string  `json:"description"`
	ChoiceID    string  `json:"choice_id"`    // Which choice causes this
}

// NarrativeSetup contains initial quest setup information
type NarrativeSetup struct {
	Location    string   `json:"location"`
	TimePeriod  string   `json:"time_period"`
	Weather     string   `json:"weather"`
	Situation   string   `json:"situation"`
	Objectives  []string `json:"objectives"`
}

// KeyCharacter represents an important NPC in the quest
type KeyCharacter struct {
	ID          string `json:"id"`
	Name        string `json:"name"`
	Role        string `json:"role"`
	Description string `json:"description"`
	Importance  string `json:"importance"` // "primary", "secondary", "tertiary"
}

// PlayerQuestState tracks individual player's progress through a dynamic quest
type PlayerQuestState struct {
	ID              string                 `json:"id" db:"id"`
	PlayerID        string                 `json:"player_id" db:"player_id"`
	QuestID         string                 `json:"quest_id" db:"quest_id"`
	CurrentState    string                 `json:"current_state" db:"current_state"`       // "not_started", "in_progress", "completed", "failed"
	CurrentChoicePoint *string             `json:"current_choice_point" db:"current_choice_point"`
	MadeChoices     []MadeChoice           `json:"made_choices" db:"made_choices"`         // JSON array
	ReputationChanges []ReputationChange   `json:"reputation_changes" db:"reputation_changes"` // JSON array
	RelationshipChanges []RelationshipChange `json:"relationship_changes" db:"relationship_changes"` // JSON array
	UnlockedContent []string               `json:"unlocked_content" db:"unlocked_content"`   // JSON array
	QuestEnding     *string                `json:"quest_ending" db:"quest_ending"`         // Ending variation ID
	StartTime       time.Time              `json:"start_time" db:"start_time"`
	LastActivity    time.Time              `json:"last_activity" db:"last_activity"`
	CompletionTime  *time.Time             `json:"completion_time" db:"completion_time"`
	TimeSpent       int                    `json:"time_spent" db:"time_spent"`             // minutes
	Difficulty      string                 `json:"difficulty" db:"difficulty"`             // Chosen difficulty
	Score           int                    `json:"score" db:"score"`                       // Performance score
	Achievements    []string               `json:"achievements" db:"achievements"`         // Earned achievements
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

// MadeChoice represents a choice the player made
type MadeChoice struct {
	ChoicePointID string    `json:"choice_point_id"`
	ChoiceID      string    `json:"choice_id"`
	Timestamp     time.Time `json:"timestamp"`
	TimeToDecide  int       `json:"time_to_decide"` // seconds
	Context       string    `json:"context"`        // Additional context
}

// ReputationChange tracks reputation changes from choices
type ReputationChange struct {
	Faction       string    `json:"faction"`
	OldValue      int       `json:"old_value"`
	NewValue      int       `json:"new_value"`
	Change        int       `json:"change"`
	ChoiceID      string    `json:"choice_id"`
	Timestamp     time.Time `json:"timestamp"`
	Description   string    `json:"description"`
}

// RelationshipChange tracks NPC relationship changes
type RelationshipChange struct {
	CharacterID   string    `json:"character_id"`
	OldValue      int       `json:"old_value"`
	NewValue      int       `json:"new_value"`
	Change        int       `json:"change"`
	ChoiceID      string    `json:"choice_id"`
	Timestamp     time.Time `json:"timestamp"`
	Description   string    `json:"description"`
}

// QuestChoiceRequest represents a player's choice submission
type QuestChoiceRequest struct {
	PlayerID      string `json:"player_id"`
	QuestID       string `json:"quest_id"`
	ChoicePointID string `json:"choice_point_id"`
	ChoiceID      string `json:"choice_id"`
	TimeToDecide  int    `json:"time_to_decide,omitempty"` // seconds
	Context       string `json:"context,omitempty"`
}

// QuestChoiceResponse represents the response to a choice
type QuestChoiceResponse struct {
	Success          bool                 `json:"success"`
	NewState         string               `json:"new_state,omitempty"`
	Consequences     []ConsequenceResult  `json:"consequences,omitempty"`
	NextChoicePoint  *ChoicePoint         `json:"next_choice_point,omitempty"`
	NarrativeUpdate  string               `json:"narrative_update,omitempty"`
	QuestCompleted   bool                 `json:"quest_completed,omitempty"`
	EndingVariation  *EndingVariation     `json:"ending_variation,omitempty"`
	Error            string               `json:"error,omitempty"`
}

// ConsequenceResult represents the actual result of a consequence
type ConsequenceResult struct {
	Type        string      `json:"type"`
	Description string      `json:"description"`
	Value       interface{} `json:"value"`
	Success     bool        `json:"success"`
}

// QuestAnalytics provides analytics for quest performance
type QuestAnalytics struct {
	QuestID           string             `json:"quest_id"`
	TotalPlayers      int64              `json:"total_players"`
	CompletionRate    float64            `json:"completion_rate"`
	AverageTime       int64              `json:"average_time"`       // minutes
	PopularChoices    map[string]int64   `json:"popular_choices"`    // ChoiceID -> count
	EndingDistribution map[string]int64  `json:"ending_distribution"` // EndingID -> count
	DifficultyRatings map[string]float64 `json:"difficulty_ratings"` // Difficulty -> average rating
	PlayerRetention   map[string]int64   `json:"player_retention"`   // Stage -> players remaining
	LastUpdated       time.Time          `json:"last_updated"`
}

// QuestTemplate represents a template for generating dynamic quests
type QuestTemplate struct {
	ID              string            `json:"id" db:"id"`
	Name            string            `json:"name" db:"name"`
	Description     string            `json:"description" db:"description"`
	Category        string            `json:"category" db:"category"`         // "corporate", "street", "gang", "personal"
	Difficulty      string            `json:"difficulty" db:"difficulty"`
	ChoicePointTemplates []ChoicePointTemplate `json:"choice_point_templates" db:"choice_point_templates"`
	EndingTemplates []EndingTemplate  `json:"ending_templates" db:"ending_templates"`
	Variables       map[string]interface{} `json:"variables" db:"variables"` // Template variables
	CreatedAt       time.Time          `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time          `json:"updated_at" db:"updated_at"`
}

// ChoicePointTemplate for generating choice points
type ChoicePointTemplate struct {
	ID       string                  `json:"id"`
	Type     string                  `json:"type"`     // "corporate", "moral", "combat"
	Choices  []ChoiceTemplate        `json:"choices"`
	Context  string                  `json:"context"`
}

// ChoiceTemplate for generating choices
type ChoiceTemplate struct {
	ID             string         `json:"id"`
	TextTemplate   string         `json:"text_template"`
	Consequences   []Consequence  `json:"consequences"`
	MoralAlignment string         `json:"moral_alignment"`
}

// EndingTemplate for generating quest endings
type EndingTemplate struct {
	ID          string   `json:"id"`
	Title       string   `json:"title"`
	Narrative   string   `json:"narrative"`
	Requirements []string `json:"requirements"`
	Rewards     []Reward `json:"rewards"`
}

// QuestGenerationRequest for generating new quests
type QuestGenerationRequest struct {
	TemplateID  string                 `json:"template_id"`
	PlayerID    string                 `json:"player_id"`
	Variables   map[string]interface{} `json:"variables,omitempty"`
	Difficulty  string                 `json:"difficulty,omitempty"`
	Seed        int64                  `json:"seed,omitempty"` // For reproducible generation
}

// QuestGenerationResponse contains the generated quest
type QuestGenerationResponse struct {
	Success bool          `json:"success"`
	Quest   *DynamicQuest `json:"quest,omitempty"`
	Error   string        `json:"error,omitempty"`
}
