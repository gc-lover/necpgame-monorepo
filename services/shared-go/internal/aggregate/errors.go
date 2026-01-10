// Package aggregate provides error definitions for Event Sourcing Aggregate operations
// Issue: #2217 - Event Sourcing Aggregate Implementation
// Agent: Backend Agent
package aggregate

import "errors"

// Common errors for event sourcing aggregate operations
var (
	// ErrAggregateIDMismatch indicates that an event belongs to a different aggregate
	ErrAggregateIDMismatch = errors.New("event aggregate ID does not match aggregate")

	// ErrAggregateNotFound indicates that the requested aggregate was not found
	ErrAggregateNotFound = errors.New("aggregate not found")

	// ErrAggregateVersionConflict indicates optimistic concurrency conflict
	ErrAggregateVersionConflict = errors.New("aggregate version conflict")

	// ErrEventVersionInvalid indicates invalid event version
	ErrEventVersionInvalid = errors.New("invalid event version")

	// ErrEventStoreUnavailable indicates event store is not available
	ErrEventStoreUnavailable = errors.New("event store unavailable")

	// ErrSnapshotNotSupported indicates snapshot functionality not implemented
	ErrSnapshotNotSupported = errors.New("snapshot not supported for this aggregate type")

	// ErrEventSerializationFailed indicates event serialization/deserialization failure
	ErrEventSerializationFailed = errors.New("event serialization failed")

	// ErrInvalidAggregateState indicates aggregate is in invalid state
	ErrInvalidAggregateState = errors.New("invalid aggregate state")

	// ErrConcurrencyViolation indicates concurrent modification detected
	ErrConcurrencyViolation = errors.New("concurrency violation detected")

	// ErrEventReplayFailed indicates failure during event replay
	ErrEventReplayFailed = errors.New("event replay failed")
)