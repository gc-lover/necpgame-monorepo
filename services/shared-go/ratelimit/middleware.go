// HTTP Middleware for Distributed Rate Limiting
// Issue: #2027
// PERFORMANCE: HTTP middleware for distributed rate limiting

package ratelimit

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"github.com/go-faster/errors"
	"go.uber.org/zap"
)

// MiddlewareConfig holds configuration for rate limit middleware
type MiddlewareConfig struct {
	Limiter       *Limiter
	Logger        *zap.Logger
	// Key generation
	KeyFunc       func(r *http.Request) string // Function to generate rate limit key
	// Error handling
	OnLimitExceeded func(w http.ResponseWriter, r *http.Request) // Custom handler for rate limit exceeded
	// Headers
	IncludeHeaders bool // Include rate limit headers in response
}

// RateLimitMiddleware provides HTTP middleware for distributed rate limiting
func RateLimitMiddleware(config MiddlewareConfig) func(http.Handler) http.Handler {
	if config.Limiter == nil {
		panic("limiter is required")
	}
	if config.Logger == nil {
		panic("logger is required")
	}
	if config.KeyFunc == nil {
		config.KeyFunc = defaultKeyFunc
	}
	if config.OnLimitExceeded == nil {
		config.OnLimitExceeded = defaultOnLimitExceeded
	}

	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			ctx := r.Context()

			// Generate rate limit key
			key := config.KeyFunc(r)

			// Check rate limit
			allowed, err := config.Limiter.Allow(ctx, key)
			if err != nil {
				config.Logger.Error("Rate limit check failed",
					zap.Error(err),
					zap.String("key", key),
					zap.String("path", r.URL.Path))

				// Allow request on error (fail open)
				next.ServeHTTP(w, r)
				return
			}

			if !allowed {
				// Get remaining and TTL for headers
				var remaining int
				var resetTime time.Time
				if config.IncludeHeaders {
					remaining, _ = config.Limiter.GetRemaining(ctx, key)
					ttl, _ := config.Limiter.GetTTL(ctx, key)
					resetTime = time.Now().Add(ttl)
				}

				// Add rate limit headers
				if config.IncludeHeaders {
					w.Header().Set("X-RateLimit-Limit", fmt.Sprintf("%d", config.Limiter.config.Rate))
					w.Header().Set("X-RateLimit-Remaining", fmt.Sprintf("%d", max(0, remaining)))
					w.Header().Set("X-RateLimit-Reset", fmt.Sprintf("%d", resetTime.Unix()))
				}

				config.OnLimitExceeded(w, r)
				return
			}

			// Add rate limit headers on success
			if config.IncludeHeaders {
				remaining, _ := config.Limiter.GetRemaining(ctx, key)
				ttl, _ := config.Limiter.GetTTL(ctx, key)
				resetTime := time.Now().Add(ttl)

				w.Header().Set("X-RateLimit-Limit", fmt.Sprintf("%d", config.Limiter.config.Rate))
				w.Header().Set("X-RateLimit-Remaining", fmt.Sprintf("%d", max(0, remaining)))
				w.Header().Set("X-RateLimit-Reset", fmt.Sprintf("%d", resetTime.Unix()))
			}

			next.ServeHTTP(w, r)
		})
	}
}

// defaultKeyFunc generates rate limit key from request
func defaultKeyFunc(r *http.Request) string {
	// Use IP address as default key
	ip := getClientIP(r)
	return fmt.Sprintf("ip:%s", ip)
}

// defaultOnLimitExceeded handles rate limit exceeded
func defaultOnLimitExceeded(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusTooManyRequests)
	w.Write([]byte(`{"error":"rate_limit_exceeded","message":"Too many requests"}`))
}

// getClientIP extracts client IP from request
func getClientIP(r *http.Request) string {
	// Check X-Forwarded-For header
	if xff := r.Header.Get("X-Forwarded-For"); xff != "" {
		return xff
	}

	// Check X-Real-IP header
	if xri := r.Header.Get("X-Real-IP"); xri != "" {
		return xri
	}

	// Fallback to RemoteAddr
	ip := r.RemoteAddr
	if ip != "" {
		return ip
	}

	return "unknown"
}

// max returns the maximum of two integers
func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

// UserKeyFunc generates rate limit key from user ID
func UserKeyFunc(userIDExtractor func(r *http.Request) string) func(r *http.Request) string {
	return func(r *http.Request) string {
		userID := userIDExtractor(r)
		if userID == "" {
			return defaultKeyFunc(r)
		}
		return fmt.Sprintf("user:%s", userID)
	}
}

// PathKeyFunc generates rate limit key from request path
func PathKeyFunc(baseKeyFunc func(r *http.Request) string) func(r *http.Request) string {
	return func(r *http.Request) string {
		baseKey := baseKeyFunc(r)
		return fmt.Sprintf("%s:path:%s", baseKey, r.URL.Path)
	}
}

// MultiLimiter provides multiple rate limiters with different rules
type MultiLimiter struct {
	limiters []*Limiter
	logger   *zap.Logger
}

// NewMultiLimiter creates a new multi-limiter
func NewMultiLimiter(limiters []*Limiter, logger *zap.Logger) *MultiLimiter {
	return &MultiLimiter{
		limiters: limiters,
		logger:   logger,
	}
}

// Allow checks all limiters and returns true if all allow
func (ml *MultiLimiter) Allow(ctx context.Context, key string) (bool, error) {
	for _, limiter := range ml.limiters {
		allowed, err := limiter.Allow(ctx, key)
		if err != nil {
			ml.logger.Warn("Rate limit check failed",
				zap.Error(err),
				zap.String("key", key))
			continue // Continue checking other limiters
		}
		if !allowed {
			return false, nil
		}
	}
	return true, nil
}

// GetRemaining returns minimum remaining from all limiters
func (ml *MultiLimiter) GetRemaining(ctx context.Context, key string) (int, error) {
	minRemaining := int(^uint(0) >> 1) // Max int

	for _, limiter := range ml.limiters {
		remaining, err := limiter.GetRemaining(ctx, key)
		if err != nil {
			continue
		}
		if remaining < minRemaining {
			minRemaining = remaining
		}
	}

	if minRemaining == int(^uint(0)>>1) {
		return 0, errors.New("failed to get remaining from any limiter")
	}

	return minRemaining, nil
}
