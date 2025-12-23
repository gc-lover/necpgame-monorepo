// Combat Stats Validator - Input validation layer
// Issue: #2245
// PERFORMANCE: Fast validation for high-throughput updates

package server

import (
	"fmt"
	"regexp"

	"github.com/google/uuid"
	"github.com/gc-lover/necpgame-monorepo/services/combat-stats-service-go/pkg/api"
)

// Validator provides input validation for combat statistics
// PERFORMANCE: Regex compilation and fast validation
type Validator struct {
	uuidRegex *regexp.Regexp
}

// NewValidator creates a new validator instance
func NewValidator() *Validator {
	return &Validator{
		uuidRegex: regexp.MustCompile(`^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$`),
	}
}

// ValidateUpdateRequest validates player stats update request
func (v *Validator) ValidateUpdateRequest(req *api.UpdatePlayerStatsRequest) error {
	if req == nil {
		return fmt.Errorf("request cannot be nil")
	}

	stats := req.SessionStats

	// PERFORMANCE: Fast integer validations
	if stats.Kills < 0 {
		return fmt.Errorf("kills cannot be negative")
	}

	if stats.Deaths < 0 {
		return fmt.Errorf("deaths cannot be negative")
	}

	if stats.DamageDealt < 0 {
		return fmt.Errorf("damage_dealt cannot be negative")
	}

	if stats.DamageReceived < 0 {
		return fmt.Errorf("damage_received cannot be negative")
	}

	if stats.SessionDuration <= 0 {
		return fmt.Errorf("session_duration must be positive")
	}

	// PERFORMANCE: Validate weapon data
	for i, weapon := range stats.WeaponsUsed {
		if err := v.validateWeaponUsage(weapon, i); err != nil {
			return err
		}
	}

	// PERFORMANCE: Validate abilities data
	for i, ability := range stats.AbilitiesUsed {
		if err := v.validateAbilityUsage(ability, i); err != nil {
			return err
		}
	}

	return nil
}

// validateWeaponUsage validates weapon usage data
func (v *Validator) validateWeaponUsage(weapon api.UpdatePlayerStatsRequestSessionStatsWeaponsUsedItem, index int) error {
	if weapon.WeaponID == uuid.Nil {
		return fmt.Errorf("weapon[%d]: weapon_id cannot be empty", index)
	}

	if weapon.ShotsFired < 0 {
		return fmt.Errorf("weapon[%d]: shots_fired cannot be negative", index)
	}

	if weapon.Hits < 0 {
		return fmt.Errorf("weapon[%d]: hits cannot be negative", index)
	}

	if weapon.Hits > weapon.ShotsFired {
		return fmt.Errorf("weapon[%d]: hits cannot exceed shots_fired", index)
	}

	if headshots, ok := weapon.Headshots.Get(); ok {
		if headshots < 0 {
			return fmt.Errorf("weapon[%d]: headshots cannot be negative", index)
		}
		if headshots > weapon.Hits {
			return fmt.Errorf("weapon[%d]: headshots cannot exceed hits", index)
		}
	}

	return nil
}

// validateAbilityUsage validates ability usage data
func (v *Validator) validateAbilityUsage(ability api.UpdatePlayerStatsRequestSessionStatsAbilitiesUsedItem, index int) error {
	if ability.AbilityID == uuid.Nil {
		return fmt.Errorf("ability[%d]: ability_id cannot be empty", index)
	}

	if ability.TimesUsed < 0 {
		return fmt.Errorf("ability[%d]: times_used cannot be negative", index)
	}

	return nil
}
