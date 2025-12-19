// Issue: #1856
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for guild territory service
type Repository struct {
	db *pgxpool.Pool
}

// Territory internal model
type Territory struct {
	ID           uuid.UUID
	Name         string
	Type         string // commercial, industrial, residential, military
	ControlType  string // contested, owned, neutral
	OwnerGuildID *uuid.UUID
	Location     string
	Bonuses      string // JSON bonuses
	CreatedAt    time.Time
	UpdatedAt    time.Time
}

// TerritoryClaim internal model
type TerritoryClaim struct {
	ID          uuid.UUID
	TerritoryID uuid.UUID
	GuildID     uuid.UUID
	Status      string // pending, active, defeated
	StartedAt   time.Time
	EndedAt     *time.Time
}

// TerritoryWar internal model
type TerritoryWar struct {
	ID            uuid.UUID
	TerritoryID   uuid.UUID
	AttackerID    uuid.UUID
	DefenderID    uuid.UUID
	Status        string // active, completed, cancelled
	WinnerID      *uuid.UUID
	AttackerScore int
	DefenderScore int
	StartedAt     time.Time
	EndedAt       *time.Time
}

// NewRepository creates new repository with database connection
func NewRepository(connStr string) (*Repository, error) {
	// DB connection pool configuration (optimization)
	config, err := pgxpool.ParseConfig(connStr)
	if err != nil {
		return nil, err
	}

	// Connection pool settings for performance (OPTIMIZATION: Issue #1856)
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	return &Repository{db: db}, nil
}

// Close closes database connection
func (r *Repository) Close() {
	r.db.Close()
}

// GetTerritories retrieves territories with pagination
func (r *Repository) GetTerritories(ctx context.Context, controlType *string, limit *int) ([]*Territory, error) {
	query := `
		SELECT id, name, type, control_type, owner_guild_id, location, bonuses, created_at, updated_at
		FROM world.territories
		WHERE ($1::text IS NULL OR control_type = $1)
		ORDER BY created_at DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, controlType, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var territories []*Territory
	for rows.Next() {
		var t Territory
		var ownerGuildID sql.NullString

		err := rows.Scan(&t.ID, &t.Name, &t.Type, &t.ControlType, &ownerGuildID, &t.Location, &t.Bonuses, &t.CreatedAt, &t.UpdatedAt)
		if err != nil {
			return nil, err
		}

		if ownerGuildID.Valid {
			if id, err := uuid.Parse(ownerGuildID.String); err == nil {
				t.OwnerGuildID = &id
			}
		}

		territories = append(territories, &t)
	}

	return territories, rows.Err()
}

// GetTerritoryByID retrieves a territory by ID
func (r *Repository) GetTerritoryByID(ctx context.Context, id uuid.UUID) (*Territory, error) {
	query := `
		SELECT id, name, type, control_type, owner_guild_id, location, bonuses, created_at, updated_at
		FROM world.territories
		WHERE id = $1
	`

	var t Territory
	var ownerGuildID sql.NullString

	err := r.db.QueryRow(ctx, query, id).Scan(
		&t.ID, &t.Name, &t.Type, &t.ControlType, &ownerGuildID, &t.Location, &t.Bonuses, &t.CreatedAt, &t.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrTerritoryNotFound
		}
		return nil, err
	}

	if ownerGuildID.Valid {
		if id, err := uuid.Parse(ownerGuildID.String); err == nil {
			t.OwnerGuildID = &id
		}
	}

	return &t, nil
}

// ClaimTerritory initiates territory claim
func (r *Repository) ClaimTerritory(ctx context.Context, territoryID, guildID uuid.UUID) (*TerritoryClaim, error) {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return nil, err
	}
	defer tx.Rollback(ctx)

	// Create claim
	claimQuery := `
		INSERT INTO world.territory_claims (id, territory_id, guild_id, status, started_at)
		VALUES (gen_random_uuid(), $1, $2, 'pending', NOW())
		RETURNING id, territory_id, guild_id, status, started_at, ended_at
	`

	var claim TerritoryClaim
	err = tx.QueryRow(ctx, claimQuery, territoryID, guildID).Scan(
		&claim.ID, &claim.TerritoryID, &claim.GuildID, &claim.Status, &claim.StartedAt, &claim.EndedAt,
	)
	if err != nil {
		return nil, err
	}

	// Update territory control type
	updateQuery := `
		UPDATE world.territories
		SET control_type = 'contested', updated_at = NOW()
		WHERE id = $1
	`
	_, err = tx.Exec(ctx, updateQuery, territoryID)
	if err != nil {
		return nil, err
	}

	return &claim, tx.Commit(ctx)
}

// GetTerritoryClaims retrieves claims for a territory
func (r *Repository) GetTerritoryClaims(ctx context.Context, territoryID uuid.UUID) ([]*TerritoryClaim, error) {
	query := `
		SELECT id, territory_id, guild_id, status, started_at, ended_at
		FROM world.territory_claims
		WHERE territory_id = $1
		ORDER BY started_at DESC
	`

	rows, err := r.db.Query(ctx, query, territoryID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var claims []*TerritoryClaim
	for rows.Next() {
		var c TerritoryClaim
		var endedAt sql.NullTime

		err := rows.Scan(&c.ID, &c.TerritoryID, &c.GuildID, &c.Status, &c.StartedAt, &endedAt)
		if err != nil {
			return nil, err
		}

		if endedAt.Valid {
			c.EndedAt = &endedAt.Time
		}

		claims = append(claims, &c)
	}

	return claims, rows.Err()
}

// GetGuildTerritories retrieves territories owned by a guild
func (r *Repository) GetGuildTerritories(ctx context.Context, guildID uuid.UUID) ([]*Territory, error) {
	query := `
		SELECT id, name, type, control_type, owner_guild_id, location, bonuses, created_at, updated_at
		FROM world.territories
		WHERE owner_guild_id = $1
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, guildID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var territories []*Territory
	for rows.Next() {
		var t Territory
		var ownerGuildID sql.NullString

		err := rows.Scan(&t.ID, &t.Name, &t.Type, &t.ControlType, &ownerGuildID, &t.Location, &t.Bonuses, &t.CreatedAt, &t.UpdatedAt)
		if err != nil {
			return nil, err
		}

		if ownerGuildID.Valid {
			if id, err := uuid.Parse(ownerGuildID.String); err == nil {
				t.OwnerGuildID = &id
			}
		}

		territories = append(territories, &t)
	}

	return territories, rows.Err()
}

