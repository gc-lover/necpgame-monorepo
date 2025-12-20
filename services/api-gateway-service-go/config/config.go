// Package config Issue: #146073424
package config

import (
	"fmt"
	"sync"
	"time"
)

// Config содержит конфигурацию API Gateway
type Config struct {
	ServerPort              int
	JWTSecret               string
	RateLimitRPM            int
	CircuitBreakerThreshold int
	Services                map[string]*ServiceConfig
}

// ServiceConfig содержит конфигурацию для каждого микросервиса
type ServiceConfig struct {
	URL            string
	HealthCheck    string
	Timeout        time.Duration
	MaxRetries     int
	CircuitBreaker *CircuitBreaker
}

// CircuitBreaker реализует паттерн circuit breaker
type CircuitBreaker struct {
	mu           sync.RWMutex
	failures     int
	lastFailTime time.Time
	state        string // "closed", "open", "half-open"
	threshold    int
	timeout      time.Duration
}

// NewCircuitBreaker создает новый circuit breaker
func NewCircuitBreaker(threshold int, timeout time.Duration) *CircuitBreaker {
	return &CircuitBreaker{
		state:     "closed",
		threshold: threshold,
		timeout:   timeout,
	}
}

// Call выполняет вызов через circuit breaker
func (cb *CircuitBreaker) Call(fn func() error) error {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	switch cb.state {
	case "open":
		if time.Since(cb.lastFailTime) > cb.timeout {
			cb.state = "half-open"
		} else {
			return fmt.Errorf("circuit breaker is open")
		}
	case "half-open":
		// Allow one call to test
	}

	err := fn()
	if err != nil {
		cb.failures++
		cb.lastFailTime = time.Now()
		if cb.failures >= cb.threshold {
			cb.state = "open"
		}
		return err
	}

	// Success - reset failures and close circuit
	cb.failures = 0
	cb.state = "closed"
	return nil
}
