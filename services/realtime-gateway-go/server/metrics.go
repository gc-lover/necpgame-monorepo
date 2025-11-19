package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	connectionsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "websocket_connections_total",
			Help: "Total number of WebSocket connections",
		},
		[]string{"status"},
	)

	connectionsActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "websocket_connections_active",
			Help: "Number of active WebSocket connections",
		},
	)

	messagesTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "websocket_messages_total",
			Help: "Total number of messages processed",
		},
		[]string{"type", "status"},
	)

	messageLatency = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "websocket_message_latency_seconds",
			Help:    "Message processing latency in seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"type"},
	)

	errorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "websocket_errors_total",
			Help: "Total number of errors",
		},
		[]string{"type"},
	)
)

func RecordConnection(status string) {
	connectionsTotal.WithLabelValues(status).Inc()
	if status == "opened" {
		connectionsActive.Inc()
	} else if status == "closed" {
		connectionsActive.Dec()
	}
}


func RecordMessage(msgType, status string) {
	messagesTotal.WithLabelValues(msgType, status).Inc()
}

func RecordMessageLatency(msgType string, latency float64) {
	messageLatency.WithLabelValues(msgType).Observe(latency)
}

func RecordError(errorType string) {
	errorsTotal.WithLabelValues(errorType).Inc()
}

