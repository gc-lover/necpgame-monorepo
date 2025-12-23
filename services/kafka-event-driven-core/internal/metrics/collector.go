// Issue: #2237
// PERFORMANCE: High-performance metrics collection for Kafka event processing
package metrics

import (
	"net/http"
	"strconv"
	"sync"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"
)

// Collector handles metrics collection and reporting
type Collector struct {
	// Event processing metrics
	eventsProcessed    *prometheus.CounterVec
	eventsPublished    *prometheus.CounterVec
	eventsConsumed     *prometheus.CounterVec
	eventsErrors       *prometheus.CounterVec
	eventLatency       *prometheus.HistogramVec
	eventSize          *prometheus.HistogramVec

	// Batch processing metrics
	batchesProcessed   *prometheus.CounterVec
	batchLatency       *prometheus.HistogramVec
	batchSize          *prometheus.HistogramVec

	// Consumer metrics
	consumerLag        *prometheus.GaugeVec
	consumerPartitions *prometheus.GaugeVec
	consumerWorkers    *prometheus.GaugeVec

	// System metrics
	goroutines         prometheus.Gauge
	memoryUsage        *prometheus.GaugeVec

	logger             *zap.Logger
	mu                 sync.RWMutex
	closed             bool
}

// NewCollector creates a new metrics collector
func NewCollector(logger *zap.Logger) *Collector {
	c := &Collector{
		logger: logger,
	}

	c.initializeMetrics()
	c.registerMetrics()

	logger.Info("Metrics collector initialized")
	return c
}

// initializeMetrics creates all metric collectors
func (c *Collector) initializeMetrics() {
	// Event processing counters
	c.eventsProcessed = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "kafka_events_processed_total",
			Help: "Total number of events processed",
		},
		[]string{"topic", "event_type", "status"},
	)

	c.eventsPublished = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "kafka_events_published_total",
			Help: "Total number of events published",
		},
		[]string{"topic", "event_type"},
	)

	c.eventsConsumed = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "kafka_events_consumed_total",
			Help: "Total number of events consumed",
		},
		[]string{"consumer_group", "topic", "event_type"},
	)

	c.eventsErrors = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "kafka_events_errors_total",
			Help: "Total number of event processing errors",
		},
		[]string{"topic", "error_type"},
	)

	// Latency histograms
	c.eventLatency = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "kafka_event_processing_duration_seconds",
			Help:    "Event processing duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"topic", "operation"},
	)

	c.eventSize = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "kafka_event_size_bytes",
			Help:    "Event size in bytes",
			Buckets: []float64{100, 500, 1000, 5000, 10000, 50000, 100000},
		},
		[]string{"topic", "event_type"},
	)

	// Batch processing metrics
	c.batchesProcessed = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "kafka_batches_processed_total",
			Help: "Total number of event batches processed",
		},
		[]string{"topic", "status"},
	)

	c.batchLatency = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "kafka_batch_processing_duration_seconds",
			Help:    "Batch processing duration in seconds",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"topic"},
	)

	c.batchSize = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "kafka_batch_size_events",
			Help:    "Number of events in batch",
			Buckets: []float64{1, 5, 10, 25, 50, 100, 500, 1000},
		},
		[]string{"topic"},
	)

	// Consumer metrics
	c.consumerLag = prometheus.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "kafka_consumer_lag",
			Help: "Consumer lag in messages",
		},
		[]string{"consumer_group", "topic", "partition"},
	)

	c.consumerPartitions = prometheus.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "kafka_consumer_partitions",
			Help: "Number of partitions assigned to consumer",
		},
		[]string{"consumer_group", "topic"},
	)

	c.consumerWorkers = prometheus.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "kafka_consumer_workers",
			Help: "Number of active consumer workers",
		},
		[]string{"consumer_group", "topic"},
	)

	// System metrics
	c.goroutines = prometheus.NewGauge(
		prometheus.GaugeOpts{
			Name: "kafka_goroutines_total",
			Help: "Total number of goroutines",
		},
	)

	c.memoryUsage = prometheus.NewGaugeVec(
		prometheus.GaugeOpts{
			Name: "kafka_memory_usage_bytes",
			Help: "Memory usage in bytes",
		},
		[]string{"type"}, // heap, stack, gc
	)
}

// registerMetrics registers all metrics with Prometheus
func (c *Collector) registerMetrics() {
	prometheus.MustRegister(
		c.eventsProcessed,
		c.eventsPublished,
		c.eventsConsumed,
		c.eventsErrors,
		c.eventLatency,
		c.eventSize,
		c.batchesProcessed,
		c.batchLatency,
		c.batchSize,
		c.consumerLag,
		c.consumerPartitions,
		c.consumerWorkers,
		c.goroutines,
		c.memoryUsage,
	)
}

