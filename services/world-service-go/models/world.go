package models

import (
	"time"

	"github.com/google/uuid"
)

type ResetType string

type ResetStatus string

type QuestPoolType string

type LoginRewardType string

type ResetExecution struct {
	ID               uuid.UUID   `json:"execution_id" db:"id"`
	ResetType        ResetType   `json:"reset_type" db:"reset_type"`
	Status           ResetStatus `json:"status" db:"status"`
	StartedAt        time.Time   `json:"started_at" db:"started_at"`
	CompletedAt      *time.Time  `json:"completed_at,omitempty" db:"completed_at"`
	PlayersProcessed int         `json:"players_processed" db:"players_processed"`
	PlayersTotal     int         `json:"players_total" db:"players_total"`
	CreatedAt        time.Time   `json:"created_at" db:"created_at"`
	UpdatedAt        time.Time   `json:"updated_at" db:"updated_at"`
}

type ResetStatusInfo struct {
	ResetType        ResetType   `json:"reset_type"`
	Status           ResetStatus `json:"status"`
	LastReset        *time.Time  `json:"last_reset,omitempty"`
	NextReset        time.Time   `json:"next_reset"`
	PlayersProcessed int         `json:"players_processed"`
	PlayersTotal     int         `json:"players_total"`
}

type NextResetInfo struct {
	ResetType             ResetType `json:"reset_type"`
	NextReset             time.Time `json:"next_reset"`
	TimeUntilResetSeconds int       `json:"time_until_reset_seconds"`
}

type QuestPoolEntry struct {
	QuestID  uuid.UUID `json:"quest_id" db:"quest_id"`
	Weight   int       `json:"weight" db:"weight"`
	MinLevel int       `json:"min_level" db:"min_level"`
	MaxLevel *int      `json:"max_level,omitempty" db:"max_level"`
	IsActive bool      `json:"is_active" db:"is_active"`
}

type QuestPool struct {
	PoolType QuestPoolType    `json:"pool_type"`
	Quests   []QuestPoolEntry `json:"quests"`
	Total    int              `json:"total"`
}

