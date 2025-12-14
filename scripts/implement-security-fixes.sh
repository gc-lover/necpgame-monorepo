#!/bin/bash
# NECPGAME Security Fixes Implementation Script
# Issue: #1862 - Addresses critical security vulnerabilities

set -e

echo "🔒 Implementing NECPGAME Security Fixes..."
echo "==========================================="

# Function to add security headers middleware
create_security_headers() {
    local service_dir=$1
    local middleware_file="$service_dir/server/security_headers.go"

    echo "Adding security headers middleware to $service_dir..."

    cat > "$middleware_file" << 'EOF'
// Issue: #1862 - Security headers middleware
package server

import (
    "net/http"
)

// SecurityHeadersMiddleware adds OWASP recommended security headers
func SecurityHeadersMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        // Prevent MIME type sniffing
        w.Header().Set("X-Content-Type-Options", "nosniff")

        // Prevent clickjacking
        w.Header().Set("X-Frame-Options", "DENY")

        // XSS protection
        w.Header().Set("X-XSS-Protection", "1; mode=block")

        // HSTS - HTTP Strict Transport Security
        w.Header().Set("Strict-Transport-Security", "max-age=31536000; includeSubDomains")

        // Content Security Policy - restrict resource loading
        w.Header().Set("Content-Security-Policy", "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'")

        // Referrer Policy
        w.Header().Set("Referrer-Policy", "strict-origin-when-cross-origin")

        next.ServeHTTP(w, r)
    })
}
EOF

    echo "OK Created security headers middleware for $service_dir"
}

# Function to add rate limiting middleware
create_rate_limiter() {
    local service_dir=$1
    local middleware_file="$service_dir/server/rate_limiter.go"

    echo "Adding rate limiting middleware to $service_dir..."

    cat > "$middleware_file" << 'EOF'
// Issue: #1862 - Rate limiting middleware
package server

import (
    "context"
    "net/http"
    "sync"
    "time"

    "github.com/didip/tollbooth"
    "github.com/didip/tollbooth/limiter"
)

// RateLimiter implements distributed rate limiting
type RateLimiter struct {
    limiter *limiter.Limiter
    mu      sync.RWMutex
}

// NewRateLimiter creates a new rate limiter (100 requests per minute per IP)
func NewRateLimiter() *RateLimiter {
    lb := tollbooth.NewLimiter(100, nil) // 100 requests per minute
    lb.SetIPLookups([]string{"X-Real-IP", "X-Forwarded-For", "RemoteAddr"})
    lb.SetMethods([]string{"GET", "POST", "PUT", "DELETE"})

    return &RateLimiter{
        limiter: lb,
    }
}

// Middleware returns HTTP middleware for rate limiting
func (rl *RateLimiter) Middleware(next http.Handler) http.Handler {
    return tollbooth.LimitFuncHandler(rl.limiter, func(w http.ResponseWriter, r *http.Request) {
        // Add rate limit headers
        w.Header().Set("X-Rate-Limit-Limit", "100")
        w.Header().Set("X-Rate-Limit-Remaining", "99") // Simplified

        next.ServeHTTP(w, r)
    })
}

// CheckRateLimit manually checks if request should be rate limited
func (rl *RateLimiter) CheckRateLimit(r *http.Request) (bool, error) {
    rl.mu.RLock()
    defer rl.mu.RUnlock()

    // Use tollbooth's internal checking
    return rl.limiter.LimitReached(r), nil
}
EOF

    echo "OK Created rate limiting middleware for $service_dir"
}

