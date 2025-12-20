package server

import (
	"encoding/json"
	"net/http"
	"time"
)

// CalculateDamage calculates damage for attacks
func (s *CombatService) CalculateDamage(w http.ResponseWriter, r *http.Request) {
	var req DamageCalculationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode damage calculation request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	s.metrics.DamageCalculations.Inc()

	// Calculate damage (simplified formula)
	baseDamage := req.BaseDamage
	criticalMultiplier := 1.0
	armorReduction := 0

	// Apply modifiers
	for _, mod := range req.Modifiers {
		switch mod.Type {
		case "MULTIPLY":
			baseDamage = int(float64(baseDamage) * mod.Value.(float64))
		case "ADD":
			baseDamage += int(mod.Value.(float64))
		case "CRITICAL":
			if mod.Value.(float64) > 1.0 {
				criticalMultiplier = mod.Value.(float64)
			}
		case "ARMOR":
			armorReduction = int(mod.Value.(float64))
		}
	}

	finalDamage := baseDamage - armorReduction
	if finalDamage < 0 {
		finalDamage = 0
	}

	// OPTIMIZATION: Issue #1607 - Use memory pool
	resp := s.damageCalculationResponsePool.Get().(*DamageCalculationResponse)
	defer s.damageCalculationResponsePool.Put(resp)

	resp.FinalDamage = finalDamage
	resp.DamageType = req.AttackType
	resp.CriticalMultiplier = criticalMultiplier
	resp.ArmorReduction = armorReduction
	resp.ModifiersApplied = req.Modifiers

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// calculateDamage helper method for damage calculation
func (s *CombatService) calculateDamage() DamageResult {
	// Simplified damage calculation
	baseDamage := 25
	isCritical := false

	// 10% chance for critical hit
	if time.Now().UnixNano()%10 == 0 {
		isCritical = true
		baseDamage *= 2
	}

	return DamageResult{
		TotalDamage:        baseDamage,
		DamageType:         "PHYSICAL",
		CriticalHit:        isCritical,
		Blocked:            false,
		Mitigated:          5,
		CriticalMultiplier: 2.0,
	}
}
