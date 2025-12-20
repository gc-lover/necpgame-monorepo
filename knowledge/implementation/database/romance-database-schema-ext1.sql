);

CREATE INDEX idx_milestones_relationship ON relationship_milestones(relationship_id);
CREATE INDEX idx_milestones_type ON relationship_milestones(milestone_type);
CREATE INDEX idx_milestones_date ON relationship_milestones(achieved_at);

-- =====================================================
-- 7. CHEMISTRY CALCULATIONS (Совместимость)
-- =====================================================

CREATE TABLE chemistry_scores (
    chemistry_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id VARCHAR(50) NOT NULL,
    npc_id VARCHAR(50) NOT NULL REFERENCES npc_romance_profiles(npc_id),
    
    -- Overall chemistry
    total_chemistry INTEGER NOT NULL DEFAULT 50 CHECK (total_chemistry >= 0 AND total_chemistry <= 100),
    
    -- Components
    personality_match INTEGER DEFAULT 50 CHECK (personality_match >= 0 AND personality_match <= 100),
    shared_interests INTEGER DEFAULT 50 CHECK (shared_interests >= 0 AND shared_interests <= 100),
    physical_attraction INTEGER DEFAULT 50 CHECK (physical_attraction >= 0 AND physical_attraction <= 100),
    cultural_compatibility INTEGER DEFAULT 50 CHECK (cultural_compatibility >= 0 AND cultural_compatibility <= 100),
    
    -- Weights (configurable)
    personality_weight DECIMAL(3,2) DEFAULT 0.40,
    interests_weight DECIMAL(3,2) DEFAULT 0.30,
    attraction_weight DECIMAL(3,2) DEFAULT 0.20,
    cultural_weight DECIMAL(3,2) DEFAULT 0.10,
    
    -- Compatibility rating
    compatibility VARCHAR(20) CHECK (compatibility IN ('very_low', 'low', 'moderate', 'high', 'very_high')),
    
    -- Recalculated periodically
    last_calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(player_id, npc_id)
);

CREATE INDEX idx_chemistry_player ON chemistry_scores(player_id);
CREATE INDEX idx_chemistry_npc ON chemistry_scores(npc_id);
CREATE INDEX idx_chemistry_total ON chemistry_scores(total_chemistry);
CREATE INDEX idx_chemistry_compatibility ON chemistry_scores(compatibility);

-- =====================================================
-- 8. CULTURAL CONTEXTS (Культурные контексты)
-- =====================================================

