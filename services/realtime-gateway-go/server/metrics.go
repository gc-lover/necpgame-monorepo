package server

import (
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
)

var (
	sessionsActive = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "sessions_active_total",
			Help: "Number of active sessions",
		},
	)

	sessionsCreated = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "sessions_created_total",
			Help: "Total number of sessions created",
		},
	)

	sessionsReconnected = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "sessions_reconnected_total",
			Help: "Total number of sessions reconnected",
		},
	)

	sessionsExpired = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "sessions_expired_total",
			Help: "Total number of expired sessions",
		},
	)

	heartbeatsTotal = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "heartbeats_total",
			Help: "Total number of heartbeats received",
		},
	)
)

func SetActiveSessions(count float64) {
	sessionsActive.Set(count)
}

func RecordSessionCreated() {
	sessionsCreated.Inc()
}

func RecordSessionReconnected() {
	sessionsReconnected.Inc()
}

func RecordSessionExpired() {
	sessionsExpired.Inc()
}

func RecordHeartbeat() {
	heartbeatsTotal.Inc()
}

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

	playerInputReceived = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "player_input_received_total",
			Help: "Total number of PlayerInput messages received from clients",
		},
	)

	playerInputForwarded = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "player_input_forwarded_total",
			Help: "Total number of PlayerInput messages forwarded to server",
		},
	)

	gameStateReceived = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "gamestate_received_total",
			Help: "Total number of GameState messages received from server",
		},
	)

	gameStateBroadcasted = promauto.NewCounter(
		prometheus.CounterOpts{
			Name: "gamestate_broadcasted_total",
			Help: "Total number of GameState messages broadcasted to clients",
		},
	)

	gameStateBroadcastDuration = promauto.NewHistogram(
		prometheus.HistogramOpts{
			Name:    "gamestate_broadcast_duration_seconds",
			Help:    "Time taken to broadcast GameState to all clients",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
	)

	activeClients = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "active_clients",
			Help: "Number of active client connections",
		},
	)

	activeServerConnection = promauto.NewGauge(
		prometheus.GaugeOpts{
			Name: "active_server_connection",
			Help: "Whether server connection is active (1) or not (0)",
		},
	)

	messageSize = promauto.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "message_size_bytes",
			Help:    "Size of messages in bytes",
			Buckets: prometheus.ExponentialBuckets(10, 2, 15),
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

func RecordPlayerInputReceived() {
	playerInputReceived.Inc()
}

func RecordPlayerInputForwarded() {
	playerInputForwarded.Inc()
}

func RecordGameStateReceived() {
	gameStateReceived.Inc()
}

func RecordGameStateBroadcasted() {
	gameStateBroadcasted.Inc()
}

func RecordGameStateBroadcastDuration(duration float64) {
	gameStateBroadcastDuration.Observe(duration)
}

func SetActiveClients(count float64) {
	activeClients.Set(count)
}

func SetActiveServerConnection(active bool) {
	if active {
		activeServerConnection.Set(1)
	} else {
		activeServerConnection.Set(0)
	}
}

func RecordMessageSize(msgType string, size int) {
	messageSize.WithLabelValues(msgType).Observe(float64(size))
}

