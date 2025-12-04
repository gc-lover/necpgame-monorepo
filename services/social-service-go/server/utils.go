// Issue: #1433
package server

import (
	"context"
	"encoding/json"
	"net/http"
)

type contextKey string

const characterIDKey contextKey = "character_id"

// getCharacterIDFromContext извлекает ID персонажа из контекста
func getCharacterIDFromContext(ctx context.Context) string {
	if id, ok := ctx.Value(characterIDKey).(string); ok {
		return id
	}
	// For development/testing - use mock ID
	return "00000000-0000-0000-0000-000000000001"
}

// respondJSON отправляет JSON ответ
func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if data != nil {
		json.NewEncoder(w).Encode(data)
	}
}

// respondError отправляет ошибку в формате JSON
func respondError(w http.ResponseWriter, status int, message string) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(map[string]string{
		"error":   http.StatusText(status),
		"message": message,
	})
}