CREATE TABLE cultural_contexts (
    culture_id VARCHAR(50) PRIMARY KEY,
    culture_name VARCHAR(100) NOT NULL,
    region VARCHAR(50),
    
    -- Romance characteristics
    romance_tempo VARCHAR(20) CHECK (romance_tempo IN ('very_slow', 'slow', 'moderate', 'fast', 'very_fast')),
    romance_style VARCHAR(50),  -- 'reserved', 'passionate', 'dramatic', etc.
    
    -- Public display of affection
    pda_allowed BOOLEAN DEFAULT TRUE,
    pda_level VARCHAR(20) CHECK (pda_level IN ('none', 'minimal', 'moderate', 'high', 'very_high')),
    pda_legal BOOLEAN DEFAULT TRUE,  -- Some countries: kissing publicly illegal
    
    -- Family importance
    family_importance VARCHAR(20) CHECK (family_importance IN ('low', 'moderate', 'high', 'critical')),
    family_approval_required BOOLEAN DEFAULT FALSE,
    family_approval_weight INTEGER DEFAULT 30,  -- How much family approval matters (%)
    
    -- Language
    love_phrase VARCHAR(100),  -- "I love you" in local language
    love_phrase_frequency VARCHAR(20) CHECK (love_phrase_frequency IN ('rare', 'occasional', 'common', 'frequent')),
    love_phrase_weight VARCHAR(20) CHECK (love_phrase_weight IN ('casual', 'moderate', 'serious', 'very_serious')),
    
    -- Marriage traditions
    marriage_importance VARCHAR(20),
    dowry_tradition BOOLEAN DEFAULT FALSE,
    engagement_ring_expected BOOLEAN DEFAULT FALSE,
    
    -- Modifiers
    relationship_dc_modifier INTEGER DEFAULT 0,  -- Modifier to DC based on culture
    cultural_bonuses JSONB,
    /*
    {
      "knowsLanguage": 5,
      "respectsTraditions": 3,
      "culturallyAware": 2
    }
    */
    
    -- Taboos
    cultural_taboos TEXT[],
    
    -- Notes
    cultural_notes TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert all cultures
INSERT INTO cultural_contexts (culture_id, culture_name, region, romance_tempo, romance_style, pda_allowed, pda_level, family_importance, love_phrase) VALUES
('japanese', 'Japanese', 'asia', 'very_slow', 'reserved', TRUE, 'minimal', 'high', '愛してる'),
('korean', 'Korean', 'asia', 'moderate', 'dramatic', TRUE, 'moderate', 'high', '사랑해'),
('chinese', 'Chinese', 'asia', 'moderate', 'traditional', TRUE, 'minimal', 'critical', '我爱你'),
('french', 'French', 'europe', 'fast', 'passionate', TRUE, 'very_high', 'moderate', 'Je t''aime'),
('british', 'British', 'europe', 'slow', 'reserved_humor', TRUE, 'moderate', 'moderate', 'I love you'),
('german', 'German', 'europe', 'moderate', 'direct', TRUE, 'high', 'moderate', 'Ich liebe dich'),
('italian', 'Italian', 'europe', 'very_fast', 'passionate', TRUE, 'very_high', 'critical', 'Ti amo'),
('brazilian', 'Brazilian', 'america', 'fast', 'physical', TRUE, 'very_high', 'high', 'Eu te amo'),
('argentinian', 'Argentinian', 'america', 'moderate', 'tango_passion', TRUE, 'high', 'high', 'Te amo'),
('mexican', 'Mexican', 'america', 'moderate', 'traditional', TRUE, 'moderate', 'critical', 'Te amo'),
('american', 'American', 'america', 'fast', 'direct', TRUE, 'high', 'moderate', 'I love you'),
('russian', 'Russian', 'cis', 'slow', 'soulful', TRUE, 'moderate', 'high', 'Я люблю тебя'),
('emirati', 'Emirati', 'middle-east', 'slow', 'traditional', FALSE, 'none', 'critical', 'أحبك'),
('israeli', 'Israeli', 'middle-east', 'fast', 'direct', TRUE, 'high', 'critical', 'אני אוהב אותך'),
('nigerian', 'Nigerian', 'africa', 'moderate', 'energetic', TRUE, 'moderate', 'critical', 'I love you'),
('kenyan', 'Kenyan', 'africa', 'slow', 'traditional', TRUE, 'moderate', 'critical', 'Nakupenda');

-- =====================================================
-- 9. EVENT TRIGGERS LOG
-- =====================================================

CREATE TABLE event_triggers_log (
    trigger_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id VARCHAR(50) NOT NULL REFERENCES romance_events(event_id),
    player_id VARCHAR(50) NOT NULL,
    npc_id VARCHAR(50) NOT NULL,
    
    -- Trigger context
    trigger_type VARCHAR(30) CHECK (trigger_type IN (
        'location_based', 'time_based', 'relationship_based', 
        'quest_based', 'random_encounter', 'npc_initiated', 'player_initiated'
    )),
    
    -- Conditions checked
    conditions_met JSONB,
    /*
    {
      "location": "bar",
      "relationship": 45,
      "chemistry": 70,
      "time": "evening",
      "allMet": true
    }
    */
    
    -- Result
    triggered BOOLEAN,
    reason_not_triggered TEXT,
    
    -- Timestamp
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_triggers_event ON event_triggers_log(event_id);
CREATE INDEX idx_triggers_player ON event_triggers_log(player_id);
CREATE INDEX idx_triggers_triggered ON event_triggers_log(triggered);

-- =====================================================
-- 10. PLAYER ROMANCE PREFERENCES
-- =====================================================

CREATE TABLE player_romance_preferences (
    player_id VARCHAR(50) PRIMARY KEY,
    
    -- Preferences
    preferred_gender VARCHAR(20),
    preferred_age_range INTEGER[],  -- [25, 35]
    preferred_cultures TEXT[],
    preferred_personalities JSONB,
    
    -- Style preferences
    romance_tempo_preference VARCHAR(20) CHECK (romance_tempo_preference IN ('slow', 'moderate', 'fast')),
    public_affection_comfort VARCHAR(20) DEFAULT 'moderate',
    conflict_tolerance VARCHAR(20) DEFAULT 'moderate',
    
    -- Settings
    polyamory_allowed BOOLEAN DEFAULT FALSE,
    max_concurrent_romances INTEGER DEFAULT 1,
    
    -- History
    total_romances INTEGER DEFAULT 0,
    successful_romances INTEGER DEFAULT 0,
    failed_romances INTEGER DEFAULT 0,
    
    -- Achievements
    romance_achievements TEXT[],
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 11. ROMANCE NOTIFICATIONS (Уведомления)
-- =====================================================

CREATE TABLE romance_notifications (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id VARCHAR(50) NOT NULL,
    npc_id VARCHAR(50) NOT NULL,
    
    -- Notification type
    type VARCHAR(30) CHECK (type IN (
        'phone_call', 'text_message', 'encounter_available', 
        'date_reminder', 'conflict_warning', 'milestone_reached'
    )),
    
    -- Content
    title VARCHAR(200),
    message TEXT,
    
    -- Action
    event_id VARCHAR(50) REFERENCES romance_events(event_id),
    action_required BOOLEAN DEFAULT FALSE,
    expires_at TIMESTAMP,
    
    -- Status
    read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notifications_player ON romance_notifications(player_id);
CREATE INDEX idx_notifications_read ON romance_notifications(read);
CREATE INDEX idx_notifications_type ON romance_notifications(type);

-- =====================================================
-- VIEWS для удобства
-- =====================================================

-- Active romances view
CREATE VIEW active_romances AS
SELECT 
    r.relationship_id,
    r.player_id,
    r.npc_id,
    n.name AS npc_name,
    r.relationship_score,
    r.relationship_stage,
    r.chemistry_score,
    r.relationship_health,
    r.breakup_risk,
    r.conflicts_unresolved,
    r.last_interaction_at,
    EXTRACT(DAY FROM (CURRENT_TIMESTAMP - r.last_interaction_at)) AS days_since_interaction
FROM relationships r
JOIN npc_romance_profiles n ON r.npc_id = n.npc_id
WHERE r.is_active = TRUE AND r.is_romantic = TRUE;

-- Available events view
CREATE VIEW available_events_view AS
SELECT 
    re.*,
    r.relationship_score,
    r.chemistry_score,
    r.relationship_stage
FROM romance_events re
CROSS JOIN relationships r
WHERE 
    r.relationship_score >= re.relationship_min 
    AND r.relationship_score <= re.relationship_max
    AND re.event_id NOT IN (
        SELECT unnest(r.completed_events)
    );

-- =====================================================
-- FUNCTIONS
-- =====================================================

-- Function: Update relationship score
CREATE OR REPLACE FUNCTION update_relationship_score(
    p_relationship_id UUID,
    p_change INTEGER
) RETURNS void AS $$
BEGIN
    UPDATE relationships
    SET 
        relationship_score = GREATEST(-100, LEAST(100, relationship_score + p_change)),
        updated_at = CURRENT_TIMESTAMP
    WHERE relationship_id = p_relationship_id;
    
    -- Update stage based on new score
    UPDATE relationships
    SET relationship_stage = 
        CASE
            WHEN relationship_score < 10 THEN 'stranger'
            WHEN relationship_score < 20 THEN 'acquaintance'
            WHEN relationship_score < 40 THEN 'friend'
            WHEN relationship_score < 60 THEN 'close_friend'
            WHEN relationship_score < 75 THEN 'romantic_interest'
            WHEN relationship_score < 90 THEN 'dating'
            WHEN relationship_score >= 90 THEN 'committed'
        END
    WHERE relationship_id = p_relationship_id;
END;
$$ LANGUAGE plpgsql;

-- Function: Calculate chemistry
CREATE OR REPLACE FUNCTION calculate_chemistry(
    p_player_id VARCHAR(50),
    p_npc_id VARCHAR(50)
) RETURNS INTEGER AS $$
DECLARE
    v_personality_match INTEGER;
    v_shared_interests INTEGER;
    v_physical_attraction INTEGER;
    v_cultural_compatibility INTEGER;
    v_total_chemistry INTEGER;
BEGIN
    SELECT 
        personality_match,
        shared_interests,
        physical_attraction,
        cultural_compatibility
    INTO
        v_personality_match,
        v_shared_interests,
        v_physical_attraction,
        v_cultural_compatibility
    FROM chemistry_scores
    WHERE player_id = p_player_id AND npc_id = p_npc_id;
    
    -- Calculate weighted chemistry
    v_total_chemistry := 
        (v_personality_match * 0.40) +
        (v_shared_interests * 0.30) +
        (v_physical_attraction * 0.20) +
        (v_cultural_compatibility * 0.10);
    
    -- Update chemistry score
    UPDATE chemistry_scores
    SET 
        total_chemistry = v_total_chemistry,
        last_calculated_at = CURRENT_TIMESTAMP
    WHERE player_id = p_player_id AND npc_id = p_npc_id;
    
    RETURN v_total_chemistry;
END;
$$ LANGUAGE plpgsql;

-- Function: Check event triggers
CREATE OR REPLACE FUNCTION check_event_triggers(
    p_event_id VARCHAR(50),
    p_player_id VARCHAR(50),
    p_npc_id VARCHAR(50),
    p_location VARCHAR(100),
    p_time VARCHAR(20)
) RETURNS BOOLEAN AS $$
DECLARE
    v_event RECORD;
    v_relationship RECORD;
    v_triggers JSONB;
BEGIN
    -- Get event and relationship
    SELECT * INTO v_event FROM romance_events WHERE event_id = p_event_id;
    SELECT * INTO v_relationship FROM relationships WHERE player_id = p_player_id AND npc_id = p_npc_id;
    
    v_triggers := v_event.triggers;
    
    -- Check relationship range
    IF v_relationship.relationship_score < v_event.relationship_min OR 
       v_relationship.relationship_score > v_event.relationship_max THEN
        RETURN FALSE;
    END IF;
    
    -- Check location
    IF v_triggers->>'locations' IS NOT NULL THEN
        IF NOT (v_triggers->'locations' @> to_jsonb(p_location)) THEN
            RETURN FALSE;
        END IF;
    END IF;
    
    -- Check time
    IF v_triggers->>'time' IS NOT NULL THEN
        IF NOT (v_triggers->'time' @> to_jsonb(p_time)) THEN
            RETURN FALSE;
        END IF;
    END IF;
    
    -- Check if already completed
    IF v_event.event_id = ANY(v_relationship.completed_events) THEN
        RETURN FALSE;
    END IF;
    
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- TRIGGERS (Database triggers, not event triggers!)
-- =====================================================

-- Auto-update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
