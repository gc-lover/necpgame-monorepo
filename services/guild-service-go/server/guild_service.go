// Package server Issue: #1943
package server

import (
	"os"
)

// NewGuildService initializes the guild service with all dependencies

// getEnv gets environment variable or returns default value
func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}
