// Package server Issue: #140894175
package server

import (
	"github.com/google/uuid"
)

// Helper functions for tests
func uuidPtr(u uuid.UUID) *uuid.UUID {
	return &u
}

func stringPtr(s string) *string {
	return &s
}

func float64Ptr(f float64) *float64 {
	return &f
}

func intPtr(i int) *int {
	return &i
}
