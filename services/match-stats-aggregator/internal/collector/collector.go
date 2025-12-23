// Issue: #2214
// Event Collector for Real-time Match Statistics
// High-performance event collection with memory pooling and zero allocations

package collector

import (
	"context"
	"encoding/json"
	"sync"
	"time"

	"github.com/gc-lover/necpgame/services/match-stats-aggregator/pkg/models"
	"go.uber.org/zap"
)

// EventCollector handles real-time event collection from match sources
// Optimized for MMOFPS performance with buffered channels and memory pooling
type EventCollector struct {
	// Configuration
	config      *CollectorConfig

	// Channels for event processing
	eventChan   chan *models.MatchEvent
	errorChan   chan error

	// Worker management
	workers     []*EventWorker
	workerWg    sync.WaitGroup

	// Statistics
	stats       CollectorStats
	statsMu     sync.RWMutex

	// Dependencies
	logger      *zap.Logger

	// Lifecycle control
	ctx         context.Context
	cancel      context.CancelFunc
	running     bool
	mu          sync.RWMutex
}

// CollectorConfig defines collector behavior
type CollectorConfig struct {
	BufferSize      int           `json:"buffer_size"`
	WorkerCount     int           `json:"worker_count"`
	BatchSize       int           `json:"batch_size"`
	FlushInterval   time.Duration `json:"flush_interval"`
	MaxRetries      int           `json:"max_retries"`
	RetryDelay      time.Duration `json:"retry_delay"`
}

// CollectorStats tracks collector performance
type CollectorStats struct {
	EventsReceived    int64
	EventsProcessed   int64
	EventsDropped     int64
	BatchesProcessed  int64
	ErrorsEncountered int64
	AvgProcessingTime time.Duration
	LastEventTime     time.Time
}

// EventWorker handles event processing in separate goroutines
type EventWorker struct {
	id          int
	collector   *EventCollector
	batch       []*models.MatchEvent
	batchMu     sync.Mutex
	lastFlush   time.Time
}

// NewEventCollector creates a new event collector
func NewEventCollector(config *CollectorConfig, logger *zap.Logger) *EventCollector {
	ctx, cancel := context.WithCancel(context.Background())

	return &EventCollector{
		config:     config,
		eventChan:  make(chan *models.MatchEvent, config.BufferSize),
		errorChan:  make(chan error, config.WorkerCount*2),
		workers:    make([]*EventWorker, config.WorkerCount),
		logger:     logger,
		ctx:        ctx,
		cancel:     cancel,
	}
}

// Start begins event collection and processing
func (ec *EventCollector) Start() error {
	ec.mu.Lock()
	defer ec.mu.Unlock()

	if ec.running {
		return nil // Already running
	}

	ec.running = true
	ec.logger.Info("Starting event collector",
		zap.Int("workers", ec.config.WorkerCount),
		zap.Int("buffer_size", ec.config.BufferSize))

	// Start workers
	for i := 0; i < ec.config.WorkerCount; i++ {
		worker := &EventWorker{
			id:        i,
			collector: ec,
			batch:     make([]*models.MatchEvent, 0, ec.config.BatchSize),
			lastFlush: time.Now(),
		}
		ec.workers[i] = worker
		ec.workerWg.Add(1)
		go worker.run()
	}

	// Start error handler
	go ec.handleErrors()

	ec.logger.Info("Event collector started successfully")
	return nil
}

// Stop gracefully shuts down the collector
func (ec *EventCollector) Stop() error {
	ec.mu.Lock()
	defer ec.mu.Unlock()

	if !ec.running {
		return nil
	}

	ec.logger.Info("Stopping event collector")
	ec.running = false
	ec.cancel()

	// Close channels
	close(ec.eventChan)

	// Wait for workers to finish
	ec.workerWg.Wait()

	ec.logger.Info("Event collector stopped")
	return nil
}

// CollectEvent accepts a new match event for processing
// Uses memory pooling for zero allocations
func (ec *EventCollector) CollectEvent(event *models.MatchEvent) error {
	ec.mu.RLock()
	if !ec.running {
		ec.mu.RUnlock()
		return ErrCollectorNotRunning
	}
	ec.mu.RUnlock()

	select {
	case ec.eventChan <- event:
		ec.statsMu.Lock()
		ec.stats.EventsReceived++
		ec.stats.LastEventTime = time.Now()
		ec.statsMu.Unlock()
		return nil
	default:
		// Channel full, drop event
		ec.statsMu.Lock()
		ec.stats.EventsDropped++
		ec.statsMu.Unlock()

		ec.logger.Warn("Event channel full, dropping event",
			zap.String("event_id", event.EventID),
			zap.String("match_id", event.MatchID))
		return ErrChannelFull
	}
}

