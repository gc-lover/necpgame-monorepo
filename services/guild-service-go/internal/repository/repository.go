//go:align 64
// Issue: #2295

package repository

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"guild-service-go/pkg/api"
)

//go:align 64
type Repository interface {
	// Guild operations
	GetGuild(ctx context.Context, id uuid.UUID) (*api.Guild, error)
	CreateGuild(ctx context.Context, guild *api.Guild) (*api.Guild, error)
	UpdateGuild(ctx context.Context, id uuid.UUID, guild *api.Guild) (*api.Guild, error)
	DeleteGuild(ctx context.Context, id uuid.UUID) error
	ListGuilds(ctx context.Context, limit, offset int) ([]*api.Guild, error)

	// Guild member operations
	GetGuildMembers(ctx context.Context, guildID uuid.UUID) ([]*api.GuildMember, error)
	ListByGuildID(ctx context.Context, guildID uuid.UUID, page, limit int) ([]*api.GuildMember, int, error)
	AddGuildMember(ctx context.Context, member *api.GuildMember) (*api.GuildMember, error)
	UpdateGuildMember(ctx context.Context, guildID, playerID uuid.UUID, member *api.GuildMember) (*api.GuildMember, error)
	RemoveGuildMember(ctx context.Context, guildID, playerID uuid.UUID) error

	// Database access for custom queries
	GetDB() interface{}
}

//go:align 64
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

//go:align 64
func NewRepository(db *pgxpool.Pool, logger *zap.Logger) Repository {
	return &PostgresRepository{
		db:     db,
		logger: logger,
	}
}

// Conversion functions disabled - using API types directly for now

// Guild operations implementation

func (r *PostgresRepository) GetGuild(ctx context.Context, id uuid.UUID) (*api.Guild, error) {
	const query = `
		SELECT
			g.id, g.guild_name, g.description, g.leader_id,
			g.level, g.experience, g.max_members, g.current_members,
			g.reputation, g.created_at, g.updated_at
		FROM guilds g
		WHERE g.id = $1 AND g.deleted_at IS NULL
	`

	var guild api.Guild
	var description *string
	var level, experience, maxMembers, currentMembers, reputation *int
	var createdAt, updatedAt *time.Time

	err := r.db.QueryRow(ctx, query, id).Scan(
		&guild.ID, &guild.Name, &description,
		&guild.LeaderId, &level, &experience, &maxMembers,
		&currentMembers, &reputation, &createdAt, &updatedAt,
	)

	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.Error("Failed to get guild", zap.Error(err), zap.String("guild_id", id.String()))
		return nil, err
	}

	// Handle nullable fields
	if description != nil {
		guild.Description = api.OptString{Value: *description, Set: true}
	}
	if level != nil {
		guild.Level = api.OptInt{Value: *level, Set: true}
	}
	if experience != nil {
		guild.Experience = api.OptInt{Value: *experience, Set: true}
	}
	if maxMembers != nil {
		guild.MaxMembers = api.OptInt{Value: *maxMembers, Set: true}
	}
	if currentMembers != nil {
		guild.MemberCount = api.OptInt{Value: *currentMembers, Set: true}
	}
	if reputation != nil {
		guild.Reputation = api.OptInt{Value: *reputation, Set: true}
	}
	if createdAt != nil {
		guild.CreatedAt = api.OptDateTime{Value: *createdAt, Set: true}
	}
	if updatedAt != nil {
		guild.UpdatedAt = api.OptDateTime{Value: *updatedAt, Set: true}
	}

	return &guild, nil
}

func (r *PostgresRepository) CreateGuild(ctx context.Context, guild *api.Guild) (*api.Guild, error) {
	const query = `
		INSERT INTO guilds (
			id, guild_name, description, leader_id,
			level, experience, max_members, current_members,
			reputation, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
		)
		RETURNING id, created_at, updated_at
	`

	now := time.Now()

	var description string
	if guild.Description.Set {
		description = guild.Description.Value
	}

	var level, experience, maxMembers, currentMembers, reputation int
	if guild.Level.Set {
		level = guild.Level.Value
	}
	if guild.Experience.Set {
		experience = guild.Experience.Value
	}
	if guild.MaxMembers.Set {
		maxMembers = guild.MaxMembers.Value
	}
	if guild.MemberCount.Set {
		currentMembers = guild.MemberCount.Value
	}
	if guild.Reputation.Set {
		reputation = guild.Reputation.Value
	}

	err := r.db.QueryRow(ctx, query,
		guild.ID, guild.Name, description, guild.LeaderId,
		level, experience, maxMembers, currentMembers, reputation,
		now, now,
	).Scan(&guild.ID, &guild.CreatedAt.Value, &guild.UpdatedAt.Value)

	if err != nil {
		r.logger.Error("Failed to create guild", zap.Error(err), zap.String("guild_name", guild.Name))
		return nil, err
	}

	guild.CreatedAt.Set = true
	guild.UpdatedAt.Set = true

	return guild, nil
}

