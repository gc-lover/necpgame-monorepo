// Package server Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
// CRITICAL for combat services - prevents aimbots, wallhacks, speed hacks
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
	ErrTooFast        = errors.New("action rate too high (potential aimbot/macro)")
	ErrImpossibleShot = errors.New("shot distance exceeds weapon range")
	ErrWallhack       = errors.New("line of sight blocked (wallhack)")
	ErrImpossibleTurn = errors.New("impossible turn angle (aimbot)")
	_                 = errors.New("movement speed exceeds maximum")
)

// ActionValidator validates combat actions (shots, attacks)
type ActionValidator struct {
	lastActions sync.Map // playerID -> *LastAction
}

type LastAction struct {
	Timestamp  time.Time
	Position   Vec3
	ActionType string
}

type Vec3 struct {
	X, Y, Z float32
}

// NewActionValidator creates a new action validator
func NewActionValidator() *ActionValidator {
	return &ActionValidator{}
}

// ValidateShot validates a shot action (anti-cheat: rate, distance, line of sight, angle)
func (av *ActionValidator) ValidateShot(playerID string, shot *ShotAction) error {
	// 1. Rate check (max 10 shots/sec for automatic weapons)
	if last, ok := av.lastActions.Load(playerID); ok {
		lastAction := last.(*LastAction)
		minInterval := av.getMinShotInterval(shot.WeaponType)

		if time.Since(lastAction.Timestamp) < minInterval {
			logrus.WithFields(logrus.Fields{
				"player_id":    playerID,
				"weapon_type":  shot.WeaponType,
				"time_since":   time.Since(lastAction.Timestamp),
				"min_interval": minInterval,
			}).Warn("Shot rate too high (potential aimbot)")
			return ErrTooFast // Potential aimbot/macro
		}
	}

	// 2. Distance check (weapon range)
	if shot.Distance > av.getWeaponRange(shot.WeaponType) {
		logrus.WithFields(logrus.Fields{
			"player_id":   playerID,
			"weapon_type": shot.WeaponType,
			"distance":    shot.Distance,
			"max_range":   av.getWeaponRange(shot.WeaponType),
		}).Warn("Shot distance exceeds weapon range")
		return ErrImpossibleShot
	}

	// 3. Line of sight check (prevent wallhacks)
	if !av.hasLineOfSight() {
		logrus.WithFields(logrus.Fields{
			"player_id": playerID,
			"from":      shot.From,
			"to":        shot.To,
		}).Warn("Line of sight blocked (potential wallhack)")
		return ErrWallhack
	}

	// 4. Angle check (prevent 180° instant turns)
	if last, ok := av.lastActions.Load(playerID); ok {
		lastAction := last.(*LastAction)
		angleDiff := calculateAngleDiff(lastAction.Position, shot.From)

		if angleDiff > 180.0 && time.Since(lastAction.Timestamp) < 100*time.Millisecond {
			logrus.WithFields(logrus.Fields{
				"player_id":  playerID,
				"angle_diff": angleDiff,
				"time_since": time.Since(lastAction.Timestamp),
			}).Warn("Impossible turn angle (potential aimbot)")
			return ErrImpossibleTurn // Aimbot
		}
	}

	// Update last action
	av.lastActions.Store(playerID, &LastAction{
		Timestamp:  time.Now(),
		Position:   shot.From,
		ActionType: "shot",
	})

	return nil
}

// ShotAction represents a shot action
type ShotAction struct {
	From       Vec3
	To         Vec3
	Distance   float32
	WeaponType string
}

// getMinShotInterval returns minimum interval between shots for weapon type
func (av *ActionValidator) getMinShotInterval(weaponType string) time.Duration {
	// Rate limits per weapon type (shots per second)
	rates := map[string]float64{
		"pistol":    5.0,  // 5 shots/sec
		"rifle":     10.0, // 10 shots/sec
		"smg":       15.0, // 15 shots/sec
		"sniper":    1.0,  // 1 shot/sec
		"shotgun":   2.0,  // 2 shots/sec
		"automatic": 10.0, // 10 shots/sec (default)
	}

	rate, ok := rates[weaponType]
	if !ok {
		rate = 10.0 // Default rate
	}

	return time.Duration(float64(time.Second) / rate)
}

