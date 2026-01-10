package repository

import (
	"time"

	"github.com/google/uuid"
)

// WorldEvent represents a world event in the database - matches API schema
type WorldEvent struct {
	ID                   uuid.UUID   `json:"id" db:"id"`
	EventID              string      `json:"event_id" db:"event_id"`
	Name                 string      `json:"name" db:"name"`
	Description          *string     `json:"description" db:"description"`
	Type                 string      `json:"type" db:"type"`
	Region               string      `json:"region" db:"region"`
	Status               string      `json:"status" db:"status"`
	StartTime            time.Time   `json:"start_time" db:"start_time"`
	EndTime              *time.Time  `json:"end_time" db:"end_time"`
	Objectives           interface{} `json:"objectives" db:"objectives"` // JSONB
	Rewards              interface{} `json:"rewards" db:"rewards"`       // JSONB
	MaxParticipants      *int        `json:"max_participants" db:"max_participants"`
	CurrentParticipants  int         `json:"current_participants" db:"current_participants"`
	Difficulty           string      `json:"difficulty" db:"difficulty"`
	MinLevel             *int        `json:"min_level" db:"min_level"`
	MaxLevel             *int        `json:"max_level" db:"max_level"`
	FactionRestrictions  interface{} `json:"faction_restrictions" db:"faction_restrictions"` // JSONB
	RegionRestrictions   interface{} `json:"region_restrictions" db:"region_restrictions"`   // JSONB
	Prerequisites        interface{} `json:"prerequisites" db:"prerequisites"`               // JSONB
	Metadata             interface{} `json:"metadata" db:"metadata"`                         // JSONB
	CreatedBy            *uuid.UUID  `json:"created_by" db:"created_by"`
	CreatedAt            time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt            time.Time   `json:"updated_at" db:"updated_at"`
}

// EventParticipation represents player participation in events - matches API schema
type EventParticipation struct {
	ID             uuid.UUID   `json:"id" db:"id"`
	PlayerID       uuid.UUID   `json:"player_id" db:"player_id"`
	EventID        uuid.UUID   `json:"event_id" db:"event_id"`
	Status         string      `json:"status" db:"status"`
	JoinedAt       time.Time   `json:"joined_at" db:"joined_at"`
	LastActivityAt time.Time   `json:"last_activity_at" db:"last_activity_at"`
	CompletedAt    *time.Time  `json:"completed_at" db:"completed_at"`
	FailedAt       *time.Time  `json:"failed_at" db:"failed_at"`
	AbandonedAt    *time.Time  `json:"abandoned_at" db:"abandoned_at"`
	ProgressData   interface{} `json:"progress_data" db:"progress_data"` // JSONB
	RewardsClaimed bool        `json:"rewards_claimed" db:"rewards_claimed"`
	Score          *int        `json:"score" db:"score"`
	Rank           *int        `json:"rank" db:"rank"`
	Metadata       interface{} `json:"metadata" db:"metadata"` // JSONB
	CreatedAt      time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time   `json:"updated_at" db:"updated_at"`
}

// EventReward represents rewards earned by players - matches V1_115 schema
type EventReward struct {
	ID             uuid.UUID   `json:"id" db:"id"`
	EventID        uuid.UUID   `json:"event_id" db:"event_id"`
	PlayerID       string      `json:"player_id" db:"player_id"`
	ParticipationID *uuid.UUID  `json:"participation_id" db:"participation_id"`
	RewardType     string      `json:"reward_type" db:"reward_type"`
	RewardID       *string     `json:"reward_id" db:"reward_id"`
	Amount         int         `json:"amount" db:"amount"`
	Claimed        bool        `json:"claimed" db:"claimed"`
	ClaimedAt      *time.Time  `json:"claimed_at" db:"claimed_at"`
	ExpiresAt      *time.Time  `json:"expires_at" db:"expires_at"`
	Metadata       interface{} `json:"metadata" db:"metadata"` // JSONB
	CreatedAt      time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time   `json:"updated_at" db:"updated_at"`
}