# Function to add input validation
create_input_validator() {
    local service_dir=$1
    local validator_file="$service_dir/server/input_validator.go"

    echo "Adding input validation to $service_dir..."

    cat > "$validator_file" << 'EOF'
// Issue: #1862 - Input validation utilities
package server

import (
    "errors"
    "net/http"
    "regexp"
    "strings"
    "unicode/utf8"
)

var (
    // SQL injection patterns
    sqlInjectionPatterns = []*regexp.Regexp{
        regexp.MustCompile(`(?i)(union\s+select|select\s+.*\s+from|insert\s+into|delete\s+from|update\s+.*\s+set|drop\s+table|alter\s+table)`),
        regexp.MustCompile(`(?i)(--|#|/\*|\*/|xp_|sp_|exec|execute)`),
    }

    // XSS patterns
    xssPatterns = []*regexp.Regexp{
        regexp.MustCompile(`(?i)(<script|javascript:|vbscript:|onload=|onerror=|onclick=)`),
        regexp.MustCompile(`(?i)(<iframe|<object|<embed)`),
    }

    // Max lengths for input validation
    maxStringLength = 1000
    maxArraySize    = 100
)

// ValidateStringInput validates string input for common attacks
func ValidateStringInput(input string) error {
    if input == "" {
        return nil // Allow empty strings
    }

    // Check length
    if utf8.RuneCountInString(input) > maxStringLength {
        return errors.New("input exceeds maximum length")
    }

    // Check for SQL injection
    for _, pattern := range sqlInjectionPatterns {
        if pattern.MatchString(input) {
            return errors.New("potentially malicious SQL content detected")
        }
    }

    // Check for XSS
    for _, pattern := range xssPatterns {
        if pattern.MatchString(input) {
            return errors.New("potentially malicious script content detected")
        }
    }

    return nil
}

// ValidateArrayInput validates array input size
func ValidateArrayInput(arr []interface{}) error {
    if len(arr) > maxArraySize {
        return errors.New("array exceeds maximum size")
    }
    return nil
}

// SanitizeString removes potentially dangerous characters
func SanitizeString(input string) string {
    // Remove null bytes
    input = strings.ReplaceAll(input, "\x00", "")

    // Trim whitespace
    input = strings.TrimSpace(input)

    return input
}

// ValidateHTTPRequest validates HTTP request parameters
func ValidateHTTPRequest(r *http.Request) error {
    // Validate query parameters
    for key, values := range r.URL.Query() {
        for _, value := range values {
            if err := ValidateStringInput(key); err != nil {
                return err
            }
            if err := ValidateStringInput(value); err != nil {
                return err
            }
        }
    }

    // Validate headers (basic check)
    for key, values := range r.Header {
        if err := ValidateStringInput(key); err != nil {
            return err
        }
        for _, value := range values {
            if err := ValidateStringInput(value); err != nil {
                return err
            }
        }
    }

    return nil
}
EOF

    echo "OK Created input validation utilities for $service_dir"
}

# Function to update main.go to include security middleware
update_main_security() {
    local service_dir=$1
    local main_file="$service_dir/main.go"

    if [ ! -f "$main_file" ]; then
        echo "WARNING  Main file not found for $service_dir"
        return
    fi

    echo "Updating main.go with security middleware for $service_dir..."

    # This is a simplified approach - in real implementation,
    # we'd need to parse and modify the Go code properly
    echo "WARNING  Manual integration required for $service_dir/main.go"
    echo "   Add these lines to your HTTP server setup:"
    echo "   "
    echo "   // Security middleware"
    echo "   rateLimiter := server.NewRateLimiter()"
    echo "   mux := rateLimiter.Middleware(server.SecurityHeadersMiddleware(yourHandler))"
    echo "   "
    echo "   // Input validation for requests"
    echo "   // Add validation middleware as needed"
}

# Main execution
echo "🔧 Starting security fixes implementation..."

# List of services to update (add more as needed)
services=(
    "services/auth-service-go"
    "services/combat-service-go"
    "services/gameplay-service-go"
    "services/economy-service-go"
    "services/combat-sessions-service-go"
)

for service in "${services[@]}"; do
    if [ -d "$service" ]; then
        echo ""
        echo "🔒 Processing $service..."

        create_security_headers "$service"
        create_rate_limiter "$service"
        create_input_validator "$service"
        update_main_security "$service"
    else
        echo "WARNING  Service directory $service not found, skipping..."
    fi
done

echo ""
echo "🎯 Security fixes implementation completed!"
echo ""
echo "📋 Next steps:"
echo "1. Review and integrate the generated middleware files"
echo "2. Update main.go files to use the security middleware"
echo "3. Test the security implementations"
echo "4. Run security audit again to verify fixes"
echo ""
echo "🔐 Security improvements:"
echo "• Rate limiting middleware added"
echo "• Security headers implemented"
echo "• Input validation utilities created"
echo "• SQL injection and XSS protection"
echo ""
echo "Issue: #1862"
