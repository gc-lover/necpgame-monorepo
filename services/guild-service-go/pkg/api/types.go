package api

import (
	"time"
)

// Guild bank types
type GuildBank struct {
	ID             string    `json:"id"`
	GuildID        string    `json:"guildId"`
	Version        int       `json:"version"`
	CurrencyType   string    `json:"currencyType"`
	Amount         int64     `json:"amount"`
	LastTransaction time.Time `json:"lastTransaction"`
}

type GuildBankResponse struct {
	Bank []*GuildBank `json:"bank"`
}

// Guild event types
type GuildEvent struct {
	ID               string     `json:"id"`
	GuildID          string     `json:"guildId"`
	EventType        string     `json:"eventType"`
	Title            string     `json:"title"`
	Description      OptString  `json:"description"`
	ScheduledAt      time.Time  `json:"scheduledAt"`
	DurationMinutes  int        `json:"durationMinutes"`
	MaxParticipants  OptInt     `json:"maxParticipants"`
	CurrentParticipants int     `json:"currentParticipants"`
	Status           string     `json:"status"`
	CreatedBy        string     `json:"createdBy"`
	CreatedAt        OptDateTime `json:"createdAt"`
}

type GuildEventResponse struct {
	Event *GuildEvent `json:"event,omitempty"`
}

type GuildEventListResponse struct {
	Events []*GuildEvent `json:"events"`
}

// Guild relationship types
type GuildRelationship struct {
	ID               string     `json:"id"`
	GuildAID         string     `json:"guildAId"`
	GuildBID         string     `json:"guildBId"`
	RelationshipType string     `json:"relationshipType"`
	EstablishedAt    time.Time  `json:"establishedAt"`
	ExpiresAt        *time.Time `json:"expiresAt,omitempty"`
}

type GuildRelationshipResponse struct {
	Relationship *GuildRelationship `json:"relationship,omitempty"`
}

type GuildRelationshipListResponse struct {
	Relationships []*GuildRelationship `json:"relationships"`
}

// Guild application types
type GuildApplicationResponse struct {
	ID          string     `json:"id"`
	UserID      string     `json:"userId"`
	GuildID     string     `json:"guildId"`
	Message     OptString  `json:"message"`
	Status      string     `json:"status"`
	AppliedAt   OptDateTime `json:"appliedAt"`
	ReviewedAt  OptDateTime `json:"reviewedAt,omitempty"`
	ReviewedBy  OptString  `json:"reviewedBy,omitempty"`
	CreatedAt   OptDateTime `json:"createdAt"`
	UpdatedAt   OptDateTime `json:"updatedAt"`
}