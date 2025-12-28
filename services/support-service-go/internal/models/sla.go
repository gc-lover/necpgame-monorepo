// Issue: #1489 - Support SLA Service: ogen handlers implementation
// PERFORMANCE: SLA model with optimized struct alignment for memory efficiency

package models

import (
	"time"

	"github.com/google/uuid"
)

// SLAPriority defines SLA priorities with time targets
type SLAPriority struct {
	Priority              string        `json:"priority"`
	FirstResponseTarget   time.Duration `json:"firstResponseTarget"`   // Target time for first response
	ResolutionTarget      time.Duration `json:"resolutionTarget"`      // Target time for resolution
	FirstResponsePenalty  int           `json:"firstResponsePenalty"`  // Penalty score for missing first response
	ResolutionPenalty     int           `json:"resolutionPenalty"`     // Penalty score for missing resolution
}

// SLAStatus represents current SLA status for a ticket
type SLAStatus struct {
	TicketID               uuid.UUID   `json:"ticketId"`
	Priority               string      `json:"priority"`
	FirstResponseTarget    time.Time   `json:"firstResponseTarget"`
	FirstResponseActual    *time.Time  `json:"firstResponseActual,omitempty"`
	ResolutionTarget       time.Time   `json:"resolutionTarget"`
	ResolutionActual       *time.Time  `json:"resolutionActual,omitempty"`
	FirstResponseSLAMet    *bool       `json:"firstResponseSlaMet,omitempty"`
	ResolutionSLAMet       *bool       `json:"resolutionSlaMet,omitempty"`
	TimeUntilFirstResponse *int        `json:"timeUntilFirstResponse,omitempty"` // seconds remaining
	TimeUntilResolution    *int        `json:"timeUntilResolution,omitempty"`    // seconds remaining
	CreatedAt              time.Time   `json:"createdAt"`
	UpdatedAt              time.Time   `json:"updatedAt"`
}

// SLAViolation represents a SLA violation
type SLAViolation struct {
	TicketID            uuid.UUID  `json:"ticketId"`
	TicketNumber        string     `json:"ticketNumber"`
	Priority            string     `json:"priority"`
	ViolationType       string     `json:"violationType"` // FIRST_RESPONSE or RESOLUTION
	TargetTime          time.Time  `json:"targetTime"`
	ActualTime          *time.Time `json:"actualTime,omitempty"`
	ViolationDuration   int        `json:"violationDurationSeconds"` // seconds overdue
	CreatedAt           time.Time  `json:"createdAt"`
}

// DefaultSLAPriorities defines standard SLA priorities
var DefaultSLAPriorities = map[string]SLAPriority{
	"LOW": {
		Priority:             "LOW",
		FirstResponseTarget:  48 * time.Hour, // 2 days
		ResolutionTarget:     7 * 24 * time.Hour, // 7 days
		FirstResponsePenalty: 1,
		ResolutionPenalty:    2,
	},
	"NORMAL": {
		Priority:             "NORMAL",
		FirstResponseTarget:  24 * time.Hour, // 1 day
		ResolutionTarget:     5 * 24 * time.Hour, // 5 days
		FirstResponsePenalty: 2,
		ResolutionPenalty:    4,
	},
	"HIGH": {
		Priority:             "HIGH",
		FirstResponseTarget:  4 * time.Hour, // 4 hours
		ResolutionTarget:     24 * time.Hour, // 1 day
		FirstResponsePenalty: 5,
		ResolutionPenalty:    10,
	},
	"URGENT": {
		Priority:             "URGENT",
		FirstResponseTarget:  1 * time.Hour, // 1 hour
		ResolutionTarget:     4 * time.Hour, // 4 hours
		FirstResponsePenalty: 10,
		ResolutionPenalty:    20,
	},
	"CRITICAL": {
		Priority:             "CRITICAL",
		FirstResponseTarget:  30 * time.Minute, // 30 minutes
		ResolutionTarget:     2 * time.Hour, // 2 hours
		FirstResponsePenalty: 20,
		ResolutionPenalty:    50,
	},
}

// Issue: #1489 - Support SLA Service: ogen handlers implementation
