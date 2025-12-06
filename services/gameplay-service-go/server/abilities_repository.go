// Issue: #156
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
)

type AbilityRepositoryInterface interface {
	GetCatalog(ctx context.Context, abilityType *api.AbilityType, slot *api.AbilitySlot, source *api.AbilitySource, limit, offset int) ([]api.Ability, int, error)
	GetAbility(ctx context.Context, abilityID uuid.UUID) (*api.Ability, error)
	GetLoadout(ctx context.Context, characterID uuid.UUID) (*api.AbilityLoadout, error)
	SaveLoadout(ctx context.Context, loadout *api.AbilityLoadout) (*api.AbilityLoadout, error)
	GetCooldowns(ctx context.Context, characterID uuid.UUID) ([]api.CooldownStatus, error)
	StartCooldown(ctx context.Context, characterID, abilityID uuid.UUID, duration time.Duration) error
	RecordActivation(ctx context.Context, characterID, abilityID uuid.UUID, targetID *uuid.UUID) error
	GetAvailableSynergies(ctx context.Context, characterID uuid.UUID, abilityID *uuid.UUID) ([]api.Synergy, error)
	UpdateCyberpsychosis(ctx context.Context, characterID uuid.UUID, impact float32) error
	GetSynergy(ctx context.Context, synergyID uuid.UUID) (*api.Synergy, error)
	CheckSynergyRequirements(ctx context.Context, characterID uuid.UUID, synergy *api.Synergy) (bool, error)
	ApplySynergy(ctx context.Context, characterID, synergyID uuid.UUID, synergy *api.Synergy) error
	GetCyberpsychosisState(ctx context.Context, characterID uuid.UUID) (*api.CyberpsychosisState, error)
	GetAbilityMetrics(ctx context.Context, characterID uuid.UUID, abilityID api.OptUUID, periodStart api.OptDateTime, periodEnd api.OptDateTime) (*api.AbilityMetrics, error)
}

type AbilityRepository struct {
	db *pgxpool.Pool
}

func NewAbilityRepository(db *pgxpool.Pool) *AbilityRepository {
	return &AbilityRepository{db: db}
}

func (r *AbilityRepository) GetCatalog(ctx context.Context, abilityType *api.AbilityType, slot *api.AbilitySlot, source *api.AbilitySource, limit, offset int) ([]api.Ability, int, error) {
	query := `SELECT id, name, description, ability_type, slot, source, rank, 
			  energy_cost, health_cost, cooldown_base, cyberpsychosis_impact, 
			  requirements, modifiers, created_at
			  FROM gameplay.abilities_catalog WHERE 1=1`
	
	args := []interface{}{}
	argIndex := 1

	if abilityType != nil {
		query += ` AND ability_type = $` + string(rune('0'+argIndex))
		args = append(args, string(*abilityType))
		argIndex++
	}

	if slot != nil {
		query += ` AND slot = $` + string(rune('0'+argIndex))
		args = append(args, string(*slot))
		argIndex++
	}

	if source != nil {
		query += ` AND source = $` + string(rune('0'+argIndex))
		args = append(args, string(*source))
		argIndex++
	}

	// Count total
	var total int
	countQuery := `SELECT COUNT(*) FROM gameplay.abilities_catalog WHERE 1=1`
	if abilityType != nil {
		countQuery += ` AND ability_type = $1`
	}
	if slot != nil {
		countQuery += ` AND slot = $2`
	}
	if source != nil {
		countQuery += ` AND source = $3`
	}
	err := r.db.QueryRow(ctx, countQuery, args[:argIndex-1]...).Scan(&total)
	if err != nil {
		return nil, 0, err
	}

	query += ` ORDER BY rank DESC, name ASC LIMIT $` + string(rune('0'+argIndex)) + ` OFFSET $` + string(rune('0'+argIndex+1))
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var abilities []api.Ability
	for rows.Next() {
		var ability api.Ability
		var requirementsJSON, modifiersJSON []byte
		var description, createdAt sql.NullString
		var energyCost, healthCost, cooldownBase sql.NullInt64
		var cyberpsychosisImpact sql.NullFloat64

		err := rows.Scan(
			&ability.ID, &ability.Name, &description,
			&ability.AbilityType, &ability.Slot, &ability.Source, &ability.Rank,
			&energyCost, &healthCost, &cooldownBase, &cyberpsychosisImpact,
			&requirementsJSON, &modifiersJSON, &createdAt,
		)
		if err != nil {
			return nil, 0, err
		}

		if description.Valid {
			ability.Description = api.NewOptString(description.String)
		}
		if energyCost.Valid {
			ability.EnergyCost = api.NewOptInt(int(energyCost.Int64))
		}
		if healthCost.Valid {
			ability.HealthCost = api.NewOptInt(int(healthCost.Int64))
		}
		if cooldownBase.Valid {
			ability.CooldownBase = api.NewOptInt(int(cooldownBase.Int64))
		}
		if cyberpsychosisImpact.Valid {
			ability.CyberpsychosisImpact = api.NewOptFloat32(float32(cyberpsychosisImpact.Float64))
		}
		if createdAt.Valid {
			if t, err := time.Parse(time.RFC3339, createdAt.String); err == nil {
				ability.CreatedAt = api.NewOptDateTime(t)
			}
		}

		if len(requirementsJSON) > 0 {
			var req api.AbilityRequirements
			if err := json.Unmarshal(requirementsJSON, &req); err == nil {
				ability.Requirements = api.NewOptAbilityRequirements(req)
			}
		}

		if len(modifiersJSON) > 0 {
			var mod api.AbilityModifiers
			if err := json.Unmarshal(modifiersJSON, &mod); err == nil {
				ability.Modifiers = api.NewOptAbilityModifiers(mod)
			}
		}

		abilities = append(abilities, ability)
	}

	return abilities, total, nil
}