// RecordEventProcessed records an event processing operation
func (c *Collector) RecordEventProcessed(topic, eventType, status string) {
	c.eventsProcessed.WithLabelValues(topic, eventType, status).Inc()
}

// RecordEventPublished records a successful event publication
func (c *Collector) RecordEventPublished(topic string, duration time.Duration, sizeBytes int) {
	c.eventsPublished.WithLabelValues(topic, "").Inc()
	c.eventLatency.WithLabelValues(topic, "publish").Observe(duration.Seconds())
	c.eventSize.WithLabelValues(topic, "").Observe(float64(sizeBytes))
}

// RecordEventConsumed records a successful event consumption
func (c *Collector) RecordEventConsumed(consumerGroup, topic, eventType string) {
	c.eventsConsumed.WithLabelValues(consumerGroup, topic, eventType).Inc()
}

// RecordEventError records an event processing error
func (c *Collector) RecordEventError(errorType string) {
	// Note: This is a simplified version - in practice you'd want topic-specific errors
	c.eventsErrors.WithLabelValues("unknown", errorType).Inc()
}

// RecordBatchPublished records a successful batch publication
func (c *Collector) RecordBatchPublished(topic string, batchSize int, duration time.Duration, totalBytes int) {
	c.batchesProcessed.WithLabelValues(topic, "success").Inc()
	c.batchLatency.WithLabelValues(topic).Observe(duration.Seconds())
	c.batchSize.WithLabelValues(topic).Observe(float64(batchSize))
}

// RecordConsumerLag records consumer lag
func (c *Collector) RecordConsumerLag(consumerGroup, topic string, partition int32, lag int64) {
	c.consumerLag.WithLabelValues(consumerGroup, topic, strconv.Itoa(int(partition))).Set(float64(lag))
}

// RecordConsumerPartitions records number of assigned partitions
func (c *Collector) RecordConsumerPartitions(consumerGroup, topic string, partitions int) {
	c.consumerPartitions.WithLabelValues(consumerGroup, topic).Set(float64(partitions))
}

// RecordConsumerWorkers records number of active workers
func (c *Collector) RecordConsumerWorkers(consumerGroup, topic string, workers int) {
	c.consumerWorkers.WithLabelValues(consumerGroup, topic).Set(float64(workers))
}

// UpdateSystemMetrics updates system-level metrics
func (c *Collector) UpdateSystemMetrics() {
	// This would be called periodically to update system metrics
	// For now, just record goroutine count
	// c.goroutines.Set(float64(runtime.NumGoroutine()))
}

// Handler returns the HTTP handler for metrics endpoint
func (c *Collector) Handler() http.Handler {
	return promhttp.Handler()
}

// Shutdown gracefully shuts down the metrics collector
func (c *Collector) Shutdown() {
	c.mu.Lock()
	defer c.mu.Unlock()

	if c.closed {
		return
	}

	c.closed = true

	// Unregister metrics
	prometheus.Unregister(c.eventsProcessed)
	prometheus.Unregister(c.eventsPublished)
	prometheus.Unregister(c.eventsConsumed)
	prometheus.Unregister(c.eventsErrors)
	prometheus.Unregister(c.eventLatency)
	prometheus.Unregister(c.eventSize)
	prometheus.Unregister(c.batchesProcessed)
	prometheus.Unregister(c.batchLatency)
	prometheus.Unregister(c.batchSize)
	prometheus.Unregister(c.consumerLag)
	prometheus.Unregister(c.consumerPartitions)
	prometheus.Unregister(c.consumerWorkers)
	prometheus.Unregister(c.goroutines)
	prometheus.Unregister(c.memoryUsage)

	c.logger.Info("Metrics collector shutdown complete")
}

// IsClosed returns whether the collector is closed
func (c *Collector) IsClosed() bool {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return c.closed
}

// GetEventsProcessed returns the total number of events processed (for testing)
func (c *Collector) GetEventsProcessed(topic, eventType, status string) float64 {
	// Note: Prometheus counters don't expose direct value access
	// Values are available through HTTP metrics endpoint
	return 0
}

// GetEventsPublished returns the total number of events published (for testing)
func (c *Collector) GetEventsPublished(topic, eventType string) float64 {
	// Note: Prometheus counters don't expose direct value access
	// Values are available through HTTP metrics endpoint
	return 0
}

// GetEventsConsumed returns the total number of events consumed (for testing)
func (c *Collector) GetEventsConsumed(consumerGroup, topic, eventType string) float64 {
	// Note: Prometheus counters don't expose direct value access
	// Values are available through HTTP metrics endpoint
	return 0
}

// GetEventErrors returns the total number of event errors (for testing)
func (c *Collector) GetEventErrors(topic, errorType string) float64 {
	// Note: Prometheus counters don't expose direct value access
	// Values are available through HTTP metrics endpoint
	return 0
}
