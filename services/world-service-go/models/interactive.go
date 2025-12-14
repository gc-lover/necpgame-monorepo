// Issue: #1841-#1844 - Interactive objects models
package models

import (
	"context"
	"time"
)

// InteractiveCategory represents main categories of interactive objects
type InteractiveCategory string

const (
	CategoryFactionControl InteractiveCategory = "faction_control"
	CategoryCommunication  InteractiveCategory = "communication"
	CategoryMedical        InteractiveCategory = "medical"
	CategoryLogistics      InteractiveCategory = "logistics"
)

// InteractiveType represents different types of interactive objects
type InteractiveType string

const (
	// Faction control types
	InteractiveTypeFactionBlockpost InteractiveType = "faction_blockpost"

	// Communication types
	InteractiveTypeCommunicationRelay InteractiveType = "communication_relay"

	// Medical types
	InteractiveTypeMedicalStation InteractiveType = "medical_station"

	// Logistics types
	InteractiveTypeLogisticsContainer InteractiveType = "logistics_container"

	// Legacy types (keep for compatibility)
	InteractiveTypeCheckpoint InteractiveType = "checkpoint"
	InteractiveTypeTerminal   InteractiveType = "terminal"
	InteractiveTypeContainer  InteractiveType = "container"
	InteractiveTypeTurret     InteractiveType = "turret"
)

// InteractiveStatus represents the status of an interactive object
type InteractiveStatus string

const (
	InteractiveStatusActive    InteractiveStatus = "active"
	InteractiveStatusInactive  InteractiveStatus = "inactive"
	InteractiveStatusBroken    InteractiveStatus = "broken"
	InteractiveStatusDestroyed InteractiveStatus = "destroyed"
	InteractiveStatusAlert     InteractiveStatus = "alert"
	InteractiveStatusLockdown  InteractiveStatus = "lockdown"
)

// SecurityLevel represents security levels for containers and relays
type SecurityLevel string

const (
	SecurityNone     SecurityLevel = "none"
	SecurityBasic    SecurityLevel = "basic"
	SecurityAdvanced SecurityLevel = "advanced"
	SecurityMilitary SecurityLevel = "military"
)

// EncryptionLevel represents encryption strength
type EncryptionLevel string

const (
	EncryptionNone     EncryptionLevel = "none"
	EncryptionBasic    EncryptionLevel = "basic"
	EncryptionAdvanced EncryptionLevel = "advanced"
	EncryptionMilitary EncryptionLevel = "military"
)

// LootQuality represents quality of loot in containers
type LootQuality string

const (
	LootTrash     LootQuality = "trash"
	LootCommon    LootQuality = "common"
	LootUncommon  LootQuality = "uncommon"
	LootRare      LootQuality = "rare"
	LootEpic      LootQuality = "epic"
	LootLegendary LootQuality = "legendary"
)

// TakeoverMethod represents methods to take control of faction blockposts
type TakeoverMethod string

const (
	TakeoverBribery TakeoverMethod = "bribery"
	TakeoverHacking TakeoverMethod = "hacking"
	TakeoverAssault TakeoverMethod = "assault"
)

// Interactive represents an interactive object in the world
type Interactive struct {
	InteractiveID string              `json:"interactive_id"`
	Version       int                 `json:"version"`
	Name          string              `json:"name"`
	Description   string              `json:"description"`
	Category      InteractiveCategory `json:"category"`
	Type          InteractiveType     `json:"type"`
	Status        InteractiveStatus   `json:"status"`

	// Location data
	WorldLocation string  `json:"world_location"` // Format: "continent/city/district"
	CoordinatesX  float64 `json:"coordinates_x"`
	CoordinatesY  float64 `json:"coordinates_y"`
	CoordinatesZ  float64 `json:"coordinates_z"`

	// Physical properties
	BaseHealth     int  `json:"base_health"`
	CurrentHealth  *int `json:"current_health,omitempty"`
	IsDestructible bool `json:"is_destructible"`
	RespawnTimeSec int  `json:"respawn_time_sec"`

	// Faction control properties
	ControllingFaction   *string `json:"controlling_faction,omitempty"`
	ControlRadiusMeters  *int    `json:"control_radius_meters,omitempty"`
	PriceModifierPercent *int    `json:"price_modifier_percent,omitempty"`
	AccessRequirement    *string `json:"access_requirement,omitempty"`

	// Communication properties
	SignalStrength    *int             `json:"signal_strength,omitempty"`
	EncryptionLevel   *EncryptionLevel `json:"encryption_level,omitempty"`
	JammingResistance *int             `json:"jamming_resistance,omitempty"`
	BandwidthMbps     *int             `json:"bandwidth_mbps,omitempty"`

	// Medical properties
	HealingRatePerSec   *int  `json:"healing_rate_per_sec,omitempty"`
	CyberwareRepair     *bool `json:"cyberware_repair,omitempty"`
	TraumaTeamAvailable *bool `json:"trauma_team_available,omitempty"`

	// Logistics properties
	StorageCapacity *int           `json:"storage_capacity,omitempty"`
	SecurityLevel   *SecurityLevel `json:"security_level,omitempty"`
	LootQuality     *LootQuality   `json:"loot_quality,omitempty"`

	// Control mechanics (for blockposts)
	TakeoverMethod               *TakeoverMethod `json:"takeover_method,omitempty"`
	TakeoverCostEddiesMin        *int            `json:"takeover_cost_eddies_min,omitempty"`
	TakeoverCostEddiesMax        *int            `json:"takeover_cost_eddies_max,omitempty"`
	TakeoverSuccessRateMin       *int            `json:"takeover_success_rate_min,omitempty"`
	TakeoverSuccessRateMax       *int            `json:"takeover_success_rate_max,omitempty"`
	TakeoverDetectionRiskPercent *int            `json:"takeover_detection_risk_percent,omitempty"`
	TakeoverTimeSeconds          *int            `json:"takeover_time_seconds,omitempty"`
	TakeoverAlarmProbability     *int            `json:"takeover_alarm_probability,omitempty"`

	// Runtime state
	IsActive        bool       `json:"is_active"`
	LastInteraction *time.Time `json:"last_interaction,omitempty"`
	SecurityStatus  string     `json:"security_status"`

	// Legacy support
	ContentData map[string]interface{} `json:"content_data,omitempty"`
	Location    string                 `json:"location,omitempty"` // Legacy field

	CreatedAt time.Time `json:"created_at"`
	UpdatedAt time.Time `json:"updated_at"`
}

