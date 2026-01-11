-- Interactive Objects Database Schema
-- Enterprise-grade interactive object management for Night City MMOFPS RPG
-- Supports real-time object synchronization and zone-specific mechanics
-- Issue: #2302

-- Create interactive schema if not exists
CREATE SCHEMA IF NOT EXISTS interactive;
COMMENT ON SCHEMA interactive IS 'Enterprise-grade interactive object management system for Night City MMOFPS RPG';

-- ===========================================
-- INTERACTIVE OBJECTS TABLE
-- ===========================================
-- PERFORMANCE: Core interactive object state management
-- Supports real-time sync for airport hubs, military compounds, motels, labs
CREATE TABLE interactive.interactive_objects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 1,

    -- Object identity
    object_type VARCHAR(50) NOT NULL CHECK (object_type IN ('airport_hub', 'military_compound', 'no_tell_motel', 'covert_lab', 'security_terminal', 'cargo_container', 'drone_station', 'weapon_cache')),
    object_subtype VARCHAR(50),
    zone_id UUID NOT NULL,

    -- Spatial positioning
    position GEOMETRY(POINT, 4326) NOT NULL,
    rotation REAL DEFAULT 0.0,
    zone_name VARCHAR(100) NOT NULL,

    -- Object state
    state VARCHAR(30) DEFAULT 'inactive' CHECK (state IN ('inactive', 'active', 'damaged', 'destroyed', 'locked', 'unlocked', 'hacked', 'secured')),
    health INTEGER DEFAULT 100 CHECK (health >= 0),
    max_health INTEGER DEFAULT 100 CHECK (max_health > 0),

    -- Interaction properties
    interaction_type VARCHAR(30) DEFAULT 'single_use' CHECK (interaction_type IN ('single_use', 'reusable', 'timed', 'conditional', 'persistent')),
    cooldown_seconds INTEGER DEFAULT 0,
    last_interaction_at TIMESTAMP WITH TIME ZONE,

    -- Access control
    access_level VARCHAR(20) DEFAULT 'public' CHECK (access_level IN ('public', 'restricted', 'military', 'corporate', 'criminal')),
    required_permissions VARCHAR(100)[] DEFAULT '{}',
    owner_entity_id UUID, -- Player, NPC, or faction ID

    -- Zone-specific properties
    zone_specific_data JSONB DEFAULT '{}', -- Airport security codes, lab research data, etc.

    -- Effects and consequences
    interaction_effects JSONB DEFAULT '{}', -- Effects applied to interacting player
    failure_consequences JSONB DEFAULT '{}', -- Consequences of failed interactions

    -- Performance optimization
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- Event sourcing metadata
    event_version BIGINT DEFAULT 0,

    -- PERFORMANCE: Optimistic locking constraint
    CONSTRAINT uk_interactive_objects_id_version UNIQUE (id, version),

    -- PERFORMANCE: Spatial constraint for valid coordinates
    CONSTRAINT ck_interactive_objects_position_valid CHECK (ST_IsValid(position))
);

-- PERFORMANCE: Spatial and functional indexes
CREATE INDEX idx_interactive_objects_position ON interactive.interactive_objects USING GIST (position) WHERE is_active = true;
CREATE INDEX idx_interactive_objects_zone_active ON interactive.interactive_objects (zone_id, is_active) WHERE is_active = true;
CREATE INDEX idx_interactive_objects_type_zone ON interactive.interactive_objects (object_type, zone_id, is_active) WHERE is_active = true;
CREATE INDEX idx_interactive_objects_state ON interactive.interactive_objects (state, updated_at DESC) WHERE is_active = true;
CREATE INDEX idx_interactive_objects_owner ON interactive.interactive_objects (owner_entity_id) WHERE owner_entity_id IS NOT NULL;

COMMENT ON TABLE interactive.interactive_objects IS 'Core interactive objects with real-time state management';

-- ===========================================
-- ZONE STATES TABLE
-- ===========================================
-- PERFORMANCE: Zone-specific state management for complex areas
-- Supports airport hubs, military compounds, motels, and covert labs
CREATE TABLE interactive.zone_states (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id UUID NOT NULL,

    -- Zone identity and type
    zone_type VARCHAR(30) NOT NULL CHECK (zone_type IN ('airport_hub', 'military_compound', 'no_tell_motel', 'covert_lab')),
    zone_name VARCHAR(100) NOT NULL,

    -- Zone-wide state
    security_level VARCHAR(20) DEFAULT 'normal' CHECK (security_level IN ('low', 'normal', 'high', 'lockdown', 'breach')),
    operational_status VARCHAR(20) DEFAULT 'operational' CHECK (operational_status IN ('operational', 'maintenance', 'compromised', 'destroyed')),

    -- Zone-specific properties
    zone_properties JSONB DEFAULT '{}',

    -- Airport hub specific
    flight_clearance_level INTEGER DEFAULT 1 CHECK (flight_clearance_level >= 0 AND flight_clearance_level <= 5),
    active_flights JSONB DEFAULT '[]',

    -- Military compound specific
    alert_status VARCHAR(20) DEFAULT 'standby' CHECK (alert_status IN ('standby', 'yellow', 'red', 'black')),
    defense_systems_active BOOLEAN DEFAULT true,
    patrol_routes JSONB DEFAULT '[]',

    -- No-tell motel specific
    occupancy_rate REAL DEFAULT 0.0 CHECK (occupancy_rate >= 0.0 AND occupancy_rate <= 1.0),
    black_market_active BOOLEAN DEFAULT false,
    surveillance_bypassed BOOLEAN DEFAULT false,

    -- Covert lab specific
    research_progress JSONB DEFAULT '{}',
    containment_status VARCHAR(20) DEFAULT 'secure' CHECK (containment_status IN ('secure', 'breached', 'critical', 'lost')),
    experimental_subjects JSONB DEFAULT '[]',

    -- Performance optimization
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- PERFORMANCE: Unique constraint for zone states
    CONSTRAINT uk_zone_states_zone UNIQUE (zone_id)
);

-- PERFORMANCE: Indexes for zone queries
CREATE INDEX idx_zone_states_type_active ON interactive.zone_states (zone_type, is_active) WHERE is_active = true;
CREATE INDEX idx_zone_states_security ON interactive.zone_states (security_level, operational_status) WHERE is_active = true;

COMMENT ON TABLE interactive.zone_states IS 'Zone-specific state management for complex interactive areas';

-- ===========================================
-- TELEMETRY DATA TABLE
-- ===========================================
-- PERFORMANCE: Analytics and performance metrics collection
-- Supports usage tracking and system optimization
CREATE TABLE interactive.telemetry_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID REFERENCES interactive.interactive_objects(id) ON DELETE CASCADE,

    -- Telemetry metadata
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('interaction', 'state_change', 'damage', 'repair', 'hack_attempt', 'access_granted', 'access_denied')),
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Event data
    event_data JSONB NOT NULL,

    -- Context information
    player_id UUID,
    zone_id UUID NOT NULL,
    session_id UUID,

    -- Performance metrics
    response_time_ms REAL,
    success BOOLEAN,
    error_message TEXT,

    -- Aggregation helpers
    event_date DATE DEFAULT CURRENT_DATE,
    event_hour INTEGER DEFAULT EXTRACT(HOUR FROM NOW()),

    -- PERFORMANCE: Foreign key index
    CONSTRAINT fk_telemetry_data_object FOREIGN KEY (object_id) REFERENCES interactive.interactive_objects(id)
);

-- PERFORMANCE: Indexes for telemetry analytics
CREATE INDEX idx_telemetry_data_object_recorded ON interactive.telemetry_data (object_id, recorded_at DESC);
CREATE INDEX idx_telemetry_data_event_date ON interactive.telemetry_data (event_date, event_type);
CREATE INDEX idx_telemetry_data_player ON interactive.telemetry_data (player_id, recorded_at DESC) WHERE player_id IS NOT NULL;
CREATE INDEX idx_telemetry_data_zone_event ON interactive.telemetry_data (zone_id, event_type, recorded_at DESC);

COMMENT ON TABLE interactive.telemetry_data IS 'Interactive object telemetry and analytics data collection';

-- ===========================================
-- INTERACTIVE OBJECT EVENTS TABLE (EVENT SOURCING)
-- ===========================================
-- PERFORMANCE: Event sourcing for object state changes
-- Supports full object history reconstruction and debugging
CREATE TABLE interactive.interactive_object_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    object_id UUID NOT NULL REFERENCES interactive.interactive_objects(id) ON DELETE CASCADE,

    -- Event data
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('created', 'interacted', 'damaged', 'repaired', 'hacked', 'secured', 'destroyed', 'state_changed')),
    event_version BIGINT NOT NULL,
    event_data JSONB NOT NULL,

    -- Metadata
    occurred_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    player_id UUID,
    zone_id UUID NOT NULL,

    -- PERFORMANCE: Event ordering constraint
    CONSTRAINT uk_interactive_object_events_object_version UNIQUE (object_id, event_version),
    CONSTRAINT fk_interactive_object_events_object FOREIGN KEY (object_id) REFERENCES interactive.interactive_objects(id)
);

