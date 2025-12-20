// Package models SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package models

import (
	"time"

	"github.com/google/uuid"
)

type AdminActionType string

const (
	AdminActionTypeBan  AdminActionType = "ban"
	AdminActionTypeKick AdminActionType = "kick"
	AdminActionTypeMute AdminActionType = "mute"

	AdminActionTypeGiveItem     AdminActionType = "give_item"
	AdminActionTypeRemoveItem   AdminActionType = "remove_item"
	AdminActionTypeSetCurrency  AdminActionType = "set_currency"
	AdminActionTypeAddCurrency  AdminActionType = "add_currency"
	AdminActionTypeSetWorldFlag AdminActionType = "set_world_flag"
	AdminActionTypeCreateEvent  AdminActionType = "create_event"
)

type AdminPermission string

// AdminAuditLog OPTIMIZATION: Field alignment - large to small (uuid.Time=24 bytes, uuid.UUID=16 bytes, *uuid.UUID=8 bytes, string=16 bytes, AdminActionType=16 bytes)
type AdminAuditLog struct {
	CreatedAt  time.Time              `json:"created_at" db:"created_at"`         // 24 bytes - largest
	Details    map[string]interface{} `json:"details" db:"details"`               // 16 bytes (interface{})
	ID         uuid.UUID              `json:"id" db:"id"`                         // 16 bytes
	AdminID    uuid.UUID              `json:"admin_id" db:"admin_id"`             // 16 bytes
	IPAddress  string                 `json:"ip_address" db:"ip_address"`         // 16 bytes
	UserAgent  string                 `json:"user_agent" db:"user_agent"`         // 16 bytes
	ActionType AdminActionType        `json:"action_type" db:"action_type"`       // 16 bytes
	TargetType string                 `json:"target_type" db:"target_type"`       // 16 bytes
	TargetID   *uuid.UUID             `json:"target_id,omitempty" db:"target_id"` // 8 bytes - smallest
}

// BanPlayerRequest OPTIMIZATION: Field alignment - large to small (uuid.UUID=16 bytes, string=16 bytes, *int64=8 bytes, bool=1 byte)
type BanPlayerRequest struct {
	CharacterID uuid.UUID `json:"character_id"`       // 16 bytes
	Reason      string    `json:"reason"`             // 16 bytes
	Duration    *int64    `json:"duration,omitempty"` // 8 bytes
	Permanent   bool      `json:"permanent"`          // 1 byte
}

type KickPlayerRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Reason      string    `json:"reason"`
}

type MutePlayerRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Reason      string    `json:"reason"`
	Duration    int64     `json:"duration"`
}

type GiveItemRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	ItemID      string    `json:"item_id"`
	Quantity    int       `json:"quantity"`
	Reason      string    `json:"reason"`
}

type RemoveItemRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	ItemID      string    `json:"item_id"`
	Quantity    int       `json:"quantity"`
	Reason      string    `json:"reason"`
}

type SetCurrencyRequest struct {
	CharacterID  uuid.UUID `json:"character_id"`
	CurrencyType string    `json:"currency_type"`
	Amount       int64     `json:"amount"`
	Reason       string    `json:"reason"`
}

type AddCurrencyRequest struct {
	CharacterID  uuid.UUID `json:"character_id"`
	CurrencyType string    `json:"currency_type"`
	Amount       int64     `json:"amount"`
	Reason       string    `json:"reason"`
}

type SetWorldFlagRequest struct {
	FlagName  string                 `json:"flag_name"`
	FlagValue map[string]interface{} `json:"flag_value"`
	Region    *string                `json:"region,omitempty"`
}

type CreateEventRequest struct {
	EventName    string                 `json:"event_name"`
	EventType    string                 `json:"event_type"`
	Description  string                 `json:"description"`
	StartTime    time.Time              `json:"start_time"`
	EndTime      *time.Time             `json:"end_time,omitempty"`
	Settings     map[string]interface{} `json:"settings"`
	Announcement bool                   `json:"announcement"`
}

type SearchPlayersRequest struct {
	Query    string `json:"query"`
	SearchBy string `json:"search_by"`
	Limit    int    `json:"limit"`
	Offset   int    `json:"offset"`
}

// AdminActionResponse OPTIMIZATION: Field alignment - large to small (time.Time=24 bytes, uuid.UUID=16 bytes, string=16 bytes, bool=1 byte)
type AdminActionResponse struct {
	Timestamp time.Time `json:"timestamp"` // 24 bytes - largest
	ActionID  uuid.UUID `json:"action_id"` // 16 bytes
	Message   string    `json:"message"`   // 16 bytes
	Success   bool      `json:"success"`   // 1 byte - smallest
}

type AuditLogListResponse struct {
	Logs  []AdminAuditLog `json:"logs"`
	Total int             `json:"total"`
}

type PlayerSearchResponse struct {
	Players []PlayerSearchResult `json:"players"`
	Total   int                  `json:"total"`
}

type PlayerSearchResult struct {
	CharacterID uuid.UUID `json:"character_id"`
	AccountID   uuid.UUID `json:"account_id"`
	Name        string    `json:"name"`
	Level       int       `json:"level"`
	LastSeen    time.Time `json:"last_seen"`
}

type AnalyticsResponse struct {
	OnlinePlayers      int                    `json:"online_players"`
	EconomyMetrics     map[string]interface{} `json:"economy_metrics"`
	CombatMetrics      map[string]interface{} `json:"combat_metrics"`
	PerformanceMetrics map[string]interface{} `json:"performance_metrics"`
	Timestamp          time.Time              `json:"timestamp"`
}
