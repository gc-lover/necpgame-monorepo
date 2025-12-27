package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// TestDamageCalculation tests the core damage calculation logic
func TestHandlers_calculateDamage(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		name           string
		req            *api.DamageCalculationRequest
		expectedMin    float64
		expectedMax    float64
		expectCritical bool
	}{
		{
			name: "Basic pistol damage",
			req: &api.DamageCalculationRequest{
				AttackerId:         uuid.New(),
				TargetId:           uuid.New(),
				BaseDamage:         100,
				WeaponType:         "pistol",
				CriticalChance:     0.0,
				CriticalMultiplier: 2.0,
				ArmorRating:        0,
				Penetration:        0,
				EnvironmentType:    "normal",
				ImplantEffects:     []string{},
			},
			expectedMin:    100,
			expectedMax:    100,
			expectCritical: false,
		},
		{
			name: "Rifle with armor reduction",
			req: &api.DamageCalculationRequest{
				AttackerId:         uuid.New(),
				TargetId:           uuid.New(),
				BaseDamage:         100,
				WeaponType:         "rifle",
				CriticalChance:     0.0,
				CriticalMultiplier: 2.0,
				ArmorRating:        50,
				Penetration:        0.2,
				EnvironmentType:    "normal",
				ImplantEffects:     []string{},
			},
			expectedMin:    100 * 1.2 * 0.6, // weapon multiplier * armor reduction
			expectedMax:    100 * 1.2 * 0.6,
			expectCritical: false,
		},
		{
			name: "Melee with implant synergy",
			req: &api.DamageCalculationRequest{
				AttackerId:         uuid.New(),
				TargetId:           uuid.New(),
				BaseDamage:         100,
				WeaponType:         "melee",
				CriticalChance:     0.0,
				CriticalMultiplier: 2.0,
				ArmorRating:        0,
				Penetration:        0,
				EnvironmentType:    "normal",
				ImplantEffects:     []string{"damage_boost", "critical_boost"},
			},
			expectedMin:    100 * 0.8 * 1.15, // weapon multiplier * implant synergy
			expectedMax:    100 * 0.8 * 1.15,
			expectCritical: false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result, err := h.calculateDamage(context.Background(), tt.req)

			require.NoError(t, err)
			assert.Equal(t, tt.req.AttackerId, result.AttackerId)
			assert.Equal(t, tt.req.TargetId, result.TargetId)
			assert.GreaterOrEqual(t, result.TotalDamage, tt.expectedMin)
			assert.LessOrEqual(t, result.TotalDamage, tt.expectedMax)
			assert.GreaterOrEqual(t, result.TotalDamage, 1.0) // Minimum damage check
		})
	}
}

