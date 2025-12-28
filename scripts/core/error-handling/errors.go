// Package errors provides comprehensive error handling for MMOFPS game services
package errors

import (
	"fmt"
	"net/http"
)

// ErrorType represents different categories of errors
type ErrorType string

const (
	ErrorTypeValidation     ErrorType = "VALIDATION_ERROR"
	ErrorTypeAuthentication ErrorType = "AUTHENTICATION_ERROR"
	ErrorTypeAuthorization  ErrorType = "AUTHORIZATION_ERROR"
	ErrorTypeNotFound       ErrorType = "NOT_FOUND_ERROR"
	ErrorTypeConflict       ErrorType = "CONFLICT_ERROR"
	ErrorTypeRateLimited    ErrorType = "RATE_LIMIT_ERROR"
	ErrorTypeExternal       ErrorType = "EXTERNAL_SERVICE_ERROR"
	ErrorTypeInternal       ErrorType = "INTERNAL_ERROR"
	ErrorTypeTimeout        ErrorType = "TIMEOUT_ERROR"
	ErrorTypeDatabase       ErrorType = "DATABASE_ERROR"
	ErrorTypeNetwork        ErrorType = "NETWORK_ERROR"
)

// GameError represents a structured error with additional context
type GameError struct {
	Type       ErrorType `json:"type"`
	Code       string    `json:"code"`
	Message    string    `json:"message"`
	Details    string    `json:"details,omitempty"`
	RequestID  string    `json:"request_id,omitempty"`
	Timestamp  string    `json:"timestamp"`
	Severity   string    `json:"severity"` // "error", "warning", "info"
	HTTPStatus int       `json:"-"`
	Fields     map[string]interface{} `json:"fields,omitempty"`
	Cause      error                  `json:"-"`
}

// Error implements the error interface
func (e *GameError) Error() string {
	if e.Details != "" {
		return fmt.Sprintf("[%s] %s: %s (%s)", e.Type, e.Code, e.Message, e.Details)
	}
	return fmt.Sprintf("[%s] %s: %s", e.Type, e.Code, e.Message)
}

// Unwrap returns the underlying error
func (e *GameError) Unwrap() error {
	return e.Cause
}

// NewGameError creates a new GameError
func NewGameError(errType ErrorType, code, message string) *GameError {
	return &GameError{
		Type:       errType,
		Code:       code,
		Message:    message,
		Severity:   "error",
		HTTPStatus: getHTTPStatusForErrorType(errType),
		Fields:     make(map[string]interface{}),
	}
}

// WithDetails adds details to the error
func (e *GameError) WithDetails(details string) *GameError {
	e.Details = details
	return e
}

// WithRequestID adds request ID for tracing
func (e *GameError) WithRequestID(requestID string) *GameError {
	e.RequestID = requestID
	return e
}

// WithTimestamp adds timestamp
func (e *GameError) WithTimestamp(timestamp string) *GameError {
	e.Timestamp = timestamp
	return e
}

// WithSeverity sets severity level
func (e *GameError) WithSeverity(severity string) *GameError {
	e.Severity = severity
	return e
}

// WithField adds a custom field to the error
func (e *GameError) WithField(key string, value interface{}) *GameError {
	if e.Fields == nil {
		e.Fields = make(map[string]interface{})
	}
	e.Fields[key] = value
	return e
}

// WithCause sets the underlying cause
func (e *GameError) WithCause(cause error) *GameError {
	e.Cause = cause
	return e
}

// Common error constructors
func NewValidationError(code, message string) *GameError {
	return NewGameError(ErrorTypeValidation, code, message)
}

func NewAuthenticationError(code, message string) *GameError {
	return NewGameError(ErrorTypeAuthentication, code, message)
}

func NewAuthorizationError(code, message string) *GameError {
	return NewGameError(ErrorTypeAuthorization, code, message)
}

func NewNotFoundError(code, message string) *GameError {
	return NewGameError(ErrorTypeNotFound, code, message)
}

func NewConflictError(code, message string) *GameError {
	return NewGameError(ErrorTypeConflict, code, message)
}

func NewRateLimitError(code, message string) *GameError {
	return NewGameError(ErrorTypeRateLimited, code, message)
}

func NewDatabaseError(code, message string) *GameError {
	return NewGameError(ErrorTypeDatabase, code, message)
}

func NewNetworkError(code, message string) *GameError {
	return NewGameError(ErrorTypeNetwork, code, message)
}

func NewTimeoutError(code, message string) *GameError {
	return NewGameError(ErrorTypeTimeout, code, message)
}

func NewInternalError(code, message string) *GameError {
	return NewGameError(ErrorTypeInternal, code, message)
}

// WrapError wraps an existing error with additional context
func WrapError(err error, errType ErrorType, code, message string) *GameError {
	gameErr := NewGameError(errType, code, message).WithCause(err)
	return gameErr
}

// getHTTPStatusForErrorType maps error types to HTTP status codes
func getHTTPStatusForErrorType(errType ErrorType) int {
	switch errType {
	case ErrorTypeValidation:
		return http.StatusBadRequest
	case ErrorTypeAuthentication:
		return http.StatusUnauthorized
	case ErrorTypeAuthorization:
		return http.StatusForbidden
	case ErrorTypeNotFound:
		return http.StatusNotFound
	case ErrorTypeConflict:
		return http.StatusConflict
	case ErrorTypeRateLimited:
		return http.StatusTooManyRequests
	case ErrorTypeTimeout:
		return http.StatusRequestTimeout
	case ErrorTypeDatabase, ErrorTypeNetwork, ErrorTypeExternal, ErrorTypeInternal:
		return http.StatusInternalServerError
	default:
		return http.StatusInternalServerError
	}
}

// IsTemporary checks if an error is temporary and can be retried
func IsTemporary(err error) bool {
	if gameErr, ok := err.(*GameError); ok {
		switch gameErr.Type {
		case ErrorTypeTimeout, ErrorTypeNetwork, ErrorTypeExternal:
			return true
		}
	}
	return false
}

// IsClientError checks if the error is due to client input
func IsClientError(err error) bool {
	if gameErr, ok := err.(*GameError); ok {
		switch gameErr.Type {
		case ErrorTypeValidation, ErrorTypeAuthentication, ErrorTypeAuthorization, ErrorTypeNotFound:
			return true
		}
	}
	return false
}

// GetHTTPStatus returns the appropriate HTTP status code for an error
func GetHTTPStatus(err error) int {
	if gameErr, ok := err.(*GameError); ok {
		return gameErr.HTTPStatus
	}
	return http.StatusInternalServerError
}
