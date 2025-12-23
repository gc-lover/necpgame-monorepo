-- Issue: #50
-- Import quest from: cis\moscow\2078-2093\quest-044-final-reset.yaml
-- Generated: 2025-12-21T02:15:36.328660

BEGIN;

-- Quest: canon-quest-moscow-final-reset
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-moscow-final-reset', 'Москва 2078-2093 — Финальный Перезапуск',
        'Массовый рейд атакует Ядро Симуляции и решает судьбу нового сезона.', 'side', NULL, NULL, '{}'::jsonb,
        '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-moscow-final-reset", "title": "Москва 2078-2093 — Финальный Перезапуск", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-12T06:40:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["moscow", "raid", "simulation-core"], "topics": ["final-reset", "collective-choice"], "related_systems": ["narrative-service", "raid-system", "world-state-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/cis/moscow/2078-2093/quest-044-final-reset.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "high"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Финальное событие лиги не описывало структуру рейда и голосование у Ядра.", "goal": "Определить этапы штурма, боссов и коллективный выбор исхода.", "essence": "Массовый рейд атакует Ядро Симуляции и решает судьбу нового сезона.", "key_points": ["Квест-id MOSCOW-2093-044, тип main, сложность extreme, рейд 10+, длительность 4+ часов.", "Этапы — раскрытие местоположения Ядра, штурм, сражения с ИИ и финальное голосование.", "Награды — 50 000 XP, Legacy-бонусы и титул «Переживший Перезапуск»."]}, "content": {"sections": [{"id": "assault", "title": "Штурм Ядра", "body": "Сотни игроков синхронно атакуют подземный комплекс, используя механики рейда.\\n", "mechanics_links": [], "assets": []}, {"id": "guardians", "title": "Защитники симуляции", "body": "Сложные ИИ-боссы с несколькими фазами и кооперативными задачами.\\n", "mechanics_links": [], "assets": []}, {"id": "final_choice", "title": "Финальный выбор", "body": "Голосование между перезапуском, сохранением или контролем определяет старт следующей лиги и награды.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 276, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Финальный Перезапуск» в YAML и интеграция в pipeline Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest