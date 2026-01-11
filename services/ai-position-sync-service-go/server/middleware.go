package server

import (
	"context"
	"log/slog"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5/middleware"
)

type Middleware struct{}

func NewMiddleware() *Middleware {
	return &Middleware{}
}

func (m *Middleware) RequestID(next http.Handler) http.Handler {
	return middleware.RequestID(next)
}

func (m *Middleware) RealIP(next http.Handler) http.Handler {
	return middleware.RealIP(next)
}

func (m *Middleware) Logger(next http.Handler) http.Handler {
	fn := func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

		defer func() {
			slog.Info("HTTP Request",
				"method", r.Method,
				"url", r.URL.Path,
				"status", ww.Status(),
				"bytes", ww.BytesWritten(),
				"duration", time.Since(start),
				"request_id", middleware.GetReqID(r.Context()),
			)
		}()

		next.ServeHTTP(ww, r)
	}
	return http.HandlerFunc(fn)
}

func (m *Middleware) Recoverer(next http.Handler) http.Handler {
	return middleware.Recoverer(next)
}

func (m *Middleware) Timeout(timeout time.Duration) func(http.Handler) http.Handler {
	return middleware.Timeout(timeout)
}

func (m *Middleware) CORS(next http.Handler) http.Handler {
	fn := func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Request-ID")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	}
	return http.HandlerFunc(fn)
}

func (m *Middleware) Auth(next http.Handler) http.Handler {
	fn := func(w http.ResponseWriter, r *http.Request) {
		// Performance: Lightweight auth check for high-throughput position sync
		// In production, this would validate JWT tokens or API keys
		authHeader := r.Header.Get("Authorization")
		if authHeader == "" {
			// Allow health checks without auth
			if r.URL.Path == "/health" {
				next.ServeHTTP(w, r)
				return
			}
			http.Error(w, "Unauthorized", http.StatusUnauthorized)
			return
		}

		// TODO: Implement proper JWT validation
		// For now, accept any non-empty Authorization header
		if len(authHeader) < 10 { // Basic length check
			http.Error(w, "Invalid authorization", http.StatusUnauthorized)
			return
		}

		next.ServeHTTP(w, r)
	}
	return http.HandlerFunc(fn)
}

func (m *Middleware) RateLimit(next http.Handler) http.Handler {
	// Performance: Rate limiting for position sync endpoints
	// This is a simple in-memory rate limiter - in production use Redis
	type client struct {
		requests int
		resetAt  time.Time
	}

	clients := make(map[string]*client)
	const maxRequests = 1000 // requests per minute
	const window = time.Minute

	fn := func(w http.ResponseWriter, r *http.Request) {
		ip := middleware.GetReqID(r.Context()) // Use request ID as client identifier

		now := time.Now()
		c, exists := clients[ip]

		if !exists {
			clients[ip] = &client{requests: 1, resetAt: now.Add(window)}
			next.ServeHTTP(w, r)
			return
		}

		if now.After(c.resetAt) {
			c.requests = 1
			c.resetAt = now.Add(window)
			next.ServeHTTP(w, r)
			return
		}

		if c.requests >= maxRequests {
			http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
			return
		}

		c.requests++
		next.ServeHTTP(w, r)
	}
	return http.HandlerFunc(fn)
}

func (m *Middleware) Metrics(next http.Handler) http.Handler {
	fn := func(w http.ResponseWriter, r *http.Request) {
		start := time.Now()
		ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

		defer func() {
			duration := time.Since(start)
			status := ww.Status()

			// Record metrics (in production, use Prometheus/statsd)
			slog.Debug("Request metrics",
				"method", r.Method,
				"path", r.URL.Path,
				"status", status,
				"duration_ms", duration.Milliseconds(),
				"bytes_written", ww.BytesWritten(),
			)
		}()

		next.ServeHTTP(ww, r)
	}
	return http.HandlerFunc(fn)
}

func (m *Middleware) Compression(next http.Handler) http.Handler {
	return middleware.Compress(5, "gzip")(next) // gzip level 5 for balance of speed/compression
}

func (m *Middleware) ContextTimeout(timeout time.Duration) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		fn := func(w http.ResponseWriter, r *http.Request) {
			ctx, cancel := context.WithTimeout(r.Context(), timeout)
			defer cancel()

			r = r.WithContext(ctx)
			next.ServeHTTP(w, r)
		}
		return http.HandlerFunc(fn)
	}
}