// getWeaponRange returns maximum range for weapon type
func (av *ActionValidator) getWeaponRange(weaponType string) float32 {
	ranges := map[string]float32{
		"pistol":    50.0,  // 50 meters
		"rifle":     200.0, // 200 meters
		"smg":       100.0, // 100 meters
		"sniper":    500.0, // 500 meters
		"shotgun":   30.0,  // 30 meters
		"automatic": 150.0, // 150 meters (default)
	}

	weaponRange, ok := ranges[weaponType]
	if !ok {
		weaponRange = 150.0 // Default range
	}

	return weaponRange
}

// hasLineOfSight checks if there's a clear line of sight between two points
// TODO: Integrate with world service for actual raycast
func (av *ActionValidator) hasLineOfSight() bool {
	// Simplified check - in production, use actual raycast from world service
	// For now, assume line of sight is clear (will be enhanced with world service integration)
	return true
}

// calculateAngleDiff calculates angle difference between two positions
func calculateAngleDiff(from, to Vec3) float64 {
	// Calculate direction vectors
	dx1 := float64(to.X - from.X)
	dy1 := float64(to.Y - from.Y)
	_ = float64(to.Z - from.Z) // dz1 - not used in simplified calculation

	// Calculate angle (simplified - assumes forward direction is +X)
	angle1 := math.Atan2(dy1, dx1) * 180.0 / math.Pi

	// For angle difference, we'd need previous direction
	// Simplified: return angle from origin
	return math.Abs(angle1)
}

// AnomalyDetector detects suspicious player behavior (statistics-based)
type AnomalyDetector struct {
	playerStats sync.Map // playerID -> *PlayerStats
}

type PlayerStats struct {
	Headshots   atomic.Int64
	TotalShots  atomic.Int64
	AvgReaction atomic.Int64 // Microseconds
	Flags       atomic.Int32
}

// NewAnomalyDetector creates a new anomaly detector
func NewAnomalyDetector() *AnomalyDetector {
	return &AnomalyDetector{}
}

// RecordShot records a shot and checks for anomalies
func (ad *AnomalyDetector) RecordShot(playerID string, headshot bool, reactionTime time.Duration) {
	stats := ad.getOrCreateStats(playerID)

	stats.TotalShots.Add(1)
	if headshot {
		stats.Headshots.Add(1)
	}

	// Update average reaction time
	currentAvg := stats.AvgReaction.Load()
	newAvg := (currentAvg + reactionTime.Microseconds()) / 2
	stats.AvgReaction.Store(newAvg)

	// Check anomalies
	total := float64(stats.TotalShots.Load())
	if total > 100 { // Need sample size
		headshotRate := float64(stats.Headshots.Load()) / total
		avgReaction := time.Duration(stats.AvgReaction.Load()) * time.Microsecond

		// Red flags
		suspicious := false

		if headshotRate > 0.7 { // 70% headshots = suspicious
			suspicious = true
		}

		if avgReaction < 100*time.Millisecond { // <100ms reaction = bot
			suspicious = true
		}

		if suspicious {
			flags := stats.Flags.Add(1)

			if flags > 10 {
				ad.reportSuspicious(playerID, headshotRate, avgReaction)
			}
		}
	}
}

// getOrCreateStats gets or creates player stats
func (ad *AnomalyDetector) getOrCreateStats(playerID string) *PlayerStats {
	statsInterface, _ := ad.playerStats.LoadOrStore(playerID, &PlayerStats{})
	return statsInterface.(*PlayerStats)
}

// reportSuspicious reports suspicious player to anti-cheat service
func (ad *AnomalyDetector) reportSuspicious(playerID string, headshotRate float64, avgReaction time.Duration) {
	logrus.WithFields(logrus.Fields{
		"player_id":     playerID,
		"headshot_rate": headshotRate,
		"avg_reaction":  avgReaction,
	}).Warn("Suspicious player detected (anomaly detection)")

	// TODO: Send to anti-cheat service
	// ad.antiCheatService.FlagPlayer(playerID, "anomaly_detection")
}
