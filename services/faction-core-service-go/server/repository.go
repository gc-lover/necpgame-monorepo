// Issue: #1442
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/faction-core-service-go/pkg/api"
	"github.com/google/uuid"
)

type Repository struct {
	db *sql.DB
}

func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) CreateFaction(ctx context.Context, req api.CreateFactionRequest) (*api.Faction, error) {
	id := uuid.New().String()
	now := time.Now()

	query := `
		INSERT INTO factions (id, name, type, ideology, description, leader_clan_id, status, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, 'active', $7, $8)
		RETURNING id, name, type, ideology, description, leader_clan_id, status, created_at, updated_at
	`

	var faction api.Faction
	err := r.db.QueryRowContext(ctx, query,
		id, req.Name, req.Type, req.Ideology, req.Description, req.LeaderClanId, now, now,
	).Scan(
		&faction.Id, &faction.Name, &faction.Type, &faction.Ideology, &faction.Description,
		&faction.LeaderClanId, &faction.Status, &faction.CreatedAt, &faction.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	return &faction, nil
}

func (r *Repository) GetFactionByID(ctx context.Context, factionId string) (*api.Faction, error) {
	query := `
		SELECT id, name, type, ideology, description, leader_clan_id, status, created_at, updated_at
		FROM factions
		WHERE id = $1
	`

	var faction api.Faction
	err := r.db.QueryRowContext(ctx, query, factionId).Scan(
		&faction.Id, &faction.Name, &faction.Type, &faction.Ideology, &faction.Description,
		&faction.LeaderClanId, &faction.Status, &faction.CreatedAt, &faction.UpdatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &faction, nil
}

func (r *Repository) UpdateFaction(ctx context.Context, factionId string, req api.UpdateFactionRequest) (*api.Faction, error) {
	now := time.Now()

	query := `
		UPDATE factions
		SET name = COALESCE($1, name),
		    ideology = COALESCE($2, ideology),
		    description = COALESCE($3, description),
		    status = COALESCE($4, status),
		    updated_at = $5
		WHERE id = $6
		RETURNING id, name, type, ideology, description, leader_clan_id, status, created_at, updated_at
	`

	var faction api.Faction
	err := r.db.QueryRowContext(ctx, query,
		req.Name, req.Ideology, req.Description, req.Status, now, factionId,
	).Scan(
		&faction.Id, &faction.Name, &faction.Type, &faction.Ideology, &faction.Description,
		&faction.LeaderClanId, &faction.Status, &faction.CreatedAt, &faction.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	return &faction, nil
}

func (r *Repository) DeleteFaction(ctx context.Context, factionId string) error {
	query := `UPDATE factions SET status = 'disbanded', updated_at = $1 WHERE id = $2`
	_, err := r.db.ExecContext(ctx, query, time.Now(), factionId)
	return err
}

func (r *Repository) ListFactions(ctx context.Context, params api.ListFactionsParams) ([]api.Faction, int, error) {
	baseQuery := `SELECT id, name, type, ideology, description, leader_clan_id, status, created_at, updated_at FROM factions WHERE 1=1`
	countQuery := `SELECT COUNT(*) FROM factions WHERE 1=1`

	args := []interface{}{}
	argIndex := 1

	// Apply filters
	if params.Type != nil {
		baseQuery += ` AND type = $` + string(rune(argIndex))
		countQuery += ` AND type = $` + string(rune(argIndex))
		args = append(args, *params.Type)
		argIndex++
	}

	if params.Status != nil {
		baseQuery += ` AND status = $` + string(rune(argIndex))
		countQuery += ` AND status = $` + string(rune(argIndex))
		args = append(args, *params.Status)
		argIndex++
	}

	// Get total count
	var total int
	if err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total); err != nil {
		return nil, 0, err
	}

	// Apply pagination
	page := 1
	if params.Page != nil {
		page = *params.Page
	}

	limit := 10
	if params.Limit != nil {
		limit = *params.Limit
	}

	offset := (page - 1) * limit
	baseQuery += ` ORDER BY created_at DESC LIMIT $` + string(rune(argIndex)) + ` OFFSET $` + string(rune(argIndex+1))
	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, baseQuery, args...)
	if err != nil {
		return nil, 0, err
	}
	defer rows.Close()

	var factions []api.Faction
	for rows.Next() {
		var faction api.Faction
		if err := rows.Scan(
			&faction.Id, &faction.Name, &faction.Type, &faction.Ideology, &faction.Description,
			&faction.LeaderClanId, &faction.Status, &faction.CreatedAt, &faction.UpdatedAt,
		); err != nil {
			return nil, 0, err
		}
		factions = append(factions, faction)
	}

	return factions, total, nil
}

func (r *Repository) UpdateHierarchy(ctx context.Context, factionId string, req api.UpdateHierarchyRequest) error {
	// Implementation of hierarchy updates
	// This would update faction_hierarchy table
	return nil
}

func (r *Repository) GetHierarchy(ctx context.Context, factionId string) ([]api.HierarchyMember, error) {
	query := `
		SELECT id, faction_id, role, clan_id, player_id, permissions, appointed_at, appointed_by
		FROM faction_hierarchy
		WHERE faction_id = $1
		ORDER BY appointed_at
	`

	rows, err := r.db.QueryContext(ctx, query, factionId)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var members []api.HierarchyMember
	for rows.Next() {
		var member api.HierarchyMember
		if err := rows.Scan(
			&member.Id, &member.FactionId, &member.Role, &member.ClanId,
			&member.PlayerId, &member.Permissions, &member.AppointedAt, &member.AppointedBy,
		); err != nil {
			return nil, err
		}
		members = append(members, member)
	}

	return members, nil
}

func (r *Repository) GetMemberCount(ctx context.Context, factionId string) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM faction_hierarchy WHERE faction_id = $1`
	err := r.db.QueryRowContext(ctx, query, factionId).Scan(&count)
	return count, err
}

func (r *Repository) GetClanCount(ctx context.Context, factionId string) (int, error) {
	var count int
	query := `SELECT COUNT(DISTINCT clan_id) FROM faction_hierarchy WHERE faction_id = $1`
	err := r.db.QueryRowContext(ctx, query, factionId).Scan(&count)
	return count, err
}

