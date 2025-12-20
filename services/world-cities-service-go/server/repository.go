// Package server Issue: #140875381
package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"strings"

	"go.uber.org/zap"
)

// WorldCitiesRepository предоставляет доступ к данным городов мира
type WorldCitiesRepository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewWorldCitiesRepository создает новый репозиторий городов мира
func NewWorldCitiesRepository(db *sql.DB, logger *zap.Logger) *WorldCitiesRepository {
	return &WorldCitiesRepository{
		db:     db,
		logger: logger,
	}
}

// GetCities возвращает города с фильтрацией и пагинацией
func (r *WorldCitiesRepository) GetCities(ctx context.Context, filters map[string]interface{}, limit, offset int) ([]*City, int, error) {
	baseQuery := `
		SELECT id, name, country, region, latitude, longitude, population,
		       development_level, description, landmarks, economic_data,
		       social_data, timeline_year, is_active, created_at, updated_at
		FROM world_cities.cities
		WHERE is_active = true
	`

	var args []interface{}

	// Применяем фильтры
	if region, ok := filters["region"].(string); ok && region != "" {
		baseQuery += fmt.Sprintf(" AND region = $%d", len(args)+1)
		args = append(args, region)
	}

	if country, ok := filters["country"].(string); ok && country != "" {
		baseQuery += fmt.Sprintf(" AND country = $%d", len(args)+1)
		args = append(args, country)
	}

	if minPop, ok := filters["min_population"].(int); ok {
		baseQuery += fmt.Sprintf(" AND population >= $%d", len(args)+1)
		args = append(args, minPop)
	}

	if maxPop, ok := filters["max_population"].(int); ok {
		baseQuery += fmt.Sprintf(" AND population <= $%d", len(args)+1)
		args = append(args, maxPop)
	}

	if devLevel, ok := filters["development_level"].(string); ok && devLevel != "" {
		baseQuery += fmt.Sprintf(" AND development_level = $%d", len(args)+1)
		args = append(args, devLevel)
	}

	// Геофильтр
	if lat, ok := filters["latitude"].(float64); ok {
		if lon, ok := filters["longitude"].(float64); ok {
			radius := 50.0 // default
			if r, ok := filters["radius_km"].(float64); ok {
				radius = r
			}

			baseQuery += fmt.Sprintf(` AND ST_DWithin(
				ST_SetSRID(ST_MakePoint($%d, $%d), 4326)::geography,
				geom::geography,
				$%d * 1000
			)`, len(args)+1, len(args)+2, len(args)+3)
			args = append(args, lon, lat, radius)
		}
	}

	// Получаем общее количество для пагинации
	countQuery := strings.Replace(baseQuery, "SELECT id, name, country, region, latitude, longitude, population,\n\t\t       development_level, description, landmarks, economic_data,\n\t\t       social_data, timeline_year, is_active, created_at, updated_at\n\t\tFROM world_cities.cities", "SELECT COUNT(*) FROM world_cities.cities", 1)

	var total int
	if err := r.db.QueryRowContext(ctx, countQuery, args...).Scan(&total); err != nil {
		return nil, 0, fmt.Errorf("failed to count cities: %w", err)
	}

	// Добавляем сортировку и пагинацию
	query := baseQuery + " ORDER BY population DESC, name ASC" + fmt.Sprintf(" LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.QueryContext(ctx, query, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to query cities: %w", err)
	}
	defer rows.Close()

	var cities []*City
	for rows.Next() {
		city := &City{}
		var landmarksBytes, economicBytes, socialBytes []byte

		err := rows.Scan(
			&city.ID, &city.Name, &city.Country, &city.Region,
			&city.Latitude, &city.Longitude, &city.Population,
			&city.DevelopmentLevel, &city.Description,
			&landmarksBytes, &economicBytes, &socialBytes,
			&city.TimelineYear, &city.IsActive,
			&city.CreatedAt, &city.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan city: %w", err)
		}

		// Десериализуем JSON поля
		if err := json.Unmarshal(landmarksBytes, &city.Landmarks); err != nil {
			r.logger.Warn("Failed to unmarshal landmarks", zap.Error(err), zap.String("city_id", city.ID))
			city.Landmarks = []string{}
		}

		if err := json.Unmarshal(economicBytes, &city.EconomicData); err != nil {
			r.logger.Warn("Failed to unmarshal economic_data", zap.Error(err), zap.String("city_id", city.ID))
			city.EconomicData = map[string]interface{}{}
		}

		if err := json.Unmarshal(socialBytes, &city.SocialData); err != nil {
			r.logger.Warn("Failed to unmarshal social_data", zap.Error(err), zap.String("city_id", city.ID))
			city.SocialData = map[string]interface{}{}
		}

		cities = append(cities, city)
	}

	if err := rows.Err(); err != nil {
		return nil, 0, fmt.Errorf("error iterating cities: %w", err)
	}

	return cities, total, nil
}