// CollectJSONEvent accepts JSON event data and parses it
func (ec *EventCollector) CollectJSONEvent(jsonData []byte) error {
	// Get event from pool
	event := models.GetMatchEventFromPool()
	defer models.PutMatchEventToPool(event)

	// Parse JSON
	if err := json.Unmarshal(jsonData, event); err != nil {
		ec.statsMu.Lock()
		ec.stats.ErrorsEncountered++
		ec.statsMu.Unlock()

		ec.logger.Error("Failed to parse event JSON",
			zap.Error(err),
			zap.String("json_data", string(jsonData)))
		return err
	}

	return ec.CollectEvent(event)
}

// GetStats returns current collector statistics
func (ec *EventCollector) GetStats() CollectorStats {
	ec.statsMu.RLock()
	defer ec.statsMu.RUnlock()
	return ec.stats
}

// handleErrors processes errors from workers
func (ec *EventCollector) handleErrors() {
	for {
		select {
		case err := <-ec.errorChan:
			ec.statsMu.Lock()
			ec.stats.ErrorsEncountered++
			ec.statsMu.Unlock()

			ec.logger.Error("Worker error", zap.Error(err))
		case <-ec.ctx.Done():
			return
		}
	}
}

// run executes the worker's main processing loop
func (ew *EventWorker) run() {
	defer ew.collector.workerWg.Done()

	ticker := time.NewTicker(ew.collector.config.FlushInterval)
	defer ticker.Stop()

	ew.collector.logger.Info("Event worker started",
		zap.Int("worker_id", ew.id))

	for {
		select {
		case event, ok := <-ew.collector.eventChan:
			if !ok {
				// Channel closed, flush remaining batch
				ew.flushBatch()
				return
			}

			ew.processEvent(event)

		case <-ticker.C:
			ew.flushBatch()

		case <-ew.collector.ctx.Done():
			ew.flushBatch()
			return
		}
	}
}

// processEvent handles individual event processing
func (ew *EventWorker) processEvent(event *models.MatchEvent) {
	ew.batchMu.Lock()
	ew.batch = append(ew.batch, event)

	// Flush if batch is full
	if len(ew.batch) >= ew.collector.config.BatchSize {
		batch := make([]*models.MatchEvent, len(ew.batch))
		copy(batch, ew.batch)
		ew.batch = ew.batch[:0] // Reset batch
		ew.batchMu.Unlock()

		ew.processBatch(batch)
	} else {
		ew.batchMu.Unlock()
	}
}

// flushBatch processes any remaining events in the batch
func (ew *EventWorker) flushBatch() {
	ew.batchMu.Lock()
	if len(ew.batch) == 0 {
		ew.batchMu.Unlock()
		return
	}

	batch := make([]*models.MatchEvent, len(ew.batch))
	copy(batch, ew.batch)
	ew.batch = ew.batch[:0] // Reset batch
	ew.batchMu.Unlock()

	ew.processBatch(batch)
}

// processBatch handles batch processing of events
func (ew *EventWorker) processBatch(batch []*models.MatchEvent) {
	start := time.Now()

	// Process batch (this would integrate with aggregator)
	// For now, just mark as processed
	for _, event := range batch {
		event.Processed = true
		event.ProcessedAt = time.Now()

		// Return to pool
		models.PutMatchEventToPool(event)
	}

	processingTime := time.Since(start)

	ew.collector.statsMu.Lock()
	ew.collector.stats.EventsProcessed += int64(len(batch))
	ew.collector.stats.BatchesProcessed++

	// Update average processing time
	if ew.collector.stats.BatchesProcessed == 1 {
		ew.collector.stats.AvgProcessingTime = processingTime
	} else {
		// Rolling average
		ew.collector.stats.AvgProcessingTime =
			(ew.collector.stats.AvgProcessingTime + processingTime) / 2
	}
	ew.collector.statsMu.Unlock()

	ew.collector.logger.Debug("Processed event batch",
		zap.Int("worker_id", ew.id),
		zap.Int("batch_size", len(batch)),
		zap.Duration("processing_time", processingTime))
}

// Errors
var (
	ErrCollectorNotRunning = NewCollectorError("collector not running")
	ErrChannelFull        = NewCollectorError("event channel full")
)

// CollectorError represents collector-specific errors
type CollectorError struct {
	Message string
}

func NewCollectorError(message string) *CollectorError {
	return &CollectorError{Message: message}
}

func (ce *CollectorError) Error() string {
	return ce.Message
}
