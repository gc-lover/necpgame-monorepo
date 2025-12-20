// Package server Issue: #2210 - Cyberware calculations module (extracted from service.go for better organization)
package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/cyberware-service-go/models"
)

// calculateCyberpsychosis calculates total cyberpsychosis from active implants
func (s *CyberwareService) calculateCyberpsychosis(implants []*models.PlayerImplant) float64 {
	total := 0.0
	for _, implant := range implants {
		if implant.Active {
			total += implant.Cyberpsychosis
		}
	}
	return total
}

// calculateSynergyEffects calculates synergy bonuses from active implants
func (s *CyberwareService) calculateSynergyEffects(implants []*models.PlayerImplant) map[string]interface{} {
	effects := make(map[string]interface{})

	// Count implants by type and category for synergy calculations
	typeCount := make(map[models.ImplantType]int)
	categoryCount := make(map[models.ImplantCategory]int)

	for _, implant := range implants {
		if implant.Active {
			typeCount[implant.Type]++
			categoryCount[implant.Category]++
		}
	}

	// Calculate synergy bonuses
	// TODO: Implement specific synergy rules based on game design

	return effects
}

// calculateUpgradeCost calculates upgrade cost for implant level
func (s *CyberwareService) calculateUpgradeCost(currentLevel int) map[string]int {
	// Exponential cost scaling
	baseCost := 100
	multiplier := 1 << (currentLevel - 1) // 2^(level-1)

	return map[string]int{
		"money":     baseCost * multiplier,
		"materials": baseCost * multiplier / 2,
	}
}

// calculateCyberpsychosisIncrease calculates cyberpsychosis increase for level
func (s *CyberwareService) calculateCyberpsychosisIncrease(currentLevel int) float64 {
	// Cyberpsychosis increases with level
	return float64(currentLevel) * 0.5
}

// calculateStatImprovements calculates stat improvements for next level
func (s *CyberwareService) calculateStatImprovements(implant *models.PlayerImplant) map[string]float64 {
	// Calculate stat improvements for next level
	improvements := make(map[string]float64)
	for stat, value := range implant.Stats {
		improvements[stat] = value * 0.1 // 10% improvement per level
	}
	return improvements
}
