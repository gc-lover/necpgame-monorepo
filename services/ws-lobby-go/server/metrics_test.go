// Issue: #140890959
package server

import (
	"testing"

	"github.com/prometheus/client_golang/prometheus/testutil"
	"github.com/stretchr/testify/assert"
)

func TestRecordWebSocketConnection_Opened(t *testing.T) {
	// Reset metrics before test
	websocketConnectionsTotal.Reset()
	websocketConnectionsActive.Set(0)

	RecordWebSocketConnection("opened")

	// Check counter
	count := testutil.ToFloat64(websocketConnectionsTotal.WithLabelValues("opened"))
	assert.Equal(t, 1.0, count)

	// Check gauge (should be incremented)
	gauge := testutil.ToFloat64(websocketConnectionsActive)
	assert.Equal(t, 1.0, gauge)
}

func TestRecordWebSocketConnection_Closed(t *testing.T) {
	// Reset metrics before test
	websocketConnectionsTotal.Reset()
	websocketConnectionsActive.Set(1)

	RecordWebSocketConnection("closed")

	// Check counter
	count := testutil.ToFloat64(websocketConnectionsTotal.WithLabelValues("closed"))
	assert.Equal(t, 1.0, count)

	// Check gauge (should be decremented)
	gauge := testutil.ToFloat64(websocketConnectionsActive)
	assert.Equal(t, 0.0, gauge)
}

func TestRecordWebSocketConnection_OtherStatus(t *testing.T) {
	// Reset metrics before test
	websocketConnectionsTotal.Reset()
	websocketConnectionsActive.Set(0)

	RecordWebSocketConnection("error")

	// Check counter
	count := testutil.ToFloat64(websocketConnectionsTotal.WithLabelValues("error"))
	assert.Equal(t, 1.0, count)

	// Check gauge (should not change)
	gauge := testutil.ToFloat64(websocketConnectionsActive)
	assert.Equal(t, 0.0, gauge)
}

func TestRecordWebSocketMessage(t *testing.T) {
	// Reset metrics before test
	websocketMessagesTotal.Reset()

	RecordWebSocketMessage("join", "success")

	// Check counter
	count := testutil.ToFloat64(websocketMessagesTotal.WithLabelValues("join", "success"))
	assert.Equal(t, 1.0, count)

	RecordWebSocketMessage("join", "success")
	count = testutil.ToFloat64(websocketMessagesTotal.WithLabelValues("join", "success"))
	assert.Equal(t, 2.0, count)

	RecordWebSocketMessage("msg", "error")
	errorCount := testutil.ToFloat64(websocketMessagesTotal.WithLabelValues("msg", "error"))
	assert.Equal(t, 1.0, errorCount)
}

func TestRecordWebSocketRoom(t *testing.T) {
	// Reset metrics before test
	websocketRoomsActive.Set(0)

	RecordWebSocketRoom(5)

	// Check gauge
	gauge := testutil.ToFloat64(websocketRoomsActive)
	assert.Equal(t, 5.0, gauge)

	RecordWebSocketRoom(10)
	gauge = testutil.ToFloat64(websocketRoomsActive)
	assert.Equal(t, 10.0, gauge)

	RecordWebSocketRoom(0)
	gauge = testutil.ToFloat64(websocketRoomsActive)
	assert.Equal(t, 0.0, gauge)
}

func TestRecordWebSocketError(t *testing.T) {
	// Reset metrics before test
	websocketErrorsTotal.Reset()

	RecordWebSocketError("unauthorized")

	// Check counter
	count := testutil.ToFloat64(websocketErrorsTotal.WithLabelValues("unauthorized"))
	assert.Equal(t, 1.0, count)

	RecordWebSocketError("unauthorized")
	count = testutil.ToFloat64(websocketErrorsTotal.WithLabelValues("unauthorized"))
	assert.Equal(t, 2.0, count)

	RecordWebSocketError("upgrade")
	upgradeCount := testutil.ToFloat64(websocketErrorsTotal.WithLabelValues("upgrade"))
	assert.Equal(t, 1.0, upgradeCount)
}

func TestMetrics_ConcurrentRecording(t *testing.T) {
	// Reset metrics before test
	websocketConnectionsTotal.Reset()
	websocketConnectionsActive.Set(0)
	websocketMessagesTotal.Reset()
	websocketRoomsActive.Set(0)
	websocketErrorsTotal.Reset()

	// Concurrent recording
	done := make(chan bool, 10)
	for i := 0; i < 10; i++ {
		go func() {
			RecordWebSocketConnection("opened")
			RecordWebSocketMessage("test", "success")
			RecordWebSocketRoom(1)
			RecordWebSocketError("test")
			done <- true
		}()
	}

	// Wait for all goroutines
	for i := 0; i < 10; i++ {
		<-done
	}

	// Check that all metrics were recorded
	connectionCount := testutil.ToFloat64(websocketConnectionsTotal.WithLabelValues("opened"))
	assert.Equal(t, 10.0, connectionCount)

	messageCount := testutil.ToFloat64(websocketMessagesTotal.WithLabelValues("test", "success"))
	assert.Equal(t, 10.0, messageCount)

	errorCount := testutil.ToFloat64(websocketErrorsTotal.WithLabelValues("test"))
	assert.Equal(t, 10.0, errorCount)
}

func TestMetrics_Registry(t *testing.T) {
	// This test verifies that metrics are created with promauto
	// which automatically registers them in the default registry
	// We can't easily test this without accessing internal state,
	// but we can verify that the metrics exist and can be collected

	// Reset and record
	websocketConnectionsTotal.Reset()
	websocketConnectionsActive.Set(0)
	websocketMessagesTotal.Reset()
	websocketRoomsActive.Set(0)
	websocketErrorsTotal.Reset()

	RecordWebSocketConnection("opened")
	RecordWebSocketMessage("test", "success")
	RecordWebSocketRoom(5)
	RecordWebSocketError("test")

	// Verify metrics can be read
	assert.Greater(t, testutil.ToFloat64(websocketConnectionsTotal.WithLabelValues("opened")), 0.0)
	assert.Greater(t, testutil.ToFloat64(websocketConnectionsActive), 0.0)
	assert.Greater(t, testutil.ToFloat64(websocketMessagesTotal.WithLabelValues("test", "success")), 0.0)
	assert.Greater(t, testutil.ToFloat64(websocketRoomsActive), 0.0)
	assert.Greater(t, testutil.ToFloat64(websocketErrorsTotal.WithLabelValues("test")), 0.0)
}
