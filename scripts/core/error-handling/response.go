// Package response provides HTTP response helpers for MMOFPS game services
package response

import (
	"encoding/json"
	"net/http"
	"time"

	"go.uber.org/zap"

	"github.com/your-org/necpgame/scripts/core/error-handling"
)

// Response represents a structured HTTP response
type Response struct {
	Data      interface{} `json:"data,omitempty"`
	Meta      interface{} `json:"meta,omitempty"`
	Status    string      `json:"status"`
	Timestamp time.Time   `json:"timestamp"`
	RequestID string      `json:"request_id,omitempty"`
}

// ErrorResponse represents a structured error response
type ErrorResponse struct {
	Error     string                 `json:"error"`
	Type      string                 `json:"type"`
	Code      string                 `json:"code"`
	Details   string                 `json:"details,omitempty"`
	Fields    map[string]interface{} `json:"fields,omitempty"`
	RequestID string                 `json:"request_id,omitempty"`
	Timestamp time.Time              `json:"timestamp"`
}

// PaginationMeta represents pagination metadata
type PaginationMeta struct {
	Page       int `json:"page"`
	PerPage    int `json:"per_page"`
	Total      int `json:"total"`
	TotalPages int `json:"total_pages"`
	HasNext    bool `json:"has_next"`
	HasPrev    bool `json:"has_prev"`
}

// Responder handles HTTP responses with proper error handling
type Responder struct {
	logger *logging.Logger
}

// NewResponder creates a new Responder
func NewResponder(logger *logging.Logger) *Responder {
	return &Responder{logger: logger}
}

// Success sends a successful JSON response
func (r *Responder) Success(w http.ResponseWriter, status int, data interface{}) {
	r.SuccessWithMeta(w, status, data, nil)
}

// SuccessWithMeta sends a successful JSON response with metadata
func (r *Responder) SuccessWithMeta(w http.ResponseWriter, status int, data interface{}, meta interface{}) {
	r.SuccessWithRequestID(w, status, data, meta, "")
}

// SuccessWithRequestID sends a successful JSON response with request ID
func (r *Responder) SuccessWithRequestID(w http.ResponseWriter, status int, data interface{}, meta interface{}, requestID string) {
	response := Response{
		Data:      data,
		Meta:      meta,
		Status:    "success",
		Timestamp: time.Now(),
		RequestID: requestID,
	}

	r.writeJSON(w, status, response)
}

// Error sends an error response
func (r *Responder) Error(w http.ResponseWriter, err error) {
	r.ErrorWithRequestID(w, err, "")
}

// ErrorWithRequestID sends an error response with request ID
func (r *Responder) ErrorWithRequestID(w http.ResponseWriter, err error, requestID string) {
	var gameErr *errors.GameError

	// Convert regular errors to GameError
	if ge, ok := err.(*errors.GameError); ok {
		gameErr = ge
	} else {
		gameErr = errors.WrapError(err, errors.ErrorTypeInternal, "INTERNAL_ERROR", "Internal server error")
	}

	// Add request ID and timestamp
	gameErr.WithRequestID(requestID).WithTimestamp(time.Now().Format(time.RFC3339))

	response := ErrorResponse{
		Error:     gameErr.Message,
		Type:      string(gameErr.Type),
		Code:      gameErr.Code,
		Details:   gameErr.Details,
		Fields:    gameErr.Fields,
		RequestID: requestID,
		Timestamp: time.Now(),
	}

	// Log the error
	r.logger.LogError(gameErr, "HTTP Error Response")

	r.writeJSON(w, gameErr.HTTPStatus, response)
}

// ValidationError sends a validation error response
func (r *Responder) ValidationError(w http.ResponseWriter, message string, fields map[string]interface{}) {
	r.ValidationErrorWithRequestID(w, message, fields, "")
}

// ValidationErrorWithRequestID sends a validation error response with request ID
func (r *Responder) ValidationErrorWithRequestID(w http.ResponseWriter, message string, fields map[string]interface{}, requestID string) {
	gameErr := errors.NewValidationError("VALIDATION_FAILED", message)

	if fields != nil {
		gameErr.WithField("validation_errors", fields)
	}

	r.ErrorWithRequestID(w, gameErr, requestID)
}

// NotFound sends a not found error response
func (r *Responder) NotFound(w http.ResponseWriter, resource string) {
	r.NotFoundWithRequestID(w, resource, "")
}

