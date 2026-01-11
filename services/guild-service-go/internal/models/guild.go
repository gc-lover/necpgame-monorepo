//go:align 64
// Issue: #2295

package models

import (
	"time"

	"github.com/google/uuid"
)

//go:align 64
type Guild struct {
	ID             uuid.UUID `json:"id" db:"id"`
	Version        int       `json:"version" db:"version"`
	GuildName      string    `json:"guild_name" db:"guild_name"`
	GuildTag       string    `json:"guild_tag" db:"guild_tag"`
	Description    string    `json:"description,omitempty" db:"description"`
	LeaderID       uuid.UUID `json:"leader_id" db:"leader_id"`
	Faction        string    `json:"faction,omitempty" db:"faction"`
	Level          int       `json:"level" db:"level"`
	Experience     int64     `json:"experience" db:"experience"`
	MaxMembers     int       `json:"max_members" db:"max_members"`
	CurrentMembers int       `json:"current_members" db:"current_members"`
	Reputation     int       `json:"reputation" db:"reputation"`
	IsRecruiting   bool      `json:"is_recruiting" db:"is_recruiting"`
	CreatedAt      time.Time `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time `json:"updated_at" db:"updated_at"`
}

//go:align 64
type GuildMember struct {
	ID                 uuid.UUID              `json:"id" db:"id"`
	GuildID            uuid.UUID              `json:"guild_id" db:"guild_id"`
	PlayerID           uuid.UUID              `json:"player_id" db:"player_id"`
	Role               string                 `json:"role" db:"role"`
	JoinedAt           time.Time              `json:"joined_at" db:"joined_at"`
	LastActive         time.Time              `json:"last_active" db:"last_active"`
	ContributionPoints int                    `json:"contribution_points" db:"contribution_points"`
	Permissions        GuildMemberPermissions `json:"permissions" db:"permissions"`
	Version            int                    `json:"version" db:"version"`
}

//go:align 64
type GuildMemberPermissions struct {
	CanInvite     bool `json:"can_invite" db:"can_invite"`
	CanKick       bool `json:"can_kick" db:"can_kick"`
	CanManageBank bool `json:"can_manage_bank" db:"can_manage_bank"`
	CanSchedule   bool `json:"can_schedule" db:"can_schedule"`
	CanPromote    bool `json:"can_promote" db:"can_promote"`
}

//go:align 64
type GuildRank struct {
	ID           uuid.UUID `json:"id" db:"id"`
	GuildID      uuid.UUID `json:"guild_id" db:"guild_id"`
	RankType     string    `json:"rank_type" db:"rank_type"`
	RankPosition int       `json:"rank_position" db:"rank_position"`
	Score        int64     `json:"score" db:"score"`
	LastUpdated  time.Time `json:"last_updated" db:"last_updated"`
}

//go:align 64
type GuildBank struct {
	ID             uuid.UUID `json:"id" db:"id"`
	GuildID        uuid.UUID `json:"guild_id" db:"guild_id"`
	Version        int       `json:"version" db:"version"`
	CurrencyType   string    `json:"currency_type" db:"currency_type"`
	Amount         int64     `json:"amount" db:"amount"`
	LastTransaction time.Time `json:"last_transaction" db:"last_transaction"`
}

//go:align 64
type GuildEvent struct {
	ID               uuid.UUID `json:"id" db:"id"`
	GuildID          uuid.UUID `json:"guild_id" db:"guild_id"`
	EventType        string    `json:"event_type" db:"event_type"`
	Title            string    `json:"title" db:"title"`
	Description      string    `json:"description,omitempty" db:"description"`
	ScheduledAt      time.Time `json:"scheduled_at" db:"scheduled_at"`
	DurationMinutes  int       `json:"duration_minutes" db:"duration_minutes"`
	MaxParticipants  *int      `json:"max_participants,omitempty" db:"max_participants"`
	CurrentParticipants int    `json:"current_participants" db:"current_participants"`
	Status           string    `json:"status" db:"status"`
	CreatedBy        uuid.UUID `json:"created_by" db:"created_by"`
	CreatedAt        time.Time `json:"created_at" db:"created_at"`
}

//go:align 64
type GuildAchievement struct {
	ID               uuid.UUID `json:"id" db:"id"`
	GuildID          uuid.UUID `json:"guild_id" db:"guild_id"`
	AchievementType  string    `json:"achievement_type" db:"achievement_type"`
	AchievementData  map[string]interface{} `json:"achievement_data" db:"achievement_data"`
	UnlockedAt       time.Time `json:"unlocked_at" db:"unlocked_at"`
}

//go:align 64
type GuildRelationship struct {
	ID               uuid.UUID `json:"id" db:"id"`
	GuildAID         uuid.UUID `json:"guild_a_id" db:"guild_a_id"`
	GuildBID         uuid.UUID `json:"guild_b_id" db:"guild_b_id"`
	RelationshipType string    `json:"relationship_type" db:"relationship_type"`
	EstablishedAt    time.Time `json:"established_at" db:"established_at"`
	ExpiresAt        *time.Time `json:"expires_at,omitempty" db:"expires_at"`
}

//go:align 64
type GuildStats struct {
	GuildID          uuid.UUID `json:"guild_id"`
	TotalMembers     int       `json:"total_members"`
	ActiveMembers    int       `json:"active_members"`
	TotalEvents      int       `json:"total_events"`
	UpcomingEvents   int       `json:"upcoming_events"`
	BankBalance      map[string]int64 `json:"bank_balance"`
	AchievementCount int       `json:"achievement_count"`
	ReputationScore  int       `json:"reputation_score"`
	Rankings         []GuildRank `json:"rankings"`
}

//go:align 64
type GuildInvitation struct {
	ID        uuid.UUID `json:"id"`
	GuildID   uuid.UUID `json:"guild_id"`
	PlayerID  uuid.UUID `json:"player_id"`
	InvitedBy uuid.UUID `json:"invited_by"`
	Message   string    `json:"message,omitempty"`
	Status    string    `json:"status"` // pending, accepted, rejected, expired
	ExpiresAt time.Time `json:"expires_at"`
	CreatedAt time.Time `json:"created_at"`
}

//go:align 64
type GuildApplication struct {
	ID         uuid.UUID `json:"id"`
	GuildID    uuid.UUID `json:"guild_id"`
	PlayerID   uuid.UUID `json:"player_id"`
	Message    string    `json:"message,omitempty"`
	Status     string    `json:"status"` // pending, accepted, rejected
	ReviewedBy *uuid.UUID `json:"reviewed_by,omitempty"`
	ReviewedAt *time.Time `json:"reviewed_at,omitempty"`
	CreatedAt  time.Time  `json:"created_at"`
}