// CalculateTerritoryBonuses calculates bonuses for a territory
func (r *Repository) CalculateTerritoryBonuses(ctx context.Context, territoryID uuid.UUID) (map[string]interface{}, error) {
	// This would calculate bonuses based on territory type and ownership duration
	// For now, return mock bonuses
	bonuses := map[string]interface{}{
		"economic_boost": 0.15,
		"defense_bonus":  0.10,
		"tax_revenue":    500,
	}
	return bonuses, nil
}

// UpdateTerritoryOwner updates territory ownership
func (r *Repository) UpdateTerritoryOwner(ctx context.Context, territoryID, newOwnerID uuid.UUID) error {
	query := `
		UPDATE world.territories
		SET owner_guild_id = $2, control_type = 'owned', updated_at = NOW()
		WHERE id = $1
	`
	_, err := r.db.Exec(ctx, query, territoryID, newOwnerID)
	return err
}

// GetTerritoryWars retrieves wars for a territory
func (r *Repository) GetTerritoryWars(ctx context.Context, territoryID uuid.UUID) ([]*TerritoryWar, error) {
	query := `
		SELECT id, territory_id, attacker_id, defender_id, status, winner_id, attacker_score, defender_score, started_at, ended_at
		FROM world.territory_wars
		WHERE territory_id = $1
		ORDER BY started_at DESC
	`

	rows, err := r.db.Query(ctx, query, territoryID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var wars []*TerritoryWar
	for rows.Next() {
		var w TerritoryWar
		var winnerID sql.NullString
		var endedAt sql.NullTime

		err := rows.Scan(&w.ID, &w.TerritoryID, &w.AttackerID, &w.DefenderID, &w.Status, &winnerID, &w.AttackerScore, &w.DefenderScore, &w.StartedAt, &endedAt)
		if err != nil {
			return nil, err
		}

		if winnerID.Valid {
			if id, err := uuid.Parse(winnerID.String); err == nil {
				w.WinnerID = &id
			}
		}
		if endedAt.Valid {
			w.EndedAt = &endedAt.Time
		}

		wars = append(wars, &w)
	}

	return wars, rows.Err()
}
