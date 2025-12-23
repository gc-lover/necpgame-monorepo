-- Issue: #50
-- Import quest from: asia\tokyo\2061-2077\quest-040-mount-takao.yaml
-- Generated: 2025-12-21T02:15:35.890472

BEGIN;

-- Quest: canon-quest-tokyo-mount-takao
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements,
                                        objectives, rewards, branches, content_data, version, is_active)
VALUES ('canon-quest-tokyo-mount-takao', 'Токио 2061-2077 — Гора Такао',
        'Игрок выбирает тропу, достигает храма Yakuoin и любуется видом на Токио и Фудзи.', 'side', NULL, NULL,
        '{}'::jsonb, '[]'::jsonb, '{}'::jsonb, '[]'::jsonb,
        '{"metadata": {"id": "canon-quest-tokyo-mount-takao", "title": "Токио 2061-2077 — Гора Такао", "document_type": "canon", "category": "timeline-author", "status": "in-review", "version": "0.1.0", "last_updated": "2025-11-11T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "narrative_lead", "contact": "narrative@necp.game"}], "tags": ["tokyo", "nature", "hiking"], "topics": ["exploration", "recovery"], "related_systems": ["narrative-service", "rest-system", "mood-system"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/_03-lore/timeline-author/quests/asia/tokyo/2061-2077/quest-040-mount-takao.md", "visibility": "internal", "audience": ["narrative", "content", "systems"], "risk_level": "low"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Markdown-описание подъёма на Такао не связывало маршруты, баффы и атмосферные детали.", "goal": "Описать квест в YAML, выделив выбор маршрута, посещение храма и бонусы выносливости.", "essence": "Игрок выбирает тропу, достигает храма Yakuoin и любуется видом на Токио и Фудзи.", "key_points": ["Квест-id TOKYO-2077-040, тип side, сложность easy, длительность 1–2 часа.", "Есть выбор фуникулёра или пешего подъёма, встречи с NPC и баффы выносливости.", "Награды — 800 XP, -1 000 едди и достижение «Покоритель Такао»."]}, "content": {"sections": [{"id": "quest_overview", "title": "Описание", "body": "Лесистые склоны Такао с красными ториями ведут к храму Yakuoin; тэнгу считаются хранителями горы.\\n", "mechanics_links": [], "assets": []}, {"id": "quest_flow", "title": "Этапы", "body": "1. Добраться до горы, выбрать подъём пешком или фуникулёром.\\n2. Пройти тропу, встретить NPC-хайкеров и собирать сувениры.\\n3. Посетить храм, получить благословение и насладиться панорамой.\\n", "mechanics_links": [], "assets": []}, {"id": "rewards", "title": "Награды и последствия", "body": "Игрок получает 800 XP, тратит 1 000 едди, получает +15% к выносливости на 24 часа и достижение «Покоритель Такао».\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 264, "needs_task": false, "queue_reference": ["shared/trackers/queues/concept/queued.yaml"], "blockers": []}, "history": [{"version": "0.1.0", "date": "2025-11-11", "author": "concept_director", "changes": "Конвертация квеста «Гора Такао» в YAML и завершение цикла Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb,
        1, true) ON CONFLICT (quest_id) DO
UPDATE
    SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest