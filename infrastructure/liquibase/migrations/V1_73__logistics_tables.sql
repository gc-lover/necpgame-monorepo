-- Issue: #140890225
-- Logistics System Database Schema
-- Создание таблиц для системы логистики и перевозок:
-- - transport_shipments (перевозки)
-- - transport_routes (маршруты перевозок)
-- - transport_incidents (инциденты перевозок)
-- - transport_tracking (отслеживание позиций перевозок)
-- - transport_sla_metrics (метрики SLA перевозок)

-- Создание схемы logistics, если её нет
CREATE SCHEMA IF NOT EXISTS logistics;

-- Включение расширения PostGIS для геопозиций (если доступно)
CREATE EXTENSION IF NOT EXISTS postgis;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transport_type') THEN
        CREATE TYPE transport_type AS ENUM ('ground', 'air', 'rail', 'courier', 'player_pickup');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transport_shipment_status') THEN
        CREATE TYPE transport_shipment_status AS ENUM ('DRAFT', 'SCHEDULED', 'IN_TRANSIT', 'DELAYED', 'DELIVERED', 'LOST', 'CANCELLED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'insurance_plan') THEN
        CREATE TYPE insurance_plan AS ENUM ('none', 'basic', 'premium', 'full');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'escort_type') THEN
        CREATE TYPE escort_type AS ENUM ('none', 'npc_escort', 'player_escort', 'armored_transport');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transport_route_type') THEN
        CREATE TYPE transport_route_type AS ENUM ('local', 'regional', 'global');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transport_incident_type') THEN
        CREATE TYPE transport_incident_type AS ENUM ('bandit_attack', 'accident', 'customs_delay', 'weather', 'other');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transport_incident_severity') THEN
        CREATE TYPE transport_incident_severity AS ENUM ('low', 'medium', 'high', 'critical');
    END IF;
END $$;

-- Таблица перевозок
CREATE TABLE IF NOT EXISTS logistics.transport_shipments (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  player_id UUID NOT NULL,
  -- FK accounts
    origin_region_id UUID,
  -- FK regions (nullable)
    destination_region_id UUID,
  -- nullable
    insurance_policy_id UUID,
  -- nullable
    escort_id UUID,
  cargo_data JSONB NOT NULL DEFAULT '{}',
  стоимость
    route_data JSONB NOT NULL DEFAULT '{}',
  scheduled_departure TIMESTAMP,
  -- nullable
    actual_departure TIMESTAMP,
  -- nullable
    estimated_arrival TIMESTAMP,
  -- nullable
    actual_arrival TIMESTAMP,
  -- nullable
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- nullable
    base_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  insurance_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  escort_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  total_cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  sla_target_hours INTEGER,
  -- FK regions (nullable)
    transport_type transport_type NOT NULL,
  -- список предметов,
  вес,
  объём,
  -- маршрут,
  расстояние,
  время доставки
    status transport_shipment_status NOT NULL DEFAULT 'DRAFT',
  -- nullable
    current_position GEOMETRY(POINT, 4326),
  -- nullable,
  для отслеживания (WGS84)
    insurance_plan insurance_plan,
  -- FK insurance_policies (nullable)
    escort_type escort_type
);

-- Индексы для transport_shipments
CREATE INDEX IF NOT EXISTS idx_transport_shipments_player_status 
    ON logistics.transport_shipments(player_id, status);
CREATE INDEX IF NOT EXISTS idx_transport_shipments_status_scheduled 
    ON logistics.transport_shipments(status, scheduled_departure) WHERE scheduled_departure IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_transport_shipments_origin_destination 
    ON logistics.transport_shipments(origin_region_id, destination_region_id) 
    WHERE origin_region_id IS NOT NULL AND destination_region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_transport_shipments_current_position 
    ON logistics.transport_shipments USING GIST(current_position) WHERE current_position IS NOT NULL;

-- Таблица маршрутов перевозок
CREATE TABLE IF NOT EXISTS logistics.transport_routes (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  shipment_id UUID NOT NULL REFERENCES logistics.transport_shipments(id) ON DELETE CASCADE,
  waypoints GEOMETRY(POINT, 4326)[],
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- массив точек маршрута (WGS84)
    distance_km DECIMAL(10,2) NOT NULL,
  estimated_hours DECIMAL(10,2) NOT NULL,
  base_risk_level DECIMAL(3,2) NOT NULL DEFAULT 0.00 CHECK (base_risk_level >= 0.00 AND base_risk_level <= 1.00),
  route_type transport_route_type NOT NULL
);

-- Индексы для transport_routes
CREATE INDEX IF NOT EXISTS idx_transport_routes_shipment_id 
    ON logistics.transport_routes(shipment_id);

-- Таблица инцидентов перевозок
CREATE TABLE IF NOT EXISTS logistics.transport_incidents (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  shipment_id UUID NOT NULL REFERENCES logistics.transport_shipments(id) ON DELETE CASCADE,
  description TEXT,
  -- nullable (WGS84)
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  resolved_at TIMESTAMP,
  -- nullable
    cargo_loss_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (cargo_loss_percentage >= 0.00 AND cargo_loss_percentage <= 100.00),
  delay_hours INTEGER NOT NULL DEFAULT 0,
  resolved BOOLEAN NOT NULL DEFAULT false,
  incident_type transport_incident_type NOT NULL,
  severity transport_incident_severity NOT NULL,
  position GEOMETRY(POINT, 4326)
);