func (r *AbilityRepository) GetAbility(ctx context.Context, abilityID uuid.UUID) (*api.Ability, error) {
	var ability api.Ability
	var requirementsJSON, modifiersJSON []byte
	var description, createdAt sql.NullString
	var energyCost, healthCost, cooldownBase sql.NullInt64
	var cyberpsychosisImpact sql.NullFloat64

	err := r.db.QueryRow(ctx,
		`SELECT id, name, description, ability_type, slot, source, rank,
		 energy_cost, health_cost, cooldown_base, cyberpsychosis_impact,
		 requirements, modifiers, created_at
		 FROM gameplay.abilities_catalog WHERE id = $1`,
		abilityID).Scan(
		&ability.ID, &ability.Name, &description,
		&ability.AbilityType, &ability.Slot, &ability.Source, &ability.Rank,
		&energyCost, &healthCost, &cooldownBase, &cyberpsychosisImpact,
		&requirementsJSON, &modifiersJSON, &createdAt,
	)

	if err != nil {
		return nil, errors.New("ability not found")
	}

	if description.Valid {
		ability.Description = api.NewOptString(description.String)
	}
	if energyCost.Valid {
		ability.EnergyCost = api.NewOptInt(int(energyCost.Int64))
	}
	if healthCost.Valid {
		ability.HealthCost = api.NewOptInt(int(healthCost.Int64))
	}
	if cooldownBase.Valid {
		ability.CooldownBase = api.NewOptInt(int(cooldownBase.Int64))
	}
	if cyberpsychosisImpact.Valid {
		ability.CyberpsychosisImpact = api.NewOptFloat32(float32(cyberpsychosisImpact.Float64))
	}
	if createdAt.Valid {
		if t, err := time.Parse(time.RFC3339, createdAt.String); err == nil {
			ability.CreatedAt = api.NewOptDateTime(t)
		}
	}

	if len(requirementsJSON) > 0 {
		var req api.AbilityRequirements
		if err := json.Unmarshal(requirementsJSON, &req); err == nil {
			ability.Requirements = api.NewOptAbilityRequirements(req)
		}
	}

	if len(modifiersJSON) > 0 {
		var mod api.AbilityModifiers
		if err := json.Unmarshal(modifiersJSON, &mod); err == nil {
			ability.Modifiers = api.NewOptAbilityModifiers(mod)
		}
	}

	return &ability, nil
}

