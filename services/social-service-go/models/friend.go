package models

import (
	"time"

	"github.com/google/uuid"
)

type FriendshipStatus string

const (
	FriendshipStatusPending  FriendshipStatus = "pending"
	FriendshipStatusAccepted FriendshipStatus = "accepted"
	FriendshipStatusBlocked  FriendshipStatus = "blocked"
)

type Friendship struct {
	ID          uuid.UUID       `json:"id" db:"id"`
	CharacterAID uuid.UUID      `json:"character_a_id" db:"character_a_id"`
	CharacterBID uuid.UUID      `json:"character_b_id" db:"character_b_id"`
	Status      FriendshipStatus `json:"status" db:"status"`
	InitiatorID uuid.UUID       `json:"initiator_id" db:"initiator_id"`
	CreatedAt   time.Time       `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time       `json:"updated_at" db:"updated_at"`
}

type SendFriendRequestRequest struct {
	ToCharacterID uuid.UUID `json:"to_character_id"`
}

type FriendListResponse struct {
	Friends []Friendship `json:"friends"`
	Total   int          `json:"total"`
}

