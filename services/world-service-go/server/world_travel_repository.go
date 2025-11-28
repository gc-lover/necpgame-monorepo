package server

import (
	"context"
	"database/sql"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/necpgame/world-service-go/models"
)

func (r *worldRepository) GetTravelEvent(ctx context.Context, id uuid.UUID) (*models.TravelEvent, error) {
	var event models.TravelEvent
	var skillChecksJSON, rewardsJSON, penaltiesJSON []byte
	
	query := `
		SELECT id, event_code, event_name, event_type, epoch_id, description, base_probability, cooldown_hours, skill_checks, rewards, penalties, created_at, updated_at
		FROM travel_events
		WHERE id = $1
	`
	err := r.db.QueryRowContext(ctx, query, id).Scan(
		&event.ID, &event.EventCode, &event.EventName, &event.EventType, &event.EpochID,
		&event.Description, &event.BaseProbability, &event.CooldownHours,
		&skillChecksJSON, &rewardsJSON, &penaltiesJSON,
		&event.CreatedAt, &event.UpdatedAt)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}
	
	if len(skillChecksJSON) > 0 {
		if err := json.Unmarshal(skillChecksJSON, &event.SkillChecks); err != nil {
			return nil, err
		}
	}
	if len(rewardsJSON) > 0 {
		if err := json.Unmarshal(rewardsJSON, &event.Rewards); err != nil {
			return nil, err
		}
	}
	if len(penaltiesJSON) > 0 {
		if err := json.Unmarshal(penaltiesJSON, &event.Penalties); err != nil {
			return nil, err
		}
	}
	
	return &event, nil
}

