package database

import (
	"context"
	"database/sql"
	"fmt"
	"strings"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// Database handles database operations for world cities
type Database struct {
	pool   *pgxpool.Pool
	logger *zap.Logger
}

// NewDatabase creates a new database instance
func NewDatabase(pool *pgxpool.Pool, logger *zap.Logger) *Database {
	return &Database{
		pool:   pool,
		logger: logger,
	}
}

// ListCities retrieves cities with filtering and pagination
func (d *Database) ListCities(ctx context.Context, filter CityFilter, options CityListOptions) ([]City, int, error) {
	query := `
		SELECT id, city_id, name, name_local, country, continent, latitude, longitude,
			   population_2020, population_2050, population_2093, area_km2, elevation_m,
			   cyberpunk_level, corruption_index, technology_index, zones, districts,
			   landmarks, economy_data, corporation_presence, faction_influence,
			   timeline_events, future_evolution, status, is_capital, is_megacity,
			   available_in_game, game_regions, source_file, version, created_at, updated_at
		FROM cities
		WHERE 1=1
	`

	args := []interface{}{}
	argCount := 0

	// Add filters
	if filter.Continent != nil {
		argCount++
		query += fmt.Sprintf(" AND continent = $%d", argCount)
		args = append(args, *filter.Continent)
	}

	if filter.Country != nil {
		argCount++
		query += fmt.Sprintf(" AND country = $%d", argCount)
		args = append(args, *filter.Country)
	}

	if filter.CyberpunkLevelMin != nil {
		argCount++
		query += fmt.Sprintf(" AND cyberpunk_level >= $%d", argCount)
		args = append(args, *filter.CyberpunkLevelMin)
	}

	if filter.CyberpunkLevelMax != nil {
		argCount++
		query += fmt.Sprintf(" AND cyberpunk_level <= $%d", argCount)
		args = append(args, *filter.CyberpunkLevelMax)
	}

	if filter.IsMegacity != nil {
		argCount++
		query += fmt.Sprintf(" AND is_megacity = $%d", argCount)
		args = append(args, *filter.IsMegacity)
	}

	if filter.Status != nil {
		argCount++
		query += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, *filter.Status)
	}

	// Add spatial filter if coordinates provided
	if filter.Latitude != nil && filter.Longitude != nil && filter.RadiusKm != nil {
		argCount++
		query += fmt.Sprintf(` AND ST_DWithin(
			ST_Point(longitude, latitude)::geography,
			ST_Point($%d, $%d)::geography,
			$%d * 1000
		)`, argCount, argCount+1, argCount+2)
		args = append(args, *filter.Longitude, *filter.Latitude, *filter.RadiusKm)
		argCount += 2
	}

	// Add sorting
	orderBy := "name ASC"
	if options.SortBy != "" {
		validSortFields := map[string]bool{
			"name": true, "population_2020": true, "population_2050": true,
			"population_2093": true, "cyberpunk_level": true, "created_at": true,
		}

		if validSortFields[options.SortBy] {
			order := "ASC"
			if strings.ToLower(options.SortOrder) == "desc" {
				order = "DESC"
			}
			orderBy = fmt.Sprintf("%s %s", options.SortBy, order)
		}
	}
	query += " ORDER BY " + orderBy

	// Add pagination
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, options.Limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, options.Offset)

	// Execute query
	rows, err := d.pool.Query(ctx, query, args...)
	if err != nil {
		d.logger.Error("Failed to query cities", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to query cities: %w", err)
	}
	defer rows.Close()

	var cities []City
	for rows.Next() {
		var city City
		err := rows.Scan(
			&city.ID, &city.CityID, &city.Name, &city.NameLocal, &city.Country, &city.Continent,
			&city.Latitude, &city.Longitude, &city.Population2020, &city.Population2050,
			&city.Population2093, &city.AreaKm2, &city.ElevationM, &city.CyberpunkLevel,
			&city.CorruptionIndex, &city.TechnologyIndex, &city.Zones, &city.Districts,
			&city.Landmarks, &city.EconomyData, &city.CorporationPresence, &city.FactionInfluence,
			&city.TimelineEvents, &city.FutureEvolution, &city.Status, &city.IsCapital,
			&city.IsMegacity, &city.AvailableInGame, &city.GameRegions, &city.SourceFile,
			&city.Version, &city.CreatedAt, &city.UpdatedAt,
		)
		if err != nil {
			d.logger.Error("Failed to scan city row", zap.Error(err))
			return nil, 0, fmt.Errorf("failed to scan city row: %w", err)
		}
		cities = append(cities, city)
	}

	if err := rows.Err(); err != nil {
		d.logger.Error("Error iterating city rows", zap.Error(err))
		return nil, 0, fmt.Errorf("error iterating city rows: %w", err)
	}

	// Get total count
	countQuery := `
		SELECT COUNT(*)
		FROM cities
		WHERE 1=1
	`

	countArgs := []interface{}{}
	countArgCount := 0

	// Add same filters for count
	if filter.Continent != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND continent = $%d", countArgCount)
		countArgs = append(countArgs, *filter.Continent)
	}

	if filter.Country != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND country = $%d", countArgCount)
		countArgs = append(countArgs, *filter.Country)
	}

	if filter.CyberpunkLevelMin != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND cyberpunk_level >= $%d", countArgCount)
		countArgs = append(countArgs, *filter.CyberpunkLevelMin)
	}

	if filter.CyberpunkLevelMax != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND cyberpunk_level <= $%d", countArgCount)
		countArgs = append(countArgs, *filter.CyberpunkLevelMax)
	}

	if filter.IsMegacity != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND is_megacity = $%d", countArgCount)
		countArgs = append(countArgs, *filter.IsMegacity)
	}

	if filter.Status != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND status = $%d", countArgCount)
		countArgs = append(countArgs, *filter.Status)
	}

	var total int
	err = d.pool.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		d.logger.Error("Failed to get total count", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get total count: %w", err)
	}

	return cities, total, nil
}

