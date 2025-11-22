package models

import (
	"time"

	"github.com/google/uuid"
)

type NotificationType string

const (
	NotificationTypeQuest      NotificationType = "quest"
	NotificationTypeMessage    NotificationType = "message"
	NotificationTypeAchievement NotificationType = "achievement"
	NotificationTypeSystem     NotificationType = "system"
	NotificationTypeFriend     NotificationType = "friend"
	NotificationTypeGuild      NotificationType = "guild"
	NotificationTypeTrade      NotificationType = "trade"
	NotificationTypeCombat     NotificationType = "combat"
)

type NotificationPriority string

const (
	NotificationPriorityLow      NotificationPriority = "low"
	NotificationPriorityMedium   NotificationPriority = "medium"
	NotificationPriorityHigh     NotificationPriority = "high"
	NotificationPriorityCritical NotificationPriority = "critical"
)

type NotificationStatus string

const (
	NotificationStatusUnread NotificationStatus = "unread"
	NotificationStatusRead   NotificationStatus = "read"
	NotificationStatusArchived NotificationStatus = "archived"
)

type DeliveryChannel string

const (
	DeliveryChannelInGame  DeliveryChannel = "in_game"
	DeliveryChannelWebSocket DeliveryChannel = "websocket"
	DeliveryChannelEmail   DeliveryChannel = "email"
)

type Notification struct {
	ID          uuid.UUID            `json:"id" db:"id"`
	AccountID   uuid.UUID            `json:"account_id" db:"account_id"`
	Type        NotificationType     `json:"type" db:"type"`
	Priority    NotificationPriority `json:"priority" db:"priority"`
	Title       string               `json:"title" db:"title"`
	Content     string               `json:"content" db:"content"`
	Data        map[string]interface{} `json:"data,omitempty" db:"data"`
	Status      NotificationStatus   `json:"status" db:"status"`
	Channels    []DeliveryChannel   `json:"channels" db:"channels"`
	CreatedAt   time.Time           `json:"created_at" db:"created_at"`
	ReadAt      *time.Time          `json:"read_at,omitempty" db:"read_at"`
	ExpiresAt   *time.Time          `json:"expires_at,omitempty" db:"expires_at"`
}

type CreateNotificationRequest struct {
	AccountID uuid.UUID            `json:"account_id"`
	Type      NotificationType     `json:"type"`
	Priority  NotificationPriority `json:"priority"`
	Title     string               `json:"title"`
	Content   string               `json:"content"`
	Data      map[string]interface{} `json:"data,omitempty"`
	Channels  []DeliveryChannel   `json:"channels,omitempty"`
	ExpiresAt *time.Time          `json:"expires_at,omitempty"`
}

type NotificationListResponse struct {
	Notifications []Notification `json:"notifications"`
	Total         int            `json:"total"`
	Unread        int            `json:"unread"`
}

type UpdateNotificationStatusRequest struct {
	Status NotificationStatus `json:"status"`
}

type NotificationPreferences struct {
	AccountID           uuid.UUID            `json:"account_id" db:"account_id"`
	QuestEnabled        bool                 `json:"quest_enabled" db:"quest_enabled"`
	MessageEnabled      bool                 `json:"message_enabled" db:"message_enabled"`
	AchievementEnabled  bool                 `json:"achievement_enabled" db:"achievement_enabled"`
	SystemEnabled       bool                 `json:"system_enabled" db:"system_enabled"`
	FriendEnabled       bool                 `json:"friend_enabled" db:"friend_enabled"`
	GuildEnabled        bool                 `json:"guild_enabled" db:"guild_enabled"`
	TradeEnabled        bool                 `json:"trade_enabled" db:"trade_enabled"`
	CombatEnabled       bool                 `json:"combat_enabled" db:"combat_enabled"`
	PreferredChannels   []DeliveryChannel   `json:"preferred_channels" db:"preferred_channels"`
	UpdatedAt           time.Time           `json:"updated_at" db:"updated_at"`
}

