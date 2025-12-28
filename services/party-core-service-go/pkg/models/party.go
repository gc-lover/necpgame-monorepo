package models

import (
	"time"

	"github.com/google/uuid"
)

// Party представляет группу игроков
type Party struct {
	ID        uuid.UUID   `json:"id" db:"id"`
	LeaderID  uuid.UUID   `json:"leader_id" db:"leader_id"`
	Name      string      `json:"name" db:"name"`
	MaxSize   int         `json:"max_size" db:"max_size"`
	LootMode  LootMode    `json:"loot_mode" db:"loot_mode"`
	CreatedAt time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt time.Time   `json:"updated_at" db:"updated_at"`
	Members   []PartyMember `json:"members" db:"members"`
}

// PartyMember представляет члена группы
type PartyMember struct {
	ID          uuid.UUID     `json:"id" db:"id"`
	PartyID     uuid.UUID     `json:"party_id" db:"party_id"`
	CharacterID uuid.UUID     `json:"character_id" db:"character_id"`
	AccountID   uuid.UUID     `json:"account_id" db:"account_id"`
	Role        PartyRole     `json:"role" db:"role"`
	JoinedAt    time.Time     `json:"joined_at" db:"joined_at"`
}

// LootMode определяет режим распределения добычи
type LootMode string

const (
	LootModeFreeForAll      LootMode = "free_for_all"
	LootModeMasterLooter    LootMode = "master_looter"
	LootModeNeedBeforeGreed LootMode = "need_before_greed"
	LootModeRoundRobin      LootMode = "round_robin"
)

// PartyRole определяет роль члена группы
type PartyRole string

const (
	PartyRoleLeader PartyRole = "leader"
	PartyRoleMember PartyRole = "member"
)

// CreatePartyRequest представляет запрос на создание группы
type CreatePartyRequest struct {
	Name     string   `json:"name,omitempty"`
	MaxSize  int      `json:"max_size,omitempty"`
	LootMode LootMode `json:"loot_mode,omitempty"`
}

// TransferLeadershipRequest представляет запрос на передачу лидерства
type TransferLeadershipRequest struct {
	NewLeaderID uuid.UUID `json:"new_leader_id"`
}
