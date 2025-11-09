---
**api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
---

-- =====================================================
-- ROMANCE EVENTS SYSTEM - DATABASE SCHEMA
-- PostgreSQL 15+
-- =====================================================

-- =====================================================
-- 1. ROMANCE EVENTS TABLE (1550+ событий)
-- =====================================================

CREATE TABLE romance_events (
    event_id VARCHAR(50) PRIMARY KEY,
    category VARCHAR(20) NOT NULL CHECK (category IN (
        'meeting', 'friendship', 'flirting', 'dating', 
        'intimacy', 'conflict', 'reconciliation', 
        'commitment', 'crisis', 'regional'
    )),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    
    -- Relationship range
    relationship_min INTEGER NOT NULL DEFAULT 0 CHECK (relationship_min >= 0 AND relationship_min <= 100),
    relationship_max INTEGER NOT NULL DEFAULT 100 CHECK (relationship_max >= 0 AND relationship_max <= 100),
    
    -- Regional context
    region VARCHAR(50),  -- asia, europe, america, cis, africa, middle-east, oceania
    country VARCHAR(50),
    city VARCHAR(50),
    
    -- Cultural tags
    cultural_tags TEXT[],  -- ['traditional', 'romantic', 'public', etc.]
    
    -- Triggers (JSON)
    triggers JSONB NOT NULL,
    /*
    {
      "locations": ["bar", "club"],
      "time": ["evening", "night"],
      "season": "spring",
      "weather": "clear",
      "relationship": 40,
      "chemistry": 60,
      "randomChance": 0.15
    }
    */
    
    -- Skill check
    skill_check JSONB,
    /*
    {
      "type": "Charisma",
      "dc": 16,
      "skill": "Persuasion",
      "attribute": "COOL",
      "formula": "d20 + floor((COOL-10)/2) + Persuasion",
      "modifiers": {
        "class": {"Fixer": 2},
        "culture": {"knowsJapanese": 3}
      }
    }
    */
    
    -- Choices (JSON array)
    choices JSONB NOT NULL,
    /*
    [
      {
        "choiceId": "A1",
        "text": "Подойти и заговорить",
        "skillCheck": {...},
        "nextNode": 2,
        "cost": 0
      }
    ]
    */
    
    -- Outcomes (JSON)
    outcomes JSONB NOT NULL,
    /*
    {
      "success": {
        "relationship": 10,
        "chemistry": 5,
        "dialogue": "...",
        "nextEvents": ["RE-010", "RE-015"]
      },
      "failure": {...},
      "criticalSuccess": {...},
      "criticalFailure": {...}
    }
    */
    
    -- Dialogue
    dialogue JSONB,
    
    -- Cultural notes
    cultural_notes TEXT,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version VARCHAR(10) DEFAULT '1.0',
    
    CONSTRAINT check_relationship_range CHECK (relationship_min <= relationship_max)
);

-- Indexes for performance
CREATE INDEX idx_romance_events_category ON romance_events(category);
CREATE INDEX idx_romance_events_region ON romance_events(region);
CREATE INDEX idx_romance_events_relationship ON romance_events(relationship_min, relationship_max);
CREATE INDEX idx_romance_events_triggers ON romance_events USING GIN (triggers);
CREATE INDEX idx_romance_events_cultural_tags ON romance_events USING GIN (cultural_tags);

-- =====================================================
-- 2. NPC ROMANCE PROFILES
-- =====================================================

