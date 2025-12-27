// Issue: #140889771
// PERFORMANCE: Optimized struct alignment and memory layout for guild operations

package server

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
}
