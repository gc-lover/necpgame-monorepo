-- Issue: #50
-- Import quest from: asia\tokyo\2061-2077\quest-039-neon-district.yaml
-- Generated: 2025-12-21T02:15:35.886984

BEGIN;

-- Quest: canon-quest-tokyo-neon-district
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-tokyo-neon-district', 'Токио 2061-2077 — Неоновый Район',
        'Игрок исследует неоновый район, выбирает развлечения и сталкивается с влиянием якудза.', 'side', NULL, NULL,
        '{}'::jsonb, '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-tokyo-neon-district", "title": "Токио 2061-2077 — Неоновый Район", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-11T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["tokyo", "kabukicho", "nightlife"], "topics": ["exploration", "crime-relations"], "related_systems": ["narrative-service", "reputation-system", "economy-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-039-neon-district.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "medium"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Markdown-описание Кабукитё не фиксировало ветвления, экономику и взаимодействие с якудза.", "goal": "Описать квест в YAML с вариантами активности, последствиями и культурными деталями.", "essence": "Игрок исследует неоновый район, выбирает развлечения и сталкивается с влиянием якудза.", "key_points": ["Квест-id TOKYO-2077-039, тип side, сложность medium, длительность 1–2 часа.", "Варианты активности включают хост-клуб, казино и информатора.", "Итоги меняют отношение якудза и открывают контакты."]}, "content": {"sections": [{"id": "quest_overview", "title": "Описание", "body": "Ночной Кабукитё сияет неоном; игрок балансирует между развлечениями и криминалом под взглядом мафии.\\n", "mechanics_links": [], "assets": []}, {"id": "quest_flow", "title": "Этапы", "body": "1. Войти в район и выбрать одну из активностей.\\n2. Провести сцену в хост-клубе, казино или с информатором, получая разные бонусы.\\n3. Встретиться с якудза и решить конфликт дипломатией, силой или побегом.\\n", "mechanics_links": [], "assets": []}, {"id": "rewards", "title": "Награды и последствия", "body": "Игрок получает 1 500 XP, тратит до 5 000 едди или выигрывает 10 000, открывает новые контакты и изменяет репутацию с якудза.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 264, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-11", "author": "concept_director", "changes": "Конвертация квеста «Неоновый Район» в YAML и завершение цикла Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest