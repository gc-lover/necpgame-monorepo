// Package server Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// CRITICAL for projectile service - prevents projectile spam, impossible trajectories
package server

import (
	"errors"
	"math"
	"sync"
	"sync/atomic"
	"time"

	"github.com/sirupsen/logrus"
)

var (
	ErrProjectileSpam       = errors.New("projectile spawn rate too high (potential spam)")
	ErrImpossibleTrajectory = errors.New("projectile trajectory exceeds maximum range")
	ErrInvalidVelocity      = errors.New("projectile velocity exceeds maximum")
)

// ProjectileValidator validates projectile spawning (anti-cheat: rate, trajectory, velocity)
type ProjectileValidator struct {
	lastSpawns sync.Map // playerID -> *LastSpawn
}

type LastSpawn struct {
	Timestamp time.Time
	Count     atomic.Int32
}

type Vec3 struct {
	X, Y, Z float32
}

// NewProjectileValidator creates a new projectile validator
func NewProjectileValidator() *ProjectileValidator {
	return &ProjectileValidator{}
}

// ValidateProjectileSpawn validates a projectile spawn request
func (pv *ProjectileValidator) ValidateProjectileSpawn(playerID string, velocity Vec3, maxRange float32) error {
	// 1. Rate check (max 20 projectiles/sec per player)
	last, ok := pv.lastSpawns.Load(playerID)
	if ok {
		lastSpawn := last.(*LastSpawn)
		elapsed := time.Since(lastSpawn.Timestamp)

		// Reset counter every second
		if elapsed > 1*time.Second {
			lastSpawn.Count.Store(0)
			lastSpawn.Timestamp = time.Now()
		}

		count := lastSpawn.Count.Add(1)
		if count > 20 {
			logrus.WithFields(logrus.Fields{
				"player_id": playerID,
				"count":     count,
				"elapsed":   elapsed,
			}).Warn("Projectile spawn rate too high (potential spam)")
			return ErrProjectileSpam
		}
	} else {
		// First spawn for this player
		pv.lastSpawns.Store(playerID, &LastSpawn{
			Timestamp: time.Now(),
		})
	}

	// 2. Velocity check (max 500 m/s for projectiles)
	velocityMagnitude := math.Sqrt(float64(velocity.X*velocity.X + velocity.Y*velocity.Y + velocity.Z*velocity.Z))
	if velocityMagnitude > 500.0 {
		logrus.WithFields(logrus.Fields{
			"player_id":          playerID,
			"velocity_magnitude": velocityMagnitude,
			"max_velocity":       500.0,
		}).Warn("Projectile velocity exceeds maximum")
		return ErrInvalidVelocity
	}

	// 3. Trajectory range check (max range based on velocity and gravity)
	// Simplified: max range = velocity^2 / (2 * gravity) for horizontal projection
	// Assuming gravity = 9.8 m/s^2
	gravity := 9.8
	maxTrajectoryRange := (velocityMagnitude * velocityMagnitude) / (2 * gravity)

	if maxRange > float32(maxTrajectoryRange) {
		logrus.WithFields(logrus.Fields{
			"player_id":      playerID,
			"max_range":      maxRange,
			"max_trajectory": maxTrajectoryRange,
			"velocity":       velocityMagnitude,
		}).Warn("Projectile trajectory exceeds maximum range")
		return ErrImpossibleTrajectory
	}

	return nil
}

// ValidateProjectilePosition validates projectile position (anti-cheat: prevent teleportation)
func (pv *ProjectileValidator) ValidateProjectilePosition(playerID string, currentPos Vec3, newPos Vec3, deltaTime time.Duration) error {
	// Calculate distance traveled
	distance := math.Sqrt(
		float64((newPos.X-currentPos.X)*(newPos.X-currentPos.X) +
			(newPos.Y-currentPos.Y)*(newPos.Y-currentPos.Y) +
			(newPos.Z-currentPos.Z)*(newPos.Z-currentPos.Z)))

	// Max speed: 500 m/s (projectile max velocity)
	maxSpeed := 500.0
	maxDistance := maxSpeed * deltaTime.Seconds()

	if distance > maxDistance {
		logrus.WithFields(logrus.Fields{
			"player_id":    playerID,
			"distance":     distance,
			"max_distance": maxDistance,
			"delta_time":   deltaTime,
		}).Warn("Projectile position change exceeds maximum (potential teleportation)")
		return ErrImpossibleTrajectory
	}

	return nil
}
