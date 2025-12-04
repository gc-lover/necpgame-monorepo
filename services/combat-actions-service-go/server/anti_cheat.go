// Issue: #1587 - Server-Side Validation & Anti-Cheat Integration
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
	ErrSpeedHack      = errors.New("movement speed exceeds maximum")
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

// ValidateAttack validates an attack action (anti-cheat: rate, distance, angle)
func (av *ActionValidator) ValidateAttack(playerID string, attack *AttackAction) error {
	// 1. Rate check (max 5 attacks/sec)
	if last, ok := av.lastActions.Load(playerID); ok {
		lastAction := last.(*LastAction)
		minInterval := 200 * time.Millisecond // 5 attacks/sec

		if time.Since(lastAction.Timestamp) < minInterval {
			logrus.WithFields(logrus.Fields{
				"player_id":    playerID,
				"time_since":    time.Since(lastAction.Timestamp),
				"min_interval": minInterval,
			}).Warn("Attack rate too high (potential macro)")
			return ErrTooFast
		}
	}

	// 2. Distance check (melee range: 2m, ranged: weapon range)
	if attack.Distance > av.getAttackRange(attack.AttackType) {
		logrus.WithFields(logrus.Fields{
			"player_id":   playerID,
			"attack_type": attack.AttackType,
			"distance":    attack.Distance,
			"max_range":   av.getAttackRange(attack.AttackType),
		}).Warn("Attack distance exceeds range")
		return ErrImpossibleShot
	}

	// 3. Angle check (prevent 180° instant turns)
	if last, ok := av.lastActions.Load(playerID); ok {
		lastAction := last.(*LastAction)
		angleDiff := calculateAngleDiff(lastAction.Position, attack.From)

		if angleDiff > 180.0 && time.Since(lastAction.Timestamp) < 100*time.Millisecond {
			logrus.WithFields(logrus.Fields{
				"player_id":  playerID,
				"angle_diff": angleDiff,
				"time_since":  time.Since(lastAction.Timestamp),
			}).Warn("Impossible turn angle (potential aimbot)")
			return ErrImpossibleTurn
		}
	}

	// Update last action
	av.lastActions.Store(playerID, &LastAction{
		Timestamp:  time.Now(),
		Position:   attack.From,
		ActionType: "attack",
	})

	return nil
}

// AttackAction represents an attack action
type AttackAction struct {
	From       Vec3
	To         Vec3
	Distance   float32
	AttackType string
}

// getAttackRange returns maximum range for attack type
func (av *ActionValidator) getAttackRange(attackType string) float32 {
	ranges := map[string]float32{
		"melee":   2.0,   // 2 meters
		"ranged":  150.0, // 150 meters
		"ability": 50.0,  // 50 meters
		"default": 5.0,   // 5 meters (default)
	}

	attackRange, ok := ranges[attackType]
	if !ok {
		attackRange = ranges["default"]
	}

	return attackRange
}

// hasLineOfSight checks if there's a clear line of sight between two points
// TODO: Integrate with world service for actual raycast
func (av *ActionValidator) hasLineOfSight(from, to Vec3) bool {
	// Simplified check - in production, use actual raycast from world service
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

// RecordAttack records an attack and checks for anomalies
func (ad *AnomalyDetector) RecordAttack(playerID string, critical bool, reactionTime time.Duration) {
	stats := ad.getOrCreateStats(playerID)

	stats.TotalShots.Add(1)
	if critical {
		stats.Headshots.Add(1) // Use Headshots field for critical hits
	}

	// Update average reaction time
	currentAvg := stats.AvgReaction.Load()
	newAvg := (currentAvg + int64(reactionTime.Microseconds())) / 2
	stats.AvgReaction.Store(newAvg)

	// Check anomalies
	total := float64(stats.TotalShots.Load())
	if total > 100 { // Need sample size
		criticalRate := float64(stats.Headshots.Load()) / total
		avgReaction := time.Duration(stats.AvgReaction.Load()) * time.Microsecond

		// Red flags
		suspicious := false

		if criticalRate > 0.7 { // 70% critical hits = suspicious
			suspicious = true
		}

		if avgReaction < 100*time.Millisecond { // <100ms reaction = bot
			suspicious = true
		}

		if suspicious {
			flags := stats.Flags.Add(1)

			if flags > 10 {
				ad.reportSuspicious(playerID, criticalRate, avgReaction)
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
func (ad *AnomalyDetector) reportSuspicious(playerID string, criticalRate float64, avgReaction time.Duration) {
	logrus.WithFields(logrus.Fields{
		"player_id":     playerID,
		"critical_rate": criticalRate,
		"avg_reaction":  avgReaction,
	}).Warn("Suspicious player detected (anomaly detection)")

	// TODO: Send to anti-cheat service
	// ad.antiCheatService.FlagPlayer(playerID, "anomaly_detection")
}

