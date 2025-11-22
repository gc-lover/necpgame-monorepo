package models

import (
	"time"

	"github.com/google/uuid"
)

type ChannelType string

const (
	ChannelTypeGlobal      ChannelType = "global"
	ChannelTypeLocal       ChannelType = "local"
	ChannelTypeTrade       ChannelType = "trade"
	ChannelTypeNewbie      ChannelType = "newbie"
	ChannelTypeParty       ChannelType = "party"
	ChannelTypeRaid        ChannelType = "raid"
	ChannelTypeGuild       ChannelType = "guild"
	ChannelTypeGuildOfficer ChannelType = "guild_officer"
	ChannelTypeWhisper     ChannelType = "whisper"
	ChannelTypeSystem      ChannelType = "system"
	ChannelTypeCombat      ChannelType = "combat"
)

type ChatMessage struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	ChannelID   uuid.UUID  `json:"channel_id" db:"channel_id"`
	ChannelType ChannelType `json:"channel_type" db:"channel_type"`
	SenderID    uuid.UUID  `json:"sender_id" db:"sender_id"`
	SenderName  string     `json:"sender_name" db:"sender_name"`
	Content     string     `json:"content" db:"content"`
	Formatted   string     `json:"formatted,omitempty" db:"formatted"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
}

type ChatChannel struct {
	ID              uuid.UUID   `json:"id" db:"id"`
	Type            ChannelType `json:"type" db:"type"`
	OwnerID         *uuid.UUID  `json:"owner_id,omitempty" db:"owner_id"`
	Name            string      `json:"name" db:"name"`
	Description     string      `json:"description,omitempty" db:"description"`
	CooldownSeconds int         `json:"cooldown_seconds" db:"cooldown_seconds"`
	MaxLength       int         `json:"max_length" db:"max_length"`
	IsActive        bool        `json:"is_active" db:"is_active"`
	CreatedAt       time.Time   `json:"created_at" db:"created_at"`
}

type CreateMessageRequest struct {
	ChannelID   uuid.UUID  `json:"channel_id"`
	ChannelType ChannelType `json:"channel_type"`
	Content     string     `json:"content"`
}

type MessageListResponse struct {
	Messages []ChatMessage `json:"messages"`
	Total    int           `json:"total"`
	HasMore  bool          `json:"has_more"`
}

type ChannelListResponse struct {
	Channels []ChatChannel `json:"channels"`
	Total    int           `json:"total"`
}

type ChatBan struct {
	ID          uuid.UUID   `json:"id" db:"id"`
	CharacterID uuid.UUID   `json:"character_id" db:"character_id"`
	ChannelID   *uuid.UUID  `json:"channel_id,omitempty" db:"channel_id"`
	ChannelType *ChannelType `json:"channel_type,omitempty" db:"channel_type"`
	Reason      string      `json:"reason" db:"reason"`
	AdminID     *uuid.UUID  `json:"admin_id,omitempty" db:"admin_id"`
	ExpiresAt   *time.Time  `json:"expires_at,omitempty" db:"expires_at"`
	CreatedAt   time.Time   `json:"created_at" db:"created_at"`
	IsActive    bool        `json:"is_active" db:"is_active"`
}

type ChatReport struct {
	ID          uuid.UUID   `json:"id" db:"id"`
	ReporterID  uuid.UUID   `json:"reporter_id" db:"reporter_id"`
	ReportedID  uuid.UUID   `json:"reported_id" db:"reported_id"`
	MessageID   *uuid.UUID  `json:"message_id,omitempty" db:"message_id"`
	ChannelID   *uuid.UUID  `json:"channel_id,omitempty" db:"channel_id"`
	Reason      string      `json:"reason" db:"reason"`
	Status      string      `json:"status" db:"status"`
	AdminID     *uuid.UUID  `json:"admin_id,omitempty" db:"admin_id"`
	CreatedAt   time.Time   `json:"created_at" db:"created_at"`
	ResolvedAt  *time.Time  `json:"resolved_at,omitempty" db:"resolved_at"`
}

type CreateBanRequest struct {
	CharacterID uuid.UUID   `json:"character_id"`
	ChannelID   *uuid.UUID  `json:"channel_id,omitempty"`
	ChannelType *ChannelType `json:"channel_type,omitempty"`
	Reason      string      `json:"reason"`
	Duration    *int        `json:"duration,omitempty"`
}

type CreateReportRequest struct {
	ReportedID  uuid.UUID   `json:"reported_id"`
	MessageID   *uuid.UUID  `json:"message_id,omitempty"`
	ChannelID   *uuid.UUID  `json:"channel_id,omitempty"`
	Reason      string      `json:"reason"`
}

type BanListResponse struct {
	Bans  []ChatBan `json:"bans"`
	Total int       `json:"total"`
}

