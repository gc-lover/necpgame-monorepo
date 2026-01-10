// Player aggregate implementation for Event Sourcing
// Issue: #2217
// Agent: Backend Agent
package aggregates

import (
	"errors"
	"fmt"
	"time"

	"github.com/google/uuid"

	"necpgame/services/event-sourcing-aggregates-go/internal/events"
)

// PlayerState represents the current state of a player
type PlayerState struct {
	PlayerID   uuid.UUID `json:"player_id"`
	Username   string    `json:"username"`
	Email      string    `json:"email"`
	Level      int       `json:"level"`
	Experience int64     `json:"experience"`
	Inventory  []PlayerItem `json:"inventory"`
	Equipment  map[string]uuid.UUID `json:"equipment"` // slot -> item_id
	CreatedAt  time.Time `json:"created_at"`
	UpdatedAt  time.Time `json:"updated_at"`
}

// PlayerItem represents an item in player's inventory
type PlayerItem struct {
	ItemID   uuid.UUID `json:"item_id"`
	Name     string    `json:"name"`
	Quantity int       `json:"quantity"`
}

// PlayerAggregate represents a player aggregate root
type PlayerAggregate struct {
	*BaseAggregate
	State PlayerState `json:"state"`
}

// NewPlayerAggregate creates a new player aggregate
func NewPlayerAggregate(id uuid.UUID) *PlayerAggregate {
	aggregate := &PlayerAggregate{
		BaseAggregate: NewBaseAggregate(id, "player"),
		State: PlayerState{
			PlayerID:  id,
			Level:     1,
			Experience: 0,
			Inventory: make([]PlayerItem, 0),
			Equipment: make(map[string]uuid.UUID),
			CreatedAt: time.Now().UTC(),
			UpdatedAt: time.Now().UTC(),
		},
	}
	return aggregate
}

// CreatePlayer creates a new player
func (p *PlayerAggregate) CreatePlayer(username, email string) error {
	if p.Version != 0 {
		return errors.New("player already exists")
	}

	if username == "" || email == "" {
		return errors.New("username and email are required")
	}

	event := events.NewPlayerCreatedEvent(p.ID, username, email)
	p.RaiseEvent(event)

	return nil
}

// GainLevel increases player level
func (p *PlayerAggregate) GainLevel(newLevel int, experience int64) error {
	if newLevel <= p.State.Level {
		return errors.New("new level must be higher than current level")
	}

	if experience < p.State.Experience {
		return errors.New("experience cannot decrease")
	}

	event := events.NewPlayerLevelGainedEvent(p.ID, newLevel, p.State.Level, experience)
	p.RaiseEvent(event)

	return nil
}

// EquipItem equips an item in specified slot
func (p *PlayerAggregate) EquipItem(itemID uuid.UUID, slot string) error {
	// Check if item exists in inventory
	found := false
	for _, item := range p.State.Inventory {
		if item.ItemID == itemID {
			found = true
			break
		}
	}

	if !found {
		return errors.New("item not found in inventory")
	}

	event := events.NewPlayerItemEquippedEvent(p.ID, itemID, slot)
	p.RaiseEvent(event)

	return nil
}

// ApplyEvent applies domain events to update aggregate state
func (p *PlayerAggregate) ApplyEvent(event events.DomainEvent) error {
	switch e := event.(type) {
	case *events.PlayerCreatedEvent:
		return p.applyPlayerCreated(e)
	case *events.PlayerLevelGainedEvent:
		return p.applyPlayerLevelGained(e)
	case *events.PlayerItemEquippedEvent:
		return p.applyPlayerItemEquipped(e)
	default:
		return fmt.Errorf("unknown event type: %s", event.GetEventType())
	}
}

// applyPlayerCreated applies PlayerCreatedEvent
func (p *PlayerAggregate) applyPlayerCreated(event *events.PlayerCreatedEvent) error {
	p.State.Username = event.Username
	p.State.Email = event.Email
	p.State.CreatedAt = event.CreatedAt
	p.State.UpdatedAt = event.CreatedAt
	return nil
}

// applyPlayerLevelGained applies PlayerLevelGainedEvent
func (p *PlayerAggregate) applyPlayerLevelGained(event *events.PlayerLevelGainedEvent) error {
	p.State.Level = event.NewLevel
	p.State.Experience = event.Experience
	p.State.UpdatedAt = event.GainedAt
	return nil
}

// applyPlayerItemEquipped applies PlayerItemEquippedEvent
func (p *PlayerAggregate) applyPlayerItemEquipped(event *events.PlayerItemEquippedEvent) error {
	p.State.Equipment[event.Slot] = event.ItemID
	p.State.UpdatedAt = event.EquippedAt
	return nil
}

// ValidateState performs business rule validation
func (p *PlayerAggregate) ValidateState() error {
	if p.State.Level < 1 {
		return errors.New("player level cannot be less than 1")
	}

	if p.State.Experience < 0 {
		return errors.New("player experience cannot be negative")
	}

	if p.State.Username == "" {
		return errors.New("player username cannot be empty")
	}

	if p.State.Email == "" {
		return errors.New("player email cannot be empty")
	}

	return nil
}

// GetState returns current player state
func (p *PlayerAggregate) GetState() PlayerState {
	return p.State
}