// EventTemplate represents reusable event templates - matches V1_115 schema
type EventTemplate struct {
	ID                   uuid.UUID   `json:"id" db:"id"`
	Name                 string      `json:"name" db:"name"`
	Type                 string      `json:"type" db:"type"`
	Difficulty           string      `json:"difficulty" db:"difficulty"`
	Description          *string     `json:"description" db:"description"`
	ObjectivesTemplate   interface{} `json:"objectives_template" db:"objectives_template"`   // JSONB
	RewardsTemplate      interface{} `json:"rewards_template" db:"rewards_template"`         // JSONB
	DurationMinutes      *int        `json:"duration_minutes" db:"duration_minutes"`
	MaxParticipants      *int        `json:"max_participants" db:"max_participants"`
	MinLevel             *int        `json:"min_level" db:"min_level"`
	MaxLevel             *int        `json:"max_level" db:"max_level"`
	RegionRestrictions   interface{} `json:"region_restrictions" db:"region_restrictions"`   // JSONB
	FactionRestrictions  interface{} `json:"faction_restrictions" db:"faction_restrictions"` // JSONB
	EventDataTemplate    interface{} `json:"event_data_template" db:"event_data_template"`   // JSONB
	IsActive             bool        `json:"is_active" db:"is_active"`
	UsageCount           int         `json:"usage_count" db:"usage_count"`
	SuccessRate          *float64    `json:"success_rate" db:"success_rate"`
	CreatedBy            *uuid.UUID  `json:"created_by" db:"created_by"`
	CreatedAt            time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt            time.Time   `json:"updated_at" db:"updated_at"`
}

// EventAnalytics represents event analytics data - matches V1_115 schema
type EventAnalytics struct {
	EventID                    uuid.UUID  `json:"event_id" db:"event_id"`
	TotalParticipants          int        `json:"total_participants" db:"total_participants"`
	CompletedParticipants      int        `json:"completed_participants" db:"completed_participants"`
	FailedParticipants         int        `json:"failed_participants" db:"failed_participants"`
	AbandonedParticipants      int        `json:"abandoned_participants" db:"abandoned_participants"`
	AverageCompletionTime      *string    `json:"average_completion_time" db:"average_completion_time"` // INTERVAL
	AverageScore               *float64   `json:"average_score" db:"average_score"`
	AverageParticipationTime   *string    `json:"average_participation_time" db:"average_participation_time"` // INTERVAL
	ParticipationRate          *float64   `json:"participation_rate" db:"participation_rate"`
	CompletionRate             *float64   `json:"completion_rate" db:"completion_rate"`
	SatisfactionRating         *float64   `json:"satisfaction_rating" db:"satisfaction_rating"`
	RevenueGenerated           *float64   `json:"revenue_generated" db:"revenue_generated"`
	EngagementScore            *float64   `json:"engagement_score" db:"engagement_score"`
	PeakConcurrentUsers        int        `json:"peak_concurrent_users" db:"peak_concurrent_users"`
	TotalRewardsClaimed        int        `json:"total_rewards_claimed" db:"total_rewards_claimed"`
	LastUpdated                time.Time  `json:"last_updated" db:"last_updated"`
}

// EventFilter represents filters for querying events
type EventFilter struct {
	Type       *string `json:"type"`
	Region     *string `json:"region"`
	Status     *string `json:"status"`
	Difficulty *string `json:"difficulty"`
	MinLevel   *int    `json:"min_level"`
	MaxLevel   *int    `json:"max_level"`
	Limit      *int    `json:"limit"`
	Offset     *int    `json:"offset"`
}

// ParticipationFilter represents filters for participation queries
type ParticipationFilter struct {
	EventID  *uuid.UUID `json:"event_id"`
	PlayerID *string    `json:"player_id"`
	Status   *string    `json:"status"`
	Limit    *int       `json:"limit"`
	Offset   *int       `json:"offset"`
}

// TemplateFilter represents filters for template queries
type TemplateFilter struct {
	Type     *string `json:"type"`
	IsActive *bool   `json:"is_active"`
	Limit    *int    `json:"limit"`
	Offset   *int    `json:"offset"`
}