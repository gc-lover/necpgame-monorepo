-- Issue: #50
-- Import quest from: cis\moscow\2078-2093\quest-049-farewell-party.yaml
-- Generated: 2025-12-21T02:15:36.349731

BEGIN;

-- Quest: canon-quest-moscow-farewell-party
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-moscow-farewell-party', 'Москва 2078-2093 — Прощальная Вечеринка',
        'Игрок организует вечер, вспоминает приключения и готовится к новой лиге.', 'side', NULL, NULL, '{}'::jsonb,
        '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-moscow-farewell-party", "title": "Москва 2078-2093 — Прощальная Вечеринка", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-12T07:20:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["moscow", "social", "farewell"], "topics": ["league-ending", "emotional-content"], "related_systems": ["narrative-service", "social-system", "mood-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/cis/moscow/2078-2093/quest-049-farewell-party.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "low"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Прощальная вечеринка не описывала активности и эмоциональные сцены.", "goal": "Создать сценарий прощального праздника с приглашениями, диалогами и наградами.", "essence": "Игрок организует вечер, вспоминает приключения и готовится к новой лиге.", "key_points": ["Квест-id MOSCOW-2093-049, тип social, сложность easy, длительность 1–2 часа.", "Этапы — подготовка, приглашения, вечеринка с мини-играми, прощальные диалоги и финальный взгляд на город.", "Награды — 1 000 XP, ачивка «До встречи в следующей жизни» и эмоциональные сцены."]}, "content": {"sections": [{"id": "setup", "title": "Подготовка вечеринки", "body": "Игрок арендует площадку, планирует бюджет и оформляет пространство.\\n", "mechanics_links": [], "assets": []}, {"id": "celebration", "title": "Праздничные активности", "body": "Музыка, танцы, мини-игры и обмен тостами с NPC и игроками.\\n", "mechanics_links": [], "assets": []}, {"id": "farewell", "title": "Прощальные сцены", "body": "Персональные диалоги и финальный взгляд на город завершают лигу.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 276, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-12", "author": "concept_director", "changes": "Конвертация квеста «Прощальная Вечеринка» в YAML и интеграция в pipeline Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest