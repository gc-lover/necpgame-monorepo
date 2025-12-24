// Metrics collection for Jackie Welles NPC service
// Issue: #1905
// PERFORMANCE: Lightweight metrics with minimal overhead

package server

import (
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/jackie-welles-service-go/pkg/api"
)

// MetricsCollector handles service metrics
type MetricsCollector struct {
	mu                      sync.RWMutex
	requestCount            int64
	errorCount              int64
	questAcceptedCount      int64
	tradeCompletedCount     int64
	dialogueStartedCount    int64
	dialogueResponseCount   int64
	interactionCount        int64
	relationshipUpdateCount int64
	requestDuration         time.Duration
	lastRequestTime         time.Time
}

// NewMetricsCollector creates a new metrics collector
func NewMetricsCollector() *MetricsCollector {
	return &MetricsCollector{}
}

// RecordRequest records a general request
func (m *MetricsCollector) RecordRequest() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.requestCount++
	m.lastRequestTime = time.Now()
}

// RecordError records an error
func (m *MetricsCollector) RecordError() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.errorCount++
}

// RecordQuestAccepted records quest acceptance
func (m *MetricsCollector) RecordQuestAccepted() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.questAcceptedCount++
}

// RecordTradeCompleted records completed trade
func (m *MetricsCollector) RecordTradeCompleted() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.tradeCompletedCount++
}

// RecordDialogueStarted records dialogue start
func (m *MetricsCollector) RecordDialogueStarted() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.dialogueStartedCount++
}

// RecordDialogueResponse records dialogue response
func (m *MetricsCollector) RecordDialogueResponse() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.dialogueResponseCount++
}

// RecordInteraction records NPC interaction
func (m *MetricsCollector) RecordInteraction() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.interactionCount++
}

// RecordRelationshipUpdate records relationship change
func (m *MetricsCollector) RecordRelationshipUpdate() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.relationshipUpdateCount++
}

// RecordRequestDuration records request processing time
func (m *MetricsCollector) RecordRequestDuration(duration time.Duration) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.requestDuration = duration
}

// GetMetrics returns current metrics snapshot
func (m *MetricsCollector) GetMetrics() *api.JackieServiceMetrics {
	m.mu.RLock()
	defer m.mu.RUnlock()

	return &api.JackieServiceMetrics{
		RequestCount:            api.NewOptInt(int(m.requestCount)),
		ErrorCount:              api.NewOptInt(int(m.errorCount)),
		QuestAcceptedCount:      api.NewOptInt(int(m.questAcceptedCount)),
		TradeCompletedCount:     api.NewOptInt(int(m.tradeCompletedCount)),
		DialogueStartedCount:    api.NewOptInt(int(m.dialogueStartedCount)),
		DialogueResponseCount:   api.NewOptInt(int(m.dialogueResponseCount)),
		InteractionCount:        api.NewOptInt(int(m.interactionCount)),
		RelationshipUpdateCount: api.NewOptInt(int(m.relationshipUpdateCount)),
		AverageRequestDuration:  api.NewOptString(m.requestDuration.String()),
		LastRequestTime:         api.NewOptDateTime(m.lastRequestTime),
		Uptime:                  api.NewOptString(time.Since(m.lastRequestTime).String()),
	}
}

// Reset resets all metrics
func (m *MetricsCollector) Reset() {
	m.mu.Lock()
	defer m.mu.Unlock()

	m.requestCount = 0
	m.errorCount = 0
	m.questAcceptedCount = 0
	m.tradeCompletedCount = 0
	m.dialogueStartedCount = 0
	m.dialogueResponseCount = 0
	m.interactionCount = 0
	m.relationshipUpdateCount = 0
	m.requestDuration = 0
}

// GetStats returns basic statistics
func (m *MetricsCollector) GetStats() map[string]interface{} {
	m.mu.RLock()
	defer m.mu.RUnlock()

	return map[string]interface{}{
		"total_requests":          m.requestCount,
		"total_errors":            m.errorCount,
		"quests_accepted":         m.questAcceptedCount,
		"trades_completed":        m.tradeCompletedCount,
		"dialogues_started":       m.dialogueStartedCount,
		"dialogue_responses":      m.dialogueResponseCount,
		"interactions":            m.interactionCount,
		"relationship_updates":    m.relationshipUpdateCount,
		"last_request":            m.lastRequestTime,
		"avg_request_duration_ms": m.requestDuration.Milliseconds(),
	}
}

// Metrics holds the metrics instance (global for the service)
var Metrics *MetricsCollector

// NewMetrics creates a new global metrics instance
func NewMetrics() *MetricsCollector {
	if Metrics == nil {
		Metrics = NewMetricsCollector()
	}
	return Metrics
}

// GetGlobalMetrics returns global metrics instance
func GetGlobalMetrics() *MetricsCollector {
	return Metrics
}

// RecordHealthCheck records health check
func (m *MetricsCollector) RecordHealthCheck() {
	m.RecordRequest()
}

// GetHealthStatus returns health status based on metrics
func (m *MetricsCollector) GetHealthStatus() *api.HealthResponse {
	m.mu.RLock()
	defer m.mu.RUnlock()

	status := "healthy"
	if m.errorCount > m.requestCount/10 { // More than 10% errors
		status = "degraded"
	}

	return &api.HealthResponse{
		Status:    api.NewOptString(status),
		Timestamp: api.NewOptDateTime(time.Now()),
		Version:   api.NewOptString("1.0.0"),
	}
}
