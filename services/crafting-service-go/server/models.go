// Issue: #2203 - Crafting service models
package server

import (
	"time"

	"github.com/google/uuid"
)

// Recipe represents a crafting recipe
type Recipe struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description,omitempty" db:"description"`
	Category    string    `json:"category" db:"category"`
	Tier        int       `json:"tier" db:"tier"`
	Quality     int       `json:"quality" db:"quality"`
	Duration    int       `json:"duration" db:"duration"` // seconds
	SuccessRate float64   `json:"success_rate" db:"success_rate"`

	// Materials and requirements stored as JSON in DB
	Materials   []RecipeMaterial   `json:"materials,omitempty" db:"materials"`
	Requirements *RecipeRequirements `json:"requirements,omitempty" db:"requirements"`

	CreatedAt time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt *time.Time `json:"updated_at,omitempty" db:"updated_at"`
}

// RecipeMaterial represents required material for recipe
type RecipeMaterial struct {
	ItemID     uuid.UUID `json:"item_id" db:"item_id"`
	Quantity   int       `json:"quantity" db:"quantity"`
	QualityMin *int      `json:"quality_min,omitempty" db:"quality_min"`
}

// RecipeRequirements represents recipe requirements
type RecipeRequirements struct {
	SkillLevel  *int    `json:"skill_level,omitempty" db:"skill_level"`
	StationType *string `json:"station_type,omitempty" db:"station_type"`
	ZoneAccess  *string `json:"zone_access,omitempty" db:"zone_access"`
}

// Order represents a crafting order
type Order struct {
	ID              uuid.UUID `json:"id" db:"id"`
	PlayerID        uuid.UUID `json:"player_id" db:"player_id"`
	RecipeID        uuid.UUID `json:"recipe_id" db:"recipe_id"`
	StationID       *uuid.UUID `json:"station_id,omitempty" db:"station_id"`
	Status          string    `json:"status" db:"status"`
	QualityModifier float64   `json:"quality_modifier" db:"quality_modifier"`
	StationBonus    float64   `json:"station_bonus" db:"station_bonus"`
	Progress        float64   `json:"progress" db:"progress"`

	StartedAt   *time.Time `json:"started_at,omitempty" db:"started_at"`
	CompletedAt *time.Time `json:"completed_at,omitempty" db:"completed_at"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   *time.Time `json:"updated_at,omitempty" db:"updated_at"`
}

// Station represents a crafting station
type Station struct {
	ID               uuid.UUID  `json:"id" db:"id"`
	Name             string     `json:"name" db:"name"`
	Description      string     `json:"description,omitempty" db:"description"`
	Type             string     `json:"type" db:"type"`
	Efficiency       float64    `json:"efficiency" db:"efficiency"`
	ZoneID           uuid.UUID  `json:"zone_id" db:"zone_id"`
	OwnerID          *uuid.UUID `json:"owner_id,omitempty" db:"owner_id"`
	CurrentOrderID   *uuid.UUID `json:"current_order_id,omitempty" db:"current_order_id"`
	IsAvailable      bool       `json:"is_available" db:"is_available"`

	MaintenanceCost  int        `json:"maintenance_cost,omitempty" db:"maintenance_cost"`
	LastMaintenance  *time.Time `json:"last_maintenance,omitempty" db:"last_maintenance"`
	CreatedAt        time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt        *time.Time `json:"updated_at,omitempty" db:"updated_at"`
}

// ProductionChain represents a multi-stage production chain
type ProductionChain struct {
	ID            uuid.UUID     `json:"id" db:"id"`
	Name          string        `json:"name" db:"name"`
	Description   string        `json:"description,omitempty" db:"description"`
	Category      string        `json:"category,omitempty" db:"category"`
	Complexity    int           `json:"complexity" db:"complexity"`
	Stages        []ChainStage  `json:"stages" db:"stages"`
	Status        string        `json:"status" db:"status"`
	CurrentStage  int           `json:"current_stage" db:"current_stage"`
	PlayerID      uuid.UUID     `json:"player_id" db:"player_id"`
	TotalProgress float64       `json:"total_progress" db:"total_progress"`

	StartedAt   *time.Time `json:"started_at,omitempty" db:"started_at"`
	CompletedAt *time.Time `json:"completed_at,omitempty" db:"completed_at"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   *time.Time `json:"updated_at,omitempty" db:"updated_at"`
}

// ChainStage represents a stage in production chain
type ChainStage struct {
	OrderID      uuid.UUID `json:"order_id" db:"order_id"`
	Sequence     int       `json:"sequence" db:"sequence"`
	Dependencies []int     `json:"dependencies" db:"dependencies"`
	Status       string    `json:"status" db:"status"`
}

// StationBooking represents a station booking
type StationBooking struct {
	StationID   uuid.UUID `json:"station_id" db:"station_id"`
	PlayerID    uuid.UUID `json:"player_id" db:"player_id"`
	BookedUntil time.Time `json:"booked_until" db:"booked_until"`
	Priority    int       `json:"priority" db:"priority"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
}
