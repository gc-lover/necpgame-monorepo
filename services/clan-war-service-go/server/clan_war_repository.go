package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/clan-war-service-go/models"
	"github.com/sirupsen/logrus"
)

type ClanWarRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewClanWarRepository(db *pgxpool.Pool, logger *logrus.Logger) *ClanWarRepository {
	return &ClanWarRepository{
		db:     db,
		logger: logger,
	}
}

func (r *ClanWarRepository) CreateWar(ctx context.Context, war *models.ClanWar) error {
	alliesJSON, err := json.Marshal(war.Allies)
	if err != nil {
		return fmt.Errorf("failed to marshal allies: %w", err)
	}

	query := `
		INSERT INTO pvp.clan_wars (
			id, attacker_guild_id, defender_guild_id, allies, status, phase,
			territory_id, attacker_score, defender_score, winner_guild_id,
			start_time, end_time, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14)
	`

	_, err = r.db.Exec(ctx, query,
		war.ID, war.AttackerGuildID, war.DefenderGuildID, alliesJSON, war.Status, war.Phase,
		war.TerritoryID, war.AttackerScore, war.DefenderScore, war.WinnerGuildID,
		war.StartTime, war.EndTime, war.CreatedAt, war.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create war: %w", err)
	}

	return nil
}

func (r *ClanWarRepository) GetWarByID(ctx context.Context, warID uuid.UUID) (*models.ClanWar, error) {
	query := `
		SELECT id, attacker_guild_id, defender_guild_id, allies, status, phase,
			territory_id, attacker_score, defender_score, winner_guild_id,
			start_time, end_time, created_at, updated_at
		FROM pvp.clan_wars
		WHERE id = $1
	`

	var war models.ClanWar
	var alliesJSON []byte
	var territoryID, winnerGuildID sql.NullString
	var endTime sql.NullTime

	err := r.db.QueryRow(ctx, query, warID).Scan(
		&war.ID, &war.AttackerGuildID, &war.DefenderGuildID, &alliesJSON, &war.Status, &war.Phase,
		&territoryID, &war.AttackerScore, &war.DefenderScore, &winnerGuildID,
		&war.StartTime, &endTime, &war.CreatedAt, &war.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get war: %w", err)
	}

	if err := json.Unmarshal(alliesJSON, &war.Allies); err != nil {
		return nil, fmt.Errorf("failed to unmarshal allies: %w", err)
	}

	if territoryID.Valid {
		id, _ := uuid.Parse(territoryID.String)
		war.TerritoryID = &id
	}

	if winnerGuildID.Valid {
		id, _ := uuid.Parse(winnerGuildID.String)
		war.WinnerGuildID = &id
	}

	if endTime.Valid {
		war.EndTime = &endTime.Time
	}

	return &war, nil
}

func (r *ClanWarRepository) ListWars(ctx context.Context, guildID *uuid.UUID, status *models.WarStatus, limit, offset int) ([]models.ClanWar, int, error) {
	query := `
		SELECT id, attacker_guild_id, defender_guild_id, allies, status, phase,
			territory_id, attacker_score, defender_score, winner_guild_id,
			start_time, end_time, created_at, updated_at
		FROM pvp.clan_wars
		WHERE 1=1
	`
	args := []interface{}{}
	argIndex := 1

	if guildID != nil {
		query += fmt.Sprintf(" AND (attacker_guild_id = $%d OR defender_guild_id = $%d)", argIndex, argIndex)
		args = append(args, *guildID)
		argIndex++
	}

	if status != nil {
		query += fmt.Sprintf(" AND status = $%d", argIndex)
		args = append(args, *status)
		argIndex++
	}

	query += " ORDER BY created_at DESC"

	countQuery := "SELECT COUNT(*) FROM (" + query + ") AS count_query"
	var total int
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count wars: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list wars: %w", err)
	}
	defer rows.Close()

	var wars []models.ClanWar
	for rows.Next() {
		var war models.ClanWar
		var alliesJSON []byte
		var territoryID, winnerGuildID sql.NullString
		var endTime sql.NullTime

		err := rows.Scan(
			&war.ID, &war.AttackerGuildID, &war.DefenderGuildID, &alliesJSON, &war.Status, &war.Phase,
			&territoryID, &war.AttackerScore, &war.DefenderScore, &winnerGuildID,
			&war.StartTime, &endTime, &war.CreatedAt, &war.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan war: %w", err)
		}

		if err := json.Unmarshal(alliesJSON, &war.Allies); err != nil {
			return nil, 0, fmt.Errorf("failed to unmarshal allies: %w", err)
		}

		if territoryID.Valid {
			id, _ := uuid.Parse(territoryID.String)
			war.TerritoryID = &id
		}

		if winnerGuildID.Valid {
			id, _ := uuid.Parse(winnerGuildID.String)
			war.WinnerGuildID = &id
		}

		if endTime.Valid {
			war.EndTime = &endTime.Time
		}

		wars = append(wars, war)
	}

	return wars, total, nil
}

