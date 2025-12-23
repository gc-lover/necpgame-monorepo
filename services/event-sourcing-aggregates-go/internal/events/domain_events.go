// Issue: #2217
// PERFORMANCE: Optimized domain events with memory-efficient serialization
package events

import (
	"time"

	"github.com/google/uuid"
)

// DomainEvent defines the interface for domain events
type DomainEvent interface {
	// EventType returns the event type string
	EventType() string

	// EventVersion returns the event schema version
	EventVersion() int

	// Timestamp returns when the event occurred
	Timestamp() time.Time

	// AggregateID returns the ID of the aggregate this event belongs to
	AggregateID() uuid.UUID

	// SetTimestamp sets the event timestamp
	SetTimestamp(timestamp time.Time)

	// SetAggregateID sets the aggregate ID
	SetAggregateID(aggregateID uuid.UUID)
}

// BaseEvent provides common event functionality
type BaseEvent struct {
	eventTimestamp   time.Time `json:"timestamp"`
	eventAggregateID uuid.UUID `json:"aggregate_id"`
}

// EventType returns the event type (must be overridden by concrete events)
func (e *BaseEvent) EventType() string {
	panic("EventType() must be implemented by concrete event")
}

// EventVersion returns the event version (must be overridden by concrete events)
func (e *BaseEvent) EventVersion() int {
	return 1
}

// Timestamp returns the event timestamp
func (e *BaseEvent) Timestamp() time.Time {
	return e.eventTimestamp
}

// AggregateID returns the aggregate ID
func (e *BaseEvent) AggregateID() uuid.UUID {
	return e.eventAggregateID
}

// SetTimestamp sets the event timestamp
func (e *BaseEvent) SetTimestamp(timestamp time.Time) {
	e.eventTimestamp = timestamp
}

// SetAggregateID sets the aggregate ID
func (e *BaseEvent) SetAggregateID(aggregateID uuid.UUID) {
	e.eventAggregateID = aggregateID
}

// NewBaseEvent creates a new base event with current timestamp
func NewBaseEvent(aggregateID uuid.UUID) BaseEvent {
	return BaseEvent{
		eventTimestamp:   time.Now(),
		eventAggregateID: aggregateID,
	}
}

// Player Events

// PlayerCreatedEvent represents player creation
type PlayerCreatedEvent struct {
	BaseEvent
	PlayerID   uuid.UUID `json:"player_id"`
	Username   string    `json:"username"`
	Email      string    `json:"email"`
	Region     string    `json:"region"`
	CreatedAt  time.Time `json:"created_at"`
}

func (e PlayerCreatedEvent) EventType() string {
	return "player.created"
}

// PlayerLevelChangedEvent represents level change
type PlayerLevelChangedEvent struct {
	BaseEvent
	OldLevel int `json:"old_level"`
	NewLevel int `json:"new_level"`
	XPChange int `json:"xp_change"`
	Reason   string `json:"reason"`
}

func (e PlayerLevelChangedEvent) EventType() string {
	return "player.level.changed"
}

// PlayerJoinedGuildEvent represents joining a guild
type PlayerJoinedGuildEvent struct {
	BaseEvent
	GuildID   uuid.UUID `json:"guild_id"`
	GuildName string    `json:"guild_name"`
	Role      string    `json:"role"`
	JoinedAt  time.Time `json:"joined_at"`
}

func (e PlayerJoinedGuildEvent) EventType() string {
	return "player.guild.joined"
}

// Guild Events

// GuildCreatedEvent represents guild creation
type GuildCreatedEvent struct {
	BaseEvent
	GuildID      uuid.UUID `json:"guild_id"`
	Name         string    `json:"name"`
	Description  string    `json:"description"`
	LeaderID     uuid.UUID `json:"leader_id"`
	CreatedAt    time.Time `json:"created_at"`
	MaxMembers   int       `json:"max_members"`
}

func (e GuildCreatedEvent) EventType() string {
	return "guild.created"
}

// GuildMemberAddedEvent represents member addition
type GuildMemberAddedEvent struct {
	BaseEvent
	MemberID   uuid.UUID `json:"member_id"`
	MemberName string    `json:"member_name"`
	Role       string    `json:"role"`
	AddedBy    uuid.UUID `json:"added_by"`
	AddedAt    time.Time `json:"added_at"`
}