-- Индексы для transport_incidents
CREATE INDEX IF NOT EXISTS idx_transport_incidents_shipment_occurred 
    ON logistics.transport_incidents(shipment_id, occurred_at DESC);
CREATE INDEX IF NOT EXISTS idx_transport_incidents_type_severity 
    ON logistics.transport_incidents(incident_type, severity);
CREATE INDEX IF NOT EXISTS idx_transport_incidents_position 
    ON logistics.transport_incidents USING GIST(position) WHERE position IS NOT NULL;

-- Таблица отслеживания позиций перевозок
CREATE TABLE IF NOT EXISTS logistics.transport_tracking (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  shipment_id UUID NOT NULL REFERENCES logistics.transport_shipments(id) ON DELETE CASCADE,
  recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  -- (WGS84)
    speed_kmh DECIMAL(10,2),
  -- nullable
    progress_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00 CHECK (progress_percentage >= 0.00 AND progress_percentage <= 100.00),
  position GEOMETRY(POINT, 4326) NOT NULL
);

-- Индексы для transport_tracking
CREATE INDEX IF NOT EXISTS idx_transport_tracking_shipment_recorded 
    ON logistics.transport_tracking(shipment_id, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_transport_tracking_position 
    ON logistics.transport_tracking USING GIST(position);

-- Таблица метрик SLA перевозок
CREATE TABLE IF NOT EXISTS logistics.transport_sla_metrics (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  shipment_id UUID NOT NULL REFERENCES logistics.transport_shipments(id) ON DELETE CASCADE,
  violation_reason TEXT,
  -- nullable
    measured_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  target_hours INTEGER NOT NULL,
  actual_hours INTEGER,
  -- nullable
    on_time BOOLEAN,
  -- nullable
    sla_violated BOOLEAN NOT NULL DEFAULT false
);

-- Индексы для transport_sla_metrics
CREATE INDEX IF NOT EXISTS idx_transport_sla_metrics_shipment_id 
    ON logistics.transport_sla_metrics(shipment_id);
CREATE INDEX IF NOT EXISTS idx_transport_sla_metrics_violated_measured 
    ON logistics.transport_sla_metrics(sla_violated, measured_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE logistics.transport_shipments IS 'Перевозки товаров между регионами';
COMMENT ON TABLE logistics.transport_routes IS 'Маршруты перевозок с точками пути';
COMMENT ON TABLE logistics.transport_incidents IS 'Инциденты перевозок (атаки, аварии, задержки)';
COMMENT ON TABLE logistics.transport_tracking IS 'Отслеживание позиций перевозок в реальном времени';
COMMENT ON TABLE logistics.transport_sla_metrics IS 'Метрики SLA перевозок для мониторинга времени доставки';

-- Комментарии к колонкам
COMMENT ON COLUMN logistics.transport_shipments.transport_type IS 'Тип транспорта: ground, air, rail, courier, player_pickup';
COMMENT ON COLUMN logistics.transport_shipments.cargo_data IS 'Данные груза: список предметов, вес, объём, стоимость в JSONB';
COMMENT ON COLUMN logistics.transport_shipments.route_data IS 'Данные маршрута: маршрут, расстояние, время доставки в JSONB';
COMMENT ON COLUMN logistics.transport_shipments.status IS 'Статус перевозки: DRAFT, SCHEDULED, IN_TRANSIT, DELAYED, DELIVERED, LOST, CANCELLED';
COMMENT ON COLUMN logistics.transport_shipments.current_position IS 'Текущая позиция перевозки (PostGIS POINT, WGS84)';
COMMENT ON COLUMN logistics.transport_shipments.insurance_plan IS 'План страхования: none, basic, premium, full';
COMMENT ON COLUMN logistics.transport_shipments.escort_type IS 'Тип эскорта: none, npc_escort, player_escort, armored_transport';
COMMENT ON COLUMN logistics.transport_shipments.sla_target_hours IS 'Целевое время доставки в часах';
COMMENT ON COLUMN logistics.transport_routes.route_type IS 'Тип маршрута: local, regional, global';
COMMENT ON COLUMN logistics.transport_routes.waypoints IS 'Массив точек маршрута (PostGIS POINT[], WGS84)';
COMMENT ON COLUMN logistics.transport_routes.base_risk_level IS 'Базовый уровень риска маршрута (0.00-1.00)';
COMMENT ON COLUMN logistics.transport_incidents.incident_type IS 'Тип инцидента: bandit_attack, accident, customs_delay, weather, other';
COMMENT ON COLUMN logistics.transport_incidents.severity IS 'Серьезность инцидента: low, medium, high, critical';
COMMENT ON COLUMN logistics.transport_incidents.position IS 'Позиция инцидента (PostGIS POINT, WGS84)';
COMMENT ON COLUMN logistics.transport_incidents.cargo_loss_percentage IS 'Процент потери груза (0.00-100.00)';
COMMENT ON COLUMN logistics.transport_tracking.position IS 'Позиция перевозки (PostGIS POINT, WGS84)';
COMMENT ON COLUMN logistics.transport_tracking.progress_percentage IS 'Прогресс доставки в процентах (0.00-100.00)';
COMMENT ON COLUMN logistics.transport_sla_metrics.on_time IS 'Доставка в срок (NULL если еще не завершена)';
COMMENT ON COLUMN logistics.transport_sla_metrics.sla_violated IS 'Нарушено ли SLA';

