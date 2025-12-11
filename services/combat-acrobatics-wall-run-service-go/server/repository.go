// Issue: #1510
package server

import (
	"context"
	"database/sql"
	"errors"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/combat-acrobatics-wall-run-service-go/pkg/api"
)

type Repository struct {
	db *pgxpool.Pool
}

func NewRepository(db *pgxpool.Pool) *Repository {
	return &Repository{db: db}
}

func (r *Repository) GetSurfacesInZone(ctx context.Context, characterID uuid.UUID, zoneID string) ([]api.Surface, error) {
	query := `
		SELECT surface_id, material, surface_type, normal_x, normal_y, normal_z,
		       position_x, position_y, position_z, is_suitable
		FROM gameplay.wall_run_surfaces
		WHERE zone_id = $1 AND is_active = true
		ORDER BY surface_id
	`

	rows, err := r.db.Query(ctx, query, zoneID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var surfaces []api.Surface
	for rows.Next() {
		var surface api.Surface
		var normalX, normalY, normalZ float64
		var posX, posY, posZ float64

		err := rows.Scan(
			&surface.SurfaceID,
			&surface.Material,
			&surface.SurfaceType,
			&normalX, &normalY, &normalZ,
			&posX, &posY, &posZ,
			&surface.IsSuitable,
		)
		if err != nil {
			return nil, err
		}

		surface.Normal = api.Direction3D{X: float32(normalX), Y: float32(normalY), Z: float32(normalZ)}
		surface.Position = api.Position3D{X: float32(posX), Y: float32(posY), Z: float32(posZ)}

		surfaces = append(surfaces, surface)
	}

	return surfaces, rows.Err()
}

func (r *Repository) GetSurface(ctx context.Context, surfaceID uuid.UUID) (*api.Surface, error) {
	query := `
		SELECT surface_id, material, surface_type, normal_x, normal_y, normal_z,
		       position_x, position_y, position_z, is_suitable
		FROM gameplay.wall_run_surfaces
		WHERE surface_id = $1 AND is_active = true
	`

	var surface api.Surface
	var normalX, normalY, normalZ float64
	var posX, posY, posZ float64

	err := r.db.QueryRow(ctx, query, surfaceID).Scan(
		&surface.SurfaceID,
		&surface.Material,
		&surface.SurfaceType,
		&normalX, &normalY, &normalZ,
		&posX, &posY, &posZ,
		&surface.IsSuitable,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, ErrNotFound
		}
		return nil, err
	}

	surface.Normal = api.Direction3D{X: float32(normalX), Y: float32(normalY), Z: float32(normalZ)}
	surface.Position = api.Position3D{X: float32(posX), Y: float32(posY), Z: float32(posZ)}

	return &surface, nil
}

func (r *Repository) GetActiveWallRun(ctx context.Context, characterID uuid.UUID) (*WallRunSession, error) {
	query := `
		SELECT id, surface_id, state_id, started_at, direction_x, direction_y, direction_z,
		       start_position_x, start_position_y, start_position_z,
		       current_position_x, current_position_y, current_position_z,
		       stamina_consumed, is_active
		FROM gameplay.wall_run_sessions
		WHERE character_id = $1 AND is_active = true
		ORDER BY started_at DESC
		LIMIT 1
	`

	var session WallRunSession
	session.CharacterID = characterID

	var dirX, dirY, dirZ float64
	var startX, startY, startZ float64
	var currX, currY, currZ float64

	err := r.db.QueryRow(ctx, query, characterID).Scan(
		&session.ID,
		&session.SurfaceID,
		&session.StateID,
		&session.StartedAt,
		&dirX, &dirY, &dirZ,
		&startX, &startY, &startZ,
		&currX, &currY, &currZ,
		&session.StaminaConsumed,
		&session.IsActive,
	)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, ErrNotFound
		}
		return nil, err
	}

	session.Direction = api.Direction3D{X: dirX, Y: dirY, Z: dirZ}
	session.StartPosition = api.Position3D{X: startX, Y: startY, Z: startZ}
	session.CurrentPosition = api.Position3D{X: currX, Y: currY, Z: currZ}

	return &session, nil
}

func (r *Repository) CreateWallRunSession(ctx context.Context, session *WallRunSession) error {
	query := `
		INSERT INTO gameplay.wall_run_sessions (
			id, character_id, surface_id, state_id, started_at,
			direction_x, direction_y, direction_z,
			start_position_x, start_position_y, start_position_z,
			current_position_x, current_position_y, current_position_z,
			stamina_consumed, is_active
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16)
	`

	_, err := r.db.Exec(ctx, query,
		session.ID,
		session.CharacterID,
		session.SurfaceID,
		session.StateID,
		session.StartedAt,
		session.Direction.X, session.Direction.Y, session.Direction.Z,
		session.StartPosition.X, session.StartPosition.Y, session.StartPosition.Z,
		session.CurrentPosition.X, session.CurrentPosition.Y, session.CurrentPosition.Z,
		session.StaminaConsumed,
		session.IsActive,
	)

	return err
}

func (r *Repository) UpdateWallRunSession(ctx context.Context, session *WallRunSession) error {
	query := `
		UPDATE gameplay.wall_run_sessions SET
			direction_x = $1, direction_y = $2, direction_z = $3,
			current_position_x = $4, current_position_y = $5, current_position_z = $6,
			stamina_consumed = $7, is_active = $8
		WHERE id = $9
	`

	_, err := r.db.Exec(ctx, query,
		session.Direction.X, session.Direction.Y, session.Direction.Z,
		session.CurrentPosition.X, session.CurrentPosition.Y, session.CurrentPosition.Z,
		session.StaminaConsumed,
		session.IsActive,
		session.ID,
	)

	return err
}
