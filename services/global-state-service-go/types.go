// Issue: #53
package main

import (
	"encoding/json"
	"time"
)

type stateEntry struct {
	Key       string          `json:"key"`
	Category  string          `json:"category"`
	Value     json.RawMessage `json:"value"`
	Version   uint64          `json:"version"`
	UpdatedAt time.Time       `json:"updatedAt"`
}

type stateMutationRequest struct {
	Key             string          `json:"key"`
	Category        string          `json:"category"`
	Value           json.RawMessage `json:"value"`
	ExpectedVersion *uint64         `json:"expectedVersion,omitempty"`
	CorrelationID   string          `json:"correlationId,omitempty"`
}

type batchMutationRequest struct {
	Mutations []stateMutationRequest `json:"mutations"`
}

type stateEventRequest struct {
	EventType     string          `json:"eventType"`
	AggregateID   string          `json:"aggregateId"`
	Payload       json.RawMessage `json:"payload"`
	CorrelationID string          `json:"correlationId,omitempty"`
}

type stateEvent struct {
	ID            string          `json:"id"`
	EventType     string          `json:"eventType"`
	AggregateID   string          `json:"aggregateId"`
	Payload       json.RawMessage `json:"payload"`
	CreatedAt     time.Time       `json:"createdAt"`
	CorrelationID string          `json:"correlationId,omitempty"`
}







