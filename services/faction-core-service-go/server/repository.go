// Package server Issue: #1442
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
	var idStr, leaderClanIDStr, name, ideology, description, status, createdAt, updatedAt string
	var factionType string
	err := r.db.QueryRowContext(ctx, query,
		id, req.Name, req.Type, req.Ideology, req.Description, req.LeaderClanID, now, now,
	).Scan(
		&idStr, &name, &factionType, &ideology, &description,
		&leaderClanIDStr, &status, &createdAt, &updatedAt,
	)
	if err != nil {
		return nil, err
	}

	// Convert to OptUUID and OptString
	idUUID, _ := uuid.Parse(idStr)
	faction.ID = api.NewOptUUID(idUUID)
	faction.Name = api.NewOptString(name)
	faction.Type = api.NewOptFactionType(api.FactionType(factionType))
	if ideology != "" {
		faction.Ideology = api.NewOptString(ideology)
	}
	if description != "" {
		faction.Description = api.NewOptString(description)
	}
	if leaderClanIDStr != "" {
		leaderClanIDUUID, _ := uuid.Parse(leaderClanIDStr)
		faction.LeaderClanID = api.NewOptUUID(leaderClanIDUUID)
	}
	faction.Status = api.NewOptFactionStatus(api.FactionStatus(status))
	createdAtTime, _ := time.Parse(time.RFC3339, createdAt)
	updatedAtTime, _ := time.Parse(time.RFC3339, updatedAt)
	faction.CreatedAt = api.NewOptDateTime(createdAtTime)
	faction.UpdatedAt = api.NewOptDateTime(updatedAtTime)

	return &faction, nil
}

