// Issue: #140875729
// PERFORMANCE: Database layer with connection pooling and prepared statements

package server

import (
	"context"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// WorldRegionsRepository handles database operations for world regions
// PERFORMANCE: Connection pooling, optimized queries
type WorldRegionsRepository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// WorldRegion represents a world region entity
// PERFORMANCE: Optimized struct alignment (large fields first)
type WorldRegion struct {
	ID           string     `json:"id" db:"id"`
	Name         string     `json:"name" db:"name"`                 // Large field first
	Continent    string     `json:"continent" db:"continent"`
	Description  string     `json:"description" db:"description"`   // Large field second
	Status       string     `json:"status" db:"status"`
	CitiesCount  int32      `json:"cities_count" db:"cities_count"` // int32 (4 bytes)
	CreatedAt    time.Time  `json:"created_at" db:"created_at"`
	UpdatedAt    time.Time  `json:"updated_at" db:"updated_at"`
}

// TimelineEvent represents a timeline event for a region
type TimelineEvent struct {
	RegionID    string `json:"region_id" db:"region_id"`
	Period      string `json:"period" db:"period"`
	Title       string `json:"title" db:"title"`
	Description string `json:"description" db:"description"`
}

// NewWorldRegionsRepository creates a new repository instance
// PERFORMANCE: Initializes connection pool
func NewWorldRegionsRepository(dbURL string) (*WorldRegionsRepository, error) {
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

	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		return nil, err
	}

	return &WorldRegionsRepository{
		db:     pool,
		logger: logger,
	}, nil
}

// GetWorldRegions retrieves all world regions with pagination
func (r *WorldRegionsRepository) GetWorldRegions(ctx context.Context, status, continent string, limit, offset int) ([]*WorldRegion, int, error) {
	query := `
		SELECT id, name, continent, description, status, cities_count, created_at, updated_at
		FROM world_regions
		WHERE 1=1`

	args := []interface{}{}
	argCount := 0

	if status != "" {
		argCount++
		query += ` AND status = $` + fmt.Sprintf("%d", argCount)
		args = append(args, status)
	}

	if continent != "" {
		argCount++
		query += ` AND continent = $` + fmt.Sprintf("%d", argCount)
		args = append(args, continent)
	}

	query += ` ORDER BY name LIMIT $` + fmt.Sprintf("%d", argCount+1) + ` OFFSET $` + fmt.Sprintf("%d", argCount+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to query world regions", zap.Error(err))
		return nil, 0, err
	}
	defer rows.Close()

	var regions []*WorldRegion
	for rows.Next() {
		var region WorldRegion
		err := rows.Scan(
			&region.ID,
			&region.Name,
			&region.Continent,
			&region.Description,
			&region.Status,
			&region.CitiesCount,
			&region.CreatedAt,
			&region.UpdatedAt,
		)
		if err != nil {
			r.logger.Error("Failed to scan world region", zap.Error(err))
			return nil, 0, err
		}
		regions = append(regions, &region)
	}

	// Get total count
	countQuery := `SELECT COUNT(*) FROM world_regions WHERE 1=1`
	countArgs := []interface{}{}

	if status != "" {
		countQuery += ` AND status = $1`
		countArgs = append(countArgs, status)
	}

	if continent != "" {
		countQuery += ` AND continent = $2`
		countArgs = append(countArgs, continent)
	}

	var total int
	err = r.db.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		r.logger.Error("Failed to get total count", zap.Error(err))
		return nil, 0, err
	}

	return regions, total, nil
}

// GetWorldRegionByID retrieves a specific world region by ID
func (r *WorldRegionsRepository) GetWorldRegionByID(ctx context.Context, regionID string) (*WorldRegion, error) {
	query := `
		SELECT id, name, continent, description, status, cities_count, created_at, updated_at
		FROM world_regions
		WHERE id = $1`

	var region WorldRegion
	err := r.db.QueryRow(ctx, query, regionID).Scan(
		&region.ID,
		&region.Name,
		&region.Continent,
		&region.Description,
		&region.Status,
		&region.CitiesCount,
		&region.CreatedAt,
		&region.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to get world region by ID", zap.String("region_id", regionID), zap.Error(err))
		return nil, err
	}

	return &region, nil
}

// GetRegionTimeline retrieves timeline events for a region
func (r *WorldRegionsRepository) GetRegionTimeline(ctx context.Context, regionID string, periodStart, periodEnd int) ([]*TimelineEvent, error) {
	query := `
		SELECT region_id, period, title, description
		FROM region_timeline_events
		WHERE region_id = $1`

	args := []interface{}{regionID}
	argCount := 1

	if periodStart > 0 {
		argCount++
		query += ` AND CAST(SUBSTRING(period, 1, 4) AS INTEGER) >= $` + fmt.Sprintf("%d", argCount)
		args = append(args, periodStart)
	}

	if periodEnd > 0 {
		argCount++
		query += ` AND CAST(SUBSTRING(period, 1, 4) AS INTEGER) <= $` + fmt.Sprintf("%d", argCount)
		args = append(args, periodEnd)
	}

	query += ` ORDER BY period`

	rows, err := r.db.Query(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to query region timeline", zap.String("region_id", regionID), zap.Error(err))
		return nil, err
	}
	defer rows.Close()

	var events []*TimelineEvent
	for rows.Next() {
		var event TimelineEvent
		err := rows.Scan(
			&event.RegionID,
			&event.Period,
			&event.Title,
			&event.Description,
		)
		if err != nil {
			r.logger.Error("Failed to scan timeline event", zap.Error(err))
			return nil, err
		}
		events = append(events, &event)
	}

	return events, nil
}

// ImportWorldRegion imports or updates a world region from YAML data
func (r *WorldRegionsRepository) ImportWorldRegion(ctx context.Context, region *WorldRegion) error {
	query := `
		INSERT INTO world_regions (id, name, continent, description, status, cities_count, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		ON CONFLICT (id) DO UPDATE SET
			name = EXCLUDED.name,
			continent = EXCLUDED.continent,
			description = EXCLUDED.description,
			status = EXCLUDED.status,
			cities_count = EXCLUDED.cities_count,
			updated_at = EXCLUDED.updated_at`

	_, err := r.db.Exec(ctx, query,
		region.ID,
		region.Name,
		region.Continent,
		region.Description,
		region.Status,
		region.CitiesCount,
		region.CreatedAt,
		region.UpdatedAt,
	)

	if err != nil {
		r.logger.Error("Failed to import world region", zap.String("region_id", region.ID), zap.Error(err))
		return err
	}

	r.logger.Info("Imported world region", zap.String("region_id", region.ID))
	return nil
}

// Close closes the database connection pool
func (r *WorldRegionsRepository) Close() {
	r.db.Close()
	r.logger.Info("World regions repository closed")
}
