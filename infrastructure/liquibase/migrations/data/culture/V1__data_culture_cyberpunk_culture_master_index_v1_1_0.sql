-- Issue: #93
-- Import culture master index from: knowledge/canon/lore/culture/CYBERPUNK-CULTURE-MASTER-INDEX.yaml
-- Version: 1.1.0
-- Generated: 2025-12-21T02:16:13.514349

BEGIN;

-- Create culture_index table if not exists
CREATE TABLE IF NOT EXISTS knowledge.culture_index
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    culture_id VARCHAR
(
    100
) UNIQUE NOT NULL,
    title VARCHAR
(
    200
) NOT NULL,
    version VARCHAR
(
    20
) NOT NULL,
    content_data JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Insert/Update culture master index
INSERT INTO knowledge.culture_index (culture_id, title, version, content_data)
VALUES ('cyberpunk-culture-master-index',
        'Культура киберпанка: мастер-индекс (2020-2093)',
        '1.1.0',
        '{"metadata": {"id": "canon-lore-culture-master-index", "title": "Культура киберпанка: мастер-индекс (2020-2093)", "document_type": "canon", "category": "lore", "status": "draft", "version": "1.0.0", "last_updated": "2025-11-11T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "lore_archivist", "contact": "lore@necp.game"}], "tags": ["culture", "lifestyle", "worldbuilding"], "topics": ["music", "fashion", "art", "braindance"], "related_systems": ["world-service", "narrative-service", "live-events-service"], "related_documents": [{"id": "canon-lore-world-events-music", "relation": "references"}, {"id": "canon-lore-world-events-fashion", "relation": "references"}, {"id": "canon-lore-braindance-industry", "relation": "references"}], "source": "shared/docs/knowledge/canon/lore/culture/CYBERPUNK-CULTURE-MASTER-INDEX.yaml", "visibility": "internal", "audience": ["concept", "narrative", "marketing"], "risk_level": "medium"}, "review": {"chain": [{"role": "lore_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Требуется единый культурный индекс киберпанка 2020-2093 с описанием музыки, моды, искусства, индустрии braindance и субкультур.", "goal": "Предоставить командам исчерпывающий культурный контекст для сюжетов, активности и маркетинговых кампаний.", "essence": "Документ фиксирует эволюцию жанров, стилей, художественных направлений, braindance индустрии и ключевых субкультур Night City.", "key_points": ["Музыкальные эпохи от Corpse Reviver до Post-Relic с локациями и легендами.", "Эволюция моды, брендов и философий от Survival Chic до Post-Human Fashion.", "Каталог искусства, braindance жанров и субкультур, влияющих на социальный ландшафт."]}, "content": {"sections": [{"id": "overview", "title": "Обзор", "body": "Культура киберпанка соединяет высокие технологии и уличную жизнь. Документ описывает её развитие как культуру\nвыживания, бунта и эстетики хаоса.\n", "mechanics_links": [], "assets": []}, {"id": "music", "title": "Музыка 2020–2093", "body": "- **2020–2040 «Corpse Reviver»** — Industrial Metal, Darkwave, Noise. Группы: Corpse Reviver, Yorinobu''s Lament.\n- **2040–2060 «Neon Renaissance»** — Cyber-J-Pop, Entropism, Chrome Punk. Легенды: Kerry Eurodyne, Neon Samurai.\n- **2060–2077 «Digital Transcendence»** — Neuro-Music, braindance синестезия. События: Synaptic Symphony.\n- **2077–2093 «Post-Relic»** — Engram Blues и посмертные концерты Digital Ghost Orchestra.\n- Клубы: Totentanz, Afterlife, Lizzie''s Bar, Clouds.\n", "mechanics_links": [], "assets": []}, {"id": "fashion", "title": "Мода и бренды", "body": "- **Survival Chic (2020–2040)** — функциональность и армейский surplus.\n- **Corporate vs Street (2040–2060)** — разделение минимализма корпоратов и неоновой уличной эстетики.\n- **Implant Aesthetics (2060–2077)** — импланты как аксессуар, Chrome Fashion и Bio-Organic тенденции.\n- **Post-Human Fashion (2077–2093)** — гуманисты, трансгуманисты и эклектичные стили.\n- Бренды: Jinguji, Kitsch, Samurai Threads, Chrome Couture.\n", "mechanics_links": [], "assets": []}, {"id": "art", "title": "Искусство и перформанс", "body": "- Street Art: граффити, неоновые инсталляции, голографические муралы (Neon Prophet).\n- Digital Art: engram-портреты, AI-искусство, glitch-потоки (Pixel Reaper).\n- Corporate Art: пропагандистские постеры, лобби-инсталляции, портреты CEO.\n- Performance Art: симуляции киберпсихоза, шоу модификаций, NET diving-перформансы.\n", "mechanics_links": [], "assets": []}, {"id": "braindance", "title": "Индустрия braindance", "body": "- Определение braindance, происхождение и коммерциализация.\n- Жанры: Entertainment (Action, Romance, Tourism, Celebrity), Educational (Skill Training, Historical),\n  Illegal (Snuff, Hardcore Violence, Forbidden Experiences).\n- Студии: N54 News, Watson Whore Productions, Dreamers Syndicate. Доход индустрии оценивается в $500 млрд/год (2090).\n- Опасности: зависимость, психологический вред, нелегальное производство.\n", "mechanics_links": [], "assets": []}, {"id": "subcultures", "title": "Субкультуры киберпанка", "body": "- Chromers — поклонение кибер-модификациям.\n- Bio-Purists — движение против имплантов.\n- NET-Anarchists — борцы за свободную NET.\n- Relic Chasers — охотники за бессмертием через Relic.\n- Engram Cultists — религия цифрового бога.\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [{"title": "Эволюция музыкальных жанров", "link": "music-genres-evolution-2020-2093.yaml"}, {"title": "Тренды моды", "link": "fashion-trends-detailed.yaml"}, {"title": "Экономика индустрии braindance", "link": "braindance-industry-economics.yaml"}, {"title": "Уникальные культурные фракции", "link": "../../factions/unique/index.yaml"}], "decisions": []}, "implementation": {"needs_task": false, "github_issue": 101, "queue_reference": [], "blockers": []}, "history": [{"version": "1.0.0", "date": "2025-11-11", "author": "lore_team", "changes": "Конвертирован мастер-индекс культуры киберпанка в YAML."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb) ON CONFLICT (culture_id) DO
UPDATE SET
    title = EXCLUDED.title,
    version = EXCLUDED.version,
    content_data = EXCLUDED.content_data,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 culture index document