// GetCity retrieves a city by city_id
func (d *Database) GetCity(ctx context.Context, cityID string) (*City, error) {
	query := `
		SELECT id, city_id, name, name_local, country, continent, latitude, longitude,
			   population_2020, population_2050, population_2093, area_km2, elevation_m,
			   cyberpunk_level, corruption_index, technology_index, zones, districts,
			   landmarks, economy_data, corporation_presence, faction_influence,
			   timeline_events, future_evolution, status, is_capital, is_megacity,
			   available_in_game, game_regions, source_file, version, created_at, updated_at
		FROM cities
		WHERE city_id = $1
	`

	var city City
	err := d.pool.QueryRow(ctx, query, cityID).Scan(
		&city.ID, &city.CityID, &city.Name, &city.NameLocal, &city.Country, &city.Continent,
		&city.Latitude, &city.Longitude, &city.Population2020, &city.Population2050,
		&city.Population2093, &city.AreaKm2, &city.ElevationM, &city.CyberpunkLevel,
		&city.CorruptionIndex, &city.TechnologyIndex, &city.Zones, &city.Districts,
		&city.Landmarks, &city.EconomyData, &city.CorporationPresence, &city.FactionInfluence,
		&city.TimelineEvents, &city.FutureEvolution, &city.Status, &city.IsCapital,
		&city.IsMegacity, &city.AvailableInGame, &city.GameRegions, &city.SourceFile,
		&city.Version, &city.CreatedAt, &city.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("city not found")
		}
		d.logger.Error("Failed to get city", zap.String("city_id", cityID), zap.Error(err))
		return nil, fmt.Errorf("failed to get city: %w", err)
	}

	return &city, nil
}

// GetCityByUUID retrieves a city by UUID
func (d *Database) GetCityByUUID(ctx context.Context, id uuid.UUID) (*City, error) {
	query := `
		SELECT id, city_id, name, name_local, country, continent, latitude, longitude,
			   population_2020, population_2050, population_2093, area_km2, elevation_m,
			   cyberpunk_level, corruption_index, technology_index, zones, districts,
			   landmarks, economy_data, corporation_presence, faction_influence,
			   timeline_events, future_evolution, status, is_capital, is_megacity,
			   available_in_game, game_regions, source_file, version, created_at, updated_at
		FROM cities
		WHERE id = $1
	`

	var city City
	err := d.pool.QueryRow(ctx, query, id).Scan(
		&city.ID, &city.CityID, &city.Name, &city.NameLocal, &city.Country, &city.Continent,
		&city.Latitude, &city.Longitude, &city.Population2020, &city.Population2050,
		&city.Population2093, &city.AreaKm2, &city.ElevationM, &city.CyberpunkLevel,
		&city.CorruptionIndex, &city.TechnologyIndex, &city.Zones, &city.Districts,
		&city.Landmarks, &city.EconomyData, &city.CorporationPresence, &city.FactionInfluence,
		&city.TimelineEvents, &city.FutureEvolution, &city.Status, &city.IsCapital,
		&city.IsMegacity, &city.AvailableInGame, &city.GameRegions, &city.SourceFile,
		&city.Version, &city.CreatedAt, &city.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("city not found")
		}
		d.logger.Error("Failed to get city by UUID", zap.String("id", id.String()), zap.Error(err))
		return nil, fmt.Errorf("failed to get city by UUID: %w", err)
	}

	return &city, nil
}

