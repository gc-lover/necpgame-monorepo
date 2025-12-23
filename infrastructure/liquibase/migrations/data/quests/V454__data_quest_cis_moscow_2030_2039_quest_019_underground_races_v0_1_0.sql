-- Issue: #50
-- Import quest from: cis\moscow\2030-2039\quest-019-underground-races.yaml
-- Generated: 2025-12-21T02:15:36.234217

BEGIN;

-- Quest: canon-quest-moscow-underground-races
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-moscow-underground-races', 'Москва 2030-2039 — «Подземные гонки»',
        'Игрок платит взнос, модифицирует транспорт и соревнуется в трёх кругах по туннелям, избегая полиции и банд.',
        'side', NULL, NULL, '{}'::jsonb, '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-moscow-underground-races", "title": "Москва 2030-2039 — «Подземные гонки»", "document_type": "canon", "category": "timeline-author", "status": "draft", "version": "0.1.0", "last_updated": "2025-11-12T03:20:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "lore_analyst", "contact": "lore@necp.game"}], "tags": ["moscow", "racing", "metro"], "topics": ["events", "underground-culture"], "related_systems": ["narrative-service", "event-system", "economy-system"], "related_documents": [{"id": "canon-region-cis-moscow-2030-2093", "relation": "contextualizes"}], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/cis/moscow/2030-2039/quest-019-underground-races.md", "visibility": "internal", "audience": ["lore", "live-ops", "systems"], "risk_level": "medium"}, "review": {"chain": [{"role": "narrative_lead", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Черновик не объяснял механику гонки по туннелям и систему наград.", "goal": "Описать нелегальные гонки в метро с тюнингом транспорта и призами.", "essence": "Игрок платит взнос, модифицирует транспорт и соревнуется в трёх кругах по туннелям, избегая полиции и банд.", "key_points": ["Взнос 1 000 едди, тюнинг 0–5 000.", {"Препятствия": "обрушения, банды, рейды."}, "Призовые места дают валюту, технику и репутацию."]}, "content": {"sections": [{"id": "registration", "title": "Регистрация и подготовка", "body": "Игрок знакомится с организаторами, оплачивает 1 000 едди и настраивает байк. Возможен дополнительный тюнинг.\\n", "mechanics_links": [], "assets": []}, {"id": "race", "title": "Гонка в туннелях", "body": "Три круга по подземным веткам метро. События: обрушения, банды, патрули полиции. Мини-игра на реакцию и выбор маршрутов.\\n", "mechanics_links": [], "assets": []}, {"id": "rewards", "title": "Награды", "body": "- 1 место: 10 000 едди, легендарный байк.\\n- 2 место: 5 000 едди, редкие запчасти.\\n- 3 место: 2 000 едди, репутация гонщиков +2.\\n- Проигрыш: потеря взноса.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 273, "needs_task": false, "queue_reference": [], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-12", "author": "lore_team", "changes": "Конверсия квеста «Подземные гонки» в knowledge-entry."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest