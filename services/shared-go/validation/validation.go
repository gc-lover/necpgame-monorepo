// Input Validation and Sanitization Library
// Issue: #2154
// PERFORMANCE: SQL injection, XSS, CSRF protection
// Enterprise-grade input validation for all Go services

package validation

import (
	"context"
	"html"
	"net/url"
	"regexp"
	"strings"
	"unicode"
	"unicode/utf8"
)

// ValidationError represents a validation error
type ValidationError struct {
	Field   string
	Message string
	Code    string
}

func (e *ValidationError) Error() string {
	return e.Message
}

// ValidationResult represents the result of validation
type ValidationResult struct {
	Valid   bool
	Errors  []*ValidationError
	Sanitized string
}

// Validator provides input validation and sanitization
type Validator struct {
	// SQL injection patterns
	sqlInjectionPatterns []*regexp.Regexp
	
	// XSS patterns
	xssPatterns []*regexp.Regexp
	
	// CSRF token validator
	csrfValidator func(ctx context.Context, token string) bool
}

// NewValidator creates a new validator instance
func NewValidator() *Validator {
	return &Validator{
		sqlInjectionPatterns: []*regexp.Regexp{
			regexp.MustCompile(`(?i)(\b(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|EXEC|EXECUTE|UNION|SCRIPT)\b)`),
			regexp.MustCompile(`['";].*--`),
			regexp.MustCompile(`/\*.*\*/`),
			regexp.MustCompile(`\bor\b.*=.*`),
			regexp.MustCompile(`\band\b.*=.*`),
		},
		xssPatterns: []*regexp.Regexp{
			regexp.MustCompile(`(?i)<script[^>]*>.*?</script>`),
			regexp.MustCompile(`(?i)<iframe[^>]*>.*?</iframe>`),
			regexp.MustCompile(`(?i)javascript:`),
			regexp.MustCompile(`(?i)on\w+\s*=`),
			regexp.MustCompile(`(?i)<img[^>]*src\s*=\s*["']?javascript:`),
			regexp.MustCompile(`(?i)<svg[^>]*onload\s*=`),
		},
	}
}

// ValidateString validates and sanitizes a string input
func (v *Validator) ValidateString(ctx context.Context, field, value string, rules ...ValidationRule) *ValidationResult {
	result := &ValidationResult{
		Valid:   true,
		Errors:  []*ValidationError{},
		Sanitized: value,
	}

	// Check for SQL injection
	if v.containsSQLInjection(value) {
		result.Valid = false
		result.Errors = append(result.Errors, &ValidationError{
			Field:   field,
			Message: "Input contains potential SQL injection",
			Code:    "SQL_INJECTION",
		})
		return result
	}

	// Check for XSS
	if v.containsXSS(value) {
		result.Valid = false
		result.Errors = append(result.Errors, &ValidationError{
			Field:   field,
			Message: "Input contains potential XSS attack",
			Code:    "XSS",
		})
		return result
	}

	// Apply custom rules
	for _, rule := range rules {
		if err := rule.Validate(value); err != nil {
			result.Valid = false
			result.Errors = append(result.Errors, &ValidationError{
				Field:   field,
				Message: err.Error(),
				Code:    rule.Code(),
			})
		}
	}

	// Sanitize if valid
	if result.Valid {
		result.Sanitized = v.SanitizeString(value)
	}

	return result
}

// ValidateAndSanitize validates and sanitizes input, returns sanitized value or error
func (v *Validator) ValidateAndSanitize(ctx context.Context, field, value string, rules ...ValidationRule) (string, error) {
	result := v.ValidateString(ctx, field, value, rules...)
	if !result.Valid {
		return "", result.Errors[0]
	}
	return result.Sanitized, nil
}

// SanitizeString sanitizes a string by escaping HTML and removing dangerous patterns
func (v *Validator) SanitizeString(value string) string {
	// HTML escape
	sanitized := html.EscapeString(value)
	
	// Remove null bytes
	sanitized = strings.ReplaceAll(sanitized, "\x00", "")
	
	// Trim whitespace
	sanitized = strings.TrimSpace(sanitized)
	
	return sanitized
}

// SanitizeURL sanitizes a URL
func (v *Validator) SanitizeURL(rawURL string) (string, error) {
	parsed, err := url.Parse(rawURL)
	if err != nil {
		return "", err
	}

	// Only allow http/https schemes
	if parsed.Scheme != "http" && parsed.Scheme != "https" {
		return "", &ValidationError{
			Field:   "url",
			Message: "Invalid URL scheme",
			Code:    "INVALID_SCHEME",
		}
	}

	// Reconstruct URL
	return parsed.String(), nil
}

// containsSQLInjection checks if string contains SQL injection patterns
func (v *Validator) containsSQLInjection(value string) bool {
	lower := strings.ToLower(value)
	for _, pattern := range v.sqlInjectionPatterns {
		if pattern.MatchString(lower) {
			return true
		}
	}
	return false
}

// containsXSS checks if string contains XSS patterns
func (v *Validator) containsXSS(value string) bool {
	lower := strings.ToLower(value)
	for _, pattern := range v.xssPatterns {
		if pattern.MatchString(lower) {
			return true
		}
	}
	return false
}

// ValidationRule defines a validation rule
type ValidationRule interface {
	Validate(value string) error
	Code() string
}

// LengthRule validates string length
type LengthRule struct {
	Min int
	Max int
}

func (r *LengthRule) Validate(value string) error {
	length := utf8.RuneCountInString(value)
	if length < r.Min {
		return &ValidationError{
			Message: "Value is too short",
			Code:    "LENGTH_MIN",
		}
	}
	if r.Max > 0 && length > r.Max {
		return &ValidationError{
			Message: "Value is too long",
			Code:    "LENGTH_MAX",
		}
	}
	return nil
}

func (r *LengthRule) Code() string {
	return "LENGTH"
}

// PatternRule validates string against regex pattern
type PatternRule struct {
	Pattern *regexp.Regexp
	Message string
}

func (r *PatternRule) Validate(value string) error {
	if !r.Pattern.MatchString(value) {
		return &ValidationError{
			Message: r.Message,
			Code:    "PATTERN",
		}
	}
	return nil
}

func (r *PatternRule) Code() string {
	return "PATTERN"
}

// RequiredRule validates that value is not empty
type RequiredRule struct{}

func (r *RequiredRule) Validate(value string) error {
	if strings.TrimSpace(value) == "" {
		return &ValidationError{
			Message: "Field is required",
			Code:    "REQUIRED",
		}
	}
	return nil
}

func (r *RequiredRule) Code() string {
	return "REQUIRED"
}

// AlphanumericRule validates that value contains only alphanumeric characters
type AlphanumericRule struct{}

func (r *AlphanumericRule) Validate(value string) error {
	for _, char := range value {
		if !unicode.IsLetter(char) && !unicode.IsDigit(char) {
			return &ValidationError{
				Message: "Value must contain only alphanumeric characters",
				Code:    "ALPHANUMERIC",
			}
		}
	}
	return nil
}

func (r *AlphanumericRule) Code() string {
	return "ALPHANUMERIC"
}

// EmailRule validates email format
type EmailRule struct{}

var emailPattern = regexp.MustCompile(`^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$`)

func (r *EmailRule) Validate(value string) error {
	if !emailPattern.MatchString(value) {
		return &ValidationError{
			Message: "Invalid email format",
			Code:    "EMAIL",
		}
	}
	return nil
}

func (r *EmailRule) Code() string {
	return "EMAIL"
}

// UUIDRule validates UUID format
type UUIDRule struct{}

var uuidPattern = regexp.MustCompile(`^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$`)

func (r *UUIDRule) Validate(value string) error {
	if !uuidPattern.MatchString(strings.ToLower(value)) {
		return &ValidationError{
			Message: "Invalid UUID format",
			Code:    "UUID",
		}
	}
	return nil
}

func (r *UUIDRule) Code() string {
	return "UUID"
}

// CSRFProtection provides CSRF token validation
type CSRFProtection struct {
	validator func(ctx context.Context, token string) bool
}

// NewCSRFProtection creates a new CSRF protection instance
func NewCSRFProtection(validator func(ctx context.Context, token string) bool) *CSRFProtection {
	return &CSRFProtection{
		validator: validator,
	}
}

// ValidateToken validates a CSRF token
func (c *CSRFProtection) ValidateToken(ctx context.Context, token string) error {
	if token == "" {
		return &ValidationError{
			Field:   "csrf_token",
			Message: "CSRF token is required",
			Code:    "CSRF_MISSING",
		}
	}

	if c.validator != nil && !c.validator(ctx, token) {
		return &ValidationError{
			Field:   "csrf_token",
			Message: "Invalid CSRF token",
			Code:    "CSRF_INVALID",
		}
	}

	return nil
}

// Common validation functions

// ValidatePlayerName validates a player name
func ValidatePlayerName(name string) error {
	v := NewValidator()
	result := v.ValidateString(context.Background(), "player_name", name,
		&RequiredRule{},
		&LengthRule{Min: 3, Max: 20},
		&AlphanumericRule{},
	)

	if !result.Valid {
		return result.Errors[0]
	}

	return nil
}

// ValidateEmail validates an email address
func ValidateEmail(email string) error {
	v := NewValidator()
	result := v.ValidateString(context.Background(), "email", email,
		&RequiredRule{},
		&EmailRule{},
		&LengthRule{Min: 5, Max: 255},
	)

	if !result.Valid {
		return result.Errors[0]
	}

	return nil
}

// ValidateUUID validates a UUID
func ValidateUUID(uuid string) error {
	v := NewValidator()
	result := v.ValidateString(context.Background(), "uuid", uuid,
		&RequiredRule{},
		&UUIDRule{},
	)

	if !result.Valid {
		return result.Errors[0]
	}

	return nil
}
