// Issue: #1856
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-core-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for guild core service
type Repository struct {
	db *pgxpool.Pool
}

// Guild internal model
type Guild struct {
	ID          uuid.UUID
	Name        string
	Tag         string
	Description sql.NullString
	LeaderID    uuid.UUID
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

// GuildMember internal model
type GuildMember struct {
	ID       uuid.UUID
	GuildID  uuid.UUID
	UserID   uuid.UUID
	Role     string
	JoinedAt time.Time
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

// GetGuilds retrieves guilds with optional filtering
func (r *Repository) GetGuilds(ctx context.Context, search *string, limit *int) ([]*Guild, error) {
	query := `
		SELECT id, name, tag, description, leader_id, created_at, updated_at
		FROM guilds.guilds
		WHERE ($1::text IS NULL OR name ILIKE '%' || $1 || '%' OR tag ILIKE '%' || $1 || '%')
		ORDER BY created_at DESC
		LIMIT $2
	`

	rows, err := r.db.Query(ctx, query, search, limit)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var guilds []*Guild
	for rows.Next() {
		var g Guild
		var desc sql.NullString

		err := rows.Scan(&g.ID, &g.Name, &g.Tag, &desc, &g.LeaderID, &g.CreatedAt, &g.UpdatedAt)
		if err != nil {
			return nil, err
		}

		g.Description = desc
		guilds = append(guilds, &g)
	}

	return guilds, rows.Err()
}

// GetGuildByID retrieves a guild by ID
func (r *Repository) GetGuildByID(ctx context.Context, id uuid.UUID) (*Guild, error) {
	query := `
		SELECT id, name, tag, description, leader_id, created_at, updated_at
		FROM guilds.guilds
		WHERE id = $1
	`

	var g Guild
	var desc sql.NullString

	err := r.db.QueryRow(ctx, query, id).Scan(
		&g.ID, &g.Name, &g.Tag, &desc, &g.LeaderID, &g.CreatedAt, &g.UpdatedAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrGuildNotFound
		}
		return nil, err
	}

	g.Description = desc
	return &g, nil
}

// CreateGuild creates a new guild
func (r *Repository) CreateGuild(ctx context.Context, req *api.CreateGuildRequest, leaderID uuid.UUID) (*Guild, error) {
	query := `
		INSERT INTO guilds.guilds (id, name, tag, description, leader_id, created_at, updated_at)
		VALUES (gen_random_uuid(), $1, $2, $3, $4, NOW(), NOW())
		RETURNING id, name, tag, description, leader_id, created_at, updated_at
	`

	var g Guild
	var desc sql.NullString
	if req.Description.IsSet() {
		desc.String = req.Description.Value
		desc.Valid = true
	}

	err := r.db.QueryRow(ctx, query, req.Name, req.Tag, desc, leaderID).Scan(
		&g.ID, &g.Name, &g.Tag, &desc, &g.LeaderID, &g.CreatedAt, &g.UpdatedAt,
	)
	if err != nil {
		return nil, err
	}

	g.Description = desc
	return &g, nil
}

// UpdateGuild updates guild information
func (r *Repository) UpdateGuild(ctx context.Context, id uuid.UUID, name, tag *string, description *api.OptString) error {
	query := `
		UPDATE guilds.guilds
		SET name = COALESCE($2, name),
		    tag = COALESCE($3, tag),
		    description = COALESCE($4, description),
		    updated_at = NOW()
		WHERE id = $1
	`

	var desc interface{}
	if description != nil && description.IsSet() {
		desc = description.Value
	}

	_, err := r.db.Exec(ctx, query, id, name, tag, desc)
	return err
}

// DeleteGuild deletes a guild
func (r *Repository) DeleteGuild(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM guilds.guilds WHERE id = $1`
	_, err := r.db.Exec(ctx, query, id)
	return err
}

// GetGuildMembers retrieves guild members
func (r *Repository) GetGuildMembers(ctx context.Context, guildID uuid.UUID) ([]*GuildMember, error) {
	query := `
		SELECT id, guild_id, user_id, role, joined_at
		FROM guilds.guild_members
		WHERE guild_id = $1
		ORDER BY joined_at ASC
	`

	rows, err := r.db.Query(ctx, query, guildID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var members []*GuildMember
	for rows.Next() {
		var m GuildMember
		err := rows.Scan(&m.ID, &m.GuildID, &m.UserID, &m.Role, &m.JoinedAt)
		if err != nil {
			return nil, err
		}
		members = append(members, &m)
	}

	return members, rows.Err()
}

// AddGuildMember adds a member to guild
func (r *Repository) AddGuildMember(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	query := `
		INSERT INTO guilds.guild_members (id, guild_id, user_id, role, joined_at)
		VALUES (gen_random_uuid(), $1, $2, $3, NOW())
	`

	_, err := r.db.Exec(ctx, query, guildID, userID, role)
	return err
}

// RemoveGuildMember removes a member from guild
func (r *Repository) RemoveGuildMember(ctx context.Context, guildID, userID uuid.UUID) error {
	query := `DELETE FROM guilds.guild_members WHERE guild_id = $1 AND user_id = $2`
	_, err := r.db.Exec(ctx, query, guildID, userID)
	return err
}

// UpdateGuildMemberRole updates member role
func (r *Repository) UpdateGuildMemberRole(ctx context.Context, guildID, userID uuid.UUID, role string) error {
	query := `
		UPDATE guilds.guild_members
		SET role = $3
		WHERE guild_id = $1 AND user_id = $2
	`

	_, err := r.db.Exec(ctx, query, guildID, userID, role)
	return err
}

// IsUserGuildLeader checks if user is guild leader
func (r *Repository) IsUserGuildLeader(ctx context.Context, guildID, userID uuid.UUID) (bool, error) {
	query := `SELECT 1 FROM guilds.guilds WHERE id = $1 AND leader_id = $2`

	var exists int
	err := r.db.QueryRow(ctx, query, guildID, userID).Scan(&exists)
	if err != nil {
		if err == sql.ErrNoRows {
			return false, nil
		}
		return false, err
	}

	return true, nil
}

// IsUserInGuild checks if user is member of guild
func (r *Repository) IsUserInGuild(ctx context.Context, guildID, userID uuid.UUID) (bool, error) {
	query := `SELECT 1 FROM guilds.guild_members WHERE guild_id = $1 AND user_id = $2`

	var exists int
	err := r.db.QueryRow(ctx, query, guildID, userID).Scan(&exists)
	if err != nil {
		if err == sql.ErrNoRows {
			return false, nil
		}
		return false, err
	}

	return true, nil
}