-- PERFORMANCE: Indexes for event sourcing queries
CREATE INDEX idx_interactive_object_events_object_occurred ON interactive.interactive_object_events (object_id, occurred_at DESC);
CREATE INDEX idx_interactive_object_events_type_zone ON interactive.interactive_object_events (event_type, zone_id, occurred_at DESC);
CREATE INDEX idx_interactive_object_events_player ON interactive.interactive_object_events (player_id, occurred_at DESC) WHERE player_id IS NOT NULL;

COMMENT ON TABLE interactive.interactive_object_events IS 'Event sourcing for interactive object state changes';

-- ===========================================
-- ZONE-SPECIFIC OBJECT TABLES
-- ===========================================

-- Airport Hub Terminals
CREATE TABLE interactive.airport_terminals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() REFERENCES interactive.interactive_objects(id) ON DELETE CASCADE,
    terminal_type VARCHAR(20) DEFAULT 'checkin' CHECK (terminal_type IN ('checkin', 'security', 'gate', 'baggage', 'control_tower')),

    -- Airport specific properties
    flight_number VARCHAR(10),
    destination VARCHAR(100),
    departure_time TIMESTAMP WITH TIME ZONE,
    security_clearance_required BOOLEAN DEFAULT false,
    cargo_manifest JSONB DEFAULT '{}',

    -- Drone management
    drone_capacity INTEGER DEFAULT 10,
    active_drones INTEGER DEFAULT 0,
    drone_routes JSONB DEFAULT '[]',

    -- Security bypass mechanics
    security_bypass_active BOOLEAN DEFAULT false,
    bypass_difficulty INTEGER DEFAULT 5 CHECK (bypass_difficulty >= 1 AND bypass_difficulty <= 10),

    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Military Compound Systems
