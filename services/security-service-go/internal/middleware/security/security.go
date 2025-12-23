package security

import (
	"net/http"
	"strings"
	"time"

	"github.com/rs/zerolog"
)

// HTTPSRedirect redirects HTTP requests to HTTPS
func HTTPSRedirect(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Skip redirect for health checks and local development
		if r.URL.Path == "/health" || r.URL.Path == "/ready" ||
		   strings.HasPrefix(r.Host, "localhost") ||
		   strings.HasPrefix(r.Host, "127.0.0.1") {
			next.ServeHTTP(w, r)
			return
		}

		// Check if request is already HTTPS
		if r.TLS != nil || r.Header.Get("X-Forwarded-Proto") == "https" {
			next.ServeHTTP(w, r)
			return
		}

		// Redirect to HTTPS
		httpsURL := "https://" + r.Host + r.RequestURI
		http.Redirect(w, r, httpsURL, http.StatusPermanentRedirect)
	})
}

// SecurityHeaders adds security-related HTTP headers
func SecurityHeaders(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Prevent MIME type sniffing
		w.Header().Set("X-Content-Type-Options", "nosniff")

		// Prevent clickjacking
		w.Header().Set("X-Frame-Options", "DENY")

		// Enable XSS protection
		w.Header().Set("X-XSS-Protection", "1; mode=block")

		// Referrer policy
		w.Header().Set("Referrer-Policy", "strict-origin-when-cross-origin")

		// Content Security Policy (basic)
		w.Header().Set("Content-Security-Policy", "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'")

		// HSTS (HTTP Strict Transport Security)
		w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")

		next.ServeHTTP(w, r)
	})
}

// CSRFProtection provides CSRF protection
func CSRFProtection(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Skip CSRF check for safe methods
		if r.Method == http.MethodGet || r.Method == http.MethodHead ||
		   r.Method == http.MethodOptions {
			next.ServeHTTP(w, r)
			return
		}

		// Check Origin header
		origin := r.Header.Get("Origin")
		if origin != "" {
			// In production, validate against allowed origins
			// For now, just ensure it's present
			if !strings.HasPrefix(origin, "http://") && !strings.HasPrefix(origin, "https://") {
				http.Error(w, "Invalid origin", http.StatusForbidden)
				return
			}
		}

		// Check Referer header as fallback
		referer := r.Header.Get("Referer")
		if origin == "" && referer != "" {
			if !strings.HasPrefix(referer, "http://") && !strings.HasPrefix(referer, "https://") {
				http.Error(w, "Invalid referer", http.StatusForbidden)
				return
			}
		}

		next.ServeHTTP(w, r)
	})
}

// BasicAuth provides basic HTTP authentication
func BasicAuth(realm, username, password string) func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			user, pass, ok := r.BasicAuth()
			if !ok {
				w.Header().Set("WWW-Authenticate", `Basic realm="`+realm+`"`)
				http.Error(w, "Unauthorized", http.StatusUnauthorized)
				return
			}

			if user != username || pass != password {
				w.Header().Set("WWW-Authenticate", `Basic realm="`+realm+`"`)
				http.Error(w, "Unauthorized", http.StatusUnauthorized)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// RateLimit provides basic rate limiting (would use more sophisticated solution in production)
var requestCounts = make(map[string]int)
var lastReset = time.Now()

func RateLimit(requestsPerMinute int) func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// Simple IP-based rate limiting (not production-ready)
			clientIP := getClientIP(r)

			now := time.Now()
			if now.Sub(lastReset) > time.Minute {
				requestCounts = make(map[string]int)
				lastReset = now
			}

			if requestCounts[clientIP] >= requestsPerMinute {
				http.Error(w, "Rate limit exceeded", http.StatusTooManyRequests)
				return
			}

			requestCounts[clientIP]++
			next.ServeHTTP(w, r)
		})
	}
}

// SecurityLogger logs security events
func SecurityLogger(logger zerolog.Logger) func(next http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()

			// Log security-relevant information
			logger.Info().
				Str("method", r.Method).
				Str("path", r.URL.Path).
				Str("remote_ip", getClientIP(r)).
				Str("user_agent", r.UserAgent()).
				Str("referer", r.Header.Get("Referer")).
				Time("timestamp", start).
				Msg("Security event")

			next.ServeHTTP(w, r)
		})
	}
}

// Helper functions

func getClientIP(r *http.Request) string {
	// Check X-Forwarded-For header first (for proxies/load balancers)
	xForwardedFor := r.Header.Get("X-Forwarded-For")
	if xForwardedFor != "" {
		// Take the first IP if multiple are present
		ips := strings.Split(xForwardedFor, ",")
		return strings.TrimSpace(ips[0])
	}

	// Check X-Real-IP header
	xRealIP := r.Header.Get("X-Real-IP")
	if xRealIP != "" {
		return xRealIP
	}

	// Fall back to RemoteAddr
	ip := r.RemoteAddr
	// Remove port if present
	if strings.Contains(ip, ":") {
		ip, _, _ = strings.Cut(ip, ":")
	}
	return ip
}

// ValidateInput provides basic input validation middleware
func ValidateInput(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Basic input validation
		if r.ContentLength > 1024*1024 { // 1MB limit
			http.Error(w, "Request too large", http.StatusRequestEntityTooLarge)
			return
		}

		// Check for suspicious patterns
		if strings.Contains(r.URL.RawQuery, "<script") ||
		   strings.Contains(r.URL.RawQuery, "javascript:") {
			http.Error(w, "Invalid request", http.StatusBadRequest)
			return
		}

		next.ServeHTTP(w, r)
	})
}
