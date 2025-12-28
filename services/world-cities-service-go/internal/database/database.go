package database

import (
	"context"
	"database/sql"
	"fmt"
	"os"
	"strings"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// DatabaseConfig represents database configuration
type DatabaseConfig struct {
	Host     string
	Port     string
	User     string
	Password string
	Database string
	SSLMode  string
}

// LoadDatabaseConfig loads database configuration from environment
func LoadDatabaseConfig() *DatabaseConfig {
	return &DatabaseConfig{
		Host:     getEnv("DB_HOST", "localhost"),
		Port:     getEnv("DB_PORT", "5432"),
		User:     getEnv("DB_USER", "postgres"),
		Password: getEnv("DB_PASSWORD", "password"),
		Database: getEnv("DB_NAME", "worldcities"),
		SSLMode:  getEnv("DB_SSLMODE", "disable"),
	}
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// Database handles database operations for world cities
type Database struct {
	pool   *pgxpool.Pool
	logger *zap.Logger
}

// NewDatabaseConnection creates a new database connection pool
func NewDatabaseConnection(cfg *DatabaseConfig) (*pgxpool.Pool, error) {
	connString := fmt.Sprintf(
		"postgres://%s:%s@%s:%s/%s?sslmode=%s",
		cfg.User, cfg.Password, cfg.Host, cfg.Port, cfg.Database, cfg.SSLMode,
	)

	config, err := pgxpool.ParseConfig(connString)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database config: %w", err)
	}

	// Configure connection pool
	config.MaxConns = 10
	config.MinConns = 2

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	return pool, nil
}

// NewDatabase creates a new database instance
func NewDatabase(pool *pgxpool.Pool, logger *zap.Logger) *Database {
	return &Database{
		pool:   pool,
		logger: logger,
	}
}

// RunMigrations runs database migrations
func RunMigrations(pool *pgxpool.Pool, logger *zap.Logger) error {
	// Create cities table if it doesn't exist
	createTableQuery := `
		CREATE TABLE IF NOT EXISTS cities (
			id UUID PRIMARY KEY,
			city_id VARCHAR(255) UNIQUE NOT NULL,
			name VARCHAR(255) NOT NULL,
			name_local VARCHAR(255),
			country VARCHAR(255) NOT NULL,
			continent VARCHAR(255) NOT NULL,
			latitude DECIMAL(10,8) NOT NULL,
			longitude DECIMAL(11,8) NOT NULL,
			population_2020 INTEGER,
			population_2050 INTEGER,
			population_2093 INTEGER,
			area_km2 DECIMAL(15,2),
			elevation_m INTEGER,
			cyberpunk_level INTEGER CHECK (cyberpunk_level >= 0 AND cyberpunk_level <= 100),
			corruption_index DECIMAL(5,2),
			technology_index DECIMAL(5,2),
			zones JSONB,
			districts JSONB,
			landmarks JSONB,
			economy_data JSONB,
			corporation_presence JSONB,
			faction_influence JSONB,
			timeline_events JSONB,
			future_evolution JSONB,
			status VARCHAR(50) DEFAULT 'active',
			is_capital BOOLEAN DEFAULT FALSE,
			is_megacity BOOLEAN DEFAULT FALSE,
			available_in_game BOOLEAN DEFAULT TRUE,
			game_regions JSONB,
			source_file VARCHAR(500),
			version VARCHAR(50) DEFAULT '1.0',
			created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
			updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
		);

		-- Create indexes for performance
		CREATE INDEX IF NOT EXISTS idx_cities_city_id ON cities(city_id);
		CREATE INDEX IF NOT EXISTS idx_cities_country ON cities(country);
		CREATE INDEX IF NOT EXISTS idx_cities_continent ON cities(continent);
		CREATE INDEX IF NOT EXISTS idx_cities_location ON cities USING gist (ST_Point(longitude, latitude));
		CREATE INDEX IF NOT EXISTS idx_cities_cyberpunk_level ON cities(cyberpunk_level);
		CREATE INDEX IF NOT EXISTS idx_cities_is_megacity ON cities(is_megacity);
		CREATE INDEX IF NOT EXISTS idx_cities_status ON cities(status);
		CREATE INDEX IF NOT EXISTS idx_cities_available_in_game ON cities(available_in_game);
		CREATE INDEX IF NOT EXISTS idx_cities_created_at ON cities(created_at);

		-- Create PostGIS extension if not exists (for spatial queries)
		CREATE EXTENSION IF NOT EXISTS postgis;
	`

	ctx := context.Background()
	_, err := pool.Exec(ctx, createTableQuery)
	if err != nil {
		logger.Error("Failed to run migrations", zap.Error(err))
		return fmt.Errorf("failed to run migrations: %w", err)
	}

	logger.Info("Database migrations completed successfully")
	return nil
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

// CreateCity creates a new city in the database
func (d *Database) CreateCity(ctx context.Context, city *City) (*City, error) {
	query := `
		INSERT INTO cities (
			id, city_id, name, name_local, country, continent, latitude, longitude,
			population_2020, population_2050, population_2093, area_km2, elevation_m,
			cyberpunk_level, corruption_index, technology_index, zones, districts,
			landmarks, economy_data, corporation_presence, faction_influence,
			timeline_events, future_evolution, status, is_capital, is_megacity,
			available_in_game, game_regions, source_file, version, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16,
			$17, $18, $19, $20, $21, $22, $23, $24, $25, $26, $27, $28, $29, $30, $31, $32
		) RETURNING id, city_id, name, name_local, country, continent, latitude, longitude,
			population_2020, population_2050, population_2093, area_km2, elevation_m,
			cyberpunk_level, corruption_index, technology_index, zones, districts,
			landmarks, economy_data, corporation_presence, faction_influence,
			timeline_events, future_evolution, status, is_capital, is_megacity,
			available_in_game, game_regions, source_file, version, created_at, updated_at
	`

	var created City
	err := d.pool.QueryRow(ctx, query,
		city.ID, city.CityID, city.Name, city.NameLocal, city.Country, city.Continent,
		city.Latitude, city.Longitude, city.Population2020, city.Population2050,
		city.Population2093, city.AreaKm2, city.ElevationM, city.CyberpunkLevel,
		city.CorruptionIndex, city.TechnologyIndex, city.Zones, city.Districts,
		city.Landmarks, city.EconomyData, city.CorporationPresence, city.FactionInfluence,
		city.TimelineEvents, city.FutureEvolution, city.Status, city.IsCapital,
		city.IsMegacity, city.AvailableInGame, city.GameRegions, city.SourceFile,
		city.Version, city.CreatedAt, city.UpdatedAt,
	).Scan(
		&created.ID, &created.CityID, &created.Name, &created.NameLocal, &created.Country, &created.Continent,
		&created.Latitude, &created.Longitude, &created.Population2020, &created.Population2050,
		&created.Population2093, &created.AreaKm2, &created.ElevationM, &created.CyberpunkLevel,
		&created.CorruptionIndex, &created.TechnologyIndex, &created.Zones, &created.Districts,
		&created.Landmarks, &created.EconomyData, &created.CorporationPresence, &created.FactionInfluence,
		&created.TimelineEvents, &created.FutureEvolution, &created.Status, &created.IsCapital,
		&created.IsMegacity, &created.AvailableInGame, &created.GameRegions, &created.SourceFile,
		&created.Version, &created.CreatedAt, &created.UpdatedAt,
	)

	if err != nil {
		d.logger.Error("Failed to create city", zap.Error(err))
		return nil, fmt.Errorf("failed to create city: %w", err)
	}

	return &created, nil
}

// UpdateCity updates an existing city in the database
func (d *Database) UpdateCity(ctx context.Context, id uuid.UUID, city *City) (*City, error) {
	query := `
		UPDATE cities SET
			city_id = $2, name = $3, name_local = $4, country = $5, continent = $6,
			latitude = $7, longitude = $8, population_2020 = $9, population_2050 = $10,
			population_2093 = $11, area_km2 = $12, elevation_m = $13, cyberpunk_level = $14,
			corruption_index = $15, technology_index = $16, zones = $17, districts = $18,
			landmarks = $19, economy_data = $20, corporation_presence = $21, faction_influence = $22,
			timeline_events = $23, future_evolution = $24, status = $25, is_capital = $26,
			is_megacity = $27, available_in_game = $28, game_regions = $29, source_file = $30,
			version = $31, updated_at = $32
		WHERE id = $1
		RETURNING id, city_id, name, name_local, country, continent, latitude, longitude,
			population_2020, population_2050, population_2093, area_km2, elevation_m,
			cyberpunk_level, corruption_index, technology_index, zones, districts,
			landmarks, economy_data, corporation_presence, faction_influence,
			timeline_events, future_evolution, status, is_capital, is_megacity,
			available_in_game, game_regions, source_file, version, created_at, updated_at
	`

	var updated City
	err := d.pool.QueryRow(ctx, query,
		id, city.CityID, city.Name, city.NameLocal, city.Country, city.Continent,
		city.Latitude, city.Longitude, city.Population2020, city.Population2050,
		city.Population2093, city.AreaKm2, city.ElevationM, city.CyberpunkLevel,
		city.CorruptionIndex, city.TechnologyIndex, city.Zones, city.Districts,
		city.Landmarks, city.EconomyData, city.CorporationPresence, city.FactionInfluence,
		city.TimelineEvents, city.FutureEvolution, city.Status, city.IsCapital,
		city.IsMegacity, city.AvailableInGame, city.GameRegions, city.SourceFile,
		city.Version, city.UpdatedAt,
	).Scan(
		&updated.ID, &updated.CityID, &updated.Name, &updated.NameLocal, &updated.Country, &updated.Continent,
		&updated.Latitude, &updated.Longitude, &updated.Population2020, &updated.Population2050,
		&updated.Population2093, &updated.AreaKm2, &updated.ElevationM, &updated.CyberpunkLevel,
		&updated.CorruptionIndex, &updated.TechnologyIndex, &updated.Zones, &updated.Districts,
		&updated.Landmarks, &updated.EconomyData, &updated.CorporationPresence, &updated.FactionInfluence,
		&updated.TimelineEvents, &updated.FutureEvolution, &updated.Status, &updated.IsCapital,
		&updated.IsMegacity, &updated.AvailableInGame, &updated.GameRegions, &updated.SourceFile,
		&updated.Version, &updated.CreatedAt, &updated.UpdatedAt,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("city not found")
		}
		d.logger.Error("Failed to update city", zap.Error(err))
		return nil, fmt.Errorf("failed to update city: %w", err)
	}

	return &updated, nil
}

// DeleteCity removes a city from the database
func (d *Database) DeleteCity(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM cities WHERE id = $1`

	result, err := d.pool.Exec(ctx, query, id)
	if err != nil {
		d.logger.Error("Failed to delete city", zap.Error(err))
		return fmt.Errorf("failed to delete city: %w", err)
	}

	rowsAffected := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("city not found")
	}

	return nil
}

