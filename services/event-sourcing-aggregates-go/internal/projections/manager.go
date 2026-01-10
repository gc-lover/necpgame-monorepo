// Projection Manager for CQRS read models
// Issue: #2217
// Agent: Backend Agent
package projections

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	"necpgame/services/event-sourcing-aggregates-go/internal/events"
)

// Projection defines interface for read model projections
type Projection interface {
	// GetName returns projection name
	GetName() string

	// GetLastProcessedEvent returns last processed event version for aggregate
	GetLastProcessedEvent(aggregateID string) int64

	// Project processes a domain event and updates read model
	Project(ctx context.Context, event events.DomainEvent) error

	// Reset resets the projection to initial state
	Reset(ctx context.Context) error
}

// ProjectionManager manages all projections
type ProjectionManager struct {
	projections []Projection
	eventBus    *events.EventBus
	logger      *zap.Logger
	running     bool
	wg          sync.WaitGroup
	mu          sync.RWMutex
}

// NewProjectionManager creates a new projection manager
func NewProjectionManager(eventBus *events.EventBus, logger *zap.Logger) *ProjectionManager {
	return &ProjectionManager{
		projections: make([]Projection, 0),
		eventBus:    eventBus,
		logger:      logger,
	}
}

// RegisterProjection registers a projection
func (m *ProjectionManager) RegisterProjection(projection Projection) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.projections = append(m.projections, projection)

	// Subscribe projection to all events it handles
	eventHandler := &projectionEventHandler{
		projection: projection,
		logger:     m.logger,
	}

	// For now, subscribe to all known event types
	eventTypes := []string{
		"PlayerCreated",
		"PlayerLevelGained",
		"PlayerItemEquipped",
	}

	for _, eventType := range eventTypes {
		m.eventBus.Subscribe(eventType, eventHandler)
	}

	m.logger.Info("Registered projection",
		zap.String("projection_name", projection.GetName()))
}

// Start starts the projection manager
func (m *ProjectionManager) Start(ctx context.Context) error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.running {
		return fmt.Errorf("projection manager already running")
	}

	m.running = true
	m.logger.Info("Starting projection manager")

	// Start background processing if needed
	m.wg.Add(1)
	go m.run(ctx)

	return nil
}

// Stop stops the projection manager
func (m *ProjectionManager) Stop() {
	m.mu.Lock()
	m.running = false
	m.mu.Unlock()

	m.logger.Info("Stopping projection manager")
	m.wg.Wait()
}

// run runs the projection manager background processing
func (m *ProjectionManager) run(ctx context.Context) {
	defer m.wg.Done()

	ticker := time.NewTicker(30 * time.Second)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			m.logger.Info("Projection manager context cancelled")
			return
		case <-ticker.C:
			m.processPendingProjections(ctx)
		}
	}
}

// processPendingProjections processes any pending projection work
func (m *ProjectionManager) processPendingProjections(ctx context.Context) {
	m.mu.RLock()
	projections := make([]Projection, len(m.projections))
	copy(projections, m.projections)
	m.mu.RUnlock()

	for _, projection := range projections {
		// Check projection health, rebuild if needed, etc.
		// This is a placeholder for more advanced projection management
		m.logger.Debug("Checking projection health",
			zap.String("projection_name", projection.GetName()))
	}
}

// GetProjections returns all registered projections
func (m *ProjectionManager) GetProjections() []Projection {
	m.mu.RLock()
	defer m.mu.RUnlock()

	result := make([]Projection, len(m.projections))
	copy(result, m.projections)
	return result
}

// ResetProjection resets a specific projection
func (m *ProjectionManager) ResetProjection(ctx context.Context, projectionName string) error {
	m.mu.RLock()
	defer m.mu.RUnlock()

	for _, projection := range m.projections {
		if projection.GetName() == projectionName {
			m.logger.Info("Resetting projection",
				zap.String("projection_name", projectionName))

			return projection.Reset(ctx)
		}
	}

	return fmt.Errorf("projection not found: %s", projectionName)
}

// GetProjectionStats returns statistics about projections
func (m *ProjectionManager) GetProjectionStats() map[string]interface{} {
	m.mu.RLock()
	defer m.mu.RUnlock()

	stats := make(map[string]interface{})
	stats["projection_count"] = len(m.projections)
	stats["running"] = m.running

	projectionNames := make([]string, len(m.projections))
	for i, p := range m.projections {
		projectionNames[i] = p.GetName()
	}
	stats["projections"] = projectionNames

	return stats
}

