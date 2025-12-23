// Issue: #2218 - Backend: Добавить unit-тесты для ws-lobby-go
package models

import (
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"
)

// Connection represents a WebSocket connection
type Connection struct {
	ID       string          `json:"connection_id"`
	UserID   uuid.UUID       `json:"user_id"`
	Conn     *websocket.Conn `json:"-"`
	JoinedAt time.Time       `json:"joined_at"`
	Region   string          `json:"region,omitempty"`
}

// Room represents a lobby room
type Room struct {
	ID          string      `json:"room_id"`
	Name        string      `json:"name"`
	Description string      `json:"description,omitempty"`
	MaxPlayers  int         `json:"max_players"`
	IsPrivate   bool        `json:"is_private"`
	GameMode    string      `json:"game_mode,omitempty"`
	CurrentPlayers int      `json:"current_players"`
	PlayerIDs   []uuid.UUID `json:"player_ids"`
	CreatedBy   uuid.UUID   `json:"created_by"`
	CreatedAt   time.Time   `json:"created_at"`
}

// LobbyMessage represents a WebSocket message
type LobbyMessage struct {
	MessageID uuid.UUID `json:"message_id,omitempty"`
	RoomID    *string   `json:"room_id,omitempty"`
	Type      string    `json:"type"`
	Priority  string    `json:"priority,omitempty"`
	Payload   interface{} `json:"payload"`
	Timestamp time.Time `json:"timestamp"`
	SenderID  uuid.UUID `json:"sender_id,omitempty"`
}

// ChatMessage represents a chat message payload
type ChatMessage struct {
	Content          string      `json:"content"`
	MentionUserIDs   []uuid.UUID `json:"mention_user_ids,omitempty"`
	ReplyToMessageID *uuid.UUID  `json:"reply_to_message_id,omitempty"`
}

// PlayerPresence represents player presence information
type PlayerPresence struct {
	UserID        uuid.UUID `json:"user_id"`
	Status        string    `json:"status"`
	CurrentRoomID *string   `json:"current_room_id,omitempty"`
	GameMode      string    `json:"game_mode,omitempty"`
	Region        string    `json:"region,omitempty"`
	LastSeen      time.Time `json:"last_seen"`
}

// RoomInvitation represents a room invitation
type RoomInvitation struct {
	InvitationID uuid.UUID `json:"invitation_id"`
	RoomID       string    `json:"room_id"`
	InviterID    uuid.UUID `json:"inviter_id"`
	InviteeID    uuid.UUID `json:"invitee_id"`
	Message      string    `json:"message,omitempty"`
	ExpiresAt    time.Time `json:"expires_at"`
	Status       string    `json:"status"`
}

// Error represents an error response
type Error struct {
	Error     string                 `json:"error"`
	Timestamp time.Time              `json:"timestamp"`
	Details   map[string]interface{} `json:"details,omitempty"`
	Code      int                    `json:"code"`
}
