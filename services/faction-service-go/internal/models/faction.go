//go:align 64
package models

import (
	"time"

	"github.com/google/uuid"
)

//go:align 64
type Faction struct {
	FactionID        uuid.UUID            `json:"faction_id" db:"faction_id"`
	Name             string               `json:"name" db:"name"`
	Description      string               `json:"description,omitempty" db:"description"`
	LeaderID         uuid.UUID            `json:"leader_id" db:"leader_id"`
	Reputation       int                  `json:"reputation" db:"reputation"`
	Influence        int                  `json:"influence" db:"influence"`
	DiplomaticStance string               `json:"diplomatic_stance" db:"diplomatic_stance"`
	MemberCount      int                  `json:"member_count" db:"member_count"`
	MaxMembers       int                  `json:"max_members" db:"max_members"`
	ActivityStatus   string               `json:"activity_status" db:"activity_status"`
	Requirements     FactionRequirements  `json:"requirements" db:"requirements"`
	Statistics       FactionStatistics    `json:"statistics" db:"statistics"`
	CreatedAt        time.Time            `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time            `json:"updated_at" db:"updated_at"`
}

//go:align 64
type FactionRequirements struct {
	MinReputation     int  `json:"min_reputation,omitempty" db:"min_reputation"`
	MinInfluence      int  `json:"min_influence,omitempty" db:"min_influence"`
	ApplicationReq    bool `json:"application_required" db:"application_required"`
	ApprovalReq       bool `json:"approval_required" db:"approval_required"`
	MinMemberLevel    int  `json:"min_member_level,omitempty" db:"min_member_level"`
}

//go:align 64
type FactionStatistics struct {
	WarsDeclared      int       `json:"wars_declared" db:"wars_declared"`
	WarsWon           int       `json:"wars_won" db:"wars_won"`
	AlliancesFormed   int       `json:"alliances_formed" db:"alliances_formed"`
	TerritoriesClaimed int      `json:"territories_claimed" db:"territories_claimed"`
	InfluenceGained   int       `json:"influence_gained" db:"influence_gained"`
	AvgMemberRep      float64   `json:"average_member_reputation" db:"avg_member_reputation"`
	LastDiplomaticAct *time.Time `json:"last_diplomatic_action,omitempty" db:"last_diplomatic_action"`
}

//go:align 64
type DiplomaticRelation struct {
	TargetFactionID   uuid.UUID `json:"target_faction_id" db:"target_faction_id"`
	TargetFactionName string    `json:"target_faction_name,omitempty" db:"target_faction_name"`
	Status            string    `json:"status" db:"status"`
	Standing          int       `json:"standing" db:"standing"`
	EstablishedAt     time.Time `json:"established_at" db:"established_at"`
	LastActionAt      *time.Time `json:"last_action_at,omitempty" db:"last_action_at"`
	Treaties          []Treaty  `json:"treaties,omitempty" db:"treaties"`
}

//go:align 64
type Treaty struct {
	TreatyID   uuid.UUID `json:"treaty_id" db:"treaty_id"`
	TreatyType string    `json:"treaty_type" db:"treaty_type"`
	SignedAt   time.Time `json:"signed_at" db:"signed_at"`
	ExpiresAt  *time.Time `json:"expires_at,omitempty" db:"expires_at"`
	Terms      string    `json:"terms,omitempty" db:"terms"`
}

//go:align 64
type DiplomaticAction struct {
	ActionID             uuid.UUID `json:"action_id" db:"action_id"`
	FactionID            uuid.UUID `json:"faction_id" db:"faction_id"`
	ActionType           string    `json:"action_type" db:"action_type"`
	TargetFactionID      uuid.UUID `json:"target_faction_id" db:"target_faction_id"`
	Status               string    `json:"status" db:"status"`
	Message              string    `json:"message,omitempty" db:"message"`
	TreatyTerms          string    `json:"treaty_terms,omitempty" db:"treaty_terms"`
	CreatedAt            time.Time `json:"created_at" db:"created_at"`
	ResponseDeadline     *time.Time `json:"target_faction_response_deadline,omitempty" db:"response_deadline"`
}

//go:align 64
type Territory struct {
	TerritoryID    uuid.UUID              `json:"territory_id" db:"territory_id"`
	Name           string                 `json:"name,omitempty" db:"name"`
	Boundaries     map[string]interface{} `json:"boundaries" db:"boundaries"`
	ControlLevel   float64                `json:"control_level" db:"control_level"`
	ClaimedAt      time.Time              `json:"claimed_at" db:"claimed_at"`
	LastConflictAt *time.Time             `json:"last_conflict_at,omitempty" db:"last_conflict_at"`
}

//go:align 64
type InfluenceZone struct {
	ZoneID         uuid.UUID   `json:"zone_id" db:"zone_id"`
	Center         Coordinate  `json:"center" db:"center"`
	Radius         float64     `json:"radius" db:"radius"`
	InfluenceLevel float64     `json:"influence_level" db:"influence_level"`
	ContestedBy    []uuid.UUID `json:"contested_by,omitempty" db:"contested_by"`
}

//go:align 64
type Coordinate struct {
	X float64 `json:"x" db:"x"`
	Y float64 `json:"y" db:"y"`
}

//go:align 64
type BorderDispute struct {
	DisputeID          uuid.UUID   `json:"dispute_id" db:"dispute_id"`
	DisputedTerritory  uuid.UUID   `json:"disputed_territory" db:"disputed_territory"`
	Claimants          []uuid.UUID `json:"claimants" db:"claimants"`
	DisputeStartedAt   time.Time   `json:"dispute_started_at" db:"dispute_started_at"`
	ResolutionDeadline *time.Time  `json:"resolution_deadline,omitempty" db:"resolution_deadline"`
}

//go:align 64
type TerritoryClaim struct {
	ClaimID         uuid.UUID   `json:"claim_id" db:"claim_id"`
	FactionID       uuid.UUID   `json:"faction_id" db:"faction_id"`
	CenterX         float64     `json:"center_x" db:"center_x"`
	CenterY         float64     `json:"center_y" db:"center_y"`
	Radius          float64     `json:"radius" db:"radius"`
	ClaimType       string      `json:"claim_type" db:"claim_type"`
	Status          string      `json:"status" db:"status"`
	Justification   string      `json:"justification,omitempty" db:"justification"`
	EstablishedAt   time.Time   `json:"established_at" db:"established_at"`
	DisputePeriod   int         `json:"dispute_period_days" db:"dispute_period"`
}

//go:align 64
type ReputationEvent struct {
	EventType    string    `json:"event_type" db:"event_type"`
	ValueChange  int       `json:"value_change" db:"value_change"`
	Timestamp    time.Time `json:"timestamp" db:"timestamp"`
	Description  string    `json:"description,omitempty" db:"description"`
}

//go:align 64
type SystemHealth struct {
	TotalMechanics   int64 `json:"total_mechanics"`
	ActiveMechanics  int64 `json:"active_mechanics"`
	TotalFactions    int64 `json:"total_factions"`
	ActiveFactions   int64 `json:"active_factions"`
	TotalDiplomacy   int64 `json:"total_diplomacy"`
	ActiveDiplomacy  int64 `json:"active_diplomacy"`
}