// ListInteractivesRequest represents a request to list interactives
type ListInteractivesRequest struct {
	Type     *InteractiveType   `json:"type,omitempty"`
	Status   *InteractiveStatus `json:"status,omitempty"`
	Location *string            `json:"location,omitempty"`
	Limit    int                `json:"limit,omitempty"`
	Offset   int                `json:"offset,omitempty"`
}

// InteractiveRepository interface for interactive objects
type InteractiveRepository interface {
	// Legacy methods (keep for compatibility)
	SaveInteractive(ctx context.Context, interactiveID string, version int, name, description, location string, interactiveType InteractiveType, status InteractiveStatus, contentData map[string]interface{}) (*Interactive, error)
	GetInteractives(ctx context.Context, filter *ListInteractivesRequest) ([]Interactive, int, error)
	GetInteractive(ctx context.Context, interactiveID string) (*Interactive, error)
	UpdateInteractive(ctx context.Context, interactiveID string, updates map[string]interface{}) (*Interactive, error)
	DeleteInteractive(ctx context.Context, interactiveID string) error

	// New comprehensive methods for world interactives
	SaveWorldInteractive(ctx context.Context, interactive *Interactive) (*Interactive, error)
	GetWorldInteractives(ctx context.Context, filter *ListWorldInteractivesRequest) ([]Interactive, int, error)
	GetWorldInteractive(ctx context.Context, interactiveID string) (*Interactive, error)
	UpdateWorldInteractive(ctx context.Context, interactiveID string, updates map[string]interface{}) (*Interactive, error)
	DeleteWorldInteractive(ctx context.Context, interactiveID string) error

	// Specialized queries
	GetInteractivesByFaction(ctx context.Context, faction string) ([]Interactive, error)
	GetInteractivesByLocation(ctx context.Context, worldLocation string, radius float64) ([]Interactive, error)
	GetInteractivesByCategory(ctx context.Context, category InteractiveCategory) ([]Interactive, error)
	UpdateInteractiveHealth(ctx context.Context, interactiveID string, newHealth int) error
	UpdateInteractiveFactionControl(ctx context.Context, interactiveID string, faction string) error
}

// ListWorldInteractivesRequest represents a comprehensive request to list world interactives
type ListWorldInteractivesRequest struct {
	Category           *InteractiveCategory `json:"category,omitempty"`
	Type               *InteractiveType     `json:"type,omitempty"`
	Status             *InteractiveStatus   `json:"status,omitempty"`
	ControllingFaction *string              `json:"controlling_faction,omitempty"`
	WorldLocation      *string              `json:"world_location,omitempty"`
	IsActive           *bool                `json:"is_active,omitempty"`
	Limit              int                  `json:"limit,omitempty"`
	Offset             int                  `json:"offset,omitempty"`
}

// InteractiveInteraction represents a player interaction with an interactive object
type InteractiveInteraction struct {
	ID              int64                  `json:"id"`
	LocationID      int64                  `json:"location_id"`
	PlayerID        int64                  `json:"player_id"`
	InteractionType string                 `json:"interaction_type"`
	Success         bool                   `json:"success"`
	DurationSeconds *int                   `json:"duration_seconds,omitempty"`
	ResourcesUsed   int                    `json:"resources_used"`
	FactionImpact   string                 `json:"faction_impact"`
	TelemetryData   map[string]interface{} `json:"telemetry_data"`
	CreatedAt       time.Time              `json:"created_at"`
}

// InteractiveInteractionRepository interface for interaction logging
type InteractiveInteractionRepository interface {
	LogInteraction(ctx context.Context, interaction *InteractiveInteraction) error
	GetInteractionsByPlayer(ctx context.Context, playerID int64, limit int) ([]InteractiveInteraction, error)
	GetInteractionsByLocation(ctx context.Context, locationID int64, limit int) ([]InteractiveInteraction, error)
}
