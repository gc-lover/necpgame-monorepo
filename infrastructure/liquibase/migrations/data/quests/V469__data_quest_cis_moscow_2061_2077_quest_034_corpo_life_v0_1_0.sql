-- Issue: #50
-- Import quest from: cis\moscow\2061-2077\quest-034-corpo-life.yaml
-- Generated: 2025-12-21T02:15:36.291000

BEGIN;

-- Quest: canon-quest-moscow-corpo-life
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-moscow-corpo-life', 'Москва 2061-2077 — Корпоративная Жизнь',
        'Игрок проходит от стажёра до топ-менеджера, лавируя между офисной политикой и выгодами.', 'side', NULL, NULL,
        '{}'::jsonb, '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-moscow-corpo-life", "title": "Москва 2061-2077 — Корпоративная Жизнь", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-12T05:20:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["moscow", "corporate", "career"], "topics": ["office-politics", "social-simulation"], "related_systems": ["narrative-service", "relationship-system", "economy-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/cis/moscow/2061-2077/quest-034-corpo-life.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "medium"}, "review": {"chain": [{"role": "concept_directор", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Корпоративная карьера описывалась линейно, без системной прогрессии и рисков.", "goal": "Построить карьерную симуляцию с выборами честного роста, интриг и предательства.", "essence": "Игрок проходит от стажёра до топ-менеджера, лавируя между офисной политикой и выгодами.", "key_points": ["Квест-id MOSCOW-2077-034, тип social, сложность medium, длительность 4+ часов.", "Этапы включают стажировку, офисные задачи, интриги, повышение и управление отделом.", "Награды — 2 500 XP, зарплатные выплаты 5 000–50 000 едди и доступ к корпоративным ресурсам."]}, "content": {"sections": [{"id": "career_path", "title": "Карьерный путь", "body": "Пять уровней роста от стажёра до директора с KPI-задачами и оценками эффективности.\\n", "mechanics_links": [], "assets": []}, {"id": "intrigue", "title": "Интриги и стратегия", "body": "Игрок выбирает между честной работой, интригами или предательством, влияя на скорость роста и наличие врагов.\\n", "mechanics_links": [], "assets": []}, {"id": "executive_play", "title": "Игры топ-менеджера", "body": "На вершине карьеры открываются корпоративные ресурсы, офис, NPC-команда и риски саботажа.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 275, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Корпоративная Жизнь» в YAML и интеграция в pipeline Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest