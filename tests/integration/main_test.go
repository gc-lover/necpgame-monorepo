// Main integration test suite for NECPGAME services
// Runs comprehensive validation of service-to-service communication

package integration

import (
	"os"
	"testing"

	"github.com/stretchr/testify/require"
)

// TestMain sets up the integration test environment
func TestMain(m *testing.M) {
	// Setup test environment
	err := SetupTestServices()
	if err != nil {
		panic("Failed to setup test services: " + err.Error())
	}

	// Run tests
	code := m.Run()

	// Cleanup if needed
	os.Exit(code)
}

// TestServiceIntegrationSuite runs all service integration tests
func TestServiceIntegrationSuite(t *testing.T) {
	t.Run("AnalyticsServiceHealth", TestAnalyticsServiceHealth)
	t.Run("AnalyticsDashboardAPIIntegration", TestAnalyticsDashboardAPIIntegration)
	t.Run("ConcurrentAnalyticsRequests", TestConcurrentAnalyticsRequests)
	t.Run("CyberspaceEasterEggsIntegration", TestCyberspaceEasterEggsIntegration)
	t.Run("WorldEventsServiceIntegration", TestWorldEventsServiceIntegration)
	t.Run("ServiceDiscovery", TestServiceDiscovery)
	t.Run("AnalyticsLatencyValidation", TestAnalyticsLatencyValidation)
	t.Run("CrossServiceCommunication", TestCrossServiceCommunication)
}

// TestPerformanceSuite runs performance validation tests
func TestPerformanceSuite(t *testing.T) {
	t.Run("ConcurrentLoadTest", func(t *testing.T) {
		err := BenchmarkConcurrentRequests("http://localhost:8091/health", 10, 20)
		require.NoError(t, err)
	})

	t.Run("LatencyBenchmark", func(t *testing.T) {
		latencies, err := MeasureLatency("http://localhost:8091/health", 50)
		require.NoError(t, err)

		err = ValidateMMOFPSLatency(latencies)
		require.NoError(t, err)
	})
}

// TestServiceMeshSuite runs service mesh and communication tests
func TestServiceMeshSuite(t *testing.T) {
	t.Run("APIGatewayRouting", TestAPIGatewayRouting)
	t.Run("ServiceHealthCheckCircuitBreaker", TestServiceHealthCheckCircuitBreaker)
	t.Run("LoadBalancingBehavior", TestLoadBalancingBehavior)
	t.Run("ServiceFaultTolerance", TestServiceFaultTolerance)
	t.Run("ServiceMetricsCollection", TestServiceMetricsCollection)
	t.Run("DatabaseConnectivity", TestDatabaseConnectivity)
	t.Run("RedisConnectivity", TestRedisConnectivity)
	t.Run("EventStreamingIntegration", TestEventStreamingIntegration)
}