// projectionEventHandler adapts projections to event handlers
type projectionEventHandler struct {
	projection Projection
	logger     *zap.Logger
}

// Handle processes an event through the projection
func (h *projectionEventHandler) Handle(ctx context.Context, event events.DomainEvent) error {
	start := time.Now()

	err := h.projection.Project(ctx, event)
	duration := time.Since(start)

	if err != nil {
		h.logger.Error("Projection failed",
			zap.Error(err),
			zap.String("projection_name", h.projection.GetName()),
			zap.String("event_type", event.GetEventType()),
			zap.String("event_id", event.GetEventID().String()),
			zap.String("aggregate_id", event.GetAggregateID().String()),
			zap.Duration("duration", duration))
		return err
	}

	h.logger.Debug("Projection completed",
		zap.String("projection_name", h.projection.GetName()),
		zap.String("event_type", event.GetEventType()),
		zap.String("event_id", event.GetEventID().String()),
		zap.String("aggregate_id", event.GetAggregateID().String()),
		zap.Duration("duration", duration))

	return nil
}

// PlayerReadModelProjection implements a player read model
type PlayerReadModelProjection struct {
	readModel map[string]interface{} // In-memory for demo, would be Redis/PostgreSQL
	lastEvents map[string]int64      // aggregate_id -> last_version
	mu         sync.RWMutex
}

// NewPlayerReadModelProjection creates a new player read model projection
func NewPlayerReadModelProjection() *PlayerReadModelProjection {
	return &PlayerReadModelProjection{
		readModel:  make(map[string]interface{}),
		lastEvents: make(map[string]int64),
	}
}

// GetName returns projection name
func (p *PlayerReadModelProjection) GetName() string {
	return "PlayerReadModel"
}

// GetLastProcessedEvent returns last processed event version
func (p *PlayerReadModelProjection) GetLastProcessedEvent(aggregateID string) int64 {
	p.mu.RLock()
	defer p.mu.RUnlock()

	return p.lastEvents[aggregateID]
}

// Project processes domain events to build read model
func (p *PlayerReadModelProjection) Project(ctx context.Context, event events.DomainEvent) error {
	p.mu.Lock()
	defer p.mu.Unlock()

	aggregateID := event.GetAggregateID().String()

	// Check if this event was already processed
	lastVersion := p.lastEvents[aggregateID]
	if int64(event.GetVersion()) <= lastVersion {
		return nil // Already processed
	}

	// Initialize read model for aggregate if needed
	if _, exists := p.readModel[aggregateID]; !exists {
		p.readModel[aggregateID] = make(map[string]interface{})
	}

	readModel := p.readModel[aggregateID].(map[string]interface{})

	// Apply event to read model
	switch e := event.(type) {
	case *events.PlayerCreatedEvent:
		readModel["player_id"] = e.PlayerID.String()
		readModel["username"] = e.Username
		readModel["email"] = e.Email
		readModel["level"] = 1
		readModel["experience"] = 0
		readModel["created_at"] = e.CreatedAt

	case *events.PlayerLevelGainedEvent:
		readModel["level"] = e.NewLevel
		readModel["experience"] = e.Experience
		readModel["last_level_up"] = e.GainedAt

	case *events.PlayerItemEquippedEvent:
		equipment := readModel["equipment"]
		if equipment == nil {
			equipment = make(map[string]interface{})
			readModel["equipment"] = equipment
		}
		equipment.(map[string]interface{})[e.Slot] = e.ItemID.String()
	}

	// Update last processed version
	p.lastEvents[aggregateID] = int64(event.GetVersion())

	return nil
}

// Reset resets the projection
func (p *PlayerReadModelProjection) Reset(ctx context.Context) error {
	p.mu.Lock()
	defer p.mu.Unlock()

	p.readModel = make(map[string]interface{})
	p.lastEvents = make(map[string]int64)

	return nil
}

// GetReadModel returns the current read model for an aggregate
func (p *PlayerReadModelProjection) GetReadModel(aggregateID string) (map[string]interface{}, bool) {
	p.mu.RLock()
	defer p.mu.RUnlock()

	model, exists := p.readModel[aggregateID]
	if !exists {
		return nil, false
	}

	// Return copy to prevent external modification
	result := make(map[string]interface{})
	for k, v := range model.(map[string]interface{}) {
		result[k] = v
	}

	return result, true
}