---
*
*api-readiness:** ready
**api-readiness-check-date:** 2025-11-06
---

-- =====================================================
-- ROMANCE EVENTS SYSTEM - DATABASE SCHEMA
-- PostgreSQL 15+
-- =====================================================

-- =====================================================
-- 1. ROMANCE EVENTS TABLE (1550+ событий)
-- =====================================================

CREATE TABLE romance_events
(
    event_id         VARCHAR(50) PRIMARY KEY,
    category         VARCHAR(20)  NOT NULL CHECK (category IN (
                                                               'meeting', 'friendship', 'flirting', 'dating',
                                                               'intimacy', 'conflict', 'reconciliation',
                                                               'commitment', 'crisis', 'regional'
        )),
    name             VARCHAR(200) NOT NULL,
    description      TEXT,

    -- Relationship range
    relationship_min INTEGER      NOT NULL DEFAULT 0 CHECK (relationship_min >= 0 AND relationship_min <= 100),
    relationship_max INTEGER      NOT NULL DEFAULT 100 CHECK (relationship_max >= 0 AND relationship_max <= 100),

    -- Regional context
    region           VARCHAR(50), -- asia, europe, america, cis, africa, middle-east, oceania
    country          VARCHAR(50),
    city             VARCHAR(50),

    -- Cultural tags
    cultural_tags    TEXT[],      -- ['traditional', 'romantic', 'public', etc.]

    -- Triggers (JSON)
    triggers         JSONB        NOT NULL,
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
    skill_check      JSONB,
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
    choices          JSONB        NOT NULL,
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
    outcomes         JSONB        NOT NULL,
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
    dialogue         JSONB,

    -- Cultural notes
    cultural_notes   TEXT,

    -- Metadata
    created_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    version          VARCHAR(10)           DEFAULT '1.0',

    CONSTRAINT check_relationship_range CHECK (relationship_min <= relationship_max)
);

-- Indexes for performance
CREATE INDEX idx_romance_events_category ON romance_events (category);
CREATE INDEX idx_romance_events_region ON romance_events (region);
CREATE INDEX idx_romance_events_relationship ON romance_events (relationship_min, relationship_max);
CREATE INDEX idx_romance_events_triggers ON romance_events USING GIN (triggers);
CREATE INDEX idx_romance_events_cultural_tags ON romance_events USING GIN (cultural_tags);

-- =====================================================
-- 2. NPC ROMANCE PROFILES
-- =====================================================

