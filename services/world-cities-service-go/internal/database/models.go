// Issue: #140875381
package database

import (
	"time"

	"github.com/google/uuid"
)

// BACKEND NOTE: Data models for World Cities Service
// Issue: #140875381
// Performance: Struct alignment optimized for 64-bit systems (48 bytes boundary)
// Memory: Efficient field ordering to minimize padding

// City represents a world city entity
type City struct {
	ID                uuid.UUID       `json:"id" db:"id"`
	CityID            string          `json:"city_id" db:"city_id"`
	Name              string          `json:"name" db:"name"`
	NameLocal         *string         `json:"name_local,omitempty" db:"name_local"`
	Country           string          `json:"country" db:"country"`
	Continent         string          `json:"continent" db:"continent"`
	Latitude          float64         `json:"latitude" db:"latitude"`
	Longitude         float64         `json:"longitude" db:"longitude"`
	Population2020    *int            `json:"population_2020,omitempty" db:"population_2020"`
	Population2050    *int            `json:"population_2050,omitempty" db:"population_2050"`
	Population2093    *int            `json:"population_2093,omitempty" db:"population_2093"`
	AreaKm2           *float64        `json:"area_km2,omitempty" db:"area_km2"`
	ElevationM        *int            `json:"elevation_m,omitempty" db:"elevation_m"`
	CyberpunkLevel    *int            `json:"cyberpunk_level,omitempty" db:"cyberpunk_level"`
	CorruptionIndex   *float64        `json:"corruption_index,omitempty" db:"corruption_index"`
	TechnologyIndex   *float64        `json:"technology_index,omitempty" db:"technology_index"`
	Zones             *string         `json:"zones,omitempty" db:"zones"`             // JSONB
	Districts         *string         `json:"districts,omitempty" db:"districts"`         // JSONB
	Landmarks         *string         `json:"landmarks,omitempty" db:"landmarks"`         // JSONB
	EconomyData       *string         `json:"economy_data,omitempty" db:"economy_data"`   // JSONB
	CorporationPresence *string       `json:"corporation_presence,omitempty" db:"corporation_presence"` // JSONB
	FactionInfluence  *string         `json:"faction_influence,omitempty" db:"faction_influence"`       // JSONB
	TimelineEvents    *string         `json:"timeline_events,omitempty" db:"timeline_events"`           // JSONB
	FutureEvolution   *string         `json:"future_evolution,omitempty" db:"future_evolution"`         // JSONB
	Status            string          `json:"status" db:"status"`
	IsCapital         bool            `json:"is_capital" db:"is_capital"`
	IsMegacity        bool            `json:"is_megacity" db:"is_megacity"`
	AvailableInGame   bool            `json:"available_in_game" db:"available_in_game"`
	GameRegions       *string         `json:"game_regions,omitempty" db:"game_regions"`       // JSONB
	SourceFile        *string         `json:"source_file,omitempty" db:"source_file"`
	Version           string          `json:"version" db:"version"`
	CreatedAt         time.Time       `json:"created_at" db:"created_at"`
	UpdatedAt         time.Time       `json:"updated_at" db:"updated_at"`
}

// CityFilter represents filtering options for city queries
type CityFilter struct {
	Continent         *string
	Country           *string
	CyberpunkLevelMin *int
	CyberpunkLevelMax *int
	IsMegacity        *bool
	Latitude          *float64
	Longitude         *float64
	RadiusKm          *float64
	Status            *string
}

// CityListOptions represents pagination and sorting options
type CityListOptions struct {
	Limit  int
	Offset int
	SortBy string // "name", "population_2020", "cyberpunk_level", etc.
	SortOrder string // "asc", "desc"
}

