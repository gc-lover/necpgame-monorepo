// Issue: #2250
package metrics

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Collector holds all Prometheus metrics
type Collector struct {
	statsRetrieved     prometheus.Counter
	statsUpdated       prometheus.Counter
	eventsRecorded     prometheus.Counter
	errors             prometheus.Counter
	activeConnections  prometheus.Gauge
	requestDuration    prometheus.Histogram
	eventProcessingTime prometheus.Histogram
}

// NewCollector creates a new metrics collector
func NewCollector() *Collector {
	return &Collector{
		statsRetrieved: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_stats_retrieved_total",
			Help: "Total number of combat statistics retrieved",
		}),
		statsUpdated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_stats_updated_total",
			Help: "Total number of combat statistics updated",
		}),
		eventsRecorded: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_events_recorded_total",
			Help: "Total number of combat events recorded",
		}),
		errors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_stats_errors_total",
			Help: "Total number of errors encountered",
		}),
		activeConnections: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "combat_stats_active_connections",
			Help: "Number of currently active connections",
		}),
		requestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name: "combat_stats_request_duration_seconds",
			Help: "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		eventProcessingTime: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "combat_stats_event_processing_duration_seconds",
			Help:    "Event processing duration in seconds",
			Buckets: []float64{.001, .005, .01, .025, .05, .1, .25, .5, 1, 2.5, 5, 10},
		}),
	}
}

// IncrementStatsRetrieved increments the stats retrieved counter
func (c *Collector) IncrementStatsRetrieved() {
	c.statsRetrieved.Inc()
}

// IncrementStatsUpdated increments the stats updated counter
func (c *Collector) IncrementStatsUpdated() {
	c.statsUpdated.Inc()
}

// IncrementEventsRecorded increments the events recorded counter
func (c *Collector) IncrementEventsRecorded() {
	c.eventsRecorded.Inc()
}

// IncrementErrors increments the errors counter
func (c *Collector) IncrementErrors() {
	c.errors.Inc()
}

// SetActiveConnections sets the number of active connections
func (c *Collector) SetActiveConnections(count float64) {
	c.activeConnections.Set(count)
}

// ObserveRequestDuration observes request duration
func (c *Collector) ObserveRequestDuration(duration float64) {
	c.requestDuration.Observe(duration)
}

// ObserveEventProcessingTime observes event processing duration
func (c *Collector) ObserveEventProcessingTime(duration float64) {
	c.eventProcessingTime.Observe(duration)
}
