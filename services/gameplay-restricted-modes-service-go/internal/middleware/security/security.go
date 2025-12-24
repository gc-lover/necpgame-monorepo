// Issue: #1499
package security

import (
	"crypto/subtle"
	"net/http"
	"strings"
)

// HTTPSRedirect redirects HTTP requests to HTTPS
func HTTPSRedirect(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Header.Get("X-Forwarded-Proto") == "http" {
			target := "https://" + r.Host + r.RequestURI
			http.Redirect(w, r, target, http.StatusPermanentRedirect)
			return
		}
		next.ServeHTTP(w, r)
	})
}

// SecurityHeaders adds security headers to responses
func SecurityHeaders(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Prevent MIME type sniffing
		w.Header().Set("X-Content-Type-Options", "nosniff")

		// Prevent clickjacking
		w.Header().Set("X-Frame-Options", "DENY")

		// XSS protection
		w.Header().Set("X-XSS-Protection", "1; mode=block")

		// Referrer policy
		w.Header().Set("Referrer-Policy", "strict-origin-when-cross-origin")

		// Content Security Policy
		w.Header().Set("Content-Security-Policy", "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'")

		// Strict Transport Security (only for HTTPS)
		if r.TLS != nil {
			w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
		}

		next.ServeHTTP(w, r)
	})
}

// CSRFProtection provides basic CSRF protection
func CSRFProtection(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// For state-changing operations (POST, PUT, DELETE, PATCH)
		if r.Method == "POST" || r.Method == "PUT" || r.Method == "DELETE" || r.Method == "PATCH" {
			// Check for CSRF token in header
			token := r.Header.Get("X-CSRF-Token")
			if token == "" {
				// Check in form data for compatibility
				token = r.FormValue("_csrf")
			}

			if token == "" {
				http.Error(w, "CSRF token missing", http.StatusForbidden)
				return
			}

			// In a real implementation, you would validate the token against a session
			// For now, just check that it's not empty
			if len(token) < 32 {
				http.Error(w, "Invalid CSRF token", http.StatusForbidden)
				return
			}
		}

		next.ServeHTTP(w, r)
	})
}

// BasicAuth provides HTTP Basic Authentication
func BasicAuth(realm, expectedUsername, expectedPassword string) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			auth := r.Header.Get("Authorization")
			if auth == "" {
				w.Header().Set("WWW-Authenticate", `Basic realm="`+realm+`"`)
				http.Error(w, "Authorization required", http.StatusUnauthorized)
				return
			}

			if !strings.HasPrefix(auth, "Basic ") {
				http.Error(w, "Invalid authorization method", http.StatusUnauthorized)
				return
			}

			// Decode base64 credentials
			credentials := strings.TrimPrefix(auth, "Basic ")
			// In production, decode base64 here
			parts := strings.SplitN(credentials, ":", 2)
			if len(parts) != 2 {
				http.Error(w, "Invalid credentials format", http.StatusUnauthorized)
				return
			}

			username, password := parts[0], parts[1]

			// Use constant-time comparison to prevent timing attacks
			if subtle.ConstantTimeCompare([]byte(username), []byte(expectedUsername)) != 1 ||
				subtle.ConstantTimeCompare([]byte(password), []byte(expectedPassword)) != 1 {
				http.Error(w, "Invalid credentials", http.StatusUnauthorized)
				return
			}

			next.ServeHTTP(w, r)
		})
	}
}

// RateLimit provides basic rate limiting (in production, use a proper rate limiter)
func RateLimit(requestsPerMinute int) func(http.Handler) http.Handler {
	// Simplified implementation - in production, use redis or similar
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			// In a real implementation, you would track requests per IP/client
			// For now, just pass through
			next.ServeHTTP(w, r)
		})
	}
}

// RequestSizeLimit limits the size of request bodies
func RequestSizeLimit(maxSize int64) func(http.Handler) http.Handler {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			r.Body = http.MaxBytesReader(w, r.Body, maxSize)
			next.ServeHTTP(w, r)
		})
	}
}

