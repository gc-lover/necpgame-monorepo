// Package server Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// CRITICAL for movement service - prevents speed hacks, teleportation
package server

import (
	"errors"
	"math"
	"sync"
	"time"

	"github.com/sirupsen/logrus"
)

var (
	ErrSpeedHack = errors.New("movement speed exceeds maximum (speed hack)")
)

// MovementValidator validates player movement (anti-cheat: speed checks)
type MovementValidator struct {
	lastPositions sync.Map // playerID -> *LastPosition
}

type LastPosition struct {
	Position  Vec3
	Timestamp time.Time
}

type Vec3 struct {
	X, Y, Z float64
}

// NewMovementValidator creates a new movement validator
func NewMovementValidator() *MovementValidator {
	return &MovementValidator{}
}

// ValidateMovement validates player movement (anti-cheat: speed check)
func (mv *MovementValidator) ValidateMovement(playerID uint64, newPos Vec3) error {
	last, ok := mv.lastPositions.Load(playerID)
	if !ok {
		// First position, allow
		mv.lastPositions.Store(playerID, &LastPosition{
			Position:  newPos,
			Timestamp: time.Now(),
		})
		return nil
	}

	lastPos := last.(*LastPosition)

	// Calculate distance
	distance := calculateDistance(lastPos.Position, newPos)
	elapsed := time.Since(lastPos.Timestamp).Seconds()

	// Max speed: 10 m/s (running), 20 m/s (sprinting with buffs)
	maxSpeed := 20.0 // m/s
	maxDistance := maxSpeed * elapsed

	if distance > maxDistance {
		logrus.WithFields(logrus.Fields{
			"player_id":    playerID,
			"distance":     distance,
			"elapsed":      elapsed,
			"max_distance": maxDistance,
			"speed":        distance / elapsed,
		}).Warn("Movement speed exceeds maximum (potential speed hack)")
		return ErrSpeedHack // Moving too fast
	}

	// Update position
	mv.lastPositions.Store(playerID, &LastPosition{
		Position:  newPos,
		Timestamp: time.Now(),
	})

	return nil
}

// calculateDistance calculates 3D distance between two points
func calculateDistance(from, to Vec3) float64 {
	dx := to.X - from.X
	dy := to.Y - from.Y
	dz := to.Z - from.Z
	return math.Sqrt(dx*dx + dy*dy + dz*dz)
}
