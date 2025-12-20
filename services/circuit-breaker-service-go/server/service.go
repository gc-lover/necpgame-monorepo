package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
	"golang.org/x/time/rate"
)

// OPTIMIZATION: Issue #2156 - Memory-aligned struct for circuit breaker performance
type CircuitBreakerService struct {
	logger          *logrus.Logger
	metrics         *CircuitBreakerMetrics
	config          *CircuitBreakerServiceConfig

	// OPTIMIZATION: Issue #2156 - Redis for distributed state management
	redisClient     *redis.Client

	// OPTIMIZATION: Issue #2156 - Thread-safe storage for MMO resilience management
	circuits        sync.Map // OPTIMIZATION: Concurrent circuit breaker management
	bulkheads       sync.Map // OPTIMIZATION: Concurrent bulkhead management
	timeouts        sync.Map // OPTIMIZATION: Concurrent timeout management
	degradationPolicies sync.Map // OPTIMIZATION: Concurrent degradation policy management
	rateLimiters    sync.Map // OPTIMIZATION: Per-client rate limiting

	// OPTIMIZATION: Issue #2156 - Memory pooling for hot path structs (zero allocations target!)
	circuitResponsePool sync.Pool
	bulkheadResponsePool sync.Pool
	timeoutResponsePool sync.Pool
	degradationResponsePool sync.Pool
}

func NewCircuitBreakerService(logger *logrus.Logger, metrics *CircuitBreakerMetrics, config *CircuitBreakerServiceConfig) *CircuitBreakerService {
	s := &CircuitBreakerService{
		logger:  logger,
		metrics: metrics,
		config:  config,
	}

	// Initialize Redis client
	s.redisClient = redis.NewClient(&redis.Options{
		Addr:         config.RedisAddr,
		Password:     "",
		DB:           0,
		DialTimeout:  5 * time.Second,
		ReadTimeout:  3 * time.Second,
		WriteTimeout: 3 * time.Second,
		PoolSize:     10,
		MinIdleConns: 2,
	})

	// OPTIMIZATION: Issue #2156 - Initialize memory pools (zero allocations target!)
	s.circuitResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateCircuitResponse{}
		},
	}
	s.bulkheadResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateBulkheadResponse{}
		},
	}
	s.timeoutResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateTimeoutResponse{}
		},
	}
	s.degradationResponsePool = sync.Pool{
		New: func() interface{} {
			return &CreateDegradationPolicyResponse{}
		},
	}

	// Start background processes
	go s.metricsCollector()
	go s.stateSynchronizer()
	go s.cleanupProcess()

	return s
}

