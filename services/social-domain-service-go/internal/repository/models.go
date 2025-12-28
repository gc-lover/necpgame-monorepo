package repository

import (
	"time"

	"github.com/google/uuid"
)

// Chat models

// ChatChannel represents a chat channel
type ChatChannel struct {
	ID          uuid.UUID  `db:"id" json:"id"`
	Name        string     `db:"name" json:"name"`
	ChannelType string     `db:"channel_type" json:"channel_type"` // global, guild, party, private
	OwnerID     uuid.UUID  `db:"owner_id" json:"owner_id"`
	IsPrivate   bool       `db:"is_private" json:"is_private"`
	MaxMembers  *int       `db:"max_members" json:"max_members,omitempty"`
	CreatedAt   time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt   time.Time  `db:"updated_at" json:"updated_at"`
}

// ChatMessage represents a chat message
type ChatMessage struct {
	ID          uuid.UUID  `db:"id" json:"id"`
	ChannelID   uuid.UUID  `db:"channel_id" json:"channel_id"`
	SenderID    uuid.UUID  `db:"sender_id" json:"sender_id"`
	MessageType string     `db:"message_type" json:"message_type"` // text, system, emote
	Content     string     `db:"content" json:"content"`
	CreatedAt   time.Time  `db:"created_at" json:"created_at"`
}

// Guild models

// Guild represents a player guild
type Guild struct {
	ID          uuid.UUID  `db:"id" json:"id"`
	Name        string     `db:"name" json:"name"`
	Description string     `db:"description" json:"description"`
	LeaderID    uuid.UUID  `db:"leader_id" json:"leader_id"`
	MaxMembers  int        `db:"max_members" json:"max_members"`
	Level       int        `db:"level" json:"level"`
	Experience  int        `db:"experience" json:"experience"`
	CreatedAt   time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt   time.Time  `db:"updated_at" json:"updated_at"`
}

// GuildMember represents a guild member
type GuildMember struct {
	GuildID  uuid.UUID `db:"guild_id" json:"guild_id"`
	PlayerID uuid.UUID `db:"player_id" json:"player_id"`
	Role     string    `db:"role" json:"role"` // leader, officer, member
	JoinedAt time.Time `db:"joined_at" json:"joined_at"`
}

// Party models

// Party represents a temporary player group
type Party struct {
	ID         uuid.UUID  `db:"id" json:"id"`
	Name       string     `db:"name" json:"name"`
	LeaderID   uuid.UUID  `db:"leader_id" json:"leader_id"`
	MaxMembers int        `db:"max_members" json:"max_members"`
	IsPrivate  bool       `db:"is_private" json:"is_private"`
	CreatedAt  time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt  time.Time  `db:"updated_at" json:"updated_at"`
}

// PartyMember represents a party member
type PartyMember struct {
	PartyID   uuid.UUID `db:"party_id" json:"party_id"`
	PlayerID  uuid.UUID `db:"player_id" json:"player_id"`
	JoinedAt  time.Time `db:"joined_at" json:"joined_at"`
}

// Orders models

// PlayerOrder represents a player commission/order
type PlayerOrder struct {
	ID           uuid.UUID  `db:"id" json:"id"`
	RequesterID  uuid.UUID  `db:"requester_id" json:"requester_id"`
	Title        string     `db:"title" json:"title"`
	Description  string     `db:"description" json:"description"`
	RewardType   string     `db:"reward_type" json:"reward_type"`   // currency, item, experience
	RewardAmount int        `db:"reward_amount" json:"reward_amount"`
	Status       string     `db:"status" json:"status"`             // open, in_progress, completed, cancelled
	AssigneeID   *uuid.UUID `db:"assignee_id" json:"assignee_id,omitempty"`
	CreatedAt    time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt    time.Time  `db:"updated_at" json:"updated_at"`
}

// Mentorship models

// MentorshipProposal represents a mentorship proposal
type MentorshipProposal struct {
	ID           uuid.UUID  `db:"id" json:"id"`
	MentorID     uuid.UUID  `db:"mentor_id" json:"mentor_id"`
	StudentID    uuid.UUID  `db:"student_id" json:"student_id"`
	ProposalType string     `db:"proposal_type" json:"proposal_type"` // teaching, learning
	Message      string     `db:"message" json:"message"`
	Status       string     `db:"status" json:"status"` // pending, accepted, rejected
	CreatedAt    time.Time  `db:"created_at" json:"created_at"`
	UpdatedAt    time.Time  `db:"updated_at" json:"updated_at"`
}

// MentorshipRelationship represents an active mentorship
type MentorshipRelationship struct {
	ID        uuid.UUID `db:"id" json:"id"`
	MentorID  uuid.UUID `db:"mentor_id" json:"mentor_id"`
	StudentID uuid.UUID `db:"student_id" json:"student_id"`
	Status    string    `db:"status" json:"status"` // active, completed, paused
	StartedAt time.Time `db:"started_at" json:"started_at"`
	EndedAt   *time.Time `db:"ended_at" json:"ended_at,omitempty"`
}

// Reputation models

// PlayerReputation represents a player's reputation
type PlayerReputation struct {
	PlayerID uuid.UUID `db:"player_id" json:"player_id"`
	Score    int       `db:"score" json:"score"`
	Level    int       `db:"level" json:"level"`
	Title    string    `db:"title" json:"title"`
}

// ReputationBenefit represents a reputation benefit
type ReputationBenefit struct {
	ID          uuid.UUID `db:"id" json:"id"`
	MinLevel    int       `db:"min_level" json:"min_level"`
	BenefitType string    `db:"benefit_type" json:"benefit_type"` // discount, bonus_xp, priority_queue
	Value       int       `db:"value" json:"value"`
	Description string    `db:"description" json:"description"`
}

// Notification models

// Notification represents a player notification
type Notification struct {
	ID        uuid.UUID  `db:"id" json:"id"`
	PlayerID  uuid.UUID  `db:"player_id" json:"player_id"`
	Type      string     `db:"type" json:"type"`      // system, guild, party, order, mentorship
	Title     string     `db:"title" json:"title"`
	Message   string     `db:"message" json:"message"`
	IsRead    bool       `db:"is_read" json:"is_read"`
	CreatedAt time.Time  `db:"created_at" json:"created_at"`
	Data      string     `db:"data" json:"data"` // JSON data for notification context
}

// NotificationPreferences represents notification preferences
type NotificationPreferences struct {
	PlayerID          uuid.UUID `db:"player_id" json:"player_id"`
	SystemEnabled     bool      `db:"system_enabled" json:"system_enabled"`
	GuildEnabled      bool      `db:"guild_enabled" json:"guild_enabled"`
	PartyEnabled      bool      `db:"party_enabled" json:"party_enabled"`
	OrderEnabled      bool      `db:"order_enabled" json:"order_enabled"`
	MentorshipEnabled bool      `db:"mentorship_enabled" json:"mentorship_enabled"`
}