func (r *AbilityRepository) GetLoadout(ctx context.Context, characterID uuid.UUID) (*api.AbilityLoadout, error) {
	var loadout api.AbilityLoadout
	var passiveJSON, hackingJSON []byte
	var slotQ, slotE, slotR sql.NullString
	var createdAt, updatedAt sql.NullString

	err := r.db.QueryRow(ctx,
		`SELECT id, character_id, slot_q, slot_e, slot_r, passive_abilities, 
		 hacking_abilities, auto_cast_enabled, created_at, updated_at
		 FROM gameplay.ability_loadouts WHERE character_id = $1`,
		characterID).Scan(
		&loadout.ID, &loadout.CharacterID, &slotQ, &slotE, &slotR,
		&passiveJSON, &hackingJSON, &loadout.AutoCastEnabled,
		&createdAt, &updatedAt,
	)

	if err != nil {
		return nil, errors.New("loadout not found")
	}

	if slotQ.Valid {
		if id, err := uuid.Parse(slotQ.String); err == nil {
			loadout.SlotQ = api.NewOptNilUUID(id)
		}
	}
	if slotE.Valid {
		if id, err := uuid.Parse(slotE.String); err == nil {
			loadout.SlotE = api.NewOptNilUUID(id)
		}
	}
	if slotR.Valid {
		if id, err := uuid.Parse(slotR.String); err == nil {
			loadout.SlotR = api.NewOptNilUUID(id)
		}
	}

	if len(passiveJSON) > 0 {
		var passive []uuid.UUID
		if err := json.Unmarshal(passiveJSON, &passive); err == nil {
			loadout.PassiveAbilities = passive
		}
	}

	if len(hackingJSON) > 0 {
		var hacking []uuid.UUID
		if err := json.Unmarshal(hackingJSON, &hacking); err == nil {
			loadout.HackingAbilities = hacking
		}
	}

	if createdAt.Valid {
		if t, err := time.Parse(time.RFC3339, createdAt.String); err == nil {
			loadout.CreatedAt = api.NewOptDateTime(t)
		}
	}
	if updatedAt.Valid {
		if t, err := time.Parse(time.RFC3339, updatedAt.String); err == nil {
			loadout.UpdatedAt = api.NewOptDateTime(t)
		}
	}

	return &loadout, nil
}