// GetCityByID возвращает город по ID
func (r *WorldCitiesRepository) GetCityByID(ctx context.Context, cityID string) (*City, error) {
	query := `
		SELECT id, name, country, region, latitude, longitude, population,
		       development_level, description, landmarks, economic_data,
		       social_data, timeline_year, is_active, created_at, updated_at
		FROM world_cities.cities
		WHERE id = $1 AND is_active = true
	`

	city := &City{}
	var landmarksBytes, economicBytes, socialBytes []byte

	err := r.db.QueryRowContext(ctx, query, cityID).Scan(
		&city.ID, &city.Name, &city.Country, &city.Region,
		&city.Latitude, &city.Longitude, &city.Population,
		&city.DevelopmentLevel, &city.Description,
		&landmarksBytes, &economicBytes, &socialBytes,
		&city.TimelineYear, &city.IsActive,
		&city.CreatedAt, &city.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	// Десериализуем JSON поля
	if err := json.Unmarshal(landmarksBytes, &city.Landmarks); err != nil {
		r.logger.Warn("Failed to unmarshal landmarks", zap.Error(err), zap.String("city_id", city.ID))
		city.Landmarks = []string{}
	}

	if err := json.Unmarshal(economicBytes, &city.EconomicData); err != nil {
		r.logger.Warn("Failed to unmarshal economic_data", zap.Error(err), zap.String("city_id", city.ID))
		city.EconomicData = map[string]interface{}{}
	}

	if err := json.Unmarshal(socialBytes, &city.SocialData); err != nil {
		r.logger.Warn("Failed to unmarshal social_data", zap.Error(err), zap.String("city_id", city.ID))
		city.SocialData = map[string]interface{}{}
	}

	return city, nil
}

// CreateCity создает новый город
func (r *WorldCitiesRepository) CreateCity(ctx context.Context, city *City) error {
	query := `
		INSERT INTO world_cities.cities (
			id, name, country, region, latitude, longitude, population,
			development_level, description, landmarks, economic_data,
			social_data, timeline_year, is_active, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16)
	`

	landmarksJSON, _ := json.Marshal(city.Landmarks)
	economicJSON, _ := json.Marshal(city.EconomicData)
	socialJSON, _ := json.Marshal(city.SocialData)

	_, err := r.db.ExecContext(ctx, query,
		city.ID, city.Name, city.Country, city.Region,
		city.Latitude, city.Longitude, city.Population,
		city.DevelopmentLevel, city.Description,
		landmarksJSON, economicJSON, socialJSON,
		city.TimelineYear, city.IsActive,
		city.CreatedAt, city.UpdatedAt,
	)

	if err != nil {
		return fmt.Errorf("failed to create city: %w", err)
	}

	// Обновляем статистику
	if err := r.refreshCityStats(ctx); err != nil {
		r.logger.Warn("Failed to refresh city stats after create", zap.Error(err))
	}

	return nil
}

// UpdateCity обновляет город
func (r *WorldCitiesRepository) UpdateCity(ctx context.Context, cityID string, updates map[string]interface{}) error {
	if len(updates) == 0 {
		return fmt.Errorf("no fields to update")
	}

	query := "UPDATE world_cities.cities SET "
	var args []interface{}

	// Добавляем updated_at в любом случае
	query += "updated_at = CURRENT_TIMESTAMP"

	// Добавляем остальные поля
	for field, value := range updates {
		query += fmt.Sprintf(", %s = $%d", field, len(args)+1)
		args = append(args, value)
	}

	query += fmt.Sprintf(" WHERE id = $%d AND is_active = true", len(args)+1)
	args = append(args, cityID)

	result, err := r.db.ExecContext(ctx, query, args...)
	if err != nil {
		return fmt.Errorf("failed to update city: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return sql.ErrNoRows
	}

	// Обновляем статистику если изменилось население
	if _, ok := updates["population"]; ok {
		if err := r.refreshCityStats(ctx); err != nil {
			r.logger.Warn("Failed to refresh city stats after population update", zap.Error(err))
		}
	}

	return nil
}

// DeleteCity удаляет город (soft delete)
func (r *WorldCitiesRepository) DeleteCity(ctx context.Context, cityID string) error {
	query := "UPDATE world_cities.cities SET is_active = false, updated_at = CURRENT_TIMESTAMP WHERE id = $1 AND is_active = true"

	result, err := r.db.ExecContext(ctx, query, cityID)
	if err != nil {
		return fmt.Errorf("failed to delete city: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return sql.ErrNoRows
	}

	// Обновляем статистику
	if err := r.refreshCityStats(ctx); err != nil {
		r.logger.Warn("Failed to refresh city stats after delete", zap.Error(err))
	}

	return nil
}

// GetNearbyCities находит ближайшие города
func (r *WorldCitiesRepository) GetNearbyCities(ctx context.Context, lat, lon, radius float64, limit int) ([]*City, error) {
	query := `
		SELECT id, name, country, region, latitude, longitude, population,
		       development_level, description, landmarks, economic_data,
		       social_data, timeline_year, is_active, created_at, updated_at
		FROM world_cities.cities
		WHERE is_active = true
		  AND ST_DWithin(
			  ST_SetSRID(ST_MakePoint($1, $2), 4326)::geography,
			  geom::geography,
			  $3 * 1000
		  )
		ORDER BY geom <-> ST_SetSRID(ST_MakePoint($1, $2), 4326)
		LIMIT $4
	`

	rows, err := r.db.QueryContext(ctx, query, lon, lat, radius, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to query nearby cities: %w", err)
	}
	defer rows.Close()

	var cities []*City
	for rows.Next() {
		city := &City{}
		var landmarksBytes, economicBytes, socialBytes []byte

		err := rows.Scan(
			&city.ID, &city.Name, &city.Country, &city.Region,
			&city.Latitude, &city.Longitude, &city.Population,
			&city.DevelopmentLevel, &city.Description,
			&landmarksBytes, &economicBytes, &socialBytes,
			&city.TimelineYear, &city.IsActive,
			&city.CreatedAt, &city.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan nearby city: %w", err)
		}

		// Десериализуем JSON поля
		if err := json.Unmarshal(landmarksBytes, &city.Landmarks); err != nil {
			city.Landmarks = []string{}
		}
		if err := json.Unmarshal(economicBytes, &city.EconomicData); err != nil {
			city.EconomicData = map[string]interface{}{}
		}
		if err := json.Unmarshal(socialBytes, &city.SocialData); err != nil {
			city.SocialData = map[string]interface{}{}
		}

		cities = append(cities, city)
	}

	return cities, rows.Err()
}

// GetCitiesStats возвращает статистику городов
func (r *WorldCitiesRepository) GetCitiesStats(ctx context.Context) (map[string]interface{}, error) {
	query := `
		SELECT
			COUNT(*) as total_cities,
			SUM(population) as total_population,
			AVG(population) as avg_population,
			jsonb_object_agg(region::text, city_count) as cities_by_region,
			jsonb_object_agg(development_level::text, dev_count) as cities_by_development
		FROM (
			SELECT
				region,
				development_level,
				COUNT(*) as city_count,
				SUM(COUNT(*)) OVER (PARTITION BY region) as reg_count,
				SUM(COUNT(*)) OVER (PARTITION BY development_level) as dev_count
			FROM world_cities.cities
			WHERE is_active = true
			GROUP BY region, development_level
		) stats
	`

	var totalCities int
	var totalPopulation sql.NullInt64
	var avgPopulation sql.NullFloat64
	var citiesByRegion, citiesByDevelopment []byte

	err := r.db.QueryRowContext(ctx, query).Scan(
		&totalCities, &totalPopulation, &avgPopulation,
		&citiesByRegion, &citiesByDevelopment,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to get cities stats: %w", err)
	}

	// Десериализуем JSON
	var regionStats, devStats map[string]int
	if err := json.Unmarshal(citiesByRegion, &regionStats); err != nil {
		r.logger.Warn("Failed to unmarshal region stats", zap.Error(err))
		regionStats = map[string]int{}
	}
	if err := json.Unmarshal(citiesByDevelopment, &devStats); err != nil {
		r.logger.Warn("Failed to unmarshal development stats", zap.Error(err))
		devStats = map[string]int{}
	}

	// Получаем 10 крупнейших городов
	largestCities, err := r.getLargestCities(ctx, 10)
	if err != nil {
		r.logger.Warn("Failed to get largest cities", zap.Error(err))
		largestCities = []*City{}
	}

	return map[string]interface{}{
		"total_cities":          totalCities,
		"total_population":      totalPopulation.Int64,
		"average_population":    avgPopulation.Float64,
		"cities_by_region":      regionStats,
		"cities_by_development": devStats,
		"largest_cities":        largestCities,
	}, nil
}

// SearchCities осуществляет полнотекстовый поиск городов
func (r *WorldCitiesRepository) SearchCities(ctx context.Context, searchQuery string, limit int) ([]*City, error) {
	query := `
		SELECT id, name, country, region, latitude, longitude, population,
		       development_level, description, landmarks, economic_data,
		       social_data, timeline_year, is_active, created_at, updated_at,
		       ts_rank_cd(search_vector, plainto_tsquery('english', $1)) as rank
		FROM world_cities.cities
		WHERE is_active = true
		  AND search_vector @@ plainto_tsquery('english', $1)
		ORDER BY rank DESC, population DESC
		LIMIT $2
	`

	rows, err := r.db.QueryContext(ctx, query, searchQuery, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to search cities: %w", err)
	}
	defer rows.Close()

	var cities []*City
	for rows.Next() {
		city := &City{}
		var landmarksBytes, economicBytes, socialBytes []byte
		var rank float64

		err := rows.Scan(
			&city.ID, &city.Name, &city.Country, &city.Region,
			&city.Latitude, &city.Longitude, &city.Population,
			&city.DevelopmentLevel, &city.Description,
			&landmarksBytes, &economicBytes, &socialBytes,
			&city.TimelineYear, &city.IsActive,
			&city.CreatedAt, &city.UpdatedAt, &rank,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan search result: %w", err)
		}

		// Десериализуем JSON поля
		if err := json.Unmarshal(landmarksBytes, &city.Landmarks); err != nil {
			city.Landmarks = []string{}
		}
		if err := json.Unmarshal(economicBytes, &city.EconomicData); err != nil {
			city.EconomicData = map[string]interface{}{}
		}
		if err := json.Unmarshal(socialBytes, &city.SocialData); err != nil {
			city.SocialData = map[string]interface{}{}
		}

		cities = append(cities, city)
	}

	return cities, rows.Err()
}

// getLargestCities возвращает N крупнейших городов
func (r *WorldCitiesRepository) getLargestCities(ctx context.Context, limit int) ([]*City, error) {
	query := `
		SELECT id, name, country, region, latitude, longitude, population,
		       development_level, description, landmarks, economic_data,
		       social_data, timeline_year, is_active, created_at, updated_at
		FROM world_cities.cities
		WHERE is_active = true
		ORDER BY population DESC
		LIMIT $1
	`

	rows, err := r.db.QueryContext(ctx, query, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to get largest cities: %w", err)
	}
	defer rows.Close()

	var cities []*City
	for rows.Next() {
		city := &City{}
		var landmarksBytes, economicBytes, socialBytes []byte

		err := rows.Scan(
			&city.ID, &city.Name, &city.Country, &city.Region,
			&city.Latitude, &city.Longitude, &city.Population,
			&city.DevelopmentLevel, &city.Description,
			&landmarksBytes, &economicBytes, &socialBytes,
			&city.TimelineYear, &city.IsActive,
			&city.CreatedAt, &city.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan largest city: %w", err)
		}

		// Десериализуем JSON поля
		if err := json.Unmarshal(landmarksBytes, &city.Landmarks); err != nil {
			city.Landmarks = []string{}
		}
		if err := json.Unmarshal(economicBytes, &city.EconomicData); err != nil {
			city.EconomicData = map[string]interface{}{}
		}
		if err := json.Unmarshal(socialBytes, &city.SocialData); err != nil {
			city.SocialData = map[string]interface{}{}
		}

		cities = append(cities, city)
	}

	return cities, rows.Err()
}

// refreshCityStats обновляет статистику городов
func (r *WorldCitiesRepository) refreshCityStats(ctx context.Context) error {
	_, err := r.db.ExecContext(ctx, "SELECT world_cities.refresh_city_stats()")
	if err != nil {
		return fmt.Errorf("failed to refresh city stats: %w", err)
	}
	return nil
}
