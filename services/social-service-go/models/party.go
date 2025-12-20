package models

import (
	"time"

	"github.com/google/uuid"
)

type LootMode string

const (
	LootModeFreeForAll      LootMode = "free_for_all"
	LootModeRoundRobin      LootMode = "round_robin"
	LootModeNeedBeforeGreed LootMode = "need_before_greed"
	LootModeMasterLooter    LootMode = "master_looter"
)

type PartyRole string

const (
	PartyRoleLeader PartyRole = "leader"
	PartyRoleMember PartyRole = "member"
)

type Party struct {
	ID        uuid.UUID     `json:"id" db:"id"`
	LeaderID  uuid.UUID     `json:"leader_id" db:"leader_id"`
	Members   []PartyMember `json:"members" db:"-"`
	MaxSize   int           `json:"max_size" db:"max_size"`
	LootMode  LootMode      `json:"loot_mode" db:"loot_mode"`
	CreatedAt time.Time     `json:"created_at" db:"created_at"`
	UpdatedAt time.Time     `json:"updated_at" db:"updated_at"`
}

type PartyMember struct {
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	Role        PartyRole `json:"role" db:"role"`
	JoinedAt    time.Time `json:"joined_at" db:"joined_at"`
}

type CreatePartyRequest struct {
	MaxSize  *int      `json:"max_size,omitempty"`
	LootMode *LootMode `json:"loot_mode,omitempty"`
}

type TransferLeadershipRequest struct {
	NewLeaderID uuid.UUID `json:"new_leader_id"`
}