func (r *ClanWarRepository) UpdateWar(ctx context.Context, war *models.ClanWar) error {
	query := `
		UPDATE pvp.clan_wars
		SET status = $2, phase = $3, attacker_score = $4, defender_score = $5,
			winner_guild_id = $6, end_time = $7, updated_at = $8
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		war.ID, war.Status, war.Phase, war.AttackerScore, war.DefenderScore,
		war.WinnerGuildID, war.EndTime, war.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to update war: %w", err)
	}

	return nil
}

func (r *ClanWarRepository) CreateBattle(ctx context.Context, battle *models.WarBattle) error {
	query := `
		INSERT INTO pvp.war_battles (
			id, war_id, type, territory_id, status, attacker_score, defender_score,
			start_time, end_time, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.Exec(ctx, query,
		battle.ID, battle.WarID, battle.Type, battle.TerritoryID, battle.Status,
		battle.AttackerScore, battle.DefenderScore, battle.StartTime, battle.EndTime,
		battle.CreatedAt, battle.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create battle: %w", err)
	}

	return nil
}

func (r *ClanWarRepository) GetBattleByID(ctx context.Context, battleID uuid.UUID) (*models.WarBattle, error) {
	query := `
		SELECT id, war_id, type, territory_id, status, attacker_score, defender_score,
			start_time, end_time, created_at, updated_at
		FROM pvp.war_battles
		WHERE id = $1
	`

	var battle models.WarBattle
	var territoryID sql.NullString
	var endTime sql.NullTime

	err := r.db.QueryRow(ctx, query, battleID).Scan(
		&battle.ID, &battle.WarID, &battle.Type, &territoryID, &battle.Status,
		&battle.AttackerScore, &battle.DefenderScore, &battle.StartTime, &endTime,
		&battle.CreatedAt, &battle.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get battle: %w", err)
	}

	if territoryID.Valid {
		id, _ := uuid.Parse(territoryID.String)
		battle.TerritoryID = &id
	}

	if endTime.Valid {
		battle.EndTime = &endTime.Time
	}

	return &battle, nil
}

func (r *ClanWarRepository) ListBattles(ctx context.Context, warID *uuid.UUID, status *models.BattleStatus, limit, offset int) ([]models.WarBattle, int, error) {
	query := `
		SELECT id, war_id, type, territory_id, status, attacker_score, defender_score,
			start_time, end_time, created_at, updated_at
		FROM pvp.war_battles
		WHERE 1=1
	`
	args := []interface{}{}
	argIndex := 1

	if warID != nil {
		query += fmt.Sprintf(" AND war_id = $%d", argIndex)
		args = append(args, *warID)
		argIndex++
	}

	if status != nil {
		query += fmt.Sprintf(" AND status = $%d", argIndex)
		args = append(args, *status)
		argIndex++
	}

	query += " ORDER BY start_time DESC"

	countQuery := "SELECT COUNT(*) FROM (" + query + ") AS count_query"
	var total int
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count battles: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list battles: %w", err)
	}
	defer rows.Close()

	var battles []models.WarBattle
	for rows.Next() {
		var battle models.WarBattle
		var territoryID sql.NullString
		var endTime sql.NullTime

		err := rows.Scan(
			&battle.ID, &battle.WarID, &battle.Type, &territoryID, &battle.Status,
			&battle.AttackerScore, &battle.DefenderScore, &battle.StartTime, &endTime,
			&battle.CreatedAt, &battle.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan battle: %w", err)
		}

		if territoryID.Valid {
			id, _ := uuid.Parse(territoryID.String)
			battle.TerritoryID = &id
		}

		if endTime.Valid {
			battle.EndTime = &endTime.Time
		}

		battles = append(battles, battle)
	}

	return battles, total, nil
}

