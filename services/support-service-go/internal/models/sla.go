package models

import (
	"time"

	"github.com/google/uuid"
)

// SLAStatus represents the SLA compliance status
type SLAStatus string

const (
	SLAStatusCompliant SLAStatus = "compliant"
	SLAStatusWarning   SLAStatus = "warning"
	SLAStatusBreached  SLAStatus = "breached"
)

// TicketSLAInfo represents SLA information for a ticket
type TicketSLAInfo struct {
	TicketID             uuid.UUID  `json:"ticket_id"`
	Priority             TicketPriority `json:"priority"`
	CreatedAt            time.Time  `json:"created_at"`
	SLADueDate           time.Time  `json:"sla_due_date"`
	ResponseDeadline     time.Time  `json:"response_deadline"`
	ResolutionDeadline   time.Time  `json:"resolution_deadline"`
	FirstResponseAt      *time.Time `json:"first_response_at,omitempty"`
	ResolvedAt           *time.Time `json:"resolved_at,omitempty"`
	SLAStatus            SLAStatus  `json:"sla_status"`
	TimeToFirstResponse  *string    `json:"time_to_first_response,omitempty"` // ISO 8601 duration
	TimeToResolution     *string    `json:"time_to_resolution,omitempty"`     // ISO 8601 duration
}

// SupportStatsResponse represents statistics for support system
type SupportStatsResponse struct {
	Period                  string            `json:"period"`
	TotalTickets            int               `json:"total_tickets"`
	ResolvedTickets         int               `json:"resolved_tickets"`
	AverageResolutionTime   string            `json:"average_resolution_time"`   // ISO 8601 duration
	AverageFirstResponseTime string           `json:"average_first_response_time"` // ISO 8601 duration
	SLAComplianceRate       float64           `json:"sla_compliance_rate"`
	TicketsByStatus         map[string]int    `json:"tickets_by_status"`
	TicketsByPriority       map[string]int    `json:"tickets_by_priority"`
	TicketsByCategory       map[string]int    `json:"tickets_by_category"`
	AgentPerformance        []AgentPerformance `json:"agent_performance"`
}

// AgentPerformance represents performance metrics for an agent
type AgentPerformance struct {
	AgentID             uuid.UUID `json:"agent_id"`
	Name                string    `json:"name"`
	ResolvedCount       int       `json:"resolved_count"`
	AverageResolutionTime string  `json:"average_resolution_time"` // ISO 8601 duration
	SLAComplianceRate   float64   `json:"sla_compliance_rate"`
}

// TicketQueueResponse represents the ticket queue with statistics
type TicketQueueResponse struct {
	Queue      []Ticket `json:"queue"`
	QueueStats struct {
		TotalWaiting int `json:"total_waiting"`
		UrgentCount  int `json:"urgent_count"`
		HighCount    int `json:"high_count"`
		NormalCount  int `json:"normal_count"`
		LowCount     int `json:"low_count"`
	} `json:"queue_stats"`
}

