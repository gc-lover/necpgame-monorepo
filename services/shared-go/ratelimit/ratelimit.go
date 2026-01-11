// Distributed API Rate Limiting with Redis
// Issue: #2027
// PERFORMANCE: Distributed rate limiting, DDoS protection, circuit breakers

package ratelimit

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/go-faster/errors"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// LimiterConfig holds configuration for rate limiter
type LimiterConfig struct {
	Redis       *redis.Client
	Logger      *zap.Logger
	// Rate limiting
	Rate        int           // Requests per window
	Window      time.Duration // Time window
	Burst       int           // Burst limit (default: same as rate)
	// Distributed settings
	KeyPrefix   string // Redis key prefix (default: "ratelimit:")
	MaxRetries  int    // Redis operation retries (default: 3)
	// Circuit breaker
	CircuitBreakerEnabled bool   // Enable circuit breaker
	FailureThreshold      int    // Failures before opening circuit
	RecoveryTimeout       time.Duration // Time before attempting recovery
}

// Limiter provides distributed rate limiting with Redis
type Limiter struct {
	config LimiterConfig
	redis  *redis.Client
	logger *zap.Logger

	// Circuit breaker state
	circuitOpen      bool
	circuitFailures  int
	circuitMutex     sync.RWMutex
	lastFailureTime  time.Time
}

// NewLimiter creates a new distributed rate limiter
func NewLimiter(config LimiterConfig) (*Limiter, error) {
	if config.Redis == nil {
		return nil, errors.New("redis client is required")
	}
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}
	if config.Rate <= 0 {
		return nil, errors.New("rate must be positive")
	}
	if config.Window <= 0 {
		config.Window = time.Minute
	}
	if config.Burst <= 0 {
		config.Burst = config.Rate
	}
	if config.KeyPrefix == "" {
		config.KeyPrefix = "ratelimit:"
	}
	if config.MaxRetries <= 0 {
		config.MaxRetries = 3
	}

	return &Limiter{
		config: config,
		redis:  config.Redis,
		logger: config.Logger,
	}, nil
}

// Allow checks if a request is allowed under rate limit
func (l *Limiter) Allow(ctx context.Context, key string) (bool, error) {
	// Check circuit breaker
	if l.config.CircuitBreakerEnabled {
		if !l.allowCircuit() {
			l.logger.Debug("Circuit breaker open, denying request",
				zap.String("key", key))
			return false, nil
		}
	}

	// Create Redis key
	redisKey := fmt.Sprintf("%s%s", l.config.KeyPrefix, key)

	// Use Redis INCR with TTL for distributed rate limiting
	pipe := l.redis.Pipeline()
	
	// Increment counter
	incr := pipe.Incr(ctx, redisKey)
	
	// Set TTL if this is first request
	pipe.Expire(ctx, redisKey, l.config.Window)
	
	// Execute pipeline
	_, err := pipe.Exec(ctx)
	if err != nil {
		l.recordFailure()
		return false, errors.Wrap(err, "failed to check rate limit")
	}

	count := incr.Val()

	// Check if limit exceeded
	if count > int64(l.config.Rate) {
		// Check burst allowance
		if count > int64(l.config.Rate+l.config.Burst) {
			l.recordFailure()
			l.logger.Debug("Rate limit exceeded",
				zap.String("key", key),
				zap.Int64("count", count),
				zap.Int("rate", l.config.Rate))
			return false, nil
		}
		// Allow burst but log warning
		l.logger.Warn("Burst limit reached",
			zap.String("key", key),
			zap.Int64("count", count),
			zap.Int("rate", l.config.Rate),
			zap.Int("burst", l.config.Burst))
	}

	// Reset circuit breaker on success
	l.recordSuccess()

	return true, nil
}

// AllowN checks if N requests are allowed under rate limit
func (l *Limiter) AllowN(ctx context.Context, key string, n int) (bool, error) {
	if n <= 0 {
		return true, nil
	}

	// Check circuit breaker
	if l.config.CircuitBreakerEnabled {
		if !l.allowCircuit() {
			return false, nil
		}
	}

	redisKey := fmt.Sprintf("%s%s", l.config.KeyPrefix, key)

	// Use Redis INCRBY for batch check
	pipe := l.redis.Pipeline()
	
	incrBy := pipe.IncrBy(ctx, redisKey, int64(n))
	pipe.Expire(ctx, redisKey, l.config.Window)
	
	_, err := pipe.Exec(ctx)
	if err != nil {
		l.recordFailure()
		return false, errors.Wrap(err, "failed to check rate limit")
	}

	count := incrBy.Val()

	// Check limit
	if count > int64(l.config.Rate+l.config.Burst) {
		l.recordFailure()
		return false, nil
	}

	l.recordSuccess()
	return true, nil
}

