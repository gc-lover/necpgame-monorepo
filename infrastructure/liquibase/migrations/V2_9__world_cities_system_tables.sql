-- Issue: #140875381
-- World Cities System Database Schema
-- Создание таблиц для системы мировых городов:
-- - world_cities.cities (города мира)
-- - world_cities.city_search_index (поисковый индекс)

-- Создание схемы world_cities, если её нет
CREATE SCHEMA IF NOT EXISTS world_cities;

-- Создание ENUM типов для оптимизации
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'development_level') THEN
        CREATE TYPE development_level AS ENUM ('undeveloped', 'developing', 'developed', 'megacity');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'world_region') THEN
        CREATE TYPE world_region AS ENUM ('North_America', 'South_America', 'Europe', 'Asia', 'Africa', 'Oceania', 'Antarctica');
    END IF;
END $$;

-- Основная таблица городов мира (column order: large → small for alignment)
CREATE TABLE IF NOT EXISTS world_cities.cities (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  name VARCHAR(100) NOT NULL,                      -- 100 chars
  country VARCHAR(100) NOT NULL,                   -- 100 chars
  region world_region NOT NULL,                    -- 1 byte (enum)
  latitude DOUBLE PRECISION NOT NULL,              -- 8 bytes
  longitude DOUBLE PRECISION NOT NULL,             -- 8 bytes
  population INTEGER NOT NULL DEFAULT 0,           -- 4 bytes
  development_level development_level NOT NULL,    -- 1 byte (enum)
  description TEXT,                                -- Variable
  landmarks JSONB DEFAULT '[]'::jsonb,             -- JSONB
  economic_data JSONB DEFAULT '{}'::jsonb,         -- JSONB
  social_data JSONB DEFAULT '{}'::jsonb,           -- JSONB
  timeline_year INTEGER NOT NULL,                   -- 4 bytes
  is_active BOOLEAN NOT NULL DEFAULT true,         -- 1 byte
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 8 bytes

  -- Геоспациальный индекс для быстрого поиска ближайших городов
  -- Используем PostGIS point для точных гео-запросов
  geom geometry(Point, 4326),

  -- Constraints
  CONSTRAINT check_latitude CHECK (latitude >= -90 AND latitude <= 90),
  CONSTRAINT check_longitude CHECK (longitude >= -180 AND longitude <= 180),
  CONSTRAINT check_timeline_year CHECK (timeline_year >= 2020 AND timeline_year <= 2093),
  CONSTRAINT check_population CHECK (population >= 0)
);

-- Создаем гео-индекс для быстрого поиска ближайших городов
-- Используем GIST индекс для геометрических запросов
CREATE INDEX IF NOT EXISTS idx_cities_geom ON world_cities.cities USING GIST (geom);

-- Стандартные индексы для производительности
CREATE INDEX IF NOT EXISTS idx_cities_region ON world_cities.cities (region);
CREATE INDEX IF NOT EXISTS idx_cities_country ON world_cities.cities (country);
CREATE INDEX IF NOT EXISTS idx_cities_population ON world_cities.cities (population);
CREATE INDEX IF NOT EXISTS idx_cities_development_level ON world_cities.cities (development_level);
CREATE INDEX IF NOT EXISTS idx_cities_timeline_year ON world_cities.cities (timeline_year);
CREATE INDEX IF NOT EXISTS idx_cities_active ON world_cities.cities (is_active);

-- Composite indexes для часто используемых фильтров
CREATE INDEX IF NOT EXISTS idx_cities_region_population ON world_cities.cities (region, population DESC);
CREATE INDEX IF NOT EXISTS idx_cities_country_region ON world_cities.cities (country, region);
CREATE INDEX IF NOT EXISTS idx_cities_timeline_active ON world_cities.cities (timeline_year, is_active);

-- Полнотекстовый поисковый индекс
CREATE INDEX IF NOT EXISTS idx_cities_search ON world_cities.cities
USING GIN (to_tsvector('english', name || ' ' || country || ' ' || COALESCE(description, '')));

-- Таблица для кеширования статистики (материализованное представление)
CREATE TABLE IF NOT EXISTS world_cities.city_stats_cache (
  id SERIAL PRIMARY KEY,
  region world_region,
  total_cities INTEGER NOT NULL DEFAULT 0,
  total_population BIGINT NOT NULL DEFAULT 0,
  avg_population DOUBLE PRECISION,
  max_population INTEGER,
  min_population INTEGER,
  cities_by_development JSONB DEFAULT '{}'::jsonb,
  last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE(region)
);

-- Триггеры для автоматического обновления updated_at
CREATE OR REPLACE FUNCTION world_cities.update_cities_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_cities_updated_at ON world_cities.cities;
CREATE TRIGGER update_cities_updated_at
    BEFORE UPDATE ON world_cities.cities
    FOR EACH ROW EXECUTE FUNCTION world_cities.update_cities_updated_at();

