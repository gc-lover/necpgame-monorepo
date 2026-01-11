// Performance Benchmarking Utilities
// Issue: #2144
// PERFORMANCE: Performance benchmarks, latency measurement, throughput testing

package testing

import (
	"context"
	"fmt"
	"sync"
	"sync/atomic"
	"time"
)

// BenchmarkResult represents benchmark results
type BenchmarkResult struct {
	Name         string
	Operations   int64
	Duration     time.Duration
	OpsPerSecond float64
	AvgLatency   time.Duration
	P50Latency   time.Duration
	P95Latency   time.Duration
	P99Latency   time.Duration
	Errors       int64
}

// RunLatencyBenchmark runs a latency benchmark
func RunLatencyBenchmark(ctx context.Context, name string, duration time.Duration, fn func() error) (*BenchmarkResult, error) {
	deadline := time.Now().Add(duration)
	var operations int64
	var errors int64
	var latencies []time.Duration
	var mu sync.Mutex

	for time.Now().Before(deadline) {
		start := time.Now()
		err := fn()
		latency := time.Since(start)

		atomic.AddInt64(&operations, 1)
		if err != nil {
			atomic.AddInt64(&errors, 1)
		}

		mu.Lock()
		latencies = append(latencies, latency)
		mu.Unlock()
	}

	if len(latencies) == 0 {
		return nil, fmt.Errorf("no operations completed")
	}

	// Calculate percentiles
	mu.Lock()
	sorted := make([]time.Duration, len(latencies))
	copy(sorted, latencies)
	mu.Unlock()

	sortDurations(sorted)

	totalDuration := time.Since(time.Now().Add(-duration))
	opsPerSecond := float64(operations) / totalDuration.Seconds()

	result := &BenchmarkResult{
		Name:         name,
		Operations:   operations,
		Duration:     totalDuration,
		OpsPerSecond: opsPerSecond,
		AvgLatency:   averageDuration(sorted),
		P50Latency:   percentile(sorted, 0.50),
		P95Latency:   percentile(sorted, 0.95),
		P99Latency:   percentile(sorted, 0.99),
		Errors:       errors,
	}

	return result, nil
}

// RunThroughputBenchmark runs a throughput benchmark
func RunThroughputBenchmark(ctx context.Context, name string, duration time.Duration, concurrency int, fn func() error) (*BenchmarkResult, error) {
	deadline := time.Now().Add(duration)
	var operations int64
	var errors int64
	var latencies []time.Duration
	var mu sync.Mutex
	var wg sync.WaitGroup

	// Start concurrent workers
	for i := 0; i < concurrency; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for time.Now().Before(deadline) {
				start := time.Now()
				err := fn()
				latency := time.Since(start)

				atomic.AddInt64(&operations, 1)
				if err != nil {
					atomic.AddInt64(&errors, 1)
				}

				mu.Lock()
				latencies = append(latencies, latency)
				mu.Unlock()
			}
		}()
	}

	wg.Wait()

	if len(latencies) == 0 {
		return nil, fmt.Errorf("no operations completed")
	}

	// Calculate percentiles
	mu.Lock()
	sorted := make([]time.Duration, len(latencies))
	copy(sorted, latencies)
	mu.Unlock()

	sortDurations(sorted)

	totalDuration := time.Since(time.Now().Add(-duration))
	opsPerSecond := float64(operations) / totalDuration.Seconds()

	result := &BenchmarkResult{
		Name:         name,
		Operations:   operations,
		Duration:     totalDuration,
		OpsPerSecond: opsPerSecond,
		AvgLatency:   averageDuration(sorted),
		P50Latency:   percentile(sorted, 0.50),
		P95Latency:   percentile(sorted, 0.95),
		P99Latency:   percentile(sorted, 0.99),
		Errors:       errors,
	}

	return result, nil
}

// Helper functions

func sortDurations(durations []time.Duration) {
	// Simple insertion sort for small arrays
	for i := 1; i < len(durations); i++ {
		key := durations[i]
		j := i - 1
		for j >= 0 && durations[j] > key {
			durations[j+1] = durations[j]
			j--
		}
		durations[j+1] = key
	}
}

func averageDuration(durations []time.Duration) time.Duration {
	if len(durations) == 0 {
		return 0
	}
	var sum time.Duration
	for _, d := range durations {
		sum += d
	}
	return sum / time.Duration(len(durations))
}

func percentile(durations []time.Duration, p float64) time.Duration {
	if len(durations) == 0 {
		return 0
	}
	index := int(float64(len(durations)) * p)
	if index >= len(durations) {
		index = len(durations) - 1
	}
	return durations[index]
}