// TestValidateDamageRequest tests request validation
func TestHandlers_validateDamageRequest(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		name    string
		req     *api.DamageCalculationRequest
		wantErr bool
	}{
		{
			name: "Valid request",
			req: &api.DamageCalculationRequest{
				AttackerId:     uuid.New(),
				TargetId:       uuid.New(),
				BaseDamage:     100,
				WeaponType:     "pistol",
				CriticalChance: 0.1,
			},
			wantErr: false,
		},
		{
			name: "Missing attacker ID",
			req: &api.DamageCalculationRequest{
				TargetId:       uuid.New(),
				BaseDamage:     100,
				WeaponType:     "pistol",
				CriticalChance: 0.1,
			},
			wantErr: true,
		},
		{
			name: "Missing target ID",
			req: &api.DamageCalculationRequest{
				AttackerId:     uuid.New(),
				BaseDamage:     100,
				WeaponType:     "pistol",
				CriticalChance: 0.1,
			},
			wantErr: true,
		},
		{
			name: "Zero base damage",
			req: &api.DamageCalculationRequest{
				AttackerId:     uuid.New(),
				TargetId:       uuid.New(),
				BaseDamage:     0,
				WeaponType:     "pistol",
				CriticalChance: 0.1,
			},
			wantErr: true,
		},
		{
			name: "Empty weapon type",
			req: &api.DamageCalculationRequest{
				AttackerId:     uuid.New(),
				TargetId:       uuid.New(),
				BaseDamage:     100,
				WeaponType:     "",
				CriticalChance: 0.1,
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := h.validateDamageRequest(tt.req)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

// TestWeaponMultiplier tests weapon damage multipliers
func TestHandlers_getWeaponMultiplier(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		weaponType string
		expected   float64
	}{
		{"pistol", 1.0},
		{"rifle", 1.2},
		{"shotgun", 1.5},
		{"sniper", 2.0},
		{"melee", 0.8},
		{"unknown", 1.0},
		{"", 1.0},
	}

	for _, tt := range tests {
		t.Run(tt.weaponType, func(t *testing.T) {
			result := h.getWeaponMultiplier(tt.weaponType)
			assert.Equal(t, tt.expected, result)
		})
	}
}

// TestEnvironmentalModifier tests environmental damage modifiers
func TestHandlers_getEnvironmentalModifier(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		envType  string
		expected float64
	}{
		{"rain", 0.9},
		{"fog", 0.8},
		{"night", 1.1},
		{"normal", 1.0},
		{"unknown", 1.0},
		{"", 1.0},
	}

	for _, tt := range tests {
		t.Run(tt.envType, func(t *testing.T) {
			result := h.getEnvironmentalModifier(tt.envType)
			assert.Equal(t, tt.expected, result)
		})
	}
}

// TestImplantSynergy tests implant effect synergy calculations
func TestHandlers_calculateImplantSynergy(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		name     string
		effects  []string
		expected float64
	}{
		{
			name:     "No effects",
			effects:  []string{},
			expected: 1.0,
		},
		{
			name:     "Single damage boost",
			effects:  []string{"damage_boost"},
			expected: 1.1,
		},
		{
			name:     "Multiple effects",
			effects:  []string{"damage_boost", "critical_boost"},
			expected: 1.15,
		},
		{
			name:     "Unknown effects",
			effects:  []string{"unknown_effect"},
			expected: 1.0,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := h.calculateImplantSynergy(tt.effects)
			assert.Equal(t, tt.expected, result)
		})
	}
}

// TestArmorReduction tests armor damage reduction calculations
func TestHandlers_calculateArmorReduction(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		name        string
		armorRating float64
		penetration float64
		expected    float64
	}{
		{"No armor", 0, 0, 0},
		{"Basic armor", 50, 0, 0.5},
		{"Armor with penetration", 50, 0.2, 0.4},
		{"Full penetration", 50, 1.0, 0},
		{"High armor cap", 150, 0, 0.9}, // 90% cap
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := h.calculateArmorReduction(tt.armorRating, tt.penetration)
			assert.Equal(t, tt.expected, result)
		})
	}
}

// TestCombatEffectValidation tests combat effect validation
func TestHandlers_validateEffect(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		name    string
		effect  api.CombatEffect
		wantErr bool
	}{
		{
			name: "Valid effect",
			effect: api.CombatEffect{
				Type:       "damage_boost",
				Value:      1.1,
				DurationMs: 10000,
			},
			wantErr: false,
		},
		{
			name: "Empty type",
			effect: api.CombatEffect{
				Value:      1.1,
				DurationMs: 10000,
			},
			wantErr: true,
		},
		{
			name: "Zero duration",
			effect: api.CombatEffect{
				Type:       "damage_boost",
				Value:      1.1,
				DurationMs: 0,
			},
			wantErr: true,
		},
		{
			name: "Negative duration",
			effect: api.CombatEffect{
				Type:       "damage_boost",
				Value:      1.1,
				DurationMs: -1000,
			},
			wantErr: true,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			err := h.validateEffect(tt.effect)
			if tt.wantErr {
				assert.Error(t, err)
			} else {
				assert.NoError(t, err)
			}
		})
	}
}

// TestValidationScore tests damage validation scoring
func TestHandlers_calculateValidationScore(t *testing.T) {
	h := &Handlers{}

	tests := []struct {
		name      string
		expected  float64
		reported  float64
		score     float64
	}{
		{"Perfect match", 100, 100, 1.0},
		{"1% difference", 100, 99, 0.99},
		{"10% difference", 100, 90, 0.9},
		{"Zero expected", 0, 50, 0},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			result := h.calculateValidationScore(tt.expected, tt.reported)
			assert.Equal(t, tt.score, result)
		})
	}
}

// Issue: #2251
