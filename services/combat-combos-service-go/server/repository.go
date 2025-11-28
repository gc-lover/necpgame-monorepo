package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/combat-ai-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type Repository struct {
	db *sql.DB
}

func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) GetAIProfiles(ctx context.Context, tier *string, faction *string, limit, offset int) ([]api.AIProfile, int, error) {
	query := `
		SELECT id, name
		FROM ai_profiles
		WHERE 1=1
	`
	args := []interface{}{}
	argPos := 1

	if tier != nil {
		query += fmt.Sprintf(" AND tier = $%d", argPos)
		args = append(args, *tier)
		argPos++
	}

	if faction != nil {
		query += fmt.Sprintf(" AND faction = $%d", argPos)
		args = append(args, *faction)
		argPos++
	}

	countQuery := fmt.Sprintf("SELECT COUNT(*) FROM (%s) AS count_query", query)
	var total int
	err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count profiles: %w", err)
	}

	query += fmt.Sprintf(" ORDER BY name ASC LIMIT $%d OFFSET $%d", argPos, argPos+1)
	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query profiles: %w", err)
	}
	defer rows.Close()

	profiles := []api.AIProfile{}
	for rows.Next() {
		var profile api.AIProfile

		err := rows.Scan(
			&profile.Id,
			&profile.Name,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan profile: %w", err)
		}

		profiles = append(profiles, profile)
	}

	return profiles, total, nil
}

func (r *Repository) GetAIProfileByID(ctx context.Context, id string) (*api.AIProfileDetailed, error) {
	query := `
		SELECT id, name, tier, faction, level_range_min, level_range_max,
		       base_health, base_armor, base_damage, behavior_tree,
		       abilities
		FROM ai_profiles
		WHERE id = $1
	`

	var profile api.AIProfileDetailed
	var abilitiesJSON []byte
	var behaviorTreeJSON []byte
	var tier, faction string
	var levelMin, levelMax, health, armor, damage int

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&profile.Id,
		&profile.Name,
		&tier,
		&faction,
		&levelMin,
		&levelMax,
		&health,
		&armor,
		&damage,
		&behaviorTreeJSON,
		&abilitiesJSON,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get profile: %w", err)
	}

	profile.SkillLevelMin = &levelMin
	profile.SkillLevelMax = &levelMax

	var behaviorTree map[string]interface{}
	if err := json.Unmarshal(behaviorTreeJSON, &behaviorTree); err != nil {
		return nil, fmt.Errorf("failed to unmarshal behavior tree: %w", err)
	}
	profile.BehaviorTree = &behaviorTree

	var abilities []map[string]interface{}
	if err := json.Unmarshal(abilitiesJSON, &abilities); err != nil {
		return nil, fmt.Errorf("failed to unmarshal abilities: %w", err)
	}
	profile.Abilities = &abilities

	return &profile, nil
}

func (r *Repository) CreateEncounter(ctx context.Context, req api.CreateEncounterRequest) (*api.Encounter, error) {
	id := uuid.New()
	
	enemiesJSON, err := json.Marshal(req.Enemies)
	if err != nil {
		return nil, fmt.Errorf("failed to marshal enemies: %w", err)
	}

	query := `
		INSERT INTO encounters (id, enemy_profile_ids, location, status)
		VALUES ($1, $2, $3, $4)
		RETURNING id, enemy_profile_ids, location, status, created_at
	`

	var encounter api.Encounter
	var enemyIDsJSONResult []byte
	var location string
	var status string
	var createdAt time.Time

	err = r.db.QueryRowContext(
		ctx, query,
		id, enemiesJSON, req.AreaId, "pending",
	).Scan(
		&encounter.Id,
		&enemyIDsJSONResult,
		&location,
		&status,
		&createdAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to create encounter: %w", err)
	}

	encounter.AreaId = &location
	encounterStatus := api.EncounterStatus(status)
	encounter.Status = &encounterStatus
	encounter.CreatedAt = &createdAt

	return &encounter, nil
}

