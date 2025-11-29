package models

import (
	"time"

	"github.com/google/uuid"
)

type RelationshipType string

const (
	RelationshipTypeFriends    RelationshipType = "friends"
	RelationshipTypeCloseAllies RelationshipType = "close_allies"
	RelationshipTypePact       RelationshipType = "pact"
	RelationshipTypeNeutral    RelationshipType = "neutral"
	RelationshipTypeEnemies    RelationshipType = "enemies"
	RelationshipTypeNemesis     RelationshipType = "nemesis"
)

type Relationship struct {
	ID            uuid.UUID       `json:"id" db:"id"`
	PlayerID      uuid.UUID       `json:"player_id" db:"player_id"`
	TargetID      uuid.UUID       `json:"target_id" db:"target_id"`
	Type          RelationshipType `json:"type" db:"type"`
	CreatedAt     time.Time       `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time       `json:"updated_at" db:"updated_at"`
}

type SetRelationshipRequest struct {
	TargetID uuid.UUID       `json:"target_id"`
	Type     RelationshipType `json:"type"`
}

type RelationshipsResponse struct {
	Relationships []Relationship `json:"relationships"`
	Total         int            `json:"total"`
	Limit         int            `json:"limit"`
	Offset        int            `json:"offset"`
}

type TrustLevel struct {
	PlayerID    uuid.UUID `json:"player_id" db:"player_id"`
	TargetID    uuid.UUID `json:"target_id" db:"target_id"`
	Level       int       `json:"level" db:"level"`
	Experience  int       `json:"experience" db:"experience"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

type UpdateTrustRequest struct {
	TargetID  uuid.UUID `json:"target_id"`
	Delta     int       `json:"delta"`
	Reason    string    `json:"reason,omitempty"`
}

type TrustContract struct {
	ID          uuid.UUID `json:"id" db:"id"`
	PlayerID    uuid.UUID `json:"player_id" db:"player_id"`
	TargetID    uuid.UUID `json:"target_id" db:"target_id"`
	Terms       string    `json:"terms" db:"terms"`
	Status      string    `json:"status" db:"status"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	ExpiresAt   *time.Time `json:"expires_at,omitempty" db:"expires_at"`
	TerminatedAt *time.Time `json:"terminated_at,omitempty" db:"terminated_at"`
}

type CreateTrustContractRequest struct {
	TargetID uuid.UUID  `json:"target_id"`
	Terms    string     `json:"terms"`
	ExpiresAt *time.Time `json:"expires_at,omitempty"`
}

type Alliance struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	LeaderID    uuid.UUID `json:"leader_id" db:"leader_id"`
	Description string    `json:"description,omitempty" db:"description"`
	Status      string    `json:"status" db:"status"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
	TerminatedAt *time.Time `json:"terminated_at,omitempty" db:"terminated_at"`
}

type CreateAllianceRequest struct {
	Name        string `json:"name"`
	Description string `json:"description,omitempty"`
}

type AllianceListResponse struct {
	Alliances []Alliance `json:"alliances"`
	Total    int        `json:"total"`
}

type AllianceInviteRequest struct {
	PlayerID uuid.UUID `json:"player_id"`
}

type PlayerRating struct {
	PlayerID    uuid.UUID `json:"player_id" db:"player_id"`
	RaterID     uuid.UUID `json:"rater_id" db:"rater_id"`
	Rating      int       `json:"rating" db:"rating"`
	Comment     string    `json:"comment,omitempty" db:"comment"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

type UpdateRatingRequest struct {
	RaterID uuid.UUID `json:"rater_id"`
	Rating  int       `json:"rating"`
	Comment string    `json:"comment,omitempty"`
}

type PlayerRatingsResponse struct {
	Ratings []PlayerRating `json:"ratings"`
	Average float64        `json:"average"`
	Total   int            `json:"total"`
}

type SocialCapital struct {
	PlayerID        uuid.UUID `json:"player_id" db:"player_id"`
	Capital         int       `json:"capital" db:"capital"`
	PositiveActions int       `json:"positive_actions" db:"positive_actions"`
	NegativeActions int       `json:"negative_actions" db:"negative_actions"`
	UpdatedAt       time.Time `json:"updated_at" db:"updated_at"`
}

type InteractionHistory struct {
	ID          uuid.UUID `json:"id" db:"id"`
	PlayerID    uuid.UUID `json:"player_id" db:"player_id"`
	TargetID    uuid.UUID `json:"target_id" db:"target_id"`
	InteractionType string `json:"interaction_type" db:"interaction_type"`
	Description string    `json:"description,omitempty" db:"description"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}

type InteractionHistoryResponse struct {
	Interactions []InteractionHistory `json:"interactions"`
	Total        int                  `json:"total"`
	Limit        int                  `json:"limit"`
	Offset       int                  `json:"offset"`
}

type ArbitrationCase struct {
	ID          uuid.UUID `json:"id" db:"id"`
	RequesterID uuid.UUID `json:"requester_id" db:"requester_id"`
	TargetID    uuid.UUID `json:"target_id" db:"target_id"`
	Issue       string    `json:"issue" db:"issue"`
	Status      string    `json:"status" db:"status"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	ResolvedAt  *time.Time `json:"resolved_at,omitempty" db:"resolved_at"`
}

type RequestArbitrationRequest struct {
	TargetID uuid.UUID `json:"target_id"`
	Issue    string    `json:"issue"`
}

