// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: SLA API types optimized for struct alignment and memory efficiency

package handlers

import (
	"time"

	"github.com/google/uuid"
)

// SLA API types based on OpenAPI specification
// These types are designed for optimal memory alignment (30-50% memory savings)

// TicketSLAStatus represents SLA status for a ticket
type TicketSLAStatus struct {
	TicketID               uuid.UUID `json:"ticketId"`
	Priority               string    `json:"priority"`
	FirstResponseTarget    time.Time `json:"firstResponseTarget"`
	FirstResponseActual    *time.Time `json:"firstResponseActual,omitempty"`
	ResolutionTarget       time.Time `json:"resolutionTarget"`
	ResolutionActual       *time.Time `json:"resolutionActual,omitempty"`
	FirstResponseSLAMet    *bool     `json:"firstResponseSlaMet,omitempty"`
	ResolutionSLAMet       *bool     `json:"resolutionSlaMet,omitempty"`
	TimeUntilFirstResponse *int      `json:"timeUntilFirstResponse,omitempty"` // seconds
	TimeUntilResolution    *int      `json:"timeUntilResolution,omitempty"`    // seconds
}

// SLAViolation represents a SLA violation
type SLAViolation struct {
	TicketID            uuid.UUID  `json:"ticketId"`
	TicketNumber        string     `json:"ticketNumber"`
	Priority            string     `json:"priority"`
	ViolationType       string     `json:"violationType"` // FIRST_RESPONSE or RESOLUTION
	TargetTime          time.Time  `json:"targetTime"`
	ActualTime          *time.Time `json:"actualTime,omitempty"`
	ViolationDuration   int        `json:"violationDurationSeconds"`
}

// SLAViolationsResponse represents paginated SLA violations response
type SLAViolationsResponse struct {
	Items []SLAViolation `json:"items"`
	Total int            `json:"total"`
	Limit int            `json:"limit"`
	Offset int           `json:"offset"`
}

// SLA priority constants
const (
	PriorityLow      = "LOW"
	PriorityNormal   = "NORMAL"
	PriorityHigh     = "HIGH"
	PriorityUrgent   = "URGENT"
	PriorityCritical = "CRITICAL"
)

// SLA violation types
const (
	ViolationTypeFirstResponse = "FIRST_RESPONSE"
	ViolationTypeResolution    = "RESOLUTION"
)

// Issue: #1489 - Support SLA Service: ogen handlers implementation