CREATE TABLE npc_romance_profiles
(
    npc_id                       VARCHAR(50) PRIMARY KEY,
    name                         VARCHAR(200) NOT NULL,
    age                          INTEGER,
    gender                       VARCHAR(20) CHECK (gender IN ('male', 'female', 'non_binary')),
    sexual_orientation           VARCHAR(20) DEFAULT 'bisexual' CHECK (sexual_orientation IN (
                                                                                              'heterosexual',
                                                                                              'homosexual', 'bisexual',
                                                                                              'pansexual', 'asexual'
        )),

    -- Location
    home_region                  VARCHAR(50),
    home_city                    VARCHAR(50),
    current_location             VARCHAR(50),

    -- Culture
    culture                      VARCHAR(50)  NOT NULL, -- japanese, french, brazilian, etc.
    primary_language             VARCHAR(50),
    speaks_languages             TEXT[],

    -- Personality (Big Five + Romance specific)
    personality                  JSONB        NOT NULL,
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
    interests                    TEXT[],
    hobbies                      TEXT[],

    -- Professional
    occupation                   VARCHAR(100),
    faction                      VARCHAR(50),

    -- Romance settings
    romance_available            BOOLEAN     DEFAULT TRUE,
    min_relationship_for_romance INTEGER     DEFAULT 40,
    family_approval_required     BOOLEAN     DEFAULT FALSE,
    marriage_oriented            BOOLEAN     DEFAULT FALSE,

    -- Companion perks
    companion_perk               JSONB,
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
    backstory                    TEXT,
    secrets                      TEXT[],

    -- Relationship history
    past_relationships           INTEGER     DEFAULT 0,
    has_ex                       BOOLEAN     DEFAULT FALSE,
    ex_drama                     BOOLEAN     DEFAULT FALSE,

    -- Metadata
    created_at                   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at                   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_npc_region ON npc_romance_profiles (home_region);
CREATE INDEX idx_npc_culture ON npc_romance_profiles (culture);
CREATE INDEX idx_npc_sexual_orientation ON npc_romance_profiles (sexual_orientation);
CREATE INDEX idx_npc_romance_available ON npc_romance_profiles (romance_available);
CREATE INDEX idx_npc_personality ON npc_romance_profiles USING GIN (personality);

-- =====================================================
-- 3. PLAYER-NPC RELATIONSHIPS
-- =====================================================

CREATE TABLE relationships
(
    relationship_id        UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    player_id              VARCHAR(50) NOT NULL,
    npc_id                 VARCHAR(50) NOT NULL REFERENCES npc_romance_profiles (npc_id),

    -- Scores
    relationship_score     INTEGER     NOT NULL DEFAULT 0 CHECK (relationship_score >= -100 AND relationship_score <= 100),
    chemistry_score        INTEGER     NOT NULL DEFAULT 0 CHECK (chemistry_score >= 0 AND chemistry_score <= 100),
    trust_score            INTEGER     NOT NULL DEFAULT 0 CHECK (trust_score >= 0 AND trust_score <= 100),
    physical_intimacy      INTEGER     NOT NULL DEFAULT 0 CHECK (physical_intimacy >= 0 AND physical_intimacy <= 100),
    emotional_intimacy     INTEGER     NOT NULL DEFAULT 0 CHECK (emotional_intimacy >= 0 AND emotional_intimacy <= 100),
    domestic_intimacy      INTEGER     NOT NULL DEFAULT 0 CHECK (domestic_intimacy >= 0 AND domestic_intimacy <= 100),

    -- Stage
    relationship_stage     VARCHAR(30)          DEFAULT 'stranger' CHECK (relationship_stage IN (
                                                                                                 'stranger',
                                                                                                 'acquaintance',
                                                                                                 'friend',
                                                                                                 'close_friend',
                                                                                                 'romantic_interest',
                                                                                                 'dating', 'committed',
                                                                                                 'engaged', 'married',
                                                                                                 'divorced', 'ex'
        )),

    -- Status
    is_active              BOOLEAN              DEFAULT TRUE,
    is_romantic            BOOLEAN              DEFAULT FALSE,
    is_sexual              BOOLEAN              DEFAULT FALSE,
    living_together        BOOLEAN              DEFAULT FALSE,
    engaged                BOOLEAN              DEFAULT FALSE,
    married                BOOLEAN              DEFAULT FALSE,

    -- Health
    relationship_health    INTEGER              DEFAULT 100 CHECK (relationship_health >= 0 AND relationship_health <= 100),
    conflicts_unresolved   INTEGER              DEFAULT 0,
    breakup_risk           DECIMAL(3, 2)        DEFAULT 0.00 CHECK (breakup_risk >= 0 AND breakup_risk <= 1),

    -- Events tracking
    completed_events       TEXT[],
    current_event          VARCHAR(50),
    next_event_suggestions TEXT[],

    -- Flags
    flags                  TEXT[], -- ['first_kiss_done', 'met_family', 'had_fight', etc.]

    -- Timestamps
    first_met_at           TIMESTAMP,
    became_friends_at      TIMESTAMP,
    first_kiss_at          TIMESTAMP,
    first_date_at          TIMESTAMP,
    moved_in_at            TIMESTAMP,
    engaged_at             TIMESTAMP,
    married_at             TIMESTAMP,
    broke_up_at            TIMESTAMP,
    last_interaction_at    TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,

    -- Metadata
    created_at             TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (player_id, npc_id)
);

CREATE INDEX idx_relationships_player ON relationships (player_id);
CREATE INDEX idx_relationships_npc ON relationships (npc_id);
CREATE INDEX idx_relationships_stage ON relationships (relationship_stage);
CREATE INDEX idx_relationships_score ON relationships (relationship_score);
CREATE INDEX idx_relationships_active ON relationships (is_active, is_romantic);
CREATE INDEX idx_relationships_flags ON relationships USING GIN (flags);

-- =====================================================
-- 4. EVENT HISTORY (История всех событий)
-- =====================================================

CREATE TABLE relationship_event_history
(
    history_id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    relationship_id      UUID        NOT NULL REFERENCES relationships (relationship_id) ON DELETE CASCADE,
    event_id             VARCHAR(50) NOT NULL REFERENCES romance_events (event_id),

    -- Context
    triggered_at         TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    location             VARCHAR(100),
    region               VARCHAR(50),

    -- Choices made
    choices_made         JSONB,
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
    skill_check_roll     INTEGER, -- d20 roll
    skill_check_total    INTEGER, -- total with modifiers
    skill_check_dc       INTEGER,
    skill_check_success  BOOLEAN,
    skill_check_critical BOOLEAN,

    -- Outcome
    outcome              VARCHAR(30) CHECK (outcome IN
                                            ('success', 'failure', 'critical_success', 'critical_failure', 'partial')),

    -- Relationship changes
    relationship_before  INTEGER,
    relationship_after   INTEGER,
    relationship_change  INTEGER,
    chemistry_change     INTEGER          DEFAULT 0,
    trust_change         INTEGER          DEFAULT 0,

    -- Flags set
    flags_set            TEXT[],

    -- Notes
    player_notes         TEXT,
    system_notes         TEXT,

    -- Metadata
    created_at           TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_event_history_relationship ON relationship_event_history (relationship_id);
CREATE INDEX idx_event_history_event ON relationship_event_history (event_id);
CREATE INDEX idx_event_history_date ON relationship_event_history (triggered_at);
CREATE INDEX idx_event_history_outcome ON relationship_event_history (outcome);

-- =====================================================
-- 5. CONFLICTS TABLE (Отслеживание конфликтов)
-- =====================================================

CREATE TABLE relationship_conflicts
(
    conflict_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    relationship_id     UUID        NOT NULL REFERENCES relationships (relationship_id) ON DELETE CASCADE,

    -- Conflict details
    conflict_type       VARCHAR(50) NOT NULL, -- jealousy, values, lie, etc.
    severity            INTEGER     NOT NULL CHECK (severity >= 1 AND severity <= 10),
    description         TEXT,

    -- Trigger event
    triggered_by_event  VARCHAR(50) REFERENCES romance_events (event_id),
    triggered_at        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    -- Status
    resolved            BOOLEAN          DEFAULT FALSE,
    resolved_at         TIMESTAMP,
    resolved_by_event   VARCHAR(50) REFERENCES romance_events (event_id),
    resolution_quality  VARCHAR(20) CHECK (resolution_quality IN ('poor', 'okay', 'good', 'excellent')),

    -- Impact
    relationship_damage INTEGER,
    trust_damage        INTEGER,

    -- Consequences
    consequences        TEXT[],

    -- Notes
    notes               TEXT,

    created_at          TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_conflicts_relationship ON relationship_conflicts (relationship_id);
CREATE INDEX idx_conflicts_resolved ON relationship_conflicts (resolved);
CREATE INDEX idx_conflicts_severity ON relationship_conflicts (severity);

-- =====================================================
-- 6. MILESTONES (Важные моменты)
-- =====================================================

CREATE TABLE relationship_milestones
(
    milestone_id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    relationship_id      UUID        NOT NULL REFERENCES relationships (relationship_id) ON DELETE CASCADE,

    -- Milestone details
    milestone_type       VARCHAR(50) NOT NULL, -- first_kiss, met_family, moved_in, engaged, married, etc.
    milestone_name       VARCHAR(200),
    description          TEXT,

    -- Event that triggered milestone
    event_id             VARCHAR(50) REFERENCES romance_events (event_id),
    location             VARCHAR(100),

    -- Achievement unlock
    achievement_unlocked VARCHAR(100),

    -- Timestamp
    achieved_at          TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    -- Memory
    memorable_quote      TEXT,                 -- "I've been waiting for this moment"
    screenshot_url       VARCHAR(500),         -- Optional player screenshot

    created_at           TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
