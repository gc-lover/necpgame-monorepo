-- Issue: #50
-- Import quest from: cis\moscow\2030-2039\quest-011-dome-charter.yaml
-- Generated: 2025-12-21T02:15:36.205130

BEGIN;

-- Quest: canon-quest-moscow-dome-charter
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-moscow-dome-charter', 'Москва 2030-2039 — Хартия Купола',
        'Игрок ведёт активистов к голосованию, балансируя между компромиссом, радикальным шантажом и предательством.',
        'side', NULL, NULL, '{}'::jsonb, '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-moscow-dome-charter", "title": "Москва 2030-2039 — Хартия Купола", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-12T03:15:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["moscow", "social-movement", "corporate"], "topics": ["rights-charter", "reputation"], "related_systems": ["narrative-service", "reputation-system", "event-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/cis/moscow/2030-2039/quest-011-dome-charter.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "high"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Квест не структурировал социальные ветки и не описывал системные последствия принятия Хартии.", "goal": "Оформить переговорный конфликт с альтернативами давления и предательства, влияющими на Архологию.", "essence": "Игрок ведёт активистов к голосованию, балансируя между компромиссом, радикальным шантажом и предательством.", "key_points": ["Квест-id MOSCOW-2039-011, тип main, сложность medium, длительность 2–4 часа.", "Содержит митинг, цифровой сбор подписей, переговоры, саботаж и финальное голосование.", "Награды — 1 800 XP, 3 000 едди, репутация серых слоёв или корпораций."]}, "content": {"sections": [{"id": "signatures", "title": "Сбор подписей", "body": "Мини-игра агитации требует убедить NPC и защитить терминалы от вмешательств корпорации.\\n", "mechanics_links": [], "assets": []}, {"id": "confrontation", "title": "Конфронтация с корпорацией", "body": "Переговоры с советом используют систему навыков, оппоненты запускают саботаж и кибератаки.\\n", "mechanics_links": [], "assets": []}, {"id": "outcomes", "title": "Последствия", "body": "Принятие Хартии улучшает доступ к услугам серых слоёв; провал ведёт к репрессиям и закрытию движения.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 273, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Хартия Купола» в YAML и интеграция в pipeline Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest