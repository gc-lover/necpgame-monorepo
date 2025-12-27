// Issue: #2229
package metrics

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

// Collector holds all Prometheus metrics
type Collector struct {
	recipesCreated     prometheus.Counter
	ordersCreated      prometheus.Counter
	ordersCompleted    prometheus.Counter
	ordersCancelled    prometheus.Counter
	stationsBooked     prometheus.Counter
	errors             prometheus.Counter
	activeOrders       prometheus.Gauge
	requestDuration    prometheus.Histogram
}

// NewCollector creates a new metrics collector
func NewCollector() *Collector {
	return &Collector{
		recipesCreated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "crafting_recipes_created_total",
			Help: "Total number of crafting recipes created",
		}),
		ordersCreated: promauto.NewCounter(prometheus.CounterOpts{
			Name: "crafting_orders_created_total",
			Help: "Total number of crafting orders created",
		}),
		ordersCompleted: promauto.NewCounter(prometheus.CounterOpts{
			Name: "crafting_orders_completed_total",
			Help: "Total number of crafting orders completed",
		}),
		ordersCancelled: promauto.NewCounter(prometheus.CounterOpts{
			Name: "crafting_orders_cancelled_total",
			Help: "Total number of crafting orders cancelled",
		}),
		stationsBooked: promauto.NewCounter(prometheus.CounterOpts{
			Name: "crafting_stations_booked_total",
			Help: "Total number of crafting stations booked",
		}),
		errors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "crafting_errors_total",
			Help: "Total number of errors encountered",
		}),
		activeOrders: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "crafting_active_orders",
			Help: "Number of currently active crafting orders",
		}),
		requestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name: "crafting_request_duration_seconds",
			Help: "Request duration in seconds",
			Buckets: prometheus.DefBuckets,
		}),
	}
}

// IncrementRecipesCreated increments the recipes created counter
func (c *Collector) IncrementRecipesCreated() {
	c.recipesCreated.Inc()
}

// IncrementOrdersCreated increments the orders created counter
func (c *Collector) IncrementOrdersCreated() {
	c.ordersCreated.Inc()
	c.activeOrders.Inc()
}

// IncrementOrdersCompleted increments the orders completed counter
func (c *Collector) IncrementOrdersCompleted() {
	c.ordersCompleted.Inc()
	c.activeOrders.Dec()
}

// IncrementOrdersCancelled increments the orders cancelled counter
func (c *Collector) IncrementOrdersCancelled() {
	c.ordersCancelled.Inc()
	c.activeOrders.Dec()
}

// IncrementStationsBooked increments the stations booked counter
func (c *Collector) IncrementStationsBooked() {
	c.stationsBooked.Inc()
}

// IncrementErrors increments the errors counter
func (c *Collector) IncrementErrors() {
	c.errors.Inc()
}

// ObserveRequestDuration observes request duration
func (c *Collector) ObserveRequestDuration(duration float64) {
	c.requestDuration.Observe(duration)
}
