// Issue: #2217
// PERFORMANCE: Optimized player aggregate for MMOFPS RPG mechanics
package aggregates

import (
	"fmt"
	"time"

	"github.com/google/uuid"

	"event-sourcing-aggregates-go/internal/events"
)

// PlayerAggregate represents a player in the game
type PlayerAggregate struct {
	BaseAggregate

	// Player state
	Username    string
	Email       string
	Region      string
	Level       int
	XP          int
	GuildID     *uuid.UUID
	GuildRole   string
	CreatedAt   time.Time
	LastLoginAt *time.Time
	IsActive    bool
}

// NewPlayerAggregate creates a new player aggregate
func NewPlayerAggregate() *PlayerAggregate {
	return &PlayerAggregate{
		BaseAggregate: NewBaseAggregate("player", uuid.New()),
		IsActive:      true,
	}
}

// NewPlayerAggregateWithID creates a new player aggregate with specific ID
func NewPlayerAggregateWithID(id uuid.UUID) *PlayerAggregate {
	return &PlayerAggregate{
		BaseAggregate: NewBaseAggregate("player", id),
		IsActive:      true,
	}
}

// HandleCreatePlayer handles player creation command
func (p *PlayerAggregate) HandleCreatePlayer(username, email, region string) error {
	if p.Version != 0 {
		return fmt.Errorf("player already exists")
	}

	if username == "" {
		return fmt.Errorf("username cannot be empty")
	}

	if email == "" {
		return fmt.Errorf("email cannot be empty")
	}

	event := &events.PlayerCreatedEvent{
		BaseEvent: events.BaseEvent{},
		PlayerID:  p.ID,
		Username:  username,
		Email:     email,
		Region:    region,
		CreatedAt: time.Now(),
	}

	event.SetAggregateID(p.ID)
	event.SetTimestamp(time.Now())

	p.AddEvent(event)
	return nil
}

// HandleChangeLevel handles level change
func (p *PlayerAggregate) HandleChangeLevel(newLevel int, xpChange int, reason string) error {
	if newLevel <= 0 {
		return fmt.Errorf("level must be positive")
	}

	if newLevel <= p.Level {
		return fmt.Errorf("new level must be higher than current level")
	}

	event := &events.PlayerLevelChangedEvent{
		BaseEvent: events.BaseEvent{},
		OldLevel:  p.Level,
		NewLevel:  newLevel,
		XPChange:  xpChange,
		Reason:    reason,
	}

	event.SetAggregateID(p.ID)
	event.SetTimestamp(time.Now())

	p.AddEvent(event)
	return nil
}

// HandleJoinGuild handles guild joining
func (p *PlayerAggregate) HandleJoinGuild(guildID uuid.UUID, guildName, role string) error {
	if p.GuildID != nil {
		return fmt.Errorf("player is already in a guild")
	}

	event := &events.PlayerJoinedGuildEvent{
		BaseEvent: events.BaseEvent{},
		GuildID:   guildID,
		GuildName: guildName,
		Role:      role,
		JoinedAt:  time.Now(),
	}

	event.SetAggregateID(p.ID)
	event.SetTimestamp(time.Now())

	p.AddEvent(event)
	return nil
}

// ApplyEvent applies an event to the player aggregate
func (p *PlayerAggregate) ApplyEvent(event events.DomainEventInterface) error {
	switch e := event.(type) {
	case *events.PlayerCreatedEvent:
		return p.applyPlayerCreated(e)
	case *events.PlayerLevelChangedEvent:
		return p.applyPlayerLevelChanged(e)
	case *events.PlayerJoinedGuildEvent:
		return p.applyPlayerJoinedGuild(e)
	default:
		return fmt.Errorf("unknown event type: %s", event.EventType())
	}
}

// applyPlayerCreated applies player creation event
func (p *PlayerAggregate) applyPlayerCreated(event *events.PlayerCreatedEvent) error {
	p.Username = event.Username
	p.Email = event.Email
	p.Region = event.Region
	p.Level = 1
	p.XP = 0
	p.CreatedAt = event.CreatedAt
	p.IsActive = true

	return nil
}

// applyPlayerLevelChanged applies level change event
func (p *PlayerAggregate) applyPlayerLevelChanged(event *events.PlayerLevelChangedEvent) error {
	p.Level = event.NewLevel
	p.XP += event.XPChange

	return nil
}

// applyPlayerJoinedGuild applies guild join event
func (p *PlayerAggregate) applyPlayerJoinedGuild(event *events.PlayerJoinedGuildEvent) error {
	p.GuildID = &event.GuildID
	p.GuildRole = event.Role

	return nil
}

// ValidateState validates player aggregate state
func (p *PlayerAggregate) ValidateState() error {
	if err := p.BaseAggregate.ValidateState(); err != nil {
		return err
	}

	if p.Username == "" {
		return fmt.Errorf("username cannot be empty")
	}

	if p.Email == "" {
		return fmt.Errorf("email cannot be empty")
	}

	if p.Level <= 0 {
		return fmt.Errorf("level must be positive")
	}

	if p.XP < 0 {
		return fmt.Errorf("XP cannot be negative")
	}

	return nil
}

// GetPlayerInfo returns player information for read models
func (p *PlayerAggregate) GetPlayerInfo() PlayerInfo {
	return PlayerInfo{
		ID:          p.ID,
		Username:    p.Username,
		Email:       p.Email,
		Region:      p.Region,
		Level:       p.Level,
		XP:          p.XP,
		GuildID:     p.GuildID,
		GuildRole:   p.GuildRole,
		CreatedAt:   p.CreatedAt,
		LastLoginAt: p.LastLoginAt,
		IsActive:    p.IsActive,
		Version:     p.Version,
	}
}

// PlayerInfo represents player information for projections
type PlayerInfo struct {
	ID          uuid.UUID  `json:"id"`
	Username    string     `json:"username"`
	Email       string     `json:"email"`
	Region      string     `json:"region"`
	Level       int        `json:"level"`
	XP          int        `json:"xp"`
	GuildID     *uuid.UUID `json:"guild_id,omitempty"`
	GuildRole   string     `json:"guild_role,omitempty"`
	CreatedAt   time.Time  `json:"created_at"`
	LastLoginAt *time.Time `json:"last_login_at,omitempty"`
	IsActive    bool       `json:"is_active"`
	Version     int        `json:"version"`
}