// NotFoundWithRequestID sends a not found error response with request ID
func (r *Responder) NotFoundWithRequestID(w http.ResponseWriter, resource string, requestID string) {
	gameErr := errors.NewNotFoundError("RESOURCE_NOT_FOUND", "Resource not found: "+resource)
	r.ErrorWithRequestID(w, gameErr, requestID)
}

// Unauthorized sends an unauthorized error response
func (r *Responder) Unauthorized(w http.ResponseWriter, message string) {
	r.UnauthorizedWithRequestID(w, message, "")
}

// UnauthorizedWithRequestID sends an unauthorized error response with request ID
func (r *Responder) UnauthorizedWithRequestID(w http.ResponseWriter, message string, requestID string) {
	gameErr := errors.NewAuthenticationError("UNAUTHORIZED", message)
	r.ErrorWithRequestID(w, gameErr, requestID)
}

// Forbidden sends a forbidden error response
func (r *Responder) Forbidden(w http.ResponseWriter, message string) {
	r.ForbiddenWithRequestID(w, message, "")
}

// ForbiddenWithRequestID sends a forbidden error response with request ID
func (r *Responder) ForbiddenWithRequestID(w http.ResponseWriter, message string, requestID string) {
	gameErr := errors.NewAuthorizationError("FORBIDDEN", message)
	r.ErrorWithRequestID(w, gameErr, requestID)
}

// TooManyRequests sends a rate limit error response
func (r *Responder) TooManyRequests(w http.ResponseWriter, message string) {
	r.TooManyRequestsWithRequestID(w, message, "")
}

// TooManyRequestsWithRequestID sends a rate limit error response with request ID
func (r *Responder) TooManyRequestsWithRequestID(w http.ResponseWriter, message string, requestID string) {
	gameErr := errors.NewRateLimitError("RATE_LIMIT_EXCEEDED", message)
	r.ErrorWithRequestID(w, gameErr, requestID)
}

// InternalError sends an internal server error response
func (r *Responder) InternalError(w http.ResponseWriter, message string) {
	r.InternalErrorWithRequestID(w, message, "")
}

// InternalErrorWithRequestID sends an internal server error response with request ID
func (r *Responder) InternalErrorWithRequestID(w http.ResponseWriter, message string, requestID string) {
	gameErr := errors.NewInternalError("INTERNAL_ERROR", message)
	r.ErrorWithRequestID(w, gameErr, requestID)
}

// PaginatedSuccess sends a successful response with pagination metadata
func (r *Responder) PaginatedSuccess(w http.ResponseWriter, data interface{}, page, perPage, total int) {
	totalPages := (total + perPage - 1) / perPage // Ceiling division

	meta := PaginationMeta{
		Page:       page,
		PerPage:    perPage,
		Total:      total,
		TotalPages: totalPages,
		HasNext:    page < totalPages,
		HasPrev:    page > 1,
	}

	r.SuccessWithMeta(w, http.StatusOK, data, meta)
}

// writeJSON writes JSON response with proper error handling
func (r *Responder) writeJSON(w http.ResponseWriter, status int, payload interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	encoder := json.NewEncoder(w)
	encoder.SetIndent("", "  ") // Pretty print for development

	if err := encoder.Encode(payload); err != nil {
		// If JSON encoding fails, log error and send fallback response
		r.logger.Errorw("Failed to encode JSON response", "error", err)

		// Send fallback plain text error
		w.Header().Set("Content-Type", "text/plain")
		w.WriteHeader(http.StatusInternalServerError)
		w.Write([]byte("Internal server error: failed to encode response"))
	}
}

// HealthResponse sends a health check response
func (r *Responder) HealthResponse(w http.ResponseWriter, serviceName, version string, checks map[string]interface{}) {
	health := map[string]interface{}{
		"status":      "healthy",
		"service":     serviceName,
		"version":     version,
		"timestamp":   time.Now(),
		"checks":      checks,
		"uptime":      "N/A", // Would need to be tracked separately
	}

	r.Success(w, http.StatusOK, health)
}

// ReadinessResponse sends a readiness check response
func (r *Responder) ReadinessResponse(w http.ResponseWriter, ready bool, dependencies map[string]bool) {
	status := "ready"
	httpStatus := http.StatusOK

	if !ready {
		status = "not ready"
		httpStatus = http.StatusServiceUnavailable
	}

	response := map[string]interface{}{
		"status":       status,
		"dependencies": dependencies,
		"timestamp":    time.Now(),
	}

	r.writeJSON(w, httpStatus, response)
}
