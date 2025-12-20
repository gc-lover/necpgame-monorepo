// Issue: #1943
package server

import (
	"time"

	"github.com/google/uuid"
)

// Request/Response types for API
type CreateGuildRequest struct {
	Name        string    `json:"name"`
	Description string    `json:"description"`
	LeaderID    uuid.UUID `json:"leader_id"`
	Region      string    `json:"region"`
}

type GetGuildsParams struct {
	Page      int    `json:"page"`
	Limit     int    `json:"limit"`
	Search    string `json:"search,omitempty"`
	MinLevel  int    `json:"min_level,omitempty"`
	MaxLevel  int    `json:"max_level,omitempty"`
	Region    string `json:"region,omitempty"`
}

type GetMembersParams struct {
	Page   int    `json:"page"`
	Limit  int    `json:"limit"`
	Role   string `json:"role,omitempty"`
	Status string `json:"status,omitempty"`
}

// Response types
type GuildResponse struct {
	ID          string    `json:"id"`
	Name        string    `json:"name"`
	Description string    `json:"description"`
	LeaderID    string    `json:"leader_id"`
	Level       int       `json:"level"`
	MemberCount int       `json:"member_count"`
	Reputation  int       `json:"reputation"`
	Region      string    `json:"region"`
	CreatedAt   time.Time `json:"created_at"`
}

type GuildListResponse struct {
	Guilds []*GuildResponse `json:"guilds"`
	Total  int              `json:"total"`
	Page   int              `json:"page"`
	Limit  int              `json:"limit"`
}

type GuildMemberResponse struct {
	PlayerID         string    `json:"player_id"`
	Username         string    `json:"username"`
	Role             string    `json:"role"`
	JoinedAt         time.Time `json:"joined_at"`
	LastActive       time.Time `json:"last_active"`
	ContributionScore int      `json:"contribution_score"`
	Permissions      []string  `json:"permissions"`
}

type InvitationResponse struct {
	ID        string    `json:"id"`
	GuildID   string    `json:"guild_id"`
	PlayerID  string    `json:"player_id"`
	InvitedBy string    `json:"invited_by"`
	Status    string    `json:"status"`
	CreatedAt time.Time `json:"created_at"`
	ExpiresAt time.Time `json:"expires_at"`
}

// Internal types
type GuildMember struct {
	PlayerID         string
	Username         string
	Role             string
	JoinedAt         time.Time
	LastActive       time.Time
	ContributionScore int
	Permissions      []string
}

type Invitation struct {
	ID        uuid.UUID
	GuildID   string
	PlayerID  uuid.UUID
	InvitedBy uuid.UUID
	Message   string
	Status    string
	CreatedAt time.Time
	ExpiresAt time.Time
}