-- Функция для расчета расстояния между двумя точками (Haversine formula)
-- Оптимизированная для PostgreSQL
CREATE OR REPLACE FUNCTION world_cities.calculate_distance(lat1 DOUBLE PRECISION, lon1 DOUBLE PRECISION,
                                                          lat2 DOUBLE PRECISION, lon2 DOUBLE PRECISION)
RETURNS DOUBLE PRECISION AS $$
DECLARE
    earth_radius DOUBLE PRECISION := 6371.0;
    dlat DOUBLE PRECISION;
    dlon DOUBLE PRECISION;
    a DOUBLE PRECISION;
    c DOUBLE PRECISION;
BEGIN
    -- Конвертируем в радианы
    dlat := RADIANS(lat2 - lat1);
    dlon := RADIANS(lon2 - lon1);

    a := SIN(dlat/2) * SIN(dlat/2) +
         COS(RADIANS(lat1)) * COS(RADIANS(lat2)) *
         SIN(dlon/2) * SIN(dlon/2);

    c := 2 * ATAN2(SQRT(a), SQRT(1-a));

    RETURN earth_radius * c;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Функция для обновления геометрии при вставке/обновлении
CREATE OR REPLACE FUNCTION world_cities.update_city_geom()
RETURNS TRIGGER AS $$
BEGIN
    NEW.geom = ST_SetSRID(ST_MakePoint(NEW.longitude, NEW.latitude), 4326);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер для автоматического обновления геометрии
DROP TRIGGER IF EXISTS update_city_geom_trigger ON world_cities.cities;
CREATE TRIGGER update_city_geom_trigger
    BEFORE INSERT OR UPDATE OF latitude, longitude ON world_cities.cities
    FOR EACH ROW EXECUTE FUNCTION world_cities.update_city_geom();

-- Функция для поиска ближайших городов
CREATE OR REPLACE FUNCTION world_cities.find_nearby_cities(
    target_lat DOUBLE PRECISION,
    target_lon DOUBLE PRECISION,
    radius_km DOUBLE PRECISION DEFAULT 100,
    max_results INTEGER DEFAULT 10
)
RETURNS TABLE (
    city_id UUID,
    city_name VARCHAR(100),
    distance_km DOUBLE PRECISION
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.name,
        world_cities.calculate_distance(target_lat, target_lon, c.latitude, c.longitude) as distance
    FROM world_cities.cities c
    WHERE c.is_active = true
      AND ST_DWithin(
          ST_SetSRID(ST_MakePoint(target_lon, target_lat), 4326)::geography,
          c.geom::geography,
          radius_km * 1000  -- конвертируем км в метры
      )
    ORDER BY c.geom <-> ST_SetSRID(ST_MakePoint(target_lon, target_lat), 4326)
    LIMIT max_results;
END;
$$ LANGUAGE plpgsql;

-- Функция для обновления статистики городов
CREATE OR REPLACE FUNCTION world_cities.refresh_city_stats()
RETURNS VOID AS $$
BEGIN
    -- Очищаем старую статистику
    TRUNCATE world_cities.city_stats_cache;

    -- Вставляем обновленную статистику
    INSERT INTO world_cities.city_stats_cache (
        region, total_cities, total_population, avg_population,
        max_population, min_population, cities_by_development, last_updated
    )
    SELECT
        region,
        COUNT(*) as total_cities,
        SUM(population) as total_population,
        AVG(population) as avg_population,
        MAX(population) as max_population,
        MIN(population) as min_population,
        jsonb_object_agg(
            development_level::text,
            cnt
        ) as cities_by_development,
        CURRENT_TIMESTAMP
    FROM (
        SELECT
            region,
            development_level,
            COUNT(*) as cnt
        FROM world_cities.cities
        WHERE is_active = true
        GROUP BY region, development_level
    ) stats
    GROUP BY region;
END;
$$ LANGUAGE plpgsql;

-- Создаем представление для быстрого доступа к статистике
CREATE OR REPLACE VIEW world_cities.city_statistics AS
SELECT
    region,
    total_cities,
    total_population,
    avg_population,
    max_population,
    min_population,
    cities_by_development,
    last_updated
FROM world_cities.city_stats_cache
ORDER BY total_cities DESC;

-- Комментарии для документации
COMMENT ON SCHEMA world_cities IS 'Schema for world cities system - manages global city data for MMOFPS game world';
COMMENT ON TABLE world_cities.cities IS 'Core cities table with geospatial indexing for fast location-based queries';
COMMENT ON COLUMN world_cities.cities.geom IS 'PostGIS geometry point for spatial queries and distance calculations';
COMMENT ON FUNCTION world_cities.find_nearby_cities IS 'Optimized function to find cities within radius using spatial indexing';
COMMENT ON FUNCTION world_cities.calculate_distance IS 'Haversine distance calculation between two lat/lon points';

-- Grant permissions для приложения
GRANT USAGE ON SCHEMA world_cities TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA world_cities TO necpgame_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA world_cities TO necpgame_app;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA world_cities TO necpgame_app;