CREATE TABLE npc_romance_profiles (
    npc_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    age INTEGER,
    gender VARCHAR(20) CHECK (gender IN ('male', 'female', 'non_binary')),
    sexual_orientation VARCHAR(20) DEFAULT 'bisexual' CHECK (sexual_orientation IN (
        'heterosexual', 'homosexual', 'bisexual', 'pansexual', 'asexual'
    )),
    
    -- Location
    home_region VARCHAR(50),
    home_city VARCHAR(50),
    current_location VARCHAR(50),
    
    -- Culture
    culture VARCHAR(50) NOT NULL,  -- japanese, french, brazilian, etc.
    primary_language VARCHAR(50),
    speaks_languages TEXT[],
    
    -- Personality (Big Five + Romance specific)
    personality JSONB NOT NULL,
    /*
    {
      "openness": 75,
      "conscientiousness": 60,
      "extraversion": 80,
      "agreeableness": 70,
      "neuroticism": 40,
      "romanticism": 85,
      "jealousy": 50,
      "commitment": 75,
      "passionateness": 90,
      "traditionalism": 40,
      "familyOriented": 80
    }
    */
    
    -- Interests & Hobbies
    interests TEXT[],
    hobbies TEXT[],
    
    -- Professional
    occupation VARCHAR(100),
    faction VARCHAR(50),
    
    -- Romance settings
    romance_available BOOLEAN DEFAULT TRUE,
    min_relationship_for_romance INTEGER DEFAULT 40,
    family_approval_required BOOLEAN DEFAULT FALSE,
    marriage_oriented BOOLEAN DEFAULT FALSE,
    
    -- Companion perks
    companion_perk JSONB,
    /*
    {
      "name": "Ghost Protocol",
      "bonuses": {
        "Hacking": 3,
        "Stealth": 2
      }
    }
    */
    
    -- Backstory
    backstory TEXT,
    secrets TEXT[],
    
    -- Relationship history
    past_relationships INTEGER DEFAULT 0,
    has_ex BOOLEAN DEFAULT FALSE,
    ex_drama BOOLEAN DEFAULT FALSE,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_npc_region ON npc_romance_profiles(home_region);
CREATE INDEX idx_npc_culture ON npc_romance_profiles(culture);
CREATE INDEX idx_npc_sexual_orientation ON npc_romance_profiles(sexual_orientation);
CREATE INDEX idx_npc_romance_available ON npc_romance_profiles(romance_available);
CREATE INDEX idx_npc_personality ON npc_romance_profiles USING GIN (personality);

-- =====================================================
-- 3. PLAYER-NPC RELATIONSHIPS
-- =====================================================

CREATE TABLE relationships (
    relationship_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id VARCHAR(50) NOT NULL,
    npc_id VARCHAR(50) NOT NULL REFERENCES npc_romance_profiles(npc_id),
    
    -- Scores
    relationship_score INTEGER NOT NULL DEFAULT 0 CHECK (relationship_score >= -100 AND relationship_score <= 100),
    chemistry_score INTEGER NOT NULL DEFAULT 0 CHECK (chemistry_score >= 0 AND chemistry_score <= 100),
    trust_score INTEGER NOT NULL DEFAULT 0 CHECK (trust_score >= 0 AND trust_score <= 100),
    physical_intimacy INTEGER NOT NULL DEFAULT 0 CHECK (physical_intimacy >= 0 AND physical_intimacy <= 100),
    emotional_intimacy INTEGER NOT NULL DEFAULT 0 CHECK (emotional_intimacy >= 0 AND emotional_intimacy <= 100),
    domestic_intimacy INTEGER NOT NULL DEFAULT 0 CHECK (domestic_intimacy >= 0 AND domestic_intimacy <= 100),
    
    -- Stage
    relationship_stage VARCHAR(30) DEFAULT 'stranger' CHECK (relationship_stage IN (
        'stranger', 'acquaintance', 'friend', 'close_friend',
        'romantic_interest', 'dating', 'committed', 'engaged', 'married', 'divorced', 'ex'
    )),
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    is_romantic BOOLEAN DEFAULT FALSE,
    is_sexual BOOLEAN DEFAULT FALSE,
    living_together BOOLEAN DEFAULT FALSE,
    engaged BOOLEAN DEFAULT FALSE,
    married BOOLEAN DEFAULT FALSE,
    
    -- Health
    relationship_health INTEGER DEFAULT 100 CHECK (relationship_health >= 0 AND relationship_health <= 100),
    conflicts_unresolved INTEGER DEFAULT 0,
    breakup_risk DECIMAL(3,2) DEFAULT 0.00 CHECK (breakup_risk >= 0 AND breakup_risk <= 1),
    
    -- Events tracking
    completed_events TEXT[],
    current_event VARCHAR(50),
    next_event_suggestions TEXT[],
    
    -- Flags
    flags TEXT[],  -- ['first_kiss_done', 'met_family', 'had_fight', etc.]
    
    -- Timestamps
    first_met_at TIMESTAMP,
    became_friends_at TIMESTAMP,
    first_kiss_at TIMESTAMP,
    first_date_at TIMESTAMP,
    moved_in_at TIMESTAMP,
    engaged_at TIMESTAMP,
    married_at TIMESTAMP,
    broke_up_at TIMESTAMP,
    last_interaction_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(player_id, npc_id)
);

CREATE INDEX idx_relationships_player ON relationships(player_id);
CREATE INDEX idx_relationships_npc ON relationships(npc_id);
CREATE INDEX idx_relationships_stage ON relationships(relationship_stage);
CREATE INDEX idx_relationships_score ON relationships(relationship_score);
CREATE INDEX idx_relationships_active ON relationships(is_active, is_romantic);
CREATE INDEX idx_relationships_flags ON relationships USING GIN (flags);

-- =====================================================
-- 4. EVENT HISTORY (История всех событий)
-- =====================================================

CREATE TABLE relationship_event_history (
    history_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    relationship_id UUID NOT NULL REFERENCES relationships(relationship_id) ON DELETE CASCADE,
    event_id VARCHAR(50) NOT NULL REFERENCES romance_events(event_id),
    
    -- Context
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    location VARCHAR(100),
    region VARCHAR(50),
    
    -- Choices made
    choices_made JSONB,
    /*
    [
      {
        "choiceId": "A1",
        "text": "Подойти и заговорить",
        "selected": true
      }
    ]
    */
    
    -- Skill check result
    skill_check_roll INTEGER,  -- d20 roll
    skill_check_total INTEGER,  -- total with modifiers
    skill_check_dc INTEGER,
    skill_check_success BOOLEAN,
    skill_check_critical BOOLEAN,
    
    -- Outcome
    outcome VARCHAR(30) CHECK (outcome IN ('success', 'failure', 'critical_success', 'critical_failure', 'partial')),
    
    -- Relationship changes
    relationship_before INTEGER,
    relationship_after INTEGER,
    relationship_change INTEGER,
    chemistry_change INTEGER DEFAULT 0,
    trust_change INTEGER DEFAULT 0,
    
    -- Flags set
    flags_set TEXT[],
    
    -- Notes
    player_notes TEXT,
    system_notes TEXT,
    
    -- Metadata
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_event_history_relationship ON relationship_event_history(relationship_id);
CREATE INDEX idx_event_history_event ON relationship_event_history(event_id);
CREATE INDEX idx_event_history_date ON relationship_event_history(triggered_at);
CREATE INDEX idx_event_history_outcome ON relationship_event_history(outcome);

-- =====================================================
-- 5. CONFLICTS TABLE (Отслеживание конфликтов)
-- =====================================================

CREATE TABLE relationship_conflicts (
    conflict_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    relationship_id UUID NOT NULL REFERENCES relationships(relationship_id) ON DELETE CASCADE,
    
    -- Conflict details
    conflict_type VARCHAR(50) NOT NULL,  -- jealousy, values, lie, etc.
    severity INTEGER NOT NULL CHECK (severity >= 1 AND severity <= 10),
    description TEXT,
    
    -- Trigger event
    triggered_by_event VARCHAR(50) REFERENCES romance_events(event_id),
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Status
    resolved BOOLEAN DEFAULT FALSE,
    resolved_at TIMESTAMP,
    resolved_by_event VARCHAR(50) REFERENCES romance_events(event_id),
    resolution_quality VARCHAR(20) CHECK (resolution_quality IN ('poor', 'okay', 'good', 'excellent')),
    
    -- Impact
    relationship_damage INTEGER,
    trust_damage INTEGER,
    
    -- Consequences
    consequences TEXT[],
    
    -- Notes
    notes TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_conflicts_relationship ON relationship_conflicts(relationship_id);
CREATE INDEX idx_conflicts_resolved ON relationship_conflicts(resolved);
CREATE INDEX idx_conflicts_severity ON relationship_conflicts(severity);

-- =====================================================
-- 6. MILESTONES (Важные моменты)
-- =====================================================

CREATE TABLE relationship_milestones (
    milestone_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    relationship_id UUID NOT NULL REFERENCES relationships(relationship_id) ON DELETE CASCADE,
    
    -- Milestone details
    milestone_type VARCHAR(50) NOT NULL,  -- first_kiss, met_family, moved_in, engaged, married, etc.
    milestone_name VARCHAR(200),
    description TEXT,
    
    -- Event that triggered milestone
    event_id VARCHAR(50) REFERENCES romance_events(event_id),
    location VARCHAR(100),
    
    -- Achievement unlock
    achievement_unlocked VARCHAR(100),
    
    -- Timestamp
    achieved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Memory
    memorable_quote TEXT,  -- "I've been waiting for this moment"
    screenshot_url VARCHAR(500),  -- Optional player screenshot
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_relationships_updated_at BEFORE UPDATE ON relationships
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_npc_profiles_updated_at BEFORE UPDATE ON npc_romance_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Auto-calculate breakup risk
CREATE OR REPLACE FUNCTION calculate_breakup_risk()
RETURNS TRIGGER AS $$
DECLARE
    v_risk DECIMAL(3,2);
BEGIN
    -- Base risk from relationship health
    v_risk := (100 - NEW.relationship_health) / 100.0;
    
    -- Add risk from unresolved conflicts
    v_risk := v_risk + (NEW.conflicts_unresolved * 0.05);
    
    -- Add risk from low scores
    IF NEW.trust_score < 30 THEN
        v_risk := v_risk + 0.20;
    END IF;
    
    IF NEW.relationship_score < 40 THEN
        v_risk := v_risk + 0.15;
    END IF;
    
    -- Cap at 1.0
    NEW.breakup_risk := LEAST(1.0, v_risk);
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER calculate_breakup_risk_trigger BEFORE UPDATE ON relationships
    FOR EACH ROW EXECUTE FUNCTION calculate_breakup_risk();

-- =====================================================
-- SAMPLE DATA (примеры)
-- =====================================================

-- Sample NPC: Hanako "Ghost" Tanaka
INSERT INTO npc_romance_profiles (
    npc_id, name, age, gender, sexual_orientation,
    home_region, home_city, culture, primary_language,
    personality, interests, occupation, faction,
    romance_available, companion_perk
) VALUES (
    'hanako-tanaka',
    'Hanako "Ghost" Tanaka',
    28,
    'female',
    'bisexual',
    'asia',
    'tokyo',
    'japanese',
    'japanese',
    '{
        "openness": 85,
        "conscientiousness": 90,
        "extraversion": 40,
        "agreeableness": 60,
        "neuroticism": 55,
        "romanticism": 70,
        "jealousy": 45,
        "commitment": 85,
        "passionateness": 75,
        "traditionalism": 60,
        "familyOriented": 70
    }',
    ARRAY['hacking', 'netrunning', 'classical_music', 'virtual_art'],
    'Elite Netrunner',
    'independent',
    TRUE,
    '{
        "name": "Ghost Protocol II",
        "bonuses": {
            "Hacking": 5,
            "Stealth": 4
        }
    }'
);

-- =====================================================
-- INDEXES для производительности
-- =====================================================

-- Composite indexes для частых запросов
CREATE INDEX idx_relationships_player_stage ON relationships(player_id, relationship_stage);
CREATE INDEX idx_relationships_player_active ON relationships(player_id, is_active, is_romantic);
CREATE INDEX idx_events_category_region ON romance_events(category, region);

-- =====================================================
-- COMMENTS
-- =====================================================

COMMENT ON TABLE romance_events IS 'Библиотека всех 1550+ романтических событий';
COMMENT ON TABLE npc_romance_profiles IS 'Профили NPC доступных для романсов';
COMMENT ON TABLE relationships IS 'Активные отношения между игроками и NPC';
COMMENT ON TABLE relationship_event_history IS 'История всех романтических событий';
COMMENT ON TABLE relationship_conflicts IS 'Отслеживание конфликтов в отношениях';
COMMENT ON TABLE relationship_milestones IS 'Важные моменты в отношениях';
COMMENT ON TABLE chemistry_scores IS 'Расчёты совместимости';
COMMENT ON TABLE cultural_contexts IS 'Культурные контексты для адаптации';

