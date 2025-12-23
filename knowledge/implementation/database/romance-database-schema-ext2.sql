RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER update_relationships_updated_at
    BEFORE UPDATE
    ON relationships
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_npc_profiles_updated_at
    BEFORE UPDATE
    ON npc_romance_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Auto-calculate breakup risk
CREATE
OR REPLACE FUNCTION calculate_breakup_risk()
RETURNS TRIGGER AS $$
DECLARE
v_risk DECIMAL(3,2);
BEGIN
    -- Base risk from relationship health
    v_risk
:= (100 - NEW.relationship_health) / 100.0;
    
    -- Add risk from unresolved conflicts
    v_risk
:= v_risk + (NEW.conflicts_unresolved * 0.05);
    
    -- Add risk from low scores
    IF
NEW.trust_score < 30 THEN
        v_risk := v_risk + 0.20;
END IF;
    
    IF
NEW.relationship_score < 40 THEN
        v_risk := v_risk + 0.15;
END IF;
    
    -- Cap at 1.0
    NEW.breakup_risk
:= LEAST(1.0, v_risk);

RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER calculate_breakup_risk_trigger
    BEFORE UPDATE
    ON relationships
    FOR EACH ROW EXECUTE FUNCTION calculate_breakup_risk();

-- =====================================================
-- SAMPLE DATA (примеры)
-- =====================================================

-- Sample NPC: Hanako "Ghost" Tanaka
INSERT INTO npc_romance_profiles (npc_id, name, age, gender, sexual_orientation,
                                  home_region, home_city, culture, primary_language,
                                  personality, interests, occupation, faction,
                                  romance_available, companion_perk)
VALUES ('hanako-tanaka',
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
        }');

-- =====================================================
-- INDEXES для производительности
-- =====================================================

-- Composite indexes для частых запросов
CREATE INDEX idx_relationships_player_stage ON relationships (player_id, relationship_stage);
CREATE INDEX idx_relationships_player_active ON relationships (player_id, is_active, is_romantic);
CREATE INDEX idx_events_category_region ON romance_events (category, region);

-- =====================================================
-- COMMENTS
-- =====================================================

COMMENT
ON TABLE romance_events IS 'Библиотека всех 1550+ романтических событий';
COMMENT
ON TABLE npc_romance_profiles IS 'Профили NPC доступных для романсов';
COMMENT
ON TABLE relationships IS 'Активные отношения между игроками и NPC';
COMMENT
ON TABLE relationship_event_history IS 'История всех романтических событий';
COMMENT
ON TABLE relationship_conflicts IS 'Отслеживание конфликтов в отношениях';
COMMENT
ON TABLE relationship_milestones IS 'Важные моменты в отношениях';
COMMENT
ON TABLE chemistry_scores IS 'Расчёты совместимости';
COMMENT
ON TABLE cultural_contexts IS 'Культурные контексты для адаптации';

