// Issue: #140895495
// PERFORMANCE: Memory-aligned data structures

package server

import (
	"time"

	"github.com/google/uuid"
)

// VoiceChannel represents a voice chat channel
type VoiceChannel struct {
	ID          uuid.UUID `json:"id" db:"id"`
	GuildID     uuid.UUID `json:"guild_id" db:"guild_id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description" db:"description"`
	MaxUsers    int       `json:"max_users" db:"max_users"`
	IsLocked    bool      `json:"is_locked" db:"is_locked"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

// VoiceChannelUser represents a user in a voice channel
type VoiceChannelUser struct {
	UserID    uuid.UUID `json:"user_id" db:"user_id"`
	ChannelID uuid.UUID `json:"channel_id" db:"channel_id"`
	JoinedAt  time.Time `json:"joined_at" db:"joined_at"`
	Username  string    `json:"username" db:"username"`
}

// VoiceSession represents a voice chat session
type VoiceSession struct {
	ID         uuid.UUID  `json:"id" db:"id"`
	UserID     uuid.UUID  `json:"user_id" db:"user_id"`
	ChannelID  uuid.UUID  `json:"channel_id" db:"channel_id"`
	StartedAt  time.Time  `json:"started_at" db:"started_at"`
	EndedAt    *time.Time `json:"ended_at,omitempty" db:"ended_at"`
	IsMuted    bool       `json:"is_muted" db:"is_muted"`
	IsDeafened bool       `json:"is_deafened" db:"is_deafened"`
}
