package sync

import (
	"context"

	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/conflict"
	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/state"
)

// Config holds sync manager configuration
type Config struct {
	StateManager     *state.Manager
	ConflictResolver *conflict.Resolver
	Logger           *zap.Logger
	Meter            metric.Meter
}

// Manager manages data synchronization operations
type Manager struct {
	config      Config
	logger      *zap.Logger
	stateMgr    *state.Manager
	conflictRes *conflict.Resolver
	meter       metric.Meter

	// Metrics
	syncOperations metric.Int64Counter
	syncLatency    metric.Float64Histogram
	activeSyncs    metric.Int64Gauge
}

// NewManager creates a new sync manager
func NewManager(config Config) *Manager {
	return &Manager{
		config:      config,
		logger:      config.Logger,
		stateMgr:    config.StateManager,
		conflictRes: config.ConflictResolver,
		meter:       config.Meter,
	}
}

// Start starts the sync manager
func (m *Manager) Start(ctx context.Context) error {
	m.logger.Info("starting sync manager")

	// Initialize metrics
	var err error
	m.syncOperations, err = m.meter.Int64Counter(
		"sync_operations_total",
		metric.WithDescription("Total number of synchronization operations"),
	)
	if err != nil {
		return err
	}

	m.syncLatency, err = m.meter.Float64Histogram(
		"sync_operation_duration_seconds",
		metric.WithDescription("Duration of synchronization operations"),
	)
	if err != nil {
		return err
	}

	m.activeSyncs, err = m.meter.Int64Gauge(
		"active_sync_operations",
		metric.WithDescription("Number of currently active sync operations"),
	)
	if err != nil {
		return err
	}

	m.logger.Info("sync manager started")
	return nil
}

// Stop stops the sync manager
func (m *Manager) Stop(ctx context.Context) error {
	m.logger.Info("sync manager stopped")
	return nil
}

// Health checks the health of the sync manager
func (m *Manager) Health(ctx context.Context) error {
	// Basic health check - can be extended
	return nil
}