// SearchCities performs advanced city search
func (d *Database) SearchCities(ctx context.Context, searchQuery string, filter CityFilter, options CityListOptions) ([]City, int, error) {
	query := `
		SELECT id, city_id, name, name_local, country, continent, latitude, longitude,
			   population_2020, population_2050, population_2093, area_km2, elevation_m,
			   cyberpunk_level, corruption_index, technology_index, zones, districts,
			   landmarks, economy_data, corporation_presence, faction_influence,
			   timeline_events, future_evolution, status, is_capital, is_megacity,
			   available_in_game, game_regions, source_file, version, created_at, updated_at
		FROM cities
		WHERE (name ILIKE $1 OR name_local ILIKE $1 OR country ILIKE $1 OR city_id ILIKE $1)
	`

	args := []interface{}{"%" + searchQuery + "%"}
	argCount := 1

	// Add same filters as in ListCities
	if filter.Continent != nil {
		argCount++
		query += fmt.Sprintf(" AND continent = $%d", argCount)
		args = append(args, *filter.Continent)
	}

	if filter.Country != nil {
		argCount++
		query += fmt.Sprintf(" AND country = $%d", argCount)
		args = append(args, *filter.Country)
	}

	if filter.CyberpunkLevelMin != nil {
		argCount++
		query += fmt.Sprintf(" AND cyberpunk_level >= $%d", argCount)
		args = append(args, *filter.CyberpunkLevelMin)
	}

	if filter.CyberpunkLevelMax != nil {
		argCount++
		query += fmt.Sprintf(" AND cyberpunk_level <= $%d", argCount)
		args = append(args, *filter.CyberpunkLevelMax)
	}

	if filter.IsMegacity != nil {
		argCount++
		query += fmt.Sprintf(" AND is_megacity = $%d", argCount)
		args = append(args, *filter.IsMegacity)
	}

	if filter.Status != nil {
		argCount++
		query += fmt.Sprintf(" AND status = $%d", argCount)
		args = append(args, *filter.Status)
	}

	// Add sorting
	orderBy := "name ASC"
	if options.SortBy != "" {
		validSortFields := map[string]bool{
			"name": true, "population_2020": true, "population_2050": true,
			"population_2093": true, "cyberpunk_level": true, "created_at": true,
		}

		if validSortFields[options.SortBy] {
			order := "ASC"
			if strings.ToLower(options.SortOrder) == "desc" {
				order = "DESC"
			}
			orderBy = fmt.Sprintf("%s %s", options.SortBy, order)
		}
	}
	query += " ORDER BY " + orderBy

	// Add pagination
	argCount++
	query += fmt.Sprintf(" LIMIT $%d", argCount)
	args = append(args, options.Limit)

	argCount++
	query += fmt.Sprintf(" OFFSET $%d", argCount)
	args = append(args, options.Offset)

	// Execute query
	rows, err := d.pool.Query(ctx, query, args...)
	if err != nil {
		d.logger.Error("Failed to search cities", zap.String("query", searchQuery), zap.Error(err))
		return nil, 0, fmt.Errorf("failed to search cities: %w", err)
	}
	defer rows.Close()

	var cities []City
	for rows.Next() {
		var city City
		err := rows.Scan(
			&city.ID, &city.CityID, &city.Name, &city.NameLocal, &city.Country, &city.Continent,
			&city.Latitude, &city.Longitude, &city.Population2020, &city.Population2050,
			&city.Population2093, &city.AreaKm2, &city.ElevationM, &city.CyberpunkLevel,
			&city.CorruptionIndex, &city.TechnologyIndex, &city.Zones, &city.Districts,
			&city.Landmarks, &city.EconomyData, &city.CorporationPresence, &city.FactionInfluence,
			&city.TimelineEvents, &city.FutureEvolution, &city.Status, &city.IsCapital,
			&city.IsMegacity, &city.AvailableInGame, &city.GameRegions, &city.SourceFile,
			&city.Version, &city.CreatedAt, &city.UpdatedAt,
		)
		if err != nil {
			d.logger.Error("Failed to scan city row", zap.Error(err))
			return nil, 0, fmt.Errorf("failed to scan city row: %w", err)
		}
		cities = append(cities, city)
	}

	if err := rows.Err(); err != nil {
		d.logger.Error("Error iterating city rows", zap.Error(err))
		return nil, 0, fmt.Errorf("error iterating city rows: %w", err)
	}

	// Get total count for search
	countQuery := `
		SELECT COUNT(*)
		FROM cities
		WHERE (name ILIKE $1 OR name_local ILIKE $1 OR country ILIKE $1 OR city_id ILIKE $1)
	`

	countArgs := []interface{}{"%" + searchQuery + "%"}
	countArgCount := 1

	// Add same filters for count
	if filter.Continent != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND continent = $%d", countArgCount)
		countArgs = append(countArgs, *filter.Continent)
	}

	if filter.Country != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND country = $%d", countArgCount)
		countArgs = append(countArgs, *filter.Country)
	}

	if filter.CyberpunkLevelMin != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND cyberpunk_level >= $%d", countArgCount)
		countArgs = append(countArgs, *filter.CyberpunkLevelMin)
	}

	if filter.CyberpunkLevelMax != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND cyberpunk_level <= $%d", countArgCount)
		countArgs = append(countArgs, *filter.CyberpunkLevelMax)
	}

	if filter.IsMegacity != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND is_megacity = $%d", countArgCount)
		countArgs = append(countArgs, *filter.IsMegacity)
	}

	if filter.Status != nil {
		countArgCount++
		countQuery += fmt.Sprintf(" AND status = $%d", countArgCount)
		countArgs = append(countArgs, *filter.Status)
	}

	var total int
	err = d.pool.QueryRow(ctx, countQuery, countArgs...).Scan(&total)
	if err != nil {
		d.logger.Error("Failed to get search total count", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get search total count: %w", err)
	}

	return cities, total, nil
}

