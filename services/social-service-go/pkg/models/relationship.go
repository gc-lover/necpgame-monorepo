// Relationship models for social system
// Issue: #140875791

package models

import (
	"time"

	"github.com/google/uuid"
)

// RelationshipLevel represents the strength of relationship between entities
type RelationshipLevel int

const (
	RelationshipLevelHostile RelationshipLevel = iota - 2
	RelationshipLevelUnfriendly
	RelationshipLevelNeutral
	RelationshipLevelFriendly
	RelationshipLevelTrusted
	RelationshipLevelAllied
)

// Relationship represents a relationship between two entities (players, NPCs, factions)
type Relationship struct {
	ID          uuid.UUID         `json:"id" db:"id"`
	SourceID    uuid.UUID         `json:"source_id" db:"source_id"`
	SourceType  EntityType        `json:"source_type" db:"source_type"`
	TargetID    uuid.UUID         `json:"target_id" db:"target_id"`
	TargetType  EntityType        `json:"target_type" db:"target_type"`
	Level       RelationshipLevel `json:"level" db:"level"`
	Trust       float64           `json:"trust" db:"trust"`             // 0-100
	Reputation  int               `json:"reputation" db:"reputation"`   // -100 to 100
	LastInteraction time.Time     `json:"last_interaction" db:"last_interaction"`
	InteractionCount int          `json:"interaction_count" db:"interaction_count"`
	CreatedAt   time.Time         `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time         `json:"updated_at" db:"updated_at"`
}

// EntityType defines the type of social entity
type EntityType string

const (
	EntityTypePlayer EntityType = "player"
	EntityTypeNPC    EntityType = "npc"
	EntityTypeFaction EntityType = "faction"
	EntityTypeGuild   EntityType = "guild"
)

// RelationshipModifier represents a modifier that can affect relationships
type RelationshipModifier struct {
	ID            uuid.UUID   `json:"id"`
	RelationshipID uuid.UUID  `json:"relationship_id"`
	Type          ModifierType `json:"type"`
	Value         int         `json:"value"`
	Reason        string      `json:"reason"`
	ExpiresAt     *time.Time  `json:"expires_at"`
	CreatedAt     time.Time   `json:"created_at"`
}

// ModifierType defines types of relationship modifiers
type ModifierType string

const (
	ModifierTypeTrade        ModifierType = "trade"
	ModifierTypeCombat       ModifierType = "combat"
	ModifierTypeQuest        ModifierType = "quest"
	ModifierTypeBetrayal     ModifierType = "betrayal"
	ModifierTypeAlliance     ModifierType = "alliance"
	ModifierTypeGift         ModifierType = "gift"
	ModifierTypeInsult       ModifierType = "insult"
)

// SocialNetwork represents a network of relationships for an entity
type SocialNetwork struct {
	EntityID       uuid.UUID       `json:"entity_id"`
	EntityType     EntityType      `json:"entity_type"`
	Relationships  []Relationship  `json:"relationships"`
	NetworkStats   NetworkStats    `json:"network_stats"`
	LastCalculated time.Time       `json:"last_calculated"`
}

// NetworkStats contains statistics about the social network
type NetworkStats struct {
	TotalRelationships int     `json:"total_relationships"`
	TrustedAllies      int     `json:"trusted_allies"`
	HostileEnemies     int     `json:"hostile_enemies"`
	AverageTrust       float64 `json:"average_trust"`
	NetworkStrength    float64 `json:"network_strength"` // Overall social power
}

// ReputationEntry represents a reputation record for an entity
type ReputationEntry struct {
	ID         uuid.UUID  `json:"id" db:"id"`
	EntityID   uuid.UUID  `json:"entity_id" db:"entity_id"`
	EntityType EntityType `json:"entity_type" db:"entity_type"`
	RegionID   string     `json:"region_id" db:"region_id"`
	Reputation int        `json:"reputation" db:"reputation"` // -100 to 100
	Reason     string     `json:"reason" db:"reason"`
	SourceID   uuid.UUID  `json:"source_id" db:"source_id"`
	SourceType EntityType `json:"source_type" db:"source_type"`
	CreatedAt  time.Time  `json:"created_at" db:"created_at"`
	ExpiresAt  *time.Time `json:"expires_at" db:"expires_at"`
}