func (r *worldRepository) GetTravelEventsByEpoch(ctx context.Context, epochID string) ([]models.TravelEvent, error) {
	query := `
		SELECT id, event_code, event_name, event_type, epoch_id, description, base_probability, cooldown_hours, skill_checks, rewards, penalties, created_at, updated_at
		FROM travel_events
		WHERE epoch_id = $1
	`
	rows, err := r.db.QueryContext(ctx, query, epochID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	
	var events []models.TravelEvent
	for rows.Next() {
		var event models.TravelEvent
		var skillChecksJSON, rewardsJSON, penaltiesJSON []byte
		
		if err := rows.Scan(
			&event.ID, &event.EventCode, &event.EventName, &event.EventType, &event.EpochID,
			&event.Description, &event.BaseProbability, &event.CooldownHours,
			&skillChecksJSON, &rewardsJSON, &penaltiesJSON,
			&event.CreatedAt, &event.UpdatedAt); err != nil {
			continue
		}
		
		if len(skillChecksJSON) > 0 {
			if err := json.Unmarshal(skillChecksJSON, &event.SkillChecks); err != nil {
				continue
			}
		}
		if len(rewardsJSON) > 0 {
			if err := json.Unmarshal(rewardsJSON, &event.Rewards); err != nil {
				continue
			}
		}
		if len(penaltiesJSON) > 0 {
			if err := json.Unmarshal(penaltiesJSON, &event.Penalties); err != nil {
				continue
			}
		}
		
		events = append(events, event)
	}
	
	return events, nil
}

func (r *worldRepository) GetAvailableTravelEvents(ctx context.Context, zoneID uuid.UUID, epochID *string) ([]models.TravelEvent, error) {
	query := `
		SELECT te.id, te.event_code, te.event_name, te.event_type, te.epoch_id, te.description, te.base_probability, te.cooldown_hours, te.skill_checks, te.rewards, te.penalties, te.created_at, te.updated_at
		FROM travel_events te
		INNER JOIN travel_event_zones tez ON te.id = tez.event_id
		WHERE tez.zone_id = $1
	`
	args := []interface{}{zoneID}
	
	if epochID != nil {
		query += ` AND te.epoch_id = $2`
		args = append(args, *epochID)
	}
	
	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	
	var events []models.TravelEvent
	for rows.Next() {
		var event models.TravelEvent
		var skillChecksJSON, rewardsJSON, penaltiesJSON []byte
		
		if err := rows.Scan(
			&event.ID, &event.EventCode, &event.EventName, &event.EventType, &event.EpochID,
			&event.Description, &event.BaseProbability, &event.CooldownHours,
			&skillChecksJSON, &rewardsJSON, &penaltiesJSON,
			&event.CreatedAt, &event.UpdatedAt); err != nil {
			continue
		}
		
		if len(skillChecksJSON) > 0 {
			if err := json.Unmarshal(skillChecksJSON, &event.SkillChecks); err != nil {
				continue
			}
		}
		if len(rewardsJSON) > 0 {
			if err := json.Unmarshal(rewardsJSON, &event.Rewards); err != nil {
				continue
			}
		}
		if len(penaltiesJSON) > 0 {
			if err := json.Unmarshal(penaltiesJSON, &event.Penalties); err != nil {
				continue
			}
		}
		
		events = append(events, event)
	}
	
	return events, nil
}

func (r *worldRepository) CreateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error {
	query := `
		INSERT INTO travel_event_instances (id, event_id, character_id, zone_id, epoch_id, state, started_at, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`
	_, err := r.db.ExecContext(ctx, query,
		instance.ID, instance.EventID, instance.CharacterID, instance.ZoneID,
		instance.EpochID, instance.State, instance.StartedAt, instance.CreatedAt, instance.UpdatedAt)
	return err
}

func (r *worldRepository) GetTravelEventInstance(ctx context.Context, id uuid.UUID) (*models.TravelEventInstance, error) {
	var instance models.TravelEventInstance
	query := `
		SELECT id, event_id, character_id, zone_id, epoch_id, state, started_at, completed_at, created_at, updated_at
		FROM travel_event_instances
		WHERE id = $1
	`
	err := r.db.GetContext(ctx, &instance, query, id)
	if err == sql.ErrNoRows {
		return nil, nil
	}
	return &instance, err
}

func (r *worldRepository) UpdateTravelEventInstance(ctx context.Context, instance *models.TravelEventInstance) error {
	query := `
		UPDATE travel_event_instances
		SET state = $2, completed_at = $3, updated_at = $4
		WHERE id = $1
	`
	_, err := r.db.ExecContext(ctx, query, instance.ID, instance.State, instance.CompletedAt, instance.UpdatedAt)
	return err
}

func (r *worldRepository) GetTravelEventInstancesByCharacterAndEvent(ctx context.Context, characterID, eventID uuid.UUID) ([]models.TravelEventInstance, error) {
	var instances []models.TravelEventInstance
	query := `
		SELECT id, event_id, character_id, zone_id, epoch_id, state, started_at, completed_at, created_at, updated_at
		FROM travel_event_instances
		WHERE character_id = $1 AND event_id = $2
		ORDER BY created_at DESC
	`
	err := r.db.SelectContext(ctx, &instances, query, characterID, eventID)
	return instances, err
}

func (r *worldRepository) GetTravelEventInstancesByEvent(ctx context.Context, eventID uuid.UUID) ([]models.TravelEventInstance, error) {
	var instances []models.TravelEventInstance
	query := `
		SELECT id, event_id, character_id, zone_id, epoch_id, state, started_at, completed_at, created_at, updated_at
		FROM travel_event_instances
		WHERE event_id = $1
		ORDER BY created_at DESC
	`
	err := r.db.SelectContext(ctx, &instances, query, eventID)
	return instances, err
}

func (r *worldRepository) GetCharacterTravelEventCooldowns(ctx context.Context, characterID uuid.UUID) ([]models.TravelEventCooldown, error) {
	var cooldowns []models.TravelEventCooldown
	query := `
		SELECT te.event_type, tei.started_at as last_triggered_at,
			(tei.started_at + (te.cooldown_hours || ' hours')::interval) as cooldown_until
		FROM travel_event_instances tei
		INNER JOIN travel_events te ON tei.event_id = te.id
		WHERE tei.character_id = $1 AND tei.state != 'cancelled'
		ORDER BY tei.started_at DESC
	`
	err := r.db.SelectContext(ctx, &cooldowns, query, characterID)
	return cooldowns, err
}