// GetCitiesAnalytics retrieves city analytics
func (d *Database) GetCitiesAnalytics(ctx context.Context) (map[string]interface{}, error) {
	query := `
		SELECT
			COUNT(*) as total_cities,
			COUNT(CASE WHEN is_megacity THEN 1 END) as megacities,
			COUNT(CASE WHEN is_capital THEN 1 END) as capitals,
			AVG(cyberpunk_level) as avg_cyberpunk_level,
			MAX(population_2093) as max_future_population,
			COUNT(CASE WHEN available_in_game THEN 1 END) as available_in_game
		FROM cities
	`

	var analytics struct {
		TotalCities         int     `json:"total_cities"`
		Megacities          int     `json:"megacities"`
		Capitals            int     `json:"capitals"`
		AvgCyberpunkLevel   float64 `json:"avg_cyberpunk_level"`
		MaxFuturePopulation int     `json:"max_future_population"`
		AvailableInGame     int     `json:"available_in_game"`
	}

	err := d.pool.QueryRow(ctx, query).Scan(
		&analytics.TotalCities,
		&analytics.Megacities,
		&analytics.Capitals,
		&analytics.AvgCyberpunkLevel,
		&analytics.MaxFuturePopulation,
		&analytics.AvailableInGame,
	)

	if err != nil {
		d.logger.Error("Failed to get cities analytics", zap.Error(err))
		return nil, fmt.Errorf("failed to get cities analytics: %w", err)
	}

	// Convert to map for JSON response
	result := map[string]interface{}{
		"total_cities":           analytics.TotalCities,
		"megacities":             analytics.Megacities,
		"capitals":               analytics.Capitals,
		"avg_cyberpunk_level":    analytics.AvgCyberpunkLevel,
		"max_future_population":  analytics.MaxFuturePopulation,
		"available_in_game":      analytics.AvailableInGame,
	}

	return result, nil
}