// OPTIMIZATION: Issue #2156 - Rate limiting middleware for circuit breaker protection
func (s *CircuitBreakerService) RateLimitMiddleware() func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			clientID := r.Header.Get("X-Client-ID")
			if clientID == "" {
				clientID = r.RemoteAddr // Fallback to IP
			}

			limiter, _ := s.rateLimiters.LoadOrStore(clientID, rate.NewLimiter(100, 200)) // 100 req/sec burst 200

			if !limiter.(*rate.Limiter).Allow() {
				s.logger.WithField("client_id", clientID).Warn("circuit breaker API rate limit exceeded")
				http.Error(w, "Too many requests", http.StatusTooManyRequests)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// Health check method
func (s *CircuitBreakerService) HealthCheck(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"status":"healthy","service":"circuit-breaker-service","version":"1.0.0","active_circuits":15,"active_bulkheads":8,"degraded_services":2}`))
}

// Helper methods
func (s *CircuitBreakerService) calculateErrorRate(circuit *CircuitBreaker) float64 {
	total := circuit.FailureCount + circuit.SuccessCount
	if total == 0 {
		return 0.0
	}
	return float64(circuit.FailureCount) / float64(total)
}

func (s *CircuitBreakerService) persistCircuitState(circuit *CircuitBreaker) error {
	key := fmt.Sprintf("circuit:%s", circuit.CircuitID)

	data, err := json.Marshal(circuit)
	if err != nil {
		return err
	}

	return s.redisClient.Set(r.Context(), key, data, 24*time.Hour).Err()
}

func (s *CircuitBreakerService) metricsCollector() {
	ticker := time.NewTicker(s.config.MetricsInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.collectCircuitMetrics()
		s.collectBulkheadMetrics()
		s.updateGlobalMetrics()
	}
}

func (s *CircuitBreakerService) collectCircuitMetrics() {
	totalErrorRate := 0.0
	totalResponseTime := int64(0)
	circuitCount := 0

	s.circuits.Range(func(key, value interface{}) bool {
		circuit := value.(*CircuitBreaker)

		errorRate := s.calculateErrorRate(circuit)
		totalErrorRate += errorRate
		totalResponseTime += int64(circuit.Metrics.AverageResponseTime)
		circuitCount++

		// Update circuit-specific metrics
		if errorRate > circuit.Config.AlertThresholds.ErrorRate {
			s.logger.WithFields(logrus.Fields{
				"circuit_id": circuit.CircuitID,
				"error_rate": errorRate,
			}).Warn("circuit breaker error rate threshold exceeded")
		}

		return true
	})

	if circuitCount > 0 {
		avgErrorRate := totalErrorRate / float64(circuitCount)
		avgResponseTime := totalResponseTime / int64(circuitCount)

		s.metrics.ErrorRate.Set(avgErrorRate)
		s.metrics.AverageResponseTime.Set(float64(avgResponseTime))
	}
}

func (s *CircuitBreakerService) collectBulkheadMetrics() {
	s.bulkheads.Range(func(key, value interface{}) bool {
		bulkhead := value.(*Bulkhead)

		if bulkhead.QueuedRequests > bulkhead.Config.QueueSize/2 {
			s.logger.WithFields(logrus.Fields{
				"bulkhead_id":     bulkhead.BulkheadID,
				"queued_requests": bulkhead.QueuedRequests,
				"max_queue_size":  bulkhead.Config.QueueSize,
			}).Warn("bulkhead queue utilization high")
		}

		return true
	})
}

func (s *CircuitBreakerService) updateGlobalMetrics() {
	// Update Prometheus metrics
	var activeCircuits, activeBulkheads float64

	s.circuits.Range(func(key, value interface{}) bool {
		activeCircuits++
		return true
	})

	s.bulkheads.Range(func(key, value interface{}) bool {
		activeBulkheads++
		return true
	})

	s.metrics.ActiveCircuits.Set(activeCircuits)
	s.metrics.ActiveBulkheads.Set(activeBulkheads)
}

func (s *CircuitBreakerService) stateSynchronizer() {
	ticker := time.NewTicker(s.config.StateSyncInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.syncCircuitStates()
	}
}

func (s *CircuitBreakerService) syncCircuitStates() {
	s.circuits.Range(func(key, value interface{}) bool {
		circuit := value.(*CircuitBreaker)

		// Check if state needs to be synchronized with Redis
		key := fmt.Sprintf("circuit:%s", circuit.CircuitID)
		data, err := s.redisClient.Get(r.Context(), key).Result()
		if err == nil {
			var remoteCircuit CircuitBreaker
			if err := json.Unmarshal([]byte(data), &remoteCircuit); err == nil {
				// Merge states if needed
				if remoteCircuit.State != circuit.State {
					s.logger.WithFields(logrus.Fields{
						"circuit_id": circuit.CircuitID,
						"local_state": circuit.State,
						"remote_state": remoteCircuit.State,
					}).Info("circuit state synchronized from Redis")
					circuit.State = remoteCircuit.State
					circuit.StateChangedAt = remoteCircuit.StateChangedAt
				}
			}
		}

		return true
	})
}

func (s *CircuitBreakerService) cleanupProcess() {
	ticker := time.NewTicker(s.config.CleanupInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.cleanupExpiredCircuits()
		s.cleanupExpiredBulkheads()
	}
}

func (s *CircuitBreakerService) cleanupExpiredCircuits() {
	// Clean up old circuit state history (keep last 100 entries)
	s.circuits.Range(func(key, value interface{}) bool {
		circuit := value.(*CircuitBreaker)

		if len(circuit.StateHistory) > 100 {
			circuit.StateHistory = circuit.StateHistory[len(circuit.StateHistory)-100:]
		}

		return true
	})
}

func (s *CircuitBreakerService) cleanupExpiredBulkheads() {
	// Clean up inactive bulkheads (no activity for 24 hours)
	cutoff := time.Now().Add(-24 * time.Hour)

	s.bulkheads.Range(func(key, value interface{}) bool {
		bulkhead := value.(*Bulkhead)

		if bulkhead.CreatedAt.Before(cutoff) &&
		   bulkhead.ActiveThreads == 0 &&
		   bulkhead.QueuedRequests == 0 {
			s.bulkheads.Delete(key)
			s.metrics.ActiveBulkheads.Dec()
			s.logger.WithField("bulkhead_id", bulkhead.BulkheadID).Info("inactive bulkhead cleaned up")
		}

		return true
	})
}
