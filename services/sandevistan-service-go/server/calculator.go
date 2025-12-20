// Package server Issue: #140875766 - Sandevistan Calculator
// Extracted from service.go to follow Single Responsibility Principle
package server

import (
	"math"
	"time"
)

// SandevistanCalculator handles all calculations for Sandevistan mechanics
type SandevistanCalculator struct{}

// NewSandevistanCalculator creates a new calculator
func NewSandevistanCalculator() *SandevistanCalculator {
	return &SandevistanCalculator{}
}

// calculateDuration calculates the duration of Sandevistan activation
func (c *SandevistanCalculator) calculateDuration(level int, override *float64) float32 {
	if override != nil {
		return *override
	}

	// Base duration increases with level
	baseDuration := 3.0                           // 3 seconds base
	levelMultiplier := 1.0 + float64(level-1)*0.2 // +20% per level
	return baseDuration * levelMultiplier
}

// calculateCooldown calculates the cooldown time after Sandevistan deactivation
func (c *SandevistanCalculator) calculateCooldown(level int) float32 {
	// Cooldown decreases with level (better upgrades)
	baseCooldown := 60.0                     // 60 seconds base
	levelReduction := float64(level-1) * 2.0 // -2 seconds per level
	cooldown := baseCooldown - levelReduction

	// Minimum cooldown of 20 seconds
	if cooldown < 20.0 {
		return 20.0
	}
	return cooldown
}

// calculateTimeDilation calculates the time dilation factor
func (c *SandevistanCalculator) calculateTimeDilation(level int) float32 {
	// Time dilation increases with level
	baseDilation := 0.25                  // 25% speed increase (4x speed)
	levelBonus := float64(level-1) * 0.05 // +5% per level
	return baseDilation + levelBonus
}

// calculateCyberpsychosisIncrease calculates how much cyberpsychosis increases per second
func (c *SandevistanCalculator) calculateCyberpsychosisIncrease(currentLevel, resistance float64) float64 {
	// Base increase per second
	baseIncrease := 0.1

	// Resistance reduces the increase
	resistanceFactor := 1.0 - (resistance / 100.0)
	if resistanceFactor < 0.1 {
		resistanceFactor = 0.1 // Minimum 10% of base
	}

	// Current level increases the rate
	levelFactor := 1.0 + (currentLevel / 100.0)

	return baseIncrease * resistanceFactor * levelFactor
}

// calculateHeatIncrease calculates heat increase per second
func (c *SandevistanCalculator) calculateHeatIncrease(currentLevel float64) float64 {
	// Heat increases based on current level
	baseIncrease := 0.05
	levelFactor := 1.0 + (currentLevel / 50.0) // Faster heat buildup at higher levels
	return baseIncrease * levelFactor
}

// calculateHeatDissipation calculates heat dissipation when Sandevistan is inactive
func (c *SandevistanCalculator) calculateHeatDissipation(currentLevel float64) float64 {
	// Heat dissipates over time
	baseDissipation := 0.02
	levelFactor := 1.0 + (currentLevel / 100.0) // Better dissipation at higher levels?
	return baseDissipation * levelFactor
}

// calculateResistance calculates cyberpsychosis resistance
func (c *SandevistanCalculator) calculateResistance(level int) float32 {
	// Resistance increases with level
	baseResistance := 10.0               // 10% base resistance
	levelBonus := float64(level-1) * 5.0 // +5% per level
	return baseResistance + levelBonus
}

// calculateDissipationRate calculates cyberpsychosis dissipation rate
func (c *SandevistanCalculator) calculateDissipationRate(level int) float32 {
	// Dissipation rate increases with level
	baseRate := 0.01                       // 1% per second base
	levelBonus := float64(level-1) * 0.005 // +0.5% per level
	return baseRate + levelBonus
}

// calculateUpgradeCost calculates the cost for upgrading Sandevistan
func (c *SandevistanCalculator) calculateUpgradeCost(upgradeType string, level int) int {
	baseCost := 1000 // Base cost

	// Cost increases exponentially with level
	levelMultiplier := math.Pow(1.5, float64(level-1))

	// Different costs for different upgrade types
	typeMultiplier := 1.0
	switch upgradeType {
	case "duration":
		typeMultiplier = 1.2
	case "cooldown":
		typeMultiplier = 1.1
	case "efficiency":
		typeMultiplier = 1.3
	case "resistance":
		typeMultiplier = 1.4
	case "capacity":
		typeMultiplier = 1.5
	}

	return int(float64(baseCost) * levelMultiplier * typeMultiplier)
}

// calculateMaxHeatLevel calculates the maximum heat level before failure
func (c *SandevistanCalculator) calculateMaxHeatLevel(level int) float64 {
	// Max heat increases with level (better cooling)
	baseMax := 80.0
	levelBonus := float64(level-1) * 5.0 // +5 per level
	return baseMax + levelBonus
}

// calculateFailureProbability calculates the probability of Sandevistan failure
func (c *SandevistanCalculator) calculateFailureProbability(heatLevel, cyberpsychosisLevel float64, level int) float64 {
	// Base failure rate
	baseRate := 0.001 // 0.1% base chance per second

	// Heat contribution
	heatFactor := heatLevel / 100.0 // Normalized to 0-1

	// Cyberpsychosis contribution
	psychosisFactor := cyberpsychosisLevel / 100.0 // Normalized to 0-1

	// Level reduces failure rate
	levelFactor := 1.0 / (1.0 + float64(level-1)*0.1) // Diminishing returns

	totalFactor := (heatFactor + psychosisFactor) / 2.0 // Average of both
	return baseRate * totalFactor * levelFactor
}

// calculateRecoveryTime calculates recovery time after failure
func (c *SandevistanCalculator) calculateRecoveryTime(failureSeverity float64) time.Duration {
	// Recovery time based on failure severity (0-1)
	baseTime := 300.0                               // 5 minutes base
	severityMultiplier := 1.0 + failureSeverity*2.0 // Up to 3x longer for severe failures
	return time.Duration(baseTime*severityMultiplier) * time.Second
}

// calculateOptimalActivationTime calculates the optimal activation duration
func (c *SandevistanCalculator) calculateOptimalActivationTime(level int, currentCyberpsychosis float64) float64 {
	// Optimal time decreases as cyberpsychosis increases
	baseOptimal := c.calculateDuration(level, nil) * 0.8 // 80% of max duration

	// Reduce optimal time based on current cyberpsychosis
	psychosisFactor := 1.0 - (currentCyberpsychosis / 150.0) // Reduce to 0 at 150% psychosis
	if psychosisFactor < 0.3 {
		psychosisFactor = 0.3 // Minimum 30% of optimal
	}

	return baseOptimal * psychosisFactor
}
