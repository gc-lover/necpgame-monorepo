package server

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/testutil"
)

// Issue: #1591 - ensure metrics middleware records status and duration
func TestMetricsMiddlewareRecordsMetrics(t *testing.T) {
	reg := prometheus.NewRegistry()
	inventoryRequestsTotal = prometheus.NewCounterVec(
		prometheus.CounterOpts{Name: "inventory_requests_total"},
		[]string{"method", "endpoint", "status"},
	)
	inventoryRequestDuration = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "inventory_request_duration_seconds",
			Buckets: prometheus.ExponentialBuckets(0.001, 2, 10),
		},
		[]string{"method", "endpoint"},
	)
	if err := reg.Register(inventoryRequestsTotal); err != nil {
		t.Fatalf("failed to register counter: %v", err)
	}
	if err := reg.Register(inventoryRequestDuration); err != nil {
		t.Fatalf("failed to register histogram: %v", err)
	}

	handler := http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusCreated)
	})

	ts := httptest.NewServer(MetricsMiddleware(handler))
	defer ts.Close()

	req, err := http.NewRequest(http.MethodGet, ts.URL+"/test-endpoint", nil)
	if err != nil {
		t.Fatalf("failed to create request: %v", err)
	}

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		t.Fatalf("request failed: %v", err)
	}
	_ = resp.Body.Close()

	if got := testutil.CollectAndCount(inventoryRequestsTotal); got == 0 {
		t.Fatalf("expected inventoryRequestsTotal to be incremented")
	}
	if got := testutil.CollectAndCount(inventoryRequestDuration); got == 0 {
		t.Fatalf("expected inventoryRequestDuration to have observations")
	}
}
