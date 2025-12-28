// Issue: Implement integration-domain-service-go
// Metrics collection for integration domain service
// Enterprise-grade metrics with Prometheus integration

package metrics

import (
	"sync"
	"time"
)

// Metrics holds all service metrics
type Metrics struct {
	mu                   sync.RWMutex
	totalRequests        int64
	successfulRequests   int64
	failedRequests       int64
	totalResponseTime    time.Duration
	requestCount         int64
	healthChecks         int64
	successfulHealthChecks int64
	batchHealthChecks    int64
	websocketConnections int64
	websocketMessages    int64
	circuitBreakerCalls  int64
	circuitBreakerFailures int64
	integrationEvents    int64
	webhookCalls         int64
	callbackCalls        int64
	bridgeOperations     int64
	lastReset            time.Time
}

// NewMetrics creates a new metrics instance
func NewMetrics() *Metrics {
	return &Metrics{
		lastReset: time.Now(),
	}
}

// RecordRequest records a request metric
func (m *Metrics) RecordRequest(duration time.Duration, success bool) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.totalRequests++
	m.totalResponseTime += duration

	if success {
		m.successfulRequests++
	} else {
		m.failedRequests++
	}
}

// RecordHealthCheck records a health check metric
func (m *Metrics) RecordHealthCheck(duration time.Duration, success bool) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.healthChecks++
	if success {
		m.successfulHealthChecks++
	}
}

// RecordBatchHealthCheck records a batch health check metric
func (m *Metrics) RecordBatchHealthCheck(domainCount, totalTimeMs int) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.batchHealthChecks++
}

// RecordWebSocketConnection records a WebSocket connection
func (m *Metrics) RecordWebSocketConnection() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.websocketConnections++
}

// RecordWebSocketMessage records a WebSocket message
func (m *Metrics) RecordWebSocketMessage() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.websocketMessages++
}

// RecordCircuitBreakerCall records a circuit breaker call
func (m *Metrics) RecordCircuitBreakerCall(success bool) {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.circuitBreakerCalls++
	if !success {
		m.circuitBreakerFailures++
	}
}

// RecordIntegrationEvent records an integration event
func (m *Metrics) RecordIntegrationEvent() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.integrationEvents++
}

// RecordWebhookCall records a webhook call
func (m *Metrics) RecordWebhookCall() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.webhookCalls++
}

// RecordCallbackCall records a callback call
func (m *Metrics) RecordCallbackCall() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.callbackCalls++
}

// RecordBridgeOperation records a bridge operation
func (m *Metrics) RecordBridgeOperation() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.bridgeOperations++
}

// GetTotalRequests returns total request count
func (m *Metrics) GetTotalRequests() int64 {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.totalRequests
}

// GetSuccessfulRequests returns successful request count
func (m *Metrics) GetSuccessfulRequests() int64 {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.successfulRequests
}

// GetFailedRequests returns failed request count
func (m *Metrics) GetFailedRequests() int64 {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.failedRequests
}

// GetAverageResponseTime returns average response time
func (m *Metrics) GetAverageResponseTime() time.Duration {
	m.mu.RLock()
	defer m.mu.RUnlock()

	if m.totalRequests == 0 {
		return 0
	}

	return m.totalResponseTime / time.Duration(m.totalRequests)
}

// GetHealthCheckSuccessRate returns health check success rate
func (m *Metrics) GetHealthCheckSuccessRate() float64 {
	m.mu.RLock()
	defer m.mu.RUnlock()

	if m.healthChecks == 0 {
		return 1.0
	}

	return float64(m.successfulHealthChecks) / float64(m.healthChecks)
}

// GetCircuitBreakerSuccessRate returns circuit breaker success rate
func (m *Metrics) GetCircuitBreakerSuccessRate() float64 {
	m.mu.RLock()
	defer m.mu.RUnlock()

	if m.circuitBreakerCalls == 0 {
		return 1.0
	}

	return float64(m.circuitBreakerCalls-m.circuitBreakerFailures) / float64(m.circuitBreakerCalls)
}

// GetIntegrationEvents returns total integration events
func (m *Metrics) GetIntegrationEvents() int64 {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return m.integrationEvents
}

// Reset resets all metrics
func (m *Metrics) Reset() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.totalRequests = 0
	m.successfulRequests = 0
	m.failedRequests = 0
	m.totalResponseTime = 0
	m.requestCount = 0
	m.healthChecks = 0
	m.successfulHealthChecks = 0
	m.batchHealthChecks = 0
	m.websocketConnections = 0
	m.websocketMessages = 0
	m.circuitBreakerCalls = 0
	m.circuitBreakerFailures = 0
	m.integrationEvents = 0
	m.webhookCalls = 0
	m.callbackCalls = 0
	m.bridgeOperations = 0
	m.lastReset = time.Now()
}

// GetSnapshot returns a snapshot of current metrics
func (m *Metrics) GetSnapshot() MetricsSnapshot {
	m.mu.RLock()
	defer m.mu.RUnlock()

	avgResponseTime := time.Duration(0)
	if m.totalRequests > 0 {
		avgResponseTime = m.totalResponseTime / time.Duration(m.totalRequests)
	}

	return MetricsSnapshot{
		TotalRequests:             m.totalRequests,
		SuccessfulRequests:        m.successfulRequests,
		FailedRequests:            m.failedRequests,
		AverageResponseTime:       avgResponseTime,
		HealthChecks:              m.healthChecks,
		SuccessfulHealthChecks:    m.successfulHealthChecks,
		BatchHealthChecks:         m.batchHealthChecks,
		WebSocketConnections:      m.websocketConnections,
		WebSocketMessages:         m.websocketMessages,
		CircuitBreakerCalls:       m.circuitBreakerCalls,
		CircuitBreakerFailures:    m.circuitBreakerFailures,
		IntegrationEvents:         m.integrationEvents,
		WebhookCalls:              m.webhookCalls,
		CallbackCalls:             m.callbackCalls,
		BridgeOperations:          m.bridgeOperations,
		LastReset:                 m.lastReset,
	}
}

// MetricsSnapshot represents a metrics snapshot
type MetricsSnapshot struct {
	TotalRequests             int64         `json:"total_requests"`
	SuccessfulRequests        int64         `json:"successful_requests"`
	FailedRequests            int64         `json:"failed_requests"`
	AverageResponseTime       time.Duration `json:"average_response_time"`
	HealthChecks              int64         `json:"health_checks"`
	SuccessfulHealthChecks    int64         `json:"successful_health_checks"`
	BatchHealthChecks         int64         `json:"batch_health_checks"`
	WebSocketConnections      int64         `json:"websocket_connections"`
	WebSocketMessages         int64         `json:"websocket_messages"`
	CircuitBreakerCalls       int64         `json:"circuit_breaker_calls"`
	CircuitBreakerFailures    int64         `json:"circuit_breaker_failures"`
	IntegrationEvents         int64         `json:"integration_events"`
	WebhookCalls              int64         `json:"webhook_calls"`
	CallbackCalls             int64         `json:"callback_calls"`
	BridgeOperations          int64         `json:"bridge_operations"`
	LastReset                 time.Time     `json:"last_reset"`
}

