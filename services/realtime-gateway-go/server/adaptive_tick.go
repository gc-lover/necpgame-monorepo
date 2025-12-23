// Issue: #1580
// Adaptive Tick Rate - scales game update frequency based on player load
// Performance: 128Hz for <50 players, scales down to 20Hz for 500+ players

package server

import (
	"sync/atomic"
	"time"
)

// AdaptiveTickRate adjusts tick rate based on server load
type AdaptiveTickRate struct {
	playerCount atomic.Int32
	tickRate    atomic.Int64 // Hz
}

// NewAdaptiveTickRate creates a new adaptive tick rate manager
func NewAdaptiveTickRate(initialRate int64) *AdaptiveTickRate {
	atr := &AdaptiveTickRate{}
	atr.tickRate.Store(initialRate)
	return atr
}

// Adjust updates the tick rate based on current player count
func (atr *AdaptiveTickRate) Adjust(playerCount int32) {
	atr.playerCount.Store(playerCount)

	var newRate int64
	switch {
	case playerCount < 50:
		newRate = 128 // 128 Hz - very responsive for small groups
	case playerCount < 100:
		newRate = 100 // 100 Hz - good for medium groups
	case playerCount < 200:
		newRate = 60  // 60 Hz - standard for larger groups
	case playerCount < 300:
		newRate = 40  // 40 Hz - acceptable for big groups
	case playerCount < 500:
		newRate = 30  // 30 Hz - minimal for very large groups
	default:
		newRate = 20  // 20 Hz - bare minimum for massive servers
	}

	atr.tickRate.Store(newRate)
}

// Get returns the current tick rate in Hz
func (atr *AdaptiveTickRate) Get() int64 {
	return atr.tickRate.Load()
}

// GetDuration returns the tick duration
func (atr *AdaptiveTickRate) GetDuration() time.Duration {
	hz := atr.tickRate.Load()
	if hz <= 0 {
		hz = 60 // Fallback
	}
	return time.Duration(1000000000 / hz) * time.Nanosecond
}

// GetPlayerCount returns the current player count
func (atr *AdaptiveTickRate) GetPlayerCount() int32 {
	return atr.playerCount.Load()
}

// GetStats returns tick rate statistics
func (atr *AdaptiveTickRate) GetStats() map[string]interface{} {
	return map[string]interface{}{
		"tick_rate_hz":    atr.Get(),
		"tick_duration_ms": float64(atr.GetDuration().Nanoseconds()) / 1000000.0,
		"player_count":    atr.GetPlayerCount(),
	}
}
