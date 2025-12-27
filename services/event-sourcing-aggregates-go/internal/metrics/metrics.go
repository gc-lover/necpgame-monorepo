// Issue: #2217
package metrics

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Collector holds all Prometheus metrics
type Collector struct {
	eventsAppended         prometheus.Counter
	eventsProcessed        prometheus.Counter
	processingErrors       prometheus.Counter
	snapshotsCreated       prometheus.Counter
	readModelsUpdated      prometheus.Counter
	activeAggregates       prometheus.Gauge
	pendingEvents          prometheus.Gauge
	processingTime         prometheus.Histogram
	eventAppendLatency     prometheus.Histogram
	snapshotCreationTime   prometheus.Histogram
}

// NewCollector creates a new metrics collector
func NewCollector() *Collector {
	return &Collector{
		eventsAppended: promauto.NewCounter(prometheus.CounterOpts{
			Name: "event_sourcing_events_appended_total",
			Help: "Total number of events appended to the event store",
		}),
		eventsProcessed: promauto.NewCounter(prometheus.CounterOpts{
			Name: "event_sourcing_events_processed_total",
			Help: "Total number of events processed",
		}),
		processingErrors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "event_sourcing_processing_errors_total",
			Help: "Total number of event processing errors",
		}),
		snapshotsCreated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "event_sourcing_snapshots_created_total",
			Help: "Total number of aggregate snapshots created",
		}),
		readModelsUpdated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "event_sourcing_read_models_updated_total",
			Help: "Total number of read models updated",
		}),
		activeAggregates: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "event_sourcing_active_aggregates",
			Help: "Number of currently active aggregates",
		}),
		pendingEvents: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "event_sourcing_pending_events",
			Help: "Number of events pending processing",
		}),
		processingTime: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "event_sourcing_processing_duration_seconds",
			Help:    "Event processing duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		eventAppendLatency: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "event_sourcing_event_append_latency_seconds",
			Help:    "Event append latency in seconds",
			Buckets: []float64{.001, .005, .01, .025, .05, .1, .25, .5, 1, 2.5, 5, 10},
		}),
		snapshotCreationTime: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "event_sourcing_snapshot_creation_duration_seconds",
			Help:    "Snapshot creation duration in seconds",
			Buckets: []float64{.001, .005, .01, .025, .05, .1, .25, .5, 1, 2.5, 5, 10},
		}),
	}
}

// IncrementEventsAppended increments the events appended counter
func (c *Collector) IncrementEventsAppended() {
	c.eventsAppended.Inc()
}

// IncrementEventsProcessed increments the events processed counter
func (c *Collector) IncrementEventsProcessed() {
	c.eventsProcessed.Inc()
}

// IncrementProcessingErrors increments the processing errors counter
func (c *Collector) IncrementProcessingErrors() {
	c.processingErrors.Inc()
}

// IncrementSnapshotsCreated increments the snapshots created counter
func (c *Collector) IncrementSnapshotsCreated() {
	c.snapshotsCreated.Inc()
}

// IncrementReadModelsUpdated increments the read models updated counter
func (c *Collector) IncrementReadModelsUpdated() {
	c.readModelsUpdated.Inc()
}

// IncrementErrors increments the errors counter (alias for processing errors)
func (c *Collector) IncrementErrors() {
	c.processingErrors.Inc()
}

// SetActiveAggregates sets the number of active aggregates
func (c *Collector) SetActiveAggregates(count float64) {
	c.activeAggregates.Set(count)
}

// SetPendingEvents sets the number of pending events
func (c *Collector) SetPendingEvents(count float64) {
	c.pendingEvents.Set(count)
}

// ObserveProcessingTime observes event processing duration
func (c *Collector) ObserveProcessingTime(duration float64) {
	c.processingTime.Observe(duration)
}

// ObserveEventAppendLatency observes event append latency
func (c *Collector) ObserveEventAppendLatency(duration float64) {
	c.eventAppendLatency.Observe(duration)
}

// ObserveSnapshotCreationTime observes snapshot creation duration
func (c *Collector) ObserveSnapshotCreationTime(duration float64) {
	c.snapshotCreationTime.Observe(duration)
}
