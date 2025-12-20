// Package server Issue: #1588 - Feature Flags for graceful degradation
package server

import (
	"sync"
)

// FeatureFlags manages feature toggles
type FeatureFlags struct {
	flags sync.Map
}

// NewFeatureFlags creates a new feature flags manager
func NewFeatureFlags() *FeatureFlags {
	return &FeatureFlags{}
}

// IsEnabled checks if feature is enabled
func (ff *FeatureFlags) IsEnabled(feature string) bool {
	enabled, ok := ff.flags.Load(feature)
	if !ok {
		return true // Default: enabled
	}
	return enabled.(bool)
}

// SetEnabled sets feature state
func (ff *FeatureFlags) SetEnabled(feature string, enabled bool) {
	ff.flags.Store(feature, enabled)
}

// Disable disables a feature
func (ff *FeatureFlags) Disable(feature string) {
	ff.flags.Store(feature, false)
}

// Enable enables a feature
func (ff *FeatureFlags) Enable(feature string) {
	ff.flags.Store(feature, true)
}
