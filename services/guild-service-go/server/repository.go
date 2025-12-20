// Package server Issue: #1943
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	_ "github.com/lib/pq"
)

// Repository handles data access with optimizations
type Repository struct {
	db *sql.DB
}

// NewRepository creates new repository with database connection

// CreateGuild creates new guild in database
func (r *Repository) CreateGuild(ctx context.Context, guild *GuildDefinition) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO guilds.guild_definitions (
			guild_id, name, description, leader_id, level, member_count,
			reputation, created_at, updated_at, region, is_active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	_, err := r.db.ExecContext(ctx, query,
		guild.GuildID,
		guild.Name,
		guild.Description,
		guild.LeaderID,
		guild.Level,
		guild.MemberCount,
		guild.Reputation,
		guild.CreatedAt,
		guild.UpdatedAt,
		guild.Region,
		guild.IsActive,
	)

	if err != nil {
		return fmt.Errorf("failed to create guild: %w", err)
	}

	return nil
}

// GetGuild retrieves guild by ID
func (r *Repository) GetGuild(ctx context.Context, guildID string) (*GuildDefinition, error) {
	if r.db == nil {
		return nil, fmt.Errorf("database not connected")
	}

	query := `
		SELECT guild_id, name, description, leader_id, level, member_count,
		       reputation, created_at, updated_at, region, is_active
		FROM guilds.guild_definitions
		WHERE guild_id = $1
	`

	var guild GuildDefinition
	err := r.db.QueryRowContext(ctx, query, guildID).Scan(
		&guild.GuildID,
		&guild.Name,
		&guild.Description,
		&guild.LeaderID,
		&guild.Level,
		&guild.MemberCount,
		&guild.Reputation,
		&guild.CreatedAt,
		&guild.UpdatedAt,
		&guild.Region,
		&guild.IsActive,
	)

	if errors.Is(err, sql.ErrNoRows) {
		return nil, fmt.Errorf("guild not found")
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get guild: %w", err)
	}

	return &guild, nil
}

// GetGuilds returns paginated list of guilds
func (r *Repository) GetGuilds(ctx context.Context, params *GetGuildsParams) ([]*GuildResponse, int, error) {
	if r.db == nil {
		return nil, 0, fmt.Errorf("database not connected")
	}

	// OPTIMIZATION: Use covering index for hot queries
	query := `
		SELECT guild_id, name, description, leader_id, level, member_count,
		       reputation, created_at, region
		FROM guilds.guild_definitions
		WHERE is_active = true
		ORDER BY level DESC, member_count DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.QueryContext(ctx, query, params.Limit, (params.Page-1)*params.Limit)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query guilds: %w", err)
	}
	defer rows.Close()

	var guilds []*GuildResponse
	for rows.Next() {
		var guild GuildResponse
		err := rows.Scan(
			&guild.ID,
			&guild.Name,
			&guild.Description,
			&guild.LeaderID,
			&guild.Level,
			&guild.MemberCount,
			&guild.Reputation,
			&guild.CreatedAt,
			&guild.Region,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan guild: %w", err)
		}
		guilds = append(guilds, &guild)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM guilds.guild_definitions WHERE is_active = true`
	var total int
	err = r.db.QueryRowContext(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count guilds: %w", err)
	}

	return guilds, total, nil
}

// AddGuildMember adds a member to guild
func (r *Repository) AddGuildMember(ctx context.Context, guildID string, playerID uuid.UUID, role string) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO guilds.guild_members (
			guild_id, player_id, role, joined_at, last_active
		) VALUES ($1, $2, $3, $4, $4)
	`

	_, err := r.db.ExecContext(ctx, query, guildID, playerID, role, time.Now())
	if err != nil {
		return fmt.Errorf("failed to add guild member: %w", err)
	}

	return nil
}

// GetGuildMembers returns guild members
func (r *Repository) GetGuildMembers(ctx context.Context, guildID string, params *GetMembersParams) ([]*GuildMember, error) {
	if r.db == nil {
		return nil, fmt.Errorf("database not connected")
	}

	query := `
		SELECT gm.player_id, p.username, gm.role, gm.joined_at, gm.last_active,
		       gm.contribution_score, gr.permissions
		FROM guilds.guild_members gm
		JOIN players.player_profiles p ON gm.player_id = p.player_id
		LEFT JOIN guilds.guild_roles gr ON gm.role = gr.name AND gr.guild_id = gm.guild_id
		WHERE gm.guild_id = $1
		ORDER BY gm.joined_at ASC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.QueryContext(ctx, query, guildID, params.Limit, (params.Page-1)*params.Limit)
	if err != nil {
		return nil, fmt.Errorf("failed to query guild members: %w", err)
	}
	defer rows.Close()

	var members []*GuildMember
	for rows.Next() {
		var member GuildMember
		var permissions []string
		err := rows.Scan(
			&member.PlayerID,
			&member.Username,
			&member.Role,
			&member.JoinedAt,
			&member.LastActive,
			&member.ContributionScore,
			&permissions,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan member: %w", err)
		}
		member.Permissions = permissions
		members = append(members, &member)
	}

	return members, nil
}

// CreateInvitation creates guild invitation
func (r *Repository) CreateInvitation(ctx context.Context, invitation *Invitation) error {
	if r.db == nil {
		return fmt.Errorf("database not connected")
	}

	query := `
		INSERT INTO guilds.guild_invitations (
			id, guild_id, player_id, invited_by, message, status,
			created_at, expires_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		invitation.ID,
		invitation.GuildID,
		invitation.PlayerID,
		invitation.InvitedBy,
		invitation.Message,
		invitation.Status,
		invitation.CreatedAt,
		invitation.ExpiresAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create invitation: %w", err)
	}

	return nil
}
