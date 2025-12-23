-- Issue: #50
-- Import quest from: asia\tokyo\2078-2093\quest-043-bushido-revival.yaml
-- Generated: 2025-12-21T02:15:35.900793

BEGIN;

-- Quest: canon-quest-tokyo-bushido-revival
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-tokyo-bushido-revival', 'Токио 2078-2093 — Возрождение Бусидо',
        'Игрок проходит семь испытаний бусидо и вступает в самурайскую фракцию.', 'side', NULL, NULL, '{}'::jsonb,
        '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-tokyo-bushido-revival", "title": "Токио 2078-2093 — Возрождение Бусидо", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-11T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["tokyo", "bushido", "faction"], "topics": ["honor", "philosophical-trials"], "related_systems": ["narrative-service", "reputation-system", "faction-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/asia/tokyo/2078-2093/quest-043-bushido-revival.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "high"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Markdown не описывал испытания добродетелей и награды за присоединение к братству.", "goal": "Оформить квест в YAML с акцентом на испытания и титулы самурая нового века.", "essence": "Игрок проходит семь испытаний бусидо и вступает в самурайскую фракцию.", "key_points": ["Квест-id TOKYO-2093-043, тип faction, сложность hard, длительность 2–4 часа.", "Испытания проверяют каждую добродетель и создают фракционные задания.", "Награды — титул «Самурай Нового Века» и доступ к самурайской линии."]}, "content": {"sections": [{"id": "quest_overview", "title": "Описание", "body": "В неоновом храме игрок изучает кодекс бусидо, проводя ритуалы и клятвы верности.\\n", "mechanics_links": [], "assets": []}, {"id": "quest_flow", "title": "Этапы", "body": "1. Встретиться с лидером движения и изучить семь добродетелей.\\n2. Выполнить испытания на честность, мужество, доброту, вежливость, честь, верность и самоконтроль.\\n3. Пройти церемонию посвящения и получить самурайский титул.\\n", "mechanics_links": [], "assets": []}, {"id": "rewards", "title": "Награды и последствия", "body": "4 000 XP, титул «Самурай Нового Века», фракционные задания и +10% к параметру «Честь».\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 266, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-11", "author": "concept_director", "changes": "Конвертация квеста «Возрождение Бусидо» в YAML и завершение цикла Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest