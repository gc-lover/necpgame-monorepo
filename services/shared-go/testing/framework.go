// Automated Testing Framework
// Issue: #2144
// PERFORMANCE: Unit tests, integration tests, performance benchmarks
// Enterprise-grade testing framework for all Go services

package testing

import (
	"context"
	"fmt"
	"testing"
	"time"
)

// TestConfig holds test configuration
type TestConfig struct {
	// Test settings
	Timeout      time.Duration
	Parallel     bool
	Verbose      bool
	SkipSlow     bool

	// Performance benchmarks
	BenchmarkDuration time.Duration
	BenchmarkRuns     int

	// Integration test settings
	IntegrationEnabled bool
	TestDatabaseURL    string
	TestRedisURL       string
}

// DefaultTestConfig returns default test configuration
func DefaultTestConfig() TestConfig {
	return TestConfig{
		Timeout:            30 * time.Second,
		Parallel:           false,
		Verbose:            false,
		SkipSlow:           false,
		BenchmarkDuration:  1 * time.Second,
		BenchmarkRuns:      5,
		IntegrationEnabled: false,
	}
}

// TestSuite represents a test suite
type TestSuite struct {
	Name        string
	UnitTests   []UnitTest
	IntegrationTests []IntegrationTest
	Benchmarks  []Benchmark
	Setup       func(ctx context.Context) error
	Teardown    func(ctx context.Context) error
}

// UnitTest represents a unit test
type UnitTest struct {
	Name    string
	Test    func(t *testing.T)
	Skip    bool
	Timeout time.Duration
}

// IntegrationTest represents an integration test
type IntegrationTest struct {
	Name    string
	Test    func(ctx context.Context, t *testing.T) error
	Skip    bool
	Timeout time.Duration
	Requires []string // Required services (database, redis, etc.)
}

// Benchmark represents a performance benchmark
type Benchmark struct {
	Name    string
	Bench   func(b *testing.B)
	Skip    bool
	Target  time.Duration // Target duration for benchmark
}

// RunTestSuite runs a complete test suite
func RunTestSuite(ctx context.Context, suite TestSuite, config TestConfig) error {
	if suite.Setup != nil {
		if err := suite.Setup(ctx); err != nil {
			return fmt.Errorf("test setup failed: %w", err)
		}
		defer func() {
			if suite.Teardown != nil {
				suite.Teardown(ctx)
			}
		}()
	}

	// Run unit tests
	for _, test := range suite.UnitTests {
		if test.Skip || (config.SkipSlow && test.Timeout > 10*time.Second) {
			continue
		}

		t := &testing.T{}
		testCtx := ctx
		if test.Timeout > 0 {
			var cancel context.CancelFunc
			testCtx, cancel = context.WithTimeout(ctx, test.Timeout)
			defer cancel()
		}

		test.Test(t)
		if t.Failed() {
			return fmt.Errorf("unit test failed: %s", test.Name)
		}
	}

	// Run integration tests
	if config.IntegrationEnabled {
		for _, test := range suite.IntegrationTests {
			if test.Skip {
				continue
			}

			t := &testing.T{}
			testCtx := ctx
			if test.Timeout > 0 {
				var cancel context.CancelFunc
				testCtx, cancel = context.WithTimeout(ctx, test.Timeout)
				defer cancel()
			}

			if err := test.Test(testCtx, t); err != nil {
				return fmt.Errorf("integration test failed: %s: %w", test.Name, err)
			}
		}
	}

	return nil
}

// RunBenchmark runs a performance benchmark
func RunBenchmark(bench Benchmark, config TestConfig) (time.Duration, error) {
	if bench.Skip {
		return 0, fmt.Errorf("benchmark skipped: %s", bench.Name)
	}

	b := &testing.B{}
	bench.Bench(b)

	if b.N == 0 {
		return 0, fmt.Errorf("benchmark failed: %s", bench.Name)
	}

	avgDuration := b.Elapsed() / time.Duration(b.N)

	if bench.Target > 0 && avgDuration > bench.Target {
		return avgDuration, fmt.Errorf("benchmark exceeded target: %s (got %v, target %v)", bench.Name, avgDuration, bench.Target)
	}

	return avgDuration, nil
}

// AssertEqual asserts that two values are equal
func AssertEqual(t *testing.T, expected, actual interface{}, msg string) {
	if expected != actual {
		t.Errorf("%s: expected %v, got %v", msg, expected, actual)
	}
}

// AssertNotNil asserts that a value is not nil
func AssertNotNil(t *testing.T, value interface{}, msg string) {
	if value == nil {
		t.Errorf("%s: expected non-nil value", msg)
	}
}

// AssertNil asserts that a value is nil
func AssertNil(t *testing.T, value interface{}, msg string) {
	if value != nil {
		t.Errorf("%s: expected nil value, got %v", msg, value)
	}
}

// AssertError asserts that an error occurred
func AssertError(t *testing.T, err error, msg string) {
	if err == nil {
		t.Errorf("%s: expected error", msg)
	}
}

// AssertNoError asserts that no error occurred
func AssertNoError(t *testing.T, err error, msg string) {
	if err != nil {
		t.Errorf("%s: unexpected error: %v", msg, err)
	}
}

// AssertTrue asserts that a condition is true
func AssertTrue(t *testing.T, condition bool, msg string) {
	if !condition {
		t.Errorf("%s: expected true", msg)
	}
}

// AssertFalse asserts that a condition is false
func AssertFalse(t *testing.T, condition bool, msg string) {
	if condition {
		t.Errorf("%s: expected false", msg)
	}
}

// AssertWithinDuration asserts that two times are within a duration
func AssertWithinDuration(t *testing.T, expected, actual time.Time, delta time.Duration, msg string) {
	diff := actual.Sub(expected)
	if diff < 0 {
		diff = -diff
	}
	if diff > delta {
		t.Errorf("%s: times not within %v: expected %v, got %v", msg, delta, expected, actual)
	}
}
