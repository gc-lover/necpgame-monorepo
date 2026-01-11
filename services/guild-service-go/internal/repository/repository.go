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

	"necpgame/services/guild-service-go/pkg/api"
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
	AddGuildMember(ctx context.Context, member *api.GuildMember) (*api.GuildMember, error)
	UpdateGuildMember(ctx context.Context, guildID, playerID uuid.UUID, member *api.GuildMember) (*api.GuildMember, error)
	RemoveGuildMember(ctx context.Context, guildID, playerID uuid.UUID) error

	// Guild treasury operations
	GetGuildTreasury(ctx context.Context, guildID uuid.UUID) (*api.GuildBank, error)

	// Guild territory operations
	NeutralizeGuildTerritories(ctx context.Context, guildID uuid.UUID) error

	// Guild event operations
	CancelGuildEvents(ctx context.Context, guildID uuid.UUID) error

	// Guild announcement operations
	ArchiveGuildAnnouncements(ctx context.Context, guildID uuid.UUID) error

	// Database access for custom queries
	GetDB() interface{}
}

//go:align 64
type PostgresRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

//go:align 64
func NewRepository() Repository {
	// For now, return a mock repository for testing
	return &PostgresRepository{
		db:     nil, // Will be set later or mocked
		logger: nil, // Will be set later or mocked
	}
}

//go:align 64
func NewRepositoryWithDB(db *pgxpool.Pool, logger *zap.Logger) Repository {
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
		var description *string
		var level, experience, maxMembers, currentMembers, reputation *int
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
		if level != nil {
			guild.Level = api.OptInt{Value: *level, Set: true}
		}
		if experience != nil {
			guild.Experience = api.OptInt{Value: *experience, Set: true} // Use OptInt instead of OptInt64
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

		err := rows.Scan(
			&member.GuildId, &member.UserId, &member.Role,
			&joinedAt, &lastActive, &member.Contribution,
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

		// Permissions are not part of the current GuildMember struct
		// TODO: Add permissions support when GuildMember struct is updated

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
			role = $3,
			last_active = $4,
			contribution_points = $5
		WHERE guild_id = $1 AND player_id = $2 AND left_at IS NULL
		RETURNING last_active
	`

	now := time.Now()
	// Get contribution value, default to 0 if not set
	contribution := 0
	if member.Contribution.Set {
		contribution = member.Contribution.Value
	}

	err := r.db.QueryRow(ctx, query,
		guildID, playerID, // WHERE conditions
		member.Role, now, contribution,
	).Scan(&member.LastActive.Value)

	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, fmt.Errorf("guild member not found")
		}
		r.logger.Error("Failed to update guild member", zap.Error(err),
			zap.String("guild_id", guildID.String()), zap.String("player_id", playerID.String()))
		return nil, err
	}

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

// GetGuildTreasury retrieves guild treasury/bank information
func (r *PostgresRepository) GetGuildTreasury(ctx context.Context, guildID uuid.UUID) (*api.GuildBank, error) {
	const query = `
		SELECT
			gb.id, gb.guild_id, gb.version, gb.currency_type,
			gb.amount, gb.last_transaction
		FROM guild_bank gb
		WHERE gb.guild_id = $1
		ORDER BY gb.last_transaction DESC
		LIMIT 1
	`

	var bank api.GuildBank
	var guildIDStr, currencyType string
	var lastTransaction time.Time

	err := r.db.QueryRow(ctx, query, guildID).Scan(
		&bank.ID, &guildIDStr, &bank.Version, &currencyType,
		&bank.Amount, &lastTransaction,
	)

	if err != nil {
		if err == pgx.ErrNoRows {
			// Return default bank entry if none exists
			return &api.GuildBank{
				ID:             uuid.New().String(),
				GuildID:        guildID.String(),
				Version:        1,
				CurrencyType:   "eddies",
				Amount:         0,
				LastTransaction: time.Now(),
			}, nil
		}
		r.logger.Error("Failed to get guild treasury", zap.Error(err), zap.String("guild_id", guildID.String()))
		return nil, err
	}

	bank.GuildID = guildIDStr
	bank.CurrencyType = currencyType
	bank.LastTransaction = lastTransaction

	return &bank, nil
}

// NeutralizeGuildTerritories removes guild control over territories
func (r *PostgresRepository) NeutralizeGuildTerritories(ctx context.Context, guildID uuid.UUID) error {
	const query = `
		UPDATE guild_territories
		SET status = 'neutralized', updated_at = $2
		WHERE guild_id = $1 AND status = 'controlled'
	`

	now := time.Now()
	result, err := r.db.Exec(ctx, query, guildID, now)
	if err != nil {
		r.logger.Error("Failed to neutralize guild territories", zap.Error(err), zap.String("guild_id", guildID.String()))
		return err
	}

	r.logger.Info("Neutralized guild territories",
		zap.String("guild_id", guildID.String()),
		zap.Int64("affected_territories", result.RowsAffected()))

	return nil
}

// CancelGuildEvents cancels all upcoming guild events
func (r *PostgresRepository) CancelGuildEvents(ctx context.Context, guildID uuid.UUID) error {
	const query = `
		UPDATE guild_events
		SET status = 'cancelled', updated_at = $2
		WHERE guild_id = $1 AND status = 'scheduled' AND scheduled_at > $2
	`

	now := time.Now()
	result, err := r.db.Exec(ctx, query, guildID, now)
	if err != nil {
		r.logger.Error("Failed to cancel guild events", zap.Error(err), zap.String("guild_id", guildID.String()))
		return err
	}

	r.logger.Info("Cancelled guild events",
		zap.String("guild_id", guildID.String()),
		zap.Int64("cancelled_events", result.RowsAffected()))

	return nil
}

// ArchiveGuildAnnouncements moves old announcements to archive
func (r *PostgresRepository) ArchiveGuildAnnouncements(ctx context.Context, guildID uuid.UUID) error {
	const query = `
		UPDATE guild_announcements
		SET status = 'archived', updated_at = $2
		WHERE guild_id = $1 AND status = 'active' AND created_at < $2 - INTERVAL '30 days'
	`

	now := time.Now()
	result, err := r.db.Exec(ctx, query, guildID, now)
	if err != nil {
		r.logger.Error("Failed to archive guild announcements", zap.Error(err), zap.String("guild_id", guildID.String()))
		return err
	}

	r.logger.Info("Archived guild announcements",
		zap.String("guild_id", guildID.String()),
		zap.Int64("archived_announcements", result.RowsAffected()))

	return nil
}

// GetDB returns the underlying database connection for custom queries
func (r *PostgresRepository) GetDB() interface{} {
	return r.db
}
