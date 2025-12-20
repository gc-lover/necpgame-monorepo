package models

import (
	"time"

	"github.com/google/uuid"
)

type ApartmentType string

type Apartment struct {
	ID             uuid.UUID              `json:"id" db:"id"`
	OwnerID        uuid.UUID              `json:"owner_id" db:"owner_id"`
	OwnerType      string                 `json:"owner_type" db:"owner_type"`
	ApartmentType  ApartmentType          `json:"apartment_type" db:"apartment_type"`
	Location       string                 `json:"location" db:"location"`
	Price          int64                  `json:"price" db:"price"`
	FurnitureSlots int                    `json:"furniture_slots" db:"furniture_slots"`
	PrestigeScore  int                    `json:"prestige_score" db:"prestige_score"`
	IsPublic       bool                   `json:"is_public" db:"is_public"`
	Guests         []uuid.UUID            `json:"guests" db:"guests"`
	Settings       map[string]interface{} `json:"settings" db:"settings"`
	CreatedAt      time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time              `json:"updated_at" db:"updated_at"`
}

type FurnitureCategory string

type FurnitureItem struct {
	ID            string                 `json:"id" db:"id"`
	Category      FurnitureCategory      `json:"category" db:"category"`
	Name          string                 `json:"name" db:"name"`
	Description   string                 `json:"description" db:"description"`
	Price         int64                  `json:"price" db:"price"`
	PrestigeValue int                    `json:"prestige_value" db:"prestige_value"`
	FunctionBonus map[string]interface{} `json:"function_bonus" db:"function_bonus"`
	CreatedAt     time.Time              `json:"created_at" db:"created_at"`
}

type PlacedFurniture struct {
	ID              uuid.UUID              `json:"id" db:"id"`
	ApartmentID     uuid.UUID              `json:"apartment_id" db:"apartment_id"`
	FurnitureItemID string                 `json:"furniture_item_id" db:"furniture_item_id"`
	Position        map[string]interface{} `json:"position" db:"position"`
	Rotation        map[string]interface{} `json:"rotation" db:"rotation"`
	Scale           map[string]interface{} `json:"scale" db:"scale"`
	CreatedAt       time.Time              `json:"created_at" db:"created_at"`
	UpdatedAt       time.Time              `json:"updated_at" db:"updated_at"`
}

type ApartmentVisit struct {
	ID          uuid.UUID `json:"id" db:"id"`
	ApartmentID uuid.UUID `json:"apartment_id" db:"apartment_id"`
	VisitorID   uuid.UUID `json:"visitor_id" db:"visitor_id"`
	VisitedAt   time.Time `json:"visited_at" db:"visited_at"`
}

type PurchaseApartmentRequest struct {
	CharacterID   uuid.UUID     `json:"character_id"`
	ApartmentType ApartmentType `json:"apartment_type"`
	Location      string        `json:"location"`
}

type PlaceFurnitureRequest struct {
	CharacterID     uuid.UUID              `json:"character_id"`
	FurnitureItemID string                 `json:"furniture_item_id"`
	Position        map[string]interface{} `json:"position"`
	Rotation        map[string]interface{} `json:"rotation"`
	Scale           map[string]interface{} `json:"scale"`
}

type RemoveFurnitureRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	FurnitureID uuid.UUID `json:"furniture_id"`
}

type UpdateApartmentSettingsRequest struct {
	CharacterID uuid.UUID              `json:"character_id"`
	IsPublic    *bool                  `json:"is_public,omitempty"`
	Guests      []uuid.UUID            `json:"guests,omitempty"`
	Settings    map[string]interface{} `json:"settings,omitempty"`
}

type VisitApartmentRequest struct {
	CharacterID uuid.UUID `json:"character_id"`
	ApartmentID uuid.UUID `json:"apartment_id"`
}

type ApartmentListResponse struct {
	Apartments []Apartment `json:"apartments"`
	Total      int         `json:"total"`
}

type FurnitureListResponse struct {
	Items []FurnitureItem `json:"items"`
	Total int             `json:"total"`
}

type PlacedFurnitureListResponse struct {
	Furniture []PlacedFurniture `json:"furniture"`
	Total     int               `json:"total"`
}

type ApartmentDetailResponse struct {
	Apartment         *Apartment             `json:"apartment"`
	Furniture         []PlacedFurniture      `json:"furniture,omitempty"`
	ItemDetails       []FurnitureItem        `json:"item_details,omitempty"`
	FunctionalBonuses map[string]interface{} `json:"functional_bonuses,omitempty"`
}

type PrestigeLeaderboardResponse struct {
	Entries []PrestigeLeaderboardEntry `json:"entries"`
	Total   int                        `json:"total"`
}

type PrestigeLeaderboardEntry struct {
	ApartmentID   uuid.UUID     `json:"apartment_id"`
	OwnerID       uuid.UUID     `json:"owner_id"`
	OwnerName     string        `json:"owner_name"`
	PrestigeScore int           `json:"prestige_score"`
	ApartmentType ApartmentType `json:"apartment_type"`
	Location      string        `json:"location"`
}
