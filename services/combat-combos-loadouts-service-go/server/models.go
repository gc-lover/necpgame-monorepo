// Combat Combos Loadouts Service Models
// Issue: #141890005

package server

import (
	"time"

	"github.com/google/uuid"
)

// BACKEND NOTE: Struct field alignment optimized for memory efficiency
// Expected memory per ComboLoadout: ~136 bytes (large fields first)

// ComboLoadout represents a character's combo loadout
type ComboLoadout struct {
	ID          uuid.UUID `json:"id" db:"id"`
	CharacterID uuid.UUID `json:"character_id" db:"character_id"`

	// Active combos (array of combo IDs)
	ActiveCombos []uuid.UUID `json:"active_combos" db:"active_combos"`

	// Preferences (nested object)
	Preferences ComboLoadoutPreferences `json:"preferences" db:"preferences"`

	// Metadata
	CreatedAt time.Time `json:"created_at" db:"created_at"`
	UpdatedAt time.Time `json:"updated_at" db:"updated_at"`
}

// ComboLoadoutPreferences represents loadout preferences
type ComboLoadoutPreferences struct {
	AutoActivate    bool        `json:"auto_activate" db:"auto_activate"`
	PriorityOrder   []uuid.UUID `json:"priority_order" db:"priority_order"`
	MaxActiveCombos int         `json:"max_active_combos" db:"max_active_combos"`
}

// UpdateLoadoutRequest represents a request to update loadout
type UpdateLoadoutRequest struct {
	CharacterID  uuid.UUID               `json:"character_id"`
	ActiveCombos []uuid.UUID             `json:"active_combos"`
	Preferences  ComboLoadoutPreferences `json:"preferences"`
}

// LoadoutResponse represents the response for loadout operations
type LoadoutResponse struct {
	Success bool           `json:"success"`
	Loadout *ComboLoadout  `json:"loadout,omitempty"`
	Error   *ErrorResponse `json:"error,omitempty"`
}

// ErrorResponse represents an error response
type ErrorResponse struct {
	Code    string                 `json:"code"`
	Message string                 `json:"message"`
	Details map[string]interface{} `json:"details,omitempty"`
}

// PaginationParams Pagination parameters for list operations
type PaginationParams struct {
	Limit  int `json:"limit,omitempty"`
	Offset int `json:"offset,omitempty"`
}

// ListLoadoutsResponse represents paginated list of loadouts
type ListLoadoutsResponse struct {
	Loadouts []ComboLoadout `json:"loadouts"`
	Total    int            `json:"total"`
	Limit    int            `json:"limit"`
	Offset   int            `json:"offset"`
}
