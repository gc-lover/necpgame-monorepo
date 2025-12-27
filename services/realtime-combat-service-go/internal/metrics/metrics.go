// Issue: #2232
package metrics

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Collector holds all Prometheus metrics
type Collector struct {
	sessionsCreated       prometheus.Counter
	sessionsStarted       prometheus.Counter
	sessionsEnded         prometheus.Counter
	damageEvents          prometheus.Counter
	actionEvents          prometheus.Counter
	errors                prometheus.Counter
	activeSessions        prometheus.Gauge
	requestDuration       prometheus.Histogram
	comboCompleted        prometheus.Counter
	synergyActivated      prometheus.Counter
}

// NewCollector creates a new metrics collector
func NewCollector() *Collector {
	return &Collector{
		sessionsCreated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_sessions_created_total",
			Help: "Total number of combat sessions created",
		}),
		sessionsStarted: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_sessions_started_total",
			Help: "Total number of combat sessions started",
		}),
		sessionsEnded: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_sessions_ended_total",
			Help: "Total number of combat sessions ended",
		}),
		damageEvents: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_damage_events_total",
			Help: "Total number of damage events processed",
		}),
		actionEvents: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_action_events_total",
			Help: "Total number of action events processed",
		}),
		errors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_errors_total",
			Help: "Total number of errors encountered",
		}),
		activeSessions: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "combat_active_sessions",
			Help: "Number of currently active combat sessions",
		}),
		requestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name: "combat_request_duration_seconds",
			Help: "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		comboCompleted: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_combos_completed_total",
			Help: "Total number of combat combos completed",
		}),
		synergyActivated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_synergies_activated_total",
			Help: "Total number of combat synergies activated",
		}),
	}
}

// IncrementSessionsCreated increments the sessions created counter
func (c *Collector) IncrementSessionsCreated() {
	c.sessionsCreated.Inc()
}

// IncrementSessionsStarted increments the sessions started counter
func (c *Collector) IncrementSessionsStarted() {
	c.sessionsStarted.Inc()
	c.activeSessions.Inc()
}

// IncrementSessionsEnded increments the sessions ended counter
func (c *Collector) IncrementSessionsEnded() {
	c.sessionsEnded.Inc()
	c.activeSessions.Dec()
}

// IncrementDamageEvents increments the damage events counter
func (c *Collector) IncrementDamageEvents() {
	c.damageEvents.Inc()
}

// IncrementActionEvents increments the action events counter
func (c *Collector) IncrementActionEvents() {
	c.actionEvents.Inc()
}

// IncrementErrors increments the errors counter
func (c *Collector) IncrementErrors() {
	c.errors.Inc()
}

// ObserveRequestDuration observes request duration
func (c *Collector) ObserveRequestDuration(duration float64) {
	c.requestDuration.Observe(duration)
}

// IncrementComboCompleted increments the combo completed counter
func (c *Collector) IncrementComboCompleted(comboID string) {
	c.comboCompleted.Inc()
}

// IncrementSynergyActivated increments the synergy activated counter
func (c *Collector) IncrementSynergyActivated(synergyID string) {
	c.synergyActivated.Inc()
}
