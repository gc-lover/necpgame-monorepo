// Issue: #1433, #1380
package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

type contextKey string

const characterIDKey contextKey = "character_id"

var logger *logrus.Logger

// SetLogger sets the logger for utils package
func SetLogger(l *logrus.Logger) {
	logger = l
}

// GetLogger returns the logger for utils package
func GetLogger() *logrus.Logger {
	return logger
}

// getCharacterIDFromContext извлекает ID персонажа из контекста
func getCharacterIDFromContext(ctx context.Context) string {
	if id, ok := ctx.Value(characterIDKey).(string); ok {
		return id
	}
	// For development/testing - use mock ID
	return "00000000-0000-0000-0000-000000000001"
}

// respondJSON отправляет JSON ответ
// Issue: #1380 - обрабатывает ошибки json.Encode
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if data != nil {
		if err := json.NewEncoder(w).Encode(data); err != nil {
			if logger != nil {
				logger.WithError(err).Error("Failed to encode JSON response")
			}
			// Try to send error response if header not written yet
			if status == http.StatusOK {
				w.WriteHeader(http.StatusInternalServerError)
				w.Write([]byte(`{"error":"Internal Server Error","message":"Failed to encode response"}`))
			}
		}
	}
}

// respondError отправляет ошибку в формате JSON
// Issue: #1380 - обрабатывает ошибки json.Encode
func respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	}); err != nil {
		if logger != nil {
			logger.WithError(err).Error("Failed to encode error response")
		}
		// Fallback to plain text if JSON encoding fails
		w.Write([]byte(`{"error":"Internal Server Error","message":"Failed to encode error response"}`))
	}
}