// Reset resets the rate limit counter for a key
func (l *Limiter) Reset(ctx context.Context, key string) error {
	redisKey := fmt.Sprintf("%s%s", l.config.KeyPrefix, key)
	return l.redis.Del(ctx, redisKey).Err()
}

// GetRemaining returns remaining requests allowed for a key
func (l *Limiter) GetRemaining(ctx context.Context, key string) (int, error) {
	redisKey := fmt.Sprintf("%s%s", l.config.KeyPrefix, key)
	
	count, err := l.redis.Get(ctx, redisKey).Int64()
	if err == redis.Nil {
		return l.config.Rate, nil
	}
	if err != nil {
		return 0, errors.Wrap(err, "failed to get rate limit count")
	}

	remaining := l.config.Rate - int(count)
	if remaining < 0 {
		remaining = 0
	}

	return remaining, nil
}

// GetTTL returns TTL for a rate limit key
func (l *Limiter) GetTTL(ctx context.Context, key string) (time.Duration, error) {
	redisKey := fmt.Sprintf("%s%s", l.config.KeyPrefix, key)
	
	ttl, err := l.redis.TTL(ctx, redisKey).Result()
	if err != nil {
		return 0, errors.Wrap(err, "failed to get TTL")
	}

	return ttl, nil
}

// allowCircuit checks if circuit breaker allows request
func (l *Limiter) allowCircuit() bool {
	if !l.config.CircuitBreakerEnabled {
		return true
	}

	l.circuitMutex.RLock()
	defer l.circuitMutex.RUnlock()

	if !l.circuitOpen {
		return true
	}

	// Check if recovery timeout passed
	if time.Since(l.lastFailureTime) > l.config.RecoveryTimeout {
		// Attempt recovery (half-open state)
		l.circuitMutex.RUnlock()
		l.circuitMutex.Lock()
		if l.circuitOpen && time.Since(l.lastFailureTime) > l.config.RecoveryTimeout {
			l.circuitOpen = false
			l.circuitFailures = 0
			l.logger.Info("Circuit breaker recovered, allowing requests")
		}
		l.circuitMutex.Unlock()
		l.circuitMutex.RLock()
		return !l.circuitOpen
	}

	return false
}

// recordFailure records a failure for circuit breaker
func (l *Limiter) recordFailure() {
	if !l.config.CircuitBreakerEnabled {
		return
	}

	l.circuitMutex.Lock()
	defer l.circuitMutex.Unlock()

	l.circuitFailures++
	l.lastFailureTime = time.Now()

	if l.circuitFailures >= l.config.FailureThreshold && !l.circuitOpen {
		l.circuitOpen = true
		l.logger.Warn("Circuit breaker opened due to failures",
			zap.Int("failures", l.circuitFailures),
			zap.Int("threshold", l.config.FailureThreshold))
	}
}

// recordSuccess records a success for circuit breaker
func (l *Limiter) recordSuccess() {
	if !l.config.CircuitBreakerEnabled {
		return
	}

	l.circuitMutex.Lock()
	defer l.circuitMutex.Unlock()

	if l.circuitOpen {
		// Reset on success in half-open state
		l.circuitOpen = false
		l.circuitFailures = 0
		l.logger.Info("Circuit breaker closed after successful request")
	} else if l.circuitFailures > 0 {
		// Decrement failure count on success
		l.circuitFailures--
	}
}

// IsBlocked checks if a key is currently blocked
func (l *Limiter) IsBlocked(ctx context.Context, key string) (bool, error) {
	redisKey := fmt.Sprintf("%s%s", l.config.KeyPrefix, key)
	
	count, err := l.redis.Get(ctx, redisKey).Int64()
	if err == redis.Nil {
		return false, nil
	}
	if err != nil {
		return false, errors.Wrap(err, "failed to check if blocked")
	}

	return count > int64(l.config.Rate+l.config.Burst), nil
}