func (e GuildMemberAddedEvent) EventType() string {
	return "guild.member.added"
}

// GuildSettingsChangedEvent represents settings change
type GuildSettingsChangedEvent struct {
	BaseEvent
	SettingName  string      `json:"setting_name"`
	OldValue     interface{} `json:"old_value"`
	NewValue     interface{} `json:"new_value"`
	ChangedBy    uuid.UUID   `json:"changed_by"`
	ChangedAt    time.Time   `json:"changed_at"`
}

func (e GuildSettingsChangedEvent) EventType() string {
	return "guild.settings.changed"
}

// Trading Events

// TradeInitiatedEvent represents trade initiation
type TradeInitiatedEvent struct {
	BaseEvent
	TradeID      uuid.UUID `json:"trade_id"`
	InitiatorID  uuid.UUID `json:"initiator_id"`
	ReceiverID   uuid.UUID `json:"receiver_id"`
	ItemsOffered []TradeItem `json:"items_offered"`
	ItemsRequested []TradeItem `json:"items_requested"`
	CurrencyOffered int `json:"currency_offered"`
	CurrencyRequested int `json:"currency_requested"`
	InitiatedAt  time.Time `json:"initiated_at"`
}

func (e TradeInitiatedEvent) EventType() string {
	return "trading.initiated"
}

// TradeItem represents an item in a trade
type TradeItem struct {
	ItemID   uuid.UUID `json:"item_id"`
	ItemName string    `json:"item_name"`
	Quantity int       `json:"quantity"`
	Quality  string    `json:"quality"`
}

// TradeExecutedEvent represents successful trade execution
type TradeExecutedEvent struct {
	BaseEvent
	TradeID     uuid.UUID `json:"trade_id"`
	ExecutedAt  time.Time `json:"executed_at"`
	FeeCharged  int       `json:"fee_charged"`
}

func (e TradeExecutedEvent) EventType() string {
	return "trading.executed"
}

// TradeCancelledEvent represents trade cancellation
type TradeCancelledEvent struct {
	BaseEvent
	TradeID      uuid.UUID `json:"trade_id"`
	CancelledBy  uuid.UUID `json:"cancelled_by"`
	Reason       string    `json:"reason"`
	CancelledAt  time.Time `json:"cancelled_at"`
}

func (e TradeCancelledEvent) EventType() string {
	return "trading.cancelled"
}

// Combat Events

// CombatStartedEvent represents combat initiation
type CombatStartedEvent struct {
	BaseEvent
	CombatID       uuid.UUID `json:"combat_id"`
	CombatType     string    `json:"combat_type"`
	Participants   []CombatParticipant `json:"participants"`
	Location       CombatLocation `json:"location"`
	Rules          CombatRules `json:"rules"`
	MaxDuration    int         `json:"max_duration_seconds"`
	StartedAt      time.Time   `json:"started_at"`
}

func (e CombatStartedEvent) EventType() string {
	return "combat.started"
}

// CombatParticipant represents a participant in combat
type CombatParticipant struct {
	PlayerID    uuid.UUID `json:"player_id"`
	CharacterID uuid.UUID `json:"character_id"`
	Team        string    `json:"team"`
	Role        string    `json:"role"`
	JoinedAt    time.Time `json:"joined_at"`
}

// CombatLocation represents combat location
type CombatLocation struct {
	X         float64 `json:"x"`
	Y         float64 `json:"y"`
	Z         float64 `json:"z"`
	ZoneID    string  `json:"zone_id"`
	ZoneName  string  `json:"zone_name"`
}

// CombatRules represents combat rules
type CombatRules struct {
	AllowRespawn   bool `json:"allow_respawn"`
	FriendlyFire   bool `json:"friendly_fire"`
	TimeLimit      int  `json:"time_limit_seconds"`
	MaxParticipants int  `json:"max_participants"`
	VictoryConditions []string `json:"victory_conditions"`
}