func (r *Repository) GetEncounter(ctx context.Context, id string) (*api.Encounter, error) {
	query := `
		SELECT id, enemy_profile_ids, location, status, created_at
		FROM encounters
		WHERE id = $1
	`

	var encounter api.Encounter
	var enemyIDsJSON []byte
	var location string
	var status string
	var createdAt time.Time
	var idUUID uuid.UUID

	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&idUUID,
		&enemyIDsJSON,
		&location,
		&status,
		&createdAt,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get encounter: %w", err)
	}

	idOpenAPI := openapi_types.UUID(idUUID)
	encounter.Id = &idOpenAPI
	encounter.AreaId = &location
	encounterStatus := api.EncounterStatus(status)
	encounter.Status = &encounterStatus
	encounter.CreatedAt = &createdAt

	var enemies []map[string]interface{}
	if err := json.Unmarshal(enemyIDsJSON, &enemies); err != nil {
		return nil, fmt.Errorf("failed to unmarshal enemies: %w", err)
	}
	encounter.Enemies = &enemies

	return &encounter, nil
}

func (r *Repository) UpdateEncounterStatus(ctx context.Context, id string, status string) error {
	query := `
		UPDATE encounters
		SET status = $1, updated_at = $2
		WHERE id = $3
	`

	result, err := r.db.ExecContext(ctx, query, status, time.Now(), id)
	if err != nil {
		return fmt.Errorf("failed to update encounter status: %w", err)
	}

	rows, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rows == 0 {
		return fmt.Errorf("encounter not found")
	}

	return nil
}

func (r *Repository) CreateRaidPhase(ctx context.Context, raidID string, phase api.RaidPhaseRequest) error {
	query := `
		UPDATE raid_phases
		SET phase_number = $1
		WHERE raid_id = $2
	`

	_, err := r.db.ExecContext(
		ctx, query,
		phase.NextPhase, raidID,
	)
	if err != nil {
		return fmt.Errorf("failed to update raid phase: %w", err)
	}

	return nil
}

func (r *Repository) GetRaidPhases(ctx context.Context, raidID string) ([]api.RaidPhase, error) {
	query := `
		SELECT raid_id, phase_number, phase_name, mechanics
		FROM raid_phases
		WHERE raid_id = $1
		ORDER BY phase_number ASC
	`

	rows, err := r.db.QueryContext(ctx, query, raidID)
	if err != nil {
		return nil, fmt.Errorf("failed to query raid phases: %w", err)
	}
	defer rows.Close()

	phases := []api.RaidPhase{}
	for rows.Next() {
		var phase api.RaidPhase
		var mechanicsJSON []byte
		var raidIDUUID uuid.UUID
		var phaseNumber int
		var phaseName string

		err := rows.Scan(
			&raidIDUUID,
			&phaseNumber,
			&phaseName,
			&mechanicsJSON,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan raid phase: %w", err)
		}

		raidIDOpenAPI := openapi_types.UUID(raidIDUUID)
		phase.RaidId = &raidIDOpenAPI
		phase.CurrentPhase = &phaseNumber
		phase.PhaseName = &phaseName

		var mechanics []string
		if err := json.Unmarshal(mechanicsJSON, &mechanics); err != nil {
			return nil, fmt.Errorf("failed to unmarshal mechanics: %w", err)
		}
		phase.Mechanics = &mechanics

		phases = append(phases, phase)
	}

	return phases, nil
}

func (r *Repository) GetProfileTelemetry(ctx context.Context, profileID string) (*api.AIProfileTelemetry, error) {
	query := `
		SELECT 
			profile_id,
			COUNT(*) as encounters_count,
			AVG(ttk_seconds) as avg_time_to_defeat,
			AVG(damage_dealt) as damage_dealt_avg,
			AVG(damage_taken) as damage_taken_avg
		FROM ai_telemetry
		WHERE profile_id = $1
		GROUP BY profile_id
	`

	var telemetry api.AIProfileTelemetry
	var encountersCount int
	var avgTTK, damageDealtAvg, damageTakenAvg float32

	err := r.db.QueryRowContext(ctx, query, profileID).Scan(
		&telemetry.ProfileId,
		&encountersCount,
		&avgTTK,
		&damageDealtAvg,
		&damageTakenAvg,
	)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get telemetry: %w", err)
	}

	telemetry.EncountersCount = &encountersCount
	telemetry.AvgTimeToDefeat = &avgTTK
	damageDealtInt := int(damageDealtAvg)
	damageTakenInt := int(damageTakenAvg)
	telemetry.DamageDealtAvg = &damageDealtInt
	telemetry.DamageTakenAvg = &damageTakenInt

	return &telemetry, nil
}

