package models

import (
	"time"

	"github.com/google/uuid"
)

type GuildRank string

const (
	GuildRankLeader   GuildRank = "leader"
	GuildRankOfficer  GuildRank = "officer"
	GuildRankMember   GuildRank = "member"
	GuildRankRecruit  GuildRank = "recruit"
)

type GuildStatus string

const (
	GuildStatusActive   GuildStatus = "active"
	GuildStatusInactive GuildStatus = "inactive"
	GuildStatusDisbanded GuildStatus = "disbanded"
)

type GuildMemberStatus string

const (
	GuildMemberStatusActive   GuildMemberStatus = "active"
	GuildMemberStatusInvited  GuildMemberStatus = "invited"
	GuildMemberStatusLeft     GuildMemberStatus = "left"
	GuildMemberStatusKicked   GuildMemberStatus = "kicked"
)

type Guild struct {
	ID          uuid.UUID   `json:"id" db:"id"`
	Name        string      `json:"name" db:"name"`
	Tag         string      `json:"tag" db:"tag"`
	LeaderID    uuid.UUID   `json:"leader_id" db:"leader_id"`
	Level       int         `json:"level" db:"level"`
	Experience  int         `json:"experience" db:"experience"`
	MaxMembers  int         `json:"max_members" db:"max_members"`
	Description string      `json:"description" db:"description"`
	Status      GuildStatus `json:"status" db:"status"`
	CreatedAt   time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time   `json:"updated_at" db:"updated_at"`
}

type GuildMember struct {
	ID           uuid.UUID         `json:"id" db:"id"`
	GuildID      uuid.UUID         `json:"guild_id" db:"guild_id"`
	CharacterID  uuid.UUID         `json:"character_id" db:"character_id"`
	Rank         GuildRank         `json:"rank" db:"rank"`
	Status       GuildMemberStatus  `json:"status" db:"status"`
	Contribution int                `json:"contribution" db:"contribution"`
	JoinedAt     time.Time         `json:"joined_at" db:"joined_at"`
	UpdatedAt    time.Time         `json:"updated_at" db:"updated_at"`
}

type GuildInvitation struct {
	ID          uuid.UUID `json:"id" db:"id"`
	GuildID     uuid.UUID `json:"guild_id" db:"guild_id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`
	InvitedBy   uuid.UUID `json:"invited_by" db:"invited_by"`
	Message     string    `json:"message" db:"message"`
	Status      string    `json:"status" db:"status"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	ExpiresAt   time.Time `json:"expires_at" db:"expires_at"`
}

type GuildBank struct {
	ID         uuid.UUID              `json:"id" db:"id"`
	GuildID    uuid.UUID              `json:"guild_id" db:"guild_id"`
	Currency   map[string]int         `json:"currency" db:"currency"`
	Items      []map[string]interface{} `json:"items" db:"items"`
	UpdatedAt  time.Time              `json:"updated_at" db:"updated_at"`
}

type CreateGuildRequest struct {
	Name        string `json:"name"`
	Tag         string `json:"tag"`
	Description string `json:"description"`
}

type UpdateGuildRequest struct {
	Name        *string `json:"name,omitempty"`
	Description *string `json:"description,omitempty"`
}

type InviteMemberRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Message     string    `json:"message,omitempty"`
}

type UpdateMemberRankRequest struct {
	Rank GuildRank `json:"rank"`
}

type GuildListResponse struct {
	Guilds []Guild `json:"guilds"`
	Total  int     `json:"total"`
}

type GuildDetailResponse struct {
	Guild   Guild         `json:"guild"`
	Members []GuildMember `json:"members"`
	Bank    *GuildBank    `json:"bank,omitempty"`
}

type GuildMemberListResponse struct {
	Members []GuildMember `json:"members"`
	Total   int          `json:"total"`
}

type GuildRankEntity struct {
	ID          uuid.UUID `json:"id" db:"id"`
	GuildID     uuid.UUID `json:"guild_id" db:"guild_id"`
	Name        string    `json:"name" db:"name"`
	Permissions []string  `json:"permissions" db:"permissions"`
	Order       int       `json:"order" db:"order"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}

type CreateGuildRankRequest struct {
	Name        string   `json:"name"`
	Permissions []string `json:"permissions"`
}

type UpdateGuildRankRequest struct {
	Name        *string  `json:"name,omitempty"`
	Permissions []string `json:"permissions,omitempty"`
	Order       *int     `json:"order,omitempty"`
}

type GuildRanksResponse struct {
	Ranks []GuildRankEntity `json:"ranks"`
	Total int                `json:"total"`
}

type GuildBankDepositRequest struct {
	Currency int                    `json:"currency"`
	Items    []map[string]interface{} `json:"items,omitempty"`
}

type GuildBankWithdrawRequest struct {
	Currency int                    `json:"currency"`
	Items    []map[string]interface{} `json:"items,omitempty"`
}

type GuildBankTransaction struct {
	ID        uuid.UUID              `json:"id" db:"id"`
	GuildID   uuid.UUID              `json:"guild_id" db:"guild_id"`
	AccountID uuid.UUID              `json:"account_id" db:"account_id"`
	Type      string                 `json:"type" db:"type"`
	Currency  int                    `json:"currency" db:"currency"`
	Items     []map[string]interface{} `json:"items" db:"items"`
	CreatedAt time.Time              `json:"created_at" db:"created_at"`
}

type GuildBankTransactionsResponse struct {
	Transactions []GuildBankTransaction `json:"transactions"`
	Total        int                     `json:"total"`
}

