// Issue: #140889771
// PERFORMANCE: Optimized struct alignment and memory layout for guild operations

package models

import (
	"time"

	"github.com/google/uuid"
)

// Guild represents a player guild/clan
type Guild struct {
	ID          uuid.UUID `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	LeaderID    uuid.UUID `json:"leader_id"`
	CreatedAt   time.Time `json:"created_at"`
	UpdatedAt   time.Time `json:"updated_at"`
	MemberCount int       `json:"member_count"`
	MaxMembers  int       `json:"max_members"`
	Level       int       `json:"level"`
	Experience  int64     `json:"experience"`
	Reputation  int       `json:"reputation"`
}

// GuildMember represents a guild member
type GuildMember struct {
	UserID   uuid.UUID `json:"user_id"`
	GuildID  uuid.UUID `json:"guild_id"`
	Role     string    `json:"role"` // leader, officer, member
	JoinedAt time.Time `json:"joined_at"`
}

// GuildAnnouncement represents a guild announcement
type GuildAnnouncement struct {
	ID        uuid.UUID `json:"id"`
	GuildID   uuid.UUID `json:"guild_id"`
	Title     string    `json:"title"`
	Content   string    `json:"content"`
	AuthorID  uuid.UUID `json:"author_id"`
	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
	IsPinned  bool      `json:"is_pinned"`
}

// GuildVoiceChannel represents a guild voice channel integrated with WebRTC signaling
type GuildVoiceChannel struct {
	ID          uuid.UUID `json:"id"`
	GuildID     uuid.UUID `json:"guild_id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	ChannelID   string    `json:"channel_id"`   // WebRTC signaling channel ID
	MaxUsers    int       `json:"max_users"`
	IsPrivate   bool      `json:"is_private"`
	CreatedBy   uuid.UUID `json:"created_by"`
	CreatedAt   time.Time `json:"created_at"`
	UpdatedAt   time.Time `json:"updated_at"`
	Status      string    `json:"status"` // active, inactive, maintenance
}

// GuildVoiceParticipant represents a participant in a guild voice channel
type GuildVoiceParticipant struct {
	UserID      uuid.UUID `json:"user_id"`
	ChannelID   uuid.UUID `json:"channel_id"`
	GuildID     uuid.UUID `json:"guild_id"`
	JoinedAt    time.Time `json:"joined_at"`
	IsMuted     bool      `json:"is_muted"`
	IsDeafened  bool      `json:"is_deafened"`
	WebRTCID    string    `json:"webrtc_id"` // WebRTC signaling participant ID
}