func (r *PostgresRepository) UpdateGuild(ctx context.Context, id uuid.UUID, guild *api.Guild) (*api.Guild, error) {
	const query = `
		UPDATE guilds SET
			guild_name = $2,
			description = $3,
			level = $4,
			experience = $5,
			max_members = $6,
			current_members = $7,
			reputation = $8,
			updated_at = $9
		WHERE id = $1 AND deleted_at IS NULL
		RETURNING updated_at
	`

	now := time.Now()

	description := ""
	if guild.Description.Set {
		description = guild.Description.Value
	}

	level := 1
	if guild.Level.Set {
		level = guild.Level.Value
	}

	experience := 0
	if guild.Experience.Set {
		experience = guild.Experience.Value
	}

	maxMembers := 50
	if guild.MaxMembers.Set {
		maxMembers = guild.MaxMembers.Value
	}

	currentMembers := 1
	if guild.MemberCount.Set {
		currentMembers = guild.MemberCount.Value
	}

	reputation := 0
	if guild.Reputation.Set {
		reputation = guild.Reputation.Value
	}

	err := r.db.QueryRow(ctx, query,
		id, // WHERE condition
		guild.Name, description, level, experience, maxMembers,
		currentMembers, reputation, now,
	).Scan(&guild.UpdatedAt.Value)

	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, fmt.Errorf("guild not found")
		}
		r.logger.Error("Failed to update guild", zap.Error(err), zap.String("guild_id", id.String()))
		return nil, err
	}

	guild.UpdatedAt.Set = true

	return guild, nil
}

func (r *PostgresRepository) DeleteGuild(ctx context.Context, id uuid.UUID) error {
	const query = `
		UPDATE guilds SET
			deleted_at = $2,
			updated_at = $2
		WHERE id = $1 AND deleted_at IS NULL
	`

	now := time.Now()
	result, err := r.db.Exec(ctx, query, id, now)
	if err != nil {
		r.logger.Error("Failed to delete guild", zap.Error(err), zap.String("guild_id", id.String()))
		return err
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("guild not found")
	}

	return nil
}