func (r *ClanWarRepository) UpdateBattle(ctx context.Context, battle *models.WarBattle) error {
	query := `
		UPDATE pvp.war_battles
		SET status = $2, attacker_score = $3, defender_score = $4, end_time = $5, updated_at = $6
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query,
		battle.ID, battle.Status, battle.AttackerScore, battle.DefenderScore,
		battle.EndTime, battle.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to update battle: %w", err)
	}

	return nil
}

func (r *ClanWarRepository) GetTerritoryByID(ctx context.Context, territoryID uuid.UUID) (*models.Territory, error) {
	query := `
		SELECT id, name, region, owner_guild_id, resources, defense_level, siege_difficulty,
			created_at, updated_at
		FROM pvp.territories
		WHERE id = $1
	`

	var territory models.Territory
	var resourcesJSON []byte
	var ownerGuildID sql.NullString

	err := r.db.QueryRow(ctx, query, territoryID).Scan(
		&territory.ID, &territory.Name, &territory.Region, &ownerGuildID, &resourcesJSON,
		&territory.DefenseLevel, &territory.SiegeDifficulty, &territory.CreatedAt, &territory.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get territory: %w", err)
	}

	if err := json.Unmarshal(resourcesJSON, &territory.Resources); err != nil {
		territory.Resources = make(map[string]interface{})
	}

	if ownerGuildID.Valid {
		id, _ := uuid.Parse(ownerGuildID.String)
		territory.OwnerGuildID = &id
	}

	return &territory, nil
}

func (r *ClanWarRepository) ListTerritories(ctx context.Context, ownerGuildID *uuid.UUID, limit, offset int) ([]models.Territory, int, error) {
	query := `
		SELECT id, name, region, owner_guild_id, resources, defense_level, siege_difficulty,
			created_at, updated_at
		FROM pvp.territories
		WHERE 1=1
	`
	args := []interface{}{}
	argIndex := 1

	if ownerGuildID != nil {
		query += fmt.Sprintf(" AND owner_guild_id = $%d", argIndex)
		args = append(args, *ownerGuildID)
		argIndex++
	}

	query += " ORDER BY name"

	countQuery := "SELECT COUNT(*) FROM (" + query + ") AS count_query"
	var total int
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count territories: %w", err)
	}

	query += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to list territories: %w", err)
	}
	defer rows.Close()

	var territories []models.Territory
	for rows.Next() {
		var territory models.Territory
		var resourcesJSON []byte
		var ownerGuildID sql.NullString

		err := rows.Scan(
			&territory.ID, &territory.Name, &territory.Region, &ownerGuildID, &resourcesJSON,
			&territory.DefenseLevel, &territory.SiegeDifficulty, &territory.CreatedAt, &territory.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan territory: %w", err)
		}

		if err := json.Unmarshal(resourcesJSON, &territory.Resources); err != nil {
			territory.Resources = make(map[string]interface{})
		}

		if ownerGuildID.Valid {
			id, _ := uuid.Parse(ownerGuildID.String)
			territory.OwnerGuildID = &id
		}

		territories = append(territories, territory)
	}

	return territories, total, nil
}

func (r *ClanWarRepository) UpdateTerritoryOwner(ctx context.Context, territoryID, ownerGuildID uuid.UUID) error {
	query := `
		UPDATE pvp.territories
		SET owner_guild_id = $2, updated_at = $3
		WHERE id = $1
	`

	_, err := r.db.Exec(ctx, query, territoryID, ownerGuildID, time.Now())
	if err != nil {
		return fmt.Errorf("failed to update territory owner: %w", err)
	}

	return nil
}

