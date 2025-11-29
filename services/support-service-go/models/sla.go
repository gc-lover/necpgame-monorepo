package models

import (
	"time"

	"github.com/google/uuid"
)

type TicketSLAStatus struct {
	TicketID                    uuid.UUID  `json:"ticket_id"`
	Priority                    string     `json:"priority"`
	FirstResponseTarget         time.Time  `json:"first_response_target"`
	FirstResponseActual         *time.Time `json:"first_response_actual,omitempty"`
	ResolutionTarget            time.Time  `json:"resolution_target"`
	ResolutionActual            *time.Time `json:"resolution_actual,omitempty"`
	FirstResponseSLAMet         *bool      `json:"first_response_sla_met,omitempty"`
	ResolutionSLAMet            *bool      `json:"resolution_sla_met,omitempty"`
	TimeUntilFirstResponseTarget *int      `json:"time_until_first_response_target,omitempty"`
	TimeUntilResolutionTarget   *int       `json:"time_until_resolution_target,omitempty"`
}

type SLAViolationType string

const (
	SLAViolationTypeFirstResponse SLAViolationType = "FIRST_RESPONSE"
	SLAViolationTypeResolution   SLAViolationType = "RESOLUTION"
)

type SLAViolation struct {
	TicketID                uuid.UUID         `json:"ticket_id"`
	TicketNumber            string             `json:"ticket_number"`
	Priority                string             `json:"priority"`
	ViolationType           SLAViolationType   `json:"violation_type"`
	TargetTime              time.Time          `json:"target_time"`
	ActualTime              *time.Time         `json:"actual_time,omitempty"`
	ViolationDurationSeconds *int              `json:"violation_duration_seconds,omitempty"`
}

type SLAViolationsResponse struct {
	Items  []SLAViolation `json:"items"`
	Total  int            `json:"total"`
	Limit  int            `json:"limit"`
	Offset int            `json:"offset"`
}