// CombatDamageEvent represents damage dealt
type CombatDamageEvent struct {
	BaseEvent
	AttackerID     uuid.UUID `json:"attacker_id"`
	TargetID       uuid.UUID `json:"target_id"`
	Damage         int       `json:"damage"`
	DamageType     string    `json:"damage_type"`
	CriticalHit    bool      `json:"critical_hit"`
	HitLocation    string    `json:"hit_location"`
	Distance       float64   `json:"distance"`
	WeaponID       uuid.UUID `json:"weapon_id"`
	EventTimestamp time.Time `json:"timestamp"` // Use different field name
}

func (e CombatDamageEvent) EventType() string {
	return "combat.damage"
}

// CombatEndedEvent represents combat conclusion
type CombatEndedEvent struct {
	BaseEvent
	CombatID     uuid.UUID `json:"combat_id"`
	EndReason    string    `json:"end_reason"`
	Duration     int       `json:"duration_seconds"`
	Winner       string    `json:"winner_team,omitempty"`
	FinalStats   CombatStats `json:"final_stats"`
	EndedAt      time.Time   `json:"ended_at"`
}

func (e CombatEndedEvent) EventType() string {
	return "combat.ended"
}

// CombatStats represents final combat statistics
type CombatStats struct {
	TotalDamage    int `json:"total_damage"`
	TotalKills     int `json:"total_kills"`
	TotalDeaths    int `json:"total_deaths"`
	TotalAssists   int `json:"total_assists"`
	LongestStreak  int `json:"longest_kill_streak"`
}

// EventRegistry manages event type mappings and serialization
type EventRegistry struct {
	eventTypes map[string]func() DomainEvent
}

// DomainEventInterface defines the interface for domain events
type DomainEventInterface interface {
	EventType() string
	EventVersion() int
	Timestamp() time.Time
	AggregateID() uuid.UUID
	SetTimestamp(timestamp time.Time)
	SetAggregateID(aggregateID uuid.UUID)
}

// NewEventRegistry creates a new event registry
func NewEventRegistry() *EventRegistry {
	registry := &EventRegistry{
		eventTypes: make(map[string]func() DomainEvent),
	}

	// Register all event types
	registry.registerEventTypes()

	return registry
}

// registerEventTypes registers all known event types
func (r *EventRegistry) registerEventTypes() {
	// Player events
	r.eventTypes["player.created"] = func() DomainEvent {
		return &PlayerCreatedEvent{}
	}
	r.eventTypes["player.level.changed"] = func() DomainEvent {
		return &PlayerLevelChangedEvent{}
	}
	r.eventTypes["player.guild.joined"] = func() DomainEvent {
		return &PlayerJoinedGuildEvent{}
	}

	// Guild events
	r.eventTypes["guild.created"] = func() DomainEvent {
		return &GuildCreatedEvent{}
	}
	r.eventTypes["guild.member.added"] = func() DomainEvent {
		return &GuildMemberAddedEvent{}
	}
	r.eventTypes["guild.settings.changed"] = func() DomainEvent {
		return &GuildSettingsChangedEvent{}
	}

	// Trading events
	r.eventTypes["trading.initiated"] = func() DomainEvent {
		return &TradeInitiatedEvent{}
	}
	r.eventTypes["trading.executed"] = func() DomainEvent {
		return &TradeExecutedEvent{}
	}
	r.eventTypes["trading.cancelled"] = func() DomainEvent {
		return &TradeCancelledEvent{}
	}

	// Combat events
	r.eventTypes["combat.started"] = func() DomainEvent {
		return &CombatStartedEvent{}
	}
	r.eventTypes["combat.damage"] = func() DomainEvent {
		return &CombatDamageEvent{}
	}
	r.eventTypes["combat.ended"] = func() DomainEvent {
		return &CombatEndedEvent{}
	}
}

// CreateEvent creates an event instance by type
func (r *EventRegistry) CreateEvent(eventType string, aggregateID uuid.UUID) DomainEvent {
	if factory, exists := r.eventTypes[eventType]; exists {
		event := factory()
		event.SetAggregateID(aggregateID)
		event.SetTimestamp(time.Now())
		return event
	}
	return nil
}

// GetEventTypes returns all registered event types
func (r *EventRegistry) GetEventTypes() []string {
	types := make([]string, 0, len(r.eventTypes))
	for eventType := range r.eventTypes {
		types = append(types, eventType)
	}
	return types
}