func (r *Repository) GetFactionByID(ctx context.Context, factionId string) (*api.Faction, error) {
	query := `
		SELECT id, name, type, ideology, description, leader_clan_id, status, created_at, updated_at
		FROM factions
		WHERE id = $1
	`

	var idStr, name, factionType, ideology, description, leaderClanIDStr, status, createdAt, updatedAt string
	err := r.db.QueryRowContext(ctx, query, factionId).Scan(
		&idStr, &name, &factionType, &ideology, &description,
		&leaderClanIDStr, &status, &createdAt, &updatedAt,
	)

	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	// Convert to Opt* types
	var faction api.Faction
	idUUID, _ := uuid.Parse(idStr)
	faction.ID = api.NewOptUUID(idUUID)
	faction.Name = api.NewOptString(name)
	faction.Type = api.NewOptFactionType(api.FactionType(factionType))
	if ideology != "" {
		faction.Ideology = api.NewOptString(ideology)
	}
	if description != "" {
		faction.Description = api.NewOptString(description)
	}
	if leaderClanIDStr != "" {
		leaderClanIDUUID, _ := uuid.Parse(leaderClanIDStr)
		faction.LeaderClanID = api.NewOptUUID(leaderClanIDUUID)
	}
	faction.Status = api.NewOptFactionStatus(api.FactionStatus(status))
	createdAtTime, _ := time.Parse(time.RFC3339, createdAt)
	updatedAtTime, _ := time.Parse(time.RFC3339, updatedAt)
	faction.CreatedAt = api.NewOptDateTime(createdAtTime)
	faction.UpdatedAt = api.NewOptDateTime(updatedAtTime)

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

	var nameVal, ideologyVal, descriptionVal, statusVal interface{}
	if req.Name.Set {
		nameVal = req.Name.Value
	}
	if req.Ideology.Set {
		ideologyVal = req.Ideology.Value
	}
	if req.Description.Set {
		descriptionVal = req.Description.Value
	}
	if req.Status.Set {
		statusVal = req.Status.Value
	}

	var idStr, name, factionType, ideology, description, leaderClanIDStr, status, createdAt, updatedAt string
	err := r.db.QueryRowContext(ctx, query,
		nameVal, ideologyVal, descriptionVal, statusVal, now, factionId,
	).Scan(
		&idStr, &name, &factionType, &ideology, &description,
		&leaderClanIDStr, &status, &createdAt, &updatedAt,
	)

	if err != nil {
		return nil, err
	}

	// Convert to Opt* types
	var faction api.Faction
	idUUID, _ := uuid.Parse(idStr)
	faction.ID = api.NewOptUUID(idUUID)
	faction.Name = api.NewOptString(name)
	faction.Type = api.NewOptFactionType(api.FactionType(factionType))
	if ideology != "" {
		faction.Ideology = api.NewOptString(ideology)
	}
	if description != "" {
		faction.Description = api.NewOptString(description)
	}
	if leaderClanIDStr != "" {
		leaderClanIDUUID, _ := uuid.Parse(leaderClanIDStr)
		faction.LeaderClanID = api.NewOptUUID(leaderClanIDUUID)
	}
	faction.Status = api.NewOptFactionStatus(api.FactionStatus(status))
	createdAtTime, _ := time.Parse(time.RFC3339, createdAt)
	updatedAtTime, _ := time.Parse(time.RFC3339, updatedAt)
	faction.CreatedAt = api.NewOptDateTime(createdAtTime)
	faction.UpdatedAt = api.NewOptDateTime(updatedAtTime)

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

	var args []interface{}
	argIndex := 1

	// Apply filters
	if params.Type.Set {
		baseQuery += ` AND type = $` + string(rune(argIndex))
		countQuery += ` AND type = $` + string(rune(argIndex))
		args = append(args, params.Type.Value)
		argIndex++
	}

	if params.Status.Set {
		baseQuery += ` AND status = $` + string(rune(argIndex))
		countQuery += ` AND status = $` + string(rune(argIndex))
		args = append(args, params.Status.Value)
		argIndex++
	}

	// Get total count
	var total int
	if err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total); err != nil {
		return nil, 0, err
	}

	// Apply pagination
	page := 1
	if params.Page.Set {
		page = params.Page.Value
	}

	limit := 10
	if params.Limit.Set {
		limit = params.Limit.Value
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
		var idStr, name, factionType, ideology, description, leaderClanIDStr, status, createdAt, updatedAt string
		if err := rows.Scan(
			&idStr, &name, &factionType, &ideology, &description,
			&leaderClanIDStr, &status, &createdAt, &updatedAt,
		); err != nil {
			return nil, 0, err
		}

		// Convert to Opt* types
		var faction api.Faction
		idUUID, _ := uuid.Parse(idStr)
		faction.ID = api.NewOptUUID(idUUID)
		faction.Name = api.NewOptString(name)
		faction.Type = api.NewOptFactionType(api.FactionType(factionType))
		if ideology != "" {
			faction.Ideology = api.NewOptString(ideology)
		}
		if description != "" {
			faction.Description = api.NewOptString(description)
		}
		if leaderClanIDStr != "" {
			leaderClanIDUUID, _ := uuid.Parse(leaderClanIDStr)
			faction.LeaderClanID = api.NewOptUUID(leaderClanIDUUID)
		}
		faction.Status = api.NewOptFactionStatus(api.FactionStatus(status))
		createdAtTime, _ := time.Parse(time.RFC3339, createdAt)
		updatedAtTime, _ := time.Parse(time.RFC3339, updatedAt)
		faction.CreatedAt = api.NewOptDateTime(createdAtTime)
		faction.UpdatedAt = api.NewOptDateTime(updatedAtTime)

		factions = append(factions, faction)
	}

	return factions, total, nil
}

func (r *Repository) UpdateHierarchy() error {
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
		var id, factionID, clanID, playerID, appointedBy uuid.UUID
		var role string
		var permissions []byte
		var appointedAt time.Time
		if err := rows.Scan(
			&id, &factionID, &role, &clanID,
			&playerID, &permissions, &appointedAt, &appointedBy,
		); err != nil {
			return nil, err
		}
		member.ID = api.NewOptUUID(id)
		member.FactionID = api.NewOptUUID(factionID)
		member.ClanID = api.NewOptUUID(clanID)
		member.PlayerID = api.NewOptUUID(playerID)
		member.AppointedBy = api.NewOptUUID(appointedBy)
		member.AppointedAt = api.NewOptDateTime(appointedAt)
		member.Role = api.NewOptHierarchyRole(api.HierarchyRole(role))
		// Parse permissions JSON from database
		if len(permissions) > 0 {
			var perms api.HierarchyMemberPermissions
			if err := perms.UnmarshalJSON(permissions); err == nil {
				member.Permissions = api.NewOptHierarchyMemberPermissions(perms)
			} else {
				// If parsing fails, set empty permissions
				member.Permissions = api.OptHierarchyMemberPermissions{}
			}
		} else {
			member.Permissions = api.OptHierarchyMemberPermissions{}
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
