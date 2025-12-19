// SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package models

import (
	"time"

	"github.com/google/uuid"
)

type AdminActionType string

const (
	AdminActionTypeBan              AdminActionType = "ban"
	AdminActionTypeKick             AdminActionType = "kick"
	AdminActionTypeMute             AdminActionType = "mute"
	AdminActionTypeUnban            AdminActionType = "unban"
	AdminActionTypeUnmute           AdminActionType = "unmute"
	AdminActionTypeGiveItem         AdminActionType = "give_item"
	AdminActionTypeRemoveItem       AdminActionType = "remove_item"
	AdminActionTypeSetCurrency      AdminActionType = "set_currency"
	AdminActionTypeAddCurrency      AdminActionType = "add_currency"
	AdminActionTypeSetWorldFlag     AdminActionType = "set_world_flag"
	AdminActionTypeCreateEvent      AdminActionType = "create_event"
	AdminActionTypeSendNotification AdminActionType = "send_notification"
)

type AdminPermission string

const (
	PermissionPlayerManagement AdminPermission = "player_management"
	PermissionInventoryControl AdminPermission = "inventory_control"
	PermissionEconomyControl   AdminPermission = "economy_control"
	PermissionWorldManagement  AdminPermission = "world_management"
	PermissionEventManagement  AdminPermission = "event_management"
	PermissionAnalytics        AdminPermission = "analytics"
	PermissionAudit            AdminPermission = "audit"
)

type AdminAuditLog struct {
	ID         uuid.UUID              `json:"id" db:"id"`
	AdminID    uuid.UUID              `json:"admin_id" db:"admin_id"`
	ActionType AdminActionType        `json:"action_type" db:"action_type"`
	TargetID   *uuid.UUID             `json:"target_id,omitempty" db:"target_id"`
	TargetType string                 `json:"target_type" db:"target_type"`
	Details    map[string]interface{} `json:"details" db:"details"`
	IPAddress  string                 `json:"ip_address" db:"ip_address"`
	UserAgent  string                 `json:"user_agent" db:"user_agent"`
	CreatedAt  time.Time              `json:"created_at" db:"created_at"`
}

type BanPlayerRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Reason      string    `json:"reason"`
	Duration    *int64    `json:"duration,omitempty"`
	Permanent   bool      `json:"permanent"`
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

type AdminActionResponse struct {
	Success   bool      `json:"success"`
	Message   string    `json:"message"`
	ActionID  uuid.UUID `json:"action_id"`
	Timestamp time.Time `json:"timestamp"`
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
