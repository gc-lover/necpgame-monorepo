// Issue: #2210
package metrics

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Collector holds all Prometheus metrics
type Collector struct {
	tournamentsCreated          prometheus.Counter
	participantsRegistered      prometheus.Counter
	matchesCreated              prometheus.Counter
	matchesCompleted            prometheus.Counter
	errors                      prometheus.Counter
	activeTournaments           prometheus.Gauge
	activeMatches               prometheus.Gauge
	requestDuration             prometheus.Histogram
	bracketGenerationTime       prometheus.Histogram
}

// NewCollector creates a new metrics collector
func NewCollector() *Collector {
	return &Collector{
		tournamentsCreated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_tournaments_created_total",
			Help: "Total number of tournaments created",
		}),
		participantsRegistered: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_participants_registered_total",
			Help: "Total number of participants registered",
		}),
		matchesCreated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_matches_created_total",
			Help: "Total number of matches created",
		}),
		matchesCompleted: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_matches_completed_total",
			Help: "Total number of matches completed",
		}),
		errors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "tournament_errors_total",
			Help: "Total number of errors encountered",
		}),
		activeTournaments: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "tournament_active_tournaments",
			Help: "Number of currently active tournaments",
		}),
		activeMatches: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "tournament_active_matches",
			Help: "Number of currently active matches",
		}),
		requestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name: "tournament_request_duration_seconds",
			Help: "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		bracketGenerationTime: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "tournament_bracket_generation_duration_seconds",
			Help:    "Bracket generation duration in seconds",
			Buckets: []float64{.001, .005, .01, .025, .05, .1, .25, .5, 1, 2.5, 5, 10},
		}),
	}
}

// IncrementTournamentsCreated increments the tournaments created counter
func (c *Collector) IncrementTournamentsCreated() {
	c.tournamentsCreated.Inc()
}

// IncrementParticipantsRegistered increments the participants registered counter
func (c *Collector) IncrementParticipantsRegistered() {
	c.participantsRegistered.Inc()
}

// IncrementMatchesCreated increments the matches created counter
func (c *Collector) IncrementMatchesCreated() {
	c.matchesCreated.Inc()
}

// IncrementMatchesCompleted increments the matches completed counter
func (c *Collector) IncrementMatchesCompleted() {
	c.matchesCompleted.Inc()
}

// IncrementErrors increments the errors counter
func (c *Collector) IncrementErrors() {
	c.errors.Inc()
}

// SetActiveTournaments sets the number of active tournaments
func (c *Collector) SetActiveTournaments(count float64) {
	c.activeTournaments.Set(count)
}

// SetActiveMatches sets the number of active matches
func (c *Collector) SetActiveMatches(count float64) {
	c.activeMatches.Set(count)
}

// ObserveRequestDuration observes request duration
func (c *Collector) ObserveRequestDuration(duration float64) {
	c.requestDuration.Observe(duration)
}

// ObserveBracketGenerationTime observes bracket generation duration
func (c *Collector) ObserveBracketGenerationTime(duration float64) {
	c.bracketGenerationTime.Observe(duration)
}
