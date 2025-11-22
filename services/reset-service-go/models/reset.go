package models

import (
	"time"

	"github.com/google/uuid"
)

type ResetType string

const (
	ResetTypeDaily  ResetType = "daily"
	ResetTypeWeekly ResetType = "weekly"
)

type ResetStatus string

const (
	ResetStatusPending   ResetStatus = "pending"
	ResetStatusRunning   ResetStatus = "running"
	ResetStatusCompleted ResetStatus = "completed"
	ResetStatusFailed    ResetStatus = "failed"
)

type ResetRecord struct {
	ID          uuid.UUID   `json:"id" db:"id"`
	Type        ResetType   `json:"type" db:"type"`
	Status      ResetStatus `json:"status" db:"status"`
	StartedAt   time.Time   `json:"started_at" db:"started_at"`
	CompletedAt *time.Time  `json:"completed_at,omitempty" db:"completed_at"`
	Error       *string     `json:"error,omitempty" db:"error"`
	Metadata    map[string]interface{} `json:"metadata,omitempty" db:"metadata"`
}

type ResetStats struct {
	LastDailyReset  *time.Time `json:"last_daily_reset,omitempty"`
	LastWeeklyReset *time.Time `json:"last_weekly_reset,omitempty"`
	NextDailyReset  time.Time  `json:"next_daily_reset"`
	NextWeeklyReset time.Time  `json:"next_weekly_reset"`
}

type TriggerResetRequest struct {
	Type ResetType `json:"type"`
}

type ResetListResponse struct {
	Resets []ResetRecord `json:"resets"`
	Total  int           `json:"total"`
}

