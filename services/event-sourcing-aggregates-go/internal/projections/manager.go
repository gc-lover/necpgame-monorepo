// Issue: #2217
// PERFORMANCE: Optimized projection manager for CQRS read models
package projections

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/store"
)

// Projection defines the interface for read model projections
type Projection interface {
	// Name returns the projection name
	Name() string

	// HandleEvent processes an event for the projection
	HandleEvent(ctx context.Context, event store.EventEnvelope) error

	// GetPosition returns the current event position
	GetPosition() int64

	// SetPosition sets the event position
	SetPosition(position int64)

	// Reset resets the projection state
	Reset(ctx context.Context) error
}

// Manager manages projection lifecycle and event processing
type Manager struct {
	projections map[string]Projection
	eventStore  *store.PostgreSQLEventStore
	logger      *zap.Logger
	mu          sync.RWMutex
	running     bool
	stopChan    chan struct{}
}

// NewManager creates a new projection manager
func NewManager(logger *zap.Logger) *Manager {
	return &Manager{
		projections: make(map[string]Projection),
		logger:      logger,
		stopChan:    make(chan struct{}),
	}
}

// RegisterProjection registers a projection
func (m *Manager) RegisterProjection(name string, projection Projection) error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.running {
		return fmt.Errorf("cannot register projection while manager is running")
	}

	if _, exists := m.projections[name]; exists {
		return fmt.Errorf("projection %s already registered", name)
	}

	m.projections[name] = projection
	m.logger.Info("Projection registered",
		zap.String("projection_name", name),
		zap.String("projection_type", fmt.Sprintf("%T", projection)))

	return nil
}

// SetEventStore sets the event store for projections
func (m *Manager) SetEventStore(eventStore *store.PostgreSQLEventStore) {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.running {
		m.logger.Warn("Cannot change event store while manager is running")
		return
	}

	m.eventStore = eventStore
	m.logger.Info("Event store set for projections")
}

// Start starts all projections
func (m *Manager) Start(ctx context.Context) error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if m.running {
		return fmt.Errorf("projection manager already running")
	}

	if m.eventStore == nil {
		return fmt.Errorf("event store not set")
	}

	m.running = true

	// Start each projection in a separate goroutine
	for name, projection := range m.projections {
		go m.runProjection(ctx, name, projection)
	}

	m.logger.Info("Projection manager started",
		zap.Int("projections_count", len(m.projections)))

	return nil
}

// Stop stops all projections
func (m *Manager) Stop() error {
	m.mu.Lock()
	defer m.mu.Unlock()

	if !m.running {
		return nil
	}

	m.running = false
	close(m.stopChan)

	m.logger.Info("Projection manager stopped")
	return nil
}

// runProjection runs a single projection
func (m *Manager) runProjection(ctx context.Context, name string, projection Projection) {
	defer func() {
		if r := recover(); r != nil {
			m.logger.Error("Projection panicked",
				zap.String("projection_name", name),
				zap.Any("panic", r))
		}
	}()

	m.logger.Info("Starting projection",
		zap.String("projection_name", name),
		zap.Int64("current_position", projection.GetPosition()))

	ticker := time.NewTicker(5 * time.Second) // Poll interval
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			m.logger.Info("Projection stopped",
				zap.String("projection_name", name))
			return
		case <-m.stopChan:
			m.logger.Info("Projection stopped",
				zap.String("projection_name", name))
			return
		case <-ticker.C:
			if err := m.processPendingEvents(ctx, name, projection); err != nil {
				m.logger.Error("Failed to process pending events",
					zap.String("projection_name", name),
					zap.Error(err))
				time.Sleep(10 * time.Second) // Backoff on error
			}
		}
	}
}

// processPendingEvents processes new events for a projection
func (m *Manager) processPendingEvents(ctx context.Context, name string, projection Projection) error {
	currentPosition := projection.GetPosition()

	// Get events after current position
	// In a real implementation, we would have a way to get events by position
	// For now, we'll simulate this

	// This is a simplified implementation
	// In production, you would:
	// 1. Query events where global_position > currentPosition
	// 2. Process them in order
	// 3. Update the projection position

	m.logger.Debug("Processing pending events",
		zap.String("projection_name", name),
		zap.Int64("current_position", currentPosition))

	return nil
}

// GetProjection returns a projection by name
func (m *Manager) GetProjection(name string) (Projection, bool) {
	m.mu.RLock()
	defer m.mu.RUnlock()

	projection, exists := m.projections[name]
	return projection, exists
}

// ListProjections returns all registered projection names
func (m *Manager) ListProjections() []string {
	m.mu.RLock()
	defer m.mu.RUnlock()

	names := make([]string, 0, len(m.projections))
	for name := range m.projections {
		names = append(names, name)
	}

	return names
}

// ResetProjection resets a projection
func (m *Manager) ResetProjection(ctx context.Context, name string) error {
	m.mu.RLock()
	projection, exists := m.projections[name]
	m.mu.RUnlock()

	if !exists {
		return fmt.Errorf("projection %s not found", name)
	}

	if err := projection.Reset(ctx); err != nil {
		return fmt.Errorf("failed to reset projection %s: %w", name, err)
	}

	m.logger.Info("Projection reset",
		zap.String("projection_name", name))

	return nil
}

// GetProjectionStats returns statistics for all projections
func (m *Manager) GetProjectionStats() map[string]ProjectionStats {
	m.mu.RLock()
	defer m.mu.RUnlock()

	stats := make(map[string]ProjectionStats)
	for name, projection := range m.projections {
		stats[name] = ProjectionStats{
			Name:     name,
			Position: projection.GetPosition(),
			// Add more stats as needed
		}
	}

	return stats
}

// ProjectionStats represents projection statistics
type ProjectionStats struct {
	Name     string `json:"name"`
	Position int64  `json:"position"`
	Status   string `json:"status"`
	// Add more fields as needed
}
