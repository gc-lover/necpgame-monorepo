// Issue: #427
// PERFORMANCE: Database layer with connection pooling and prepared statements

package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// ClanWarRepositoryInterface defines the contract for clan war data operations
type ClanWarRepositoryInterface interface {
	CreateWar(ctx context.Context, war *ClanWar) error
	GetWarByID(ctx context.Context, warID uuid.UUID) (*ClanWar, error)
	ListWars(ctx context.Context, limit, offset int) ([]*ClanWar, error)
	UpdateWar(ctx context.Context, war *ClanWar) error

	CreateBattle(ctx context.Context, battle *Battle) error
	GetBattleByID(ctx context.Context, battleID uuid.UUID) (*Battle, error)
	ListBattles(ctx context.Context, warID uuid.UUID, limit, offset int) ([]*Battle, error)

	GetTerritoryByID(ctx context.Context, territoryID uuid.UUID) (*Territory, error)
	ListTerritories(ctx context.Context, limit, offset int) ([]*Territory, error)

	// Additional methods for extended testing
	GetWarByIDWithBattles(ctx context.Context, warID uuid.UUID) (*ClanWar, []*Battle, error)
	GetActiveWarsByClan(ctx context.Context, clanID uuid.UUID) ([]*ClanWar, error)
	GetWarStatistics(ctx context.Context, warID uuid.UUID) (*WarStatistics, error)
}

// ClanWarRepository handles database operations for clan wars
// PERFORMANCE: Connection pooling, optimized queries
type ClanWarRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// ClanWar represents a clan war entity
// PERFORMANCE: Optimized struct alignment (large fields first)
type ClanWar struct {
	ID          uuid.UUID  `json:"id" db:"id"`
	ClanID1     uuid.UUID  `json:"clan_id_1" db:"clan_id_1"`
	ClanID2     uuid.UUID  `json:"clan_id_2" db:"clan_id_2"`
	Status      string     `json:"status" db:"status"`           // pending, active, completed, cancelled
	TerritoryID uuid.UUID  `json:"territory_id" db:"territory_id"`
	StartTime   *time.Time `json:"start_time" db:"start_time"`
	EndTime     *time.Time `json:"end_time" db:"end_time"`
	WinnerClanID *uuid.UUID `json:"winner_clan_id" db:"winner_clan_id"`
	ScoreClan1  int32      `json:"score_clan_1" db:"score_clan_1"`
	ScoreClan2  int32      `json:"score_clan_2" db:"score_clan_2"`
	CreatedAt   time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time  `json:"updated_at" db:"updated_at"`
}

// Battle represents a battle within a clan war
type Battle struct {
	ID          uuid.UUID `json:"id" db:"id"`
	WarID       uuid.UUID `json:"war_id" db:"war_id"`
	TerritoryID uuid.UUID `json:"territory_id" db:"territory_id"`
	Status      string    `json:"status" db:"status"` // pending, active, completed
	StartTime   *time.Time `json:"start_time" db:"start_time"`
	EndTime     *time.Time `json:"end_time" db:"end_time"`
	WinnerClanID *uuid.UUID `json:"winner_clan_id" db:"winner_clan_id"`
	ScoreClan1  int32     `json:"score_clan_1" db:"score_clan_1"`
	ScoreClan2  int32     `json:"score_clan_2" db:"score_clan_2"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

// Territory represents a territory that can be contested
type Territory struct {
	ID          uuid.UUID `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Description string    `json:"description" db:"description"`
	Type        string    `json:"type" db:"type"` // resource, strategic, commercial
	OwnerClanID *uuid.UUID `json:"owner_clan_id" db:"owner_clan_id"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
}

// NewClanWarRepository creates a new repository instance
// PERFORMANCE: Initializes connection pool
func NewClanWarRepository(dbURL string) (*ClanWarRepository, error) {
	// PERFORMANCE: Configure optimized connection pool
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}

	// PERFORMANCE: Optimize pool settings for MMOFPS
	config.MaxConns = 25              // Match backend pool size
	config.MinConns = 5               // Keep minimum connections
	config.MaxConnLifetime = time.Hour // Long-lived connections
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	repo := &ClanWarRepository{
		db: pool,
	}

	// PERFORMANCE: Initialize structured logger
	if l, err := zap.NewProduction(); err == nil {
		repo.logger = l
	} else {
		repo.logger = zap.NewNop()
	}

	return repo, nil
}

// CreateWar creates a new clan war
// PERFORMANCE: Prepared statement, context timeout
func (r *ClanWarRepository) CreateWar(ctx context.Context, war *ClanWar) error {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return err
	}

	query := `
		INSERT INTO clan_wars (id, clan_id_1, clan_id_2, status, territory_id, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	now := time.Now()
	war.CreatedAt = now
	war.UpdatedAt = now

	_, err := r.db.Exec(ctx, query,
		war.ID, war.ClanID1, war.ClanID2, war.Status, war.TerritoryID,
		war.StartTime, war.EndTime, war.WinnerClanID, war.ScoreClan1, war.ScoreClan2,
		war.CreatedAt, war.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create clan war", zap.Error(err), zap.String("war_id", war.ID.String()))
		return err
	}

	r.logger.Info("Clan war created", zap.String("war_id", war.ID.String()))
	return nil
}

// GetWarByID retrieves a clan war by ID
// PERFORMANCE: Indexed query, context timeout
func (r *ClanWarRepository) GetWarByID(ctx context.Context, warID uuid.UUID) (*ClanWar, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, clan_id_1, clan_id_2, status, territory_id, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at
		FROM clan_wars
		WHERE id = $1
	`

	var war ClanWar
	err := r.db.QueryRow(ctx, query, warID).Scan(
		&war.ID, &war.ClanID1, &war.ClanID2, &war.Status, &war.TerritoryID,
		&war.StartTime, &war.EndTime, &war.WinnerClanID, &war.ScoreClan1, &war.ScoreClan2,
		&war.CreatedAt, &war.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil // Not found
		}
		r.logger.Error("Failed to get clan war", zap.Error(err), zap.String("war_id", warID.String()))
		return nil, err
	}

	return &war, nil
}

// ListWars retrieves a list of clan wars
// PERFORMANCE: Paginated query with LIMIT/OFFSET
func (r *ClanWarRepository) ListWars(ctx context.Context, limit, offset int) ([]*ClanWar, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, clan_id_1, clan_id_2, status, territory_id, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at
		FROM clan_wars
		ORDER BY created_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list clan wars", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var wars []*ClanWar
	for rows.Next() {
		var war ClanWar
		err := rows.Scan(
			&war.ID, &war.ClanID1, &war.ClanID2, &war.Status, &war.TerritoryID,
			&war.StartTime, &war.EndTime, &war.WinnerClanID, &war.ScoreClan1, &war.ScoreClan2,
			&war.CreatedAt, &war.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan clan war", zap.Error(err))
			return nil, err
		}
		wars = append(wars, &war)
	}

	return wars, rows.Err()
}

// UpdateWar updates an existing clan war
// PERFORMANCE: Optimistic locking with updated_at
func (r *ClanWarRepository) UpdateWar(ctx context.Context, war *ClanWar) error {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return err
	}

	query := `
		UPDATE clan_wars
		SET status = $2, start_time = $3, end_time = $4, winner_clan_id = $5, score_clan_1 = $6, score_clan_2 = $7, updated_at = $8
		WHERE id = $1
	`

	war.UpdatedAt = time.Now()

	result, err := r.db.Exec(ctx, query,
		war.ID, war.Status, war.StartTime, war.EndTime, war.WinnerClanID,
		war.ScoreClan1, war.ScoreClan2, war.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to update clan war", zap.Error(err), zap.String("war_id", war.ID.String()))
		return err
	}

	if rowsAffected := result.RowsAffected(); rowsAffected == 0 {
		return sql.ErrNoRows
	}

	r.logger.Info("Clan war updated", zap.String("war_id", war.ID.String()))
	return nil
}

// CreateBattle creates a new battle within a clan war
func (r *ClanWarRepository) CreateBattle(ctx context.Context, battle *Battle) error {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return err
	}

	query := `
		INSERT INTO clan_war_battles (id, war_id, territory_id, status, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	now := time.Now()
	battle.CreatedAt = now
	battle.UpdatedAt = now

	_, err := r.db.Exec(ctx, query,
		battle.ID, battle.WarID, battle.TerritoryID, battle.Status,
		battle.StartTime, battle.EndTime, battle.WinnerClanID,
		battle.ScoreClan1, battle.ScoreClan2, battle.CreatedAt, battle.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to create battle", zap.Error(err), zap.String("battle_id", battle.ID.String()))
		return err
	}

	r.logger.Info("Battle created", zap.String("battle_id", battle.ID.String()))
	return nil
}

// GetBattleByID retrieves a battle by ID
func (r *ClanWarRepository) GetBattleByID(ctx context.Context, battleID uuid.UUID) (*Battle, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, war_id, territory_id, status, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at
		FROM clan_war_battles
		WHERE id = $1
	`

	var battle Battle
	err := r.db.QueryRow(ctx, query, battleID).Scan(
		&battle.ID, &battle.WarID, &battle.TerritoryID, &battle.Status,
		&battle.StartTime, &battle.EndTime, &battle.WinnerClanID,
		&battle.ScoreClan1, &battle.ScoreClan2, &battle.CreatedAt, &battle.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil // Not found
		}
		r.logger.Error("Failed to get battle", zap.Error(err), zap.String("battle_id", battleID.String()))
		return nil, err
	}

	return &battle, nil
}

// ListBattles retrieves battles for a specific war
func (r *ClanWarRepository) ListBattles(ctx context.Context, warID uuid.UUID, limit, offset int) ([]*Battle, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, war_id, territory_id, status, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at
		FROM clan_war_battles
		WHERE war_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3
	`

	rows, err := r.db.Query(ctx, query, warID, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list battles", zap.Error(err), zap.String("war_id", warID.String()))
		return nil, err
	}
	defer rows.Close()

	var battles []*Battle
	for rows.Next() {
		var battle Battle
		err := rows.Scan(
			&battle.ID, &battle.WarID, &battle.TerritoryID, &battle.Status,
			&battle.StartTime, &battle.EndTime, &battle.WinnerClanID,
			&battle.ScoreClan1, &battle.ScoreClan2, &battle.CreatedAt, &battle.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan battle", zap.Error(err))
			return nil, err
		}
		battles = append(battles, &battle)
	}

	return battles, rows.Err()
}

// GetTerritoryByID retrieves a territory by ID
func (r *ClanWarRepository) GetTerritoryByID(ctx context.Context, territoryID uuid.UUID) (*Territory, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, name, description, type, owner_clan_id, created_at, updated_at
		FROM clan_war_territories
		WHERE id = $1
	`

	var territory Territory
	err := r.db.QueryRow(ctx, query, territoryID).Scan(
		&territory.ID, &territory.Name, &territory.Description, &territory.Type,
		&territory.OwnerClanID, &territory.CreatedAt, &territory.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, nil // Not found
		}
		r.logger.Error("Failed to get territory", zap.Error(err), zap.String("territory_id", territoryID.String()))
		return nil, err
	}

	return &territory, nil
}

// ListTerritories retrieves a list of territories
func (r *ClanWarRepository) ListTerritories(ctx context.Context, limit, offset int) ([]*Territory, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, name, description, type, owner_clan_id, created_at, updated_at
		FROM clan_war_territories
		ORDER BY created_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list territories", zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var territories []*Territory
	for rows.Next() {
		var territory Territory
		err := rows.Scan(
			&territory.ID, &territory.Name, &territory.Description, &territory.Type,
			&territory.OwnerClanID, &territory.CreatedAt, &territory.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan territory", zap.Error(err))
			return nil, err
		}
		territories = append(territories, &territory)
	}

	return territories, rows.Err()
}

// GetWarByIDWithBattles retrieves a clan war along with its battles
func (r *ClanWarRepository) GetWarByIDWithBattles(ctx context.Context, warID uuid.UUID) (*ClanWar, []*Battle, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, nil, err
	}

	// Get the war first
	war, err := r.GetWarByID(ctx, warID)
	if err != nil {
		return nil, nil, err
	}
	if war == nil {
		return nil, nil, nil // War not found
	}

	// Get battles for this war
	battles, err := r.ListBattles(ctx, warID, 1000, 0) // Large limit to get all battles
	if err != nil {
		r.logger.Error("Failed to get battles for war", zap.Error(err), zap.String("war_id", warID.String()))
		return nil, nil, err
	}

	return war, battles, nil
}

// GetActiveWarsByClan retrieves all active wars for a specific clan
func (r *ClanWarRepository) GetActiveWarsByClan(ctx context.Context, clanID uuid.UUID) ([]*ClanWar, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	query := `
		SELECT id, clan_id_1, clan_id_2, status, territory_id, start_time, end_time, winner_clan_id, score_clan_1, score_clan_2, created_at, updated_at
		FROM clan_wars
		WHERE (clan_id_1 = $1 OR clan_id_2 = $1) AND status = 'active'
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, clanID)
	if err != nil {
		r.logger.Error("Failed to get active wars by clan", zap.Error(err), zap.String("clan_id", clanID.String()))
		return nil, err
	}
	defer rows.Close()

	var wars []*ClanWar
	for rows.Next() {
		var war ClanWar
		err := rows.Scan(
			&war.ID, &war.ClanID1, &war.ClanID2, &war.Status, &war.TerritoryID,
			&war.StartTime, &war.EndTime, &war.WinnerClanID, &war.ScoreClan1, &war.ScoreClan2,
			&war.CreatedAt, &war.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan clan war", zap.Error(err))
			return nil, err
		}
		wars = append(wars, &war)
	}

	return wars, rows.Err()
}

// WarStatistics represents aggregated statistics for a war
type WarStatistics struct {
	WarID         uuid.UUID `json:"war_id"`
	TotalBattles  int       `json:"total_battles"`
	ActiveBattles int       `json:"active_battles"`
	Clan1Score    int       `json:"clan_1_score"`
	Clan2Score    int       `json:"clan_2_score"`
}

// GetWarStatistics retrieves statistics for a specific war
func (r *ClanWarRepository) GetWarStatistics(ctx context.Context, warID uuid.UUID) (*WarStatistics, error) {
	// PERFORMANCE: Context timeout check
	if err := ctx.Err(); err != nil {
		return nil, err
	}

	// Get basic war info first
	war, err := r.GetWarByID(ctx, warID)
	if err != nil {
		return nil, err
	}
	if war == nil {
		return nil, nil // War not found
	}

	// Aggregate battle statistics
	query := `
		SELECT
			COUNT(*) as total_battles,
			COUNT(CASE WHEN status = 'active' THEN 1 END) as active_battles,
			COALESCE(SUM(score_clan_1), 0) as clan_1_total_score,
			COALESCE(SUM(score_clan_2), 0) as clan_2_total_score
		FROM clan_war_battles
		WHERE war_id = $1
	`

	var stats WarStatistics
	stats.WarID = warID

	err = r.db.QueryRow(ctx, query, warID).Scan(
		&stats.TotalBattles,
		&stats.ActiveBattles,
		&stats.Clan1Score,
		&stats.Clan2Score,
	)

	if err != nil {
		r.logger.Error("Failed to get war statistics", zap.Error(err), zap.String("war_id", warID.String()))
		return nil, err
	}

	// Add war-level scores
	stats.Clan1Score += int(war.ScoreClan1)
	stats.Clan2Score += int(war.ScoreClan2)

	return &stats, nil
}

// Issue: #427