CREATE TABLE interactive.military_systems (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() REFERENCES interactive.interactive_objects(id) ON DELETE CASCADE,
    system_type VARCHAR(30) DEFAULT 'security' CHECK (system_type IN ('security', 'weapons', 'communications', 'power', 'surveillance')),

    -- Military specific properties
    threat_level INTEGER DEFAULT 1 CHECK (threat_level >= 1 AND threat_level <= 5),
    operational_readiness REAL DEFAULT 1.0 CHECK (operational_readiness >= 0.0 AND operational_readiness <= 1.0),

    -- Weapon systems
    weapon_type VARCHAR(30),
    ammunition_count INTEGER DEFAULT 100 CHECK (ammunition_count >= 0),
    targeting_accuracy REAL DEFAULT 0.8 CHECK (targeting_accuracy >= 0.0 AND targeting_accuracy <= 1.0),

    -- Surveillance
    camera_coverage REAL DEFAULT 0.0 CHECK (camera_coverage >= 0.0 AND camera_coverage <= 1.0),
    drone_swarm_size INTEGER DEFAULT 0 CHECK (drone_swarm_size >= 0),

    -- Shield generator
    shield_strength REAL DEFAULT 1.0 CHECK (shield_strength >= 0.0 AND shield_strength <= 1.0),
    energy_reserves REAL DEFAULT 1.0 CHECK (energy_reserves >= 0.0 AND energy_reserves <= 1.0),

    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- No-Tell Motel Rooms
CREATE TABLE interactive.motel_rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() REFERENCES interactive.interactive_objects(id) ON DELETE CASCADE,
    room_number VARCHAR(10) NOT NULL,
    room_type VARCHAR(20) DEFAULT 'standard' CHECK (room_type IN ('standard', 'suite', 'penthouse', 'basement')),

    -- Motel specific properties
    occupant_id UUID, -- Current occupant
    check_in_time TIMESTAMP WITH TIME ZONE,
    check_out_time TIMESTAMP WITH TIME ZONE,

    -- Secure storage
    storage_locked BOOLEAN DEFAULT true,
    storage_contents JSONB DEFAULT '{}',
    storage_security_level INTEGER DEFAULT 3 CHECK (storage_security_level >= 1 AND storage_security_level <= 5),

    -- Surveillance
    cameras_active BOOLEAN DEFAULT true,
    recording_active BOOLEAN DEFAULT false,
    audio_surveillance BOOLEAN DEFAULT false,

    -- Black market
    market_inventory JSONB DEFAULT '{}',
    market_prices JSONB DEFAULT '{}',
    fence_reputation INTEGER DEFAULT 0 CHECK (fence_reputation >= -100 AND fence_reputation <= 100),

    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Covert Lab Equipment
CREATE TABLE interactive.lab_equipment (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() REFERENCES interactive.interactive_objects(id) ON DELETE CASCADE,
    equipment_type VARCHAR(30) DEFAULT 'terminal' CHECK (equipment_type IN ('terminal', 'containment', 'synthesis', 'analysis', 'storage')),

    -- Lab specific properties
    research_project VARCHAR(100),
    project_stage VARCHAR(20) DEFAULT 'planning' CHECK (project_stage IN ('planning', 'research', 'testing', 'production', 'complete')),

    -- Biohazard containment
    containment_level INTEGER DEFAULT 1 CHECK (containment_level >= 1 AND containment_level <= 4),
    breach_risk REAL DEFAULT 0.0 CHECK (breach_risk >= 0.0 AND breach_risk <= 1.0),

    -- AI terminal network
    terminal_access_level INTEGER DEFAULT 1 CHECK (terminal_access_level >= 1 AND terminal_access_level <= 5),
    connected_terminals UUID[] DEFAULT '{}',
    network_security JSONB DEFAULT '{}',

    -- Chemical synthesis
    reagent_inventory JSONB DEFAULT '{}',
    synthesis_recipes JSONB DEFAULT '{}',
    production_capacity INTEGER DEFAULT 0,

    -- Subject management
    active_subjects INTEGER DEFAULT 0,
    subject_status JSONB DEFAULT '{}',
    ethical_violations INTEGER DEFAULT 0,

    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- ===========================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- ===========================================

-- Update interactive_objects updated_at timestamp
CREATE OR REPLACE FUNCTION interactive.update_interactive_object_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_interactive_objects_updated_at
    BEFORE UPDATE ON interactive.interactive_objects
    FOR EACH ROW
    EXECUTE FUNCTION interactive.update_interactive_object_updated_at();

-- Update zone_states updated_at timestamp
CREATE OR REPLACE FUNCTION interactive.update_zone_state_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_zone_states_updated_at
    BEFORE UPDATE ON interactive.zone_states
    FOR EACH ROW
    EXECUTE FUNCTION interactive.update_zone_state_updated_at();

-- ===========================================
-- PERFORMANCE OPTIMIZATION FUNCTIONS
-- ===========================================

-- Function to get active objects in zone
CREATE OR REPLACE FUNCTION interactive.get_active_objects_in_zone(
    zone_uuid UUID,
    object_types VARCHAR(50)[] DEFAULT NULL
)
RETURNS TABLE (
    id UUID,
    object_type VARCHAR(50),
    position GEOMETRY(POINT, 4326),
    state VARCHAR(30),
    health INTEGER,
    interaction_type VARCHAR(30),
    access_level VARCHAR(20)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        io.id,
        io.object_type,
        io.position,
        io.state,
        io.health,
        io.interaction_type,
        io.access_level
    FROM interactive.interactive_objects io
    WHERE io.zone_id = zone_uuid
      AND io.is_active = true
      AND (object_types IS NULL OR io.object_type = ANY(object_types))
    ORDER BY io.object_type, io.created_at;
END;
$$ LANGUAGE plpgsql;

-- Function to get zone state summary
CREATE OR REPLACE FUNCTION interactive.get_zone_state_summary(zone_uuid UUID)
RETURNS TABLE (
    zone_type VARCHAR(30),
    security_level VARCHAR(20),
    operational_status VARCHAR(20),
    object_count BIGINT,
    active_object_count BIGINT,
    damaged_object_count BIGINT
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        zs.zone_type,
        zs.security_level,
        zs.operational_status,
        COUNT(io.id) as object_count,
        COUNT(io.id) FILTER (WHERE io.state = 'active') as active_object_count,
        COUNT(io.id) FILTER (WHERE io.state = 'damaged') as damaged_object_count
    FROM interactive.zone_states zs
    LEFT JOIN interactive.interactive_objects io ON zs.zone_id = io.zone_id AND io.is_active = true
    WHERE zs.zone_id = zone_uuid AND zs.is_active = true
    GROUP BY zs.zone_type, zs.security_level, zs.operational_status;
END;
$$ LANGUAGE plpgsql;

-- Function to update object event version
CREATE OR REPLACE FUNCTION interactive.increment_object_event_version()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE interactive.interactive_objects
    SET event_version = event_version + 1
    WHERE id = NEW.object_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_interactive_object_events_version_increment
    AFTER INSERT ON interactive.interactive_object_events
    FOR EACH ROW
    EXECUTE FUNCTION interactive.increment_object_event_version();

COMMENT ON SCHEMA interactive IS 'Enterprise-grade interactive object management system for Night City MMOFPS RPG - Issue: #2302';