func (r *AbilityRepository) SaveLoadout(ctx context.Context, loadout *api.AbilityLoadout) (*api.AbilityLoadout, error) {
	var slotQ, slotE, slotR *string
	if loadout.SlotQ.Set {
		s := loadout.SlotQ.Value.String()
		slotQ = &s
	}
	if loadout.SlotE.Set {
		s := loadout.SlotE.Value.String()
		slotE = &s
	}
	if loadout.SlotR.Set {
		s := loadout.SlotR.Value.String()
		slotR = &s
	}

	var passiveJSON, hackingJSON []byte
	if len(loadout.PassiveAbilities) > 0 {
		var err error
		passiveJSON, err = json.Marshal(loadout.PassiveAbilities)
		if err != nil {
			return nil, err
		}
	}
	if len(loadout.HackingAbilities) > 0 {
		var err error
		hackingJSON, err = json.Marshal(loadout.HackingAbilities)
		if err != nil {
			return nil, err
		}
	}

	createdAt := time.Now()
	if loadout.CreatedAt.Set {
		createdAt = loadout.CreatedAt.Value
	}
	updatedAt := time.Now()
	if loadout.UpdatedAt.Set {
		updatedAt = loadout.UpdatedAt.Value
	}

	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.ability_loadouts 
		 (id, character_id, slot_q, slot_e, slot_r, passive_abilities, 
		  hacking_abilities, auto_cast_enabled, created_at, updated_at)
		 VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
		 ON CONFLICT (character_id) DO UPDATE SET
		 slot_q = $3, slot_e = $4, slot_r = $5, passive_abilities = $6,
		 hacking_abilities = $7, auto_cast_enabled = $8, updated_at = $10`,
		loadout.ID, loadout.CharacterID, slotQ, slotE, slotR,
		passiveJSON, hackingJSON, loadout.AutoCastEnabled.Value,
		createdAt, updatedAt,
	)

	if err != nil {
		return nil, err
	}

	loadout.CreatedAt = api.NewOptDateTime(createdAt)
	loadout.UpdatedAt = api.NewOptDateTime(updatedAt)

	return loadout, nil
}

func (r *AbilityRepository) GetCooldowns(ctx context.Context, characterID uuid.UUID) ([]api.CooldownStatus, error) {
	rows, err := r.db.Query(ctx,
		`SELECT ability_id, started_at, expires_at, 
		 cooldown_duration, is_active
		 FROM gameplay.ability_cooldowns 
		 WHERE character_id = $1 AND is_active = true
		 ORDER BY expires_at ASC`,
		characterID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var cooldowns []api.CooldownStatus
	for rows.Next() {
		var cd api.CooldownStatus
		var startedAt, expiresAt sql.NullString
		var duration sql.NullInt64
		var isActive bool

		err := rows.Scan(
			&cd.AbilityID,
			&startedAt, &expiresAt, &duration, &isActive,
		)
		if err != nil {
			return nil, err
		}

		cd.IsOnCooldown = isActive

		if startedAt.Valid {
			if t, err := time.Parse(time.RFC3339, startedAt.String); err == nil {
				cd.StartedAt = api.NewOptNilDateTime(t)
			}
		}
		if expiresAt.Valid {
			if t, err := time.Parse(time.RFC3339, expiresAt.String); err == nil {
				cd.ExpiresAt = api.NewOptNilDateTime(t)
				// Calculate remaining seconds
				remaining := int(time.Until(t).Seconds())
				if remaining > 0 {
					cd.RemainingSeconds = api.NewOptNilInt(remaining)
				}
			}
		}
		if duration.Valid {
			cd.CooldownDuration = api.NewOptNilInt(int(duration.Int64))
		}

		cooldowns = append(cooldowns, cd)
	}

	return cooldowns, nil
}

func (r *AbilityRepository) StartCooldown(ctx context.Context, characterID, abilityID uuid.UUID, duration time.Duration) error {
	now := time.Now()
	expiresAt := now.Add(duration)

	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.ability_cooldowns 
		 (id, character_id, ability_id, started_at, expires_at, cooldown_duration, is_active)
		 VALUES (gen_random_uuid(), $1, $2, $3, $4, $5, true)
		 ON CONFLICT (character_id, ability_id) DO UPDATE SET
		 started_at = $3, expires_at = $4, cooldown_duration = $5, is_active = true`,
		characterID, abilityID, now, expiresAt, int(duration.Seconds()),
	)

	return err
}

