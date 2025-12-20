package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	websocketConnectionsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "websocket_connections_total",
			Help: "Total number of WebSocket connections",
		},
		[]string{"status"},
	)

	websocketConnectionsActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "websocket_connections_active",
			Help: "Number of active WebSocket connections",
		},
	)

	websocketMessagesTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "websocket_messages_total",
			Help: "Total number of WebSocket messages",
		},
		[]string{"type", "status"},
	)

	websocketRoomsActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "websocket_rooms_active",
			Help: "Number of active rooms",
		},
	)

	websocketErrorsTotal = promauto.NewCounterVec(
		prometheus.CounterOpts{
			Name: "websocket_errors_total",
			Help: "Total number of WebSocket errors",
		},
		[]string{"type"},
	)
)

func RecordWebSocketConnection(status string) {
	websocketConnectionsTotal.WithLabelValues(status).Inc()
	if status == "opened" {
		websocketConnectionsActive.Inc()
	} else if status == "closed" {
		websocketConnectionsActive.Dec()
	}
}

func RecordWebSocketMessage(msgType, status string) {
	websocketMessagesTotal.WithLabelValues(msgType, status).Inc()
}

func RecordWebSocketRoom(count int) {
	websocketRoomsActive.Set(float64(count))
}

func RecordWebSocketError(errorType string) {
	websocketErrorsTotal.WithLabelValues(errorType).Inc()
}
