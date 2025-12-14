// Issue: #1841-#1844 - Interactive objects models
package models

import (
	"context"
	"time"
)

// InteractiveType represents different types of interactive objects
type InteractiveType string

const (
	InteractiveTypeCheckpoint InteractiveType = "checkpoint"
	InteractiveTypeTerminal   InteractiveType = "terminal"
	InteractiveTypeContainer  InteractiveType = "container"
	InteractiveTypeTurret     InteractiveType = "turret"
)

// InteractiveStatus represents the status of an interactive object
type InteractiveStatus string

const (
	InteractiveStatusActive   InteractiveStatus = "active"
	InteractiveStatusInactive InteractiveStatus = "inactive"
	InteractiveStatusBroken   InteractiveStatus = "broken"
)

// Interactive represents an interactive object in the world
type Interactive struct {
	InteractiveID string                 `json:"interactive_id"`
	Version       int                    `json:"version"`
	Name          string                 `json:"name"`
	Description   string                 `json:"description"`
	Location      string                 `json:"location"`
	Type          InteractiveType        `json:"type"`
	Status        InteractiveStatus      `json:"status"`
	ContentData   map[string]interface{} `json:"content_data"`
	CreatedAt     time.Time              `json:"created_at"`
	UpdatedAt     time.Time              `json:"updated_at"`
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
	SaveInteractive(ctx context.Context, interactiveID string, version int, name, description, location string, interactiveType InteractiveType, status InteractiveStatus, contentData map[string]interface{}) (*Interactive, error)
	GetInteractives(ctx context.Context, filter *ListInteractivesRequest) ([]Interactive, int, error)
	GetInteractive(ctx context.Context, interactiveID string) (*Interactive, error)
	UpdateInteractive(ctx context.Context, interactiveID string, updates map[string]interface{}) (*Interactive, error)
	DeleteInteractive(ctx context.Context, interactiveID string) error
}
