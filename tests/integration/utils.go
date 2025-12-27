// Integration test utilities for NECPGAME services
// Provides common test helpers and setup functions

package integration

import (
	"context"
	"fmt"
	"net/http"
	"sync"
	"time"
)

// ServiceConfig holds configuration for service endpoints
type ServiceConfig struct {
	Name     string
	URL      string
	Timeout  time.Duration
	Required bool
}

// DefaultServiceConfigs returns default service configurations for integration tests
func DefaultServiceConfigs() []ServiceConfig {
	return []ServiceConfig{
		{
			Name:     "analytics-dashboard",
			URL:      "http://localhost:8091",
			Timeout:  5 * time.Second,
			Required: true,
		},
		{
			Name:     "cyberspace-easter-eggs",
			URL:      "http://localhost:8080",
			Timeout:  5 * time.Second,
			Required: false, // May require auth
		},
		{
			Name:     "world-events",
			URL:      "http://localhost:8070",
			Timeout:  5 * time.Second,
			Required: false,
		},
		{
			Name:     "guild-service",
			URL:      "http://localhost:8060",
			Timeout:  5 * time.Second,
			Required: false,
		},
	}
}

// CheckServiceHealth checks if a service is healthy and responding
func CheckServiceHealth(config ServiceConfig) (bool, int, error) {
	client := &http.Client{Timeout: config.Timeout}

	resp, err := client.Get(config.URL + "/health")
	if err != nil {
		return false, 0, fmt.Errorf("service %s not reachable: %w", config.Name, err)
	}
	defer resp.Body.Close()

	healthy := resp.StatusCode == http.StatusOK || resp.StatusCode == http.StatusUnauthorized
	return healthy, resp.StatusCode, nil
}

// WaitForService waits for a service to become healthy
func WaitForService(config ServiceConfig, maxWait time.Duration) error {
	ctx, cancel := context.WithTimeout(context.Background(), maxWait)
	defer cancel()

	ticker := time.NewTicker(500 * time.Millisecond)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return fmt.Errorf("timeout waiting for service %s to become healthy", config.Name)
		case <-ticker.C:
			healthy, statusCode, err := CheckServiceHealth(config)
			if err != nil {
				continue
			}

			if healthy {
				return nil
			}

			// Log non-critical status codes
			if statusCode >= 500 {
				fmt.Printf("Service %s returned server error: %d\n", config.Name, statusCode)
			}
		}
	}
}

// SetupTestServices ensures all required services are running before tests
func SetupTestServices() error {
	configs := DefaultServiceConfigs()

	for _, config := range configs {
		if !config.Required {
			continue
		}

		fmt.Printf("Checking service: %s\n", config.Name)

		err := WaitForService(config, 30*time.Second)
		if err != nil {
			return fmt.Errorf("required service %s not available: %w", config.Name, err)
		}

		fmt.Printf("✓ Service %s is healthy\n", config.Name)
	}

	return nil
}

// MeasureLatency measures HTTP request latency with high precision
func MeasureLatency(url string, numRequests int) ([]time.Duration, error) {
	client := &http.Client{Timeout: 10 * time.Second}
	var latencies []time.Duration

	for i := 0; i < numRequests; i++ {
		start := time.Now()

		resp, err := client.Get(url)
		if err != nil {
			return nil, fmt.Errorf("request %d failed: %w", i, err)
		}
		resp.Body.Close()

		if resp.StatusCode != http.StatusOK && resp.StatusCode != http.StatusUnauthorized {
			return nil, fmt.Errorf("request %d returned status %d", i, resp.StatusCode)
		}

		latencies = append(latencies, time.Since(start))
	}

	return latencies, nil
}

// CalculatePercentile calculates P95, P99 percentiles from latency measurements
func CalculatePercentile(latencies []time.Duration, percentile float64) time.Duration {
	if len(latencies) == 0 {
		return 0
	}

	// Simple sort (not efficient but sufficient for tests)
	sorted := make([]time.Duration, len(latencies))
	copy(sorted, latencies)

	for i := 0; i < len(sorted)-1; i++ {
		for j := 0; j < len(sorted)-i-1; j++ {
			if sorted[j] > sorted[j+1] {
				sorted[j], sorted[j+1] = sorted[j+1], sorted[j]
			}
		}
	}

	index := int(float64(len(sorted)-1) * percentile / 100.0)
	if index >= len(sorted) {
		index = len(sorted) - 1
	}

	return sorted[index]
}

// ValidateMMOFPSLatency validates that latency meets MMOFPS requirements
func ValidateMMOFPSLatency(latencies []time.Duration) error {
	if len(latencies) == 0 {
		return fmt.Errorf("no latency measurements provided")
	}

	p95 := CalculatePercentile(latencies, 95)
	p99 := CalculatePercentile(latencies, 99)

	fmt.Printf("Latency validation - P95: %v, P99: %v\n", p95, p99)

	// MMOFPS requirements
	maxP95 := 50 * time.Millisecond  // P95 < 50ms for hot paths
	maxP99 := 100 * time.Millisecond // P99 < 100ms for analytics

	if p95 > maxP95 {
		return fmt.Errorf("P95 latency %v exceeds MMOFPS requirement of %v", p95, maxP95)
	}

	if p99 > maxP99 {
		return fmt.Errorf("P99 latency %v exceeds MMOFPS requirement of %v", p99, maxP99)
	}

	return nil
}

// BenchmarkConcurrentRequests tests concurrent request handling
func BenchmarkConcurrentRequests(url string, numGoroutines, requestsPerGoroutine int) error {
	var wg sync.WaitGroup
	errors := make(chan error, numGoroutines*requestsPerGoroutine)

	client := &http.Client{Timeout: 5 * time.Second}

	start := time.Now()

	for i := 0; i < numGoroutines; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()

			for j := 0; j < requestsPerGoroutine; j++ {
				resp, err := client.Get(url)
				if err != nil {
					errors <- fmt.Errorf("goroutine %d, request %d: %v", id, j, err)
					continue
				}
				resp.Body.Close()

				if resp.StatusCode != http.StatusOK && resp.StatusCode != http.StatusUnauthorized {
					errors <- fmt.Errorf("goroutine %d, request %d: status %d", id, j, resp.StatusCode)
					continue
				}
			}
		}(i)
	}

	wg.Wait()
	close(errors)

	totalRequests := numGoroutines * requestsPerGoroutine
	var errorCount int
	for range errors {
		errorCount++
	}

	duration := time.Since(start)
	rps := float64(totalRequests) / duration.Seconds()

	fmt.Printf("Concurrent load test: %d goroutines, %d requests each\n", numGoroutines, requestsPerGoroutine)
	fmt.Printf("Total requests: %d, Errors: %d, Duration: %v, RPS: %.1f\n",
		totalRequests, errorCount, duration, rps)

	if errorCount > 0 {
		return fmt.Errorf("%d/%d requests failed", errorCount, totalRequests)
	}

	return nil
}