type PlayerQuest struct {
	ID          uuid.UUID     `json:"id" db:"id"`
	PlayerID    uuid.UUID     `json:"player_id" db:"player_id"`
	QuestID     uuid.UUID     `json:"quest_id" db:"quest_id"`
	PoolType    QuestPoolType `json:"pool_type" db:"pool_type"`
	AssignedAt  time.Time     `json:"assigned_at" db:"assigned_at"`
	CompletedAt *time.Time    `json:"completed_at,omitempty" db:"completed_at"`
	ResetDate   time.Time     `json:"reset_date" db:"reset_date"`
	CreatedAt   time.Time     `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time     `json:"updated_at" db:"updated_at"`
}

type LoginReward struct {
	RewardType LoginRewardType        `json:"reward_type"`
	DayNumber  int                    `json:"day_number"`
	RewardData map[string]interface{} `json:"reward_data"`
	ClaimedAt  *time.Time             `json:"claimed_at,omitempty"`
}

type PlayerLoginRewards struct {
	PlayerID         uuid.UUID     `json:"player_id"`
	AvailableRewards []LoginReward `json:"available_rewards"`
	ClaimedRewards   []LoginReward `json:"claimed_rewards"`
	StreakDays       int           `json:"streak_days"`
}

type LoginStreak struct {
	PlayerID      uuid.UUID `json:"player_id" db:"player_id"`
	StreakDays    int       `json:"streak_days" db:"streak_days"`
	LastLoginDate time.Time `json:"last_login_date" db:"last_login_date"`
	MaxStreakDays int       `json:"max_streak_days" db:"max_streak_days"`
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
	UpdatedAt     time.Time `json:"updated_at" db:"updated_at"`
}

type ResetSchedule struct {
	DailyReset  DailyResetSchedule  `json:"daily_reset"`
	WeeklyReset WeeklyResetSchedule `json:"weekly_reset"`
}

type DailyResetSchedule struct {
	Time      string    `json:"time"`
	Timezone  string    `json:"timezone"`
	NextReset time.Time `json:"next_reset"`
}

type WeeklyResetSchedule struct {
	DayOfWeek int       `json:"day_of_week"`
	Time      string    `json:"time"`
	Timezone  string    `json:"timezone"`
	NextReset time.Time `json:"next_reset"`
}

type ResetEvent struct {
	ID        uuid.UUID              `json:"id" db:"id"`
	EventType string                 `json:"event_type" db:"event_type"`
	ResetType *ResetType             `json:"reset_type,omitempty" db:"reset_type"`
	PlayerID  *uuid.UUID             `json:"player_id,omitempty" db:"player_id"`
	EventData map[string]interface{} `json:"event_data" db:"event_data"`
	CreatedAt time.Time              `json:"created_at" db:"created_at"`
}

type TravelEvent struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	EventCode       string                 `json:"event_code" db:"event_code"`
	EventName       string                 `json:"event_name" db:"event_name"`
	EventType       string                 `json:"event_type" db:"event_type"`
	EpochID         string                 `json:"epoch_id" db:"epoch_id"`
	Description     string                 `json:"description" db:"description"`
	BaseProbability float64                `json:"base_probability" db:"base_probability"`
	CooldownHours   int                    `json:"cooldown_hours" db:"cooldown_hours"`
	SkillChecks     []SkillCheckDefinition `json:"skill_checks" db:"skill_checks"`
	Rewards         map[string]interface{} `json:"rewards" db:"rewards"`
	Penalties       map[string]interface{} `json:"penalties" db:"penalties"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

type TravelEventInstance struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	EventID     uuid.UUID  `json:"event_id" db:"event_id"`
	CharacterID uuid.UUID  `json:"character_id" db:"character_id"`
	ZoneID      uuid.UUID  `json:"zone_id" db:"zone_id"`
	EpochID     string     `json:"epoch_id" db:"epoch_id"`
	State       string     `json:"state" db:"state"`
	StartedAt   time.Time  `json:"started_at" db:"started_at"`
	CompletedAt *time.Time `json:"completed_at,omitempty" db:"completed_at"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time  `json:"updated_at" db:"updated_at"`
}

type SkillCheckDefinition struct {
	Skill string `json:"skill"`
	DC    int    `json:"dc"`
}

type TravelEventReward struct {
	Type string                 `json:"type"`
	Data map[string]interface{} `json:"data"`
}

type TravelEventPenalty struct {
	Type string                 `json:"type"`
	Data map[string]interface{} `json:"data"`
}

type TravelEventCooldown struct {
	EventType       string    `json:"event_type"`
	LastTriggeredAt time.Time `json:"last_triggered_at"`
	CooldownUntil   time.Time `json:"cooldown_until"`
}

type TriggerTravelEventRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	ZoneID      uuid.UUID `json:"zone_id"`
	EpochID     *string   `json:"epoch_id,omitempty"`
}

type StartTravelEventRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
}

type SkillCheckRequest struct {
	Skill       string    `json:"skill"`
	CharacterID uuid.UUID `json:"character_id"`
}

type SkillCheckResponse struct {
	Success         bool                   `json:"success"`
	CriticalSuccess bool                   `json:"critical_success"`
	CriticalFailure bool                   `json:"critical_failure"`
	RollResult      int                    `json:"roll_result"`
	DC              int                    `json:"dc"`
	Modifiers       map[string]interface{} `json:"modifiers"`
}

type CompleteTravelEventRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	Success     bool      `json:"success"`
}

type TravelEventCompletionResponse struct {
	EventInstanceID uuid.UUID            `json:"event_instance_id"`
	Rewards         []TravelEventReward  `json:"rewards"`
	Penalties       []TravelEventPenalty `json:"penalties"`
}

type AvailableTravelEventsResponse struct {
	ZoneID  uuid.UUID     `json:"zone_id"`
	EpochID string        `json:"epoch_id"`
	Events  []TravelEvent `json:"events"`
	Total   int           `json:"total"`
}

type EpochTravelEventsResponse struct {
	EpochID string        `json:"epoch_id"`
	Events  []TravelEvent `json:"events"`
	Total   int           `json:"total"`
}

type TravelEventCooldownResponse struct {
	CharacterID uuid.UUID             `json:"character_id"`
	Cooldowns   []TravelEventCooldown `json:"cooldowns"`
}

type TravelEventProbabilityResponse struct {
	EventType   string                 `json:"event_type"`
	Probability float64                `json:"probability"`
	Modifiers   map[string]interface{} `json:"modifiers"`
}

type TravelEventRewardsResponse struct {
	EventID uuid.UUID           `json:"event_id"`
	Rewards []TravelEventReward `json:"rewards"`
}

type TravelEventPenaltiesResponse struct {
	EventID   uuid.UUID            `json:"event_id"`
	Penalties []TravelEventPenalty `json:"penalties"`
}

// GlobalState Global State models (Issue: #140876058)
type GlobalState struct {
	Key       string                 `json:"key" db:"key"`
	Category  string                 `json:"category" db:"category"`
	Value     map[string]interface{} `json:"value" db:"value"`
	Version   int                    `json:"version" db:"version"`
	SyncType  string                 `json:"sync_type" db:"sync_type"`
	CreatedAt time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt time.Time              `json:"updated_at" db:"updated_at"`
}

type StateUpdateRequest struct {
	Value    map[string]interface{} `json:"value"`
	Version  *int                   `json:"version,omitempty"`
	SyncType *string                `json:"sync_type,omitempty"`
}

type BatchStateUpdateRequest struct {
	Updates []BatchStateUpdate `json:"updates"`
}

type BatchStateUpdate struct {
	Key      string                 `json:"key"`
	Value    map[string]interface{} `json:"value"`
	Version  *int                   `json:"version,omitempty"`
	SyncType *string                `json:"sync_type,omitempty"`
}