func (r *AbilityRepository) RecordActivation(ctx context.Context, characterID, abilityID uuid.UUID, targetID *uuid.UUID) error {
	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.ability_effects_history 
		 (id, character_id, ability_id, activated_at, target_id, result, synergy_triggered)
		 VALUES (gen_random_uuid(), $1, $2, $3, $4, '{}'::jsonb, false)`,
		characterID, abilityID, time.Now(), targetID,
	)

	return err
}

func (r *AbilityRepository) GetAvailableSynergies(ctx context.Context, characterID uuid.UUID, abilityID *uuid.UUID) ([]api.Synergy, error) {
	query := `SELECT id, synergy_type, ability_ids, implant_ids, equipment_ids, 
			  requirements, bonuses, complexity, created_at
			  FROM gameplay.ability_synergies WHERE 1=1`
	
	args := []interface{}{}
	argIndex := 1

	if abilityID != nil {
		query += ` AND $` + string(rune('0'+argIndex)) + ` = ANY(ability_ids::text[])`
		args = append(args, abilityID.String())
		argIndex++
	}

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var synergies []api.Synergy
	for rows.Next() {
		var syn api.Synergy
		var abilityIDsJSON, implantIDsJSON, equipmentIDsJSON []byte
		var requirementsJSON, bonusesJSON []byte
		var createdAt sql.NullString

		err := rows.Scan(
			&syn.ID, &syn.SynergyType,
			&abilityIDsJSON, &implantIDsJSON, &equipmentIDsJSON,
			&requirementsJSON, &bonusesJSON, &syn.Complexity, &createdAt,
		)
		if err != nil {
			return nil, err
		}

		if len(abilityIDsJSON) > 0 {
			var ids []uuid.UUID
			if err := json.Unmarshal(abilityIDsJSON, &ids); err == nil {
				syn.AbilityIds = ids
			}
		}

		if len(implantIDsJSON) > 0 {
			var ids []uuid.UUID
			if err := json.Unmarshal(implantIDsJSON, &ids); err == nil {
				syn.ImplantIds = api.NewOptNilUUIDArray(ids)
			}
		}

		if len(equipmentIDsJSON) > 0 {
			var ids []uuid.UUID
			if err := json.Unmarshal(equipmentIDsJSON, &ids); err == nil {
				syn.EquipmentIds = api.NewOptNilUUIDArray(ids)
			}
		}

		if len(requirementsJSON) > 0 {
			var req api.SynergyRequirements
			if err := json.Unmarshal(requirementsJSON, &req); err == nil {
				syn.Requirements = api.NewOptSynergyRequirements(req)
			}
		}

		if len(bonusesJSON) > 0 {
			var bon api.SynergyBonuses
			if err := json.Unmarshal(bonusesJSON, &bon); err == nil {
				syn.Bonuses = bon
			}
		}

		if createdAt.Valid {
			if t, err := time.Parse(time.RFC3339, createdAt.String); err == nil {
				syn.CreatedAt = api.NewOptDateTime(t)
			}
		}

		synergies = append(synergies, syn)
	}

	return synergies, nil
}

func (r *AbilityRepository) UpdateCyberpsychosis(ctx context.Context, characterID uuid.UUID, impact float32) error {
	_, err := r.db.Exec(ctx,
		`INSERT INTO gameplay.cyberpsychosis_state (id, character_id, current_level, last_update)
		 VALUES (gen_random_uuid(), $1, $2, $3)
		 ON CONFLICT (character_id) DO UPDATE SET
		 current_level = cyberpsychosis_state.current_level + $2,
		 last_update = $3`,
		characterID, impact, time.Now(),
	)

	return err
}

// GetSynergy returns synergy by ID
// Issue: #156
func (r *AbilityRepository) GetSynergy(ctx context.Context, synergyID uuid.UUID) (*api.Synergy, error) {
	var syn api.Synergy
	var abilityIDsJSON, implantIDsJSON, equipmentIDsJSON []byte
	var requirementsJSON, bonusesJSON []byte
	var createdAt sql.NullString

	err := r.db.QueryRow(ctx,
		`SELECT id, synergy_type, ability_ids, implant_ids, equipment_ids,
		 requirements, bonuses, complexity, created_at
		 FROM gameplay.ability_synergies WHERE id = $1`,
		synergyID).Scan(
		&syn.ID, &syn.SynergyType,
		&abilityIDsJSON, &implantIDsJSON, &equipmentIDsJSON,
		&requirementsJSON, &bonusesJSON, &syn.Complexity, &createdAt,
	)

	if err != nil {
		return nil, errors.New("synergy not found")
	}

	if len(abilityIDsJSON) > 0 {
		var ids []uuid.UUID
		if err := json.Unmarshal(abilityIDsJSON, &ids); err == nil {
			syn.AbilityIds = ids
		}
	}

	if len(implantIDsJSON) > 0 {
		var ids []uuid.UUID
		if err := json.Unmarshal(implantIDsJSON, &ids); err == nil {
			syn.ImplantIds = api.NewOptNilUUIDArray(ids)
		}
	}

	if len(equipmentIDsJSON) > 0 {
		var ids []uuid.UUID
		if err := json.Unmarshal(equipmentIDsJSON, &ids); err == nil {
			syn.EquipmentIds = api.NewOptNilUUIDArray(ids)
		}
	}

	if len(requirementsJSON) > 0 {
		var req api.SynergyRequirements
		if err := json.Unmarshal(requirementsJSON, &req); err == nil {
			syn.Requirements = api.NewOptSynergyRequirements(req)
		}
	}

	if len(bonusesJSON) > 0 {
		var bon api.SynergyBonuses
		if err := json.Unmarshal(bonusesJSON, &bon); err == nil {
			syn.Bonuses = bon
		}
	}

	if createdAt.Valid {
		if t, err := time.Parse(time.RFC3339, createdAt.String); err == nil {
			syn.CreatedAt = api.NewOptDateTime(t)
		}
	}

	return &syn, nil
}

// CheckSynergyRequirements checks if character meets synergy requirements
// Issue: #156
func (r *AbilityRepository) CheckSynergyRequirements(ctx context.Context, characterID uuid.UUID, synergy *api.Synergy) (bool, error) {
	// Simplified check - in production, would check abilities, implants, equipment
	// For now, return true if synergy has requirements set
	if synergy.Requirements.Set {
		// TODO: Implement actual requirement checking
		return true, nil
	}
	return true, nil
}

// ApplySynergy applies synergy to character
// Issue: #156
func (r *AbilityRepository) ApplySynergy(ctx context.Context, characterID, synergyID uuid.UUID, synergy *api.Synergy) error {
	bonusesJSON, err := json.Marshal(synergy.Bonuses)
	if err != nil {
		return err
	}

	_, err = r.db.Exec(ctx,
		`INSERT INTO gameplay.ability_synergy_applications 
		 (id, character_id, synergy_id, applied_at, bonuses)
		 VALUES (gen_random_uuid(), $1, $2, $3, $4)`,
		characterID, synergyID, time.Now(), bonusesJSON,
	)

	return err
}

// GetCyberpsychosisState returns current cyberpsychosis state
// Issue: #156
func (r *AbilityRepository) GetCyberpsychosisState(ctx context.Context, characterID uuid.UUID) (*api.CyberpsychosisState, error) {
	var state api.CyberpsychosisState
	var lastUpdate sql.NullString
	var thresholdBreaches, activeImplantsCount sql.NullInt64
	var restrictionsJSON []byte

	err := r.db.QueryRow(ctx,
		`SELECT character_id, current_level, threshold_breaches, active_implants_count, last_update, restrictions
		 FROM gameplay.cyberpsychosis_state WHERE character_id = $1`,
		characterID).Scan(
		&state.CharacterID, &state.CurrentLevel,
		&thresholdBreaches, &activeImplantsCount, &lastUpdate, &restrictionsJSON,
	)

	if err != nil {
		// Return default state if not found
		return &api.CyberpsychosisState{
			CharacterID:       characterID,
			CurrentLevel:      0.0,
			ThresholdBreaches: api.NewOptInt(0),
			ActiveImplantsCount: api.NewOptInt(0),
			LastUpdate:        api.NewOptDateTime(time.Now()),
		}, nil
	}

	if thresholdBreaches.Valid {
		state.ThresholdBreaches = api.NewOptInt(int(thresholdBreaches.Int64))
	}

	if activeImplantsCount.Valid {
		state.ActiveImplantsCount = api.NewOptInt(int(activeImplantsCount.Int64))
	}

	if lastUpdate.Valid {
		if t, err := time.Parse(time.RFC3339, lastUpdate.String); err == nil {
			state.LastUpdate = api.NewOptDateTime(t)
		}
	}

	if len(restrictionsJSON) > 0 {
		var restrictions api.CyberpsychosisStateRestrictions
		if err := json.Unmarshal(restrictionsJSON, &restrictions); err == nil {
			state.Restrictions = api.NewOptCyberpsychosisStateRestrictions(restrictions)
		}
	}

	return &state, nil
}

// GetAbilityMetrics returns ability usage metrics
// Issue: #156
func (r *AbilityRepository) GetAbilityMetrics(ctx context.Context, characterID uuid.UUID, abilityID api.OptUUID, periodStart api.OptDateTime, periodEnd api.OptDateTime) (*api.AbilityMetrics, error) {
	query := `SELECT 
		character_id, ability_id,
		MIN(activated_at) as period_start,
		MAX(activated_at) as period_end,
		COUNT(*) as activations_count,
		COUNT(CASE WHEN result->>'success' = 'true' THEN 1 END) as successful_activations,
		COUNT(CASE WHEN result->>'success' = 'false' THEN 1 END) as failed_activations,
		COUNT(CASE WHEN synergy_triggered = true THEN 1 END) as synergies_triggered,
		AVG((result->>'damage')::float) as average_damage,
		SUM((result->>'damage')::float) as total_damage
		FROM gameplay.ability_effects_history
		WHERE character_id = $1`
	
	args := []interface{}{characterID}
	argIndex := 2

	if abilityID.Set {
		query += ` AND ability_id = $` + string(rune('0'+argIndex))
		args = append(args, abilityID.Value)
		argIndex++
	}

	if periodStart.Set {
		query += ` AND activated_at >= $` + string(rune('0'+argIndex))
		args = append(args, periodStart.Value)
		argIndex++
	}

	if periodEnd.Set {
		query += ` AND activated_at <= $` + string(rune('0'+argIndex))
		args = append(args, periodEnd.Value)
		argIndex++
	}

	query += ` GROUP BY character_id, ability_id`

	var metrics api.AbilityMetrics
	var periodStartStr, periodEndStr sql.NullString
	var activationsCount, successfulActivations, failedActivations, synergiesTriggered sql.NullInt64
	var averageDamage, totalDamage sql.NullFloat64
	var scannedAbilityID uuid.UUID

	err := r.db.QueryRow(ctx, query, args...).Scan(
		&metrics.CharacterID, &scannedAbilityID,
		&periodStartStr, &periodEndStr,
		&activationsCount, &successfulActivations, &failedActivations, &synergiesTriggered,
		&averageDamage, &totalDamage,
	)

	if err != nil {
		// Return default metrics if not found
		metrics.CharacterID = characterID
		if abilityID.Set {
			metrics.AbilityID = abilityID.Value
		}
		metrics.ActivationsCount = api.NewOptInt(0)
		metrics.SuccessfulActivations = api.NewOptInt(0)
		metrics.FailedActivations = api.NewOptInt(0)
		metrics.SynergiesTriggered = api.NewOptInt(0)
		metrics.TotalDamage = api.NewOptFloat32(0.0)
		return &metrics, nil
	}

	metrics.AbilityID = scannedAbilityID

	if periodStartStr.Valid {
		if t, err := time.Parse(time.RFC3339, periodStartStr.String); err == nil {
			metrics.PeriodStart = api.NewOptDateTime(t)
		}
	}

	if periodEndStr.Valid {
		if t, err := time.Parse(time.RFC3339, periodEndStr.String); err == nil {
			metrics.PeriodEnd = api.NewOptDateTime(t)
		}
	}

	if activationsCount.Valid {
		metrics.ActivationsCount = api.NewOptInt(int(activationsCount.Int64))
	}

	if successfulActivations.Valid {
		metrics.SuccessfulActivations = api.NewOptInt(int(successfulActivations.Int64))
	}

	if failedActivations.Valid {
		metrics.FailedActivations = api.NewOptInt(int(failedActivations.Int64))
	}

	if synergiesTriggered.Valid {
		metrics.SynergiesTriggered = api.NewOptInt(int(synergiesTriggered.Int64))
	}

	if averageDamage.Valid {
		metrics.AverageDamage = api.NewOptNilFloat32(float32(averageDamage.Float64))
	}

	if totalDamage.Valid {
		metrics.TotalDamage = api.NewOptFloat32(float32(totalDamage.Float64))
	}

	return &metrics, nil
}