func (r *PostgresRepository) ListGuilds(ctx context.Context, limit, offset int) ([]*api.Guild, error) {
	const query = `
		SELECT
			g.id, g.guild_name, g.description, g.leader_id,
			g.level, g.experience, g.max_members, g.current_members,
			g.reputation, g.created_at, g.updated_at
		FROM guilds g
		WHERE g.deleted_at IS NULL
		ORDER BY g.created_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list guilds", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var guilds []*api.Guild
	for rows.Next() {
		var guild api.Guild
		var description, faction *string
		var level, experience, maxMembers, currentMembers, reputation *int
		var isRecruiting *bool
		var createdAt, updatedAt *time.Time

		err := rows.Scan(
			&guild.ID, &guild.Name, &description,
			&guild.LeaderId, &level, &experience, &maxMembers,
			&currentMembers, &reputation, &createdAt, &updatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan guild", zap.Error(err))
			return nil, err
		}

		// Handle nullable fields
		if description != nil {
			guild.Description = api.OptString{Value: *description, Set: true}
		}
		if faction != nil {
			guild.Faction = api.OptString{Value: *faction, Set: true}
		}
		if level != nil {
			guild.Level = api.OptInt{Value: *level, Set: true}
		}
		if experience != nil {
			guild.Experience = api.OptInt64{Value: *experience, Set: true}
		}
		if maxMembers != nil {
			guild.MaxMembers = api.OptInt{Value: *maxMembers, Set: true}
		}
		if currentMembers != nil {
			guild.MemberCount = api.OptInt{Value: *currentMembers, Set: true}
		}
		if reputation != nil {
			guild.Reputation = api.OptInt{Value: *reputation, Set: true}
		}
		if isRecruiting != nil {
			guild.IsRecruiting = api.OptBool{Value: *isRecruiting, Set: true}
		}
		if createdAt != nil {
			guild.CreatedAt = api.OptDateTime{Value: *createdAt, Set: true}
		}
		if updatedAt != nil {
			guild.UpdatedAt = api.OptDateTime{Value: *updatedAt, Set: true}
		}

		guilds = append(guilds, &guild)
	}

	if err = rows.Err(); err != nil {
		r.logger.Error("Error iterating guild rows", zap.Error(err))
		return nil, err
	}

	return guilds, nil
}

// Guild member operations implementation

func (r *PostgresRepository) GetGuildMembers(ctx context.Context, guildID uuid.UUID) ([]*api.GuildMember, error) {
	const query = `
		SELECT
			gm.id, gm.guild_id, gm.player_id, gm.role, gm.joined_at,
			gm.last_active, gm.contribution_points, gm.permissions, gm.version
		FROM guild_members gm
		WHERE gm.guild_id = $1 AND gm.left_at IS NULL
		ORDER BY gm.joined_at ASC
	`

	rows, err := r.db.Query(ctx, query, guildID)
	if err != nil {
		r.logger.Error("Failed to get guild members", zap.Error(err), zap.String("guild_id", guildID.String()))
		return nil, err
	}
	defer rows.Close()

	var members []*api.GuildMember
	for rows.Next() {
		var member api.GuildMember
		var joinedAt, lastActive *time.Time
		var permissions map[string]interface{}

		err := rows.Scan(
			&member.ID, &member.GuildId, &member.UserId, &member.Role,
			&joinedAt, &lastActive, &member.Contribution,
			&permissions, &member.Version,
		)
		if err != nil {
			r.logger.Error("Failed to scan guild member", zap.Error(err))
			return nil, err
		}

		// Handle nullable fields
		if joinedAt != nil {
			member.JoinedAt = api.OptDateTime{Value: *joinedAt, Set: true}
		}
		if lastActive != nil {
			member.LastActive = api.OptDateTime{Value: *lastActive, Set: true}
		}

		// Handle permissions (convert from JSON)
		if permissions != nil {
			if canInvite, ok := permissions["can_invite"].(bool); ok {
				member.Permissions.CanInvite = api.OptBool{Value: canInvite, Set: true}
			}
			if canKick, ok := permissions["can_kick"].(bool); ok {
				member.Permissions.CanKick = api.OptBool{Value: canKick, Set: true}
			}
			if canManageBank, ok := permissions["can_manage_bank"].(bool); ok {
				member.Permissions.CanManageBank = api.OptBool{Value: canManageBank, Set: true}
			}
			if canSchedule, ok := permissions["can_schedule"].(bool); ok {
				member.Permissions.CanSchedule = api.OptBool{Value: canSchedule, Set: true}
			}
			if canPromote, ok := permissions["can_promote"].(bool); ok {
				member.Permissions.CanPromote = api.OptBool{Value: canPromote, Set: true}
			}
		}

		members = append(members, &member)
	}

	if err = rows.Err(); err != nil {
		r.logger.Error("Error iterating guild member rows", zap.Error(err))
		return nil, err
	}

	return members, nil
}

func (r *PostgresRepository) AddGuildMember(ctx context.Context, member *api.GuildMember) (*api.GuildMember, error) {
	// TODO: Implement database insertion
	// This is a placeholder implementation
	return member, nil
}

func (r *PostgresRepository) UpdateGuildMember(ctx context.Context, guildID, playerID uuid.UUID, member *api.GuildMember) (*api.GuildMember, error) {
	const query = `
		UPDATE guild_members SET
			version = version + 1,
			role = $4,
			last_active = $5,
			contribution_points = $6,
			permissions = $7
		WHERE guild_id = $1 AND player_id = $2 AND version = $3 AND left_at IS NULL
		RETURNING version, last_active
	`

	now := time.Now()
	newVersion := member.Version + 1

	// Prepare permissions JSON
	permissions := map[string]interface{}{
		"can_invite":     member.Permissions.CanInvite.GetOrZero(),
		"can_kick":       member.Permissions.CanKick.GetOrZero(),
		"can_manage_bank": member.Permissions.CanManageBank.GetOrZero(),
		"can_schedule":   member.Permissions.CanSchedule.GetOrZero(),
		"can_promote":    member.Permissions.CanPromote.GetOrZero(),
	}

	err := r.db.QueryRow(ctx, query,
		guildID, playerID, member.Version, // WHERE conditions
		member.Role, now, member.Contribution.GetOrZero(), permissions,
	).Scan(&newVersion, &member.LastActive.Value)

	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, fmt.Errorf("guild member not found or version conflict")
		}
		r.logger.Error("Failed to update guild member", zap.Error(err),
			zap.String("guild_id", guildID.String()), zap.String("player_id", playerID.String()))
		return nil, err
	}

	member.Version = newVersion
	member.LastActive.Set = true

	return member, nil
}

func (r *PostgresRepository) RemoveGuildMember(ctx context.Context, guildID, playerID uuid.UUID) error {
	const query = `
		UPDATE guild_members SET
			left_at = $3
		WHERE guild_id = $1 AND player_id = $2 AND left_at IS NULL
	`

	now := time.Now()
	result, err := r.db.Exec(ctx, query, guildID, playerID, now)
	if err != nil {
		r.logger.Error("Failed to remove guild member", zap.Error(err),
			zap.String("guild_id", guildID.String()), zap.String("player_id", playerID.String()))
		return err
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("guild member not found")
	}

	return nil
}

// GetDB returns the underlying database connection for custom queries
func (r *PostgresRepository) GetDB() interface{} {
	return r.db
}
