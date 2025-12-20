// Package models Issue: #2224
package models

import (
	"time"

	"github.com/google/uuid"
)

// CreateWorldEventRequest represents request to create a world event
type CreateWorldEventRequest struct {
	Title            string              `json:"title"`
	Description      string              `json:"description,omitempty"`
	Type             WorldEventType      `json:"type"`
	Scale            WorldEventScale     `json:"scale"`
	Frequency        WorldEventFrequency `json:"frequency"`
	StartTime        *time.Time          `json:"start_time,omitempty"`
	Duration         *int                `json:"duration,omitempty"`
	TargetRegions    []string            `json:"target_regions,omitempty"`
	TargetFactions   []uuid.UUID         `json:"target_factions,omitempty"`
	Prerequisites    []uuid.UUID         `json:"prerequisites,omitempty"`
	CooldownDuration *int                `json:"cooldown_duration,omitempty"`
	MaxConcurrent    *int                `json:"max_concurrent,omitempty"`
}

// UpdateWorldEventRequest represents request to update a world event
type UpdateWorldEventRequest struct {
	Title            string      `json:"title,omitempty"`
	Description      string      `json:"description,omitempty"`
	StartTime        *time.Time  `json:"start_time,omitempty"`
	Duration         *int        `json:"duration,omitempty"`
	TargetRegions    []string    `json:"target_regions,omitempty"`
	TargetFactions   []uuid.UUID `json:"target_factions,omitempty"`
	CooldownDuration *int        `json:"cooldown_duration,omitempty"`
	MaxConcurrent    *int        `json:"max_concurrent,omitempty"`
}

// CreateEventEffectRequest represents request to create an event effect
type CreateEventEffectRequest struct {
	TargetSystem TargetSystem           `json:"target_system"`
	EffectType   string                 `json:"effect_type"`
	Parameters   map[string]interface{} `json:"parameters"`
	StartTime    time.Time              `json:"start_time"`
	EndTime      time.Time              `json:"end_time"`
}

// UpdateEventEffectRequest represents request to update an event effect
type UpdateEventEffectRequest struct {
	Parameters map[string]interface{} `json:"parameters,omitempty"`
	StartTime  time.Time              `json:"start_time,omitempty"`
	EndTime    time.Time              `json:"end_time,omitempty"`
	IsActive   *bool                  `json:"is_active,omitempty"`
}

// CreateAnnouncementRequest represents request to create an event announcement
type CreateAnnouncementRequest struct {
	Title          string               `json:"title"`
	Message        string               `json:"message"`
	Type           AnnouncementType     `json:"type"`
	TargetAudience TargetAudience       `json:"target_audience,omitempty"`
	Priority       AnnouncementPriority `json:"priority"`
	ExpiresAt      *time.Time           `json:"expires_at,omitempty"`
}

// WorldEventsListResponse represents paginated list of world events
type WorldEventsListResponse struct {
	Events []WorldEvent `json:"events"`
	Total  int          `json:"total"`
	Limit  int          `json:"limit,omitempty"`
	Offset int          `json:"offset,omitempty"`
}

// WorldEventEffectsResponse represents list of effects for an event
type WorldEventEffectsResponse struct {
	Effects []EventEffect `json:"effects"`
	EventID uuid.UUID     `json:"event_id"`
	Count   int           `json:"count"`
}

// HealthResponse represents health check response
type HealthResponse struct {
	Status    string    `json:"status"`
	Timestamp time.Time `json:"timestamp"`
	Version   string    `json:"version"`
	Service   string    `json:"service"`
	Uptime    string    `json:"uptime,omitempty"`
}

// ErrorResponse represents error response
type ErrorResponse struct {
	Error string `json:"error"`
	Code  int    `json:"code"`
}

// SuccessResponse represents success response
type SuccessResponse struct {
	Message string      `json:"message"`
	Data    interface{} `json:"data,omitempty"`
}
