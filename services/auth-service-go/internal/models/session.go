package models

import (
	"time"

	"github.com/google/uuid"
)

// SessionStats represents session statistics
type SessionStats struct {
	ActiveSessions              int                `json:"active_sessions"`
	TotalSessions               int                `json:"total_sessions"`
	ExpiredSessions             int                `json:"expired_sessions"`
	SessionsByDevice            map[string]int     `json:"sessions_by_device"`
	RecentActivity24h           int                `json:"recent_activity_24h"`
	AverageSessionDurationHours float64            `json:"average_session_duration_hours"`
}