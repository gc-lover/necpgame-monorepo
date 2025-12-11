-- Issue: #50, #34
-- Import quest from: europe/amsterdam/2020-2029/quest-002-anne-frank-house.yaml
-- Version: 1.0.0
-- Generated: 2025-12-12T00:49:14.678332

BEGIN;

-- Quest: quest-amsterdam-2029-anne-frank-house
INSERT INTO gameplay.quest_definitions (quest_id, title, description, quest_type, level_min, level_max, requirements, objectives, rewards, branches, content_data, version, is_active) VALUES (
'quest-amsterdam-2029-anne-frank-house', 
'Амстердам 2020-2029 — Дом Анны Франк', 
'Игрок проходит через очереди, секретный вход и комнаты achterhuis, завершает квест рефлексией о памяти и ответственности.', 
'side', 
NULL, 
NULL, 
'{}'::jsonb, 
'[]'::jsonb, 
'{}'::jsonb, 
'[]'::jsonb, 
'{"metadata": {"id": "quest-amsterdam-2029-anne-frank-house", "title": "Амстердам 2020-2029 — Дом Анны Франк", "document_type": "canon", "category": "timeline-quest", "status": "draft", "version": "1.0.0", "last_updated": "2025-11-13T00:00:00+00:00", "concept_approved": false, "concept_reviewed_at": "", "owners": [{"role": "concept_director", "contact": "concept@necp.game"}], "tags": ["amsterdam", "holocaust", "remembrance"], "topics": ["historical_memory", "museum_experience"], "related_systems": ["narrative-service", "gameplay-service", "quest-service", "character-service", "economy-service", "education-service", "liveops-service"], "related_documents": [], "source": "shared/docs/knowledge/canon/lore/timeline-author/quests/europe/amsterdam/2020-2029/quest-002-anne-frank-house.md", "visibility": "internal", "audience": ["concept", "narrative", "liveops"], "risk_level": "medium"}, "review": {"chain": [{"role": "concept_director", "reviewer": "", "reviewed_at": "", "status": "pending"}], "next_actions": []}, "summary": {"problem": "Мемориальной линии Европы требовался сценарий, аккуратно освещающий историю Анны Франк и ужас Холокоста.", "goal": "Провести игрока по дому-музею, дать почувствовать тесноту убежища и важность дневника.", "essence": "Игрок проходит через очереди, секретный вход и комнаты achterhuis, завершает квест рефлексией о памяти и ответственности.", "key_points": ["Тайное убежище скрывалось за поворотным книжным шкафом.", "Дневник Анны стал голосом миллионов жертв.", "Реконструкция показывает, как семья жила два года в страхе."]}, "content": {"sections": [{"id": "entrance", "title": "Ожидание у музея", "body": "Игрок стоит в очереди, слушает аудиогид о семье Франк и подготовке убежища. Очередь подчёркивает популярность места памяти.\\n", "mechanics_links": [], "assets": []}, {"id": "hidden_annex", "title": "Тайное убежище", "body": "Через книжный шкаф игрок попадает в achterhuis, исследует комнаты и личные предметы восьми жителей.\\n", "mechanics_links": [], "assets": []}, {"id": "diary", "title": "Дневник Анны", "body": "В экспозиции представлены страницы дневника и аудиозаписи чтений. Игрок переживает надежды и страхи Анны.\\n", "mechanics_links": [], "assets": []}, {"id": "reflection", "title": "Обет памяти", "body": "Финальная зона призывает оставить обещание против антисемитизма. Игрок делится посланием и получает поддержку музея.\\n", "mechanics_links": [], "assets": []}, {"id": "rewards", "title": "Награды", "body": "- 5 000 XP, эмоция «Скорбь и память», расход 15 еддис.\\n- Титул «Мы помним Анну» и доступ к образовательным программам.\\n", "mechanics_links": [], "assets": []}]}, "appendix": {"glossary": [], "references": [], "decisions": []}, "implementation": {"github_issue": 744, "needs_task": false, "queue_reference": [], "blockers": []}, "history": [{"version": "1.0.0", "date": "2025-11-13", "author": "concept_team", "changes": "Квест «Дом Анны Франк» адаптирован под шаблон Concept Director."}], "validation": {"checksum": "", "schema_version": "1.0"}}'::jsonb, 
1, 
true
) ON CONFLICT (quest_id) DO UPDATE SET title = EXCLUDED.title, description = EXCLUDED.description, quest_type = EXCLUDED.quest_type, level_min = EXCLUDED.level_min, level_max = EXCLUDED.level_max, requirements = EXCLUDED.requirements, objectives = EXCLUDED.objectives, rewards = EXCLUDED.rewards, branches = EXCLUDED.branches, content_data = EXCLUDED.content_data, updated_at = CURRENT_TIMESTAMP;

COMMIT;

-- Total imported: 1 quest
