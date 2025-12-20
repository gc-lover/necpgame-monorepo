// Package models Issue: #2224
package models

import (
	"time"

	"github.com/google/uuid"
)

// WorldEventIdentity contains identity and basic information
type WorldEventIdentity struct {
	ID          uuid.UUID      `json:"id" db:"id"`
	Title       string         `json:"title" db:"title"`
	Description string         `json:"description,omitempty" db:"description"`
	Type        WorldEventType `json:"type" db:"type"`
	Version     int            `json:"version" db:"version"`
}

// WorldEventTiming contains timing-related fields
type WorldEventTiming struct {
	StartTime        *time.Time `json:"start_time,omitempty" db:"start_time"`
	EndTime          *time.Time `json:"end_time,omitempty" db:"end_time"`
	Duration         *int       `json:"duration,omitempty" db:"duration"`
	CooldownDuration *int       `json:"cooldown_duration,omitempty" db:"cooldown_duration"`
}

// WorldEventScope contains scope and targeting information
type WorldEventScope struct {
	Scale          WorldEventScale     `json:"scale" db:"scale"`
	Frequency      WorldEventFrequency `json:"frequency" db:"frequency"`
	Status         WorldEventStatus    `json:"status" db:"status"`
	MaxConcurrent  *int                `json:"max_concurrent,omitempty" db:"max_concurrent"`
	TargetRegions  []string            `json:"target_regions,omitempty" db:"target_regions"`
	TargetFactions []uuid.UUID         `json:"target_factions,omitempty" db:"target_factions"`
	Prerequisites  []uuid.UUID         `json:"prerequisites,omitempty" db:"prerequisites"`
}

// WorldEventMetadata contains metadata fields
type WorldEventMetadata struct {
	CreatedAt time.Time `json:"created_at" db:"created_at"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
}

// WorldEvent represents a world event
type WorldEvent struct {
	WorldEventIdentity
	WorldEventTiming
	WorldEventScope
	WorldEventMetadata

	Effects []EventEffect `json:"effects,omitempty" db:"-"`
}

// WorldEventType represents the type of world event
type WorldEventType string

const (
	EventTypeEconomic  WorldEventType = "ECONOMIC"
	EventTypePolitical WorldEventType = "POLITICAL"
)

// WorldEventScale represents the scale of world event
type WorldEventScale string

const (
	EventScaleGlobal   WorldEventScale = "GLOBAL"
	EventScaleRegional WorldEventScale = "REGIONAL"
	EventScaleCity     WorldEventScale = "CITY"
	EventScaleLocal    WorldEventScale = "LOCAL"
)

// WorldEventFrequency represents the frequency of world event
type WorldEventFrequency string

const (
	EventFrequencyOneTime WorldEventFrequency = "ONE_TIME"
)

// WorldEventStatus represents the status of world event
type WorldEventStatus string

const (
	EventStatusPlanned   WorldEventStatus = "PLANNED"
	EventStatusAnnounced WorldEventStatus = "ANNOUNCED"
	EventStatusActive    WorldEventStatus = "ACTIVE"
	EventStatusCooldown  WorldEventStatus = "COOLDOWN"
)

// EventEffect represents an effect of a world event
type EventEffect struct {
	ID           uuid.UUID              `json:"id" db:"id"`
	EventID      uuid.UUID              `json:"event_id" db:"event_id"`
	TargetSystem TargetSystem           `json:"target_system" db:"target_system"`
	EffectType   string                 `json:"effect_type" db:"effect_type"`
	Parameters   map[string]interface{} `json:"parameters" db:"parameters"`
	StartTime    time.Time              `json:"start_time" db:"start_time"`
	EndTime      time.Time              `json:"end_time" db:"end_time"`
	IsActive     bool                   `json:"is_active" db:"is_active"`
	CreatedAt    time.Time              `json:"created_at" db:"created_at"`
}

// TargetSystem represents the target system for event effects
type TargetSystem string

// EventAnnouncement represents an announcement about a world event
type EventAnnouncement struct {
	ID             uuid.UUID            `json:"id" db:"id"`
	EventID        uuid.UUID            `json:"event_id" db:"event_id"`
	Title          string               `json:"title" db:"title"`
	Message        string               `json:"message" db:"message"`
	Type           AnnouncementType     `json:"type" db:"type"`
	TargetAudience TargetAudience       `json:"target_audience" db:"target_audience"`
	Priority       AnnouncementPriority `json:"priority" db:"priority"`
	ExpiresAt      *time.Time           `json:"expires_at,omitempty" db:"expires_at"`
	CreatedAt      time.Time            `json:"created_at" db:"created_at"`
}

// AnnouncementType represents the type of announcement
type AnnouncementType string

// TargetAudience represents the target audience for announcements
type TargetAudience string

// AnnouncementPriority represents the priority of announcement
type AnnouncementPriority string
